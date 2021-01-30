package com.interswitch.smartmoveserver.audit;

import com.interswitch.smartmoveserver.model.PageView;
import com.interswitchng.audit.annotation.AuditDomain;
import com.interswitchng.audit.annotation.AuditDomains;
import com.interswitchng.audit.annotation.Audited;
import com.interswitchng.audit.model.Auditable;
import com.interswitchng.audit.model.AuditableAction;
import com.interswitchng.audit.service.AuditActorService;
import com.interswitchng.audit.service.AuditableActionStatus;
import com.interswitchng.audit.service.AuditableService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

@Slf4j
@Aspect
@Component
public class AuditService implements AuditableService {

    private final AuditActorService auditActorService;
    private final HashMap<Class, AuditableActionStatus> actionStatusMap;
    @Autowired
    private IswAuditLoggerImpl auditLogger;

    public AuditService(AuditActorService auditActorService) {
        this.auditActorService = auditActorService;
        actionStatusMap = new HashMap<>();
    }

    @Override
    @Around(value = "@annotation(com.interswitchng.audit.annotation.Audited) && execution(* com.interswitch*..*(..))")
    public Object onAudit(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.info("Inside of Around...");
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = signature.getMethod();
        Audited annotation = method.getAnnotation(Audited.class);
        AuditableAction action = annotation.auditableAction();

        Class<AuditableActionStatus> actionStatusClass = annotation.auditableActionClass();
        AuditableActionStatus actionStatus = actionStatusMap.get(actionStatusClass);
        if (actionStatus == null) {
            try {
                actionStatus = actionStatusClass.getDeclaredConstructor().newInstance();
                actionStatusMap.put(actionStatusClass, actionStatus);
            } catch (ReflectiveOperationException | IllegalArgumentException | SecurityException e) {
                log.error("Exception populating audit action status map", e);
                return proceedingJoinPoint.proceed();
            }
        }

        List<Auditable> auditables = extractAuditables(proceedingJoinPoint.getArgs());

        Object returnValue;
        try {
            returnValue = proceedingJoinPoint.proceed();
            if (actionStatus.actionSuccessful(returnValue, action)) {
                List<Auditable> returned = extractAuditables(returnValue);
                if (!returned.isEmpty()) {
                    auditables.clear();
                    auditables.addAll(returned);
                }
                log.info("Auditables before pushing===>" + auditables);
                pushAuditables(auditables, action);
            }
        } catch (Throwable throwable) {
            log.error("Exception executing audited method", throwable);
            if (actionStatus.actionSuccessful(throwable, action)) {
                pushAuditables(auditables, action);
            }
            throw throwable;
        }

        return returnValue;
    }

    private List<Auditable> extractAuditables(Object... arguments) {
        List<Auditable> auditables = new ArrayList<>();
        if (arguments != null && arguments.length > 0) {
            for (Object object : arguments) {
                if (object instanceof Auditable) {
                    auditables.add((Auditable) object);
                } else if (object instanceof Collection) {
                    for (Object item : (Collection) object) {
                        if (item instanceof Auditable) {
                            auditables.add((Auditable) item);
                        }
                    }
                } else if (object instanceof Map) {
                    for (Object item : ((Map) object).values()) {
                        if (item instanceof Auditable) {
                            auditables.add((Auditable) item);
                        }
                    }
                } else if (object instanceof PageView) {
                    for (Object item : ((PageView) object).getContent()) {
                        if (item instanceof Auditable) {
                            auditables.add((Auditable) item);
                        }
                    }
                }
            }
        }
        return auditables;
    }

    private void pushAuditables(List<Auditable> auditables, AuditableAction auditableAction) {
        for (Auditable auditable : auditables) {
            log.info("Wanna log audit trail===>" + auditable);
            auditLogger.log(auditable, auditActorService.getActor(), getDomainCodes(auditable), auditableAction);
            log.info("finished calling log audit trail===>" + auditable);
        }
    }

    private Set<String> getDomainCodes(Auditable auditable) {
        Set<String> domainCodes = new HashSet<>();

        BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(auditable);

        AuditDomains clazz = auditable.getClass().getDeclaredAnnotation(AuditDomains.class);
        if (clazz != null) {
            for (String f : clazz.fields()) {
                try {
                    domainCodes.add(String.valueOf(wrapper.getPropertyValue(f)));
                } catch (BeansException e) {
                    log.error(String.format("Audit domain property [%s] of auditable [%s] is not readable",
                            f, auditable.getClass().getSimpleName()), e);
                }
            }
        }

        AuditDomain field;
        for (Field f : auditable.getClass().getDeclaredFields()) {
            field = f.getDeclaredAnnotation(AuditDomain.class);
            if (field != null) {
                try {
                    domainCodes.add(String.valueOf(wrapper.getPropertyValue(f.getName())));
                } catch (BeansException e) {
                    log.error(String.format("Audit domain property [%s] of auditable [%s] is not readable",
                            f.getName(), auditable.getClass().getSimpleName()), e);
                }
            }
        }

        if (StringUtils.hasText(auditActorService.getActorDomainCode())) {
            domainCodes.add(auditActorService.getActorDomainCode());
        }

        return domainCodes;
    }
}

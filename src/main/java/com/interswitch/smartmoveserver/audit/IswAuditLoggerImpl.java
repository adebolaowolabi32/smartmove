package com.interswitch.smartmoveserver.audit;

import com.interswitch.smartmoveserver.model.AuditTrail;
import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.repository.AuditTrailRepository;
import com.interswitch.smartmoveserver.service.UserService;
import com.interswitch.smartmoveserver.util.DateUtil;
import com.interswitchng.audit.model.Auditable;
import com.interswitchng.audit.model.AuditableAction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Component
public class IswAuditLoggerImpl {

    @Autowired
    private AuditTrailRepository auditTrailRepository;

    @Autowired
    private UserService userService;

    private final ExecutorService executorService;

    public IswAuditLoggerImpl() {
        this.executorService = Executors.newFixedThreadPool(10);
    }

    public void log(Auditable auditable, String actor, Set<String> domainCodes, AuditableAction auditableAction) {

        executorService.submit(() -> {
            try {
                log.info("Inside log implementation,auditable value ===>"+auditable);
                AuditTrail audit = new AuditTrail();
                audit.setResource(auditable.getAuditableName().toLowerCase());
                audit.setResourceId((Long) auditable.getAuditableId());
                //audit.setAuditable(auditable);
                audit.setAction(auditableAction);
                audit.setActor(actor);
                User actorUser = userService.findByUsername(actor);
                User actorOwner = actorUser.getOwner()!=null && (actorUser.getRole()!= Enum.Role.ISW_ADMIN || actorUser.getRole()!= Enum.Role.OPERATOR )? actorUser.getOwner() : actorUser ;
                audit.setOwner(actorOwner);
                log.info("owner==>"+actorOwner);
                audit.setActionDate(Instant.now());
                audit.setActionTimeStamp(DateUtil.formatDate(LocalDateTime.now()));
                audit.setDescription(actor +" "+auditableAction.name().concat("d").toLowerCase() +" "+auditable.getAuditableName().toLowerCase()+" ");

                log.info("wanna log to audit trail inside log===>"+audit);

                if(audit.getResourceId()>0) {
                    auditTrailRepository.save(audit);
                }

            } catch (Throwable throwable) {
                log.error("Exception logging audit record", throwable);
            }

        });


    }
}

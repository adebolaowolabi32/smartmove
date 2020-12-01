package com.interswitch.smartmoveserver.audit;

import com.interswitch.smartmoveserver.model.AuditRecord;
import com.interswitch.smartmoveserver.repository.AuditRecordRepository;
import com.interswitchng.audit.logger.AuditLogger;
import com.interswitchng.audit.model.Auditable;
import com.interswitchng.audit.model.AuditableAction;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Component
public class IswAuditLoggerImpl {

    @Autowired
    private AuditRecordRepository auditRecordRepository;

    private final ExecutorService executorService;

    public IswAuditLoggerImpl() {
        this.executorService = Executors.newFixedThreadPool(10);
    }

    public void log(Auditable auditable, String actor, Set<String> domainCodes, AuditableAction auditableAction) {

        executorService.submit(() -> {
            try {
                log.info("Inside log implementation,auditable value ===>"+auditable);
                AuditRecord audit = new AuditRecord();
                audit.setResource(auditable.getAuditableName().toLowerCase());
                audit.setResourceId((Long) auditable.getAuditableId());
                //audit.setAuditable(auditable);
                audit.setAction(auditableAction);
                audit.setActor(actor);
                audit.setActionDate(Instant.now());
                audit.setDescription(actor +" "+auditableAction.name().concat("d").toLowerCase() +" "+auditable.getAuditableName().toLowerCase()+" ");

                log.info("wanna log to audit trail inside log===>"+audit);

                if(audit.getResourceId()>0) {
                    auditRecordRepository.save(audit);
                }

            } catch (Throwable throwable) {
                log.error("Exception logging audit record", throwable);
            }

        });


    }
}

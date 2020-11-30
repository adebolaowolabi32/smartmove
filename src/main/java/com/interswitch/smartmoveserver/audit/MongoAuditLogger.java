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
public class MongoAuditLogger implements AuditLogger {

    @Autowired
    private AuditRecordRepository auditRecordRepository;

    @Async
    @Override
    public void log(Auditable auditable, String actor, Set<String> domainCodes, AuditableAction auditableAction) {
            try {
                AuditRecord audit = new AuditRecord();
                audit.setResource(auditable.getAuditableName());
                audit.setResourceId((Long) auditable.getAuditableId());
                //audit.setAuditable(auditable);
                audit.setAction(auditableAction);
                audit.setActor(actor);
                audit.setActionDate(Instant.now());
                audit.setDescription(actor +" "+auditableAction.name().concat("ed").toUpperCase() +""+auditable.getAuditableName()+"");

               log.info("wanna log to audit trail===>"+audit);
                auditRecordRepository.save(audit);

            } catch (Throwable throwable) {
                log.error("Exception logging audit record", throwable);
            }

    }
}

package com.interswitch.smartmoveserver.audit;

import com.interswitch.smartmoveserver.model.PageView;
import com.interswitchng.audit.model.Auditable;
import com.interswitchng.audit.model.AuditableAction;
import com.interswitchng.audit.service.AuditableActionStatus;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class AuditableActionStatusImpl implements AuditableActionStatus {

    @Override
    public boolean actionSuccessful(Object returnedObject, AuditableAction auditableAction) {
        switch (auditableAction){
            case CREATE :
            case UPDATE:
                returnObjectSuccessful(returnedObject);
            default:
                return true;
        }
    }

    @Override
    public boolean actionSuccessful(Exception e, AuditableAction auditableAction) {
        return false;
    }

    private boolean returnObjectSuccessful(Object returnedObject) {
        boolean status = false;
        if (Objects.nonNull(returnedObject)) {
            Stream stream;
            if (returnedObject instanceof Auditable) {
                stream = Stream.of(returnedObject);
            } else if (returnedObject instanceof Collection) {
                stream = ((Collection) returnedObject).stream();
            } else if (returnedObject instanceof Map) {
                stream = ((Map) returnedObject).values().stream();
            } else if (returnedObject instanceof PageView) {
                stream = ((Page) returnedObject).getContent().stream();
            } else {
                stream = Stream.empty();
            }

            Object auditable = stream
                    .filter(Auditable.class::isInstance)
                    .findFirst()
                    .map(Auditable.class::cast)
                    .orElse(null);

            if (Objects.nonNull(auditable)) {
                Object id = ((Auditable) auditable).getAuditableId();
                if (Objects.nonNull(id)) {
                    if (id instanceof Long && ((Long) id) > 0L) {
                        status = true;
                    } else if (id instanceof Integer && ((Integer) id) > 0) {
                        status = true;
                    } else if (id instanceof String && ((String) id).length() > 0) {
                        status = true;
                    }
                }
            }
        }
        return status;
    }
}

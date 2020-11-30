package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.AuditRecord;
import org.springframework.data.repository.CrudRepository;

public interface AuditRecordRepository extends CrudRepository<AuditRecord, Long> {
}

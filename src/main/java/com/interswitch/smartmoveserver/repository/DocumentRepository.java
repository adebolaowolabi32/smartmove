package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Document;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DocumentRepository extends CrudRepository<Document, Integer> {
    Document findById(int integer);

}

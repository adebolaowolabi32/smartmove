package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Document;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DocumentRepository extends CrudRepository<Document, Long> {
    Document findById(long id);
    Document findByDocPath(String path);
    Document findByUrl(String url);

}

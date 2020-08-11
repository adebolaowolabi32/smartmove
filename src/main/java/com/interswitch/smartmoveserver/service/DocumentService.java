package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.Document;
import com.interswitch.smartmoveserver.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class DocumentService {
    public class DocumentServiceImpl{

        @Autowired
        private DocumentRepository documentRepository;

        public Document saveDocument(MultipartFile file) {
            try{
                Document doc = new Document(0,file.getOriginalFilename(),file.getContentType(),file.getBytes());
                return documentRepository.save(doc);
            } catch(IOException ex){
            }
            return null;
        }

        public Document saveDocument(File file) {
            return null;
        }

        public Document getDocumentById(int id) {
            return null;
        }
    }
}

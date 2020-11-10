package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.Document;
import com.interswitch.smartmoveserver.repository.DocumentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author earnest.suru
 */
@Slf4j
@Service
public class DocumentService {

    @Value("${app.docs.dir}")
    private String dir;

    @Value("${app.docs.base-uri}")
    private String baseUri;

    private Path uploadDir;

    private Path deletedDir;

    @Autowired
    private DocumentRepository documentRepository;

    @PostConstruct
    public void init() throws IOException {

         this.uploadDir = Paths.get(dir, "uploads");
         this.deletedDir = Paths.get(dir, "deleted");

        if (!Files.exists(Paths.get(dir))) {
            Files.createDirectories(Paths.get(dir));
        }

        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        if (!Files.exists(deletedDir)) {
            Files.createDirectories(deletedDir);
        }
    }


    public Document saveDocument(Document doc) {
        try {
            MultipartFile file = doc.getFile();
            if (file == null || file.getSize() <= 0)
                throw new ResponseStatusException(HttpStatus.PARTIAL_CONTENT, "file part not specified");
            String fileName = generateFileName(file);
            Path filePath = uploadDir.resolve(fileName);
            file.transferTo(filePath);
            doc.setUrl(this.getDocumentUri(fileName));
            doc.setDocPath(filePath.toAbsolutePath().toString());
            return documentRepository.save(doc);
        } catch (IOException ex) {
            log.error("IOException while trying to saving document", ex);
            return null;
        }

    }

    public Document getDocumentById(long id) {
        return documentRepository.findById(id);
    }

    public Document getDocumentByPath(String path) {
        return documentRepository.findByDocPath(path);
    }

    public Document getDocumentByPublicUrl(String url) {
        return documentRepository.findByUrl(url);
    }

    private void setFilePermissions(Path path) {
        try {

            Set<PosixFilePermission> filePermissions = new HashSet<>();
            filePermissions.add(PosixFilePermission.OWNER_READ);
            filePermissions.add(PosixFilePermission.OWNER_WRITE);
            filePermissions.add(PosixFilePermission.OWNER_EXECUTE);
            filePermissions.add(PosixFilePermission.GROUP_READ);
            filePermissions.add(PosixFilePermission.OTHERS_READ);
            Files.setPosixFilePermissions(path, filePermissions);

        } catch (IOException e) {
            log.error("Exception setting file permissions", e);
        }
    }

    private String getDocumentUri(String originalFileName) {
        return UriComponentsBuilder.fromUriString(this.baseUri)
                .pathSegment("uploads", originalFileName)
                .build()
                .toUriString();
    }

    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "_" + multiPart.getOriginalFilename().replace(" ", "_");
    }

}

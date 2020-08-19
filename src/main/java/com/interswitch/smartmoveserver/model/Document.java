package com.interswitch.smartmoveserver.model;

import com.interswitch.smartmoveserver.util.FilefileOpsUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.IOException;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name="documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String docType;

    @Lob
    private byte [] data;

    @Column(name = "base64Data")
    private String base64Data;

    private String url;

    public Document(MultipartFile file) throws IOException {
        this.data = file.getBytes();
        this.name = file.getOriginalFilename();
        this.docType=file.getContentType();
        this.base64Data = FilefileOpsUtil.convertFileToBase64String(FilefileOpsUtil.convertMultiPartToFile(file));
    }
}

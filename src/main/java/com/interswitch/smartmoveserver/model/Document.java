package com.interswitch.smartmoveserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;


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

    @Column(name = "path")
    private String docPath;

    private String url;

    private transient MultipartFile file;

    public Document(MultipartFile file) {
        this.file = file;
        this.name = file.getOriginalFilename();
        this.docType=file.getContentType();
    }
}

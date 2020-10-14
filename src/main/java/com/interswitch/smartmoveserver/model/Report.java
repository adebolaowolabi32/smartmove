package com.interswitch.smartmoveserver.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Locale;

/*
 * Created by adebola.owolabi on 4/21/2020
 */
@Data
public class Report {
    private Integer id;

    private String name;

    private String fileName;

    private String schemeName;

    private String participantName;

    private LocalDateTime createdDate;

    public static String generateFileName(String name, MimeType mimeType) {
        return String.format("%s.%s", name, "")
                .toLowerCase(Locale.ROOT)
                .replaceAll(" ", "-");
    }

    public enum MimeType {
        TEXT_CSV,
        APPLICATION_PDF
    }

    public enum Status {
        PENDING, SUCCESSFUL, FAILED
    }
}

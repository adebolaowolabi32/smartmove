package com.interswitch.smartmoveserver.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

public class FileUtil {

    public static String convertFileToBase64String(File file) throws IOException {

        FileInputStream fileInputStreamReader = new FileInputStream(file);

        byte[] bytes = new byte[(int) file.length()];

        fileInputStreamReader.read(bytes);

        fileInputStreamReader.close();

        return Base64.getEncoder().encodeToString((bytes));
    }

    public static File convertBas64StringToFile(String base64String, String fileName) throws IOException {
        byte[] decodeByte = Base64.getDecoder().decode(base64String);

        File file = new File(fileName);

        FileOutputStream stream = new FileOutputStream(file);

        stream.write(decodeByte);

        stream.close();

        return file;
    }

    public static File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;

    }
}

package com.interswitch.smartmoveserver.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

public class FileOpsUtil {

    public static String convertFileToBase64String(File file) throws IOException {
        byte[] bytes = new byte[(int) file.length()];
        try (FileInputStream fileInputStreamReader = new FileInputStream(file)) {
            fileInputStreamReader.read(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Base64.getEncoder().encodeToString((bytes));
    }

    public static File convertBas64StringToFile(String base64String, String fileName) throws IOException {
        byte[] decodeByte = Base64.getDecoder().decode(base64String);
        File file = new File(fileName);

        try (FileOutputStream stream = new FileOutputStream(file)) {
            stream.write(decodeByte);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return file;
    }

    public static File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convFile;
    }

}

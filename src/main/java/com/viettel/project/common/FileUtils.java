package com.viettel.project.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class FileUtils {
    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    public static String genNewFileNameFromFile(MultipartFile file) {
        String oldName = file.getOriginalFilename();
        return  System.currentTimeMillis() + "-" + oldName;
    }

    public static void saveFileToFolder(MultipartFile input, File destination) {
        if (destination.isDirectory() && !destination.exists()) {
            logger.info("Create folderl: " + destination.getName() + ", result: " + destination.mkdir());
        }

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(destination);
            fileOutputStream.write(input.getBytes());
        } catch (FileNotFoundException e) {
            logger.error("Destination / input file does not exist");
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("Can not read bytes from file input");
            e.printStackTrace();
        }

    }

    public static void deleteFile(File avatarFile) {
        if (Objects.isNull(avatarFile) || !avatarFile.exists()) return;
        logger.info("Delete file: " + avatarFile.getName() + ", result: " + avatarFile.delete());
    }
}

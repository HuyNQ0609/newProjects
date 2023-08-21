package com.vsafe.admin.server.helpers.utils.file;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

public class FileUtils {

    // Not include dot
    public static String getExtension(MultipartFile file) {
        return StringUtils.getFilenameExtension(file.getOriginalFilename());
    }
}

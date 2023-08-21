package com.vsafe.admin.server.helpers.utils;

import com.vsafe.admin.server.helpers.constants.SystemConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class Utils {
    public static <E> E nvl(E expr1, E expr2) {
        return (null != expr1) ? expr1 : expr2;
    }

    public static boolean isValidInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isValidLong(String input) {
        try {
            Long.parseLong(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Boolean validFileType(List<MultipartFile> files, List<String> typeFile) {
        List<String> extensions = new ArrayList<>(Arrays.asList("zip", "rar"));
        if (CollectionUtils.isEmpty(typeFile)) {
            typeFile = Arrays.asList(SystemConstants.CONTENT_TYPE_FILE.DOC, SystemConstants.CONTENT_TYPE_FILE.JPEG, SystemConstants.CONTENT_TYPE_FILE.DOCX,
                    SystemConstants.CONTENT_TYPE_FILE.JPG, SystemConstants.CONTENT_TYPE_FILE.PDF, SystemConstants.CONTENT_TYPE_FILE.PNG,
                    SystemConstants.CONTENT_TYPE_FILE.XLS, SystemConstants.CONTENT_TYPE_FILE.XLSX, SystemConstants.CONTENT_TYPE_FILE.IMAGE_GIF,
                    SystemConstants.CONTENT_TYPE_FILE.ZIP_1, SystemConstants.CONTENT_TYPE_FILE.ZIP_2, SystemConstants.CONTENT_TYPE_FILE.RAR);
        }
        List<String> contentTypes = files.stream().map(MultipartFile::getContentType).collect(Collectors.toList());
        log.info("contentType---------" + contentTypes);
        boolean check = typeFile.containsAll(contentTypes);
        if (!check && files.size() > 0) {
            List<String> nameFile = files.stream().map(MultipartFile::getOriginalFilename).collect(Collectors.toList());
            List<String> extension = new ArrayList<>();
            nameFile.stream().forEach(item -> {
                extension.add(FilenameUtils.getExtension(item));
            });
            boolean containsAll = extensions.containsAll(extension);
            return containsAll;
        }
        return check;
    }

    public static String getCurrentTimeFormatYYYYMMDDHHMMSS() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
    }

    public static String genSecureCodeGeneral(String secretKey, String... fields) {
        if (secretKey == null || secretKey.isEmpty() || fields == null || fields.length <= 0) {
            return "";
        }
        StringBuilder combined = new StringBuilder();
        for (String f : fields) {
            combined.append(f);
            combined.append("|");
        }
        combined.append(secretKey);
        log.info("Raw string: " + combined.toString());
        String shaRaw = sha256(combined.toString());
        log.info("Token send : " + shaRaw);
        return shaRaw;
    }

    public static String sha256(String base) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();

            for (byte aHash : hash) {
                String hex = Integer.toHexString(0xff & aHash);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            System.out.println(hexString.toString());
            return hexString.toString();
        } catch (Exception ex) {
            return "";
        }
    }
}

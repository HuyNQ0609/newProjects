package com.vsafe.admin.server.helpers.utils;

import org.apache.tomcat.util.codec.binary.Base64;

public class Base64ImageUtils {
    private final static String HAIR = "data:image/";
    private final static String NECK = ";base64,";
    private final static String PNG = "png";
    private final static String JPEG = "jpeg";
    private final static String JPG = "jpg";
    private final static String GIF = "gif";

    public static byte[] transformToHeadlessKnight(String image) throws RuntimeException {
        return Base64.decodeBase64(cutHead(image));
    }

    // Bỏ chuỗi đầu của base64: data:image/jpeg;base64, ....
    private static String cutHead(String image) throws RuntimeException {
        return image.replace(formHead(HAIR, cutWhom(image), NECK), "");
    }

    private static String formHead(String hair, String face, String neck) {
        return String.join("", hair, face, neck);
    }

    private static String cutWhom(String image) throws RuntimeException {
        if (image.contains(PNG)) {
            return PNG;
        } else if (image.contains(JPEG)) {
            return JPEG;
        } else if (image.contains(JPG)) {
            return JPG;
        } else if (image.contains(GIF)) {
            return GIF;
        } else {
            throw new RuntimeException("Lỗi");
        }
    }
}

package com.vsafe.admin.server.helpers.utils.mapper;

import com.google.gson.Gson;

public class GsonParserUtils {
    public static String parseObjectToString(Object object) {
        return new Gson().toJson(object);
    }

    public static <T> T parseStringToObject(String json, Class<T> classObject) {
        try {
            return new Gson().fromJson(json, classObject);
        } catch (Exception e) {
            return null;
        }
    }

    public static String toJson(Object classObject) {
        try {
            return new Gson().toJson(classObject);
        } catch (Exception e) {
            return null;
        }
    }
}

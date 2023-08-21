package com.vsafe.admin.server.helpers.utils.datastructure;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * CloneDeep
 */
public class Cloner {

    public static class ClonerException extends Exception {
        public ClonerException(Exception e) {
            super(e);
        }
    }

    public static <T extends Object> T clone(T orig) throws ClonerException {
        T copy = null;
        try {
            if (isKeySet(orig)) {
                // HashMap.KeySet needs special processing, because the
                // ObjectMapper class does not deal with it properly.
                copy = (T) cloneKeySet((Set) orig);
            } else {
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(orig);
                copy = (T) mapper.readValue(json, orig.getClass());
            }
        } catch (IOException e) {
            throw new ClonerException(e);
        }

        return copy;
    }

    private static <T extends Object> boolean isKeySet(T orig) {
        String className = orig.getClass().getName();
        return (className.matches(".*?HashMap.KeySet$"));
    }

    public static <T extends Object> Set<?> cloneKeySet(Set<?> orig) {

        Class clazz = orig.getClass();
        Set regularSet = new HashSet<>();
        regularSet.addAll(orig);


        return regularSet;
    }

}

package com.vsafe.admin.server.helpers.utils.datastructure.tree;

import java.util.*;

public class Tree {
    // Convert list to tree set
    public static <T extends Node<T>> List<T> createTreeSet(List<T> nodes) {
        List<T> treeSet = new ArrayList<>();
        Map<String, T> mapTmp = new HashMap<>();
        for (T current : nodes) {
            mapTmp.put(current.getId().toString(), current);
        }

        for (T current : nodes) {
            String parentId = current.getParentId();

            if (parentId != null) {
                T parent = mapTmp.get(parentId + "");
                if (parent != null) {
                    current.setParent(parent);
                    parent.addChild(current);
                    mapTmp.put(parentId + "", parent);
                    mapTmp.put(current.getId() + "", current);
                }
            }
        }

        for (T node : mapTmp.values()) {
            if (node.getParent() == null) {
                treeSet.add(node);
            }
        }
        treeSet.sort(Comparator.comparing(T::getId));
        return treeSet;
    }

    public static <T extends Node<T>> List<T> flatten(T node, List<T> flatList) {

        if (node != null) {
            flatList.add(node);
        }

        List<T> children = node.getChildren();
        for (T child : children) {
            if (child.getChildren() != null) {
                flatten(child, flatList);
            }
        }
        return flatList;
    }

    public static String createTreePath(String... path) {
        return String.join(",","",String.join(",",path), "");
    }
}

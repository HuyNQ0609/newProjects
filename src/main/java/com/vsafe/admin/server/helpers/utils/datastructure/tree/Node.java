package com.vsafe.admin.server.helpers.utils.datastructure.tree;

import java.util.ArrayList;
import java.util.List;

public interface Node<T> {
    void setId(String id);

    String getId();

    void setParentId(String parentId);

    String getParentId();

    void setChildren(List<T> children);

    List<T> getChildren();

    void setParent(T parent);

    T getParent();

    default void addChild(T child) {
        if (this.getChildren() == null) this.setChildren(new ArrayList<>());
        if (!this.getChildren().contains(child) && child != null) {
            this.getChildren().add(child);
        }
    }
}

package com.jinelei.iotgenius.auth.enumeration;

@SuppressWarnings("unused")
public enum TreeBuildMode {
    /**
     * 所有子节点
     */
    ALL,
    /**
     * 所有父节点
     */
    PARENT,
    /**
     * 当前节点和所有父节点
     */
    PARENT_AND_CURRENT,
    /**
     * 所有子节点
     */
    CHILD,
    /**
     * 所有子节点和当前节点
     */
    CHILD_AND_CURRENT,
}

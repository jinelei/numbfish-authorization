package com.jinelei.numbfish.auth.enumeration;

@SuppressWarnings("unused")
public enum AuthorizeType {
    USER(0, "用户模式"),
    CLIENT(1, "客户端模式"),
    UNKNOWN(-1, "未知模式");
    private final int value;
    private final String name;

    AuthorizeType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}

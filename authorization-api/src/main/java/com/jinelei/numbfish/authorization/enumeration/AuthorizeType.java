package com.jinelei.numbfish.authorization.enumeration;

@SuppressWarnings("unused")
public enum AuthorizeType {
    UNKNOWN(0, "未知模式"),
    USER(1, "用户模式"),
    CLIENT(2, "客户端模式"),;
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

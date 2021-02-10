package com.android_group10.needy;

public enum ServiceType {
    SHOPPING(1),
    TRANSPORTATION(2),
    CLEANING(3),
    OTHER(4);

    private final int value;

    ServiceType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

package com.axreng.backend.enums;

public enum EnumSearchStatus {

    ACTIVE("active"),
    DONE("done");

    private String status;

    EnumSearchStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}

package com.example.enums;

public enum APIResponseStatus {
    SUCCESS("Success"), FAILURE("Failure");

    APIResponseStatus(String displayName){
        this.displayName=displayName;
    }

    private String displayName;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}

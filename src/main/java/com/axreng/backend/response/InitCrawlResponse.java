package com.axreng.backend.response;

public class InitCrawlResponse {

    private String id;

    public InitCrawlResponse(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

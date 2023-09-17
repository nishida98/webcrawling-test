package com.axreng.backend.request;

public class InitCrawlRequest {

    private String keyword;

    public InitCrawlRequest(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}

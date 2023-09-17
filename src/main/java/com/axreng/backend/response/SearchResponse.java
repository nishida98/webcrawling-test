package com.axreng.backend.response;

import java.util.TreeSet;

public class SearchResponse {

    private String id;
    private String status;
    private TreeSet<String> urls;

    public SearchResponse() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public TreeSet<String> getUrls() {
        return urls;
    }

    public void setUrls(TreeSet<String> urls) {
        this.urls = urls;
    }

}

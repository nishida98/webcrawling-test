package com.axreng.backend.exception;

public class NotFoundCrawlId extends Exception{

    public NotFoundCrawlId(String id) {
        super("crawl not found: "+id);
    }

}

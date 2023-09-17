package com.axreng.backend.service;

import com.axreng.backend.exception.InvalidSearchKeywordException;
import com.axreng.backend.exception.NotFoundCrawlId;
import com.axreng.backend.response.SearchResponse;

public interface CrawlService {

    String initCrawl(String keyword) throws InvalidSearchKeywordException;

    SearchResponse search(String id) throws NotFoundCrawlId;

}

package com.axreng.backend;

import com.axreng.backend.exception.InvalidSearchKeywordException;
import com.axreng.backend.exception.NotFoundCrawlId;
import com.axreng.backend.request.InitCrawlRequest;
import com.axreng.backend.response.ExceptionResponse;
import com.axreng.backend.response.InitCrawlResponse;
import com.axreng.backend.response.SearchResponse;
import com.axreng.backend.service.CrawlService;
import com.axreng.backend.service.CrawlServiceImpl;
import com.google.gson.Gson;

import static spark.Spark.*;

public class Main {

    public static void main(String[] args) {

        final CrawlService crawlService = new CrawlServiceImpl();

        get("/crawl/:id", (req, res) -> {
            res.type("application/json");
            SearchResponse response = crawlService.search(req.params(":id"));
            return new Gson().toJson(response);
        });

        post("/crawl", (req, res) -> {
            res.type("application/json");
            InitCrawlRequest request = new Gson().fromJson(req.body(), InitCrawlRequest.class);
            String id = crawlService.initCrawl(request.getKeyword());
            return new Gson().toJson(new InitCrawlResponse(id));
        });

        exception(InvalidSearchKeywordException.class, (exception, req,res)->{

            res.type("application/json");
            res.status(400);
            res.body(new Gson().toJson(new ExceptionResponse(400, exception.getMessage())));

        });

        exception(NotFoundCrawlId.class, (exception, req, res)->{

            res.type("application/json");
            res.status(404);
            res.body(new Gson().toJson(new ExceptionResponse(404, exception.getMessage())));

        });

    }

}

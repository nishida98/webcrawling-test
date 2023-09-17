package com.axreng.backend.service;

import com.axreng.backend.enums.EnumSearchStatus;
import com.axreng.backend.exception.InvalidSearchKeywordException;
import com.axreng.backend.exception.NotFoundCrawlId;
import com.axreng.backend.response.SearchResponse;
import com.axreng.backend.utils.HTMLLinkExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CrawlServiceImpl implements CrawlService{

    private static final Logger logger = LoggerFactory.getLogger(CrawlServiceImpl.class);
    private static final String BASE_URL = System.getenv("BASE_URL");

    private ConcurrentHashMap<String, SearchResponse> crawlMap;

    public CrawlServiceImpl() {
        crawlMap = new ConcurrentHashMap<>();
    }

    @Override
    public String initCrawl(String keyword) throws InvalidSearchKeywordException {

        if(keyword == null || keyword.length() < 4 || keyword.length() > 32){
            logger.info("Invalid word: "+keyword);
            throw new InvalidSearchKeywordException();
        }

        String id = generateId();


        Thread crawlThread = new Thread(() -> {

            logger.info("Starting crawling for keyword {}", keyword);
            try {
                Set<String> visitedLinks = new HashSet<>();
                SearchResponse response = new SearchResponse();
                response.setId(id);
                response.setStatus(EnumSearchStatus.ACTIVE.getStatus());
                response.setUrls(new TreeSet<>());
                crawlMap.put(id,response);
                crawler(BASE_URL, id, keyword, visitedLinks);
                response.setStatus(EnumSearchStatus.DONE.getStatus());

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            logger.info("Ending crawling for keyword {}", keyword);

        });
        crawlThread.start();
        logger.info("Crawling for ID {} started",id);

        return id;
    }

    @Override
    public SearchResponse search(String id) throws NotFoundCrawlId {

        if(!crawlMap.containsKey(id)){
            throw new NotFoundCrawlId(id);
        }

        return crawlMap.get(id);
    }

    private String generateId() {
        String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        Random random = new Random();
        StringBuilder builder = new StringBuilder(8);

        for (int i = 0; i < 8; i++) {
            builder.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }

        return builder.toString();

    }

    private void crawler(String url, String id, String keyword, Set<String> visitedLinks) throws IOException {

        Set<String> linksHelper = new HashSet<>();

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        int responseCode = con.getResponseCode();
        System.out.println("Response code: " + responseCode);
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        String html = response.toString();
        if(html.contains(keyword))
            crawlMap.get(id).getUrls().add(url);
        linksHelper = linkExtractor(html, visitedLinks);
        for(String link : linksHelper)
            crawler(link, id, keyword, visitedLinks);

    }

    private Set<String> linkExtractor(String html, Set<String> visitedLinks) {

        HTMLLinkExtractor htmlLinkExtractor = new HTMLLinkExtractor();
        Set<String> extractedLinks = htmlLinkExtractor.grabHTMLLinks(html, BASE_URL);

        extractedLinks.removeIf(element -> visitedLinks.contains(element));

        visitedLinks.addAll(extractedLinks);

        return extractedLinks;

    }

}

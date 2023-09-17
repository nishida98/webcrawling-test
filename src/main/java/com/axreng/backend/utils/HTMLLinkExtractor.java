package com.axreng.backend.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLLinkExtractor {

    private Pattern patternTag, patternLink;
    private Matcher matcherTag, matcherLink;

    private static final String HTML_A_TAG_PATTERN = "(?i)<a([^>]+)>(.+?)</a>";
    private static final String HTML_A_HREF_TAG_PATTERN =
            "\\s*(?i)href\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))";


    public HTMLLinkExtractor() {
        patternTag = Pattern.compile(HTML_A_TAG_PATTERN);
        patternLink = Pattern.compile(HTML_A_HREF_TAG_PATTERN);
    }

    public Set<String> grabHTMLLinks(final String html, String baseURL) {

        Set<String> result = new HashSet<>();

        matcherTag = patternTag.matcher(html);

        while (matcherTag.find()) {

            String href = matcherTag.group(1); // href

            matcherLink = patternLink.matcher(href);

            while (matcherLink.find()) {

                String link = matcherLink.group(1); // link

                if(link.contains("https") || (link.contains("http") && !link.contains(baseURL)) || !link.contains("html"))
                    continue;

                if(link.contains(".."))
                    link = link.replace("../", baseURL);
                else if(!link.contains(baseURL))
                    link = baseURL.concat(link);


                result.add(link.replace("\"",""));

            }

        }

        return result;

    }
}

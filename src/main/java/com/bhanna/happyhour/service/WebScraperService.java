package com.bhanna.happyhour.service;

import lombok.extern.log4j.Log4j2;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
public class WebScraperService {

    private static final List<String> UNWANTED_HTML = List.of("abbr", "address", "area", "aside", "audio", "base", "blockquote", "br", "button", "canvas", "cite", "code", "data", "datalist", "dd", "del", "details", "dialog", "embed", "fieldset", "footer", "form", "head", "iframe", "img", "input", "legend", "map", "meta", "nav", "noscript", "picture", "script", "select", "small", "source", "style", "template", "textarea", "time", "title", "track", "video");
    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3";
    private static final String ACCEPT_LANGUAGE = "en-US,en;q=0.5";


    private Document getDocumentForBusiness(String url) throws Exception {
        try {
            // Create a connection and set custom headers
            Connection connection = Jsoup.connect(url)
                    .header("User-Agent", USER_AGENT)
                    .header("Accept-Language", ACCEPT_LANGUAGE)
                    .timeout(5000); // Set a timeout in milliseconds

            // Fetch and parse the HTML content from the URL
            return connection.get();

        } catch (SocketTimeoutException e) {
            log.error("Timeout while connecting to the URL: {}", url, e);
            throw e;
        } catch (IOException e) {
            log.error("IOException while fetching URL: {}", url, e);
            throw e;
        }
    }

    private void removeHtmlComments(Element element) {
        for (Node node : element.childNodes()) {
            if (node instanceof TextNode) {
                node.remove();
            }
        }
    }

    private void cleanHtmlDocument(Element element) {
        removeUnwantedHtmlElements(element);
        removeHtmlComments(element);
        element.select(":empty").remove();
        element.select("[style]").removeAttr("style");
    }

    private void removeUnwantedHtmlElements(Element element) {
        for (String html : UNWANTED_HTML) {
            Elements elements = element.select(html);
            elements.remove();
        }
    }

    private ArrayList<String> extractTextToList(Element element) {
        ArrayList<String> textList = new ArrayList<>();

        for (Element child : element.select("*")) {
            String text = child.ownText().trim();
            if (!text.isEmpty()) {
                textList.add(text);
            }
        }

        return textList;
    }

    private static String convertArrayListToCSV(ArrayList<String> list) {
        StringBuilder csvBuilder = new StringBuilder();

        for (String element : list) {
            if (csvBuilder.length() > 0) {
                csvBuilder.append(",");
            }
            // Handle cases where the element might contain commas or special characters
            csvBuilder.append("\"").append(element.replace("\"", "\"\"")).append("\"");
        }

        return csvBuilder.toString();
    }

    public String scrapeWebsite(String url) {
        try {
            log.info("Scraping website: [url:{}]", url);
            Document doc = getDocumentForBusiness(url);
            Element body = doc.body();
            cleanHtmlDocument(body);
            ArrayList<String> list = extractTextToList(body);
            String csv = convertArrayListToCSV(list);
            log.info("Website scraped successfully: [url:{}, csv:{}]", url, csv);
            return csv;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("Failed to scrape website", e);
        }
    }

}

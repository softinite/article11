package com.softinite.article11;

import com.softinite.article11.conf.Configuration;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.net.URL;
import java.util.logging.Level;

/**
 * Created by Sergiu Ivasenco on 12/10/17.
 */
@Log
public class Crawler {

    @Setter(AccessLevel.PRIVATE)
    @Getter(AccessLevel.PROTECTED)
    private Configuration configuration;

    public Crawler(Configuration configuration) {
        log.info("Setting up crawler with configuration=" + configuration.toString());
        setConfiguration(configuration);
    }

    public void findRecord() {
        log.info("Looking for " + getConfiguration().getNamePrefix() + " at " + configuration.getTargetUrl());
        try{
            Document document = Jsoup.connect(getConfiguration().getTargetUrl()).get();
            log.info("Searching through " + document.title());
            Elements mainContents = document.select("#content_inner");
            Elements middleCol = mainContents.select("#middlecol");
            Element itemPage = middleCol.select(".item-page").first();
            Record currentRecord = new Record();
            for(Element node : itemPage.children()) {
                if (StringUtils.equals(node.tagName(), "h2")) {
                    log.info("Found title=" + StringUtils.trim(node.text()));
                } else if (StringUtils.equals(node.tagName(), "p") || StringUtils.equals(node.tagName(), "h3")) {
                    String content = StringUtils.trim(node.text());
                    if (NumberUtils.isDigits(content)) {
                        currentRecord = new Record();
                        currentRecord.setYear(content);
                    }
                } else if (StringUtils.equals(node.tagName(), "ul")) {
                    processListElements(node.select("li"), currentRecord);
                }
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed to fetch data from " + configuration.getTargetUrl(), e);
        }
    }

    private void processListElements(Elements listElements, Record currentRecord) {
        if (getConfiguration().getYears() != null && getConfiguration().getYears().size() > 0 && !getConfiguration().getYears().contains(currentRecord.getYear())) {
            return;
        }
        for(Element node : listElements) {
            processPdfLine(node, currentRecord);
        }
    }

    private void processPdfLine(Element node, Record currentRecord) {
        currentRecord.setPublicationDate(fetchDate(node));
        Elements links = node.getElementsByTag("a");
        for(Element link : links) {
            processPdfLink(link, currentRecord);
        }
    }

    private String fetchDate(Element node) {
        if (node != null) {
            Elements strongElements = node.getElementsByTag("strong");
            if (strongElements != null && strongElements.size() > 0) {
                return StringUtils.trim(strongElements.first().text());
            }
        }
        return StringUtils.EMPTY;
    }

    private void processPdfLink(Element link, Record currentRecord) {
        currentRecord.setDocumentNumber(StringUtils.trim(link.text()));
        currentRecord.setDocumentUrl(link.attr("abs:href"));
        log.info("Investigate record document " + currentRecord.toString());
        String documentName = fetchName(currentRecord.getDocumentUrl());
        if (documentName != null) {
            File documentFile = new File("records" + File.separator + documentName);
            if (!documentFile.exists()) {
                downloadPdfDocument(currentRecord.getDocumentUrl(), documentFile);
            }
            scanDocument(documentFile, getConfiguration().getNamePrefix());
        }
    }

    protected void scanDocument(File documentFile, String namePrefix) {

    }

    protected void downloadPdfDocument(String documentUrl, File documentFile) {
        log.info("downloading: " + documentUrl);
        try {
            URL url = new URL(documentUrl);
            FileUtils.copyURLToFile(url, documentFile);
        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed to download document " + documentUrl, e);
        }
    }

    protected String fetchName(String documentUrl) {
        if (StringUtils.isNotBlank(documentUrl)) {
            String[] urlElements = StringUtils.split(documentUrl, "/");
            if (urlElements != null && urlElements.length > 1) {
                return urlElements[urlElements.length - 1];
            }
        }
        return null;
    }

}

package com.softinite.article11;

import com.softinite.article11.conf.Configuration;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
            for(Element node : itemPage.children()) {
                if (StringUtils.equals(node.tagName(), "h2")) {
                    log.info("Found title=" + StringUtils.trim(node.text()));
                } else if (StringUtils.equals(node.tagName(), "p")) {
                    String content = StringUtils.trim(node.text());
                    if (NumberUtils.isDigits(content)) {
                        log.info("Processing the year " + content);
                    }
                } else if (StringUtils.equals(node.tagName(), "ul")) {
                    processListElements(node.select("li"));
                }
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed to fetch data from " + configuration.getTargetUrl(), e);
        }
    }

    private void processListElements(Elements listElements) {
        for(Element node : listElements) {
            processPdfLine(node);
        }
    }

    private void processPdfLine(Element node) {
        log.info(StringUtils.trim(node.text()));
        Elements links = node.getElementsByTag("a");
        for(Element link : links) {
            processPdfLink(link);
        }
    }

    private void processPdfLink(Element link) {
        log.info(StringUtils.trim(link.text()) + "=" + link.attr("abs:href"));
    }

}

package com.softinite.article11;

import com.softinite.article11.conf.Configuration;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Sergiu Ivasenco on 12/10/17.
 */
public class CrawlerIT {

    private Crawler crawler;

    @Before
    public void setup() {
        crawler = new Crawler(new Configuration());
    }

    @Test
    public void scanDocument() {
        crawler.scanDocument(new File("/tmp/crawler/Ordin_nr._1008P_din_22.12.2017.pdf"), "florea", new Record());
    }

    @Test
    public void downloadPdfDocument() {
        String docUrl = "http://cetatenie.just.ro/images/Ordin_nr._1008P_din_22.12.2017.pdf";
        String docName = crawler.fetchName(docUrl);
        File docFile = new File("/tmp/crawler/" + docName);
        assertFalse(docFile.exists());
        crawler.downloadPdfDocument(docUrl, docFile);
        assertTrue(docFile.exists());
    }

    @Test
    public void fetchName() {
        assertEquals("Ordin_nr._1008P_din_22.12.2017.pdf", crawler.fetchName("http://cetatenie.just.ro/images/Ordin_nr._1008P_din_22.12.2017.pdf"));
        assertEquals("Ordin_nr._970P_din_07.12.2017.pdf", crawler.fetchName("http://cetatenie.just.ro/images/Ordin_nr._970P_din_07.12.2017.pdf"));
        assertEquals("ORD_____514__P__13_iulie_2017.pdf", crawler.fetchName("http://cetatenie.just.ro/images/ORD_____514__P__13_iulie_2017.pdf"));
        assertEquals("Ordin_2004_P_din_08_12_2016_Moldova.pdf", crawler.fetchName("http://cetatenie.just.ro/images/Ordin_2004_P_din_08_12_2016_Moldova.pdf"));
        assertEquals("Ordin_art._11_nr._509P_din_12.05.2016.pdf", crawler.fetchName("http://cetatenie.just.ro/images/Ordin_art._11_nr._509P_din_12.05.2016.pdf"));
        assertEquals("Ordin_1041P_din_27.11.2015.pdf", crawler.fetchName("http://cetatenie.just.ro/images/Ordin_1041P_din_27.11.2015.pdf"));
    }

}

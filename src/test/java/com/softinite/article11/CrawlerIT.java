package com.softinite.article11;

import com.softinite.article11.conf.Configuration;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created by Sergiu Ivasenco on 12/10/17.
 */
public class CrawlerIT {

    @Test
    public void checkRandom() {
        Configuration configuration = new Configuration();
        configuration.setNamePrefix("Random");
        configuration.setYears(Arrays.asList("2012"));
        Crawler crawler = new Crawler(configuration);
        crawler.findRecord();
    }

}

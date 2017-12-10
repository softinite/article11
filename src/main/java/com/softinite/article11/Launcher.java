package com.softinite.article11;

import com.beust.jcommander.JCommander;
import com.softinite.article11.conf.Configuration;
import lombok.extern.java.Log;

/**
 * Created by Sergiu Ivasenco on 12/10/17.
 */
@Log
public class Launcher {

    public static void main(String[] args) {
        log.info("Starting the launcher.");
        Configuration configuration = new Configuration();
        JCommander jCommander = JCommander
                .newBuilder()
                .addObject(configuration)
                .build();
        log.info("jCommander instance has been created.");
        jCommander.parse(args);
        if (configuration.getHelp() == Boolean.TRUE) {
            log.info("Showing help");
            jCommander.usage();
        } else {
            Crawler crawler = new Crawler(configuration);
            crawler.findRecord();
        }
    }

}

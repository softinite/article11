package com.softinite.article11.conf;

import com.beust.jcommander.Parameter;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by Sergiu Ivasenco on 12/10/17.
 */
@Data
public class Configuration {

    private String targetUrl = "http://cetatenie.just.ro/index.php/ro/ordine/articol-11";

    @Parameter(names = {"-help", "-h", "-?"}, description = "Displays all the available options.", help = true)
    private Boolean help = Boolean.FALSE;

    @Parameter(names = {"-prefix", "-p"}, description = "The name prefix to be searched.")
    private String namePrefix;

    @Parameter(names = {"-years", "-y"}, description = "Only process provided years")
    private List<String> years;

    @Override
    public String toString() {
        return String.format("{ targetUrl: %s, namePrefix: %s, years: %s}", getTargetUrl(), getNamePrefix(), StringUtils.join(years, ','));
    }

}

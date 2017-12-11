package com.softinite.article11;

import lombok.Data;

import java.util.List;

/**
 * Created by Sergiu Ivasenco on 12/10/17.
 */
@Data
public class Record {

    private String year;
    private String publicationDate;
    private String documentNumber;
    private String documentUrl;
    private List<String> names;

    @Override
    public String toString() {
        return String.format("{ year: %s, publicationDate: %s, documentNumber: %s, documentUrl: %s}", getYear(), getPublicationDate(), getDocumentNumber(), getDocumentUrl());
    }
}

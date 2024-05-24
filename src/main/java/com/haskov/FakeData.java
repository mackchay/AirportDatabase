package com.haskov;

import com.github.javafaker.Faker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class FakeData {
    private Faker faker;
    private FileOutputStream fileOutputStream;
    private String fileName = "Generator.sql";

    FakeData() {
        faker = new Faker();
        try {
            fileOutputStream = new FileOutputStream(fileName);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public File generateFakeData() {

        return new File(fileName);
    }

    public void generateJobTitles() {

    }
}

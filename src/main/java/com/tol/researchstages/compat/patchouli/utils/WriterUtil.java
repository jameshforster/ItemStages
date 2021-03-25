package com.tol.researchstages.compat.patchouli.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

abstract class WriterUtil {
    final String basePath = "/patchouli_books/";
    final String bookFileName;
    File file;

    protected WriterUtil(String bookFileName) {
        this.bookFileName = bookFileName;
    }

    public void write(FileWriter writer, JsonObject json) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        writer.write(gson.toJson(json));
        writer.close();
    }

    public void write(FileWriter writer, String json) throws IOException {
        writer.write(json);
        writer.close();
    }
}

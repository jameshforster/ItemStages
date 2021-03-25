package com.tol.researchstages.compat.patchouli.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class BookWriterUtils extends WriterUtil {

    public BookWriterUtils(String bookFileName) {
        super(bookFileName);
        this.file = new File(Minecraft.getInstance().gameDir.getAbsolutePath() + basePath + bookFileName + "/book.json");
    }

    public void writeBook(String name, String landingText, int version) {
        try {
            file.getParentFile().mkdirs();
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            JsonObject json = generateJson(name, landingText, version);
            write(fileWriter, json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JsonObject generateJson(String name, String landingText, int version) {
        JsonObject entry = new JsonObject();
        entry.add("name", new JsonPrimitive(name));
        entry.add("landing_text", new JsonPrimitive(landingText));
        entry.add("version", new JsonPrimitive(version));

        return entry;
    }
}

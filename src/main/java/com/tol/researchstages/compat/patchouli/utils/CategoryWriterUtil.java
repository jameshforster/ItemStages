package com.tol.researchstages.compat.patchouli.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CategoryWriterUtil extends WriterUtil {

    public CategoryWriterUtil(String bookFileName, String categoryName) {
        super(bookFileName);
        file = new File(Minecraft.getInstance().gameDir.getAbsolutePath() +  basePath + bookFileName + "/en_us/categories/" + categoryName + ".json");
    }

    public void writeCategory(String name, String description, String icon, int sortNum) {
        try {
            file.getParentFile().mkdirs();
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            JsonObject json = generateJson(name, description, icon, sortNum);
            write(fileWriter, json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JsonObject generateJson(String name, String description, String icon, int sortNum) {
        JsonObject entry = new JsonObject();
        entry.add("name", new JsonPrimitive(name));
        entry.add("description", new JsonPrimitive(description));
        entry.add("icon", new JsonPrimitive(icon));
        entry.add("sortnum", new JsonPrimitive(sortNum));

        return entry;
    }
}

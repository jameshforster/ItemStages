package com.tol.researchstages.compat.patchouli.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CategoryWriterUtil extends WriterUtil {
    String categoryId;

    public CategoryWriterUtil(String bookFileName, String categoryName) {
        super(bookFileName);
        this.categoryId = categoryName;
        file = new File(Minecraft.getInstance().gameDir.getAbsolutePath() +  basePath + bookFileName + "/en_us/categories/" + categoryName + ".json");
    }

    public void writeCategory(String name, String description, String icon, int sortNum, boolean enableFlag) {
        try {
            file.getParentFile().mkdirs();
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            JsonObject json = generateJson(name, description, icon, sortNum, enableFlag);
            write(fileWriter, json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JsonObject generateJson(String name, String description, String icon, int sortNum, boolean enableFlag) {
        JsonObject entry = new JsonObject();
        entry.add("name", new JsonPrimitive(name));
        entry.add("description", new JsonPrimitive(description));
        entry.add("icon", new JsonPrimitive(icon));
        entry.add("sortnum", new JsonPrimitive(sortNum));
        if (enableFlag) {
            entry.add("flag", new JsonPrimitive(categoryId));
        }

        return entry;
    }
}

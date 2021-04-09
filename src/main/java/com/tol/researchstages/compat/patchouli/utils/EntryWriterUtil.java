package com.tol.researchstages.compat.patchouli.utils;

import com.google.gson.*;
import com.tol.researchstages.research.DefaultCondition;
import com.tol.researchstages.research.ResearchStage;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class EntryWriterUtil extends WriterUtil {
    private final File file;
    private String entryName;

    public EntryWriterUtil(String bookFileName, String entryName) {
        super(bookFileName);
        this.entryName = entryName;
        file = new File(Minecraft.getInstance().gameDir.getAbsolutePath() +  basePath + bookFileName + "/en_us/entries/" + entryName + ".json");
    }

    public void writeEntry(ResearchStage researchStage) {
        try {
            file.getParentFile().mkdirs();
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            JsonObject entry = generateJson(researchStage);
            write(fileWriter, entry);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JsonObject generateJson(ResearchStage researchStage) {
        JsonObject entry = new JsonObject();
        String entryName = researchStage.stageName;
        if (researchStage.bookParams.incompleteName != null) {
            entryName = researchStage.bookParams.incompleteName;
        } else if (researchStage.bookParams.completedName != null) {
            entryName = researchStage.bookParams.completedName;
        }
        entry.add("name", new JsonPrimitive(entryName));
        entry.add("category", new JsonPrimitive("patchouli:" + researchStage.bookParams.categoryName));
        entry.add("icon", new JsonPrimitive((researchStage.bookParams.displayIcon != null) ? researchStage.bookParams.displayIcon : "minecraft:writable_book"));
        entry.add("sortnum", new JsonPrimitive(researchStage.bookParams.sortNum));
        if (!(researchStage.bookParams.displayCondition instanceof DefaultCondition)) {
            entry.add("flag", new JsonPrimitive(researchStage.stageName));
        }

        JsonObject researchDetailsPage = new JsonObject();
        researchDetailsPage.add("type", new JsonPrimitive("patchouli:research_template"));
        researchDetailsPage.add("stageName", new JsonPrimitive(researchStage.stageName));

        JsonObject researchItemsPage = new JsonObject();
        researchItemsPage.add("type", new JsonPrimitive("patchouli:research_template_page2"));
        researchItemsPage.add("stageName", new JsonPrimitive(researchStage.stageName));

        JsonArray pages = new JsonArray();
        pages.add(researchDetailsPage);
        pages.add(researchItemsPage);

        entry.add("pages", pages);

        return entry;
    }
}

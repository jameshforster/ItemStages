package com.tol.researchstages.compat.patchouli.utils;

import com.google.gson.*;
import com.tol.researchstages.research.ResearchStage;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class EntryWriterUtil {
    private final File file;
    private FileWriter fileWriter = null;

    public EntryWriterUtil(String path) {

        file = new File(Minecraft.getInstance().gameDir.getAbsolutePath() + path);
        try {
            file.getParentFile().mkdirs();
            file.createNewFile();

            fileWriter = new FileWriter(file);
        } catch (IOException e) {
            LogManager.getLogger().error("\n######\n" +
                    "IO EXCEPTION: " + e.getMessage() +
                    "\n######\n");
        }
    }

    public void writeEntry(ResearchStage researchStage) {
        JsonObject entry = new JsonObject();
        entry.add("name", new JsonPrimitive((researchStage.incompleteName != null) ? researchStage.incompleteName : researchStage.stageName));
        entry.add("category", new JsonPrimitive("patchouli:research_category"));
        entry.add("icon", new JsonPrimitive((researchStage.displayIcon != null) ? researchStage.displayIcon : "minecraft:writable_book"));

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

        if (fileWriter != null) {
            try {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                fileWriter.write(gson.toJson(entry));
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

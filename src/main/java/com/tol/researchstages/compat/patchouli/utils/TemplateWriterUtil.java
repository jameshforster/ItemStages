package com.tol.researchstages.compat.patchouli.utils;

import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TemplateWriterUtil extends WriterUtil {

    public TemplateWriterUtil(String bookFileName) {
        super(bookFileName);
    }

    public void writeFirstTemplate() {
        this.file = new File(Minecraft.getInstance().gameDir.getAbsolutePath() + basePath + bookFileName + "/en_us/templates/research_template.json");
        try {
            file.getParentFile().mkdirs();
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);

            write(fileWriter, firstTemplate);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeSecondTemplate() {
        this.file = new File(Minecraft.getInstance().gameDir.getAbsolutePath() + basePath + bookFileName + "/en_us/templates/research_template_page2.json");
        try {
            file.getParentFile().mkdirs();
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);

            write(fileWriter, secondTemplate);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final String firstTemplate = "{\n" +
            "  \"processor\": \"com.tol.researchstages.compat.patchouli.processors.ResearchComponentProcessor\",\n" +
            "  \"components\": [\n" +
            "    {\n" +
            "      \"type\": \"header\",\n" +
            "      \"text\": \"#researchTitle\",\n" +
            "      \"x\": -1,\n" +
            "      \"y\": -1\n" +
            "    },\n" +
            "    {\n" +
            "      \"type\": \"separator\",\n" +
            "      \"x\": -1,\n" +
            "      \"y\": -1\n" +
            "    },\n" +
            "    {\n" +
            "      \"type\": \"text\",\n" +
            "      \"text\": \"#content\",\n" +
            "      \"x\": 5,\n" +
            "      \"y\": 20\n" +
            "    },\n" +
            "    {\n" +
            "      \"type\": \"separator\",\n" +
            "      \"x\": -1,\n" +
            "      \"y\": 75\n" +
            "    },\n" +
            "    {\n" +
            "      \"type\" : \"text\",\n" +
            "      \"text\" : \"Progress: #progress#\",\n" +
            "      \"x\": 5,\n" +
            "      \"y\": 80\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    private final String secondTemplate = "{\n" +
            "  \"processor\": \"com.tol.researchstages.compat.patchouli.processors.ResearchComponentProcessor\",\n" +
            "  \"components\": [\n" +
            "    {\n" +
            "      \"type\": \"header\",\n" +
            "      \"text\": \"Research Items\",\n" +
            "      \"x\": -1,\n" +
            "      \"y\": -1\n" +
            "    },\n" +
            "    {\n" +
            "      \"type\": \"separator\",\n" +
            "      \"x\": -1,\n" +
            "      \"y\": -1\n" +
            "    },\n" +
            "    {\n" +
            "      \"type\" : \"text\",\n" +
            "      \"text\" : \"Researched Items\",\n" +
            "      \"x\": 5,\n" +
            "      \"y\": 20\n" +
            "    },\n" +
            "    {\n" +
            "      \"type\": \"custom\",\n" +
            "      \"class\": \"com.tol.researchstages.compat.patchouli.components.ResearchedItemsComponent\",\n" +
            "      \"x\": 5,\n" +
            "      \"y\": 30,\n" +
            "      \"stage_name\": \"#stageName\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"type\": \"separator\",\n" +
            "      \"x\": -1,\n" +
            "      \"y\": 75\n" +
            "    },\n" +
            "    {\n" +
            "      \"type\" : \"text\",\n" +
            "      \"text\" : \"Remaining Research Options\",\n" +
            "      \"x\": 5,\n" +
            "      \"y\": 80\n" +
            "    },\n" +
            "    {\n" +
            "      \"type\": \"custom\",\n" +
            "      \"class\": \"com.tol.researchstages.compat.patchouli.components.RemainingResearchItemsComponent\",\n" +
            "      \"x\": 5,\n" +
            "      \"y\": 90,\n" +
            "      \"stage_name\": \"#stageName\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";
}

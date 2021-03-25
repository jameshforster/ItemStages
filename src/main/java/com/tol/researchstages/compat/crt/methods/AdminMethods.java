package com.tol.researchstages.compat.crt.methods;

import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.tol.researchstages.compat.patchouli.utils.BookWriterUtils;
import com.tol.researchstages.compat.patchouli.utils.CategoryWriterUtil;
import com.tol.researchstages.compat.patchouli.utils.EntryWriterUtil;
import com.tol.researchstages.compat.patchouli.utils.TemplateWriterUtil;
import com.tol.researchstages.research.ResearchStage;
import com.tol.researchstages.utils.ResearchStageUtils;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.ResearchStages.admin")
public class AdminMethods {

	@ZenCodeType.Method
	public static void triggerEntryWriter() {
		for (ResearchStage researchStage : ResearchStageUtils.RESEARCH_STAGES.values()) {
			new EntryWriterUtil(researchStage.bookParams.bookFileName, researchStage.stageName).writeEntry(researchStage);
		}
	}

	@ZenCodeType.Method
	public static void triggerBookWriter(String fileName, String bookName, String landingText, int version) {
		new BookWriterUtils(fileName).writeBook(bookName, landingText, version);
		TemplateWriterUtil templateWriter = new TemplateWriterUtil(fileName);
		templateWriter.writeFirstTemplate();
		templateWriter.writeSecondTemplate();
	}

	@ZenCodeType.Method
	public static void triggerCategoryWriter(String bookFileName, String filename, String categoryName, String description, String icon, int sortNum) {
		new CategoryWriterUtil(bookFileName, filename).writeCategory(categoryName, description, icon, sortNum);
	}
}

package com.tol.itemstages.research;

import com.tol.itemstages.capabilities.ResearchCapability;

import java.math.BigDecimal;
import java.util.HashMap;

public class PlayerResearch {

	public HashMap<ResearchStage, BigDecimal> research = new HashMap<ResearchStage, BigDecimal>();

	public BigDecimal getProgress(ResearchStage researchStage) {
		return research.getOrDefault(researchStage, new BigDecimal(0));
	}

	public void setResearch(HashMap<ResearchStage, BigDecimal> input) {
		this.research.putAll(input);
	}

	public void updateResearch(ResearchStage researchStage, BigDecimal progress) {
		BigDecimal currentProgress = research.getOrDefault(researchStage, new BigDecimal(0));

		research.put(researchStage, currentProgress.add(progress));
	}

	public void removeResearch(ResearchStage researchStage) {
		research.remove(researchStage);
	}
}

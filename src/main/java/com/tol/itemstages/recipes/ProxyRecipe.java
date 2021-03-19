package com.tol.itemstages.recipes;

import net.darkhax.gamestages.data.GameStageSaveHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.LogManager;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class ProxyRecipe implements InvocationHandler {
	public final List<String> stages;
	private final Object target;

	public boolean hasStages() {
		if (Minecraft.getInstance().world != null && Minecraft.getInstance().world.isRemote) {
			boolean hasAllStages = true;
			PlayerEntity player = Minecraft.getInstance().player;
			for (String stage : stages) {
				if (!GameStageSaveHandler.getPlayerData(player.getUniqueID()).hasStage(stage)) {
					hasAllStages = false;
				}
			}
			return hasAllStages;
		}

		return true;
	}

	public ProxyRecipe(Object target, List<String> stages) {
		this.target = target;
		this.stages = stages;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (method.getName().equals("func_77572_b")) {
			LogManager.getLogger().error("\n\n#####" +
					"\nINVOKE METHOD ON PROXY CALLED: " + method.getName() + "\n" +
					"\nRETURNING: " + method.getReturnType() + "\n" +
					"\nWITH PARAMETERS: " + Arrays.toString(method.getParameterTypes()) + "\n" +
					"\nWITH ARGS: " + Arrays.toString(args) + "\n" +
					"#####\n\n");
			if (hasStages()) {
				LogManager.getLogger().error("\n\n#####" +
						"\nPLAYER HAS STAGES RETURNING METHOD: " + method.invoke(target, args) + "\n" +
						"#####\n\n");
				return method.invoke(target, args);
			} else {
				LogManager.getLogger().error("\n\n#####" +
						"\nPLAYER DOES NOT HAVE STAGES RETURNING EMPTY: " + ItemStack.EMPTY + "\n" +
						"#####\n\n");
				return ItemStack.EMPTY;
			}
		}
		return method.invoke(target, args);
	}
}

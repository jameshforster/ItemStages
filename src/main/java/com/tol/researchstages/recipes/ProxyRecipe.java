package com.tol.researchstages.recipes;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.Proxy;
import javassist.util.proxy.ProxyFactory;
import net.darkhax.gamestages.data.GameStageSaveHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.lang.reflect.Method;
import java.util.List;

public class ProxyRecipe<T extends IRecipe<?>> {
	public List<String> stages;
	public T stagedRecipe;
	public T originalRecipe;

	public ProxyRecipe(T recipe, List<String> stages) {
		this.stages = stages;
		this.originalRecipe = recipe;

		ProxyFactory factory = new ProxyFactory();
		factory.setSuperclass(recipe.getClass());

		Class<?> c = factory.createClass();
		try {
			Objenesis objenesis = new ObjenesisStd();
			this.stagedRecipe = (T) objenesis.newInstance(c);
			((Proxy) this.stagedRecipe).setHandler(handler);
		} catch (Exception e) {
			e.printStackTrace();
			this.stagedRecipe = recipe;
		}
	}

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

	MethodHandler handler = new MethodHandler() {
		@Override
		public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
			if (thisMethod.getName().equals("func_77572_b") || thisMethod.getName().equals("func_77571_b")) {
				if (hasStages()) {
					return thisMethod.invoke(originalRecipe, args);
				} else {
					return ItemStack.EMPTY;
				}
			}
			return thisMethod.invoke(originalRecipe, args);
		}
	};
}

package dev.meteoroid.repairamulet;

import dev.meteoroid.repairamulet.item.ModItems;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RepairAmulet implements ModInitializer {
	public static final String MOD_ID = "repairamulet";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	//	public static final Identifier REPAIR_AMULET_ID = Identifier.of("repairamulet", "repair_amulet");
	//	public static final RegistryKey<Item> REPAIR_AMULET_KEY = RegistryKey.of(
	//			Registries.ITEM.getKey(),
	//			Identifier.of("repairamulet", "repair_amulet")
	//	);
	//	private static Item REPAIR_AMULET;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		// Registry.register(Registries.ITEM, Identifier.of("repairamulet", "repair_amulet"), REPAIR_AMULET);
		ModItems.registerModItems();

		ServerTickEvents.END_SERVER_TICK.register(server -> {
			server.getPlayerManager().getPlayerList().forEach(player -> {
				if (hasAmulet(player)) {
					repairAllItems(player);
				}
			});
		});

		LOGGER.info("Hello Fabric world!");
	}

	private void repairAllItems(PlayerEntity player) {
		for (ItemStack stack : player.getInventory().main) repairItem(stack);
		for (ItemStack stack : player.getInventory().armor) repairItem(stack);
		for (ItemStack stack : player.getInventory().offHand) repairItem(stack);
		for (ItemStack stack : player.getEnderChestInventory().heldStacks) repairItem(stack);
	}

	private void repairItem(ItemStack stack) {
		if (stack.isDamaged()) {
			stack.setDamage(stack.getDamage() - 1);
		}
	}

	private boolean hasAmulet(ServerPlayerEntity player) {
		if (ModItems.REPAIR_AMULET == null) {
			LOGGER.error("REPAIR_AMULET is null!");
			return false;
		}
		for (ItemStack stack : player.getInventory().main) {
			if (stack.getItem() == ModItems.REPAIR_AMULET) return true;
		}
		for (ItemStack stack : player.getInventory().armor) {
			if (stack.getItem() == ModItems.REPAIR_AMULET) return true;
		}
		for (ItemStack stack : player.getInventory().offHand) {
			if (stack.getItem() == ModItems.REPAIR_AMULET) return true;
		}
		return false;
	}
}
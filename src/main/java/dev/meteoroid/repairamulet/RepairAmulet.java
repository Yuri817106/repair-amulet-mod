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
	public static final int TICKS_PER_SECOND_LV1 = 50;
	public static final int TICKS_PER_SECOND_LV2 = 25;
	public static final int TICKS_PER_SECOND_LV3 = 15;
	public static int tickCounter = 0;

    @Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		// Registry.register(Registries.ITEM, Identifier.of("repairamulet", "repair_amulet"), REPAIR_AMULET);
		ModItems.registerModItems();

		ServerTickEvents.END_SERVER_TICK.register(server -> {
			tickCounter++;
			for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
				int repairSpeed = getRepairSpeed(player);
				if (repairSpeed > 0 && tickCounter >= repairSpeed) {
					repairAllItems(player);
				}
				if (tickCounter >= repairSpeed) {
					tickCounter = 0; // 重置計數器
				}
			}
		});

		LOGGER.info("Hello Fabric world!");
	}

	private int getRepairSpeed(ServerPlayerEntity player) {
		if (hasAmulet(player, ModItems.REPAIR_AMULET_LV3)) return TICKS_PER_SECOND_LV3;
		if (hasAmulet(player, ModItems.REPAIR_AMULET_LV2)) return TICKS_PER_SECOND_LV2;
		if (hasAmulet(player, ModItems.REPAIR_AMULET_LV1)) return TICKS_PER_SECOND_LV1;
		return -1;
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

	private boolean hasAmulet(ServerPlayerEntity player, Item amuletLevel) {
		for (ItemStack stack : player.getInventory().main) if (stack.getItem() == amuletLevel) return true;
		for (ItemStack stack : player.getInventory().armor) if (stack.getItem() == amuletLevel) return true;
		for (ItemStack stack : player.getInventory().offHand) if (stack.getItem() == amuletLevel) return true;
		return false;
	}
}

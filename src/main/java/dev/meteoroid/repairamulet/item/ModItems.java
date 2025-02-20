package dev.meteoroid.repairamulet.item;

import dev.meteoroid.repairamulet.RepairAmulet;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class ModItems {

    public static final Item REPAIR_AMULET_LV1 = registerItem("repair_amulet_lv1", Item::new, new Item.Settings().maxCount(1));
    public static final Item REPAIR_AMULET_LV2 = registerItem("repair_amulet_lv2", Item::new, new Item.Settings().maxCount(1));
    public static final Item REPAIR_AMULET_LV3 = registerItem("repair_amulet_lv3", Item::new, new Item.Settings().maxCount(1));


    private static Item registerItem(String name, Function<Item.Settings, Item> factory, Item.Settings settings) {
        final RegistryKey<Item> registryKey = RegistryKey.of(Registries.ITEM.getKey(), Identifier.of(RepairAmulet.MOD_ID, name));
        return Items.register(registryKey, factory, settings);
    }

    public static void registerModItems() {
        RepairAmulet.LOGGER.info("Registering Mod Items for " + RepairAmulet.MOD_ID);
    }
}

package com.finderfeed.cataclysm_custscenes;

import com.finderfeed.cataclysm_custscenes.entities.maledictus.MaledictusCutsceneEntity;
import com.finderfeed.cataclysm_custscenes.items.CatCutDebugStick;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

@Mod(CataclysmCutscenes.MODID)
public class CataclysmCutscenes {

    public static final String MODID = "cataclysmcutscenes";

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.createItems(MODID);
    public static final Supplier<Item> DEBUG_STICK = ITEMS.register("debug_stick",() -> new CatCutDebugStick(new Item.Properties()));


    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, MODID);
    public static final Supplier<EntityType<MaledictusCutsceneEntity>> MALEDICTUS_CUTSCENE = ENTITIES.register("maledictus_cutscene", ()-> EntityType.Builder.of(
                    MaledictusCutsceneEntity::new, MobCategory.MISC
            )
            .sized(1f,1f)
            .build("maledictus_cutscene"));

    public CataclysmCutscenes(IEventBus modEventBus, ModContainer modContainer) {
        ITEMS.register(modEventBus);
        ENTITIES.register(modEventBus);
    }



}

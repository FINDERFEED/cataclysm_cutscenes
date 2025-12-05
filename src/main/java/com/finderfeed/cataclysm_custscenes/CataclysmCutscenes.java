package com.finderfeed.cataclysm_custscenes;

import com.finderfeed.cataclysm_custscenes.entities.ancient_remnant.AncientRemnantCutsceneEntity;
import com.finderfeed.cataclysm_custscenes.entities.ignis.IgnisCutsceneEntity;
import com.finderfeed.cataclysm_custscenes.entities.leviathan.LeviathanCutsceneEntity;
import com.finderfeed.cataclysm_custscenes.entities.maledictus.MaledictusCutsceneEntity;
import com.finderfeed.cataclysm_custscenes.entities.scylla.ScyllaCutsceneEntity;
import com.finderfeed.cataclysm_custscenes.items.CatCutDebugStick;
import com.mojang.serialization.Codec;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

@Mod(CataclysmCutscenes.MODID)
public class CataclysmCutscenes {

    public static final String MODID = "cinematiccataclysm";

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final Supplier<Item> DEBUG_STICK = ITEMS.register("debug_stick",() -> new CatCutDebugStick(new Item.Properties()));










    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);

    public static final Supplier<EntityType<MaledictusCutsceneEntity>> MALEDICTUS_CUTSCENE = ENTITIES.register("maledictus_cutscene", ()-> EntityType.Builder.of(
                    MaledictusCutsceneEntity::new, MobCategory.MISC
            )
            .sized(1f,1f)
            .build("maledictus_cutscene"));

    public static final Supplier<EntityType<IgnisCutsceneEntity>> IGNIS_CUTSCENE_ENTITY = ENTITIES.register("ignis_cutscene", ()-> EntityType.Builder.of(
                    IgnisCutsceneEntity::new, MobCategory.MISC
            )
            .sized(1f,1f)
            .build("ignis_cutscene"));

    public static final Supplier<EntityType<ScyllaCutsceneEntity>> SCYLLA_CUTSCENE_ENTITY = ENTITIES.register("scylla_cutscene", ()-> EntityType.Builder.of(
                    ScyllaCutsceneEntity::new, MobCategory.MISC
            )
            .sized(1f,1f)
            .build("scylla_cutscene"));

    public static final Supplier<EntityType<AncientRemnantCutsceneEntity>> ANCIENT_REMNANT_CUTSCENE = ENTITIES.register("ancient_remnant_cutscene", ()-> EntityType.Builder.of(
                    AncientRemnantCutsceneEntity::new, MobCategory.MISC
            )
            .sized(1f,1f)
            .build("ancient_remnant_cutscene"));

    public static final Supplier<EntityType<LeviathanCutsceneEntity>> LEVIATHAN_CUTSCENE = ENTITIES.register("leviathan_cutscene", ()-> EntityType.Builder.of(
                    LeviathanCutsceneEntity::new, MobCategory.MISC
            )
            .sized(1f,1f)
            .build("leviathan_cutscene"));




//    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MODID);
//    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> SPAWNED_BOSS_ONCE = ATTACHMENT_TYPES.register("spawned_boss_once",()->AttachmentType.builder(()->false)
//            .serialize(Codec.BOOL)
//            .build());



    public CataclysmCutscenes() {

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        ENTITIES.register(bus);
        ITEMS.register(bus);
    }



}

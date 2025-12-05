package com.finderfeed.cataclysm_custscenes;

import com.finderfeed.cataclysm_custscenes.entities.ancient_remnant.AncientRemnantCutsceneEntityRenderer;
import com.finderfeed.cataclysm_custscenes.entities.ignis.IgnisCutsceneEntityRenderer;
import com.finderfeed.cataclysm_custscenes.entities.leviathan.LeviathanCutsceneEntityRenderer;
import com.finderfeed.cataclysm_custscenes.entities.maledictus.MaledictusCutsceneEntityRenderer;
import com.finderfeed.cataclysm_custscenes.entities.scylla.ScyllaCutsceneRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = CataclysmCutscenes.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CatCutClientEvents {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(CataclysmCutscenes.MALEDICTUS_CUTSCENE.get(), MaledictusCutsceneEntityRenderer::new);
        event.registerEntityRenderer(CataclysmCutscenes.IGNIS_CUTSCENE_ENTITY.get(), IgnisCutsceneEntityRenderer::new);
        event.registerEntityRenderer(CataclysmCutscenes.SCYLLA_CUTSCENE_ENTITY.get(), ScyllaCutsceneRenderer::new);
        event.registerEntityRenderer(CataclysmCutscenes.ANCIENT_REMNANT_CUTSCENE.get(), AncientRemnantCutsceneEntityRenderer::new);
        event.registerEntityRenderer(CataclysmCutscenes.LEVIATHAN_CUTSCENE.get(), LeviathanCutsceneEntityRenderer::new);
    }

}

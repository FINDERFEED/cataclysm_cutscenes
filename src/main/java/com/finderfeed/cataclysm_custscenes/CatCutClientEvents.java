package com.finderfeed.cataclysm_custscenes;

import com.finderfeed.cataclysm_custscenes.entities.ancient_remnant.AncientRemnantCutsceneEntityRenderer;
import com.finderfeed.cataclysm_custscenes.entities.ignis.IgnisCutsceneEntityRenderer;
import com.finderfeed.cataclysm_custscenes.entities.maledictus.MaledictusCutsceneEntityRenderer;
import com.finderfeed.cataclysm_custscenes.entities.scylla.ScyllaCutsceneRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = CataclysmCutscenes.MODID, value = Dist.CLIENT)
public class CatCutClientEvents {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(CataclysmCutscenes.MALEDICTUS_CUTSCENE.get(), MaledictusCutsceneEntityRenderer::new);
        event.registerEntityRenderer(CataclysmCutscenes.IGNIS_CUTSCENE_ENTITY.get(), IgnisCutsceneEntityRenderer::new);
        event.registerEntityRenderer(CataclysmCutscenes.SCYLLA_CUTSCENE_ENTITY.get(), ScyllaCutsceneRenderer::new);
        event.registerEntityRenderer(CataclysmCutscenes.ANCIENT_REMNANT_CUTSCENE.get(), AncientRemnantCutsceneEntityRenderer::new);
    }

}

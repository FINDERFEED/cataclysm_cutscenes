package com.finderfeed.cataclysm_custscenes.entities.maledictus;

import com.github.L_Ender.cataclysm.client.render.entity.Maledictus_Renderer;
import com.github.L_Ender.cataclysm.entity.InternalAnimationMonster.IABossMonsters.Maledictus.Maledictus_Entity;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class MaledictusCutsceneEntityRenderer extends Maledictus_Renderer {

    public MaledictusCutsceneEntityRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public boolean shouldRender(Maledictus_Entity livingEntity, Frustum camera, double camX, double camY, double camZ) {
        return true;
    }

}

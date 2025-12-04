package com.finderfeed.cataclysm_custscenes.entities.ancient_remnant;

import com.github.L_Ender.cataclysm.client.render.entity.Ancient_Remnant_Rework_Renderer;
import com.github.L_Ender.cataclysm.entity.InternalAnimationMonster.IABossMonsters.Ancient_Remnant.Ancient_Remnant_Entity;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class AncientRemnantCutsceneEntityRenderer extends Ancient_Remnant_Rework_Renderer {

    public AncientRemnantCutsceneEntityRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public boolean shouldRender(Ancient_Remnant_Entity livingEntity, Frustum camera, double camX, double camY, double camZ) {
        return true;
    }

}

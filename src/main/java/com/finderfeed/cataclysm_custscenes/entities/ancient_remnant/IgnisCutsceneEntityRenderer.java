package com.finderfeed.cataclysm_custscenes.entities.ancient_remnant;

import com.github.L_Ender.cataclysm.client.render.entity.Ancient_Remnant_Renderer;
import com.github.L_Ender.cataclysm.client.render.entity.Ignis_Renderer;
import com.github.L_Ender.cataclysm.entity.AnimationMonster.BossMonsters.Ancient_Ancient_Remnant_Entity;
import com.github.L_Ender.cataclysm.entity.AnimationMonster.BossMonsters.Ignis_Entity;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class IgnisCutsceneEntityRenderer extends Ignis_Renderer {

    public IgnisCutsceneEntityRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public boolean shouldRender(Ignis_Entity livingEntity, Frustum camera, double camX, double camY, double camZ) {
        return livingEntity.tickCount > 200;
    }

}

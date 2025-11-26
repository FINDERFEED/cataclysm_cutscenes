package com.finderfeed.cataclysm_custscenes.entities.scylla;

import com.github.L_Ender.cataclysm.client.render.entity.Scylla_Renderer;
import com.github.L_Ender.cataclysm.entity.InternalAnimationMonster.IABossMonsters.Scylla.Scylla_Entity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class ScyllaCutsceneRenderer extends Scylla_Renderer {

    public ScyllaCutsceneRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public boolean shouldRender(Scylla_Entity livingentity, Frustum camera, double camX, double camY, double camZ) {
        return true;
    }

}

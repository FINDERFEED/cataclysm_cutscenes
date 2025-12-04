package com.finderfeed.cataclysm_custscenes.entities.leviathan;

import com.github.L_Ender.cataclysm.client.render.entity.The_Leviathan_Renderer;
import com.github.L_Ender.cataclysm.entity.AnimationMonster.BossMonsters.The_Leviathan.The_Leviathan_Entity;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class LeviathanCutsceneEntityRenderer extends The_Leviathan_Renderer {

    public LeviathanCutsceneEntityRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn);
    }


    @Override
    public boolean shouldRender(The_Leviathan_Entity livingentity, Frustum camera, double camX, double camY, double camZ) {
        return true;
    }


}

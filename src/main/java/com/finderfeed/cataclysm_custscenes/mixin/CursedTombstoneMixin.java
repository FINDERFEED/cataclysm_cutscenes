package com.finderfeed.cataclysm_custscenes.mixin;

import com.finderfeed.cataclysm_custscenes.CatCutMixinHandler;
import com.github.L_Ender.cataclysm.blockentities.Cursed_tombstone_Entity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Cursed_tombstone_Entity.class)
public class CursedTombstoneMixin {

    @Inject(method = "commonTick", at = @At(value = "INVOKE", target = "Lcom/github/L_Ender/cataclysm/entity/effect/ScreenShake_Entity;ScreenShake(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/phys/Vec3;FFII)V", ordinal = 0), cancellable = true)
    private static void commonTick(Level level, BlockPos pos, BlockState blockState, Cursed_tombstone_Entity entity, CallbackInfo ci){
        CatCutMixinHandler.cursedTombstoneMixin(level, pos, blockState, entity, ci);
    }

}

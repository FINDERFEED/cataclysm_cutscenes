package com.finderfeed.cataclysm_custscenes.mixin;

import com.finderfeed.cataclysm_custscenes.CatCutUtil;
import com.finderfeed.cataclysm_custscenes.CataclysmCutscenes;
import com.finderfeed.cataclysm_custscenes.entities.ignis.IgnisCutsceneEntity;
import com.github.L_Ender.cataclysm.blockentities.AltarOfFire_Block_Entity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AltarOfFire_Block_Entity.class)
public class AltarOfFireBlockEntityMixin {

    @Shadow public boolean summoningthis;

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lcom/github/L_Ender/cataclysm/entity/effect/ScreenShake_Entity;ScreenShake(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/phys/Vec3;FFII)V", shift = At.Shift.BEFORE), cancellable = true, remap = false)
    public void tick(Level level, BlockState state, BlockPos pos, CallbackInfo ci){

        AltarOfFire_Block_Entity tile = (AltarOfFire_Block_Entity) (Object) this;

        if (!level.isClientSide){
            if (!CatCutUtil.wasBossSpawned(tile)){
                summoningthis = false;
                IgnisCutsceneEntity ignisCutsceneEntity = IgnisCutsceneEntity.summon(level, pos.getCenter(), pos);
                tile.setItem(0, ItemStack.EMPTY);
                CatCutUtil.setBossWasSpawned(tile, true);
                tile.setChanged();
                level.sendBlockUpdated(pos, state, state, 3);
                ci.cancel();
            }
        }

    }

}

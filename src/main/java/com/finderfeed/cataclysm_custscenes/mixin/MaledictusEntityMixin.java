package com.finderfeed.cataclysm_custscenes.mixin;

import com.finderfeed.cataclysm_custscenes.CatCutUtil;
import com.finderfeed.cataclysm_custscenes.CataclysmCutscenes;
import com.github.L_Ender.cataclysm.blockentities.Cursed_tombstone_Entity;
import com.github.L_Ender.cataclysm.entity.InternalAnimationMonster.IABossMonsters.Maledictus.Maledictus_Entity;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Maledictus_Entity.class)
public class MaledictusEntityMixin {

    @Inject(method = "AfterDefeatBoss", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlockAndUpdate(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z", shift = At.Shift.AFTER))
    public void afterDefeatBoss(LivingEntity living, CallbackInfo ci, @Local BlockPos pos){

        if (((Object)(this)) instanceof Maledictus_Entity maledictusEntity){

            var level = maledictusEntity.level();

            if (!level.isClientSide) {
                if (level.getBlockEntity(pos) instanceof Cursed_tombstone_Entity entity) {
                    CatCutUtil.setBossWasSpawned(entity, true);
                    entity.setChanged();
                }
            }


        }

    }

}

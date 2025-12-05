package com.finderfeed.cataclysm_custscenes.mixin;

import com.finderfeed.cataclysm_custscenes.CatCutUtil;
import com.finderfeed.cataclysm_custscenes.CataclysmCutscenes;
import com.finderfeed.cataclysm_custscenes.entities.leviathan.LeviathanCutsceneEntity;
import com.github.L_Ender.cataclysm.blockentities.AltarOfAbyss_Block_Entity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AltarOfAbyss_Block_Entity.class)
public class AltarOfAbyssBlockEntityMixin {

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lcom/github/L_Ender/cataclysm/entity/effect/ScreenShake_Entity;ScreenShake(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/phys/Vec3;FFII)V", shift = At.Shift.BEFORE), cancellable = true, remap = false)
    private void tick(Level level, BlockState state, BlockPos pos, CallbackInfo ci){
        AltarOfAbyss_Block_Entity tile = (AltarOfAbyss_Block_Entity) (Object) (this);
        if (!level.isClientSide) {

            if (!CatCutUtil.wasBossSpawned(tile)) {
                tile.summoningthis = false;
                CatCutUtil.setBossWasSpawned(tile, true);
                tile.setItem(0, ItemStack.EMPTY);
                tile.setChanged();
                Vec3 summonPos = new Vec3((double) ((float) pos.getX() + 0.5F), (double) (pos.getY() + 3), (double) ((float) pos.getZ() + 0.5F));
                LeviathanCutsceneEntity.summon(level, summonPos, pos);

                level.sendBlockUpdated(pos, state, state, 3);
                ci.cancel();

            }
        }
    }

}

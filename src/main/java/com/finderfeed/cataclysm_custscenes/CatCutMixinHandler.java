package com.finderfeed.cataclysm_custscenes;

import com.finderfeed.cataclysm_custscenes.entities.maledictus.MaledictusCutsceneEntity;
import com.github.L_Ender.cataclysm.blockentities.Cursed_tombstone_Entity;
import com.github.L_Ender.cataclysm.blocks.Cursed_Tombstone_Block;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class CatCutMixinHandler {
    
    public static void cursedTombstoneMixin(Level level, BlockPos pos, BlockState blockState, Cursed_tombstone_Entity entity, CallbackInfo ci){

        if (!level.isClientSide) {
            if (!CatCutUtil.wasBossSpawned(entity)) {
                Direction direction = blockState.getValue(Cursed_Tombstone_Block.FACING);

                Vec3 dir = new Vec3(
                        direction.getStepX(),
                        direction.getStepY(),
                        direction.getStepZ()
                );

                MaledictusCutsceneEntity maledictusCutsceneEntity = MaledictusCutsceneEntity.summon(level, pos.getCenter().add(0, -0.5, 0), dir, pos, direction);

                level.destroyBlock(pos, false);
                ci.cancel();
            }
        }
    }
    
}

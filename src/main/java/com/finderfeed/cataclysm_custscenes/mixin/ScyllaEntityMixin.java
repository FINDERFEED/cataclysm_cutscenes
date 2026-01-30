package com.finderfeed.cataclysm_custscenes.mixin;

import com.finderfeed.cataclysm_custscenes.entities.scylla.ScyllaCutsceneEntity;
import com.github.L_Ender.cataclysm.entity.InternalAnimationMonster.IABossMonsters.Scylla.Scylla_Entity;
import com.github.L_Ender.cataclysm.init.ModEntities;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Scylla_Entity.class)
public class ScyllaEntityMixin {

    @Inject(method = "mobInteract", at = @At(value = "INVOKE", target = "Lcom/github/L_Ender/cataclysm/entity/InternalAnimationMonster/IABossMonsters/Scylla/Scylla_Entity;heal(F)V"))
    public void mobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir){

        Scylla_Entity scyllaEntity = (Scylla_Entity) (Object) this;

        if (!scyllaEntity.level().isClientSide){
            ScyllaCutsceneEntity.summon(scyllaEntity.level(), scyllaEntity.position(), scyllaEntity.getHomePos());
            scyllaEntity.remove(Entity.RemovalReason.DISCARDED);
        }


    }

}

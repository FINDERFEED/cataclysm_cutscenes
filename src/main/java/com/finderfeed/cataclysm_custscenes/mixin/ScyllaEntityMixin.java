package com.finderfeed.cataclysm_custscenes.mixin;

import com.finderfeed.cataclysm_custscenes.entities.scylla.ScyllaCutsceneEntity;
import com.github.L_Ender.cataclysm.Cataclysm;
import com.github.L_Ender.cataclysm.entity.InternalAnimationMonster.IABossMonsters.Scylla.Scylla_Entity;
import com.github.L_Ender.cataclysm.init.ModEntities;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(value = Scylla_Entity.class, remap = false)
public class ScyllaEntityMixin {

    @Inject(method = "mobInteract", at = @At(value = "INVOKE", target = "Lcom/github/L_Ender/cataclysm/entity/InternalAnimationMonster/IABossMonsters/Scylla/Scylla_Entity;setAct(Z)V", shift = At.Shift.AFTER), require = 0)
    public void mobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir){

        Scylla_Entity scyllaEntity = (Scylla_Entity) (Object) this;

        if (!scyllaEntity.level().isClientSide){
            ScyllaCutsceneEntity.summon(scyllaEntity.level(), scyllaEntity.position(), scyllaEntity.getHomePos());
            scyllaEntity.remove(Entity.RemovalReason.DISCARDED);
        }


    }

}

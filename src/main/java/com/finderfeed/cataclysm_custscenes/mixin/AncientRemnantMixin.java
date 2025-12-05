package com.finderfeed.cataclysm_custscenes.mixin;

import com.finderfeed.cataclysm_custscenes.CataclysmCutscenes;
import com.finderfeed.cataclysm_custscenes.entities.ancient_remnant.AncientRemnantCutsceneEntity;
import com.github.L_Ender.cataclysm.Cataclysm;
import com.github.L_Ender.cataclysm.entity.InternalAnimationMonster.IABossMonsters.Ancient_Remnant.Ancient_Remnant_Entity;
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
@Mixin(value = Ancient_Remnant_Entity.class, remap = false)
public class AncientRemnantMixin {

    @Inject(method = "mobInteract", at = @At(value = "INVOKE", target = "Lcom/github/L_Ender/cataclysm/entity/InternalAnimationMonster/IABossMonsters/Ancient_Remnant/Ancient_Remnant_Entity;setNecklace(Z)V", shift = At.Shift.BEFORE), require = 0)
    public void interact(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir){
        Ancient_Remnant_Entity ancientRemnantEntity = (Ancient_Remnant_Entity) (Object) (this);

        if (!ancientRemnantEntity.level().isClientSide){
            AncientRemnantCutsceneEntity.summon(ancientRemnantEntity.level(), ancientRemnantEntity.position(), ancientRemnantEntity.getHomePos());
            ancientRemnantEntity.remove(Entity.RemovalReason.DISCARDED);
        }

    }

}

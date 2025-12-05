package com.finderfeed.cataclysm_custscenes;


import com.finderfeed.cataclysm_custscenes.entities.scylla.ScyllaCutsceneEntity;
import com.finderfeed.fdlib.util.FDTargetFinder;
import com.github.L_Ender.cataclysm.entity.AnimationMonster.BossMonsters.Ignis_Entity;
import com.github.L_Ender.cataclysm.entity.AnimationMonster.BossMonsters.The_Leviathan.The_Leviathan_Entity;
import com.github.L_Ender.cataclysm.entity.InternalAnimationMonster.IABossMonsters.Ancient_Remnant.Ancient_Remnant_Entity;
import com.github.L_Ender.cataclysm.entity.InternalAnimationMonster.IABossMonsters.Maledictus.Maledictus_Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.living.LivingBreatheEvent;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;

@Mod.EventBusSubscriber(modid = CataclysmCutscenes.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CatCutEvents {

    private static HashMap<ServerPlayer, Integer> PLAYER_INVULNERABILITY = new HashMap<>();

    @SubscribeEvent
    public static void serverTickEvent(TickEvent.ServerTickEvent event){
        if (event.phase == TickEvent.Phase.END) {
            tickPlayerInvulnerability();
        }
    }

    private static void tickPlayerInvulnerability(){
        var iter = PLAYER_INVULNERABILITY.entrySet().iterator();
        while (iter.hasNext()){
            var entry = iter.next();
            int value = entry.getValue();
            if (value <= 0){
                iter.remove();
            }else{
                entry.setValue(value - 1);
            }
        }
    }

    public static boolean isPlayerInvulnerable(ServerPlayer player){
        return PLAYER_INVULNERABILITY.containsKey(player);
    }

    public static void setPlayerInvulnerable(ServerPlayer serverPlayer, int duration){
        PLAYER_INVULNERABILITY.put(serverPlayer,duration);
        for (var entity : FDTargetFinder.getEntitiesInSphere(Mob.class, serverPlayer.level(), serverPlayer.position(), 100)){
            if (entity.getTarget() == serverPlayer){
                entity.setTarget(null);
            }
        }
    }

    @SubscribeEvent
    public static void livingHurtEvent(LivingHurtEvent event){
        if (event.getEntity() instanceof ServerPlayer serverPlayer && isPlayerInvulnerable(serverPlayer)){
            event.setCanceled(true);
        }
    }


    @SubscribeEvent
    public static void targetEvent(LivingChangeTargetEvent event){
        if (event.getNewTarget() instanceof ServerPlayer serverPlayer && isPlayerInvulnerable(serverPlayer)){
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void airEvent(LivingBreatheEvent event){
        if (event.getEntity() instanceof ServerPlayer serverPlayer){
            if (isPlayerInvulnerable(serverPlayer)){
                event.setConsumeAirAmount(0);
                serverPlayer.setAirSupply(ServerPlayer.TOTAL_AIR_SUPPLY);
            }
        }
    }



}

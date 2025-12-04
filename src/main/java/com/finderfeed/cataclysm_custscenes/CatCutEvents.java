package com.finderfeed.cataclysm_custscenes;


import com.finderfeed.cataclysm_custscenes.entities.scylla.ScyllaCutsceneEntity;
import com.finderfeed.fdlib.util.FDTargetFinder;
import com.github.L_Ender.cataclysm.entity.AnimationMonster.BossMonsters.Ignis_Entity;
import com.github.L_Ender.cataclysm.entity.AnimationMonster.BossMonsters.The_Leviathan.The_Leviathan_Entity;
import com.github.L_Ender.cataclysm.entity.InternalAnimationMonster.IABossMonsters.Ancient_Remnant.Ancient_Remnant_Entity;
import com.github.L_Ender.cataclysm.entity.InternalAnimationMonster.IABossMonsters.Maledictus.Maledictus_Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Mob;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.EntityInvulnerabilityCheckEvent;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.HashMap;

@EventBusSubscriber(modid = CataclysmCutscenes.MODID)
public class CatCutEvents {

    private static HashMap<ServerPlayer, Integer> PLAYER_INVULNERABILITY = new HashMap<>();

    @SubscribeEvent
    public static void serverTickEvent(ServerTickEvent.Post event){
        tickPlayerInvulnerability();
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
    public static void livingHurtEvent(EntityInvulnerabilityCheckEvent event){
        if (event.getEntity() instanceof ServerPlayer serverPlayer && isPlayerInvulnerable(serverPlayer)){
            event.setInvulnerable(true);
        }
    }

    @SubscribeEvent
    public static void targetEvent(LivingChangeTargetEvent event){
        if (event.getNewAboutToBeSetTarget() instanceof ServerPlayer serverPlayer && isPlayerInvulnerable(serverPlayer)){
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void entityAttributes(EntityAttributeCreationEvent event){
        event.put(CataclysmCutscenes.MALEDICTUS_CUTSCENE.get(), Maledictus_Entity.maledictus().build());
        event.put(CataclysmCutscenes.IGNIS_CUTSCENE_ENTITY.get(), Ignis_Entity.ignis().build());
        event.put(CataclysmCutscenes.SCYLLA_CUTSCENE_ENTITY.get(), ScyllaCutsceneEntity.scylla().build());
        event.put(CataclysmCutscenes.ANCIENT_REMNANT_CUTSCENE.get(), Ancient_Remnant_Entity.maledictus().build());
        event.put(CataclysmCutscenes.LEVIATHAN_CUTSCENE.get(), The_Leviathan_Entity.leviathan().build());
    }

}

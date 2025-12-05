package com.finderfeed.cataclysm_custscenes;

import com.finderfeed.cataclysm_custscenes.entities.scylla.ScyllaCutsceneEntity;
import com.github.L_Ender.cataclysm.entity.AnimationMonster.BossMonsters.Ignis_Entity;
import com.github.L_Ender.cataclysm.entity.AnimationMonster.BossMonsters.The_Leviathan.The_Leviathan_Entity;
import com.github.L_Ender.cataclysm.entity.InternalAnimationMonster.IABossMonsters.Ancient_Remnant.Ancient_Remnant_Entity;
import com.github.L_Ender.cataclysm.entity.InternalAnimationMonster.IABossMonsters.Maledictus.Maledictus_Entity;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CataclysmCutscenes.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CatCutModEvents {

    @SubscribeEvent
    public static void entityAttributes(EntityAttributeCreationEvent event){
        event.put(CataclysmCutscenes.MALEDICTUS_CUTSCENE.get(), Maledictus_Entity.maledictus().build());
        event.put(CataclysmCutscenes.IGNIS_CUTSCENE_ENTITY.get(), Ignis_Entity.ignis().build());
        event.put(CataclysmCutscenes.SCYLLA_CUTSCENE_ENTITY.get(), ScyllaCutsceneEntity.scylla().build());
        event.put(CataclysmCutscenes.ANCIENT_REMNANT_CUTSCENE.get(), Ancient_Remnant_Entity.maledictus().build());
        event.put(CataclysmCutscenes.LEVIATHAN_CUTSCENE.get(), The_Leviathan_Entity.leviathan().build());
    }

}

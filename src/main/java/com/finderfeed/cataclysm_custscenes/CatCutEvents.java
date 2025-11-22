package com.finderfeed.cataclysm_custscenes;


import com.github.L_Ender.cataclysm.entity.InternalAnimationMonster.IABossMonsters.Maledictus.Maledictus_Entity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@EventBusSubscriber(modid = CataclysmCutscenes.MODID)
public class CatCutEvents {

    @SubscribeEvent
    public static void entityAttributes(EntityAttributeCreationEvent event){
        event.put(CataclysmCutscenes.MALEDICTUS_CUTSCENE.get(), Maledictus_Entity.maledictus().build());
    }

}

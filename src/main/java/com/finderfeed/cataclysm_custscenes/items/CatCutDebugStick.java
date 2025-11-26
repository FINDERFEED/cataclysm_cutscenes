package com.finderfeed.cataclysm_custscenes.items;

import com.finderfeed.cataclysm_custscenes.entities.ignis.IgnisCutsceneEntity;
import com.finderfeed.cataclysm_custscenes.entities.scylla.ScyllaCutsceneEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CatCutDebugStick extends Item {

    public CatCutDebugStick(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {

        if (level instanceof ServerLevel serverLevel && usedHand == InteractionHand.MAIN_HAND){
            ScyllaCutsceneEntity.summon(level, player.position());
        }

        return super.use(level, player, usedHand);
    }
}

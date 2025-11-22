package com.finderfeed.cataclysm_custscenes.items;

import com.finderfeed.cataclysm_custscenes.CataclysmCutscenes;
import com.finderfeed.cataclysm_custscenes.entities.maledictus.MaledictusCutsceneEntity;
import com.finderfeed.fdlib.util.FDTargetFinder;
import com.github.L_Ender.cataclysm.blocks.Cursed_Tombstone_Block;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class CatCutDebugStick extends Item {

    public CatCutDebugStick(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {

        if (level instanceof ServerLevel serverLevel && usedHand == InteractionHand.MAIN_HAND){

            MaledictusCutsceneEntity.summon(level, player.position().add(40,0,0),new Vec3(0,0,1), player.getOnPos(), Direction.EAST);

        }

        return super.use(level, player, usedHand);
    }
}

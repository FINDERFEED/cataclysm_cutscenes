package com.finderfeed.cataclysm_custscenes;

import com.finderfeed.fdlib.FDLibCalls;
import com.finderfeed.fdlib.systems.cutscenes.CutsceneData;
import com.finderfeed.fdlib.util.FDTargetFinder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

public class CatCutUtil {

    public static void startCutsceneForPlayers(ServerLevel serverLevel, Vec3 pos, float radius, int invulnerabilityDuration, CutsceneData cutsceneData){
        FDLibCalls.startCutsceneForPlayers(serverLevel, pos, radius, cutsceneData);
        for (var player : FDTargetFinder.getEntitiesInSphere(ServerPlayer.class, serverLevel, pos, radius)){
            CatCutEvents.setPlayerInvulnerable(player, invulnerabilityDuration);
        }
    }


}

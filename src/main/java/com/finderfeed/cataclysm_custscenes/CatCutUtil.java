package com.finderfeed.cataclysm_custscenes;

import com.finderfeed.fdlib.FDLibCalls;
import com.finderfeed.fdlib.systems.cutscenes.CutsceneData;
import com.finderfeed.fdlib.util.FDTargetFinder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class CatCutUtil {

    public static List<ServerPlayer> startCutsceneForPlayers(ServerLevel serverLevel, Vec3 pos, float radius, int invulnerabilityDuration, CutsceneData cutsceneData){
        FDLibCalls.startCutsceneForPlayers(serverLevel, pos, radius, cutsceneData);
        List<ServerPlayer> startedCutsceneFor;
        for (var player : startedCutsceneFor = FDTargetFinder.getEntitiesInSphere(ServerPlayer.class, serverLevel, pos, radius)){
            CatCutEvents.setPlayerInvulnerable(player, invulnerabilityDuration);
        }
        return startedCutsceneFor;
    }

    public static List<ServerPlayer> startCutsceneForPlayersCylinder(ServerLevel serverLevel, Vec3 pos, float radius, float cylinderHeight, int invulnerabilityDuration, CutsceneData cutsceneData){
        FDLibCalls.startCutsceneForPlayers(serverLevel, pos, radius, cutsceneData);
        List<ServerPlayer> startedCutsceneFor;
        for (var player : startedCutsceneFor = FDTargetFinder.getEntitiesInCylinder(ServerPlayer.class, serverLevel, pos, cylinderHeight, radius)){
            CatCutEvents.setPlayerInvulnerable(player, invulnerabilityDuration);
        }
        return startedCutsceneFor;
    }


}

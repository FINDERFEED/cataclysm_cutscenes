package com.finderfeed.cataclysm_custscenes.entities.scylla;

import com.finderfeed.cataclysm_custscenes.CatCutUtil;
import com.finderfeed.cataclysm_custscenes.CataclysmCutscenes;
import com.finderfeed.cataclysm_custscenes.entities.ignis.IgnisCutsceneEntity;
import com.finderfeed.fdlib.init.FDScreenEffects;
import com.finderfeed.fdlib.nbt.AutoSerializable;
import com.finderfeed.fdlib.network.FDPacketHandler;
import com.finderfeed.fdlib.systems.cutscenes.CameraPos;
import com.finderfeed.fdlib.systems.cutscenes.CutsceneData;
import com.finderfeed.fdlib.systems.cutscenes.EasingType;
import com.finderfeed.fdlib.systems.impact_frames.ImpactFrame;
import com.finderfeed.fdlib.systems.impact_frames.ImpactFramesPacket;
import com.finderfeed.fdlib.systems.screen.screen_effect.instances.datas.ScreenColorData;
import com.finderfeed.fdlib.systems.shake.DefaultShakePacket;
import com.finderfeed.fdlib.systems.shake.FDShakeData;
import com.finderfeed.fdlib.util.FDTargetFinder;
import com.github.L_Ender.cataclysm.config.CMConfig;
import com.github.L_Ender.cataclysm.entity.InternalAnimationMonster.IABossMonsters.Scylla.Scylla_Ceraunus_Entity;
import com.github.L_Ender.cataclysm.entity.InternalAnimationMonster.IABossMonsters.Scylla.Scylla_Entity;
import com.github.L_Ender.cataclysm.entity.effect.Cm_Falling_Block_Entity;
import com.github.L_Ender.cataclysm.init.ModEntities;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingDestroyBlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkDirection;


import java.util.EnumSet;
import java.util.List;

public class ScyllaCutsceneEntity extends Scylla_Entity implements AutoSerializable {

    public static void summon(Level level, Vec3 pos, BlockPos homePos){

        ScyllaCutsceneEntity entity = new ScyllaCutsceneEntity(CataclysmCutscenes.SCYLLA_CUTSCENE_ENTITY.get(), level);

        entity.setHomePos(homePos);

        entity.setPos(pos);

        var affected = CatCutUtil.startCutsceneForPlayers((ServerLevel) level, pos, 40, 220, createCutscene(pos));

        var inSurvival = affected.stream().filter((player)->{
            return !player.isCreative() && !player.isSpectator();
        }).toList();

        for (int i = 0; i < inSurvival.size(); i++){

            var player = inSurvival.get(i);
            int md = i % 2 == 0 ? 1 : -1;

            int offsetX = i / 2;

            Vec3 v = new Vec3(md * (23 + offsetX * 2),2,0).add(pos);

            player.teleportTo(v.x,v.y,v.z);
            player.lookAt(EntityAnchorArgument.Anchor.FEET,pos.add(0,1,0));

        }

        level.addFreshEntity(entity);

    }

    private static CutsceneData createCutscene(Vec3 pos){
        CutsceneData cutsceneData = CutsceneData.create()
                .addScreenEffect(0, FDScreenEffects.SCREEN_COLOR, new ScreenColorData(0f,0f,0f,1f), 0,0,60)
                .time(85)
                .timeEasing(EasingType.EASE_IN_OUT)
                .lookEasing(EasingType.EASE_IN);

        CameraPos last = null;

        cutsceneData.addCameraPos(new CameraPos(pos.add(0,2.5f,35),new Vec3(0,-1,-0.05)));
        cutsceneData.addCameraPos(last = new CameraPos(pos.add(0,2.5f,8),new Vec3(0,0,-1)));

        CutsceneData data1 = CutsceneData.create()
                .time(2)
                .addCameraPos(last);

        CutsceneData data2 = CutsceneData.create()
                .time(20)
                .timeEasing(EasingType.EASE_OUT)
                .addCameraPos(last)
                .addCameraPos(last = new CameraPos(last.getPos().add(0,0,20),new Vec3(0,0,-1)))
                ;

        CutsceneData data3 = CutsceneData.create()
                .time(17)
                .addCameraPos(last);

        CutsceneData data4 = CutsceneData.create()
                .time(5)
                .timeEasing(EasingType.EASE_OUT)
                .addCameraPos(last)
                .addCameraPos(last = new CameraPos(last.getPos().add(0,0,1),new Vec3(0,0,-1)))
                ;

        CutsceneData data5 = CutsceneData.create()
                .time(5)
                .addCameraPos(last);

        CutsceneData data6 = CutsceneData.create()
                .time(25)
                .timeEasing(EasingType.EASE_IN_OUT)
                .addCameraPos(last)
                .addCameraPos(last = new CameraPos(last.getPos().add(0,0,-24),new Vec3(0,0,-1)))
                ;

        CutsceneData data7 = CutsceneData.create()
                .time(5)
                .addCameraPos(last)
                .addCameraPos(last = new CameraPos(last.getPos().add(0,0,-2),new Vec3(0,0,-1)))
                ;

        CutsceneData data8 = CutsceneData.create()
                .addScreenEffect(10, FDScreenEffects.SCREEN_COLOR, new ScreenColorData(0f,0f,0f,1f), 20,10,20)
                .timeEasing(EasingType.EASE_OUT)
                .time(30)
                .addCameraPos(last)
                .addCameraPos(last = new CameraPos(last.getPos().add(0,0,10), new Vec3(0,0,-1)))
                ;



        CutsceneData dataLast = CutsceneData.create()
                .time(10)
                .addCameraPos(last);

        cutsceneData.nextCutscene(data1.nextCutscene(data2.nextCutscene(data3.nextCutscene(data4.nextCutscene(
                data5.nextCutscene(data6.nextCutscene(data7.nextCutscene(data8.nextCutscene(dataLast)))))))));

        return cutsceneData;
    }

    public ScyllaCutsceneEntity(EntityType entity, Level world) {
        super(entity, world);
    }

    @Override
    public void tick() {
        super.tick();

        if (!level().isClientSide){

            //6 - some sort of stomp or jump
            //8 - waves
            //9 storm (for some reason anchor doesn't work)
            //11 - some wind punch
            //10 - throw spear
            //13 - throw anchor
            //14 -return anchor
            //17 - land
            //18 - strike earth
            //20 - throw with lightnings
            //23 - anchor
            //24 - fall spawn
            //27 strike from above

            this.tickCutscene();


        }

    }

    private void tickCutscene(){

        if (tickCount < 60){
            this.setAttackState(23);
        }

        Vec3 pos = this.position();

        float shakeAmpl = 0.5f;

        if (tickCount == 60 || tickCount == 61 || tickCount == 62){

            if (tickCount == 60){
                DefaultShakePacket.send((ServerLevel) level(), this.position(), 40, FDShakeData.builder()
                                .outTime(5)
                                .amplitude(shakeAmpl)
                        .build());
            }

            this.smashEffect(pos.add(7,0,-1),2,tickCount - 59,2.5f);
            this.summonLightningBolt(pos.add(7 + random.nextFloat() * 2 - 1,0,random.nextFloat() * 2 - 1));

        }else if (tickCount == 65 || tickCount == 66 || tickCount == 67){
            if (tickCount == 65){
                DefaultShakePacket.send((ServerLevel) level(), this.position(), 40, FDShakeData.builder()
                        .outTime(5)
                        .amplitude(shakeAmpl)
                        .build());
            }

            this.smashEffect(pos.add(-7,0,-1),2,tickCount - 64,2.5f);
            this.summonLightningBolt(pos.add(-7 + random.nextFloat() * 2 - 1,0,random.nextFloat() * 2 - 1));

        }else if (tickCount == 70 || tickCount == 71 || tickCount == 72){
            if (tickCount == 70){
                DefaultShakePacket.send((ServerLevel) level(), this.position(), 40, FDShakeData.builder()
                        .outTime(5)
                        .amplitude(shakeAmpl)
                        .build());
            }
            this.smashEffect(pos.add(0,0,-8),2,tickCount - 69,2.5f);
            this.summonLightningBolt(pos.add(random.nextFloat() * 2 - 1,0,-7 + random.nextFloat() * 2 - 1));

        }else if (tickCount == 75){
            this.setAttackState(24);

        }else if (tickCount == 95){
            this.setAttackState(13);
        }else if (tickCount == 110){
            Scylla_Ceraunus_Entity scyllaAnchor = new Scylla_Ceraunus_Entity(this.level(), this);
            scyllaAnchor.setControllerUUID(this.getUUID());
            scyllaAnchor.shoot(0,0.35,1, 2f, 0.0f);
            this.setAnchorUUID(scyllaAnchor.getUUID());
            level().addFreshEntity(scyllaAnchor);
        }else if (this.tickCount == 126){
            DefaultShakePacket.send((ServerLevel) level(), this.position(), 40, FDShakeData.builder()
                    .outTime(5)
                    .amplitude(1.5f)
                    .build());
        }else if (this.tickCount == 130){
            this.setAttackState(14);

        } else if (this.tickCount == 150){
            this.setAttackState(27);
        }else if (this.tickCount == 164){
            DefaultShakePacket.send((ServerLevel) level(), this.position(), 40, FDShakeData.builder()
                    .outTime(20)
                    .amplitude(shakeAmpl)
                    .build());
        }


        if (this.tickCount > 200 && this.getAnchor() != null){
            this.getAnchor().remove(RemovalReason.DISCARDED);
            this.setAnchorUUID(null);
        }


        //6 - some sort of stomp or jump
        //8 - waves
        //9 storm (for some reason anchor doesn't work)
        //11 - some wind punch
        //10 - throw spear
        //13 - throw anchor
        //14 -return anchor
        //17 - land
        //18 - strike earth
        //20 - throw with lightnings
        //23 - anchor
        //24 - fall spawn
        //27 strike from above



        int startLandingEffect = 84;
        if (tickCount >= startLandingEffect && tickCount <= startLandingEffect + 10){
            if (tickCount <= startLandingEffect + 5){
                this.summonLightningBolt(pos);
            }


            this.smashEffect(this.position().add(0,0,-1),2, tickCount - 89, 2.5f);


            if (tickCount == startLandingEffect){
                ImpactFramesPacket impactFramesPacket = new ImpactFramesPacket(List.of(
                        new ImpactFrame().setDuration(2),
                        new ImpactFrame().setDuration(1).setInverted(true),
                        new ImpactFrame().setDuration(1),
                        new ImpactFrame().setDuration(1).setInverted(true),
                        new ImpactFrame().setDuration(1)
                ));

                for (var serverPlayer : FDTargetFinder.getEntitiesInCylinder(ServerPlayer.class, level(), this.position().add(0,-10,0),30,30)){
                    FDPacketHandler.INSTANCE.sendTo(impactFramesPacket, serverPlayer.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
                }

                DefaultShakePacket.send((ServerLevel) level(), this.position(), 40, FDShakeData.builder()
                        .outTime(5)
                        .amplitude(shakeAmpl)
                        .build());


            }
        }




        if (tickCount >= 200){
            Scylla_Entity scyllaEntity = ModEntities.SCYLLA.get().create(level());
            scyllaEntity.setPos(this.position());
            scyllaEntity.setHomePos(this.getHomePos());
            scyllaEntity.setDimensionType(level().dimension().location().toString());
            this.remove(RemovalReason.DISCARDED);
            scyllaEntity.setAct(true);
            level().addFreshEntity(scyllaEntity);
        }

    }


    // Code is copied from Maledictus_Entity
    public void smashEffect(Vec3 posEffect, float spreadarc, int distance, float vec) {
        double perpFacing = (double)this.yBodyRot * 0.017453292519943295;
        double facingAngle = perpFacing + 1.5707963267948966;
        int hitY = Mth.floor(posEffect.y);
        double spread = Math.PI * (double)spreadarc;
        int arcLen = Mth.ceil((double)distance * spread);

        for(int i = 0; i < arcLen; ++i) {
            double theta = ((double)i / ((double)arcLen - 1.0) - 0.5) * spread + facingAngle;
            double vx = Math.cos(theta);
            double vz = Math.sin(theta);
            double px = posEffect.x + vx * (double)distance + (double)vec * Math.cos((double)(this.yBodyRot + 90.0F) * Math.PI / 180.0);
            double pz = posEffect.z + vz * (double)distance + (double)vec * Math.sin((double)(this.yBodyRot + 90.0F) * Math.PI / 180.0);
            int hitX = Mth.floor(px);
            int hitZ = Mth.floor(pz);
            BlockPos pos = new BlockPos(hitX, hitY, hitZ);
            BlockState block = this.level().getBlockState(pos);
            int maxDepth = 30;

            for(int depthCount = 0; depthCount < maxDepth && block.getRenderShape() != RenderShape.MODEL; ++depthCount) {
                pos = pos.below();
                block = this.level().getBlockState(pos);
            }

            if (block.getRenderShape() != RenderShape.MODEL) {
                block = Blocks.AIR.defaultBlockState();
            }

            if (!this.level().isClientSide) {
                Cm_Falling_Block_Entity fallingBlockEntity = new Cm_Falling_Block_Entity(this.level(), (double)hitX + 0.5, (double)hitY + 1.0, (double)hitZ + 0.5, block, 10);
                fallingBlockEntity.push(0.0, 0.2 + this.getRandom().nextGaussian() * 0.15, 0.0);
                this.level().addFreshEntity(fallingBlockEntity);
            }
        }

    }

    private void summonLightningBolt(Vec3 pos){
        LightningBolt lightningBolt = new LightningBolt(EntityType.LIGHTNING_BOLT, level());
        lightningBolt.setPos(pos);
        lightningBolt.setVisualOnly(true);
        level().addFreshEntity(lightningBolt);
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    public boolean shouldRender(double x, double y, double z) {
        return true;
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return true;
    }

    @Override
    protected void registerGoals() {

    }






    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {

    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {

    }

    @Override
    public void push(Entity entityIn) {

    }


    @Override
    public void push(double x, double y, double z) {

    }

    @Override
    protected void pushEntities() {

    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        return (source.is(DamageTypes.GENERIC_KILL) || source.is(DamageTypes.FELL_OUT_OF_WORLD)) && super.hurt(source, damage);
    }

    @Override
    protected void tickDeath() {
        this.remove(RemovalReason.DISCARDED);
    }

    @Override
    protected SoundEvent getDeathSound() {
        return null;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return null;
    }

    @Override
    protected SoundEvent getSwimSound() {
        return null;
    }

    @Override
    protected void ReturnToHome() {

    }


    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.autoLoad(compound);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        this.autoSave(compound);
    }

    @Mod.EventBusSubscriber(modid = CataclysmCutscenes.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class Events {

        @SubscribeEvent
        public static void livingDestroyBlockEvent(LivingDestroyBlockEvent event){
            if (event.getEntity() instanceof ScyllaCutsceneEntity){
                event.setCanceled(true);
            };

        }

    }

}

package com.finderfeed.cataclysm_custscenes.entities.ignis;

import com.finderfeed.cataclysm_custscenes.CatCutUtil;
import com.finderfeed.cataclysm_custscenes.CataclysmCutscenes;
import com.finderfeed.fdlib.init.FDScreenEffects;
import com.finderfeed.fdlib.nbt.AutoSerializable;
import com.finderfeed.fdlib.nbt.SerializableField;
import com.finderfeed.fdlib.systems.cutscenes.CameraPos;
import com.finderfeed.fdlib.systems.cutscenes.CurveType;
import com.finderfeed.fdlib.systems.cutscenes.CutsceneData;
import com.finderfeed.fdlib.systems.cutscenes.EasingType;
import com.finderfeed.fdlib.systems.impact_frames.ImpactFrame;
import com.finderfeed.fdlib.systems.impact_frames.ImpactFramesPacket;
import com.finderfeed.fdlib.systems.screen.screen_effect.instances.datas.ScreenColorData;
import com.finderfeed.fdlib.systems.shake.DefaultShakePacket;
import com.finderfeed.fdlib.systems.shake.FDShakeData;
import com.finderfeed.fdlib.util.FDTargetFinder;
import com.finderfeed.fdlib.util.ProjectileMovementPath;
import com.finderfeed.fdlib.util.math.FDMathUtil;
import com.finderfeed.fdlib.util.rendering.FDEasings;
import com.github.L_Ender.cataclysm.entity.AnimationMonster.BossMonsters.Ignis_Entity;
import com.github.L_Ender.cataclysm.init.ModEntities;
import com.github.L_Ender.cataclysm.init.ModSounds;
import com.github.L_Ender.lionfishapi.server.animation.AnimationHandler;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDestroyBlockEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public class IgnisCutsceneEntity extends Ignis_Entity implements AutoSerializable {

    @SerializableField
    private ProjectileMovementPath movementPath;

    //Altar relative
    public static IgnisCutsceneEntity summon(Level level, Vec3 pos, BlockPos homePos){

        pos = pos.add(0,0.5,0);

        IgnisCutsceneEntity cutsceneEntity = new IgnisCutsceneEntity(CataclysmCutscenes.IGNIS_CUTSCENE_ENTITY.get(), level);

        cutsceneEntity.setPos(pos.add(0,5,0));

        cutsceneEntity.movementPath = createPath(pos);

        cutsceneEntity.setHomePos(homePos);

        cutsceneEntity.lookAt(EntityAnchorArgument.Anchor.FEET, pos.add(0,0,-100));

        var affected = CatCutUtil.startCutsceneForPlayers((ServerLevel) level, pos, 60, 300, createCutsceneData(pos));

        var inSurvival = affected.stream().filter((player)->{
            return true || !player.isCreative() && !player.isSpectator();
        }).toList();

        for (int i = 0; i < inSurvival.size(); i++){

            float p = ((float) i / inSurvival.size());
            float angle = FDMathUtil.FPI * 2f * p;
            Vec3 optimalPos = pos.add(new Vec3(-20,0,0).yRot(angle));
            ServerPlayer serverPlayer = inSurvival.get(i);
            serverPlayer.teleportTo(optimalPos.x,optimalPos.y,optimalPos.z);
            serverPlayer.lookAt(EntityAnchorArgument.Anchor.FEET, pos);

        }


        level.addFreshEntity(cutsceneEntity);

        return cutsceneEntity;
    }

    private static ProjectileMovementPath createPath(Vec3 pos){
        ProjectileMovementPath movementPath = new ProjectileMovementPath(20, false);

        movementPath.addPos(pos.add(0,5,0));
        movementPath.addPos(pos.add(0,6,-2.5));
        movementPath.addPos(pos.add(0,7,-3));
        movementPath.addPos(pos.add(0,3,-5));
        movementPath.addPos(pos.add(0,-2,-7));

        return movementPath;
    }

    private static CutsceneData createCutsceneData(Vec3 pos){
        CutsceneData cutsceneData = CutsceneData.create()
                .addScreenEffect(0, FDScreenEffects.SCREEN_COLOR, new ScreenColorData(0,0,0,1), 0,0,50)
                .time(180)

                .timeEasing(EasingType.EASE_IN_OUT);

        CameraPos lastPos = null;

        int c = 24;
        float angle = FDMathUtil.FPI * 2 / c;
        for (int i = 0; i <= c; i++){
            Vec3 v = new Vec3(0,0,-1).yRot(i * angle);

            float p = i / (c - 1f);

            float radius = FDMathUtil.lerp(10, 25, 1 - p);

            float height = FDMathUtil.lerp(0, 8, p);

            Vec3 offset = v.scale(radius).add(0,height - 2,0);

            Vec3 camPos = pos.add(offset);

            Vec3 look = camPos.subtract(pos.add(0,5,0)).normalize().reverse().yRot(-angle / 2 * FDEasings.easeIn(1 - p));

            cutsceneData.addCameraPos(lastPos = new CameraPos(camPos, look));
        }

        CutsceneData cutsceneData2 = new CutsceneData().time(20)
                .timeEasing(EasingType.EASE_IN_OUT)
                .addCameraPos(lastPos)
                .addCameraPos(lastPos = new CameraPos(lastPos.getPos().add(0,0,3),new Vec3(0,0,1)))
                ;


        CutsceneData cutsceneData3 = new CutsceneData()
                .time(30)
                .timeEasing(EasingType.EASE_IN_OUT)
                .moveCurveType(CurveType.CATMULLROM)
                .addCameraPos(lastPos)
                .addCameraPos(new CameraPos(lastPos.getPos().add(0,-2,-3), new Vec3(0,1,1)))
                .addCameraPos(new CameraPos(lastPos.getPos().add(0,-5,-5), new Vec3(0,1,1)))
                .addCameraPos(lastPos = new CameraPos(lastPos.getPos().add(0,-6,-6), new Vec3(0,0,1)))
                ;


        CutsceneData cutsceneData4 = new CutsceneData().addCameraPos(lastPos).time(120)
                .addScreenEffect(100, FDScreenEffects.SCREEN_COLOR, new ScreenColorData(0,0,0,1), 20,10,20);


        cutsceneData.nextCutscene(cutsceneData2.nextCutscene(cutsceneData3.nextCutscene(cutsceneData4)));

        return cutsceneData;
    }



    public IgnisCutsceneEntity(EntityType<? extends IgnisCutsceneEntity> entity, Level world) {
        super(entity, world);
    }

    @Override
    public void tick() {



        this.setTarget(null);

        super.tick();

        if (!level().isClientSide){
            if (this.movementPath == null){
                this.remove(RemovalReason.DISCARDED);
            }
        }

        this.tickCutscene();

        this.noPhysics = true;
        this.setNoGravity(true);

    }

    private void tickCutscene(){
        int summoningRitualDuration = 200;
        if (!level().isClientSide){

            if (tickCount < 20) {
                this.setIsShieldBreak(true);
                this.setShieldDurability(0);
            }

            if (tickCount == summoningRitualDuration - 20){
                AnimationHandler.INSTANCE.sendAnimationMessage(this, SMASH_IN_AIR);

                for (var serverPlayer : FDTargetFinder.getEntitiesInCylinder(ServerPlayer.class, level(), this.position().add(0,-10,0),40,40)){
                    PacketDistributor.sendToPlayer(serverPlayer,new DefaultShakePacket(FDShakeData.builder()
                            .inTime(20)
                            .outTime(10)
                            .amplitude(1f)
                            .build()));
                }


            }else if (tickCount == summoningRitualDuration + 15){
                AnimationHandler.INSTANCE.sendAnimationMessage(this, SMASH);
            }else if (tickCount == summoningRitualDuration + 22){
                for (var serverPlayer : FDTargetFinder.getEntitiesInCylinder(ServerPlayer.class, level(), this.position().add(0,-10,0),40,40)){
                    PacketDistributor.sendToPlayer(serverPlayer,new DefaultShakePacket(FDShakeData.builder()
                            .outTime(5)
                            .amplitude(1.5f)
                            .build()));
                }
            } else if (tickCount == summoningRitualDuration + 55) {
                AnimationHandler.INSTANCE.sendAnimationMessage(this, HORIZONTAL_SWING_ATTACK);
            }else if (tickCount == summoningRitualDuration + 85){
                this.setShowShield(true);
            } else if (tickCount == summoningRitualDuration - 2){
                ImpactFramesPacket impactFramesPacket = new ImpactFramesPacket(List.of(
                        new ImpactFrame().setDuration(2),
                        new ImpactFrame().setDuration(1).setInverted(true),
                        new ImpactFrame().setDuration(1),
                        new ImpactFrame().setDuration(1).setInverted(true),
                        new ImpactFrame().setDuration(1)
                ));

                for (var serverPlayer : FDTargetFinder.getEntitiesInCylinder(ServerPlayer.class, level(), this.position().add(0,-10,0),40,40)){
                    PacketDistributor.sendToPlayer(serverPlayer,impactFramesPacket);
                }
            } else if (tickCount == summoningRitualDuration){
                this.level().playSound(null, this.getX(),this.getY(),this.getZ(), SoundEvents.GENERIC_EXPLODE, SoundSource.HOSTILE, 4f, 0.75f);
                this.level().playSound(null, this.getX(),this.getY(),this.getZ(), SoundEvents.TOTEM_USE, SoundSource.HOSTILE, 4f, 0.75f);
                this.level().playSound(null, this.getX(),this.getY(),this.getZ(), ModSounds.IGNIS_HURT, SoundSource.HOSTILE, 4f, 1f);
            } else if (tickCount >= summoningRitualDuration + 155){
                this.remove(RemovalReason.DISCARDED);
                Ignis_Entity ignisEntity = ModEntities.IGNIS.get().create(level());
                ignisEntity.setHomePos(this.getHomePos());
                ignisEntity.setDimensionType(this.level().dimension().location().toString());
                ignisEntity.setPos(this.position());
                level().addFreshEntity(ignisEntity);
            }

            if (tickCount > summoningRitualDuration - 1){

                if (!this.movementPath.isFinished()) {
                    this.movementPath.tick(this);
                }else{
                    this.setDeltaMovement(Vec3.ZERO);
                }

            }


        }else{
            this.swordProgress = 10;
            this.prevswordProgress = 10;


            this.startingRitualParticles(summoningRitualDuration);

            if (tickCount > summoningRitualDuration && tickCount <= summoningRitualDuration + 4){
                this.ignisSpawnExplosion(this.position().add(0,2,0));
            }

            if (tickCount == summoningRitualDuration + 85){
                this.ignisSpawnExplosion(this.position().add(this.getLookAngle().scale(2)));
            }

            if (tickCount < summoningRitualDuration) {
                level().addParticle(ParticleTypes.LAVA,
                        this.getX() + random.nextFloat() - 0.5,
                        this.getY() + random.nextFloat() - 0.5 + 0.5,
                        this.getZ() + random.nextFloat() - 0.5,
                        0, 0, 0);


                level().addParticle(ParticleTypes.FLAME,
                        this.getX() + random.nextFloat() - 0.5,
                        this.getY() + random.nextFloat() - 0.5 + 0.5,
                        this.getZ() + random.nextFloat() - 0.5,
                        random.nextFloat() * 0.2 - 0.1,
                        random.nextFloat() * 0.2 - 0.1,
                        random.nextFloat() * 0.2 - 0.1
                );


                if (tickCount < summoningRitualDuration - 10 && tickCount % 2 == 0){
                    this.particlesFromAltar(-tickCount / 15f );
                    this.particlesFromAltar(-tickCount / 15f + FDMathUtil.FPI);
                }



            }


        }
    }

    private void particlesFromAltar(float angleOffset){

        int c = 12;
        float angle = FDMathUtil.FPI / c;
        for (int i = 0; i <= c; i++){
            float p = i / (c - 1f);
            Vec3 v = new Vec3(0,0,-0.6 * FDEasings.easeOut(1 - p)).yRot(i * angle + angleOffset);


            float height = FDMathUtil.lerp(-5, 0, p);

            Vec3 ppos = this.position().add(0,height,0).add(v);

            level().addParticle(ParticleTypes.FLAME, ppos.x,ppos.y,ppos.z,0,0,0);

        }
    }

    private void startingRitualParticles(int duration){

        float p = tickCount / (float) duration;

        if (tickCount < duration){
            Vec3 center = this.position().add(0,FDMathUtil.lerp(-2,6, p * p * p * p) - 5,0);
            Vec3 center2 = this.position().add(0,FDMathUtil.lerp(15,6, p * p * p * p) - 5,0);

            this.createParticlesOnCircle(ParticleTypes.FLAME, center, 30 * (1 - p), 12, FDMathUtil.FPI * 1.5f * p * p * p);
            this.createParticlesOnCircle(ParticleTypes.FLAME, center2, 30 * (1 - p), 12, -FDMathUtil.FPI * 1.5f * p * p * p);
        }


    }

    //Altar of fire block entity
    private void ignisSpawnExplosion(Vec3 center){

        float size = 3f;

        for(float i = -size; i <= size; ++i) {
            for(float j = -size; j <= size; ++j) {
                for(float k = -size; k <= size; ++k) {
                    double d3 = (double)j + (this.random.nextDouble() - this.random.nextDouble()) * 0.5;
                    double d4 = (double)i + (this.random.nextDouble() - this.random.nextDouble()) * 0.5;
                    double d5 = (double)k + (this.random.nextDouble() - this.random.nextDouble()) * 0.5;
                    double d6 = (double) Mth.sqrt((float)(d3 * d3 + d4 * d4 + d5 * d5)) / 0.5 + this.random.nextGaussian() * 0.05;
                    level().addParticle(ParticleTypes.FLAME, center.x, center.y, center.z, d3 / d6, d4 / d6, d5 / d6);
                    if (i != -size && i != size && j != -size && j != size) {
                        k += size * 2.0F - 1.0F;
                    }
                }
            }
        }
    }

    private void createParticlesOnCircle(ParticleOptions options, Vec3 center, float radius, int count, float startAngle){

        float angle = FDMathUtil.FPI * 2 / count;
        for (int i = 0; i < count; i++){

            Vec3 v = new Vec3(radius,0,0).yRot(i * angle + startAngle);
            Vec3 pos = center.add(v);

            level().addParticle(options, true, pos.x,pos.y,pos.z,0,0,0);

        }

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
    public void push(Vec3 vector) {

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

    @EventBusSubscriber(modid = CataclysmCutscenes.MODID)
    public static class Events {

        @SubscribeEvent
        public static void livingDestroyBlockEvent(LivingDestroyBlockEvent event){
            if (event.getEntity() instanceof IgnisCutsceneEntity){
                event.setCanceled(true);
            };

        }

    }
}

package com.finderfeed.cataclysm_custscenes.entities.ancient_remnant;

import com.finderfeed.cataclysm_custscenes.CatCutUtil;
import com.finderfeed.cataclysm_custscenes.CataclysmCutscenes;
import com.finderfeed.cataclysm_custscenes.entities.ignis.IgnisCutsceneEntity;
import com.finderfeed.fdlib.init.FDScreenEffects;
import com.finderfeed.fdlib.nbt.AutoSerializable;
import com.finderfeed.fdlib.nbt.SerializableField;
import com.finderfeed.fdlib.systems.cutscenes.CameraPos;
import com.finderfeed.fdlib.systems.cutscenes.CutsceneData;
import com.finderfeed.fdlib.systems.cutscenes.EasingType;
import com.finderfeed.fdlib.systems.screen.screen_effect.instances.datas.ScreenColorData;
import com.finderfeed.fdlib.systems.shake.DefaultShakePacket;
import com.finderfeed.fdlib.systems.shake.FDShakeData;
import com.finderfeed.fdlib.util.FDTargetFinder;
import com.finderfeed.fdlib.util.math.FDMathUtil;
import com.github.L_Ender.cataclysm.entity.InternalAnimationMonster.IABossMonsters.Ancient_Remnant.Ancient_Remnant_Entity;
import com.github.L_Ender.cataclysm.entity.projectile.Ancient_Desert_Stele_Entity;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDestroyBlockEvent;
import net.neoforged.neoforge.network.PacketDistributor;

public class AncientRemnantCutsceneEntity extends Ancient_Remnant_Entity implements AutoSerializable {

    @SerializableField
    private Vec3 cutsceneDirection;

    public static void summon(Level level, Vec3 pos){

        AncientRemnantCutsceneEntity cutsceneEntity = new AncientRemnantCutsceneEntity(CataclysmCutscenes.ANCIENT_REMNANT_CUTSCENE.get(), level);

        cutsceneEntity.setPos(pos);

        cutsceneEntity.setAttackState(1);

        Vec3 cutsceneDirection = decideCutsceneDirection(level, pos.add(0,2,0));

        cutsceneEntity.lookAt(EntityAnchorArgument.Anchor.FEET, pos.add(cutsceneDirection.reverse().scale(100)));

        cutsceneEntity.cutsceneDirection = cutsceneDirection;

        var cutscene = cutsceneData(pos, cutsceneDirection);
        CatCutUtil.startCutsceneForPlayersCylinder((ServerLevel) level, pos.add(0,-10,0).add(cutsceneDirection.scale(30)),40, 35, 200, cutscene);

        level.addFreshEntity(cutsceneEntity);

    }

    public static CutsceneData cutsceneData(Vec3 pos, Vec3 cutsceneDirection){
        CutsceneData data = CutsceneData.create()
                .addScreenEffect(0, FDScreenEffects.SCREEN_COLOR, new ScreenColorData(0f,0f,0f,1f),0,0,40)
                .timeEasing(EasingType.EASE_IN_OUT)
                .time(90);

        Vec3 initPos = pos.add(cutsceneDirection.scale(5)).add(0,3,0);
        CameraPos lastPos = null;

        data
                .addCameraPos(new CameraPos(initPos,cutsceneDirection.reverse().add(0,-0.5,0)))
                .addCameraPos(lastPos = new CameraPos(initPos.add(cutsceneDirection.scale(5)).add(0,2,0),cutsceneDirection.reverse().add(0,-0.1,0)))
        ;

        CutsceneData data1 = CutsceneData.create()
                .addScreenEffect(40, FDScreenEffects.SCREEN_COLOR, new ScreenColorData(0f,0f,0f,1f),30,20,20)
                .timeEasing(EasingType.EASE_OUT)
                .time(70)
                .addCameraPos(lastPos)
                .addCameraPos(new CameraPos(lastPos.getPos().add(cutsceneDirection.scale(18)).add(0,-7,0),cutsceneDirection.reverse().add(0,0.5,0)))
                ;


        data.nextCutscene(data1);

        return data;
    }

    public static Vec3 decideCutsceneDirection(Level level, Vec3 basePos){

        Vec3 dir = null;
        double maxDist = -1;

        for (var d : Direction.Plane.HORIZONTAL){
            Vec3 checkVec = new Vec3(
                    d.getStepX(),
                    d.getStepY(),
                    d.getStepZ()
            );

            Vec3 endVec = basePos.add(checkVec.scale(100));

            ClipContext clipContext = new ClipContext(basePos,endVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, CollisionContext.empty());
            var result = level.clip(clipContext);
            Vec3 location = result.getLocation();
            Vec3 between = location.subtract(basePos);
            double dist = between.length();
            if (dist > maxDist){
                maxDist = dist;
                dir = checkVec;
            }
        }

        return dir;
    }

    public AncientRemnantCutsceneEntity(EntityType entity, Level world) {
        super(entity, world);
    }

    @Override
    public void tick() {

        this.noPhysics = true;
        this.setNoGravity(true);



        super.tick();
        if (!level().isClientSide){

            if (this.cutsceneDirection == null){
                this.remove(RemovalReason.DISCARDED);
            }

            this.tickCutscene();

            //1 - sleep
            //2 - wake up
            //7 - roar
            //8 stomp
            //17 - epic roar

        }
    }

    private void tickCutscene(){

        if (tickCount >= 10){
            this.getLookControl().setLookAt(this.position().add(this.cutsceneDirection.scale(100)));
        }

        if (tickCount == 3){
            this.setAttackState(2);
        }else if (tickCount == 75){
            this.setAttackState(17);


            for (int forward = 0; forward < 30; forward++){

                for (int sideward = -10; sideward < 10; sideward++){

                    if (random.nextFloat() > 0.33f){
                        continue;
                    }

                    Vec3 offsetPos = this.position().add(0,2,0)
                            .add(this.cutsceneDirection.scale(forward))
                            .add(this.cutsceneDirection.yRot(FDMathUtil.FPI / 2).scale(sideward))
                            ;

                    Vec3 endCheck = offsetPos.add(0,40,0);
                    ClipContext clipContext = new ClipContext(offsetPos, endCheck, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, CollisionContext.empty());
                    var result = level().clip(clipContext);
                    Vec3 location = result.getLocation();
                    Vec3 between = endCheck.subtract(location);
                    double dist = between.length();
                    if (dist > 10){
                        Ancient_Desert_Stele_Entity ancientDesertSteleEntity = new Ancient_Desert_Stele_Entity(level(),
                                location.x,location.y - 2, location.z, 0, forward + 25,0, this);
                        level().addFreshEntity(ancientDesertSteleEntity);
                    }

                }

            }

        }else if (this.tickCount == 95){
            Vec3 shakeStart = this.position().add(cutsceneDirection.scale(30)).add(0,-5,0);
            for (var player : FDTargetFinder.getEntitiesInCylinder(ServerPlayer.class, level(), shakeStart, 30, 40)){
                PacketDistributor.sendToPlayer(player, new DefaultShakePacket(FDShakeData.builder()
                        .amplitude(1f)
                        .outTime(60)
                        .build()));
            }
        }

        if (tickCount >= 200){
            this.remove(RemovalReason.DISCARDED);
        }

    }

    @Override
    public int getHeadRotSpeed() {
        return 4;
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
            if (event.getEntity() instanceof AncientRemnantCutsceneEntity){
                event.setCanceled(true);
            };

        }

    }

}

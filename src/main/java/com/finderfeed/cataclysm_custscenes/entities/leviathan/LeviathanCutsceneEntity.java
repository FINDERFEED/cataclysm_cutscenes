package com.finderfeed.cataclysm_custscenes.entities.leviathan;

import com.finderfeed.cataclysm_custscenes.CatCutUtil;
import com.finderfeed.cataclysm_custscenes.CataclysmCutscenes;
import com.finderfeed.fdlib.init.FDScreenEffects;
import com.finderfeed.fdlib.nbt.AutoSerializable;
import com.finderfeed.fdlib.nbt.SerializableField;
import com.finderfeed.fdlib.systems.cutscenes.CameraPos;
import com.finderfeed.fdlib.systems.cutscenes.CutsceneData;
import com.finderfeed.fdlib.systems.screen.screen_effect.instances.datas.ScreenColorData;
import com.finderfeed.fdlib.util.math.FDMathUtil;
import com.github.L_Ender.cataclysm.entity.AnimationMonster.BossMonsters.The_Leviathan.The_Leviathan_Entity;
import com.github.L_Ender.cataclysm.init.ModEntities;
import com.github.L_Ender.cataclysm.init.ModSounds;
import com.github.L_Ender.lionfishapi.server.animation.AnimationHandler;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
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

public class LeviathanCutsceneEntity extends The_Leviathan_Entity implements AutoSerializable {

    @SerializableField
    private Vec3 summonedPos;

    public static void summon(Level level, Vec3 pos, BlockPos homePos){
        pos = pos.add(0,4,0);
        LeviathanCutsceneEntity leviathanCutsceneEntity = new LeviathanCutsceneEntity(CataclysmCutscenes.LEVIATHAN_CUTSCENE.get(), level);

        leviathanCutsceneEntity.summonedPos = pos;

        leviathanCutsceneEntity.setHomePos(homePos);
        leviathanCutsceneEntity.setDimensionType(level.dimension().location().toString());

        Vec3 cutsceneEntityPos = leviathanCutsceneEntity.calculateSwimmingPos(0,220);
        leviathanCutsceneEntity.setPos(cutsceneEntityPos);

        CutsceneData cutsceneData = cutsceneData(pos);
        var affected = CatCutUtil.startCutsceneForPlayersCylinder((ServerLevel) level, pos.add(0,-10,0), 30, 60,170, cutsceneData);


        var inSurvival = affected.stream().filter((player)->{
            return !player.isCreative() && !player.isSpectator();
        }).toList();

        for (int i = 0; i < inSurvival.size(); i++){

            float p = ((float) i / inSurvival.size());
            float angle = FDMathUtil.FPI * 2f * p;
            Vec3 optimalPos = leviathanCutsceneEntity.findOptimalPlayerTpPos(pos, angle + FDMathUtil.FPI / 4);
            ServerPlayer serverPlayer = inSurvival.get(i);
            serverPlayer.teleportTo(optimalPos.x,optimalPos.y,optimalPos.z);
            serverPlayer.lookAt(EntityAnchorArgument.Anchor.FEET, pos);

        }



        level.addFreshEntity(leviathanCutsceneEntity);
    }

    private Vec3 findOptimalPlayerTpPos(Vec3 summonedPos, float angle){

        Vec3 v = new Vec3(25,0,0).yRot(angle);

        for (int i = 0; i < 10; i++){

            Vec3 startCheck = summonedPos.add(v).add(0,i,0);
            Vec3 endCheck = startCheck.add(0,-40,0);
            ClipContext clipContext = new ClipContext(startCheck,endCheck, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, CollisionContext.empty());
            var result = level().clip(clipContext);
            var location = result.getLocation();
            if (location.distanceTo(startCheck) > 2){
                return location;
            }

        }


        return summonedPos.add(v.scale(0.5f));
    }

    public static CutsceneData cutsceneData(Vec3 summonedPos){

        CameraPos last = null;

        CutsceneData data = CutsceneData.create()
                .addScreenEffect(0, FDScreenEffects.SCREEN_COLOR, new ScreenColorData(0,0,0,1),0,20,10)
                .addScreenEffect(30, FDScreenEffects.SCREEN_COLOR, new ScreenColorData(0,0,0,1),10,15,10)
                .addScreenEffect(60, FDScreenEffects.SCREEN_COLOR, new ScreenColorData(0,0,0,1),10,20,10)
                .addScreenEffect(100, FDScreenEffects.SCREEN_COLOR, new ScreenColorData(0,0,0,1),20,23,3)
                .time(140)
                ;

        int c = 12;
        for (int i = 0; i <= c; i++){
            float p = i / (float) c;

            Vec3 lookAngle = new Vec3(1,0,0).yRot(p * FDMathUtil.FPI * 2);
            data.addCameraPos(last = new CameraPos(summonedPos, lookAngle));

        }

        CutsceneData data1 = CutsceneData.create()
                .time(40)
                .addScreenEffect(15, FDScreenEffects.SCREEN_COLOR, new ScreenColorData(0,0,0,1),0,30,20)
                .addCameraPos(last)
                ;

        return data.nextCutscene(data1);
    }

    public LeviathanCutsceneEntity(EntityType type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    public void tick() {
        this.setNoGravity(true);
        this.noPhysics = true;
        super.tick();

        //LEVIATHAN_GRAB - open mouth
        if (!level().isClientSide){
            if (this.summonedPos == null){
                this.remove(RemovalReason.DISCARDED);
                return;
            }

            this.tickCutscene();

        }

    }

    private void tickCutscene(){

        int swimmingAroundTime = 140;
        int swimmingToCameraTime = 10;

        if (tickCount == swimmingAroundTime - 20){
            AnimationHandler.INSTANCE.sendAnimationMessage(this, LEVIATHAN_GRAB);
        }else if (tickCount == swimmingAroundTime - 5){

            this.level().playSound(null,summonedPos.x,summonedPos.y,summonedPos.z, ModSounds.LEVIATHAN_ROAR.get(), SoundSource.HOSTILE, 5f, 1f);
        }

        if (tickCount < swimmingAroundTime) {
            Vec3 currentSwimPos = this.calculateSwimmingPos(tickCount, swimmingAroundTime);
            Vec3 nextSwimPos = this.calculateSwimmingPos(tickCount + 1, swimmingAroundTime);
            Vec3 deltaMovement = nextSwimPos.subtract(currentSwimPos);
            this.setDeltaMovement(deltaMovement);
            this.lookAt(EntityAnchorArgument.Anchor.FEET,this.position().add(deltaMovement.scale(100)));
        }else if (tickCount == swimmingAroundTime){
        }else if (tickCount - swimmingAroundTime <= swimmingToCameraTime){
            Vec3 currentSwimPos = this.calculateSwimmingPos(0, swimmingAroundTime);
            float p = (float)(tickCount - swimmingAroundTime) / swimmingToCameraTime;
            float p2 = (float)(tickCount - swimmingAroundTime + 1) / swimmingToCameraTime;
            Vec3 targetPos = FDMathUtil.interpolateVectors(currentSwimPos, summonedPos, p * 0.9f);
            Vec3 targetPos2 = FDMathUtil.interpolateVectors(currentSwimPos, summonedPos, p2 * 0.9f);
            Vec3 deltaMovement = targetPos2.subtract(targetPos);
            this.setDeltaMovement(deltaMovement);
            this.lookAt(EntityAnchorArgument.Anchor.FEET,this.position().add(deltaMovement.scale(100)));

        }else if (tickCount >= swimmingAroundTime + swimmingToCameraTime + 20){
            this.removeAndSpawn();
        }else{
            this.setDeltaMovement(Vec3.ZERO);
        }

        if (tickCount > 300){
            this.removeAndSpawn();
        }

    }

    private void removeAndSpawn(){
        The_Leviathan_Entity leviathanEntity = ModEntities.THE_LEVIATHAN.get().create(level());
        leviathanEntity.setHomePos(this.getHomePos());
        leviathanEntity.setPos(this.summonedPos);
        leviathanEntity.setDimensionType(level().dimension().location().toString());
        this.remove(RemovalReason.DISCARDED);
        level().addFreshEntity(leviathanEntity);
    }

    private Vec3 calculateSwimmingPos(int swimmingTick, int fullSwimTime){
        float currentAngle = FDMathUtil.FPI * 2 * ((float) swimmingTick / fullSwimTime);
        Vec3 currentPos = new Vec3(28,0,0).yRot(currentAngle);
        Vec3 pos = this.summonedPos.add(currentPos);
        return pos;
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
            if (event.getEntity() instanceof LeviathanCutsceneEntity){
                event.setCanceled(true);
            };

        }

    }


}

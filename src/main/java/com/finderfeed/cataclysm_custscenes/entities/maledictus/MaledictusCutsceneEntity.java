package com.finderfeed.cataclysm_custscenes.entities.maledictus;

import com.finderfeed.cataclysm_custscenes.CatCutUtil;
import com.finderfeed.cataclysm_custscenes.CataclysmCutscenes;
import com.finderfeed.fdlib.init.FDScreenEffects;
import com.finderfeed.fdlib.nbt.AutoSerializable;
import com.finderfeed.fdlib.nbt.SerializableField;
import com.finderfeed.fdlib.systems.cutscenes.CameraPos;
import com.finderfeed.fdlib.systems.cutscenes.CutsceneData;
import com.finderfeed.fdlib.systems.cutscenes.EasingType;
import com.finderfeed.fdlib.systems.screen.screen_effect.instances.datas.ScreenColorData;
import com.finderfeed.fdlib.util.ProjectileMovementPath;
import com.github.L_Ender.cataclysm.config.CMConfig;
import com.github.L_Ender.cataclysm.entity.InternalAnimationMonster.IABossMonsters.Maledictus.Maledictus_Entity;
import com.github.L_Ender.cataclysm.entity.effect.Cm_Falling_Block_Entity;
import com.github.L_Ender.cataclysm.init.ModEntities;
import com.github.L_Ender.cataclysm.init.ModSounds;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDestroyBlockEvent;
import org.jetbrains.annotations.Nullable;

public class MaledictusCutsceneEntity extends Maledictus_Entity implements AutoSerializable{

    // 0 - idle
    // 1 - mace strike
    // 2 - bow pull
    // 3 - other bow pull
    // 4 - idle in air (falling)
    // 5 - end fly
    // 6 - death
    // 7 - big strike with flame
    // 8 - start flying and then hands up
    // 9 - strike earth with blocks
    // 10 - just something
    // 11 - swipe through air
    // 12 - another swipe through air

    public static final int IDLE = 0;
    public static final int MACE_STRIKE = 1;
    public static final int BOW_PULL_1 = 2;
    public static final int BOW_PULL_2 = 3;
    public static final int AIR_IDLE_FALLING = 4;
    public static final int END_FLY = 5;
    public static final int DEATH = 6;
    public static final int BIG_STRIKE_FLAME = 7;
    public static final int START_FLYING_HANDS_UP = 8;
    public static final int STRIKE_WITH_BLOCKS = 9;

    @SerializableField
    private ProjectileMovementPath movementPath;

    private int afterPathTicks = 0;

    public static MaledictusCutsceneEntity summon(Level level, Vec3 pos, Vec3 direction, BlockPos homeAndTombstonePos, Direction tombstoneDirection){

        MaledictusCutsceneEntity maledictus = new MaledictusCutsceneEntity(CataclysmCutscenes.MALEDICTUS_CUTSCENE.get(), level);

        direction = direction.multiply(1,0,1).normalize();

        maledictus.setHomePos(homeAndTombstonePos);
        maledictus.setTombstonePos(homeAndTombstonePos);
        maledictus.setTombstoneDirection(tombstoneDirection);
        ResourceLocation dimLoc = level.dimension().location();

        maledictus.setDimensionType(dimLoc.toString());
        maledictus.movementPath = createMovementPath(pos, direction);


        maledictus.setPos(maledictus.movementPath.getPositions().getFirst());
        maledictus.lookAt(EntityAnchorArgument.Anchor.FEET, pos.add(direction.scale(200)));

        level.addFreshEntity(maledictus);


        float cutsceneStartRadius = 50;
        CatCutUtil.startCutsceneForPlayers((ServerLevel) level, pos, cutsceneStartRadius, 200, createCutsceneData(pos, direction));


        return maledictus;
    }

    private static CutsceneData createCutsceneData(Vec3 pos, Vec3 direction){

        Vec3 initPos = pos.add(0,18,0).add(direction.scale(3));

        CameraPos lastCamera;

        CutsceneData cutsceneData = CutsceneData.create();

        cutsceneData.addScreenEffect(0, FDScreenEffects.SCREEN_COLOR,new ScreenColorData(0,0,0,1f),0,0,10);
        cutsceneData.addCameraPos(new CameraPos(initPos, direction.reverse()));
        cutsceneData.addCameraPos(lastCamera = new CameraPos(initPos.add(direction.scale(5)), direction.reverse()));
        cutsceneData.timeEasing(EasingType.EASE_IN_OUT);
        cutsceneData.time(30);


        Vec3 lookAtBoss1Dir = direction.reverse().add(0,-0.5,0);

        CutsceneData cutsceneData1 = CutsceneData.create();
        Vec3 moveToPos1 = lastCamera.getPos().add(direction.scale(15)).add(0,-17,0);
        cutsceneData1.addCameraPos(lastCamera);
        cutsceneData1.addCameraPos(new CameraPos(moveToPos1, direction.reverse().add(0,2,0)));
        cutsceneData1.addCameraPos(lastCamera = new CameraPos(moveToPos1.add(direction), lookAtBoss1Dir));

        cutsceneData1.timeEasing(EasingType.EASE_IN_OUT);
        cutsceneData1.lookEasing(EasingType.EASE_IN_OUT);
        cutsceneData1.time(35);



        CutsceneData cutsceneData2 = CutsceneData.create().addCameraPos(lastCamera).time(4);

        CutsceneData cutsceneData3 = CutsceneData.create();
        cutsceneData3.addCameraPos(lastCamera);
        cutsceneData3.addCameraPos(lastCamera = new CameraPos(lastCamera.getPos().add(lookAtBoss1Dir.reverse().scale(2)),lookAtBoss1Dir));
        cutsceneData3.time(5);
        cutsceneData3.timeEasing(EasingType.EASE_OUT);

        CutsceneData cutsceneData4 = CutsceneData.create().addCameraPos(lastCamera).time(22);

        CutsceneData cutsceneData5 = CutsceneData.create();
        cutsceneData5.addCameraPos(lastCamera);
        cutsceneData5.addCameraPos(lastCamera = new CameraPos(lastCamera.getPos().add(lookAtBoss1Dir.reverse().scale(2)),lookAtBoss1Dir));
        cutsceneData5.time(5);
        cutsceneData5.timeEasing(EasingType.EASE_OUT);


        CutsceneData cutsceneData6 = CutsceneData.create().addCameraPos(lastCamera).time(20);

        CutsceneData cutsceneData7 = CutsceneData.create();
        cutsceneData7.addCameraPos(lastCamera);
        cutsceneData7.addCameraPos(lastCamera = new CameraPos(lastCamera.getPos().add(lookAtBoss1Dir.scale(6)),direction.reverse()));
        cutsceneData7.time(20);
        cutsceneData7.timeEasing(EasingType.EASE_IN_OUT);


        CutsceneData cutsceneData8 = CutsceneData.create().addCameraPos(lastCamera).time(20);
        cutsceneData8.addScreenEffect(0, FDScreenEffects.SCREEN_COLOR,new ScreenColorData(0,0,0,1f),20,20,20);


        cutsceneData.nextCutscene(cutsceneData1
                .nextCutscene(cutsceneData2
                        .nextCutscene(cutsceneData3
                                .nextCutscene(cutsceneData4
                                        .nextCutscene(cutsceneData5
                                                .nextCutscene(cutsceneData6
                                                        .nextCutscene(cutsceneData7
                                                                .nextCutscene(cutsceneData8))))))));

        return cutsceneData;
    }

    private static ProjectileMovementPath createMovementPath(Vec3 pos, Vec3 direction){
        ProjectileMovementPath path = new ProjectileMovementPath(20,false);

        Vec3 initPos = pos.add(0,16,0);
        Vec3 hOffset = direction.scale(20);
        Vec3 betweenPos = initPos.add(direction.scale(12)).add(0,5,0);
        Vec3 endPos = pos.add(hOffset).add(0,-2,0);

        path.addPos(initPos);
        path.addPos(betweenPos);
        path.addPos(endPos);

        return path;
    }

    public MaledictusCutsceneEntity(EntityType entity, Level world) {
        super(entity, world);
        this.setNoGravity(true);
        this.noPhysics = true;
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide){
            this.serverTick();
        }else{
            this.clientTick();
        }
        this.setNoGravity(true);
        this.noPhysics = true;
    }

    private void serverTick(){

        if (movementPath == null){
            this.remove(RemovalReason.DISCARDED);
            return;
        }

        int startFlyAfterTick = 31;

        int afterStartFlyTick = tickCount - startFlyAfterTick;


        //19 - cool end fly
        //34 heroic pose
        //5 - end jump

        if (afterStartFlyTick >= 0) {


            if (afterStartFlyTick >= 22 && afterStartFlyTick <= 25){
                if (afterStartFlyTick == 22){
                    level().playSound(null,this.getX(),this.getY(),this.getZ(),  ModSounds.EXPLOSION.get(), SoundSource.HOSTILE, 2F, 0.75F + this.getRandom().nextFloat() * 0.1F);
                }
                this.smashEffect(2, afterStartFlyTick - 21, 2.5f);
            }


            if (afterStartFlyTick == 0) {
//                level().playSound(null,this.getX(),this.getY(),this.getZ(),  ModSounds.MALEDICTUS_MACE_SWING.get(), SoundSource.HOSTILE, 3F, 1f);
            }else if (afterStartFlyTick == 10) {

                level().playSound(null,this.getX(),this.getY(),this.getZ(),  ModSounds.MALEDICTUS_BATTLE_CRY.get(), SoundSource.HOSTILE, 3F, 1f);
                this.setAttackState(19);
            } else if (afterStartFlyTick == 20) {
                this.setAttackState(25);
            } else if (afterStartFlyTick == 100) {
                this.setAttackState(0);
            }else if (afterStartFlyTick >= 140){

                Maledictus_Entity maledictus = ModEntities.MALEDICTUS.get().create(level());

                maledictus.setPos(this.getX(),this.getY(),this.getZ());
                maledictus.setHomePos(this.getHomePos());
                maledictus.setTombstonePos(this.getHomePos());
                maledictus.setTombstoneDirection(this.getTombstoneDirection());
                ResourceLocation dimLoc = level().dimension().location();
                maledictus.setDimensionType(dimLoc.toString());

                level().addFreshEntity(maledictus);

                this.setRemoved(RemovalReason.DISCARDED);
            }
        }else{
            if (tickCount == 30){

                level().playSound(null,this.getX(),this.getY(),this.getZ(),  ModSounds.MALEDICTUS_MACE_SWING.get(), SoundSource.HOSTILE, 3F, 1f);
            }
            this.setAttackState(24);
        }

        Vec3 first = movementPath.getPositions().getFirst();
        Vec3 last = movementPath.getPositions().getLast();
        Vec3 b = last.subtract(first).multiply(1,0,1);
        Vec3 lookAt = last.add(b.scale(200));
        this.lookAt(EntityAnchorArgument.Anchor.EYES, lookAt);

        if (tickCount > startFlyAfterTick) {
            movementPath.tick(this);
            if (movementPath.isFinished()) {
                if (!this.getDeltaMovement().equals(Vec3.ZERO)) {
                    Vec3 lastPos = movementPath.getPositions().getLast();
                    this.teleportTo(lastPos.x, lastPos.y, lastPos.z);
                }
                this.setDeltaMovement(Vec3.ZERO);
            }
        }


    }

    private void clientTick(){

    }





    @Override
    protected void registerGoals() {

    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
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
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        this.autoLoad(compound);
    }

    @Override
    protected void AfterDefeatBoss(@Nullable LivingEntity living) {

    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.autoSave(compound);
    }

    @Override
    protected void ReturnToHome() {

    }

    // Code is copied from Maledictus_Entity, because its private
    public void smashEffect(float spreadarc, int distance, float vec) {
        double perpFacing = (double)this.yBodyRot * 0.017453292519943295;
        double facingAngle = perpFacing + 1.5707963267948966;
        int hitY = Mth.floor(this.getBoundingBox().minY - 0.5);
        double spread = Math.PI * (double)spreadarc;
        int arcLen = Mth.ceil((double)distance * spread);

        for(int i = 0; i < arcLen; ++i) {
            double theta = ((double)i / ((double)arcLen - 1.0) - 0.5) * spread + facingAngle;
            double vx = Math.cos(theta);
            double vz = Math.sin(theta);
            double px = this.getX() + vx * (double)distance + (double)vec * Math.cos((double)(this.yBodyRot + 90.0F) * Math.PI / 180.0);
            double pz = this.getZ() + vz * (double)distance + (double)vec * Math.sin((double)(this.yBodyRot + 90.0F) * Math.PI / 180.0);
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

    @EventBusSubscriber(modid = CataclysmCutscenes.MODID)
    public static class Events {

        @SubscribeEvent
        public static void livingDestroyBlockEvent(LivingDestroyBlockEvent event){
            if (event.getEntity() instanceof MaledictusCutsceneEntity){
                event.setCanceled(true);
            };

        }

    }

}

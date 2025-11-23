package com.finderfeed.cataclysm_custscenes.entities.ancient_remnant;

import com.finderfeed.cataclysm_custscenes.CataclysmCutscenes;
import com.finderfeed.fdlib.systems.cutscenes.CutsceneData;
import com.finderfeed.fdlib.util.ProjectileMovementPath;
import com.finderfeed.fdlib.util.math.FDMathUtil;
import com.finderfeed.fdlib.util.rendering.FDEasings;
import com.github.L_Ender.cataclysm.entity.AnimationMonster.BossMonsters.Ignis_Entity;
import com.github.L_Ender.lionfishapi.server.animation.AnimationHandler;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
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

public class IgnisCutsceneEntity extends Ignis_Entity {

    private ProjectileMovementPath movementPath;

    //Altar relative
    public static IgnisCutsceneEntity summon(Level level, Vec3 pos){
        IgnisCutsceneEntity cutsceneEntity = new IgnisCutsceneEntity(CataclysmCutscenes.IGNIS_CUTSCENE_ENTITY.get(), level);

        cutsceneEntity.setPos(pos.add(0,5,0));

        cutsceneEntity.movementPath = createPath(pos);

        cutsceneEntity.lookAt(EntityAnchorArgument.Anchor.FEET, pos.add(0,0,-100));

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
        CutsceneData cutsceneData = CutsceneData.create();
        return cutsceneData;
    }

    public IgnisCutsceneEntity(EntityType<? extends IgnisCutsceneEntity> entity, Level world) {
        super(entity, world);
    }

    @Override
    public void tick() {

        this.setTarget(null);



        super.tick();



        this.tickCutscene();

        this.noPhysics = true;
        this.setNoGravity(true);

    }

    private void tickCutscene(){
        int summoningRitualDuration = 200;
        if (!level().isClientSide){

            this.setIsShieldBreak(true);

            if (tickCount == summoningRitualDuration - 20){
                AnimationHandler.INSTANCE.sendAnimationMessage(this, SMASH_IN_AIR);
            }else if (tickCount == summoningRitualDuration + 15){
                AnimationHandler.INSTANCE.sendAnimationMessage(this, SMASH);
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





            }


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

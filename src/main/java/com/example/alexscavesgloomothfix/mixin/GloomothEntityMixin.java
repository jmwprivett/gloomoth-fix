package com.example.alexscavesgloomothfix.mixin;

import com.github.alexmodguy.alexscaves.server.entity.living.GloomothEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GloomothEntity.class)
public abstract class GloomothEntityMixin extends PathfinderMob {
    protected GloomothEntityMixin(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow(remap = false)
    public abstract void setFlying(boolean flying);

    @ModifyConstant(method = "checkGloomothSpawnRules", constant = @Constant(intValue = 4), remap = false)
    private static int alexscavesGloomothFix$relaxVerticalClearance(int original) {
        return 2;
    }

    @Inject(method = "doInitialPosing", at = @At("HEAD"), cancellable = true, remap = false)
    private void alexscavesGloomothFix$fixInitialPosing(LevelAccessor level, CallbackInfo ci) {
        this.alexscavesGloomothFix$applyInitialPosing(level);
        ci.cancel();
    }

    @Inject(method = "finalizeSpawn", at = @At("RETURN"))
    private void alexscavesGloomothFix$fixChunkGenerationPosing(
        ServerLevelAccessor level,
        DifficultyInstance difficulty,
        MobSpawnType spawnType,
        SpawnGroupData spawnGroupData,
        CompoundTag tag,
        CallbackInfoReturnable<SpawnGroupData> cir
    ) {
        if (spawnType == MobSpawnType.CHUNK_GENERATION) {
            this.alexscavesGloomothFix$applyInitialPosing(level);
        }
    }

    @Unique
    private void alexscavesGloomothFix$applyInitialPosing(LevelAccessor level) {
        BlockPos currentPos = this.blockPosition();
        int ascentLimit = 3 + this.getRandom().nextInt(5);
        int ascended = 0;
        int maxY = level.getMaxBuildHeight() - 1;

        while (ascended < ascentLimit && currentPos.getY() < maxY && level.isEmptyBlock(currentPos.above())) {
            currentPos = currentPos.above();
            ascended++;
        }

        this.setFlying(true);
        this.setPos(currentPos.getX() + 0.5D, currentPos.getY(), currentPos.getZ() + 0.5D);
    }
}

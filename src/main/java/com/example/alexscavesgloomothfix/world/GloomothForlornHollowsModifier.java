package com.example.alexscavesgloomothfix.world;

import com.example.alexscavesgloomothfix.AlexsCavesGloomothFix;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;

public final class GloomothForlornHollowsModifier implements BiomeModifier {
    public static final GloomothForlornHollowsModifier INSTANCE = new GloomothForlornHollowsModifier();
    public static final Codec<GloomothForlornHollowsModifier> CODEC = Codec.unit(() -> INSTANCE);

    private static final ResourceKey<Biome> FORLORN_HOLLOWS = ResourceKey.create(
        Registries.BIOME,
        ResourceLocation.fromNamespaceAndPath("alexscaves", "forlorn_hollows")
    );
    private static final int GLOOMOTH_WEIGHT = 100;
    private static final int GLOOMOTH_MIN_COUNT = 4;
    private static final int GLOOMOTH_MAX_COUNT = 4;
    private static final float CREATURE_SPAWN_PROBABILITY = 0.08F;

    private GloomothForlornHollowsModifier() {
    }

    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase != Phase.ADD || !biome.is(FORLORN_HOLLOWS)) {
            return;
        }

        var mobSpawns = builder.getMobSpawnSettings();
        var gloomothType = ACEntityRegistry.GLOOMOTH.get();

        mobSpawns.getSpawner(MobCategory.AMBIENT).removeIf(spawner -> spawner.type == gloomothType);
        mobSpawns.getSpawner(ACEntityRegistry.CAVE_CREATURE).removeIf(spawner -> spawner.type == gloomothType);
        mobSpawns.addSpawn(
            ACEntityRegistry.CAVE_CREATURE,
            new MobSpawnSettings.SpawnerData(gloomothType, GLOOMOTH_WEIGHT, GLOOMOTH_MIN_COUNT, GLOOMOTH_MAX_COUNT)
        );
        mobSpawns.creatureGenerationProbability(CREATURE_SPAWN_PROBABILITY);
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return AlexsCavesGloomothFix.GLOOMOTH_FORLORN_HOLLOWS_MODIFIER.get();
    }
}

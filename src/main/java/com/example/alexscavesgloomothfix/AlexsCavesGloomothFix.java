package com.example.alexscavesgloomothfix;

import com.example.alexscavesgloomothfix.world.GloomothForlornHollowsModifier;
import com.mojang.serialization.Codec;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod(AlexsCavesGloomothFix.MOD_ID)
public final class AlexsCavesGloomothFix {
    public static final String MOD_ID = "alexscaves_gloomoth_fix";

    public static final DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS =
        DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, MOD_ID);

    public static final RegistryObject<Codec<GloomothForlornHollowsModifier>> GLOOMOTH_FORLORN_HOLLOWS_MODIFIER =
        BIOME_MODIFIER_SERIALIZERS.register("gloomoth_forlorn_hollows", () -> GloomothForlornHollowsModifier.CODEC);

    public AlexsCavesGloomothFix(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();
        BIOME_MODIFIER_SERIALIZERS.register(modEventBus);
    }
}

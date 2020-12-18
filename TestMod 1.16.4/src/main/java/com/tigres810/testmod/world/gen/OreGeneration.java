package com.tigres810.testmod.world.gen;

import java.util.ArrayList;

import com.tigres810.testmod.Test;
import com.tigres810.testmod.util.RegistryHandler;

import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Test.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class OreGeneration {

	private static final ArrayList<ConfiguredFeature<?, ?>> overworldOres = new ArrayList<ConfiguredFeature<?, ?>>();
 
    public static void registerOres(){
    	overworldOres.add(register("flux_ore", Feature.ORE.withConfiguration(new OreFeatureConfig(
    			OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, RegistryHandler.FLUX_ORE_BLOCK.get().getDefaultState(), 8)) //Vein size
    			.range(64).square() //Spawn Height start
    			.func_242731_b(64))); //Amount of ore to generate each chunk
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void gen(BiomeLoadingEvent event) {
    	BiomeGenerationSettingsBuilder generation = event.getGeneration();
    	for(ConfiguredFeature<?, ?> ore : overworldOres){
            if (ore != null) generation.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, ore);
        }
    }
    
    private static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> register(String name, ConfiguredFeature<FC, ?> configuredFeature) {
        return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, Test.MOD_ID + ":" + name, configuredFeature);
    }
}

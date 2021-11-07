package de.dafuqs.spectrum.entity;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.entity.entity.*;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SpectrumEntityTypes {

	public static EntityType<ShootingStarEntity> SHOOTING_STAR;
	public static EntityType<GravityBlockEntity> GRAVITY_BLOCK;
	public static EntityType<InvisibleItemFrameEntity> INVISIBLE_ITEM_FRAME;
	public static EntityType<InvisibleGlowItemFrameEntity> INVISIBLE_GLOW_ITEM_FRAME;
	public static EntityType<? extends ThrownItemEntity> BLOCK_FLOODER_PROJECTILE;

	public static void register() {
		SHOOTING_STAR = register("shooting_star", 240, 20, true, EntityDimensions.changing(0.98F, 0.98F), false, ShootingStarEntity::new);
		GRAVITY_BLOCK = register("gravity_block", 160, 20, true, EntityDimensions.changing(0.98F, 0.98F), true, GravityBlockEntity::new);
		INVISIBLE_ITEM_FRAME = register("invisible_item_frame", 10, 2147483647, false, EntityDimensions.changing(0.5F, 0.5F), false, InvisibleItemFrameEntity::new);
		INVISIBLE_GLOW_ITEM_FRAME = register("invisible_glow_item_frame", 10, 2147483647, false, EntityDimensions.changing(0.5F, 0.5F), false, InvisibleGlowItemFrameEntity::new);
		BLOCK_FLOODER_PROJECTILE = register("block_flooder_projectile", 4, 10, true,EntityDimensions.changing(0.25F, 0.25F), true, BlockFlooderProjectile::new);
	}

	public static <X extends Entity> EntityType<X> register(String name, int trackingDistance, int updateIntervalTicks, boolean alwaysUpdateVelocity, EntityDimensions size, boolean fireImmune, EntityType.EntityFactory<X> factory) {
		FabricEntityTypeBuilder<X> builder = FabricEntityTypeBuilder.create(SpawnGroup.MISC, factory).trackable(trackingDistance, updateIntervalTicks, alwaysUpdateVelocity).dimensions(size);
		if(fireImmune) {
			builder.fireImmune();
		}
		return Registry.register(Registry.ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, name), builder.build());
	}

	public static <X extends Entity> EntityType<X> register(String name, SpawnGroup category, EntityDimensions size, EntityType.EntityFactory<X> factory) {
		return Registry.register(Registry.ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, name), FabricEntityTypeBuilder.create(category, factory).dimensions(size).build());
	}

}
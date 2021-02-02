package de.dafuqs.pigment.blocks.types;

import de.dafuqs.pigment.blocks.PigmentBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

import java.util.Optional;

public class PlayerOnlyGlassBlock extends GlassBlock {

    public PlayerOnlyGlassBlock(Settings settings) {
        super(settings);
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }

    @Deprecated
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if(context instanceof EntityShapeContext) {
            EntityShapeContext entityShapeContext = (EntityShapeContext) context;

            Optional<Entity> entity = entityShapeContext.getEntity();
            if(entity.isPresent() && entity.get() instanceof PlayerEntity) {
                return VoxelShapes.empty();
            }
        }
        return state.getOutlineShape(world, pos);
    }

    @Environment(EnvType.CLIENT)
    public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
        if(stateFrom.isOf(this)) {
            return true;
        }
        if(state.getBlock().equals(PigmentBlocks.AMETHYST_PLAYER_ONLY_GLASS) && stateFrom.getBlock().equals(PigmentBlocks.AMETHYST_GLASS)
                || state.getBlock().equals(PigmentBlocks.CITRINE_PLAYER_ONLY_GLASS) && stateFrom.getBlock().equals(PigmentBlocks.CITRINE_GLASS)
                || state.getBlock().equals(PigmentBlocks.TOPAZ_PLAYER_ONLY_GLASS) && stateFrom.getBlock().equals(PigmentBlocks.TOPAZ_GLASS)
                || state.getBlock().equals(PigmentBlocks.MOONSTONE_PLAYER_ONLY_GLASS) && stateFrom.getBlock().equals(PigmentBlocks.MOONSTONE_GLASS)
                || state.getBlock().equals(PigmentBlocks.ONYX_PLAYER_ONLY_GLASS) && stateFrom.getBlock().equals(PigmentBlocks.ONYX_GLASS)
                || state.getBlock().equals(PigmentBlocks.VANILLA_PLAYER_ONLY_GLASS) && stateFrom.getBlock().equals(Blocks.GLASS)) {
            return true;
        }
        return super.isSideInvisible(state, stateFrom, direction);
    }

    public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
        return true;
    }

}

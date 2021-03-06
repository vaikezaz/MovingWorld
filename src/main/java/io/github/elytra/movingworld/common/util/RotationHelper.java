package io.github.elytra.movingworld.common.util;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3i;

import io.github.elytra.movingworld.MovingWorldMod;
import io.github.elytra.movingworld.api.rotation.IRotationBlock;
import io.github.elytra.movingworld.api.rotation.IRotationProperty;
import io.github.elytra.movingworld.common.chunk.LocatedBlock;

public class RotationHelper {

    public static LocatedBlock rotateBlock(LocatedBlock locatedBlock, boolean ccw) {
        IBlockState blockState = locatedBlock.blockState;
        if (locatedBlock != null && locatedBlock.blockState != null) {
            if (blockState.getBlock() != null && blockState.getBlock() instanceof IRotationBlock) {
                locatedBlock = ((IRotationBlock) blockState.getBlock()).rotate(locatedBlock, ccw);

                if (((IRotationBlock) blockState.getBlock()).fullRotation())
                    return locatedBlock;
            }

            for (IProperty prop : blockState.getProperties().keySet()) {
                if (prop instanceof IRotationProperty) {
                    // Custom rotation property found.
                    MovingWorldMod.LOG.debug("Rotate state in " + blockState.getBlock().getLocalizedName() + " " + blockState.getValue(prop));
                    IRotationProperty rotationProperty = (IRotationProperty) prop;
                    blockState = rotationProperty.rotate(blockState, ccw);
                    MovingWorldMod.LOG.debug("Rotate state out " + blockState.getBlock().getLocalizedName() + " " + blockState.getValue(prop));
                }
            }
        }

        return new LocatedBlock(blockState, locatedBlock.tileEntity, locatedBlock.blockPos, locatedBlock.bPosNoOffset);
    }

    public static int rotateInteger(int integer, int min, int max, boolean ccw) {
        int result = integer;

        if (!ccw) {
            if (result + 1 > max)
                result = min;
            else
                result = result + 1;
        } else {
            if (result - 1 < min)
                result = max;
            else
                result = result - 1;
        }

        return result;
    }

    public static Vec3i getDirectionVec(EnumFacing facing) {
        switch (facing) {
            case DOWN:
                return new Vec3i(0, -1, 0);
            case UP:
                return new Vec3i(0, 1, 0);
            case NORTH:
                return new Vec3i(0, 0, -1);
            case SOUTH:
                return new Vec3i(0, 0, 1);
            case WEST:
                return new Vec3i(-1, 0, 0);
            case EAST:
                return new Vec3i(1, 0, 0);
        }

        return null;
    }

}

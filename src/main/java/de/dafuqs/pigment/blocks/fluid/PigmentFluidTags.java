package de.dafuqs.pigment.blocks.fluid;

import de.dafuqs.pigment.PigmentCommon;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.fluid.Fluid;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class PigmentFluidTags {

    public static Tag<Fluid> LIQUID_CRYSTAL;

    private static Tag<Fluid> register(String id) {
        return TagRegistry.fluid(new Identifier(PigmentCommon.MOD_ID, id));
    }

    public static void register() {
        LIQUID_CRYSTAL = register("liquid_crystal");
    }
}

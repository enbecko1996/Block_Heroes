package com.enbecko.objectcreator.restart;

import com.enbecko.objectcreator.BaseCreatorBlock;
import com.enbecko.objectcreator.TEBaseCreatorBlock;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by Niclas on 24.11.2016.
 */
public class HelmetBaseCreatorBlock extends NewBaseCreatorBlock {

    public HelmetBaseCreatorBlock(Material materialIn) {
        super(materialIn);
    }

    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TENewBase();
    }

}

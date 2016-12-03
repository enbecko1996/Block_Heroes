package com.enbecko.objectcreator;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BaseCreatorBlock extends BlockContainer{

    protected BaseCreatorBlock(Material materialIn) {
        super(materialIn);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TEBaseCreatorBlock();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return false;
    }

    @Override
    public boolean isBlockNormalCube(IBlockState blockState) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState blockState) {
        return false;
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        TEBaseCreatorBlock creatorBlock = (TEBaseCreatorBlock) worldIn.getTileEntity(pos);
        System.out.println(creatorBlock+" is Now active: "+creatorBlock.setActive(!creatorBlock.isActive()));
        return true;
    }

    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        TEBaseCreatorBlock creatorBlock = (TEBaseCreatorBlock) world.getTileEntity(pos);
        creatorBlock.setActive(false);
        System.out.println(creatorBlock+" is Now active: "+creatorBlock.isActive()+" && dead ");
        return super.removedByPlayer(state, world, pos, player, willHarvest);
    }
}

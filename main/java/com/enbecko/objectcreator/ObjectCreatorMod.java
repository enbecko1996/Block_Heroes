package com.enbecko.objectcreator;

import com.enbecko.objectcreator.restart.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.ojalgo.array.Array1D;
import org.ojalgo.array.BasicArray;
import org.ojalgo.array.PrimitiveArray;
import org.ojalgo.matrix.store.PrimitiveDenseStore;

@Mod(modid = ObjectCreatorMod.MODID, version = ObjectCreatorMod.VERSION)
public class ObjectCreatorMod
{
    public static final String MODID = "objectcreator";
    public static final String VERSION = "1.0";
    public static Block helmet_baseCreator;
    public static Block newBlock;
    public static int colorVariety = 255;
    public static final ResourceLocation WHITE = new ResourceLocation("objectcreator:basis/white.png");

    @SidedProxy(clientSide="com.enbecko.objectcreator.ClientProxy", serverSide="com.enbecko.objectcreator.CommonProxy")
    public static CommonProxy proxy;

    public static CreativeTabs createdObjCreativeTab = new CreativeTabs("Block Heroes") {
        @Override
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem(){
            ItemStack iStack = new ItemStack(Blocks.WOOL);
            return iStack.getItem();
        }
    };

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.registerThings();

        helmet_baseCreator = new HelmetBaseBlock(Material.GROUND).setCreativeTab(createdObjCreativeTab).setUnlocalizedName("helmet creator");
        newBlock = new HelmetBaseCreatorBlock(Material.GROUND).setCreativeTab(createdObjCreativeTab).setUnlocalizedName("new creator");
        GameRegistry.registerBlock(helmet_baseCreator, "helmet_base_creator");
        GameRegistry.registerBlock(newBlock, "new_base_creator");
        GameRegistry.registerTileEntity(TEBaseCreatorBlock.class, "helmet_base_creator");
        GameRegistry.registerTileEntity(TENewBase.class, "new_base_creator");
    }

    public static void main(String[] args) {
       /** final PrimitiveDenseStore onPointE = PrimitiveDenseStore.FACTORY.columns(new double[] {0, 0, 0});
        final PrimitiveDenseStore e1 = PrimitiveDenseStore.FACTORY.columns(new double[] {1, 0, 0});
        final PrimitiveDenseStore e2 = PrimitiveDenseStore.FACTORY.columns(new double[] {0, 1, 0});

        final PrimitiveDenseStore onPointL = PrimitiveDenseStore.FACTORY.columns(new double[] {0.5, .1, 1});
        final PrimitiveDenseStore l1 = PrimitiveDenseStore.FACTORY.columns(new double[] {0, 0, -3});

        Face3D face3D = new Face3D(onPointE, e1, e2, false);
        RayTrace3D rayTrace3D = new RayTrace3D(onPointL, l1, true);
        BasicLogger.debug(face3D.checkIfCrosses(rayTrace3D));*/
       Bone bone = new Bone(200, PrimitiveArray.wrap(new double[]{2, 1, 5, 1}));
        bone.scale(2, 1, 1);
        bone.getRayTraceResult(100, new RayTrace3D(PrimitiveDenseStore.FACTORY.makeEye(4, 1), PrimitiveDenseStore.FACTORY.makeEye(4, 1), true));
    }
}

package com.enbecko.objectcreator;

import com.enbecko.objectcreator.BlockSetModes.BlockSetMode;
import com.enbecko.objectcreator.core.*;
import com.google.gson.Gson;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Niclas on 06.11.2016.
 */
public class TEBaseCreatorBlock extends TileEntity implements ITickable, IMouseEventListener, IKeyListener {
    @Nonnull public final LocalCoords coordSystem;
    private long tick;
    private final vec3.Double lookVec = new vec3.Double(), eyePos = new vec3.Double();
    public CreatorBlockRayTraceResult rayTraceResult;
    private final BlockSetModes BLOCKSETMODES;
    @Nonnull private BlockSetMode theBlockSetMode;
    private final List<CreatorBlock> probableRayTraceResults = new ArrayList<CreatorBlock>();
    private boolean isActive, init;

    public TEBaseCreatorBlock() {
        this.coordSystem = new LocalCoords(vec3.vecPrec.BYTE);
        this.BLOCKSETMODES = new BlockSetModes();
        this.theBlockSetMode = BLOCKSETMODES.getSetMode(BlockSetModes.STRAIGHT_LINE);
        this.testStuff();
    }

    public boolean setActive(boolean active) {
        if(this.isActive != active) {
            if(active) {
                EventDispatcher.getTheEventDispatcher().addKeyListener(this);
                EventDispatcher.getTheEventDispatcher().addMouseListener(this);
            } else {
                EventDispatcher.getTheEventDispatcher().removeKeyListener(this);
                EventDispatcher.getTheEventDispatcher().removeMouseListener(this);
            }
            this.isActive = active;
        }
        return this.isActive;
    }

    public boolean isActive() {
        return this.isActive;
    }

    public void testStuff() {
        for (int k = 0; k < 1; k++) {
            this.coordSystem.setBlockAt(k, 0, 0, new CreatorBlock(1, 1, 1, vec3.vecPrec.BYTE), false);
            this.coordSystem.setBlockAt(k, 0, 1, new CreatorBlock(1, 1, 1, vec3.vecPrec.BYTE), false);
            this.coordSystem.setBlockAt(k, 1, 0, new CreatorBlock(1, 1, 1, vec3.vecPrec.BYTE), false);
            this.coordSystem.setBlockAt(k, 1, 1, new CreatorBlock(1, 1, 1, vec3.vecPrec.BYTE), false);
            this.coordSystem.setBlockAt(k, 2, 0, new CreatorBlock(1, 1, 1, vec3.vecPrec.BYTE), false);
            this.coordSystem.setBlockAt(k, 2, 1, new CreatorBlock(1, 1, 1, vec3.vecPrec.BYTE), false);
            this.coordSystem.setBlockAt(k, 0, 3, new CreatorBlock(1, 1, 1, vec3.vecPrec.BYTE), false);
            this.coordSystem.setBlockAt(k, 1, 3, new CreatorBlock(1, 1, 1, vec3.vecPrec.BYTE), false);
            this.coordSystem.setBlockAt(k, 0, 2, new CreatorBlock(1, 1, 1, vec3.vecPrec.BYTE), false);
            this.coordSystem.setBlockAt(k, 1, 2, new CreatorBlock(1, 1, 1, vec3.vecPrec.BYTE), false);
            this.coordSystem.setBlockAt(k, 1, 4, new CreatorBlock(1, 1, 1, vec3.vecPrec.BYTE), false);
        }
        this.coordSystem.updateAllFaces();
    }

    @Nonnull
    public BlockSetMode getTheBlockSetMode() {
        return this.theBlockSetMode;
    }

    @SideOnly(Side.CLIENT)
    public void renderCoordinateSystem(Tessellator tessellator) {
        this.coordSystem.renderObj(tessellator);
    }

    public boolean saveToDisk() {
        Gson gson = new Gson();
        String json = gson.toJson(coordSystem);
        System.out.println("json: " + json);
        return true;
    }

    public void init() {
        this.coordSystem.setCenter(this.getPos());
    }

    @Override
    public void update() {
        if(!this.init) {
            this.init = true;
            this.init();
        }
        this.theBlockSetMode.shouldTickDowns(this.coordSystem);
    }

    /**
    public void setBlockAtResult(@Nonnull CreatorBlockRayTraceResult result) {
        int pX = result.casted.x, pY = result.casted.y, pZ = result.casted.z;
        switch (result.side) {
            case X_pos:
                this.coordSystem.setBlockAt(pX + 1, pY, pZ, new CreatorBlock(1,1,1, vec3.vecPrec.BYTE), true);
                break;
            case X_neg:
                this.coordSystem.setBlockAt(pX - 1, pY, pZ, new CreatorBlock(1,1,1, vec3.vecPrec.BYTE), true);
                break;
            case Y_pos:
                this.coordSystem.setBlockAt(pX, pY + 1, pZ, new CreatorBlock(1,1,1, vec3.vecPrec.BYTE), true);
                break;
            case Y_neg:
                this.coordSystem.setBlockAt(pX, pY - 1, pZ, new CreatorBlock(1,1,1, vec3.vecPrec.BYTE), true);
                break;
            case Z_pos:
                this.coordSystem.setBlockAt(pX, pY, pZ + 1, new CreatorBlock(1,1,1, vec3.vecPrec.BYTE), true);
                break;
            case Z_neg:
                this.coordSystem.setBlockAt(pX, pY, pZ - 1, new CreatorBlock(1,1,1, vec3.vecPrec.BYTE), true);
                break;
        }
    }*/

    @Override
    public void onMouseEvent(MouseEvent event) {
        if(event.getButton() == -1) {
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            Vec3d lookVec1 = player.getLook(0);
            lookVec.update(lookVec1.xCoord, lookVec1.yCoord, lookVec1.zCoord);
            Vec3d eyePos1 = player.getPositionEyes(0);
            eyePos.update(eyePos1.xCoord, eyePos1.yCoord, eyePos1.zCoord);

            this.rayTraceResult = this.getRayTraceResult(this.coordSystem, 100, eyePos, lookVec);

            System.out.println(eyePos+" "+lookVec+" "+this.rayTraceResult);
            this.theBlockSetMode.mouseMoved(event, this.rayTraceResult, this.coordSystem);
        } else {
            this.theBlockSetMode.dispatchMouseEvent(event, rayTraceResult, this.coordSystem);
        }
    }

    @Override
    public void onKeyEvent(InputEvent.KeyInputEvent event) {
        this.theBlockSetMode.dispatchKeyEvent(event, this.rayTraceResult, this.coordSystem);
    }

    @Nullable
    @SideOnly(Side.CLIENT)
    public CreatorBlockRayTraceResult getRayTraceResult(@Nonnull LocalCoords coordSystem, double blockReachDistance, @Nonnull vec3.Double eyePos, @Nonnull vec3.Double lookVec)
    {
        this.coordSystem.makeVecToEye(eyePos);
        this.coordSystem.getProbableRayTraceResults(this.probableRayTraceResults, lookVec, blockReachDistance);
        return this.coordSystem.rayTraceBlocks(this.probableRayTraceResults, eyePos, lookVec);
    }

    public static class CreatorBlockRayTraceResult {
        public final RayTraceSide side;
        public final CreatorBlock casted;
        public CreatorBlockRayTraceResult(@Nonnull CreatorBlock casted, @Nonnull RayTraceSide side) {
            this.side = side;
            this.casted = casted;
        }

        public String toString() {
            return "{"+this.casted+", "+this.side+"}";
        }
    }

    public enum RayTraceSide {
        X_pos, X_neg, Y_pos, Y_neg, Z_pos, Z_neg;
    }
}

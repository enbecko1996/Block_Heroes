package com.enbecko.objectcreator.restart;

import com.enbecko.objectcreator.*;
import com.enbecko.objectcreator.core.vec3;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.ojalgo.array.Array1D;
import org.ojalgo.matrix.store.MatrixStore;
import org.ojalgo.matrix.store.PrimitiveDenseStore;

import javax.annotation.Nonnull;

/**
 * Created by Niclas on 24.11.2016.
 */
public class TENewBase extends TileEntity implements ITickable, IMouseEventListener, IKeyListener {
    private final vec3.Double lookVec = new vec3.Double(), eyePos = new vec3.Double();
    public TEBaseCreatorBlock.CreatorBlockRayTraceResult rayTraceResult;
    private final BlockSetModes BLOCKSETMODES;
    @Nonnull
    private BlockSetModes.BlockSetMode theBlockSetMode;

    public Bone bone;
    private boolean isActive;

    public boolean isActive() {
        return this.isActive;
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

    public TENewBase() {
        this.bone = new Bone(100, null);
        this.BLOCKSETMODES = new BlockSetModes();
        this.theBlockSetMode = BLOCKSETMODES.getSetMode(BlockSetModes.STRAIGHT_LINE);
    }

    @Override
    public void tick() {

    }

    @Override
    public void onMouseEvent(MouseEvent event) {
        if(event.getButton() == -1) {
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            Vec3d lookVec1 = player.getLook(0);
            RayTrace3D rayTrace3D = new RayTrace3D(eyePos, lookVec, true);
            this.rayTraceResult = this.bone.getRayTraceResult(100, rayTrace3D);

            System.out.println(eyePos+" "+lookVec+" "+this.rayTraceResult);
           // this.theBlockSetMode.mouseMoved(event, this.rayTraceResult, this.coordSystem);
        } else {
           // this.theBlockSetMode.dispatchMouseEvent(event, rayTraceResult, this.coordSystem);
        }
    }

    @Override
    public void onKeyEvent(InputEvent.KeyInputEvent event) {

    }
}

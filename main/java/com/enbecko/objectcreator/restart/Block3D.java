package com.enbecko.objectcreator.restart;

import com.enbecko.objectcreator.ObjectCreatorMod;
import com.enbecko.objectcreator.core.vec3;
import com.enbecko.objectcreator.core.vec3Int;
import com.google.common.primitives.UnsignedBytes;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.ojalgo.access.Access1D;
import org.ojalgo.matrix.store.MatrixStore;
import org.ojalgo.matrix.store.PrimitiveDenseStore;

/**
 * Created by Niclas on 19.11.2016.
 */
public class Block3D {

    private final vec3Int position;
    private final PrimitiveDenseStore ojAlgoPosition;
    private final int[] color = new int[3];

    public Block3D(Access1D position) {
        this(position, ObjectCreatorMod.colorVariety, ObjectCreatorMod.colorVariety, ObjectCreatorMod.colorVariety);
    }

    public Block3D(Block3D block3D, Access1D position) {
        this(position, block3D.color[0], block3D.color[1], block3D.color[2]);
    }

    public Block3D(Block3D block3D, int x, int y, int z) {
        this(x, y, z, block3D.color[0], block3D.color[1], block3D.color[2]);
    }

    public Block3D(int x, int y, int z) {
        this(x, y, z, ObjectCreatorMod.colorVariety, ObjectCreatorMod.colorVariety, ObjectCreatorMod.colorVariety);
    }

    public Block3D(Access1D position, int red, int green, int blue) {
        this.setColor(red, green, blue);
        this.position = new vec3Int();
        this.ojAlgoPosition = PrimitiveDenseStore.FACTORY.columns(this.position.toFourIntegerArray());
    }

    public Block3D(int x, int y, int z, int red, int green, int blue) {
        this.setColor(red, green, blue);
        this.position = new vec3Int(x, y, z);
        this.ojAlgoPosition = PrimitiveDenseStore.FACTORY.columns(this.position.toFourIntegerArray());
    }

    public void setRed(int red) {
        if(red <= ObjectCreatorMod.colorVariety) {
            if(red >= 0)
                color[0] = red;
            else
                color[0] = 0;
        } else
            color[0] = ObjectCreatorMod.colorVariety;
    }

    public void setGreen(int green) {
        if(green <= ObjectCreatorMod.colorVariety) {
            if(green >= 0)
                color[1] = green;
            else
                color[1] = 0;
        } else
            color[1] = ObjectCreatorMod.colorVariety;
    }

    public void setBlue(int blue) {
        if(blue <= ObjectCreatorMod.colorVariety) {
            if(blue >= 0)
                color[2] = blue;
            else
                color[2] = 0;
        } else
            color[2] = ObjectCreatorMod.colorVariety;
    }

    public void setColor(int red, int green, int blue) {
        this.setRed(red);
        this.setGreen(green);
        this.setBlue(blue);
    }

    public int getRed() {
        return this.color[0];
    }

    public int getGreen() {
        return this.color[1];
    }

    public int getBlue() {
        return this.color[2];
    }


    public void fillFaces(Face3D ... face3Ds) {
        for (Face3D face3D : face3Ds) {
            face3D.updateOnPoint(this.ojAlgoPosition.copy());
        }
    }

    public PrimitiveDenseStore getOjAlgoPosition() {
        return this.ojAlgoPosition.copy();
    }

    public vec3Int getPosition() {
        return this.position;
    }
}

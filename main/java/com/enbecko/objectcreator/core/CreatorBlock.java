package com.enbecko.objectcreator.core;

import java.io.Serializable;

import static com.enbecko.objectcreator.core.LocalCoords.scale;

/**
 * Created by Niclas on 04.11.2016.
 */
public class CreatorBlock <T extends vec3> implements Serializable, IRaycastable {
    public final T pos;
    private int red, green, blue;
    public final vec3.Double vecToEye = new vec3.Double();

    public CreatorBlock(int red, int green, int blue, T pos) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.pos = pos;
    }

    @SuppressWarnings("unchecked")
    public CreatorBlock(int red, int green, int blue, vec3.vecPrec precision) {
        this(red, green, blue, (T) vec3.newVecWithPrecision(precision));
    }

    @Deprecated
    @SuppressWarnings("unchecked")
    public CreatorBlock(int red, int green, int blue, double x, double y, double z, vec3.vecPrec precision) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        pos = (T) vec3.newVecWithPrecision(precision, x, y, z);
    }

    @Deprecated
    @SuppressWarnings("unchecked")
    public CreatorBlock(CreatorBlock creatorBlock, double x, double y, double z, vec3.vecPrec precision) {
        this.red = creatorBlock.getRed();
        this.green = creatorBlock.getGreen();
        this.blue = creatorBlock.getBlue();
        pos = (T) vec3.newVecWithPrecision(precision, x, y, z);
    }

    public CreatorBlock(CreatorBlock creatorBlock, T pos) {
        this.red = creatorBlock.getRed();
        this.green = creatorBlock.getGreen();
        this.blue = creatorBlock.getBlue();
        this.pos = pos;
    }

    public void makeVecToEye(vec3 center, vec3.Double eyePos) {
        synchronized (this.vecToEye) {
            this.vecToEye.update(center.getXD() + (this.pos.getXD() + 0.5D) * scale, center.getYD() + (this.pos.getYD() + 0.5D) * scale, center.getZD() + (this.pos.getZD() + 0.5D) * scale);
            this.vecToEye.subFromThis(eyePos);
        }
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    public String toString() {
        return "{"+this.pos+", {r: "+red+", g: "+green+", b: "+blue+"}}";
    }

}

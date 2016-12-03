package com.enbecko.objectcreator.core;

import javax.annotation.Nonnull;

/**
 * Created by Niclas on 14.11.2016.
 */
public class vec3Int extends vec3.Int {

    private int[] out = new int[4];
    private Integer[] outInte = new Integer[4];

    public vec3Int() {
        super();
    }

    public vec3Int (vec3.Int v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }

    public vec3Int addAndMakeNew(vec3Int other) {
        return new vec3Int(this.x + other.x, this.y + other.y, this.z + other.z);
    }

    public vec3Int negate() {
        return new vec3Int(- this.x, - this.y, - this.z);
    }

    public vec3Int sub(@Nonnull vec3Int other) {
        this.x -= other.x;
        this.y -= other.y;
        this.z -= other.z;
        return this;
    }

    public vec3Int(int x, int y, int z) {
        super(x, y, z);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public vec3Int update(int x, int y, int z) {
        super.update(x, y, z);
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public vec3Int update(double x, double y, double z) {
        this.x = (int) x;
        this.y = (int) y;
        this.z = (int) z;
        return this;
    }

    public vec3Int update(vec3Int v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        return this;
    }

    public int[] toFourIntArray() {
        out[0] = x;
        out[1] = y;
        out[2] = z;
        out[3] = 1;
        return out;
    }

    public Integer[] toFourIntegerArray() {
        outInte[0] = x;
        outInte[1] = y;
        outInte[2] = z;
        outInte[3] = 1;
        return outInte;
    }


    public double dot(@Nonnull vec3Int other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    public String toString() {
        return "{" + this.x + ", " + this.y + ", " + this.z + "}";
    }
}

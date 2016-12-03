package com.enbecko.objectcreator.restart;

/**
 * Created by Niclas on 22.11.2016.
 */
public class Boundaries3D {

    public int xMin, xMax, yMin, yMax, zMin, zMax;

    public Boundaries3D(int xMin, int xMax, int yMin, int yMax, int zMin, int zMax) {
        this.xMax = xMax;
        this.xMin = xMin;
        this.yMax = yMax;
        this.yMin = yMin;
        this.zMax = zMax;
        this.zMin = zMin;
    }

    public String toString() {
        return "{x: ["+xMin+"-"+xMax+"], y: ["+yMin+"-"+yMax+"], z: ["+zMin+"-"+zMax+"]}";
    }
}

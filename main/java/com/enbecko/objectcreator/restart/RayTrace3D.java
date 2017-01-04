package com.enbecko.objectcreator.restart;

import com.enbecko.objectcreator.core.vec3;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Niclas on 19.11.2016.
 */
public class RayTrace3D {

    public vec3.Double getOnPoint() {
        return onPointV;
    }

    public vec3.Double getVec1() {
        return vec1V;
    }

    private final vec3.Double onPointV = new vec3.Double();
    private final vec3.Double vec1V = new vec3.Double();

    public RayTrace3D(vec3.Double onPoint, vec3.Double vec1, boolean isEndless) {
        this.onPointV.update(onPoint);
        this.vec1V.update(vec1);
    }

    public RayTrace3D updateOnPoint(vec3.Double onPoint) {
        this.onPointV.update(onPoint);
        return this;
    }

    public void updateVec(vec3.Double vec1) {
        this.vec1V.update(vec1);
    }

    public RayTrace3D update(vec3.Double onPoint, @Nonnull vec3.Double vec1) {
        this.onPointV.update(onPoint);
        this.vec1V.update(vec1);
        return this;
    }
}

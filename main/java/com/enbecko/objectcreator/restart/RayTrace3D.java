package com.enbecko.objectcreator.restart;

import com.enbecko.objectcreator.core.vec3;
import org.ojalgo.matrix.store.MatrixStore;
import org.ojalgo.matrix.store.PrimitiveDenseStore;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Niclas on 19.11.2016.
 */
public class RayTrace3D {

    public PrimitiveDenseStore getOnPoint() {
        return onPoint;
    }

    public PrimitiveDenseStore getVec1() {
        return vec1;
    }

    public vec3.Double getOnPointVec() {
        return onPointV;
    }

    public vec3.Double getVec1Vec() {
        return vec1V;
    }

    private final PrimitiveDenseStore onPoint = PrimitiveDenseStore.FACTORY.makeZero(4, 1);
    private final PrimitiveDenseStore vec1 = PrimitiveDenseStore.FACTORY.makeZero(4, 1);
    private final vec3.Double onPointV = new vec3.Double();
    private final vec3.Double vec1V = new vec3.Double();

    public RayTrace3D(MatrixStore<Double> onPoint, MatrixStore<Double> vec1, boolean isEndless) {
        this.onPoint.set(3, 0, 1);
        this.vec1.set(3, 0, 1);
        if(onPoint != null)
            this.onPoint.fillMatching(onPoint);
        if(vec1 != null)
            this.vec1.fillMatching(vec1);
        this.onPointV.update(this.onPoint.get(0, 0), this.onPoint.get(1, 0), this.onPoint.get(2, 0));
        this.vec1V.update(this.vec1.get(0, 0), this.vec1.get(1, 0), this.vec1.get(2, 0));
    }

    public RayTrace3D(MatrixStore<Double> onPoint) {
        this(onPoint, PrimitiveDenseStore.FACTORY.makeEye(4, 1), false);
    }

    public RayTrace3D updateOnPoint(@Nonnull MatrixStore<Double> onPoint) {
        this.onPoint.fillMatching(onPoint);
        this.onPointV.update(this.onPoint.get(0, 0), this.onPoint.get(1, 0), this.onPoint.get(2, 0));
        return this;
    }

    public void updateVec(@Nonnull MatrixStore<Double> vec1) {
        this.vec1.fillMatching(vec1);
        this.vec1V.update(this.vec1.get(0, 0), this.vec1.get(1, 0), this.vec1.get(2, 0));
    }

    public RayTrace3D update(@Nonnull MatrixStore<Double> onPoint, @Nonnull MatrixStore<Double> vec1) {
        this.onPoint.fillMatching(onPoint);
        this.vec1.fillMatching(vec1);
        this.onPointV.update(this.onPoint.get(0, 0), this.onPoint.get(1, 0), this.onPoint.get(2, 0));
        this.vec1V.update(this.vec1.get(0, 0), this.vec1.get(1, 0), this.vec1.get(2, 0));
        return this;
    }
}

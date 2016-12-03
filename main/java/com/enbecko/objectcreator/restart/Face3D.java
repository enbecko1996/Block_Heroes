package com.enbecko.objectcreator.restart;

import org.ojalgo.matrix.decomposition.LU;
import org.ojalgo.matrix.store.MatrixStore;
import org.ojalgo.matrix.store.PrimitiveDenseStore;
import org.ojalgo.netio.BasicLogger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Niclas on 19.11.2016.
 */
public class Face3D {

    private final PrimitiveDenseStore onPoint = PrimitiveDenseStore.FACTORY.makeZero(4, 1);
    private final PrimitiveDenseStore vec1 = PrimitiveDenseStore.FACTORY.makeZero(3, 1);
    private final PrimitiveDenseStore vec2 = PrimitiveDenseStore.FACTORY.makeZero(3, 1);
    private final LU<Double> LUmaker = LU.PRIMITIVE.make();
    PrimitiveDenseStore copyVec1 = PrimitiveDenseStore.FACTORY.makeZero(3, 1);
    PrimitiveDenseStore copyVec2 = PrimitiveDenseStore.FACTORY.makeZero(3, 1);
    PrimitiveDenseStore copyVec3 = PrimitiveDenseStore.FACTORY.makeZero(3, 1);
    PrimitiveDenseStore copyOnThis = PrimitiveDenseStore.FACTORY.makeZero(4, 1);
    PrimitiveDenseStore copyOnLine = PrimitiveDenseStore.FACTORY.makeZero(4, 1);
    private boolean isEndless;

    public Face3D(MatrixStore<Double> onPoint, MatrixStore<Double> vec1, MatrixStore<Double> vec2, boolean isEndless) {
        this.isEndless = isEndless;
        this.onPoint.set(3, 0, 1);
        if(onPoint != null)
            this.onPoint.fillMatching(onPoint);
        if(vec1 != null)
            this.vec1.fillMatching(vec1);
        if(vec2 != null)
            this.vec2.fillMatching(vec2);
    }

    public Face3D() {
    }


    public Face3D(MatrixStore<Double> onPoint) {
        this.onPoint.set(3, 0, 1);
        if(onPoint != null)
            this.onPoint.fillMatching(onPoint);
    }

    public Face3D updateOnPoint(@Nonnull MatrixStore<Double> onPoint) {
        this.onPoint.fillMatching(onPoint);
        return this;
    }

    public Face3D updateVecs(@Nullable MatrixStore<Double> vec1, @Nullable MatrixStore<Double> vec2) {
        if(vec1 != null)
            this.vec1.fillMatching(vec1);
        if(vec2 != null)
            this.vec2.fillMatching(vec2);
        return this;
    }

    public Face3D update(@Nonnull MatrixStore<Double> onPoint, @Nullable MatrixStore<Double> vec1, @Nullable MatrixStore<Double> vec2) {
        this.onPoint.fillMatching(onPoint);
        if(vec1 != null)
            this.vec1.fillMatching(vec1);
        if(vec2 != null)
            this.vec2.fillMatching(vec2);
        return this;
    }

    @Nullable
    public MatrixStore<Double> checkIfCrosses(RayTrace3D rayTrace3D) {
        copyVec1.fillMatching(this.vec1);
        copyVec2.fillMatching(this.vec2);
        copyVec3.fillMatching(rayTrace3D.getVec1());
        copyOnThis.fillMatching(this.onPoint);
        copyOnLine.fillMatching(rayTrace3D.getOnPoint());

        final MatrixStore<Double> matrix = PrimitiveDenseStore.FACTORY.columns(copyVec1, copyVec2, copyVec3.negate());
        final MatrixStore<Double> result = PrimitiveDenseStore.FACTORY.columns(copyOnLine.subtract(copyOnThis));
        BasicLogger.debug(matrix);
        LUmaker.decompose(matrix);
        final MatrixStore<Double> tmp = LUmaker.getSolution(result);

        if((!this.isEndless && tmp.get(0) >= 0 && tmp.get(0) <= 1 && tmp.get(1) >= 0 && tmp.get(1) <= 1) || isEndless) {
            copyVec1.fillMatching(copyVec1.multiply(tmp.get(0)));
            copyVec2.fillMatching(copyVec2.multiply(tmp.get(1)));

            return PrimitiveDenseStore.FACTORY.columns(copyOnThis.add(copyVec1).add(copyVec2));
        }
        return null;
    }
}

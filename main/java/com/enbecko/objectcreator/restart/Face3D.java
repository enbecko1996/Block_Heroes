package com.enbecko.objectcreator.restart;

import com.enbecko.objectcreator.core.vec3;
import org.ojalgo.matrix.decomposition.LU;
import org.ojalgo.matrix.store.MatrixStore;
import org.ojalgo.matrix.store.vec3.Double;
import org.ojalgo.netio.BasicLogger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Niclas on 19.11.2016.
 */
public class Face3D {

    private final vec3.Double onPoint = new vec3.Double();
    private final vec3.Double vec1 = new vec3.Double();
    private final vec3.Double vec2 = new vec3.Double();
   // private final LU<Double> LUmaker = LU.PRIMITIVE.make();
    vec3.Double copyVec1 = new vec3.Double();
    vec3.Double copyVec2 = new vec3.Double();
    vec3.Double copyVec3 = new vec3.Double();
    vec3.Double copyOnThis = new vec3.Double();
    vec3.Double copyOnLine = new vec3.Double();
    private boolean isEndless;

    public Face3D(vec3.Double onPoint, vec3.Double vec1, vec3.Double vec2, boolean isEndless) {
        this.isEndless = isEndless;
        if(onPoint != null)
            this.onPoint.update(onPoint);
        if(vec1 != null)
            this.vec1.update(vec1);
        if(vec2 != null)
            this.vec2.update(vec2);
    }

    public Face3D() {
    }


    public Face3D(vec3.Double onPoint) {
        if(onPoint != null)
            this.onPoint.update(onPoint);
    }

    public Face3D updateOnPoint(@Nonnull vec3.Double onPoint) {
        this.onPoint.update(onPoint);
        return this;
    }

    public Face3D updateVecs(@Nullable vec3.Double vec1, @Nullable vec3.Double vec2) {
        if(vec1 != null)
            this.vec1.update(vec1);
        if(vec2 != null)
            this.vec2.update(vec2);
        return this;
    }

    public Face3D update(@Nonnull vec3.Double onPoint, @Nullable vec3.Double vec1, @Nullable vec3.Double vec2) {
        this.onPoint.update(onPoint);
        if(vec1 != null)
            this.vec1.update(vec1);
        if(vec2 != null)
            this.vec2.update(vec2);
        return this;
    }

    @Nullable
    public vec3.Double checkIfCrosses(RayTrace3D rayTrace3D) {
        copyVec1.update(this.vec1);
        copyVec2.update(this.vec2);
        copyVec3.update(rayTrace3D.getVec1());
        copyOnThis.update(this.onPoint);
        copyOnLine.update(rayTrace3D.getOnPoint());

        final vec3.Double matrix = vec3.Double.FACTORY.columns(copyVec1, copyVec2, copyVec3.negate());
        final vec3.Double result = vec3.Double.FACTORY.columns(copyOnLine.subtract(copyOnThis));
        BasicLogger.debug(matrix);
        LUmaker.decompose(matrix);
        final vec3.Double tmp = LUmaker.getSolution(result);

        if((!this.isEndless && tmp.get(0) >= 0 && tmp.get(0) <= 1 && tmp.get(1) >= 0 && tmp.get(1) <= 1) || isEndless) {
            copyVec1.update(copyVec1.multiply(tmp.get(0)));
            copyVec2.update(copyVec2.multiply(tmp.get(1)));

            return vec3.Double.FACTORY.columns(copyOnThis.add(copyVec1).add(copyVec2));
        }
        return null;
    }
}

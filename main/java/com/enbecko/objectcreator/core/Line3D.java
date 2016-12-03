package com.enbecko.objectcreator.core;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Niclas on 15.11.2016.
 */
public class Line3D <T extends vec3, T1 extends vec3>{

    private final T start;
    private final T1 prop;
    private final vec3.Double normalProp;
    private boolean isEndless = true;
    private double length;
    private final vec3.vecPrec startPrecision, propPrecision;

    @SuppressWarnings("unchecked")
    public Line3D(@Nonnull T start, @Nonnull T1 prop) {
        this.start = start;
        this.prop = prop;
        this.length = prop.length();
        this.startPrecision = this.start.getPrecision();
        this.propPrecision = this.prop.getPrecision();
        this.normalProp = prop.newNormalizedD();
    }

    @SuppressWarnings("unchecked")
    public Line3D(@Nonnull T start, T1 prop, double length) {
        this.start = start;
        this.length = length;
        this.normalProp = prop.newNormalizedD();
        this.startPrecision = this.start.getPrecision();
        this.propPrecision = prop.getPrecision();
        this.prop = (T1) normalProp.mulAndMakeNew(this.propPrecision, length);
    }

    public void setEndless(boolean endless) {
        this.isEndless = endless;
    }

    public void setStart(@Nonnull T start) {
        this.start.update(start.getXD(), start.getYD(), start.getZD());
    }

    public void setProp(@Nonnull T1 prop) {
        this.normalProp.update(prop.getXD(), prop.getYD(), prop.getZD());
        this.normalProp.normalize();
        this.prop.update(prop.getXD(), prop.getYD(), prop.getZD());
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public T1 endPoint() {
        if(!this.isEndless)
            return (T1) start.addAndMakeNew(this.propPrecision, prop);
        else
            return null;
    }

    public boolean setLength(double length) {
        this.length = length;
        return !this.isEndless;
    }

    public double getLength() {
        if(!this.isEndless)
            return this.length;
        else
            return Double.POSITIVE_INFINITY;
    }

    public vec3.vecPrec getStartPrecision() {
        return this.startPrecision;
    }

    public vec3.vecPrec getPropPrecision() {
        return this.propPrecision;
    }

    public boolean isEndless() {
        return this.isEndless;
    }
}

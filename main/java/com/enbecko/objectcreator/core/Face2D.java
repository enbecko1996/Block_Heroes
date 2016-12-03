package com.enbecko.objectcreator.core;

import com.sun.istack.internal.Nullable;

/**
 * Created by Niclas on 09.11.2016.
 */
public class Face2D <T extends vec3> {
    private double width, height, size, coordinate;
    final vec3.vecPrec precision;
    public final T LOW_LEFT, UP_RIGHT;
    protected final LocalCoordsRenderableFaces2DInOneCoord.CreatorBlockFaceDirection direction;
    private InterceptableFace3D rayTraceFace;

    @SuppressWarnings("unchecked")
    public Face2D(LocalCoordsRenderableFaces2DInOneCoord.CreatorBlockFaceDirection direction, vec3.vecPrec precision) {
        this.direction = direction;
        this.precision = precision;
        this.LOW_LEFT = (T) vec3.newVecWithPrecision(precision);
        this.UP_RIGHT = (T) vec3.newVecWithPrecision(precision);
    }

    @SuppressWarnings("unchecked")
    public Face2D(T LOW_LEFT, T UP_RIGHT, LocalCoordsRenderableFaces2DInOneCoord.CreatorBlockFaceDirection direction) {
        this.direction = direction;
        this.precision = LOW_LEFT.getPrecision();
        this.LOW_LEFT = (T) vec3.newVecWithPrecision(precision);
        this.UP_RIGHT = (T) vec3.newVecWithPrecision(precision);
        this.updateBounds(LOW_LEFT, UP_RIGHT);
    }

    @SuppressWarnings("unchecked")
    public Face2D(Face2D face2D, LocalCoordsRenderableFaces2DInOneCoord.CreatorBlockFaceDirection direction){
        this((T)face2D.LOW_LEFT, (T)face2D.UP_RIGHT, direction);
    }

    @Deprecated
    public Face2D(double left, double bot, double right, double top, LocalCoordsRenderableFaces2DInOneCoord.CreatorBlockFaceDirection direction, vec3.vecPrec precision)
    {
        this(direction, precision);
        this.updateBounds(left, bot, right, top);
    }

    public Face2D setRayTracable() {
        if(this.rayTraceFace == null) {
            switch (this.direction) {
                case X:
                    this.rayTraceFace = new InterceptableFace3D(this.coordinate, this.LOW_LEFT.getYD(), this.LOW_LEFT.getZD(), this.coordinate, this.UP_RIGHT.getYD(), this.LOW_LEFT.getZD(), this.coordinate, this.UP_RIGHT.getYD(), this.UP_RIGHT.getZD(), vec3.vecPrec.DOUBLE);
                    break;
                case Y:
                    this.rayTraceFace = new InterceptableFace3D(this.LOW_LEFT.getXD(), this.coordinate, this.LOW_LEFT.getZD(), this.LOW_LEFT.getXD(), this.coordinate, this.UP_RIGHT.getZD(), this.UP_RIGHT.getXD(), this.coordinate, this.UP_RIGHT.getZD(), vec3.vecPrec.DOUBLE);
                    break;
                case Z:
                    this.rayTraceFace = new InterceptableFace3D(this.LOW_LEFT.getXD(), this.LOW_LEFT.getYD(), this.coordinate, this.LOW_LEFT.getXD(), this.UP_RIGHT.getYD(), this.coordinate, this.UP_RIGHT.getXD(), this.UP_RIGHT.getYD(), this.coordinate, vec3.vecPrec.DOUBLE);
                    break;
            }
        }
        return this;
    }

    public double getLocalLeft() {
        switch (direction) {
            case X:
                return LOW_LEFT.getZD();
            default:
                return LOW_LEFT.getXD();
        }
    }

    public double getLocalBot() {
        switch (direction) {
            case Y:
                return LOW_LEFT.getZD();
            default:
                return LOW_LEFT.getYD();
        }
    }

    public double getLocalRight() {
        switch (direction) {
            case X:
                return UP_RIGHT.getZD();
            default:
                return UP_RIGHT.getXD();
        }
    }

    public double getLocalTop() {
        switch (direction) {
            case Y:
                return UP_RIGHT.getZD();
            default:
                return UP_RIGHT.getYD();
        }
    }

    public void expandHor(double exp) {
        switch (direction) {
            case X:
                if(exp > 0)
                    UP_RIGHT.setZD(UP_RIGHT.getZD() + exp);
                else
                    LOW_LEFT.setZD(LOW_LEFT.getZD() + exp);
                break;
            default:
                if(exp > 0)
                    UP_RIGHT.setXD(UP_RIGHT.getXD() + exp);
                else
                    LOW_LEFT.setXD(LOW_LEFT.getXD() + exp);
                break;
        }
        this.width += Math.abs(exp);
        this.size = this.width * this.height;
    }

    public void expandVer(double exp) {
        switch (direction) {
            case Y:
                if(exp > 0)
                    UP_RIGHT.setZD(UP_RIGHT.getZD() + exp);
                else
                    LOW_LEFT.setZD(LOW_LEFT.getZD() + exp);
                break;
            default:
                if(exp > 0)
                    UP_RIGHT.setYD(UP_RIGHT.getYD() + exp);
                else
                    LOW_LEFT.setYD(LOW_LEFT.getYD() + exp);
                break;
        }
        this.height += Math.abs(exp);
        this.size = this.width * this.height;
    }

    public void updateBounds(T LOW_LEFT, T UP_RIGHT) {
        this.LOW_LEFT.update(LOW_LEFT);
        this.UP_RIGHT.update(UP_RIGHT);
        switch (direction) {
            case X:
                this.width = this.UP_RIGHT.getZD() - this.LOW_LEFT.getZD();
                this.height = this.UP_RIGHT.getYD() - this.LOW_LEFT.getYD();
                this.coordinate = this.UP_RIGHT.getXD();
                if(this.LOW_LEFT.getXD() != this.UP_RIGHT.getXD())
                    throw new RuntimeException(this.LOW_LEFT+" "+this.UP_RIGHT+" can't create a 2D face in X");
                break;
            case Y:
                this.width = this.UP_RIGHT.getXD() - this.LOW_LEFT.getXD();
                this.height = this.UP_RIGHT.getZD() - this.LOW_LEFT.getZD();
                this.coordinate = this.UP_RIGHT.getYD();
                if(this.LOW_LEFT.getYD() != this.UP_RIGHT.getYD())
                    throw new RuntimeException(this.LOW_LEFT+" "+this.UP_RIGHT+" can't create a 2D face in Y");
                break;
            case Z:
                this.width = this.UP_RIGHT.getXD() - this.LOW_LEFT.getXD();
                this.height = this.UP_RIGHT.getYD() - this.LOW_LEFT.getYD();
                this.coordinate = this.UP_RIGHT.getZD();
                if(this.LOW_LEFT.getZD() != this.UP_RIGHT.getZD())
                    throw new RuntimeException(this.LOW_LEFT+" "+this.UP_RIGHT+" can't create a 2D face in Z");
                break;
        }
        this.size = this.width * this.height;
    }

    public void updateBounds(double left, double bot, double right, double top) {
        if(left < right && bot < top) {
            switch (this.direction) {
                case X:
                    this.LOW_LEFT.setZD(left);
                    this.LOW_LEFT.setYD(bot);
                    this.UP_RIGHT.setYD(top);
                    this.UP_RIGHT.setZD(right);
                    this.width = this.UP_RIGHT.getZD() - this.LOW_LEFT.getZD();
                    this.height = this.UP_RIGHT.getYD() - this.LOW_LEFT.getYD();
                    if (this.rayTraceFace != null)
                        this.rayTraceFace.updateBounds(this.coordinate, this.LOW_LEFT.getYD(), this.LOW_LEFT.getZD(), this.coordinate, this.UP_RIGHT.getYD(), this.LOW_LEFT.getZD(), this.coordinate, this.UP_RIGHT.getYD(), this.UP_RIGHT.getZD());
                    break;
                case Y:
                    this.LOW_LEFT.setXD(left);
                    this.LOW_LEFT.setZD(bot);
                    this.UP_RIGHT.setZD(top);
                    this.UP_RIGHT.setXD(right);
                    this.width = this.UP_RIGHT.getXD() - this.LOW_LEFT.getXD();
                    this.height = this.UP_RIGHT.getZD() - this.LOW_LEFT.getZD();
                    if (this.rayTraceFace != null)
                        this.rayTraceFace.updateBounds(this.LOW_LEFT.getXD(), this.coordinate, this.LOW_LEFT.getZD(), this.LOW_LEFT.getXD(), this.coordinate, this.UP_RIGHT.getZD(), this.UP_RIGHT.getXD(), this.coordinate, this.UP_RIGHT.getZD());
                    break;
                case Z:
                    this.LOW_LEFT.setXD(left);
                    this.LOW_LEFT.setYD(bot);
                    this.UP_RIGHT.setYD(top);
                    this.UP_RIGHT.setXD(right);
                    this.width = this.UP_RIGHT.getXD() - this.LOW_LEFT.getXD();
                    this.height = this.UP_RIGHT.getYD() - this.LOW_LEFT.getYD();
                    if (this.rayTraceFace != null)
                        this.rayTraceFace.updateBounds(this.LOW_LEFT.getXD(), this.LOW_LEFT.getYD(), this.coordinate, this.LOW_LEFT.getXD(), this.UP_RIGHT.getYD(), this.coordinate, this.UP_RIGHT.getXD(), this.UP_RIGHT.getYD(), this.coordinate);
                    break;
            }
            this.size = this.width * this.height;
        }
    }

    public void updateCoordinate(double coordinate) {
        this.coordinate = coordinate;
        switch (this.direction) {
            case X:
                this.LOW_LEFT.setXD(coordinate);
                this.UP_RIGHT.setXD(coordinate);
                if(this.rayTraceFace != null)
                    this.rayTraceFace.updateBounds(this.coordinate, this.LOW_LEFT.getYD(), this.LOW_LEFT.getZD(), this.coordinate, this.UP_RIGHT.getYD(), this.LOW_LEFT.getZD(), this.coordinate, this.UP_RIGHT.getYD(), this.UP_RIGHT.getZD());
                break;
            case Y:
                this.LOW_LEFT.setYD(coordinate);
                this.UP_RIGHT.setYD(coordinate);
                if(this.rayTraceFace != null)
                    this.rayTraceFace.updateBounds(this.LOW_LEFT.getXD(), this.coordinate, this.LOW_LEFT.getZD(), this.LOW_LEFT.getXD(), this.coordinate, this.UP_RIGHT.getZD(), this.UP_RIGHT.getXD(), this.coordinate, this.UP_RIGHT.getZD());
                break;
            case Z:
                this.LOW_LEFT.setZD(coordinate);
                this.UP_RIGHT.setZD(coordinate);
                if(this.rayTraceFace != null)
                    this.rayTraceFace.updateBounds(this.LOW_LEFT.getXD(), this.LOW_LEFT.getYD(), this.coordinate, this.LOW_LEFT.getXD(), this.UP_RIGHT.getYD(), this.coordinate, this.UP_RIGHT.getXD(), this.UP_RIGHT.getYD(), this.coordinate);
                break;
        }
    }

    @Nullable
    public vec3.Double rayTrace(vec3.Double startPoint, vec3.Double lookVec){
        if(this.rayTraceFace != null)
            return this.rayTraceFace.checkIfCrosses(this.direction, startPoint, lookVec);
        return null;
    }

    public double getWidth() {
        return this.getLocalRight() - this.getLocalLeft();
    }

    public double getHeight() {
        return this.getLocalTop() - this.getLocalBot();
    }

    public double getSize() {
        return this.getHeight() * this.getWidth();
    }

    public double getCoordinate() {
        return this.coordinate;
    }
}

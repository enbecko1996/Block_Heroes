package com.enbecko.objectcreator.core;

public class InterceptableFace3D <T extends vec3> {

    private final T p1, p2, p3;
    private double d;
    private boolean isEndless = false;
    private T oneToTwo, twoToThree;
    vec3.Double normalVec, tmp = new vec3.Double(0d,0d,0d);
    private boolean groundFace;

    @SuppressWarnings("unchecked")
    @Deprecated
    public InterceptableFace3D(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, vec3.vecPrec precision) {
        this.p1 = (T) vec3.newVecWithPrecision(precision);
        this.p2 = (T) vec3.newVecWithPrecision(precision);
        this.p3 = (T) vec3.newVecWithPrecision(precision);
        this.updateBounds((T) vec3.newVecWithPrecision(precision, x1, y1, z1), (T) vec3.newVecWithPrecision(precision, x2, y2, z2), (T) vec3.newVecWithPrecision(precision, x3, y3, z3));
    }

    @SuppressWarnings("unchecked")
    public InterceptableFace3D(T p1, T p2, T p3) {
        this.p1 = (T) vec3.newVecWithPrecision(p1.getPrecision());
        this.p2 = (T) vec3.newVecWithPrecision(p2.getPrecision());
        this.p3 = (T) vec3.newVecWithPrecision(p3.getPrecision());
        this.updateBounds(p1, p2, p3);
    }

    @SuppressWarnings("unchecked")
    public InterceptableFace3D(vec3.vecPrec prec) {
        this.p1 = (T) vec3.newVecWithPrecision(prec);
        this.p2 = (T) vec3.newVecWithPrecision(prec);
        this.p3 = (T) vec3.newVecWithPrecision(prec);
        this.updateBounds(p1, p2, p3);
    }

    @SuppressWarnings("unchecked")
    public InterceptableFace3D(vec3 p1, vec3 p2, vec3 p3, vec3.vecPrec prec) {
        this.p1 = (T) vec3.newVecWithPrecision(prec);
        this.p2 = (T) vec3.newVecWithPrecision(prec);
        this.p3 = (T) vec3.newVecWithPrecision(prec);
        this.updateBounds(this.p1, this.p2, this.p3);
    }

    public InterceptableFace3D setEndless(boolean endless) {
        this.isEndless = endless;
        return this;
    }

    @SuppressWarnings("unchecked")
    public void updateBounds(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3) {
        this.p1.update(x1, y1, z1);
        this.p2.update(x2, y2, z2);
        this.p3.update(x3, y3, z3);
        if(oneToTwo != null && twoToThree != null) {
            oneToTwo.update(p2);
            twoToThree.update(p3);
        }else {
            oneToTwo = (T) vec3.newVecWithPrecision(this.p1.getPrecision(), this.p2);
            twoToThree = (T) vec3.newVecWithPrecision(this.p2.getPrecision(), this.p3);
        }
        oneToTwo.subFromThis(this.p1);
        twoToThree.subFromThis(this.p1);

        this.groundFace = true;
        // if((oneToTwo.getXD() <= 0.01F && oneToTwo.y != 0 && oneToTwo.z <= 0.01F) || (twoToThree.x <= 0.01F && twoToThree.y != 0 && twoToThree.z <= 0.01F))
        //    this.groundFace = false;
        if(normalVec != null) {
            normalVec.update(oneToTwo);
        }
        else {
            normalVec = new vec3.Double(oneToTwo);
        }
        normalVec = ((vec3.Double)normalVec.cross(twoToThree)).normalize();
        d = normalVec.x * this.p1.getXD() + normalVec.y * this.p1.getYD() + normalVec.z * this.p1.getZD();
    }

    public InterceptableFace3D moveAlong(vec3 move) {
        this.p1.addToThis(move);
        this.p2.addToThis(move);
        this.p3.addToThis(move);
        return this;
    }

    @SuppressWarnings("unchecked")
    public void updateBounds(vec3.Double p1, vec3.Double p2, vec3.Double p3) {
        this.updateBounds(p1.x, p1.y, p1.z, p2.x, p2.y, p2.z, p3.x, p3.y, p3.z);
    }

    @SuppressWarnings("unchecked")
    public void updateBounds(T p1, T p2, T p3) {
        this.p1.update(p1);
        this.p2.update(p2);
        this.p3.update(p3);
        if(oneToTwo != null && twoToThree != null) {
            oneToTwo.update(p2);
            twoToThree.update(p3);
        }else {
            oneToTwo = (T) vec3.newVecWithPrecision(this.p1.getPrecision(), this.p2);
            twoToThree = (T) vec3.newVecWithPrecision(this.p2.getPrecision(), this.p3);
        }
        oneToTwo.subFromThis(this.p1);
        twoToThree.subFromThis(this.p1);

        this.groundFace = true;
       // if((oneToTwo.getXD() <= 0.01F && oneToTwo.y != 0 && oneToTwo.z <= 0.01F) || (twoToThree.x <= 0.01F && twoToThree.y != 0 && twoToThree.z <= 0.01F))
        //    this.groundFace = false;
        if(normalVec != null) {
            normalVec.update(oneToTwo);
        }
        else {
            normalVec = new vec3.Double(oneToTwo);
        }
        normalVec = ((vec3.Double)normalVec.cross(twoToThree)).normalize();
        d = normalVec.x * this.p1.getXD() + normalVec.y * this.p1.getYD() + normalVec.z * this.p1.getZD();
    }

    public vec3.Double checkIfCrosses(LocalCoordsRenderableFaces2DInOneCoord.CreatorBlockFaceDirection direction, vec3.Double eye, vec3.Double look) {
        double r = 0;
        double pX = this.p1.getXD(), pY = this.p1.getYD(), pZ = this.p1.getZD();
        double tX = this.twoToThree.getXD(), tY = this.twoToThree.getYD(), tZ = this.twoToThree.getZD();
        double oX = this.oneToTwo.getXD(), oY = this.oneToTwo.getYD(), oZ = this.oneToTwo.getZD();
        switch (direction) {
            case X:
                r = (pX - eye.x) / look.x;
                break;
            case Y:
                r = (pY - eye.y) / look.y;
                break;
            case Z:
                r = (pZ - eye.z) / look.z;
        }
        tmp.update(eye.x + r * look.x, eye.y + r * look.y, eye.z + r * look.z);
        double s = (tmp.y - pY - ((tmp.x - pX) / tX) * tY) / (oY + ((oX * tY) / tX));
        if(Double.isNaN(s) || Double.isInfinite(s))
            s = (tmp.z - pZ - ((tmp.y - pY) / tY) * tZ) / (oZ + ((oY * tZ) / tY));
        if(Double.isNaN(s) || Double.isInfinite(s))
            s = (tmp.x - pX - ((tmp.z - pZ) / tZ) * tX) / (oX + ((oZ * tX) / tZ));
        if(Double.isNaN(s) || Double.isInfinite(s))
            s = (tmp.y - pY - ((tmp.z - pZ) / tZ) * tY) / (oY + ((oZ * tY) / tZ));
        if(Double.isNaN(s) || Double.isInfinite(s))
            s = (tmp.x - pX - ((tmp.y - pY) / tY) * tX) / (oX + ((oY * tX) / tY));
        if(Double.isNaN(s) || Double.isInfinite(s))
            s = (tmp.z - pZ - ((tmp.x - pX) / tX) * tZ) / (oZ + ((oX * tZ) / tX));

        double t = (tmp.x - pX - s * oX) / tX;
        if(Double.isNaN(t) || Double.isInfinite(t))
            t = (tmp.y - pY - s * oY) / tY;
        if(Double.isNaN(t) || Double.isInfinite(s))
            t = (tmp.z - pZ - s * oZ) / tZ;

        //float r =  (d - eye.x * normalVec.x - eye.y * normalVec.y - eye.z * normalVec.z) / (look.x * normalVec.x + look.y * normalVec.y + look.z * normalVec.z);
        /**float t = (((eye.x - x2 + r * look.x) / oX) - (((eye.y - y2 + r * look.y) * tX) / (tY * oX))) / (1 - ((oY * tX) / (tY * oX)));
        float s = (eye.y - y2 + r * look.y - t * oY) / tY;
        float t2 = (((eye.x - x2 + r * look.x) / oX) - (((eye.z - z2 + r * look.z) * tX) / (tZ * oX))) / (1 - ((oZ * tX) / (tZ * oX)));
        float s2 = (eye.z - z2 + r * look.z - t2 * oZ) / tZ;
        */
        if(!Double.isInfinite(r) && !Double.isNaN(r)) {
            if (!this.isEndless) {
                if (r > 0 && (t >= 0 && t <= 1 && s >= 0 && s <= 1))
                    return tmp.update(pX + s * oX + t * tX, pY + s * oY + t * tY, pZ + s * oZ + t * tZ);
                else
                    return null;
            } else
                return tmp.update(pX + s * oX + t * tX, pY + s * oY + t * tY, pZ + s * oZ + t * tZ);
        } else
            return null;
        //  float pD = this.distancePointAndThis(eye);
        // float p2D = this.distancePointAndThis(this.tmp.update(eye.x + look.x, eye.y + look.y, eye.z + look.z));
        //  if(pD > p2D)
        //      return tmp;
        //  else
        //    return -tmp;
    }

    public double distancePointAndThis(vec3.Double point) {
        tmp.update(this.p1);
        tmp.subFromThis(point);
        return Math.abs(tmp.x * normalVec.x + tmp.y * normalVec.y + tmp.z * normalVec.z) / normalVec.length();
    }

    public String toString() {
        return "{"+this.groundFace+" : "+this.normalVec+", "+this.p1+", "+this.p2+", "+this.p3+"}";
    }

}
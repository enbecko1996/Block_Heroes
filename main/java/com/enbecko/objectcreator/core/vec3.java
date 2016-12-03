package com.enbecko.objectcreator.core;

import javax.annotation.Nullable;

/**
 * Created by Niclas on 15.11.2016.
 */
public abstract class vec3 <T extends Number>{

    private double w = 1;

    public abstract vec3Gen<T> getGenericVec();

    public abstract T getPreciseX();

    public abstract T getPreciseY();

    public abstract T getPreciseZ();

    public vec3.Double subAndMakeNewD(vec3 other) {
        return new vec3.Double(this).subFromThis(other);
    }

    public abstract double getXD();

    public abstract double getYD();

    public abstract double getZD();

    public double getW() {
        return this.w;
    }

    public static vec3 newVecWithPrecision(vecPrec precision, double x, double y, double z) {
        switch (precision) {
            case BYTE:
                return new vec3.Byte((byte) x, (byte) y, (byte) z);
            case SHORT:
                return new vec3.Short((short) x, (short) y, (short) z);
            case INT:
                return new vec3.Int((int) x, (int) y, (int) z);
            case LONG:
                return new vec3.Long((long) x, (long) y, (long) z);
            case FLOAT:
                return new vec3.Float((float) x, (float) y, (float) z);
            case DOUBLE:
                return new vec3.Double(x, y, z);
        }
        throw new RuntimeException(precision+" cannot create a new vec");
    }

    public static vec3 newVecWithPrecision(vecPrec precision) {
        return newVecWithPrecision(precision, 0, 0, 0);
    }

    public static vec3 newVecWithPrecision(vecPrec precision, vec3 other) {
        return newVecWithPrecision(precision, other.getXD(), other.getYD(), other.getZD());
    }

    public vec3.Float newNormalizedF(){
        return new vec3.Float((float) (this.getXD()), (float) (this.getYD()), (float) (this.getZD())).normalize();
    }

    @Deprecated
    public vec3.Float addToNewF(vec3 other){
        return new vec3.Float((float) (this.getXD() + other.getXD()),(float) (this.getYD() + other.getYD()),(float) (this.getZD() + other.getZD()));
    }

    @Deprecated
    public vec3.Float subToNewF(vec3 other){
        return new vec3.Float((float) (this.getXD() - other.getXD()), (float) (this.getYD() - other.getYD()), (float) (this.getZD() - other.getZD()));
    }

    @Deprecated
    public vec3.Float mulToNewF(double scale){
        return new vec3.Float((float) (this.getXD() * scale), (float) (this.getYD() * scale), (float) (this.getZD() * scale));
    }

    @Deprecated
    public vec3.Float divToNewF(double scale){
        return new vec3.Float((float) (this.getXD() / scale), (float) (this.getYD() / scale), (float) (this.getZD() / scale));
    }

    public vec3.Double newNormalizedD(){
        return new vec3.Double(this.getXD(), this.getYD(), this.getZD()).normalize();
    }

    @Deprecated
    public vec3.Double addToNewD(vec3 other){
        return new vec3.Double(this.getXD() + other.getXD(), this.getYD() + other.getYD(), this.getZD() + other.getZD());
    }

    @Deprecated
    public vec3.Double subToNewD(vec3 other){
        return new vec3.Double(this.getXD() - other.getXD(), this.getYD() - other.getYD(), this.getZD() - other.getZD());
    }

    @Deprecated
    public vec3.Double mulToNewD(double scale){
        return new vec3.Double(this.getXD() * scale, this.getYD() * scale, this.getZD() * scale);
    }

    @Deprecated
    public vec3.Double divToNewD(double scale){
        return new vec3.Double(this.getXD() / scale, this.getYD() / scale, this.getZD() / scale);
    }

    public double length() {
        return MathHelper.getVecLength(this);
    }

    public enum vecPrec {
        BYTE, SHORT, INT, LONG, FLOAT, DOUBLE;
    }

    public abstract vec3 mulToThis(double length);

    public abstract vec3 divToThis(double length);

    public abstract vec3 subFromThis(vec3 other);

    public abstract vec3 addToThis(vec3 other);

    public vec3 subFromThis(double x, double y, double z) {
        this.setXD(this.getXD() - x);
        this.setYD(this.getYD() - y);
        this.setZD(this.getZD() - z);
        return this;
    }

    public vec3 addToThis(double x, double y, double z) {
        this.setXD(this.getXD() + x);
        this.setYD(this.getYD() + y);
        this.setZD(this.getZD() + z);
        return this;
    }

    public vec3 subFromThisAndInsertInto(vec3 toSub, vec3 insert) {
        insert.update(this);
        insert.subFromThis(toSub);
        return this;
    }

    public vec3 addToThisAndInsertInto(vec3 toAdd, vec3 insert) {
        insert.update(this);
        insert.addToThis(toAdd);
        return this;
    }

    public vec3 subFromThisAndInsertInto(double x, double y, double z, vec3 insert) {
        insert.update(this);
        insert.subFromThis(x, y, z);
        return this;
    }

    public vec3 addToThisAndInsertInto(double x, double y, double z, vec3 insert) {
        insert.update(this);
        insert.addToThis(x, y, z);
        return this;
    }

    public abstract vec3 update(double x, double y, double z);

    public vec3 update(vec3 other) {
        return this.update(other.getXD(), other.getYD(), other.getZD());
    }

    public double dot(vec3 other) {
        return (this.getXD() * other.getXD() + this.getYD() * other.getYD() + this.getZD() * other.getZD());
    }

    public vec3 cross(vec3 other) {
        return this.update(this.getYD() * other.getZD() - this.getZD() * other.getYD(), this.getZD() * other.getXD() - this.getXD() * other.getZD(), this.getXD() * other.getYD() - this.getYD() * other.getXD());
    }

    @Nullable
    public vec3 mulAndMakeNew(vecPrec precision, double length) {
        vec3.Double calc = new vec3.Double(this).mulToThis(length);
        switch (precision) {
            case BYTE:
                return new Byte(calc);
            case SHORT:
                return new Short(calc);
            case INT:
                return new Int(calc);
            case LONG:
                return new Long(calc);
            case FLOAT:
                return new Float(calc);
            case DOUBLE:
                return new Double(calc);
        }
        return null;
    }

    @Nullable
    public vec3 addAndMakeNew(vecPrec precision, vec3 other) {
        switch (precision) {
            case BYTE:
                return new Byte(this).addToThis(other);
            case SHORT:
                return new Short(this).addToThis(other);
            case INT:
                return new Int(this).addToThis(other);
            case LONG:
                return new Long(this).addToThis(other);
            case FLOAT:
                return new Float(this).addToThis(other);
            case DOUBLE:
                return new Double(this).addToThis(other);
        }
        return null;
    }

    @Nullable
    public vec3 subAndMakeNew(vecPrec precision, vec3 other) {
        switch (precision) {
            case BYTE:
                return new Byte(this).subFromThis(other);
            case SHORT:
                return new Short(this).subFromThis(other);
            case INT:
                return new Int(this).subFromThis(other);
            case LONG:
                return new Long(this).subFromThis(other);
            case FLOAT:
                return new Float(this).subFromThis(other);
            case DOUBLE:
                return new Double(this).subFromThis(other);
        }
        return null;
    }

    @Nullable
    public vec3 divAndMakeNew(vecPrec precision, double length) {
        vec3.Double calc = new vec3.Double(this).divToThis(length);
        switch (precision) {
            case BYTE:
                return new Byte(calc);
            case SHORT:
                return new Short(calc);
            case INT:
                return new Int(calc);
            case LONG:
                return new Long(calc);
            case FLOAT:
                return new Float(calc);
            case DOUBLE:
                return new Double(calc);
        }
        return null;
    }

    public abstract vecPrec getPrecision();

    public abstract void setXD(double x);

    public abstract void setYD(double y);

    public abstract void setZD(double z);

    public static abstract class decimalVec3 <T extends Number> extends vec3 {

    }

    public static abstract class intVec3 <T extends Number> extends vec3 {
        public int getXI() {
            return (int) this.getXD();
        }
        public int getYI() {
            return (int) this.getYD();
        }
        public int getZI() {
            return (int) this.getZD();
        }
    }

    public static class Int extends intVec3 <Integer> {

        public int x, y, z;
        private vec3Gen<java.lang.Integer> generic;

        public Int(vec3 vec3) {
            this.x = (int) vec3.getXD();
            this.y = (int) vec3.getYD();
            this.z = (int) vec3.getZD();
        }

        public Int() {

        }

        public Int(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public vec3.Int update(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
            return this;
        }

        public vec3 update(double x, double y, double z) {
            return this.update((int) x, (int) y, (int) z);
        }

        public vec3.Int subFromThis(vec3 other) {
            this.x -= (int) other.getXD();
            this.y -= (int) other.getYD();
            this.z -= (int) other.getZD();
            return this;
        }

        public vec3.Int subAndMakeNew(vec3 other) {
            return new vec3.Int(this).subFromThis(other);
        }

        public vec3.Int addToThis(vec3 other) {
            this.x += (int) other.getXD();
            this.y += (int) other.getYD();
            this.z += (int) other.getZD();
            return this;
        }

        public vec3.Int addAndMakeNew(vec3 other) {
            return new vec3.Int(this).addToThis(other);
        }

        public vec3.Int mulToThis(int scale) {
            this.x *= scale;
            this.y *= scale;
            this.z *= scale;
            return this;
        }

        public vec3.Int mulToThis(double scale) {
            return new vec3.Int(this).mulToThis((int) scale);
        }

        public vec3.Int divToThis(int scale) {
            this.x /= scale;
            this.y /= scale;
            this.z /= scale;
            return this;
        }

        public vec3.Int divToThis(double scale) {
            return new vec3.Int(this).divToThis((int) scale);
        }

        @Override
        public vecPrec getPrecision() {
            return vecPrec.INT;
        }

        @Override
        public void setXD(double x) {
            this.x = (int) x;
        }

        @Override
        public void setYD(double y) {
            this.y = (int) y;
        }

        @Override
        public void setZD(double z) {
            this.z = (int) z;
        }

        public double dot(vec3 other) {
            return (int) (this.x * other.getXD() + this.y * other.getYD() + this.z * other.getZD());
        }

        @Override
        @SuppressWarnings("unchecked")
        public vec3Gen<java.lang.Integer> getGenericVec() {
            if(this.generic == null)
                this.generic = new vec3Gen<java.lang.Integer>(this.x, this.y, this.z);
            return this.generic;
        }

        @Override
        public Integer getPreciseX() {
            return this.x;
        }

        @Override
        public Integer getPreciseY() {
            return this.y;
        }

        @Override
        public Integer getPreciseZ() {
            return this.z;
        }

        @Override
        public double getXD() {
            return this.x;
        }

        @Override
        public double getYD() {
            return this.y;
        }

        @Override
        public double getZD() {
            return this.z;
        }

        public int getX() {
            return this.x;
        }

        public int getY() {
            return this.y;
        }

        public int getZ() {
            return this.z;
        }

        public String toString() {
            return "{x: "+this.x+",y: "+this.y+",z: "+this.z+"}";
        }
    }

    public static class Long extends intVec3 <java.lang.Long> {

        public long x, y, z;
        private vec3Gen<java.lang.Long> generic;

        public Long(vec3 vec3) {
            this.x = (long) vec3.getXD();
            this.y = (long) vec3.getYD();
            this.z = (long) vec3.getZD();
        }

        public Long() {

        }

        public Long(long x, long y, long z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public vec3.Long update(long x, long y, long z) {
            this.x = x;
            this.y = y;
            this.z = z;
            return this;
        }

        public vec3 update(double x, double y, double z) {
            return this.update((long) x, (long) y, (long) z);
        }

        public vec3.Long subFromThis(vec3 other) {
            this.x -= (long) other.getXD();
            this.y -= (long) other.getYD();
            this.z -= (long) other.getZD();
            return this;
        }

        public vec3.Long subAndMakeNew(vec3 other) {
            return new vec3.Long(this).subFromThis(other);
        }

        public vec3.Long addToThis(vec3 other) {
            this.x += (long) other.getXD();
            this.y += (long) other.getYD();
            this.z += (long) other.getZD();
            return this;
        }

        public vec3.Long addAndMakeNew(vec3 other) {
            return new vec3.Long(this).addToThis(other);
        }

        public vec3.Long mulToThis(long scale) {
            this.x *= scale;
            this.y *= scale;
            this.z *= scale;
            return this;
        }

        public vec3.Long mulToThis(double scale) {
            return new vec3.Long(this).mulToThis((long) scale);
        }

        public vec3.Long divToThis(long scale) {
            this.x /= scale;
            this.y /= scale;
            this.z /= scale;
            return this;
        }

        public vec3.Long divToThis(double scale) {
            return new vec3.Long(this).divToThis((long) scale);
        }

        @Override
        public vecPrec getPrecision() {
            return vecPrec.LONG;
        }

        public double dot(vec3 other) {
            return (long) (this.x * other.getXD() + this.y * other.getYD() + this.z * other.getZD());
        }

        @Override
        @SuppressWarnings("unchecked")
        public vec3Gen<java.lang.Long> getGenericVec() {
            if(this.generic == null)
                this.generic = new vec3Gen<java.lang.Long>(this.x, this.y, this.z);
            return this.generic;
        }

        @Override
        public double getXD() {
            return this.x;
        }

        @Override
        public double getYD() {
            return this.y;
        }

        @Override
        public double getZD() {
            return this.z;
        }

        @Override
        public java.lang.Long getPreciseX() {
            return this.x;
        }

        @Override
        public java.lang.Long getPreciseY() {
            return this.y;
        }

        @Override
        public java.lang.Long getPreciseZ() {
            return this.z;
        }

        public long getX() {
            return this.x;
        }

        public long getY() {
            return this.y;
        }

        public long getZ() {
            return this.z;
        }

        @Override
        public void setXD(double x) {
            this.x = (long) x;
        }

        @Override
        public void setYD(double y) {
            this.y = (long) y;
        }

        @Override
        public void setZD(double z) {
            this.z = (long) z;
        }

        public String toString() {
            return "{x: "+this.x+",y: "+this.y+",z: "+this.z+"}";
        }
    }

    public static class Double extends intVec3 <java.lang.Double> {

        public double x, y, z;
        private vec3Gen<java.lang.Double> generic;

        public Double(vec3 vec3) {
            this.x =  vec3.getXD();
            this.y =  vec3.getYD();
            this.z =  vec3.getZD();
        }

        public Double() {

        }

        public Double(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public vec3.Double update(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
            return this;
        }

        public vec3.Double subFromThis(vec3 other) {
            this.x -=  other.getXD();
            this.y -=  other.getYD();
            this.z -=  other.getZD();
            return this;
        }

        public vec3.Double subAndMakeNew(vec3 other) {
            return new vec3.Double(this).subFromThis(other);
        }

        public vec3.Double addToThis(vec3 other) {
            this.x += other.getXD();
            this.y += other.getYD();
            this.z += other.getZD();
            return this;
        }

        public vec3.Double addAndMakeNew(vec3 other) {
            return new vec3.Double(this).addToThis(other);
        }

        public vec3.Double mulToThis(double scale) {
            this.x *= scale;
            this.y *= scale;
            this.z *= scale;
            return this;
        }

        public vec3.Double divToThis(double scale) {
            this.x /= scale;
            this.y /= scale;
            this.z /= scale;
            return this;
        }

        @Override
        public vecPrec getPrecision() {
            return vecPrec.DOUBLE;
        }

        public double dot(vec3 other) {
            return (this.x * other.getXD() + this.y * other.getYD() + this.z * other.getZD());
        }

        public vec3.Double normalize() {
            double len = this.length();
            return this.update(this.getXD() / len, this.getYD() / len, this.getZD() / len);
        }

        @Override
        @SuppressWarnings("unchecked")
        public vec3Gen<java.lang.Double> getGenericVec() {
            if(this.generic == null)
                this.generic = new vec3Gen<java.lang.Double>(this.x, this.y, this.z);
            return this.generic;
        }

        @Override
        public double getXD() {
            return this.x;
        }

        @Override
        public double getYD() {
            return this.y;
        }

        @Override
        public double getZD() {
            return this.z;
        }

        public double getX() {
            return this.x;
        }

        public double getY() {
            return this.y;
        }

        public double getZ() {
            return this.z;
        }

        @Override
        public java.lang.Double getPreciseX() {
            return this.x;
        }

        @Override
        public java.lang.Double getPreciseY() {
            return this.y;
        }

        @Override
        public java.lang.Double getPreciseZ() {
            return this.z;
        }

        @Override
        public void setXD(double x) {
            this.x = x;
        }

        @Override
        public void setYD(double y) {
            this.y = y;
        }

        @Override
        public void setZD(double z) {
            this.z = z;
        }

        public String toString() {
            return "{x: "+this.x+",y: "+this.y+",z: "+this.z+"}";
        }
    }

    public static class Float extends intVec3<java.lang.Float> {

        public float x, y, z;
        private vec3Gen<java.lang.Float> generic;

        public Float(vec3 vec3) {
            this.x = (float) vec3.getXD();
            this.y = (float) vec3.getYD();
            this.z = (float) vec3.getZD();
        }

        public Float() {

        }

        public Float(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public vec3.Float update(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
            return this;
        }

        public vec3 update(double x, double y, double z) {
            return this.update((float) x, (float) y, (float) z);
        }

        public vec3.Float subFromThis(vec3 other) {
            this.x -=  other.getXD();
            this.y -=  other.getYD();
            this.z -=  other.getZD();
            return this;
        }

        public vec3.Float subAndMakeNew(vec3 other) {
            return new vec3.Float(this).subFromThis(other);
        }

        public vec3.Float addToThis(vec3 other) {
            this.x += (float) other.getXD();
            this.y += (float) other.getYD();
            this.z += (float) other.getZD();
            return this;
        }

        public vec3.Float addAndMakeNew(vec3 other) {
            return new vec3.Float(this).addToThis(other);
        }

        public vec3.Float mulToThis(float scale) {
            this.x *= scale;
            this.y *= scale;
            this.z *= scale;
            return this;
        }

        public vec3.Float mulToThis(double scale) {
            return new vec3.Float(this).mulToThis((float) scale);
        }

        public vec3.Float divToThis(float scale) {
            this.x /= scale;
            this.y /= scale;
            this.z /= scale;
            return this;
        }

        public vec3.Float divToThis(double scale) {
            return new vec3.Float(this).divToThis((float) scale);
        }

        @Override
        public vecPrec getPrecision() {
            return vecPrec.FLOAT;
        }

        public double dot(vec3 other) {
            return (float) (this.x * other.getXD() + this.y * other.getYD() + this.z * other.getZD());
        }

        public vec3.Float normalize() {
            double len = this.length();
            return this.update((float) (this.getXD() / len), (float) (this.getYD() / len), (float) (this.getZD() / len));
        }

        @Override
        @SuppressWarnings("unchecked")
        public vec3Gen<java.lang.Float> getGenericVec() {
            if(this.generic == null)
                this.generic = new vec3Gen<java.lang.Float>(this.x, this.y, this.z);
            return this.generic;
        }

        @Override
        public double getXD() {
            return this.x;
        }

        @Override
        public double getYD() {
            return this.y;
        }

        @Override
        public double getZD() {
            return this.z;
        }

        public float getX() {
            return this.x;
        }

        public float getY() {
            return this.y;
        }

        public float getZ() {
            return this.z;
        }

        @Override
        public java.lang.Float getPreciseX() {
            return this.x;
        }

        @Override
        public java.lang.Float getPreciseY() {
            return this.y;
        }

        @Override
        public java.lang.Float getPreciseZ() {
            return this.z;
        }

        @Override
        public void setXD(double x) {
            this.x = (float) x;
        }

        @Override
        public void setYD(double y) {
            this.y = (float) y;
        }

        @Override
        public void setZD(double z) {
            this.z = (float) z;
        }

        public String toString() {
            return "{x: "+this.x+",y: "+this.y+",z: "+this.z+"}";
        }
    }

    public static class Short extends intVec3<java.lang.Short> {

        public short x, y, z;
        private vec3Gen<java.lang.Short> generic;

        public Short(vec3 vec3) {
            this.x = (short) vec3.getXD();
            this.y = (short) vec3.getYD();
            this.z = (short) vec3.getZD();
        }

        public Short() {

        }

        public Short(short x, short y, short z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public vec3.Short update(short x, short y, short z) {
            this.x = x;
            this.y = y;
            this.z = z;
            return this;
        }

        public vec3 update(double x, double y, double z) {
            return this.update((short) x, (short) y, (short) z);
        }

        public vec3.Short subFromThis(vec3 other) {
            this.x -= (short) other.getXD();
            this.y -= (short) other.getYD();
            this.z -= (short) other.getZD();
            return this;
        }

        public vec3.Short subAndMakeNew(vec3 other) {
            return new vec3.Short(this).subFromThis(other);
        }

        public vec3.Short addToThis(vec3 other) {
            this.x += (short) other.getXD();
            this.y += (short) other.getYD();
            this.z += (short) other.getZD();
            return this;
        }

        public vec3.Short addAndMakeNew(vec3 other) {
            return new vec3.Short(this).addToThis(other);
        }

        public vec3.Short mulToThis(short scale) {
            this.x *= scale;
            this.y *= scale;
            this.z *= scale;
            return this;
        }

        public vec3.Short mulToThis(double scale) {
            return new vec3.Short(this).mulToThis((short) scale);
        }

        public vec3.Short divToThis(short scale) {
            this.x /= scale;
            this.y /= scale;
            this.z /= scale;
            return this;
        }

        public vec3.Short divToThis(double scale) {
            return new vec3.Short(this).divToThis((short) scale);
        }

        @Override
        public vecPrec getPrecision() {
            return vecPrec.SHORT;
        }

        public double dot(vec3 other) {
            return (short) (this.x * other.getXD() + this.y * other.getYD() + this.z * other.getZD());
        }

        @Override
        @SuppressWarnings("unchecked")
        public vec3Gen<java.lang.Short> getGenericVec() {
            if(this.generic == null)
                this.generic = new vec3Gen<java.lang.Short>(this.x, this.y, this.z);
            return this.generic;
        }

        @Override
        public double getXD() {
            return this.x;
        }

        @Override
        public double getYD() {
            return this.y;
        }

        @Override
        public double getZD() {
            return this.z;
        }

        public short getX() {
            return this.x;
        }

        public short getY() {
            return this.y;
        }

        public short getZ() {
            return this.z;
        }

        @Override
        public java.lang.Short getPreciseX() {
            return this.x;
        }

        @Override
        public java.lang.Short getPreciseY() {
            return this.y;
        }

        @Override
        public java.lang.Short getPreciseZ() {
            return this.z;
        }

        @Override
        public void setXD(double x) {
            this.x = (short) x;
        }

        @Override
        public void setYD(double y) {
            this.y = (short) y;
        }

        @Override
        public void setZD(double z) {
            this.z = (short) z;
        }

        public String toString() {
            return "{x: "+this.x+",y: "+this.y+",z: "+this.z+"}";
        }
    }

    public static class Byte extends intVec3<java.lang.Byte> {

        public byte x, y, z;
        private vec3Gen<java.lang.Byte> generic;

        public Byte(vec3 vec3) {
            this.x = (byte) vec3.getXD();
            this.y = (byte) vec3.getYD();
            this.z = (byte) vec3.getZD();
        }

        public Byte() {

        }

        public Byte(byte x, byte y, byte z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public vec3.Byte update(byte x, byte y, byte z) {
            this.x = x;
            this.y = y;
            this.z = z;
            return this;
        }

        public vec3 update(double x, double y, double z) {
            return this.update((byte) x, (byte) y, (byte) z);
        }

        public vec3.Byte subFromThis(vec3 other) {
            this.x -= (byte) other.getXD();
            this.y -= (byte) other.getYD();
            this.z -= (byte) other.getZD();
            return this;
        }

        public vec3.Byte subAndMakeNew(vec3 other) {
            return new vec3.Byte(this).subFromThis(other);
        }

        public vec3.Byte addToThis(vec3 other) {
            this.x += (byte) other.getXD();
            this.y += (byte) other.getYD();
            this.z += (byte) other.getZD();
            return this;
        }

        public vec3.Byte addAndMakeNew(vec3 other) {
            return new vec3.Byte(this).addToThis(other);
        }

        public vec3.Byte mulToThis(byte scale) {
            this.x *= scale;
            this.y *= scale;
            this.z *= scale;
            return this;
        }

        public vec3.Byte mulToThis(double scale) {
            return new vec3.Byte(this).mulToThis((byte) scale);
        }

        public vec3.Byte divToThis(byte scale) {
            this.x /= scale;
            this.y /= scale;
            this.z /= scale;
            return this;
        }

        public vec3.Byte divToThis(double scale) {
            return new vec3.Byte(this).divToThis((byte) scale);
        }

        @Override
        public vecPrec getPrecision() {
            return vecPrec.BYTE;
        }

        public double dot(vec3 other) {
            return (byte) (this.x * other.getXD() + this.y * other.getYD() + this.z * other.getZD());
        }

        @Override
        @SuppressWarnings("unchecked")
        public vec3Gen<java.lang.Byte> getGenericVec() {
            if(this.generic == null)
                this.generic = new vec3Gen<java.lang.Byte>(this.x, this.y, this.z);
            return this.generic;
        }

        @Override
        public double getXD() {
            return this.x;
        }

        @Override
        public double getYD() {
            return this.y;
        }

        @Override
        public double getZD() {
            return this.z;
        }

        public byte getX() {
            return this.x;
        }

        public byte getY() {
            return this.y;
        }

        public byte getZ() {
            return this.z;
        }

        @Override
        public java.lang.Byte getPreciseX() {
            return this.x;
        }

        @Override
        public java.lang.Byte getPreciseY() {
            return this.y;
        }

        @Override
        public java.lang.Byte getPreciseZ() {
            return this.z;
        }

        @Override
        public void setXD(double x) {
            this.x = (byte) x;
        }

        @Override
        public void setYD(double y) {
            this.y = (byte) y;
        }

        @Override
        public void setZD(double z) {
            this.z = (byte) z;
        }

        public String toString() {
            return "{x: "+this.x+",y: "+this.y+",z: "+this.z+"}";
        }
    }
}

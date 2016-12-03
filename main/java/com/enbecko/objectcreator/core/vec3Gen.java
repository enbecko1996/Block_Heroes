package com.enbecko.objectcreator.core;

import com.sun.istack.internal.Nullable;

import javax.annotation.Nonnull;

/**
 * Created by Niclas on 14.11.2016.
 */
public class vec3Gen<T extends Number> {

    public T x, y, z;
    public final Class type;

    public vec3Gen(Class type) {
        this.type = type;
    }

    public vec3Gen(T x, T y, T z) {
        this.x = x;
        this.y = y;
        this.z = z;
        if (this.x instanceof Double)
            this.type = Double.class;
        else if(this.x instanceof Float)
            this.type = Float.class;
        else if(this.x instanceof Integer)
            this.type = Integer.class;
        else if(this.x instanceof Long)
            this.type = Long.class;
        else
            this.type = Double.class;
    }

    public vec3Gen(vec3Gen<T> vec3) {
        this.x = vec3.x;
        this.y = vec3.y;
        this.z = vec3.z;
        if (this.x instanceof Double)
            this.type = Double.class;
        else if (this.x instanceof Float)
            this.type = Float.class;
        else if (this.x instanceof Integer)
            this.type = Integer.class;
        else if (this.x instanceof Long)
            this.type = Long.class;
        else
            this.type = Double.class;
    }

    public vec3.Double newDoubleVec3() {
        return new vec3.Double(this.x.doubleValue(), this.y.doubleValue(), this.z.doubleValue());
    }

    public vec3Gen<Float> newFloatVec3() {
        return new vec3Gen<Float>(this.x.floatValue(), this.y.floatValue(), this.z.floatValue());
    }

    public vec3.Int newIntVec3() {
        return new vec3.Int(this.x.intValue(), this.y.intValue(), this.z.intValue());
    }

    public vec3Gen<Long> newLongVec3() {
        return new vec3Gen<Long>(this.x.longValue(), this.y.longValue(), this.z.longValue());
    }

    @SuppressWarnings("unchecked")
    public vec3Gen<T> subFromThis(@Nonnull vec3Gen<T> other) {
        if(this.x instanceof Double) {
            this.x = (T) Double.valueOf(this.x.doubleValue() - other.x.doubleValue());
            this.y = (T) Double.valueOf(this.y.doubleValue() - other.y.doubleValue());
            this.z = (T) Double.valueOf(this.z.doubleValue() - other.z.doubleValue());
        }
        else if(this.x instanceof Float) {
            this.x = (T) Float.valueOf(this.x.floatValue() - other.x.floatValue());
            this.y = (T) Float.valueOf(this.y.floatValue() - other.y.floatValue());
            this.z = (T) Float.valueOf(this.z.floatValue() - other.z.floatValue());
        }
        else if(this.x instanceof Integer) {
            this.x = (T) Integer.valueOf(this.x.intValue() - other.x.intValue());
            this.y = (T) Integer.valueOf(this.y.intValue() - other.y.intValue());
            this.z = (T) Integer.valueOf(this.z.intValue() - other.z.intValue());
        }
        else if(this.x instanceof Long) {
            this.x = (T) Long.valueOf(this.x.longValue() - other.x.longValue());
            this.y = (T) Long.valueOf(this.y.longValue() - other.y.longValue());
            this.z = (T) Long.valueOf(this.z.longValue() - other.z.longValue());
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    public vec3Gen<T> subAndMakeNew(@Nonnull vec3Gen<T> other) {
        return new vec3Gen<T>(this).subFromThis(other);
    }

    public vec3Gen<T> update(T x, T y, T z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public void setX(T x)
    {
        this.x = x;
    }
    public void setY(T y)
    {
        this.y = y;
    }
    public void setZ(T z)
    {
        this.z = z;
    }

    public vec3Gen<T> update(vec3Gen<T> v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        return this;
    }

    @SuppressWarnings("unchecked")
    public vec3Gen<T> normalize()
    {
        double len = 1D / this.length();
        Class<T> type;
        if(this.x instanceof Double)
            this.update((T) Double.valueOf(this.x.doubleValue() * len), (T) Double.valueOf(this.y.doubleValue() * len), (T) Double.valueOf(this.z.doubleValue() * len));
        else if(this.x instanceof Float)
            this.update((T) Float.valueOf(this.x.floatValue() * (float) len), (T) Float.valueOf(this.y.floatValue() * (float) len), (T) Float.valueOf(this.z.floatValue() * (float) len));
        else if(this.x instanceof Integer)
            this.update((T) Integer.valueOf(this.x.intValue() * (int) len), (T) Integer.valueOf(this.y.intValue() * (int) len), (T) Integer.valueOf(this.z.intValue() * (int) len));
        else if(this.x instanceof Long)
            this.update((T) Long.valueOf(this.x.longValue() * (long) len), (T) Long.valueOf(this.y.longValue() * (long) len), (T) Long.valueOf(this.z.longValue() * (long) len));
        return this;
    }

    @SuppressWarnings("unchecked")
    public vec3Gen<T> normalizedAndResizedVersionOf(vec3Gen<T> tmp, float res) {
        double len = 1F/ tmp.length();
        if(this.x instanceof Double)
            this.update((T) Double.valueOf(this.x.doubleValue() * (len * res)), (T) Double.valueOf(this.y.doubleValue() * (len * res)), (T) Double.valueOf(this.z.doubleValue() * (len * res)));
        else if(this.x instanceof Float)
            this.update((T) Float.valueOf(this.x.floatValue() * (float) (len * res)), (T) Float.valueOf(this.y.floatValue() * (float) (len * res)), (T) Float.valueOf(this.z.floatValue() * (float) (len * res)));
        else if(this.x instanceof Integer)
            this.update((T) Integer.valueOf(this.x.intValue() * (int) (len * res)), (T) Integer.valueOf(this.y.intValue() * (int) (len * res)), (T) Integer.valueOf(this.z.intValue() * (int) (len * res)));
        else if(this.x instanceof Long)
            this.update((T) Long.valueOf(this.x.longValue() * (long) (len * res)), (T) Long.valueOf(this.y.longValue() * (long) (len * res)), (T) Long.valueOf(this.z.longValue() * (long) (len * res)));
        return this;
    }

    public vec3Gen<T> newNormalized()
    {
        return new vec3Gen<T>(this.x, this.y, this.z).normalize();
    }


    public vec3Gen<T> normalizeAndResize(float res)
    {
        return this.normalizedAndResizedVersionOf(this, res);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public T[] toFourArry()
    {
        if(this.x instanceof Double) {
            Double[] outD = new Double[4];
            outD[0] = x.doubleValue();
            outD[1] = y.doubleValue();
            outD[2] = z.doubleValue();
            outD[3] = 1d;
            return (T[]) outD;
        } else if(this.x instanceof Float) {
            Float[] outF = new Float[4];
            outF[0] = x.floatValue();
            outF[1] = y.floatValue();
            outF[2] = z.floatValue();
            outF[3] = 1f;
            return (T[]) outF;
        } else if(this.x instanceof Integer) {
            Integer[] outI = new Integer[2];
            outI[0] = x.intValue();
            outI[1] = y.intValue();
            outI[2] = z.intValue();
            outI[3] = 1;
            return (T[]) outI;
        } else if(this.x instanceof Long) {
            Long[] outL = new Long[2];
            outL[0] = x.longValue();
            outL[1] = y.longValue();
            outL[2] = z.longValue();
            outL[3] = 1L;
            return (T[]) outL;
        }
        return null;
    }

    public double length() {
        if(this.x instanceof Double)
            return MathHelper.getVecLength(this.x.doubleValue(), this.y.doubleValue(), this.z.doubleValue());
        else if(this.x instanceof Float)
            return MathHelper.getVecLength(this.x.floatValue(), this.y.floatValue(), this.z.floatValue());
        else if(this.x instanceof Integer)
            return MathHelper.getVecLength(this.x.intValue(), this.y.intValue(), this.z.intValue());
        else if(this.x instanceof Long)
            return MathHelper.getVecLength(this.x.longValue(), this.y.longValue(), this.z.longValue());
        return 0;
    }

    @Nullable
    public T dot(@Nonnull vec3Gen<T> other) {
        if(this.x instanceof Double)
            return (T) Double.valueOf(this.x.doubleValue() * other.x.doubleValue() + this.y.doubleValue() * other.y.doubleValue() + this.z.doubleValue() * other.z.doubleValue());
        else if(this.x instanceof Float)
            return (T) Float.valueOf(this.x.floatValue() * other.x.floatValue() + this.y.floatValue() * other.y.floatValue() + this.z.floatValue() * other.z.floatValue());
        else if(this.x instanceof Integer)
            return (T) Integer.valueOf(this.x.intValue() * other.x.intValue() + this.y.intValue() * other.y.intValue() + this.z.intValue() * other.z.intValue());
        else if(this.x instanceof Long)
            return (T) Long.valueOf(this.x.longValue() * other.x.longValue() + this.y.longValue() * other.y.longValue() + this.z.longValue() * other.z.longValue());
        return null;
    }

    public String toString() {
        return "{"+this.x+", "+this.y+", "+this.z+"}";
    }
}

package com.enbecko.objectcreator.core;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Niclas on 22.03.2016.
 */
public class MathHelper{

    public static double getVecLength(double...vec)
    {
        double tmp = 0;
        for(int k = 0; k < vec.length; k++)
        {
            tmp+= Math.pow(vec[k], 2);
        }
        return Math.sqrt(tmp);
    }

    public static float getAngleBetween(vec3.Double first, vec3.Double sec)
    {
        return (float) Math.toDegrees(Math.acos((first.x * sec.x + first.y * sec.y + first.z * sec.z) / (getVecLength(first) * getVecLength(sec))));
    }

    public static float getAngleBetweenZ(vec3.Double first, vec3.Double sec)
    {
        return (float) Math.toDegrees(Math.acos((first.x * sec.x + first.y * sec.y) / (getVecLength(first.x, first.y) * getVecLength(sec.x, sec.y))));
    }

    public static float getAngleBetweenX(vec3.Double first, vec3.Double sec)
    {
        return (float) Math.toDegrees(Math.acos((first.z * sec.z + first.y * sec.y) / (getVecLength(first.z, first.y) * getVecLength(sec.z, sec.y))));
    }

    public static boolean sameSign(float a, float b) {
        return a*b >= 0.0f;
    }

    public static float getVecLength(float x, float y)
    {
        double tmp = 0;
        tmp = Math.pow(x, 2)+Math.pow(y, 2);
        return (float)Math.sqrt(tmp);
    }

    public static float getVecLength(vec3.Double vec)
    {
        double tmp = 0;
        tmp = Math.pow(vec.x, 2)+Math.pow(vec.y, 2)+Math.pow(vec.z, 2);
        return (float)Math.sqrt(tmp);
    }

    public static float getVecLength(vec3 vec)
    {
        double tmp = 0;
        tmp = Math.pow(vec.getXD(), 2)+Math.pow(vec.getYD(), 2)+Math.pow(vec.getZD(), 2);
        return (float)Math.sqrt(tmp);
    }

    @Nonnull
    public static <T extends Number> vec3Gen<T>[] lineBetweenGeneric(vec3Gen<T> p1, vec3Gen<T> p2, final T oneUnit) {
        final double oU = oneUnit.doubleValue();
        List<vec3Gen<T>> tmpVecs = new ArrayList<vec3Gen<T>>();
        vec3Gen<T> delta = p2.subAndMakeNew(p1);
        double mY = delta.y.doubleValue() / delta.x.doubleValue();
        float up = 0;
        int upSteps = 0;
        System.out.println(p1+" "+p2+" "+delta+" "+mY);
        for (double x = p1.x.doubleValue(); x < p2.x.doubleValue(); x += oU) {
            up += mY * oU;
            if(up >= oU) {
                upSteps++;
                up -= oU;
            }
            addGenericVec(p1.type, tmpVecs, x, upSteps * oU, p1.z.doubleValue());
        }
        double mZ = delta.y.doubleValue() / delta.z.doubleValue();
        up = 0;
        upSteps = 0;
        System.out.println(p1+" "+p2+" "+delta+" "+mZ);
        for (double z = p1.z.doubleValue(); z < p2.z.doubleValue(); z += oU) {
            up += mZ * oU;
            if(up >= oU) {
                upSteps++;
                up -= oU;
            }
            addGenericVec(p1.type, tmpVecs, p1.x.doubleValue(), upSteps * oU, z);
        }
        double mX = delta.z.doubleValue() / delta.x.doubleValue();
        up = 0;
        upSteps = 0;
        System.out.println(p1+" "+p2+" "+delta+" "+mX);
        for (double y = p1.y.doubleValue(); y < p2.y.doubleValue(); y += oU) {
            up += mX * oU;
            if(up >= oU) {
                upSteps++;
                up -= oU;
            }
            addGenericVec(p1.type, tmpVecs, p1.x.doubleValue(), y, upSteps * oU);
        }

        @SuppressWarnings("unchecked")
        vec3Gen<T>[] out = new vec3Gen[tmpVecs.size()];
        return tmpVecs.toArray(out);
    }

    /**
     * @return an array containing the line
     * @throws ArrayStoreException if the runtime type of the specified array
     *         is not a supertype of the runtime type of every element in
     *         this list
     * @throws NullPointerException if the specified array is null
     */
    @Nullable
    private static vec3[] lineBetween(@Nonnull vec3.vecPrec prec, vec3 p1, vec3 p2, final double oneUnit) {
        List<vec3> tmpVecsX = new ArrayList<vec3>(), tmpVecsY = new ArrayList<vec3>(), tmpVecsZ = new ArrayList<vec3>();
        vec3.Double delta = p2.subAndMakeNewD(p1);
        double mY = delta.y / delta.x;
        float up = 0;
        int upSteps = 0;
        System.out.println(p1+" "+p2+" "+delta+" "+mY);
        for (double x = p1.getXD(); x <= p2.getXD(); x += oneUnit) {
            up += mY * oneUnit;
            if(up >= oneUnit) {
                upSteps++;
                up -= oneUnit;
            }
            addToList(prec, tmpVecsZ, x, upSteps * oneUnit, p1.getZD());
        }
        double mZ = delta.y / delta.z;
        up = 0;
        upSteps = 0;
        System.out.println(p1+" "+p2+" "+delta+" "+mZ);
        for (double z = p1.getZD(); z <= p2.getZD(); z += oneUnit) {
            up += mZ * oneUnit;
            if(up >= oneUnit) {
                upSteps++;
                up -= oneUnit;
            }
            addToList(prec, tmpVecsX, p1.getXD(), upSteps * oneUnit, z);
        }
        double mX = delta.z / delta.x;
        up = 0;
        upSteps = 0;
        System.out.println(p1+" "+p2+" "+delta+" "+mX);
        for (double x = p1.getXD(); x <= p2.getXD(); x += oneUnit) {
            up += mX * oneUnit;
            if(up >= oneUnit) {
                upSteps++;
                up -= oneUnit;
            }
            addToList(prec, tmpVecsX, x, p1.getYD(), upSteps * oneUnit);
        }
        System.out.println(tmpVecsX+" "+tmpVecsY+" "+tmpVecsZ);
        vec3[] out = null;
        switch (prec) {
            case BYTE:
                out = new vec3.Byte[tmpVecsX.size()];
                break;
            case SHORT:
                out = new vec3.Short[tmpVecsX.size()];
                break;
            case INT:
                out = new vec3.Int[tmpVecsX.size()];
                break;
            case LONG:
                out = new vec3.Long[tmpVecsX.size()];
                break;
            case FLOAT:
                out = new vec3.Float[tmpVecsX.size()];
                break;
            case DOUBLE:
                out = new vec3.Double[tmpVecsX.size()];
                break;
        }
        return tmpVecsX.toArray(out);
    }

    private static void addToList(vec3.vecPrec prec, List<vec3> list, double x, double y, double z) {
        switch (prec) {
            case BYTE:
                list.add(new vec3.Byte((byte) x, (byte) y, (byte) z));
                break;
            case SHORT:
                list.add(new vec3.Short((short) x, (short) y, (short) z));
                break;
            case INT:
                list.add(new vec3.Int((int) x, (int) y, (int) z));
                break;
            case LONG:
                list.add(new vec3.Long((long) x, (long) y, (long) z));
                break;
            case FLOAT:
                list.add(new vec3.Float((float) x, (float) y, (float) z));
                break;
            case DOUBLE:
                list.add(new vec3.Double(x, y, z));
                break;
        }
    }

    /**
     * @return an array containing the line
     * @throws ArrayStoreException if the runtime type of the specified array
     *         is not a supertype of the runtime type of every element in
     *         this list
     * @throws NullPointerException if the specified array is null
     */
    @Nullable
    public static vec3.Int[] lineBetweenI(vec3.Int first, vec3.Int sec, int unit) {
        return (vec3.Int[]) lineBetween(vec3.vecPrec.INT, first, sec, unit);
    }

    /**
     * @return an array containing the line
     * @throws ArrayStoreException if the runtime type of the specified array
     *         is not a supertype of the runtime type of every element in
     *         this list
     * @throws NullPointerException if the specified array is null
     */
    @Nullable
    public static vec3.Byte[] lineBetweenB(vec3.Byte first, vec3.Byte sec, byte unit) {
        return (vec3.Byte[]) lineBetween(vec3.vecPrec.BYTE, first, sec, unit);
    }

    /**
     * @return an array containing the line
     * @throws ArrayStoreException if the runtime type of the specified array
     *         is not a supertype of the runtime type of every element in
     *         this list
     * @throws NullPointerException if the specified array is null
     */
    @Nullable
    public static vec3.Short[] lineBetweenS(vec3.Short first, vec3.Short sec, short unit) {
        return (vec3.Short[]) lineBetween(vec3.vecPrec.SHORT, first, sec, unit);
    }

    /**
     * @return an array containing the line
     * @throws ArrayStoreException if the runtime type of the specified array
     *         is not a supertype of the runtime type of every element in
     *         this list
     * @throws NullPointerException if the specified array is null
     */
    @Nullable
    public static vec3.Long[] lineBetweenL(vec3.Long first, vec3.Long sec, long unit) {
        return (vec3.Long[]) lineBetween(vec3.vecPrec.LONG, first, sec, unit);
    }

    /**
     * @return an array containing the line
     * @throws ArrayStoreException if the runtime type of the specified array
     *         is not a supertype of the runtime type of every element in
     *         this list
     * @throws NullPointerException if the specified array is null
     */
    @Nullable
    public static vec3.Float[] lineBetweenF(vec3.Float first, vec3.Float sec, float unit) {
        return (vec3.Float[]) lineBetween(vec3.vecPrec.FLOAT, first, sec, unit);
    }

    /**
     * @return an array containing the line
     * @throws ArrayStoreException if the runtime type of the specified array
     *         is not a supertype of the runtime type of every element in
     *         this list
     * @throws NullPointerException if the specified array is null
     */
    @Nullable
    public static vec3.Double[] lineBetweenD(vec3.Double first, vec3.Double sec, double unit) {
        return (vec3.Double[]) lineBetween(vec3.vecPrec.DOUBLE, first, sec, unit);
    }

    @SuppressWarnings("unchecked")
    private static <T extends Number> void addGenericVec(Class type, List<vec3Gen<T>> vec3List, double x, double y, double z) {
        if(type == Double.class)
            vec3List.add(new vec3Gen<T>((T) Double.valueOf(x), (T) Double.valueOf(y), (T) Double.valueOf(z)));
        else if(type == Float.class)
            vec3List.add(new vec3Gen<T>((T) Float.valueOf((float) x), (T) Float.valueOf((float) y), (T) Float.valueOf((float) z)));
        else if(type == Integer.class)
            vec3List.add(new vec3Gen<T>((T) Integer.valueOf((int) x), (T) Integer.valueOf((int) y), (T) Integer.valueOf((int) z)));
        else if(type == Long.class)
            vec3List.add(new vec3Gen<T>((T) Long.valueOf((long) x), (T) Long.valueOf((long) y), (T) Long.valueOf((long) z)));
    }
}

package com.enbecko.objectcreator.restart;

import com.enbecko.objectcreator.core.LocalCoordsRenderableFaces2DInOneCoord;
import com.enbecko.objectcreator.core.RenderableFace2D;
import com.enbecko.objectcreator.core.vec3;
import com.enbecko.objectcreator.core.vec3Int;
import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.model.TexturedQuad;
import org.ojalgo.matrix.decomposition.LU;
import org.ojalgo.matrix.store.MatrixStore;
import org.ojalgo.matrix.store.PrimitiveDenseStore;
import org.ojalgo.netio.BasicLogger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Niclas on 19.11.2016.
 */
public class BlockRenderFace3D {

    private final vec3Int onPoint = new vec3Int();
    private final vec3Int vec1 = new vec3Int();
    private final vec3Int vec2 = new vec3Int();

    public BlockRenderFace3D(vec3Int onPoint, vec3Int vec1, vec3Int vec2) {
        if(onPoint != null)
            this.onPoint.update(onPoint);
        if(vec1 != null)
            this.vec1.update(vec1);
        if(vec2 != null)
            this.vec2.update(vec2);
    }

    public BlockRenderFace3D() {
    }


    public BlockRenderFace3D(vec3Int onPoint) {
        if(onPoint != null)
            this.onPoint.update(onPoint);
        this.vec1.update(1, 0, 0);
        this.vec2.update(0, 1, 0);
    }

    public BlockRenderFace3D updateOnPoint(@Nonnull vec3Int onPoint) {
        this.onPoint.update(onPoint);
        return this;
    }

    public BlockRenderFace3D updateVecs(@Nullable vec3Int vec1, @Nullable vec3Int vec2) {
        if(vec1 != null)
            this.vec1.update(vec1);
        if(vec2 != null)
            this.vec2.update(vec2);
        return this;
    }

    public BlockRenderFace3D update(@Nonnull vec3Int onPoint, @Nullable vec3Int vec1, @Nullable vec3Int vec2) {
        if(onPoint != null)
            this.onPoint.update(onPoint);
        if(vec1 != null)
            this.vec1.update(vec1);
        if(vec2 != null)
            this.vec2.update(vec2);
        return this;
    }

    @Nullable
    public TexturedQuad getQuad(BoneRenderer.Direction direction, final float coord, boolean negative) {
        float[] vert0 = null;
        float[] vert1 = null;
        float[] vert2 = null;
        float[] vert3 = null;
        vec3Int LOW_LEFT = new vec3Int(this.onPoint);
        vec3Int UP_RIGHT = new vec3Int(this.onPoint.addAndMakeNew(this.vec1).addAndMakeNew(this.vec2));
        switch (direction) {
            case X:
                vert0 = new float[]{coord, LOW_LEFT.y, LOW_LEFT.z};
                vert1 = new float[]{coord, LOW_LEFT.y, UP_RIGHT.z};
                vert2 = new float[]{coord, UP_RIGHT.y, UP_RIGHT.z};
                vert3 = new float[]{coord, UP_RIGHT.y, LOW_LEFT.z};
                break;
            case Y:
                vert0 = new float[]{LOW_LEFT.x, coord, LOW_LEFT.z};
                vert1 = new float[]{UP_RIGHT.x, coord, LOW_LEFT.z};
                vert2 = new float[]{UP_RIGHT.x, coord, UP_RIGHT.z};
                vert3 = new float[]{LOW_LEFT.x, coord, UP_RIGHT.z};
                break;
            case Z:
                vert0 = new float[]{LOW_LEFT.x, LOW_LEFT.y, coord};
                vert3 = new float[]{UP_RIGHT.x, LOW_LEFT.y, coord};
                vert2 = new float[]{UP_RIGHT.x, UP_RIGHT.y, coord};
                vert1 = new float[]{LOW_LEFT.x, UP_RIGHT.y, coord};
                break;
        }
        if (vert0 != null && vert1 != null && vert2 != null && vert3 != null) {
            float[] tex0 = null;
            float[] tex1 = null;
            float[] tex2 = null;
            float[] tex3 = null;
            /**
             try {
             tex0 = texs.get(faces.get(f)[0][2] - 1);
             tex1 = texs.get(faces.get(f)[1][2] - 1);
             tex2 = texs.get(faces.get(f)[2][2] - 1);
             tex3 = texs.get(faces.get(f)[faces.get(f).length - 1][2] - 1);
             }catch (Exception e)
             {
             }*/

            PositionTexComb first, sec, third, fourth;
            first = new PositionTexComb(vert0);
            third = new PositionTexComb(vert2);
            if (negative) {
                sec = new PositionTexComb(vert1);
                fourth = new PositionTexComb(vert3);
            }
            else {
                sec = new PositionTexComb(vert3);
                fourth = new PositionTexComb(vert1);
            }
            PositionTextureVertex vert00 = null;
            PositionTextureVertex vert11 = null;
            PositionTextureVertex vert22 = null;
            PositionTextureVertex vert33 = null;
            if(first.hasVert && sec.hasVert && third.hasVert && fourth.hasVert) {
                if (first.hasTex && sec.hasTex && third.hasTex && fourth.hasTex) {
                    vert00 = new PositionTextureVertex(first.vert[0], first.vert[1], first.vert[2], 0, 0).setTexturePosition(tex0[0], 1 - tex0[1]);
                    vert11 = new PositionTextureVertex(sec.vert[0], sec.vert[1], sec.vert[2], 0, 0).setTexturePosition(tex1[0], 1 - tex1[1]);
                    vert22 = new PositionTextureVertex(third.vert[0], third.vert[1], third.vert[2], 0, 0).setTexturePosition(tex2[0], 1 - tex2[1]);
                    vert33 = new PositionTextureVertex(fourth.vert[0], fourth.vert[1], fourth.vert[2], 0, 0).setTexturePosition(tex3[0], 1 - tex3[1]);
                } else {
                    vert00 = new PositionTextureVertex(first.vert[0], first.vert[1], first.vert[2], 0, 0);
                    vert11 = new PositionTextureVertex(sec.vert[0], sec.vert[1], sec.vert[2], 0, 0);
                    vert22 = new PositionTextureVertex(third.vert[0], third.vert[1], third.vert[2], 0, 0);
                    vert33 = new PositionTextureVertex(fourth.vert[0], fourth.vert[1], fourth.vert[2], 0, 0);
                }
            }
            if (vert00 != null && vert11 != null && vert22 != null && vert33 != null)
                return new TexturedQuad(new PositionTextureVertex[]{vert00, vert11, vert22, vert33});
        }
        return null;
    }

    private class PositionTexComb{
        public final float[] vert, tex;
        public final boolean hasTex, hasVert;
        public PositionTexComb(@Nonnull float[] vert, @Nonnull float[] tex) {
            if(vert.length == 3 && tex.length == 2) {
                this.hasVert = true;
                this.hasTex = true;
                this.vert = vert;
                this.tex = tex;
            } else {
                this.hasVert = false;
                this.hasTex = false;
                this.vert = null;
                this.tex = null;
            }
        }

        public PositionTexComb(@Nonnull float[] vert) {
            if(vert.length == 3) {
                hasVert = true;
                this.vert = vert;
                this.tex = null;
            } else {
                this.hasVert = false;
                this.vert = null;
                this.tex = null;
            }
            this.hasTex = false;
        }
    }

    public String toString() {
        return "{face: "+this.onPoint+", "+this.vec1+", "+this.vec2+"}";
    }
}

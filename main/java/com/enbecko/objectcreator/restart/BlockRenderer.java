package com.enbecko.objectcreator.restart;

import com.enbecko.objectcreator.ObjectCreatorMod;
import com.enbecko.objectcreator.core.vec3Int;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;
import org.ojalgo.matrix.store.PrimitiveDenseStore;

/**
 * Created by Niclas on 19.11.2016.
 */
public class BlockRenderer {

    private final TexturedQuad[] quads;
    private final Block3D block3D;
    private static final vec3Int nulli = new vec3Int(0,0,0);
    private static final vec3Int xVec = new vec3Int(1,0,0);
    private static final vec3Int yVec = new vec3Int(0,1,0);
    private static final vec3Int zVec = new vec3Int(0,0,1);
    public static final BlockRenderer theBlockRenderer = BlockRenderer.getNewBlockRenderer(null);

    public BlockRenderer(Block3D block3D, TexturedQuad ...  quads) {
        this.block3D = block3D;
        this.quads = quads;
    }

    public void render(Tessellator tessellator) {
        GL11.glColor3f((float) this.block3D.getRed() / ObjectCreatorMod.colorVariety, (float)this.block3D.getGreen() / ObjectCreatorMod.colorVariety, (float)this.block3D.getBlue() / ObjectCreatorMod.colorVariety);
        for (TexturedQuad quad : quads) {
            quad.draw(tessellator.getBuffer(), 1);
        }
        GL11.glColor3f(1, 1, 1);
    }

    public static BlockRenderer getNewBlockRenderer(Bone bone, Block3D block3D) {
        BlockRenderFace3D[] faces = new BlockRenderFace3D[6];
        TexturedQuad[] quads = new TexturedQuad[6];
        faces[0] = new BlockRenderFace3D(block3D.getPosition(), yVec, xVec);
        quads[0] = faces[0].getQuad(BoneRenderer.Direction.Z, block3D.getPosition().z, true);
        faces[1] = new BlockRenderFace3D(block3D.getPosition().addAndMakeNew(zVec), yVec, zVec.negate());
        quads[1] = faces[1].getQuad(BoneRenderer.Direction.X, block3D.getPosition().x, false);
        faces[2] = new BlockRenderFace3D(block3D.getPosition().addAndMakeNew(xVec), yVec, zVec);
        quads[2] = faces[2].getQuad(BoneRenderer.Direction.X, block3D.getPosition().x + 1, false);
        faces[3] = new BlockRenderFace3D(block3D.getPosition().addAndMakeNew(yVec), zVec, xVec);
        quads[3] = faces[3].getQuad(BoneRenderer.Direction.Y, block3D.getPosition().y + 1, false);
        faces[4] = new BlockRenderFace3D(block3D.getPosition().addAndMakeNew(xVec).addAndMakeNew(zVec), yVec, xVec.negate());
        quads[4] = faces[4].getQuad(BoneRenderer.Direction.Z, block3D.getPosition().z + 1, true);
        faces[5] = new BlockRenderFace3D(block3D.getPosition().addAndMakeNew(zVec), zVec.negate(), xVec);
        quads[5] = faces[5].getQuad(BoneRenderer.Direction.Y, block3D.getPosition().y, false);
        return new BlockRenderer(block3D, quads);
    }

    private static BlockRenderer getNewBlockRenderer(Bone bone) {
        BlockRenderFace3D[] faces = new BlockRenderFace3D[6];
        TexturedQuad[] quads = new TexturedQuad[6];
        faces[0] = new BlockRenderFace3D(nulli, yVec, xVec);
        quads[0] = faces[0].getQuad(BoneRenderer.Direction.Z, 0, true);
        faces[1] = new BlockRenderFace3D(nulli, yVec, zVec.negate());
        quads[1] = faces[1].getQuad(BoneRenderer.Direction.X, 0, false);
        faces[2] = new BlockRenderFace3D(nulli, yVec, zVec);
        quads[2] = faces[2].getQuad(BoneRenderer.Direction.X, 0 + 1, false);
        faces[3] = new BlockRenderFace3D(nulli, zVec, xVec);
        quads[3] = faces[3].getQuad(BoneRenderer.Direction.Y, 0 + 1, false);
        faces[4] = new BlockRenderFace3D(nulli, yVec, xVec.negate());
        quads[4] = faces[4].getQuad(BoneRenderer.Direction.Z, 0 + 1, true);
        faces[5] = new BlockRenderFace3D(nulli, zVec.negate(), xVec);
        quads[5] = faces[5].getQuad(BoneRenderer.Direction.Y, 0, false);
        return new BlockRenderer(null, quads);
    }

}

package com.enbecko.objectcreator.restart;

import com.enbecko.objectcreator.ObjectCreatorMod;
import com.enbecko.objectcreator.TEBaseCreatorBlock;
import com.enbecko.objectcreator.TEBaseCreatorBlock.CreatorBlockRayTraceResult;
import com.enbecko.objectcreator.TEBaseCreatorBlock.RayTraceSide;
import com.enbecko.objectcreator.core.CreatorBlock;
import com.enbecko.objectcreator.core.LocalCoords;
import com.enbecko.objectcreator.core.vec3;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;


/**
 * Created by Niclas on 06.11.2016.
 */
public class TENewRenderer extends TileEntitySpecialRenderer<TENewBase> {

    private final vec3.Float tmp = new vec3.Float();

    public TENewRenderer() {
    }

    @Override
    public void renderTileEntityAt(TENewBase te, double x, double y, double z, float partialTicks, int destroyStage) {
        GL11.glPushMatrix();

        GL11.glTranslatef((float) x, (float) y + 1, (float) z);
        GL11.glPushMatrix();

        this.bindTexture(ObjectCreatorMod.WHITE);
        //GL11.glDisable(GL11.GL_CULL_FACE);
        te.bone.render(Tessellator.getInstance());
        //GL11.glEnable(GL11.GL_CULL_FACE);

        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }

    private void drawFaceOutline(LocalCoords coordSystem, @Nonnull CreatorBlockRayTraceResult result) {
        final RayTraceSide side = result.side;
        final CreatorBlock casted = result.casted;
        tmp.update(casted.pos);
        switch (side) {
            case X_pos:
                this.drawFullBlockOutline(tmp.addToThis(coordSystem.getNormalX()));
                break;
            case X_neg:
                this.drawFullBlockOutline(tmp.subFromThis(coordSystem.getNormalX()));
                break;
            case Y_pos:
                this.drawFullBlockOutline(tmp.addToThis(coordSystem.getNormalY()));
                break;
            case Y_neg:
                this.drawFullBlockOutline(tmp.subFromThis(coordSystem.getNormalY()));
                break;
            case Z_pos:
                this.drawFullBlockOutline(tmp.addToThis(coordSystem.getNormalZ()));
                break;
            case Z_neg:
                this.drawFullBlockOutline(tmp.subFromThis(coordSystem.getNormalZ()));
                break;
        }
    }

    private void drawFullBlockOutline(vec3 pos) {
        float xP = (float) pos.getXD(), yP = (float) pos.getYD(), zP = (float) pos.getZD();
        GL11.glVertex3f(xP, yP, zP);
        GL11.glVertex3f(xP, yP + 1, zP);

        GL11.glVertex3f(xP, yP + 1, zP);
        GL11.glVertex3f(xP, yP + 1, zP + 1);

        GL11.glVertex3f(xP, yP + 1, zP + 1);
        GL11.glVertex3f(xP, yP, zP + 1);

        GL11.glVertex3f(xP, yP, zP + 1);
        GL11.glVertex3f(xP, yP, zP);

        GL11.glVertex3f(xP, yP, zP);
        GL11.glVertex3f(xP + 1, yP, zP);

        GL11.glVertex3f(xP + 1, yP, zP);
        GL11.glVertex3f(xP + 1, yP + 1, zP);

        GL11.glVertex3f(xP + 1, yP + 1, zP);
        GL11.glVertex3f(xP, yP + 1, zP);

        GL11.glVertex3f(xP + 1, yP + 1, zP);
        GL11.glVertex3f(xP + 1, yP + 1, zP + 1);

        GL11.glVertex3f(xP + 1, yP + 1, zP + 1);
        GL11.glVertex3f(xP + 1, yP, zP + 1);

        GL11.glVertex3f(xP + 1, yP, zP + 1);
        GL11.glVertex3f(xP + 1, yP, zP);

        GL11.glVertex3f(xP, yP, zP + 1);
        GL11.glVertex3f(xP + 1, yP, zP + 1);

        GL11.glVertex3f(xP, yP + 1, zP + 1);
        GL11.glVertex3f(xP + 1, yP + 1, zP + 1);
    }

    private void drawFullBlockOutline(CreatorBlock block) {
       this.drawFullBlockOutline(block.pos);
    }
}

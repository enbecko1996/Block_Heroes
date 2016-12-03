package com.enbecko.objectcreator;

import com.enbecko.objectcreator.TEBaseCreatorBlock.CreatorBlockRayTraceResult;
import com.enbecko.objectcreator.TEBaseCreatorBlock.RayTraceSide;
import com.enbecko.objectcreator.core.LocalCoords;
import com.enbecko.objectcreator.core.CreatorBlock;
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
public class TEBaseCreatorBlockRenderer extends TileEntitySpecialRenderer<TEBaseCreatorBlock> {

    private final vec3.Float tmp = new vec3.Float();

    public TEBaseCreatorBlockRenderer() {
    }

    @Override
    public void renderTileEntityAt(TEBaseCreatorBlock te, double x, double y, double z, float partialTicks, int destroyStage) {
        GL11.glPushMatrix();

        BlockPos pos = te.getPos();
        LocalCoords coordSystem = te.coordSystem;
        vec3.Int center = coordSystem.getCenter();
        int dX = center.getX() - pos.getX();
        int dY = center.getY() - pos.getY();
        int dZ = center.getZ() - pos.getZ();

        GL11.glTranslatef((float) x + dX, (float) y + dY, (float) z + dZ);
        GL11.glPushMatrix();

        GL11.glTranslatef(coordSystem.rotationPoint.getX(), coordSystem.rotationPoint.getY(), coordSystem.rotationPoint.getZ());
        GL11.glRotatef(coordSystem.rotation.getX(), 1, 0, 0);
        GL11.glRotatef(coordSystem.rotation.getY(), 0, 1, 0);
        GL11.glRotatef(coordSystem.rotation.getZ(), 0, 0, 1);
        GL11.glTranslatef(- coordSystem.rotationPoint.getX(), - coordSystem.rotationPoint.getY(), - coordSystem.rotationPoint.getZ());

        this.bindTexture(TextureMap.LOCATION_MISSING_TEXTURE);
        te.renderCoordinateSystem(Tessellator.getInstance());

        GL11.glLineWidth(3);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glColor3f(1, 0, 0);

        CreatorBlockRayTraceResult result = te.rayTraceResult;
        if(result != null)
            this.drawFaceOutline(te.coordSystem, result);

        GL11.glColor3f(1, 1, 1);
        GL11.glEnd();

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

package com.enbecko.objectcreator.core;

import com.enbecko.objectcreator.TEBaseCreatorBlock;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.ojalgo.access.Access1D;
import org.ojalgo.access.Access2D;
import org.ojalgo.access.Factory1D;
import org.ojalgo.algebra.NormedVectorSpace;
import org.ojalgo.algebra.VectorSpace;
import org.ojalgo.array.Array1D;
import org.ojalgo.array.ArrayFactory;
import org.ojalgo.array.BufferArray;
import org.ojalgo.array.RationalArray;
import org.ojalgo.matrix.BasicMatrix;
import org.ojalgo.matrix.PrimitiveMatrix;
import org.ojalgo.matrix.store.PhysicalStore;
import org.ojalgo.matrix.store.PrimitiveDenseStore;
import org.ojalgo.matrix.transformation.Householder;
import org.ojalgo.random.Weibull;
import org.ojalgo.scalar.RationalNumber;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Niclas on 04.11.2016.
 */
public class LocalCoords <T extends vec3> implements Serializable{

    private final List<XCoordinateArrayList> coordSystem = new ArrayList<XCoordinateArrayList>();
    private final List<CreatorBlock> allBlocksUnsorted = new ArrayList<CreatorBlock>();
    private final List<LocalCoordsRenderableFaces2DInOneCoord> xDirectedFaces = new ArrayList<LocalCoordsRenderableFaces2DInOneCoord>();
    private final List<LocalCoordsRenderableFaces2DInOneCoord> yDirectedFaces = new ArrayList<LocalCoordsRenderableFaces2DInOneCoord>();
    private final List<LocalCoordsRenderableFaces2DInOneCoord> zDirectedFaces = new ArrayList<LocalCoordsRenderableFaces2DInOneCoord>();
    private final LocalCoordsRenderableFaces2DInOneCoord thePlaceHolderFace;
    private InterceptableFace3D face3DX = new InterceptableFace3D <vec3.Float> (vec3.vecPrec.FLOAT);
    private InterceptableFace3D face3DY = new InterceptableFace3D <vec3.Float> (vec3.vecPrec.FLOAT);
    private InterceptableFace3D face3DZ = new InterceptableFace3D <vec3.Float> (vec3.vecPrec.FLOAT);

    private Face2D face2DX = new Face2D <vec3.Float> (LocalCoordsRenderableFaces2DInOneCoord.CreatorBlockFaceDirection.X, vec3.vecPrec.FLOAT).setRayTracable();
    private Face2D face2DY = new Face2D <vec3.Float> (LocalCoordsRenderableFaces2DInOneCoord.CreatorBlockFaceDirection.Y, vec3.vecPrec.FLOAT).setRayTracable();
    private Face2D face2DZ = new Face2D <vec3.Float> (LocalCoordsRenderableFaces2DInOneCoord.CreatorBlockFaceDirection.Z, vec3.vecPrec.FLOAT).setRayTracable();

    private final vec3.Int center;
    private final vec3.Float tmpXPlus = new vec3.Float(), tmpYPlus = new vec3.Float(), tmpZPlus = new vec3.Float(), tmp = new vec3.Float();
    private final vec3.vecPrec precision;
    public final vec3.Float rotationPoint = new vec3.Float(0, 0, 0);
    public final vec3.Float rotation = new vec3.Float(0, 0, 0);
    private final T oneUnitX;
    private final T oneUnitY;
    private final T oneUnitZ;
    private final vec3.Double eX = new vec3.Double(1, 0, 0);
    private final vec3.Double eY = new vec3.Double(0, 1, 0);
    private final vec3.Double eZ = new vec3.Double(0, 0, 1);
    public static int scale = 1;

    @SuppressWarnings("unchecked")
    public LocalCoords(vec3.vecPrec precision) {
        this.center = new vec3.Int();
        this.precision = precision;
        oneUnitX = (T) vec3.newVecWithPrecision(precision, 1, 0, 0);
        oneUnitY = (T) vec3.newVecWithPrecision(precision, 0, 1, 0);
        oneUnitZ = (T) vec3.newVecWithPrecision(precision, 0, 0, 1);
        this.thePlaceHolderFace = new LocalCoordsRenderableFaces2DInOneCoord(this.precision);
    }

    public vec3.vecPrec getPrecision() {
        return this.precision;
    }

    public LocalCoords setCenter(BlockPos pos) {
        this.center.update(pos.getX(), pos.getY() + 1, pos.getZ());
        return this;
    }

    public T getNormalX() {
        return this.oneUnitX;
    }

    public T getNormalY() {
        return this.oneUnitY;
    }

    public T getNormalZ() {
        return this.oneUnitZ;
    }

    public void renderObj(@Nonnull Tessellator tessellator) {
        for (LocalCoordsRenderableFaces2DInOneCoord face : xDirectedFaces) {
            face.render(tessellator);
        }
        for (LocalCoordsRenderableFaces2DInOneCoord face : yDirectedFaces) {
            face.render(tessellator);
        }
        for (LocalCoordsRenderableFaces2DInOneCoord face : zDirectedFaces) {
            face.render(tessellator);
        }
    }

    @SideOnly(Side.CLIENT)
    public void makeVecToEye(vec3.Double eye) {
        for (CreatorBlock block : this.allBlocksUnsorted) {
            block.makeVecToEye(this.center, eye);
        }
    }

    @SideOnly(Side.CLIENT)
    public void getProbableRayTraceResults(@Nonnull List<CreatorBlock> toFill, @Nonnull vec3.Double cast, double reachDist) {
        toFill.clear();
        for (CreatorBlock block : this.allBlocksUnsorted) {
            double angle = Math.abs(Math.acos(cast.dot(block.vecToEye) / (cast.length() * block.vecToEye.length())));
            double limit = (90F / (block.vecToEye.length() + 1));
            if(angle < Math.toRadians(limit) && block.vecToEye.length() < reachDist)
                toFill.add(block);
        }
    }

    @Nullable
    @SideOnly(Side.CLIENT)
    public TEBaseCreatorBlock.CreatorBlockRayTraceResult rayTraceBlocks(final List<CreatorBlock> blocks, @Nonnull vec3.Double eyePos, @Nonnull vec3.Double lookVec) {
        double minDist = Double.MAX_VALUE;
        CreatorBlock theResultBlock = null;
        TEBaseCreatorBlock.RayTraceSide theResultSide = null;
        for (@Nonnull CreatorBlock block : blocks) {
            final boolean dXPos = block.vecToEye.x < 0, dYPos = block.vecToEye.y < 0, dZPos = block.vecToEye.z < 0;
            final int posX = this.center.getX(), posY = this.center.getY(), posZ = this.center.getZ();
            final double blockPosX = block.pos.getXD(), blockPosY = block.pos.getYD(), blockPosZ = block.pos.getZD();
            this.tmp.update(this.center);
            this.tmp.addToThis(block.pos);

            this.tmp.addToThisAndInsertInto(this.oneUnitX, this.tmpXPlus);
            this.tmp.addToThisAndInsertInto(this.oneUnitY, this.tmpYPlus);
            this.tmp.addToThisAndInsertInto(this.oneUnitZ, this.tmpZPlus);

            this.face3DX.updateBounds(this.tmp, this.tmpYPlus, this.tmpZPlus);
            this.face3DY.updateBounds(this.tmp, this.tmpZPlus, this.tmpXPlus);
            this.face3DZ.updateBounds(this.tmp, this.tmpYPlus, this.tmpXPlus);


            if (dXPos) {
                this.face3DX.moveAlong(this.oneUnitX);
            }
            if (dYPos) {
                this.face3DY.moveAlong(this.oneUnitY);
            }
            if (dZPos) {
                this.face3DZ.moveAlong(this.oneUnitZ);
            }

            System.out.println(face3DX + " " + face3DY + " " + face3DZ);

            final double xFaceDist = this.rayTraceFace(LocalCoordsRenderableFaces2DInOneCoord.CreatorBlockFaceDirection.X, face3DX, eyePos, lookVec);
            final double yFaceDist = this.rayTraceFace(LocalCoordsRenderableFaces2DInOneCoord.CreatorBlockFaceDirection.Y, face3DY, eyePos, lookVec);
            final double zFaceDist = this.rayTraceFace(LocalCoordsRenderableFaces2DInOneCoord.CreatorBlockFaceDirection.Z, face3DZ, eyePos, lookVec);
            if (xFaceDist < minDist || yFaceDist < minDist || zFaceDist < minDist)
                theResultBlock = block;
            if (xFaceDist < minDist) {
                minDist = xFaceDist;
                if (dXPos)
                    theResultSide = TEBaseCreatorBlock.RayTraceSide.X_pos;
                else
                    theResultSide = TEBaseCreatorBlock.RayTraceSide.X_neg;
            }
            if (yFaceDist < minDist) {
                minDist = yFaceDist;
                if (dYPos)
                    theResultSide = TEBaseCreatorBlock.RayTraceSide.Y_pos;
                else
                    theResultSide = TEBaseCreatorBlock.RayTraceSide.Y_neg;
            }
            if (zFaceDist < minDist) {
                minDist = zFaceDist;
                if (dZPos)
                    theResultSide = TEBaseCreatorBlock.RayTraceSide.Z_pos;
                else
                    theResultSide = TEBaseCreatorBlock.RayTraceSide.Z_neg;
            }

        }
        if(theResultBlock != null && theResultSide != null && minDist < Double.MAX_VALUE)
            return new TEBaseCreatorBlock.CreatorBlockRayTraceResult(theResultBlock, theResultSide);
        return null;
/**
        //for (@Nonnull CreatorBlock block : blocks) {
          //  final boolean dXPos = block.vecToEye.x < 0, dYPos = block.vecToEye.y < 0, dZPos = block.vecToEye.z < 0;
          //  final int posX = this.center.getX(), posY = this.center.getY(), posZ = this.center.getZ();
          //  final double blockPosX = block.pos.getXD(), blockPosY = block.pos.getYD(), blockPosZ = block.pos.getZD();
            face2DX.updateBounds(posZ + blockPosZ * scale, posY + blockPosY * scale, posZ + (blockPosZ + 1) * scale, posY + (blockPosY + 1) * scale);
            face2DY.updateBounds(posX + blockPosX * scale, posZ + blockPosZ * scale, posX + (blockPosX + 1) * scale, posZ + (blockPosZ + 1) * scale);
            face2DZ.updateBounds(posX + blockPosX * scale, posY + blockPosY * scale, posX + (blockPosX + 1) * scale, posY + (blockPosY + 1) * scale);
            if (dXPos)
                face2DX.updateCoordinate(posX + (blockPosX + 1) * scale);
            else
                face2DX.updateCoordinate(posX + (blockPosX) * scale);
            if (dYPos)
                face2DY.updateCoordinate(posY + (blockPosY + 1) * scale);
            else
                face2DY.updateCoordinate(posY + (blockPosY) * scale);
            if (dZPos)
                face2DZ.updateCoordinate(posZ + (blockPosZ + 1) * scale);
            else
                face2DZ.updateCoordinate(posZ + (blockPosZ));
            final double xFaceDist = this.rayTraceFace(null, face2DX, eyePos, lookVec);
            final double yFaceDist = this.rayTraceFace(null, face2DY, eyePos, lookVec);
            final double zFaceDist = this.rayTraceFace(null, face2DZ, eyePos, lookVec);
            if (xFaceDist < minDist || yFaceDist < minDist || zFaceDist < minDist)
                theResultBlock = block;
            if (xFaceDist < minDist) {
                minDist = xFaceDist;
                if (dXPos)
                    theResultSide = TEBaseCreatorBlock.RayTraceSide.X_pos;
                else
                    theResultSide = TEBaseCreatorBlock.RayTraceSide.X_neg;
            }
            if (yFaceDist < minDist) {
                minDist = yFaceDist;
                if (dYPos)
                    theResultSide = TEBaseCreatorBlock.RayTraceSide.Y_pos;
                else
                    theResultSide = TEBaseCreatorBlock.RayTraceSide.Y_neg;
            }
            if (zFaceDist < minDist) {
                minDist = zFaceDist;
                if (dZPos)
                    theResultSide = TEBaseCreatorBlock.RayTraceSide.Z_pos;
                else
                    theResultSide = TEBaseCreatorBlock.RayTraceSide.Z_neg;
            }

        }
        if(theResultBlock != null && theResultSide != null && minDist < Double.MAX_VALUE)
            return new TEBaseCreatorBlock.CreatorBlockRayTraceResult(theResultBlock, theResultSide);
        return null;*/

    }

    private double rayTraceFace(@Deprecated LocalCoordsRenderableFaces2DInOneCoord.CreatorBlockFaceDirection direction, Face2D face3D, @Nonnull vec3.Double start, @Nonnull vec3.Double lookVec) {
        vec3.Double result = face3D.rayTrace(start, lookVec);
        if(result != null)
            return result.subFromThis(start).length();
        return Double.MAX_VALUE;
    }

    private double rayTraceFace(@Deprecated LocalCoordsRenderableFaces2DInOneCoord.CreatorBlockFaceDirection direction, InterceptableFace3D face3D, @Nonnull vec3.Double start, @Nonnull vec3.Double lookVec) {
        vec3.Double result = face3D.checkIfCrosses(direction, start, lookVec);
        if(result != null)
            return result.subFromThis(start).length();
        return Double.MAX_VALUE;
    }

    @Nullable
    public CreatorBlock setBlockAt(double x, double y, double z, @Nonnull CreatorBlock block, final boolean updateFaces) {
        if(block == null)
            throw new RuntimeException("For convenience reasons only use NonNull Creatorblocks when using setBlockAt()");
        for (XCoordinateArrayList xCoord : coordSystem) {
            if (xCoord.x == x) {
                CreatorBlock tmpBlock = xCoord.setBlockAt(x, y, z, block);
                if(tmpBlock != null) {
                    this.allBlocksUnsorted.add(tmpBlock);
                    this.addBlockToCorrespondingFaces(tmpBlock, x, y, z);
                    if (updateFaces)
                        this.updateAllFacesThatContain(x, y, z);
                    return tmpBlock;
                }
            }
        }
        XCoordinateArrayList newXCoord;
        this.coordSystem.add(newXCoord = new XCoordinateArrayList(x));
        CreatorBlock tmpBlock = newXCoord.setBlockAt(x, y, z, block);
        if(tmpBlock != null) {
            this.allBlocksUnsorted.add(tmpBlock);
            this.addBlockToCorrespondingFaces(tmpBlock, x, y, z);
            if (updateFaces)
                this.updateAllFacesThatContain(x, y, z);
            return tmpBlock;
        } else {
            return null;
        }
    }

    public boolean setAir(double x, double y, double z) {
        for (XCoordinateArrayList xCoord : coordSystem) {
            if (xCoord.x == x) {
                if (xCoord.setAir(x, y, z) && xCoord.isEmpty()) {
                    this.coordSystem.remove(xCoord);
                    return true;
                }
            }
        }
        return false;
    }

    @Nullable
    public CreatorBlock getBlockAt(double x, double y, double z) {
        for (XCoordinateArrayList xCoord : coordSystem) {
            if (xCoord.x == x)
                return xCoord.getBlockAt(y, z);
        }
        return null;
    }

    private List<CreatorBlock> getAllBlocks() {
        this.allBlocksUnsorted.clear();
        for (XCoordinateArrayList xCoord : coordSystem) {
            for (YCoordinateArrayList yCoords : xCoord.yCoords) {
                for (CreatorBlock block : yCoords.blocks) {
                    if(block != null)
                        this.allBlocksUnsorted.add(block);
                }
            }
        }
        return this.allBlocksUnsorted;
    }

    @SideOnly(Side.CLIENT)
    public void updateAllFaces() {
        for (LocalCoordsRenderableFaces2DInOneCoord xFace : this.xDirectedFaces) {
            this.updateFace(LocalCoordsRenderableFaces2DInOneCoord.CreatorBlockFaceDirection.X, xFace.coordinate, true);
        }
        for (LocalCoordsRenderableFaces2DInOneCoord yFace : this.yDirectedFaces) {
            this.updateFace(LocalCoordsRenderableFaces2DInOneCoord.CreatorBlockFaceDirection.Y, yFace.coordinate, true);
        }
        for (LocalCoordsRenderableFaces2DInOneCoord zFace : this.zDirectedFaces) {
            this.updateFace(LocalCoordsRenderableFaces2DInOneCoord.CreatorBlockFaceDirection.Z, zFace.coordinate, true);
        }
    }

    @SideOnly(Side.CLIENT)
    public void updateAllFacesThatContain(double x, double y, double z) {
        System.out.println("updating all");
        this.updateFace(LocalCoordsRenderableFaces2DInOneCoord.CreatorBlockFaceDirection.X, x, false);
        this.updateFace(LocalCoordsRenderableFaces2DInOneCoord.CreatorBlockFaceDirection.Y, y, false);
        this.updateFace(LocalCoordsRenderableFaces2DInOneCoord.CreatorBlockFaceDirection.Z, z, false);
    }

    @SideOnly(Side.CLIENT)
    public boolean updateFace(LocalCoordsRenderableFaces2DInOneCoord.CreatorBlockFaceDirection direction, double coord, boolean onlyBuildMe) {
        LocalCoordsRenderableFaces2DInOneCoord[] prevCurNext = this.getPrevCurAndNext(direction, coord);
        LocalCoordsRenderableFaces2DInOneCoord prev = prevCurNext[0], cur = prevCurNext[1], next = prevCurNext[2];

        if(cur != null) {
            if(!onlyBuildMe) {
                if (prev != null)
                    prev.createRenderableFaces(null, cur, true);
                if (next != null)
                    next.createRenderableFaces(cur, null, true);
            }
            return cur.createRenderableFaces(prev, next, false);
        } else if(!onlyBuildMe){
            if (prev != null)
                prev.createRenderableFaces(null, this.thePlaceHolderFace, true);
            if(next != null)
                next.createRenderableFaces(this.thePlaceHolderFace, null, true);
            return true;
        }
        return false;
    }

    @Nonnull
    public LocalCoordsRenderableFaces2DInOneCoord[] getPrevCurAndNext(LocalCoordsRenderableFaces2DInOneCoord.CreatorBlockFaceDirection direction, double coord) {
        LocalCoordsRenderableFaces2DInOneCoord[] out = new LocalCoordsRenderableFaces2DInOneCoord[3];
        switch (direction) {
            case X:
                for (int x = 0; x < this.xDirectedFaces.size(); x++) {
                    LocalCoordsRenderableFaces2DInOneCoord xFace = this.xDirectedFaces.get(x);
                    if(xFace.coordinate == coord) {
                        out[0] = x - 1 >= 0 ? this.xDirectedFaces.get(x - 1).coordinate == coord - 1 ? this.xDirectedFaces.get(x-1) : null : null;
                        out[1] = xFace;
                        out[2] = x + 1 < this.xDirectedFaces.size() ? this.xDirectedFaces.get(x + 1).coordinate == coord + 1 ? this.xDirectedFaces.get(x+1) : null : null;
                        break;
                    }
                }
                break;
            case Y:
                for (int y = 0; y < this.yDirectedFaces.size(); y++) {
                    LocalCoordsRenderableFaces2DInOneCoord yFace = this.yDirectedFaces.get(y);
                    if(yFace.coordinate == coord) {
                        out[0] = y - 1 >= 0 ? this.yDirectedFaces.get(y - 1).coordinate == coord - 1 ? this.yDirectedFaces.get(y-1) : null : null;
                        out[1] = yFace;
                        out[2] = y + 1 < this.yDirectedFaces.size() ? this.yDirectedFaces.get(y + 1).coordinate == coord + 1 ? this.yDirectedFaces.get(y+1) : null : null;
                        break;
                    }
                }
                break;
            case Z:
                for (int z = 0; z < this.zDirectedFaces.size(); z++) {
                    LocalCoordsRenderableFaces2DInOneCoord zFace = this.zDirectedFaces.get(z);
                    if(zFace.coordinate == coord) {
                        out[0] = z - 1 >= 0 ? this.zDirectedFaces.get(z - 1).coordinate == coord - 1 ? this.zDirectedFaces.get(z-1) : null : null;
                        out[1] = zFace;
                        out[2] = z + 1 < this.zDirectedFaces.size() ? this.zDirectedFaces.get(z + 1).coordinate == coord + 1 ? this.zDirectedFaces.get(z+1) : null : null;
                        break;
                    }
                }
                break;
        }
        return out;
    }

    public void addBlockToCorrespondingFaces(final CreatorBlock block, double x, double y, double z) {
        LocalCoordsRenderableFaces2DInOneCoord xFace, yFace, zFace;
        xTest:
        if ((xFace = this.faceXDirectAt(x)) != null)
            xFace.add(block);
        else {
            for (int k = 0; k < this.xDirectedFaces.size(); k++) {
                if (this.xDirectedFaces.get(k).coordinate > x) {
                    this.xDirectedFaces.add(k, new LocalCoordsRenderableFaces2DInOneCoord(LocalCoordsRenderableFaces2DInOneCoord.CreatorBlockFaceDirection.X, x, this.precision).add(block));
                    break xTest;
                }
            }
            this.xDirectedFaces.add(new LocalCoordsRenderableFaces2DInOneCoord(LocalCoordsRenderableFaces2DInOneCoord.CreatorBlockFaceDirection.X, x, this.precision).add(block));
        }

        yTest:
        if ((yFace = this.faceYDirectAt(y)) != null)
            yFace.add(block);
        else {
            for (int k = 0; k < this.yDirectedFaces.size(); k++) {
                if (this.yDirectedFaces.get(k).coordinate > y) {
                    this.yDirectedFaces.add(k, new LocalCoordsRenderableFaces2DInOneCoord(LocalCoordsRenderableFaces2DInOneCoord.CreatorBlockFaceDirection.Y, y, this.precision).add(block));
                    break yTest;
                }
            }
            this.yDirectedFaces.add(new LocalCoordsRenderableFaces2DInOneCoord(LocalCoordsRenderableFaces2DInOneCoord.CreatorBlockFaceDirection.Y, y, this.precision).add(block));
        }

        zTest:
        if ((zFace = this.faceZDirectAt(z)) != null)
            zFace.add(block);
        else {
            for (int k = 0; k < this.zDirectedFaces.size(); k++) {
                if (this.zDirectedFaces.get(k).coordinate > z) {
                    this.zDirectedFaces.add(k, new LocalCoordsRenderableFaces2DInOneCoord(LocalCoordsRenderableFaces2DInOneCoord.CreatorBlockFaceDirection.Z, z, this.precision).add(block));
                    break zTest;
                }
            }
            this.zDirectedFaces.add(new LocalCoordsRenderableFaces2DInOneCoord(LocalCoordsRenderableFaces2DInOneCoord.CreatorBlockFaceDirection.Z, z, this.precision).add(block));
        }
    }

    public void removeBlockFromCorrespondingFaces(double x, double y, double z) {
        LocalCoordsRenderableFaces2DInOneCoord xFace, yFace, zFace;
        if ((xFace = this.faceXDirectAt(x)) != null) {
            if(!xFace.isEmpty()) {
                if(xFace.remove(x, y, z)) {
                    if(xFace.isEmpty())
                        this.xDirectedFaces.remove(xFace);
                }
            }
        }

        if ((yFace = this.faceYDirectAt(y)) != null) {
            if(!yFace.isEmpty()) {
                if(yFace.remove(x, y, z)) {
                    if(yFace.isEmpty())
                        this.yDirectedFaces.remove(yFace);
                }
            }
        }

        if ((zFace = this.faceZDirectAt(z)) != null) {
            if(!zFace.isEmpty()) {
                if(zFace.remove(x, y, z)) {
                    if(zFace.isEmpty())
                        this.zDirectedFaces.remove(zFace);
                }
            }
        }
    }

    @Nullable
    public LocalCoordsRenderableFaces2DInOneCoord faceXDirectAt(final double posX) {
        for (LocalCoordsRenderableFaces2DInOneCoord face : this.xDirectedFaces) {
            if(face.coordinate == posX) {
                return face;
            }
        }
        return null;
    }

    @Nullable
    public LocalCoordsRenderableFaces2DInOneCoord faceYDirectAt(double posY) {
        for (LocalCoordsRenderableFaces2DInOneCoord face : this.yDirectedFaces) {
            if(face.coordinate == posY) {
                return face;
            }
        }
        return null;
    }

    @Nullable
    public LocalCoordsRenderableFaces2DInOneCoord faceZDirectAt(double posZ) {
        for (LocalCoordsRenderableFaces2DInOneCoord face : this.zDirectedFaces) {
            if(face.coordinate == posZ) {
                return face;
            }
        }
        return null;
    }

    public String toString() {
        return Arrays.toString(this.getAllBlocks().toArray(new CreatorBlock[this.allBlocksUnsorted.size()]));
    }

    public boolean isEmpty() {
        return this.coordSystem.isEmpty();
    }

    private class XCoordinateArrayList{
        public final double x;
        private final List<YCoordinateArrayList> yCoords = new ArrayList<YCoordinateArrayList>();

        public XCoordinateArrayList(double x) {
            this.x = x;
        }

        @Nullable
        public CreatorBlock getBlockAt(double y, double z) {
            for (YCoordinateArrayList yCoord : yCoords) {
                if (yCoord.y == y)
                    return yCoord.getBlockAt(z);
            }
            return null;
        }

        public boolean setAir(double x, double y, double z) {
            for (YCoordinateArrayList yCoord : yCoords) {
                if (yCoord.y == y) {
                    if (yCoord.setAir(x, y, z) && yCoord.isEmpty()) {
                        this.yCoords.remove(yCoord);
                        return true;
                    }
                }
            }
            return false;
        }

        @Nullable
        public CreatorBlock setBlockAt(double x, double y, double z, CreatorBlock block) {
            for (YCoordinateArrayList yCoord : yCoords) {
                if (yCoord.y == y)
                    return yCoord.setBlockAt(x, y, z, block);
            }
            YCoordinateArrayList newYCoord;
            this.yCoords.add(newYCoord = new YCoordinateArrayList(y));
            return newYCoord.setBlockAt(x, y, z, block);
        }

        public boolean isEmpty() {
            return this.yCoords.isEmpty();
        }
    }

    private class YCoordinateArrayList {
        public final double y;
        private final List<CreatorBlock> blocks = new ArrayList<CreatorBlock>();

        public YCoordinateArrayList(double y) {
            this.y = y;
        }

        @Nullable
        public CreatorBlock getBlockAt(double z) {
            for (CreatorBlock block : blocks) {
                if(block.pos.getZD() == z)
                    return block;
            }
            return null;
        }

        public boolean setAir(double x, double y, double z) {
            for (CreatorBlock block : blocks) {
                if(block.pos.getZD() == z) {
                    this.blocks.remove(block);
                    allBlocksUnsorted.remove(block);
                    removeBlockFromCorrespondingFaces(x, y, z);
                    updateAllFacesThatContain(x, y, z);
                    return true;
                }
            }
            return false;
        }

        @Nullable
        public CreatorBlock setBlockAt(double x, double y, double z, CreatorBlock blockToSet) {
            for (CreatorBlock block : blocks) {
                if(block.pos.getZD() == z)
                    return null;
            }
            CreatorBlock setBlock;
            this.blocks.add(setBlock = new CreatorBlock(blockToSet, x, y, z, precision));
            return setBlock;
        }

        public boolean isEmpty() {
            return this.blocks.isEmpty();
        }
    }

    public vec3.Int getCenter() {
        return this.center;
    }
}

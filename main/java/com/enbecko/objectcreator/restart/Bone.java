package com.enbecko.objectcreator.restart;

import com.enbecko.objectcreator.core.vec3;
import net.minecraft.client.renderer.Tessellator;
import org.jetbrains.annotations.Contract;
import org.ojalgo.access.ColumnView;
import org.ojalgo.matrix.PrimitiveMatrix;
import org.ojalgo.matrix.store.MatrixStore;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.enbecko.objectcreator.TEBaseCreatorBlock.CreatorBlockRayTraceResult;
import org.ojalgo.matrix.store.PrimitiveDenseStore;
import org.ojalgo.matrix.transformation.Rotation;

/**
 * Created by Niclas on 19.11.2016.
 */
public class Bone implements IVectorSpace {

    private int size;
    private final Boundaries3D boundaries = new Boundaries3D(Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE);
    /**
     * @param blocks
     * Array containing all blocks with maximum size
     */
    private Block3D[][][] blocks;
    private BoneRenderer boneRenderer;
    private final List<BlockRenderer> blockRenderers = new ArrayList<BlockRenderer>();
    private PrimitiveMatrix transform = PrimitiveMatrix.FACTORY.makeEye(4, 4);
    private final vec3.Float rotation = new vec3.Float();
    double[] transformAsArray = new double[(int) this.transform.countColumns() * (int) this.transform.countRows()];
    double[] inverseTransformAsArray = new double[(int) this.transform.countColumns() * (int) this.transform.countRows()];
    private PrimitiveMatrix inverseTransform = PrimitiveMatrix.FACTORY.makeEye(4, 4);

    public Bone(int size) {
        this.size = size;
        this.blocks = new Block3D[size][size][size];
        this.setBlockAt(1, 1, 1, new Block3D(1, 1, 1, 0, 255, 255));
        this.setBlockAt(1, 2, 1, new Block3D(1, 1, 1));
        this.setBlockAt(1, 1, 2, new Block3D(1, 1, 1));
        this.setBlockAt(1, 2, 2, new Block3D(1, 1, 1));
        this.setBlockAt(1, 5, 2, new Block3D(1, 1, 1));
        this.setBlockAt(1, 5, 1, new Block3D(1, 1, 1));
        this.setBlockAt(1, 4, 1, new Block3D(1, 1, 1));
        this.setBlockAt(1, 3, 1, new Block3D(1, 1, 1));
        this.setBlockAt(1, 5, 3, new Block3D(1, 1, 1));
        this.setAir(1, 5, 3);

        this.setBlockAt(2, 1, 1, new Block3D(1, 1, 1));
        this.setBlockAt(2, 1, 0, new Block3D(1, 1, 1));
        this.setBlockAt(2, 0, 1, new Block3D(1, 1, 1));
    }

    @Nullable
    public CreatorBlockRayTraceResult getRayTraceResult(double distance, RayTrace3D rayTrace3D) {
        PrimitiveDenseStore on = rayTrace3D.getOnPoint();
        PrimitiveDenseStore ve = rayTrace3D.getVec1();
        this.transform = this.arrayToMatrix(this.transformAsArray);
        this.inverseTransform = this.arrayToMatrix(this.inverseTransformAsArray);
        System.out.println(this.transform);
        System.out.println(this.inverseTransform);
        System.out.println("before: "+on+" "+ve);
        MatrixStore<Double> store = this.inverseTransform.toPrimitiveStore();
        MatrixStore<Double> onP = store.multiply(on);
        MatrixStore<Double> vecP = store.multiply(ve);
        System.out.println("after: "+onP+" "+vecP);
        store = this.transform.toPrimitiveStore();
        onP = store.multiply(on);
        vecP = store.multiply(ve);
        System.out.println("after: "+onP+" "+vecP);
        return null;
    }

    public void rotate(float angle, float x, float y, float z) {
        this.fillArrayWithTransform(transform, transformAsArray);
        rotation.update(x, y, z);
        rotation.normalize();
        if (!Float.isNaN(rotation.x) && !Float.isNaN(rotation.y) && !Float.isNaN(rotation.z) && !Float.isInfinite(rotation.x) && !Float.isInfinite(rotation.y) && !Float.isInfinite(rotation.z)) {
            Matrix.rotateM(this.transformAsArray, 0, angle, x, y, z);
            Matrix.invertM(this.inverseTransformAsArray, 0, this.transformAsArray, 0);
            // Matrix.rotateM(this.inverseTransformAsArray, 0, - angle, x, y, z);
            /**this.transform = PrimitiveMatrix.FACTORY.columns(new double[]{this.transformAsArray[0], this.transformAsArray[1], this.transformAsArray[2], this.transformAsArray[3]},
                    new double[]{this.transformAsArray[4], this.transformAsArray[5], this.transformAsArray[6], this.transformAsArray[7]},
                    new double[]{this.transformAsArray[8], this.transformAsArray[9], this.transformAsArray[10], this.transformAsArray[11]},
                    new double[]{this.transformAsArray[12], this.transformAsArray[13], this.transformAsArray[14], this.transformAsArray[15]});
            this.inverseTransform = this.transform.invert();
            /**  this.inverseTransform =  PrimitiveMatrix.FACTORY.columns(new double[] {this.inverseTransformAsArray[0], this.inverseTransformAsArray[1], this.inverseTransformAsArray[2], this.inverseTransformAsArray[3]},
             new double[] {this.inverseTransformAsArray[4], this.inverseTransformAsArray[5], this.inverseTransformAsArray[6], this.inverseTransformAsArray[7]},
             new double[] {this.inverseTransformAsArray[8], this.inverseTransformAsArray[9], this.inverseTransformAsArray[10], this.inverseTransformAsArray[11]},
             new double[] {this.inverseTransformAsArray[12], this.inverseTransformAsArray[13], this.inverseTransformAsArray[14], this.inverseTransformAsArray[15]});
             */
        }
    }

    public void scale(float x, float y, float z) {
        this.fillArrayWithTransform(transform, transformAsArray);
        Matrix.scaleM(this.transformAsArray, 0, x, y, z);
        Matrix.invertM(this.inverseTransformAsArray, 0, this.transformAsArray, 0);
        // Matrix.rotateM(this.inverseTransformAsArray, 0, - angle, x, y, z);
        /**this.transform =  PrimitiveMatrix.FACTORY.columns(new double[] {this.transformAsArray[0], this.transformAsArray[1], this.transformAsArray[2], this.transformAsArray[3]},
                new double[] {this.transformAsArray[4], this.transformAsArray[5], this.transformAsArray[6], this.transformAsArray[7]},
                new double[] {this.transformAsArray[8], this.transformAsArray[9], this.transformAsArray[10], this.transformAsArray[11]},
                new double[] {this.transformAsArray[12], this.transformAsArray[13], this.transformAsArray[14], this.transformAsArray[15]});
        this.inverseTransform = this.transform.invert();
        /**  this.inverseTransform =  PrimitiveMatrix.FACTORY.columns(new double[] {this.inverseTransformAsArray[0], this.inverseTransformAsArray[1], this.inverseTransformAsArray[2], this.inverseTransformAsArray[3]},
         new double[] {this.inverseTransformAsArray[4], this.inverseTransformAsArray[5], this.inverseTransformAsArray[6], this.inverseTransformAsArray[7]},
         new double[] {this.inverseTransformAsArray[8], this.inverseTransformAsArray[9], this.inverseTransformAsArray[10], this.inverseTransformAsArray[11]},
         new double[] {this.inverseTransformAsArray[12], this.inverseTransformAsArray[13], this.inverseTransformAsArray[14], this.inverseTransformAsArray[15]});
         */
    }

    @Contract(pure = true)
    public PrimitiveMatrix arrayToMatrix(double[] array) {
        return PrimitiveMatrix.FACTORY.columns(new double[]{array[0], array[1], array[2], array[3]},
                new double[]{array[4], array[5], array[6], array[7]},
                new double[]{array[8], array[9], array[10], array[11]},
                new double[]{array[12], array[13], array[14], array[15]});
    }

    private void fillArrayWithTransform(PrimitiveMatrix matrix, double[] array) {
        Iterable<ColumnView<Number>> columns = matrix.columns();
        int it = 0;
        for (ColumnView<Number> columnView : columns) {
            final long leng = columnView.count();
            for (int k = 0; k < leng; k++) {
                double nm = (Double) columnView.get(k);
                array[it * 4 + k] = (float) nm;
            }
            it++;
        }
    }

    public void unifyAndMakeBoneRenderer() {
        this.boneRenderer = BoneRenderer.makeNewBoneRenderer(this.blocks, this.boundaries);
        this.finalizeForRender();
        this.blockRenderers.clear();
    }

    public void render(Tessellator tessellator) {
        if(this.boneRenderer != null)
            this.boneRenderer.render(tessellator);
        this.renderBlocksSingular(tessellator);
    }

    private void renderBlocksSingular(Tessellator tessellator) {
        for (BlockRenderer blockRenderer : this.blockRenderers) {
            blockRenderer.render(tessellator);
        }
    }

    private void finalizeForRender() {
        this.boneRenderer.getQuads();
    }

    private boolean updateSize(int newSize) {
        int fillLen;
        if (newSize > this.size) {
            fillLen = this.blocks.length;
        } else if(newSize < this.size) {
            fillLen = newSize;
        } else {
            return false;
        }
        Block3D[][][] tmpBlockArr = null;
        try {
            tmpBlockArr = new Block3D[newSize][newSize][newSize];
            for (int x = 0; x < fillLen; x++) {
                for (int y = 0; y < fillLen; y++) {
                    for (int z = 0; z < fillLen; z++) {
                        tmpBlockArr[x][y][z] = this.blocks[x][y][z];
                    }
                }
            }
        } catch (StackOverflowError ex) {
            ex.printStackTrace();
            tmpBlockArr = null;
        }
        if(tmpBlockArr != null) {
            this.blocks = tmpBlockArr;
            return true;
        }
        return false;
    }

    public boolean setBlockAt(int x, int y, int z, @Nonnull Block3D block3D) {
        if(block3D != null) {
            if(x < this.size && y < this.size && z < this.size) {
                if (this.blocks[x][y][z] == null) {
                    this.blocks[x][y][z] = new Block3D(block3D, x, y, z);
                    blockRenderers.add(BlockRenderer.getNewBlockRenderer(this, this.blocks[x][y][z]));
                    if (x > this.boundaries.xMax)
                        this.boundaries.xMax = x;
                    if (x < this.boundaries.xMin)
                        this.boundaries.xMin = x;
                    if (y > this.boundaries.yMax)
                        this.boundaries.yMax = y;
                    if (y < this.boundaries.yMin)
                        this.boundaries.yMin = y;
                    if (z > this.boundaries.zMax)
                        this.boundaries.zMax = z;
                    if (z < this.boundaries.zMin)
                        this.boundaries.zMin = z;
                    return true;
                }
            }
        }
        return false;
    }

    public boolean setAir(int x, int y, int z) {
        if (this.blocks[x][y][z] != null) {
            this.blocks[x][y][z] = null;
            this.reduceSizeIfPossible(x, y, z);
            return true;
        }
        return false;
    }

    public void reduceSizeIfPossible(int x, int y, int z) {
        if(x == this.boundaries.xMax || x == this.boundaries.xMin) {
            boolean removeX = true;
            tester:
            for (int dY = this.boundaries.yMin; dY < this.boundaries.yMax; dY++) {
                for (int dZ = this.boundaries.zMin; dZ < this.boundaries.zMax; dZ++) {
                    if (this.blocks[x][dY][dZ] != null) {
                        removeX = false;
                        break tester;
                    }
                }
            }
            if(removeX) {
                if (x == this.boundaries.xMax) {
                    if (x == this.boundaries.xMin) {
                        this.boundaries.xMax = Integer.MIN_VALUE;
                        this.boundaries.xMin = Integer.MAX_VALUE;
                    } else {
                        boolean did = false;
                        tester:
                        for (int k = x; k > this.boundaries.xMin; k--) {
                            for (int dY = this.boundaries.yMin; dY <= this.boundaries.yMax; dY++) {
                                for (int dZ = this.boundaries.zMin; dZ <= this.boundaries.zMax; dZ++) {
                                    if(this.blocks[k][dY][dZ] != null) {
                                        this.boundaries.xMax = k;
                                        did = true;
                                        break tester;
                                    }
                                }
                            }
                        }
                        if(!did)
                            this.boundaries.xMax = Integer.MIN_VALUE;
                    }
                }
                if (x == this.boundaries.xMin) {
                    boolean did = false;
                    tester:
                    for (int k = x; k <= this.boundaries.xMax; k++) {
                        for (int dY = this.boundaries.yMin; dY <= this.boundaries.yMax; dY++) {
                            for (int dZ = this.boundaries.zMin; dZ <= this.boundaries.zMax; dZ++) {
                                if(this.blocks[k][dY][dZ] != null) {
                                    this.boundaries.xMax = k;
                                    did = true;
                                    break tester;
                                }
                            }
                        }
                    }
                    if(!did)
                        this.boundaries.xMin = Integer.MAX_VALUE;
                }
            }
        }
        if(y == this.boundaries.yMax || y == this.boundaries.yMin) {
            boolean removeY = true;
            tester:
            for (int dX = this.boundaries.xMin; dX < this.boundaries.xMax; dX++) {
                for (int dZ = this.boundaries.zMin; dZ < this.boundaries.zMax; dZ++) {
                    if (this.blocks[dX][y][dZ] != null) {
                        removeY = false;
                        break tester;
                    }

                }
            }
            if (removeY) {
                if (y == this.boundaries.yMax) {
                    if (y == this.boundaries.yMin) {
                        this.boundaries.yMax = Integer.MIN_VALUE;
                        this.boundaries.yMin = Integer.MAX_VALUE;
                    } else {
                        boolean did = false;
                        tester:
                        for (int k = y; k > this.boundaries.yMin; k--) {
                            for (int dX = this.boundaries.xMin; dX <= this.boundaries.xMax; dX++) {
                                for (int dZ = this.boundaries.zMin; dZ <= this.boundaries.zMax; dZ++) {
                                    if(this.blocks[dX][k][dZ] != null) {
                                        this.boundaries.yMax = k;
                                        did = true;
                                        break tester;
                                    }
                                }
                            }
                        }
                        if(!did)
                            this.boundaries.yMax = Integer.MIN_VALUE;
                    }
                }
            }
            if (y == this.boundaries.yMin) {
                boolean did = false;
                tester:
                for (int k = y; k <= this.boundaries.yMax; k++) {
                    for (int dX = this.boundaries.xMin; dX <= this.boundaries.xMax; dX++) {
                        for (int dZ = this.boundaries.zMin; dZ <= this.boundaries.zMax; dZ++) {
                            if(this.blocks[dX][k][dZ] != null) {
                                this.boundaries.yMin = k;
                                did = true;
                                break tester;
                            }
                        }
                    }
                }
                if(!did)
                    this.boundaries.yMin = Integer.MAX_VALUE;
            }
        }
        if(z == this.boundaries.zMax || z == this.boundaries.zMin) {
            boolean removeZ = true;
            tester:
            for (int dX = this.boundaries.xMin; dX < this.boundaries.xMax; dX++) {
                for (int dY = this.boundaries.yMin; dY < this.boundaries.yMax; dY++) {
                    if (this.blocks[dX][dY][z] != null) {
                        removeZ = false;
                        break tester;
                    }

                }
            }
            if (removeZ) {
                if (z == this.boundaries.zMax) {
                    if (z == this.boundaries.zMin) {
                        this.boundaries.zMax = Integer.MIN_VALUE;
                        this.boundaries.zMin = Integer.MAX_VALUE;
                    } else {
                        boolean did = false;
                        tester:
                        for (int k = z; k > this.boundaries.zMin; k--) {
                            for (int dX = this.boundaries.xMin; dX <= this.boundaries.xMax; dX++) {
                                for (int dY = this.boundaries.yMin; dY <= this.boundaries.yMax; dY++) {
                                    if(this.blocks[dX][dY][k] != null) {
                                        this.boundaries.zMax = k;
                                        did = true;
                                        break tester;
                                    }
                                }
                            }
                        }
                        if(!did)
                            this.boundaries.zMax = Integer.MIN_VALUE;
                    }
                }
            }
            if (z == this.boundaries.zMin) {
                boolean did = false;
                tester:
                for (int k = z; k <= this.boundaries.zMax; k++) {
                    for (int dX = this.boundaries.xMin; dX <= this.boundaries.xMax; dX++) {
                        for (int dY = this.boundaries.yMin; dY <= this.boundaries.yMax; dY++) {
                            if(this.blocks[dX][dY][k] != null) {
                                this.boundaries.zMin = k;
                                did = true;
                                break tester;
                            }
                        }
                    }
                }
                if(!did)
                    this.boundaries.zMin = Integer.MAX_VALUE;
            }
        }
    }

    public Block3D getBlockAt(int x, int y, int z) {
        return this.blocks[x][y][z];
    }

    public int getSize() {
        return this.size;
    }

    @Override
    public double getOneUnit() {
        return 1;
    }

    @Override
    public MatrixStore<Double> getTransform() {
        return null;
    }
}

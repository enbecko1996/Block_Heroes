package com.enbecko.objectcreator.restart;

import com.enbecko.objectcreator.core.vec3;
import com.enbecko.objectcreator.core.vec3Int;
import com.sun.istack.internal.Nullable;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.client.renderer.Tessellator;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Niclas on 19.11.2016.
 */
public class BoneRenderer {

    public final List<Face2D> xFaces = new ArrayList<Face2D>();
    public final List<Face2D> yFaces = new ArrayList<Face2D>();
    public final List<Face2D> zFaces = new ArrayList<Face2D>();

    public void getQuads() {
        for (Face2D face2D : xFaces)
            face2D.getQuads(Direction.X);
        for (Face2D face2D : yFaces)
            face2D.getQuads(Direction.Y);
        for (Face2D face2D : zFaces)
            face2D.getQuads(Direction.Z);
    }

    public void render(Tessellator te) {
        for (Face2D face2D : xFaces)
            face2D.render(te);
        for (Face2D face2D : yFaces)
            face2D.render(te);
        for (Face2D face2D : zFaces)
            face2D.render(te);
    }

    public static BoneRenderer makeNewBoneRenderer(final Block3D[][][] block3Ds, @Nonnull final Boundaries3D boundaries) {
        BoneRenderer out = new BoneRenderer();
        boolean didEvenSet = false;

        BoolMatrix2x2 theLastMatrixX = newMatrix(Direction.X, boundaries);
        BoolMatrix2x2 tmpMatrixX = newMatrix(Direction.X, boundaries);
        for (int x = boundaries.xMin; x <= boundaries.xMax; x++) {
            didEvenSet = false;
            BoolMatrix2x2 tmpNxtMatrix = newMatrix(Direction.X, boundaries);
            for (int dY = boundaries.yMin; dY <= boundaries.yMax; dY++) {
                for (int dZ = boundaries.zMin; dZ <= boundaries.zMax; dZ++) {
                    boolean did = insertIntoMatrix(Direction.X, tmpMatrixX, tmpNxtMatrix, theLastMatrixX, block3Ds, x, dY, dZ, boundaries.xMin, boundaries.xMax);
                    if(!didEvenSet)
                        didEvenSet = did;
                }
            }
            if(didEvenSet) {
                finishMatrixAndMakeFaces(Direction.X, x, tmpMatrixX, out.xFaces);
            }
            tmpMatrixX = tmpNxtMatrix;
        }
        finishMatrixAndMakeFaces(Direction.X, boundaries.xMax + 1, theLastMatrixX, out.xFaces);

        BoolMatrix2x2 tmpMatrixY = newMatrix(Direction.Y, boundaries);
        BoolMatrix2x2 theLastMatrixY = newMatrix(Direction.Y, boundaries);
        for (int y = boundaries.yMin; y <= boundaries.yMax; y++) {
            didEvenSet = false;
            BoolMatrix2x2 tmpNxtMatrix = newMatrix(Direction.Y, boundaries);
            for (int dX = boundaries.xMin; dX <= boundaries.xMax; dX++) {
                for (int dZ = boundaries.zMin; dZ <= boundaries.zMax; dZ++) {
                    boolean did = insertIntoMatrix(Direction.Y, tmpMatrixY, tmpNxtMatrix, theLastMatrixY, block3Ds, dX, y, dZ, boundaries.yMin, boundaries.yMax);
                    if(!didEvenSet)
                        didEvenSet = did;
                }
            }
            if(didEvenSet) {
                finishMatrixAndMakeFaces(Direction.Y, y, tmpMatrixY, out.yFaces);
            }
            tmpMatrixY = tmpNxtMatrix;
        }
        finishMatrixAndMakeFaces(Direction.Y, boundaries.yMax + 1, theLastMatrixY, out.yFaces);

        BoolMatrix2x2 tmpMatrixZ = newMatrix(Direction.Z, boundaries);
        BoolMatrix2x2 theLastMatrixZ = newMatrix(Direction.Z, boundaries);
        for (int z = boundaries.zMin; z <= boundaries.zMax; z++) {
            didEvenSet = false;
            BoolMatrix2x2 tmpNxtMatrix = newMatrix(Direction.Z, boundaries);
            for (int dX = boundaries.xMin; dX <= boundaries.xMax; dX++) {
                for (int dY = boundaries.yMin; dY <= boundaries.yMax; dY++) {
                    boolean did = insertIntoMatrix(Direction.Z, tmpMatrixZ, tmpNxtMatrix, theLastMatrixZ, block3Ds, dX, dY, z, boundaries.zMin, boundaries.zMax);
                    if(!didEvenSet)
                        didEvenSet = did;
                }
            }
            if(didEvenSet) {
                finishMatrixAndMakeFaces(Direction.Z, z, tmpMatrixZ, out.zFaces);
            }
            tmpMatrixZ = tmpNxtMatrix;
        }
        finishMatrixAndMakeFaces(Direction.Z, boundaries.zMax + 1, theLastMatrixZ, out.zFaces);

        System.out.println("hallo" +out.xFaces+" \n\n"+out.yFaces+" \n\n"+out.yFaces);

        return out;
    }

    @Nullable
    private static BoolMatrix2x2 newMatrix(Direction direction, Boundaries3D boundaries) {
        switch (direction) {
            case X:
                return new BoolMatrix2x2(boundaries.zMax - boundaries.zMin + 1, boundaries.yMax - boundaries.yMin + 1, boundaries.zMin, boundaries.yMin);
            case Y:
                return new BoolMatrix2x2(boundaries.xMax - boundaries.xMin + 1, boundaries.zMax - boundaries.zMin + 1, boundaries.xMin, boundaries.zMin);
            case Z:
                return new BoolMatrix2x2(boundaries.xMax - boundaries.xMin + 1, boundaries.yMax - boundaries.yMin + 1, boundaries.xMin, boundaries.yMin);
        }
        return null;
    }

    private static boolean insertIntoMatrix(Direction direction, BoolMatrix2x2 matrix2x2, BoolMatrix2x2 nxtMatrix, BoolMatrix2x2 theLastMatrix, Block3D[][][] block3Ds, int x, int y, int z, int lowerBound, int upperBound) {
        if (block3Ds[x][y][z] != null) {
            switch (direction) {
                case X:
                    nxtMatrix.set(z, y);
                    if (x == lowerBound) {
                        if(matrix2x2.get(z, y))
                            matrix2x2.unset(z, y);
                        else
                            matrix2x2.set(z, y);
                        return true;
                    } else if (x == upperBound) {
                        theLastMatrix.set(z, y);
                        if(matrix2x2.get(z, y))
                            matrix2x2.unset(z, y);
                        else
                            matrix2x2.set(z, y);
                        return true;
                    } else {
                        if(matrix2x2.get(z, y))
                            matrix2x2.unset(z, y);
                        else
                            matrix2x2.set(z, y);
                        return true;
                    }
                case Y:
                    nxtMatrix.set(x, z);
                    if (y == lowerBound) {
                        if(matrix2x2.get(x, z))
                            matrix2x2.unset(x, z);
                        else
                            matrix2x2.set(x, z);
                        return true;
                    } else if (y == upperBound) {
                        theLastMatrix.set(x, z);
                        if(matrix2x2.get(x, z))
                            matrix2x2.unset(x, z);
                        else
                            matrix2x2.set(x, z);
                        return true;
                    } else {
                        if(matrix2x2.get(x, z))
                            matrix2x2.unset(x, z);
                        else
                            matrix2x2.set(x, z);
                        return true;
                    }
                case Z:
                    nxtMatrix.set(x, y);
                    if (z == lowerBound) {
                        if(matrix2x2.get(x, y))
                            matrix2x2.unset(x, y);
                        else
                            matrix2x2.set(x, y);
                        return true;
                    } else if (z == upperBound) {
                        theLastMatrix.set(x, y);
                        if(matrix2x2.get(x, y))
                            matrix2x2.unset(x, y);
                        else
                            matrix2x2.set(x, y);
                        return true;
                    } else {
                        if(matrix2x2.get(x, y))
                            matrix2x2.unset(x, y);
                        else
                            matrix2x2.set(x, y);
                        return true;
                    }
            }
        }
        return false;
    }

    private static void finishMatrixAndMakeFaces(Direction direction, int coord, BoolMatrix2x2 matrix, List<Face2D> into) {
        matrix.analyze();
        if (!matrix.isEmpty()) {
            List<Expandable> expandables = matrix.getExpandables();
            int pV = matrix.low_left_vertUpdate, pH = matrix.low_left_horUpdate;
            Face2D tmp = new Face2D(coord);
            switch (direction) {
                case X:
                    for (Expandable expandable : expandables) {
                        tmp.addFace(new BlockRenderFace3D(new vec3Int(coord, pV + expandable.startVert, pH + expandable.startHor), new vec3Int(0, expandable.vert, 0), new vec3Int(0, 0, expandable.hor)));
                    }
                    break;
                case Y:
                    for (Expandable expandable : expandables) {
                        tmp.addFace(new BlockRenderFace3D(new vec3Int(pH + expandable.startHor, coord, pV + expandable.startVert), new vec3Int(expandable.hor, 0, 0), new vec3Int(0, 0, expandable.vert)));
                    }
                    break;
                case Z:
                    for (Expandable expandable : expandables) {
                        tmp.addFace(new BlockRenderFace3D(new vec3Int(pH + expandable.startHor, pV + expandable.startVert, coord), new vec3Int(expandable.hor, 0, 0), new vec3Int(0, expandable.vert, 0)));
                    }
                    break;
            }
            into.add(tmp);
        }
    }

    public enum Direction {
        X, Y, Z;
    }

    public void createRenderableFacesAndAddInto(BoolMatrix2x2 matrix2x2, List<Face2D> face2Ds, Direction direction) {
        for (int w = 0; w < matrix2x2.wid; w++) {
            for (int h = 0; h < matrix2x2.hei; h++) {
                if(matrix2x2.posis[w][h]) {
                    int wid = 0, hei = 0;
                    for (int ww = w; ww < matrix2x2.wid; ww++) {
                        if (!matrix2x2.posis[ww][h]) {
                            wid = ww - w;
                            break;
                        }
                    }
                    for (int hh = h; hh < matrix2x2.hei; hh++) {
                        if (!matrix2x2.posis[w][hh]) {
                            hei = hh - h;
                            break;
                        }
                    }
                    Expandable tmp = new Expandable(w, h, wid, hei);
                }
            }
        }
        switch (direction) {
            case X:
        }
    }

    private static class BoolMatrix2x2 {
        private final boolean[][] posis;
        private boolean[][] posisCopy;
        private final int wid, hei, low_left_hor, low_left_vert, left, right, top, bot;
        private int leftUpdate = Integer.MAX_VALUE, rightUpdate = 0, topUpdate = 0, botUpdate = Integer.MAX_VALUE, low_left_horUpdate, low_left_vertUpdate;
        private boolean isAnalyzed;
        private final List<Expandable> expandables = new ArrayList<Expandable>();

        public BoolMatrix2x2(int wid, int hei, int low_left_hor, int low_left_vert) {
            this.posis = new boolean[wid][hei];
            this.wid = wid;
            this.hei = hei;
            this.low_left_hor = low_left_hor;
            this.low_left_vert = low_left_vert;
            this.left = low_left_hor;
            this.right = low_left_hor + wid;
            this.bot = low_left_vert;
            this.top = low_left_vert + hei;
        }

        public List<Expandable> getExpandables() {
            return this.expandables;
        }

        public boolean isAnalyzed() {
            return this.isAnalyzed;
        }

        public boolean isEmpty() {
            return this.expandables.size() <= 0;
        }

        public boolean get(int w, int h) {
            int l, b;
            if ((l = w - low_left_hor) <= this.wid && (b = h - low_left_vert) <= this.hei) {
                return this.posis[l][b];
            }
            return false;
        }

        public void set(int w, int h) {
            int l, b;
            if ((l = w - low_left_hor) <= this.wid && (b = h - low_left_vert) <= this.hei) {
                this.posis[l][b] = true;
                if(w < this.leftUpdate)
                    this.leftUpdate = w;
                if(w + 1 > this.rightUpdate)
                    this.rightUpdate = w + 1;
                if(h < this.botUpdate)
                    this.botUpdate = h;
                if(h + 1 > this.topUpdate)
                    this.topUpdate = h + 1;
            }
        }

        private void finalizeForAnalyze() {
            if(this.leftUpdate > this.low_left_hor)
                this.low_left_horUpdate = this.leftUpdate;
            else
                this.low_left_horUpdate = this.low_left_hor;
            if(this.botUpdate > this.low_left_vert)
                this.low_left_vertUpdate = this.botUpdate;
            else
                this.low_left_vertUpdate = this.low_left_vert;
            if(this.leftUpdate > this.right || this.leftUpdate > this.rightUpdate)
                this.leftUpdate = this.left;
            if(this.rightUpdate < this.left || this.rightUpdate < this.leftUpdate)
                this.rightUpdate = this.right;
            if(this.botUpdate > this.top || this.botUpdate > this.topUpdate)
                this.botUpdate = this.bot;
            if(this.topUpdate < this.bot || this.topUpdate < this.botUpdate)
                this.topUpdate = this.top;
        }

        public void unset(int w, int h) {
            int l, b;
            if ((l = w - low_left_hor) <= this.wid && (b = h - low_left_vert) <= this.hei) {
                this.posis[l][b] = false;
            }
        }

        public void analyze() {
            if(!isAnalyzed) {
                List<TeilerPair> teilers = new ArrayList<TeilerPair>();
                this.finalizeForAnalyze();
                final int tmpWid = this.rightUpdate - this.leftUpdate;
                final int tmpHei = this.topUpdate - this.botUpdate;
                this.posisCopy = new boolean[tmpWid][tmpHei];
                for (int w = 0; w < tmpWid; w++) {
                    for (int h = 0; h < tmpHei; h++) {
                        this.posisCopy[w][h] = this.posis[this.leftUpdate - this.low_left_hor + w][this.botUpdate - this.low_left_vert + h];
                    }
                }
                //System.out.println(this.arrayToMatrix(this.posisCopy, tmpWid, tmpHei));
                int n = tmpWid;
                int m = tmpHei;
                int searchSize = n * m;
                int limitWid = n, limitHei = m;
                while (searchSize > 0) {
                    while (teilers.isEmpty() && searchSize > 0) {
                        this.getTeilers(teilers, searchSize, limitWid, limitHei);
                        //System.out.println(searchSize+" "+teilers);
                        searchSize--;
                    }
                    // System.out.println("limi "+limitWid+" "+limitHei);

                    for (int k = 0; k < teilers.size(); k++) {
                        TeilerPair cur = teilers.get(k);
                        int possibilities = ((cur.t1 <= limitWid && cur.t2 <= limitWid && cur.t1 <= limitHei && cur.t2 <= limitHei) ? 2 : 1);
                        for (int l = 0; l < possibilities; l++) {
                            if ((possibilities == 2 && l == 0 && cur.t1 <= limitWid && cur.t2 <= limitHei) || (possibilities == 1 && cur.t1 <= limitWid && cur.t2 <= limitHei)) {
                                n = cur.t1;
                                m = cur.t2;
                            } else if (cur.t2 <= limitWid && cur.t1 <= limitHei) {
                                n = cur.t2;
                                m = cur.t1;
                            } else
                                break;
                            for (int w = 0; w < tmpWid - n + 1; w++) {
                                for (int h = 0; h < tmpHei - m + 1; h++) {
                                    boolean yes = true;
                                    faceLoop:
                                    for (int dW = 0; dW < n; dW++) {
                                        for (int dH = 0; dH < m; dH++) {
                                            //System.out.println(dW+" "+dH+" "+this.posisCopy[dW + w][dH + h]);
                                            if (!this.posisCopy[dW + w][dH + h]) {
                                                yes = false;
                                                break faceLoop;
                                            }
                                        }
                                    }
                                    if (yes) {
                                        for (int dW = 0; dW < n; dW++) {
                                            for (int dH = 0; dH < m; dH++) {
                                                this.posisCopy[dW + w][dH + h] = false;
                                            }
                                        }
                                        expandables.add(new Expandable(w, h, n, m));
                                    }
                                }
                            }
                        }
                    }
                    teilers.clear();
                }
                this.isAnalyzed = true;
            }else {
                if(isAnalyzed)
                    throw new RuntimeException("A matrix2x2 can't be analyzed twice");
            }
            //System.out.println(this.low_left_horUpdate+" "+this.low_left_vertUpdate+" "+expandables);
        }

        public void getTeilers(List<TeilerPair> list, int nmbr, int limit1, int limit2) {
            list.clear();
            int offcut = nmbr;
            if(nmbr == 1) {
                list.add(new TeilerPair(1, 1));
                return;
            }
            for(int i = 1; i < offcut; i++) {
                if((nmbr % i) == 0) {
                    offcut = nmbr / i;
                    if(offcut <= nmbr) {
                        if((i <= limit1 && offcut <= limit2) || (i <= limit2 && offcut <= limit1))
                            list.add(new TeilerPair(i, offcut));
                    } else
                        return;
                }
            }
        }

        public String arrayToMatrix(boolean[][] array, int wid, int hei) {
            StringBuilder builder = new StringBuilder();
            for (int h = hei - 1; h >= 0; h--) {
                for (int w = 0; w < wid; w++) {
                    builder.append((array[w][h] ? "X" : "O")+", ");
                }
                builder.append("\n");
            }
            return builder.toString();
        }

        public String toString() {
            return this.arrayToMatrix(this.posis, this.wid, this.hei);
        }
    }

    private static class TeilerPair{
        public final int t1, t2;
        public TeilerPair(int t1, int t2) {
            this.t1 = t1;
            this.t2 = t2;
        }

        public String toString() {
            return "Teiler: {"+t1+", "+t2+"}";
        }
    }

    public static class Expandable {
        public final int startHor, startVert;
        public final int hor, vert;
        public Expandable(int startHor, int startVert, int hor, int vert) {
            this.startVert = startVert;
            this.startHor = startHor;
            this.hor = hor;
            this.vert = vert;
        }

        public String toString() {
            return "Expand: {{"+ startHor +", "+ startVert +"}, {"+hor+", "+vert+"}}";
        }
    }

    public BoneRenderer updateBoneRenderer(Block3D[][][] block3Ds, vec3.Int pos, int size) {
        int xP = pos.x, yP = pos.y, zP = pos.z;
        List<Block3D> tmpPerFace = new ArrayList<Block3D>();
        List<Block3D> tmpPerFaceNext = new ArrayList<Block3D>();
        for (int y = 0; y < size; y++) {
            for (int z = 0; z < size; z++) {
                if(block3Ds[xP][y][z] != null) {
                    if (block3Ds[xP - 1][y][z] != null) {
                        tmpPerFace.add(block3Ds[xP][y][z]);
                    }
                    if (block3Ds[xP + 1][y][z] != null) {
                        tmpPerFaceNext.add(block3Ds[xP][y][z]);
                    }
                }
            }
        }
        for (int x = 0; x < size; x++) {
            for (int z = 0; z < size; z++) {
                if(block3Ds[x][yP][z] != null) {
                    if (block3Ds[x][yP - 1][z] != null) {
                        tmpPerFace.add(block3Ds[xP][yP][z]);
                    }
                    if (block3Ds[x][yP + 1][z] != null) {
                        tmpPerFaceNext.add(block3Ds[xP][yP][z]);
                    }
                }
            }
        }
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if(block3Ds[x][y][zP] != null) {
                    if (block3Ds[x][y][zP - 1] != null) {
                        tmpPerFace.add(block3Ds[x][y][zP]);
                    }
                    if (block3Ds[x][y][zP + 1] != null) {
                        tmpPerFaceNext.add(block3Ds[x][y][zP]);
                    }
                }
            }
        }
        return this;
    }

    private static class Face2D {
        public final int coord;
        private final List<BlockRenderFace3D> face3Ds = new ArrayList<BlockRenderFace3D>();
        private final List<TexturedQuad> quads = new ArrayList<TexturedQuad>();

        public Face2D(int coordinate) {
            this.coord = coordinate;
        }

        public Face2D addFace(@Nonnull BlockRenderFace3D face3D) {
            if(!this.face3Ds.contains(face3D))
                this.face3Ds.add(face3D);
            return this;
        }

        public void getQuads(Direction direction) {
            for (BlockRenderFace3D face3D : face3Ds) {
                this.quads.add(face3D.getQuad(direction, this.coord, false));
            }
        }

        public void render(Tessellator tessellator) {
            for (TexturedQuad texturedquad : this.quads) {
                if(texturedquad != null)
                    texturedquad.draw(tessellator.getBuffer(), 1);
            }
        }

        public void clearFaces() {
            this.face3Ds.clear();
        }

        public String toString() {
            return this.face3Ds.toString()+"\n";
        }
    }

}

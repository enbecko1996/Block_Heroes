package com.enbecko.objectcreator.core;

import net.minecraft.client.model.TexturedQuad;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Niclas on 04.11.2016.
 */
public class LocalCoordsRenderableFaces2DInOneCoord {

    private final List<CreatorBlock> creatorBlocks = new ArrayList<CreatorBlock>();
    private final List<RenderableFace2D> facesToPrev = new ArrayList<RenderableFace2D>();
    private final List<RenderableFace2D> facesToNext = new ArrayList<RenderableFace2D>();
    private final List<TexturedQuad> texturedQuads = new ArrayList<TexturedQuad>();
    @Nonnull private final CreatorBlockFaceDirection direction;
    public final double coordinate;
    private final vec3.vecPrec precision;

    public LocalCoordsRenderableFaces2DInOneCoord(List<CreatorBlock> creatorBlocks, CreatorBlockFaceDirection direction, double coord, vec3.vecPrec precision) {
        this(direction, coord, precision);
        this.creatorBlocks.addAll(creatorBlocks);
    }

    public LocalCoordsRenderableFaces2DInOneCoord(CreatorBlockFaceDirection direction, double coord, vec3.vecPrec precision) {
        this.precision = precision;
        this.direction = direction;
        this.coordinate = coord;
    }

    public LocalCoordsRenderableFaces2DInOneCoord(vec3.vecPrec precision) {
        this.precision = precision;
        this.direction = CreatorBlockFaceDirection.PLACEHOLDER;
        this.coordinate = -1;
    }

    public enum CreatorBlockFaceDirection {
        X, Y, Z, PLACEHOLDER;

        public double getHorPos(CreatorBlock block) {
            switch (this) {
                case X:
                    return block.pos.getZD();
                default:
                    return block.pos.getXD();
            }
        }

        public double getVertPos(CreatorBlock block) {
            switch (this) {
                case Y:
                    return block.pos.getZD();
                default:
                    return block.pos.getYD();
            }
        }
    }

    public boolean isEmpty() {
        return this.creatorBlocks.size() <= 0;
    }

    public boolean isPlaceholder() {
        return this.direction == CreatorBlockFaceDirection.PLACEHOLDER;
    }

    public LocalCoordsRenderableFaces2DInOneCoord add(CreatorBlock creatorBlock) {
        if(!this.isPlaceholder() && creatorBlock != null && !this.creatorBlocks.contains(creatorBlock))
            this.creatorBlocks.add(creatorBlock);
        return this;
    }

    public boolean remove(double x, double y, double z) {
        if(!this.isPlaceholder()) {
            for (int k = 0; k < this.creatorBlocks.size(); k++) {
                CreatorBlock block = this.creatorBlocks.get(k);
                if (block.pos.getXD() == x && block.pos.getYD() == y && block.pos.getZD() == z) {
                    this.creatorBlocks.remove(k);
                    return true;
                }
            }
        }
        return false;
    }

    public List<TexturedQuad> getTexturedQuads() {
        return this.texturedQuads;
    }

    @Nullable
    private List<TexturedQuad> updateQuads() {
        if(!this.isPlaceholder()) {
            this.texturedQuads.clear();
            for (RenderableFace2D renderableFace : this.facesToPrev) {
                TexturedQuad tmpQuad;
                if ((tmpQuad = renderableFace.getQuad(this.direction, (float) this.coordinate, true)) != null) {
                    this.texturedQuads.add(tmpQuad);
                }
            }
            for (RenderableFace2D renderableFace : this.facesToNext) {
                TexturedQuad tmpQuad;
                if ((tmpQuad = renderableFace.getQuad(this.direction, (float) (this.coordinate + 1), false)) != null) {
                    this.texturedQuads.add(tmpQuad);
                }
            }
            //System.out.println(Arrays.toString(this.texturedQuads.toArray(new TexturedQuad[this.texturedQuads.size()])));
            return this.texturedQuads;
        }
        return null;
    }

    public boolean render(@Nonnull Tessellator tessellator) {
        for (TexturedQuad texturedquad : this.texturedQuads) {
            if(texturedquad != null)
                texturedquad.draw(tessellator.getBuffer(), 1);
        }
        return true;
    }

    public boolean createRenderableFaces() {
        return this.createRenderableFaces(null, null, false);
    }

    @SideOnly(Side.CLIENT)
    public boolean createRenderableFaces(@Nullable LocalCoordsRenderableFaces2DInOneCoord prev, @Nullable LocalCoordsRenderableFaces2DInOneCoord next, final boolean updateNonNull) {
        System.out.println(prev);
        if(!this.isPlaceholder()) {
            @Nonnull List<CreatorBlock> editableBlocks = new ArrayList<CreatorBlock>(this.creatorBlocks);
            List<RenderableFace2D> fillableList;
            List<CreatorBlock> usableBlockList, editableBlockListDeleted = new ArrayList<CreatorBlock>();
            int start = 0, end = 2;
            if (updateNonNull) {
                if (prev == null)
                    start = 1;
                if (next == null)
                    end = 1;
            }
            for (int side = start; side < end; side++) {
                List<RenderableFace2D> smallestList = null;
                editableBlockListDeleted.clear();
                editableBlockListDeleted.addAll(this.creatorBlocks);
                switch (side) {
                    case 0:
                        fillableList = this.facesToPrev;
                        if (prev != null && prev.direction.equals(this.direction) && prev.coordinate == this.coordinate - 1) {
                            if(!prev.isPlaceholder())
                                this.editDeletableList(prev, editableBlockListDeleted);
                        }
                        break;
                    case 1:
                        fillableList = this.facesToNext;
                        if (next != null && next.direction.equals(this.direction) && next.coordinate == this.coordinate + 1) {
                            if (!next.isPlaceholder())
                                this.editDeletableList(next, editableBlockListDeleted);
                        }
                        break;
                    default:
                        fillableList = this.facesToPrev;
                }
                for (int delete = 0; delete < 2; delete++) {
                    editableBlocks.clear();
                    editableBlocks.addAll(this.creatorBlocks);
                    List<RenderableFace2D> tmpList = new ArrayList<RenderableFace2D>();
                    switch (delete) {
                        case 0:
                            usableBlockList = editableBlocks;
                            break;
                        case 1:
                            usableBlockList = editableBlockListDeleted;
                            break;
                        default:
                            usableBlockList = editableBlocks;
                    }
                    while (usableBlockList.size() > 0) {
                        RenderableFace2D curBiggestRenderableFace = new RenderableFace2D(this.direction, this.precision);
                        for (CreatorBlock starter : usableBlockList) {
                            RenderableFace2D renderableFace = new RenderableFace2D(this.direction.getHorPos(starter), this.direction.getVertPos(starter), this.direction.getHorPos(starter) + 1, this.direction.getVertPos(starter) + 1, this.direction, this.precision).setStarterCube(starter);
                            RenderableFace2D tmp = this.iterateFrom(renderableFace, renderableFace, usableBlockList);
                            if (tmp.getSize() > curBiggestRenderableFace.getSize())
                                curBiggestRenderableFace = tmp;
                        }
                        tmpList.add(curBiggestRenderableFace);
                        usableBlockList.removeAll(curBiggestRenderableFace.getFaceCubes());
                    }
                    if (smallestList == null || tmpList.size() < smallestList.size())
                        smallestList = tmpList;
                }
                fillableList.clear();
                fillableList.addAll(smallestList);
            }
            //System.out.println(Arrays.toString(this.facesToPrev.toArray(new RenderableFace2D[facesToPrev.size()])) + " " + Arrays.toString(this.facesToNext.toArray(new RenderableFace2D[facesToNext.size()])));
            this.updateQuads();
            return true;
        }
        return false;
    }

    private void editDeletableList(@Nonnull final LocalCoordsRenderableFaces2DInOneCoord mother, @Nonnull final List<CreatorBlock> toEdit) {
        for (CreatorBlock block : mother.creatorBlocks) {
            for (int k = 0; k < toEdit.size(); k++) {
                CreatorBlock myBlock = toEdit.get(k);
                switch (this.direction) {
                    case X:
                        if (block.pos.getYD() == myBlock.pos.getYD() && block.pos.getZD() == myBlock.pos.getZD()) {
                            toEdit.remove(k);
                            k--;
                        }
                        break;
                    case Y:
                        if (block.pos.getXD() == myBlock.pos.getXD() && block.pos.getZD() == myBlock.pos.getZD()) {
                            toEdit.remove(k);
                            k--;
                        }
                        break;
                    case Z:
                        if (block.pos.getYD() == myBlock.pos.getYD() && block.pos.getXD() == myBlock.pos.getXD()) {
                            toEdit.remove(k);
                            k--;
                        }
                        break;
                }
            }
        }
    }

    @Nonnull
    public RenderableFace2D iterateFrom(RenderableFace2D biggestRenderableFace, RenderableFace2D renderableFace, List<CreatorBlock> blocks) {
        RenderableFace2D tmpRenderableFace, tmpRenderableFace2;
        tmpRenderableFace = this.expandFace(ExpandDirect.HOR, renderableFace, blocks);
        tmpRenderableFace2 = this.expandFace(ExpandDirect.VERT, renderableFace, blocks);
        if(tmpRenderableFace == null && tmpRenderableFace2 == null)
            return biggestRenderableFace;
        else {
            if (tmpRenderableFace != null && tmpRenderableFace.getSize() > biggestRenderableFace.getSize())
                biggestRenderableFace = tmpRenderableFace;
            if (tmpRenderableFace2 != null && tmpRenderableFace2.getSize() > biggestRenderableFace.getSize())
                biggestRenderableFace = tmpRenderableFace2;

        }

        if((tmpRenderableFace = this.iterateFrom(biggestRenderableFace, tmpRenderableFace, blocks)).getSize() > (tmpRenderableFace2 = this.iterateFrom(biggestRenderableFace, tmpRenderableFace2, blocks)).getSize()) {
            return tmpRenderableFace;
        } else {
            return tmpRenderableFace2;
        }
    }

    private RenderableFace2D expandFace(@Nonnull ExpandDirect direct, RenderableFace2D renderableFace, List<CreatorBlock> blocks) {
        if (renderableFace != null) {
            List<CreatorBlock> expandables = new ArrayList<CreatorBlock>();
            boolean made = false;
            RenderableFace2D tmpRenderableFace = new RenderableFace2D(renderableFace, this.direction);
            switch (direct) {
                case HOR:
                    for (int h = 0; h < tmpRenderableFace.getHeight(); h++) {
                        made = false;
                        for (CreatorBlock block : blocks) {
                            if (block != null) {
                                if (direction.getHorPos(block) == tmpRenderableFace.getLocalLeft() + tmpRenderableFace.getWidth() && direction.getVertPos(block) == tmpRenderableFace.getLocalBot() + h) {
                                    expandables.add(block);
                                    made = true;
                                }
                            }
                        }
                        if (!made)
                            return null;
                    }
                    tmpRenderableFace.addCubes(expandables);
                    tmpRenderableFace.expand(ExpandDirect.HOR);
                    break;
                case VERT:
                    for (int w = 0; w < tmpRenderableFace.getWidth(); w++) {
                        made = false;
                        for (CreatorBlock block : blocks) {
                            if (block != null) {
                                if (direction.getVertPos(block) == tmpRenderableFace.getLocalBot() + tmpRenderableFace.getHeight() && direction.getHorPos(block) == tmpRenderableFace.getLocalLeft() + w) {
                                    expandables.add(block);
                                    made = true;
                                }
                            }
                        }
                        if (!made)
                            return null;
                    }
                    tmpRenderableFace.addCubes(expandables);
                    tmpRenderableFace.expand(ExpandDirect.VERT);
                    break;
            }
            return tmpRenderableFace;
        }
        return null;
    }

    protected enum ExpandDirect {
        HOR, VERT;
    }
}

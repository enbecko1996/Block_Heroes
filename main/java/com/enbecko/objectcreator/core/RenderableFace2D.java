package com.enbecko.objectcreator.core;

import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.model.TexturedQuad;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class RenderableFace2D <T extends vec3> extends Face2D {

	List<CreatorBlock> faceCubes = new ArrayList<CreatorBlock>();
	CreatorBlock starterCube;

	@SuppressWarnings("unchecked")
	public RenderableFace2D(RenderableFace2D renderableFace, LocalCoordsRenderableFaces2DInOneCoord.CreatorBlockFaceDirection direction)
	{
		super(renderableFace, direction);
		this.starterCube = new CreatorBlock(renderableFace.starterCube, renderableFace.starterCube.pos);
		this.faceCubes = new ArrayList<CreatorBlock>();
		this.faceCubes.addAll(renderableFace.faceCubes);
	}

	@Deprecated
	public RenderableFace2D(double left, double bot, double right, double top, LocalCoordsRenderableFaces2DInOneCoord.CreatorBlockFaceDirection direction, vec3.vecPrec prec)
	{
		super(left, bot, right, top, direction, prec);
	}

	@SuppressWarnings("unchecked")
	public RenderableFace2D(T LOW_LEFT, T UP_RIGHT, LocalCoordsRenderableFaces2DInOneCoord.CreatorBlockFaceDirection direction) {
		super(LOW_LEFT, UP_RIGHT, direction);
	}
	
	public RenderableFace2D(LocalCoordsRenderableFaces2DInOneCoord.CreatorBlockFaceDirection direc, vec3.vecPrec prec)
	{
		super(direc, prec);
	}

	@Nullable
	public TexturedQuad getQuad(LocalCoordsRenderableFaces2DInOneCoord.CreatorBlockFaceDirection direction, final float coord, boolean negative) {
		float[] vert0 = null;
		float[] vert1 = null;
		float[] vert2 = null;
		float[] vert3 = null;
		vec3.Float LOW_LEFT = new vec3.Float(this.LOW_LEFT);
		vec3.Float UP_RIGHT = new vec3.Float(this.UP_RIGHT);
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

	public List<CreatorBlock> getFaceCubes() {
		return this.faceCubes;
	}

	public RenderableFace2D addCube(CreatorBlock cube)
	{
		if(cube != null && !this.faceCubes.contains(cube))
			this.faceCubes.add(cube);
		return this;
	}

	public RenderableFace2D addCubes(List<CreatorBlock> blocks)
	{
		for (CreatorBlock block : blocks) {
			if(block != null)
				this.faceCubes.add(block);
		}
		return this;
	}

	public RenderableFace2D expand(LocalCoordsRenderableFaces2DInOneCoord.ExpandDirect direct) {
		switch (direct) {
			case HOR:
				this.expandHor(1);
				break;
			case VERT:
				this.expandVer(1);
				break;
		}
		return this;
	}

	public RenderableFace2D setStarterCube(CreatorBlock cube) {
		if(cube != null) {
			this.starterCube = cube;
			if(!this.faceCubes.contains(cube))
				this.faceCubes.add(cube);
		}
		return this;
	}

	public String toString() {
		return "{"+this.direction+" face: "+this.LOW_LEFT+", "+this.UP_RIGHT+", "+this.getWidth()+", "+this.getHeight()+", ["+this.getSize()+"]}";
	}

}

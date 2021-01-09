package com.tigres810.testmod.tileentitys.renders;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.tigres810.testmod.tileentitys.TileFluidTankBlock;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;

public class Old_RenderFluidTankBlock extends TileEntityRenderer<TileFluidTankBlock> {
	
	public static final float TANK_THICKNESS = 0.3f;
	public static final float TANK_HEIGHT = 0.2f;
	public static final float TANK_BOTTOM = 0.0f;

	public Old_RenderFluidTankBlock(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
	}
	
	private void add(IVertexBuilder renderer, MatrixStack stack, float x, float y, float z, float u, float v, float r, float g, float b, float a) {
        renderer.pos(stack.getLast().getMatrix(), x, y, z)
                .color(r, g, b, a)
                .tex(u, v)
                .lightmap(0, 240)
                .normal(1, 0, 0)
                .endVertex();
    }

	@Override
	public void render(TileFluidTankBlock tileEntityIn, float partialTicks, MatrixStack matrixStackIn,
			IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		if(tileEntityIn == null || tileEntityIn.isRemoved()) return;
		
		FluidStack fluid = tileEntityIn.getTank().getFluid();
		if (fluid == null) return;
		
		Fluid renderFluid = fluid.getFluid();
		if (renderFluid == null) return;
		
		FluidAttributes attributes = renderFluid.getAttributes();
        ResourceLocation fluidStill = attributes.getStillTexture(fluid);
        TextureAtlasSprite sprite = Minecraft.getInstance().getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE).apply(fluidStill);
		
		IVertexBuilder builder = bufferIn.getBuffer(RenderType.getTranslucent());
		
		float scale = (1.0f - TANK_THICKNESS/2 - TANK_THICKNESS) * fluid.getAmount() / (tileEntityIn.getTank().getCapacity());
		
        Quaternion rotation = Vector3f.YP.rotationDegrees(0);
        
        int color = renderFluid.getAttributes().getColor();
        
        float a = 1.0F;
        float r = (color >> 16 & 0xFF) / 255.0F;
        float g = (color >> 8 & 0xFF) / 255.0F;
        float b = (color & 0xFF) / 255.0F;
		
		matrixStackIn.push();
        matrixStackIn.translate(.5, 0, .5);
        matrixStackIn.rotate(rotation);
        if(scale == 0.330f) { matrixStackIn.translate(0, -.1, 0); matrixStackIn.scale(.6f, scale + 0.110f, .6f);  } else if(scale == 0.440f) { matrixStackIn.translate(0, -.2, 0); matrixStackIn.scale(.6f, scale + 0.110f, .6f); } else if(scale == 0.550f) { matrixStackIn.translate(0, -.4, 0); matrixStackIn.scale(.6f, scale + 0.210f, .6f); } else { matrixStackIn.scale(.6f, scale, .6f); }
        matrixStackIn.translate(-.5, scale, -.5);
        
        // Top Face
        add(builder, matrixStackIn, 1 - .8f, 1, .8f, sprite.getMinU(), sprite.getMaxV(), r, g, b, a);
        add(builder, matrixStackIn, 0 + .8f, 1, .8f, sprite.getMaxU(), sprite.getMaxV(), r, g, b, a);
        add(builder, matrixStackIn, 0 + .8f, 1, .2f, sprite.getMaxU(), sprite.getMinV(), r, g, b, a);
        add(builder, matrixStackIn, 1 - .8f, 1, .2f, sprite.getMinU(), sprite.getMinV(), r, g, b, a);
        
        // Bottom Face of Top
        add(builder, matrixStackIn, 0 + .8f, 1, .8f, sprite.getMinU(), sprite.getMaxV(), r, g, b, a);
        add(builder, matrixStackIn, 1 - .8f, 1, .8f, sprite.getMaxU(), sprite.getMaxV(), r, g, b, a);
        add(builder, matrixStackIn, 1 - .8f, 1, .2f, sprite.getMaxU(), sprite.getMinV(), r, g, b, a);
        add(builder, matrixStackIn, 0 + .8f, 1, .2f, sprite.getMinU(), sprite.getMinV(), r, g, b, a);
        
        // Front Faces [NORTH - SOUTH]
        add(builder, matrixStackIn, 0 + .8f, 0 + 1, .8f, sprite.getMinU(), sprite.getMinV(), r, g, b, a);
        add(builder, matrixStackIn, 1 - .8f, 0 + 1, .8f, sprite.getMaxU(), sprite.getMinV(), r, g, b, a);
        add(builder, matrixStackIn, 1 - .8f, 1 - 1, .8f, sprite.getMaxU(), sprite.getMaxV(), r, g, b, a);
        add(builder, matrixStackIn, 0 + .8f, 1 - 1, .8f, sprite.getMinU(), sprite.getMaxV(), r, g, b, a);
        
        add(builder, matrixStackIn, 0 + .8f, 1 - 1, .2f, sprite.getMinU(), sprite.getMaxV(), r, g, b, a);
        add(builder, matrixStackIn, 1 - .8f, 1 - 1, .2f, sprite.getMaxU(), sprite.getMaxV(), r, g, b, a);
        add(builder, matrixStackIn, 1 - .8f, 0 + 1, .2f, sprite.getMaxU(), sprite.getMinV(), r, g, b, a);
        add(builder, matrixStackIn, 0 + .8f, 0 + 1, .2f, sprite.getMinU(), sprite.getMinV(), r, g, b, a);
        
        // Back Faces
        add(builder, matrixStackIn, 0 + .8f, 0 + 1, .2f, sprite.getMinU(), sprite.getMinV(), r, g, b, a);
        add(builder, matrixStackIn, 1 - .8f, 0 + 1, .2f, sprite.getMaxU(), sprite.getMinV(), r, g, b, a);
        add(builder, matrixStackIn, 1 - .8f, 1 - 1, .2f, sprite.getMaxU(), sprite.getMaxV(), r, g, b, a);
        add(builder, matrixStackIn, 0 + .8f, 1 - 1, .2f, sprite.getMinU(), sprite.getMaxV(), r, g, b, a);

        add(builder, matrixStackIn, 0 + .8f, 1 - 1, .8f, sprite.getMinU(), sprite.getMaxV(), r, g, b, a);
        add(builder, matrixStackIn, 1 - .8f, 1 - 1, .8f, sprite.getMaxU(), sprite.getMaxV(), r, g, b, a);
        add(builder, matrixStackIn, 1 - .8f, 0 + 1, .8f, sprite.getMaxU(), sprite.getMinV(), r, g, b, a);
        add(builder, matrixStackIn, 0 + .8f, 0 + 1, .8f, sprite.getMinU(), sprite.getMinV(), r, g, b, a);
        
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90));
        matrixStackIn.translate(-1f, 0, 0);
        // Front Faces [EAST - WEST]
        add(builder, matrixStackIn, 0 + .8f, 0 + 1, .8f, sprite.getMinU(), sprite.getMinV(), r, g, b, a);
        add(builder, matrixStackIn, 1 - .8f, 0 + 1, .8f, sprite.getMaxU(), sprite.getMinV(), r, g, b, a);
        add(builder, matrixStackIn, 1 - .8f, 1 - 1, .8f, sprite.getMaxU(), sprite.getMaxV(), r, g, b, a);
        add(builder, matrixStackIn, 0 + .8f, 1 - 1, .8f, sprite.getMinU(), sprite.getMaxV(), r, g, b, a);

        add(builder, matrixStackIn, 0 + .8f, 1 - 1, .2f, sprite.getMinU(), sprite.getMaxV(), r, g, b, a);
        add(builder, matrixStackIn, 1 - .8f, 1 - 1, .2f, sprite.getMaxU(), sprite.getMaxV(), r, g, b, a);
        add(builder, matrixStackIn, 1 - .8f, 0 + 1, .2f, sprite.getMaxU(), sprite.getMinV(), r, g, b, a);
        add(builder, matrixStackIn, 0 + .8f, 0 + 1, .2f, sprite.getMinU(), sprite.getMinV(), r, g, b, a);
        
        // Back Faces
        add(builder, matrixStackIn, 0 + .8f, 0 + 1, .2f, sprite.getMinU(), sprite.getMinV(), r, g, b, a);
        add(builder, matrixStackIn, 1 - .8f, 0 + 1, .2f, sprite.getMaxU(), sprite.getMinV(), r, g, b, a);
        add(builder, matrixStackIn, 1 - .8f, 1 - 1, .2f, sprite.getMaxU(), sprite.getMaxV(), r, g, b, a);
        add(builder, matrixStackIn, 0 + .8f, 1 - 1, .2f, sprite.getMinU(), sprite.getMaxV(), r, g, b, a);

        add(builder, matrixStackIn, 0 + .8f, 1 - 1, .8f, sprite.getMinU(), sprite.getMaxV(), r, g, b, a);
        add(builder, matrixStackIn, 1 - .8f, 1 - 1, .8f, sprite.getMaxU(), sprite.getMaxV(), r, g, b, a);
        add(builder, matrixStackIn, 1 - .8f, 0 + 1, .8f, sprite.getMaxU(), sprite.getMinV(), r, g, b, a);
        add(builder, matrixStackIn, 0 + .8f, 0 + 1, .8f, sprite.getMinU(), sprite.getMinV(), r, g, b, a);
		matrixStackIn.pop();
	}

}

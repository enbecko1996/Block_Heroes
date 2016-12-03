package com.enbecko.objectcreator;

import com.enbecko.objectcreator.restart.TENewBase;
import com.enbecko.objectcreator.restart.TENewRenderer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class ClientProxy extends CommonProxy {

	public static KeyBinding KEY_X, KEY_Y, KEY_Z;

	@Override
	public void registerThings()
	{
		ClientRegistry.bindTileEntitySpecialRenderer(TEBaseCreatorBlock.class, new TEBaseCreatorBlockRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TENewBase.class, new TENewRenderer());
		MinecraftForge.EVENT_BUS.register(EventDispatcher.getTheEventDispatcher());

		KEY_X = new KeyBinding("key.x", Keyboard.KEY_X, "key.categories.MercenaryMod");
		KEY_Y = new KeyBinding("key.y", Keyboard.KEY_Y, "key.categories.MercenaryMod");
		KEY_Z = new KeyBinding("key.Z", Keyboard.KEY_Z, "key.categories.MercenaryMod");
		ClientRegistry.registerKeyBinding(KEY_X);
		ClientRegistry.registerKeyBinding(KEY_Y);
		ClientRegistry.registerKeyBinding(KEY_Z);
	}
}

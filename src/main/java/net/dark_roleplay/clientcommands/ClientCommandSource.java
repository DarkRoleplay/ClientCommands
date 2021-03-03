package net.dark_roleplay.clientcommands;

import net.minecraft.command.CommandSource;
import net.minecraft.command.ICommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;

public class ClientCommandSource extends CommandSource {
	public ClientCommandSource(ICommandSource sourceIn, Vector3d posIn, Vector2f rotationIn, ServerWorld worldIn, int permissionLevelIn, String nameIn, ITextComponent displayNameIn, MinecraftServer serverIn, @Nullable Entity entityIn) {
		super(sourceIn, posIn, rotationIn, worldIn, permissionLevelIn, nameIn, displayNameIn, serverIn, entityIn);
	}

	@Override
	public void sendFeedback(ITextComponent message, boolean allowLogging) {
		super.sendFeedback(message, false);
	}
}

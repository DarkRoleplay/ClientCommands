package net.dark_roleplay.clientcommands;

import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class ClientCommandSource extends CommandSourceStack
{
	public ClientCommandSource(CommandSource sourceIn, Vec3 posIn, Vec2 rotationIn, ServerLevel worldIn, int permissionLevelIn, String nameIn, Component displayNameIn, MinecraftServer serverIn, @Nullable Entity entityIn) {
		super(sourceIn, posIn, rotationIn, worldIn, permissionLevelIn, nameIn, displayNameIn, serverIn, entityIn, false, (a, b, c) -> {}, EntityAnchorArgument.Anchor.FEET);
	}
}

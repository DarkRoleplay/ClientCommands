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

public class ClientCommandSource extends CommandSourceStack {
	public ClientCommandSource(CommandSource source, Vec3 pos, Vec2 rotation, ServerLevel world, int permissionLevel, String name, Component displayName, MinecraftServer server, @Nullable Entity entity) {
		super(source, pos, rotation, world, permissionLevel, name, displayName, server, entity, false, (a, b, c) -> {
		}, EntityAnchorArgument.Anchor.FEET);
	}
}

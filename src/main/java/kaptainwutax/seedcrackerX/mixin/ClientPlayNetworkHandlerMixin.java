package kaptainwutax.seedcrackerX.mixin;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import kaptainwutax.seedcrackerX.SeedCracker;
import kaptainwutax.seedcrackerX.config.Config;
import kaptainwutax.seedcrackerX.cracker.DataAddedEvent;
import kaptainwutax.seedcrackerX.cracker.HashedSeedData;
import kaptainwutax.seedcrackerX.finder.FinderQueue;
import kaptainwutax.seedcrackerX.finder.ReloadFinders;
import kaptainwutax.seedcrackerX.init.ClientCommands;
import kaptainwutax.seedcrackerX.util.Log;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.util.telemetry.TelemetrySender;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.command.CommandSource;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import net.minecraft.network.packet.s2c.play.CommandTreeS2CPacket;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {

    @Shadow
    private ClientWorld world;
    @Shadow
    private CommandDispatcher<CommandSource> commandDispatcher;

    @Inject(method = "onChunkData", at = @At(value = "TAIL"))
    private void onChunkData(ChunkDataS2CPacket packet, CallbackInfo ci) {
        int chunkX = packet.getX();
        int chunkZ = packet.getZ();
        FinderQueue.get().onChunkData(this.world, new ChunkPos(chunkX, chunkZ));
    }

    @SuppressWarnings("unchecked")
    @Inject(method = "<init>", at = @At("RETURN"))
    public void onInit(MinecraftClient client, Screen screen, ClientConnection connection, GameProfile profile, TelemetrySender telemetrySender, CallbackInfo ci) {
        ClientCommands.registerCommands((CommandDispatcher<ServerCommandSource>) (Object) this.commandDispatcher);
    }

    @SuppressWarnings("unchecked")
    @Inject(method = "onCommandTree", at = @At("TAIL"))
    public void onOnCommandTree(CommandTreeS2CPacket packet, CallbackInfo ci) {
        ClientCommands.registerCommands((CommandDispatcher<ServerCommandSource>) (Object) this.commandDispatcher);
    }

    @Inject(method = "onGameJoin", at = @At(value = "TAIL"))
    public void onGameJoin(GameJoinS2CPacket packet, CallbackInfo ci) {
        newDimension(packet.dimensionType(), new HashedSeedData(packet.sha256Seed()));
    }

    @Inject(method = "onPlayerRespawn", at = @At(value = "TAIL"))
    public void onPlayerRespawn(PlayerRespawnS2CPacket packet, CallbackInfo ci) {
        newDimension(packet.getDimensionType(), new HashedSeedData(packet.getSha256Seed()));
    }

    public void newDimension(DimensionType dimension, HashedSeedData hashedSeedData) {
        ReloadFinders.reloadHeight(dimension.getMinimumY(), dimension.getMinimumY() + dimension.getLogicalHeight());

        if (SeedCracker.get().getDataStorage().addHashedSeedData(hashedSeedData, DataAddedEvent.POKE_BIOMES) && Config.get().active) {
            Log.warn(Log.translate("fetchedHashedSeed") + " [" + hashedSeedData.getHashedSeed() + "].");
        }
    }

}

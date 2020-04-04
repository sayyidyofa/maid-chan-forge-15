package me.lazydev.projects.maidchanforge;

import me.lazydev.projects.maidchanforge.models.PlayerLog;
import me.lazydev.projects.maidchanforge.models.ResponseMessage;
import me.lazydev.projects.maidchanforge.util.Reference;
import me.lazydev.projects.maidchanforge.util.bots.line.BotHandler;
import me.lazydev.projects.maidchanforge.util.db.sqlite.SQLiteHandler;
import me.lazydev.projects.maidchanforge.util.helper.Helpers;
import me.lazydev.projects.maidchanforge.util.websocket.WebSocketHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Date;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("maid-chan-forge")
public class MaidChanForge {

    public static final Logger LOGGER = LogManager.getLogger();
    private static final WebSocketHandler wsServer = new WebSocketHandler(Reference.WEBSOCKET_SERVER_PORT);
    private static SQLiteHandler sqLite = new SQLiteHandler(true);

    public MaidChanForge() {
        //FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    /*private void setup(final FMLCommonSetupEvent event) {

    }*/

    @SubscribeEvent
    public void onServerStarted(FMLServerStartedEvent event) {
        LOGGER.info(wsServer.getAddress());
    }

    @SubscribeEvent
    public void onServerStopping(FMLServerStoppingEvent event) throws IOException, InterruptedException {
        BotHandler.broadcast("");
        wsServer.broadcast("Server is shutting down...");
        wsServer.stop();
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
            PlayerLog playerLog = new PlayerLog(
                    event.getPlayer().getDisplayName().getString(),
                    new Date(),
                    "login",
                    Helpers.getIpFromAddress(((ServerPlayerEntity) event.getEntity()).connection.netManager.getRemoteAddress().toString())
                    );
            sqLite.storeAction(playerLog.playerName, playerLog.timestamp, playerLog.action, playerLog.remoteAddress);
            //wsServer.broadcast("[debug] A player has joined");
            //System.out.println(Helpers.getUserCache());
            //event.getPlayer().sendMessage(new StringTextComponent(String.format(Helpers.isFirstLogin(playerLog.playerName) ? "Welcome to our server, %s!": "Welcome back, %s!", playerLog.playerName)));
            wsServer.broadcast(Helpers.serialize(
                    new ResponseMessage(
                            "update/player",
                            playerLog.playerName,
                            playerLog.action
                    )
            ));
        }

        @SubscribeEvent
        public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
            PlayerLog playerLog = new PlayerLog(
                    event.getPlayer().getDisplayName().getString(),
                    new Date(),
                    "logout",
                    Helpers.getIpFromAddress(((ServerPlayerEntity) event.getEntity()).connection.netManager.getRemoteAddress().toString())
            );
            sqLite.storeAction(playerLog.playerName, playerLog.timestamp, playerLog.action, playerLog.remoteAddress);
            //wsServer.broadcast("[debug] A player has logged out");
            wsServer.broadcast(Helpers.serialize(
                    new ResponseMessage(
                            "update/player",
                            playerLog.playerName,
                            playerLog.action
                    )
            ));
        }

        /*@SubscribeEvent
        public static void onPlayerIdle(TickEvent.PlayerTickEvent event) {
            if (event.side.isServer()) {

            }
        }*/
    }
}

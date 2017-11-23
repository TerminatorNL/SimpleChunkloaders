package cf.terminator.simplechunkloaders.listeners;

import cf.terminator.simplechunkloaders.Database;
import cf.terminator.simplechunkloaders.Main;
import cf.terminator.simplechunkloaders.TicketManager;
import cf.terminator.simplechunkloaders.util.ChunkloaderPos;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;

import java.util.ArrayList;
import java.util.UUID;

public class LoginHandler {

    @SubscribeEvent
    public void onLogin(PlayerEvent.PlayerLoggedInEvent e){
        UUID uuid = e.player.getGameProfile().getId();
        if(uuid == null){
            Main.LOGGER.info("Ignored potentional fake player login: " + e.player);
            return;
        }
        ArrayList<ChunkloaderPos> chunksToLoad = Database.getForcedChunks(uuid.toString());
        for(ChunkloaderPos pos : chunksToLoad){
            Main.LOGGER.info(e.player.getCommandSenderName() + " has a chunkloader at: (World: " + pos.dim + ") " + pos.blockX + ", " + pos.blockY + "," + pos.blockZ);
            TicketManager.loadChunks(e.player.getGameProfile().getId().toString(), pos);
        }
    }

    @SubscribeEvent
    public void onLogout(PlayerEvent.PlayerLoggedOutEvent e){
        UUID uuid = e.player.getGameProfile().getId();
        if(uuid == null){
            Main.LOGGER.info("Ignored potentional fake player logout: " + e.player);
            return;
        }
        ArrayList<ChunkloaderPos> chunksToLoad = Database.getForcedChunks(uuid.toString());
        for(ChunkloaderPos pos : chunksToLoad){
            TicketManager.unloadChunks(e.player.getGameProfile().getId().toString(), pos);
        }
    }
}

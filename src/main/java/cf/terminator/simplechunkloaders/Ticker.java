package cf.terminator.simplechunkloaders;

import cf.terminator.simplechunkloaders.listeners.Updater;
import cf.terminator.simplechunkloaders.util.ChunkloaderPos;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

import java.util.HashMap;
import java.util.Map;

public class Ticker {
    public static HashMap<String, ChunkloaderPos> MARKED_FOR_REMOVAL = new HashMap<>();
    private static final int TIMEOUT = 20 * 20; /* 20 Seconds. */
    private int clock = TIMEOUT;

    @SubscribeEvent
    public void onTick(TickEvent.ServerTickEvent e){
        if(e.phase == TickEvent.Phase.START){
            return;
        }
        clock--;
        if(clock <= 0){
            clock = TIMEOUT;

            for(Map.Entry<String, TicketManager.TicketHolder> holders: TicketManager.TICKETHOLDERS.entrySet()){
                for(ChunkloaderPos pos : holders.getValue().getPositions()){
                    if(Updater.isValidChunkloader(pos) == false){
                        Database.delForcedChunk(holders.getKey(), pos);
                        holders.getValue().unloadChunks(pos);
                    }
                }
            }
        }
    }
}

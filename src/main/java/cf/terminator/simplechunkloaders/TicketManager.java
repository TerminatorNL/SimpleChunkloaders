package cf.terminator.simplechunkloaders;

import cf.terminator.simplechunkloaders.util.ChunkloaderPos;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraftforge.common.ForgeChunkManager;

import java.util.ArrayList;
import java.util.HashMap;

public class TicketManager {
    public static final int RANGE = 1; /* Thats 3x3 */
    public static final HashMap<String, TicketHolder> TICKETHOLDERS = new HashMap<>();



    public static void loadChunks(String player, ChunkloaderPos pos){
        if(TICKETHOLDERS.containsKey(player) == false){
            TICKETHOLDERS.put(player, new TicketHolder(player));
        }
        TicketHolder holder = TICKETHOLDERS.get(player);
        holder.loadChunks(pos);
    }

    public static void unloadChunks(String player, ChunkloaderPos pos){
        if(TICKETHOLDERS.containsKey(player) == false){
            throw new RuntimeException("Trying to unload already unloaded chunks");
        }
        TicketHolder holder = TICKETHOLDERS.get(player);
        holder.unloadChunks(pos);
    }





    public static class TicketHolder{
        private final HashMap<ChunkloaderPos, ForgeChunkManager.Ticket> POSITIONS = new HashMap<>();
        private final String player;

        TicketHolder(String player){
            this.player = player;
        }

        public ArrayList<ChunkloaderPos> getPositions(){
            return new ArrayList<>(POSITIONS.keySet());
        }

        public void loadChunks(ChunkloaderPos pos){
            if(POSITIONS.containsKey(pos) == false){
                ForgeChunkManager.Ticket ticket = ForgeChunkManager.requestTicket(
                        Main.INSTANCE,
                        FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(pos.dim),
                        ForgeChunkManager.Type.NORMAL);
                if(ticket == null){
                    throw new RuntimeException("Unable to obtain a ticket! Adjust the forge configurations to allow getting more tickets!");
                }
                POSITIONS.put(pos, ticket);
            }

            for(int x=-1;x<=RANGE;x++) {
                for(int z=-1;z<=RANGE;z++) {
                    ForgeChunkManager.forceChunk(POSITIONS.get(pos), new ChunkCoordIntPair(pos.chunkX + x, pos.chunkZ + z));
                }
            }
        }

        public void unloadChunks(ChunkloaderPos pos){
            if(POSITIONS.containsKey(pos) == false){
                Main.LOGGER.info("UNEXPECTED BEHAVIOR DETECTED. ATTEMPTED TO UNFORCE A CHUNK WHICH WAS NOT FORCED");
                return;
            }
            for(int x=-1;x<=RANGE;x++) {
                for(int z=-1;z<=RANGE;z++) {
                    ForgeChunkManager.unforceChunk(POSITIONS.get(pos), new ChunkCoordIntPair(pos.chunkX + x, pos.chunkZ + z));
                }
            }
            POSITIONS.remove(pos);
        }
    }
}

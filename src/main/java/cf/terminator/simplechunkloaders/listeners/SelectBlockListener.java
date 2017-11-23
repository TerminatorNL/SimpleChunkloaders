package cf.terminator.simplechunkloaders.listeners;

import cf.terminator.simplechunkloaders.Database;
import cf.terminator.simplechunkloaders.TicketManager;
import cf.terminator.simplechunkloaders.util.ChunkloaderPos;
import cf.terminator.simplechunkloaders.util.Firework;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class SelectBlockListener {
    private EntityPlayerMP player;
    private int ticksLeft = 20 * 6; /* 6 seconds */

    public SelectBlockListener(EntityPlayerMP player){
        this.player = player;
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);
        player.addChatMessage(new ChatComponentText("Please select an iron block by clicking it."));
    }

    private void unregister(){
        MinecraftForge.EVENT_BUS.unregister(this);
        FMLCommonHandler.instance().bus().unregister(this);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ServerTickEvent e){
        if(e.phase == TickEvent.Phase.START) {
            ticksLeft--;
            if(ticksLeft <= 0){
                if(player != null) {
                    unregister();
                    player.addChatMessage(new ChatComponentText("You did not select an iron block within 6 seconds, cancelled."));
                }
            }
        }
    }


    @SubscribeEvent
    public void onClick(PlayerInteractEvent e){
        if(e.entityPlayer == player){
            e.setCanceled(true);
            Chunk chunk = player.worldObj.getChunkFromBlockCoords(e.x, e.z);
            ChunkloaderPos pos = new ChunkloaderPos(player.dimension, chunk.xPosition, chunk.zPosition, e.x, e.y, e.z);
            if(Updater.isValidChunkloader(pos)){
                Database.addForcedChunk( player.getGameProfile().getId().toString(), pos);
                TicketManager.loadChunks(player.getGameProfile().getId().toString(), pos);
                player.addChatMessage(new ChatComponentText("Chunkloader activated!"));
                ItemStack rocketStack = Firework.generateRocket();
                EntityFireworkRocket rocket = new EntityFireworkRocket(player.worldObj, pos.blockX + 0.5D, pos.blockY, pos.blockZ + 0.5D, rocketStack);
                player.worldObj.spawnEntityInWorld(rocket);
                unregister();
            }else{
                player.addChatMessage(new ChatComponentText("That's not an iron block!"));
            }

        }
    }
}

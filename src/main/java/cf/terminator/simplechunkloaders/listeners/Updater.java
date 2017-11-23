package cf.terminator.simplechunkloaders.listeners;

import cf.terminator.simplechunkloaders.util.ChunkloaderPos;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.block.Block;
import net.minecraft.world.WorldServer;

public class Updater {

    public static boolean isValidChunkloader(ChunkloaderPos pos){
        WorldServer world  = FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(pos.dim);
        Block block = world.getBlock(pos.blockX, pos.blockY, pos.blockZ);
        return "tile.blockIron".equals(block.getUnlocalizedName());
    }
}

package cf.terminator.simplechunkloaders;

import cf.terminator.simplechunkloaders.commands.ChunkLoaderCommand;
import cf.terminator.simplechunkloaders.listeners.LoginHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.List;

@Mod(modid = Main.MODID_LOWER, name = Main.MODID, version = Main.VERSION, acceptableRemoteVersions = "*")
public class Main
{
    public static final String MODID_LOWER = "simplechunkloaders";
    public static final String MODID = "SimpleChunkloaders";
    public static final String VERSION = "1.0";

    public static Main INSTANCE;
    public static Logger LOGGER;
    public static File DATABASE_FILE = new File("./world/" + MODID + "-data.nbt");

    @EventHandler
    public void init(FMLPreInitializationEvent e){
        INSTANCE = this;
        LOGGER = e.getModLog();
        FMLCommonHandler.instance().bus().register(new Ticker());
        FMLCommonHandler.instance().bus().register(new LoginHandler());
        ForgeChunkManager.setForcedChunkLoadingCallback(this, new ForgeChunkManager.LoadingCallback() {
            @Override
            public void ticketsLoaded(List<ForgeChunkManager.Ticket> tickets, World world) {
                /* Required, but ignored */
            }
        });
    }


    @EventHandler
    public void init(FMLServerStartingEvent e){
        e.registerServerCommand(new ChunkLoaderCommand());
    }
}

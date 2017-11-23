package cf.terminator.simplechunkloaders.commands;

import cf.terminator.simplechunkloaders.Database;
import cf.terminator.simplechunkloaders.Main;
import cf.terminator.simplechunkloaders.listeners.SelectBlockListener;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChunkLoaderCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "chunkloader";
    }

    @Override
    public int getRequiredPermissionLevel(){
        return 0;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + getCommandName();
    }

    @Override
    public List getCommandAliases() {
        return Arrays.asList(new String[]{getCommandName()});
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if(sender instanceof EntityPlayerMP){

            if(Database.getForcedChunks(((EntityPlayerMP) sender).getGameProfile().getId().toString()).size() >= 3){
                sender.addChatMessage(new ChatComponentText("Sorry! You can have only 3 chunkloaders at all times."));
                return;
            }
            new SelectBlockListener((EntityPlayerMP) sender);
        }else{
            Main.LOGGER.info("Woopsie! You cannot run this command using console!");
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] p_71516_2_) {
        return new ArrayList<>();
    }

    @Override
    public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
        return false;
    }

}

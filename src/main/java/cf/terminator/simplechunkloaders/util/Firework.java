package cf.terminator.simplechunkloaders.util;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

import java.io.DataInputStream;
import java.io.IOException;

public class Firework {
    private static ItemStack ROCKET_STACK;


    public static ItemStack generateRocket(){
        if(ROCKET_STACK != null){
            return ROCKET_STACK.copy();
        }
        try {
            ROCKET_STACK = new ItemStack(Items.fireworks, 1);
            DataInputStream stream = new DataInputStream(Firework.class.getResourceAsStream("/Firework notification.nbt"));
            NBTTagCompound tag = CompressedStreamTools.read(stream);
            stream.close();
            ROCKET_STACK.setTagCompound(tag);
        } catch (IOException e) {
            e.printStackTrace();
        }
        /* Will generate a nullpointer exception if for some reason we could not read the resource. I doubt that. */
        return ROCKET_STACK.copy();
    }
}

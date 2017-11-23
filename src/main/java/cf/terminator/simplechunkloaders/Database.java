package cf.terminator.simplechunkloaders;

import cf.terminator.simplechunkloaders.util.ChunkloaderPos;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.io.IOException;
import java.util.ArrayList;

import static cf.terminator.simplechunkloaders.Main.DATABASE_FILE;

public class Database {

    private static NBTTagCompound CACHE = load();
    private static final int NBTTAGCOMPOUND_ID = 10;

    private static NBTTagCompound load(){
        if(DATABASE_FILE.exists() == false){
            return new NBTTagCompound();
        }
        try {
            return CompressedStreamTools.read(DATABASE_FILE);
        } catch (IOException e) {
            Main.LOGGER.info("Unable to load chunkloader data! Server startup interrupted!");
            throw new RuntimeException(e);
        }
    }

    private static void save(){
        try {
            if(CACHE == null){
                CACHE = new NBTTagCompound();
            }
            if(DATABASE_FILE.exists()){
                if(DATABASE_FILE.delete() == false) {
                    throw new IOException("Unable to delete database file at " + DATABASE_FILE);
                }
            }
            if(DATABASE_FILE.createNewFile() == false) {
                throw new IOException("Unable to create database file at " + DATABASE_FILE);
            }
            CompressedStreamTools.write(CACHE, DATABASE_FILE);
        } catch (IOException e) {
            Main.LOGGER.info("Unable to save chunkloader data!");
            e.printStackTrace();
        }
    }

    public static void addForcedChunk(String player, ChunkloaderPos pos){
        if(containsChunk(player,pos) == true){
            return;
        }
        NBTTagList taglist = CACHE.getTagList(player, NBTTAGCOMPOUND_ID);
        NBTTagCompound entry = new NBTTagCompound();
        entry.setInteger("DIM", pos.dim);
        entry.setInteger("chunkX", pos.chunkX);
        entry.setInteger("chunkZ", pos.chunkZ);
        entry.setInteger("blockX", pos.blockX);
        entry.setInteger("blockY", pos.blockY);
        entry.setInteger("blockZ", pos.blockZ);
        taglist.appendTag(entry);
        CACHE.setTag(player, taglist);
        save();
    }

    public static void delForcedChunk(String player, ChunkloaderPos pos){
        NBTTagList taglist = CACHE.getTagList(player, NBTTAGCOMPOUND_ID);
        int size = taglist.tagCount();
        for(int i=0;i<size;i++){
            NBTTagCompound entry = taglist.getCompoundTagAt(i);
            ChunkloaderPos loaded = new ChunkloaderPos(
                    entry.getInteger("DIM"),
                    entry.getInteger("chunkX"),
                    entry.getInteger("chunkZ"),
                    entry.getInteger("blockX"),
                    entry.getInteger("blockY"),
                    entry.getInteger("blockZ")
            );
            if(pos.equals(loaded)){
                taglist.removeTag(i);
                if(taglist.tagCount() == 0){
                    CACHE.removeTag(player);
                }else {
                    CACHE.setTag(player, taglist);
                }
                break;
            }
        }
        save();
    }

    public static boolean containsChunk(String player, ChunkloaderPos pos){
        return getForcedChunks(player).contains(pos);
    }

    public static ArrayList<ChunkloaderPos> getForcedChunks(String player){
        NBTTagList taglist = CACHE.getTagList(player, NBTTAGCOMPOUND_ID);
        int size = taglist.tagCount();
        ArrayList<ChunkloaderPos> list = new ArrayList<>();
        for(int i=0;i<size;i++){
            NBTTagCompound entry = taglist.getCompoundTagAt(i);
            list.add(new ChunkloaderPos(
                    entry.getInteger("DIM"),
                    entry.getInteger("chunkX"),
                    entry.getInteger("chunkZ"),
                    entry.getInteger("blockX"),
                    entry.getInteger("blockY"),
                    entry.getInteger("blockZ")
            ));
        }
        return list;
    }
}

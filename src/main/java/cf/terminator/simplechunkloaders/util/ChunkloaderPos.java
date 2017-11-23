package cf.terminator.simplechunkloaders.util;


public class ChunkloaderPos {

    public final int dim;
    public final int chunkX;
    public final int chunkZ;
    public final int blockX;
    public final int blockY;
    public final int blockZ;


    public ChunkloaderPos(int dim, int chunkX, int chunkZ, int blockX, int blockY, int blockZ){
        this.dim = dim;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.blockX = blockX;
        this.blockY = blockY;
        this.blockZ = blockZ;
    }
    @Override
    public int hashCode(){
        return dim + chunkX + chunkZ + blockX + blockY + blockZ;
    }

    @Override
    public boolean equals(Object o){
        if(o == null || o instanceof ChunkloaderPos == false){
            return false;
        }
        if(o == this){
            return true;
        }
        ChunkloaderPos other = (ChunkloaderPos) o;
        return  other.dim    == dim    &&
                other.chunkX == chunkX &&
                other.chunkZ == chunkZ &&
                other.blockX == blockX &&
                other.blockY == blockY &&
                other.blockZ == blockZ;
    }

}

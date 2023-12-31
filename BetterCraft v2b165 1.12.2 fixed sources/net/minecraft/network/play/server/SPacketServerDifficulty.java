// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class SPacketServerDifficulty implements Packet<INetHandlerPlayClient>
{
    private EnumDifficulty difficulty;
    private boolean difficultyLocked;
    
    public SPacketServerDifficulty() {
    }
    
    public SPacketServerDifficulty(final EnumDifficulty difficultyIn, final boolean difficultyLockedIn) {
        this.difficulty = difficultyIn;
        this.difficultyLocked = difficultyLockedIn;
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleServerDifficulty(this);
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.difficulty = EnumDifficulty.getDifficultyEnum(buf.readUnsignedByte());
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeByte(this.difficulty.getDifficultyId());
    }
    
    public boolean isDifficultyLocked() {
        return this.difficultyLocked;
    }
    
    public EnumDifficulty getDifficulty() {
        return this.difficulty;
    }
}

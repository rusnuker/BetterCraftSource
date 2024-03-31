// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.emote.keys;

import java.beans.ConstructorProperties;

public class EmoteKeyFrame
{
    private long offset;
    private EmotePose[] emotePoses;
    
    public EmoteKeyFrame clone() {
        final EmotePose[] emotePosesCopied = new EmotePose[this.emotePoses.length];
        int t = 0;
        EmotePose[] emotePoses;
        for (int length = (emotePoses = this.emotePoses).length, i = 0; i < length; ++i) {
            final EmotePose emotePose = emotePoses[i];
            emotePosesCopied[t] = new EmotePose(emotePose.getBodyPart(), emotePose.getX(), emotePose.getY(), emotePose.getZ(), emotePose.isInterpolate());
            ++t;
        }
        return new EmoteKeyFrame(this.offset, emotePosesCopied);
    }
    
    public long getOffset() {
        return this.offset;
    }
    
    public EmotePose[] getEmotePoses() {
        return this.emotePoses;
    }
    
    public void setOffset(final long offset) {
        this.offset = offset;
    }
    
    public void setEmotePoses(final EmotePose[] emotePoses) {
        this.emotePoses = emotePoses;
    }
    
    @ConstructorProperties({ "offset", "emotePoses" })
    public EmoteKeyFrame(final long offset, final EmotePose[] emotePoses) {
        this.offset = offset;
        this.emotePoses = emotePoses;
    }
}

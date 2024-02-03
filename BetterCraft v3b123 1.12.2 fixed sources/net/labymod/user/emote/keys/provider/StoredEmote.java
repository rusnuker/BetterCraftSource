// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.emote.keys.provider;

import net.labymod.user.emote.keys.EmotePose;
import net.labymod.user.emote.keys.EmoteKeyFrame;
import net.labymod.user.emote.keys.PoseAtTime;
import java.util.Iterator;

public class StoredEmote extends EmoteProvider
{
    private Iterator<PoseAtTime>[] iterator;
    
    public StoredEmote(final KeyFrameStorage keyFrameStorage) {
        this.iterator = new Iterator[7];
        final EmoteKeyFrame[] emoteKeyFrames = keyFrameStorage.getKeyframes();
        for (int id = 0; id < 7; ++id) {
            int count = 0;
            EmoteKeyFrame[] array;
            for (int length = (array = emoteKeyFrames).length, i = 0; i < length; ++i) {
                final EmoteKeyFrame emoteKeyFrame = array[i];
                EmotePose[] emotePoses;
                for (int length2 = (emotePoses = emoteKeyFrame.getEmotePoses()).length, j = 0; j < length2; ++j) {
                    final EmotePose subPose = emotePoses[j];
                    if (subPose.getBodyPart() == id) {
                        ++count;
                    }
                }
            }
            final PoseAtTime[] posesAtTime = new PoseAtTime[count];
            int index = 0;
            EmoteKeyFrame[] array2;
            for (int length3 = (array2 = emoteKeyFrames).length, k = 0; k < length3; ++k) {
                final EmoteKeyFrame keyframe = array2[k];
                EmotePose[] emotePoses2;
                for (int length4 = (emotePoses2 = keyframe.getEmotePoses()).length, l = 0; l < length4; ++l) {
                    final EmotePose pose = emotePoses2[l];
                    if (pose.getBodyPart() == id) {
                        posesAtTime[index] = new PoseAtTime(pose, keyframe.getOffset(), true);
                        ++index;
                    }
                }
            }
            final int keyFrameCount = count;
            this.iterator[id] = new Iterator<PoseAtTime>() {
                private int currentIndex = 0;
                
                @Override
                public boolean hasNext() {
                    return this.currentIndex < keyFrameCount && posesAtTime[this.currentIndex] != null;
                }
                
                @Override
                public PoseAtTime next() {
                    return posesAtTime[this.currentIndex++];
                }
                
                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }
    }
    
    @Override
    public boolean hasNext(final int bodyPoseId) {
        return this.iterator[bodyPoseId].hasNext();
    }
    
    @Override
    public PoseAtTime next(final int bodyPoseId) {
        return this.iterator[bodyPoseId].next();
    }
    
    @Override
    public boolean isWaiting() {
        return false;
    }
    
    @Override
    public void clear() {
        for (int i = 0; i < this.iterator.length; ++i) {
            while (this.hasNext(i)) {
                this.next(i);
            }
        }
    }
    
    public Iterator<PoseAtTime>[] getIterator() {
        return this.iterator;
    }
}

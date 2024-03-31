// 
// Decompiled by Procyon v0.6.0
// 

package com.mojang.text2speech;

import ca.weblite.objc.annotations.Msg;
import com.google.common.collect.Queues;
import ca.weblite.objc.Client;
import java.util.Queue;
import ca.weblite.objc.Proxy;
import ca.weblite.objc.NSObject;

public class NarratorOSX extends NSObject implements Narrator
{
    private final Proxy synth;
    private boolean speaking;
    private final Queue<String> queue;
    
    public NarratorOSX() {
        super("NSObject");
        this.synth = Client.getInstance().sendProxy("NSSpeechSynthesizer", "alloc", new Object[0]);
        this.queue = (Queue<String>)Queues.newConcurrentLinkedQueue();
        this.synth.send("init", new Object[0]);
        this.synth.send("setDelegate:", new Object[] { this });
    }
    
    private void startSpeaking(final String message) {
        this.synth.send("startSpeakingString:", new Object[] { message });
    }
    
    @Msg(selector = "speechSynthesizer:didFinishSpeaking:", signature = "v@:B")
    public void didFinishSpeaking(final boolean naturally) {
        if (this.queue.isEmpty()) {
            this.speaking = false;
        }
        else {
            this.startSpeaking(this.queue.poll());
        }
    }
    
    public void say(final String msg) {
        if (this.speaking) {
            this.queue.offer(msg);
        }
        else {
            this.speaking = true;
            this.startSpeaking(msg);
        }
    }
    
    public void clear() {
        this.queue.clear();
    }
    
    public boolean active() {
        return true;
    }
}

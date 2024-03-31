// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.utils;

import java.util.Timer;
import java.util.Objects;
import java.util.Arrays;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class TextAnimationUtils
{
    public static void writeTextAnimation(final String value, final long speed, final Consumer<String> consumer, final Consumer<String> cancel) {
        final AtomicReference<Runnable> cancelTask = new AtomicReference<Runnable>();
        final AtomicInteger index = new AtomicInteger();
        final TimerTask task = new TimerTask() {
            @Override
            public void run() {
                int formatCount = 0;
                for (int i = 0; i < value.length() && new String(Arrays.copyOfRange(value.toCharArray(), index.get() + i, index.get() + i + 2)).startsWith("§"); i += 2) {
                    ++formatCount;
                }
                consumer.accept(new String(Arrays.copyOfRange(value.toCharArray(), 0, index.getAndAdd((formatCount >= 1) ? (formatCount * 3 - 1) : 1))));
                if (index.get() > value.length()) {
                    if (Objects.nonNull(cancel)) {
                        cancel.accept(value);
                    }
                    cancelTask.get().run();
                }
            }
        };
        getTimer().schedule(task, speed, speed);
        cancelTask.set(task::cancel);
    }
    
    public static Runnable runningTextAnimation(final String value, final long speed, final int length, final Consumer<String> consumer) {
        final AtomicInteger index = new AtomicInteger(length);
        final TimerTask task = new TimerTask() {
            @Override
            public void run() {
                consumer.accept(new String(Arrays.copyOfRange(value.toCharArray(), index.get() - length, index.addAndGet(1))));
                if (index.get() >= value.length()) {
                    index.set(length);
                }
            }
        };
        getTimer().schedule(task, speed, speed);
        return task::cancel;
    }
    
    private static final Timer getTimer() {
        return new Timer();
    }
}

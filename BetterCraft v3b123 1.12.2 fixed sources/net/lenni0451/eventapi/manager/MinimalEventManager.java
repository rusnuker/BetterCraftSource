// 
// Decompiled by Procyon v0.6.0
// 

package net.lenni0451.eventapi.manager;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ConcurrentHashMap;
import net.lenni0451.eventapi.listener.IEventListener;
import java.util.List;
import net.lenni0451.eventapi.events.IEvent;
import java.util.Map;

public class MinimalEventManager
{
    private static final Map<Class<? extends IEvent>, List<IEventListener>> EVENT_LISTENER;
    
    static {
        EVENT_LISTENER = new ConcurrentHashMap<Class<? extends IEvent>, List<IEventListener>>();
    }
    
    public static void call(final IEvent event) {
        if (MinimalEventManager.EVENT_LISTENER.containsKey(event.getClass())) {
            MinimalEventManager.EVENT_LISTENER.get(event.getClass()).forEach(l -> {
                try {
                    l.onEvent(event2);
                }
                catch (final Throwable e) {
                    e.printStackTrace();
                }
            });
        }
    }
    
    public static <T extends IEventListener> void register(final Class<? extends IEvent> eventType, final T listener) {
        MinimalEventManager.EVENT_LISTENER.computeIfAbsent(eventType, c -> new CopyOnWriteArrayList()).add((Object)listener);
    }
    
    public static <T extends IEventListener> void unregister(final T listener) {
        MinimalEventManager.EVENT_LISTENER.forEach((key, value) -> value.removeIf(l -> l.equals(eventListener)));
    }
}

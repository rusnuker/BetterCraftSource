// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.event;

import java.util.Iterator;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.ArrayList;
import java.lang.annotation.Annotation;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.HashMap;

public class TeamSpeakEventDispatcher
{
    private static final HashMap<Class<?>, List<EventListener>> EVENT_HANDLERS;
    
    static {
        EVENT_HANDLERS = Maps.newHashMap();
    }
    
    public static void registerListener(final Object listener) {
        try {
            Method[] methods;
            for (int length = (methods = listener.getClass().getMethods()).length, i = 0; i < length; ++i) {
                final Method method = methods[i];
                if (method.isAnnotationPresent(EventHandler.class)) {
                    final EventHandler eventHandler = method.getAnnotation(EventHandler.class);
                    final Class[] parameterTypes = method.getParameterTypes();
                    if (parameterTypes.length == 1) {
                        if (Event.class.isAssignableFrom(parameterTypes[0])) {
                            final Class<?> parameter = parameterTypes[0];
                            List<EventListener> eventListeners = TeamSpeakEventDispatcher.EVENT_HANDLERS.get(parameter);
                            if (eventListeners == null) {
                                eventListeners = new ArrayList<EventListener>();
                                TeamSpeakEventDispatcher.EVENT_HANDLERS.put(parameter, eventListeners);
                            }
                            eventListeners.add(new EventListener(listener, method, eventHandler.priority()));
                            Collections.sort(eventListeners);
                        }
                    }
                }
            }
        }
        catch (final Throwable throwable) {
            throwable.printStackTrace();
            System.err.println("Could not register listener " + listener.getClass().getSimpleName());
        }
    }
    
    public static void unregisterListener(final Object listener) {
        try {
            for (final List<EventListener> eventListeners : TeamSpeakEventDispatcher.EVENT_HANDLERS.values()) {
                final Iterator<EventListener> iterator = eventListeners.iterator();
                while (iterator.hasNext()) {
                    final EventListener next = iterator.next();
                    if (next.getListener() != listener) {
                        continue;
                    }
                    iterator.remove();
                }
            }
        }
        catch (final Throwable throwable) {
            throwable.printStackTrace();
            System.err.println("Could not unregister listener " + listener.getClass().getSimpleName());
        }
    }
    
    public static void dispatch(final Event event) {
        final List<EventListener> eventListeners = TeamSpeakEventDispatcher.EVENT_HANDLERS.get(event.getClass());
        if (eventListeners == null) {
            return;
        }
        for (final EventListener eventListener : eventListeners) {
            try {
                eventListener.getListenerMethod().invoke(eventListener.getListener(), event);
            }
            catch (final Exception e) {
                e.printStackTrace();
                System.err.println("Could not dispatch event " + event.getClass().getSimpleName());
            }
        }
    }
}

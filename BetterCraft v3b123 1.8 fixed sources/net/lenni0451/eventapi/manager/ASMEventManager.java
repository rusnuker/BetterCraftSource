// 
// Decompiled by Procyon v0.6.0
// 

package net.lenni0451.eventapi.manager;

import java.lang.reflect.Field;
import net.lenni0451.eventapi.utils.ASMUtils;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import net.lenni0451.eventapi.events.types.IStoppable;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.ClassNode;
import java.util.HashMap;
import java.util.Iterator;
import java.lang.reflect.Modifier;
import net.lenni0451.eventapi.events.EventTarget;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ConcurrentHashMap;
import net.lenni0451.eventapi.listener.IErrorListener;
import net.lenni0451.eventapi.injection.asm.IInjectionEventHandler;
import java.lang.reflect.Method;
import java.util.List;
import net.lenni0451.eventapi.events.IEvent;
import java.util.Map;

public class ASMEventManager
{
    public static ClassLoader CLASS_LOADER;
    private static final Map<Class<? extends IEvent>, Map<Object, List<Method>>> TRACKED_LISTENER;
    private static final Map<Class<? extends IEvent>, IInjectionEventHandler> EVENT_HANDLER;
    private static final List<IErrorListener> ERROR_LISTENER;
    
    static {
        ASMEventManager.CLASS_LOADER = Thread.currentThread().getContextClassLoader();
        TRACKED_LISTENER = new ConcurrentHashMap<Class<? extends IEvent>, Map<Object, List<Method>>>();
        EVENT_HANDLER = new ConcurrentHashMap<Class<? extends IEvent>, IInjectionEventHandler>();
        ERROR_LISTENER = new CopyOnWriteArrayList<IErrorListener>();
    }
    
    public static void call(final IEvent event) {
        if (event != null && ASMEventManager.EVENT_HANDLER.containsKey(event.getClass())) {
            try {
                ASMEventManager.EVENT_HANDLER.get(event.getClass()).call(event);
                if (ASMEventManager.EVENT_HANDLER.containsKey(IEvent.class)) {
                    ASMEventManager.EVENT_HANDLER.get(IEvent.class).call(event);
                }
            }
            catch (final Throwable e) {
                if (ASMEventManager.ERROR_LISTENER.isEmpty()) {
                    throw new RuntimeException(e);
                }
                ASMEventManager.ERROR_LISTENER.forEach(errorListener -> errorListener.catchException(t));
            }
        }
    }
    
    public static void register(final Object eventListener) {
        final List<Class<? extends IEvent>> updatedEvents = new ArrayList<Class<? extends IEvent>>();
        final boolean isStaticCall = eventListener instanceof Class;
        Method[] declaredMethods;
        for (int length = (declaredMethods = ((Class)(isStaticCall ? eventListener : eventListener.getClass())).getDeclaredMethods()).length, i = 0; i < length; ++i) {
            final Method method = declaredMethods[i];
            final EventTarget eventTarget = method.getDeclaredAnnotation(EventTarget.class);
            if (eventTarget != null && method.getParameterTypes().length == 1 && IEvent.class.isAssignableFrom(method.getParameterTypes()[0])) {
                if (!isStaticCall || Modifier.isStatic(method.getModifiers())) {
                    if (isStaticCall || !Modifier.isStatic(method.getModifiers())) {
                        final Class<? extends IEvent> eventClass = (Class<? extends IEvent>)method.getParameterTypes()[0];
                        ASMEventManager.TRACKED_LISTENER.computeIfAbsent(eventClass, eventClazz -> new ConcurrentHashMap()).computeIfAbsent(eventListener, listener -> new CopyOnWriteArrayList()).add(method);
                        updatedEvents.add(eventClass);
                    }
                }
            }
        }
        rebuildEventHandler(updatedEvents);
    }
    
    public static void unregister(final Object eventListener) {
        final List<Class<? extends IEvent>> updatedEvents = new ArrayList<Class<? extends IEvent>>();
        final boolean isStaticCall = eventListener instanceof Class;
        Method[] declaredMethods;
        for (int length = (declaredMethods = ((Class)(isStaticCall ? eventListener : eventListener.getClass())).getDeclaredMethods()).length, i = 0; i < length; ++i) {
            final Method method = declaredMethods[i];
            final EventTarget eventTarget = method.getDeclaredAnnotation(EventTarget.class);
            if (eventTarget != null && method.getParameterTypes().length == 1 && IEvent.class.isAssignableFrom(method.getParameterTypes()[0])) {
                final Class<? extends IEvent> eventClass = (Class<? extends IEvent>)method.getParameterTypes()[0];
                ASMEventManager.TRACKED_LISTENER.computeIfAbsent(eventClass, eventClazz -> new ConcurrentHashMap()).remove(eventListener);
                if (ASMEventManager.TRACKED_LISTENER.get(eventClass).isEmpty()) {
                    ASMEventManager.TRACKED_LISTENER.remove(eventClass);
                    ASMEventManager.EVENT_HANDLER.remove(eventClass);
                }
                else {
                    updatedEvents.add(eventClass);
                }
            }
        }
        rebuildEventHandler(updatedEvents);
    }
    
    private static void rebuildEventHandler(final List<Class<? extends IEvent>> updatedEvents) {
        for (final Class<? extends IEvent> event : updatedEvents) {
            rebuildEventHandler(event);
        }
    }
    
    private static void rebuildEventHandler(final Class<? extends IEvent> event) {
        final List<Map.Entry<Method, Object>> methods = new ArrayList<Map.Entry<Method, Object>>();
        final Map<Object, String> instanceMappings = new HashMap<Object, String>();
        for (final Map.Entry<Object, List<Method>> entry : ASMEventManager.TRACKED_LISTENER.get(event).entrySet()) {
            for (final Method method : entry.getValue()) {
                methods.add(new Map.Entry<Method, Object>() {
                    @Override
                    public Method getKey() {
                        return method;
                    }
                    
                    @Override
                    public Object getValue() {
                        return entry.getKey();
                    }
                    
                    @Override
                    public Object setValue(final Object value) {
                        return entry.getKey();
                    }
                });
            }
        }
        methods.sort((o1, o2) -> {
            final EventTarget eventTarget1 = o1.getKey().getDeclaredAnnotation(EventTarget.class);
            final EventTarget eventTarget2 = o2.getKey().getDeclaredAnnotation(EventTarget.class);
            return Byte.compare(eventTarget2.priority(), eventTarget1.priority());
        });
        final ClassNode newHandlerNode = new ClassNode();
        newHandlerNode.visit(52, 33, "eventhandler/" + event.getSimpleName() + "EventHandler" + System.nanoTime(), null, "java/lang/Object", new String[] { IInjectionEventHandler.class.getName().replace(".", "/") });
        newHandlerNode.visitSource("EventAPI by Lenni0451", null);
        int index = 0;
        for (final Object listener : ASMEventManager.TRACKED_LISTENER.get(event).keySet()) {
            instanceMappings.put(listener, "listener" + index);
            final FieldNode fieldNode = new FieldNode(1, instanceMappings.get(listener), "L" + listener.getClass().getName().replace(".", "/") + ";", null, null);
            newHandlerNode.fields.add(fieldNode);
            ++index;
        }
        final MethodNode constructorNode = new MethodNode(1, "<init>", "()V", null, null);
        constructorNode.instructions.add(new VarInsnNode(25, 0));
        constructorNode.instructions.add(new MethodInsnNode(183, "java/lang/Object", "<init>", "()V"));
        constructorNode.instructions.add(new InsnNode(177));
        newHandlerNode.methods.add(constructorNode);
        final MethodNode methodNode = new MethodNode(1, IInjectionEventHandler.class.getDeclaredMethods()[0].getName(), "(L" + IEvent.class.getName().replace(".", "/") + ";)V", null, null);
        final InsnList instructions = new InsnList();
        for (final Map.Entry<Method, Object> entry2 : methods) {
            if (Modifier.isStatic(entry2.getKey().getModifiers())) {
                instructions.add(new VarInsnNode(25, 1));
                instructions.add(new TypeInsnNode(192, event.getName().replace(".", "/")));
                instructions.add(new MethodInsnNode(184, ((entry2.getValue() instanceof Class) ? entry2.getValue() : entry2.getValue().getClass()).getName().replace(".", "/"), entry2.getKey().getName(), "(L" + event.getName().replace(".", "/") + ";)V"));
            }
            else {
                instructions.add(new VarInsnNode(25, 0));
                instructions.add(new FieldInsnNode(180, newHandlerNode.name, instanceMappings.get(entry2.getValue()), "L" + entry2.getValue().getClass().getName().replace(".", "/") + ";"));
                instructions.add(new VarInsnNode(25, 1));
                instructions.add(new TypeInsnNode(192, event.getName().replace(".", "/")));
                instructions.add(new MethodInsnNode(182, entry2.getValue().getClass().getName().replace(".", "/"), entry2.getKey().getName(), "(L" + event.getName().replace(".", "/") + ";)V"));
            }
            if (IStoppable.class.isAssignableFrom(event)) {
                instructions.add(new VarInsnNode(25, 1));
                instructions.add(new TypeInsnNode(193, IStoppable.class.getName().replace(".", "/")));
                final LabelNode continueLabel = new LabelNode();
                instructions.add(new JumpInsnNode(153, continueLabel));
                instructions.add(new VarInsnNode(25, 1));
                instructions.add(new TypeInsnNode(192, IStoppable.class.getName().replace(".", "/")));
                instructions.add(new MethodInsnNode(185, IStoppable.class.getName().replace(".", "/"), IStoppable.class.getDeclaredMethods()[0].getName(), "()Z"));
                instructions.add(new JumpInsnNode(153, continueLabel));
                instructions.add(new InsnNode(177));
                instructions.add(continueLabel);
                instructions.add(new FrameNode(3, 0, null, 0, null));
            }
        }
        instructions.add(new InsnNode(177));
        methodNode.instructions = instructions;
        newHandlerNode.methods.add(methodNode);
        try {
            final Class<?> clazz = ASMUtils.defineClass(ASMEventManager.CLASS_LOADER, newHandlerNode);
            final IInjectionEventHandler eventHandler = (IInjectionEventHandler)clazz.newInstance();
            for (final Object listener2 : ASMEventManager.TRACKED_LISTENER.get(event).keySet()) {
                final Field field = clazz.getDeclaredField(instanceMappings.get(listener2));
                field.setAccessible(true);
                field.set(eventHandler, listener2);
            }
            ASMEventManager.EVENT_HANDLER.put(event, eventHandler);
        }
        catch (final Throwable t) {
            if (ASMEventManager.ERROR_LISTENER.isEmpty()) {
                throw new RuntimeException(t);
            }
            ASMEventManager.ERROR_LISTENER.forEach(errorListener -> errorListener.catchException(t4));
        }
    }
    
    public static void addErrorListener(final IErrorListener errorListener) {
        if (!ASMEventManager.ERROR_LISTENER.contains(errorListener)) {
            ASMEventManager.ERROR_LISTENER.add(errorListener);
        }
    }
    
    public static boolean removeErrorListener(final IErrorListener errorListener) {
        return ASMEventManager.ERROR_LISTENER.remove(errorListener);
    }
}

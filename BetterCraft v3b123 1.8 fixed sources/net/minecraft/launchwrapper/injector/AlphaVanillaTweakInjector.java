// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.launchwrapper.injector;

import java.lang.reflect.Modifier;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Frame;
import java.util.HashMap;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.applet.AppletStub;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Field;
import java.lang.reflect.Constructor;
import java.applet.Applet;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.IClassTransformer;

public class AlphaVanillaTweakInjector implements IClassTransformer
{
    @Override
    public byte[] transform(final String name, final String transformedName, final byte[] bytes) {
        return bytes;
    }
    
    public static void main(final String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<?> clazz;
        try {
            clazz = getaClass("net.minecraft.client.MinecraftApplet");
        }
        catch (final ClassNotFoundException ignored) {
            clazz = getaClass("com.mojang.minecraft.MinecraftApplet");
        }
        System.out.println("AlphaVanillaTweakInjector.class.getClassLoader() = " + AlphaVanillaTweakInjector.class.getClassLoader());
        final Constructor<?> constructor = clazz.getConstructor((Class<?>[])new Class[0]);
        final Object object = constructor.newInstance(new Object[0]);
        Field[] declaredFields;
        for (int length = (declaredFields = clazz.getDeclaredFields()).length, i = 0; i < length; ++i) {
            final Field field = declaredFields[i];
            final String name = field.getType().getName();
            if (!name.contains("awt") && !name.contains("java") && !name.equals("long")) {
                System.out.println("Found likely Minecraft candidate: " + field);
                final Field fileField = getWorkingDirField(name);
                if (fileField != null) {
                    System.out.println("Found File, changing to " + Launch.minecraftHome);
                    fileField.setAccessible(true);
                    fileField.set(null, Launch.minecraftHome);
                    break;
                }
            }
        }
        startMinecraft((Applet)object, args);
    }
    
    private static void startMinecraft(final Applet applet, final String[] args) {
        final Map<String, String> params = new HashMap<String, String>();
        String name = "Player" + System.currentTimeMillis() % 1000L;
        if (args.length > 0) {
            name = args[0];
        }
        String sessionId = "-";
        if (args.length > 1) {
            sessionId = args[1];
        }
        params.put("username", name);
        params.put("sessionid", sessionId);
        final Frame launcherFrameFake = new Frame();
        launcherFrameFake.setTitle("Minecraft");
        launcherFrameFake.setBackground(Color.BLACK);
        final JPanel panel = new JPanel();
        launcherFrameFake.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(854, 480));
        launcherFrameFake.add(panel, "Center");
        launcherFrameFake.pack();
        launcherFrameFake.setLocationRelativeTo(null);
        launcherFrameFake.setVisible(true);
        launcherFrameFake.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                System.exit(1);
            }
        });
        class LauncherFake extends Applet implements AppletStub
        {
            private static final long serialVersionUID = 1L;
            
            @Override
            public void appletResize(final int width, final int height) {
            }
            
            @Override
            public boolean isActive() {
                return true;
            }
            
            @Override
            public URL getDocumentBase() {
                try {
                    return new URL("http://www.minecraft.net/game/");
                }
                catch (final MalformedURLException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            
            @Override
            public URL getCodeBase() {
                try {
                    return new URL("http://www.minecraft.net/game/");
                }
                catch (final MalformedURLException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            
            @Override
            public String getParameter(final String paramName) {
                if (params.containsKey(paramName)) {
                    return params.get(paramName);
                }
                System.err.println("Client asked for parameter: " + paramName);
                return null;
            }
        }
        final LauncherFake fakeLauncher = new LauncherFake();
        applet.setStub(fakeLauncher);
        fakeLauncher.setLayout(new BorderLayout());
        fakeLauncher.add(applet, "Center");
        fakeLauncher.validate();
        launcherFrameFake.removeAll();
        launcherFrameFake.setLayout(new BorderLayout());
        launcherFrameFake.add(fakeLauncher, "Center");
        launcherFrameFake.validate();
        applet.init();
        applet.start();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                applet.stop();
            }
        });
        VanillaTweakInjector.loadIconsOnFrames();
    }
    
    private static Class<?> getaClass(final String name) throws ClassNotFoundException {
        return Launch.classLoader.findClass(name);
    }
    
    private static Field getWorkingDirField(final String name) throws ClassNotFoundException {
        final Class<?> clazz = getaClass(name);
        Field[] declaredFields;
        for (int length = (declaredFields = clazz.getDeclaredFields()).length, i = 0; i < length; ++i) {
            final Field field = declaredFields[i];
            if (Modifier.isStatic(field.getModifiers()) && field.getType().getName().equals("java.io.File")) {
                return field;
            }
        }
        return null;
    }
}

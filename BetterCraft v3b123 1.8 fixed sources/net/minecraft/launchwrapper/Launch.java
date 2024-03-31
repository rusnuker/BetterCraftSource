// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.launchwrapper;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Set;
import java.util.List;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import java.util.Arrays;
import org.apache.logging.log4j.Level;
import java.util.HashSet;
import java.util.Collection;
import java.util.ArrayList;
import joptsimple.OptionParser;
import java.util.HashMap;
import java.net.URLClassLoader;
import java.util.Map;
import java.io.File;

public class Launch
{
    private static final String DEFAULT_TWEAK = "net.minecraft.launchwrapper.VanillaTweaker";
    public static File minecraftHome;
    public static File assetsDir;
    public static Map<String, Object> blackboard;
    public static LaunchClassLoader classLoader;
    
    public static void main(final String[] args) {
        new Launch().launch(args);
    }
    
    private Launch() {
        final URLClassLoader ucl = (URLClassLoader)this.getClass().getClassLoader();
        Launch.classLoader = new LaunchClassLoader(ucl.getURLs());
        Launch.blackboard = new HashMap<String, Object>();
        Thread.currentThread().setContextClassLoader(Launch.classLoader);
    }
    
    private void launch(final String[] args) {
        final OptionParser parser = new OptionParser();
        parser.allowsUnrecognizedOptions();
        final OptionSpec<String> profileOption = parser.accepts("version", "The version we launched with").withRequiredArg();
        final OptionSpec<File> gameDirOption = parser.accepts("gameDir", "Alternative game directory").withRequiredArg().ofType(File.class);
        final OptionSpec<File> assetsDirOption = parser.accepts("assetsDir", "Assets directory").withRequiredArg().ofType(File.class);
        final OptionSpec<String> tweakClassOption = parser.accepts("tweakClass", "Tweak class(es) to load").withRequiredArg().defaultsTo("net.minecraft.launchwrapper.VanillaTweaker", new String[0]);
        final OptionSpec<String> nonOption = parser.nonOptions();
        final OptionSet options = parser.parse(args);
        Launch.minecraftHome = options.valueOf(gameDirOption);
        Launch.assetsDir = options.valueOf(assetsDirOption);
        final String profileName = options.valueOf(profileOption);
        final List<String> tweakClassNames = new ArrayList<String>(options.valuesOf(tweakClassOption));
        final List<String> argumentList = new ArrayList<String>();
        Launch.blackboard.put("TweakClasses", tweakClassNames);
        Launch.blackboard.put("ArgumentList", argumentList);
        final Set<String> allTweakerNames = new HashSet<String>();
        final List<ITweaker> allTweakers = new ArrayList<ITweaker>();
        try {
            final List<ITweaker> tweakers = new ArrayList<ITweaker>(tweakClassNames.size() + 1);
            Launch.blackboard.put("Tweaks", tweakers);
            ITweaker primaryTweaker = null;
            do {
                final Iterator<String> it = tweakClassNames.iterator();
                while (it.hasNext()) {
                    final String tweakName = it.next();
                    if (allTweakerNames.contains(tweakName)) {
                        LogWrapper.log(Level.WARN, "Tweak class name %s has already been visited -- skipping", tweakName);
                        it.remove();
                    }
                    else {
                        allTweakerNames.add(tweakName);
                        LogWrapper.log(Level.INFO, "Loading tweak class name %s", tweakName);
                        Launch.classLoader.addClassLoaderExclusion(tweakName.substring(0, tweakName.lastIndexOf(46)));
                        final ITweaker tweaker = (ITweaker)Class.forName(tweakName, true, Launch.classLoader).newInstance();
                        tweakers.add(tweaker);
                        it.remove();
                        if (primaryTweaker != null) {
                            continue;
                        }
                        LogWrapper.log(Level.INFO, "Using primary tweak class name %s", tweakName);
                        primaryTweaker = tweaker;
                    }
                }
                final Iterator<ITweaker> it2 = tweakers.iterator();
                while (it2.hasNext()) {
                    final ITweaker tweaker2 = it2.next();
                    LogWrapper.log(Level.INFO, "Calling tweak class %s", tweaker2.getClass().getName());
                    tweaker2.acceptOptions(options.valuesOf(nonOption), Launch.minecraftHome, Launch.assetsDir, profileName);
                    tweaker2.injectIntoClassLoader(Launch.classLoader);
                    allTweakers.add(tweaker2);
                    it2.remove();
                }
            } while (!tweakClassNames.isEmpty());
            for (final ITweaker tweaker3 : allTweakers) {
                argumentList.addAll(Arrays.asList(tweaker3.getLaunchArguments()));
            }
            final String launchTarget = primaryTweaker.getLaunchTarget();
            final Class<?> clazz = Class.forName(launchTarget, false, Launch.classLoader);
            final Method mainMethod = clazz.getMethod("main", String[].class);
            LogWrapper.info("Launching wrapped minecraft {%s}", launchTarget);
            mainMethod.invoke(null, argumentList.toArray(new String[argumentList.size()]));
        }
        catch (final Exception e) {
            LogWrapper.log(Level.ERROR, e, "Unable to launch", new Object[0]);
            System.exit(1);
        }
    }
}

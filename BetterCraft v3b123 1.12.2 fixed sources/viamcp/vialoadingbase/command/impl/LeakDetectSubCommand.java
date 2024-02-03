// 
// Decompiled by Procyon v0.6.0
// 

package viamcp.vialoadingbase.command.impl;

import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.List;
import java.util.Arrays;
import io.netty.util.ResourceLeakDetector;
import com.viaversion.viaversion.api.command.ViaCommandSender;
import com.viaversion.viaversion.api.command.ViaSubCommand;

public class LeakDetectSubCommand extends ViaSubCommand
{
    @Override
    public String name() {
        return "leakdetect";
    }
    
    @Override
    public String description() {
        return "Sets ResourceLeakDetector level";
    }
    
    @Override
    public boolean execute(final ViaCommandSender viaCommandSender, final String[] strings) {
        if (strings.length == 1) {
            try {
                final ResourceLeakDetector.Level level = ResourceLeakDetector.Level.valueOf(strings[0]);
                ResourceLeakDetector.setLevel(level);
                viaCommandSender.sendMessage("Set leak detector level to " + level);
            }
            catch (final IllegalArgumentException e) {
                viaCommandSender.sendMessage("Invalid level (" + Arrays.toString(ResourceLeakDetector.Level.values()) + ")");
            }
        }
        else {
            viaCommandSender.sendMessage("Current leak detection level is " + ResourceLeakDetector.getLevel());
        }
        return true;
    }
    
    @Override
    public List<String> onTabComplete(final ViaCommandSender sender, final String[] args) {
        if (args.length == 1) {
            return Arrays.stream(ResourceLeakDetector.Level.values()).map((Function<? super ResourceLeakDetector.Level, ?>)Enum::name).filter(it -> it.startsWith(array[0])).collect((Collector<? super Object, ?, List<String>>)Collectors.toList());
        }
        return super.onTabComplete(sender, args);
    }
}

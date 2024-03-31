// 
// Decompiled by Procyon v0.6.0
// 

package viamcp.vialoadingbase.model;

import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.BooleanSupplier;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import java.util.List;

public class Platform
{
    public static int COUNT;
    public static final List<ProtocolVersion> TEMP_INPUT_PROTOCOLS;
    private final String name;
    private final BooleanSupplier load;
    private final Runnable executor;
    private final Consumer<List<ProtocolVersion>> versionCallback;
    
    static {
        Platform.COUNT = 0;
        TEMP_INPUT_PROTOCOLS = new ArrayList<ProtocolVersion>();
    }
    
    public Platform(final String name, final BooleanSupplier load, final Runnable executor) {
        this(name, load, executor, null);
    }
    
    public Platform(final String name, final BooleanSupplier load, final Runnable executor, final Consumer<List<ProtocolVersion>> versionCallback) {
        this.name = name;
        this.load = load;
        this.executor = executor;
        this.versionCallback = versionCallback;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void createProtocolPath() {
        if (this.versionCallback != null) {
            this.versionCallback.accept(Platform.TEMP_INPUT_PROTOCOLS);
        }
    }
    
    public void build(final Logger logger) {
        if (this.load.getAsBoolean()) {
            try {
                this.executor.run();
                logger.info("Loaded Platform " + this.name);
                ++Platform.COUNT;
            }
            catch (final Throwable t) {
                logger.severe("An error occurred while loading Platform " + this.name + ":");
                t.printStackTrace();
            }
            return;
        }
        logger.severe("Platform " + this.name + " is not present");
    }
}

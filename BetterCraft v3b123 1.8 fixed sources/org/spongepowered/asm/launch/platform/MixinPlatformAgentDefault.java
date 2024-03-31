// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.launch.platform;

public class MixinPlatformAgentDefault extends MixinPlatformAgentAbstract
{
    @Override
    public void prepare() {
        final String compatibilityLevel = this.handle.getAttribute("MixinCompatibilityLevel");
        if (compatibilityLevel != null) {
            this.manager.setCompatibilityLevel(compatibilityLevel);
        }
        final String mixinConfigs = this.handle.getAttribute("MixinConfigs");
        if (mixinConfigs != null) {
            for (final String config : mixinConfigs.split(",")) {
                this.manager.addConfig(config.trim());
            }
        }
        final String tokenProviders = this.handle.getAttribute("MixinTokenProviders");
        if (tokenProviders != null) {
            for (final String provider : tokenProviders.split(",")) {
                this.manager.addTokenProvider(provider.trim());
            }
        }
        final String connectorClass = this.handle.getAttribute("MixinConnector");
        if (connectorClass != null) {
            this.manager.addConnector(connectorClass.trim());
        }
    }
}

// 
// Decompiled by Procyon v0.6.0
// 

package com.mojang.realmsclient.gui.screens;

import org.apache.logging.log4j.LogManager;
import com.google.common.util.concurrent.Futures;
import com.mojang.realmsclient.gui.LongRunningTask;
import com.mojang.realmsclient.util.RealmsTasks;
import javax.annotation.Nullable;
import com.google.common.util.concurrent.FutureCallback;
import net.minecraft.realms.Realms;
import java.util.concurrent.locks.ReentrantLock;
import com.mojang.realmsclient.dto.RealmsServerAddress;
import org.apache.logging.log4j.Logger;
import net.minecraft.realms.RealmsScreen;

public class RealmsResourcePackScreen extends RealmsScreen
{
    private static final Logger LOGGER;
    private final RealmsScreen lastScreen;
    private final RealmsServerAddress serverAddress;
    private final ReentrantLock connectLock;
    
    public RealmsResourcePackScreen(final RealmsScreen lastScreen, final RealmsServerAddress serverAddress, final ReentrantLock connectLock) {
        this.lastScreen = lastScreen;
        this.serverAddress = serverAddress;
        this.connectLock = connectLock;
    }
    
    @Override
    public void confirmResult(final boolean result, final int id) {
        try {
            if (!result) {
                Realms.setScreen(this.lastScreen);
            }
            else {
                try {
                    final RealmsServerAddress finalAddress = this.serverAddress;
                    Futures.addCallback(Realms.downloadResourcePack(this.serverAddress.resourcePackUrl, this.serverAddress.resourcePackHash), new FutureCallback<Object>() {
                        @Override
                        public void onSuccess(@Nullable final Object result) {
                            final RealmsLongRunningMcoTaskScreen longRunningMcoTaskScreen = new RealmsLongRunningMcoTaskScreen(RealmsResourcePackScreen.this.lastScreen, new RealmsTasks.RealmsConnectTask(RealmsResourcePackScreen.this.lastScreen, finalAddress));
                            longRunningMcoTaskScreen.start();
                            Realms.setScreen(longRunningMcoTaskScreen);
                        }
                        
                        @Override
                        public void onFailure(final Throwable t) {
                            Realms.clearResourcePack();
                            RealmsResourcePackScreen.LOGGER.error(t);
                            Realms.setScreen(new RealmsGenericErrorScreen("Failed to download resource pack!", RealmsResourcePackScreen.this.lastScreen));
                        }
                    });
                }
                catch (final Exception e) {
                    Realms.clearResourcePack();
                    RealmsResourcePackScreen.LOGGER.error(e);
                    Realms.setScreen(new RealmsGenericErrorScreen("Failed to download resource pack!", this.lastScreen));
                }
            }
        }
        finally {
            if (this.connectLock != null && this.connectLock.isHeldByCurrentThread()) {
                this.connectLock.unlock();
            }
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}

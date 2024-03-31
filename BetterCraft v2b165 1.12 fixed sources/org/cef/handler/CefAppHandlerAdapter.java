// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.handler;

import org.cef.callback.CefSchemeRegistrar;
import org.cef.CefApp;
import org.cef.callback.CefCommandLine;

public abstract class CefAppHandlerAdapter implements CefAppHandler
{
    private String[] args_;
    
    public CefAppHandlerAdapter(final String[] args) {
        this.args_ = args;
    }
    
    @Override
    public void onBeforeCommandLineProcessing(final String process_type, final CefCommandLine command_line) {
        if (process_type.isEmpty() && this.args_ != null) {
            boolean parseSwitchesDone = false;
            String[] args_;
            for (int length = (args_ = this.args_).length, i = 0; i < length; ++i) {
                final String arg = args_[i];
                if (parseSwitchesDone || arg.length() < 2) {
                    command_line.appendArgument(arg);
                }
                else {
                    final int switchCnt = arg.startsWith("--") ? 2 : (arg.startsWith("/") ? 1 : (arg.startsWith("-") ? 1 : 0));
                    switch (switchCnt) {
                        case 2: {
                            if (arg.length() == 2) {
                                parseSwitchesDone = true;
                                break;
                            }
                        }
                        case 1: {
                            final String[] switchVals = arg.substring(switchCnt).split("=");
                            if (switchVals.length == 2) {
                                command_line.appendSwitchWithValue(switchVals[0], switchVals[1]);
                                break;
                            }
                            command_line.appendSwitch(switchVals[0]);
                            break;
                        }
                        case 0: {
                            command_line.appendArgument(arg);
                            break;
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public boolean onBeforeTerminate() {
        return false;
    }
    
    @Override
    public void stateHasChanged(final CefApp.CefAppState state) {
    }
    
    @Override
    public void onRegisterCustomSchemes(final CefSchemeRegistrar registrar) {
    }
    
    @Override
    public void onContextInitialized() {
    }
    
    @Override
    public CefPrintHandler getPrintHandler() {
        return null;
    }
    
    @Override
    public void onScheduleMessagePumpWork(final long delay_ms) {
        CefApp.getInstance().doMessageLoopWork(delay_ms);
    }
    
    public void setArgs(final String[] args) {
        this.args_ = args;
    }
}

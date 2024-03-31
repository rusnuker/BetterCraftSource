// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.browser;

import java.awt.Point;
import org.cef.callback.CefPdfPrintCallback;
import org.cef.misc.CefPdfPrintSettings;
import org.cef.callback.CefRunFileDialogCallback;
import org.cef.handler.CefDialogHandler;
import org.cef.network.CefRequest;
import org.cef.callback.CefStringVisitor;
import java.util.Vector;
import org.cef.handler.CefWindowHandler;
import org.cef.handler.CefRenderHandler;
import org.cef.CefClient;
import java.awt.Component;

public interface CefBrowser
{
    void createImmediately();
    
    Component getUIComponent();
    
    CefClient getClient();
    
    CefRenderHandler getRenderHandler();
    
    CefWindowHandler getWindowHandler();
    
    boolean canGoBack();
    
    void goBack();
    
    boolean canGoForward();
    
    void goForward();
    
    boolean isLoading();
    
    void reload();
    
    void reloadIgnoreCache();
    
    void stopLoad();
    
    int getIdentifier();
    
    CefFrame getMainFrame();
    
    CefFrame getFocusedFrame();
    
    CefFrame getFrame(final long p0);
    
    CefFrame getFrame(final String p0);
    
    Vector<Long> getFrameIdentifiers();
    
    Vector<String> getFrameNames();
    
    int getFrameCount();
    
    boolean isPopup();
    
    boolean hasDocument();
    
    void viewSource();
    
    void getSource(final CefStringVisitor p0);
    
    void getText(final CefStringVisitor p0);
    
    void loadRequest(final CefRequest p0);
    
    void loadURL(final String p0);
    
    void loadString(final String p0, final String p1);
    
    void executeJavaScript(final String p0, final String p1, final int p2);
    
    String getURL();
    
    void close(final boolean p0);
    
    void setCloseAllowed();
    
    boolean doClose();
    
    void onBeforeClose();
    
    void setFocus(final boolean p0);
    
    void setWindowVisibility(final boolean p0);
    
    double getZoomLevel();
    
    void setZoomLevel(final double p0);
    
    void runFileDialog(final CefDialogHandler.FileDialogMode p0, final String p1, final String p2, final Vector<String> p3, final int p4, final CefRunFileDialogCallback p5);
    
    void startDownload(final String p0);
    
    void print();
    
    void printToPDF(final String p0, final CefPdfPrintSettings p1, final CefPdfPrintCallback p2);
    
    void find(final int p0, final String p1, final boolean p2, final boolean p3, final boolean p4);
    
    void stopFinding(final boolean p0);
    
    CefBrowser getDevTools();
    
    CefBrowser getDevTools(final Point p0);
    
    void replaceMisspelling(final String p0);
}

// 
// Decompiled by Procyon v0.6.0
// 

package fr.litarvan.openauth.microsoft;

import javafx.beans.value.ObservableValue;
import java.net.URLStreamHandler;
import java.net.HttpURLConnection;
import java.io.IOException;
import java.net.Proxy;
import java.net.URLConnection;
import java.net.URL;
import sun.net.www.protocol.https.Handler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.application.Platform;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.Container;
import javafx.embed.swing.JFXPanel;
import java.awt.Component;
import java.util.concurrent.CompletableFuture;
import javax.swing.JFrame;

public class LoginFrame extends JFrame
{
    private CompletableFuture<String> future;
    
    public LoginFrame() {
        this.setTitle("Connexion \u00e0 Microsoft");
        this.setSize(750, 750);
        this.setLocationRelativeTo(null);
        this.setContentPane(new JFXPanel());
    }
    
    public CompletableFuture<String> start(final String url) {
        if (this.future != null) {
            return this.future;
        }
        this.future = new CompletableFuture<String>();
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                LoginFrame.this.future.completeExceptionally(new MicrosoftAuthenticationException("User closed the authentication window"));
            }
        });
        Platform.runLater(() -> this.init(url));
        return this.future;
    }
    
    protected void init(final String url) {
        try {
            overrideFactory();
        }
        catch (final Throwable t) {}
        final WebView webView = new WebView();
        final JFXPanel content = (JFXPanel)this.getContentPane();
        content.setScene(new Scene(webView, this.getWidth(), this.getHeight()));
        webView.getEngine().locationProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.contains("access_token")) {
                this.setVisible(false);
                this.future.complete(newValue);
            }
            return;
        });
        webView.getEngine().load(url);
        this.setVisible(true);
    }
    
    protected static void overrideFactory() {
        URL.setURLStreamHandlerFactory(protocol -> {
            if ("https".equals(protocol)) {
                return new Handler() {
                    @Override
                    protected URLConnection openConnection(final URL url) throws IOException {
                        return this.openConnection(url, null);
                    }
                    
                    @Override
                    protected URLConnection openConnection(final URL url, final Proxy proxy) throws IOException {
                        final HttpURLConnection connection = (HttpURLConnection)super.openConnection(url, proxy);
                        if (("login.microsoftonline.com".equals(url.getHost()) && url.getPath().endsWith("/oauth2/authorize")) || ("login.live.com".equals(url.getHost()) && "/oauth20_authorize.srf".equals(url.getPath())) || ("login.live.com".equals(url.getHost()) && "/ppsecure/post.srf".equals(url.getPath())) || ("login.microsoftonline.com".equals(url.getHost()) && "/login.srf".equals(url.getPath())) || ("login.microsoftonline.com".equals(url.getHost()) && url.getPath().endsWith("/login")) || ("login.microsoftonline.com".equals(url.getHost()) && url.getPath().endsWith("/SAS/ProcessAuth")) || ("login.microsoftonline.com".equals(url.getHost()) && url.getPath().endsWith("/federation/oauth2")) || ("login.microsoftonline.com".equals(url.getHost()) && url.getPath().endsWith("/oauth2/v2.0/authorize"))) {
                            return new MicrosoftPatchedHttpURLConnection(url, connection);
                        }
                        return connection;
                    }
                };
            }
            else {
                return null;
            }
        });
    }
}

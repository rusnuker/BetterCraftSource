// 
// Decompiled by Procyon v0.6.0
// 

package fr.litarvan.openauth.microsoft;

import java.io.OutputStream;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.HttpURLConnection;

public class MicrosoftPatchedHttpURLConnection extends HttpURLConnection
{
    private final HttpURLConnection inner;
    
    public MicrosoftPatchedHttpURLConnection(final URL url, final HttpURLConnection inner) {
        super(url);
        this.inner = inner;
    }
    
    @Override
    public void setRequestMethod(final String method) throws ProtocolException {
        this.inner.setRequestMethod(method);
    }
    
    @Override
    public void setInstanceFollowRedirects(final boolean followRedirects) {
        this.inner.setInstanceFollowRedirects(followRedirects);
    }
    
    @Override
    public boolean getInstanceFollowRedirects() {
        return this.inner.getInstanceFollowRedirects();
    }
    
    @Override
    public String getRequestMethod() {
        return this.inner.getRequestMethod();
    }
    
    @Override
    public int getResponseCode() throws IOException {
        return this.inner.getResponseCode();
    }
    
    @Override
    public String getResponseMessage() throws IOException {
        return this.inner.getResponseMessage();
    }
    
    @Override
    public Map<String, List<String>> getHeaderFields() {
        return this.inner.getHeaderFields();
    }
    
    @Override
    public String getHeaderField(final String name) {
        return this.inner.getHeaderField(name);
    }
    
    @Override
    public String getHeaderField(final int n) {
        return this.inner.getHeaderField(n);
    }
    
    @Override
    public void disconnect() {
        this.inner.disconnect();
    }
    
    @Override
    public void setDoOutput(final boolean dooutput) {
        this.inner.setDoOutput(dooutput);
    }
    
    @Override
    public boolean usingProxy() {
        return this.inner.usingProxy();
    }
    
    @Override
    public void connect() throws IOException {
        this.inner.connect();
    }
    
    @Override
    public InputStream getInputStream() throws IOException {
        final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try (final InputStream in = this.inner.getInputStream()) {
            final byte[] data = new byte[8192];
            int n;
            while ((n = in.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, n);
            }
        }
        final byte[] patched = buffer.toString("UTF-8").replaceAll("integrity ?=", "integrity.disabled=").replaceAll("setAttribute\\(\"integrity\"", "setAttribute(\"integrity.disabled\"").getBytes(StandardCharsets.UTF_8);
        return new ByteArrayInputStream(patched);
    }
    
    @Override
    public OutputStream getOutputStream() throws IOException {
        return this.inner.getOutputStream();
    }
    
    @Override
    public InputStream getErrorStream() {
        return this.inner.getErrorStream();
    }
}

// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.mcleaks;

import java.util.Objects;
import java.io.Reader;
import java.io.InputStreamReader;
import com.google.gson.JsonParser;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import java.net.InetSocketAddress;
import java.io.IOException;
import com.google.gson.JsonObject;

public class McLeaksAPI
{
    public static RedeemedSession sessions_mcLeaksSession;
    
    public static RedeemedSession redeemSession(final String token) throws IOException {
        final JsonObject request = new JsonObject();
        request.addProperty("token", token);
        final JsonObject response = requestAndResponse("https://auth.mcleaks.net/v1/redeem", request.toString());
        ensureSuccess(response);
        final JsonObject result = response.getAsJsonObject("result");
        return new RedeemedSession(result.get("mcname").getAsString(), result.get("session").getAsString(), null);
    }
    
    public static void joinServer(final RedeemedSession redeemedSession, final String serverHash, final InetSocketAddress address) throws IOException {
        final JsonObject request = new JsonObject();
        request.addProperty("mcname", redeemedSession.name);
        request.addProperty("session", redeemedSession.session);
        request.addProperty("serverhash", serverHash);
        request.addProperty("server", String.valueOf(String.valueOf(address.getHostName())) + ":" + address.getPort());
        final JsonObject response = requestAndResponse("https://auth.mcleaks.net/v1/joinserver", request.toString());
        ensureSuccess(response);
    }
    
    private static void ensureSuccess(final JsonObject jsonObject) {
        if (!jsonObject.get("success").getAsBoolean()) {
            throw new IllegalArgumentException(jsonObject.get("errorMessage").getAsString());
        }
    }
    
    private static JsonObject requestAndResponse(final String url, final String request) throws IOException {
        final HttpsURLConnection urlConnection = (HttpsURLConnection)new URL(url).openConnection();
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        urlConnection.setRequestMethod("POST");
        urlConnection.setConnectTimeout(15000);
        urlConnection.setReadTimeout(5000);
        urlConnection.setDefaultUseCaches(false);
        urlConnection.setUseCaches(false);
        Throwable t = null;
        try {
            final OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream(), StandardCharsets.UTF_8);
            try {
                writer.write(request);
            }
            finally {
                if (writer != null) {
                    writer.close();
                }
            }
            if (writer != null) {
                writer.close();
            }
        }
        finally {
            if (t == null) {
                final Throwable t2 = t = null;
            }
            else {
                final Throwable t2 = null;
                if (t != t2) {
                    t.addSuppressed(t2);
                }
            }
        }
        if (t == null) {
            final Throwable t2 = t = null;
        }
        else {
            final Throwable t2 = null;
            if (t != t2) {
                t.addSuppressed(t2);
            }
        }
        return new JsonParser().parse(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8)).getAsJsonObject();
    }
    
    public static final class RedeemedSession
    {
        public final String name;
        public final String session;
        
        private RedeemedSession(final String name, final String session) {
            this.name = name;
            this.session = session;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final RedeemedSession that = (RedeemedSession)o;
            return Objects.equals(this.name, that.name) && Objects.equals(this.session, that.session);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(this.name, this.session);
        }
        
        @Override
        public String toString() {
            return "RedeemedSession{name='" + this.name + '\'' + ", session='" + this.session + '\'' + '}';
        }
    }
}

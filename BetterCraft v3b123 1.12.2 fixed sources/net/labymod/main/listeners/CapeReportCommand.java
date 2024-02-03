// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.main.listeners;

import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import net.labymod.utils.ModColor;
import net.labymod.main.LabyMod;
import net.labymod.api.events.MessageSendEvent;

public class CapeReportCommand implements MessageSendEvent
{
    private static long lastReport;
    
    static {
        CapeReportCommand.lastReport = 0L;
    }
    
    @Override
    public boolean onSend(final String msg) {
        final String m = msg.toLowerCase();
        if (m.startsWith("/capereport") || m.startsWith("/reportcape")) {
            if (msg.contains(" ")) {
                if (CapeReportCommand.lastReport < System.currentTimeMillis()) {
                    final String user = msg.split(" ")[1];
                    report(user);
                }
                else {
                    LabyMod.getInstance().displayMessageInChat(String.valueOf(ModColor.cl("c")) + "You've just reported a cape, please wait for a short while..");
                }
            }
            else {
                LabyMod.getInstance().displayMessageInChat(String.valueOf(ModColor.cl("c")) + msg + " <player>");
            }
            return true;
        }
        return false;
    }
    
    public static void report(final String user) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    CapeReportCommand.access$1(System.currentTimeMillis() + 20000L);
                    LabyMod.getInstance().displayMessageInChat(CapeReportCommand.jsonPost("http://api.labymod.net/capes/capeReport.php", "reporter=" + LabyMod.getInstance().getPlayerName() + "&capeowner=" + user));
                }
                catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    
    public static String jsonPost(final String urlStr, final String json) throws Exception {
        final URL url = new URL(urlStr);
        final HttpURLConnection httpConnection = (HttpURLConnection)url.openConnection();
        httpConnection.setDoOutput(true);
        httpConnection.setDoInput(true);
        httpConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        httpConnection.setRequestMethod("POST");
        final OutputStreamWriter out = new OutputStreamWriter(httpConnection.getOutputStream());
        out.write(json);
        out.close();
        final int code = httpConnection.getResponseCode();
        if (code / 100 == 2) {
            final BufferedReader br = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
            final StringBuffer sb = new StringBuffer();
            for (String str = br.readLine(); str != null; str = br.readLine()) {
                sb.append(str);
            }
            br.close();
            return sb.toString().replaceAll("\u00c2", "");
        }
        return "Response: " + code;
    }
    
    static /* synthetic */ void access$1(final long lastReport) {
        CapeReportCommand.lastReport = lastReport;
    }
}

// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.checkhost;

import java.util.HashMap;
import com.google.gson.JsonArray;
import java.util.Iterator;
import com.google.gson.JsonElement;
import java.util.ArrayList;
import java.net.URLEncoder;
import com.google.gson.JsonParser;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;
import com.google.gson.JsonObject;
import me.amkgre.bettercraft.client.mods.checkhost.results.CheckHostDnsResult;
import me.amkgre.bettercraft.client.mods.checkhost.results.CheckHostHttpResult;
import me.amkgre.bettercraft.client.mods.checkhost.results.CheckHostUdpResult;
import me.amkgre.bettercraft.client.mods.checkhost.results.CheckHostTcpResult;
import java.io.IOException;
import java.util.List;
import me.amkgre.bettercraft.client.mods.checkhost.results.CheckHostPingResult;
import java.util.Map;

public final class CheckHostAPI
{
    public static CheckResult<Map<CheckHostServer, CheckHostPingResult>> createPingRequest(final String host, final int maxNodes) throws IOException {
        final Map.Entry<String, List<CheckHostServer>> entry = sendCheckHostRequest(CheckHostType.PING, host, maxNodes);
        return new CheckResult<Map<CheckHostServer, CheckHostPingResult>>(CheckHostType.PING, entry.getKey(), entry.getValue());
    }
    
    public static CheckResult<Map<CheckHostServer, CheckHostTcpResult>> createTcpRequest(final String host, final int maxNodes) throws IOException {
        final Map.Entry<String, List<CheckHostServer>> entry = sendCheckHostRequest(CheckHostType.TCP, host, maxNodes);
        return new CheckResult<Map<CheckHostServer, CheckHostTcpResult>>(CheckHostType.TCP, entry.getKey(), entry.getValue());
    }
    
    public static CheckResult<Map<CheckHostServer, CheckHostUdpResult>> createUdpRequest(final String host, final int maxNodes) throws IOException {
        final Map.Entry<String, List<CheckHostServer>> entry = sendCheckHostRequest(CheckHostType.UDP, host, maxNodes);
        return new CheckResult<Map<CheckHostServer, CheckHostUdpResult>>(CheckHostType.UDP, entry.getKey(), entry.getValue());
    }
    
    public static CheckResult<Map<CheckHostServer, CheckHostHttpResult>> createHttpRequest(final String host, final int maxNodes) throws IOException {
        final Map.Entry<String, List<CheckHostServer>> entry = sendCheckHostRequest(CheckHostType.HTTP, host, maxNodes);
        return new CheckResult<Map<CheckHostServer, CheckHostHttpResult>>(CheckHostType.HTTP, entry.getKey(), entry.getValue());
    }
    
    public static CheckResult<Map<CheckHostServer, CheckHostDnsResult>> createDnsRequest(final String host, final int maxNodes) throws IOException {
        final Map.Entry<String, List<CheckHostServer>> entry = sendCheckHostRequest(CheckHostType.DNS, host, maxNodes);
        return new CheckResult<Map<CheckHostServer, CheckHostDnsResult>>(CheckHostType.DNS, entry.getKey(), entry.getValue());
    }
    
    private static JsonObject performGetRequest(final String url) throws IOException {
        final HttpURLConnection con = (HttpURLConnection)new URL(url).openConnection();
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:71.0) Gecko/20100101 Firefox/71.0");
        con.setRequestProperty("Accept", "application/json");
        final BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String json = "";
        String line = null;
        while ((line = br.readLine()) != null) {
            json = String.valueOf(json) + line + System.lineSeparator();
        }
        br.close();
        final JsonObject main = new JsonParser().parse(json).getAsJsonObject();
        con.disconnect();
        return main;
    }
    
    private static Map.Entry<String, List<CheckHostServer>> sendCheckHostRequest(final CheckHostType type, final String host, final int maxNodes) throws IOException {
        final JsonObject main = performGetRequest("https://check-host.net/check-" + type.getValue() + "?host=" + URLEncoder.encode(host, "UTF-8") + "&max_nodes=" + maxNodes);
        if (!main.has("nodes")) {
            throw new IOException("Invalid response!");
        }
        final ArrayList<CheckHostServer> servers = new ArrayList<CheckHostServer>();
        final JsonObject nodes = main.get("nodes").getAsJsonObject();
        for (final Map.Entry entry : nodes.entrySet()) {
            final JsonArray list = entry.getValue().getAsJsonArray();
            final ArrayList<String> infos = new ArrayList<String>();
            if (list.size() > 3) {
                for (int i = 3; i < list.size(); ++i) {
                    infos.add(list.get(i).getAsString());
                }
            }
            servers.add(new CheckHostServer(entry.getKey(), list.get(1).getAsString(), list.get(0).getAsString(), list.get(2).getAsString(), infos));
        }
        return new Map.Entry<String, List<CheckHostServer>>() {
            @Override
            public String getKey() {
                return main.get("request_id").getAsString();
            }
            
            @Override
            public List<CheckHostServer> getValue() {
                return servers;
            }
            
            @Override
            public List<CheckHostServer> setValue(final List<CheckHostServer> value) {
                return servers;
            }
        };
    }
    
    static Map<CheckHostServer, CheckHostPingResult> ping(final Map.Entry<String, List<CheckHostServer>> input) throws IOException {
        final String id = input.getKey();
        final List<CheckHostServer> servers = input.getValue();
        final JsonObject main = performGetRequest("https://check-host.net/check-result/" + URLEncoder.encode(id, "UTF-8"));
        final HashMap<CheckHostServer, CheckHostPingResult> result = new HashMap<CheckHostServer, CheckHostPingResult>();
        for (int i = 0; i < servers.size(); ++i) {
            final CheckHostServer server = servers.get(i);
            if (main.has(server.getName())) {
                if (!main.get(server.getName()).isJsonNull()) {
                    final JsonArray ja = main.get(server.getName()).getAsJsonArray();
                    for (int k = 0; k < ja.size(); ++k) {
                        final JsonElement elmt = ja.get(k);
                        if (elmt.isJsonArray()) {
                            final JsonArray ja2 = elmt.getAsJsonArray();
                            final ArrayList<CheckHostPingResult.PingEntry> pEntries = new ArrayList<CheckHostPingResult.PingEntry>();
                            for (int j = 0; j < ja2.size(); ++j) {
                                if (ja2.get(j).isJsonArray()) {
                                    final JsonArray ja3 = ja2.get(j).getAsJsonArray();
                                    if (ja3.size() != 2 && ja3.size() != 3) {
                                        pEntries.add(new CheckHostPingResult.PingEntry("Unable to resolve domain name.", -1.0, null));
                                    }
                                    else {
                                        final String status = ja3.get(0).getAsString();
                                        final double ping = ja3.get(1).getAsDouble();
                                        String addr = null;
                                        if (ja3.size() > 2) {
                                            addr = ja3.get(2).getAsString();
                                        }
                                        final CheckHostPingResult.PingEntry pEntry = new CheckHostPingResult.PingEntry(status, ping, addr);
                                        pEntries.add(pEntry);
                                    }
                                }
                            }
                            result.put(server, new CheckHostPingResult(pEntries));
                        }
                    }
                }
            }
        }
        return result;
    }
    
    static Map<CheckHostServer, CheckHostTcpResult> tcp(final Map.Entry<String, List<CheckHostServer>> input) throws IOException {
        final String id = input.getKey();
        final List<CheckHostServer> servers = input.getValue();
        final JsonObject main = performGetRequest("https://check-host.net/check-result/" + URLEncoder.encode(id, "UTF-8"));
        final HashMap<CheckHostServer, CheckHostTcpResult> result = new HashMap<CheckHostServer, CheckHostTcpResult>();
        for (int i = 0; i < servers.size(); ++i) {
            final CheckHostServer server = servers.get(i);
            JsonArray ja = null;
            if (main.has(server.getName()) && !main.get(server.getName()).isJsonNull()) {
                if ((ja = main.get(server.getName()).getAsJsonArray()).size() == 1) {
                    final JsonObject obj = ja.get(0).getAsJsonObject();
                    String error = null;
                    if (obj.has("error")) {
                        error = obj.get("error").getAsString();
                    }
                    String addr = null;
                    if (obj.has("address")) {
                        addr = obj.get("address").getAsString();
                    }
                    double ping = 0.0;
                    if (obj.has("time")) {
                        ping = obj.get("time").getAsDouble();
                    }
                    final CheckHostTcpResult res = new CheckHostTcpResult(ping, addr, error);
                    result.put(server, res);
                }
            }
        }
        return result;
    }
    
    static Map<CheckHostServer, CheckHostUdpResult> udp(final Map.Entry<String, List<CheckHostServer>> input) throws IOException {
        final String id = input.getKey();
        final List<CheckHostServer> servers = input.getValue();
        final JsonObject main = performGetRequest("https://check-host.net/check-result/" + URLEncoder.encode(id, "UTF-8"));
        final HashMap<CheckHostServer, CheckHostUdpResult> result = new HashMap<CheckHostServer, CheckHostUdpResult>();
        for (int i = 0; i < servers.size(); ++i) {
            final CheckHostServer server = servers.get(i);
            JsonArray ja = null;
            if (main.has(server.getName()) && !main.get(server.getName()).isJsonNull()) {
                if ((ja = main.get(server.getName()).getAsJsonArray()).size() == 1) {
                    final JsonObject obj = ja.get(0).getAsJsonObject();
                    String error = null;
                    if (obj.has("error")) {
                        error = obj.get("error").getAsString();
                    }
                    String addr = null;
                    if (obj.has("address")) {
                        addr = obj.get("address").getAsString();
                    }
                    double ping = 0.0;
                    if (obj.has("time")) {
                        ping = obj.get("time").getAsDouble();
                    }
                    double timeout = 0.0;
                    if (obj.has("timeout")) {
                        timeout = obj.get("timeout").getAsDouble();
                    }
                    final CheckHostUdpResult res = new CheckHostUdpResult(timeout, ping, addr, error);
                    result.put(server, res);
                }
            }
        }
        return result;
    }
    
    static Map<CheckHostServer, CheckHostHttpResult> http(final Map.Entry<String, List<CheckHostServer>> input) throws IOException {
        final String id = input.getKey();
        final List<CheckHostServer> servers = input.getValue();
        final JsonObject main = performGetRequest("https://check-host.net/check-result/" + URLEncoder.encode(id, "UTF-8"));
        final HashMap<CheckHostServer, CheckHostHttpResult> result = new HashMap<CheckHostServer, CheckHostHttpResult>();
        for (int i = 0; i < servers.size(); ++i) {
            final CheckHostServer server = servers.get(i);
            JsonArray ja = null;
            if (main.has(server.getName()) && !main.get(server.getName()).isJsonNull()) {
                if ((ja = main.get(server.getName()).getAsJsonArray()).size() == 1) {
                    ja = ja.get(0).getAsJsonArray();
                    final double ping = ja.get(1).getAsDouble();
                    final String status = ja.get(2).getAsString();
                    final int n;
                    final int error = n = ((ja.size() > 3 && ja.get(3).isJsonPrimitive()) ? ja.get(3).getAsInt() : -1);
                    if (error != -1) {
                        final String addr = (ja.size() > 4 && ja.get(4).isJsonPrimitive()) ? ja.get(4).getAsString() : null;
                        final CheckHostHttpResult res = new CheckHostHttpResult(status, ping, addr, error);
                        result.put(server, res);
                    }
                }
            }
        }
        return result;
    }
    
    static Map<CheckHostServer, CheckHostDnsResult> dns(final Map.Entry<String, List<CheckHostServer>> input) throws IOException {
        final String id = input.getKey();
        final List<CheckHostServer> servers = input.getValue();
        final JsonObject main = performGetRequest("https://check-host.net/check-result/" + URLEncoder.encode(id, "UTF-8"));
        final HashMap<CheckHostServer, CheckHostDnsResult> result = new HashMap<CheckHostServer, CheckHostDnsResult>();
        for (int i = 0; i < servers.size(); ++i) {
            final CheckHostServer server = servers.get(i);
            JsonArray ja = null;
            if (main.has(server.getName()) && !main.get(server.getName()).isJsonNull()) {
                if ((ja = main.get(server.getName()).getAsJsonArray()).size() == 1) {
                    final JsonObject obj = ja.get(0).getAsJsonObject();
                    final HashMap<String, String[]> domainInfos = new HashMap<String, String[]>();
                    for (final Map.Entry entry : obj.entrySet()) {
                        if (!entry.getKey().equals("TTL")) {
                            if (!entry.getValue().isJsonArray()) {
                                continue;
                            }
                            final JsonArray ja2 = entry.getValue().getAsJsonArray();
                            final String[] values = new String[ja2.size()];
                            for (int k = 0; k < ja2.size(); ++k) {
                                if (ja2.get(k).isJsonPrimitive()) {
                                    values[k] = ja2.get(k).getAsString();
                                }
                            }
                            domainInfos.put(entry.getKey(), values);
                        }
                    }
                    final CheckHostDnsResult res = new CheckHostDnsResult((obj.has("TTL") && obj.get("TTL").isJsonPrimitive()) ? obj.get("TTL").getAsInt() : -1, domainInfos);
                    result.put(server, res);
                }
            }
        }
        return result;
    }
}

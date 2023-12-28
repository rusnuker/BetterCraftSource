// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http.websocketx.extensions;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.Map;
import java.util.Collections;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import java.util.regex.Pattern;

public final class WebSocketExtensionUtil
{
    private static final String EXTENSION_SEPARATOR = ",";
    private static final String PARAMETER_SEPARATOR = ";";
    private static final char PARAMETER_EQUAL = '=';
    private static final Pattern PARAMETER;
    
    static boolean isWebsocketUpgrade(final HttpHeaders headers) {
        return headers.containsValue(HttpHeaderNames.CONNECTION, HttpHeaderValues.UPGRADE, true) && headers.contains(HttpHeaderNames.UPGRADE, HttpHeaderValues.WEBSOCKET, true);
    }
    
    public static List<WebSocketExtensionData> extractExtensions(final String extensionHeader) {
        final String[] rawExtensions = extensionHeader.split(",");
        if (rawExtensions.length > 0) {
            final List<WebSocketExtensionData> extensions = new ArrayList<WebSocketExtensionData>(rawExtensions.length);
            for (final String rawExtension : rawExtensions) {
                final String[] extensionParameters = rawExtension.split(";");
                final String name = extensionParameters[0].trim();
                Map<String, String> parameters;
                if (extensionParameters.length > 1) {
                    parameters = new HashMap<String, String>(extensionParameters.length - 1);
                    for (int i = 1; i < extensionParameters.length; ++i) {
                        final String parameter = extensionParameters[i].trim();
                        final Matcher parameterMatcher = WebSocketExtensionUtil.PARAMETER.matcher(parameter);
                        if (parameterMatcher.matches() && parameterMatcher.group(1) != null) {
                            parameters.put(parameterMatcher.group(1), parameterMatcher.group(3));
                        }
                    }
                }
                else {
                    parameters = Collections.emptyMap();
                }
                extensions.add(new WebSocketExtensionData(name, parameters));
            }
            return extensions;
        }
        return Collections.emptyList();
    }
    
    static String appendExtension(final String currentHeaderValue, final String extensionName, final Map<String, String> extensionParameters) {
        final StringBuilder newHeaderValue = new StringBuilder((currentHeaderValue != null) ? currentHeaderValue.length() : (extensionName.length() + 1));
        if (currentHeaderValue != null && !currentHeaderValue.trim().isEmpty()) {
            newHeaderValue.append(currentHeaderValue);
            newHeaderValue.append(",");
        }
        newHeaderValue.append(extensionName);
        for (final Map.Entry<String, String> extensionParameter : extensionParameters.entrySet()) {
            newHeaderValue.append(";");
            newHeaderValue.append(extensionParameter.getKey());
            if (extensionParameter.getValue() != null) {
                newHeaderValue.append('=');
                newHeaderValue.append(extensionParameter.getValue());
            }
        }
        return newHeaderValue.toString();
    }
    
    private WebSocketExtensionUtil() {
    }
    
    static {
        PARAMETER = Pattern.compile("^([^=]+)(=[\\\"]?([^\\\"]+)[\\\"]?)?$");
    }
}

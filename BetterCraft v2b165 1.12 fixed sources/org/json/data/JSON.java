// 
// Decompiled by Procyon v0.6.0
// 

package org.json.data;

import java.util.Scanner;
import java.net.URL;

public class JSON
{
    public static String getZipCode(final String content_url, final String code, final String content) throws Exception {
        final URL url = new URL(content_url);
        final Scanner scanner = new Scanner(url.openStream());
        final StringBuffer stringBuffer = new StringBuffer();
        while (scanner.hasNext()) {
            stringBuffer.append(scanner.nextLine());
        }
        final JSONObject jsonObject = JSONObject.parse(stringBuffer.toString());
        final String lowerCase;
        switch (lowerCase = code.toLowerCase()) {
            case "string": {
                return jsonObject.getString(content);
            }
            case "int": {
                return new StringBuilder().append(jsonObject.getInt(content)).toString();
            }
            case "long": {
                try {
                    return new StringBuilder().append(jsonObject.getLong(content)).toString();
                }
                catch (final Exception e) {
                    return "Ping is Null, please try again later";
                }
                break;
            }
            case "boolean": {
                return new StringBuilder().append(jsonObject.getBoolean(content)).toString();
            }
            case "newobject": {
                final String[] contents = content.split(";");
                final JSONObject jsonObject2 = jsonObject.getJSONObject(contents[0]);
                return jsonObject2.getString(contents[1]);
            }
            default:
                break;
        }
        return null;
    }
}

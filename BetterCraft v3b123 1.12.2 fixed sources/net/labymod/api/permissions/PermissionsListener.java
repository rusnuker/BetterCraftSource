// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.api.permissions;

import com.google.gson.JsonPrimitive;
import java.util.Iterator;
import com.google.gson.JsonObject;
import net.labymod.api.protocol.liquid.FixedLiquidBucketProtocol;
import net.labymod.main.LabyMod;
import net.labymod.support.util.Debug;
import java.util.Map;
import com.google.gson.JsonElement;
import net.labymod.api.events.ServerMessageEvent;

public class PermissionsListener implements ServerMessageEvent
{
    @Override
    public void onServerMessage(final String messageKey, final JsonElement serverMessage) {
        if (!messageKey.equals("PERMISSIONS") || !serverMessage.isJsonObject()) {
            return;
        }
        final JsonObject permissionsObject = serverMessage.getAsJsonObject();
        for (final Map.Entry<String, JsonElement> jsonEntry : permissionsObject.entrySet()) {
            final Permissions.Permission permission = Permissions.Permission.getPermissionByName(jsonEntry.getKey());
            final JsonPrimitive primitive = jsonEntry.getValue().isJsonPrimitive() ? jsonEntry.getValue().getAsJsonPrimitive() : null;
            if (permission == null || primitive == null || !primitive.isBoolean()) {
                Debug.log(Debug.EnumDebugMode.PLUGINMESSAGE, "Permission " + jsonEntry.getKey() + " is not supported!");
            }
            else {
                LabyMod.getInstance().getServerManager().getPermissionMap().put(permission, primitive.getAsBoolean());
                if (permission != Permissions.Permission.IMPROVED_LAVA) {
                    continue;
                }
                FixedLiquidBucketProtocol.onPermissionUpdate(primitive.getAsBoolean());
            }
        }
    }
}

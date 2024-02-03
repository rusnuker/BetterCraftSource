// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.api.events;

import net.labymod.user.util.UserActionEntry;
import java.util.List;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.labymod.user.User;

public interface UserMenuActionEvent
{
    void createActions(final User p0, final EntityPlayer p1, final NetworkPlayerInfo p2, final List<UserActionEntry> p3);
}

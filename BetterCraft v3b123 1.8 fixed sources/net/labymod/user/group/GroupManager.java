// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.group;

import net.labymod.user.User;
import net.labymod.support.util.Debug;
import java.util.Scanner;
import net.labymod.main.Source;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.awt.Color;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import com.google.gson.Gson;

public class GroupManager
{
    public static final LabyGroup DEFAULT_GROUP;
    public static final short GROUP_ID_PLUS = 10;
    private final Gson gson;
    private final ExecutorService executorService;
    private Map<Short, LabyGroup> groups;
    private Short[] groupOrder;
    
    static {
        DEFAULT_GROUP = new LabyGroup(0, "user", "User", null, 'f', "USER", "NONE", null, null).init();
    }
    
    public GroupManager(final ExecutorService executorService) {
        this.gson = new Gson();
        this.groups = new HashMap<Short, LabyGroup>();
        this.groupOrder = new Short[0];
        this.executorService = executorService;
        this.load();
    }
    
    public void load() {
        this.executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    final Map<Short, LabyGroup> map = new HashMap<Short, LabyGroup>();
                    final HttpURLConnection connection = (HttpURLConnection)new URL("http://dl.labymod.net/groups.json").openConnection();
                    connection.setRequestProperty("User-Agent", Source.getUserAgent());
                    connection.setReadTimeout(5000);
                    connection.setConnectTimeout(2000);
                    connection.connect();
                    final int responseCode = connection.getResponseCode();
                    if (responseCode / 100 == 2) {
                        String jsonString = "";
                        final Scanner scanner = new Scanner(connection.getInputStream());
                        while (scanner.hasNextLine()) {
                            jsonString = String.valueOf(jsonString) + scanner.nextLine();
                        }
                        scanner.close();
                        final GroupData groupData = GroupManager.this.gson.fromJson(jsonString, GroupData.class);
                        final LabyGroup[] labyGroups = groupData.getGroups();
                        final Short[] array = new Short[labyGroups.length];
                        int index = 0;
                        LabyGroup[] array2;
                        for (int length = (array2 = labyGroups).length, i = 0; i < length; ++i) {
                            final LabyGroup group = array2[i];
                            final short id = (short)group.getId();
                            group.init();
                            map.put(id, group);
                            array[index] = id;
                            ++index;
                        }
                        GroupManager.access$1(GroupManager.this, map);
                        GroupManager.access$2(GroupManager.this, array);
                    }
                    else {
                        Debug.log(Debug.EnumDebugMode.USER_MANAGER, "Wrong response code while loading groups: " + responseCode);
                    }
                }
                catch (final Exception error) {
                    error.printStackTrace();
                }
            }
        });
    }
    
    public LabyGroup getGroupById(final short id) {
        if (id == GroupManager.DEFAULT_GROUP.getId()) {
            return GroupManager.DEFAULT_GROUP;
        }
        return this.groups.get(id);
    }
    
    public boolean hasPermissionOf(final User user, final short groupId) {
        final LabyGroup userGroup = user.getGroup();
        if (groupId == GroupManager.DEFAULT_GROUP.getId()) {
            return true;
        }
        if (userGroup == null) {
            return false;
        }
        final short userGroupId = (short)userGroup.getId();
        Short[] groupOrder;
        for (int length = (groupOrder = this.groupOrder).length, i = 0; i < length; ++i) {
            final short orderId = groupOrder[i];
            if (orderId == userGroupId) {
                return true;
            }
            if (orderId == groupId) {
                return false;
            }
        }
        return false;
    }
    
    static /* synthetic */ void access$1(final GroupManager groupManager, final Map groups) {
        groupManager.groups = groups;
    }
    
    static /* synthetic */ void access$2(final GroupManager groupManager, final Short[] groupOrder) {
        groupManager.groupOrder = groupOrder;
    }
}

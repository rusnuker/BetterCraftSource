// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_19_4to1_19_3.storage;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.connection.StoredObject;

public class PlayerVehicleTracker extends StoredObject
{
    private int vehicleId;
    
    public PlayerVehicleTracker(final UserConnection user) {
        super(user);
        this.vehicleId = -1;
    }
    
    public int getVehicleId() {
        return this.vehicleId;
    }
    
    public void setVehicleId(final int vehicleId) {
        this.vehicleId = vehicleId;
    }
}

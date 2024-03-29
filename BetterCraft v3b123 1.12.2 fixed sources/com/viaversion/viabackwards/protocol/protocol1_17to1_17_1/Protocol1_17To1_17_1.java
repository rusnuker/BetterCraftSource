// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.protocol.protocol1_17to1_17_1;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.connection.StorableObject;
import com.viaversion.viaversion.api.connection.UserConnection;
import java.util.Iterator;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import com.viaversion.viabackwards.protocol.protocol1_16_4to1_17.storage.PlayerLastCursorItem;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viabackwards.protocol.protocol1_17to1_17_1.storage.InventoryStateIds;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.protocols.protocol1_17to1_16_4.ServerboundPackets1_17;
import com.viaversion.viaversion.protocols.protocol1_17to1_16_4.ClientboundPackets1_17;
import com.viaversion.viaversion.protocols.protocol1_17_1to1_17.ClientboundPackets1_17_1;
import com.viaversion.viabackwards.api.BackwardsProtocol;

public final class Protocol1_17To1_17_1 extends BackwardsProtocol<ClientboundPackets1_17_1, ClientboundPackets1_17, ServerboundPackets1_17, ServerboundPackets1_17>
{
    private static final int MAX_PAGE_LENGTH = 8192;
    private static final int MAX_TITLE_LENGTH = 128;
    private static final int MAX_PAGES = 200;
    
    public Protocol1_17To1_17_1() {
        super(ClientboundPackets1_17_1.class, ClientboundPackets1_17.class, ServerboundPackets1_17.class, ServerboundPackets1_17.class);
    }
    
    @Override
    protected void registerPackets() {
        ((Protocol<ClientboundPackets1_17_1, ClientboundPackets1_17, SM, SU>)this).registerClientbound(ClientboundPackets1_17_1.REMOVE_ENTITIES, null, wrapper -> {
            final int[] entityIds = wrapper.read(Type.VAR_INT_ARRAY_PRIMITIVE);
            wrapper.cancel();
            final int[] array;
            int i = 0;
            for (int length = array.length; i < length; ++i) {
                final int entityId = array[i];
                final PacketWrapper newPacket = wrapper.create(ClientboundPackets1_17.REMOVE_ENTITY);
                newPacket.write(Type.VAR_INT, entityId);
                newPacket.send(Protocol1_17To1_17_1.class);
            }
            return;
        });
        ((AbstractProtocol<ClientboundPackets1_17_1, CM, SM, SU>)this).registerClientbound(ClientboundPackets1_17_1.CLOSE_WINDOW, wrapper -> {
            final short containerId = wrapper.passthrough((Type<Short>)Type.UNSIGNED_BYTE);
            wrapper.user().get(InventoryStateIds.class).removeStateId(containerId);
            return;
        });
        ((AbstractProtocol<ClientboundPackets1_17_1, CM, SM, SU>)this).registerClientbound(ClientboundPackets1_17_1.SET_SLOT, wrapper -> {
            final short containerId2 = wrapper.passthrough((Type<Short>)Type.UNSIGNED_BYTE);
            final int stateId = wrapper.read((Type<Integer>)Type.VAR_INT);
            wrapper.user().get(InventoryStateIds.class).setStateId(containerId2, stateId);
            return;
        });
        ((AbstractProtocol<ClientboundPackets1_17_1, CM, SM, SU>)this).registerClientbound(ClientboundPackets1_17_1.WINDOW_ITEMS, wrapper -> {
            final short containerId3 = wrapper.passthrough((Type<Short>)Type.UNSIGNED_BYTE);
            final int stateId2 = wrapper.read((Type<Integer>)Type.VAR_INT);
            wrapper.user().get(InventoryStateIds.class).setStateId(containerId3, stateId2);
            wrapper.write(Type.FLAT_VAR_INT_ITEM_ARRAY, (Item[])(Object)wrapper.read((Type<T>)Type.FLAT_VAR_INT_ITEM_ARRAY_VAR_INT));
            final Item carried = wrapper.read(Type.FLAT_VAR_INT_ITEM);
            final PlayerLastCursorItem lastCursorItem = wrapper.user().get(PlayerLastCursorItem.class);
            if (lastCursorItem != null) {
                lastCursorItem.setLastCursorItem(carried);
            }
            return;
        });
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_17>)this).registerServerbound(ServerboundPackets1_17.CLOSE_WINDOW, wrapper -> {
            final short containerId4 = wrapper.passthrough((Type<Short>)Type.UNSIGNED_BYTE);
            wrapper.user().get(InventoryStateIds.class).removeStateId(containerId4);
            return;
        });
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_17>)this).registerServerbound(ServerboundPackets1_17.CLICK_WINDOW, wrapper -> {
            final short containerId5 = wrapper.passthrough((Type<Short>)Type.UNSIGNED_BYTE);
            final int stateId3 = wrapper.user().get(InventoryStateIds.class).removeStateId(containerId5);
            wrapper.write(Type.VAR_INT, (stateId3 == Integer.MAX_VALUE) ? 0 : stateId3);
            return;
        });
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_17>)this).registerServerbound(ServerboundPackets1_17.EDIT_BOOK, wrapper -> {
            final Item item = wrapper.read(Type.FLAT_VAR_INT_ITEM);
            final boolean signing = wrapper.read((Type<Boolean>)Type.BOOLEAN);
            wrapper.passthrough((Type<Object>)Type.VAR_INT);
            final CompoundTag tag = item.tag();
            StringTag titleTag = null;
            ListTag pagesTag;
            if (tag == null || (pagesTag = tag.get("pages")) == null || (signing && (titleTag = tag.get("title")) == null)) {
                wrapper.write(Type.VAR_INT, 0);
                wrapper.write(Type.BOOLEAN, false);
            }
            else {
                if (pagesTag.size() > 200) {
                    pagesTag = new ListTag(pagesTag.getValue().subList(0, 200));
                }
                wrapper.write(Type.VAR_INT, pagesTag.size());
                pagesTag.iterator();
                final Iterator iterator;
                while (iterator.hasNext()) {
                    final Tag pageTag = iterator.next();
                    String page = ((StringTag)pageTag).getValue();
                    if (page.length() > 8192) {
                        page = page.substring(0, 8192);
                    }
                    wrapper.write(Type.STRING, page);
                }
                wrapper.write(Type.BOOLEAN, signing);
                if (signing) {
                    if (titleTag == null) {
                        titleTag = tag.get("title");
                    }
                    String title = titleTag.getValue();
                    if (title.length() > 128) {
                        title = title.substring(0, 128);
                    }
                    wrapper.write(Type.STRING, title);
                }
            }
        });
    }
    
    @Override
    public void init(final UserConnection connection) {
        connection.put(new InventoryStateIds());
    }
}

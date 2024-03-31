// 
// Decompiled by Procyon v0.6.0
// 

package wdl;

import net.minecraft.client.resources.I18n;

public abstract class MessageTypeCategory
{
    public final String internalName;
    static final MessageTypeCategory CORE_RECOMMENDED;
    static final MessageTypeCategory CORE_DEBUG;
    
    static {
        CORE_RECOMMENDED = new I18nableMessageTypeCategory("CORE_RECOMMENDED", "wdl.messages.category.core_recommended");
        CORE_DEBUG = new I18nableMessageTypeCategory("CORE_DEBUG", "wdl.messages.category.core_debug");
    }
    
    public MessageTypeCategory(final String internalName) {
        this.internalName = internalName;
    }
    
    public abstract String getDisplayName();
    
    @Override
    public String toString() {
        return "MessageTypeCategory [internalName=" + this.internalName + ", displayName=" + this.getDisplayName() + "]";
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.internalName == null) ? 0 : this.internalName.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final MessageTypeCategory other = (MessageTypeCategory)obj;
        if (this.internalName == null) {
            if (other.internalName != null) {
                return false;
            }
        }
        else if (!this.internalName.equals(other.internalName)) {
            return false;
        }
        return true;
    }
    
    public static class I18nableMessageTypeCategory extends MessageTypeCategory
    {
        public final String i18nKey;
        
        public I18nableMessageTypeCategory(final String internalName, final String i18nKey) {
            super(internalName);
            this.i18nKey = i18nKey;
        }
        
        @Override
        public String getDisplayName() {
            return I18n.format(this.i18nKey, new Object[0]);
        }
    }
}

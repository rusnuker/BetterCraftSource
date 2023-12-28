// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.callback;

import java.util.Vector;

class CefContextMenuParams_N extends CefNativeAdapter implements CefContextMenuParams
{
    @Override
    public int getXCoord() {
        try {
            return this.N_GetXCoord(this.getNativeRef(null));
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return 0;
        }
    }
    
    @Override
    public int getYCoord() {
        try {
            return this.N_GetYCoord(this.getNativeRef(null));
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return 0;
        }
    }
    
    @Override
    public int getTypeFlags() {
        try {
            return this.N_GetTypeFlags(this.getNativeRef(null));
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return 0;
        }
    }
    
    @Override
    public String getLinkUrl() {
        try {
            return this.N_GetLinkUrl(this.getNativeRef(null));
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public String getUnfilteredLinkUrl() {
        try {
            return this.N_GetUnfilteredLinkUrl(this.getNativeRef(null));
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public String getSourceUrl() {
        try {
            return this.N_GetSourceUrl(this.getNativeRef(null));
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public boolean hasImageContents() {
        try {
            return this.N_HasImageContents(this.getNativeRef(null));
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public String getPageUrl() {
        try {
            return this.N_GetPageUrl(this.getNativeRef(null));
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public String getFrameUrl() {
        try {
            return this.N_GetFrameUrl(this.getNativeRef(null));
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public String getFrameCharset() {
        try {
            return this.N_GetFrameCharset(this.getNativeRef(null));
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public MediaType getMediaType() {
        try {
            return this.N_GetMediaType(this.getNativeRef(null));
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public int getMediaStateFlags() {
        try {
            return this.N_GetMediaStateFlags(this.getNativeRef(null));
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return 0;
        }
    }
    
    @Override
    public String getSelectionText() {
        try {
            return this.N_GetSelectionText(this.getNativeRef(null));
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public String getMisspelledWord() {
        try {
            return this.N_GetMisspelledWord(this.getNativeRef(null));
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public boolean getDictionarySuggestions(final Vector<String> suggestions) {
        try {
            return this.N_GetDictionarySuggestions(this.getNativeRef(null), suggestions);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean isEditable() {
        try {
            return this.N_IsEditable(this.getNativeRef(null));
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean isSpellCheckEnabled() {
        try {
            return this.N_IsSpellCheckEnabled(this.getNativeRef(null));
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public int getEditStateFlags() {
        try {
            return this.N_GetEditStateFlags(this.getNativeRef(null));
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return 0;
        }
    }
    
    private final native int N_GetXCoord(final long p0);
    
    private final native int N_GetYCoord(final long p0);
    
    private final native int N_GetTypeFlags(final long p0);
    
    private final native String N_GetLinkUrl(final long p0);
    
    private final native String N_GetUnfilteredLinkUrl(final long p0);
    
    private final native String N_GetSourceUrl(final long p0);
    
    private final native boolean N_HasImageContents(final long p0);
    
    private final native String N_GetPageUrl(final long p0);
    
    private final native String N_GetFrameUrl(final long p0);
    
    private final native String N_GetFrameCharset(final long p0);
    
    private final native MediaType N_GetMediaType(final long p0);
    
    private final native int N_GetMediaStateFlags(final long p0);
    
    private final native String N_GetSelectionText(final long p0);
    
    private final native String N_GetMisspelledWord(final long p0);
    
    private final native boolean N_GetDictionarySuggestions(final long p0, final Vector<String> p1);
    
    private final native boolean N_IsEditable(final long p0);
    
    private final native boolean N_IsSpellCheckEnabled(final long p0);
    
    private final native int N_GetEditStateFlags(final long p0);
}

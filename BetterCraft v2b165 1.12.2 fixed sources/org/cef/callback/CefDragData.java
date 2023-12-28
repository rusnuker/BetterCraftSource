// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.callback;

import java.util.Vector;
import java.io.OutputStream;

public abstract class CefDragData
{
    CefDragData() {
    }
    
    @Override
    protected void finalize() throws Throwable {
        this.dispose();
        super.finalize();
    }
    
    public static final CefDragData create() {
        return CefDragData_N.createNative();
    }
    
    public abstract CefDragData clone();
    
    public abstract void dispose();
    
    public abstract boolean isReadOnly();
    
    public abstract boolean isLink();
    
    public abstract boolean isFragment();
    
    public abstract boolean isFile();
    
    public abstract String getLinkURL();
    
    public abstract String getLinkTitle();
    
    public abstract String getLinkMetadata();
    
    public abstract String getFragmentText();
    
    public abstract String getFragmentHtml();
    
    public abstract String getFragmentBaseURL();
    
    public abstract int getFileContents(final OutputStream p0);
    
    public abstract String getFileName();
    
    public abstract boolean getFileNames(final Vector<String> p0);
    
    public abstract void setLinkURL(final String p0);
    
    public abstract void setLinkTitle(final String p0);
    
    public abstract void setLinkMetadata(final String p0);
    
    public abstract void setFragmentText(final String p0);
    
    public abstract void setFragmentHtml(final String p0);
    
    public abstract void setFragmentBaseURL(final String p0);
    
    public abstract void resetFileContents();
    
    public abstract void addFile(final String p0, final String p1);
    
    @Override
    public String toString() {
        return "CefDragData [isReadOnly()=" + this.isReadOnly() + ", isLink()=" + this.isLink() + ", isFragment()=" + this.isFragment() + ", isFile()=" + this.isFile() + ", getLinkURL()=" + this.getLinkURL() + ", getLinkTitle()=" + this.getLinkTitle() + ", getLinkMetadata()=" + this.getLinkMetadata() + ", getFragmentText()=" + this.getFragmentText() + ", getFragmentHtml()=" + this.getFragmentHtml() + ", getFragmentBaseURL()=" + this.getFragmentBaseURL() + ", getFileName()=" + this.getFileName() + "]";
    }
    
    public static final class DragOperations
    {
        public static final int DRAG_OPERATION_NONE = 0;
        public static final int DRAG_OPERATION_COPY = 1;
        public static final int DRAG_OPERATION_LINK = 2;
        public static final int DRAG_OPERATION_GENERIC = 4;
        public static final int DRAG_OPERATION_PRIVATE = 8;
        public static final int DRAG_OPERATION_MOVE = 16;
        public static final int DRAG_OPERATION_DELETE = 32;
        public static final int DRAG_OPERATION_EVERY = Integer.MAX_VALUE;
    }
}

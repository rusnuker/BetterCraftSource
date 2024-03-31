// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.transformer;

import org.spongepowered.asm.mixin.extensibility.IActivityContext;

public class ActivityStack implements IActivityContext
{
    public static final String GLUE_STRING = " -> ";
    private final Activity head;
    private Activity tail;
    private String glue;
    
    public ActivityStack() {
        this(null, " -> ");
    }
    
    public ActivityStack(final String root) {
        this(root, " -> ");
    }
    
    public ActivityStack(final String root, final String glue) {
        final Activity activity = new Activity(null, root);
        this.tail = activity;
        this.head = activity;
        this.glue = glue;
    }
    
    @Override
    public void clear() {
        this.tail = this.head;
        this.head.next = null;
    }
    
    @Override
    public IActivity begin(final String description) {
        return this.tail = new Activity(this.tail, (description != null) ? description : "null");
    }
    
    @Override
    public IActivity begin(String descriptionFormat, final Object... args) {
        if (descriptionFormat == null) {
            descriptionFormat = "null";
        }
        return this.tail = new Activity(this.tail, String.format(descriptionFormat, args));
    }
    
    void end(final Activity activity) {
        this.tail = activity.last;
        this.tail.next = null;
    }
    
    @Override
    public String toString() {
        return this.toString(this.glue);
    }
    
    @Override
    public String toString(final String glue) {
        if (this.head.description == null && this.head.next == null) {
            return "Unknown";
        }
        final StringBuilder sb = new StringBuilder();
        for (Activity activity = this.head; activity != null; activity = activity.next) {
            if (activity.description != null) {
                sb.append(activity.description);
                if (activity.next != null) {
                    sb.append(glue);
                }
            }
        }
        return sb.toString();
    }
    
    public class Activity implements IActivity
    {
        public String description;
        Activity last;
        Activity next;
        
        Activity(final Activity last, final String description) {
            if (last != null) {
                last.next = this;
            }
            this.last = last;
            this.description = description;
        }
        
        @Override
        public void append(final String text) {
            this.description = ((this.description != null) ? (this.description + text) : text);
        }
        
        @Override
        public void append(final String textFormat, final Object... args) {
            this.append(String.format(textFormat, args));
        }
        
        @Override
        public void end() {
            if (this.last != null) {
                ActivityStack.this.end(this);
                this.last = null;
            }
        }
        
        @Override
        public void next(final String description) {
            if (this.next != null) {
                this.next.end();
            }
            this.description = description;
        }
        
        @Override
        public void next(String descriptionFormat, final Object... args) {
            if (descriptionFormat == null) {
                descriptionFormat = "null";
            }
            this.next(String.format(descriptionFormat, args));
        }
    }
}

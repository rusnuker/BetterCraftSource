// 
// Decompiled by Procyon v0.6.0
// 

package org.cef;

import java.awt.Point;
import java.awt.Component;

public class DummyComponent extends Component
{
    @Override
    public Point getLocationOnScreen() {
        return new Point(0, 0);
    }
}

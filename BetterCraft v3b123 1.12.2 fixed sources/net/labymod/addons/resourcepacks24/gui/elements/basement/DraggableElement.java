// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.addons.resourcepacks24.gui.elements.basement;

public abstract class DraggableElement extends DeletableElement
{
    protected double lastX;
    protected double lastY;
    protected double lastWidth;
    protected double lastHeight;
    
    public DraggableElement(final boolean deletable) {
        super(deletable);
    }
    
    @Override
    public boolean draw(final double x, final double y, final double width, final double height, final int mouseX, final int mouseY) {
        this.lastX = x;
        this.lastY = y;
        this.lastWidth = width;
        this.lastHeight = height;
        return super.draw(x, y, width, height, mouseX, mouseY);
    }
    
    public static class Dragging
    {
        private DraggableElement element;
        private double offsetX;
        private double offsetY;
        private double width;
        private double height;
        private boolean valid;
        
        public Dragging(final DraggableElement element, final int mouseX, final int mouseY) {
            this.element = element;
            this.offsetX = mouseX - element.lastX;
            this.offsetY = mouseY - element.lastY;
            this.width = element.lastWidth;
            this.height = element.lastHeight;
        }
        
        public DraggableElement getElement() {
            return this.element;
        }
        
        public double getOffsetX() {
            return this.offsetX;
        }
        
        public double getOffsetY() {
            return this.offsetY;
        }
        
        public double getWidth() {
            return this.width;
        }
        
        public double getHeight() {
            return this.height;
        }
        
        public boolean isValid() {
            return this.valid;
        }
        
        public void setValid(final boolean valid) {
            this.valid = valid;
        }
    }
}

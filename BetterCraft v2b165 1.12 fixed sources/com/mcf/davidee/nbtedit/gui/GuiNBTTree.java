// 
// Decompiled by Procyon v0.6.0
// 

package com.mcf.davidee.nbtedit.gui;

import net.minecraft.client.gui.GuiScreen;
import java.util.Comparator;
import java.util.Collections;
import com.mcf.davidee.nbtedit.NBTStringHelper;
import java.util.Map;
import org.apache.logging.log4j.Level;
import com.mcf.davidee.nbtedit.NBTHelper;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTBase;
import net.minecraft.client.renderer.BufferBuilder;
import java.awt.Color;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.input.Mouse;
import com.mcf.davidee.nbtedit.nbt.SaveStates;
import java.util.Iterator;
import com.mcf.davidee.nbtedit.NBTEdit;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import java.util.ArrayList;
import com.mcf.davidee.nbtedit.nbt.NamedNBT;
import com.mcf.davidee.nbtedit.nbt.Node;
import java.util.List;
import com.mcf.davidee.nbtedit.nbt.NBTTree;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class GuiNBTTree extends Gui
{
    private Minecraft mc;
    private NBTTree tree;
    private List<GuiNBTNode> nodes;
    private GuiSaveSlotButton[] saves;
    private GuiNBTButton[] buttons;
    private final int X_GAP = 10;
    private final int START_X = 10;
    private final int START_Y = 30;
    private final int Y_GAP;
    private int y;
    private int yClick;
    private int bottom;
    private int width;
    private int height;
    private int heightDiff;
    private int offset;
    private Node<NamedNBT> focused;
    private int focusedSlotIndex;
    private GuiEditNBT window;
    
    public Node<NamedNBT> getFocused() {
        return this.focused;
    }
    
    public GuiSaveSlotButton getFocusedSaveSlot() {
        return (this.focusedSlotIndex != -1) ? this.saves[this.focusedSlotIndex] : null;
    }
    
    public NBTTree getNBTTree() {
        return this.tree;
    }
    
    public GuiNBTTree(final NBTTree tree) {
        this.mc = Minecraft.getMinecraft();
        this.Y_GAP = Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 2;
        this.tree = tree;
        this.yClick = -1;
        this.focusedSlotIndex = -1;
        this.nodes = new ArrayList<GuiNBTNode>();
        this.buttons = new GuiNBTButton[16];
        this.saves = new GuiSaveSlotButton[7];
    }
    
    private int getHeightDifference() {
        return this.getContentHeight() - (this.bottom - 30 + 2);
    }
    
    private int getContentHeight() {
        return this.Y_GAP * this.nodes.size();
    }
    
    public GuiEditNBT getWindow() {
        return this.window;
    }
    
    public void initGUI(final int width, final int height, final int bottom) {
        this.width = width;
        this.height = height;
        this.bottom = bottom;
        this.yClick = -1;
        this.initGUI(false);
        if (this.window != null) {
            this.window.initGUI((width - 178) / 2, (height - 93) / 2);
        }
    }
    
    public void updateScreen() {
        if (this.window != null) {
            this.window.update();
        }
        if (this.focusedSlotIndex != -1) {
            this.saves[this.focusedSlotIndex].update();
        }
    }
    
    private void setFocused(final Node<NamedNBT> toFocus) {
        if (toFocus == null) {
            GuiNBTButton[] buttons;
            for (int length = (buttons = this.buttons).length, i = 0; i < length; ++i) {
                final GuiNBTButton b = buttons[i];
                b.setEnabled(false);
            }
        }
        else if (toFocus.getObject().getNBT() instanceof NBTTagCompound) {
            GuiNBTButton[] buttons2;
            for (int length2 = (buttons2 = this.buttons).length, j = 0; j < length2; ++j) {
                final GuiNBTButton b = buttons2[j];
                b.setEnabled(true);
            }
            this.buttons[12].setEnabled(toFocus != this.tree.getRoot());
            this.buttons[11].setEnabled(toFocus.hasParent() && !(toFocus.getParent().getObject().getNBT() instanceof NBTTagList));
            this.buttons[13].setEnabled(true);
            this.buttons[14].setEnabled(toFocus != this.tree.getRoot());
            this.buttons[15].setEnabled(NBTEdit.clipboard != null);
        }
        else if (toFocus.getObject().getNBT() instanceof NBTTagList) {
            if (toFocus.hasChildren()) {
                final byte type = toFocus.getChildren().get(0).getObject().getNBT().getId();
                GuiNBTButton[] buttons3;
                for (int length3 = (buttons3 = this.buttons).length, k = 0; k < length3; ++k) {
                    final GuiNBTButton b2 = buttons3[k];
                    b2.setEnabled(false);
                }
                this.buttons[type - 1].setEnabled(true);
                this.buttons[12].setEnabled(true);
                this.buttons[11].setEnabled(!(toFocus.getParent().getObject().getNBT() instanceof NBTTagList));
                this.buttons[13].setEnabled(true);
                this.buttons[14].setEnabled(true);
                this.buttons[15].setEnabled(NBTEdit.clipboard != null && NBTEdit.clipboard.getNBT().getId() == type);
            }
            else {
                GuiNBTButton[] buttons4;
                for (int length4 = (buttons4 = this.buttons).length, l = 0; l < length4; ++l) {
                    final GuiNBTButton b = buttons4[l];
                    b.setEnabled(true);
                }
            }
            this.buttons[11].setEnabled(!(toFocus.getParent().getObject().getNBT() instanceof NBTTagList));
            this.buttons[13].setEnabled(true);
            this.buttons[14].setEnabled(true);
            this.buttons[15].setEnabled(NBTEdit.clipboard != null);
        }
        else {
            GuiNBTButton[] buttons5;
            for (int length5 = (buttons5 = this.buttons).length, n = 0; n < length5; ++n) {
                final GuiNBTButton b = buttons5[n];
                b.setEnabled(false);
            }
            this.buttons[12].setEnabled(true);
            this.buttons[11].setEnabled(true);
            this.buttons[13].setEnabled(true);
            this.buttons[14].setEnabled(true);
            this.buttons[15].setEnabled(false);
        }
        this.focused = toFocus;
        if (this.focused != null && this.focusedSlotIndex != -1) {
            this.stopEditingSlot();
        }
    }
    
    public void initGUI() {
        this.initGUI(false);
    }
    
    public void initGUI(final boolean shiftToFocused) {
        this.y = 30;
        this.nodes.clear();
        this.addNodes(this.tree.getRoot(), 10);
        this.addButtons();
        if (this.focused != null && !this.checkValidFocus(this.focused)) {
            this.setFocused(null);
        }
        if (this.focusedSlotIndex != -1) {
            this.saves[this.focusedSlotIndex].startEditing();
        }
        this.heightDiff = this.getHeightDifference();
        if (this.heightDiff <= 0) {
            this.offset = 0;
        }
        else {
            if (this.offset < -this.heightDiff) {
                this.offset = -this.heightDiff;
            }
            if (this.offset > 0) {
                this.offset = 0;
            }
            for (final GuiNBTNode node : this.nodes) {
                node.shift(this.offset);
            }
            if (shiftToFocused && this.focused != null) {
                this.shiftTo(this.focused);
            }
        }
    }
    
    private void addSaveSlotButtons() {
        final SaveStates saveStates = NBTEdit.getSaveStates();
        for (int i = 0; i < 7; ++i) {
            this.saves[i] = new GuiSaveSlotButton(saveStates.getSaveState(i), this.width - 24, 31 + i * 25);
        }
    }
    
    private void addButtons() {
        int x = 18;
        int y = 4;
        for (byte i = 14; i < 17; ++i) {
            this.buttons[i - 1] = new GuiNBTButton(i, x, y);
            x += 15;
        }
        x += 30;
        for (byte i = 12; i < 14; ++i) {
            this.buttons[i - 1] = new GuiNBTButton(i, x, y);
            x += 15;
        }
        x = 18;
        y = 17;
        for (byte i = 1; i < 12; ++i) {
            this.buttons[i - 1] = new GuiNBTButton(i, x, y);
            x += 9;
        }
    }
    
    private boolean checkValidFocus(final Node<NamedNBT> fc) {
        for (final GuiNBTNode node : this.nodes) {
            if (node.getNode() == fc) {
                this.setFocused(fc);
                return true;
            }
        }
        return fc.hasParent() && this.checkValidFocus(fc.getParent());
    }
    
    private void addNodes(final Node<NamedNBT> node, int x) {
        this.nodes.add(new GuiNBTNode(this, node, x, this.y));
        x += 10;
        this.y += this.Y_GAP;
        if (node.shouldDrawChildren()) {
            for (final Node<NamedNBT> child : node.getChildren()) {
                this.addNodes(child, x);
            }
        }
    }
    
    public void draw(final int mx, final int my) {
        int cmx = mx;
        int cmy = my;
        if (this.window != null) {
            cmx = -1;
            cmy = -1;
        }
        for (final GuiNBTNode node : this.nodes) {
            if (node.shouldDraw(29, this.bottom)) {
                node.draw(cmx, cmy);
            }
        }
        this.overlayBackground(0, 29, 255, 255);
        this.overlayBackground(this.bottom, this.height, 255, 255);
        GuiNBTButton[] buttons;
        for (int length = (buttons = this.buttons).length, i = 0; i < length; ++i) {
            final GuiNBTButton but = buttons[i];
            but.draw(cmx, cmy);
        }
        this.drawScrollBar(cmx, cmy);
        if (this.window != null) {
            this.window.draw(mx, my);
        }
    }
    
    private void drawScrollBar(final int mx, final int my) {
        if (this.heightDiff > 0) {
            if (Mouse.isButtonDown(0)) {
                if (this.yClick == -1) {
                    if (mx >= this.width - 20 && mx < this.width && my >= 29 && my < this.bottom) {
                        this.yClick = my;
                    }
                }
                else {
                    float scrollMultiplier = 1.0f;
                    int height = this.getHeightDifference();
                    if (height < 1) {
                        height = 1;
                    }
                    int length = (this.bottom - 29) * (this.bottom - 29) / this.getContentHeight();
                    if (length < 32) {
                        length = 32;
                    }
                    if (length > this.bottom - 29 - 8) {
                        length = this.bottom - 29 - 8;
                    }
                    scrollMultiplier /= (this.bottom - 29 - length) / (float)height;
                    this.shift((int)((this.yClick - my) * scrollMultiplier));
                    this.yClick = my;
                }
            }
            else {
                this.yClick = -1;
            }
            Gui.drawRect(this.width - 20, 29, this.width, this.bottom, Integer.MIN_VALUE);
            int length2 = (this.bottom - 29) * (this.bottom - 29) / this.getContentHeight();
            if (length2 < 32) {
                length2 = 32;
            }
            if (length2 > this.bottom - 29 - 8) {
                length2 = this.bottom - 29 - 8;
            }
            int y = -this.offset * (this.bottom - 29 - length2) / this.heightDiff + 29;
            if (y < 29) {
                y = 29;
            }
            this.drawGradientRect(this.width - 20, y, this.width, y + length2, -2130706433, -2144128205);
        }
    }
    
    protected void overlayBackground(final int par1, final int par2, final int par3, final int par4) {
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder worldRenderer = tessellator.getBuffer();
        this.mc.renderEngine.bindTexture(GuiNBTTree.OPTIONS_BACKGROUND);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        final float var6 = 32.0f;
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        final Color color = new Color(4210752);
        worldRenderer.color(color.getRed(), color.getGreen(), color.getBlue(), par4);
        worldRenderer.pos(0.0, par2, 0.0).tex(0.0, par2 / var6);
        worldRenderer.pos(this.width, par2, 0.0).tex(this.width / var6, par2 / var6);
        worldRenderer.color(color.getRed(), color.getGreen(), color.getBlue(), par3);
        worldRenderer.pos(this.width, par1, 0.0).tex(this.width / var6, par1 / var6);
        worldRenderer.pos(0.0, par1, 0.0).tex(0.0, par1 / var6);
        tessellator.draw();
    }
    
    public void mouseClicked(final int mx, final int my) {
        if (this.window == null) {
            boolean reInit = false;
            for (final GuiNBTNode node : this.nodes) {
                if (node.hideShowClicked(mx, my)) {
                    reInit = true;
                    if (node.shouldDrawChildren()) {
                        this.offset += 31 - node.y;
                        break;
                    }
                    break;
                }
            }
            if (!reInit) {
                GuiNBTButton[] buttons;
                for (int length = (buttons = this.buttons).length, i = 0; i < length; ++i) {
                    final GuiNBTButton button = buttons[i];
                    if (button.inBounds(mx, my)) {
                        this.buttonClicked(button);
                        return;
                    }
                }
                if (my >= 30 && mx <= this.width - 175) {
                    Node<NamedNBT> newFocus = null;
                    for (final GuiNBTNode node2 : this.nodes) {
                        if (node2.clicked(mx, my)) {
                            newFocus = node2.getNode();
                            break;
                        }
                    }
                    if (this.focusedSlotIndex != -1) {
                        this.stopEditingSlot();
                    }
                    this.setFocused(newFocus);
                }
            }
            else {
                this.initGUI();
            }
        }
        else {
            this.window.click(mx, my);
        }
    }
    
    private void saveButtonClicked(final GuiSaveSlotButton button) {
        if (button.save.tag.hasNoTags()) {
            final Node<NamedNBT> obj = (this.focused == null) ? this.tree.getRoot() : this.focused;
            final NBTBase base = obj.getObject().getNBT();
            final String name = obj.getObject().getName();
            if (base instanceof NBTTagList) {
                final NBTTagList list = new NBTTagList();
                this.tree.addChildrenToList(obj, list);
                button.save.tag.setTag(name, list);
            }
            else if (base instanceof NBTTagCompound) {
                final NBTTagCompound compound = new NBTTagCompound();
                this.tree.addChildrenToTag(obj, compound);
                button.save.tag.setTag(name, compound);
            }
            else {
                button.save.tag.setTag(name, base.copy());
            }
            button.saved();
            NBTEdit.getSaveStates().save();
            this.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
        }
        else {
            final Map<String, NBTBase> nbtMap = NBTHelper.getMap(button.save.tag);
            if (nbtMap.isEmpty()) {
                NBTEdit.log(Level.WARN, "Unable to copy from save \"" + button.save.name + "\".");
                NBTEdit.log(Level.WARN, "The save is invalid - a valid save must only contain 1 core NBTBase");
            }
            else {
                if (this.focused == null) {
                    this.setFocused(this.tree.getRoot());
                }
                final Map.Entry<String, NBTBase> firstEntry = nbtMap.entrySet().iterator().next();
                assert firstEntry != null;
                final String name = firstEntry.getKey();
                final NBTBase nbt = firstEntry.getValue().copy();
                if (this.focused == this.tree.getRoot() && nbt instanceof NBTTagCompound && name.equals("ROOT")) {
                    this.setFocused(null);
                    this.tree = new NBTTree((NBTTagCompound)nbt);
                    this.initGUI();
                    this.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
                }
                else if (this.canAddToParent(this.focused.getObject().getNBT(), nbt)) {
                    this.focused.setDrawChildren(true);
                    final Iterator<Node<NamedNBT>> it = this.focused.getChildren().iterator();
                    while (it.hasNext()) {
                        if (it.next().getObject().getName().equals(name)) {
                            it.remove();
                            break;
                        }
                    }
                    final Node<NamedNBT> node = this.insert(new NamedNBT(name, nbt));
                    this.tree.addChildrenToTree(node);
                    this.tree.sort(node);
                    this.setFocused(node);
                    this.initGUI(true);
                    this.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
                }
            }
        }
    }
    
    private void buttonClicked(final GuiNBTButton button) {
        if (button.getId() == 16) {
            this.paste();
        }
        else if (button.getId() == 15) {
            this.cut();
        }
        else if (button.getId() == 14) {
            this.copy();
        }
        else if (button.getId() == 13) {
            this.deleteSelected();
        }
        else if (button.getId() == 12) {
            this.edit();
        }
        else if (this.focused != null) {
            this.focused.setDrawChildren(true);
            final List<Node<NamedNBT>> children = this.focused.getChildren();
            final String type = NBTStringHelper.getButtonName(button.getId());
            if (this.focused.getObject().getNBT() instanceof NBTTagList) {
                final NBTBase nbt = NBTStringHelper.newTag(button.getId());
                if (nbt != null) {
                    final Node<NamedNBT> newNode = new Node<NamedNBT>(this.focused, new NamedNBT("", nbt));
                    children.add(newNode);
                    this.setFocused(newNode);
                }
            }
            else if (children.size() == 0) {
                this.setFocused(this.insert(String.valueOf(type) + "1", button.getId()));
            }
            else {
                for (int i = 1; i <= children.size() + 1; ++i) {
                    final String name = String.valueOf(type) + i;
                    if (this.validName(name, children)) {
                        this.setFocused(this.insert(name, button.getId()));
                        break;
                    }
                }
            }
            this.initGUI(true);
        }
    }
    
    private boolean validName(final String name, final List<Node<NamedNBT>> list) {
        for (final Node<NamedNBT> node : list) {
            if (node.getObject().getName().equals(name)) {
                return false;
            }
        }
        return true;
    }
    
    private Node<NamedNBT> insert(final NamedNBT nbt) {
        final Node<NamedNBT> newNode = new Node<NamedNBT>(this.focused, nbt);
        if (this.focused.hasChildren()) {
            final List<Node<NamedNBT>> children = this.focused.getChildren();
            boolean added = false;
            for (int i = 0; i < children.size(); ++i) {
                if (NBTEdit.SORTER.compare(newNode, (Node<NamedNBT>)children.get(i)) < 0) {
                    children.add(i, newNode);
                    added = true;
                    break;
                }
            }
            if (!added) {
                children.add(newNode);
            }
        }
        else {
            this.focused.addChild(newNode);
        }
        return newNode;
    }
    
    private Node<NamedNBT> insert(final String name, final byte type) {
        final NBTBase nbt = NBTStringHelper.newTag(type);
        if (nbt != null) {
            return this.insert(new NamedNBT(name, nbt));
        }
        return null;
    }
    
    public void deleteSelected() {
        if (this.focused != null && this.tree.delete(this.focused)) {
            final Node<NamedNBT> oldFocused = this.focused;
            this.shiftFocus(true);
            if (this.focused == oldFocused) {
                this.setFocused(null);
            }
            this.initGUI();
        }
    }
    
    public void editSelected() {
        if (this.focused != null) {
            final NBTBase base = this.focused.getObject().getNBT();
            if (this.focused.hasChildren() && (base instanceof NBTTagCompound || base instanceof NBTTagList)) {
                this.focused.setDrawChildren(!this.focused.shouldDrawChildren());
                final int index;
                if (this.focused.shouldDrawChildren() && (index = this.indexOf(this.focused)) != -1) {
                    this.offset += 31 - this.nodes.get(index).y;
                }
                this.initGUI();
            }
            else if (this.buttons[11].isEnabled()) {
                this.edit();
            }
        }
        else if (this.focusedSlotIndex != -1) {
            this.stopEditingSlot();
        }
    }
    
    private boolean canAddToParent(final NBTBase parent, final NBTBase child) {
        if (parent instanceof NBTTagCompound) {
            return true;
        }
        if (parent instanceof NBTTagList) {
            final NBTTagList list = (NBTTagList)parent;
            return list.tagCount() == 0 || list.getTagType() == child.getId();
        }
        return false;
    }
    
    private boolean canPaste() {
        return NBTEdit.clipboard != null && this.focused != null && this.canAddToParent(this.focused.getObject().getNBT(), NBTEdit.clipboard.getNBT());
    }
    
    private void paste() {
        if (NBTEdit.clipboard != null) {
            this.focused.setDrawChildren(true);
            final NamedNBT namedNBT = NBTEdit.clipboard.copy();
            if (this.focused.getObject().getNBT() instanceof NBTTagList) {
                namedNBT.setName("");
                final Node<NamedNBT> node = new Node<NamedNBT>(this.focused, namedNBT);
                this.focused.addChild(node);
                this.tree.addChildrenToTree(node);
                this.tree.sort(node);
                this.setFocused(node);
            }
            else {
                final String name = namedNBT.getName();
                final List<Node<NamedNBT>> children = this.focused.getChildren();
                if (!this.validName(name, children)) {
                    for (int i = 1; i <= children.size() + 1; ++i) {
                        final String n = String.valueOf(name) + "(" + i + ")";
                        if (this.validName(n, children)) {
                            namedNBT.setName(n);
                            break;
                        }
                    }
                }
                final Node<NamedNBT> node2 = this.insert(namedNBT);
                this.tree.addChildrenToTree(node2);
                this.tree.sort(node2);
                this.setFocused(node2);
            }
            this.initGUI(true);
        }
    }
    
    private void copy() {
        if (this.focused != null) {
            final NamedNBT namedNBT = this.focused.getObject();
            if (namedNBT.getNBT() instanceof NBTTagList) {
                final NBTTagList list = new NBTTagList();
                this.tree.addChildrenToList(this.focused, list);
                NBTEdit.clipboard = new NamedNBT(namedNBT.getName(), list);
            }
            else if (namedNBT.getNBT() instanceof NBTTagCompound) {
                final NBTTagCompound compound = new NBTTagCompound();
                this.tree.addChildrenToTag(this.focused, compound);
                NBTEdit.clipboard = new NamedNBT(namedNBT.getName(), compound);
            }
            else {
                NBTEdit.clipboard = this.focused.getObject().copy();
            }
            this.setFocused(this.focused);
        }
    }
    
    private void cut() {
        this.copy();
        this.deleteSelected();
    }
    
    private void edit() {
        final NBTBase base = this.focused.getObject().getNBT();
        final NBTBase parent = this.focused.getParent().getObject().getNBT();
        (this.window = new GuiEditNBT(this, this.focused, !(parent instanceof NBTTagList), !(base instanceof NBTTagCompound) && !(base instanceof NBTTagList))).initGUI((this.width - 178) / 2, (this.height - 93) / 2);
    }
    
    public void nodeEdited(final Node<NamedNBT> node) {
        final Node<NamedNBT> parent = node.getParent();
        Collections.sort(parent.getChildren(), NBTEdit.SORTER);
        this.initGUI(true);
    }
    
    public void arrowKeyPressed(final boolean up) {
        if (this.focused == null) {
            this.shift(up ? this.Y_GAP : (-this.Y_GAP));
        }
        else {
            this.shiftFocus(up);
        }
    }
    
    private int indexOf(final Node<NamedNBT> node) {
        for (int i = 0; i < this.nodes.size(); ++i) {
            if (this.nodes.get(i).getNode() == node) {
                return i;
            }
        }
        return -1;
    }
    
    private void shiftFocus(final boolean up) {
        int index = this.indexOf(this.focused);
        if (index != -1) {
            index += (up ? -1 : 1);
            if (index >= 0 && index < this.nodes.size()) {
                this.setFocused(this.nodes.get(index).getNode());
                this.shift(up ? this.Y_GAP : (-this.Y_GAP));
            }
        }
    }
    
    private void shiftTo(final Node<NamedNBT> node) {
        final int index = this.indexOf(node);
        if (index != -1) {
            final GuiNBTNode gui = this.nodes.get(index);
            this.shift((this.bottom + 30 + 1) / 2 - (gui.y + gui.height));
        }
    }
    
    public void shift(final int i) {
        if (this.heightDiff <= 0 || this.window != null) {
            return;
        }
        int dif = this.offset + i;
        if (dif > 0) {
            dif = 0;
        }
        if (dif < -this.heightDiff) {
            dif = -this.heightDiff;
        }
        for (final GuiNBTNode node : this.nodes) {
            node.shift(dif - this.offset);
        }
        this.offset = dif;
    }
    
    public void closeWindow() {
        this.window = null;
    }
    
    public boolean isEditingSlot() {
        return this.focusedSlotIndex != -1;
    }
    
    public void stopEditingSlot() {
        this.saves[this.focusedSlotIndex].stopEditing();
        NBTEdit.getSaveStates().save();
        this.focusedSlotIndex = -1;
    }
    
    public void keyTyped(final char ch, final int key) {
        if (this.focusedSlotIndex != -1) {
            this.saves[this.focusedSlotIndex].keyTyped(ch, key);
        }
        else {
            if (key == 46 && GuiScreen.isCtrlKeyDown()) {
                this.copy();
            }
            if (key == 47 && GuiScreen.isCtrlKeyDown() && this.canPaste()) {
                this.paste();
            }
            if (key == 45 && GuiScreen.isCtrlKeyDown()) {
                this.cut();
            }
        }
    }
    
    public void rightClick(final int mx, final int my) {
        for (int i = 0; i < 7; ++i) {
            if (this.saves[i].inBounds(mx, my)) {
                this.setFocused(null);
                if (this.focusedSlotIndex != -1) {
                    if (this.focusedSlotIndex == i) {
                        return;
                    }
                    this.saves[this.focusedSlotIndex].stopEditing();
                    NBTEdit.getSaveStates().save();
                }
                this.saves[i].startEditing();
                this.focusedSlotIndex = i;
                break;
            }
        }
    }
    
    private void putColor(final BufferBuilder renderer, final int argb, final int p_178988_2_) {
        final int i = renderer.getColorIndex(p_178988_2_);
        final int j = argb >> 16 & 0xFF;
        final int k = argb >> 8 & 0xFF;
        final int l = argb & 0xFF;
        final int i2 = argb >> 24 & 0xFF;
        renderer.putColorRGBA(i, j, k, l, i2);
    }
}

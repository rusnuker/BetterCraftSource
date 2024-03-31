// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.note;

import java.util.Iterator;
import org.lwjgl.input.Keyboard;
import java.io.IOException;
import java.nio.file.StandardOpenOption;
import java.nio.file.OpenOption;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.text.SimpleDateFormat;
import java.security.SecureRandom;
import java.nio.file.Files;
import java.io.File;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiTextField;
import java.util.List;
import net.minecraft.client.gui.GuiScreen;

public class GuiNote extends GuiScreen
{
    public GuiScreen before;
    private boolean allSelected;
    private List<GuiTextField> textFieldList;
    
    public GuiNote(final GuiScreen screen) {
        this.textFieldList = (List<GuiTextField>)Lists.newCopyOnWriteArrayList();
        this.before = screen;
    }
    
    @Override
    public void initGui() {
        try {
            final File file = new File("test.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            final List<String> lines = Files.readAllLines(file.toPath());
            for (int index = 0; index < lines.size(); ++index) {
                final String line = lines.get(index);
                final GuiTextField textField = new GuiTextField(new SecureRandom().nextInt(), this.fontRendererObj, 0, index * 10, GuiNote.width, 10);
                textField.setEnableBackgroundDrawing(false);
                textField.setText(String.valueOf(line) + new SimpleDateFormat("dd-MM-yyyy - HH:mm:ss.SSS").format(System.currentTimeMillis()));
                this.textFieldList.add(textField);
            }
        }
        catch (final Throwable throwable) {
            throwable.printStackTrace();
        }
    }
    
    @Override
    public void onGuiClosed() {
        try {
            final File file = new File("test.txt");
            final List<String> lines = this.textFieldList.stream().map(object -> object.getText()).collect((Collector<? super Object, ?, List<String>>)Collectors.toList());
            Files.write(file.toPath(), lines, StandardOpenOption.TRUNCATE_EXISTING);
        }
        catch (final Throwable throwable) {
            throwable.printStackTrace();
        }
        super.onGuiClosed();
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        this.textFieldList.forEach(textField -> textField.mouseClicked(mouseX2, mouseY2, mouseButton2));
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        for (final GuiTextField textField : this.textFieldList) {
            if (textField.isFocused() && keyCode == 28) {
                final int index = this.textFieldList.indexOf(textField);
                if (index + 1 != this.textFieldList.size()) {
                    textField.setFocused(false);
                    this.textFieldList.get(index + 1).setFocused(true);
                    return;
                }
                final GuiTextField newTextField = new GuiTextField(new SecureRandom().nextInt(), this.fontRendererObj, 0, this.textFieldList.size() * 10, GuiNote.width, 10);
                newTextField.setEnableBackgroundDrawing(false);
                textField.setFocused(false);
                newTextField.setFocused(true);
                this.textFieldList.add(newTextField);
                return;
            }
            else {
                if (textField.isFocused() && textField.getText().isEmpty() && keyCode == 14) {
                    if (this.textFieldList.indexOf(textField) != 0) {
                        this.textFieldList.get(this.textFieldList.indexOf(textField) - 1).setFocused(true);
                    }
                    this.textFieldList.remove(textField);
                    this.resortLines();
                    return;
                }
                if (textField.isFocused() && keyCode == 200 && this.textFieldList.indexOf(textField) != 0) {
                    textField.setFocused(false);
                    this.textFieldList.get(this.textFieldList.indexOf(textField) - 1).setFocused(true);
                    return;
                }
                if (textField.isFocused() && keyCode == 208 && this.textFieldList.indexOf(textField) + 1 != this.textFieldList.size()) {
                    textField.setFocused(false);
                    this.textFieldList.get(this.textFieldList.indexOf(textField) + 1).setFocused(true);
                    return;
                }
                if (keyCode == 30 && Keyboard.isKeyDown(29)) {
                    this.textFieldList.forEach(textFieldObj -> textFieldObj.setFocused(true));
                    this.allSelected = true;
                    return;
                }
                if (this.allSelected) {
                    this.textFieldList.forEach(textFieldObj -> {
                        if (textFieldObj.getText().isEmpty()) {
                            this.textFieldList.remove(textFieldObj);
                        }
                        else {
                            textFieldObj.setFocused(false);
                            textFieldObj.setText("");
                        }
                        return;
                    });
                    this.textFieldList.get(0).setFocused(true);
                    this.textFieldList.get(0).setText(String.valueOf(typedChar));
                    this.allSelected = false;
                    this.resortLines();
                    return;
                }
                textField.textboxKeyTyped(typedChar, keyCode);
            }
        }
        super.keyTyped(typedChar, keyCode);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        this.textFieldList.forEach(textField -> {
            textField.updateCursorCounter();
            textField.drawTextBox();
            return;
        });
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    public void resortLines() {
        for (int index = 0; index < this.textFieldList.size(); ++index) {
            final GuiTextField textField = this.textFieldList.get(index);
            textField.yPosition = index * 10;
        }
    }
}

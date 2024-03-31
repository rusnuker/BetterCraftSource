// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.misc.altmanager;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.client.gui.Gui;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import java.util.function.Consumer;
import com.google.gson.JsonObject;
import net.minecraft.util.Session;
import net.minecraft.client.Minecraft;
import java.util.UUID;
import org.apache.http.util.EntityUtils;
import com.google.gson.JsonParser;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiScreen;

public class GuiSessionLogin extends GuiScreen
{
    private GuiTextField tokenField;
    private GuiScreen parent;
    
    public GuiSessionLogin(final GuiScreen parent) {
        this.parent = parent;
    }
    
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, GuiSessionLogin.width / 2 - 100, GuiSessionLogin.height / 4 + 92 + 12, "Login"));
        this.buttonList.add(new GuiButton(1, GuiSessionLogin.width / 2 - 100, GuiSessionLogin.height / 4 + 116 + 12, "Back"));
        (this.tokenField = new GuiTextField(0, this.fontRendererObj, GuiSessionLogin.width / 2 - 100, 70, 200, 20)).setMaxStringLength(Integer.MAX_VALUE);
        this.tokenField.setFocused(true);
    }
    
    public void keyTyped(final char character, final int keyCode) throws IOException {
        this.tokenField.textboxKeyTyped(character, keyCode);
        if (keyCode == 15) {
            this.tokenField.setFocused(!this.tokenField.isFocused());
        }
        if (keyCode == 28) {
            this.actionPerformed(this.buttonList.get(0));
        }
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.tokenField.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) {
        switch (button.id) {
            case 0: {
                if (!this.tokenField.getText().isEmpty()) {
                    final String content = this.tokenField.getText();
                    final String token = (content.split(":").length > 0) ? content.split(":")[content.startsWith("token:")] : content;
                    try {
                        this.sendGetRequest("https://api.minecraftservices.com/minecraft/profile", request -> request.addHeader("Authorization", String.format("Bearer %s", s)), response -> {
                            try {
                                final JsonObject json = new JsonParser().parse(EntityUtils.toString(response.getEntity())).getAsJsonObject();
                                final UUID uuid = UUID.fromString(json.get("id").getAsString().replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
                                final String name = json.get("name").getAsString();
                                Minecraft.getMinecraft();
                                Minecraft.setSession(new Session(name, uuid.toString(), tokenIn, "mojang"));
                            }
                            catch (final IOException exception2) {
                                throw new RuntimeException(exception2);
                            }
                            return;
                        });
                    }
                    catch (final IOException exception) {
                        exception.printStackTrace();
                    }
                }
                this.mc.displayGuiScreen(this.parent);
                break;
            }
            case 1: {
                this.mc.displayGuiScreen(this.parent);
                break;
            }
        }
    }
    
    private void sendGetRequest(final String url, final Consumer<HttpGet> request, final Consumer<HttpResponse> response) throws IOException {
        final HttpGet httpGet = new HttpGet(url);
        request.accept(httpGet);
        final CloseableHttpClient httpClient = HttpClients.createDefault();
        final CloseableHttpResponse httpResponse = httpClient.execute((HttpUriRequest)httpGet);
        response.accept(httpResponse);
        httpResponse.close();
        httpClient.close();
    }
    
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    public void updateScreen() {
        this.tokenField.updateCursorCounter();
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        Gui.drawCenteredString(this.mc.fontRendererObj, "Session Login", GuiSessionLogin.width / 2, 20, -1);
        this.mc.fontRendererObj.drawString(String.valueOf(EnumChatFormatting.GRAY.toString()) + "Token", GuiSessionLogin.width / 2 - 100, 57, -1);
        this.tokenField.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}

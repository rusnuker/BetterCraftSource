// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.commands.impl;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.init.Items;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentText;
import me.nzxtercode.bettercraft.client.commands.CommandManager;
import me.nzxtercode.bettercraft.client.BetterCraft;
import net.minecraft.client.Minecraft;
import me.nzxtercode.bettercraft.client.commands.Command;

public class GiveCommand extends Command
{
    public GiveCommand() {
        super("give", new String[0]);
    }
    
    @Override
    public void run(final String alias, final String[] args) {
        if (Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode) {
            if (args.length == 0) {
                final GuiNewChat chatGUI = Minecraft.getMinecraft().ingameGUI.getChatGUI();
                BetterCraft.getInstance();
                chatGUI.printChatMessage(new ChatComponentText(String.valueOf(BetterCraft.clientPrefix) + "§7" + CommandManager.getInstance().prefix + "give item <name/id>"));
                final GuiNewChat chatGUI2 = Minecraft.getMinecraft().ingameGUI.getChatGUI();
                BetterCraft.getInstance();
                chatGUI2.printChatMessage(new ChatComponentText(String.valueOf(BetterCraft.clientPrefix) + "§7" + CommandManager.getInstance().prefix + "give skull <player>"));
                final GuiNewChat chatGUI3 = Minecraft.getMinecraft().ingameGUI.getChatGUI();
                BetterCraft.getInstance();
                chatGUI3.printChatMessage(new ChatComponentText(String.valueOf(BetterCraft.clientPrefix) + "§7" + CommandManager.getInstance().prefix + "give cmdblock"));
                final GuiNewChat chatGUI4 = Minecraft.getMinecraft().ingameGUI.getChatGUI();
                BetterCraft.getInstance();
                chatGUI4.printChatMessage(new ChatComponentText(String.valueOf(BetterCraft.clientPrefix) + "§7" + CommandManager.getInstance().prefix + "give dragon_egg"));
                final GuiNewChat chatGUI5 = Minecraft.getMinecraft().ingameGUI.getChatGUI();
                BetterCraft.getInstance();
                chatGUI5.printChatMessage(new ChatComponentText(String.valueOf(BetterCraft.clientPrefix) + "§7" + CommandManager.getInstance().prefix + "give mob_spawner"));
                final GuiNewChat chatGUI6 = Minecraft.getMinecraft().ingameGUI.getChatGUI();
                BetterCraft.getInstance();
                chatGUI6.printChatMessage(new ChatComponentText(String.valueOf(BetterCraft.clientPrefix) + "§7" + CommandManager.getInstance().prefix + "give opsword"));
            }
            else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("opsword")) {
                    final Item item = Item.getItemById(276);
                    final ItemStack itemStack = new ItemStack(item, 1);
                    itemStack.setStackDisplayName("OP Sword");
                    itemStack.setItemDamage(30000);
                    itemStack.addEnchantment(Enchantment.sharpness, 30000);
                    itemStack.addEnchantment(Enchantment.fireAspect, 30000);
                    itemStack.addEnchantment(Enchantment.unbreaking, 30000);
                    itemStack.addEnchantment(Enchantment.lure, 30000);
                    itemStack.addEnchantment(Enchantment.looting, 30000);
                    Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C10PacketCreativeInventoryAction(5, itemStack));
                    final GuiNewChat chatGUI7 = Minecraft.getMinecraft().ingameGUI.getChatGUI();
                    BetterCraft.getInstance();
                    chatGUI7.printChatMessage(new ChatComponentText(String.valueOf(BetterCraft.clientPrefix) + "You got a §f" + item.getUnlocalizedName().replace("item.", "") + " §7in head slot"));
                }
                else if (args[0].equalsIgnoreCase("cmdblock") || args[0].equalsIgnoreCase("137")) {
                    final Block item2 = Blocks.command_block;
                    final ItemStack itemStack = new ItemStack(item2, 1);
                    Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C10PacketCreativeInventoryAction(5, itemStack));
                    final GuiNewChat chatGUI8 = Minecraft.getMinecraft().ingameGUI.getChatGUI();
                    BetterCraft.getInstance();
                    chatGUI8.printChatMessage(new ChatComponentText(String.valueOf(BetterCraft.clientPrefix) + "You got a §f" + item2.getUnlocalizedName().replace("tile.", "") + " §7in head slot"));
                }
                else if (args[0].equalsIgnoreCase("dragon_egg") || args[0].equalsIgnoreCase("122")) {
                    final Block item2 = Blocks.dragon_egg;
                    final ItemStack itemStack = new ItemStack(item2, 1);
                    Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C10PacketCreativeInventoryAction(5, itemStack));
                    final GuiNewChat chatGUI9 = Minecraft.getMinecraft().ingameGUI.getChatGUI();
                    BetterCraft.getInstance();
                    chatGUI9.printChatMessage(new ChatComponentText(String.valueOf(BetterCraft.clientPrefix) + "You got a §f" + item2.getUnlocalizedName().replace("tile.", "") + " §7in head slot"));
                }
                else if (args[0].equalsIgnoreCase("mob_spawner") || args[0].equalsIgnoreCase("spawner") || args[0].equalsIgnoreCase("52")) {
                    final Block item2 = Blocks.mob_spawner;
                    final ItemStack itemStack = new ItemStack(item2, 1);
                    Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C10PacketCreativeInventoryAction(4, itemStack));
                    final GuiNewChat chatGUI10 = Minecraft.getMinecraft().ingameGUI.getChatGUI();
                    BetterCraft.getInstance();
                    chatGUI10.printChatMessage(new ChatComponentText(String.valueOf(BetterCraft.clientPrefix) + "You got a §f" + item2.getUnlocalizedName().replace("tile.", "") + " §7in head slot"));
                }
            }
            else if (args.length >= 1) {
                if (args[0].equalsIgnoreCase("item")) {
                    final Item item = Item.getByNameOrId(args[1]);
                    final ItemStack itemStack = new ItemStack(item, 1);
                    Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C10PacketCreativeInventoryAction(36, itemStack));
                    try {
                        final GuiNewChat chatGUI11 = Minecraft.getMinecraft().ingameGUI.getChatGUI();
                        BetterCraft.getInstance();
                        chatGUI11.printChatMessage(new ChatComponentText(String.valueOf(BetterCraft.clientPrefix) + "You got a §f" + item.getUnlocalizedName().replace("tile.", "").replace("item.", "") + " §7in head slot"));
                    }
                    catch (final Exception e) {
                        final GuiNewChat chatGUI12 = Minecraft.getMinecraft().ingameGUI.getChatGUI();
                        BetterCraft.getInstance();
                        chatGUI12.printChatMessage(new ChatComponentText(String.valueOf(BetterCraft.clientPrefix) + "Item not exist"));
                    }
                }
                else if (args[0].equalsIgnoreCase("skull")) {
                    final Item item = Items.skull;
                    final ItemStack itemStack = new ItemStack(item, 1, 3);
                    final NBTTagCompound entityTag = new NBTTagCompound();
                    entityTag.setString("SkullOwner", args[1]);
                    itemStack.setTagCompound(entityTag);
                    Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C10PacketCreativeInventoryAction(36, itemStack));
                    final GuiNewChat chatGUI13 = Minecraft.getMinecraft().ingameGUI.getChatGUI();
                    BetterCraft.getInstance();
                    chatGUI13.printChatMessage(new ChatComponentText(String.valueOf(BetterCraft.clientPrefix) + "You got a §f" + item.getUnlocalizedName().replace("item.", "") + " §7in head slot"));
                }
            }
        }
    }
    
    @Override
    public List<String> autocomplete(final int arg, final String[] args) {
        return new ArrayList<String>();
    }
}

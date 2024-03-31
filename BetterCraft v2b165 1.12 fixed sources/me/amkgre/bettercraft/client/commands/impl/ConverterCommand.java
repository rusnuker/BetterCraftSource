// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.commands.impl;

import me.amkgre.bettercraft.client.utils.ClipboardUtils;
import me.amkgre.bettercraft.client.commands.CommandManager;
import me.amkgre.bettercraft.client.commands.Command;

public class ConverterCommand extends Command
{
    @Override
    public void execute(final String[] args) {
        if (args.length == 0) {
            Command.clientMSG("§m§8----------§r §5Converter §m§8----------", true);
            Command.clientMSG("§d" + CommandManager.syntax + "converter iptodecimal <IP>", true);
            Command.clientMSG("§d" + CommandManager.syntax + "converter decimaltoip <Int>", true);
            Command.clientMSG("§m§8----------§r §5Converter §m§8----------", true);
        }
        else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("iptodecimal")) {
                try {
                    long result = 0L;
                    final String[] atoms = args[1].split("\\.");
                    for (int i = 3; i >= 0; --i) {
                        result |= Long.parseLong(atoms[3 - i]) << i * 8;
                    }
                    Command.clientMSG("IP Adresse to Decimal: §d" + result, true);
                    ClipboardUtils.setClipboard(String.valueOf(result));
                }
                catch (final Exception ex) {}
            }
            else if (args[0].equalsIgnoreCase("decimaltoip")) {
                try {
                    long ip = Long.parseLong(args[1]);
                    final StringBuilder sb = new StringBuilder(15);
                    for (int i = 0; i < 4; ++i) {
                        sb.insert(0, Long.toString(ip & 0xFFL));
                        if (i < 3) {
                            sb.insert(0, '.');
                        }
                        ip >>= 8;
                    }
                    Command.clientMSG("Decimal to IP Adresse: §d" + sb.toString(), true);
                    ClipboardUtils.setClipboard(String.valueOf(sb.toString()));
                }
                catch (final Exception ex2) {}
            }
        }
    }
    
    @Override
    public String getName() {
        return "converter";
    }
}

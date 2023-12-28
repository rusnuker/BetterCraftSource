// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.smtp;

import java.util.HashMap;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.ByteBuf;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.AsciiString;
import java.util.Map;

public final class SmtpCommand
{
    public static final SmtpCommand EHLO;
    public static final SmtpCommand HELO;
    public static final SmtpCommand MAIL;
    public static final SmtpCommand RCPT;
    public static final SmtpCommand DATA;
    public static final SmtpCommand NOOP;
    public static final SmtpCommand RSET;
    public static final SmtpCommand EXPN;
    public static final SmtpCommand VRFY;
    public static final SmtpCommand HELP;
    public static final SmtpCommand QUIT;
    private static final CharSequence DATA_CMD;
    private static final Map<CharSequence, SmtpCommand> COMMANDS;
    private final AsciiString name;
    private final boolean contentExpected;
    private int hashCode;
    
    public static SmtpCommand valueOf(final CharSequence commandName) {
        final SmtpCommand command = SmtpCommand.COMMANDS.get(commandName);
        if (command != null) {
            return command;
        }
        return new SmtpCommand(AsciiString.of(ObjectUtil.checkNotNull(commandName, "commandName")), AsciiString.contentEqualsIgnoreCase(commandName, SmtpCommand.DATA_CMD));
    }
    
    private SmtpCommand(final AsciiString name, final boolean contentExpected) {
        this.name = name;
        this.contentExpected = contentExpected;
    }
    
    public AsciiString name() {
        return this.name;
    }
    
    void encode(final ByteBuf buffer) {
        ByteBufUtil.writeAscii(buffer, this.name());
    }
    
    boolean isContentExpected() {
        return this.contentExpected;
    }
    
    @Override
    public int hashCode() {
        if (this.hashCode != -1) {
            this.hashCode = AsciiString.hashCode(this.name);
        }
        return this.hashCode;
    }
    
    @Override
    public boolean equals(final Object obj) {
        return obj == this || (obj instanceof SmtpCommand && this.name.contentEqualsIgnoreCase(((SmtpCommand)obj).name()));
    }
    
    @Override
    public String toString() {
        return "SmtpCommand{name=" + (Object)this.name + ", contentExpected=" + this.contentExpected + ", hashCode=" + this.hashCode + '}';
    }
    
    static {
        EHLO = new SmtpCommand(new AsciiString("EHLO"), false);
        HELO = new SmtpCommand(new AsciiString("HELO"), false);
        MAIL = new SmtpCommand(new AsciiString("MAIL"), false);
        RCPT = new SmtpCommand(new AsciiString("RCPT"), false);
        DATA = new SmtpCommand(new AsciiString("DATA"), true);
        NOOP = new SmtpCommand(new AsciiString("NOOP"), false);
        RSET = new SmtpCommand(new AsciiString("RSET"), false);
        EXPN = new SmtpCommand(new AsciiString("EXPN"), false);
        VRFY = new SmtpCommand(new AsciiString("VRFY"), false);
        HELP = new SmtpCommand(new AsciiString("HELP"), false);
        QUIT = new SmtpCommand(new AsciiString("QUIT"), false);
        DATA_CMD = new AsciiString("DATA");
        (COMMANDS = new HashMap<CharSequence, SmtpCommand>()).put(SmtpCommand.EHLO.name(), SmtpCommand.EHLO);
        SmtpCommand.COMMANDS.put(SmtpCommand.HELO.name(), SmtpCommand.HELO);
        SmtpCommand.COMMANDS.put(SmtpCommand.MAIL.name(), SmtpCommand.MAIL);
        SmtpCommand.COMMANDS.put(SmtpCommand.RCPT.name(), SmtpCommand.RCPT);
        SmtpCommand.COMMANDS.put(SmtpCommand.DATA.name(), SmtpCommand.DATA);
        SmtpCommand.COMMANDS.put(SmtpCommand.NOOP.name(), SmtpCommand.NOOP);
        SmtpCommand.COMMANDS.put(SmtpCommand.RSET.name(), SmtpCommand.RSET);
        SmtpCommand.COMMANDS.put(SmtpCommand.EXPN.name(), SmtpCommand.EXPN);
        SmtpCommand.COMMANDS.put(SmtpCommand.VRFY.name(), SmtpCommand.VRFY);
        SmtpCommand.COMMANDS.put(SmtpCommand.HELP.name(), SmtpCommand.HELP);
        SmtpCommand.COMMANDS.put(SmtpCommand.QUIT.name(), SmtpCommand.QUIT);
    }
}

// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.smtp;

import io.netty.util.internal.ObjectUtil;
import java.util.List;
import java.util.ArrayList;
import io.netty.util.AsciiString;

public final class SmtpRequests
{
    private static final SmtpRequest DATA;
    private static final SmtpRequest NOOP;
    private static final SmtpRequest RSET;
    private static final SmtpRequest HELP_NO_ARG;
    private static final SmtpRequest QUIT;
    private static final AsciiString FROM_NULL_SENDER;
    
    public static SmtpRequest helo(final CharSequence hostname) {
        return new DefaultSmtpRequest(SmtpCommand.HELO, new CharSequence[] { hostname });
    }
    
    public static SmtpRequest ehlo(final CharSequence hostname) {
        return new DefaultSmtpRequest(SmtpCommand.EHLO, new CharSequence[] { hostname });
    }
    
    public static SmtpRequest noop() {
        return SmtpRequests.NOOP;
    }
    
    public static SmtpRequest data() {
        return SmtpRequests.DATA;
    }
    
    public static SmtpRequest rset() {
        return SmtpRequests.RSET;
    }
    
    public static SmtpRequest help(final String cmd) {
        return (cmd == null) ? SmtpRequests.HELP_NO_ARG : new DefaultSmtpRequest(SmtpCommand.HELP, new CharSequence[] { cmd });
    }
    
    public static SmtpRequest quit() {
        return SmtpRequests.QUIT;
    }
    
    public static SmtpRequest mail(final CharSequence sender, final CharSequence... mailParameters) {
        if (mailParameters == null || mailParameters.length == 0) {
            return new DefaultSmtpRequest(SmtpCommand.MAIL, new CharSequence[] { (sender != null) ? ("FROM:<" + (Object)sender + '>') : SmtpRequests.FROM_NULL_SENDER });
        }
        final List<CharSequence> params = new ArrayList<CharSequence>(mailParameters.length + 1);
        params.add((sender != null) ? ("FROM:<" + (Object)sender + '>') : SmtpRequests.FROM_NULL_SENDER);
        for (final CharSequence param : mailParameters) {
            params.add(param);
        }
        return new DefaultSmtpRequest(SmtpCommand.MAIL, params);
    }
    
    public static SmtpRequest rcpt(final CharSequence recipient, final CharSequence... rcptParameters) {
        ObjectUtil.checkNotNull(recipient, "recipient");
        if (rcptParameters == null || rcptParameters.length == 0) {
            return new DefaultSmtpRequest(SmtpCommand.RCPT, new CharSequence[] { "TO:<" + (Object)recipient + '>' });
        }
        final List<CharSequence> params = new ArrayList<CharSequence>(rcptParameters.length + 1);
        params.add("TO:<" + (Object)recipient + '>');
        for (final CharSequence param : rcptParameters) {
            params.add(param);
        }
        return new DefaultSmtpRequest(SmtpCommand.RCPT, params);
    }
    
    public static SmtpRequest expn(final CharSequence mailingList) {
        return new DefaultSmtpRequest(SmtpCommand.EXPN, new CharSequence[] { ObjectUtil.checkNotNull(mailingList, "mailingList") });
    }
    
    public static SmtpRequest vrfy(final CharSequence user) {
        return new DefaultSmtpRequest(SmtpCommand.VRFY, new CharSequence[] { ObjectUtil.checkNotNull(user, "user") });
    }
    
    private SmtpRequests() {
    }
    
    static {
        DATA = new DefaultSmtpRequest(SmtpCommand.DATA);
        NOOP = new DefaultSmtpRequest(SmtpCommand.NOOP);
        RSET = new DefaultSmtpRequest(SmtpCommand.RSET);
        HELP_NO_ARG = new DefaultSmtpRequest(SmtpCommand.HELP);
        QUIT = new DefaultSmtpRequest(SmtpCommand.QUIT);
        FROM_NULL_SENDER = new AsciiString("FROM:<>");
    }
}

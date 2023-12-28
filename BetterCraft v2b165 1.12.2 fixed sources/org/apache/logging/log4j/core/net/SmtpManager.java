// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.net;

import java.util.Hashtable;
import javax.mail.PasswordAuthentication;
import javax.mail.Authenticator;
import java.util.Properties;
import org.apache.logging.log4j.core.util.NetUtils;
import org.apache.logging.log4j.util.PropertiesUtil;
import javax.mail.Transport;
import java.util.Date;
import javax.mail.Multipart;
import javax.mail.BodyPart;
import javax.mail.internet.MimeBodyPart;
import javax.activation.DataSource;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.InternetHeaders;
import java.io.IOException;
import org.apache.logging.log4j.LoggingException;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.util.NameUtil;
import org.apache.logging.log4j.util.Strings;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.impl.MutableLogEvent;
import org.apache.logging.log4j.message.ReusableMessage;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.core.LoggerContext;
import javax.mail.MessagingException;
import javax.mail.Message;
import javax.mail.internet.MimeMessage;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.util.CyclicBuffer;
import javax.mail.Session;
import org.apache.logging.log4j.core.appender.AbstractManager;

public class SmtpManager extends AbstractManager
{
    private static final SMTPManagerFactory FACTORY;
    private final Session session;
    private final CyclicBuffer<LogEvent> buffer;
    private volatile MimeMessage message;
    private final FactoryData data;
    
    private static MimeMessage createMimeMessage(final FactoryData data, final Session session, final LogEvent appendEvent) throws MessagingException {
        return new MimeMessageBuilder(session).setFrom(data.from).setReplyTo(data.replyto).setRecipients(Message.RecipientType.TO, data.to).setRecipients(Message.RecipientType.CC, data.cc).setRecipients(Message.RecipientType.BCC, data.bcc).setSubject(data.subject.toSerializable(appendEvent)).build();
    }
    
    protected SmtpManager(final String name, final Session session, final MimeMessage message, final FactoryData data) {
        super(null, name);
        this.session = session;
        this.message = message;
        this.data = data;
        this.buffer = new CyclicBuffer<LogEvent>(LogEvent.class, data.numElements);
    }
    
    public void add(LogEvent event) {
        if (event instanceof Log4jLogEvent && event.getMessage() instanceof ReusableMessage) {
            ((Log4jLogEvent)event).makeMessageImmutable();
        }
        else if (event instanceof MutableLogEvent) {
            event = ((MutableLogEvent)event).createMemento();
        }
        this.buffer.add(event);
    }
    
    public static SmtpManager getSmtpManager(final Configuration config, final String to, final String cc, final String bcc, final String from, final String replyTo, final String subject, String protocol, final String host, final int port, final String username, final String password, final boolean isDebug, final String filterName, final int numElements) {
        if (Strings.isEmpty(protocol)) {
            protocol = "smtp";
        }
        final StringBuilder sb = new StringBuilder();
        if (to != null) {
            sb.append(to);
        }
        sb.append(':');
        if (cc != null) {
            sb.append(cc);
        }
        sb.append(':');
        if (bcc != null) {
            sb.append(bcc);
        }
        sb.append(':');
        if (from != null) {
            sb.append(from);
        }
        sb.append(':');
        if (replyTo != null) {
            sb.append(replyTo);
        }
        sb.append(':');
        if (subject != null) {
            sb.append(subject);
        }
        sb.append(':');
        sb.append(protocol).append(':').append(host).append(':').append("port").append(':');
        if (username != null) {
            sb.append(username);
        }
        sb.append(':');
        if (password != null) {
            sb.append(password);
        }
        sb.append(isDebug ? ":debug:" : "::");
        sb.append(filterName);
        final String name = "SMTP:" + NameUtil.md5(sb.toString());
        final AbstractStringLayout.Serializer subjectSerializer = PatternLayout.newSerializerBuilder().setConfiguration(config).setPattern(subject).build();
        return AbstractManager.getManager(name, (ManagerFactory<SmtpManager, FactoryData>)SmtpManager.FACTORY, new FactoryData(to, cc, bcc, from, replyTo, subjectSerializer, protocol, host, port, username, password, isDebug, numElements));
    }
    
    public void sendEvents(final Layout<?> layout, final LogEvent appendEvent) {
        if (this.message == null) {
            this.connect(appendEvent);
        }
        try {
            final LogEvent[] priorEvents = this.buffer.removeAll();
            final byte[] rawBytes = this.formatContentToBytes(priorEvents, appendEvent, layout);
            final String contentType = layout.getContentType();
            final String encoding = this.getEncoding(rawBytes, contentType);
            final byte[] encodedBytes = this.encodeContentToBytes(rawBytes, encoding);
            final InternetHeaders headers = this.getHeaders(contentType, encoding);
            final MimeMultipart mp = this.getMimeMultipart(encodedBytes, headers);
            this.sendMultipartMessage(this.message, mp);
        }
        catch (final MessagingException | IOException | RuntimeException e) {
            this.logError("Caught exception while sending e-mail notification.", e);
            throw new LoggingException("Error occurred while sending email", e);
        }
    }
    
    protected byte[] formatContentToBytes(final LogEvent[] priorEvents, final LogEvent appendEvent, final Layout<?> layout) throws IOException {
        final ByteArrayOutputStream raw = new ByteArrayOutputStream();
        this.writeContent(priorEvents, appendEvent, layout, raw);
        return raw.toByteArray();
    }
    
    private void writeContent(final LogEvent[] priorEvents, final LogEvent appendEvent, final Layout<?> layout, final ByteArrayOutputStream out) throws IOException {
        this.writeHeader(layout, out);
        this.writeBuffer(priorEvents, appendEvent, layout, out);
        this.writeFooter(layout, out);
    }
    
    protected void writeHeader(final Layout<?> layout, final OutputStream out) throws IOException {
        final byte[] header = layout.getHeader();
        if (header != null) {
            out.write(header);
        }
    }
    
    protected void writeBuffer(final LogEvent[] priorEvents, final LogEvent appendEvent, final Layout<?> layout, final OutputStream out) throws IOException {
        for (final LogEvent priorEvent : priorEvents) {
            final byte[] bytes = layout.toByteArray(priorEvent);
            out.write(bytes);
        }
        final byte[] bytes2 = layout.toByteArray(appendEvent);
        out.write(bytes2);
    }
    
    protected void writeFooter(final Layout<?> layout, final OutputStream out) throws IOException {
        final byte[] footer = layout.getFooter();
        if (footer != null) {
            out.write(footer);
        }
    }
    
    protected String getEncoding(final byte[] rawBytes, final String contentType) {
        final DataSource dataSource = (DataSource)new ByteArrayDataSource(rawBytes, contentType);
        return MimeUtility.getEncoding(dataSource);
    }
    
    protected byte[] encodeContentToBytes(final byte[] rawBytes, final String encoding) throws MessagingException, IOException {
        final ByteArrayOutputStream encoded = new ByteArrayOutputStream();
        this.encodeContent(rawBytes, encoding, encoded);
        return encoded.toByteArray();
    }
    
    protected void encodeContent(final byte[] bytes, final String encoding, final ByteArrayOutputStream out) throws MessagingException, IOException {
        try (final OutputStream encoder = MimeUtility.encode((OutputStream)out, encoding)) {
            encoder.write(bytes);
        }
    }
    
    protected InternetHeaders getHeaders(final String contentType, final String encoding) {
        final InternetHeaders headers = new InternetHeaders();
        headers.setHeader("Content-Type", contentType + "; charset=UTF-8");
        headers.setHeader("Content-Transfer-Encoding", encoding);
        return headers;
    }
    
    protected MimeMultipart getMimeMultipart(final byte[] encodedBytes, final InternetHeaders headers) throws MessagingException {
        final MimeMultipart mp = new MimeMultipart();
        final MimeBodyPart part = new MimeBodyPart(headers, encodedBytes);
        mp.addBodyPart((BodyPart)part);
        return mp;
    }
    
    protected void sendMultipartMessage(final MimeMessage msg, final MimeMultipart mp) throws MessagingException {
        synchronized (msg) {
            msg.setContent((Multipart)mp);
            msg.setSentDate(new Date());
            Transport.send((Message)msg);
        }
    }
    
    private synchronized void connect(final LogEvent appendEvent) {
        if (this.message != null) {
            return;
        }
        try {
            this.message = createMimeMessage(this.data, this.session, appendEvent);
        }
        catch (final MessagingException e) {
            this.logError("Could not set SmtpAppender message options", (Throwable)e);
            this.message = null;
        }
    }
    
    static {
        FACTORY = new SMTPManagerFactory();
    }
    
    private static class FactoryData
    {
        private final String to;
        private final String cc;
        private final String bcc;
        private final String from;
        private final String replyto;
        private final AbstractStringLayout.Serializer subject;
        private final String protocol;
        private final String host;
        private final int port;
        private final String username;
        private final String password;
        private final boolean isDebug;
        private final int numElements;
        
        public FactoryData(final String to, final String cc, final String bcc, final String from, final String replyTo, final AbstractStringLayout.Serializer subjectSerializer, final String protocol, final String host, final int port, final String username, final String password, final boolean isDebug, final int numElements) {
            this.to = to;
            this.cc = cc;
            this.bcc = bcc;
            this.from = from;
            this.replyto = replyTo;
            this.subject = subjectSerializer;
            this.protocol = protocol;
            this.host = host;
            this.port = port;
            this.username = username;
            this.password = password;
            this.isDebug = isDebug;
            this.numElements = numElements;
        }
    }
    
    private static class SMTPManagerFactory implements ManagerFactory<SmtpManager, FactoryData>
    {
        @Override
        public SmtpManager createManager(final String name, final FactoryData data) {
            final String prefix = "mail." + data.protocol;
            final Properties properties = PropertiesUtil.getSystemProperties();
            ((Hashtable<String, String>)properties).put("mail.transport.protocol", data.protocol);
            if (properties.getProperty("mail.host") == null) {
                ((Hashtable<String, String>)properties).put("mail.host", NetUtils.getLocalHostname());
            }
            if (null != data.host) {
                ((Hashtable<String, String>)properties).put(prefix + ".host", data.host);
            }
            if (data.port > 0) {
                ((Hashtable<String, String>)properties).put(prefix + ".port", String.valueOf(data.port));
            }
            final Authenticator authenticator = this.buildAuthenticator(data.username, data.password);
            if (null != authenticator) {
                ((Hashtable<String, String>)properties).put(prefix + ".auth", "true");
            }
            final Session session = Session.getInstance(properties, authenticator);
            session.setProtocolForAddress("rfc822", data.protocol);
            session.setDebug(data.isDebug);
            return new SmtpManager(name, session, null, data);
        }
        
        private Authenticator buildAuthenticator(final String username, final String password) {
            if (null != password && null != username) {
                return new Authenticator() {
                    private final PasswordAuthentication passwordAuthentication = new PasswordAuthentication(username, password);
                    
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return this.passwordAuthentication;
                    }
                };
            }
            return null;
        }
    }
}

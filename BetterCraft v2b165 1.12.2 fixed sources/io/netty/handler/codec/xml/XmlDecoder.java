// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.xml;

import com.fasterxml.aalto.stax.InputFactoryImpl;
import javax.xml.stream.XMLStreamException;
import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import com.fasterxml.aalto.AsyncByteArrayFeeder;
import com.fasterxml.aalto.AsyncXMLStreamReader;
import com.fasterxml.aalto.AsyncXMLInputFactory;
import io.netty.handler.codec.ByteToMessageDecoder;

public class XmlDecoder extends ByteToMessageDecoder
{
    private static final AsyncXMLInputFactory XML_INPUT_FACTORY;
    private static final XmlDocumentEnd XML_DOCUMENT_END;
    private final AsyncXMLStreamReader<AsyncByteArrayFeeder> streamReader;
    private final AsyncByteArrayFeeder streamFeeder;
    
    public XmlDecoder() {
        this.streamReader = (AsyncXMLStreamReader<AsyncByteArrayFeeder>)XmlDecoder.XML_INPUT_FACTORY.createAsyncForByteArray();
        this.streamFeeder = (AsyncByteArrayFeeder)this.streamReader.getInputFeeder();
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
        final byte[] buffer = new byte[in.readableBytes()];
        in.readBytes(buffer);
        try {
            this.streamFeeder.feedInput(buffer, 0, buffer.length);
        }
        catch (final XMLStreamException exception) {
            in.skipBytes(in.readableBytes());
            throw exception;
        }
        while (!this.streamFeeder.needMoreInput()) {
            final int type = this.streamReader.next();
            switch (type) {
                case 7: {
                    out.add(new XmlDocumentStart(this.streamReader.getEncoding(), this.streamReader.getVersion(), this.streamReader.isStandalone(), this.streamReader.getCharacterEncodingScheme()));
                    continue;
                }
                case 8: {
                    out.add(XmlDecoder.XML_DOCUMENT_END);
                    continue;
                }
                case 1: {
                    final XmlElementStart elementStart = new XmlElementStart(this.streamReader.getLocalName(), this.streamReader.getName().getNamespaceURI(), this.streamReader.getPrefix());
                    for (int x = 0; x < this.streamReader.getAttributeCount(); ++x) {
                        final XmlAttribute attribute = new XmlAttribute(this.streamReader.getAttributeType(x), this.streamReader.getAttributeLocalName(x), this.streamReader.getAttributePrefix(x), this.streamReader.getAttributeNamespace(x), this.streamReader.getAttributeValue(x));
                        elementStart.attributes().add(attribute);
                    }
                    for (int x = 0; x < this.streamReader.getNamespaceCount(); ++x) {
                        final XmlNamespace namespace = new XmlNamespace(this.streamReader.getNamespacePrefix(x), this.streamReader.getNamespaceURI(x));
                        elementStart.namespaces().add(namespace);
                    }
                    out.add(elementStart);
                    continue;
                }
                case 2: {
                    final XmlElementEnd elementEnd = new XmlElementEnd(this.streamReader.getLocalName(), this.streamReader.getName().getNamespaceURI(), this.streamReader.getPrefix());
                    for (int x2 = 0; x2 < this.streamReader.getNamespaceCount(); ++x2) {
                        final XmlNamespace namespace2 = new XmlNamespace(this.streamReader.getNamespacePrefix(x2), this.streamReader.getNamespaceURI(x2));
                        elementEnd.namespaces().add(namespace2);
                    }
                    out.add(elementEnd);
                    continue;
                }
                case 3: {
                    out.add(new XmlProcessingInstruction(this.streamReader.getPIData(), this.streamReader.getPITarget()));
                    continue;
                }
                case 4: {
                    out.add(new XmlCharacters(this.streamReader.getText()));
                    continue;
                }
                case 5: {
                    out.add(new XmlComment(this.streamReader.getText()));
                    continue;
                }
                case 6: {
                    out.add(new XmlSpace(this.streamReader.getText()));
                    continue;
                }
                case 9: {
                    out.add(new XmlEntityReference(this.streamReader.getLocalName(), this.streamReader.getText()));
                    continue;
                }
                case 11: {
                    out.add(new XmlDTD(this.streamReader.getText()));
                    continue;
                }
                case 12: {
                    out.add(new XmlCdata(this.streamReader.getText()));
                    continue;
                }
            }
        }
    }
    
    static {
        XML_INPUT_FACTORY = (AsyncXMLInputFactory)new InputFactoryImpl();
        XML_DOCUMENT_END = XmlDocumentEnd.INSTANCE;
    }
}

// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http.multipart;

import io.netty.util.internal.InternalThreadLocalMap;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.StringUtil;
import java.nio.charset.UnsupportedCharsetException;
import java.io.IOException;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaderNames;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.ArrayList;
import io.netty.handler.codec.http.HttpConstants;
import io.netty.buffer.ByteBuf;
import java.util.Map;
import java.util.List;
import java.nio.charset.Charset;
import io.netty.handler.codec.http.HttpRequest;

public class HttpPostMultipartRequestDecoder implements InterfaceHttpPostRequestDecoder
{
    private final HttpDataFactory factory;
    private final HttpRequest request;
    private Charset charset;
    private boolean isLastChunk;
    private final List<InterfaceHttpData> bodyListHttpData;
    private final Map<String, List<InterfaceHttpData>> bodyMapHttpData;
    private ByteBuf undecodedChunk;
    private int bodyListHttpDataRank;
    private String multipartDataBoundary;
    private String multipartMixedBoundary;
    private HttpPostRequestDecoder.MultiPartStatus currentStatus;
    private Map<CharSequence, Attribute> currentFieldAttributes;
    private FileUpload currentFileUpload;
    private Attribute currentAttribute;
    private boolean destroyed;
    private int discardThreshold;
    
    public HttpPostMultipartRequestDecoder(final HttpRequest request) {
        this(new DefaultHttpDataFactory(16384L), request, HttpConstants.DEFAULT_CHARSET);
    }
    
    public HttpPostMultipartRequestDecoder(final HttpDataFactory factory, final HttpRequest request) {
        this(factory, request, HttpConstants.DEFAULT_CHARSET);
    }
    
    public HttpPostMultipartRequestDecoder(final HttpDataFactory factory, final HttpRequest request, final Charset charset) {
        this.bodyListHttpData = new ArrayList<InterfaceHttpData>();
        this.bodyMapHttpData = new TreeMap<String, List<InterfaceHttpData>>(CaseIgnoringComparator.INSTANCE);
        this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.NOTSTARTED;
        this.discardThreshold = 10485760;
        if (factory == null) {
            throw new NullPointerException("factory");
        }
        if (request == null) {
            throw new NullPointerException("request");
        }
        if (charset == null) {
            throw new NullPointerException("charset");
        }
        this.request = request;
        this.charset = charset;
        this.factory = factory;
        this.setMultipart(this.request.headers().get(HttpHeaderNames.CONTENT_TYPE));
        if (request instanceof HttpContent) {
            this.offer((HttpContent)request);
        }
        else {
            this.undecodedChunk = Unpooled.buffer();
            this.parseBody();
        }
    }
    
    private void setMultipart(final String contentType) {
        final String[] dataBoundary = HttpPostRequestDecoder.getMultipartDataBoundary(contentType);
        if (dataBoundary != null) {
            this.multipartDataBoundary = dataBoundary[0];
            if (dataBoundary.length > 1 && dataBoundary[1] != null) {
                this.charset = Charset.forName(dataBoundary[1]);
            }
        }
        else {
            this.multipartDataBoundary = null;
        }
        this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.HEADERDELIMITER;
    }
    
    private void checkDestroyed() {
        if (this.destroyed) {
            throw new IllegalStateException(HttpPostMultipartRequestDecoder.class.getSimpleName() + " was destroyed already");
        }
    }
    
    @Override
    public boolean isMultipart() {
        this.checkDestroyed();
        return true;
    }
    
    @Override
    public void setDiscardThreshold(final int discardThreshold) {
        if (discardThreshold < 0) {
            throw new IllegalArgumentException("discardThreshold must be >= 0");
        }
        this.discardThreshold = discardThreshold;
    }
    
    @Override
    public int getDiscardThreshold() {
        return this.discardThreshold;
    }
    
    @Override
    public List<InterfaceHttpData> getBodyHttpDatas() {
        this.checkDestroyed();
        if (!this.isLastChunk) {
            throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
        }
        return this.bodyListHttpData;
    }
    
    @Override
    public List<InterfaceHttpData> getBodyHttpDatas(final String name) {
        this.checkDestroyed();
        if (!this.isLastChunk) {
            throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
        }
        return this.bodyMapHttpData.get(name);
    }
    
    @Override
    public InterfaceHttpData getBodyHttpData(final String name) {
        this.checkDestroyed();
        if (!this.isLastChunk) {
            throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
        }
        final List<InterfaceHttpData> list = this.bodyMapHttpData.get(name);
        if (list != null) {
            return list.get(0);
        }
        return null;
    }
    
    @Override
    public HttpPostMultipartRequestDecoder offer(final HttpContent content) {
        this.checkDestroyed();
        final ByteBuf buf = content.content();
        if (this.undecodedChunk == null) {
            this.undecodedChunk = buf.copy();
        }
        else {
            this.undecodedChunk.writeBytes(buf);
        }
        if (content instanceof LastHttpContent) {
            this.isLastChunk = true;
        }
        this.parseBody();
        if (this.undecodedChunk != null && this.undecodedChunk.writerIndex() > this.discardThreshold) {
            this.undecodedChunk.discardReadBytes();
        }
        return this;
    }
    
    @Override
    public boolean hasNext() {
        this.checkDestroyed();
        if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.EPILOGUE && this.bodyListHttpDataRank >= this.bodyListHttpData.size()) {
            throw new HttpPostRequestDecoder.EndOfDataDecoderException();
        }
        return !this.bodyListHttpData.isEmpty() && this.bodyListHttpDataRank < this.bodyListHttpData.size();
    }
    
    @Override
    public InterfaceHttpData next() {
        this.checkDestroyed();
        if (this.hasNext()) {
            return this.bodyListHttpData.get(this.bodyListHttpDataRank++);
        }
        return null;
    }
    
    @Override
    public InterfaceHttpData currentPartialHttpData() {
        if (this.currentFileUpload != null) {
            return this.currentFileUpload;
        }
        return this.currentAttribute;
    }
    
    private void parseBody() {
        if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.PREEPILOGUE || this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.EPILOGUE) {
            if (this.isLastChunk) {
                this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.EPILOGUE;
            }
            return;
        }
        this.parseBodyMultipart();
    }
    
    protected void addHttpData(final InterfaceHttpData data) {
        if (data == null) {
            return;
        }
        List<InterfaceHttpData> datas = this.bodyMapHttpData.get(data.getName());
        if (datas == null) {
            datas = new ArrayList<InterfaceHttpData>(1);
            this.bodyMapHttpData.put(data.getName(), datas);
        }
        datas.add(data);
        this.bodyListHttpData.add(data);
    }
    
    private void parseBodyMultipart() {
        if (this.undecodedChunk == null || this.undecodedChunk.readableBytes() == 0) {
            return;
        }
        for (InterfaceHttpData data = this.decodeMultipart(this.currentStatus); data != null; data = this.decodeMultipart(this.currentStatus)) {
            this.addHttpData(data);
            if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.PREEPILOGUE) {
                break;
            }
            if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.EPILOGUE) {
                break;
            }
        }
    }
    
    private InterfaceHttpData decodeMultipart(final HttpPostRequestDecoder.MultiPartStatus state) {
        switch (state) {
            case NOTSTARTED: {
                throw new HttpPostRequestDecoder.ErrorDataDecoderException("Should not be called with the current getStatus");
            }
            case PREAMBLE: {
                throw new HttpPostRequestDecoder.ErrorDataDecoderException("Should not be called with the current getStatus");
            }
            case HEADERDELIMITER: {
                return this.findMultipartDelimiter(this.multipartDataBoundary, HttpPostRequestDecoder.MultiPartStatus.DISPOSITION, HttpPostRequestDecoder.MultiPartStatus.PREEPILOGUE);
            }
            case DISPOSITION: {
                return this.findMultipartDisposition();
            }
            case FIELD: {
                Charset localCharset = null;
                final Attribute charsetAttribute = this.currentFieldAttributes.get(HttpHeaderValues.CHARSET);
                if (charsetAttribute != null) {
                    try {
                        localCharset = Charset.forName(charsetAttribute.getValue());
                    }
                    catch (final IOException e) {
                        throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
                    }
                    catch (final UnsupportedCharsetException e2) {
                        throw new HttpPostRequestDecoder.ErrorDataDecoderException(e2);
                    }
                }
                final Attribute nameAttribute = this.currentFieldAttributes.get(HttpHeaderValues.NAME);
                if (this.currentAttribute == null) {
                    final Attribute lengthAttribute = this.currentFieldAttributes.get(HttpHeaderNames.CONTENT_LENGTH);
                    long size;
                    try {
                        size = ((lengthAttribute != null) ? Long.parseLong(lengthAttribute.getValue()) : 0L);
                    }
                    catch (final IOException e3) {
                        throw new HttpPostRequestDecoder.ErrorDataDecoderException(e3);
                    }
                    catch (final NumberFormatException e4) {
                        size = 0L;
                    }
                    try {
                        if (size > 0L) {
                            this.currentAttribute = this.factory.createAttribute(this.request, cleanString(nameAttribute.getValue()), size);
                        }
                        else {
                            this.currentAttribute = this.factory.createAttribute(this.request, cleanString(nameAttribute.getValue()));
                        }
                    }
                    catch (final NullPointerException e5) {
                        throw new HttpPostRequestDecoder.ErrorDataDecoderException(e5);
                    }
                    catch (final IllegalArgumentException e6) {
                        throw new HttpPostRequestDecoder.ErrorDataDecoderException(e6);
                    }
                    catch (final IOException e3) {
                        throw new HttpPostRequestDecoder.ErrorDataDecoderException(e3);
                    }
                    if (localCharset != null) {
                        this.currentAttribute.setCharset(localCharset);
                    }
                }
                try {
                    this.loadFieldMultipart(this.multipartDataBoundary);
                }
                catch (final HttpPostRequestDecoder.NotEnoughDataDecoderException ignored) {
                    return null;
                }
                final Attribute finalAttribute = this.currentAttribute;
                this.currentAttribute = null;
                this.currentFieldAttributes = null;
                this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.HEADERDELIMITER;
                return finalAttribute;
            }
            case FILEUPLOAD: {
                return this.getFileUpload(this.multipartDataBoundary);
            }
            case MIXEDDELIMITER: {
                return this.findMultipartDelimiter(this.multipartMixedBoundary, HttpPostRequestDecoder.MultiPartStatus.MIXEDDISPOSITION, HttpPostRequestDecoder.MultiPartStatus.HEADERDELIMITER);
            }
            case MIXEDDISPOSITION: {
                return this.findMultipartDisposition();
            }
            case MIXEDFILEUPLOAD: {
                return this.getFileUpload(this.multipartMixedBoundary);
            }
            case PREEPILOGUE: {
                return null;
            }
            case EPILOGUE: {
                return null;
            }
            default: {
                throw new HttpPostRequestDecoder.ErrorDataDecoderException("Shouldn't reach here.");
            }
        }
    }
    
    void skipControlCharacters() {
        HttpPostBodyUtil.SeekAheadOptimize sao;
        try {
            sao = new HttpPostBodyUtil.SeekAheadOptimize(this.undecodedChunk);
        }
        catch (final HttpPostBodyUtil.SeekAheadNoBackArrayException ignored) {
            try {
                this.skipControlCharactersStandard();
            }
            catch (final IndexOutOfBoundsException e1) {
                throw new HttpPostRequestDecoder.NotEnoughDataDecoderException(e1);
            }
            return;
        }
        while (sao.pos < sao.limit) {
            final char c = (char)(sao.bytes[sao.pos++] & 0xFF);
            if (!Character.isISOControl(c) && !Character.isWhitespace(c)) {
                sao.setReadPosition(1);
                return;
            }
        }
        throw new HttpPostRequestDecoder.NotEnoughDataDecoderException("Access out of bounds");
    }
    
    void skipControlCharactersStandard() {
        char c;
        do {
            c = (char)this.undecodedChunk.readUnsignedByte();
        } while (Character.isISOControl(c) || Character.isWhitespace(c));
        this.undecodedChunk.readerIndex(this.undecodedChunk.readerIndex() - 1);
    }
    
    private InterfaceHttpData findMultipartDelimiter(final String delimiter, final HttpPostRequestDecoder.MultiPartStatus dispositionStatus, final HttpPostRequestDecoder.MultiPartStatus closeDelimiterStatus) {
        final int readerIndex = this.undecodedChunk.readerIndex();
        try {
            this.skipControlCharacters();
        }
        catch (final HttpPostRequestDecoder.NotEnoughDataDecoderException ignored) {
            this.undecodedChunk.readerIndex(readerIndex);
            return null;
        }
        this.skipOneLine();
        String newline;
        try {
            newline = this.readDelimiter(delimiter);
        }
        catch (final HttpPostRequestDecoder.NotEnoughDataDecoderException ignored2) {
            this.undecodedChunk.readerIndex(readerIndex);
            return null;
        }
        if (newline.equals(delimiter)) {
            this.currentStatus = dispositionStatus;
            return this.decodeMultipart(dispositionStatus);
        }
        if (!newline.equals(delimiter + "--")) {
            this.undecodedChunk.readerIndex(readerIndex);
            throw new HttpPostRequestDecoder.ErrorDataDecoderException("No Multipart delimiter found");
        }
        this.currentStatus = closeDelimiterStatus;
        if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.HEADERDELIMITER) {
            this.currentFieldAttributes = null;
            return this.decodeMultipart(HttpPostRequestDecoder.MultiPartStatus.HEADERDELIMITER);
        }
        return null;
    }
    
    private InterfaceHttpData findMultipartDisposition() {
        final int readerIndex = this.undecodedChunk.readerIndex();
        if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.DISPOSITION) {
            this.currentFieldAttributes = new TreeMap<CharSequence, Attribute>(CaseIgnoringComparator.INSTANCE);
        }
        while (!this.skipOneLine()) {
            String newline;
            try {
                this.skipControlCharacters();
                newline = this.readLine();
            }
            catch (final HttpPostRequestDecoder.NotEnoughDataDecoderException ignored) {
                this.undecodedChunk.readerIndex(readerIndex);
                return null;
            }
            final String[] contents = splitMultipartHeader(newline);
            if (HttpHeaderNames.CONTENT_DISPOSITION.contentEqualsIgnoreCase(contents[0])) {
                boolean checkSecondArg;
                if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.DISPOSITION) {
                    checkSecondArg = HttpHeaderValues.FORM_DATA.contentEqualsIgnoreCase(contents[1]);
                }
                else {
                    checkSecondArg = (HttpHeaderValues.ATTACHMENT.contentEqualsIgnoreCase(contents[1]) || HttpHeaderValues.FILE.contentEqualsIgnoreCase(contents[1]));
                }
                if (!checkSecondArg) {
                    continue;
                }
                for (int i = 2; i < contents.length; ++i) {
                    final String[] values = contents[i].split("=", 2);
                    Attribute attribute;
                    try {
                        final String name = cleanString(values[0]);
                        String value = values[1];
                        if (HttpHeaderValues.FILENAME.contentEquals(name)) {
                            value = value.substring(1, value.length() - 1);
                        }
                        else {
                            value = cleanString(value);
                        }
                        attribute = this.factory.createAttribute(this.request, name, value);
                    }
                    catch (final NullPointerException e) {
                        throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
                    }
                    catch (final IllegalArgumentException e2) {
                        throw new HttpPostRequestDecoder.ErrorDataDecoderException(e2);
                    }
                    this.currentFieldAttributes.put(attribute.getName(), attribute);
                }
            }
            else if (HttpHeaderNames.CONTENT_TRANSFER_ENCODING.contentEqualsIgnoreCase(contents[0])) {
                Attribute attribute2;
                try {
                    attribute2 = this.factory.createAttribute(this.request, HttpHeaderNames.CONTENT_TRANSFER_ENCODING.toString(), cleanString(contents[1]));
                }
                catch (final NullPointerException e3) {
                    throw new HttpPostRequestDecoder.ErrorDataDecoderException(e3);
                }
                catch (final IllegalArgumentException e4) {
                    throw new HttpPostRequestDecoder.ErrorDataDecoderException(e4);
                }
                this.currentFieldAttributes.put(HttpHeaderNames.CONTENT_TRANSFER_ENCODING, attribute2);
            }
            else if (HttpHeaderNames.CONTENT_LENGTH.contentEqualsIgnoreCase(contents[0])) {
                Attribute attribute2;
                try {
                    attribute2 = this.factory.createAttribute(this.request, HttpHeaderNames.CONTENT_LENGTH.toString(), cleanString(contents[1]));
                }
                catch (final NullPointerException e3) {
                    throw new HttpPostRequestDecoder.ErrorDataDecoderException(e3);
                }
                catch (final IllegalArgumentException e4) {
                    throw new HttpPostRequestDecoder.ErrorDataDecoderException(e4);
                }
                this.currentFieldAttributes.put(HttpHeaderNames.CONTENT_LENGTH, attribute2);
            }
            else {
                if (!HttpHeaderNames.CONTENT_TYPE.contentEqualsIgnoreCase(contents[0])) {
                    throw new HttpPostRequestDecoder.ErrorDataDecoderException("Unknown Params: " + newline);
                }
                if (HttpHeaderValues.MULTIPART_MIXED.contentEqualsIgnoreCase(contents[1])) {
                    if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.DISPOSITION) {
                        final String values2 = StringUtil.substringAfter(contents[2], '=');
                        this.multipartMixedBoundary = "--" + values2;
                        this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.MIXEDDELIMITER;
                        return this.decodeMultipart(HttpPostRequestDecoder.MultiPartStatus.MIXEDDELIMITER);
                    }
                    throw new HttpPostRequestDecoder.ErrorDataDecoderException("Mixed Multipart found in a previous Mixed Multipart");
                }
                else {
                    for (int j = 1; j < contents.length; ++j) {
                        if (contents[j].toLowerCase().startsWith(HttpHeaderValues.CHARSET.toString())) {
                            final String values3 = StringUtil.substringAfter(contents[j], '=');
                            Attribute attribute3;
                            try {
                                attribute3 = this.factory.createAttribute(this.request, HttpHeaderValues.CHARSET.toString(), cleanString(values3));
                            }
                            catch (final NullPointerException e5) {
                                throw new HttpPostRequestDecoder.ErrorDataDecoderException(e5);
                            }
                            catch (final IllegalArgumentException e6) {
                                throw new HttpPostRequestDecoder.ErrorDataDecoderException(e6);
                            }
                            this.currentFieldAttributes.put(HttpHeaderValues.CHARSET, attribute3);
                        }
                        else {
                            Attribute attribute4;
                            try {
                                attribute4 = this.factory.createAttribute(this.request, cleanString(contents[0]), contents[j]);
                            }
                            catch (final NullPointerException e7) {
                                throw new HttpPostRequestDecoder.ErrorDataDecoderException(e7);
                            }
                            catch (final IllegalArgumentException e8) {
                                throw new HttpPostRequestDecoder.ErrorDataDecoderException(e8);
                            }
                            this.currentFieldAttributes.put(attribute4.getName(), attribute4);
                        }
                    }
                }
            }
        }
        final Attribute filenameAttribute = this.currentFieldAttributes.get(HttpHeaderValues.FILENAME);
        if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.DISPOSITION) {
            if (filenameAttribute != null) {
                this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.FILEUPLOAD;
                return this.decodeMultipart(HttpPostRequestDecoder.MultiPartStatus.FILEUPLOAD);
            }
            this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.FIELD;
            return this.decodeMultipart(HttpPostRequestDecoder.MultiPartStatus.FIELD);
        }
        else {
            if (filenameAttribute != null) {
                this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.MIXEDFILEUPLOAD;
                return this.decodeMultipart(HttpPostRequestDecoder.MultiPartStatus.MIXEDFILEUPLOAD);
            }
            throw new HttpPostRequestDecoder.ErrorDataDecoderException("Filename not found");
        }
    }
    
    protected InterfaceHttpData getFileUpload(final String delimiter) {
        final Attribute encoding = this.currentFieldAttributes.get(HttpHeaderNames.CONTENT_TRANSFER_ENCODING);
        Charset localCharset = this.charset;
        HttpPostBodyUtil.TransferEncodingMechanism mechanism = HttpPostBodyUtil.TransferEncodingMechanism.BIT7;
        if (encoding != null) {
            String code;
            try {
                code = encoding.getValue().toLowerCase();
            }
            catch (final IOException e) {
                throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
            }
            if (code.equals(HttpPostBodyUtil.TransferEncodingMechanism.BIT7.value())) {
                localCharset = CharsetUtil.US_ASCII;
            }
            else if (code.equals(HttpPostBodyUtil.TransferEncodingMechanism.BIT8.value())) {
                localCharset = CharsetUtil.ISO_8859_1;
                mechanism = HttpPostBodyUtil.TransferEncodingMechanism.BIT8;
            }
            else {
                if (!code.equals(HttpPostBodyUtil.TransferEncodingMechanism.BINARY.value())) {
                    throw new HttpPostRequestDecoder.ErrorDataDecoderException("TransferEncoding Unknown: " + code);
                }
                mechanism = HttpPostBodyUtil.TransferEncodingMechanism.BINARY;
            }
        }
        final Attribute charsetAttribute = this.currentFieldAttributes.get(HttpHeaderValues.CHARSET);
        if (charsetAttribute != null) {
            try {
                localCharset = Charset.forName(charsetAttribute.getValue());
            }
            catch (final IOException e) {
                throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
            }
            catch (final UnsupportedCharsetException e2) {
                throw new HttpPostRequestDecoder.ErrorDataDecoderException(e2);
            }
        }
        if (this.currentFileUpload == null) {
            final Attribute filenameAttribute = this.currentFieldAttributes.get(HttpHeaderValues.FILENAME);
            final Attribute nameAttribute = this.currentFieldAttributes.get(HttpHeaderValues.NAME);
            final Attribute contentTypeAttribute = this.currentFieldAttributes.get(HttpHeaderNames.CONTENT_TYPE);
            final Attribute lengthAttribute = this.currentFieldAttributes.get(HttpHeaderNames.CONTENT_LENGTH);
            long size;
            try {
                size = ((lengthAttribute != null) ? Long.parseLong(lengthAttribute.getValue()) : 0L);
            }
            catch (final IOException e3) {
                throw new HttpPostRequestDecoder.ErrorDataDecoderException(e3);
            }
            catch (final NumberFormatException ignored) {
                size = 0L;
            }
            try {
                String contentType;
                if (contentTypeAttribute != null) {
                    contentType = contentTypeAttribute.getValue();
                }
                else {
                    contentType = "application/octet-stream";
                }
                this.currentFileUpload = this.factory.createFileUpload(this.request, cleanString(nameAttribute.getValue()), cleanString(filenameAttribute.getValue()), contentType, mechanism.value(), localCharset, size);
            }
            catch (final NullPointerException e4) {
                throw new HttpPostRequestDecoder.ErrorDataDecoderException(e4);
            }
            catch (final IllegalArgumentException e5) {
                throw new HttpPostRequestDecoder.ErrorDataDecoderException(e5);
            }
            catch (final IOException e3) {
                throw new HttpPostRequestDecoder.ErrorDataDecoderException(e3);
            }
        }
        try {
            this.readFileUploadByteMultipart(delimiter);
        }
        catch (final HttpPostRequestDecoder.NotEnoughDataDecoderException e6) {
            return null;
        }
        if (this.currentFileUpload.isCompleted()) {
            if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.FILEUPLOAD) {
                this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.HEADERDELIMITER;
                this.currentFieldAttributes = null;
            }
            else {
                this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.MIXEDDELIMITER;
                this.cleanMixedAttributes();
            }
            final FileUpload fileUpload = this.currentFileUpload;
            this.currentFileUpload = null;
            return fileUpload;
        }
        return null;
    }
    
    @Override
    public void destroy() {
        this.checkDestroyed();
        this.cleanFiles();
        this.destroyed = true;
        if (this.undecodedChunk != null && this.undecodedChunk.refCnt() > 0) {
            this.undecodedChunk.release();
            this.undecodedChunk = null;
        }
        for (int i = this.bodyListHttpDataRank; i < this.bodyListHttpData.size(); ++i) {
            this.bodyListHttpData.get(i).release();
        }
    }
    
    @Override
    public void cleanFiles() {
        this.checkDestroyed();
        this.factory.cleanRequestHttpData(this.request);
    }
    
    @Override
    public void removeHttpDataFromClean(final InterfaceHttpData data) {
        this.checkDestroyed();
        this.factory.removeHttpDataFromClean(this.request, data);
    }
    
    private void cleanMixedAttributes() {
        this.currentFieldAttributes.remove(HttpHeaderValues.CHARSET);
        this.currentFieldAttributes.remove(HttpHeaderNames.CONTENT_LENGTH);
        this.currentFieldAttributes.remove(HttpHeaderNames.CONTENT_TRANSFER_ENCODING);
        this.currentFieldAttributes.remove(HttpHeaderNames.CONTENT_TYPE);
        this.currentFieldAttributes.remove(HttpHeaderValues.FILENAME);
    }
    
    private String readLineStandard() {
        final int readerIndex = this.undecodedChunk.readerIndex();
        try {
            final ByteBuf line = Unpooled.buffer(64);
            while (this.undecodedChunk.isReadable()) {
                byte nextByte = this.undecodedChunk.readByte();
                if (nextByte == 13) {
                    nextByte = this.undecodedChunk.getByte(this.undecodedChunk.readerIndex());
                    if (nextByte == 10) {
                        this.undecodedChunk.readByte();
                        return line.toString(this.charset);
                    }
                    line.writeByte(13);
                }
                else {
                    if (nextByte == 10) {
                        return line.toString(this.charset);
                    }
                    line.writeByte(nextByte);
                }
            }
        }
        catch (final IndexOutOfBoundsException e) {
            this.undecodedChunk.readerIndex(readerIndex);
            throw new HttpPostRequestDecoder.NotEnoughDataDecoderException(e);
        }
        this.undecodedChunk.readerIndex(readerIndex);
        throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
    }
    
    private String readLine() {
        HttpPostBodyUtil.SeekAheadOptimize sao;
        try {
            sao = new HttpPostBodyUtil.SeekAheadOptimize(this.undecodedChunk);
        }
        catch (final HttpPostBodyUtil.SeekAheadNoBackArrayException ignored) {
            return this.readLineStandard();
        }
        final int readerIndex = this.undecodedChunk.readerIndex();
        try {
            final ByteBuf line = Unpooled.buffer(64);
            while (sao.pos < sao.limit) {
                byte nextByte = sao.bytes[sao.pos++];
                if (nextByte == 13) {
                    if (sao.pos < sao.limit) {
                        nextByte = sao.bytes[sao.pos++];
                        if (nextByte == 10) {
                            sao.setReadPosition(0);
                            return line.toString(this.charset);
                        }
                        final HttpPostBodyUtil.SeekAheadOptimize seekAheadOptimize = sao;
                        --seekAheadOptimize.pos;
                        line.writeByte(13);
                    }
                    else {
                        line.writeByte(nextByte);
                    }
                }
                else {
                    if (nextByte == 10) {
                        sao.setReadPosition(0);
                        return line.toString(this.charset);
                    }
                    line.writeByte(nextByte);
                }
            }
        }
        catch (final IndexOutOfBoundsException e) {
            this.undecodedChunk.readerIndex(readerIndex);
            throw new HttpPostRequestDecoder.NotEnoughDataDecoderException(e);
        }
        this.undecodedChunk.readerIndex(readerIndex);
        throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
    }
    
    private String readDelimiterStandard(final String delimiter) {
        final int readerIndex = this.undecodedChunk.readerIndex();
        try {
            final StringBuilder sb = new StringBuilder(64);
            int delimiterPos = 0;
            final int len = delimiter.length();
            while (this.undecodedChunk.isReadable() && delimiterPos < len) {
                final byte nextByte = this.undecodedChunk.readByte();
                if (nextByte != delimiter.charAt(delimiterPos)) {
                    this.undecodedChunk.readerIndex(readerIndex);
                    throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
                }
                ++delimiterPos;
                sb.append((char)nextByte);
            }
            if (this.undecodedChunk.isReadable()) {
                byte nextByte = this.undecodedChunk.readByte();
                if (nextByte == 13) {
                    nextByte = this.undecodedChunk.readByte();
                    if (nextByte == 10) {
                        return sb.toString();
                    }
                    this.undecodedChunk.readerIndex(readerIndex);
                    throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
                }
                else {
                    if (nextByte == 10) {
                        return sb.toString();
                    }
                    if (nextByte == 45) {
                        sb.append('-');
                        nextByte = this.undecodedChunk.readByte();
                        if (nextByte == 45) {
                            sb.append('-');
                            if (!this.undecodedChunk.isReadable()) {
                                return sb.toString();
                            }
                            nextByte = this.undecodedChunk.readByte();
                            if (nextByte == 13) {
                                nextByte = this.undecodedChunk.readByte();
                                if (nextByte == 10) {
                                    return sb.toString();
                                }
                                this.undecodedChunk.readerIndex(readerIndex);
                                throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
                            }
                            else {
                                if (nextByte == 10) {
                                    return sb.toString();
                                }
                                this.undecodedChunk.readerIndex(this.undecodedChunk.readerIndex() - 1);
                                return sb.toString();
                            }
                        }
                    }
                }
            }
        }
        catch (final IndexOutOfBoundsException e) {
            this.undecodedChunk.readerIndex(readerIndex);
            throw new HttpPostRequestDecoder.NotEnoughDataDecoderException(e);
        }
        this.undecodedChunk.readerIndex(readerIndex);
        throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
    }
    
    private String readDelimiter(final String delimiter) {
        HttpPostBodyUtil.SeekAheadOptimize sao;
        try {
            sao = new HttpPostBodyUtil.SeekAheadOptimize(this.undecodedChunk);
        }
        catch (final HttpPostBodyUtil.SeekAheadNoBackArrayException ignored) {
            return this.readDelimiterStandard(delimiter);
        }
        final int readerIndex = this.undecodedChunk.readerIndex();
        int delimiterPos = 0;
        final int len = delimiter.length();
        try {
            final StringBuilder sb = new StringBuilder(64);
            while (sao.pos < sao.limit && delimiterPos < len) {
                final byte nextByte = sao.bytes[sao.pos++];
                if (nextByte != delimiter.charAt(delimiterPos)) {
                    this.undecodedChunk.readerIndex(readerIndex);
                    throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
                }
                ++delimiterPos;
                sb.append((char)nextByte);
            }
            if (sao.pos < sao.limit) {
                byte nextByte = sao.bytes[sao.pos++];
                if (nextByte == 13) {
                    if (sao.pos >= sao.limit) {
                        this.undecodedChunk.readerIndex(readerIndex);
                        throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
                    }
                    nextByte = sao.bytes[sao.pos++];
                    if (nextByte == 10) {
                        sao.setReadPosition(0);
                        return sb.toString();
                    }
                    this.undecodedChunk.readerIndex(readerIndex);
                    throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
                }
                else {
                    if (nextByte == 10) {
                        sao.setReadPosition(0);
                        return sb.toString();
                    }
                    if (nextByte == 45) {
                        sb.append('-');
                        if (sao.pos < sao.limit) {
                            nextByte = sao.bytes[sao.pos++];
                            if (nextByte == 45) {
                                sb.append('-');
                                if (sao.pos >= sao.limit) {
                                    sao.setReadPosition(0);
                                    return sb.toString();
                                }
                                nextByte = sao.bytes[sao.pos++];
                                if (nextByte == 13) {
                                    if (sao.pos >= sao.limit) {
                                        this.undecodedChunk.readerIndex(readerIndex);
                                        throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
                                    }
                                    nextByte = sao.bytes[sao.pos++];
                                    if (nextByte == 10) {
                                        sao.setReadPosition(0);
                                        return sb.toString();
                                    }
                                    this.undecodedChunk.readerIndex(readerIndex);
                                    throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
                                }
                                else {
                                    if (nextByte == 10) {
                                        sao.setReadPosition(0);
                                        return sb.toString();
                                    }
                                    sao.setReadPosition(1);
                                    return sb.toString();
                                }
                            }
                        }
                    }
                }
            }
        }
        catch (final IndexOutOfBoundsException e) {
            this.undecodedChunk.readerIndex(readerIndex);
            throw new HttpPostRequestDecoder.NotEnoughDataDecoderException(e);
        }
        this.undecodedChunk.readerIndex(readerIndex);
        throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
    }
    
    private void readFileUploadByteMultipartStandard(final String delimiter) {
        final int readerIndex = this.undecodedChunk.readerIndex();
        boolean newLine = true;
        int index = 0;
        int lastPosition = this.undecodedChunk.readerIndex();
        boolean found = false;
        while (this.undecodedChunk.isReadable()) {
            byte nextByte = this.undecodedChunk.readByte();
            if (newLine) {
                if (nextByte == delimiter.codePointAt(index)) {
                    ++index;
                    if (delimiter.length() == index) {
                        found = true;
                        break;
                    }
                    continue;
                }
                else {
                    newLine = false;
                    index = 0;
                    if (nextByte == 13) {
                        if (!this.undecodedChunk.isReadable()) {
                            continue;
                        }
                        nextByte = this.undecodedChunk.readByte();
                        if (nextByte == 10) {
                            newLine = true;
                            index = 0;
                            lastPosition = this.undecodedChunk.readerIndex() - 2;
                        }
                        else {
                            lastPosition = this.undecodedChunk.readerIndex() - 1;
                            this.undecodedChunk.readerIndex(lastPosition);
                        }
                    }
                    else if (nextByte == 10) {
                        newLine = true;
                        index = 0;
                        lastPosition = this.undecodedChunk.readerIndex() - 1;
                    }
                    else {
                        lastPosition = this.undecodedChunk.readerIndex();
                    }
                }
            }
            else if (nextByte == 13) {
                if (!this.undecodedChunk.isReadable()) {
                    continue;
                }
                nextByte = this.undecodedChunk.readByte();
                if (nextByte == 10) {
                    newLine = true;
                    index = 0;
                    lastPosition = this.undecodedChunk.readerIndex() - 2;
                }
                else {
                    lastPosition = this.undecodedChunk.readerIndex() - 1;
                    this.undecodedChunk.readerIndex(lastPosition);
                }
            }
            else if (nextByte == 10) {
                newLine = true;
                index = 0;
                lastPosition = this.undecodedChunk.readerIndex() - 1;
            }
            else {
                lastPosition = this.undecodedChunk.readerIndex();
            }
        }
        final ByteBuf buffer = this.undecodedChunk.copy(readerIndex, lastPosition - readerIndex);
        if (found) {
            try {
                this.currentFileUpload.addContent(buffer, true);
                this.undecodedChunk.readerIndex(lastPosition);
                return;
            }
            catch (final IOException e) {
                throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
            }
        }
        try {
            this.currentFileUpload.addContent(buffer, false);
            this.undecodedChunk.readerIndex(lastPosition);
            throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
        }
        catch (final IOException e) {
            throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
        }
    }
    
    private void readFileUploadByteMultipart(final String delimiter) {
        HttpPostBodyUtil.SeekAheadOptimize sao;
        try {
            sao = new HttpPostBodyUtil.SeekAheadOptimize(this.undecodedChunk);
        }
        catch (final HttpPostBodyUtil.SeekAheadNoBackArrayException ignored) {
            this.readFileUploadByteMultipartStandard(delimiter);
            return;
        }
        final int readerIndex = this.undecodedChunk.readerIndex();
        boolean newLine = true;
        int index = 0;
        int lastrealpos = sao.pos;
        boolean found = false;
        while (sao.pos < sao.limit) {
            byte nextByte = sao.bytes[sao.pos++];
            if (newLine) {
                if (nextByte == delimiter.codePointAt(index)) {
                    ++index;
                    if (delimiter.length() == index) {
                        found = true;
                        break;
                    }
                    continue;
                }
                else {
                    newLine = false;
                    index = 0;
                    if (nextByte == 13) {
                        if (sao.pos >= sao.limit) {
                            continue;
                        }
                        nextByte = sao.bytes[sao.pos++];
                        if (nextByte == 10) {
                            newLine = true;
                            index = 0;
                            lastrealpos = sao.pos - 2;
                        }
                        else {
                            final HttpPostBodyUtil.SeekAheadOptimize seekAheadOptimize = sao;
                            --seekAheadOptimize.pos;
                            lastrealpos = sao.pos;
                        }
                    }
                    else if (nextByte == 10) {
                        newLine = true;
                        index = 0;
                        lastrealpos = sao.pos - 1;
                    }
                    else {
                        lastrealpos = sao.pos;
                    }
                }
            }
            else if (nextByte == 13) {
                if (sao.pos >= sao.limit) {
                    continue;
                }
                nextByte = sao.bytes[sao.pos++];
                if (nextByte == 10) {
                    newLine = true;
                    index = 0;
                    lastrealpos = sao.pos - 2;
                }
                else {
                    final HttpPostBodyUtil.SeekAheadOptimize seekAheadOptimize2 = sao;
                    --seekAheadOptimize2.pos;
                    lastrealpos = sao.pos;
                }
            }
            else if (nextByte == 10) {
                newLine = true;
                index = 0;
                lastrealpos = sao.pos - 1;
            }
            else {
                lastrealpos = sao.pos;
            }
        }
        final int lastPosition = sao.getReadPosition(lastrealpos);
        final ByteBuf buffer = this.undecodedChunk.copy(readerIndex, lastPosition - readerIndex);
        if (found) {
            try {
                this.currentFileUpload.addContent(buffer, true);
                this.undecodedChunk.readerIndex(lastPosition);
                return;
            }
            catch (final IOException e) {
                throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
            }
        }
        try {
            this.currentFileUpload.addContent(buffer, false);
            this.undecodedChunk.readerIndex(lastPosition);
            throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
        }
        catch (final IOException e) {
            throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
        }
    }
    
    private void loadFieldMultipartStandard(final String delimiter) {
        final int readerIndex = this.undecodedChunk.readerIndex();
        try {
            boolean newLine = true;
            int index = 0;
            int lastPosition = this.undecodedChunk.readerIndex();
            boolean found = false;
            while (this.undecodedChunk.isReadable()) {
                byte nextByte = this.undecodedChunk.readByte();
                if (newLine) {
                    if (nextByte == delimiter.codePointAt(index)) {
                        ++index;
                        if (delimiter.length() == index) {
                            found = true;
                            break;
                        }
                        continue;
                    }
                    else {
                        newLine = false;
                        index = 0;
                        if (nextByte == 13) {
                            if (this.undecodedChunk.isReadable()) {
                                nextByte = this.undecodedChunk.readByte();
                                if (nextByte == 10) {
                                    newLine = true;
                                    index = 0;
                                    lastPosition = this.undecodedChunk.readerIndex() - 2;
                                }
                                else {
                                    lastPosition = this.undecodedChunk.readerIndex() - 1;
                                    this.undecodedChunk.readerIndex(lastPosition);
                                }
                            }
                            else {
                                lastPosition = this.undecodedChunk.readerIndex() - 1;
                            }
                        }
                        else if (nextByte == 10) {
                            newLine = true;
                            index = 0;
                            lastPosition = this.undecodedChunk.readerIndex() - 1;
                        }
                        else {
                            lastPosition = this.undecodedChunk.readerIndex();
                        }
                    }
                }
                else if (nextByte == 13) {
                    if (this.undecodedChunk.isReadable()) {
                        nextByte = this.undecodedChunk.readByte();
                        if (nextByte == 10) {
                            newLine = true;
                            index = 0;
                            lastPosition = this.undecodedChunk.readerIndex() - 2;
                        }
                        else {
                            lastPosition = this.undecodedChunk.readerIndex() - 1;
                            this.undecodedChunk.readerIndex(lastPosition);
                        }
                    }
                    else {
                        lastPosition = this.undecodedChunk.readerIndex() - 1;
                    }
                }
                else if (nextByte == 10) {
                    newLine = true;
                    index = 0;
                    lastPosition = this.undecodedChunk.readerIndex() - 1;
                }
                else {
                    lastPosition = this.undecodedChunk.readerIndex();
                }
            }
            if (!found) {
                try {
                    this.currentAttribute.addContent(this.undecodedChunk.copy(readerIndex, lastPosition - readerIndex), false);
                }
                catch (final IOException e) {
                    throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
                }
                this.undecodedChunk.readerIndex(lastPosition);
                throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
            }
            try {
                this.currentAttribute.addContent(this.undecodedChunk.copy(readerIndex, lastPosition - readerIndex), true);
            }
            catch (final IOException e) {
                throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
            }
            this.undecodedChunk.readerIndex(lastPosition);
        }
        catch (final IndexOutOfBoundsException e2) {
            this.undecodedChunk.readerIndex(readerIndex);
            throw new HttpPostRequestDecoder.NotEnoughDataDecoderException(e2);
        }
    }
    
    private void loadFieldMultipart(final String delimiter) {
        HttpPostBodyUtil.SeekAheadOptimize sao;
        try {
            sao = new HttpPostBodyUtil.SeekAheadOptimize(this.undecodedChunk);
        }
        catch (final HttpPostBodyUtil.SeekAheadNoBackArrayException ignored) {
            this.loadFieldMultipartStandard(delimiter);
            return;
        }
        final int readerIndex = this.undecodedChunk.readerIndex();
        try {
            boolean newLine = true;
            int index = 0;
            int lastrealpos = sao.pos;
            boolean found = false;
            while (sao.pos < sao.limit) {
                byte nextByte = sao.bytes[sao.pos++];
                if (newLine) {
                    if (nextByte == delimiter.codePointAt(index)) {
                        ++index;
                        if (delimiter.length() == index) {
                            found = true;
                            break;
                        }
                        continue;
                    }
                    else {
                        newLine = false;
                        index = 0;
                        if (nextByte == 13) {
                            if (sao.pos >= sao.limit) {
                                continue;
                            }
                            nextByte = sao.bytes[sao.pos++];
                            if (nextByte == 10) {
                                newLine = true;
                                index = 0;
                                lastrealpos = sao.pos - 2;
                            }
                            else {
                                final HttpPostBodyUtil.SeekAheadOptimize seekAheadOptimize = sao;
                                --seekAheadOptimize.pos;
                                lastrealpos = sao.pos;
                            }
                        }
                        else if (nextByte == 10) {
                            newLine = true;
                            index = 0;
                            lastrealpos = sao.pos - 1;
                        }
                        else {
                            lastrealpos = sao.pos;
                        }
                    }
                }
                else if (nextByte == 13) {
                    if (sao.pos >= sao.limit) {
                        continue;
                    }
                    nextByte = sao.bytes[sao.pos++];
                    if (nextByte == 10) {
                        newLine = true;
                        index = 0;
                        lastrealpos = sao.pos - 2;
                    }
                    else {
                        final HttpPostBodyUtil.SeekAheadOptimize seekAheadOptimize2 = sao;
                        --seekAheadOptimize2.pos;
                        lastrealpos = sao.pos;
                    }
                }
                else if (nextByte == 10) {
                    newLine = true;
                    index = 0;
                    lastrealpos = sao.pos - 1;
                }
                else {
                    lastrealpos = sao.pos;
                }
            }
            final int lastPosition = sao.getReadPosition(lastrealpos);
            if (!found) {
                try {
                    this.currentAttribute.addContent(this.undecodedChunk.copy(readerIndex, lastPosition - readerIndex), false);
                }
                catch (final IOException e) {
                    throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
                }
                this.undecodedChunk.readerIndex(lastPosition);
                throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
            }
            try {
                this.currentAttribute.addContent(this.undecodedChunk.copy(readerIndex, lastPosition - readerIndex), true);
            }
            catch (final IOException e) {
                throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
            }
            this.undecodedChunk.readerIndex(lastPosition);
        }
        catch (final IndexOutOfBoundsException e2) {
            this.undecodedChunk.readerIndex(readerIndex);
            throw new HttpPostRequestDecoder.NotEnoughDataDecoderException(e2);
        }
    }
    
    private static String cleanString(final String field) {
        final StringBuilder sb = new StringBuilder(field.length());
        for (int i = 0; i < field.length(); ++i) {
            final char nextChar = field.charAt(i);
            if (nextChar == ':') {
                sb.append(' ');
            }
            else if (nextChar == ',') {
                sb.append(' ');
            }
            else if (nextChar == '=') {
                sb.append(' ');
            }
            else if (nextChar == ';') {
                sb.append(' ');
            }
            else if (nextChar == '\t') {
                sb.append(' ');
            }
            else if (nextChar != '\"') {
                sb.append(nextChar);
            }
        }
        return sb.toString().trim();
    }
    
    private boolean skipOneLine() {
        if (!this.undecodedChunk.isReadable()) {
            return false;
        }
        byte nextByte = this.undecodedChunk.readByte();
        if (nextByte == 13) {
            if (!this.undecodedChunk.isReadable()) {
                this.undecodedChunk.readerIndex(this.undecodedChunk.readerIndex() - 1);
                return false;
            }
            nextByte = this.undecodedChunk.readByte();
            if (nextByte == 10) {
                return true;
            }
            this.undecodedChunk.readerIndex(this.undecodedChunk.readerIndex() - 2);
            return false;
        }
        else {
            if (nextByte == 10) {
                return true;
            }
            this.undecodedChunk.readerIndex(this.undecodedChunk.readerIndex() - 1);
            return false;
        }
    }
    
    private static String[] splitMultipartHeader(final String sb) {
        final ArrayList<String> headers = new ArrayList<String>(1);
        int nameEnd;
        int nameStart;
        for (nameStart = (nameEnd = HttpPostBodyUtil.findNonWhitespace(sb, 0)); nameEnd < sb.length(); ++nameEnd) {
            final char ch = sb.charAt(nameEnd);
            if (ch == ':') {
                break;
            }
            if (Character.isWhitespace(ch)) {
                break;
            }
        }
        int colonEnd;
        for (colonEnd = nameEnd; colonEnd < sb.length(); ++colonEnd) {
            if (sb.charAt(colonEnd) == ':') {
                ++colonEnd;
                break;
            }
        }
        final int valueStart = HttpPostBodyUtil.findNonWhitespace(sb, colonEnd);
        final int valueEnd = HttpPostBodyUtil.findEndOfString(sb);
        headers.add(sb.substring(nameStart, nameEnd));
        final String svalue = sb.substring(valueStart, valueEnd);
        String[] values;
        if (svalue.indexOf(59) >= 0) {
            values = splitMultipartHeaderValues(svalue);
        }
        else {
            values = svalue.split(",");
        }
        for (final String value : values) {
            headers.add(value.trim());
        }
        final String[] array = new String[headers.size()];
        for (int i = 0; i < headers.size(); ++i) {
            array[i] = headers.get(i);
        }
        return array;
    }
    
    private static String[] splitMultipartHeaderValues(final String svalue) {
        final List<String> values = (List<String>)InternalThreadLocalMap.get().arrayList(1);
        boolean inQuote = false;
        boolean escapeNext = false;
        int start = 0;
        for (int i = 0; i < svalue.length(); ++i) {
            final char c = svalue.charAt(i);
            if (inQuote) {
                if (escapeNext) {
                    escapeNext = false;
                }
                else if (c == '\\') {
                    escapeNext = true;
                }
                else if (c == '\"') {
                    inQuote = false;
                }
            }
            else if (c == '\"') {
                inQuote = true;
            }
            else if (c == ';') {
                values.add(svalue.substring(start, i));
                start = i + 1;
            }
        }
        values.add(svalue.substring(start));
        return values.toArray(new String[values.size()]);
    }
}

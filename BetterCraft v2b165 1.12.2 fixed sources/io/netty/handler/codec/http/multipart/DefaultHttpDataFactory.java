// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http.multipart;

import java.util.Iterator;
import java.io.IOException;
import java.util.ArrayList;
import io.netty.util.internal.PlatformDependent;
import io.netty.handler.codec.http.HttpConstants;
import java.util.List;
import io.netty.handler.codec.http.HttpRequest;
import java.util.Map;
import java.nio.charset.Charset;

public class DefaultHttpDataFactory implements HttpDataFactory
{
    public static final long MINSIZE = 16384L;
    public static final long MAXSIZE = -1L;
    private final boolean useDisk;
    private final boolean checkSize;
    private long minSize;
    private long maxSize;
    private Charset charset;
    private final Map<HttpRequest, List<HttpData>> requestFileDeleteMap;
    
    public DefaultHttpDataFactory() {
        this.maxSize = -1L;
        this.charset = HttpConstants.DEFAULT_CHARSET;
        this.requestFileDeleteMap = (Map<HttpRequest, List<HttpData>>)PlatformDependent.newConcurrentHashMap();
        this.useDisk = false;
        this.checkSize = true;
        this.minSize = 16384L;
    }
    
    public DefaultHttpDataFactory(final Charset charset) {
        this();
        this.charset = charset;
    }
    
    public DefaultHttpDataFactory(final boolean useDisk) {
        this.maxSize = -1L;
        this.charset = HttpConstants.DEFAULT_CHARSET;
        this.requestFileDeleteMap = (Map<HttpRequest, List<HttpData>>)PlatformDependent.newConcurrentHashMap();
        this.useDisk = useDisk;
        this.checkSize = false;
    }
    
    public DefaultHttpDataFactory(final boolean useDisk, final Charset charset) {
        this(useDisk);
        this.charset = charset;
    }
    
    public DefaultHttpDataFactory(final long minSize) {
        this.maxSize = -1L;
        this.charset = HttpConstants.DEFAULT_CHARSET;
        this.requestFileDeleteMap = (Map<HttpRequest, List<HttpData>>)PlatformDependent.newConcurrentHashMap();
        this.useDisk = false;
        this.checkSize = true;
        this.minSize = minSize;
    }
    
    public DefaultHttpDataFactory(final long minSize, final Charset charset) {
        this(minSize);
        this.charset = charset;
    }
    
    @Override
    public void setMaxLimit(final long maxSize) {
        this.maxSize = maxSize;
    }
    
    private List<HttpData> getList(final HttpRequest request) {
        List<HttpData> list = this.requestFileDeleteMap.get(request);
        if (list == null) {
            list = new ArrayList<HttpData>();
            this.requestFileDeleteMap.put(request, list);
        }
        return list;
    }
    
    @Override
    public Attribute createAttribute(final HttpRequest request, final String name) {
        if (this.useDisk) {
            final Attribute attribute = new DiskAttribute(name, this.charset);
            attribute.setMaxSize(this.maxSize);
            final List<HttpData> fileToDelete = this.getList(request);
            fileToDelete.add(attribute);
            return attribute;
        }
        if (this.checkSize) {
            final Attribute attribute = new MixedAttribute(name, this.minSize, this.charset);
            attribute.setMaxSize(this.maxSize);
            final List<HttpData> fileToDelete = this.getList(request);
            fileToDelete.add(attribute);
            return attribute;
        }
        final MemoryAttribute attribute2 = new MemoryAttribute(name);
        attribute2.setMaxSize(this.maxSize);
        return attribute2;
    }
    
    @Override
    public Attribute createAttribute(final HttpRequest request, final String name, final long definedSize) {
        if (this.useDisk) {
            final Attribute attribute = new DiskAttribute(name, definedSize, this.charset);
            attribute.setMaxSize(this.maxSize);
            final List<HttpData> fileToDelete = this.getList(request);
            fileToDelete.add(attribute);
            return attribute;
        }
        if (this.checkSize) {
            final Attribute attribute = new MixedAttribute(name, definedSize, this.minSize, this.charset);
            attribute.setMaxSize(this.maxSize);
            final List<HttpData> fileToDelete = this.getList(request);
            fileToDelete.add(attribute);
            return attribute;
        }
        final MemoryAttribute attribute2 = new MemoryAttribute(name, definedSize);
        attribute2.setMaxSize(this.maxSize);
        return attribute2;
    }
    
    private static void checkHttpDataSize(final HttpData data) {
        try {
            data.checkSize(data.length());
        }
        catch (final IOException ignored) {
            throw new IllegalArgumentException("Attribute bigger than maxSize allowed");
        }
    }
    
    @Override
    public Attribute createAttribute(final HttpRequest request, final String name, final String value) {
        if (this.useDisk) {
            Attribute attribute;
            try {
                attribute = new DiskAttribute(name, value, this.charset);
                attribute.setMaxSize(this.maxSize);
            }
            catch (final IOException e) {
                attribute = new MixedAttribute(name, value, this.minSize, this.charset);
                attribute.setMaxSize(this.maxSize);
            }
            checkHttpDataSize(attribute);
            final List<HttpData> fileToDelete = this.getList(request);
            fileToDelete.add(attribute);
            return attribute;
        }
        if (this.checkSize) {
            final Attribute attribute = new MixedAttribute(name, value, this.minSize, this.charset);
            attribute.setMaxSize(this.maxSize);
            checkHttpDataSize(attribute);
            final List<HttpData> fileToDelete = this.getList(request);
            fileToDelete.add(attribute);
            return attribute;
        }
        try {
            final MemoryAttribute attribute2 = new MemoryAttribute(name, value, this.charset);
            attribute2.setMaxSize(this.maxSize);
            checkHttpDataSize(attribute2);
            return attribute2;
        }
        catch (final IOException e2) {
            throw new IllegalArgumentException(e2);
        }
    }
    
    @Override
    public FileUpload createFileUpload(final HttpRequest request, final String name, final String filename, final String contentType, final String contentTransferEncoding, final Charset charset, final long size) {
        if (this.useDisk) {
            final FileUpload fileUpload = new DiskFileUpload(name, filename, contentType, contentTransferEncoding, charset, size);
            fileUpload.setMaxSize(this.maxSize);
            checkHttpDataSize(fileUpload);
            final List<HttpData> fileToDelete = this.getList(request);
            fileToDelete.add(fileUpload);
            return fileUpload;
        }
        if (this.checkSize) {
            final FileUpload fileUpload = new MixedFileUpload(name, filename, contentType, contentTransferEncoding, charset, size, this.minSize);
            fileUpload.setMaxSize(this.maxSize);
            checkHttpDataSize(fileUpload);
            final List<HttpData> fileToDelete = this.getList(request);
            fileToDelete.add(fileUpload);
            return fileUpload;
        }
        final MemoryFileUpload fileUpload2 = new MemoryFileUpload(name, filename, contentType, contentTransferEncoding, charset, size);
        fileUpload2.setMaxSize(this.maxSize);
        checkHttpDataSize(fileUpload2);
        return fileUpload2;
    }
    
    @Override
    public void removeHttpDataFromClean(final HttpRequest request, final InterfaceHttpData data) {
        if (data instanceof HttpData) {
            final List<HttpData> fileToDelete = this.getList(request);
            fileToDelete.remove(data);
        }
    }
    
    @Override
    public void cleanRequestHttpData(final HttpRequest request) {
        final List<HttpData> fileToDelete = this.requestFileDeleteMap.remove(request);
        if (fileToDelete != null) {
            for (final HttpData data : fileToDelete) {
                data.delete();
            }
            fileToDelete.clear();
        }
    }
    
    @Override
    public void cleanAllHttpData() {
        final Iterator<Map.Entry<HttpRequest, List<HttpData>>> i = this.requestFileDeleteMap.entrySet().iterator();
        while (i.hasNext()) {
            final Map.Entry<HttpRequest, List<HttpData>> e = i.next();
            i.remove();
            final List<HttpData> fileToDelete = e.getValue();
            if (fileToDelete != null) {
                for (final HttpData data : fileToDelete) {
                    data.delete();
                }
                fileToDelete.clear();
            }
        }
    }
    
    @Override
    public void cleanRequestHttpDatas(final HttpRequest request) {
        this.cleanRequestHttpData(request);
    }
    
    @Override
    public void cleanAllHttpDatas() {
        this.cleanAllHttpData();
    }
}

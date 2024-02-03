// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.xml;

import javax.xml.transform.stream.StreamSource;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;
import java.io.ByteArrayInputStream;
import org.xml.sax.helpers.XMLReaderFactory;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassReader;
import org.xml.sax.SAXException;
import java.io.IOException;
import javax.xml.transform.TransformerException;
import org.xml.sax.ContentHandler;
import javax.xml.transform.Templates;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;
import java.util.zip.ZipEntry;
import java.io.Writer;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.TransformerFactory;
import java.io.OutputStreamWriter;
import java.util.zip.ZipOutputStream;
import java.util.zip.ZipInputStream;
import javax.xml.transform.Source;
import java.io.OutputStream;
import java.io.InputStream;

public class Processor
{
    public static final int BYTECODE = 1;
    public static final int MULTI_XML = 2;
    public static final int SINGLE_XML = 3;
    private static final String SINGLE_XML_NAME = "classes.xml";
    private final int inRepresentation;
    private final int outRepresentation;
    private final InputStream input;
    private final OutputStream output;
    private final Source xslt;
    private int n;
    
    public Processor(final int inRepresentation, final int outRepresentation, final InputStream input, final OutputStream output, final Source xslt) {
        this.n = 0;
        this.inRepresentation = inRepresentation;
        this.outRepresentation = outRepresentation;
        this.input = input;
        this.output = output;
        this.xslt = xslt;
    }
    
    public int process() throws TransformerException, IOException, SAXException {
        final ZipInputStream zipInputStream = new ZipInputStream(this.input);
        final ZipOutputStream zipOutputStream = new ZipOutputStream(this.output);
        final OutputStreamWriter outputStreamWriter = new OutputStreamWriter(zipOutputStream);
        Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
        final TransformerFactory instance = TransformerFactory.newInstance();
        if (!instance.getFeature("http://javax.xml.transform.sax.SAXSource/feature") || !instance.getFeature("http://javax.xml.transform.sax.SAXResult/feature")) {
            return 0;
        }
        final SAXTransformerFactory saxTransformerFactory = (SAXTransformerFactory)instance;
        Templates templates = null;
        if (this.xslt != null) {
            templates = saxTransformerFactory.newTemplates(this.xslt);
        }
        final Processor$EntryElement entryElement = this.getEntryElement(zipOutputStream);
        ContentHandler contentHandler = null;
        switch (this.outRepresentation) {
            case 1: {
                contentHandler = new Processor$OutputSlicingHandler(new Processor$ASMContentHandlerFactory(zipOutputStream), entryElement, false);
                break;
            }
            case 2: {
                contentHandler = new Processor$OutputSlicingHandler(new Processor$SAXWriterFactory(outputStreamWriter, true), entryElement, true);
                break;
            }
            case 3: {
                zipOutputStream.putNextEntry(new ZipEntry("classes.xml"));
                contentHandler = new Processor$SAXWriter(outputStreamWriter, false);
                break;
            }
        }
        ContentHandler contentHandler2;
        if (templates == null) {
            contentHandler2 = contentHandler;
        }
        else {
            contentHandler2 = new Processor$InputSlicingHandler("class", contentHandler, new Processor$TransformerHandlerFactory(saxTransformerFactory, templates, contentHandler));
        }
        final Processor$SubdocumentHandlerFactory processor$SubdocumentHandlerFactory = new Processor$SubdocumentHandlerFactory(contentHandler2);
        if (contentHandler2 != null && this.inRepresentation != 3) {
            contentHandler2.startDocument();
            contentHandler2.startElement("", "classes", "classes", new AttributesImpl());
        }
        int n = 0;
        ZipEntry nextEntry;
        while ((nextEntry = zipInputStream.getNextEntry()) != null) {
            this.update(nextEntry.getName(), this.n++);
            if (this.isClassEntry(nextEntry)) {
                this.processEntry(zipInputStream, nextEntry, processor$SubdocumentHandlerFactory);
            }
            else {
                this.copyEntry(zipInputStream, entryElement.openEntry(this.getName(nextEntry)));
                entryElement.closeEntry();
            }
            ++n;
        }
        if (contentHandler2 != null && this.inRepresentation != 3) {
            contentHandler2.endElement("", "classes", "classes");
            contentHandler2.endDocument();
        }
        if (this.outRepresentation == 3) {
            zipOutputStream.closeEntry();
        }
        zipOutputStream.flush();
        zipOutputStream.close();
        return n;
    }
    
    private void copyEntry(final InputStream inputStream, final OutputStream outputStream) throws IOException {
        if (this.outRepresentation == 3) {
            return;
        }
        final byte[] array = new byte[2048];
        int read;
        while ((read = inputStream.read(array)) != -1) {
            outputStream.write(array, 0, read);
        }
    }
    
    private boolean isClassEntry(final ZipEntry zipEntry) {
        final String name = zipEntry.getName();
        return (this.inRepresentation == 3 && name.equals("classes.xml")) || name.endsWith(".class") || name.endsWith(".class.xml");
    }
    
    private void processEntry(final ZipInputStream zipInputStream, final ZipEntry zipEntry, final Processor$ContentHandlerFactory processor$ContentHandlerFactory) {
        final ContentHandler contentHandler = processor$ContentHandlerFactory.createContentHandler();
        try {
            final boolean b = this.inRepresentation == 3;
            if (this.inRepresentation == 1) {
                new ClassReader(readEntry(zipInputStream, zipEntry)).accept(new SAXClassAdapter(contentHandler, b), 0);
            }
            else {
                final XMLReader xmlReader = XMLReaderFactory.createXMLReader();
                xmlReader.setContentHandler(contentHandler);
                xmlReader.parse(new InputSource(b ? new Processor$ProtectedInputStream(zipInputStream) : new ByteArrayInputStream(readEntry(zipInputStream, zipEntry))));
            }
        }
        catch (final Exception ex) {
            this.update(zipEntry.getName(), 0);
            this.update(ex, 0);
        }
    }
    
    private Processor$EntryElement getEntryElement(final ZipOutputStream zipOutputStream) {
        if (this.outRepresentation == 3) {
            return new Processor$SingleDocElement(zipOutputStream);
        }
        return new Processor$ZipEntryElement(zipOutputStream);
    }
    
    private String getName(final ZipEntry zipEntry) {
        String s = zipEntry.getName();
        if (this.isClassEntry(zipEntry)) {
            if (this.inRepresentation != 1 && this.outRepresentation == 1) {
                s = s.substring(0, s.length() - 4);
            }
            else if (this.inRepresentation == 1 && this.outRepresentation != 1) {
                s += ".xml";
            }
        }
        return s;
    }
    
    private static byte[] readEntry(final InputStream inputStream, final ZipEntry zipEntry) throws IOException {
        final long size = zipEntry.getSize();
        if (size > -1L) {
            final byte[] array = new byte[(int)size];
            int read;
            for (int n = 0; (read = inputStream.read(array, n, array.length - n)) > 0; n += read) {}
            return array;
        }
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final byte[] array2 = new byte[4096];
        int read2;
        while ((read2 = inputStream.read(array2)) != -1) {
            byteArrayOutputStream.write(array2, 0, read2);
        }
        return byteArrayOutputStream.toByteArray();
    }
    
    protected void update(final Object o, final int n) {
        if (o instanceof Throwable) {
            ((Throwable)o).printStackTrace();
        }
        else if (n % 100 == 0) {
            System.err.println(n + " " + o);
        }
    }
    
    public static void main(final String[] array) throws Exception {
        if (array.length < 2) {
            showUsage();
            return;
        }
        final int representation = getRepresentation(array[0]);
        final int representation2 = getRepresentation(array[1]);
        InputStream in = System.in;
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(System.out);
        Source source = null;
        for (int i = 2; i < array.length; ++i) {
            if ("-in".equals(array[i])) {
                in = new FileInputStream(array[++i]);
            }
            else if ("-out".equals(array[i])) {
                bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(array[++i]));
            }
            else {
                if (!"-xslt".equals(array[i])) {
                    showUsage();
                    return;
                }
                source = new StreamSource(new FileInputStream(array[++i]));
            }
        }
        if (representation == 0 || representation2 == 0) {
            showUsage();
            return;
        }
        final Processor processor = new Processor(representation, representation2, in, bufferedOutputStream, source);
        final long currentTimeMillis = System.currentTimeMillis();
        final int process = processor.process();
        final long currentTimeMillis2 = System.currentTimeMillis();
        System.err.println(process);
        System.err.println(currentTimeMillis2 - currentTimeMillis + "ms  " + 1000.0f * process / (currentTimeMillis2 - currentTimeMillis) + " resources/sec");
    }
    
    private static int getRepresentation(final String s) {
        if ("code".equals(s)) {
            return 1;
        }
        if ("xml".equals(s)) {
            return 2;
        }
        if ("singlexml".equals(s)) {
            return 3;
        }
        return 0;
    }
    
    private static void showUsage() {
        System.err.println("Usage: Main <in format> <out format> [-in <input jar>] [-out <output jar>] [-xslt <xslt fiel>]");
        System.err.println("  when -in or -out is omitted sysin and sysout would be used");
        System.err.println("  <in format> and <out format> - code | xml | singlexml");
    }
}

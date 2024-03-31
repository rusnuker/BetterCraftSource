// 
// Decompiled by Procyon v0.6.0
// 

package javax.activation;

import java.io.ObjectInput;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.Externalizable;

public class MimeType implements Externalizable
{
    private static final String SPECIALS = "()<>@,;:\\\"/[]?=";
    private String primaryType;
    private String subType;
    private final MimeTypeParameterList parameterList;
    
    static boolean isSpecial(final char c) {
        return Character.isWhitespace(c) || Character.isISOControl(c) || "()<>@,;:\\\"/[]?=".indexOf(c) != -1;
    }
    
    public MimeType() {
        this.primaryType = "application";
        this.subType = "*";
        this.parameterList = new MimeTypeParameterList();
    }
    
    public MimeType(final String rawdata) throws MimeTypeParseException {
        this.primaryType = "application";
        this.subType = "*";
        this.parameterList = new MimeTypeParameterList();
        this.parseMimeType(rawdata);
    }
    
    public MimeType(final String primary, final String sub) throws MimeTypeParseException {
        this.primaryType = "application";
        this.subType = "*";
        this.parameterList = new MimeTypeParameterList();
        this.setPrimaryType(primary);
        this.setSubType(sub);
    }
    
    public String getPrimaryType() {
        return this.primaryType;
    }
    
    public void setPrimaryType(final String primary) throws MimeTypeParseException {
        this.primaryType = parseToken(primary);
    }
    
    public String getSubType() {
        return this.subType;
    }
    
    public void setSubType(final String sub) throws MimeTypeParseException {
        this.subType = parseToken(sub);
    }
    
    public MimeTypeParameterList getParameters() {
        return this.parameterList;
    }
    
    public String getParameter(final String name) {
        return this.parameterList.get(name);
    }
    
    public void setParameter(final String name, final String value) {
        this.parameterList.set(name, value);
    }
    
    public void removeParameter(final String name) {
        this.parameterList.remove(name);
    }
    
    @Override
    public String toString() {
        return String.valueOf(this.getBaseType()) + this.parameterList.toString();
    }
    
    public String getBaseType() {
        return String.valueOf(this.getPrimaryType()) + '/' + this.getSubType();
    }
    
    public boolean match(final MimeType type) {
        return this.primaryType.equals(type.primaryType) && ("*".equals(this.subType) || "*".equals(type.subType) || this.subType.equals(type.subType));
    }
    
    public boolean match(final String rawdata) throws MimeTypeParseException {
        return this.match(new MimeType(rawdata));
    }
    
    @Override
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeUTF(this.toString());
        out.flush();
    }
    
    @Override
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        try {
            this.parseMimeType(in.readUTF());
        }
        catch (final MimeTypeParseException mtpex) {
            throw new IOException(mtpex.getMessage());
        }
    }
    
    private void parseMimeType(final String rawData) throws MimeTypeParseException {
        final int index = rawData.indexOf(47);
        if (index == -1) {
            throw new MimeTypeParseException("Expected '/'");
        }
        this.setPrimaryType(rawData.substring(0, index));
        final int index2 = rawData.indexOf(59, index + 1);
        if (index2 == -1) {
            this.setSubType(rawData.substring(index + 1));
        }
        else {
            this.setSubType(rawData.substring(index + 1, index2));
            this.parameterList.parse(rawData.substring(index2));
        }
    }
    
    private static String parseToken(String tokenString) throws MimeTypeParseException {
        tokenString = tokenString.trim();
        for (int i = 0; i < tokenString.length(); ++i) {
            final char c = tokenString.charAt(i);
            if (isSpecial(c)) {
                throw new MimeTypeParseException("Special '" + c + "' not allowed in token");
            }
        }
        return tokenString;
    }
}

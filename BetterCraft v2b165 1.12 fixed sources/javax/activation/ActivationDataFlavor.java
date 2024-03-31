// 
// Decompiled by Procyon v0.6.0
// 

package javax.activation;

import java.io.InputStream;
import java.awt.datatransfer.DataFlavor;

public class ActivationDataFlavor extends DataFlavor
{
    private final Class representationClass;
    private final String mimeType;
    private String humanPresentableName;
    
    public ActivationDataFlavor(final Class representationClass, final String mimeType, final String humanPresentableName) {
        this.representationClass = representationClass;
        this.mimeType = mimeType;
        this.humanPresentableName = humanPresentableName;
    }
    
    public ActivationDataFlavor(final Class representationClass, final String humanPresentableName) {
        this.representationClass = representationClass;
        this.mimeType = "application/x-java-serialized-object";
        this.humanPresentableName = humanPresentableName;
    }
    
    public ActivationDataFlavor(final String mimeType, final String humanPresentableName) {
        this.mimeType = mimeType;
        this.representationClass = InputStream.class;
        this.humanPresentableName = humanPresentableName;
    }
    
    @Override
    public String getMimeType() {
        return this.mimeType;
    }
    
    @Override
    public Class getRepresentationClass() {
        return this.representationClass;
    }
    
    @Override
    public String getHumanPresentableName() {
        return this.humanPresentableName;
    }
    
    @Override
    public void setHumanPresentableName(final String humanPresentableName) {
        this.humanPresentableName = humanPresentableName;
    }
    
    @Override
    public boolean equals(final DataFlavor dataFlavor) {
        return this.isMimeTypeEqual(dataFlavor.getMimeType()) && this.representationClass == dataFlavor.getRepresentationClass();
    }
    
    @Override
    public boolean isMimeTypeEqual(final String mimeType) {
        try {
            final MimeType thisType = new MimeType(this.mimeType);
            final MimeType thatType = new MimeType(mimeType);
            return thisType.match(thatType);
        }
        catch (final MimeTypeParseException e) {
            return false;
        }
    }
    
    @Override
    protected String normalizeMimeTypeParameter(final String parameterName, final String parameterValue) {
        return String.valueOf(parameterName) + "=" + parameterValue;
    }
    
    @Override
    protected String normalizeMimeType(final String mimeType) {
        return mimeType;
    }
}

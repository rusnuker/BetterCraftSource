// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.splash.advertisement;

import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import com.google.gson.stream.JsonReader;
import java.awt.Color;
import com.google.gson.TypeAdapter;

public class AdColorAdapter extends TypeAdapter<Color>
{
    @Override
    public Color read(final JsonReader jsonReader) throws IOException {
        return Color.decode(jsonReader.nextString());
    }
    
    @Override
    public void write(final JsonWriter jsonWriter, final Color color) throws IOException {
        jsonWriter.name("color").value(String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue()));
    }
}

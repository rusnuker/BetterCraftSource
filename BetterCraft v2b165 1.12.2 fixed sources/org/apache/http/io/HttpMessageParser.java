// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.http.io;

import org.apache.http.HttpException;
import java.io.IOException;
import org.apache.http.HttpMessage;

public interface HttpMessageParser<T extends HttpMessage>
{
    T parse() throws IOException, HttpException;
}

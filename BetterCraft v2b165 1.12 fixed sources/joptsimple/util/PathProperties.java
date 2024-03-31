// 
// Decompiled by Procyon v0.6.0
// 

package joptsimple.util;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

public enum PathProperties
{
    FILE_EXISTING("file.existing") {
        @Override
        boolean accept(final Path path) {
            return Files.isRegularFile(path, new LinkOption[0]);
        }
    }, 
    DIRECTORY_EXISTING("directory.existing") {
        @Override
        boolean accept(final Path path) {
            return Files.isDirectory(path, new LinkOption[0]);
        }
    }, 
    NOT_EXISTING("file.not.existing") {
        @Override
        boolean accept(final Path path) {
            return Files.notExists(path, new LinkOption[0]);
        }
    }, 
    FILE_OVERWRITABLE("file.overwritable") {
        @Override
        boolean accept(final Path path) {
            return PathProperties$4.FILE_EXISTING.accept(path) && PathProperties$4.WRITABLE.accept(path);
        }
    }, 
    READABLE("file.readable") {
        @Override
        boolean accept(final Path path) {
            return Files.isReadable(path);
        }
    }, 
    WRITABLE("file.writable") {
        @Override
        boolean accept(final Path path) {
            return Files.isWritable(path);
        }
    };
    
    private final String messageKey;
    
    private PathProperties(final String messageKey) {
        this.messageKey = messageKey;
    }
    
    abstract boolean accept(final Path p0);
    
    String getMessageKey() {
        return this.messageKey;
    }
}

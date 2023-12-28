// 
// Decompiled by Procyon v0.6.0
// 

package com.mojang.realmsclient.dto;

public class RealmsWorldResetDto
{
    private final String seed;
    private final long worldTemplateId;
    private final int levelType;
    private final boolean generateStructures;
    
    public RealmsWorldResetDto(final String seed, final long worldTemplateId, final int levelType, final boolean generateStructures) {
        this.seed = seed;
        this.worldTemplateId = worldTemplateId;
        this.levelType = levelType;
        this.generateStructures = generateStructures;
    }
}

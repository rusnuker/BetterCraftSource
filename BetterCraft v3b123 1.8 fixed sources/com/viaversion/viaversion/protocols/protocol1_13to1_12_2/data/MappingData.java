// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_13to1_12_2.data;

import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import java.util.Iterator;
import com.viaversion.viaversion.util.Key;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.IntArrayTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.api.data.MappingDataLoader;
import com.viaversion.viaversion.util.Int2IntBiMap;
import com.viaversion.viaversion.api.data.Int2IntMapBiMappings;
import com.viaversion.viaversion.util.Int2IntBiHashMap;
import com.viaversion.viaversion.api.data.BiMappings;
import com.viaversion.viaversion.api.data.Mappings;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.google.common.collect.HashBiMap;
import java.util.HashMap;
import com.google.common.collect.BiMap;
import java.util.Map;
import com.viaversion.viaversion.api.data.MappingDataBase;

public class MappingData extends MappingDataBase
{
    private final Map<String, int[]> blockTags;
    private final Map<String, int[]> itemTags;
    private final Map<String, int[]> fluidTags;
    private final BiMap<Short, String> oldEnchantmentsIds;
    private final Map<String, String> translateMapping;
    private final Map<String, String> mojangTranslation;
    private final BiMap<String, String> channelMappings;
    
    public MappingData() {
        super("1.12", "1.13");
        this.blockTags = new HashMap<String, int[]>();
        this.itemTags = new HashMap<String, int[]>();
        this.fluidTags = new HashMap<String, int[]>();
        this.oldEnchantmentsIds = (BiMap<Short, String>)HashBiMap.create();
        this.translateMapping = new HashMap<String, String>();
        this.mojangTranslation = new HashMap<String, String>();
        this.channelMappings = (BiMap<String, String>)HashBiMap.create();
    }
    
    @Override
    protected void loadExtras(final CompoundTag data) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: aload_0         /* this */
        //     2: getfield        com/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/MappingData.blockTags:Ljava/util/Map;
        //     5: aload_1         /* data */
        //     6: ldc             "block_tags"
        //     8: invokevirtual   com/viaversion/viaversion/libs/opennbt/tag/builtin/CompoundTag.get:(Ljava/lang/String;)Lcom/viaversion/viaversion/libs/opennbt/tag/builtin/Tag;
        //    11: checkcast       Lcom/viaversion/viaversion/libs/opennbt/tag/builtin/CompoundTag;
        //    14: invokespecial   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/MappingData.loadTags:(Ljava/util/Map;Lcom/viaversion/viaversion/libs/opennbt/tag/builtin/CompoundTag;)V
        //    17: aload_0         /* this */
        //    18: aload_0         /* this */
        //    19: getfield        com/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/MappingData.itemTags:Ljava/util/Map;
        //    22: aload_1         /* data */
        //    23: ldc             "item_tags"
        //    25: invokevirtual   com/viaversion/viaversion/libs/opennbt/tag/builtin/CompoundTag.get:(Ljava/lang/String;)Lcom/viaversion/viaversion/libs/opennbt/tag/builtin/Tag;
        //    28: checkcast       Lcom/viaversion/viaversion/libs/opennbt/tag/builtin/CompoundTag;
        //    31: invokespecial   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/MappingData.loadTags:(Ljava/util/Map;Lcom/viaversion/viaversion/libs/opennbt/tag/builtin/CompoundTag;)V
        //    34: aload_0         /* this */
        //    35: aload_0         /* this */
        //    36: getfield        com/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/MappingData.fluidTags:Ljava/util/Map;
        //    39: aload_1         /* data */
        //    40: ldc             "fluid_tags"
        //    42: invokevirtual   com/viaversion/viaversion/libs/opennbt/tag/builtin/CompoundTag.get:(Ljava/lang/String;)Lcom/viaversion/viaversion/libs/opennbt/tag/builtin/Tag;
        //    45: checkcast       Lcom/viaversion/viaversion/libs/opennbt/tag/builtin/CompoundTag;
        //    48: invokespecial   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/MappingData.loadTags:(Ljava/util/Map;Lcom/viaversion/viaversion/libs/opennbt/tag/builtin/CompoundTag;)V
        //    51: aload_1         /* data */
        //    52: ldc             "legacy_enchantments"
        //    54: invokevirtual   com/viaversion/viaversion/libs/opennbt/tag/builtin/CompoundTag.get:(Ljava/lang/String;)Lcom/viaversion/viaversion/libs/opennbt/tag/builtin/Tag;
        //    57: checkcast       Lcom/viaversion/viaversion/libs/opennbt/tag/builtin/CompoundTag;
        //    60: astore_2        /* legacyEnchantments */
        //    61: aload_0         /* this */
        //    62: aload_0         /* this */
        //    63: getfield        com/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/MappingData.oldEnchantmentsIds:Lcom/google/common/collect/BiMap;
        //    66: aload_2         /* legacyEnchantments */
        //    67: invokespecial   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/MappingData.loadEnchantments:(Ljava/util/Map;Lcom/viaversion/viaversion/libs/opennbt/tag/builtin/CompoundTag;)V
        //    70: invokestatic    com/viaversion/viaversion/api/Via.getConfig:()Lcom/viaversion/viaversion/api/configuration/ViaVersionConfig;
        //    73: invokeinterface com/viaversion/viaversion/api/configuration/ViaVersionConfig.isSnowCollisionFix:()Z
        //    78: ifeq            96
        //    81: aload_0         /* this */
        //    82: getfield        com/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/MappingData.blockMappings:Lcom/viaversion/viaversion/api/data/Mappings;
        //    85: sipush          1248
        //    88: sipush          3416
        //    91: invokeinterface com/viaversion/viaversion/api/data/Mappings.setNewId:(II)V
        //    96: invokestatic    com/viaversion/viaversion/api/Via.getConfig:()Lcom/viaversion/viaversion/api/configuration/ViaVersionConfig;
        //    99: invokeinterface com/viaversion/viaversion/api/configuration/ViaVersionConfig.isInfestedBlocksFix:()Z
        //   104: ifeq            194
        //   107: aload_0         /* this */
        //   108: getfield        com/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/MappingData.blockMappings:Lcom/viaversion/viaversion/api/data/Mappings;
        //   111: sipush          1552
        //   114: iconst_1       
        //   115: invokeinterface com/viaversion/viaversion/api/data/Mappings.setNewId:(II)V
        //   120: aload_0         /* this */
        //   121: getfield        com/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/MappingData.blockMappings:Lcom/viaversion/viaversion/api/data/Mappings;
        //   124: sipush          1553
        //   127: bipush          14
        //   129: invokeinterface com/viaversion/viaversion/api/data/Mappings.setNewId:(II)V
        //   134: aload_0         /* this */
        //   135: getfield        com/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/MappingData.blockMappings:Lcom/viaversion/viaversion/api/data/Mappings;
        //   138: sipush          1554
        //   141: sipush          3983
        //   144: invokeinterface com/viaversion/viaversion/api/data/Mappings.setNewId:(II)V
        //   149: aload_0         /* this */
        //   150: getfield        com/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/MappingData.blockMappings:Lcom/viaversion/viaversion/api/data/Mappings;
        //   153: sipush          1555
        //   156: sipush          3984
        //   159: invokeinterface com/viaversion/viaversion/api/data/Mappings.setNewId:(II)V
        //   164: aload_0         /* this */
        //   165: getfield        com/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/MappingData.blockMappings:Lcom/viaversion/viaversion/api/data/Mappings;
        //   168: sipush          1556
        //   171: sipush          3985
        //   174: invokeinterface com/viaversion/viaversion/api/data/Mappings.setNewId:(II)V
        //   179: aload_0         /* this */
        //   180: getfield        com/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/MappingData.blockMappings:Lcom/viaversion/viaversion/api/data/Mappings;
        //   183: sipush          1557
        //   186: sipush          3986
        //   189: invokeinterface com/viaversion/viaversion/api/data/Mappings.setNewId:(II)V
        //   194: ldc             "channelmappings-1.13.json"
        //   196: invokestatic    com/viaversion/viaversion/api/data/MappingDataLoader.loadFromDataDir:(Ljava/lang/String;)Lcom/viaversion/viaversion/libs/gson/JsonObject;
        //   199: astore_3        /* object */
        //   200: aload_3         /* object */
        //   201: ifnull          328
        //   204: aload_3         /* object */
        //   205: invokevirtual   com/viaversion/viaversion/libs/gson/JsonObject.entrySet:()Ljava/util/Set;
        //   208: invokeinterface java/util/Set.iterator:()Ljava/util/Iterator;
        //   213: astore          4
        //   215: aload           4
        //   217: invokeinterface java/util/Iterator.hasNext:()Z
        //   222: ifeq            328
        //   225: aload           4
        //   227: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   232: checkcast       Ljava/util/Map$Entry;
        //   235: astore          entry
        //   237: aload           entry
        //   239: invokeinterface java/util/Map$Entry.getKey:()Ljava/lang/Object;
        //   244: checkcast       Ljava/lang/String;
        //   247: astore          oldChannel
        //   249: aload           entry
        //   251: invokeinterface java/util/Map$Entry.getValue:()Ljava/lang/Object;
        //   256: checkcast       Lcom/viaversion/viaversion/libs/gson/JsonElement;
        //   259: invokevirtual   com/viaversion/viaversion/libs/gson/JsonElement.getAsString:()Ljava/lang/String;
        //   262: astore          newChannel
        //   264: aload           newChannel
        //   266: invokestatic    com/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/MappingData.isValid1_13Channel:(Ljava/lang/String;)Z
        //   269: ifne            311
        //   272: invokestatic    com/viaversion/viaversion/api/Via.getPlatform:()Lcom/viaversion/viaversion/api/platform/ViaPlatform;
        //   275: invokeinterface com/viaversion/viaversion/api/platform/ViaPlatform.getLogger:()Ljava/util/logging/Logger;
        //   280: new             Ljava/lang/StringBuilder;
        //   283: dup            
        //   284: invokespecial   java/lang/StringBuilder.<init>:()V
        //   287: ldc             "Channel '"
        //   289: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   292: aload           newChannel
        //   294: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   297: ldc             "' is not a valid 1.13 plugin channel, please check your configuration!"
        //   299: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   302: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   305: invokevirtual   java/util/logging/Logger.warning:(Ljava/lang/String;)V
        //   308: goto            215
        //   311: aload_0         /* this */
        //   312: getfield        com/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/MappingData.channelMappings:Lcom/google/common/collect/BiMap;
        //   315: aload           oldChannel
        //   317: aload           newChannel
        //   319: invokeinterface com/google/common/collect/BiMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   324: pop            
        //   325: goto            215
        //   328: invokestatic    com/viaversion/viaversion/util/GsonUtil.getGson:()Lcom/viaversion/viaversion/libs/gson/Gson;
        //   331: new             Ljava/io/InputStreamReader;
        //   334: dup            
        //   335: ldc             Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/MappingData;.class
        //   337: invokevirtual   java/lang/Class.getClassLoader:()Ljava/lang/ClassLoader;
        //   340: ldc             "assets/viaversion/data/mapping-lang-1.12-1.13.json"
        //   342: invokevirtual   java/lang/ClassLoader.getResourceAsStream:(Ljava/lang/String;)Ljava/io/InputStream;
        //   345: invokespecial   java/io/InputStreamReader.<init>:(Ljava/io/InputStream;)V
        //   348: new             Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/MappingData$1;
        //   351: dup            
        //   352: aload_0         /* this */
        //   353: invokespecial   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/MappingData$1.<init>:(Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/MappingData;)V
        //   356: invokevirtual   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/MappingData$1.getType:()Ljava/lang/reflect/Type;
        //   359: invokevirtual   com/viaversion/viaversion/libs/gson/Gson.fromJson:(Ljava/io/Reader;Ljava/lang/reflect/Type;)Ljava/lang/Object;
        //   362: checkcast       Ljava/util/Map;
        //   365: astore          translateData
        //   367: new             Ljava/io/InputStreamReader;
        //   370: dup            
        //   371: ldc             Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/MappingData;.class
        //   373: invokevirtual   java/lang/Class.getClassLoader:()Ljava/lang/ClassLoader;
        //   376: ldc             "assets/viaversion/data/en_US.properties"
        //   378: invokevirtual   java/lang/ClassLoader.getResourceAsStream:(Ljava/lang/String;)Ljava/io/InputStream;
        //   381: getstatic       java/nio/charset/StandardCharsets.UTF_8:Ljava/nio/charset/Charset;
        //   384: invokespecial   java/io/InputStreamReader.<init>:(Ljava/io/InputStream;Ljava/nio/charset/Charset;)V
        //   387: astore          reader
        //   389: aconst_null    
        //   390: astore          7
        //   392: aload           reader
        //   394: invokestatic    com/google/common/io/CharStreams.toString:(Ljava/lang/Readable;)Ljava/lang/String;
        //   397: ldc_w           "\n"
        //   400: invokevirtual   java/lang/String.split:(Ljava/lang/String;)[Ljava/lang/String;
        //   403: astore          lines
        //   405: aload           reader
        //   407: ifnull          492
        //   410: aload           7
        //   412: ifnull          435
        //   415: aload           reader
        //   417: invokevirtual   java/io/Reader.close:()V
        //   420: goto            492
        //   423: astore          8
        //   425: aload           7
        //   427: aload           8
        //   429: invokevirtual   java/lang/Throwable.addSuppressed:(Ljava/lang/Throwable;)V
        //   432: goto            492
        //   435: aload           reader
        //   437: invokevirtual   java/io/Reader.close:()V
        //   440: goto            492
        //   443: astore          8
        //   445: aload           8
        //   447: astore          7
        //   449: aload           8
        //   451: athrow         
        //   452: astore          9
        //   454: aload           reader
        //   456: ifnull          489
        //   459: aload           7
        //   461: ifnull          484
        //   464: aload           reader
        //   466: invokevirtual   java/io/Reader.close:()V
        //   469: goto            489
        //   472: astore          10
        //   474: aload           7
        //   476: aload           10
        //   478: invokevirtual   java/lang/Throwable.addSuppressed:(Ljava/lang/Throwable;)V
        //   481: goto            489
        //   484: aload           reader
        //   486: invokevirtual   java/io/Reader.close:()V
        //   489: aload           9
        //   491: athrow         
        //   492: aload           lines
        //   494: astore          6
        //   496: aload           6
        //   498: arraylength    
        //   499: istore          7
        //   501: iconst_0       
        //   502: istore          8
        //   504: iload           8
        //   506: iload           7
        //   508: if_icmpge       627
        //   511: aload           6
        //   513: iload           8
        //   515: aaload         
        //   516: astore          line
        //   518: aload           line
        //   520: invokevirtual   java/lang/String.isEmpty:()Z
        //   523: ifeq            529
        //   526: goto            621
        //   529: aload           line
        //   531: ldc_w           "="
        //   534: iconst_2       
        //   535: invokevirtual   java/lang/String.split:(Ljava/lang/String;I)[Ljava/lang/String;
        //   538: astore          keyAndTranslation
        //   540: aload           keyAndTranslation
        //   542: arraylength    
        //   543: iconst_2       
        //   544: if_icmpeq       550
        //   547: goto            621
        //   550: aload           keyAndTranslation
        //   552: iconst_0       
        //   553: aaload         
        //   554: astore          key
        //   556: aload           keyAndTranslation
        //   558: iconst_1       
        //   559: aaload         
        //   560: ldc_w           "%(\\d\\$)?d"
        //   563: ldc_w           "%$1s"
        //   566: invokevirtual   java/lang/String.replaceAll:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //   569: invokevirtual   java/lang/String.trim:()Ljava/lang/String;
        //   572: astore          translation
        //   574: aload_0         /* this */
        //   575: getfield        com/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/MappingData.mojangTranslation:Ljava/util/Map;
        //   578: aload           key
        //   580: aload           translation
        //   582: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   587: pop            
        //   588: aload           translateData
        //   590: aload           key
        //   592: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //   597: checkcast       Ljava/lang/String;
        //   600: astore          dataValue
        //   602: aload           dataValue
        //   604: ifnull          621
        //   607: aload_0         /* this */
        //   608: getfield        com/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/MappingData.translateMapping:Ljava/util/Map;
        //   611: aload           key
        //   613: aload           dataValue
        //   615: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   620: pop            
        //   621: iinc            8, 1
        //   624: goto            504
        //   627: goto            637
        //   630: astore          e
        //   632: aload           e
        //   634: invokevirtual   java/io/IOException.printStackTrace:()V
        //   637: return         
        //    StackMapTable: 00 14 FC 00 60 07 00 56 FB 00 61 FD 00 14 07 00 88 07 00 94 FE 00 5F 07 00 09 07 00 A0 07 00 A0 FF 00 10 00 04 07 00 02 07 00 56 07 00 56 07 00 88 00 00 FF 00 5E 00 08 07 00 02 07 00 56 07 00 56 07 00 88 07 00 0B 07 01 14 07 01 0F 07 00 50 00 01 07 00 50 0B FF 00 07 00 08 07 00 02 07 00 56 07 00 56 07 00 88 07 00 0B 00 07 01 0F 07 00 50 00 01 07 00 50 48 07 00 50 FF 00 13 00 0A 07 00 02 07 00 56 07 00 56 07 00 88 07 00 0B 00 07 01 0F 07 00 50 00 07 00 50 00 01 07 00 50 0B 04 FF 00 02 00 06 07 00 02 07 00 56 07 00 56 07 00 88 07 00 0B 07 01 14 00 00 FE 00 0B 07 01 14 01 01 FC 00 18 07 00 A0 FC 00 14 07 01 14 F9 00 46 FF 00 05 00 05 07 00 02 07 00 56 07 00 56 07 00 88 07 00 0B 00 00 42 07 00 52 06
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  415    420    423    435    Ljava/lang/Throwable;
        //  392    405    443    452    Ljava/lang/Throwable;
        //  392    405    452    492    Any
        //  464    469    472    484    Ljava/lang/Throwable;
        //  443    454    452    492    Any
        //  367    627    630    637    Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.assembler.ir.StackMappingVisitor.push(StackMappingVisitor.java:290)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.execute(StackMappingVisitor.java:837)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.visit(StackMappingVisitor.java:398)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2086)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:203)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:93)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:868)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:761)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:638)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:605)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:195)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:162)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:137)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:333)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:254)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:144)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @Override
    protected Mappings loadMappings(final CompoundTag data, final String key) {
        if (key.equals("blocks")) {
            return super.loadMappings(data, "blockstates");
        }
        if (key.equals("blockstates")) {
            return null;
        }
        return super.loadMappings(data, key);
    }
    
    @Override
    protected BiMappings loadBiMappings(final CompoundTag data, final String key) {
        if (key.equals("items")) {
            return (BiMappings)MappingDataLoader.loadMappings(data, "items", size -> {
                final Int2IntBiHashMap map = new Int2IntBiHashMap(size);
                map.defaultReturnValue(-1);
                return map;
            }, Int2IntBiHashMap::put, (v, mappedSize) -> Int2IntMapBiMappings.of(v));
        }
        return super.loadBiMappings(data, key);
    }
    
    public static String validateNewChannel(final String newId) {
        if (!isValid1_13Channel(newId)) {
            return null;
        }
        final int separatorIndex = newId.indexOf(58);
        if (separatorIndex == -1) {
            return "minecraft:" + newId;
        }
        if (separatorIndex == 0) {
            return "minecraft" + newId;
        }
        return newId;
    }
    
    public static boolean isValid1_13Channel(final String channelId) {
        return channelId.matches("([0-9a-z_.-]+:)?[0-9a-z_/.-]+");
    }
    
    private void loadTags(final Map<String, int[]> output, final CompoundTag newTags) {
        for (final Map.Entry<String, Tag> entry : newTags.entrySet()) {
            final IntArrayTag ids = entry.getValue();
            output.put(Key.namespaced(entry.getKey()), ids.getValue());
        }
    }
    
    private void loadEnchantments(final Map<Short, String> output, final CompoundTag enchantments) {
        for (final Map.Entry<String, Tag> enty : enchantments.entrySet()) {
            output.put(Short.parseShort(enty.getKey()), enty.getValue().getValue());
        }
    }
    
    public Map<String, int[]> getBlockTags() {
        return this.blockTags;
    }
    
    public Map<String, int[]> getItemTags() {
        return this.itemTags;
    }
    
    public Map<String, int[]> getFluidTags() {
        return this.fluidTags;
    }
    
    public BiMap<Short, String> getOldEnchantmentsIds() {
        return this.oldEnchantmentsIds;
    }
    
    public Map<String, String> getTranslateMapping() {
        return this.translateMapping;
    }
    
    public Map<String, String> getMojangTranslation() {
        return this.mojangTranslation;
    }
    
    public BiMap<String, String> getChannelMappings() {
        return this.channelMappings;
    }
}

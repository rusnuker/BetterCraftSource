// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.world.gen.structure;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockChest;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Mirror;
import java.util.Iterator;
import java.util.Collections;
import net.minecraft.util.Tuple;
import com.google.common.collect.Lists;
import javax.annotation.Nullable;
import net.minecraft.util.EnumFacing;
import java.util.Random;
import java.util.List;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.structure.template.TemplateManager;

public class WoodlandMansionPieces
{
    public static void func_191153_a() {
        MapGenStructureIO.registerStructureComponent(MansionTemplate.class, "WMP");
    }
    
    public static void func_191152_a(final TemplateManager p_191152_0_, final BlockPos p_191152_1_, final Rotation p_191152_2_, final List<MansionTemplate> p_191152_3_, final Random p_191152_4_) {
        final Grid woodlandmansionpieces$grid = new Grid(p_191152_4_);
        final Placer woodlandmansionpieces$placer = new Placer(p_191152_0_, p_191152_4_);
        woodlandmansionpieces$placer.func_191125_a(p_191152_1_, p_191152_2_, p_191152_3_, woodlandmansionpieces$grid);
    }
    
    static class FirstFloor extends RoomCollection
    {
        private FirstFloor() {
            super(null);
        }
        
        @Override
        public String func_191104_a(final Random p_191104_1_) {
            return "1x1_a" + (p_191104_1_.nextInt(5) + 1);
        }
        
        @Override
        public String func_191099_b(final Random p_191099_1_) {
            return "1x1_as" + (p_191099_1_.nextInt(4) + 1);
        }
        
        @Override
        public String func_191100_a(final Random p_191100_1_, final boolean p_191100_2_) {
            return "1x2_a" + (p_191100_1_.nextInt(9) + 1);
        }
        
        @Override
        public String func_191098_b(final Random p_191098_1_, final boolean p_191098_2_) {
            return "1x2_b" + (p_191098_1_.nextInt(5) + 1);
        }
        
        @Override
        public String func_191102_c(final Random p_191102_1_) {
            return "1x2_s" + (p_191102_1_.nextInt(2) + 1);
        }
        
        @Override
        public String func_191101_d(final Random p_191101_1_) {
            return "2x2_a" + (p_191101_1_.nextInt(4) + 1);
        }
        
        @Override
        public String func_191103_e(final Random p_191103_1_) {
            return "2x2_s1";
        }
    }
    
    static class Grid
    {
        private final Random field_191117_a;
        private final SimpleGrid field_191118_b;
        private final SimpleGrid field_191119_c;
        private final SimpleGrid[] field_191120_d;
        private final int field_191121_e;
        private final int field_191122_f;
        
        public Grid(final Random p_i47362_1_) {
            this.field_191117_a = p_i47362_1_;
            final int i = 11;
            this.field_191121_e = 7;
            this.field_191122_f = 4;
            (this.field_191118_b = new SimpleGrid(11, 11, 5)).func_191142_a(this.field_191121_e, this.field_191122_f, this.field_191121_e + 1, this.field_191122_f + 1, 3);
            this.field_191118_b.func_191142_a(this.field_191121_e - 1, this.field_191122_f, this.field_191121_e - 1, this.field_191122_f + 1, 2);
            this.field_191118_b.func_191142_a(this.field_191121_e + 2, this.field_191122_f - 2, this.field_191121_e + 3, this.field_191122_f + 3, 5);
            this.field_191118_b.func_191142_a(this.field_191121_e + 1, this.field_191122_f - 2, this.field_191121_e + 1, this.field_191122_f - 1, 1);
            this.field_191118_b.func_191142_a(this.field_191121_e + 1, this.field_191122_f + 2, this.field_191121_e + 1, this.field_191122_f + 3, 1);
            this.field_191118_b.func_191144_a(this.field_191121_e - 1, this.field_191122_f - 1, 1);
            this.field_191118_b.func_191144_a(this.field_191121_e - 1, this.field_191122_f + 2, 1);
            this.field_191118_b.func_191142_a(0, 0, 11, 1, 5);
            this.field_191118_b.func_191142_a(0, 9, 11, 11, 5);
            this.func_191110_a(this.field_191118_b, this.field_191121_e, this.field_191122_f - 2, EnumFacing.WEST, 6);
            this.func_191110_a(this.field_191118_b, this.field_191121_e, this.field_191122_f + 3, EnumFacing.WEST, 6);
            this.func_191110_a(this.field_191118_b, this.field_191121_e - 2, this.field_191122_f - 1, EnumFacing.WEST, 3);
            this.func_191110_a(this.field_191118_b, this.field_191121_e - 2, this.field_191122_f + 2, EnumFacing.WEST, 3);
            while (this.func_191111_a(this.field_191118_b)) {}
            (this.field_191120_d = new SimpleGrid[3])[0] = new SimpleGrid(11, 11, 5);
            this.field_191120_d[1] = new SimpleGrid(11, 11, 5);
            this.field_191120_d[2] = new SimpleGrid(11, 11, 5);
            this.func_191116_a(this.field_191118_b, this.field_191120_d[0]);
            this.func_191116_a(this.field_191118_b, this.field_191120_d[1]);
            this.field_191120_d[0].func_191142_a(this.field_191121_e + 1, this.field_191122_f, this.field_191121_e + 1, this.field_191122_f + 1, 8388608);
            this.field_191120_d[1].func_191142_a(this.field_191121_e + 1, this.field_191122_f, this.field_191121_e + 1, this.field_191122_f + 1, 8388608);
            this.field_191119_c = new SimpleGrid(this.field_191118_b.field_191149_b, this.field_191118_b.field_191150_c, 5);
            this.func_191115_b();
            this.func_191116_a(this.field_191119_c, this.field_191120_d[2]);
        }
        
        public static boolean func_191109_a(final SimpleGrid p_191109_0_, final int p_191109_1_, final int p_191109_2_) {
            final int i = p_191109_0_.func_191145_a(p_191109_1_, p_191109_2_);
            return i == 1 || i == 2 || i == 3 || i == 4;
        }
        
        public boolean func_191114_a(final SimpleGrid p_191114_1_, final int p_191114_2_, final int p_191114_3_, final int p_191114_4_, final int p_191114_5_) {
            return (this.field_191120_d[p_191114_4_].func_191145_a(p_191114_2_, p_191114_3_) & 0xFFFF) == p_191114_5_;
        }
        
        @Nullable
        public EnumFacing func_191113_b(final SimpleGrid p_191113_1_, final int p_191113_2_, final int p_191113_3_, final int p_191113_4_, final int p_191113_5_) {
            EnumFacing[] facings;
            for (int length = (facings = EnumFacing.Plane.HORIZONTAL.facings()).length, i = 0; i < length; ++i) {
                final EnumFacing enumfacing = facings[i];
                if (this.func_191114_a(p_191113_1_, p_191113_2_ + enumfacing.getFrontOffsetX(), p_191113_3_ + enumfacing.getFrontOffsetZ(), p_191113_4_, p_191113_5_)) {
                    return enumfacing;
                }
            }
            return null;
        }
        
        private void func_191110_a(final SimpleGrid p_191110_1_, final int p_191110_2_, final int p_191110_3_, final EnumFacing p_191110_4_, final int p_191110_5_) {
            if (p_191110_5_ > 0) {
                p_191110_1_.func_191144_a(p_191110_2_, p_191110_3_, 1);
                p_191110_1_.func_191141_a(p_191110_2_ + p_191110_4_.getFrontOffsetX(), p_191110_3_ + p_191110_4_.getFrontOffsetZ(), 0, 1);
                for (int i = 0; i < 8; ++i) {
                    final EnumFacing enumfacing = EnumFacing.getHorizontal(this.field_191117_a.nextInt(4));
                    if (enumfacing != p_191110_4_.getOpposite() && (enumfacing != EnumFacing.EAST || !this.field_191117_a.nextBoolean())) {
                        final int j = p_191110_2_ + p_191110_4_.getFrontOffsetX();
                        final int k = p_191110_3_ + p_191110_4_.getFrontOffsetZ();
                        if (p_191110_1_.func_191145_a(j + enumfacing.getFrontOffsetX(), k + enumfacing.getFrontOffsetZ()) == 0 && p_191110_1_.func_191145_a(j + enumfacing.getFrontOffsetX() * 2, k + enumfacing.getFrontOffsetZ() * 2) == 0) {
                            this.func_191110_a(p_191110_1_, p_191110_2_ + p_191110_4_.getFrontOffsetX() + enumfacing.getFrontOffsetX(), p_191110_3_ + p_191110_4_.getFrontOffsetZ() + enumfacing.getFrontOffsetZ(), enumfacing, p_191110_5_ - 1);
                            break;
                        }
                    }
                }
                final EnumFacing enumfacing2 = p_191110_4_.rotateY();
                final EnumFacing enumfacing3 = p_191110_4_.rotateYCCW();
                p_191110_1_.func_191141_a(p_191110_2_ + enumfacing2.getFrontOffsetX(), p_191110_3_ + enumfacing2.getFrontOffsetZ(), 0, 2);
                p_191110_1_.func_191141_a(p_191110_2_ + enumfacing3.getFrontOffsetX(), p_191110_3_ + enumfacing3.getFrontOffsetZ(), 0, 2);
                p_191110_1_.func_191141_a(p_191110_2_ + p_191110_4_.getFrontOffsetX() + enumfacing2.getFrontOffsetX(), p_191110_3_ + p_191110_4_.getFrontOffsetZ() + enumfacing2.getFrontOffsetZ(), 0, 2);
                p_191110_1_.func_191141_a(p_191110_2_ + p_191110_4_.getFrontOffsetX() + enumfacing3.getFrontOffsetX(), p_191110_3_ + p_191110_4_.getFrontOffsetZ() + enumfacing3.getFrontOffsetZ(), 0, 2);
                p_191110_1_.func_191141_a(p_191110_2_ + p_191110_4_.getFrontOffsetX() * 2, p_191110_3_ + p_191110_4_.getFrontOffsetZ() * 2, 0, 2);
                p_191110_1_.func_191141_a(p_191110_2_ + enumfacing2.getFrontOffsetX() * 2, p_191110_3_ + enumfacing2.getFrontOffsetZ() * 2, 0, 2);
                p_191110_1_.func_191141_a(p_191110_2_ + enumfacing3.getFrontOffsetX() * 2, p_191110_3_ + enumfacing3.getFrontOffsetZ() * 2, 0, 2);
            }
        }
        
        private boolean func_191111_a(final SimpleGrid p_191111_1_) {
            boolean flag = false;
            for (int i = 0; i < p_191111_1_.field_191150_c; ++i) {
                for (int j = 0; j < p_191111_1_.field_191149_b; ++j) {
                    if (p_191111_1_.func_191145_a(j, i) == 0) {
                        int k = 0;
                        k += (func_191109_a(p_191111_1_, j + 1, i) ? 1 : 0);
                        k += (func_191109_a(p_191111_1_, j - 1, i) ? 1 : 0);
                        k += (func_191109_a(p_191111_1_, j, i + 1) ? 1 : 0);
                        k += (func_191109_a(p_191111_1_, j, i - 1) ? 1 : 0);
                        if (k >= 3) {
                            p_191111_1_.func_191144_a(j, i, 2);
                            flag = true;
                        }
                        else if (k == 2) {
                            int l = 0;
                            l += (func_191109_a(p_191111_1_, j + 1, i + 1) ? 1 : 0);
                            l += (func_191109_a(p_191111_1_, j - 1, i + 1) ? 1 : 0);
                            l += (func_191109_a(p_191111_1_, j + 1, i - 1) ? 1 : 0);
                            l += (func_191109_a(p_191111_1_, j - 1, i - 1) ? 1 : 0);
                            if (l <= 1) {
                                p_191111_1_.func_191144_a(j, i, 2);
                                flag = true;
                            }
                        }
                    }
                }
            }
            return flag;
        }
        
        private void func_191115_b() {
            final List<Tuple<Integer, Integer>> list = (List<Tuple<Integer, Integer>>)Lists.newArrayList();
            final SimpleGrid woodlandmansionpieces$simplegrid = this.field_191120_d[1];
            for (int i = 0; i < this.field_191119_c.field_191150_c; ++i) {
                for (int j = 0; j < this.field_191119_c.field_191149_b; ++j) {
                    final int k = woodlandmansionpieces$simplegrid.func_191145_a(j, i);
                    final int l = k & 0xF0000;
                    if (l == 131072 && (k & 0x200000) == 0x200000) {
                        list.add(new Tuple<Integer, Integer>(j, i));
                    }
                }
            }
            if (list.isEmpty()) {
                this.field_191119_c.func_191142_a(0, 0, this.field_191119_c.field_191149_b, this.field_191119_c.field_191150_c, 5);
            }
            else {
                final Tuple<Integer, Integer> tuple = list.get(this.field_191117_a.nextInt(list.size()));
                final int l2 = woodlandmansionpieces$simplegrid.func_191145_a(tuple.getFirst(), tuple.getSecond());
                woodlandmansionpieces$simplegrid.func_191144_a(tuple.getFirst(), tuple.getSecond(), l2 | 0x400000);
                final EnumFacing enumfacing1 = this.func_191113_b(this.field_191118_b, tuple.getFirst(), tuple.getSecond(), 1, l2 & 0xFFFF);
                final int i2 = tuple.getFirst() + enumfacing1.getFrontOffsetX();
                final int i3 = tuple.getSecond() + enumfacing1.getFrontOffsetZ();
                for (int j2 = 0; j2 < this.field_191119_c.field_191150_c; ++j2) {
                    for (int k2 = 0; k2 < this.field_191119_c.field_191149_b; ++k2) {
                        if (!func_191109_a(this.field_191118_b, k2, j2)) {
                            this.field_191119_c.func_191144_a(k2, j2, 5);
                        }
                        else if (k2 == tuple.getFirst() && j2 == tuple.getSecond()) {
                            this.field_191119_c.func_191144_a(k2, j2, 3);
                        }
                        else if (k2 == i2 && j2 == i3) {
                            this.field_191119_c.func_191144_a(k2, j2, 3);
                            this.field_191120_d[2].func_191144_a(k2, j2, 8388608);
                        }
                    }
                }
                final List<EnumFacing> list2 = (List<EnumFacing>)Lists.newArrayList();
                EnumFacing[] facings;
                for (int length = (facings = EnumFacing.Plane.HORIZONTAL.facings()).length, n = 0; n < length; ++n) {
                    final EnumFacing enumfacing2 = facings[n];
                    if (this.field_191119_c.func_191145_a(i2 + enumfacing2.getFrontOffsetX(), i3 + enumfacing2.getFrontOffsetZ()) == 0) {
                        list2.add(enumfacing2);
                    }
                }
                if (list2.isEmpty()) {
                    this.field_191119_c.func_191142_a(0, 0, this.field_191119_c.field_191149_b, this.field_191119_c.field_191150_c, 5);
                    woodlandmansionpieces$simplegrid.func_191144_a(tuple.getFirst(), tuple.getSecond(), l2);
                }
                else {
                    final EnumFacing enumfacing3 = list2.get(this.field_191117_a.nextInt(list2.size()));
                    this.func_191110_a(this.field_191119_c, i2 + enumfacing3.getFrontOffsetX(), i3 + enumfacing3.getFrontOffsetZ(), enumfacing3, 4);
                    while (this.func_191111_a(this.field_191119_c)) {}
                }
            }
        }
        
        private void func_191116_a(final SimpleGrid p_191116_1_, final SimpleGrid p_191116_2_) {
            final List<Tuple<Integer, Integer>> list = (List<Tuple<Integer, Integer>>)Lists.newArrayList();
            for (int i = 0; i < p_191116_1_.field_191150_c; ++i) {
                for (int j = 0; j < p_191116_1_.field_191149_b; ++j) {
                    if (p_191116_1_.func_191145_a(j, i) == 2) {
                        list.add(new Tuple<Integer, Integer>(j, i));
                    }
                }
            }
            Collections.shuffle(list, this.field_191117_a);
            int k3 = 10;
            for (final Tuple<Integer, Integer> tuple : list) {
                final int l = tuple.getFirst();
                final int m = tuple.getSecond();
                if (p_191116_2_.func_191145_a(l, m) == 0) {
                    int i2 = l;
                    int j2 = l;
                    int k4 = m;
                    int l2 = m;
                    int i3 = 65536;
                    if (p_191116_2_.func_191145_a(l + 1, m) == 0 && p_191116_2_.func_191145_a(l, m + 1) == 0 && p_191116_2_.func_191145_a(l + 1, m + 1) == 0 && p_191116_1_.func_191145_a(l + 1, m) == 2 && p_191116_1_.func_191145_a(l, m + 1) == 2 && p_191116_1_.func_191145_a(l + 1, m + 1) == 2) {
                        j2 = l + 1;
                        l2 = m + 1;
                        i3 = 262144;
                    }
                    else if (p_191116_2_.func_191145_a(l - 1, m) == 0 && p_191116_2_.func_191145_a(l, m + 1) == 0 && p_191116_2_.func_191145_a(l - 1, m + 1) == 0 && p_191116_1_.func_191145_a(l - 1, m) == 2 && p_191116_1_.func_191145_a(l, m + 1) == 2 && p_191116_1_.func_191145_a(l - 1, m + 1) == 2) {
                        i2 = l - 1;
                        l2 = m + 1;
                        i3 = 262144;
                    }
                    else if (p_191116_2_.func_191145_a(l - 1, m) == 0 && p_191116_2_.func_191145_a(l, m - 1) == 0 && p_191116_2_.func_191145_a(l - 1, m - 1) == 0 && p_191116_1_.func_191145_a(l - 1, m) == 2 && p_191116_1_.func_191145_a(l, m - 1) == 2 && p_191116_1_.func_191145_a(l - 1, m - 1) == 2) {
                        i2 = l - 1;
                        k4 = m - 1;
                        i3 = 262144;
                    }
                    else if (p_191116_2_.func_191145_a(l + 1, m) == 0 && p_191116_1_.func_191145_a(l + 1, m) == 2) {
                        j2 = l + 1;
                        i3 = 131072;
                    }
                    else if (p_191116_2_.func_191145_a(l, m + 1) == 0 && p_191116_1_.func_191145_a(l, m + 1) == 2) {
                        l2 = m + 1;
                        i3 = 131072;
                    }
                    else if (p_191116_2_.func_191145_a(l - 1, m) == 0 && p_191116_1_.func_191145_a(l - 1, m) == 2) {
                        i2 = l - 1;
                        i3 = 131072;
                    }
                    else if (p_191116_2_.func_191145_a(l, m - 1) == 0 && p_191116_1_.func_191145_a(l, m - 1) == 2) {
                        k4 = m - 1;
                        i3 = 131072;
                    }
                    int j3 = this.field_191117_a.nextBoolean() ? i2 : j2;
                    int k5 = this.field_191117_a.nextBoolean() ? k4 : l2;
                    int l3 = 2097152;
                    if (!p_191116_1_.func_191147_b(j3, k5, 1)) {
                        j3 = ((j3 == i2) ? j2 : i2);
                        k5 = ((k5 == k4) ? l2 : k4);
                        if (!p_191116_1_.func_191147_b(j3, k5, 1)) {
                            k5 = ((k5 == k4) ? l2 : k4);
                            if (!p_191116_1_.func_191147_b(j3, k5, 1)) {
                                j3 = ((j3 == i2) ? j2 : i2);
                                k5 = ((k5 == k4) ? l2 : k4);
                                if (!p_191116_1_.func_191147_b(j3, k5, 1)) {
                                    l3 = 0;
                                    j3 = i2;
                                    k5 = k4;
                                }
                            }
                        }
                    }
                    for (int i4 = k4; i4 <= l2; ++i4) {
                        for (int j4 = i2; j4 <= j2; ++j4) {
                            if (j4 == j3 && i4 == k5) {
                                p_191116_2_.func_191144_a(j4, i4, 0x100000 | l3 | i3 | k3);
                            }
                            else {
                                p_191116_2_.func_191144_a(j4, i4, i3 | k3);
                            }
                        }
                    }
                    ++k3;
                }
            }
        }
    }
    
    public static class MansionTemplate extends StructureComponentTemplate
    {
        private String field_191082_d;
        private Rotation field_191083_e;
        private Mirror field_191084_f;
        
        public MansionTemplate() {
        }
        
        public MansionTemplate(final TemplateManager p_i47355_1_, final String p_i47355_2_, final BlockPos p_i47355_3_, final Rotation p_i47355_4_) {
            this(p_i47355_1_, p_i47355_2_, p_i47355_3_, p_i47355_4_, Mirror.NONE);
        }
        
        public MansionTemplate(final TemplateManager p_i47356_1_, final String p_i47356_2_, final BlockPos p_i47356_3_, final Rotation p_i47356_4_, final Mirror p_i47356_5_) {
            super(0);
            this.field_191082_d = p_i47356_2_;
            this.templatePosition = p_i47356_3_;
            this.field_191083_e = p_i47356_4_;
            this.field_191084_f = p_i47356_5_;
            this.func_191081_a(p_i47356_1_);
        }
        
        private void func_191081_a(final TemplateManager p_191081_1_) {
            final Template template = p_191081_1_.getTemplate(null, new ResourceLocation("mansion/" + this.field_191082_d));
            final PlacementSettings placementsettings = new PlacementSettings().setIgnoreEntities(true).setRotation(this.field_191083_e).setMirror(this.field_191084_f);
            this.setup(template, this.templatePosition, placementsettings);
        }
        
        @Override
        protected void writeStructureToNBT(final NBTTagCompound tagCompound) {
            super.writeStructureToNBT(tagCompound);
            tagCompound.setString("Template", this.field_191082_d);
            tagCompound.setString("Rot", this.placeSettings.getRotation().name());
            tagCompound.setString("Mi", this.placeSettings.getMirror().name());
        }
        
        @Override
        protected void readStructureFromNBT(final NBTTagCompound tagCompound, final TemplateManager p_143011_2_) {
            super.readStructureFromNBT(tagCompound, p_143011_2_);
            this.field_191082_d = tagCompound.getString("Template");
            this.field_191083_e = Rotation.valueOf(tagCompound.getString("Rot"));
            this.field_191084_f = Mirror.valueOf(tagCompound.getString("Mi"));
            this.func_191081_a(p_143011_2_);
        }
        
        @Override
        protected void handleDataMarker(final String p_186175_1_, final BlockPos p_186175_2_, final World p_186175_3_, final Random p_186175_4_, final StructureBoundingBox p_186175_5_) {
            if (p_186175_1_.startsWith("Chest")) {
                final Rotation rotation = this.placeSettings.getRotation();
                IBlockState iblockstate = Blocks.CHEST.getDefaultState();
                if ("ChestWest".equals(p_186175_1_)) {
                    iblockstate = iblockstate.withProperty((IProperty<Comparable>)BlockChest.FACING, rotation.rotate(EnumFacing.WEST));
                }
                else if ("ChestEast".equals(p_186175_1_)) {
                    iblockstate = iblockstate.withProperty((IProperty<Comparable>)BlockChest.FACING, rotation.rotate(EnumFacing.EAST));
                }
                else if ("ChestSouth".equals(p_186175_1_)) {
                    iblockstate = iblockstate.withProperty((IProperty<Comparable>)BlockChest.FACING, rotation.rotate(EnumFacing.SOUTH));
                }
                else if ("ChestNorth".equals(p_186175_1_)) {
                    iblockstate = iblockstate.withProperty((IProperty<Comparable>)BlockChest.FACING, rotation.rotate(EnumFacing.NORTH));
                }
                this.func_191080_a(p_186175_3_, p_186175_5_, p_186175_4_, p_186175_2_, LootTableList.field_191192_o, iblockstate);
            }
            else if ("Mage".equals(p_186175_1_)) {
                final EntityEvoker entityevoker = new EntityEvoker(p_186175_3_);
                entityevoker.enablePersistence();
                entityevoker.moveToBlockPosAndAngles(p_186175_2_, 0.0f, 0.0f);
                p_186175_3_.spawnEntityInWorld(entityevoker);
                p_186175_3_.setBlockState(p_186175_2_, Blocks.AIR.getDefaultState(), 2);
            }
            else if ("Warrior".equals(p_186175_1_)) {
                final EntityVindicator entityvindicator = new EntityVindicator(p_186175_3_);
                entityvindicator.enablePersistence();
                entityvindicator.moveToBlockPosAndAngles(p_186175_2_, 0.0f, 0.0f);
                entityvindicator.onInitialSpawn(p_186175_3_.getDifficultyForLocation(new BlockPos(entityvindicator)), null);
                p_186175_3_.spawnEntityInWorld(entityvindicator);
                p_186175_3_.setBlockState(p_186175_2_, Blocks.AIR.getDefaultState(), 2);
            }
        }
    }
    
    static class PlacementData
    {
        public Rotation field_191138_a;
        public BlockPos field_191139_b;
        public String field_191140_c;
        
        private PlacementData() {
        }
    }
    
    static class Placer
    {
        private final TemplateManager field_191134_a;
        private final Random field_191135_b;
        private int field_191136_c;
        private int field_191137_d;
        
        public Placer(final TemplateManager p_i47361_1_, final Random p_i47361_2_) {
            this.field_191134_a = p_i47361_1_;
            this.field_191135_b = p_i47361_2_;
        }
        
        public void func_191125_a(final BlockPos p_191125_1_, final Rotation p_191125_2_, final List<MansionTemplate> p_191125_3_, final Grid p_191125_4_) {
            final PlacementData woodlandmansionpieces$placementdata = new PlacementData(null);
            woodlandmansionpieces$placementdata.field_191139_b = p_191125_1_;
            woodlandmansionpieces$placementdata.field_191138_a = p_191125_2_;
            woodlandmansionpieces$placementdata.field_191140_c = "wall_flat";
            final PlacementData woodlandmansionpieces$placementdata2 = new PlacementData(null);
            this.func_191133_a(p_191125_3_, woodlandmansionpieces$placementdata);
            woodlandmansionpieces$placementdata2.field_191139_b = woodlandmansionpieces$placementdata.field_191139_b.up(8);
            woodlandmansionpieces$placementdata2.field_191138_a = woodlandmansionpieces$placementdata.field_191138_a;
            woodlandmansionpieces$placementdata2.field_191140_c = "wall_window";
            if (!p_191125_3_.isEmpty()) {}
            final SimpleGrid woodlandmansionpieces$simplegrid = p_191125_4_.field_191118_b;
            final SimpleGrid woodlandmansionpieces$simplegrid2 = p_191125_4_.field_191119_c;
            this.field_191136_c = p_191125_4_.field_191121_e + 1;
            this.field_191137_d = p_191125_4_.field_191122_f + 1;
            final int i = p_191125_4_.field_191121_e + 1;
            final int j = p_191125_4_.field_191122_f;
            this.func_191130_a(p_191125_3_, woodlandmansionpieces$placementdata, woodlandmansionpieces$simplegrid, EnumFacing.SOUTH, this.field_191136_c, this.field_191137_d, i, j);
            this.func_191130_a(p_191125_3_, woodlandmansionpieces$placementdata2, woodlandmansionpieces$simplegrid, EnumFacing.SOUTH, this.field_191136_c, this.field_191137_d, i, j);
            final PlacementData woodlandmansionpieces$placementdata3 = new PlacementData(null);
            woodlandmansionpieces$placementdata3.field_191139_b = woodlandmansionpieces$placementdata.field_191139_b.up(19);
            woodlandmansionpieces$placementdata3.field_191138_a = woodlandmansionpieces$placementdata.field_191138_a;
            woodlandmansionpieces$placementdata3.field_191140_c = "wall_window";
            boolean flag = false;
            for (int k = 0; k < woodlandmansionpieces$simplegrid2.field_191150_c && !flag; ++k) {
                for (int l = woodlandmansionpieces$simplegrid2.field_191149_b - 1; l >= 0 && !flag; --l) {
                    if (Grid.func_191109_a(woodlandmansionpieces$simplegrid2, l, k)) {
                        woodlandmansionpieces$placementdata3.field_191139_b = woodlandmansionpieces$placementdata3.field_191139_b.offset(p_191125_2_.rotate(EnumFacing.SOUTH), 8 + (k - this.field_191137_d) * 8);
                        woodlandmansionpieces$placementdata3.field_191139_b = woodlandmansionpieces$placementdata3.field_191139_b.offset(p_191125_2_.rotate(EnumFacing.EAST), (l - this.field_191136_c) * 8);
                        this.func_191131_b(p_191125_3_, woodlandmansionpieces$placementdata3);
                        this.func_191130_a(p_191125_3_, woodlandmansionpieces$placementdata3, woodlandmansionpieces$simplegrid2, EnumFacing.SOUTH, l, k, l, k);
                        flag = true;
                    }
                }
            }
            this.func_191123_a(p_191125_3_, p_191125_1_.up(16), p_191125_2_, woodlandmansionpieces$simplegrid, woodlandmansionpieces$simplegrid2);
            this.func_191123_a(p_191125_3_, p_191125_1_.up(27), p_191125_2_, woodlandmansionpieces$simplegrid2, null);
            if (!p_191125_3_.isEmpty()) {}
            final RoomCollection[] awoodlandmansionpieces$roomcollection = { new FirstFloor(null), new SecondFloor(null), new ThirdFloor(null) };
            for (int l2 = 0; l2 < 3; ++l2) {
                final BlockPos blockpos = p_191125_1_.up(8 * l2 + ((l2 == 2) ? 3 : 0));
                final SimpleGrid woodlandmansionpieces$simplegrid3 = p_191125_4_.field_191120_d[l2];
                final SimpleGrid woodlandmansionpieces$simplegrid4 = (l2 == 2) ? woodlandmansionpieces$simplegrid2 : woodlandmansionpieces$simplegrid;
                final String s = (l2 == 0) ? "carpet_south" : "carpet_south_2";
                final String s2 = (l2 == 0) ? "carpet_west" : "carpet_west_2";
                for (int i2 = 0; i2 < woodlandmansionpieces$simplegrid4.field_191150_c; ++i2) {
                    for (int j2 = 0; j2 < woodlandmansionpieces$simplegrid4.field_191149_b; ++j2) {
                        if (woodlandmansionpieces$simplegrid4.func_191145_a(j2, i2) == 1) {
                            BlockPos blockpos2 = blockpos.offset(p_191125_2_.rotate(EnumFacing.SOUTH), 8 + (i2 - this.field_191137_d) * 8);
                            blockpos2 = blockpos2.offset(p_191125_2_.rotate(EnumFacing.EAST), (j2 - this.field_191136_c) * 8);
                            p_191125_3_.add(new MansionTemplate(this.field_191134_a, "corridor_floor", blockpos2, p_191125_2_));
                            if (woodlandmansionpieces$simplegrid4.func_191145_a(j2, i2 - 1) == 1 || (woodlandmansionpieces$simplegrid3.func_191145_a(j2, i2 - 1) & 0x800000) == 0x800000) {
                                p_191125_3_.add(new MansionTemplate(this.field_191134_a, "carpet_north", blockpos2.offset(p_191125_2_.rotate(EnumFacing.EAST), 1).up(), p_191125_2_));
                            }
                            if (woodlandmansionpieces$simplegrid4.func_191145_a(j2 + 1, i2) == 1 || (woodlandmansionpieces$simplegrid3.func_191145_a(j2 + 1, i2) & 0x800000) == 0x800000) {
                                p_191125_3_.add(new MansionTemplate(this.field_191134_a, "carpet_east", blockpos2.offset(p_191125_2_.rotate(EnumFacing.SOUTH), 1).offset(p_191125_2_.rotate(EnumFacing.EAST), 5).up(), p_191125_2_));
                            }
                            if (woodlandmansionpieces$simplegrid4.func_191145_a(j2, i2 + 1) == 1 || (woodlandmansionpieces$simplegrid3.func_191145_a(j2, i2 + 1) & 0x800000) == 0x800000) {
                                p_191125_3_.add(new MansionTemplate(this.field_191134_a, s, blockpos2.offset(p_191125_2_.rotate(EnumFacing.SOUTH), 5).offset(p_191125_2_.rotate(EnumFacing.WEST), 1), p_191125_2_));
                            }
                            if (woodlandmansionpieces$simplegrid4.func_191145_a(j2 - 1, i2) == 1 || (woodlandmansionpieces$simplegrid3.func_191145_a(j2 - 1, i2) & 0x800000) == 0x800000) {
                                p_191125_3_.add(new MansionTemplate(this.field_191134_a, s2, blockpos2.offset(p_191125_2_.rotate(EnumFacing.WEST), 1).offset(p_191125_2_.rotate(EnumFacing.NORTH), 1), p_191125_2_));
                            }
                        }
                    }
                }
                final String s3 = (l2 == 0) ? "indoors_wall" : "indoors_wall_2";
                final String s4 = (l2 == 0) ? "indoors_door" : "indoors_door_2";
                final List<EnumFacing> list = (List<EnumFacing>)Lists.newArrayList();
                for (int k2 = 0; k2 < woodlandmansionpieces$simplegrid4.field_191150_c; ++k2) {
                    for (int l3 = 0; l3 < woodlandmansionpieces$simplegrid4.field_191149_b; ++l3) {
                        boolean flag2 = l2 == 2 && woodlandmansionpieces$simplegrid4.func_191145_a(l3, k2) == 3;
                        if (woodlandmansionpieces$simplegrid4.func_191145_a(l3, k2) == 2 || flag2) {
                            final int i3 = woodlandmansionpieces$simplegrid3.func_191145_a(l3, k2);
                            final int j3 = i3 & 0xF0000;
                            final int k3 = i3 & 0xFFFF;
                            flag2 = (flag2 && (i3 & 0x800000) == 0x800000);
                            list.clear();
                            if ((i3 & 0x200000) == 0x200000) {
                                EnumFacing[] facings;
                                for (int length = (facings = EnumFacing.Plane.HORIZONTAL.facings()).length, n = 0; n < length; ++n) {
                                    final EnumFacing enumfacing = facings[n];
                                    if (woodlandmansionpieces$simplegrid4.func_191145_a(l3 + enumfacing.getFrontOffsetX(), k2 + enumfacing.getFrontOffsetZ()) == 1) {
                                        list.add(enumfacing);
                                    }
                                }
                            }
                            EnumFacing enumfacing2 = null;
                            if (!list.isEmpty()) {
                                enumfacing2 = list.get(this.field_191135_b.nextInt(list.size()));
                            }
                            else if ((i3 & 0x100000) == 0x100000) {
                                enumfacing2 = EnumFacing.UP;
                            }
                            BlockPos blockpos3 = blockpos.offset(p_191125_2_.rotate(EnumFacing.SOUTH), 8 + (k2 - this.field_191137_d) * 8);
                            blockpos3 = blockpos3.offset(p_191125_2_.rotate(EnumFacing.EAST), -1 + (l3 - this.field_191136_c) * 8);
                            if (Grid.func_191109_a(woodlandmansionpieces$simplegrid4, l3 - 1, k2) && !p_191125_4_.func_191114_a(woodlandmansionpieces$simplegrid4, l3 - 1, k2, l2, k3)) {
                                p_191125_3_.add(new MansionTemplate(this.field_191134_a, (enumfacing2 == EnumFacing.WEST) ? s4 : s3, blockpos3, p_191125_2_));
                            }
                            if (woodlandmansionpieces$simplegrid4.func_191145_a(l3 + 1, k2) == 1 && !flag2) {
                                final BlockPos blockpos4 = blockpos3.offset(p_191125_2_.rotate(EnumFacing.EAST), 8);
                                p_191125_3_.add(new MansionTemplate(this.field_191134_a, (enumfacing2 == EnumFacing.EAST) ? s4 : s3, blockpos4, p_191125_2_));
                            }
                            if (Grid.func_191109_a(woodlandmansionpieces$simplegrid4, l3, k2 + 1) && !p_191125_4_.func_191114_a(woodlandmansionpieces$simplegrid4, l3, k2 + 1, l2, k3)) {
                                BlockPos blockpos5 = blockpos3.offset(p_191125_2_.rotate(EnumFacing.SOUTH), 7);
                                blockpos5 = blockpos5.offset(p_191125_2_.rotate(EnumFacing.EAST), 7);
                                p_191125_3_.add(new MansionTemplate(this.field_191134_a, (enumfacing2 == EnumFacing.SOUTH) ? s4 : s3, blockpos5, p_191125_2_.add(Rotation.CLOCKWISE_90)));
                            }
                            if (woodlandmansionpieces$simplegrid4.func_191145_a(l3, k2 - 1) == 1 && !flag2) {
                                BlockPos blockpos6 = blockpos3.offset(p_191125_2_.rotate(EnumFacing.NORTH), 1);
                                blockpos6 = blockpos6.offset(p_191125_2_.rotate(EnumFacing.EAST), 7);
                                p_191125_3_.add(new MansionTemplate(this.field_191134_a, (enumfacing2 == EnumFacing.NORTH) ? s4 : s3, blockpos6, p_191125_2_.add(Rotation.CLOCKWISE_90)));
                            }
                            if (j3 == 65536) {
                                this.func_191129_a(p_191125_3_, blockpos3, p_191125_2_, enumfacing2, awoodlandmansionpieces$roomcollection[l2]);
                            }
                            else if (j3 == 131072 && enumfacing2 != null) {
                                final EnumFacing enumfacing3 = p_191125_4_.func_191113_b(woodlandmansionpieces$simplegrid4, l3, k2, l2, k3);
                                final boolean flag3 = (i3 & 0x400000) == 0x400000;
                                this.func_191132_a(p_191125_3_, blockpos3, p_191125_2_, enumfacing3, enumfacing2, awoodlandmansionpieces$roomcollection[l2], flag3);
                            }
                            else if (j3 == 262144 && enumfacing2 != null && enumfacing2 != EnumFacing.UP) {
                                EnumFacing enumfacing4 = enumfacing2.rotateY();
                                if (!p_191125_4_.func_191114_a(woodlandmansionpieces$simplegrid4, l3 + enumfacing4.getFrontOffsetX(), k2 + enumfacing4.getFrontOffsetZ(), l2, k3)) {
                                    enumfacing4 = enumfacing4.getOpposite();
                                }
                                this.func_191127_a(p_191125_3_, blockpos3, p_191125_2_, enumfacing4, enumfacing2, awoodlandmansionpieces$roomcollection[l2]);
                            }
                            else if (j3 == 262144 && enumfacing2 == EnumFacing.UP) {
                                this.func_191128_a(p_191125_3_, blockpos3, p_191125_2_, awoodlandmansionpieces$roomcollection[l2]);
                            }
                        }
                    }
                }
            }
        }
        
        private void func_191130_a(final List<MansionTemplate> p_191130_1_, final PlacementData p_191130_2_, final SimpleGrid p_191130_3_, EnumFacing p_191130_4_, final int p_191130_5_, final int p_191130_6_, final int p_191130_7_, final int p_191130_8_) {
            int i = p_191130_5_;
            int j = p_191130_6_;
            final EnumFacing enumfacing = p_191130_4_;
            do {
                if (!Grid.func_191109_a(p_191130_3_, i + p_191130_4_.getFrontOffsetX(), j + p_191130_4_.getFrontOffsetZ())) {
                    this.func_191124_c(p_191130_1_, p_191130_2_);
                    p_191130_4_ = p_191130_4_.rotateY();
                    if (i == p_191130_7_ && j == p_191130_8_ && enumfacing == p_191130_4_) {
                        continue;
                    }
                    this.func_191131_b(p_191130_1_, p_191130_2_);
                }
                else if (Grid.func_191109_a(p_191130_3_, i + p_191130_4_.getFrontOffsetX(), j + p_191130_4_.getFrontOffsetZ()) && Grid.func_191109_a(p_191130_3_, i + p_191130_4_.getFrontOffsetX() + p_191130_4_.rotateYCCW().getFrontOffsetX(), j + p_191130_4_.getFrontOffsetZ() + p_191130_4_.rotateYCCW().getFrontOffsetZ())) {
                    this.func_191126_d(p_191130_1_, p_191130_2_);
                    i += p_191130_4_.getFrontOffsetX();
                    j += p_191130_4_.getFrontOffsetZ();
                    p_191130_4_ = p_191130_4_.rotateYCCW();
                }
                else {
                    i += p_191130_4_.getFrontOffsetX();
                    j += p_191130_4_.getFrontOffsetZ();
                    if (i == p_191130_7_ && j == p_191130_8_ && enumfacing == p_191130_4_) {
                        continue;
                    }
                    this.func_191131_b(p_191130_1_, p_191130_2_);
                }
            } while (i != p_191130_7_ || j != p_191130_8_ || enumfacing != p_191130_4_);
        }
        
        private void func_191123_a(final List<MansionTemplate> p_191123_1_, final BlockPos p_191123_2_, final Rotation p_191123_3_, final SimpleGrid p_191123_4_, @Nullable final SimpleGrid p_191123_5_) {
            for (int i = 0; i < p_191123_4_.field_191150_c; ++i) {
                for (int j = 0; j < p_191123_4_.field_191149_b; ++j) {
                    BlockPos lvt_8_3_ = p_191123_2_.offset(p_191123_3_.rotate(EnumFacing.SOUTH), 8 + (i - this.field_191137_d) * 8);
                    lvt_8_3_ = lvt_8_3_.offset(p_191123_3_.rotate(EnumFacing.EAST), (j - this.field_191136_c) * 8);
                    final boolean flag = p_191123_5_ != null && Grid.func_191109_a(p_191123_5_, j, i);
                    if (Grid.func_191109_a(p_191123_4_, j, i) && !flag) {
                        p_191123_1_.add(new MansionTemplate(this.field_191134_a, "roof", lvt_8_3_.up(3), p_191123_3_));
                        if (!Grid.func_191109_a(p_191123_4_, j + 1, i)) {
                            final BlockPos blockpos1 = lvt_8_3_.offset(p_191123_3_.rotate(EnumFacing.EAST), 6);
                            p_191123_1_.add(new MansionTemplate(this.field_191134_a, "roof_front", blockpos1, p_191123_3_));
                        }
                        if (!Grid.func_191109_a(p_191123_4_, j - 1, i)) {
                            BlockPos blockpos2 = lvt_8_3_.offset(p_191123_3_.rotate(EnumFacing.EAST), 0);
                            blockpos2 = blockpos2.offset(p_191123_3_.rotate(EnumFacing.SOUTH), 7);
                            p_191123_1_.add(new MansionTemplate(this.field_191134_a, "roof_front", blockpos2, p_191123_3_.add(Rotation.CLOCKWISE_180)));
                        }
                        if (!Grid.func_191109_a(p_191123_4_, j, i - 1)) {
                            final BlockPos blockpos3 = lvt_8_3_.offset(p_191123_3_.rotate(EnumFacing.WEST), 1);
                            p_191123_1_.add(new MansionTemplate(this.field_191134_a, "roof_front", blockpos3, p_191123_3_.add(Rotation.COUNTERCLOCKWISE_90)));
                        }
                        if (!Grid.func_191109_a(p_191123_4_, j, i + 1)) {
                            BlockPos blockpos4 = lvt_8_3_.offset(p_191123_3_.rotate(EnumFacing.EAST), 6);
                            blockpos4 = blockpos4.offset(p_191123_3_.rotate(EnumFacing.SOUTH), 6);
                            p_191123_1_.add(new MansionTemplate(this.field_191134_a, "roof_front", blockpos4, p_191123_3_.add(Rotation.CLOCKWISE_90)));
                        }
                    }
                }
            }
            if (p_191123_5_ != null) {
                for (int k = 0; k < p_191123_4_.field_191150_c; ++k) {
                    for (int i2 = 0; i2 < p_191123_4_.field_191149_b; ++i2) {
                        BlockPos blockpos5 = p_191123_2_.offset(p_191123_3_.rotate(EnumFacing.SOUTH), 8 + (k - this.field_191137_d) * 8);
                        blockpos5 = blockpos5.offset(p_191123_3_.rotate(EnumFacing.EAST), (i2 - this.field_191136_c) * 8);
                        final boolean flag2 = Grid.func_191109_a(p_191123_5_, i2, k);
                        if (Grid.func_191109_a(p_191123_4_, i2, k) && flag2) {
                            if (!Grid.func_191109_a(p_191123_4_, i2 + 1, k)) {
                                final BlockPos blockpos6 = blockpos5.offset(p_191123_3_.rotate(EnumFacing.EAST), 7);
                                p_191123_1_.add(new MansionTemplate(this.field_191134_a, "small_wall", blockpos6, p_191123_3_));
                            }
                            if (!Grid.func_191109_a(p_191123_4_, i2 - 1, k)) {
                                BlockPos blockpos7 = blockpos5.offset(p_191123_3_.rotate(EnumFacing.WEST), 1);
                                blockpos7 = blockpos7.offset(p_191123_3_.rotate(EnumFacing.SOUTH), 6);
                                p_191123_1_.add(new MansionTemplate(this.field_191134_a, "small_wall", blockpos7, p_191123_3_.add(Rotation.CLOCKWISE_180)));
                            }
                            if (!Grid.func_191109_a(p_191123_4_, i2, k - 1)) {
                                BlockPos blockpos8 = blockpos5.offset(p_191123_3_.rotate(EnumFacing.WEST), 0);
                                blockpos8 = blockpos8.offset(p_191123_3_.rotate(EnumFacing.NORTH), 1);
                                p_191123_1_.add(new MansionTemplate(this.field_191134_a, "small_wall", blockpos8, p_191123_3_.add(Rotation.COUNTERCLOCKWISE_90)));
                            }
                            if (!Grid.func_191109_a(p_191123_4_, i2, k + 1)) {
                                BlockPos blockpos9 = blockpos5.offset(p_191123_3_.rotate(EnumFacing.EAST), 6);
                                blockpos9 = blockpos9.offset(p_191123_3_.rotate(EnumFacing.SOUTH), 7);
                                p_191123_1_.add(new MansionTemplate(this.field_191134_a, "small_wall", blockpos9, p_191123_3_.add(Rotation.CLOCKWISE_90)));
                            }
                            if (!Grid.func_191109_a(p_191123_4_, i2 + 1, k)) {
                                if (!Grid.func_191109_a(p_191123_4_, i2, k - 1)) {
                                    BlockPos blockpos10 = blockpos5.offset(p_191123_3_.rotate(EnumFacing.EAST), 7);
                                    blockpos10 = blockpos10.offset(p_191123_3_.rotate(EnumFacing.NORTH), 2);
                                    p_191123_1_.add(new MansionTemplate(this.field_191134_a, "small_wall_corner", blockpos10, p_191123_3_));
                                }
                                if (!Grid.func_191109_a(p_191123_4_, i2, k + 1)) {
                                    BlockPos blockpos11 = blockpos5.offset(p_191123_3_.rotate(EnumFacing.EAST), 8);
                                    blockpos11 = blockpos11.offset(p_191123_3_.rotate(EnumFacing.SOUTH), 7);
                                    p_191123_1_.add(new MansionTemplate(this.field_191134_a, "small_wall_corner", blockpos11, p_191123_3_.add(Rotation.CLOCKWISE_90)));
                                }
                            }
                            if (!Grid.func_191109_a(p_191123_4_, i2 - 1, k)) {
                                if (!Grid.func_191109_a(p_191123_4_, i2, k - 1)) {
                                    BlockPos blockpos12 = blockpos5.offset(p_191123_3_.rotate(EnumFacing.WEST), 2);
                                    blockpos12 = blockpos12.offset(p_191123_3_.rotate(EnumFacing.NORTH), 1);
                                    p_191123_1_.add(new MansionTemplate(this.field_191134_a, "small_wall_corner", blockpos12, p_191123_3_.add(Rotation.COUNTERCLOCKWISE_90)));
                                }
                                if (!Grid.func_191109_a(p_191123_4_, i2, k + 1)) {
                                    BlockPos blockpos13 = blockpos5.offset(p_191123_3_.rotate(EnumFacing.WEST), 1);
                                    blockpos13 = blockpos13.offset(p_191123_3_.rotate(EnumFacing.SOUTH), 8);
                                    p_191123_1_.add(new MansionTemplate(this.field_191134_a, "small_wall_corner", blockpos13, p_191123_3_.add(Rotation.CLOCKWISE_180)));
                                }
                            }
                        }
                    }
                }
            }
            for (int l = 0; l < p_191123_4_.field_191150_c; ++l) {
                for (int j2 = 0; j2 < p_191123_4_.field_191149_b; ++j2) {
                    BlockPos blockpos14 = p_191123_2_.offset(p_191123_3_.rotate(EnumFacing.SOUTH), 8 + (l - this.field_191137_d) * 8);
                    blockpos14 = blockpos14.offset(p_191123_3_.rotate(EnumFacing.EAST), (j2 - this.field_191136_c) * 8);
                    final boolean flag3 = p_191123_5_ != null && Grid.func_191109_a(p_191123_5_, j2, l);
                    if (Grid.func_191109_a(p_191123_4_, j2, l) && !flag3) {
                        if (!Grid.func_191109_a(p_191123_4_, j2 + 1, l)) {
                            final BlockPos blockpos15 = blockpos14.offset(p_191123_3_.rotate(EnumFacing.EAST), 6);
                            if (!Grid.func_191109_a(p_191123_4_, j2, l + 1)) {
                                final BlockPos blockpos16 = blockpos15.offset(p_191123_3_.rotate(EnumFacing.SOUTH), 6);
                                p_191123_1_.add(new MansionTemplate(this.field_191134_a, "roof_corner", blockpos16, p_191123_3_));
                            }
                            else if (Grid.func_191109_a(p_191123_4_, j2 + 1, l + 1)) {
                                final BlockPos blockpos17 = blockpos15.offset(p_191123_3_.rotate(EnumFacing.SOUTH), 5);
                                p_191123_1_.add(new MansionTemplate(this.field_191134_a, "roof_inner_corner", blockpos17, p_191123_3_));
                            }
                            if (!Grid.func_191109_a(p_191123_4_, j2, l - 1)) {
                                p_191123_1_.add(new MansionTemplate(this.field_191134_a, "roof_corner", blockpos15, p_191123_3_.add(Rotation.COUNTERCLOCKWISE_90)));
                            }
                            else if (Grid.func_191109_a(p_191123_4_, j2 + 1, l - 1)) {
                                BlockPos blockpos18 = blockpos14.offset(p_191123_3_.rotate(EnumFacing.EAST), 9);
                                blockpos18 = blockpos18.offset(p_191123_3_.rotate(EnumFacing.NORTH), 2);
                                p_191123_1_.add(new MansionTemplate(this.field_191134_a, "roof_inner_corner", blockpos18, p_191123_3_.add(Rotation.CLOCKWISE_90)));
                            }
                        }
                        if (!Grid.func_191109_a(p_191123_4_, j2 - 1, l)) {
                            BlockPos blockpos19 = blockpos14.offset(p_191123_3_.rotate(EnumFacing.EAST), 0);
                            blockpos19 = blockpos19.offset(p_191123_3_.rotate(EnumFacing.SOUTH), 0);
                            if (!Grid.func_191109_a(p_191123_4_, j2, l + 1)) {
                                final BlockPos blockpos20 = blockpos19.offset(p_191123_3_.rotate(EnumFacing.SOUTH), 6);
                                p_191123_1_.add(new MansionTemplate(this.field_191134_a, "roof_corner", blockpos20, p_191123_3_.add(Rotation.CLOCKWISE_90)));
                            }
                            else if (Grid.func_191109_a(p_191123_4_, j2 - 1, l + 1)) {
                                BlockPos blockpos21 = blockpos19.offset(p_191123_3_.rotate(EnumFacing.SOUTH), 8);
                                blockpos21 = blockpos21.offset(p_191123_3_.rotate(EnumFacing.WEST), 3);
                                p_191123_1_.add(new MansionTemplate(this.field_191134_a, "roof_inner_corner", blockpos21, p_191123_3_.add(Rotation.COUNTERCLOCKWISE_90)));
                            }
                            if (!Grid.func_191109_a(p_191123_4_, j2, l - 1)) {
                                p_191123_1_.add(new MansionTemplate(this.field_191134_a, "roof_corner", blockpos19, p_191123_3_.add(Rotation.CLOCKWISE_180)));
                            }
                            else if (Grid.func_191109_a(p_191123_4_, j2 - 1, l - 1)) {
                                final BlockPos blockpos22 = blockpos19.offset(p_191123_3_.rotate(EnumFacing.SOUTH), 1);
                                p_191123_1_.add(new MansionTemplate(this.field_191134_a, "roof_inner_corner", blockpos22, p_191123_3_.add(Rotation.CLOCKWISE_180)));
                            }
                        }
                    }
                }
            }
        }
        
        private void func_191133_a(final List<MansionTemplate> p_191133_1_, final PlacementData p_191133_2_) {
            final EnumFacing enumfacing = p_191133_2_.field_191138_a.rotate(EnumFacing.WEST);
            p_191133_1_.add(new MansionTemplate(this.field_191134_a, "entrance", p_191133_2_.field_191139_b.offset(enumfacing, 9), p_191133_2_.field_191138_a));
            p_191133_2_.field_191139_b = p_191133_2_.field_191139_b.offset(p_191133_2_.field_191138_a.rotate(EnumFacing.SOUTH), 16);
        }
        
        private void func_191131_b(final List<MansionTemplate> p_191131_1_, final PlacementData p_191131_2_) {
            p_191131_1_.add(new MansionTemplate(this.field_191134_a, p_191131_2_.field_191140_c, p_191131_2_.field_191139_b.offset(p_191131_2_.field_191138_a.rotate(EnumFacing.EAST), 7), p_191131_2_.field_191138_a));
            p_191131_2_.field_191139_b = p_191131_2_.field_191139_b.offset(p_191131_2_.field_191138_a.rotate(EnumFacing.SOUTH), 8);
        }
        
        private void func_191124_c(final List<MansionTemplate> p_191124_1_, final PlacementData p_191124_2_) {
            p_191124_2_.field_191139_b = p_191124_2_.field_191139_b.offset(p_191124_2_.field_191138_a.rotate(EnumFacing.SOUTH), -1);
            p_191124_1_.add(new MansionTemplate(this.field_191134_a, "wall_corner", p_191124_2_.field_191139_b, p_191124_2_.field_191138_a));
            p_191124_2_.field_191139_b = p_191124_2_.field_191139_b.offset(p_191124_2_.field_191138_a.rotate(EnumFacing.SOUTH), -7);
            p_191124_2_.field_191139_b = p_191124_2_.field_191139_b.offset(p_191124_2_.field_191138_a.rotate(EnumFacing.WEST), -6);
            p_191124_2_.field_191138_a = p_191124_2_.field_191138_a.add(Rotation.CLOCKWISE_90);
        }
        
        private void func_191126_d(final List<MansionTemplate> p_191126_1_, final PlacementData p_191126_2_) {
            p_191126_2_.field_191139_b = p_191126_2_.field_191139_b.offset(p_191126_2_.field_191138_a.rotate(EnumFacing.SOUTH), 6);
            p_191126_2_.field_191139_b = p_191126_2_.field_191139_b.offset(p_191126_2_.field_191138_a.rotate(EnumFacing.EAST), 8);
            p_191126_2_.field_191138_a = p_191126_2_.field_191138_a.add(Rotation.COUNTERCLOCKWISE_90);
        }
        
        private void func_191129_a(final List<MansionTemplate> p_191129_1_, final BlockPos p_191129_2_, final Rotation p_191129_3_, final EnumFacing p_191129_4_, final RoomCollection p_191129_5_) {
            Rotation rotation = Rotation.NONE;
            String s = p_191129_5_.func_191104_a(this.field_191135_b);
            if (p_191129_4_ != EnumFacing.EAST) {
                if (p_191129_4_ == EnumFacing.NORTH) {
                    rotation = rotation.add(Rotation.COUNTERCLOCKWISE_90);
                }
                else if (p_191129_4_ == EnumFacing.WEST) {
                    rotation = rotation.add(Rotation.CLOCKWISE_180);
                }
                else if (p_191129_4_ == EnumFacing.SOUTH) {
                    rotation = rotation.add(Rotation.CLOCKWISE_90);
                }
                else {
                    s = p_191129_5_.func_191099_b(this.field_191135_b);
                }
            }
            BlockPos blockpos = Template.func_191157_a(new BlockPos(1, 0, 0), Mirror.NONE, rotation, 7, 7);
            rotation = rotation.add(p_191129_3_);
            blockpos = blockpos.func_190942_a(p_191129_3_);
            final BlockPos blockpos2 = p_191129_2_.add(blockpos.getX(), 0, blockpos.getZ());
            p_191129_1_.add(new MansionTemplate(this.field_191134_a, s, blockpos2, rotation));
        }
        
        private void func_191132_a(final List<MansionTemplate> p_191132_1_, final BlockPos p_191132_2_, final Rotation p_191132_3_, final EnumFacing p_191132_4_, final EnumFacing p_191132_5_, final RoomCollection p_191132_6_, final boolean p_191132_7_) {
            if (p_191132_5_ == EnumFacing.EAST && p_191132_4_ == EnumFacing.SOUTH) {
                final BlockPos blockpos13 = p_191132_2_.offset(p_191132_3_.rotate(EnumFacing.EAST), 1);
                p_191132_1_.add(new MansionTemplate(this.field_191134_a, p_191132_6_.func_191100_a(this.field_191135_b, p_191132_7_), blockpos13, p_191132_3_));
            }
            else if (p_191132_5_ == EnumFacing.EAST && p_191132_4_ == EnumFacing.NORTH) {
                BlockPos blockpos14 = p_191132_2_.offset(p_191132_3_.rotate(EnumFacing.EAST), 1);
                blockpos14 = blockpos14.offset(p_191132_3_.rotate(EnumFacing.SOUTH), 6);
                p_191132_1_.add(new MansionTemplate(this.field_191134_a, p_191132_6_.func_191100_a(this.field_191135_b, p_191132_7_), blockpos14, p_191132_3_, Mirror.LEFT_RIGHT));
            }
            else if (p_191132_5_ == EnumFacing.WEST && p_191132_4_ == EnumFacing.NORTH) {
                BlockPos blockpos15 = p_191132_2_.offset(p_191132_3_.rotate(EnumFacing.EAST), 7);
                blockpos15 = blockpos15.offset(p_191132_3_.rotate(EnumFacing.SOUTH), 6);
                p_191132_1_.add(new MansionTemplate(this.field_191134_a, p_191132_6_.func_191100_a(this.field_191135_b, p_191132_7_), blockpos15, p_191132_3_.add(Rotation.CLOCKWISE_180)));
            }
            else if (p_191132_5_ == EnumFacing.WEST && p_191132_4_ == EnumFacing.SOUTH) {
                final BlockPos blockpos16 = p_191132_2_.offset(p_191132_3_.rotate(EnumFacing.EAST), 7);
                p_191132_1_.add(new MansionTemplate(this.field_191134_a, p_191132_6_.func_191100_a(this.field_191135_b, p_191132_7_), blockpos16, p_191132_3_, Mirror.FRONT_BACK));
            }
            else if (p_191132_5_ == EnumFacing.SOUTH && p_191132_4_ == EnumFacing.EAST) {
                final BlockPos blockpos17 = p_191132_2_.offset(p_191132_3_.rotate(EnumFacing.EAST), 1);
                p_191132_1_.add(new MansionTemplate(this.field_191134_a, p_191132_6_.func_191100_a(this.field_191135_b, p_191132_7_), blockpos17, p_191132_3_.add(Rotation.CLOCKWISE_90), Mirror.LEFT_RIGHT));
            }
            else if (p_191132_5_ == EnumFacing.SOUTH && p_191132_4_ == EnumFacing.WEST) {
                final BlockPos blockpos18 = p_191132_2_.offset(p_191132_3_.rotate(EnumFacing.EAST), 7);
                p_191132_1_.add(new MansionTemplate(this.field_191134_a, p_191132_6_.func_191100_a(this.field_191135_b, p_191132_7_), blockpos18, p_191132_3_.add(Rotation.CLOCKWISE_90)));
            }
            else if (p_191132_5_ == EnumFacing.NORTH && p_191132_4_ == EnumFacing.WEST) {
                BlockPos blockpos19 = p_191132_2_.offset(p_191132_3_.rotate(EnumFacing.EAST), 7);
                blockpos19 = blockpos19.offset(p_191132_3_.rotate(EnumFacing.SOUTH), 6);
                p_191132_1_.add(new MansionTemplate(this.field_191134_a, p_191132_6_.func_191100_a(this.field_191135_b, p_191132_7_), blockpos19, p_191132_3_.add(Rotation.CLOCKWISE_90), Mirror.FRONT_BACK));
            }
            else if (p_191132_5_ == EnumFacing.NORTH && p_191132_4_ == EnumFacing.EAST) {
                BlockPos blockpos20 = p_191132_2_.offset(p_191132_3_.rotate(EnumFacing.EAST), 1);
                blockpos20 = blockpos20.offset(p_191132_3_.rotate(EnumFacing.SOUTH), 6);
                p_191132_1_.add(new MansionTemplate(this.field_191134_a, p_191132_6_.func_191100_a(this.field_191135_b, p_191132_7_), blockpos20, p_191132_3_.add(Rotation.COUNTERCLOCKWISE_90)));
            }
            else if (p_191132_5_ == EnumFacing.SOUTH && p_191132_4_ == EnumFacing.NORTH) {
                BlockPos blockpos21 = p_191132_2_.offset(p_191132_3_.rotate(EnumFacing.EAST), 1);
                blockpos21 = blockpos21.offset(p_191132_3_.rotate(EnumFacing.NORTH), 8);
                p_191132_1_.add(new MansionTemplate(this.field_191134_a, p_191132_6_.func_191098_b(this.field_191135_b, p_191132_7_), blockpos21, p_191132_3_));
            }
            else if (p_191132_5_ == EnumFacing.NORTH && p_191132_4_ == EnumFacing.SOUTH) {
                BlockPos blockpos22 = p_191132_2_.offset(p_191132_3_.rotate(EnumFacing.EAST), 7);
                blockpos22 = blockpos22.offset(p_191132_3_.rotate(EnumFacing.SOUTH), 14);
                p_191132_1_.add(new MansionTemplate(this.field_191134_a, p_191132_6_.func_191098_b(this.field_191135_b, p_191132_7_), blockpos22, p_191132_3_.add(Rotation.CLOCKWISE_180)));
            }
            else if (p_191132_5_ == EnumFacing.WEST && p_191132_4_ == EnumFacing.EAST) {
                final BlockPos blockpos23 = p_191132_2_.offset(p_191132_3_.rotate(EnumFacing.EAST), 15);
                p_191132_1_.add(new MansionTemplate(this.field_191134_a, p_191132_6_.func_191098_b(this.field_191135_b, p_191132_7_), blockpos23, p_191132_3_.add(Rotation.CLOCKWISE_90)));
            }
            else if (p_191132_5_ == EnumFacing.EAST && p_191132_4_ == EnumFacing.WEST) {
                BlockPos blockpos24 = p_191132_2_.offset(p_191132_3_.rotate(EnumFacing.WEST), 7);
                blockpos24 = blockpos24.offset(p_191132_3_.rotate(EnumFacing.SOUTH), 6);
                p_191132_1_.add(new MansionTemplate(this.field_191134_a, p_191132_6_.func_191098_b(this.field_191135_b, p_191132_7_), blockpos24, p_191132_3_.add(Rotation.COUNTERCLOCKWISE_90)));
            }
            else if (p_191132_5_ == EnumFacing.UP && p_191132_4_ == EnumFacing.EAST) {
                final BlockPos blockpos25 = p_191132_2_.offset(p_191132_3_.rotate(EnumFacing.EAST), 15);
                p_191132_1_.add(new MansionTemplate(this.field_191134_a, p_191132_6_.func_191102_c(this.field_191135_b), blockpos25, p_191132_3_.add(Rotation.CLOCKWISE_90)));
            }
            else if (p_191132_5_ == EnumFacing.UP && p_191132_4_ == EnumFacing.SOUTH) {
                BlockPos blockpos26 = p_191132_2_.offset(p_191132_3_.rotate(EnumFacing.EAST), 1);
                blockpos26 = blockpos26.offset(p_191132_3_.rotate(EnumFacing.NORTH), 0);
                p_191132_1_.add(new MansionTemplate(this.field_191134_a, p_191132_6_.func_191102_c(this.field_191135_b), blockpos26, p_191132_3_));
            }
        }
        
        private void func_191127_a(final List<MansionTemplate> p_191127_1_, final BlockPos p_191127_2_, final Rotation p_191127_3_, final EnumFacing p_191127_4_, final EnumFacing p_191127_5_, final RoomCollection p_191127_6_) {
            int i = 0;
            int j = 0;
            Rotation rotation = p_191127_3_;
            Mirror mirror = Mirror.NONE;
            if (p_191127_5_ == EnumFacing.EAST && p_191127_4_ == EnumFacing.SOUTH) {
                i = -7;
            }
            else if (p_191127_5_ == EnumFacing.EAST && p_191127_4_ == EnumFacing.NORTH) {
                i = -7;
                j = 6;
                mirror = Mirror.LEFT_RIGHT;
            }
            else if (p_191127_5_ == EnumFacing.NORTH && p_191127_4_ == EnumFacing.EAST) {
                i = 1;
                j = 14;
                rotation = p_191127_3_.add(Rotation.COUNTERCLOCKWISE_90);
            }
            else if (p_191127_5_ == EnumFacing.NORTH && p_191127_4_ == EnumFacing.WEST) {
                i = 7;
                j = 14;
                rotation = p_191127_3_.add(Rotation.COUNTERCLOCKWISE_90);
                mirror = Mirror.LEFT_RIGHT;
            }
            else if (p_191127_5_ == EnumFacing.SOUTH && p_191127_4_ == EnumFacing.WEST) {
                i = 7;
                j = -8;
                rotation = p_191127_3_.add(Rotation.CLOCKWISE_90);
            }
            else if (p_191127_5_ == EnumFacing.SOUTH && p_191127_4_ == EnumFacing.EAST) {
                i = 1;
                j = -8;
                rotation = p_191127_3_.add(Rotation.CLOCKWISE_90);
                mirror = Mirror.LEFT_RIGHT;
            }
            else if (p_191127_5_ == EnumFacing.WEST && p_191127_4_ == EnumFacing.NORTH) {
                i = 15;
                j = 6;
                rotation = p_191127_3_.add(Rotation.CLOCKWISE_180);
            }
            else if (p_191127_5_ == EnumFacing.WEST && p_191127_4_ == EnumFacing.SOUTH) {
                i = 15;
                mirror = Mirror.FRONT_BACK;
            }
            BlockPos blockpos = p_191127_2_.offset(p_191127_3_.rotate(EnumFacing.EAST), i);
            blockpos = blockpos.offset(p_191127_3_.rotate(EnumFacing.SOUTH), j);
            p_191127_1_.add(new MansionTemplate(this.field_191134_a, p_191127_6_.func_191101_d(this.field_191135_b), blockpos, rotation, mirror));
        }
        
        private void func_191128_a(final List<MansionTemplate> p_191128_1_, final BlockPos p_191128_2_, final Rotation p_191128_3_, final RoomCollection p_191128_4_) {
            final BlockPos blockpos = p_191128_2_.offset(p_191128_3_.rotate(EnumFacing.EAST), 1);
            p_191128_1_.add(new MansionTemplate(this.field_191134_a, p_191128_4_.func_191103_e(this.field_191135_b), blockpos, p_191128_3_, Mirror.NONE));
        }
    }
    
    abstract static class RoomCollection
    {
        private RoomCollection() {
        }
        
        public abstract String func_191104_a(final Random p0);
        
        public abstract String func_191099_b(final Random p0);
        
        public abstract String func_191100_a(final Random p0, final boolean p1);
        
        public abstract String func_191098_b(final Random p0, final boolean p1);
        
        public abstract String func_191102_c(final Random p0);
        
        public abstract String func_191101_d(final Random p0);
        
        public abstract String func_191103_e(final Random p0);
    }
    
    static class SecondFloor extends RoomCollection
    {
        private SecondFloor() {
            super(null);
        }
        
        @Override
        public String func_191104_a(final Random p_191104_1_) {
            return "1x1_b" + (p_191104_1_.nextInt(4) + 1);
        }
        
        @Override
        public String func_191099_b(final Random p_191099_1_) {
            return "1x1_as" + (p_191099_1_.nextInt(4) + 1);
        }
        
        @Override
        public String func_191100_a(final Random p_191100_1_, final boolean p_191100_2_) {
            return p_191100_2_ ? "1x2_c_stairs" : ("1x2_c" + (p_191100_1_.nextInt(4) + 1));
        }
        
        @Override
        public String func_191098_b(final Random p_191098_1_, final boolean p_191098_2_) {
            return p_191098_2_ ? "1x2_d_stairs" : ("1x2_d" + (p_191098_1_.nextInt(5) + 1));
        }
        
        @Override
        public String func_191102_c(final Random p_191102_1_) {
            return "1x2_se" + (p_191102_1_.nextInt(1) + 1);
        }
        
        @Override
        public String func_191101_d(final Random p_191101_1_) {
            return "2x2_b" + (p_191101_1_.nextInt(5) + 1);
        }
        
        @Override
        public String func_191103_e(final Random p_191103_1_) {
            return "2x2_s1";
        }
    }
    
    static class SimpleGrid
    {
        private final int[][] field_191148_a;
        private final int field_191149_b;
        private final int field_191150_c;
        private final int field_191151_d;
        
        public SimpleGrid(final int p_i47358_1_, final int p_i47358_2_, final int p_i47358_3_) {
            this.field_191149_b = p_i47358_1_;
            this.field_191150_c = p_i47358_2_;
            this.field_191151_d = p_i47358_3_;
            this.field_191148_a = new int[p_i47358_1_][p_i47358_2_];
        }
        
        public void func_191144_a(final int p_191144_1_, final int p_191144_2_, final int p_191144_3_) {
            if (p_191144_1_ >= 0 && p_191144_1_ < this.field_191149_b && p_191144_2_ >= 0 && p_191144_2_ < this.field_191150_c) {
                this.field_191148_a[p_191144_1_][p_191144_2_] = p_191144_3_;
            }
        }
        
        public void func_191142_a(final int p_191142_1_, final int p_191142_2_, final int p_191142_3_, final int p_191142_4_, final int p_191142_5_) {
            for (int i = p_191142_2_; i <= p_191142_4_; ++i) {
                for (int j = p_191142_1_; j <= p_191142_3_; ++j) {
                    this.func_191144_a(j, i, p_191142_5_);
                }
            }
        }
        
        public int func_191145_a(final int p_191145_1_, final int p_191145_2_) {
            return (p_191145_1_ >= 0 && p_191145_1_ < this.field_191149_b && p_191145_2_ >= 0 && p_191145_2_ < this.field_191150_c) ? this.field_191148_a[p_191145_1_][p_191145_2_] : this.field_191151_d;
        }
        
        public void func_191141_a(final int p_191141_1_, final int p_191141_2_, final int p_191141_3_, final int p_191141_4_) {
            if (this.func_191145_a(p_191141_1_, p_191141_2_) == p_191141_3_) {
                this.func_191144_a(p_191141_1_, p_191141_2_, p_191141_4_);
            }
        }
        
        public boolean func_191147_b(final int p_191147_1_, final int p_191147_2_, final int p_191147_3_) {
            return this.func_191145_a(p_191147_1_ - 1, p_191147_2_) == p_191147_3_ || this.func_191145_a(p_191147_1_ + 1, p_191147_2_) == p_191147_3_ || this.func_191145_a(p_191147_1_, p_191147_2_ + 1) == p_191147_3_ || this.func_191145_a(p_191147_1_, p_191147_2_ - 1) == p_191147_3_;
        }
    }
    
    static class ThirdFloor extends SecondFloor
    {
        private ThirdFloor() {
            super(null, null);
        }
    }
}

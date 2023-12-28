// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.command;

import java.util.Collections;
import net.minecraft.util.ResourceLocation;
import java.util.Collection;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.CriterionProgress;
import net.minecraft.advancements.PlayerAdvancements;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import net.minecraft.advancements.Advancement;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class AdvancementCommand extends CommandBase
{
    @Override
    public String getCommandName() {
        return "advancement";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.advancement.usage";
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 1) {
            throw new WrongUsageException("commands.advancement.usage", new Object[0]);
        }
        final ActionType advancementcommand$actiontype = ActionType.func_193536_a(args[0]);
        if (advancementcommand$actiontype != null) {
            if (args.length < 3) {
                throw advancementcommand$actiontype.func_193534_a();
            }
            final EntityPlayerMP entityplayermp = CommandBase.getPlayer(server, sender, args[1]);
            final Mode advancementcommand$mode = Mode.func_193547_a(args[2]);
            if (advancementcommand$mode == null) {
                throw advancementcommand$actiontype.func_193534_a();
            }
            this.func_193516_a(server, sender, args, entityplayermp, advancementcommand$actiontype, advancementcommand$mode);
        }
        else {
            if (!"test".equals(args[0])) {
                throw new WrongUsageException("commands.advancement.usage", new Object[0]);
            }
            if (args.length == 3) {
                this.func_192552_c(sender, CommandBase.getPlayer(server, sender, args[1]), func_192551_a(server, args[2]));
            }
            else {
                if (args.length != 4) {
                    throw new WrongUsageException("commands.advancement.test.usage", new Object[0]);
                }
                this.func_192554_c(sender, CommandBase.getPlayer(server, sender, args[1]), func_192551_a(server, args[2]), args[3]);
            }
        }
    }
    
    private void func_193516_a(final MinecraftServer p_193516_1_, final ICommandSender p_193516_2_, final String[] p_193516_3_, final EntityPlayerMP p_193516_4_, final ActionType p_193516_5_, final Mode p_193516_6_) throws CommandException {
        if (p_193516_6_ == Mode.EVERYTHING) {
            if (p_193516_3_.length != 3) {
                throw p_193516_6_.func_193544_a(p_193516_5_);
            }
            final int j = p_193516_5_.func_193532_a(p_193516_4_, p_193516_1_.func_191949_aK().func_192780_b());
            if (j == 0) {
                throw p_193516_6_.func_193543_a(p_193516_5_, p_193516_4_.getName());
            }
            p_193516_6_.func_193546_a(p_193516_2_, this, p_193516_5_, p_193516_4_.getName(), j);
        }
        else {
            if (p_193516_3_.length < 4) {
                throw p_193516_6_.func_193544_a(p_193516_5_);
            }
            final Advancement advancement = func_192551_a(p_193516_1_, p_193516_3_[3]);
            if (p_193516_6_ == Mode.ONLY && p_193516_3_.length == 5) {
                final String s = p_193516_3_[4];
                if (!advancement.func_192073_f().keySet().contains(s)) {
                    throw new CommandException("commands.advancement.criterionNotFound", new Object[] { advancement.func_192067_g(), p_193516_3_[4] });
                }
                if (!p_193516_5_.func_193535_a(p_193516_4_, advancement, s)) {
                    throw new CommandException(String.valueOf(p_193516_5_.field_193541_d) + ".criterion.failed", new Object[] { advancement.func_192067_g(), p_193516_4_.getName(), s });
                }
                CommandBase.notifyCommandListener(p_193516_2_, this, String.valueOf(p_193516_5_.field_193541_d) + ".criterion.success", advancement.func_192067_g(), p_193516_4_.getName(), s);
            }
            else {
                if (p_193516_3_.length != 4) {
                    throw p_193516_6_.func_193544_a(p_193516_5_);
                }
                final List<Advancement> list = this.func_193514_a(advancement, p_193516_6_);
                final int i = p_193516_5_.func_193532_a(p_193516_4_, list);
                if (i == 0) {
                    throw p_193516_6_.func_193543_a(p_193516_5_, advancement.func_192067_g(), p_193516_4_.getName());
                }
                p_193516_6_.func_193546_a(p_193516_2_, this, p_193516_5_, advancement.func_192067_g(), p_193516_4_.getName(), i);
            }
        }
    }
    
    private void func_193515_a(final Advancement p_193515_1_, final List<Advancement> p_193515_2_) {
        for (final Advancement advancement : p_193515_1_.func_192069_e()) {
            p_193515_2_.add(advancement);
            this.func_193515_a(advancement, p_193515_2_);
        }
    }
    
    private List<Advancement> func_193514_a(final Advancement p_193514_1_, final Mode p_193514_2_) {
        final List<Advancement> list = (List<Advancement>)Lists.newArrayList();
        if (p_193514_2_.field_193555_h) {
            for (Advancement advancement = p_193514_1_.func_192070_b(); advancement != null; advancement = advancement.func_192070_b()) {
                list.add(advancement);
            }
        }
        list.add(p_193514_1_);
        if (p_193514_2_.field_193556_i) {
            this.func_193515_a(p_193514_1_, list);
        }
        return list;
    }
    
    private void func_192554_c(final ICommandSender p_192554_1_, final EntityPlayerMP p_192554_2_, final Advancement p_192554_3_, final String p_192554_4_) throws CommandException {
        final PlayerAdvancements playeradvancements = p_192554_2_.func_192039_O();
        final CriterionProgress criterionprogress = playeradvancements.func_192747_a(p_192554_3_).func_192106_c(p_192554_4_);
        if (criterionprogress == null) {
            throw new CommandException("commands.advancement.criterionNotFound", new Object[] { p_192554_3_.func_192067_g(), p_192554_4_ });
        }
        if (!criterionprogress.func_192151_a()) {
            throw new CommandException("commands.advancement.test.criterion.notDone", new Object[] { p_192554_2_.getName(), p_192554_3_.func_192067_g(), p_192554_4_ });
        }
        CommandBase.notifyCommandListener(p_192554_1_, this, "commands.advancement.test.criterion.success", p_192554_2_.getName(), p_192554_3_.func_192067_g(), p_192554_4_);
    }
    
    private void func_192552_c(final ICommandSender p_192552_1_, final EntityPlayerMP p_192552_2_, final Advancement p_192552_3_) throws CommandException {
        final AdvancementProgress advancementprogress = p_192552_2_.func_192039_O().func_192747_a(p_192552_3_);
        if (!advancementprogress.func_192105_a()) {
            throw new CommandException("commands.advancement.test.advancement.notDone", new Object[] { p_192552_2_.getName(), p_192552_3_.func_192067_g() });
        }
        CommandBase.notifyCommandListener(p_192552_1_, this, "commands.advancement.test.advancement.success", p_192552_2_.getName(), p_192552_3_.func_192067_g());
    }
    
    @Override
    public List<String> getTabCompletionOptions(final MinecraftServer server, final ICommandSender sender, final String[] args, @Nullable final BlockPos pos) {
        if (args.length == 1) {
            return CommandBase.getListOfStringsMatchingLastWord(args, "grant", "revoke", "test");
        }
        final ActionType advancementcommand$actiontype = ActionType.func_193536_a(args[0]);
        if (advancementcommand$actiontype != null) {
            if (args.length == 2) {
                return CommandBase.getListOfStringsMatchingLastWord(args, server.getAllUsernames());
            }
            if (args.length == 3) {
                return CommandBase.getListOfStringsMatchingLastWord(args, Mode.field_193553_f);
            }
            final Mode advancementcommand$mode = Mode.func_193547_a(args[2]);
            if (advancementcommand$mode != null && advancementcommand$mode != Mode.EVERYTHING) {
                if (args.length == 4) {
                    return CommandBase.getListOfStringsMatchingLastWord(args, this.func_193517_a(server));
                }
                if (args.length == 5 && advancementcommand$mode == Mode.ONLY) {
                    final Advancement advancement = server.func_191949_aK().func_192778_a(new ResourceLocation(args[3]));
                    if (advancement != null) {
                        return CommandBase.getListOfStringsMatchingLastWord(args, advancement.func_192073_f().keySet());
                    }
                }
            }
        }
        if ("test".equals(args[0])) {
            if (args.length == 2) {
                return CommandBase.getListOfStringsMatchingLastWord(args, server.getAllUsernames());
            }
            if (args.length == 3) {
                return CommandBase.getListOfStringsMatchingLastWord(args, this.func_193517_a(server));
            }
            if (args.length == 4) {
                final Advancement advancement2 = server.func_191949_aK().func_192778_a(new ResourceLocation(args[2]));
                if (advancement2 != null) {
                    return CommandBase.getListOfStringsMatchingLastWord(args, advancement2.func_192073_f().keySet());
                }
            }
        }
        return Collections.emptyList();
    }
    
    private List<ResourceLocation> func_193517_a(final MinecraftServer p_193517_1_) {
        final List<ResourceLocation> list = (List<ResourceLocation>)Lists.newArrayList();
        for (final Advancement advancement : p_193517_1_.func_191949_aK().func_192780_b()) {
            list.add(advancement.func_192067_g());
        }
        return list;
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return args.length > 1 && ("grant".equals(args[0]) || "revoke".equals(args[0]) || "test".equals(args[0])) && index == 1;
    }
    
    public static Advancement func_192551_a(final MinecraftServer p_192551_0_, final String p_192551_1_) throws CommandException {
        final Advancement advancement = p_192551_0_.func_191949_aK().func_192778_a(new ResourceLocation(p_192551_1_));
        if (advancement == null) {
            throw new CommandException("commands.advancement.advancementNotFound", new Object[] { p_192551_1_ });
        }
        return advancement;
    }
    
    enum ActionType
    {
        GRANT("grant") {
            @Override
            protected boolean func_193537_a(final EntityPlayerMP p_193537_1_, final Advancement p_193537_2_) {
                final AdvancementProgress advancementprogress = p_193537_1_.func_192039_O().func_192747_a(p_193537_2_);
                if (advancementprogress.func_192105_a()) {
                    return false;
                }
                for (final String s : advancementprogress.func_192107_d()) {
                    p_193537_1_.func_192039_O().func_192750_a(p_193537_2_, s);
                }
                return true;
            }
            
            @Override
            protected boolean func_193535_a(final EntityPlayerMP p_193535_1_, final Advancement p_193535_2_, final String p_193535_3_) {
                return p_193535_1_.func_192039_O().func_192750_a(p_193535_2_, p_193535_3_);
            }
        }, 
        REVOKE("revoke") {
            @Override
            protected boolean func_193537_a(final EntityPlayerMP p_193537_1_, final Advancement p_193537_2_) {
                final AdvancementProgress advancementprogress = p_193537_1_.func_192039_O().func_192747_a(p_193537_2_);
                if (!advancementprogress.func_192108_b()) {
                    return false;
                }
                for (final String s : advancementprogress.func_192102_e()) {
                    p_193537_1_.func_192039_O().func_192744_b(p_193537_2_, s);
                }
                return true;
            }
            
            @Override
            protected boolean func_193535_a(final EntityPlayerMP p_193535_1_, final Advancement p_193535_2_, final String p_193535_3_) {
                return p_193535_1_.func_192039_O().func_192744_b(p_193535_2_, p_193535_3_);
            }
        };
        
        final String field_193540_c;
        final String field_193541_d;
        
        private ActionType(final String s, final int n, final String p_i47557_3_) {
            this.field_193540_c = p_i47557_3_;
            this.field_193541_d = "commands.advancement." + p_i47557_3_;
        }
        
        @Nullable
        static ActionType func_193536_a(final String p_193536_0_) {
            ActionType[] values;
            for (int length = (values = values()).length, i = 0; i < length; ++i) {
                final ActionType advancementcommand$actiontype = values[i];
                if (advancementcommand$actiontype.field_193540_c.equals(p_193536_0_)) {
                    return advancementcommand$actiontype;
                }
            }
            return null;
        }
        
        CommandException func_193534_a() {
            return new CommandException(String.valueOf(this.field_193541_d) + ".usage", new Object[0]);
        }
        
        public int func_193532_a(final EntityPlayerMP p_193532_1_, final Iterable<Advancement> p_193532_2_) {
            int i = 0;
            for (final Advancement advancement : p_193532_2_) {
                if (this.func_193537_a(p_193532_1_, advancement)) {
                    ++i;
                }
            }
            return i;
        }
        
        protected abstract boolean func_193537_a(final EntityPlayerMP p0, final Advancement p1);
        
        protected abstract boolean func_193535_a(final EntityPlayerMP p0, final Advancement p1, final String p2);
    }
    
    enum Mode
    {
        ONLY("ONLY", 0, "only", false, false), 
        THROUGH("THROUGH", 1, "through", true, true), 
        FROM("FROM", 2, "from", false, true), 
        UNTIL("UNTIL", 3, "until", true, false), 
        EVERYTHING("EVERYTHING", 4, "everything", true, true);
        
        static final String[] field_193553_f;
        final String field_193554_g;
        final boolean field_193555_h;
        final boolean field_193556_i;
        
        static {
            field_193553_f = new String[values().length];
            for (int i = 0; i < values().length; ++i) {
                Mode.field_193553_f[i] = values()[i].field_193554_g;
            }
        }
        
        private Mode(final String s, final int n, final String p_i47556_3_, final boolean p_i47556_4_, final boolean p_i47556_5_) {
            this.field_193554_g = p_i47556_3_;
            this.field_193555_h = p_i47556_4_;
            this.field_193556_i = p_i47556_5_;
        }
        
        CommandException func_193543_a(final ActionType p_193543_1_, final Object... p_193543_2_) {
            return new CommandException(String.valueOf(p_193543_1_.field_193541_d) + "." + this.field_193554_g + ".failed", p_193543_2_);
        }
        
        CommandException func_193544_a(final ActionType p_193544_1_) {
            return new CommandException(String.valueOf(p_193544_1_.field_193541_d) + "." + this.field_193554_g + ".usage", new Object[0]);
        }
        
        void func_193546_a(final ICommandSender p_193546_1_, final AdvancementCommand p_193546_2_, final ActionType p_193546_3_, final Object... p_193546_4_) {
            CommandBase.notifyCommandListener(p_193546_1_, p_193546_2_, String.valueOf(p_193546_3_.field_193541_d) + "." + this.field_193554_g + ".success", p_193546_4_);
        }
        
        @Nullable
        static Mode func_193547_a(final String p_193547_0_) {
            Mode[] values;
            for (int length = (values = values()).length, i = 0; i < length; ++i) {
                final Mode advancementcommand$mode = values[i];
                if (advancementcommand$mode.field_193554_g.equals(p_193547_0_)) {
                    return advancementcommand$mode;
                }
            }
            return null;
        }
    }
}

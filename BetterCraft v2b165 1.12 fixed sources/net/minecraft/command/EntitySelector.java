// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.command;

import com.google.common.collect.Maps;
import com.google.common.collect.ComparisonChain;
import java.util.Comparator;
import net.minecraft.util.EntitySelectors;
import com.google.common.base.Predicates;
import net.minecraft.util.math.MathHelper;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.GameType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.Collection;
import net.minecraft.world.World;
import java.util.Collections;
import java.util.Iterator;
import com.google.common.collect.Lists;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.entity.Entity;
import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import javax.annotation.Nullable;
import com.google.common.collect.Sets;
import com.google.common.base.Predicate;
import java.util.Set;
import com.google.common.base.Splitter;
import java.util.regex.Pattern;

public class EntitySelector
{
    private static final Pattern TOKEN_PATTERN;
    private static final Splitter field_190828_b;
    private static final Splitter field_190829_c;
    private static final Set<String> field_190830_d;
    private static final String field_190831_e;
    private static final String field_190832_f;
    private static final String field_190833_g;
    private static final String field_190834_h;
    private static final String field_190835_i;
    private static final String field_190836_j;
    private static final String field_190837_k;
    private static final String field_190838_l;
    private static final String field_190839_m;
    private static final String field_190840_n;
    private static final String field_190841_o;
    private static final String field_190842_p;
    private static final String field_190843_q;
    private static final String field_190844_r;
    private static final String field_190845_s;
    private static final String field_190846_t;
    private static final String field_190847_u;
    private static final String field_190848_v;
    private static final String field_190849_w;
    private static final String field_190850_x;
    private static final Predicate<String> field_190851_y;
    private static final Set<String> WORLD_BINDING_ARGS;
    
    static {
        TOKEN_PATTERN = Pattern.compile("^@([pares])(?:\\[([^ ]*)\\])?$");
        field_190828_b = Splitter.on(',').omitEmptyStrings();
        field_190829_c = Splitter.on('=').limit(2);
        field_190830_d = Sets.newHashSet();
        field_190831_e = func_190826_c("r");
        field_190832_f = func_190826_c("rm");
        field_190833_g = func_190826_c("l");
        field_190834_h = func_190826_c("lm");
        field_190835_i = func_190826_c("x");
        field_190836_j = func_190826_c("y");
        field_190837_k = func_190826_c("z");
        field_190838_l = func_190826_c("dx");
        field_190839_m = func_190826_c("dy");
        field_190840_n = func_190826_c("dz");
        field_190841_o = func_190826_c("rx");
        field_190842_p = func_190826_c("rxm");
        field_190843_q = func_190826_c("ry");
        field_190844_r = func_190826_c("rym");
        field_190845_s = func_190826_c("c");
        field_190846_t = func_190826_c("m");
        field_190847_u = func_190826_c("team");
        field_190848_v = func_190826_c("name");
        field_190849_w = func_190826_c("type");
        field_190850_x = func_190826_c("tag");
        field_190851_y = new Predicate<String>() {
            @Override
            public boolean apply(@Nullable final String p_apply_1_) {
                return p_apply_1_ != null && (EntitySelector.field_190830_d.contains(p_apply_1_) || (p_apply_1_.length() > "score_".length() && p_apply_1_.startsWith("score_")));
            }
        };
        WORLD_BINDING_ARGS = Sets.newHashSet(EntitySelector.field_190835_i, EntitySelector.field_190836_j, EntitySelector.field_190837_k, EntitySelector.field_190838_l, EntitySelector.field_190839_m, EntitySelector.field_190840_n, EntitySelector.field_190832_f, EntitySelector.field_190831_e);
    }
    
    private static String func_190826_c(final String p_190826_0_) {
        EntitySelector.field_190830_d.add(p_190826_0_);
        return p_190826_0_;
    }
    
    @Nullable
    public static EntityPlayerMP matchOnePlayer(final ICommandSender sender, final String token) throws CommandException {
        return matchOneEntity(sender, token, (Class<? extends EntityPlayerMP>)EntityPlayerMP.class);
    }
    
    public static List<EntityPlayerMP> func_193531_b(final ICommandSender p_193531_0_, final String p_193531_1_) throws CommandException {
        return matchEntities(p_193531_0_, p_193531_1_, (Class<? extends EntityPlayerMP>)EntityPlayerMP.class);
    }
    
    @Nullable
    public static <T extends Entity> T matchOneEntity(final ICommandSender sender, final String token, final Class<? extends T> targetClass) throws CommandException {
        final List<T> list = matchEntities(sender, token, targetClass);
        return (T)((list.size() == 1) ? ((T)list.get(0)) : null);
    }
    
    @Nullable
    public static ITextComponent matchEntitiesToTextComponent(final ICommandSender sender, final String token) throws CommandException {
        final List<Entity> list = matchEntities(sender, token, (Class<? extends Entity>)Entity.class);
        if (list.isEmpty()) {
            return null;
        }
        final List<ITextComponent> list2 = (List<ITextComponent>)Lists.newArrayList();
        for (final Entity entity : list) {
            list2.add(entity.getDisplayName());
        }
        return CommandBase.join(list2);
    }
    
    public static <T extends Entity> List<T> matchEntities(final ICommandSender sender, final String token, final Class<? extends T> targetClass) throws CommandException {
        final Matcher matcher = EntitySelector.TOKEN_PATTERN.matcher(token);
        if (!matcher.matches() || !sender.canCommandSenderUseCommand(1, "@")) {
            return Collections.emptyList();
        }
        final Map<String, String> map = getArgumentMap(matcher.group(2));
        if (!isEntityTypeValid(sender, map)) {
            return Collections.emptyList();
        }
        final String s = matcher.group(1);
        final BlockPos blockpos = getBlockPosFromArguments(map, sender.getPosition());
        final Vec3d vec3d = getPosFromArguments(map, sender.getPositionVector());
        final List<World> list = getWorlds(sender, map);
        final List<T> list2 = (List<T>)Lists.newArrayList();
        for (final World world : list) {
            if (world != null) {
                final List<Predicate<Entity>> list3 = (List<Predicate<Entity>>)Lists.newArrayList();
                list3.addAll(getTypePredicates(map, s));
                list3.addAll(getXpLevelPredicates(map));
                list3.addAll(getGamemodePredicates(map));
                list3.addAll(getTeamPredicates(map));
                list3.addAll(getScorePredicates(sender, map));
                list3.addAll(getNamePredicates(map));
                list3.addAll(getTagPredicates(map));
                list3.addAll(getRadiusPredicates(map, vec3d));
                list3.addAll(getRotationsPredicates(map));
                if ("s".equalsIgnoreCase(s)) {
                    final Entity entity = sender.getCommandSenderEntity();
                    if (entity != null && targetClass.isAssignableFrom(entity.getClass())) {
                        if (map.containsKey(EntitySelector.field_190838_l) || map.containsKey(EntitySelector.field_190839_m) || map.containsKey(EntitySelector.field_190840_n)) {
                            final int i = getInt(map, EntitySelector.field_190838_l, 0);
                            final int j = getInt(map, EntitySelector.field_190839_m, 0);
                            final int k = getInt(map, EntitySelector.field_190840_n, 0);
                            final AxisAlignedBB axisalignedbb = getAABB(blockpos, i, j, k);
                            if (!axisalignedbb.intersectsWith(entity.getEntityBoundingBox())) {
                                return Collections.emptyList();
                            }
                        }
                        for (final Predicate<Entity> predicate : list3) {
                            if (!predicate.apply(entity)) {
                                return Collections.emptyList();
                            }
                        }
                        return (List<T>)Lists.newArrayList(entity);
                    }
                    return Collections.emptyList();
                }
                else {
                    list2.addAll((Collection<? extends T>)filterResults(map, (Class<? extends Entity>)targetClass, list3, s, world, blockpos));
                }
            }
        }
        return getEntitiesFromPredicates(list2, map, sender, targetClass, s, vec3d);
    }
    
    private static List<World> getWorlds(final ICommandSender sender, final Map<String, String> argumentMap) {
        final List<World> list = (List<World>)Lists.newArrayList();
        if (hasArgument(argumentMap)) {
            list.add(sender.getEntityWorld());
        }
        else {
            Collections.addAll(list, sender.getServer().worldServers);
        }
        return list;
    }
    
    private static <T extends Entity> boolean isEntityTypeValid(final ICommandSender commandSender, final Map<String, String> params) {
        final String s = getArgument(params, EntitySelector.field_190849_w);
        if (s == null) {
            return true;
        }
        final ResourceLocation resourcelocation = new ResourceLocation(s.startsWith("!") ? s.substring(1) : s);
        if (EntityList.isStringValidEntityName(resourcelocation)) {
            return true;
        }
        final TextComponentTranslation textcomponenttranslation = new TextComponentTranslation("commands.generic.entity.invalidType", new Object[] { resourcelocation });
        textcomponenttranslation.getStyle().setColor(TextFormatting.RED);
        commandSender.addChatMessage(textcomponenttranslation);
        return false;
    }
    
    private static List<Predicate<Entity>> getTypePredicates(final Map<String, String> params, final String type) {
        final String s = getArgument(params, EntitySelector.field_190849_w);
        if (s == null || (!type.equals("e") && !type.equals("r") && !type.equals("s"))) {
            return (List<Predicate<Entity>>)((type.equals("e") || type.equals("s")) ? Collections.emptyList() : Collections.singletonList(new Predicate<Entity>() {
                @Override
                public boolean apply(@Nullable final Entity p_apply_1_) {
                    return p_apply_1_ instanceof EntityPlayer;
                }
            }));
        }
        final boolean flag = s.startsWith("!");
        final ResourceLocation resourcelocation = new ResourceLocation(flag ? s.substring(1) : s);
        return (List<Predicate<Entity>>)Collections.singletonList(new Predicate<Entity>() {
            @Override
            public boolean apply(@Nullable final Entity p_apply_1_) {
                return EntityList.isStringEntityName(p_apply_1_, resourcelocation) ^ flag;
            }
        });
    }
    
    private static List<Predicate<Entity>> getXpLevelPredicates(final Map<String, String> params) {
        final List<Predicate<Entity>> list = (List<Predicate<Entity>>)Lists.newArrayList();
        final int i = getInt(params, EntitySelector.field_190834_h, -1);
        final int j = getInt(params, EntitySelector.field_190833_g, -1);
        if (i > -1 || j > -1) {
            list.add(new Predicate<Entity>() {
                @Override
                public boolean apply(@Nullable final Entity p_apply_1_) {
                    if (!(p_apply_1_ instanceof EntityPlayerMP)) {
                        return false;
                    }
                    final EntityPlayerMP entityplayermp = (EntityPlayerMP)p_apply_1_;
                    return (i <= -1 || entityplayermp.experienceLevel >= i) && (j <= -1 || entityplayermp.experienceLevel <= j);
                }
            });
        }
        return list;
    }
    
    private static List<Predicate<Entity>> getGamemodePredicates(final Map<String, String> params) {
        final List<Predicate<Entity>> list = (List<Predicate<Entity>>)Lists.newArrayList();
        String s = getArgument(params, EntitySelector.field_190846_t);
        if (s == null) {
            return list;
        }
        final boolean flag = s.startsWith("!");
        if (flag) {
            s = s.substring(1);
        }
        GameType gametype;
        try {
            final int i = Integer.parseInt(s);
            gametype = GameType.parseGameTypeWithDefault(i, GameType.NOT_SET);
        }
        catch (final Throwable var6) {
            gametype = GameType.parseGameTypeWithDefault(s, GameType.NOT_SET);
        }
        final GameType type = gametype;
        list.add(new Predicate<Entity>() {
            @Override
            public boolean apply(@Nullable final Entity p_apply_1_) {
                if (!(p_apply_1_ instanceof EntityPlayerMP)) {
                    return false;
                }
                final EntityPlayerMP entityplayermp = (EntityPlayerMP)p_apply_1_;
                final GameType gametype1 = entityplayermp.interactionManager.getGameType();
                return flag ? (gametype1 != type) : (gametype1 == type);
            }
        });
        return list;
    }
    
    private static List<Predicate<Entity>> getTeamPredicates(final Map<String, String> params) {
        final List<Predicate<Entity>> list = (List<Predicate<Entity>>)Lists.newArrayList();
        String s = getArgument(params, EntitySelector.field_190847_u);
        final boolean flag = s != null && s.startsWith("!");
        if (flag) {
            s = s.substring(1);
        }
        if (s != null) {
            final String s_f_ = s;
            list.add(new Predicate<Entity>() {
                @Override
                public boolean apply(@Nullable final Entity p_apply_1_) {
                    if (!(p_apply_1_ instanceof EntityLivingBase)) {
                        return false;
                    }
                    final EntityLivingBase entitylivingbase = (EntityLivingBase)p_apply_1_;
                    final Team team = entitylivingbase.getTeam();
                    final String s1 = (team == null) ? "" : team.getRegisteredName();
                    return s1.equals(s_f_) ^ flag;
                }
            });
        }
        return list;
    }
    
    private static List<Predicate<Entity>> getScorePredicates(final ICommandSender sender, final Map<String, String> params) {
        final Map<String, Integer> map = getScoreMap(params);
        return (List<Predicate<Entity>>)(map.isEmpty() ? Collections.emptyList() : Lists.newArrayList(new Predicate<Entity>() {
            @Override
            public boolean apply(@Nullable final Entity p_apply_1_) {
                if (p_apply_1_ == null) {
                    return false;
                }
                final Scoreboard scoreboard = sender.getServer().worldServerForDimension(0).getScoreboard();
                for (final Map.Entry<String, Integer> entry : map.entrySet()) {
                    String s = entry.getKey();
                    boolean flag = false;
                    if (s.endsWith("_min") && s.length() > 4) {
                        flag = true;
                        s = s.substring(0, s.length() - 4);
                    }
                    final ScoreObjective scoreobjective = scoreboard.getObjective(s);
                    if (scoreobjective == null) {
                        return false;
                    }
                    final String s2 = (p_apply_1_ instanceof EntityPlayerMP) ? p_apply_1_.getName() : p_apply_1_.getCachedUniqueIdString();
                    if (!scoreboard.entityHasObjective(s2, scoreobjective)) {
                        return false;
                    }
                    final Score score = scoreboard.getOrCreateScore(s2, scoreobjective);
                    final int i = score.getScorePoints();
                    if (i < entry.getValue() && flag) {
                        return false;
                    }
                    if (i > entry.getValue() && !flag) {
                        return false;
                    }
                }
                return true;
            }
        }));
    }
    
    private static List<Predicate<Entity>> getNamePredicates(final Map<String, String> params) {
        final List<Predicate<Entity>> list = (List<Predicate<Entity>>)Lists.newArrayList();
        String s = getArgument(params, EntitySelector.field_190848_v);
        final boolean flag = s != null && s.startsWith("!");
        if (flag) {
            s = s.substring(1);
        }
        if (s != null) {
            final String s_f_ = s;
            list.add(new Predicate<Entity>() {
                @Override
                public boolean apply(@Nullable final Entity p_apply_1_) {
                    return p_apply_1_ != null && p_apply_1_.getName().equals(s_f_) != flag;
                }
            });
        }
        return list;
    }
    
    private static List<Predicate<Entity>> getTagPredicates(final Map<String, String> params) {
        final List<Predicate<Entity>> list = (List<Predicate<Entity>>)Lists.newArrayList();
        String s = getArgument(params, EntitySelector.field_190850_x);
        final boolean flag = s != null && s.startsWith("!");
        if (flag) {
            s = s.substring(1);
        }
        if (s != null) {
            final String s_f_ = s;
            list.add(new Predicate<Entity>() {
                @Override
                public boolean apply(@Nullable final Entity p_apply_1_) {
                    if (p_apply_1_ == null) {
                        return false;
                    }
                    if ("".equals(s_f_)) {
                        return p_apply_1_.getTags().isEmpty() ^ flag;
                    }
                    return p_apply_1_.getTags().contains(s_f_) ^ flag;
                }
            });
        }
        return list;
    }
    
    private static List<Predicate<Entity>> getRadiusPredicates(final Map<String, String> params, final Vec3d pos) {
        final double d0 = getInt(params, EntitySelector.field_190832_f, -1);
        final double d2 = getInt(params, EntitySelector.field_190831_e, -1);
        final boolean flag = d0 < -0.5;
        final boolean flag2 = d2 < -0.5;
        if (flag && flag2) {
            return Collections.emptyList();
        }
        final double d3 = Math.max(d0, 1.0E-4);
        final double d4 = d3 * d3;
        final double d5 = Math.max(d2, 1.0E-4);
        final double d6 = d5 * d5;
        return (List<Predicate<Entity>>)Lists.newArrayList(new Predicate<Entity>() {
            @Override
            public boolean apply(@Nullable final Entity p_apply_1_) {
                if (p_apply_1_ == null) {
                    return false;
                }
                final double d6 = pos.squareDistanceTo(p_apply_1_.posX, p_apply_1_.posY, p_apply_1_.posZ);
                return (flag || d6 >= d4) && (flag2 || d6 <= d6);
            }
        });
    }
    
    private static List<Predicate<Entity>> getRotationsPredicates(final Map<String, String> params) {
        final List<Predicate<Entity>> list = (List<Predicate<Entity>>)Lists.newArrayList();
        if (params.containsKey(EntitySelector.field_190844_r) || params.containsKey(EntitySelector.field_190843_q)) {
            final int i = MathHelper.clampAngle(getInt(params, EntitySelector.field_190844_r, 0));
            final int j = MathHelper.clampAngle(getInt(params, EntitySelector.field_190843_q, 359));
            list.add(new Predicate<Entity>() {
                @Override
                public boolean apply(@Nullable final Entity p_apply_1_) {
                    if (p_apply_1_ == null) {
                        return false;
                    }
                    final int i1 = MathHelper.clampAngle(MathHelper.floor(p_apply_1_.rotationYaw));
                    if (i > j) {
                        return i1 >= i || i1 <= j;
                    }
                    return i1 >= i && i1 <= j;
                }
            });
        }
        if (params.containsKey(EntitySelector.field_190842_p) || params.containsKey(EntitySelector.field_190841_o)) {
            final int k = MathHelper.clampAngle(getInt(params, EntitySelector.field_190842_p, 0));
            final int l = MathHelper.clampAngle(getInt(params, EntitySelector.field_190841_o, 359));
            list.add(new Predicate<Entity>() {
                @Override
                public boolean apply(@Nullable final Entity p_apply_1_) {
                    if (p_apply_1_ == null) {
                        return false;
                    }
                    final int i1 = MathHelper.clampAngle(MathHelper.floor(p_apply_1_.rotationPitch));
                    if (k > l) {
                        return i1 >= k || i1 <= l;
                    }
                    return i1 >= k && i1 <= l;
                }
            });
        }
        return list;
    }
    
    private static <T extends Entity> List<T> filterResults(final Map<String, String> params, final Class<? extends T> entityClass, final List<Predicate<Entity>> inputList, final String type, final World worldIn, final BlockPos position) {
        final List<T> list = (List<T>)Lists.newArrayList();
        String s = getArgument(params, EntitySelector.field_190849_w);
        s = ((s != null && s.startsWith("!")) ? s.substring(1) : s);
        final boolean flag = !type.equals("e");
        final boolean flag2 = type.equals("r") && s != null;
        final int i = getInt(params, EntitySelector.field_190838_l, 0);
        final int j = getInt(params, EntitySelector.field_190839_m, 0);
        final int k = getInt(params, EntitySelector.field_190840_n, 0);
        final int l = getInt(params, EntitySelector.field_190831_e, -1);
        final Predicate<Entity> predicate = Predicates.and((Iterable<? extends Predicate<? super Entity>>)inputList);
        final Predicate<Entity> predicate2 = Predicates.and((Predicate<? super Entity>)EntitySelectors.IS_ALIVE, (Predicate<? super Entity>)predicate);
        if (!params.containsKey(EntitySelector.field_190838_l) && !params.containsKey(EntitySelector.field_190839_m) && !params.containsKey(EntitySelector.field_190840_n)) {
            if (l >= 0) {
                final AxisAlignedBB axisalignedbb1 = new AxisAlignedBB(position.getX() - l, position.getY() - l, position.getZ() - l, position.getX() + l + 1, position.getY() + l + 1, position.getZ() + l + 1);
                if (flag && !flag2) {
                    list.addAll((Collection<? extends T>)worldIn.getPlayers((Class<? extends Entity>)entityClass, (Predicate<? super Entity>)predicate2));
                }
                else {
                    list.addAll((Collection<? extends T>)worldIn.getEntitiesWithinAABB((Class<? extends Entity>)entityClass, axisalignedbb1, (Predicate<? super Entity>)predicate2));
                }
            }
            else if (type.equals("a")) {
                list.addAll((Collection<? extends T>)worldIn.getPlayers((Class<? extends Entity>)entityClass, (Predicate<? super Entity>)predicate));
            }
            else if (!type.equals("p") && (!type.equals("r") || flag2)) {
                list.addAll((Collection<? extends T>)worldIn.getEntities((Class<? extends Entity>)entityClass, (Predicate<? super Entity>)predicate2));
            }
            else {
                list.addAll((Collection<? extends T>)worldIn.getPlayers((Class<? extends Entity>)entityClass, (Predicate<? super Entity>)predicate2));
            }
        }
        else {
            final AxisAlignedBB axisalignedbb2 = getAABB(position, i, j, k);
            if (flag && !flag2) {
                final Predicate<Entity> predicate3 = new Predicate<Entity>() {
                    @Override
                    public boolean apply(@Nullable final Entity p_apply_1_) {
                        return p_apply_1_ != null && axisalignedbb2.intersectsWith(p_apply_1_.getEntityBoundingBox());
                    }
                };
                list.addAll((Collection<? extends T>)worldIn.getPlayers((Class<? extends Entity>)entityClass, Predicates.and((Predicate<? super Entity>)predicate2, (Predicate<? super Entity>)predicate3)));
            }
            else {
                list.addAll((Collection<? extends T>)worldIn.getEntitiesWithinAABB((Class<? extends Entity>)entityClass, axisalignedbb2, (Predicate<? super Entity>)predicate2));
            }
        }
        return list;
    }
    
    private static <T extends Entity> List<T> getEntitiesFromPredicates(List<T> matchingEntities, final Map<String, String> params, final ICommandSender sender, final Class<? extends T> targetClass, final String type, final Vec3d pos) {
        final int i = getInt(params, EntitySelector.field_190845_s, (!type.equals("a") && !type.equals("e")) ? 1 : 0);
        if (!type.equals("p") && !type.equals("a") && !type.equals("e")) {
            if (type.equals("r")) {
                Collections.shuffle(matchingEntities);
            }
        }
        else {
            Collections.sort(matchingEntities, new Comparator<Entity>() {
                @Override
                public int compare(final Entity p_compare_1_, final Entity p_compare_2_) {
                    return ComparisonChain.start().compare(p_compare_1_.getDistanceSq(pos.xCoord, pos.yCoord, pos.zCoord), p_compare_2_.getDistanceSq(pos.xCoord, pos.yCoord, pos.zCoord)).result();
                }
            });
        }
        final Entity entity = sender.getCommandSenderEntity();
        if (entity != null && targetClass.isAssignableFrom(entity.getClass()) && i == 1 && matchingEntities.contains(entity) && !"r".equals(type)) {
            matchingEntities = (List<T>)Lists.newArrayList(entity);
        }
        if (i != 0) {
            if (i < 0) {
                Collections.reverse(matchingEntities);
            }
            matchingEntities = matchingEntities.subList(0, Math.min(Math.abs(i), matchingEntities.size()));
        }
        return matchingEntities;
    }
    
    private static AxisAlignedBB getAABB(final BlockPos pos, final int x, final int y, final int z) {
        final boolean flag = x < 0;
        final boolean flag2 = y < 0;
        final boolean flag3 = z < 0;
        final int i = pos.getX() + (flag ? x : 0);
        final int j = pos.getY() + (flag2 ? y : 0);
        final int k = pos.getZ() + (flag3 ? z : 0);
        final int l = pos.getX() + (flag ? 0 : x) + 1;
        final int i2 = pos.getY() + (flag2 ? 0 : y) + 1;
        final int j2 = pos.getZ() + (flag3 ? 0 : z) + 1;
        return new AxisAlignedBB(i, j, k, l, i2, j2);
    }
    
    private static BlockPos getBlockPosFromArguments(final Map<String, String> params, final BlockPos pos) {
        return new BlockPos(getInt(params, EntitySelector.field_190835_i, pos.getX()), getInt(params, EntitySelector.field_190836_j, pos.getY()), getInt(params, EntitySelector.field_190837_k, pos.getZ()));
    }
    
    private static Vec3d getPosFromArguments(final Map<String, String> params, final Vec3d pos) {
        return new Vec3d(getCoordinate(params, EntitySelector.field_190835_i, pos.xCoord, true), getCoordinate(params, EntitySelector.field_190836_j, pos.yCoord, false), getCoordinate(params, EntitySelector.field_190837_k, pos.zCoord, true));
    }
    
    private static double getCoordinate(final Map<String, String> params, final String key, final double defaultD, final boolean offset) {
        return params.containsKey(key) ? (MathHelper.getInt(params.get(key), MathHelper.floor(defaultD)) + (offset ? 0.5 : 0.0)) : defaultD;
    }
    
    private static boolean hasArgument(final Map<String, String> params) {
        for (final String s : EntitySelector.WORLD_BINDING_ARGS) {
            if (params.containsKey(s)) {
                return true;
            }
        }
        return false;
    }
    
    private static int getInt(final Map<String, String> params, final String key, final int defaultI) {
        return params.containsKey(key) ? MathHelper.getInt(params.get(key), defaultI) : defaultI;
    }
    
    @Nullable
    private static String getArgument(final Map<String, String> params, final String key) {
        return params.get(key);
    }
    
    public static Map<String, Integer> getScoreMap(final Map<String, String> params) {
        final Map<String, Integer> map = (Map<String, Integer>)Maps.newHashMap();
        for (final String s : params.keySet()) {
            if (s.startsWith("score_") && s.length() > "score_".length()) {
                map.put(s.substring("score_".length()), MathHelper.getInt(params.get(s), 1));
            }
        }
        return map;
    }
    
    public static boolean matchesMultiplePlayers(final String selectorStr) throws CommandException {
        final Matcher matcher = EntitySelector.TOKEN_PATTERN.matcher(selectorStr);
        if (!matcher.matches()) {
            return false;
        }
        final Map<String, String> map = getArgumentMap(matcher.group(2));
        final String s = matcher.group(1);
        final int i = (!"a".equals(s) && !"e".equals(s)) ? 1 : 0;
        return getInt(map, EntitySelector.field_190845_s, i) != 1;
    }
    
    public static boolean hasArguments(final String selectorStr) {
        return EntitySelector.TOKEN_PATTERN.matcher(selectorStr).matches();
    }
    
    private static Map<String, String> getArgumentMap(@Nullable final String argumentString) throws CommandException {
        final Map<String, String> map = (Map<String, String>)Maps.newHashMap();
        if (argumentString == null) {
            return map;
        }
        for (final String s : EntitySelector.field_190828_b.split(argumentString)) {
            final Iterator<String> iterator = EntitySelector.field_190829_c.split(s).iterator();
            final String s2 = iterator.next();
            if (!EntitySelector.field_190851_y.apply(s2)) {
                throw new CommandException("commands.generic.selector_argument", new Object[] { s });
            }
            map.put(s2, iterator.hasNext() ? iterator.next() : "");
        }
        return map;
    }
}

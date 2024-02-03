// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.account;

import java.lang.reflect.Method;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.Charsets;
import java.net.HttpURLConnection;
import net.labymod.utils.UUIDFetcher;
import java.util.Iterator;
import java.util.List;
import com.mojang.authlib.UserAuthentication;
import com.mojang.authlib.HttpAuthenticationService;
import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.net.Proxy;
import java.lang.reflect.Field;
import net.labymod.support.util.Debug;
import net.labymod.core.LabyModCore;
import net.minecraft.client.Minecraft;
import net.labymod.main.LabyMod;
import com.google.gson.JsonObject;
import net.labymod.utils.JsonParse;
import java.net.URL;
import java.util.UUID;
import net.minecraft.util.Session;
import net.labymod.utils.Consumer;

public class AccountLoginHandler
{
    private AccountManager accountManager;
    
    public AccountLoginHandler(final AccountManager accountManager) {
        this.accountManager = accountManager;
    }
    
    public void loginWithPassword(final String username, final String password, final Consumer<Boolean> response) {
        this.authenticatePassword(username, password, new Consumer<Session>() {
            @Override
            public void accept(final Session session) {
                if (session == null) {
                    response.accept(false);
                }
                else {
                    AccountLoginHandler.this.login(session, response);
                }
            }
        });
    }
    
    public void loginWithToken(final UUID uuid, final String username, final String accessToken, final Consumer<Boolean> response) {
        final Session session = new Session(username, uuid.toString(), accessToken, Session.Type.MOJANG.toString());
        this.login(session, response);
    }
    
    public void login(final Session session, final Consumer<Boolean> response) {
        try {
            this.setSession(session, response);
        }
        catch (final Exception e) {
            response.accept(false);
            e.printStackTrace();
            this.accountManager.throwError(AuthError.EXCEPTION, e.getMessage());
        }
    }
    
    public void refreshToken(final String accessToken, final Consumer<String> refreshedToken, final Consumer<Boolean> response) {
        try {
            final String payload = "{ \"clientToken\": \"" + this.accountManager.getClientToken() + "\", \"accessToken\": \"" + accessToken + "\" }";
            final String result = this.performPostRequest(new URL("https://authserver.mojang.com/refresh"), payload);
            final JsonObject resultObject = JsonParse.parse(result).getAsJsonObject();
            if (resultObject.has("accessToken")) {
                final String newAccessToken = resultObject.get("accessToken").getAsString();
                refreshedToken.accept(JsonParse.parse(newAccessToken).getAsString());
                response.accept(true);
            }
            else if (resultObject.has("errorMessage")) {
                response.accept(false);
                this.accountManager.throwError(AuthError.FAILED_TO_REFRESH, resultObject.get("errorMessage").getAsString());
            }
            else {
                response.accept(false);
                this.accountManager.throwError(AuthError.FAILED_TO_REFRESH, "Unknown error");
            }
        }
        catch (final Exception error) {
            error.printStackTrace();
            response.accept(false);
            this.accountManager.throwError(AuthError.EXCEPTION, new String[0]);
        }
    }
    
    public void setSession(final Session newSession, final Consumer<Boolean> response) {
        try {
            LabyMod.getInstance().getLabyConnect().getClientConnection().disconnect(false);
            final Minecraft mc = Minecraft.getMinecraft();
            final Field field = ReflectionHelper.findField(Minecraft.class, LabyModCore.getMappingAdapter().getSessionMappings());
            field.setAccessible(true);
            field.set(mc, newSession);
            LabyMod.getInstance().getLabyConnect().getClientConnection().connect();
            Debug.log(Debug.EnumDebugMode.ACCOUNT_MANAGER, "AccountManager: You are now playing with " + newSession.getProfile().getName() + ".");
            response.accept(true);
        }
        catch (final Exception error) {
            error.printStackTrace();
            response.accept(false);
            this.accountManager.throwError(AuthError.EXCEPTION, new String[0]);
        }
    }
    
    private void removeFinal(final Field field) throws Exception {
        field.setAccessible(true);
        int modData = field.getModifiers();
        final Field fieldModification = field.getClass().getDeclaredField("modifiers");
        modData &= 0xFFFFFFEF;
        fieldModification.setAccessible(true);
        fieldModification.setInt(field, modData);
    }
    
    public void authenticatePassword(final String username, final String password, final Consumer<Session> callback) {
        try {
            final HttpAuthenticationService auth = new YggdrasilAuthenticationService(Proxy.NO_PROXY, this.accountManager.getClientToken());
            final UserAuthentication authentification = auth.createUserAuthentication(Agent.MINECRAFT);
            authentification.setUsername(username);
            authentification.setPassword(password);
            authentification.logIn();
            if (authentification.canPlayOnline()) {
                final Session session = new Session(authentification.getSelectedProfile().getName(), authentification.getSelectedProfile().getId().toString(), authentification.getAuthenticatedToken(), Session.Type.MOJANG.toString());
                callback.accept(session);
            }
            else {
                callback.accept(null);
                this.accountManager.throwError(AuthError.NOT_READY_TO_PLAY, new String[0]);
            }
        }
        catch (final Exception error) {
            error.printStackTrace();
            callback.accept(null);
            this.accountManager.throwError(AuthError.EXCEPTION, error.getMessage());
        }
    }
    
    public void handleDirectLogin(final String username, final String password, final boolean saveAccount, final Consumer<Boolean> result) {
        this.authenticatePassword(username, password, new Consumer<Session>() {
            @Override
            public void accept(final Session session) {
                if (session == null) {
                    result.accept(false);
                }
                else {
                    final List<LauncherProfile> launcherProfiles = AccountLoginHandler.this.accountManager.getLauncherProfiles();
                    LauncherProfile targetProfile = null;
                    for (final LauncherProfile launcherProfile : launcherProfiles) {
                        if (session.getProfile().getId().equals(launcherProfile.getGameProfile().getId())) {
                            targetProfile = launcherProfile;
                            break;
                        }
                    }
                    if (targetProfile == null) {
                        if (saveAccount) {
                            AccountLoginHandler.this.accountManager.addAccount(username, session);
                        }
                    }
                    else {
                        targetProfile.setAccessToken(session.getToken());
                    }
                    AccountLoginHandler.this.accountManager.saveLauncherProfiles(new Consumer<Boolean>() {
                        @Override
                        public void accept(final Boolean accepted) {
                            if (accepted) {
                                AccountLoginHandler.this.setSession(session, result);
                            }
                            else {
                                result.accept(false);
                            }
                        }
                    });
                }
            }
        });
    }
    
    public void handleOfflineLogin(final String username, final boolean saveAccount, final Consumer<Boolean> result) {
        UUIDFetcher.getUUID(username, new Consumer<UUID>() {
            @Override
            public void accept(final UUID uuid) {
                if (uuid == null) {
                    AccountLoginHandler.this.accountManager.throwError(AuthError.EXCEPTION, "Username not found");
                    result.accept(false);
                }
                else {
                    final Session session = new Session(username, uuid.toString(), "", Session.Type.MOJANG.toString());
                    final List<LauncherProfile> launcherProfiles = AccountLoginHandler.this.accountManager.getLauncherProfiles();
                    LauncherProfile targetProfile = null;
                    for (final LauncherProfile launcherProfile : launcherProfiles) {
                        if (session.getProfile().getId().equals(launcherProfile.getGameProfile().getId())) {
                            targetProfile = launcherProfile;
                            break;
                        }
                    }
                    if (targetProfile == null && saveAccount) {
                        AccountLoginHandler.this.accountManager.addAccount(username, session);
                    }
                    AccountLoginHandler.this.accountManager.saveLauncherProfiles(new Consumer<Boolean>() {
                        @Override
                        public void accept(final Boolean accepted) {
                            if (accepted) {
                                AccountLoginHandler.this.setSession(session, result);
                            }
                            else {
                                result.accept(false);
                            }
                        }
                    });
                }
            }
        });
    }
    
    private String performPostRequest(final URL url, final String post) throws Exception {
        final HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setConnectTimeout(15000);
        connection.setReadTimeout(15000);
        connection.setUseCaches(false);
        final byte[] postAsBytes = post.getBytes(Charsets.UTF_8);
        connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        connection.setRequestProperty("Content-Length", new StringBuilder().append(postAsBytes.length).toString());
        connection.setDoOutput(true);
        OutputStream outputStream = null;
        try {
            outputStream = connection.getOutputStream();
            IOUtils.write(postAsBytes, outputStream);
        }
        finally {
            IOUtils.closeQuietly(outputStream);
        }
        IOUtils.closeQuietly(outputStream);
        InputStream inputStream = null;
        try {
            inputStream = connection.getInputStream();
            final String result = IOUtils.toString(inputStream, Charsets.UTF_8);
            return result;
        }
        catch (final IOException e) {
            IOUtils.closeQuietly(inputStream);
            inputStream = connection.getErrorStream();
            if (inputStream != null) {
                final String result2 = IOUtils.toString(inputStream, Charsets.UTF_8);
                return result2;
            }
            throw e;
        }
        finally {
            IOUtils.closeQuietly(inputStream);
        }
    }
    
    public static class ReflectionHelper
    {
        public static Field findField(final Class<?> clazz, final String... fieldNames) {
            Exception failed = null;
            final int length = fieldNames.length;
            int i = 0;
            while (i < length) {
                final String fieldName = fieldNames[i];
                try {
                    final Field f = clazz.getDeclaredField(fieldName);
                    f.setAccessible(true);
                    return f;
                }
                catch (final Exception e) {
                    failed = e;
                    ++i;
                }
            }
            throw new UnableToFindFieldException(fieldNames, failed);
        }
        
        public static <T, E> T getPrivateValue(final Class<? super E> classToAccess, final E instance, final int fieldIndex) {
            try {
                final Field f = classToAccess.getDeclaredFields()[fieldIndex];
                f.setAccessible(true);
                return (T)f.get(instance);
            }
            catch (final Exception e) {
                throw new UnableToAccessFieldException(new String[0], e);
            }
        }
        
        public static <T, E> T getPrivateValue(final Class<? super E> classToAccess, final E instance, final String... fieldNames) {
            try {
                return (T)findField(classToAccess, fieldNames).get(instance);
            }
            catch (final Exception e) {
                throw new UnableToAccessFieldException(fieldNames, e);
            }
        }
        
        public static <T, E> void setPrivateValue(final Class<? super T> classToAccess, final T instance, final E value, final int fieldIndex) {
            try {
                final Field f = classToAccess.getDeclaredFields()[fieldIndex];
                f.setAccessible(true);
                f.set(instance, value);
            }
            catch (final Exception e) {
                throw new UnableToAccessFieldException(new String[0], e);
            }
        }
        
        public static <T, E> void setPrivateValue(final Class<? super T> classToAccess, final T instance, final E value, final String... fieldNames) {
            try {
                findField(classToAccess, fieldNames).set(instance, value);
            }
            catch (final Exception e) {
                throw new UnableToAccessFieldException(fieldNames, e);
            }
        }
        
        public static Class<? super Object> getClass(final ClassLoader loader, final String... classNames) {
            Exception err = null;
            final int length = classNames.length;
            int i = 0;
            while (i < length) {
                final String className = classNames[i];
                try {
                    return (Class<? super Object>)Class.forName(className, false, loader);
                }
                catch (final Exception e) {
                    err = e;
                    ++i;
                }
            }
            throw new UnableToFindClassException(classNames, err);
        }
        
        public static <E> Method findMethod(final Class<? super E> clazz, final E instance, final String[] methodNames, final Class<?>... methodTypes) {
            Exception failed = null;
            final int length = methodNames.length;
            int i = 0;
            while (i < length) {
                final String methodName = methodNames[i];
                try {
                    final Method m = clazz.getDeclaredMethod(methodName, methodTypes);
                    m.setAccessible(true);
                    return m;
                }
                catch (final Exception e) {
                    failed = e;
                    ++i;
                }
            }
            throw new UnableToFindMethodException(methodNames, failed);
        }
        
        public static class UnableToFindMethodException extends RuntimeException
        {
            private static final long serialVersionUID = 1L;
            private String[] methodNames;
            
            public UnableToFindMethodException(final String[] methodNames, final Exception failed) {
                super(failed);
                this.methodNames = methodNames;
            }
        }
        
        public static class UnableToFindClassException extends RuntimeException
        {
            private static final long serialVersionUID = 1L;
            private String[] classNames;
            
            public UnableToFindClassException(final String[] classNames, final Exception err) {
                super(err);
                this.classNames = classNames;
            }
        }
        
        public static class UnableToAccessFieldException extends RuntimeException
        {
            private static final long serialVersionUID = 1L;
            private String[] fieldNameList;
            
            public UnableToAccessFieldException(final String[] fieldNames, final Exception e) {
                super(e);
                this.fieldNameList = fieldNames;
            }
        }
        
        public static class UnableToFindFieldException extends RuntimeException
        {
            private static final long serialVersionUID = 1L;
            private String[] fieldNameList;
            
            public UnableToFindFieldException(final String[] fieldNameList, final Exception e) {
                super(e);
                this.fieldNameList = fieldNameList;
            }
        }
    }
}

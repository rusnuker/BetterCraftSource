// 
// Decompiled by Procyon v0.6.0
// 

package optifine;

import java.lang.reflect.Field;

public class FieldLocatorName implements IFieldLocator
{
    private ReflectorClass reflectorClass;
    private String targetFieldName;
    
    public FieldLocatorName(final ReflectorClass p_i38_1_, final String p_i38_2_) {
        this.reflectorClass = null;
        this.targetFieldName = null;
        this.reflectorClass = p_i38_1_;
        this.targetFieldName = p_i38_2_;
    }
    
    @Override
    public Field getField() {
        final Class oclass = this.reflectorClass.getTargetClass();
        if (oclass == null) {
            return null;
        }
        try {
            final Field field = this.getDeclaredField(oclass, this.targetFieldName);
            field.setAccessible(true);
            return field;
        }
        catch (final NoSuchFieldException var3) {
            Config.log("(Reflector) Field not present: " + oclass.getName() + "." + this.targetFieldName);
            return null;
        }
        catch (final SecurityException securityexception) {
            securityexception.printStackTrace();
            return null;
        }
        catch (final Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }
    
    private Field getDeclaredField(final Class p_getDeclaredField_1_, final String p_getDeclaredField_2_) throws NoSuchFieldException {
        final Field[] afield = p_getDeclaredField_1_.getDeclaredFields();
        for (int i = 0; i < afield.length; ++i) {
            final Field field = afield[i];
            if (field.getName().equals(p_getDeclaredField_2_)) {
                return field;
            }
        }
        if (p_getDeclaredField_1_ == Object.class) {
            throw new NoSuchFieldException(p_getDeclaredField_2_);
        }
        return this.getDeclaredField(p_getDeclaredField_1_.getSuperclass(), p_getDeclaredField_2_);
    }
}

// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.kyori.examination;

public abstract class ExaminableProperty
{
    private ExaminableProperty() {
    }
    
    public abstract String name();
    
    public abstract <R> R examine(final Examiner<? extends R> examiner);
    
    @Override
    public String toString() {
        return "ExaminableProperty{" + this.name() + "}";
    }
    
    public static ExaminableProperty of(final String name, final Object value) {
        return new ExaminableProperty() {
            @Override
            public String name() {
                return name;
            }
            
            @Override
            public <R> R examine(final Examiner<? extends R> examiner) {
                return (R)examiner.examine(value);
            }
        };
    }
    
    public static ExaminableProperty of(final String name, final String value) {
        return new ExaminableProperty() {
            @Override
            public String name() {
                return name;
            }
            
            @Override
            public <R> R examine(final Examiner<? extends R> examiner) {
                return (R)examiner.examine(value);
            }
        };
    }
    
    public static ExaminableProperty of(final String name, final boolean value) {
        return new ExaminableProperty() {
            @Override
            public String name() {
                return name;
            }
            
            @Override
            public <R> R examine(final Examiner<? extends R> examiner) {
                return (R)examiner.examine(value);
            }
        };
    }
    
    public static ExaminableProperty of(final String name, final boolean[] value) {
        return new ExaminableProperty() {
            @Override
            public String name() {
                return name;
            }
            
            @Override
            public <R> R examine(final Examiner<? extends R> examiner) {
                return (R)examiner.examine(value);
            }
        };
    }
    
    public static ExaminableProperty of(final String name, final byte value) {
        return new ExaminableProperty() {
            @Override
            public String name() {
                return name;
            }
            
            @Override
            public <R> R examine(final Examiner<? extends R> examiner) {
                return (R)examiner.examine(value);
            }
        };
    }
    
    public static ExaminableProperty of(final String name, final byte[] value) {
        return new ExaminableProperty() {
            @Override
            public String name() {
                return name;
            }
            
            @Override
            public <R> R examine(final Examiner<? extends R> examiner) {
                return (R)examiner.examine(value);
            }
        };
    }
    
    public static ExaminableProperty of(final String name, final char value) {
        return new ExaminableProperty() {
            @Override
            public String name() {
                return name;
            }
            
            @Override
            public <R> R examine(final Examiner<? extends R> examiner) {
                return (R)examiner.examine(value);
            }
        };
    }
    
    public static ExaminableProperty of(final String name, final char[] value) {
        return new ExaminableProperty() {
            @Override
            public String name() {
                return name;
            }
            
            @Override
            public <R> R examine(final Examiner<? extends R> examiner) {
                return (R)examiner.examine(value);
            }
        };
    }
    
    public static ExaminableProperty of(final String name, final double value) {
        return new ExaminableProperty() {
            @Override
            public String name() {
                return name;
            }
            
            @Override
            public <R> R examine(final Examiner<? extends R> examiner) {
                return (R)examiner.examine(value);
            }
        };
    }
    
    public static ExaminableProperty of(final String name, final double[] value) {
        return new ExaminableProperty() {
            @Override
            public String name() {
                return name;
            }
            
            @Override
            public <R> R examine(final Examiner<? extends R> examiner) {
                return (R)examiner.examine(value);
            }
        };
    }
    
    public static ExaminableProperty of(final String name, final float value) {
        return new ExaminableProperty() {
            @Override
            public String name() {
                return name;
            }
            
            @Override
            public <R> R examine(final Examiner<? extends R> examiner) {
                return (R)examiner.examine(value);
            }
        };
    }
    
    public static ExaminableProperty of(final String name, final float[] value) {
        return new ExaminableProperty() {
            @Override
            public String name() {
                return name;
            }
            
            @Override
            public <R> R examine(final Examiner<? extends R> examiner) {
                return (R)examiner.examine(value);
            }
        };
    }
    
    public static ExaminableProperty of(final String name, final int value) {
        return new ExaminableProperty() {
            @Override
            public String name() {
                return name;
            }
            
            @Override
            public <R> R examine(final Examiner<? extends R> examiner) {
                return (R)examiner.examine(value);
            }
        };
    }
    
    public static ExaminableProperty of(final String name, final int[] value) {
        return new ExaminableProperty() {
            @Override
            public String name() {
                return name;
            }
            
            @Override
            public <R> R examine(final Examiner<? extends R> examiner) {
                return (R)examiner.examine(value);
            }
        };
    }
    
    public static ExaminableProperty of(final String name, final long value) {
        return new ExaminableProperty() {
            @Override
            public String name() {
                return name;
            }
            
            @Override
            public <R> R examine(final Examiner<? extends R> examiner) {
                return (R)examiner.examine(value);
            }
        };
    }
    
    public static ExaminableProperty of(final String name, final long[] value) {
        return new ExaminableProperty() {
            @Override
            public String name() {
                return name;
            }
            
            @Override
            public <R> R examine(final Examiner<? extends R> examiner) {
                return (R)examiner.examine(value);
            }
        };
    }
    
    public static ExaminableProperty of(final String name, final short value) {
        return new ExaminableProperty() {
            @Override
            public String name() {
                return name;
            }
            
            @Override
            public <R> R examine(final Examiner<? extends R> examiner) {
                return (R)examiner.examine(value);
            }
        };
    }
    
    public static ExaminableProperty of(final String name, final short[] value) {
        return new ExaminableProperty() {
            @Override
            public String name() {
                return name;
            }
            
            @Override
            public <R> R examine(final Examiner<? extends R> examiner) {
                return (R)examiner.examine(value);
            }
        };
    }
}

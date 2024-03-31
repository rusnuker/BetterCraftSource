// 
// Decompiled by Procyon v0.6.0
// 

package org.cef;

public class CefSettings
{
    public String browser_subprocess_path;
    public boolean windowless_rendering_enabled;
    public boolean command_line_args_disabled;
    public String cache_path;
    public boolean persist_session_cookies;
    public String user_agent;
    public String product_version;
    public String locale;
    public String log_file;
    public LogSeverity log_severity;
    public String javascript_flags;
    public String resources_dir_path;
    public String locales_dir_path;
    public boolean pack_loading_disabled;
    public int remote_debugging_port;
    public int uncaught_exception_stack_size;
    public boolean ignore_certificate_errors;
    public ColorType background_color;
    
    public CefSettings() {
        this.browser_subprocess_path = null;
        this.windowless_rendering_enabled = true;
        this.command_line_args_disabled = false;
        this.cache_path = null;
        this.persist_session_cookies = false;
        this.user_agent = null;
        this.product_version = null;
        this.locale = null;
        this.log_file = null;
        this.log_severity = LogSeverity.LOGSEVERITY_DEFAULT;
        this.javascript_flags = null;
        this.resources_dir_path = null;
        this.locales_dir_path = null;
        this.pack_loading_disabled = false;
        this.remote_debugging_port = 0;
        this.uncaught_exception_stack_size = 0;
        this.ignore_certificate_errors = false;
        this.background_color = null;
    }
    
    public CefSettings clone() {
        final CefSettings tmp = new CefSettings();
        tmp.browser_subprocess_path = this.browser_subprocess_path;
        tmp.windowless_rendering_enabled = this.windowless_rendering_enabled;
        tmp.command_line_args_disabled = this.command_line_args_disabled;
        tmp.cache_path = this.cache_path;
        tmp.persist_session_cookies = this.persist_session_cookies;
        tmp.user_agent = this.user_agent;
        tmp.product_version = this.product_version;
        tmp.locale = this.locale;
        tmp.log_file = this.log_file;
        tmp.log_severity = this.log_severity;
        tmp.javascript_flags = this.javascript_flags;
        tmp.resources_dir_path = this.resources_dir_path;
        tmp.locales_dir_path = this.locales_dir_path;
        tmp.pack_loading_disabled = this.pack_loading_disabled;
        tmp.remote_debugging_port = this.remote_debugging_port;
        tmp.uncaught_exception_stack_size = this.uncaught_exception_stack_size;
        tmp.ignore_certificate_errors = this.ignore_certificate_errors;
        if (this.background_color != null) {
            tmp.background_color = this.background_color.clone();
        }
        return tmp;
    }
    
    public class ColorType
    {
        private long color_value;
        
        private ColorType() {
            this.color_value = 0L;
        }
        
        public ColorType(final int alpha, final int red, final int green, final int blue) {
            this.color_value = 0L;
            this.color_value = (alpha << 24 | red << 16 | green << 8 | blue << 0);
        }
        
        public long getColor() {
            return this.color_value;
        }
        
        public ColorType clone() {
            final ColorType res = new ColorType();
            res.color_value = this.color_value;
            return res;
        }
    }
    
    public enum LogSeverity
    {
        LOGSEVERITY_DEFAULT("LOGSEVERITY_DEFAULT", 0), 
        LOGSEVERITY_VERBOSE("LOGSEVERITY_VERBOSE", 1), 
        LOGSEVERITY_INFO("LOGSEVERITY_INFO", 2), 
        LOGSEVERITY_WARNING("LOGSEVERITY_WARNING", 3), 
        LOGSEVERITY_ERROR("LOGSEVERITY_ERROR", 4), 
        LOGSEVERITY_FATAL("LOGSEVERITY_FATAL", 5), 
        LOGSEVERITY_DISABLE("LOGSEVERITY_DISABLE", 6);
        
        private LogSeverity(final String s, final int n) {
        }
    }
}

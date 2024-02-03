// 
// Decompiled by Procyon v0.6.0
// 

package tv.twitch;

import java.util.Iterator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum ErrorCode
{
    TTV_EC_SUCCESS(0), 
    TTV_EC_UNKNOWN_ERROR(1), 
    TTV_EC_CANNOT_OPEN_FILE(2), 
    TTV_EC_ALREADY_INITIALIZED(3), 
    TTV_EC_CANNOT_WRITE_TO_FILE(4), 
    TTV_EC_CANNOT_CREATE_MUTEX(5), 
    TTV_EC_CANNOT_DESTROY_MUTEX(6), 
    TTV_EC_COULD_NOT_TAKE_MUTEX(7), 
    TTV_EC_COULD_NOT_RELEASE_MUTEX(8), 
    TTV_EC_INVALID_MUTEX(9), 
    TTV_EC_WAIT_TIMED_OUT(10), 
    TTV_EC_INVALID_ARG(11), 
    TTV_EC_NOT_INITIALIZED(12), 
    TTV_EC_AUTHENTICATION(13), 
    TTV_EC_INVALID_AUTHTOKEN(14), 
    TTV_EC_MEMORY(15), 
    TTV_EC_ALIGN16_REQUIRED(16), 
    TTV_EC_UNSUPPORTED_INPUT_FORMAT(17), 
    TTV_EC_UNSUPPORTED_OUTPUT_FORMAT(18), 
    TTV_EC_INVALID_RESOLUTION(19), 
    TTV_EC_INVALID_FPS(20), 
    TTV_EC_INVALID_BITRATE(21), 
    TTV_EC_FAILED_TO_INIT_SPEAKER_CAPTURE(22), 
    TTV_EC_FRAME_QUEUE_FULL(23), 
    TTV_EC_HTTPREQUEST_ERROR(24), 
    TTV_EC_INVALID_CLIENTID(25), 
    TTV_EC_INVALID_CHANNEL_NAME(26), 
    TTV_EC_API_REQUEST_FAILED(27), 
    TTV_EC_API_REQUEST_TIMEDOUT(28), 
    TTV_EC_INVALID_HTTP_REQUEST_PARAMS(29), 
    TTV_EC_COINITIALIZE_FAIED(30), 
    TTV_EC_WEBAPI_RESULT_INVALID_JSON(31), 
    TTV_EC_WEBAPI_RESULT_NO_AUTHTOKEN(32), 
    TTV_EC_WEBAPI_RESULT_NO_STREAMKEY(33), 
    TTV_EC_WEBAPI_RESULT_NO_CHANNELNAME(34), 
    TTV_EC_WEBAPI_RESULT_NO_INGESTS(35), 
    TTV_EC_WEBAPI_RESULT_NO_RECORDING_STATUS(36), 
    TTV_EC_WEBAPI_RESULT_NO_STREAMINFO(37), 
    TTV_EC_WEBAPI_RESULT_INVALID_VIEWERS(38), 
    TTV_EC_WEBAPI_RESULT_NO_USERNAME(39), 
    TTV_EC_WEBAPI_RESULT_NO_USER_DISPLAY_NAME(40), 
    TTV_EC_NO_STREAM_KEY(41), 
    TTV_EC_NEED_TO_LOGIN(42), 
    TTV_EC_INVALID_VIDEOFRAME(43), 
    TTV_EC_INVALID_BUFFER(44), 
    TTV_EC_INVALID_CALLBACK(45), 
    TTV_EC_INVALID_JSON(46), 
    TTV_EC_NO_SPSPPS(47), 
    TTV_EC_NO_D3D_SUPPORT(48), 
    TTV_EC_NO_INGEST_SERVER_AVAILABLE(49), 
    TTV_EC_INVALID_INGEST_SERVER(50), 
    TTV_EC_CANNOT_SUSPEND_THREADSYNC(51), 
    TTV_EC_CANNOT_SIGNAL_THREADSYNC(52), 
    TTV_EC_INVALID_ENCODER(53), 
    TTV_EC_AUDIO_DEVICE_INIT_FAILED(54), 
    TTV_EC_AUDIO_BUFFER_TOO_BIG(55), 
    TTV_EC_AUDIO_PASSTHROUGH_NOT_ENABLED(56), 
    TTV_EC_INVALID_SAMPLERATE(57), 
    TTV_EC_X264_INVALID_PRESET(58), 
    TTV_EC_X264_INVALID_PROFILE(59), 
    TTV_EC_FLV_UNABLE_TO_OPEN_FILE(60), 
    TTV_EC_FLV_FILE_NOT_OPEN(61), 
    TTV_EC_FLV_UNSUPPORTED_AUDIO_RATE(62), 
    TTV_EC_FLV_UNSUPPORTED_AUDIO_CODEC(63), 
    TTV_EC_RTMP_REJECTED(64), 
    TTV_EC_RTMP_WRONG_PROTOCOL_IN_URL(65), 
    TTV_EC_RTMP_UNABLE_TO_SEND_DATA(66), 
    TTV_EC_RTMP_INVALID_FLV_PACKET(67), 
    TTV_EC_RTMP_TIMEOUT(68), 
    TTV_EC_MAC_INPUT_Q_SETUP_FAILED(69), 
    TTV_EC_MAC_INPUT_Q_BUFFER_SETUP_FAILED(70), 
    TTV_EC_MAC_INPUT_Q_START_FAILED(71), 
    TTV_EC_INTEL_FAILED_SESSION_INIT(72), 
    TTV_EC_INTEL_FAILED_VPP_INIT(73), 
    TTV_EC_INTEL_FAILED_ENCODER_INIT(74), 
    TTV_EC_INTEL_FAILED_SURFACE_ALLOCATION(75), 
    TTV_EC_INTEL_FAILED_TASKPOLL_INIT(76), 
    TTV_EC_INTEL_NO_FREE_TASK(77), 
    TTV_EC_INTEL_NO_FREE_SURFACE(78), 
    TTV_EC_APPLEENCODER_FAILED_START(79), 
    TTV_EC_APPLEENCODER_FAILED_FRAME_SUBMISSION(80), 
    TTV_EC_LAMEMP3_FAILED_INIT(81), 
    TTV_EC_LAMEMP3_FAILED_SHUTDOWN(82), 
    TTV_EC_APPLEAAC_FAILED_INIT(83), 
    TTV_EC_APPLEAAC_FAILED_ENCODING(84), 
    TTV_EC_APPLEAAC_FAILED_SHUTDOWN(85), 
    TTV_EC_REQUEST_PENDING(86), 
    TTV_EC_WSASTARTUP_FAILED(87), 
    TTV_EC_WSACLEANUP_FAILED(88), 
    TTV_EC_SOCKET_GETADDRINFO_FAILED(89), 
    TTV_EC_SOCKET_CREATE_FAILED(90), 
    TTV_EC_SOCKET_CONNECT_FAILED(91), 
    TTV_EC_SOCKET_SEND_ERROR(92), 
    TTV_EC_SOCKET_RECV_ERROR(93), 
    TTV_EC_SOCKET_IOCTL_ERROR(94), 
    TTV_EC_OS_TOO_OLD(95), 
    TTV_EC_SOCKET_ERR(1000), 
    TTV_EC_SOCKET_EINTR(1004), 
    TTV_EC_SOCKET_EBADF(1009), 
    TTV_EC_SOCKET_EACCES(1013), 
    TTV_EC_SOCKET_EFAULT(1014), 
    TTV_EC_SOCKET_EINVAL(1022), 
    TTV_EC_SOCKET_EMFILE(1024), 
    TTV_EC_SOCKET_EWOULDBLOCK(1035), 
    TTV_EC_SOCKET_EINPROGRESS(1036), 
    TTV_EC_SOCKET_EALREADY(1037), 
    TTV_EC_SOCKET_ENOTSOCK(1038), 
    TTV_EC_SOCKET_EDESTADDRREQ(1039), 
    TTV_EC_SOCKET_EMSGSIZE(1040), 
    TTV_EC_SOCKET_EPROTOTYPE(1041), 
    TTV_EC_SOCKET_ENOPROTOOPT(1042), 
    TTV_EC_SOCKET_EPROTONOSUPPORT(1043), 
    TTV_EC_SOCKET_ESOCKTNOSUPPORT(1044), 
    TTV_EC_SOCKET_EOPNOTSUPP(1045), 
    TTV_EC_SOCKET_EPFNOSUPPORT(1046), 
    TTV_EC_SOCKET_EAFNOSUPPORT(1047), 
    TTV_EC_SOCKET_EADDRINUSE(1048), 
    TTV_EC_SOCKET_EADDRNOTAVAIL(1049), 
    TTV_EC_SOCKET_ENETDOWN(1050), 
    TTV_EC_SOCKET_ENETUNREACH(1051), 
    TTV_EC_SOCKET_ENETRESET(1052), 
    TTV_EC_SOCKET_ECONNABORTED(1053), 
    TTV_EC_SOCKET_ECONNRESET(1054), 
    TTV_EC_SOCKET_ENOBUFS(1055), 
    TTV_EC_SOCKET_EISCONN(1056), 
    TTV_EC_SOCKET_ENOTCONN(1057), 
    TTV_EC_SOCKET_ESHUTDOWN(1058), 
    TTV_EC_SOCKET_ETOOMANYREFS(1059), 
    TTV_EC_SOCKET_ETIMEDOUT(1060), 
    TTV_EC_SOCKET_ECONNREFUSED(1061), 
    TTV_EC_SOCKET_ELOOP(1062), 
    TTV_EC_SOCKET_ENAMETOOLONG(1063), 
    TTV_EC_SOCKET_EHOSTDOWN(1064), 
    TTV_EC_SOCKET_EHOSTUNREACH(1065), 
    TTV_EC_SOCKET_ENOTEMPTY(1066), 
    TTV_EC_SOCKET_EPROCLIM(1067), 
    TTV_EC_SOCKET_EUSERS(1068), 
    TTV_EC_SOCKET_EDQUOT(1069), 
    TTV_EC_SOCKET_ESTALE(1070), 
    TTV_EC_SOCKET_EREMOTE(1071), 
    TTV_EC_SOCKET_SYSNOTREADY(1091), 
    TTV_EC_SOCKET_VERNOTSUPPORTED(1092), 
    TTV_EC_SOCKET_NOTINITIALISED(1093), 
    TTV_EC_SOCKET_EDISCON(1101), 
    TTV_EC_SOCKET_ENOMORE(1102), 
    TTV_EC_SOCKET_ECANCELLED(1103), 
    TTV_EC_SOCKET_EINVALIDPROCTABLE(1104), 
    TTV_EC_SOCKET_EINVALIDPROVIDER(1105), 
    TTV_EC_SOCKET_EPROVIDERFAILEDINIT(1106), 
    TTV_EC_SOCKET_SYSCALLFAILURE(1107), 
    TTV_EC_SOCKET_SERVICE_NOT_FOUND(1108), 
    TTV_EC_SOCKET_TYPE_NOT_FOUND(1109), 
    TTV_EC_SOCKET_E_NO_MORE(1110), 
    TTV_EC_SOCKET_E_CANCELLED(1111), 
    TTV_EC_SOCKET_EREFUSED(1112), 
    TTV_EC_SOCKET_HOST_NOT_FOUND(2001), 
    TTV_EC_SOCKET_TRY_AGAIN(2002), 
    TTV_EC_SOCKET_NO_RECOVERY(2003), 
    TTV_EC_SOCKET_NO_DATA(2004), 
    TTV_EC_SOCKET_QOS_RECEIVERS(2005), 
    TTV_EC_SOCKET_QOS_SENDERS(2006), 
    TTV_EC_SOCKET_QOS_NO_SENDERS(2007), 
    TTV_EC_SOCKET_QOS_NO_RECEIVERS(2008), 
    TTV_EC_SOCKET_QOS_REQUEST_CONFIRMED(2009), 
    TTV_EC_SOCKET_QOS_ADMISSION_FAILURE(2010), 
    TTV_EC_SOCKET_QOS_POLICY_FAILURE(2011), 
    TTV_EC_SOCKET_QOS_BAD_STYLE(2012), 
    TTV_EC_SOCKET_QOS_BAD_OBJECT(2013), 
    TTV_EC_SOCKET_QOS_TRAFFIC_CTRL_ERROR(2014), 
    TTV_EC_SOCKET_QOS_GENERIC_ERROR(2015), 
    TTV_EC_SOCKET_QOS_ESERVICETYPE(2016), 
    TTV_EC_SOCKET_QOS_EFLOWSPEC(2017), 
    TTV_EC_SOCKET_QOS_EPROVSPECBUF(2018), 
    TTV_EC_SOCKET_QOS_EFILTERSTYLE(2019), 
    TTV_EC_SOCKET_QOS_EFILTERTYPE(2020), 
    TTV_EC_SOCKET_QOS_EFILTERCOUNT(2021), 
    TTV_EC_SOCKET_QOS_EOBJLENGTH(2022), 
    TTV_EC_SOCKET_QOS_EFLOWCOUNT(2023), 
    TTV_EC_SOCKET_QOS_EUNKOWNPSOBJ(2024), 
    TTV_EC_SOCKET_QOS_EPOLICYOBJ(2025), 
    TTV_EC_SOCKET_QOS_EFLOWDESC(2026), 
    TTV_EC_SOCKET_QOS_EPSFLOWSPEC(2027), 
    TTV_EC_SOCKET_QOS_EPSFILTERSPEC(2028), 
    TTV_EC_SOCKET_QOS_ESDMODEOBJ(2029), 
    TTV_EC_SOCKET_QOS_ESHAPERATEOBJ(2030), 
    TTV_EC_SOCKET_QOS_RESERVED_PETYPE(2031), 
    TTV_EC_SOCKET_SECURE_HOST_NOT_FOUND(2032), 
    TTV_EC_SOCKET_IPSEC_NAME_POLICY_ERROR(2033), 
    TTV_EC_SOCKET_END(2034), 
    TTV_EC_CHAT_NOT_INITIALIZED(2035), 
    TTV_EC_CHAT_ALREADY_INITIALIZED(2036), 
    TTV_EC_CHAT_ALREADY_IN_CHANNEL(2037), 
    TTV_EC_CHAT_INVALID_LOGIN(2038), 
    TTV_EC_CHAT_INVALID_CHANNEL(2039), 
    TTV_EC_CHAT_LOST_CONNECTION(2040), 
    TTV_EC_CHAT_COULD_NOT_CONNECT(2041), 
    TTV_EC_CHAT_NOT_IN_CHANNEL(2042), 
    TTV_EC_CHAT_INVALID_MESSAGE(2043), 
    TTV_EC_CHAT_TOO_MANY_REQUESTS(2044), 
    TTV_EC_CHAT_LEAVING_CHANNEL(2045), 
    TTV_EC_CHAT_SHUTTING_DOWN(2046), 
    TTV_EC_CHAT_ANON_DENIED(2047), 
    TTV_EC_CHAT_EMOTICON_DATA_NOT_READY(2048), 
    TTV_EC_CHAT_EMOTICON_DATA_DOWNLOADING(2049), 
    TTV_EC_CHAT_EMOTICON_DATA_LOCKED(2050), 
    TTV_EC_CHAT_EMOTICON_DOWNLOAD_FAILED(2051), 
    TTV_EC_WEBCAM_NO_PLATFORM_SUPPORT(2052), 
    TTV_EC_WEBCAM_COULD_NOT_COMPLETE(2053), 
    TTV_EC_WEBCAM_OUT_OF_MEMORY(2054), 
    TTV_EC_WEBCAM_UNKNOWN_ERROR(2055), 
    TTV_EC_WEBCAM_INVALID_PARAMETER(2056), 
    TTV_EC_WEBCAM_INVALID_CAPABILITY(2057), 
    TTV_EC_WEBCAM_BUFFER_NOT_BIG_ENOUGH(2058), 
    TTV_EC_WEBCAM_DEVICE_NOT_STARTED(2059), 
    TTV_EC_WEBCAM_DEVICE_ALREADY_STARTED(2060), 
    TTV_EC_WEBCAM_DEVICE_NOT_FOUND(2061), 
    TTV_EC_WEBCAM_FRAME_NOT_AVAILABLE(2062), 
    TTV_EC_WEBCAM_NOT_INITIALIZED(2063), 
    TTV_EC_WEBCAM_FAILED_TO_START(2064), 
    TTV_EC_WEBCAM_LEFT_IN_UNSAFE_STATE(2065), 
    TTV_EC_WEBCAM_UNSUPPORTED_SOURCE_FORMAT(2066), 
    TTV_EC_WEBCAM_UNSUPPORTED_TARGET_FORMAT(2067), 
    TTV_EC_INVALID_STRUCT_SIZE(2068), 
    TTV_EC_STREAM_ALREADY_STARTED(2069), 
    TTV_EC_STREAM_NOT_STARTED(2070), 
    TTV_EC_REQUEST_ABORTED(2071), 
    TTV_EC_FRAME_QUEUE_TOO_LONG(2072), 
    TTV_EC_GRAPHICS_API_ERROR(2073), 
    TTV_EC_METADATA_CACHE_FULL(2074), 
    TTV_EC_SOUNDFLOWER_NOT_INSTALLED(2075), 
    TTV_EC_STILL_IN_USE(2076), 
    TTV_EC_NO_ENCODER_PLUGIN(2077), 
    TTV_EC_WARNING_START(-1000), 
    TTV_WRN_MUTEX_LOCKED(-999), 
    TTV_WRN_FAILED_TO_INIT_MIC_CAPTURE(-998), 
    TTV_WRN_NOTENOUGHDATA(-997), 
    TTV_WRN_NOMOREDATA(-996), 
    TTV_WRN_FRAMES_QUEUEING(-995), 
    TTV_WRN_MUTEX_NOT_AQUIRED(-994), 
    TTV_WRN_PREV_GAME_NAME_MATCH_REQUEST_DROPPED(-993), 
    TTV_WRN_DEPRECATED(-992), 
    TTV_WRN_CHAT_MESSAGE_SPAM_DISCARDED(-991), 
    TTV_WRN_WAIT_TIMEOUT(-990), 
    TTV_WRN_STREAMINFO_PENDING(-989);
    
    private static Map<Integer, ErrorCode> s_Map;
    private int m_Value;
    
    public static ErrorCode lookupValue(final int n) {
        return ErrorCode.s_Map.get(n);
    }
    
    private ErrorCode(final int value) {
        this.m_Value = value;
    }
    
    public int getValue() {
        return this.m_Value;
    }
    
    public static boolean succeeded(final ErrorCode errorCode) {
        return errorCode.getValue() <= ErrorCode.TTV_EC_SUCCESS.getValue();
    }
    
    public static boolean failed(final ErrorCode errorCode) {
        return errorCode.getValue() > ErrorCode.TTV_EC_SUCCESS.getValue();
    }
    
    public static String getString(final ErrorCode errorCode) {
        final Core instance = Core.getInstance();
        if (instance == null) {
            return "Unknown error";
        }
        return instance.errorToString(errorCode);
    }
    
    static {
        ErrorCode.s_Map = new HashMap<Integer, ErrorCode>();
        for (final ErrorCode errorCode : EnumSet.allOf(ErrorCode.class)) {
            ErrorCode.s_Map.put(errorCode.getValue(), errorCode);
        }
    }
}

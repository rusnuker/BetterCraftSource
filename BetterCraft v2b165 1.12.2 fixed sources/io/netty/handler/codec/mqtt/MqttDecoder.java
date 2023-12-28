// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.mqtt;

import io.netty.util.CharsetUtil;
import java.util.ArrayList;
import io.netty.handler.codec.DecoderException;
import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

public final class MqttDecoder extends ReplayingDecoder<DecoderState>
{
    private static final int DEFAULT_MAX_BYTES_IN_MESSAGE = 8092;
    private MqttFixedHeader mqttFixedHeader;
    private Object variableHeader;
    private int bytesRemainingInVariablePart;
    private final int maxBytesInMessage;
    
    public MqttDecoder() {
        this(8092);
    }
    
    public MqttDecoder(final int maxBytesInMessage) {
        super(DecoderState.READ_FIXED_HEADER);
        this.maxBytesInMessage = maxBytesInMessage;
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf buffer, final List<Object> out) throws Exception {
        switch (this.state()) {
            case READ_FIXED_HEADER: {
                this.mqttFixedHeader = decodeFixedHeader(buffer);
                this.bytesRemainingInVariablePart = this.mqttFixedHeader.remainingLength();
                this.checkpoint(DecoderState.READ_VARIABLE_HEADER);
            }
            case READ_VARIABLE_HEADER: {
                try {
                    if (this.bytesRemainingInVariablePart > this.maxBytesInMessage) {
                        throw new DecoderException("too large message: " + this.bytesRemainingInVariablePart + " bytes");
                    }
                    final Result<?> decodedVariableHeader = decodeVariableHeader(buffer, this.mqttFixedHeader);
                    this.variableHeader = ((Result<Object>)decodedVariableHeader).value;
                    this.bytesRemainingInVariablePart -= ((Result<Object>)decodedVariableHeader).numberOfBytesConsumed;
                    this.checkpoint(DecoderState.READ_PAYLOAD);
                }
                catch (final Exception cause) {
                    out.add(this.invalidMessage(cause));
                }
            }
            case READ_PAYLOAD: {
                try {
                    final Result<?> decodedPayload = decodePayload(buffer, this.mqttFixedHeader.messageType(), this.bytesRemainingInVariablePart, this.variableHeader);
                    this.bytesRemainingInVariablePart -= ((Result<Object>)decodedPayload).numberOfBytesConsumed;
                    if (this.bytesRemainingInVariablePart != 0) {
                        throw new DecoderException("non-zero remaining payload bytes: " + this.bytesRemainingInVariablePart + " (" + this.mqttFixedHeader.messageType() + ')');
                    }
                    this.checkpoint(DecoderState.READ_FIXED_HEADER);
                    final MqttMessage message = MqttMessageFactory.newMessage(this.mqttFixedHeader, this.variableHeader, ((Result<Object>)decodedPayload).value);
                    this.mqttFixedHeader = null;
                    this.variableHeader = null;
                    out.add(message);
                    break;
                }
                catch (final Exception cause) {
                    out.add(this.invalidMessage(cause));
                }
            }
            case BAD_MESSAGE: {
                buffer.skipBytes(this.actualReadableBytes());
                break;
            }
            default: {
                throw new Error();
            }
        }
    }
    
    private MqttMessage invalidMessage(final Throwable cause) {
        this.checkpoint(DecoderState.BAD_MESSAGE);
        return MqttMessageFactory.newInvalidMessage(cause);
    }
    
    private static MqttFixedHeader decodeFixedHeader(final ByteBuf buffer) {
        final short b1 = buffer.readUnsignedByte();
        final MqttMessageType messageType = MqttMessageType.valueOf(b1 >> 4);
        final boolean dupFlag = (b1 & 0x8) == 0x8;
        final int qosLevel = (b1 & 0x6) >> 1;
        final boolean retain = (b1 & 0x1) != 0x0;
        int remainingLength = 0;
        int multiplier = 1;
        int loops = 0;
        short digit;
        do {
            digit = buffer.readUnsignedByte();
            remainingLength += (digit & 0x7F) * multiplier;
            multiplier *= 128;
            ++loops;
        } while ((digit & 0x80) != 0x0 && loops < 4);
        if (loops == 4 && (digit & 0x80) != 0x0) {
            throw new DecoderException("remaining length exceeds 4 digits (" + messageType + ')');
        }
        final MqttFixedHeader decodedFixedHeader = new MqttFixedHeader(messageType, dupFlag, MqttQoS.valueOf(qosLevel), retain, remainingLength);
        return MqttCodecUtil.validateFixedHeader(MqttCodecUtil.resetUnusedFields(decodedFixedHeader));
    }
    
    private static Result<?> decodeVariableHeader(final ByteBuf buffer, final MqttFixedHeader mqttFixedHeader) {
        switch (mqttFixedHeader.messageType()) {
            case CONNECT: {
                return decodeConnectionVariableHeader(buffer);
            }
            case CONNACK: {
                return decodeConnAckVariableHeader(buffer);
            }
            case SUBSCRIBE:
            case UNSUBSCRIBE:
            case SUBACK:
            case UNSUBACK:
            case PUBACK:
            case PUBREC:
            case PUBCOMP:
            case PUBREL: {
                return decodeMessageIdVariableHeader(buffer);
            }
            case PUBLISH: {
                return decodePublishVariableHeader(buffer, mqttFixedHeader);
            }
            case PINGREQ:
            case PINGRESP:
            case DISCONNECT: {
                return new Result<Object>(null, 0);
            }
            default: {
                return new Result<Object>(null, 0);
            }
        }
    }
    
    private static Result<MqttConnectVariableHeader> decodeConnectionVariableHeader(final ByteBuf buffer) {
        final Result<String> protoString = decodeString(buffer);
        int numberOfBytesConsumed = ((Result<Object>)protoString).numberOfBytesConsumed;
        final byte protocolLevel = buffer.readByte();
        ++numberOfBytesConsumed;
        final MqttVersion mqttVersion = MqttVersion.fromProtocolNameAndLevel((String)((Result<Object>)protoString).value, protocolLevel);
        final int b1 = buffer.readUnsignedByte();
        ++numberOfBytesConsumed;
        final Result<Integer> keepAlive = decodeMsbLsb(buffer);
        numberOfBytesConsumed += ((Result<Object>)keepAlive).numberOfBytesConsumed;
        final boolean hasUserName = (b1 & 0x80) == 0x80;
        final boolean hasPassword = (b1 & 0x40) == 0x40;
        final boolean willRetain = (b1 & 0x20) == 0x20;
        final int willQos = (b1 & 0x18) >> 3;
        final boolean willFlag = (b1 & 0x4) == 0x4;
        final boolean cleanSession = (b1 & 0x2) == 0x2;
        if (mqttVersion == MqttVersion.MQTT_3_1_1) {
            final boolean zeroReservedFlag = (b1 & 0x1) == 0x0;
            if (!zeroReservedFlag) {
                throw new DecoderException("non-zero reserved flag");
            }
        }
        final MqttConnectVariableHeader mqttConnectVariableHeader = new MqttConnectVariableHeader(mqttVersion.protocolName(), mqttVersion.protocolLevel(), hasUserName, hasPassword, willRetain, willQos, willFlag, cleanSession, (int)((Result<Object>)keepAlive).value);
        return new Result<MqttConnectVariableHeader>(mqttConnectVariableHeader, numberOfBytesConsumed);
    }
    
    private static Result<MqttConnAckVariableHeader> decodeConnAckVariableHeader(final ByteBuf buffer) {
        final boolean sessionPresent = (buffer.readUnsignedByte() & 0x1) == 0x1;
        final byte returnCode = buffer.readByte();
        final int numberOfBytesConsumed = 2;
        final MqttConnAckVariableHeader mqttConnAckVariableHeader = new MqttConnAckVariableHeader(MqttConnectReturnCode.valueOf(returnCode), sessionPresent);
        return new Result<MqttConnAckVariableHeader>(mqttConnAckVariableHeader, 2);
    }
    
    private static Result<MqttMessageIdVariableHeader> decodeMessageIdVariableHeader(final ByteBuf buffer) {
        final Result<Integer> messageId = decodeMessageId(buffer);
        return new Result<MqttMessageIdVariableHeader>(MqttMessageIdVariableHeader.from((int)((Result<Object>)messageId).value), ((Result<Object>)messageId).numberOfBytesConsumed);
    }
    
    private static Result<MqttPublishVariableHeader> decodePublishVariableHeader(final ByteBuf buffer, final MqttFixedHeader mqttFixedHeader) {
        final Result<String> decodedTopic = decodeString(buffer);
        if (!MqttCodecUtil.isValidPublishTopicName((String)((Result<Object>)decodedTopic).value)) {
            throw new DecoderException("invalid publish topic name: " + (String)((Result<Object>)decodedTopic).value + " (contains wildcards)");
        }
        int numberOfBytesConsumed = ((Result<Object>)decodedTopic).numberOfBytesConsumed;
        int messageId = -1;
        if (mqttFixedHeader.qosLevel().value() > 0) {
            final Result<Integer> decodedMessageId = decodeMessageId(buffer);
            messageId = (int)((Result<Object>)decodedMessageId).value;
            numberOfBytesConsumed += ((Result<Object>)decodedMessageId).numberOfBytesConsumed;
        }
        final MqttPublishVariableHeader mqttPublishVariableHeader = new MqttPublishVariableHeader((String)((Result<Object>)decodedTopic).value, messageId);
        return new Result<MqttPublishVariableHeader>(mqttPublishVariableHeader, numberOfBytesConsumed);
    }
    
    private static Result<Integer> decodeMessageId(final ByteBuf buffer) {
        final Result<Integer> messageId = decodeMsbLsb(buffer);
        if (!MqttCodecUtil.isValidMessageId((int)((Result<Object>)messageId).value)) {
            throw new DecoderException("invalid messageId: " + ((Result<Object>)messageId).value);
        }
        return messageId;
    }
    
    private static Result<?> decodePayload(final ByteBuf buffer, final MqttMessageType messageType, final int bytesRemainingInVariablePart, final Object variableHeader) {
        switch (messageType) {
            case CONNECT: {
                return decodeConnectionPayload(buffer, (MqttConnectVariableHeader)variableHeader);
            }
            case SUBSCRIBE: {
                return decodeSubscribePayload(buffer, bytesRemainingInVariablePart);
            }
            case SUBACK: {
                return decodeSubackPayload(buffer, bytesRemainingInVariablePart);
            }
            case UNSUBSCRIBE: {
                return decodeUnsubscribePayload(buffer, bytesRemainingInVariablePart);
            }
            case PUBLISH: {
                return decodePublishPayload(buffer, bytesRemainingInVariablePart);
            }
            default: {
                return new Result<Object>(null, 0);
            }
        }
    }
    
    private static Result<MqttConnectPayload> decodeConnectionPayload(final ByteBuf buffer, final MqttConnectVariableHeader mqttConnectVariableHeader) {
        final Result<String> decodedClientId = decodeString(buffer);
        final String decodedClientIdValue = (String)((Result<Object>)decodedClientId).value;
        final MqttVersion mqttVersion = MqttVersion.fromProtocolNameAndLevel(mqttConnectVariableHeader.name(), (byte)mqttConnectVariableHeader.version());
        if (!MqttCodecUtil.isValidClientId(mqttVersion, decodedClientIdValue)) {
            throw new MqttIdentifierRejectedException("invalid clientIdentifier: " + decodedClientIdValue);
        }
        int numberOfBytesConsumed = ((Result<Object>)decodedClientId).numberOfBytesConsumed;
        Result<String> decodedWillTopic = null;
        Result<String> decodedWillMessage = null;
        if (mqttConnectVariableHeader.isWillFlag()) {
            decodedWillTopic = decodeString(buffer, 0, 32767);
            numberOfBytesConsumed += ((Result<Object>)decodedWillTopic).numberOfBytesConsumed;
            decodedWillMessage = decodeAsciiString(buffer);
            numberOfBytesConsumed += ((Result<Object>)decodedWillMessage).numberOfBytesConsumed;
        }
        Result<String> decodedUserName = null;
        Result<String> decodedPassword = null;
        if (mqttConnectVariableHeader.hasUserName()) {
            decodedUserName = decodeString(buffer);
            numberOfBytesConsumed += ((Result<Object>)decodedUserName).numberOfBytesConsumed;
        }
        if (mqttConnectVariableHeader.hasPassword()) {
            decodedPassword = decodeString(buffer);
            numberOfBytesConsumed += ((Result<Object>)decodedPassword).numberOfBytesConsumed;
        }
        final MqttConnectPayload mqttConnectPayload = new MqttConnectPayload((String)((Result<Object>)decodedClientId).value, (decodedWillTopic != null) ? ((String)((Result<Object>)decodedWillTopic).value) : null, (decodedWillMessage != null) ? ((String)((Result<Object>)decodedWillMessage).value) : null, (decodedUserName != null) ? ((String)((Result<Object>)decodedUserName).value) : null, (decodedPassword != null) ? ((String)((Result<Object>)decodedPassword).value) : null);
        return new Result<MqttConnectPayload>(mqttConnectPayload, numberOfBytesConsumed);
    }
    
    private static Result<MqttSubscribePayload> decodeSubscribePayload(final ByteBuf buffer, final int bytesRemainingInVariablePart) {
        final List<MqttTopicSubscription> subscribeTopics = new ArrayList<MqttTopicSubscription>();
        int numberOfBytesConsumed = 0;
        while (numberOfBytesConsumed < bytesRemainingInVariablePart) {
            final Result<String> decodedTopicName = decodeString(buffer);
            numberOfBytesConsumed += ((Result<Object>)decodedTopicName).numberOfBytesConsumed;
            final int qos = buffer.readUnsignedByte() & 0x3;
            ++numberOfBytesConsumed;
            subscribeTopics.add(new MqttTopicSubscription((String)((Result<Object>)decodedTopicName).value, MqttQoS.valueOf(qos)));
        }
        return new Result<MqttSubscribePayload>(new MqttSubscribePayload(subscribeTopics), numberOfBytesConsumed);
    }
    
    private static Result<MqttSubAckPayload> decodeSubackPayload(final ByteBuf buffer, final int bytesRemainingInVariablePart) {
        final List<Integer> grantedQos = new ArrayList<Integer>();
        int numberOfBytesConsumed = 0;
        while (numberOfBytesConsumed < bytesRemainingInVariablePart) {
            final int qos = buffer.readUnsignedByte() & 0x3;
            ++numberOfBytesConsumed;
            grantedQos.add(qos);
        }
        return new Result<MqttSubAckPayload>(new MqttSubAckPayload(grantedQos), numberOfBytesConsumed);
    }
    
    private static Result<MqttUnsubscribePayload> decodeUnsubscribePayload(final ByteBuf buffer, final int bytesRemainingInVariablePart) {
        final List<String> unsubscribeTopics = new ArrayList<String>();
        int numberOfBytesConsumed = 0;
        while (numberOfBytesConsumed < bytesRemainingInVariablePart) {
            final Result<String> decodedTopicName = decodeString(buffer);
            numberOfBytesConsumed += ((Result<Object>)decodedTopicName).numberOfBytesConsumed;
            unsubscribeTopics.add((String)((Result<Object>)decodedTopicName).value);
        }
        return new Result<MqttUnsubscribePayload>(new MqttUnsubscribePayload(unsubscribeTopics), numberOfBytesConsumed);
    }
    
    private static Result<ByteBuf> decodePublishPayload(final ByteBuf buffer, final int bytesRemainingInVariablePart) {
        final ByteBuf b = buffer.readRetainedSlice(bytesRemainingInVariablePart);
        return new Result<ByteBuf>(b, bytesRemainingInVariablePart);
    }
    
    private static Result<String> decodeString(final ByteBuf buffer) {
        return decodeString(buffer, 0, Integer.MAX_VALUE);
    }
    
    private static Result<String> decodeAsciiString(final ByteBuf buffer) {
        final Result<String> result = decodeString(buffer, 0, Integer.MAX_VALUE);
        final String s = (String)((Result<Object>)result).value;
        for (int i = 0; i < s.length(); ++i) {
            if (s.charAt(i) > '\u007f') {
                return new Result<String>(null, ((Result<Object>)result).numberOfBytesConsumed);
            }
        }
        return new Result<String>(s, ((Result<Object>)result).numberOfBytesConsumed);
    }
    
    private static Result<String> decodeString(final ByteBuf buffer, final int minBytes, final int maxBytes) {
        final Result<Integer> decodedSize = decodeMsbLsb(buffer);
        final int size = (int)((Result<Object>)decodedSize).value;
        int numberOfBytesConsumed = ((Result<Object>)decodedSize).numberOfBytesConsumed;
        if (size < minBytes || size > maxBytes) {
            buffer.skipBytes(size);
            numberOfBytesConsumed += size;
            return new Result<String>(null, numberOfBytesConsumed);
        }
        final String s = buffer.toString(buffer.readerIndex(), size, CharsetUtil.UTF_8);
        buffer.skipBytes(size);
        numberOfBytesConsumed += size;
        return new Result<String>(s, numberOfBytesConsumed);
    }
    
    private static Result<Integer> decodeMsbLsb(final ByteBuf buffer) {
        return decodeMsbLsb(buffer, 0, 65535);
    }
    
    private static Result<Integer> decodeMsbLsb(final ByteBuf buffer, final int min, final int max) {
        final short msbSize = buffer.readUnsignedByte();
        final short lsbSize = buffer.readUnsignedByte();
        final int numberOfBytesConsumed = 2;
        int result = msbSize << 8 | lsbSize;
        if (result < min || result > max) {
            result = -1;
        }
        return new Result<Integer>(result, 2);
    }
    
    enum DecoderState
    {
        READ_FIXED_HEADER, 
        READ_VARIABLE_HEADER, 
        READ_PAYLOAD, 
        BAD_MESSAGE;
    }
    
    private static final class Result<T>
    {
        private final T value;
        private final int numberOfBytesConsumed;
        
        Result(final T value, final int numberOfBytesConsumed) {
            this.value = value;
            this.numberOfBytesConsumed = numberOfBytesConsumed;
        }
    }
}

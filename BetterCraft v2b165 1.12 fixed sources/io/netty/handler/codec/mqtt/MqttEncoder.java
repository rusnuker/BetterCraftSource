// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.mqtt;

import io.netty.util.CharsetUtil;
import java.util.Iterator;
import io.netty.util.internal.EmptyArrays;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.MessageToMessageEncoder;

@ChannelHandler.Sharable
public final class MqttEncoder extends MessageToMessageEncoder<MqttMessage>
{
    public static final MqttEncoder INSTANCE;
    
    private MqttEncoder() {
    }
    
    @Override
    protected void encode(final ChannelHandlerContext ctx, final MqttMessage msg, final List<Object> out) throws Exception {
        out.add(doEncode(ctx.alloc(), msg));
    }
    
    static ByteBuf doEncode(final ByteBufAllocator byteBufAllocator, final MqttMessage message) {
        switch (message.fixedHeader().messageType()) {
            case CONNECT: {
                return encodeConnectMessage(byteBufAllocator, (MqttConnectMessage)message);
            }
            case CONNACK: {
                return encodeConnAckMessage(byteBufAllocator, (MqttConnAckMessage)message);
            }
            case PUBLISH: {
                return encodePublishMessage(byteBufAllocator, (MqttPublishMessage)message);
            }
            case SUBSCRIBE: {
                return encodeSubscribeMessage(byteBufAllocator, (MqttSubscribeMessage)message);
            }
            case UNSUBSCRIBE: {
                return encodeUnsubscribeMessage(byteBufAllocator, (MqttUnsubscribeMessage)message);
            }
            case SUBACK: {
                return encodeSubAckMessage(byteBufAllocator, (MqttSubAckMessage)message);
            }
            case UNSUBACK:
            case PUBACK:
            case PUBREC:
            case PUBREL:
            case PUBCOMP: {
                return encodeMessageWithOnlySingleByteFixedHeaderAndMessageId(byteBufAllocator, message);
            }
            case PINGREQ:
            case PINGRESP:
            case DISCONNECT: {
                return encodeMessageWithOnlySingleByteFixedHeader(byteBufAllocator, message);
            }
            default: {
                throw new IllegalArgumentException("Unknown message type: " + message.fixedHeader().messageType().value());
            }
        }
    }
    
    private static ByteBuf encodeConnectMessage(final ByteBufAllocator byteBufAllocator, final MqttConnectMessage message) {
        int payloadBufferSize = 0;
        final MqttFixedHeader mqttFixedHeader = message.fixedHeader();
        final MqttConnectVariableHeader variableHeader = message.variableHeader();
        final MqttConnectPayload payload = message.payload();
        final MqttVersion mqttVersion = MqttVersion.fromProtocolNameAndLevel(variableHeader.name(), (byte)variableHeader.version());
        final String clientIdentifier = payload.clientIdentifier();
        if (!MqttCodecUtil.isValidClientId(mqttVersion, clientIdentifier)) {
            throw new MqttIdentifierRejectedException("invalid clientIdentifier: " + clientIdentifier);
        }
        final byte[] clientIdentifierBytes = encodeStringUtf8(clientIdentifier);
        payloadBufferSize += 2 + clientIdentifierBytes.length;
        final String willTopic = payload.willTopic();
        final byte[] willTopicBytes = (willTopic != null) ? encodeStringUtf8(willTopic) : EmptyArrays.EMPTY_BYTES;
        final String willMessage = payload.willMessage();
        final byte[] willMessageBytes = (willMessage != null) ? encodeStringUtf8(willMessage) : EmptyArrays.EMPTY_BYTES;
        if (variableHeader.isWillFlag()) {
            payloadBufferSize += 2 + willTopicBytes.length;
            payloadBufferSize += 2 + willMessageBytes.length;
        }
        final String userName = payload.userName();
        final byte[] userNameBytes = (userName != null) ? encodeStringUtf8(userName) : EmptyArrays.EMPTY_BYTES;
        if (variableHeader.hasUserName()) {
            payloadBufferSize += 2 + userNameBytes.length;
        }
        final String password = payload.password();
        final byte[] passwordBytes = (password != null) ? encodeStringUtf8(password) : EmptyArrays.EMPTY_BYTES;
        if (variableHeader.hasPassword()) {
            payloadBufferSize += 2 + passwordBytes.length;
        }
        final byte[] protocolNameBytes = mqttVersion.protocolNameBytes();
        final int variableHeaderBufferSize = 2 + protocolNameBytes.length + 4;
        final int variablePartSize = variableHeaderBufferSize + payloadBufferSize;
        final int fixedHeaderBufferSize = 1 + getVariableLengthInt(variablePartSize);
        final ByteBuf buf = byteBufAllocator.buffer(fixedHeaderBufferSize + variablePartSize);
        buf.writeByte(getFixedHeaderByte1(mqttFixedHeader));
        writeVariableLengthInt(buf, variablePartSize);
        buf.writeShort(protocolNameBytes.length);
        buf.writeBytes(protocolNameBytes);
        buf.writeByte(variableHeader.version());
        buf.writeByte(getConnVariableHeaderFlag(variableHeader));
        buf.writeShort(variableHeader.keepAliveTimeSeconds());
        buf.writeShort(clientIdentifierBytes.length);
        buf.writeBytes(clientIdentifierBytes, 0, clientIdentifierBytes.length);
        if (variableHeader.isWillFlag()) {
            buf.writeShort(willTopicBytes.length);
            buf.writeBytes(willTopicBytes, 0, willTopicBytes.length);
            buf.writeShort(willMessageBytes.length);
            buf.writeBytes(willMessageBytes, 0, willMessageBytes.length);
        }
        if (variableHeader.hasUserName()) {
            buf.writeShort(userNameBytes.length);
            buf.writeBytes(userNameBytes, 0, userNameBytes.length);
        }
        if (variableHeader.hasPassword()) {
            buf.writeShort(passwordBytes.length);
            buf.writeBytes(passwordBytes, 0, passwordBytes.length);
        }
        return buf;
    }
    
    private static int getConnVariableHeaderFlag(final MqttConnectVariableHeader variableHeader) {
        int flagByte = 0;
        if (variableHeader.hasUserName()) {
            flagByte |= 0x80;
        }
        if (variableHeader.hasPassword()) {
            flagByte |= 0x40;
        }
        if (variableHeader.isWillRetain()) {
            flagByte |= 0x20;
        }
        flagByte |= (variableHeader.willQos() & 0x3) << 3;
        if (variableHeader.isWillFlag()) {
            flagByte |= 0x4;
        }
        if (variableHeader.isCleanSession()) {
            flagByte |= 0x2;
        }
        return flagByte;
    }
    
    private static ByteBuf encodeConnAckMessage(final ByteBufAllocator byteBufAllocator, final MqttConnAckMessage message) {
        final ByteBuf buf = byteBufAllocator.buffer(4);
        buf.writeByte(getFixedHeaderByte1(message.fixedHeader()));
        buf.writeByte(2);
        buf.writeByte(message.variableHeader().isSessionPresent() ? 1 : 0);
        buf.writeByte(message.variableHeader().connectReturnCode().byteValue());
        return buf;
    }
    
    private static ByteBuf encodeSubscribeMessage(final ByteBufAllocator byteBufAllocator, final MqttSubscribeMessage message) {
        final int variableHeaderBufferSize = 2;
        int payloadBufferSize = 0;
        final MqttFixedHeader mqttFixedHeader = message.fixedHeader();
        final MqttMessageIdVariableHeader variableHeader = message.variableHeader();
        final MqttSubscribePayload payload = message.payload();
        for (final MqttTopicSubscription topic : payload.topicSubscriptions()) {
            final String topicName = topic.topicName();
            final byte[] topicNameBytes = encodeStringUtf8(topicName);
            payloadBufferSize += 2 + topicNameBytes.length;
            ++payloadBufferSize;
        }
        final int variablePartSize = variableHeaderBufferSize + payloadBufferSize;
        final int fixedHeaderBufferSize = 1 + getVariableLengthInt(variablePartSize);
        final ByteBuf buf = byteBufAllocator.buffer(fixedHeaderBufferSize + variablePartSize);
        buf.writeByte(getFixedHeaderByte1(mqttFixedHeader));
        writeVariableLengthInt(buf, variablePartSize);
        final int messageId = variableHeader.messageId();
        buf.writeShort(messageId);
        for (final MqttTopicSubscription topic2 : payload.topicSubscriptions()) {
            final String topicName2 = topic2.topicName();
            final byte[] topicNameBytes2 = encodeStringUtf8(topicName2);
            buf.writeShort(topicNameBytes2.length);
            buf.writeBytes(topicNameBytes2, 0, topicNameBytes2.length);
            buf.writeByte(topic2.qualityOfService().value());
        }
        return buf;
    }
    
    private static ByteBuf encodeUnsubscribeMessage(final ByteBufAllocator byteBufAllocator, final MqttUnsubscribeMessage message) {
        final int variableHeaderBufferSize = 2;
        int payloadBufferSize = 0;
        final MqttFixedHeader mqttFixedHeader = message.fixedHeader();
        final MqttMessageIdVariableHeader variableHeader = message.variableHeader();
        final MqttUnsubscribePayload payload = message.payload();
        for (final String topicName : payload.topics()) {
            final byte[] topicNameBytes = encodeStringUtf8(topicName);
            payloadBufferSize += 2 + topicNameBytes.length;
        }
        final int variablePartSize = variableHeaderBufferSize + payloadBufferSize;
        final int fixedHeaderBufferSize = 1 + getVariableLengthInt(variablePartSize);
        final ByteBuf buf = byteBufAllocator.buffer(fixedHeaderBufferSize + variablePartSize);
        buf.writeByte(getFixedHeaderByte1(mqttFixedHeader));
        writeVariableLengthInt(buf, variablePartSize);
        final int messageId = variableHeader.messageId();
        buf.writeShort(messageId);
        for (final String topicName2 : payload.topics()) {
            final byte[] topicNameBytes2 = encodeStringUtf8(topicName2);
            buf.writeShort(topicNameBytes2.length);
            buf.writeBytes(topicNameBytes2, 0, topicNameBytes2.length);
        }
        return buf;
    }
    
    private static ByteBuf encodeSubAckMessage(final ByteBufAllocator byteBufAllocator, final MqttSubAckMessage message) {
        final int variableHeaderBufferSize = 2;
        final int payloadBufferSize = message.payload().grantedQoSLevels().size();
        final int variablePartSize = variableHeaderBufferSize + payloadBufferSize;
        final int fixedHeaderBufferSize = 1 + getVariableLengthInt(variablePartSize);
        final ByteBuf buf = byteBufAllocator.buffer(fixedHeaderBufferSize + variablePartSize);
        buf.writeByte(getFixedHeaderByte1(message.fixedHeader()));
        writeVariableLengthInt(buf, variablePartSize);
        buf.writeShort(message.variableHeader().messageId());
        for (final int qos : message.payload().grantedQoSLevels()) {
            buf.writeByte(qos);
        }
        return buf;
    }
    
    private static ByteBuf encodePublishMessage(final ByteBufAllocator byteBufAllocator, final MqttPublishMessage message) {
        final MqttFixedHeader mqttFixedHeader = message.fixedHeader();
        final MqttPublishVariableHeader variableHeader = message.variableHeader();
        final ByteBuf payload = message.payload().duplicate();
        final String topicName = variableHeader.topicName();
        final byte[] topicNameBytes = encodeStringUtf8(topicName);
        final int variableHeaderBufferSize = 2 + topicNameBytes.length + ((mqttFixedHeader.qosLevel().value() > 0) ? 2 : 0);
        final int payloadBufferSize = payload.readableBytes();
        final int variablePartSize = variableHeaderBufferSize + payloadBufferSize;
        final int fixedHeaderBufferSize = 1 + getVariableLengthInt(variablePartSize);
        final ByteBuf buf = byteBufAllocator.buffer(fixedHeaderBufferSize + variablePartSize);
        buf.writeByte(getFixedHeaderByte1(mqttFixedHeader));
        writeVariableLengthInt(buf, variablePartSize);
        buf.writeShort(topicNameBytes.length);
        buf.writeBytes(topicNameBytes);
        if (mqttFixedHeader.qosLevel().value() > 0) {
            buf.writeShort(variableHeader.messageId());
        }
        buf.writeBytes(payload);
        return buf;
    }
    
    private static ByteBuf encodeMessageWithOnlySingleByteFixedHeaderAndMessageId(final ByteBufAllocator byteBufAllocator, final MqttMessage message) {
        final MqttFixedHeader mqttFixedHeader = message.fixedHeader();
        final MqttMessageIdVariableHeader variableHeader = (MqttMessageIdVariableHeader)message.variableHeader();
        final int msgId = variableHeader.messageId();
        final int variableHeaderBufferSize = 2;
        final int fixedHeaderBufferSize = 1 + getVariableLengthInt(variableHeaderBufferSize);
        final ByteBuf buf = byteBufAllocator.buffer(fixedHeaderBufferSize + variableHeaderBufferSize);
        buf.writeByte(getFixedHeaderByte1(mqttFixedHeader));
        writeVariableLengthInt(buf, variableHeaderBufferSize);
        buf.writeShort(msgId);
        return buf;
    }
    
    private static ByteBuf encodeMessageWithOnlySingleByteFixedHeader(final ByteBufAllocator byteBufAllocator, final MqttMessage message) {
        final MqttFixedHeader mqttFixedHeader = message.fixedHeader();
        final ByteBuf buf = byteBufAllocator.buffer(2);
        buf.writeByte(getFixedHeaderByte1(mqttFixedHeader));
        buf.writeByte(0);
        return buf;
    }
    
    private static int getFixedHeaderByte1(final MqttFixedHeader header) {
        int ret = 0;
        ret |= header.messageType().value() << 4;
        if (header.isDup()) {
            ret |= 0x8;
        }
        ret |= header.qosLevel().value() << 1;
        if (header.isRetain()) {
            ret |= 0x1;
        }
        return ret;
    }
    
    private static void writeVariableLengthInt(final ByteBuf buf, int num) {
        do {
            int digit = num % 128;
            num /= 128;
            if (num > 0) {
                digit |= 0x80;
            }
            buf.writeByte(digit);
        } while (num > 0);
    }
    
    private static int getVariableLengthInt(int num) {
        int count = 0;
        do {
            num /= 128;
            ++count;
        } while (num > 0);
        return count;
    }
    
    private static byte[] encodeStringUtf8(final String s) {
        return s.getBytes(CharsetUtil.UTF_8);
    }
    
    static {
        INSTANCE = new MqttEncoder();
    }
}

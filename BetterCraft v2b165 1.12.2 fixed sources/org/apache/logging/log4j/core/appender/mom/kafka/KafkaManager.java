// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.appender.mom.kafka;

import org.apache.logging.log4j.Logger;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.logging.log4j.core.util.Log4jThread;
import java.util.concurrent.TimeUnit;
import java.util.Objects;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.kafka.clients.producer.Producer;
import java.util.Properties;
import org.apache.logging.log4j.core.appender.AbstractManager;

public class KafkaManager extends AbstractManager
{
    public static final String DEFAULT_TIMEOUT_MILLIS = "30000";
    static KafkaProducerFactory producerFactory;
    private final Properties config;
    private Producer<byte[], byte[]> producer;
    private final int timeoutMillis;
    private final String topic;
    private final boolean syncSend;
    
    public KafkaManager(final LoggerContext loggerContext, final String name, final String topic, final boolean syncSend, final Property[] properties) {
        super(loggerContext, name);
        this.config = new Properties();
        this.topic = Objects.requireNonNull(topic, "topic");
        this.syncSend = syncSend;
        this.config.setProperty("key.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
        this.config.setProperty("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
        this.config.setProperty("batch.size", "0");
        for (final Property property : properties) {
            this.config.setProperty(property.getName(), property.getValue());
        }
        this.timeoutMillis = Integer.parseInt(this.config.getProperty("timeout.ms", "30000"));
    }
    
    public boolean releaseSub(final long timeout, final TimeUnit timeUnit) {
        if (timeout > 0L) {
            this.closeProducer(timeout, timeUnit);
        }
        else {
            this.closeProducer(this.timeoutMillis, TimeUnit.MILLISECONDS);
        }
        return true;
    }
    
    private void closeProducer(final long timeout, final TimeUnit timeUnit) {
        if (this.producer != null) {
            final Thread closeThread = new Log4jThread(new Runnable() {
                @Override
                public void run() {
                    if (KafkaManager.this.producer != null) {
                        KafkaManager.this.producer.close();
                    }
                }
            }, "KafkaManager-CloseThread");
            closeThread.setDaemon(true);
            closeThread.start();
            try {
                closeThread.join(timeUnit.toMillis(timeout));
            }
            catch (final InterruptedException ignore) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    public void send(final byte[] msg) throws ExecutionException, InterruptedException, TimeoutException {
        if (this.producer != null) {
            final ProducerRecord<byte[], byte[]> newRecord = (ProducerRecord<byte[], byte[]>)new ProducerRecord(this.topic, (Object)msg);
            if (this.syncSend) {
                final Future<RecordMetadata> response = this.producer.send((ProducerRecord)newRecord);
                response.get(this.timeoutMillis, TimeUnit.MILLISECONDS);
            }
            else {
                this.producer.send((ProducerRecord)newRecord, (Callback)new Callback() {
                    public void onCompletion(final RecordMetadata metadata, final Exception e) {
                        if (e != null) {
                            KafkaManager.LOGGER.error("Unable to write to Kafka [" + KafkaManager.this.getName() + "].", e);
                        }
                    }
                });
            }
        }
    }
    
    public void startup() {
        this.producer = KafkaManager.producerFactory.newKafkaProducer(this.config);
    }
    
    public String getTopic() {
        return this.topic;
    }
    
    static {
        KafkaManager.producerFactory = new DefaultKafkaProducerFactory();
    }
}

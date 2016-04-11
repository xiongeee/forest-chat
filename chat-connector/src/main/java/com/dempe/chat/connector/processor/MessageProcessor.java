package com.dempe.chat.connector.processor;

import com.dempe.chat.common.messages.AbstractMessage;
import com.dempe.chat.common.messages.PublishMessage;
import com.dempe.chat.common.messages.WillMessage;
import com.dempe.chat.connector.ConnectionDescriptor;
import com.dempe.chat.store.ClientSession;
import com.dempe.chat.store.ISessionsStore;
import com.dempe.chat.store.MemorySessionStore;
import com.google.common.collect.Maps;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentMap;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/4/11
 * Time: 11:27
 * To change this template use File | Settings | File Templates.
 */
public class MessageProcessor {

    protected final static Logger LOGGER = LoggerFactory.getLogger(MessageProcessor.class);

    protected static ConcurrentMap<String, ConnectionDescriptor> m_clientIDs = Maps.newConcurrentMap();

    protected static ConcurrentMap<String, WillMessage> m_willStore = Maps.newConcurrentMap();
    protected static ISessionsStore m_sessionsStore = new MemorySessionStore();


    protected void setIdleTime(ChannelPipeline pipeline, int idleTime) {
        if (pipeline.names().contains("idleStateHandler")) {
            pipeline.remove("idleStateHandler");
        }
        pipeline.addFirst("idleStateHandler", new IdleStateHandler(0, 0, idleTime));
    }

    protected void directSend(ClientSession clientsession, String topic, AbstractMessage.QOSType qos, ByteBuffer message, boolean retained, Integer messageID) {
        String clientId = clientsession.clientID;
        LOGGER.debug("directSend invoked clientId <{}> on topic <{}> QoS {} retained {} messageID {}", clientId, topic, qos, retained, messageID);
        PublishMessage pubMessage = new PublishMessage();
        pubMessage.setRetainFlag(retained);
        pubMessage.setTopicName(topic);
        pubMessage.setQos(qos);
        pubMessage.setPayload(message);
        LOGGER.info("send publish message to <{}> on topic <{}>", clientId, topic);
        //set the PacketIdentifier only for QoS > 0
        if (pubMessage.getQos() != AbstractMessage.QOSType.MOST_ONE) {
            pubMessage.setMessageID(messageID);
        } else {
            if (messageID != null) {
                throw new RuntimeException("Internal bad error, trying to forwardPublish a QoS 0 message with PacketIdentifier: " + messageID);
            }
        }
        if (m_clientIDs == null) {
            throw new RuntimeException("Internal bad error, found m_clientIDs to null while it should be initialized, somewhere it's overwritten!!");
        }
        LOGGER.debug("clientIDs are {}", m_clientIDs);
        if (m_clientIDs.get(clientId) == null) {
            //TODO while we were publishing to the target client, that client disconnected,
            // could happen is not an error HANDLE IT
            throw new RuntimeException(String.format("Can't find a ConnectionDescriptor for client <%s> in cache <%s>", clientId, m_clientIDs));
        }
        Channel channel = m_clientIDs.get(clientId).channel;
        LOGGER.debug("Session for clientId {} is {}", clientId, channel);
        channel.writeAndFlush(pubMessage);
    }


}

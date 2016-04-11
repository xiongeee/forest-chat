package com.dempe.chat.connector.processor;

import com.dempe.chat.common.messages.UnsubAckMessage;
import com.dempe.chat.common.messages.UnsubscribeMessage;
import com.dempe.chat.connector.NettyUtils;
import com.dempe.chat.store.ClientSession;
import com.dempe.chat.store.subscriptions.SubscriptionsStore;
import io.netty.channel.Channel;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/4/11
 * Time: 20:40
 * To change this template use File | Settings | File Templates.
 */
public class UnSubscriptionMessageProcessor extends MessageProcessor {

    public void processUnsubscribe(Channel channel, UnsubscribeMessage msg) {
        List<String> topics = msg.topicFilters();
        int messageID = msg.getMessageID();
        String clientID = NettyUtils.clientID(channel);

        LOGGER.debug("UNSUBSCRIBE subscription on topics {} for clientID <{}>", topics, clientID);

        ClientSession clientSession = m_sessionsStore.sessionForClient(clientID);
        for (String topic : topics) {
            boolean validTopic = SubscriptionsStore.validate(topic);
            if (!validTopic) {
                //close the connection, not valid topicFilter is a protocol violation
                channel.close();
                LOGGER.warn("UNSUBSCRIBE found an invalid topic filter <{}> for clientID <{}>", topic, clientID);
                return;
            }

            // TODO 删除订阅关系
            clientSession.unsubscribeFrom(topic);
        }

        //ack the client
        UnsubAckMessage ackMessage = new UnsubAckMessage();
        ackMessage.setMessageID(messageID);

        LOGGER.info("replying with UnsubAck to MSG ID {}", messageID);
        channel.writeAndFlush(ackMessage);
    }

}

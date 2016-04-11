/*
 * Copyright (c) 2012-2015 The original author or authors
 * ------------------------------------------------------
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * The Apache License v2.0 is available at
 * http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 */
package com.dempe.chat.store;

import com.dempe.chat.store.subscriptions.Subscription;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

/**
 * @author andrea
 */
public class MemorySessionStore implements ISessionsStore {



    @Override
    public void updateCleanStatus(String clientID, boolean cleanSession) {

    }

    @Override
    public void initStore() {

    }

    @Override
    public void addNewSubscription(Subscription newSubscription) {

    }

    @Override
    public void removeSubscription(String topic, String clientID) {

    }

    @Override
    public void wipeSubscriptions(String sessionID) {

    }

    @Override
    public List<ClientTopicCouple> listAllSubscriptions() {
        return null;
    }

    @Override
    public Subscription getSubscription(ClientTopicCouple couple) {
        return null;
    }

    @Override
    public boolean contains(String clientID) {
        return false;
    }

    @Override
    public ClientSession createNewSession(String clientID, boolean cleanSession) {
        return null;
    }

    @Override
    public ClientSession sessionForClient(String clientID) {
        return null;
    }

    @Override
    public void inFlightAck(String clientID, int messageID) {

    }

    @Override
    public void inFlight(String clientID, int messageID, String guid) {

    }

    @Override
    public int nextPacketID(String clientID) {
        return 0;
    }

    @Override
    public void bindToDeliver(String guid, String clientID) {

    }

    @Override
    public Collection<String> enqueued(String clientID) {
        return null;
    }

    @Override
    public void removeEnqueued(String clientID, String guid) {

    }

    @Override
    public void secondPhaseAcknowledged(String clientID, int messageID) {

    }

    @Override
    public void secondPhaseAckWaiting(String clientID, int messageID) {

    }

    @Override
    public String mapToGuid(String clientID, int messageID) {
        return null;
    }
}

package com.vsafe.admin.server.core.configurations.dbConfig.mongo;

import com.mongodb.event.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MongoConnectionPoolListener implements ConnectionPoolListener {
    @Override
    public void connectionPoolOpened(ConnectionPoolOpenedEvent event) {
        log.trace(event.toString());
        ConnectionPoolListener.super.connectionPoolOpened(event);
    }

    @Override
    public void connectionPoolCreated(ConnectionPoolCreatedEvent event) {
        log.trace(event.toString());
        ConnectionPoolListener.super.connectionPoolCreated(event);
    }

    @Override
    public void connectionPoolCleared(ConnectionPoolClearedEvent event) {
        log.trace(event.toString());
        ConnectionPoolListener.super.connectionPoolCleared(event);
    }

    @Override
    public void connectionPoolReady(ConnectionPoolReadyEvent event) {
        log.trace(event.toString());
        ConnectionPoolListener.super.connectionPoolReady(event);
    }

    @Override
    public void connectionPoolClosed(ConnectionPoolClosedEvent event) {
        log.trace(event.toString());
        ConnectionPoolListener.super.connectionPoolClosed(event);
    }

    @Override
    public void connectionCheckOutStarted(ConnectionCheckOutStartedEvent event) {
        log.trace(event.toString());
        ConnectionPoolListener.super.connectionCheckOutStarted(event);
    }

    @Override
    public void connectionCheckedOut(ConnectionCheckedOutEvent event) {
        log.trace(event.toString());
        ConnectionPoolListener.super.connectionCheckedOut(event);
    }

    @Override
    public void connectionCheckOutFailed(ConnectionCheckOutFailedEvent event) {
        log.trace(event.toString());
        ConnectionPoolListener.super.connectionCheckOutFailed(event);
    }

    @Override
    public void connectionCheckedIn(ConnectionCheckedInEvent event) {
        log.trace(event.toString());
        ConnectionPoolListener.super.connectionCheckedIn(event);
    }

    @Override
    public void connectionAdded(ConnectionAddedEvent event) {
        log.trace(event.toString());
        ConnectionPoolListener.super.connectionAdded(event);
    }

    @Override
    public void connectionCreated(ConnectionCreatedEvent event) {
        log.trace(event.toString());
        ConnectionPoolListener.super.connectionCreated(event);
    }

    @Override
    public void connectionReady(ConnectionReadyEvent event) {
        log.trace(event.toString());
        ConnectionPoolListener.super.connectionReady(event);
    }

    @Override
    public void connectionRemoved(ConnectionRemovedEvent event) {
        log.trace(event.toString());
        ConnectionPoolListener.super.connectionRemoved(event);
    }

    @Override
    public void connectionClosed(ConnectionClosedEvent event) {
        log.trace(event.toString());
        ConnectionPoolListener.super.connectionClosed(event);
    }
}

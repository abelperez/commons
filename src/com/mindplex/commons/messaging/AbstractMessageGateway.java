/**
 * Copyright (C) 2011 Mindplex Media, LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.mindplex.commons.messaging;

import java.lang.IllegalStateException;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.jms.*;

import com.mindplex.commons.base.Function;
import static com.mindplex.commons.base.Check.notEmpty;

/**
 * A general-purpose messaging gateway based on the JMS API.  Most of the
 * methods contained in this gateway are support type functions that
 * help quickly create JMS objects like message producers and consumers.
 *
 * <p>Note that this gateway provides two modes of operations. One is that
 * it contains a default destination that is specified at construction time.
 * The default destination is used when sending messages without explicitly
 * specifying a destination.  The other mode is simply the opposite.
 * Destinations are explicitly specified in each send operation and effectively
 * override the default destination. 
 * 
 * @author Abel Perez
 */
public abstract class AbstractMessageGateway implements MessageGateway
{    
    /**
     * An active connection this gateway uses to communicate with a JMS provider.
     */
    private Connection connection;

    /**
     * A single threaded JMS session for creating message consumers and producers.
     */
    private Session session;

    /**
     * The JMS destination that encapsulates the address details about a JMS provider.
     * This is the default destination messages will be sent when a target destination
     * is not specified through a {@code send} operation.
     */
    private Destination destination;

    /**
     * The name of the default JMS destination.
     */
    private String destinationName;

    /**
     * A flag that denotes whether this gateways JMS connection is closed or open.
     */
    private AtomicBoolean closed = new AtomicBoolean(true);

    /**
     * Constructs this {@code AbstractMessageGateway} with the specified default destination.
     *  
     * @param destinationName the name of the default destination.
     *
     * @throws IllegalStateException, IllegalArgumentException or NullPointerException if
     * the specified argument is invalid or this gateway fails to open a connection
     * to a JMS provider.
     */
    protected AbstractMessageGateway(String destinationName) {
        this.destinationName = notEmpty(destinationName);
        initialize();
    }

    /**
     * Initializes this gateway by opening a connection to a JMS provider, setting up an
     * exception listener, creating a new JMS session, and creating the default destination
     * to send messages to.
     *
     * @throws IllegalStateException can occur if the required steps to setup a valid JMS
     * session fail.
     */
    protected void initialize() {
        try {
            connection = MessagingFactory.getConnection();
            connection.setExceptionListener(GatewayExceptionListener.of(this, getExceptionListenerFunction()));
            session = getConnection().createSession(false, Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue(destinationName);
            connection.start();
            closed.set(false);

        } catch (Exception exception) {
            throw new IllegalStateException("Failed to initialize message gateway.", exception);
        }
    }
    
    /**
     * Converts a {@code String} destination instance into a JMS {@code Destination}.
     *
     * @param destination the string destination.
     * 
     * @return a JMS {@code Destination}.
     *
     * @throws IllegalStateException can occur if the current JMS session is invalid.
     */
    public Destination createDestination(String destination) {
        try {
            // simply create a JMS Queue Destination and assign it
            // the specified string destination.
            return getSession().createQueue(destination);

        } catch (Exception exception) {
            throw new IllegalStateException("Failed to get convert string destination to jms destination.", exception);
        }
    }
    
    /**
     * Creates an empty JMS {@code Message}.
     *
     * @return a JMS {@code Message}.
     *
     * @throws IllegalStateException can occur if the current JMS session is invalid.
     */
    public Message createMessage() {
        try {
            return getSession().createTextMessage();

        } catch (Exception exception) {
            throw new IllegalStateException("Failed to create jms text message.", exception);
        }
    }

    /**
     * Converts a {@code String} message instance into a JMS {@code Message}.
     *
     * @param message the string to convert to a JMS text message.
     *
     * @return a JMS {@code Message}.
     *
     * @throws IllegalStateException can occur if the current JMS session is invalid.
     */
    public Message createMessage(String message) {
        try {
            return getSession().createTextMessage(message);

        } catch (Exception exception) {
            throw new IllegalStateException("Failed to create jms text message.", exception);
        }
    }

    /**
     * Creates a JMS {@code MessageProducer} instance from the current JMS session.
     *
     * @return a JMS {@code MessageProducer}.
     *
     * @throws IllegalStateException can occur if the current JMS session is invalid.      
     */
    public MessageProducer createProducer() {
        try {
            return getSession().createProducer(getDestination());

        } catch (Exception exception) {
            throw new IllegalStateException("Failed to create jms message producer.", exception);
        }
    }

    /**
     * Creates a JMS {@code MessageProducer} instance from the current JMS session.
     *
     * @param destination the JMS destination to assign the newly created message
     * producer.
     * @return a JMS {@code MessageProducer}.
     *
     * @throws IllegalStateException can occur if the current JMS session is invalid.
     */
    public MessageProducer createProducer(Destination destination) {
        try {
            return getSession().createProducer(destination);

        } catch (Exception exception) {
            throw new IllegalStateException("Failed to create jms message producer.", exception);
        }
    }

    /**
     * Creates a JMS {@code MessageConsumer} instance from the current JMS session.
     *
     * @return a JMS {@code MessageConsumer}.
     *
     * @throws IllegalStateException can occur if the current JMS session is invalid.
     */
    public MessageConsumer createConsumer() {
        try {
            return getSession().createConsumer(getDestination());

        } catch (Exception exception) {
            throw new IllegalStateException("Failed to create jms message consumer.", exception);
        }
    }

    /**
     * Creates a JMS {@code MessageConsumer} instance from the current JMS session.
     *
     * @param destination the JMS destination to assign the newly created message
     * consumer.
     * @return a JMS {@code MessageConsumer}.
     *
     * @throws IllegalStateException can occur if the current JMS session is invalid.
     */
    public MessageConsumer createConsumer(Destination destination) {
        try {
            return getSession().createConsumer(destination);

        } catch (Exception exception) {
            throw new IllegalStateException("Failed to create jms message consumer.", exception);
        }
    }

    /**
     * Closes the current JMS session and tears down the JMS provider
     * connection. 
     */
    public void close() {
        try {
            closed.set(true);
            session.close();
            connection.close();

        } catch (Exception exception) {
            // no need to react
            // to this, gateway
            // is already going
            // down.

        } finally {
            connection = null;
        }
    }

    /**
     * Returns {@code true} if the JMS provider connection is valid;
     * otherwise {@code false}. In this context, valid means the connection
     * is not {@code null}.
     * 
     * @return {@code true} if the JMS provider connection is valid;
     * otherwise {@code false}.
     */
    public boolean isConnectionValid() {
        return (connection != null);
    }

    /**
     * Closes down the current JMS provider connection. In most cases this
     * operation is invoked from some retry or recovery process.
     *  
     * @see #close
     */
    public void destroy() {
        if (! closed.get()) {
            // todo working on this.
        }
        close();
    }

    /**
     * Returns {@code true} if this gateway is closed; otherwise {@code false}.
     *
     * @return {@code true} if this gateway is closed; otherwise {@code false}.
     */
    public boolean isClosed() {
        return closed.get();
    }

    /**
     * Gets the current JMS session.  Its possible that this session is accessed
     * while in a closed state.  Before using the session, {@code isClosed}
     * should be checked.
     *
     * @return the current JMS session.
     */
    protected Session getSession() {
        return session;
    }

    /**
     * Gets the JMS provider connection. Its possible that this connection is
     * accessed while in a closed state.  Before using the connection,
     * {@code isClosed} should be checked.
     *
     * @return the JMS provider connection.
     */
    protected Connection getConnection() {
        return connection;
    }

    /**
     * Gets the default JMS destination.
     *
     * @return the default JMS destination.
     */
    protected Destination getDestination() {
        return destination;
    }

    /**
     * Gets a default function to apply against any {@code JMSException} received
     * by this gateways registered {@code ExceptionListener}.The registered
     * exception listener is a listener that is invoked asynchronously by the
     * underlying JMS provider when a connection fails.
     *
     * @return a default function to apply against any {@code JMSException}
     * received by this gateways registered {@code ExceptionListener}.
     */
    protected Function<JMSException, Object> getExceptionListenerFunction() {
        return ExceptionListenerFunction.SoleInstance;
    }

    /**
     * A singleton instance of a {@code Function} that wraps the specified exception
     * in a GatewayException and re-throws it.
     */
    private static enum ExceptionListenerFunction implements Function<JMSException, Object> {

        // singleton instance.
        SoleInstance;

        // Wraps the specified exception in a GatewayException
        // and re-throw it.
        
        public Object apply(JMSException exception) {
            throw new GatewayException("messaging provider connection failed.", exception);
        }
    }
}

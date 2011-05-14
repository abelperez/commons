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
import javax.jms.*;

import static com.mindplex.commons.base.Check.*;

/**
 * A {@code StandardMessageGateway} that supports operations for sending
 * and receiving messages.  
 *
 * todo add support for session modes i.e., auto ack, transacted.
 * 
 * @author Abel Perez
 */
public class StandardMessageGateway extends AbstractMessageGateway implements MessageGateway
{    
    /**
     * The message producer this gateway uses to send messages to a
     * specified destination.
     */
    private MessageProducer producer;

    /**
     * A message producer that is exclusively used for sending invalid messages.
     */
    private MessageProducer invalidMessageProducer;

    /**
     * Constructs this {@code StandardMessageGateway} with the specified
     * destination as the default destination to send messages to.  The
     * default destination is used when no destination is supplied to the
     * {@code send} methods of this gateway.
     *  
     * @param destination default destination for this gateway.
     */
    private StandardMessageGateway(String destination) {
        super(destination);
        producer = createProducer();
        invalidMessageProducer = createProducer(createDestination(getInvalidMessageChannel()));

    }

    /**
     * Factory method that constructs this {@code StandardMessageGateway}
     * with the specified destination as the default destination to send
     * messages to.  The default destination is used when no destination is
     * supplied to the {@code send} methods of this gateway.
     *
     * @param destination default destination for this gateway.
     *
     * @return an instance of this {@code StandardMessageGateway}.
     */
    public static StandardMessageGateway of(String destination) {
        return new StandardMessageGateway(destination);
    }

    /**
     * Sends the specified message to the default destination this gateway
     * was initialized with.
     *
     * @param message the message to send.
     *
     * @throws RuntimeException can occur if the specified message is not
     * valid or this gateway encounters an error communicating with the
     * target message broker.
     */
    public void send(String message) {
        doSend(createMessage(message));
    }

    /**
     * Sends the specified message to the supplied destination.
     *
     * @param message the message to send.
     * @param destination the destination to send the given message to.
     *
     * @throws RuntimeException can occur if the specified message is not
     * valid or this gateway encounters an error communicating with the
     * target message broker.
     */
    public void send(String message, String destination) {
        doSend(createMessage(message), createDestination(destination));
    }

    /**
     * Sends the specified message to the default destination this gateway was
     * initialized with and waits for a response on a temporary queue for the
     * specified timeout period.
     *
     * <p>If no response message is received before the timeout period is
     * exceeded then an exception is raised.
     *
     * @param message the message to send.
     * @param timeout the response message timeout.
     *
     * @throws RuntimeException can occur if the specified message is not
     * valid or this gateway encounters an error communicating with the
     * target message broker.
     */    
    public String request(String message, long timeout) {
        return doSendAndListen(createMessage(message), getDestination(), timeout);
    }

    /**
     * Sends the specified message to the supplied destination and waits for a
     * response on a temporary queue for the specified timeout period.
     *
     * <p>If no response message is received before the timeout period is
     * exceeded then an exception is raised.
     *
     * @param message the message to send.
     * @param destination the destination to send the specified message to.     
     * @param timeout the response message timeout.
     *
     * @throws RuntimeException can occur if the specified message is not
     * valid or this gateway encounters an error communicating with the
     * target message broker.
     */    
    public String request(String message, String destination, long timeout) {
        return doSendAndListen(createMessage(message), createDestination(destination), timeout);
    }

    /**
     * Waits for the specified timeout period for a message to come in from
     * the supplied destination.  This method at best will only consume one
     * message from the specified destination.
     *  
     * <p>If no response message is received before the timeout period is
     * exceeded then an exception is raised.
     *
     * @param destination the destination to wait for a message.
     * @param timeout the response message timeout.
     *
     * @throws RuntimeException can occur if the specified message is not
     * valid or this gateway encounters an error communicating with the
     * target message broker.
     */    
    public String receive(String destination, long timeout) {
        return doListen(createDestination(destination), timeout);    
    }

    /**
     * Rejects the specified {@code message} by sending it to this gateways
     * pre-configured invalid message channel.
     *  
     * @param message the message to reject.
     *
     * @throws RuntimeException can occur if the specified message is not
     * valid or this gateway encounters an error communicating with the
     * target message broker.
     */
    public void reject(String message) {
        doSendInvalidMessage(createMessage(notEmpty(message)));
    }

    /**
     * @see StandardMessageGateway#send(String)
     *  
     * @param message the JMS message to send.
     */
    protected void doSend(Message message) {
        try {
            verifyAlive();
            producer.send(message);

        } catch (Exception exception) {
            destroy();
            throw new IllegalStateException("Failed to send message.");
        }
    }

    /**
     * @see StandardMessageGateway#send(String, String)
     *
     * @param message the JMS message to send. 
     * @param destination the JMS Destination to send the specified message to.
     */
    protected void doSend(Message message, Destination destination) {
        try {
            verifyAlive();

            // create a temporary message producer,
            // assign it the specified destination
            // and send the message.

            MessageProducer producer = createProducer(destination);
            producer.send(message);
            producer.close();

        } catch (Exception exception) {
            destroy();
            throw new IllegalStateException("Failed to send message.", exception);
        }
    }

    /**
     * @see StandardMessageGateway#send(String)
     *
     * @param message the JMS message to send.
     */
    protected void doSendInvalidMessage(Message message) {
        try {
            verifyAlive();
            invalidMessageProducer.send(message);

        } catch (Exception exception) {
            destroy();
            throw new IllegalStateException("Failed to send invalid message.");
        }
    }

    /**
     * Returns the next available message from the specified destination if
     * the timeout interval is not exceeded.  If the timeout interval is exceeded
     * this method returns {@code null}.
     * 
     * @param destination the destination to listen for the next available
     * message.
     * @param timeout the timeout interval to wait for the next available message.
     *
     * @return the next available message; otherwise {@code null}.
     */
    protected String doListen(Destination destination, long timeout) {
        try {
            verifyAlive();

            // Wait for the next message to arrive at the specified
            // destination.  If the message does not arrive within
            // the specified timeout interval we bail.

            MessageConsumer receiver = createConsumer(destination);
            Message response = receiver.receive(timeout);

            // If the response did not timeout and the response
            // is not null, we convert the received text message into
            // a string message and return the message.

            if (isNull(response)) return null;
            TextMessage target = (TextMessage) response;
            return target.getText();

        } catch (Exception exception) {
            destroy();
            throw new IllegalStateException("Failed to listen.");
        }
    }

    /**
     * Sends the specified message to the default destination and waits for
     * the specified timeout interval for a response message.  If the response
     * message does not arrive before the timeout interval is exceeded, the
     * the result will be {@code null}.
     *
     * @param message the message to send.
     * @param timeout the time interval to wait for a response message.
     *
     * @return a response message or {@code null} if the specified timeout
     * interval exceeded.
     *
     * @throws IllegalStateException can occur if the underlying connection
     * to the message broker fails or the specified message is in an invalid
     * state.
     */
    protected String doSendAndListen(Message message, long timeout) {
        try {
            verifyAlive();

            // First we create a temporary response queue
            // and set it as the replyTo destination of the
            // message we're sending.

            Queue responseDestination = getSession().createTemporaryQueue();
            message.setJMSReplyTo(responseDestination);

            // Secondly we send off the given message to the
            // default destination this gateway was initialized with.

            producer.send(message);
            producer.close();

            // Now we can wait for a response message to arrive
            // at the temporary destination we created.  If the
            // message does not arrive within the specified timeout
            // interval we bail.

            MessageConsumer receiver = createConsumer(responseDestination);
            Message response = receiver.receive(timeout);

            // Lastly, if the response did not timeout and the response
            // is not null, we convert the received text message into
            // a string message and return the message.

            if (isNull(response)) return null;
            TextMessage target = (TextMessage) response;
            return target.getText();

        } catch (Exception exception) {
            destroy();
            throw new IllegalStateException("Failed to complete request/reply.", exception);
        }
    }

    /**
     * Sends the specified message to the supplied destination and waits for
     * the specified timeout interval for a response message.  If the response
     * message does not arrive before the timeout interval is exceeded, the
     * the result will be {@code null}.
     * 
     * @param message the message to send.
     * @param destination the destination to send the specified message to.
     * @param timeout the time interval to wait for a response message.
     *
     * @return a response message or {@code null} if the specified timeout
     * interval exceeded.
     *
     * @throws IllegalStateException can occur if the underlying connection
     * to the message broker fails or the specified message and destination
     * are in an invalid state.
     */
    protected String doSendAndListen(Message message, Destination destination, long timeout) {
        try {
            verifyAlive();

            // First we create a temporary response queue
            // and set it as the replyTo destination of the
            // message we're sending.

            Queue responseDestination = getSession().createTemporaryQueue();
            message.setJMSReplyTo(responseDestination);
            
            // Secondly we create a new message producer,
            // assign it the supplied destination, and send off
            // the given message.

            MessageProducer producer = getSession().createProducer(destination);
            producer.send(message);
            producer.close();

            // Now we can wait for a response message to arrive
            // at the temporary destination we created.  If the
            // message does not arrive within the specified timeout
            // interval we bail.

            MessageConsumer receiver = createConsumer(responseDestination);
            Message response = receiver.receive(timeout);

            // Lastly, if the response did not timeout and the response
            // is not null, we convert the received text message into
            // a string message and return the message.

            if (isNull(response)) return null;
            TextMessage target = (TextMessage) response;
            return target.getText();

        } catch (Exception exception) {
            destroy();
            throw new IllegalStateException("Failed to complete request/reply.", exception);
        }
    }

    /**
     * Verifies that this gateways connection is alive.  If the connection is
     * lost, then we re-initialize everything to ensure the proceeding messaging
     * operation succeeds. Essentially this method serves the purpose of a guard
     * against connectivity issues with the underlying message broker this
     * gateway communicates with.
     *
     * @throws IllegalStateException can occur if this gateway fails to
     * re-initialize due to a problem connecting to the underlying message
     * broker this gateway communicates with.
     */
    protected void verifyAlive() {
        if (! isConnectionValid()) {
            // Connection must be broken
            // lets reinitialize everything.
            initialize();
            resetProducer();
        }
    }

    /**
     * Restarts this gateway by closing down the current message broker
     * connection and reinitializing the connection, session, and message
     * producer used by this gateway to send and receive messages. This
     * method is mainly used when the underlying message broker connection
     * is lost.
     *
     * @throws IllegalStateException can occur if this gateway fails to
     * re-initialize due to a problem connecting to the underlying message
     * broker this gateway communicates with.
     */
    protected void restart() {
        close();
        initialize();
        resetProducer();
    }

    /**
     * Resets the message producer this gateway uses to send messages.
     * This method is mainly used when the underlying message broker
     * connection is lost.  Resetting the message producer is the last step
     * taken when re-initializing this gateway to its original state.
     *
     * @throws IllegalStateException can occur if the underlying message
     * broker connection this gateway uses is in a invalid state.
     */
    protected void resetProducer() {
        try {
            producer = createProducer();
        } catch (Exception exception) {
            throw new IllegalStateException("Failed to reset MessageProducer");
        }
    }
}
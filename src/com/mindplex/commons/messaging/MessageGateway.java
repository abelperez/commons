package com.mindplex.commons.messaging;

/**
 * A {@code MessageGateway} provides operations for sending and receiving
 * messages synchronously and asynchronously. This interface makes no
 * assumptions about any messaging providers and is generic in its pure sense.
 *
 * <p>There is three different messaging exchanges this gateway supports.
 * The first one is the fire-and-forgot messaging exchange.  This pattern
 * effectively send a one way message in a fire and forget fashion.  The
 * message producer does not block waiting for any kind of response.
 *
 * <p>The second messaging exchange is the request-reply pattern. This
 * pattern allows for sending messages and waiting for a response all
 * in one blocking operation. The message producer can block indefinitely
 * or for a specified timeout interval.  Behind the scenes this pattern
 * defines a temporary response queue to the message being sent. The
 * receiver of the message can then read the reply-to destination and
 * send a fire-and-forget type of response.
 *
 * <p>The third messaging style is solicit-response. In this pattern
 * the messaging gateway essentially becomes a message listener and is
 * solicited with response messages.  The gateway can be selective about
 * what messages to receive.  All messages are received asynchronously.
 *
 * <p>The following illustrated a manual requests and response example.
 * This is the most basic way to use this gateway.
 *
 * <p>
 * <pre>
 * MessageGateway gateway = ...
 * gateway.send("hello world", "mindplex");
 * String response = gateway.receive("mindplex", 10000);
 * </pre>
 *
 * <p>The next example illustrates the actual request-reply messaging pattern
 * through the {@code request} operation. A message is sent to the specified
 * destination and the operation blocks for the specified timeout interval of
 * 1 minute.
 *
 * <p>
 * <pre>
 * MessageGateway gateway = ...
 * String response = gateway.request("hello world", "mindplex", 60000);
 * </pre>
 *
 * <p>The last way this messaging gateway can be used is as a message listener.
 * The {@code receive} operation is basically used to attach to a destination
 * and consume the next available message.  Combined with the timeout interval
 * a simple loop around the {@code receive} method creates a listener effect.
 *
 * <p>For example, here we loop around the receive method and collect messages
 * as they come in.  This give us a nice throttling effect so we can control the
 * rate at which we consume messages.
 *
 * <p>
 * <pre>
 * MessageGateway gateway = ...
 * while (enabled) {
 *   String response = gateway.receive("mindplex", 2000);
 *   // do something with the response
 * }
 * </pre>
 *
 * @author Abel Perez
 */
public interface MessageGateway
{
    /**
     * Sends the specified {@code String} message to a default destination.
     * 
     * @param message the message to send.
     */
    public void send(String message);

    /**
     * Sends the specified {@code String} message to the given destination.
     *
     * @param message the message to send.
     * @param destination the destination to send the message to.
     */
    public void send(String message, String destination);

    /**
     * Sends the specified message to a default destination and waits for the
     * timeout interval.  If the response comes back before the timeout interval
     * is exceeded the actual response is returned; otherwise {@code null};
     * 
     * @param message the message to send.
     * @param timeout the timeout interval to block for.
     *
     * @return a {@code String} response message or {@code null} if the timeout
     * interval is exceeded.
     */
    public String request(String message, long timeout);

    /**
     * Sends the specified message to the given destination and waits for the
     * timeout interval.  If the response comes back before the timeout interval
     * is exceeded the actual response is returned; otherwise {@code null};
     *
     * @param message the message to send.
     * @param destination the destination to send the message to.
     * @param timeout the timeout interval to block for.
     *
     * @return a {@code String} response message or {@code null} if the timeout
     * interval is exceeded.
     */
    public String request(String message, String destination, long timeout);

    /**
     * Returns the next available message from the specified destination. If the
     * specified timeout interval is exceeded then {@code null} is returned.
     *
     * @param destination the destination to consume the next available message
     * from.
     * @param timeout the time to block waiting for a message to be received.
     *
     * @return the next available message or {@code null}.
     */
    public String receive(String destination, long timeout);

    /**
     * Rejects the specified message by sending it to this gateways invalid
     * message channel.
     *
     * <p>Message consumers that receive improper messages should reject the
     * message by sending it to an invalid message channel. This allows the
     * messaging system to become aware of messages that might be floating
     * around as the result of a bug or misconfiguration.
     *
     * @param message the message to reject.
     */
    public void reject(String message);
}

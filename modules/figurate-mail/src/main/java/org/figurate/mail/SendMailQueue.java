package org.figurate.mail;

import javax.mail.Message;

/**
 * Implementors provide an asynchronous deferred queue for sending email messages.
 */
public interface SendMailQueue {

    /**
     * Register a message to be sent.
     * @param message the email message to send
     */
    void send(Message message);

    /**
     * Cancel a message from being sent.
     * @param message the email message to cancel
     * @return true if message is successfully cancelled, otherwise false
     */
    boolean cancel(Message message);
}

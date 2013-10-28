package com.leading.xmpp_client.listener;

/** 
 * Runtime exceptions produced by wrong meta-data settings.
 *
 */
public class InvalidFormatException extends java.lang.RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidFormatException() {
        super();
    }

    public InvalidFormatException(String message) {
        super(message);
    }

    public InvalidFormatException(Throwable cause) {
        super(cause);
    }

    public InvalidFormatException(String message, Throwable cause) {
        super(message, cause);
    }

}

package com.trustasia.tagm.core;

/**
 * @version 1.0
 * @Author robin.li
 * @Date 2025-04-09 18:18:00
 */
public class SignerException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public SignerException(String message) {
        super(message);
    }

    public SignerException(String message, Throwable cause) {
        super(message, cause);
    }

    public SignerException(Throwable cause) {
        super(cause);
    }
}

package com.B305.ogym.exception.user;

import org.aspectj.bridge.Message;

public class ReservationDuplicateException extends RuntimeException {

    public ReservationDuplicateException(String message) {
        super(message);
    }
}

package com.B305.ogym.exception.user;


// resource가 존재하지 않을때 5xx 에러 대신에 HttpStatus.NOT_FOUND를 보낸다.
//@ResponseStatus(HttpStatus.NOT_FOUND)
public class AuthorityNotFoundException extends RuntimeException  {
    public AuthorityNotFoundException(String message) {
        super(message);
    }
}

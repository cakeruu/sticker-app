package com.grup30.stickerapp.application.exceptions;

import org.springframework.http.HttpStatus;

public class NotFoundException extends CustomException{
    public NotFoundException(String message) {
        super(message);
    }
    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.NOT_FOUND;
    }
}

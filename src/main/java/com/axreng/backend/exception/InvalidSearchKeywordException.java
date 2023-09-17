package com.axreng.backend.exception;

public class InvalidSearchKeywordException extends Exception{

    public InvalidSearchKeywordException() {
        super("field 'keyword' is required (from 4 up to 32 chars)");
    }

}

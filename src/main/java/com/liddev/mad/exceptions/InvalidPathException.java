package com.liddev.mad.exceptions;

/**
 *
 * @author Renlar <liddev.com>
 */
public class InvalidPathException extends MadException{
    public InvalidPathException(){}
    
    public InvalidPathException(String message){
        super(message);
    }
    
    public InvalidPathException(Throwable cause){
        super (cause);
    }

    public InvalidPathException(String message, Throwable cause){
        super (message, cause);
    }
}

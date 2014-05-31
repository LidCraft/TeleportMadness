package com.liddev.mad.core.exceptions;

/**
 *
 * @author Renlar <liddev.com>
 */
public class MadException extends Exception{
    public MadException(){}
    
    public MadException(String message){
        super(message);
    }
    
    public MadException(Throwable cause){
        super (cause);
    }

    public MadException(String message, Throwable cause){
        super (message, cause);
    }
    
}

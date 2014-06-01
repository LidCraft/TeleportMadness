/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.liddev.mad.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 *
 * @author Renlar <liddev.com>
 */
public class DynamicClassHelpers {
    
    
    public static <T> T instantiate(final String className, final Class<T> type){
        try{
            return type.cast(Class.forName(className).newInstance());
        } catch(final InstantiationException e){
            throw new IllegalStateException(e);
        } catch(final IllegalAccessException e){
            throw new IllegalStateException(e);
        } catch(final ClassNotFoundException e){
            throw new IllegalStateException(e);
        }
    }
    
    public static <T> T instantiate(final String className, final Class<T> type, Class<?>[] parameterTypes, Object[] parameters){
        try {
            return type.cast(getConstructor(className, parameterTypes).newInstance(parameters));
        } catch (ClassNotFoundException ex) {
            throw new IllegalStateException(ex);
        } catch (NoSuchMethodException ex) {
            throw new IllegalStateException(ex);
        } catch (InstantiationException ex) {
            throw new IllegalStateException(ex);
        } catch (IllegalAccessException ex) {
            throw new IllegalStateException(ex);
        } catch (IllegalArgumentException ex) {
            throw new IllegalStateException(ex);
        } catch (InvocationTargetException ex) {
            throw new IllegalStateException(ex);
        }
    }
    
    public static Constructor<?> getConstructor(final String className, Class<?>... parameterTypes ) throws ClassNotFoundException, NoSuchMethodException{
        return getClass(className).getConstructor(parameterTypes);
    }
    
    public static Class<?> getClass(final String className) throws ClassNotFoundException{
            return Class.forName(className);
    }
}

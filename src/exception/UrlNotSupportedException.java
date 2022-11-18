/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

/**
 *
 * @author HP
 */
public class UrlNotSupportedException extends Exception{
    String message = null;

    public UrlNotSupportedException() {
        message = "not supported url";
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import java.lang.reflect.Method;

/**
 *
 * @author HP
 */
public class ClassMethod {
    private Class classe;
    private Method methode;

    public ClassMethod() {
    }

    public ClassMethod(Class classe, Method methode) {
        this.classe = classe;
        this.methode = methode;
    }
    
    

    public Class getClasse() {
        return classe;
    }

    public Method getMethode() {
        return methode;
    }

    public void setClasse(Class classe) {
        this.classe = classe;
    }

    public void setMethode(Method methode) {
        this.methode = methode;
    }
}

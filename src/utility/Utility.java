/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import annotation.UrlAnnotation;
import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.ServletContext;

/**
 *
 * @author HP
 */
public class Utility {
    
    public String retrieveUrl(String url){
        String urlWithoutBackSlash = url.replaceFirst("/","");
	int lastIndex = urlWithoutBackSlash.lastIndexOf(".do");
        String lien =  urlWithoutBackSlash.substring(0,lastIndex);
        return lien;
    }
    
    //maka an class rehetra anaty package 
        public ArrayList<Class> getAllControllers(ServletContext context) throws Exception{
            
            ArrayList<Class> allClasses = new ArrayList<Class>();
            try{
                
                //realpath =  emplacement anle project
                String pathPackage = context.getRealPath("/") + "WEB-INF\\classes\\controllers";
                pathPackage = pathPackage.replace("\\","/");
                //makao amle dossier classes
                File dossier = new File(pathPackage);
                //maka anle class tsirairay
                File[] fichiers = dossier.listFiles();
                Class temp = null;
                for(File fileClass : fichiers){
                    //ex : calculator.class => fileClass.getName().substring(0,fileClass.getName().lastIndexOf('.')) --> manala anle .class
                    //mamorina objet de type anle class
                    temp = Class.forName("controllers." + fileClass.getName().substring(0,fileClass.getName().lastIndexOf('.')));
                    allClasses.add(temp);
                }
                
            }
            catch(Exception e){
                
                throw e;
            }
            return allClasses;
        }
        
        public void fillHashMap(ServletContext context) throws Exception{
            
            ArrayList<Class> allClasses = this.getAllControllers(context);
            HashMap<String , ClassMethod> map = (HashMap)context.getAttribute("map");
            
            Method[] tempMethods = null;
            UrlAnnotation temp = null;
            ClassMethod tempClassMethod = null;
            
            for(Class classe : allClasses){
                
                   tempMethods = classe.getMethods();
                   for(Method method : tempMethods){
                       
                       if(method.isAnnotationPresent(UrlAnnotation.class)){
                           
                           temp = method.getAnnotation(UrlAnnotation.class);
                           tempClassMethod = new ClassMethod(classe, method);
                           String lien = temp.lien();
                           map.put(lien, tempClassMethod);
                           //System.out.println("tafiditra");
                       }
                   }
            }
            context.setAttribute("map",map);
        }
}

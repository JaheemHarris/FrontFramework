/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import annotation.UrlAnnotation;
import exception.UrlNotSupportedException;
import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

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
        
        public ClassMethod getClassMethod(ServletContext context,String url){
            
            ClassMethod classMethod = null;
            HashMap<String , ClassMethod > map = (HashMap<String , ClassMethod>)context.getAttribute("map");
            String rightUrlMethod = retrieveUrl(url);
            if(map.containsKey(rightUrlMethod)){
                
                classMethod = (ClassMethod)map.get(rightUrlMethod);
            }
            return classMethod;
        }
        
        public void  checkUrl(ServletContext context,String url) throws UrlNotSupportedException{
            
            HashMap<String , ClassMethod > map = (HashMap<String , ClassMethod>)context.getAttribute("map");
            String rightUrlMethod = retrieveUrl(url);
            if(!map.containsKey(rightUrlMethod)){
                
                throw new UrlNotSupportedException();
            }
        }
        
        public void sendData(HttpServletRequest request,ModelView modelview){
            
            HashMap map = modelview.getMap();
            map.forEach(
            (key, value)
                -> request.setAttribute((String)key,value));
        }
}

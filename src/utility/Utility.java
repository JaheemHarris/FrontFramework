/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import annotation.UrlAnnotation;
import exception.UrlNotSupportedException;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        
        public Object saveData(HttpServletRequest request, ClassMethod classMethod) throws Exception{
            Object controllerObject = classMethod.getClasse().newInstance();
            HashMap<String, List> classParams = this.getInsertParameters(request, classMethod);
            classParams.forEach(
                    (className, names)
                            -> {
                        Object classInstance = null;
                        List<String> paramNames = names;
                        Field[] fields = classMethod.getClasse().getDeclaredFields();
                        for(Field field : fields){
                            try{
                                Method[] listMethods = classMethod.getClasse().getDeclaredMethods();
                                Method controlMethod = null;
                                for(Method method : listMethods){;
                                    if(method.getName().compareToIgnoreCase(toSetterName(className)) == 0){
                                        controlMethod = method;
                                        break;
                                    }
                                }
                                String simpleClassName = field.getType().getSimpleName();
                                if(simpleClassName.compareToIgnoreCase(className) == 0 && controlMethod != null){
                                    classInstance = field.getType().newInstance();
                                    for(String name : paramNames){
                                        Method[] fieldMethods = classInstance.getClass().getDeclaredMethods();
                                        Method fieldMethod = null;
                                        for(Method method : fieldMethods){;
                                            if(method.getName().compareToIgnoreCase(toSetterName(name)) == 0){
                                                fieldMethod = method;
                                                break;
                                            }
                                        }
                                        if(fieldMethod != null){
                                            String requestParamName = className+"."+name;
                                            fieldMethod.invoke(classInstance, request.getParameter(requestParamName));
                                        }
                                    }
                                    controlMethod.invoke(controllerObject, classInstance);
                                    break;
                                }
                            }catch (InstantiationException ex) {
                                Logger.getLogger(Utility.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IllegalAccessException ex) {
                                Logger.getLogger(Utility.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IllegalArgumentException ex) {
                                Logger.getLogger(Utility.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (InvocationTargetException ex) {
                                Logger.getLogger(Utility.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    } 
            );
            return controllerObject;
        }
        
        public HashMap getInsertParameters(HttpServletRequest request, ClassMethod classMethod){
            HashMap<String, List<String>> classParams = new HashMap<String, List<String>>();
            Enumeration<String> paramNames = request.getParameterNames();
            while(paramNames.hasMoreElements()){
                String paramName = paramNames.nextElement();
                String[] paramSplit = paramName.split("\\.");
                if(paramSplit.length == 2){
                    String classParam = paramSplit[0];
                    String param = paramSplit[1];
                    List<String> paramsList = new ArrayList();
                    if(classParams.containsKey(classParam)){
                        paramsList = (List<String>)classParams.get(classParam);
                    }
                    paramsList.add(param);
                    classParams.put(classParam, paramsList);
                }
            }
            return classParams;
        }
        
        public String firstLetterUpperCase(String toChange){
            char[] toChangeChar = toChange.toCharArray();
            String firstLetter = String.valueOf(toChangeChar[0]);
            String returnValue = firstLetter.toUpperCase();
            for(int i=1;i<toChangeChar.length;i++){
                returnValue = returnValue + String.valueOf(toChangeChar[i]);
            }
            return returnValue;
        }
        
        public String toSetterName(String name){
            String setterName = "set"+firstLetterUpperCase(name);
            return setterName;
        }
}

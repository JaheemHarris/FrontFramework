# FrontFramework
> [_FrontFramework_](https://github.com/JaheemHarris/FrontFramework) is a lightweight Java Web Framework for building web mvc applications.

It explains how Spring Controllers and Spring Controller annotations works.

## Installation

> Import FrontFramework.jar in your library project.

## How to use it

Put your controller classes in a package named controllers so they could be identified by the framework

```java
package controllers;
```

Put your model classes in a package named models so they could be identified by the framework
```java
package models;
```

 Import these classes
```java
import annotation.UrlAnnotation;
import utility.ModelView;
```



This is how you should design your controller functions
URLs must end with ".do"




This function send data to page
GET method then
```java

// Use @UrlAnnotation to execute this function if the endpoint without .do at the end matches the value of "lien"
// ex: http://localhost/my-app/heroes.do


// But you can also access this function if the function name matches the endpoint without the .do at the end
// ex : http://localhost/my-app/getHeroes.do


// The function should return ModelView Object

@UrlAnnotation(lien = "heroes")
public ModelView getHeroes() throws Exception{

  // Instanciate the ModelView object
  ModelView modelView = new ModelView();
  
  List<Hero> heroesList = new ArrayList<Hero>();
  
  /* Implement your code here to get heroesList
  ...
  */
  
  // Send data to pages with ModelView object with key, value pairs
  modelView.addData("heroes", heroesList);
  
  // ModelView Object must contain the page to load or the link to get redirected
  modelView.setPage("liste-emp.jsp");
  
  return modelView;
}
```


This one when data is sent in the request
Through POST or GET method

```java
// ex: POST http://localhost/my-app/hero.do with parametes name, level
// or ex: GET http://localhost/my-app/hero.do?name=SonGoku&level=15
// Parameters must match Hero Class Model attribure
// These parameters are automatically binded on the model class matches attributes

//Hero Class
// Model class must contain default constructor without parameters
// with get,set functions for each attributes
public class Hero{
    private String name;
    private int level;

    public Hero(){
    }
    
    /* Impletementation of getters and setters methods here (getName, setName, getLevel, setLevel)
       ...
    */
}

//import your model class to the controller
import models.Hero;

//The controller must have the model as an attribute
private Hero hero;


// POST, GET parameters sended : name=SonGoku , level=1
@UrlAnnotation(lien = "hero")
    public ModelView addEmp() throws Exception{
        ModelView modelView = new ModelView();
        try{
            /* Implement code to store the attribute here
               ...
               ex: heroService.add(hero);
            */
            
            // if it is successfully done
            modelView.setPage("hero.do?success");
        }catch(Exception e){
            // it error occurs
            modelView.setPage("hero.do?fail");
        }
        return modelView;
    }
```



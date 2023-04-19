# FrontFramework
> [_FrontFramework_](https://github.com/JaheemHarris/FrontFramework) is a lightweight Java Web Framework for building web mvc applications.

It explains how Spring Controllers and Spring Controller annotations works.

## Installation

> Import FrontFramework.jar in your library project.

## How to use it

Put your controllers classes in a package named controllers so they could be identified by the framework

```java
package controllers;
```

 Import these classes
```java
import annotation.UrlAnnotation;
import utility.ModelView;
```



This is how you should design your controller functions
```java

// Use @UrlAnnotation to execute this function if the endpoint matches the value of "lien"
// The function should return ModelView Object
// ex: http://localhost/my-app/heroes
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



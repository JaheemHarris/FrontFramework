/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import java.util.HashMap;

/**
 *
 * @author HP
 */
public class ModelView {
    HashMap map ;
    String page;
    
    public ModelView(){
        map = new HashMap();
    }

    public ModelView(String page) {
        this.page = page;
        map = new HashMap();
    }
    
    public void addData(String key,Object o){
        
        map.put(key,o);
    }

    public HashMap getMap() {
        return map;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

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
}

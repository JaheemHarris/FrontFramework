/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package front;

import exception.UrlNotSupportedException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utility.ClassMethod;
import utility.Utility;

/**
 *
 * @author HP
 */
@WebServlet(name = "FrontServlet", urlPatterns = "*.do")
public class FrontServlet extends HttpServlet {
    
    private Utility utility;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    @Override
    public void init() throws ServletException{
        ServletContext context = getServletContext();
        if(context.getAttribute("map") == null){
            context.setAttribute("map", new HashMap<String, ClassMethod>());
        }
       
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try{
                
                //path aty arina
                String servletPath = request.getServletPath();
                ServletContext context = this.getServletContext();
                utility = new Utility();
                utility.fillHashMap(context);
                try{
                    utility.checkUrl(context, servletPath);
                }catch (UrlNotSupportedException ex) {
                    throw ex;
                }
                ClassMethod classMethod = utility.getClassMethod(context, servletPath);
                Method methode = classMethod.getMethode();
                Class classe = classMethod.getClasse();
                methode.invoke(classe.newInstance());
            }catch(Exception e){
                try {
                    throw e;
                } catch (Exception ex) {
                    Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}

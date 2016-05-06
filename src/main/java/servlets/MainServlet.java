package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 *
 * @author brunogamacatao
 */
@WebServlet(name = "MainServlet", urlPatterns = {"/main"})
public class MainServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        System.out.println("looking for cookie");
        if(request.getCookies()!=null) {
            for (Cookie c : request.getCookies()) {
                if (c.getName().equals("aardvark")) {
                    System.out.println("adding cookie");
                    response.addCookie(c);
                }
            }
        }

        try (PrintWriter out = response.getWriter()) {
            printDocument(out, request, response);
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

        if (request.getParameter("action") != null) {
            String action = request.getParameter("action");
            System.out.println("action = " + action);
            if (action.equals("delete")) {
                HttpSession session = request.getSession(true);
                String key = request.getParameter("key");
                System.out.println("key = " + key);
                session.removeAttribute(key);
            }
        }

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
        if (request.getParameter("action") == null) {
            HttpSession session = request.getSession(true);
            
            if (request.getParameter("key") != null) {
                String key = request.getParameter("key");
                String value = request.getParameter("value");
                session.setAttribute(key, value);
            }
        }

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

    private void printDocument(PrintWriter out, HttpServletRequest request, HttpServletResponse response) {
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        printHead(out, request, response);
        printBody(out, request, response);
        out.println("</html>");
    }
    
    private void printHead(PrintWriter out, HttpServletRequest request, HttpServletResponse response) {
        out.println(
        "<head>\n" +
        "        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
        "        <title>Http Session Inspector</title>\n" +
        "        <!-- Latest compiled and minified CSS -->\n" +
        "        <link rel=\"stylesheet\" href=\"css/bootstrap.min.css\">\n" +
        "        <!-- Optional theme -->\n" +
        "        <link rel=\"stylesheet\" href=\"css/bootstrap-theme.min.css\">\n" +
        "        <!-- Latest compiled and minified JavaScript -->\n" +
        "        <script src=\"js/bootstrap.min.js\"></script>\n" +
        "    </head>");
    }
    
    private void printBody(PrintWriter out, HttpServletRequest request, HttpServletResponse response) {
        out.println("<body>");
        out.println("<div class=\"container\">");
        printSessionInfo(out, request, response);
        printDataTable(out, request, response);
        printForm(out, request, response);
        out.println("</div>");
        out.println("</body>");
    }
    
    private void printSessionInfo(PrintWriter out, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        
        out.println("<h3>Session Type:");
        out.println(session == null ? "null" : session.getClass());
        out.println("</h3>");
        out.println("<h3>Session ID:");
        out.println(session == null ? "null" : session.getId());
        out.println("</h3>");
    }
    
    private void printDataTable(PrintWriter out, HttpServletRequest request, HttpServletResponse response) {
        out.println(
        "<table class=\"table table-bordered\">\n" +
        "    <thead>\n" +
        "        <tr>\n" +
        "            <th>Key</th>\n" +
        "            <th>Value</th>\n" +
        "            <th>Type</th>\n" +
        "            <th>Delete</th>\n" +
        "        </tr>\n" +
        "    </thead>\n" +
        "    <tbody>");
        
        HttpSession session = request.getSession();
        if (session != null) {
            java.util.Enumeration<String> names = session.getAttributeNames();
            while (names.hasMoreElements()) {
                String name = names.nextElement();
                Object value = session.getAttribute(name);
                String type = value.getClass().getName();
                
                out.println(
                "<tr>\n" +
                "  <td>" + name + "</td>\n" +
                "  <td>" + value + "</td>\n" +
                "  <td>" + type + "</td>\n" +
                "  <td><a href=\"main?action=delete&key=" + name + "\">Delete</a></td>\n" +
                "</tr>");
            }
        }
        
        out.println(
        "    </tbody>\n" +
        "</table>");
    }
    
    private void printForm(PrintWriter out, HttpServletRequest request, HttpServletResponse response) {
        out.println(
        "<form action=\"" + getServletContext().getContextPath() + "/main\" method=\"POST\" class=\"form-horizontal\">\n" +
        "    <div class=\"form-group\">\n" +
        "        <label class=\"col-sm-2 control-label\">Key: </label>\n" +
        "        <div class=\"col-sm-10\">\n" +
        "            <input type=\"text\" name=\"key\"/>\n" +
        "        </div>\n" +
        "    </div>\n" +
        "    <div class=\"form-group\">\n" +
        "        <label class=\"col-sm-2 control-label\">Value: </label>\n" +
        "        <div class=\"col-sm-10\">\n" +
        "            <input type=\"text\" name=\"value\"/>\n" +
        "        </div>\n" +
        "    </div>\n" +
        "    <div class=\"form-group\">\n" +
        "        <div class=\"col-sm-offset-2 col-sm-10\">\n" +
        "            <input type=\"submit\" value=\"Add Key\"/>\n" +
        "        </div>\n" +
        "    </div>\n" +
        "</form>");
    }
}

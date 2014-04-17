package scripts

import javax.servlet.Servlet
import javax.servlet.ServletException
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by fortuna on 28/02/14.
 */
Hashtable props = [];
props.put("alias", "/hello");
props.put("init.message", "Hello World!");

def registration = context.registerService(Servlet.name, new HttpServlet() {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.writer.write("Hello World");
    }
}, props);

package filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import models.User;
import services.AccountService;

public class AdminFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // before we can use HttpServletRequest or HttpServletResponse methods
        // we must cast the ServletRequest and ServletResponse objects as the correct type
        Integer adminRole = 1;
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession();
        String email = (String) session.getAttribute("email");
        AccountService as = new AccountService();
        User user = as.get(email);

        if (user == null) {
            // Never gonna happen because the AuthenticationFilter prevents to reaches
            // here without being logged, but just in case this code is here 
            // to prevent unhandled exception when user object is null
            httpResponse.sendRedirect("login");
            return;
        } else if (user.getRole().getRoleId().equals(adminRole)) {
            // If has admin role, proceed to the admin page
            chain.doFilter(request, response);
        } else {
            // Not an admin, sending to the notes page
            httpResponse.sendRedirect("notes");
            return;
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

}

package crm11.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter(filterName = "authenticationFilter", urlPatterns = {
        "/user-table", "/user-add", "/user-details", "/user-delete", "/user-edit",
        "/role-table", "/role-add", "/role-delete", "/role-edit",
        "/groupwork", "/groupwork-add", "/groupwork-details", "/groupwork-delete", "/groupwork-edit",
        "/task", "/task-add", "/task-delete", "/task-edit"
})
public class AuthenticationFilter implements Filter {

    // Các trang KHÔNG cần admin
    private static final List<String> NON_ADMIN_PAGES = Arrays.asList(
            "/user-table",
            "/user-details",
            "/role-table",
            "/task",
            "/groupwork",
            "/groupwork-details"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
    	
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String role = null;

        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("role".equals(cookie.getName())) {
                    role = cookie.getValue();
                    break;
                }
            }
        }

        // chưa đăng nhập nhảy về trang login
        if (role == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        String url = req.getServletPath();

        // Trang cần quyền ADMIN
        if (!NON_ADMIN_PAGES.contains(url)) {
            if (!"ROLE_ADMIN".equals(role)) {
                resp.sendRedirect(req.getContextPath() + "/404.html");
                return;
            }
        }
        
        chain.doFilter(request, response);
    }
}

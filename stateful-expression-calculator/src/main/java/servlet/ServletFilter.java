package servlet;

//import javax.servlet.*;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.time.LocalDateTime;

@WebFilter("/calc/*")
public class ServletFilter implements Filter {
    private FilterConfig filterConfig;
    private boolean active;

    @Override
    public void init(FilterConfig config) {
        this.filterConfig = config;
        String act = config.getInitParameter("active");
        if (act != null) {
            active = (act.equalsIgnoreCase("true"));
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException, ServletException {
        if (active) {
            if (isAllowToWork()) {
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                String path = "/src/main/webapp/badServer.jsp";
                ServletContext servletContext = filterConfig.getServletContext();
                RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(path);
                requestDispatcher.forward(servletRequest, servletResponse);
            }
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    private boolean isAllowToWork() {
        return LocalDateTime.now()
                .isBefore(LocalDateTime.of(2021, 10, 1, 10, 0, 0));
    }

    @Override
    public void destroy() {
        this.filterConfig = null;
    }
}
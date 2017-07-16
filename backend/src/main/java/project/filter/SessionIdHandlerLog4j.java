package project.filter;


import org.apache.commons.lang.StringUtils;
import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class SessionIdHandlerLog4j implements Filter {

    private final static String SESSION_ID_KEY = "sessionId";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest)) {
            return;
        }
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            MDC.put(SESSION_ID_KEY, normalizeJSESSIONID(httpServletRequest.getSession().getId()));
            chain.doFilter(request, response);
        } finally {
            MDC.remove(SESSION_ID_KEY);
        }
    }

    @Override
    public void destroy() {
    }

    private static String normalizeJSESSIONID(String sessionID) {
        if (StringUtils.isEmpty(sessionID)) {
            return null;
        }
        if (sessionID.indexOf(32) != -1) {
            return sessionID.substring(0, sessionID.indexOf(32));
        }
        return sessionID;
    }
}

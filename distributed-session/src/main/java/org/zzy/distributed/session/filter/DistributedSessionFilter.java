package org.zzy.distributed.session.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zzy.distributed.session.SessionHttpServletRequestWrapper;
import org.zzy.distributed.session.SessionHttpServletResponseWrapper;
import org.zzy.distributed.session.manager.SessionManager;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by zhaoyu on 16-8-19.
 */
public class DistributedSessionFilter implements SessionFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(DistributedSessionFilter.class);

    private SessionManager sessionManager;

    public DistributedSessionFilter() {
        LOGGER.info("DistributedSessionFilter constructor");
    }

    public DistributedSessionFilter(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOGGER.info("DistributedSessionFilter init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        SessionHttpServletRequestWrapper requestWrapper = new SessionHttpServletRequestWrapper(
                (HttpServletRequest) request,
                (HttpServletResponse) response,
                sessionManager
        );
        chain.doFilter(requestWrapper, response);
    }

    @Override
    public void destroy() {

    }
}

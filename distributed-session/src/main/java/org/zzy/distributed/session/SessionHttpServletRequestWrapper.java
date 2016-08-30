package org.zzy.distributed.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zzy.distributed.session.manager.SessionManager;
import org.zzy.distributed.session.repository.SessionRepository;

import javax.servlet.http.*;

/**
 * Created by zhaoyu on 16-8-24.
 */
public class SessionHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(SessionHttpServletRequestWrapper.class);

    private SessionManager sessionManager;
    private HttpServletRequest request;
    private HttpServletResponse response;

    private HttpSession session;
    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request
     * @throws IllegalArgumentException if the request is null
     */
    public SessionHttpServletRequestWrapper(HttpServletRequest request,
                                            HttpServletResponse response,
                                            SessionManager sessionManager) {
        super(request);
        this.sessionManager = sessionManager;
        this.response = response;
        this.request = request;
    }

    @Override
    public HttpSession getSession() {
        return getSession(Boolean.TRUE);
    }

    @Override
    public HttpSession getSession(boolean create) {
        if (session != null) {
            return session;
        }
        else {
            session = this.sessionManager.getSession(this, response, create);
        }
        return session;
    }

    public String getSessionIdFromHttpHeader(String name) {
        return request.getHeader(name);
    }

    public String getParameterValueFromCookie(String name) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null || cookies.length == 0) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                return cookie.getValue().trim();
            }
        }

        return null;
    }
}

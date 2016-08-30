package org.zzy.distributed.session.manager;

import org.zzy.distributed.session.DefaultSession;
import org.zzy.distributed.session.SessionHttpServletRequestWrapper;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by zhaoyu on 16-8-19.
 */
public interface SessionManager {

    HttpSession getSession(SessionHttpServletRequestWrapper requestWrapper,
                           HttpServletResponse response, boolean create);

    String getSessionIdName();
    DefaultSession getSessionFromCache(String sessionId);
    void updateSession(DefaultSession defaultSession);
    void removeSession(String sessionId);

    void writeDistributedSessionIdToCookie(
            HttpSession session, HttpServletResponse response);
}

package org.zzy.distributed.session.manager;

import org.zzy.distributed.session.DefaultSession;
import org.zzy.distributed.session.SessionHttpServletRequestWrapper;
import org.zzy.distributed.session.SessionProxy;
import org.zzy.distributed.session.repository.SessionRepository;
import org.zzy.distributed.session.utils.StringUtils;
import org.zzy.distributed.session.utils.UUIDUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by zhaoyu on 16-8-19.
 */
public class CachedSessionManager implements SessionManager {

    private SessionRepository sessionRepository;

    private String sessionIdName = "distributed_session_id";
    private int sessionMaxInactiveInterval = 6 * 60;
    private int cookieExpireTime = 30 * 60;
    private String cookieDomain = ".localhost";
    private String cookiePath = "/";


    public CachedSessionManager() {
    }

    @Override
    public HttpSession getSession(SessionHttpServletRequestWrapper requestWrapper,
                                  HttpServletResponse response,
                                  boolean create) {
        DefaultSession session = null;

        String sessionId = requestWrapper.getParameterValueFromCookie(sessionIdName);
        SessionProxy sessionProxy = null;
        if (StringUtils.isNotBlank(sessionId)) {
            session = getSessionFromCache(sessionId);
            // session expired
            if (session == null) {
                session = createEmptyHttpSession();
                sessionProxy = new SessionProxy(session, this, response);
                // save to redis
                sessionRepository.put(session.getId(), session);

                // write cookie into response
                writeDistributedSessionIdToCookie(session, response);
            }
            else {
                sessionProxy = new SessionProxy(session, this, response);
                sessionProxy.setNew(Boolean.FALSE);
                sessionProxy.setLastAccessedTime(System.currentTimeMillis());
                sessionProxy.setThisAccessedTime(System.currentTimeMillis());
            }
        }

        if (session == null && create) {
            session = createEmptyHttpSession();
            sessionProxy = new SessionProxy(session, this, response);
            // save to redis
            sessionRepository.put(session.getId(), session);
        }
        if (session != null && session.isNew()) {
            // write cookie into response
            writeDistributedSessionIdToCookie(session, response);
        }
        return sessionProxy;
    }

    @Override
    public String getSessionIdName() {
        return this.sessionIdName;
    }

    @Override
    public DefaultSession getSessionFromCache(String sessionId) {
        return sessionRepository.get(sessionId);
    }

    @Override
    public void updateSession(DefaultSession defaultSession) {
        sessionRepository.put(defaultSession.getId(), defaultSession);
    }

    @Override
    public void removeSession(String sessionId) {
        sessionRepository.evict(sessionId);
    }

    private DefaultSession createEmptyHttpSession() {
        DefaultSession defaultSession = new DefaultSession(
                UUIDUtils.generate(),
                System.currentTimeMillis(),
                System.currentTimeMillis(),
                sessionMaxInactiveInterval,
                Boolean.TRUE,
                System.currentTimeMillis()
        );
        return defaultSession;
    }

    @Override
    public void writeDistributedSessionIdToCookie(HttpSession session,
                                                   HttpServletResponse response) {
        Cookie cookie = new Cookie(sessionIdName, session.getId());
        cookie.setDomain(cookieDomain);
        cookie.setPath(cookiePath);
        if (session.getMaxInactiveInterval() == 0) {
            // delete cookie
            cookie.setMaxAge(0);
        } else {
            cookie.setMaxAge(cookieExpireTime);
        }
        response.addCookie(cookie);
    }

    public void setCookieDomain(String cookieDomain) {
        this.cookieDomain = cookieDomain;
    }

    public void setSessionRepository(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public void setSessionIdName(String sessionIdName) {
        this.sessionIdName = sessionIdName;
    }

    public void setCookieExpireTime(int cookieExpireTime) {
        this.cookieExpireTime = cookieExpireTime;
    }
}

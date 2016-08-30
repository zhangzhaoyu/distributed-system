package org.zzy.distributed.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zzy.distributed.session.manager.SessionManager;
import org.zzy.distributed.session.utils.StringUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.util.Enumeration;

/**
 * Created by zhaoyu on 16-8-30.
 */
public class SessionProxy implements HttpSession {

    private static final Logger LOGGER = LoggerFactory.getLogger(SessionProxy.class);

    private DefaultSession session;
    private transient SessionManager sessionManager;
    private transient SessionListener sessionListener;

    public SessionProxy(DefaultSession session,
                        SessionManager sessionManager,
                        HttpServletResponse response) {
        this.sessionManager = sessionManager;
        this.session = session;
        // add session listener
        this.sessionListener = new SessionListener() {
            @Override
            public void update(HttpSession arg) {
                if (arg instanceof DefaultSession) {
                    LOGGER.info("update session info.");
                    DefaultSession session = (DefaultSession) arg;
                    sessionManager.updateSession(session);
                }
                else {
                    throw new IllegalArgumentException("not the instance of" + DefaultSession.class.getName());
                }
            }

            @Override
            public void remove(HttpSession httpSession) {
                LOGGER.info("remove session.");
                sessionManager.removeSession(httpSession.getId());
                sessionManager.writeDistributedSessionIdToCookie(httpSession, response);

            }
        };
    }

    @Override
    public long getCreationTime() {
        this.session = sessionManager.getSessionFromCache(getId());
        return this.session.getCreationTime();
    }

    @Override
    public String getId() {
        return this.session.getId();
    }

    @Override
    public long getLastAccessedTime() {
        this.session = sessionManager.getSessionFromCache(getId());
        return this.session.getLastAccessedTime();
    }

    @Override
    public ServletContext getServletContext() {
        return null;
    }

    @Override
    public void setMaxInactiveInterval(int interval) {
        this.session.setMaxInactiveInterval(interval);
        sessionListener.update(this.session);
    }

    @Override
    public int getMaxInactiveInterval() {
        this.session = sessionManager.getSessionFromCache(getId());
        return this.session.getMaxInactiveInterval();
    }

    @Override
    public HttpSessionContext getSessionContext() {
        return null;
    }

    @Override
    public Object getAttribute(String name) {
        this.session = sessionManager.getSessionFromCache(getId());
        return this.session.getAttribute(name);
    }

    @Override
    public Object getValue(String name) {
        this.session = sessionManager.getSessionFromCache(getId());
        return this.session.getValue(name);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        this.session = sessionManager.getSessionFromCache(getId());
        return this.session.getAttributeNames();
    }

    @Override
    public String[] getValueNames() {
        this.session = sessionManager.getSessionFromCache(getId());
        return this.session.getValueNames();
    }

    @Override
    public void setAttribute(String name, Object value) {
        this.session.setAttribute(name, value);
        sessionListener.update(this.session);
    }

    @Override
    public void putValue(String name, Object value) {
        this.session.putValue(name, value);
        sessionListener.update(this.session);
    }

    @Override
    public void removeAttribute(String name) {
        this.session.removeAttribute(name);
        sessionListener.update(this.session);
    }

    @Override
    public void removeValue(String name) {
        this.session.removeValue(name);
        sessionListener.update(this.session);
    }

    @Override
    public void invalidate() {
        // 0 delete cookie right now
        // -1 after the browser closed
        this.session.setMaxInactiveInterval(0);
        sessionListener.remove(this.session);
    }

    @Override
    public boolean isNew() {
        this.session = sessionManager.getSessionFromCache(getId());
        return this.session.isNew();
    }

    public void setNew(boolean aNew) {
        this.session.setNew(aNew);
        sessionListener.update(this.session);
    }

    public void setThisAccessedTime(long thisAccessedTime) {
        this.session.setThisAccessedTime(thisAccessedTime);
        sessionListener.update(this.session);
    }

    public void setLastAccessedTime(long lastAccessedTime) {
        this.session.setLastAccessedTime(lastAccessedTime);
        sessionListener.update(this.session);
    }

    @Override
    public String toString() {
        return this.session != null? this.session.toString() : "null";
    }
}

package org.zzy.distributed.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by zhaoyu on 16-8-23.
 */
public class DefaultSession implements HttpSession, Serializable {

    private static final long serialVersionUID = 7481895757921747303L;
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSession.class);


    private String id;
    private long creationTime = 0L;
    private long lastAccessedTime = 0L;
    private int maxInactiveInterval = 0;
    private boolean isNew = false;
    private long thisAccessedTime = 0L;

    private Map<String, Object> data = new HashMap<>();

    public DefaultSession() {
    }

    public DefaultSession(String id, long creationTime,
                          long lastAccessedTime,
                          int maxInactiveInterval,
                          boolean isNew,
                          long thisAccessedTime) {
        this.id = id;
        this.creationTime = creationTime;
        this.lastAccessedTime = lastAccessedTime;
        this.maxInactiveInterval = maxInactiveInterval;
        this.isNew = isNew;
        this.thisAccessedTime = thisAccessedTime;
    }

    @Override
    public long getCreationTime() {
        return this.creationTime;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public long getLastAccessedTime() {
        return this.lastAccessedTime;
    }

    @Override
    @Deprecated
    public ServletContext getServletContext() {
        return null;
    }

    @Override
    public void setMaxInactiveInterval(int interval) {
        this.maxInactiveInterval = interval;
    }

    @Override
    public int getMaxInactiveInterval() {
        return this.maxInactiveInterval;
    }

    @Override
    public HttpSessionContext getSessionContext() {
        return null;
    }

    @Override
    public Object getAttribute(String name) {
        return data.get(name);
    }

    @Override
    public Object getValue(String name) {
        return getAttribute(name);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        Set<String> keySet = this.data.keySet();
        return new Enumeration<String>() {
            @Override
            public boolean hasMoreElements() {
                return keySet.iterator().hasNext();
            }

            @Override
            public String nextElement() {
                return keySet.iterator().next();
            }
        };
    }

    @Override
    public String[] getValueNames() {
        String[] names = new String[data.size()];
        return data.keySet().toArray(names);
    }

    @Override
    public void setAttribute(String name, Object value) {
        this.data.put(name, value);
    }

    @Override
    public void putValue(String name, Object value) {
        setAttribute(name, value);
    }

    @Override
    public void removeAttribute(String name) {
        this.data.remove(name);
    }

    @Override
    public void removeValue(String name) {
        removeAttribute(name);
    }

    @Override
    public void invalidate() {
        this.setMaxInactiveInterval(0);
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public void setThisAccessedTime(long thisAccessedTime) {
        this.thisAccessedTime = thisAccessedTime;
    }

    public void setLastAccessedTime(long lastAccessedTime) {
        this.lastAccessedTime = lastAccessedTime;
    }

    @Override
    public String toString() {
        return "DefaultSession{" +
                "thisAccessedTime=" + thisAccessedTime +
                ", isNew=" + isNew +
                ", maxInactiveInterval=" + maxInactiveInterval +
                ", lastAccessedTime=" + lastAccessedTime +
                ", creationTime=" + creationTime +
                ", id='" + id + '\'' +
                '}';
    }
}
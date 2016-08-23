package org.zzy.distributed.session.filter;

import org.zzy.distributed.session.manager.SessionManager;

/**
 * Created by zhaoyu on 16-8-19.
 */
public class DistributedSessionFilter implements SessionFilter {

    private SessionManager sessionManager;

    public DistributedSessionFilter() {
    }

    public DistributedSessionFilter(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }
}

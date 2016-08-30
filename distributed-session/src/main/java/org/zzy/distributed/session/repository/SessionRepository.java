package org.zzy.distributed.session.repository;

import org.zzy.distributed.session.DefaultSession;

/**
 * Created by zhaoyu on 16-8-19.
 */
public interface SessionRepository {

    String SESSION_PREFIX = "session";

    DefaultSession get(Object key);
    void put(Object key, DefaultSession session);
    void put(Object key, DefaultSession session, long timeout);
    void putIfAbsent(Object key, DefaultSession defaultSession);
    void evict(Object key);

    default String generateKey(Object key) {
        return SESSION_PREFIX + ":" + key;
    }
}

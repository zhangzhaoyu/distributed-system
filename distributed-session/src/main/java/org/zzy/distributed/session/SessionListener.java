package org.zzy.distributed.session;

import javax.servlet.http.HttpSession;
import java.util.Observer;

/**
 * Created by zhaoyu on 16-8-30.
 */
public interface SessionListener {
    void update(HttpSession session);
    void remove(HttpSession session);
}

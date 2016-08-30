package org.zzy.distributed.session.utils;

import java.util.UUID;

/**
 * Created by zhaoyu on 16-8-25.
 */
public abstract class UUIDUtils {
    private UUIDUtils() {}

    public static String generate() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static void main(String[] args) {
        System.out.println(generate());
    }
}

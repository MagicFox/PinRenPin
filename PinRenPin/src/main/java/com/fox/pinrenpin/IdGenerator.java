package com.fox.pinrenpin;

import java.util.UUID;

/**
 * Created by MagicFox on 2016/8/19.
 */
public class IdGenerator {
    private IdGenerator() {
    }

    public static String create() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}

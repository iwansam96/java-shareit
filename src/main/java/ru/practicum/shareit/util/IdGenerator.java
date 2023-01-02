package ru.practicum.shareit.util;

public class IdGenerator {
    private static Long itemId = 1L;
    private static Long userId = 1L;

    public static Long generateId(String type) {
        if ("item".equals(type))
            return itemId++;
        else if ("user".equals(type)) {
            return userId++;
        }
        else
            return -1L;
    }
}

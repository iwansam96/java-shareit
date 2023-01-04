package ru.practicum.shareit.util;

public class IdGenerator {
    private static Long itemId = 1L;
    private static Long userId = 1L;

    public static Long generateId(IdType type) {
        if (IdType.ITEM.equals(type))
            return itemId++;
        else if (IdType.USER.equals(type)) {
            return userId++;
        } else
            return -1L;
    }
}

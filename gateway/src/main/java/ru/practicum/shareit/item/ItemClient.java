package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    ResponseEntity<Object> add(Long userId, ItemDto itemDto) {
        return post("", userId, itemDto);
    }

    ResponseEntity<Object> edit(Long userId, Long itemId, ItemUpdateDto itemDto) {
        return patch("/" + itemId, userId, itemDto);
    }

    ResponseEntity<Object> getById(Long itemId, Long userId) {
        return get("/" + itemId, userId);
    }

    ResponseEntity<Object> getByOwnerId(Long userId, Integer from, Integer size) {
        return get("?from=" + from + "&size=" + size, userId);
    }

    ResponseEntity<Object> search(String text, Integer from, Integer size) {
        String path = "/search?text=" + text + "&from=" + from + "&size=" + size;
        return get(path);
    }
}

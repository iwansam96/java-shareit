package ru.practicum.shareit.request;

import ru.practicum.shareit.client.BaseClient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.request.dto.ItemRequestDtoInput;

@Service
public class RequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";

    public RequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> addRequest(Long requestorId, ItemRequestDtoInput itemRequestDtoInput) {
        return post("", requestorId, itemRequestDtoInput);
    }

    public ResponseEntity<Object> getByRequestorId(Long requestorId) {
        return get("", requestorId);
    }

    public ResponseEntity<Object> getAllByPages(Long userId, Integer from, Integer size) {
        return get("/all?from=" + from + "&size=" + size, userId);
    }

    public ResponseEntity<Object> getByRequestId(Long userId, Long requestId) {
        return get("/" + requestId, userId);
    }
}

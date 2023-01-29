package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("select it " +
            "from Item as it " +
            "where it.owner.id = ?1")
    List<Item> findByOwnerId(Pageable pageable, Long userId);

    @Query("select it from Item as it " +
            "where lower(it.description) like lower(concat('%', ?1, '%'))" +
            "and it.available = true")
    List<Item> search(Pageable pageable, String text);

    List<Item> getItemsByRequest(ItemRequest request);
}

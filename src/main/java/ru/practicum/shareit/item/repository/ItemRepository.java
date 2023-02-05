package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findItemsByOwner_Id(Pageable pageable, Long userId);

    List<Item> findItemsByDescriptionContainingIgnoreCaseAndAvailableIsTrue(Pageable pageable, String text);

    List<Item> getItemsByRequest(ItemRequest request);
}

package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    //    get for getByUserId
    @Query("select b from Booking b where b.booker.id = ?1 and b.status in ?2 order by b.start desc")
    List<Booking> getByUserIdAndStatus(Long id, List<BookingStatus> state);

    //    get CURRENT for getByUserId
    @Query("select b from Booking b where b.booker.id = ?1 and b.status in ?2 " +
            "and current_timestamp > b.start and current_timestamp < b.end order by b.start desc")
    List<Booking> getCurrentByUserIdAndStatus(Long id, List<BookingStatus> state);

    //    get PAST for getByUserId
    @Query("select b from Booking b where b.booker.id = ?1 and b.status in ?2 and b.end < current_timestamp " +
            "order by b.start desc")
    List<Booking> getPastByUserIdAndStatus(Long id, List<BookingStatus> state);


    //    get for getByItemsByUserId
    @Query("select b from Booking b where b.item.owner.id = ?1 and b.status in ?2 order by b.start desc")
    List<Booking> getByOwnerIdAndStatus(Long userId, List<BookingStatus> states);

    //    get CURRENT for getByItemsByUserId
    @Query("select b from Booking b where b.item.owner.id = ?1 and b.status in ?2 " +
            "and current_timestamp > b.start and current_timestamp < b.end order by b.start desc")
    List<Booking> getCurrentByOwnerIdAndStatus(Long userId, List<BookingStatus> states);

    //    get PAST for getByItemsByUserId
    @Query("select b from Booking b where b.item.owner.id = ?1 and b.status in ?2 and b.end < current_timestamp " +
            "order by b.start desc")
    List<Booking> getPastByOwnerIdAndStatus(Long userId, List<BookingStatus> states);

    Set<Booking> getBookingsByItem_Id(Long itemId);

    List<Booking> getBookingsByBooker_IdAndEndBefore(Long userId, LocalDateTime before);
}

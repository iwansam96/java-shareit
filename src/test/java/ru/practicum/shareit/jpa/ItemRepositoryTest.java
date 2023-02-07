package ru.practicum.shareit.jpa;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import org.junit.jupiter.api.Assertions;

import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@Sql("/item_repository_test.sql")
public class ItemRepositoryTest {
    @Autowired
    private TestEntityManager em;

    @Autowired
    private BookingRepository bookingRepository;

    @Test
    @DisplayName("Test getCurrentByUserIdAndStatus method")
    public void getCurrentByUserIdAndStatus() {
        List<BookingStatus> statuses = new ArrayList<>();
        statuses.add(BookingStatus.APPROVED);
        List<Booking> fromGet = bookingRepository.getCurrentByUserIdAndStatus(Pageable.ofSize(10), 2L, statuses);

        TypedQuery<Booking> query = em.getEntityManager()
                .createQuery("select b from Booking b where b.booker.id = 2 and b.status in :statuses " +
                                "and  current_timestamp > b.start and current_timestamp < b.end order by b.start desc",
                        Booking.class);
        List<Booking> fromDb = query.setParameter("statuses", statuses).getResultList();

        Assertions.assertEquals(fromGet, fromDb);
    }

    @Test
    @DisplayName("Test getPastByUserIdAndStatus method")
    public void getPastByUserIdAndStatus() {
        List<BookingStatus> statuses = new ArrayList<>();
        statuses.add(BookingStatus.CANCELLED);
        List<Booking> fromGet = bookingRepository.getPastByUserIdAndStatus(Pageable.ofSize(10), 2L, statuses);

        TypedQuery<Booking> query = em.getEntityManager()
                .createQuery("select b from Booking b where b.booker.id = 2 and b.status in :statuses " +
                                "and b.end < current_timestamp order by b.start desc",
                        Booking.class);
        List<Booking> fromDb = query.setParameter("statuses", statuses).getResultList();

        Assertions.assertEquals(fromGet, fromDb);
    }

    @Test
    @DisplayName("Test getCurrentByOwnerIdAndStatus method")
    public void getCurrentByOwnerIdAndStatus() {
        List<BookingStatus> statuses = new ArrayList<>();
        statuses.add(BookingStatus.APPROVED);
        List<Booking> fromGet = bookingRepository.getCurrentByOwnerIdAndStatus(Pageable.ofSize(10), 1L, statuses);

        TypedQuery<Booking> query = em.getEntityManager()
                .createQuery("select b from Booking b where b.item.owner.id = 1 and b.status in :statuses " +
                                "and current_timestamp > b.start and current_timestamp < b.end order by b.start desc",
                        Booking.class);
        List<Booking> fromDb = query.setParameter("statuses", statuses).getResultList();

        Assertions.assertEquals(fromGet, fromDb);
    }

    @Test
    @DisplayName("Test getPastByOwnerIdAndStatus method")
    public void getPastByOwnerIdAndStatus() {
        List<BookingStatus> statuses = new ArrayList<>();
        statuses.add(BookingStatus.CANCELLED);
        List<Booking> fromGet = bookingRepository.getPastByOwnerIdAndStatus(Pageable.ofSize(10), 1L, statuses);

        TypedQuery<Booking> query = em.getEntityManager()
                .createQuery("select b from Booking b where b.item.owner.id = 1 and b.status in :statuses " +
                                "and b.end < current_timestamp order by b.start desc",
                        Booking.class);
        List<Booking> fromDb = query.setParameter("statuses", statuses).getResultList();

        Assertions.assertEquals(fromGet, fromDb);
    }
}

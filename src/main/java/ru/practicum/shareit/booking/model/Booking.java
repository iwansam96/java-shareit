package ru.practicum.shareit.booking.model;

import lombok.*;
import org.hibernate.Hibernate;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "bookings", schema = "public")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Long id;

    @Column(name = "booking_start", nullable = false)
    private LocalDateTime start;

    @Column(name = "booking_end", nullable = false)
    private LocalDateTime end;

    @ManyToOne
    @JoinColumn(name = "item", referencedColumnName = "item_id", nullable = false)
    private Item item;

    @ManyToOne
    @JoinColumn(name = "booker", referencedColumnName = "user_id", nullable = false)
    private User booker;

    @Enumerated(EnumType.STRING)
    @Column(name = "booking_status", nullable = false)
    private BookingStatus status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Booking booking = (Booking) o;
        return id != null && Objects.equals(id, booking.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

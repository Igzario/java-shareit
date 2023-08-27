package ru.practicum.shareit.booking;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ru.practicum.shareit.booking.enums.Status;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Entity
@Table(name = "bookings", schema = "public")
@Data
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "start_date")
    @NonNull
    private LocalDateTime startDate;
    @Column(name = "end_date")
    @NonNull
    private LocalDateTime endDate;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(name = "booker_id")
    private Long bookerId;
    @Column(name = "item_id")
    private Long itemId;
}
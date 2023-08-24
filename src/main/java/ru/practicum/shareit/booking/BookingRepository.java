package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.enums.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findBookingsByBookerIdOrderByStartDateDesc(Long bookerId);

    List<Booking> findBookingsByBookerIdAndStartDateAfterOrderByStartDateDesc(Long bookerId, LocalDateTime start);

    List<Booking> findBookingsByBookerIdAndStatus(Long bookerId, Status status);

    Page<Booking> findBookingsByBookerIdOrderByStartDateDesc(Long bookerId, Pageable pageable);

    List<Booking> findBookingsByBookerIdAndStartDateBeforeAndEndDateAfter(Long userId, LocalDateTime start, LocalDateTime end);

    List<Booking> findBookingsByBookerIdAndEndDateBeforeOrderByStartDateDesc(Long userId, LocalDateTime end);

    @Query(value = "select b.id, b.startDate from Booking b join Item i on i.id=b.itemId where i.owner = ?1 order by b.startDate desc")
    List<Long> getAllBookingItemsForUser(Long owner);

    @Query(value = "select b.id, b.startDate from Booking b join Item i on i.id=b.itemId where i.owner = ?1 and b.startDate>?2 order by b.startDate desc")
    List<Long> getAllBookingItemsForUserFuture(Long owner, LocalDateTime startDate);

    @Query(value = "select b.id, b.startDate from Booking b join Item i on i.id=b.itemId where i.owner = ?1 and b.status=?2 order by b.startDate desc")
    List<Long> getAllBookingItemsForUserStatus(Long owner, Status status);

    @Query(value = "select b.id from Booking b join Item i on i.id=b.itemId where i.owner = ?1 order by b.startDate desc")
    Page<Long> getAllBookingItemsForUserId(Long owner, Pageable pageable);

    @Query(value = "select b.id from Booking b where b.bookerId=?1 and b.itemId=?2 and b.endDate<?3 and b.status=?4 order by b.id")
    List<Long> findBookingsToAddComment(Long bookerId, Long itemId, LocalDateTime localDateTime, Status status);

    @Query(value = "select b.id from Booking b join Item i on i.id=b.itemId where i.owner=?1 and b.startDate<?2 and b.endDate>?2 order by b.id")
    List<Long> findBookingsCurrent(Long userId, LocalDateTime localDateTimeNow);

    @Query(value = "select b.id, b.startDate from Booking b join Item i on i.id=b.itemId where i.owner=?1 and b.endDate<?2 order by b.startDate DESC ")
    List<Long> findBookingsPast(Long userId, LocalDateTime localDateTimeNow);

    List<Booking> findBookingByItemIdAndStatusIsNotAndEndDateAfterOrderByStartDate(Long itemId, Status status, LocalDateTime localDateTimeNow);

    List<Booking> findBookingByItemId(Long itemId);
}
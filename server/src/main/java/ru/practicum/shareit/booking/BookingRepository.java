package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findBookingsByBookerIdOrderByStartDateDesc(Long bookerId);

    List<Booking> findBookingsByBookerIdAndStartDateAfterOrderByStartDateDesc(Long bookerId, LocalDateTime start);

    List<Booking> findBookingsByBookerIdAndStatus(Long bookerId, String status);

    Page<Booking> findBookingsByBookerIdOrderByStartDateDesc(Long bookerId, Pageable pageable);

    List<Booking> findBookingsByBookerIdAndStartDateBeforeAndEndDateAfter(Long userId, LocalDateTime start, LocalDateTime end);

    List<Booking> findBookingsByBookerIdAndEndDateBeforeOrderByStartDateDesc(Long userId, LocalDateTime end);

    @Query(value = "select b.id, b.startDate from Booking b join Item i on i.id=b.itemId where i.owner = :owner order by b.startDate desc")
    List<Long> getAllBookingItemsForUser(@Param("owner") Long owner);

    @Query(value = "select b.id, b.startDate from Booking b join Item i on i.id=b.itemId where i.owner = :owner and b.startDate>:startDate order by b.startDate desc")
    List<Long> getAllBookingItemsForUserFuture(@Param("owner") Long owner, @Param("startDate") LocalDateTime startDate);

    @Query(value = "select b.id, b.startDate from Booking b join Item i on i.id=b.itemId where i.owner = :owner and b.status=:status order by b.startDate desc")
    List<Long> getAllBookingItemsForUserStatus(@Param("owner") Long owner, @Param("status") String status);

    @Query(value = "select b.id from Booking b join Item i on i.id=b.itemId where i.owner = :owner order by b.startDate desc")
    Page<Long> getAllBookingItemsForUserId(@Param("owner") Long owner, Pageable pageable);

    @Query(value = "select b.id from Booking b where b.bookerId=:bookerId and b.itemId=:itemId and b.endDate<:localDateTime and b.status=:status order by b.id")
    List<Long> findBookingsToAddComment(@Param("bookerId") Long bookerId, @Param("itemId") Long itemId,
                                        @Param("localDateTime") LocalDateTime localDateTime, @Param("status") String status);

    @Query(value = "select b.id from Booking b join Item i on i.id=b.itemId where i.owner=:userId and b.startDate<:localDateTime and b.endDate>:localDateTime order by b.id")
    List<Long> findBookingsCurrent(@Param("userId") Long userId, @Param("localDateTime") LocalDateTime localDateTimeNow);

    @Query(value = "select b.id, b.startDate from Booking b join Item i on i.id=b.itemId where i.owner=:userId and b.endDate<:localDateTime order by b.startDate DESC ")
    List<Long> findBookingsPast(@Param("userId") Long userId, @Param("localDateTime") LocalDateTime localDateTimeNow);

    List<Booking> findBookingByItemIdAndStatusIsNotAndEndDateAfterOrderByStartDate(@Param("itemId") Long itemId, @Param("status") String status, @Param("now") LocalDateTime now);

    List<Booking> findBookingByItemId(Long itemId);
}
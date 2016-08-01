package com.hmlee.chatchat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hmlee.chatchat.model.domain.Statistic;

import java.util.List;

/**
 * Created by hmlee
 */
@Repository
public interface StatisticRepository extends JpaRepository<Statistic, Long> {

    @Query("SELECT COUNT(s) FROM Statistic s WHERE s.receiverPhoneNumber = :receiverPhoneNumber")
    public int unreadCountByReceiverPhoneNumber(@Param("receiverPhoneNumber") String receiverPhoneNumber);

    @Query("SELECT s FROM Statistic s WHERE s.idx = :msgId AND s.receiverPhoneNumber = :receiverPhoneNumber AND s.isRead = :isRead")
    public Statistic findMessageWithReadStatus(@Param("msgId") Long msgId, @Param("receiverPhoneNumber") String receiverPhoneNumber, @Param("isRead") boolean isRead);

    @Query("SELECT s FROM Statistic s WHERE s.receiverPhoneNumber = :receiverPhoneNumber AND s.isRead = false")
    public List<Statistic> findUnreadMessage(@Param("receiverPhoneNumber") String receiverPhoneNumber);
}

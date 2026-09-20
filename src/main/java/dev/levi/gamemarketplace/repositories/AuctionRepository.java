package dev.levi.gamemarketplace.repositories;

import dev.levi.gamemarketplace.entities.Auction;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface AuctionRepository extends JpaRepository<Auction, UUID> {

    @Query("""
        SELECT a FROM Auction a
        JOIN FETCH a.item
        WHERE a.status = 'ACTIVE' AND a.endsAt > :now
        """)
    Page<Auction> findNonExpiredActiveAuctions(@Param("now") Instant now, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Auction a WHERE a.id = :id")
    Optional<Auction> findByIdWithLock(UUID id);

    @Query("""
        SELECT a FROM Auction a
        JOIN FETCH a.item
        JOIN FETCH a.seller
        WHERE a.id = :id
    """)
    Optional<Auction> findByIdWithDetails(@Param("id") UUID id);

}
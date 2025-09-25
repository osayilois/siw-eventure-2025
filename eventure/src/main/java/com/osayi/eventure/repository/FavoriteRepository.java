// src/main/java/com/osayi/eventure/repository/FavoriteRepository.java
package com.osayi.eventure.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.osayi.eventure.model.Favorite;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUserIdOrderByCreatedAtDesc(Long userId);
    boolean existsByUserIdAndEventId(Long userId, Long eventId);
    void deleteByUserIdAndEventId(Long userId, Long eventId);
}

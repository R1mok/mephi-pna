package ru.b19513.pet_manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.b19513.pet_manager.repository.entity.FeedNote;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FeedNoteRepository extends JpaRepository<FeedNote, Long> {
    List<FeedNote> findByPetIdAndDateTimeIsAfterAndDateTimeBefore(long petId, LocalDateTime dateTime, LocalDateTime dateTime2);
    List<FeedNote> findByPetId(long petId);
    FeedNote findFirstByPetIdOrderByDateTimeDesc(long petId);
}

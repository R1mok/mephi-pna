package ru.b19513.pet_manager.service;

import ru.b19513.pet_manager.controller.entity.FeedNoteDTO;
import ru.b19513.pet_manager.controller.entity.PetDTO;
import ru.b19513.pet_manager.controller.entity.PetParametersDTO;
import ru.b19513.pet_manager.controller.entity.StatusDTO;
import ru.b19513.pet_manager.controller.entity.enums.Gender;
import ru.b19513.pet_manager.controller.entity.enums.PetType;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;

public interface PetService {
    PetDTO createPet(long groupId, String name, String description, Gender gender, PetType petType, Date bornDate);

    PetDTO updatePet(PetDTO pet);

    Collection<PetDTO> getPets(long groupId);

    PetDTO getPet(long petId);

    StatusDTO deletePet(long petId);

    FeedNoteDTO createFeedNote(long petId, long userId, String comment);

    Collection<FeedNoteDTO> getFeedNotes(long petId);

    Collection<FeedNoteDTO> findFeedNotesByDate(long petId, LocalDateTime from, LocalDateTime to);

    PetDTO addNewParameter(long petId, Date time, double weight, double height);

    Collection<PetParametersDTO> getPetParameters(long petId);
}

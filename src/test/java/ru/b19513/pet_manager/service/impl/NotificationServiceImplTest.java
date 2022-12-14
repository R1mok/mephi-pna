package ru.b19513.pet_manager.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.b19513.pet_manager.controller.entity.NotificationScheduleDTO;
import ru.b19513.pet_manager.controller.entity.NotificationTimeoutDTO;
import ru.b19513.pet_manager.controller.entity.enums.Gender;
import ru.b19513.pet_manager.controller.entity.enums.PetType;
import ru.b19513.pet_manager.repository.*;
import ru.b19513.pet_manager.repository.entity.NotificationSchedule;
import ru.b19513.pet_manager.repository.entity.NotificationTimeout;
import ru.b19513.pet_manager.service.GroupService;
import ru.b19513.pet_manager.service.NotificationService;
import ru.b19513.pet_manager.service.PetService;
import ru.b19513.pet_manager.service.UserService;
import ru.b19513.pet_manager.service.mapper.NotificationMapper;

import javax.transaction.Transactional;

import java.time.Instant;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
class NotificationServiceImplTest {
    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    NotificationNoteRepository notificationNoteRepository;
    @Autowired
    PetRepository petRepository;
    @Autowired
    UserService userService;
    @Autowired
    PetService petService;
    @Autowired
    GroupService groupService;
    @Autowired
    NotificationService notificationService;
    @Autowired
    NotificationMapper notificationMapper;

    @Test
    @Transactional
    void createNotificationTimeout() {
        var userOwnerDTO = userService.signInNewUser("R1mok", "pass", "Anton");
        var groupDTO = groupService.createGroup(userRepository.getById(userOwnerDTO.getId()), "group");
        var petDTO = petService.createPet(groupDTO.getId(), "Barsik", "My lover", Gender.MALE, PetType.CAT, Date.from(Instant.MIN));
        var notifTimeoutDTO = notificationService.createNotificationTimeout(groupDTO.getId(),
                petDTO.getId(), "Barsik should to feed", 5);
        var notifInRepo = notificationMapper.entityToDTO((NotificationTimeout) notificationRepository.findAll().get(0));
        Assertions.assertEquals(notifInRepo.getId(), notifTimeoutDTO.getId());
        Assertions.assertEquals(notifInRepo.getGroupId(), notifTimeoutDTO.getGroupId());
        Assertions.assertEquals(notifInRepo.getComment(), notifTimeoutDTO.getComment());
        Assertions.assertEquals(notifInRepo.getPeriods(), notifTimeoutDTO.getPeriods());
        petRepository.deleteAll();
        notificationRepository.deleteAll();
        groupRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    void createNotificationSchedule() {
        var userOwnerDTO = userService.signInNewUser("R1mok", "pass", "Anton");
        var groupDTO = groupService.createGroup(userRepository.getById(userOwnerDTO.getId()), "group");
        var petDTO = petService.createPet(groupDTO.getId(), "Barsik", "My lover", Gender.MALE, PetType.CAT, Date.from(Instant.MIN));
        var notifScheduleDTO = notificationService.createNotificationSchedule(groupDTO.getId(),
                petDTO.getId(), "Barsik should to feed", List.of(LocalTime.of(12, 30)));
        var notifInRepo = notificationMapper.entityToDTO((NotificationSchedule) notificationRepository.findAll().get(0));
        Assertions.assertEquals(notifInRepo.getId(), notifScheduleDTO.getId());
        Assertions.assertEquals(notifInRepo.getGroupId(), notifScheduleDTO.getGroupId());
        Assertions.assertEquals(notifInRepo.getComment(), notifScheduleDTO.getComment());
        Assertions.assertEquals(notifInRepo.getTimes(), notifScheduleDTO.getTimes());
        petRepository.deleteAll();
        notificationRepository.deleteAll();
        groupRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    void updateNotificationSchedule() {
        var userOwnerDTO = userService.signInNewUser("R1mok", "pass", "Anton");
        var groupDTO = groupService.createGroup(userRepository.getById(userOwnerDTO.getId()), "group");
        var petDTO = petService.createPet(groupDTO.getId(), "Barsik", "My lover", Gender.MALE, PetType.CAT, Date.from(Instant.MIN));
        var notifScheduleDTO = notificationService.createNotificationSchedule(groupDTO.getId(),
                petDTO.getId(), "Barsik should to feeed", List.of(LocalTime.of(12, 30)));
        var notificationDTO = NotificationScheduleDTO.builder()
                .id(notifScheduleDTO.getId())
                .comment("Barsik should to feed")
                .build();
        var notifFromRepo = notificationRepository.findById(notificationDTO.getId()).get();
        notifFromRepo.setComment("Barsik should to feed");
        notificationService.updateNotificationSchedule(notificationDTO);
        var updatedNotif = notificationRepository.findById(notificationDTO.getId()).get();
        Assertions.assertEquals(notifFromRepo, updatedNotif);
        notificationRepository.deleteAll();
        petRepository.deleteAll();
        groupRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    void updateNotificationTimeout() {
        var userOwnerDTO = userService.signInNewUser("R1mok", "pass", "Anton");
        var groupDTO = groupService.createGroup(userRepository.getById(userOwnerDTO.getId()), "group");
        var petDTO = petService.createPet(groupDTO.getId(), "Barsik", "My lover", Gender.MALE, PetType.CAT, Date.from(Instant.MIN));
        var notifTimeoutDTO = notificationService.createNotificationTimeout(groupDTO.getId(),
                petDTO.getId(), "Barsik should to feeed", 1);
        var notificationDTO = NotificationTimeoutDTO.builder()
                .id(notifTimeoutDTO.getId())
                .comment("Barsik should to feed")
                .build();
        var notifFromRepo = notificationRepository.findById(notificationDTO.getId()).get();
        notifFromRepo.setComment("Barsik should to feed");
        notificationService.updateNotificationTimeout(notificationDTO);
        var updatedNotif = notificationRepository.findById(notificationDTO.getId()).get();
        Assertions.assertEquals(notifFromRepo, updatedNotif);
        notificationRepository.deleteAll();
        petRepository.deleteAll();
        groupRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    void showNotification() throws InterruptedException {
        var userDTO = userService.signInNewUser("R1mok", "pass", "Anton");
        var groupDTO = groupService.createGroup(userRepository.getById(userDTO.getId()), "group");
        var group = groupRepository.findAll().get(0);
        var petDTO = petService.createPet(groupDTO.getId(), "Barsik", "My lover", Gender.MALE, PetType.CAT, Date.from(Instant.MIN));
        var fnDTO = petService.createFeedNote(petDTO.getId(), userDTO.getId(), "Feed Barsik");
        notificationService.createNotificationTimeout(groupDTO.getId(), petDTO.getId(), "Feed Barsik", 1);
        Thread.sleep(1000);
        var notifActualDTO = notificationService.showNotification(userRepository.findByLogin(userDTO.getLogin()).get()).get(0);
        var notifExpectedDTO = notificationMapper.entityToDTO(notificationRepository
                .findAll().stream().filter(e -> e.getGroup().equals(group)).collect(Collectors.toSet())).get(0);
        Assertions.assertEquals(notifExpectedDTO.getComment(), notifActualDTO.getComment());
        Assertions.assertEquals(notifExpectedDTO.getId(), notifActualDTO.getId());
        Assertions.assertEquals(notifExpectedDTO.getGroupId(), notifActualDTO.getGroupId());
        notificationRepository.deleteAll();
        petRepository.deleteAll();
        groupRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    void deleteNotification() {
        var userDTO = userService.signInNewUser("R1mok", "pass", "Anton");
        var groupDTO = groupService.createGroup(userRepository.getById(userDTO.getId()), "group");
        var group = groupRepository.findAll().get(0);
        var petDTO = petService.createPet(groupDTO.getId(), "Barsik", "My lover", Gender.MALE, PetType.CAT, Date.from(Instant.MIN));
        var fnDTO = petService.createFeedNote(petDTO.getId(), userDTO.getId(), "Feed Barsik");
        var notifDTO = notificationService.createNotificationTimeout(groupDTO.getId(), petDTO.getId(), "Feed Barsik", 1);
        notificationService.deleteNotification(notifDTO.getId());
        var user = userRepository.findByLogin(userDTO.getLogin()).get();
        var countOfNotification = notificationService.showNotification(user).size();
        Assertions.assertEquals(0, countOfNotification);
        notificationRepository.deleteAll();
        petRepository.deleteAll();
        groupRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    void setTimeInNotificationNote() {
        var userDTO = userService.signInNewUser("R1mok", "pass", "Anton");
        var groupDTO = groupService.createGroup(userRepository.getById(userDTO.getId()), "group");
        var group = groupRepository.findAll().get(0);
        var petDTO = petService.createPet(groupDTO.getId(), "Barsik", "My lover", Gender.MALE, PetType.CAT, Date.from(Instant.MIN));
        var notifDTO = notificationService.createNotificationTimeout(groupDTO.getId(), petDTO.getId(), "Feed Barsik", 1);
        notificationService.setTimeInNotificationNote(userRepository.getById(userDTO.getId()), List.of(notifDTO.getId()));
        var notifNote = notificationNoteRepository.findAll().get(0);
        Assertions.assertEquals(notificationRepository.findAll().get(0), notifNote.getNotification());
        Assertions.assertEquals(userRepository.getById(userDTO.getId()), notifNote.getUser());
    }
}
package ru.b19513.pet_manager.repository.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "t_notification_schedule")
public class NotificationSchedule extends Notification {

    @Builder
    public NotificationSchedule(Group group, Pet pet, String comment, List<ScheduleTime> times, boolean enabled){
        super(group, pet, comment, enabled);
        this.times = times;
    }

    @OneToMany(mappedBy = "notification")
    private List<ScheduleTime> times;
}
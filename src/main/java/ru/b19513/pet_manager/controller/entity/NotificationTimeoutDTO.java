package ru.b19513.pet_manager.controller.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.b19513.pet_manager.repository.entity.Period;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@Schema(description = "Уведомление типа Timeout")
public class NotificationTimeoutDTO extends NotificationDTO{

    @Builder
    public NotificationTimeoutDTO(long id, long groupId, boolean enabled, String comment, String groupName, String petName,
            List<Period> periods, long elapsed) {
        super(id, groupId, enabled, comment, groupName, petName);
        this.elapsed = elapsed;
        this.periods = periods;
    }

    @Schema(description = "Время, через которое нужно послать уведомление (в секундах)")
    private long elapsed;
    @Schema(description = "Время отсчета уведомления")
    private LocalDateTime time;
    @Schema(description = "Список периодов, когда не нужно посылать уведомления")
    private List<Period> periods;
}

package disaster.service.notification;


import disaster.model.calendar.CalendarSearchDto;
import disaster.model.notification.DisasterNotification;
import reactor.core.publisher.Flux;

public interface NotificationService {

    Flux<DisasterNotification> getNotificationsBySearchDto(CalendarSearchDto searchDto);
}
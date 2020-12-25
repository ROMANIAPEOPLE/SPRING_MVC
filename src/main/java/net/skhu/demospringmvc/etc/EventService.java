//package net.skhu.demospringmvc;
//
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Service
//public class EventService {
//
//    public List<Event> getEvents() {
//        Event event = Event.builder()
//                .name("스프링 웹 MVC 스터티")
//                .limitOfEnrollment(5)
//                .startDateTime(LocalDateTime.of(2020,12,20,10,0))
//                .endDateTime(LocalDateTime.of(2021,01,01,10,0))
//                .build();
//
//
//        Event event2 = Event.builder()
//                .name("스프링 웹 MVC 스터티2")
//                .limitOfEnrollment(5)
//                .startDateTime(LocalDateTime.of(2020,12,20,10,0))
//                .endDateTime(LocalDateTime.of(2021,01,01,10,0))
//                .build();
//
//
//        return List.of(event,event2);
//
//    }
//
//}

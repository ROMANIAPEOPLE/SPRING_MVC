//package net.skhu.demospringmvc;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//
//@Controller
//@RequiredArgsConstructor
//public class EventController {
//
//    private EventService eventService;
//
//
//    @GetMapping("/events")
//    public String events(Model model){
//        model.addAttribute("events",eventService.getEvents());
//        return "events";
//    }
//}

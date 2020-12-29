package net.skhu.demospringmvc.handlerMethod;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@SessionAttributes("event")
public class SampleController3 {

    @GetMapping("/event/{id}")
    @ResponseBody
    public Event getEvent(@PathVariable Integer id) {
        Event event = new Event();
        event.setId(id);
        return event;

    }

    @GetMapping("/event/{id}")
    @ResponseBody
    public Event getEvent2(@PathVariable Integer id, @MatrixVariable String name) {
        Event event = new Event();
        event.setId(id);
        event.setName(name);
        return event;
    }

    @PostMapping("/events")
    @ResponseBody
    public Event getEvent(@RequestParam String name) {
        Event event = new Event();
        event.setName(name);
        return event;
    }

    @PostMapping("/events2/{name}")
    @ResponseBody
    public Event getEvent(@ModelAttribute Event event, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            System.out.println("===============");
            bindingResult.getAllErrors().forEach(c-> System.out.println(c.toString()));
        }
        return event;
    }

    @GetMapping("/events/form")
    public String eventsForm(Model model) {
        Event newEvent = new Event();
        newEvent.setLimit(50);
        model.addAttribute("event",newEvent);
        //httpSession.setAttribute("event", newEvent);
        return "/events/form";
    }
    @GetMapping("events/list")
    public String getEvents(Model model, @SessionAttribute LocalDateTime visitTime) {
        // visitTime 출력
        System.out.println(visitTime);

        Event event = new Event();
        event.setName("spring");
        event.setLimit(10);

        List<Event> eventList = new ArrayList<>();
        eventList.add(event);

        model.addAttribute(eventList);

        return "/events/list";
    }

    @PostMapping
    @ResponseBody
    public String eventsFormLimitSubmit(@ModelAttribute Event event,
                                        BindingResult bindingResult,
                                        SessionStatus sessionStatus,
                                        RedirectAttributes attributes) {

        if(bindingResult.hasErrors()) {
            return "/event/form-limit";
        }
        sessionStatus.setComplete();
        //RedirectAttributes에 원하는 값만 추가하기
        attributes.addAttribute("name", event.getName());

        return "/event/form-list";
    }

}

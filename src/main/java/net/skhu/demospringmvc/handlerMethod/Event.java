package net.skhu.demospringmvc.handlerMethod;//package net.skhu.demospringmvc;
import lombok.*;
import org.springframework.web.bind.annotation.InitBinder;

import java.time.LocalDateTime;

@Getter @Setter
@Builder @NoArgsConstructor
@AllArgsConstructor
public class Event {

    private Integer id;

    private String name;

    private Integer limit;

    private int limitOfEnrollment;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;


}

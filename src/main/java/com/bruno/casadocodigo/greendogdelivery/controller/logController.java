package com.bruno.casadocodigo.greendogdelivery.controller;

import com.bruno.casadocodigo.greendogdelivery.entity.Cliente;
import com.bruno.casadocodigo.greendogdelivery.entity.Log;
import com.bruno.casadocodigo.greendogdelivery.repository.LogRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import static com.bruno.casadocodigo.greendogdelivery.entity.Log.SORT_BY_LOG_DATA_AT_ASC;

@Controller
@RequestMapping("/logs")
public class logController {

    private final LogRepository logRepository;
    private final String LOG_URI = "logs/";

    public logController(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @GetMapping("/")
    public ModelAndView list() {
        Iterable<Log> logs = this.logRepository.findAll(SORT_BY_LOG_DATA_AT_ASC);
        return new ModelAndView(LOG_URI + "list", "logs", logs);
    }

}

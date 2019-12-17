package com.konopka.calendar.controller;

import com.konopka.calendar.service.CalendarService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@RestController
public class CalendarController {

    private CalendarService calendarService;

    @Autowired
    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/calendar")
    public ResponseEntity<Resource> fetchCalendarFileInICalFormat(@RequestParam(required = true) int month, @RequestParam(required = true) int year) throws IOException {
        List<String> eventsInCalendar = fetchEventsFromSpecificCalendar(month, year);
        List<String> summariesForEventsInCalendar = fetchSummariesForEventsInCalendar(month, year);
        Resource fileSystemResource = new FileSystemResource(calendarService.createNewCalendarFile(eventsInCalendar, summariesForEventsInCalendar, month));

        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType("text/calendar"))
                .body(fileSystemResource);
    }

    private List<String> fetchSummariesForEventsInCalendar(int month, int year) throws IOException {
        List<String> summaries = new ArrayList<>();
        Document document = Jsoup.connect("http://www.weeia.p.lodz.pl/pliki_strony_kontroler/kalendarz.php?rok=" + year + "&miesiac=" + month + "&lang=1").get();
        Elements elements = document.select("div.InnerBox");

        for (Element element : elements) {
            summaries.add(element.text());
        }

        return summaries;
    }

    private List<String> fetchEventsFromSpecificCalendar(int month, int year) throws IOException {
        Document document = Jsoup.connect("http://www.weeia.p.lodz.pl/pliki_strony_kontroler/kalendarz.php?rok=" + year + "&miesiac=" + month + "&lang=1").get();
        Elements elements = document.select("a.active");
        List<String> events = new ArrayList<>();

        for (Element element : elements) {
            events.add(element.text());
        }

        return events;
    }
}


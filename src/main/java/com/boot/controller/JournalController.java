package com.boot.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.boot.entity.Journal;

@RestController
public class JournalController
{

    private static List<Journal> entries = new ArrayList<Journal>();
    static
    {
        try
        {
            entries.add(new Journal("Get to know Spring Boot", "Today I will learn Spring Boot", "01-02-2018"));
            entries.add(new Journal("Simple Spring Boot Project", "I will do my first Spring Boot Project", "17-01-2018"));
            entries.add(new Journal("Spring Boot Reading", "Read more about Spring Boot", "20-02-2019"));
            entries.add(new Journal("Spring Boot in Cloud Foundry", "Spring Boot using Cloud Foundry", "03-01-2019"));
            entries.add(new Journal("Spring Boot in Kubernetes", "Spring Boot using Kubernetes", "03-01-2019"));
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
    }

    @RequestMapping("/journal/all")
    public List<Journal> getAll() throws ParseException
    {
        return entries;
    }

    @RequestMapping("/journal/findBy/title/{title}")
    public List<Journal> findByTitleContains(@PathVariable String title) throws ParseException
    {
        return entries.stream()
                .filter(entry -> entry.getTitle()
                        .toLowerCase()
                        .contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/journal", method = RequestMethod.POST)
    public Journal add(@RequestBody Journal entry)
    {
        entries.add(entry);
        return entry;
    }
}

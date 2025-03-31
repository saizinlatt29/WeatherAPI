package com.example.controller;

import com.example.domain.Weather;
import com.example.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/weather")
public class WeatherController {
    @Autowired
    WeatherService weatherService;

    @PostMapping
    public ResponseEntity<?> saveWeather(@RequestBody Weather weather){
        return new ResponseEntity<Weather>(weatherService.save(weather), HttpStatus.BAD_REQUEST);
    }

    @GetMapping
    public ResponseEntity<?> getWeather(@RequestParam(required = false) String date ,@RequestParam(required = false) String city,
                                        @RequestParam(required = false) String sort){
        if(date==null && city ==null && sort ==null){
            return new ResponseEntity<List<Weather>>(weatherService.findAll(),HttpStatusCode.valueOf(200));
        } else if (date!=null && city ==null && sort ==null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dt = LocalDate.parse(date,formatter);
            List<Weather> weathers = weatherService.findByDate(dt);
            if(weathers==null){
                return  new ResponseEntity<String>("not found",HttpStatus.NOT_FOUND);
            }else {
                return  new ResponseEntity<List<Weather>>(weathers,HttpStatusCode.valueOf(200));
            }
        } else if (date==null && city !=null && sort ==null) {
            List<String> cities = new ArrayList<>(Arrays.asList(city.split(",")));
            List<Weather> weathers = weatherService.findByCityInIgnoreCase(cities);
            if(weathers==null){
                return  new ResponseEntity<String>("not found",HttpStatus.NOT_FOUND);
            }else {
                return  new ResponseEntity<List<Weather>>(weathers,HttpStatusCode.valueOf(200));
            }
        } else if (date==null && city ==null && sort !=null) {
            if(sort.equals("date")){
                List<Weather> weathers = weatherService.findAll();
                weathers.sort((a,b)-> a.getDate().compareTo(b.getDate()));
                return  new ResponseEntity<List<Weather>>(weathers,HttpStatusCode.valueOf(200));
            } else if (sort.equals("-date")) {
                List<Weather> weathers = weatherService.findAll();
                weathers.sort((a,b)-> b.getDate().compareTo(a.getDate()));
                return  new ResponseEntity<List<Weather>>(weathers,HttpStatusCode.valueOf(200));
            }else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id){
        Weather weather = weatherService.findById(id);
        if(weather!=null){
            return new ResponseEntity<Weather>(weather,HttpStatus.FOUND);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}

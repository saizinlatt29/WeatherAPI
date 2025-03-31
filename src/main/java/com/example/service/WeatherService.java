package com.example.service;

import com.example.domain.Weather;
import com.example.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class WeatherService {
    @Autowired
    WeatherRepository repository;

    public Weather save(Weather weather){
        return repository.save(weather);
    }

    public Weather findById(Integer id){
        return repository.findById(id).orElse(null);
    }

    public List<Weather> findAll(){
        return repository.findAll();
    }

    public List<Weather> findByDate(LocalDate date){
        return repository.findByDate(date);
    }

   public List<Weather> findByCityInIgnoreCase(List<String> cities){
        return repository.findByCityInIgnoreCase(cities);
   }

}

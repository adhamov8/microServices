package com.example.orderservice.service;

import com.example.orderservice.model.Station;
import com.example.orderservice.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StationService {

    private final StationRepository stationRepository;

    @Autowired
    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public List<Station> getAllStations() {
        return stationRepository.findAll();
    }

    public Station getStationById(Long id) {
        return stationRepository.findById(id).orElse(null);
    }
}

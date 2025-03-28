package com.example.orderservice.service;

import com.example.orderservice.model.Station;
import com.example.orderservice.repository.StationRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class StationServiceTest {

    @Test
    void testGetAllStations() {
        StationRepository stationRepository = Mockito.mock(StationRepository.class);
        StationService stationService = new StationService(stationRepository);

        Station s1 = new Station(); s1.setId(1L); s1.setStation("Station A");
        Station s2 = new Station(); s2.setId(2L); s2.setStation("Station B");
        Mockito.when(stationRepository.findAll()).thenReturn(List.of(s1, s2));

        List<Station> stations = stationService.getAllStations();
        assertThat(stations).hasSize(2);
    }

    @Test
    void testGetStationById() {
        StationRepository stationRepository = Mockito.mock(StationRepository.class);
        StationService stationService = new StationService(stationRepository);

        Station s = new Station();
        s.setId(10L);
        s.setStation("Station X");

        Mockito.when(stationRepository.findById(10L)).thenReturn(java.util.Optional.of(s));

        Station found = stationService.getStationById(10L);
        assertThat(found).isNotNull();
        assertThat(found.getStation()).isEqualTo("Station X");
    }
}

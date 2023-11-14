package kopo.poly.service;


import kopo.poly.dto.EventDTO;

public interface IApiService {


    String getEvent() throws Exception;
    String getWeather(String x, String y) throws Exception;

}


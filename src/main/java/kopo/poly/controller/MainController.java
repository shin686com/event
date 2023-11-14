package kopo.poly.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kopo.poly.dto.EventDTO;
import kopo.poly.dto.weatherDTO;
import kopo.poly.service.IApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Controller
public class MainController {
    private final IApiService ApiService;

    @GetMapping(value = "index")
    public String index() {
        log.info(this.getClass().getName() + ".index Start!");

        log.info(this.getClass().getName() + ".index End!");

        return "index";
    }

    @GetMapping(value = "test")
    public String test() throws Exception {
        log.info(this.getClass().getName() + ".test Start!");
        log.info(this.getClass().getName() + ".test End!");
        return "test";
    }

    @GetMapping(value = "success")
    public String success() throws Exception {
        log.info(this.getClass().getName() + ".success Start!");
        log.info(this.getClass().getName() + ".success End!");
        return "success";
    }

    @GetMapping(value = "api")
    @ResponseBody
    public List<EventDTO> api() throws Exception {
        log.info(this.getClass().getName() + ".api Start!");
        String result = ApiService.getEvent();
        log.info("데이터: " + result);
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;
        List<EventDTO> rList = new ArrayList<EventDTO>();
        JSONArray parse_itemlist = null;
        try {
            jsonObject = (JSONObject) jsonParser.parse(result);
            JSONObject parse_response = (JSONObject) jsonObject.get("response");
            JSONObject parse_body = (JSONObject) parse_response.get("body");
            JSONObject parse_items = (JSONObject) parse_body.get("items");
            parse_itemlist = (JSONArray) parse_items.get("item");

            for (int i = 0; i < parse_itemlist.size(); i++) {
                JSONObject event = (JSONObject) parse_itemlist.get(i);
                EventDTO temp = new EventDTO();
                temp.setTitle((String) event.get("title"));
                temp.setFirstimage((String) event.get("firstimage"));
                temp.setEventstartdate((String) event.get("eventstartdate"));
                temp.setEventenddate((String) event.get("eventenddate"));
                temp.setAddr((String) event.get("addr1"));
//                log.info("타이틀 : "+(String)event.get("title"));
//                log.info("주소 : "+(String)event.get("addr1"));
//                log.info("이미지 : "+(String) event.get("firstimage"));
//                log.info("시작일 : "+(String) event.get("eventstartdate"));
//                log.info("종료일 : "+(String) event.get("eventenddate"));
                rList.add(temp);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info(this.getClass().getName() + ".api End!");
        return rList;
    }

    @GetMapping("/weather")
    @ResponseBody
    public List<weatherDTO> restApiGetWeather(@RequestParam("x")String x,@RequestParam("y")String y) throws Exception {
        log.info(this.getClass().getName() + ".api Start!");
        String result = ApiService.getWeather(x,y);
        log.info("데이터: " + result);
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;
        List<weatherDTO> rList = new ArrayList<weatherDTO>();
        JSONArray parse_itemlist = null;
        try {
            jsonObject = (JSONObject) jsonParser.parse(result);
            JSONObject parse_response = (JSONObject) jsonObject.get("response");
            JSONObject parse_body = (JSONObject) parse_response.get("body");
            JSONObject parse_items = (JSONObject) parse_body.get("items");
            parse_itemlist = (JSONArray) parse_items.get("item");

            for (int i = 0; i < parse_itemlist.size(); i++) {
                JSONObject weather = (JSONObject) parse_itemlist.get(i);
                weatherDTO temp = new weatherDTO();
                temp.setCategory((String) weather.get("category"));
                temp.setFcstValue((String) weather.get("fcstValue"));
                log.info("카테고리 : " + (String) weather.get("category"));
                log.info("값 : " + (String) weather.get("fcstValue"));
                rList.add(temp);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info(this.getClass().getName() + ".api End!");
        return rList;
    }
}
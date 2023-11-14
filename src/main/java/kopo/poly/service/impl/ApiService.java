package kopo.poly.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import kopo.poly.dto.EventDTO;
import kopo.poly.service.IApiService;
import kopo.poly.util.CmmUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ApiService implements IApiService {

    @Value("${event.url}")
    private String evnetApi;

    @Value("${event.api.key}")
    private String apiKey; //발급받은 인증키

    @Override
    public String getEvent() throws Exception {

        log.info(this.getClass().getName() + "API Start!");
        StringBuffer result = new StringBuffer();
        HttpURLConnection conn =null;
        try {
            StringBuilder urlBuilder = new StringBuilder("https://apis.data.go.kr/B551011/KorService1/searchFestival1"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=vEGYRXx26Vq3iX5YSal2u9DGvg1usDpqaP2gY3lTiVDd4h5PslrUCKVs9vI7in15GK9XYMImjmMMmsLH%2B4KThw%3D%3D"); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지 번호*/
            urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("100", "UTF-8"));//가져올갯수
            urlBuilder.append("&" + URLEncoder.encode("mobileOS", "UTF-8") + "=" + URLEncoder.encode("ETC", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("listYN", "UTF-8") + "=" + URLEncoder.encode("Y", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("arrange", "UTF-8") + "=" + URLEncoder.encode("A", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("mobileApp", "UTF-8") + "=" + URLEncoder.encode("EventWeb", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("eventStartDate", "UTF-8") + "=" + URLEncoder.encode("20230525", "UTF-8"));
            urlBuilder.append("&_type=json"); /*결과 json 포맷*/
            URL url = new URL(urlBuilder.toString());
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line + "\n");
            }
            rd.close();
            conn.disconnect();
        } catch (Exception e) {
            if(conn!=null){
                conn.disconnect();
            }
            e.printStackTrace();
        }
        log.info(this.getClass().getName() + ".API End!");
        return result + "";
    }

    @Override
    public String getWeather(String x, String y) throws Exception {

        log.info(this.getClass().getName() + "API Start!");
        StringBuffer result = new StringBuffer();
        HttpURLConnection conn =null;
        LocalDate now = LocalDate.now();
        String formatedNow = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        log.info("x : "+x);
        log.info("y : "+y);



        try {
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=vEGYRXx26Vq3iX5YSal2u9DGvg1usDpqaP2gY3lTiVDd4h5PslrUCKVs9vI7in15GK9XYMImjmMMmsLH%2B4KThw%3D%3D"); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지 번호*/
            urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("10", "UTF-8"));//가져올갯수
            urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode(formatedNow , "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode("0500", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode("37", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode("128", "UTF-8"));
            urlBuilder.append("&dataType=JSON"); /*결과 json 포맷*/
            URL url = new URL(urlBuilder.toString());
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line + "\n");
            }
            rd.close();
            conn.disconnect();
        } catch (Exception e) {
            if(conn!=null){
                conn.disconnect();
            }
            e.printStackTrace();
        }
        log.info(this.getClass().getName() + ".API End!");
        return result + "";
    }


}

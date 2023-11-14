package kopo.poly.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Data
public class EventDTO {
    private int eventSeq;
    private String title;
    private String addr;
    private String detail;
    private int price;
    private String readCnt;
    private String channel;
    private String category;
    private String eventstartdate;
    private String eventenddate;
    private String firstimage;
    private String firstimage2;
    private String mapx;
    private String mapy;
    private String tel;
    private String good;
    private String bad;
    private String tag;

}

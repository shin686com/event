package kopo.poly.dto;

import lombok.Data;

@Data
public class PageDTO {
    private int total;
    private int page;
    private int size;
}

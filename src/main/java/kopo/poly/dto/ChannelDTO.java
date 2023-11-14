package kopo.poly.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Data
public class ChannelDTO {
    private String channelSeq;
    private String channel;
    private String userId;
    private String file;
}

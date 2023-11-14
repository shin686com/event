package kopo.poly.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Data
public class CommentDTO {
    private String userid;
    private String comment;
    private String parents;
    private String posts;
    private String id;
}

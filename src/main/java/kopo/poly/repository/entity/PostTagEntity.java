package kopo.poly.repository.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "POSTTAG")
public class PostTagEntity {

    @Id
    private String id;

    @NonNull
    @Field(name = "POSTID")
    private String postid;

    @NonNull
    @Field(name = "TAGID")
    private String tagid;
}

package kopo.poly.repository.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "LIKE")
public class LikeEntity {

    @Id
    private String id;

    @NonNull
    @Field(name = "USER")
    private String user;

    @NonNull
    @Field(name = "TITLE")
    private String title;

    @NonNull
    @Field(name = "LIKES")
    private int likes;
}

package kopo.poly.repository.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "JOIN")
public class JoinEntity {
    @Id
    private String id;

    @NonNull
    @Field(name = "USERID")
    private String userid;

    @NonNull
    @Field(name = "NAME")
    private String name;
    @NonNull
    @Field(name = "TITLE")
    private String title;
    @NonNull
    @Field(name = "CELLPHONENO")
    private String cellphoneNo;
}

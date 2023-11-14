package kopo.poly.repository.entity;

import com.mongodb.lang.Nullable;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "COMMENT")
public class CommentEntity {
    @Id
    private String id;

    @NonNull
    @Field(name = "USERID")
    private String userid;

    @NonNull
    @Field(name = "COMMENT")
    private String comment;

    @Nullable
    @Field(name = "PARENTS")
    private String parents;

    @NonNull
    @Field(name = "POSTS")
    private String posts;

}

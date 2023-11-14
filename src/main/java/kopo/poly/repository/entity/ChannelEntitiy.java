package kopo.poly.repository.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "Channel")
public class ChannelEntitiy {

    @Id
    private String channelSeq;
    @NonNull
    @Field(name = "CHANNEL")
    private String channel;
    @NonNull
    @Field(name = "USER_ID")
    private String userId;
    @NonNull
    @Field(name = "FILE")
    private String file;
}

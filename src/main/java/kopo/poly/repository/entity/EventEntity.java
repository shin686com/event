package kopo.poly.repository.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "EVENT")
public class EventEntity {

    @Transient
    public static final String SEQUENCE_NAME = "user_sequence";
    @Id
    private int eventSeq;

    @NonNull
    @Field(name = "TITLE")
    private String title;

    @NonNull
    @Field(name = "CHANNEL")
    private String channel;

    @NonNull
    @Field(name = "FIRSTIMAGE")
    private String firstimage;

    @NonNull
    @Field(name = "DETAIL")
    private String detail;

    @NonNull
    @Field(name = "PRICE")
    private int price;

    @NonNull
    @Field(name = "ADDR")
    private String addr;
    @NonNull
    @Field(name = "CATEGORY")
    private String category;

    @NonNull
    @Field(name = "read_cnt")
    private Long readCnt;



}


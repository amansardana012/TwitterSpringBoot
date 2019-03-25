package com.example.intuitTwitter.domain.valueObject;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tweets")
public class Tweet {

    @ApiModelProperty(hidden=true)
    private String _id;
    private String userId;
    private String content;
    @ApiModelProperty(hidden=true)
    private Long timeStampInMillis;

    public Tweet(){}

    public Tweet(String userId, String content) {
        this.userId = userId;
        this.content = content;
        this.timeStampInMillis = System.currentTimeMillis();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getTimeStampInMillis() {
        return timeStampInMillis;
    }

    public void setTimeStampInMillis(Long timeStampInMillis) {
        this.timeStampInMillis = timeStampInMillis;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

}

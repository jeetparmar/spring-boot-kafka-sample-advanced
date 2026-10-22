package com.example.demo.model;

public class UserMessage {
    private String id;
    private String content;
    private long timestamp;

    public UserMessage() {}

    public UserMessage(String id, String content, long timestamp) {
        this.id = id;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    @Override
    public String toString() {
        return "UserMessage{id='" + id + "', content='" + content + "', timestamp=" + timestamp + "}";
    }
}

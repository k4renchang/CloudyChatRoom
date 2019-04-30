package com.example.cloudychatroom.Components;

public class Message {
    private String name, message, key;

    public Message() {}

    public Message(String name, String message) {
        this.name = name;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "Message{" +
                "name='" + name + '\'' +
                ", message='" + message + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}

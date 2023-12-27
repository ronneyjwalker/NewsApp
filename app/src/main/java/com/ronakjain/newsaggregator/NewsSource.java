package com.ronakjain.newsaggregator;

public class NewsSource {

    private String identity;
    private String name;
    private String type;
    private String link;

    public NewsSource(String identity, String name, String type, String source) {
        setIdentification(identity);
        setName(name);
        setType(type);
        setLink(source);
    }

    @Override
    public String toString() {
        return name;
    }

    public String getType() {
        return type;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getIdentification() {
        return identity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIdentification(String identification) {
        this.identity = identification;
    }

    public String getName() {
        return name;
    }

    public void setType(String type) {
        this.type = type;
    }
}

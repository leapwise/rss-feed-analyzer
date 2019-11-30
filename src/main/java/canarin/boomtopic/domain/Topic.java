package canarin.boomtopic.domain;

import javax.persistence.*;

@Entity
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long identifier;
    private String newsHeader;
    @Lob @Column(columnDefinition = "LONGVARCHAR")
    private String newsLink;
    private String keyword;
    private Integer keywordFrequency;

    public Topic(Long identifier, String newsHeader, String newsLink, String keyword, Integer keywordFrequency) {
        this.identifier = identifier;
        this.newsHeader = newsHeader;
        this.newsLink = newsLink;
        this.keyword = keyword;
        this.keywordFrequency = keywordFrequency;
    }

    public Topic() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Long identifier) {
        this.identifier = identifier;
    }

    public String getNewsHeader() {
        return newsHeader;
    }

    public void setNewsHeader(String newsHeader) {
        this.newsHeader = newsHeader;
    }

    public String getNewsLink() {
        return newsLink;
    }

    public void setNewsLink(String newsLink) {
        this.newsLink = newsLink;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getKeywordFrequency() {
        return keywordFrequency;
    }

    public void setKeywordFrequency(Integer keywordFrequency) {
        this.keywordFrequency = keywordFrequency;
    }

    @Override
    public String toString() {
        return "Topic{" +
                "id=" + id +
                ", identifier=" + identifier +
                ", newsHeader='" + newsHeader + '\'' +
                ", newsLink='" + newsLink + '\'' +
                ", keyword='" + keyword + '\'' +
                ", keywordFrequency=" + keywordFrequency +
                '}';
    }

    public Topic build() throws NullPointerException {
        return new Topic(identifier, newsHeader, newsLink, keyword, keywordFrequency);
    }
}

package canarin.boomtopic.domain;

import java.net.URL;
import java.util.List;

public class Feed {

    private String title;
    private String link;
    private URL url;
    private List<String> nouns;

    public Feed(String title, String link, List<String> nouns, URL url) {
        this.title = title;
        this.link = link;
        this.url = url;
        this.nouns = nouns;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public URL getUrl() {
        return url;
    }

    public List<String> getNouns() {
        return nouns;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public void setNouns(List<String> nouns) {
        this.nouns = nouns;
    }
}

package hr.leapwise.exercise.domain.engine.analyisis.model.custom.impl;

import hr.leapwise.exercise.domain.engine.analyisis.model.custom.Dissasambled;
import hr.leapwise.exercise.domain.engine.analyisis.model.custom.abstracts.CustomDescriptorsHolder;

import java.util.*;

public class CustomDissasambled extends CustomDescriptorsHolder implements Dissasambled {

    protected Set<CustomFeedModel> feeds = new HashSet<>();

    public void addFeed(final CustomFeedModel feed) {
        this.feeds.add(feed);
    }

    public Set<CustomFeedModel> getFeeds() {
        return feeds;
    }
}

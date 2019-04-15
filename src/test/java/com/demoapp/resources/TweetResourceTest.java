package com.demoapp.resources;

import com.demoapp.domain.Tweet;
import org.junit.Assert;

import java.sql.Timestamp;
import java.time.Instant;

import static org.junit.Assert.*;

public class TweetResourceTest {
    @org.junit.Test
    public void getEntry() throws Exception {

        String entry = "entry";
        Tweet tweet = new Tweet(entry,"hashtag");
        tweet.setLastModifiedAt(new Timestamp(System.currentTimeMillis()));
        TweetResource tweetResource = new TweetResource(tweet);
        Assert.assertTrue("entry is not matching",tweetResource.getEntry().equals(entry));

    }

    @org.junit.Test
    public void getHashtag() throws Exception {

    }

    @org.junit.Test
    public void getLastModifiedAt() throws Exception {

    }

}
package com.clairvoyant.controller;

import com.clairvoyant.model.NewsFeed;
import com.clairvoyant.model.NewsFeedDto;
import com.clairvoyant.reader.NewsFeedReader;
import com.clairvoyant.service.NewsFeedService;
import com.rometools.rome.feed.synd.SyndEntry;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by imteyaz on 09/04/18
 **/

@RestController
@CrossOrigin("*")
public class NewsFeedController {

  private final NewsFeedService newsFeedService;
  private final NewsFeedReader newsFeedReader;

  @Autowired
  public NewsFeedController(NewsFeedService newsFeedService,
      NewsFeedReader newsFeedReader) {
    this.newsFeedService = newsFeedService;
    this.newsFeedReader = newsFeedReader;
  }

  @PostMapping(value = "/rss",
      produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public List<NewsFeed> saveRssFeed(@RequestBody String url) {
    List<SyndEntry> syndEntries = newsFeedReader.readNewsFeed(url);
    return newsFeedService.save(NewsFeed.from(syndEntries));
  }

  @PutMapping(value = "/newsfeed/", produces = MediaType.APPLICATION_JSON_VALUE)
  public NewsFeed updateNewsFeed(@RequestBody NewsFeedDto newsFeed) {
    return newsFeedService.updateNewsFeed(newsFeed);
  }

  @PostMapping(value = "/newsfeed/", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<NewsFeedDto> saveNewsFeed(@RequestBody NewsFeedDto newsFeed) {
    return ResponseEntity.ok(newsFeedService.saveNewsFeed(newsFeed));
  }

  @GetMapping(value = "/all-rss-feeds", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<NewsFeedDto>> getAll() {
    return ResponseEntity.ok(newsFeedService.getAllNewsFeed());
  }

  @GetMapping(value = "/newsfeed/title", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<NewsFeedDto> getByTitle(@RequestParam String title) {
    return ResponseEntity.ok(newsFeedService.getByTitle(title));
  }

  @DeleteMapping(value = "/newsfeed/title", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<NewsFeedDto> deleteByTitle(@RequestParam("param") String title) {
    newsFeedService.deleteByTitle(title);
    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }
}

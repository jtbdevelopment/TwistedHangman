package com.jtbdevelopment.TwistedHangman.game.utility;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Date: 11/1/14 Time: 5:54 PM
 */
@Document
public class PreMadePuzzle {

  @Id
  private ObjectId id;
  @Indexed
  private String source;
  @Indexed
  private String category;
  private String wordPhrase;

  public ObjectId getId() {
    return id;
  }

  public void setId(ObjectId id) {
    this.id = id;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getWordPhrase() {
    return wordPhrase;
  }

  public void setWordPhrase(String wordPhrase) {
    this.wordPhrase = wordPhrase;
  }
}

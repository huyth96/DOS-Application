package com.drinkorder.ui.chat.customer;

public class CustomerChatMessageItem {
  private final long id;
  private final String message;
  private final String timestamp;
  private final boolean mine;

  public CustomerChatMessageItem(long id, String message, String timestamp, boolean mine) {
    this.id = id;
    this.message = message;
    this.timestamp = timestamp;
    this.mine = mine;
  }

  public long getId() {
    return id;
  }

  public String getMessage() {
    return message;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public boolean isMine() {
    return mine;
  }
}

// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.data;

/** Represents a comment in store or user page. */
public class Comment {

  private final long id;
  private final long rating;
  private final String drink;
  private final String content;
  private final String store;
  private final String email;
  private final String storeInfo;

  public Comment(
      long id,
      long rating,
      String drink,
      String content,
      String store,
      String email,
      String storeInfo) {
    this.id = id;
    this.rating = rating;
    this.drink = drink;
    this.content = content;
    this.store = store;
    this.email = email;
    this.storeInfo = storeInfo;
  }

  public long getId() {
    return id;
  }

  public long getRating() {
    return rating;
  }

  public String getDrink() {
    return drink;
  }

  public String getContent() {
    return content;
  }

  public String getStore() {
    return store;
  }

  public String getEmail() {
    return email;
  }

  public String getStoreInfo() {
    return storeInfo;
  }
}

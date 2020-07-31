package com.google.sps.data;

import java.util.*;

public class Recommended {

  public static Drink getDrink(String storeId, String drinkName) {
    List<Drink> drinkList = DrinkDAO.getDrinksByStore(storeId);
    for (Drink d : drinkList) {
      if (d.getName().equals(drinkName)) {
        return d;
      }
    }
    return null;
  }
  // given a coffeeshop, return its tydrecommended score
  public static double getScore(Drink d) {
    int numComments = d.getNumRatings();
    double avgRating = d.getRating();
    double distanceMinutes = getDistanceMins();
    double commentScore, ratingScore, distanceScore;
    //      /30             /35         /35

    // get commentScore
    if (numComments >= 100) { // 100+
      commentScore = 30;
    } else if (numComments >= 75) { // 75-99
      commentScore = 25;
    } else if (numComments >= 50) { // 50-74
      commentScore = 20;
    } else if (numComments >= 25) { // 25-49
      commentScore = 15;
    } else {
      commentScore = ((double) numComments * .6);
    }

    // get ratingScore
    ratingScore = avgRating * 7;

    // get distanceScore
    if (distanceMinutes <= 4.0) { // 0-4 mins
      distanceScore = 35;
    } else {
      distanceScore = 35 - distanceMinutes;
    }

    return (commentScore + ratingScore + distanceScore);
  }
  // this function returns the highest scored store from a list of stores
  public static String getBestShop(List<String> listStoreIds, String inputBeverage) {
    double bestScore = 0.0;
    String highestScoredStore = "";
    for (String storeId : listStoreIds) {
      Drink d = getDrink(storeId, inputBeverage);
      if (d != null) {
        double currentShopScore = getScore(d);
        if (currentShopScore > bestScore) {
          highestScoredStore = storeId;
          bestScore = currentShopScore;
        }
      }
    }
    if (!highest.equals("")) return highest;
    return "No Results";
  }

  public static int getDistanceMins() { // todo: get distance of store in mins
    // calculate distance with store functions and compare with user info
    return 1; // example return for testing purposes
  }
}

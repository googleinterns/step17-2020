package com.google.sps.data;

import java.util.*;

public class Recommended {

  public static Drink getDrink(String storeid, String drinkName) {
    List<Drink> drinklist = DrinkDAO.getDrinksByStore(storeid);
    for (Drink d : drinklist) {
      if (d.getName().equals(drinkName)) {
        return d;
      }
    }
    return null;
  }
  // given a coffeeshop, return its tydrecommended score
  public static double getScore(Drink d) {
    double comments = d.getNumRatings();
    // instead of shop rating get drink rating from this store
    double rating = d.getRating();
    double distanceMinutes = getDistanceMins();
    double commentScore, ratingScore, distanceScore;
    //      /30             /35         /35

    // get commentScore
    if (comments >= 100.0) // 100+
    commentScore = 30;
    else if (comments >= 75.0) // 75-99
    commentScore = 25;
    else if (comments >= 50.0) // 50-74
    commentScore = 20;
    else if (comments >= 25.0) // 25-49
    commentScore = 15;
    else commentScore = comments * .6;

    // get ratingScore
    ratingScore = rating * 7;

    // get distanceScore
    if (distanceMinutes <= 4.0) // 0-4 mins
    distanceScore = 35;
    else distanceScore = 35 - distanceMinutes;

    return (commentScore + ratingScore + distanceScore);
  }
  // this function returns the highest rated store from a list of stores
  public static String getBestShop(List<String> listStoreIds, String inputBeverage) {
    double max = 0.0;
    String highest = "";
    for (String storeid : listStoreIds) {
      Drink d = getDrink(storeid, inputBeverage);
      if (d != null) {
        double currentShopScore = getScore(d);
        if (currentShopScore > max) {
          highest = storeid;
          max = currentShopScore;
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

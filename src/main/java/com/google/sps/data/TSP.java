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

import java.util.*;

/** Represents a comment in store or user page. */
public class TSP {

  // private Map<String, Integer> indexToStoreId = new HashMap<>();

  public static double[][] constructAdjacencyMatrix(
      List<Map<String, String>> coffeeShops, double userLat, double userLng) {
    // There are size of coffee shops + 1 (user location) points in total
    double[][] adjacencyMatrix = new double[coffeeShops.size() + 1][coffeeShops.size() + 1];

    for (int i = 0; i < coffeeShops.size(); i++) {
      adjacencyMatrix[0][i + 1] = Double.parseDouble(coffeeShops.get(i).get("distance"));
    }
    for (int i = 0; i < coffeeShops.size(); i++) {
      for (int j = 0; j < coffeeShops.size(); j++) {
        double lat1 = Double.parseDouble(coffeeShops.get(i).get("lat"));
        double lon1 = Double.parseDouble(coffeeShops.get(i).get("lng"));
        double lat2 = Double.parseDouble(coffeeShops.get(j).get("lat"));
        double lon2 = Double.parseDouble(coffeeShops.get(j).get("lng"));
        adjacencyMatrix[i + 1][j + 1] = haversineDistance(lat1, lon1, lat2, lon2);
      }
    }
    return adjacencyMatrix;
  }

  /**
   * /* Calculates the straight line distance between two sets of latitutes and longitutes A
   * breakdown of the haversine formula can be found at /*
   * http://www.movable-type.co.uk/scripts/latlong.html
   */
  public static double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
    int R = 6371; // Radius of the earth in km
    double dLat = degreeToRadian(lat2 - lat1);
    double dLon = degreeToRadian(lon2 - lon1);
    double a =
        Math.sin(dLat / 2) * Math.sin(dLat / 2)
            + Math.cos(degreeToRadian(lat1))
                * Math.cos(degreeToRadian(lat2))
                * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    double distance = R * c; // Distance in km
    return distance;
  }

  public static double degreeToRadian(double degree) {
    return degree * (Math.PI / 180);
  }

  /* Function to find the minimum weight */
  public static double travelingSalesman(
      double[][] adjacencyMatrix,
      boolean[] visited,
      int currPos,
      int n,
      int count,
      double cost,
      double ans,
      List<Integer> bestPath,
      List<Integer> currPath) {

    // If last node is reached (not counting index 0 which is the user location)
    // Update "ans" if the cost of current traversal is less than "ans"
    // Update bestPath so far when "ans" get updated
    // Finally return to check for more possible values
    if (count == (n - 1)) {
      double currDist = cost + adjacencyMatrix[0][currPos];
      if (ans > currDist) {
        ans = currDist;
        bestPath.clear();
        bestPath.addAll(currPath);
        // Add user location to the end of the path
        bestPath.add(0);
      }
      return ans;
    }

    // BACKTRACKING STEP
    // 0 is the user location, which will never be considered as part of the path
    // since the for loop starts iterating from 1
    visited[0] = true;
    for (int i = 1; i < n; i++) {
      if (visited[i] == false && adjacencyMatrix[currPos][i] > 0) {

        // Mark as visited
        visited[i] = true;
        currPath.add(i);
        ans =
            travelingSalesman(
                adjacencyMatrix,
                visited,
                i,
                n,
                count + 1,
                cost + adjacencyMatrix[currPos][i],
                ans,
                bestPath,
                currPath);

        // Mark ith node as unvisited
        visited[i] = false;
        currPath.remove(currPath.size() - 1);
      }
    }
    return ans;
  }
}

package org.example;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class SearchHandler {
    public static void searchBeatmaps(String keyword, int mapStatus, double minDiff, double maxDiff, double minCS, double maxCS, double minAR, double maxAR, double minOD, double maxOD, double minBPM, double maxBPM, double minLength, double maxLength, JLabel status, File downloadDirectory, int cycle) throws IOException, ParseException, InterruptedException {
        URL url = new URL("https://api.chimu.moe/v1/search?mode=0&amount=50&offset=" + (cycle * 50) + "&" +

                String.format("query=%s&", keyword) +

                String.format("status=%s&", mapStatus) +

                String.format("min_diff=%s&", minDiff) +

                String.format("max_diff=%s&", maxDiff) +

                String.format("min_ar=%s&", minAR) +

                String.format("max_ar=%s&", maxAR) +

                String.format("min_cs=%s&", minCS) +

                String.format("max_cs=%s&", maxCS) +

                String.format("min_od=%s&", minOD) +

                String.format("max_od=%s&", maxOD) +

                String.format("min_bpm=%s&", minBPM) +

                String.format("max_bpm=%s&", maxBPM) +

                String.format("min_length=%s&", minLength) +

                String.format("max_length=%s", maxLength)
                );
        System.out.println(url);
        //mapset api url
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("User-Agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
        int responseCode = conn.getResponseCode();

        // 200 OK
        if (responseCode != 200) {
            throw new RuntimeException("HttpResponseCode: " + responseCode);
        } else {

            StringBuilder informationString = new StringBuilder();
            Scanner scanner = new Scanner(url.openStream());

            while (scanner.hasNext()) {
                informationString.append(scanner.nextLine());
            }
            //Close the scanner
            scanner.close();

            System.out.println(informationString);



            //JSON simple library Setup with Maven is used to convert strings to JSON
            JSONParser parse = new JSONParser();
            JSONObject mapsetObject = (JSONObject) parse.parse(String.valueOf(informationString));
            JSONArray maps = (JSONArray) mapsetObject.get("data");
            System.out.println(maps.size());

            status.setText(String.format("%s%s - %s (%s).osz", downloadDirectory.getPath() + "\\", "map", "artist", "mapper"));
            status.repaint();

            for (Object map:maps) {
                try {
                    JSONObject mapObject = (JSONObject) parse.parse(String.valueOf(map));
                    DownloadHandler.download(downloadDirectory, String.valueOf(mapObject.get("SetId")));
                } catch (Exception e) {
                    System.out.println("Error with retrieving mapset info of map.");
                }
            }

            if (maps.size() == 50) {
                cycle += 1;
                searchBeatmaps(keyword, mapStatus, minDiff, maxDiff, minCS, maxCS, minAR, maxAR, minOD, maxOD, minBPM, maxBPM, minLength, maxLength, status, downloadDirectory, cycle);
            } else {
                status.setText(String.format("%s%s - %s (%s).osz", downloadDirectory.getPath() + "\\", "map", "artist", "mapper"));
                status.repaint();
            }
            //TODO: ONCE SEARCH FINISHES (MAX LENGTH 50 BEATMAPS), check if most recent is blank, other
        }
    }
}

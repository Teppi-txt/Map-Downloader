package org.example;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DownloadHandler {
    public static void download(File path, String mapsetID) throws IOException, ParseException {
        URL downloadURL = new URL(String.format("https://api.chimu.moe/v1/download/%s?n=1", mapsetID));
        //download api url

        URLConnection c = downloadURL.openConnection();

        c.setRequestProperty("User-Agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
        BufferedInputStream in = new BufferedInputStream(c.getInputStream());
        byte[] dataBuffer = new byte[1024];
        int bytesRead;


        String mapsetTitle = "unknown";
        String mapsetArtist = "unknown";
        String mapsetMapper = "unknown";
        URL mapsetURL = new URL(String.format("https://api.chimu.moe/v1/set/%s?n=1", mapsetID));
        //mapset api url
        HttpURLConnection conn = (HttpURLConnection) mapsetURL.openConnection();
        int responseCode = conn.getResponseCode();

        // 200 OK
        if (responseCode != 200) {
            throw new RuntimeException("HttpResponseCode: " + responseCode);
        } else {

            StringBuilder informationString = new StringBuilder();
            Scanner scanner = new Scanner(mapsetURL.openStream());

            while (scanner.hasNext()) {
                informationString.append(scanner.nextLine());
            }
            //Close the scanner
            scanner.close();


            //JSON simple library Setup with Maven is used to convert strings to JSON
            JSONParser parse = new JSONParser();
            JSONObject mapsetObject = (JSONObject) parse.parse(String.valueOf(informationString));
            mapsetTitle = (String) mapsetObject.get("Title");
            mapsetArtist = (String) mapsetObject.get("Artist");
            mapsetMapper = (String) mapsetObject.get("Creator");
        }

        if (!new File(String.format("%s%s - %s (%s).osz", path.getPath() + "\\", mapsetArtist, mapsetTitle, mapsetMapper)).exists()) {
            System.out.println(String.format("%s%s - %s (%s).osz", path.getPath() + "\\", mapsetArtist, mapsetTitle, mapsetMapper));
            try {
                FileOutputStream fileOS = new FileOutputStream(String.format("%s%s - %s (%s).osz", path.getPath() + "\\", mapsetArtist, mapsetTitle, mapsetMapper));
                while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                    fileOS.write(dataBuffer, 0, bytesRead);
                }
                fileOS.close();
                in.close();
            } catch (Exception e) {
                System.out.println("Unable to download beatmap: " + mapsetTitle);
            }
        };
    }
}

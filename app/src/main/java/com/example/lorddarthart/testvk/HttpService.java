package com.example.lorddarthart.testvk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HttpService {
    ArrayList<NoteText> hm = new ArrayList<>();
    NoteText nt;

    public List<NoteText> GetNotes(String ownerId, Long count) throws IOException, JSONException {
        String url = "https://api.vk.com/method/wall.get?access_token="+/*Тут должен быть токен*/""+"&v=5.92&filter=all&owner_id=" + ownerId + "&count=" + count;

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");

        con.setRequestProperty("connection", "keep-alive");
        con.setRequestProperty("content-type", "application/octet-stream");
        con.setConnectTimeout(10000);
        con.setReadTimeout(10000);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        if (!(responseCode == 401)&&!(responseCode == 18)&&!(responseCode == 19)&&!(responseCode == 30)) {
            InputStream inputStream = con.getInputStream();
            String stringResponse = inputStreamToString(inputStream);
            try {
                JSONArray jsonNotes = new JSONObject(stringResponse).getJSONObject("response").getJSONArray("items");
                if (jsonNotes.length()<count) {
                    count = Long.valueOf(jsonNotes.length());
                }
                for (int i = 0; i < count; i++) {
                    nt = new NoteText();
                    nt.setResponse(responseCode);
                    if (!(jsonNotes.getJSONObject(i).getString("text").equals(""))) {
                        nt.setNoteText(jsonNotes.getJSONObject(i).getString("text"));
                    } else {
                        if (!(jsonNotes.getJSONObject(i).isNull("copy_history"))&&!(jsonNotes.getJSONObject(i).getJSONArray("copy_history").getJSONObject(0).getString("text").equals(""))) {
                            nt.setNoteText(jsonNotes.getJSONObject(i).getJSONArray("copy_history").getJSONObject(0).getString("text"));
                        } else {
                            continue;
                        }
                    }
                    hm.add(nt);
                }
            } catch (Exception e) {
                nt = new NoteText();
                nt.setResponse(401);
                nt.setNoteText("");
                hm.add(nt);
            }
        }
        return hm;
    }

    private String inputStreamToString(InputStream inputStream) throws IOException {
        try (ByteArrayOutputStream result = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            return result.toString("UTF-8");
        }
    }
}

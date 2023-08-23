package Helpers;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;


public class HTTP {
    public static HttpURLConnection sendPostRequest(String content, String API, String body) throws UnirestException, IOException {
        URL url = new URL(API);
        URLConnection con = url.openConnection();
        HttpURLConnection http = (HttpURLConnection)con;
        http.setRequestMethod("POST"); // PUT is another valid option
        http.setDoOutput(true);

        byte[] out = body.getBytes(StandardCharsets.UTF_8);
        int len = out.length;

        http.setFixedLengthStreamingMode(len);
        http.setRequestProperty("Content-Type", content + "; charset=UTF-8");
        http.connect();
        try(OutputStream os = http.getOutputStream()) {
            os.write(out);
        }
        return http;
    }

    public static String convertInputStream2String(InputStream inputStream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        for (int length; (length = inputStream.read(buffer)) != -1; ) {
            result.write(buffer, 0, length);
        }
        String res =  result.toString("UTF-8");
        return res;
    }
}





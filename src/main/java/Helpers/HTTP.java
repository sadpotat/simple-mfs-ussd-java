package Helpers;

import Cache.CacheLoader;
import Cache.RequestProperties;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;


public class HTTP {
    public static HttpURLConnection sendRequest(RequestProperties reqProperties) throws IOException {
        CacheLoader cache = CacheLoader.getInstance();
        String urlStr = cache.getUrlFromApiId(reqProperties.getApiId());
        // creating a httpconnection object and setting its properties
        URL url = new URL(urlStr);
        URLConnection con = url.openConnection();
        HttpURLConnection http = (HttpURLConnection)con;
        http.setRequestMethod(reqProperties.getReqMethod()); // PUT is another valid option
        http.setRequestProperty("Content-Type", reqProperties.getContentType());
        http.setConnectTimeout(reqProperties.getTimeout());
        http.setDoOutput(true);

        // the body parameter
        byte[] out = reqProperties.getBody().getBytes(StandardCharsets.UTF_8);
        int len = out.length;
        http.setFixedLengthStreamingMode(len);

        // sending the request
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
        return result.toString(StandardCharsets.UTF_8);
    }
}





package com.learning.google_search_api;

import java.net.URLEncoder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class App {
    public static void main(String[] args) throws Exception {

        String searchQuery = "Fifth gentech chennai";

        String google = "http://www.google.com/search?q=";
        String charset = "UTF-8";
        String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36";

        Document document = Jsoup.connect(google + URLEncoder.encode(searchQuery, charset)).userAgent(userAgent).get();
        Elements rightColumn = document.select("div#rhscol");

        Elements no = rightColumn.select("div._eFb").select("span._Xbe._ZWk.kno-fv");

        String website = rightColumn.select("._ldf>a").attr("href");
        String number = no.text();

        System.out.println(website + "\t " + number);
    }

}

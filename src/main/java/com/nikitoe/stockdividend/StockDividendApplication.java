package com.nikitoe.stockdividend;

import java.io.IOException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.HttpStatus;

//@SpringBootApplication
public class StockDividendApplication {

    public static void main(String[] args) {
        //SpringApplication.run(StockDividendApplication.class, args);

        System.out.println(HttpStatus.OK);

        try {
            Connection connection = Jsoup.connect(
                "https://finance.yahoo.com/quote/COKE/history?period1=99100800&period2=1663286400&interval=1mo&filter=history&frequency=1mo&includeAdjustedClose=true");
            Document document = connection.get();

            Elements elements = document.getElementsByAttributeValue("data-test",
                "historical-prices");
            Element element = elements.get(0);  //table 전체

            Element tbody = element.children().get(1); // table의 tbody 만
            for (Element e : tbody.children()) {
                String txt = e.text();
                if (!txt.endsWith("Dividend")) {
                    continue;
                }
                String[] splites = txt.split(" ");

                String month = splites[0]; // 월
                int day = Integer.valueOf(splites[1].replace(",", "")); // 일
                int year = Integer.valueOf(splites[2]); // 년도
                String dividend = splites[3]; // 배당금

                System.out.println(year + "/" + month + "/" + day + " -> " + dividend);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

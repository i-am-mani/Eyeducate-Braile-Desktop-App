package sample;

//API KEY FOR NEWAPI:- 9896f8de64c94ab5a0e86aa92c563fc2

import com.fazecast.jSerialComm.SerialPort;
import com.jaunt.*;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class webScrapper {

    public static Map<String, String> topHeadlines = new HashMap<>();
    public static StringBuilder article;

    public static void main(String[] args) throws IOException {
        SerialPort[] serialPorts = SerialPort.getCommPorts();
        for (SerialPort f : serialPorts) {
            System.out.println(f);
        }
        SerialPort serialPort = SerialPort.getCommPorts()[1];
        serialPort.openPort();
        while (!serialPort.isOpen()) {
        }
        OutputStream output = serialPort.getOutputStream();
        output.write("z".getBytes());


    }

    public void extractTopHeadlines(int pageNumber) {
        topHeadlines.clear();
        UserAgent userAgent = new UserAgent();
        try {

            userAgent.visit("https://www.indiatoday.in/top-stories?page=" + String.valueOf(pageNumber));
            Elements mainItem = userAgent.doc.findEach("<section class=\"col-md-8\" id=\"content\" role=\"main\"> <a id=\"main-content\">");
            Element element = mainItem.findFirst("<div class=\"view-content\"");
            List<Element> items = element.getChildElements();
            for (Element item : items) {
                List<Element> nestedTags = item.getChildElements();
                for (Element tag : nestedTags) {
                    if (tag.getAt("class").equals("detail")) {
                        String heading = tag.findFirst("<h\\d>").getTextContent();
                        String link = tag.findFirst("<a>").getAt("href");
                        topHeadlines.put(heading, link);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String extractArticleInformation(String url) {

        article = new StringBuilder();
        UserAgent userAgent = new UserAgent();
        try {
            userAgent.setCacheEnabled(true);
            userAgent.visit(url);

            Elements elements = userAgent.doc.findEvery("<p>");
//            System.out.println(elements.size());
            for (Element element : elements) {
                article.append(element.getChildText());
//                System.out.println(element.getChildText());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return article.toString();
    }

    public void extractAllText(Element element) {

        List<Element> tags = element.getChildElements();
        if (!tags.isEmpty()) {
            for (Element tag : tags) {
                if ((tag.getName().contains("h") || tag.getName().contains("p")) && !tag.getName().contains("script")) {
//                    System.out.println(tag.getTextContent());
                    article.append(tag.getChildText());
                }
                extractAllText(tag);
            }

        }
    }


    public String getContentFromWikipedia(String name) {
        StringBuilder sb = new StringBuilder();
        try {

            String[] nameList = name.split(" ");
            StringBuilder searchURL = new StringBuilder();

            for (String str : nameList) {
                searchURL.append(str.substring(0, 1).toUpperCase() + str.substring(1));
                searchURL.append("_");
            }

            UserAgent userAgent = new UserAgent();                       //create new userAgent (headless browser).
            userAgent.visit("https://en.wikipedia.org/wiki/" + searchURL.toString().substring(0, name.length()));

            Element items = userAgent.doc.findFirst("<div class=\"mw-parser-output\">");

            List<Element> elements = items.getChildElements();

            for (Element item : elements) {

                if (item.getName().contains("h")) {

                    sb.append("\n\t\t" + item.getTextContent().replaceAll("\\[edit\\]", "") + "\n\n");

                } else if (item.getName().contains("p")) {
                    sb.append(item.getTextContent().replaceAll("&#\\d*;\\d*", ""));
                }
            }


        } catch (JauntException e) {         //if an HTTP/connection error occurs, handle JauntException.
            System.err.println(e);
        }
        return sb.toString();
    }
}

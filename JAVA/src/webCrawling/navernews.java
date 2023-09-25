package webCrawling;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class navernews extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("네이버 뉴스 크롤러");

        // ListView를 사용하여 결과를 표시할 준비
        ListView<String> listView = new ListView<>();
        ObservableList<String> items = FXCollections.observableArrayList();
        listView.setItems(items);

        StackPane root = new StackPane();
        root.getChildren().add(listView);

        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();

        int page = 5;

        for (int j = 1; j <= page; j++) {
            String url = "https://news.naver.com/main/list.naver?mode=LS2D&mid=shm&sid1=105&sid2=230&date=20230925&page=" + j;

            try {
                Document doc = Jsoup.connect(url).get();
                Elements elements = doc.getElementsByAttributeValue("class", "list_body newsflash_body");

                Element element = elements.get(0);
                Elements photoElements = element.getElementsByAttributeValue("class", "photo");

                for (int i = 0; i < photoElements.size(); i++) {
                    Element articleElement = photoElements.get(i);
                    Elements aElements = articleElement.select("a");
                    Element aElement = aElements.get(0);

                    String articleUrl = aElement.attr("href");    // 기사링크

                    Element imgElement = aElement.select("img").get(0);
                    String imgUrl = imgElement.attr("src");        // 사진링크
                    String title = imgElement.attr("alt");        // 기사제목

                    Document subDoc = Jsoup.connect(articleUrl).get();

                    // 결과를 GUI에 추가
                    items.add(title);

                    System.out.println(title);
                    System.out.println();
                }
                System.out.println(j + " 페이지 크롤링 종료");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
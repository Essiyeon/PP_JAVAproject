/*
 * 첫 번째 자바프로젝트
 * 주제 : javafx, jsoup 를 이용한 네이버 뉴스웹 크롤링 GUI
 * 주제 선정 배경 : 대부분의 뉴스사이트는 무분별한 광고가 너무 많이 노출됨, 헤드라인만 읽고 싶음
 * 추가하고 싶은 기능 (optional) : 사진도 같이 뜨게하고싶음, 새로고침버튼, 주제별(정치,과학 등)모아보기 버튼
 * 
 * 2023-09-25 Second Commit
 * 네이버 뉴스 IT/과학카테고리의 IT일반 탭의 뉴스를 크롤링합니다.
 * 뉴스의 헤드라인만 GUI창에 출력합니다.
 */
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
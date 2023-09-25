/*
 * 첫 번째 자바프로젝트
 * 주제 : javafx, jsoup 를 이용한 웹 크롤링 GUI (아직 구체화 시키지못했습니다.)
 * 
 * - 특정 웹사이트(아직 고르지못함, 멜론 혹은 cgv)에서 원하는 정보를 받아와 출력하거나 저장함
 * ex)멜론 사이트에서 최신음악정보를 가져와 GUI 창에 띄우고 곡 이름을 눌렀을 때 멜론 사이트로 이동하게하기
 * 
 * 2023-09-18 Commit
 * 이 클래스는 jsoup를 사용해보기 위한 연습 코드입니다.
 * CGV에서 받아온 예매차트를 GUI창으로 출력합니다.
 */

package webCrawling;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Iterator;

public class webCrawling_GUI extends Application {

    @Override
    public void start(Stage primaryStage) {

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));

        String url = "http://www.cgv.co.kr/movies/";
        Document doc = null;

        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Elements element = doc.select("div.sect-movie-chart");


        Iterator<Element> ie1 = element.select("strong.rank").iterator();
        Iterator<Element> ie2 = element.select("strong.title").iterator();

        while (ie1.hasNext()) {
            String rank = ie1.next().text();
            String title = ie2.next().text();

            Label label = new Label(rank + "\t" + title);
            label.setFont(Font.font("Arial", 14));
            vbox.getChildren().add(label);
        }


        Scene scene = new Scene(vbox, 400, 600);
        primaryStage.setTitle("Crawled Movie Data");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
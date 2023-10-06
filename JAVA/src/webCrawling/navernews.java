/*
 * 첫 번째 자바프로젝트
 * 주제 : javafx, jsoup 를 이용한 네이버 뉴스웹 크롤링 GUI
 * 주제 선정 배경 : 대부분의 뉴스사이트는 무분별한 광고가 너무 많이 노출됨, 헤드라인만 읽고 싶음
 * 추가하고 싶은 기능 (optional) : 사진도 같이 뜨게하고싶음, 새로고침버튼, 주제별(정치,과학 등)모아보기 버튼
 * 
 * 2023-09-25 Second Commit
 * 네이버 뉴스 IT/과학카테고리의 IT일반 탭의 뉴스를 크롤링합니다.
 * 뉴스의 헤드라인만 GUI창에 출력합니다.
 * 
 * 2023-09-25 3rd Commit
 * GUI창의 헤드라인을 클릭하면 기사본문으로 이동합니다.
 * 
 * 2023-10-01 First Commit
 * 시스템에서 오늘 날짜를 받아와서 그 날의 기사출력
 * (이전에는 코드를 수정해서 오늘 날짜를 20231001의 형태로 넣어줘야했음)
 * 2023-10-01 Second Commit
 * 새로고침버튼 추가 GUI창에서 새로고침버튼을 누르면 코드가 재실행되면서 GUI창도 업데이트됨(업데이트된 기사가 있을 때)
 * 2023-10-01 3rd Commit
 * 위 내용 업데이트 이후 GUI 창에서 클릭시에 기사 본문이 열리지않음
 * 코드 수정과정에서 기사제목 클릭시 이벤트 처리과정부분을 빠트림 깃헙에서 이전 버전 불러서 복구해놓음
 * 
 * 2023-10-06 First Commit
 * 뉴스 이미지와 기사를 함께 출력하도록 수정함
 */
package webCrawling;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label; // 추가로 Label을 임포트
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class navernews extends Application {

    private ListView<Article> listView; // ListView의 아이템을 Article 객체로 변경
    private ObservableList<Article> items;
    private List<String> articleUrls;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("네이버 뉴스 크롤러");

        BorderPane root = new BorderPane();

        // ListView를 사용하여 결과를 표시할 준비
        listView = new ListView<>();
        items = FXCollections.observableArrayList();
        listView.setItems(items);

        // 새로고침 버튼을 추가합니다.
        Button refreshButton = new Button("새로고침");
        refreshButton.setOnAction(event -> refresh());

        root.setTop(refreshButton);
        root.setCenter(listView);

        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();

        // 초기 데이터 로드
        refresh();
    }

    // Article 클래스 정의: 이미지 URL과 기사 제목을 함께 관리
    public static class Article {
        private final String imgUrl;
        private final String title;

        public Article(String imgUrl, String title) {
            this.imgUrl = imgUrl;
            this.title = title;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public String getTitle() {
            return title;
        }
    }

    // 데이터를 새로고침하는 메서드
    private void refresh() {
        items.clear();
        articleUrls = new ArrayList<>();

        int page = 3;
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formattedDate = currentDate.format(dateFormatter);

        for (int j = 1; j <= page; j++) {
            String url = "https://news.naver.com/main/list.naver?mode=LS2D&mid=shm&sid1=105&sid2=230&date=" + formattedDate + "&page=" + j;

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


                    // 결과를 GUI에 추가
                    items.add(new Article(imgUrl, title));
                    articleUrls.add(articleUrl);

                    System.out.println(title); //콘솔창출력부
                    System.out.println();
                }
                System.out.println(j + " 페이지 크롤링 종료");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 기사 제목을 클릭했을 때 이벤트 처리
        listView.setOnMouseClicked(event -> {
            int selectedIndex = listView.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                String articleUrl = articleUrls.get(selectedIndex);
                openArticlePage(articleUrl);
            }
        });

        // 이미지와 기사 제목을 함께 보여주기 위해 ListCell을 설정
        listView.setCellFactory(new Callback<ListView<Article>, ListCell<Article>>() {
            @Override
            public ListCell<Article> call(ListView<Article> param) {
                return new ListCell<Article>() {
                    @Override
                    protected void updateItem(Article item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            // 이미지를 ImageView로 생성하고 설정
                            Image image = new Image(item.getImgUrl());
                            ImageView imageView = new ImageView(image);

                            // 이미지와 기사 제목을 함께 보여주는 VBox 생성
                            VBox vbox = new VBox(imageView, new Label(item.getTitle()));
                            vbox.setSpacing(10); // 간격 조정
                            setGraphic(vbox); // ListCell에 표시
                        }
                    }
                };
            }
        });
    }

    // 기사 본문 페이지를 열기 위한 메서드
    private void openArticlePage(String articleUrl) {
        try {
            Desktop.getDesktop().browse(new URI(articleUrl));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}

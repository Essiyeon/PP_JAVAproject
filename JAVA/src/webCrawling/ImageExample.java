/*
 * 2023-10-06 First Commit
 * GUI창에 Image를 출력하기위한 예시프로그램입니다.
 * 클래스이름을 Image, ImageViwe로 지정했다가
 * 사용되는 javafx안의 라이브러리들과 이름이 겹쳐 인식이안되는 오류가 있었습니다.
 */

package webCrawling;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ImageExample extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Image Example");

        // 이미지를 로드합니다. 이미지 파일 경로 또는 URL을 사용할 수 있습니다.
        Image image = new Image("https://i3.ruliweb.com/img/22/06/16/1816a5ef58854ae36.png");

        // ImageView를 생성하고 이미지를 설정합니다.
        ImageView imageView = new ImageView(image);

        StackPane root = new StackPane();
        root.getChildren().add(imageView);

        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }
}
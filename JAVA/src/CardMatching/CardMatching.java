/*
 * 첫 번 째 자바 프로젝트
 * 영어단어 카드 매칭 게임
 * 
 */

package CardMatching;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unused")
public class CardMatching extends Application {
    private static final int GRID_SIZE = 4; // 카드 그리드 크기 (4x4)
    private List<String> words;
    private List<String> meanings;
    private List<Card> cards;
    private Card selectedCard = null;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // 단어와 뜻 초기화
        initializeWordsAndMeanings();

        // 카드 생성 및 섞기
        createAndShuffleCards();

        // 그리드 레이아웃 생성
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);

        // 카드를 그리드에 추가
        int row = 0;
        int col = 0;
        for (Card card : cards) {
            grid.add(card, col, row);
            col++;
            if (col == GRID_SIZE) {
                col = 0;
                row++;
            }
        }

        // 카드 클릭 이벤트 처리
        for (Card card : cards) {
            card.setOnMouseClicked(event -> {
                if (selectedCard == null) {
                    selectedCard = card;
                    card.showWord();
                } else if (selectedCard != card) {
                    card.showWord();
                    if (selectedCard.getMeaning().equals(card.getMeaning())) {
                        // 일치하는 경우
                        selectedCard.setMatched(true);
                        card.setMatched(true);
                        selectedCard = null;
                    } else {
                        // 불일치하는 경우
                        selectedCard.hideWord();
                        card.hideWord();
                        selectedCard = null;
                    }
                }
            });
        }

        Scene scene = new Scene(grid, 400, 400);
        primaryStage.setTitle("Card Matching Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initializeWordsAndMeanings() {
        // 단어와 뜻 초기화 (여기에 단어와 뜻을 추가하세요)
        words = new ArrayList<>();
        meanings = new ArrayList<>();
        words.add("Apple");
        meanings.add("사과");
        words.add("Banana");
        meanings.add("바나나");
        words.add("Orange");
        meanings.add("오렌지");
        words.add("Grapes");
        meanings.add("포도");
        // 추가 단어와 뜻을 원하는 만큼 추가하세요.
    }

    private void createAndShuffleCards() {
        // 카드 생성
        cards = new ArrayList<>();
        for (int i = 0; i < words.size(); i++) {
            String word = words.get(i);
            String meaning = meanings.get(i);
            
            // 두 개의 카드를 생성: 하나는 영어 단어, 다른 하나는 한글 뜻
            Card card1 = new Card(word, meaning);
            Card card2 = new Card(meaning, word);
            
            cards.add(card1);
            cards.add(card2);
        }

        // 카드 섞기
        Collections.shuffle(cards);
    }

    // 카드 클래스 정의
    class Card extends Label {
        private final String word;
        private final String meaning;
        private boolean matched = false;

        public Card(String word, String meaning) {
            this.word = word;
            this.meaning = meaning;
            this.setStyle("-fx-border-color: black; -fx-border-width: 1px; -fx-padding: 10px;");
            this.setMinSize(80, 80);
            this.setAlignment(Pos.CENTER);
            this.setText("?");
        }

        public String getWord() {
            return word;
        }

        public String getMeaning() {
            return meaning;
        }

        public void showWord() {
            if (!matched) {
                setText(meaning);
            }
        }

        public void hideWord() {
            if (!matched) {
                setText("?");
            }
        }

        public boolean isMatched() {
            return matched;
        }

        public void setMatched(boolean matched) {
            this.matched = matched;
            if (matched) {
                setText("맞음!");
            }
        }
    }
}

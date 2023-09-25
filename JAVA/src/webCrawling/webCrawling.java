/*
 * 첫 번째 자바프로젝트
 * 주제 : javafx, jsoup 를 이용한 웹 크롤링 GUI (아직 구체화 시키지못했습니다.)
 * 
 * - 특정 웹사이트(아직 고르지못함, 멜론 혹은 cgv)에서 원하는 정보를 받아와 출력하거나 저장함
 * ex)멜론 사이트에서 최신음악정보를 가져와 GUI 창에 띄우고 곡 이름을 눌렀을 때 멜론 사이트로 이동하게하기
 * 
 * 2023-09-18 Commit
 * 이 클래스는 jsoup를 사용해보기 위한 연습 코드입니다.
 * CGV에서 받아온 예매차트를 콘솔창에 출력합니다.
 */

package webCrawling;

import java.io.IOException;
import java.util.Iterator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class webCrawling {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// Jsoup를 이용해서 http://www.cgv.co.kr/movies/ 크롤링
		String url = "http://www.cgv.co.kr/movies/"; //크롤링할 url지정
		Document doc = null;        //Document에는 페이지의 전체 소스가 저장된다

		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//select를 이용하여 원하는 태그를 선택한다. select는 원하는 값을 가져오기 위한 중요한 기능이다.
		Elements element = doc.select("div.sect-movie-chart");    

		System.out.println("============================================================");

		//Iterator을 사용하여 하나씩 값 가져오기
		Iterator<Element> ie1 = element.select("strong.rank").iterator();
		Iterator<Element> ie2 = element.select("strong.title").iterator();
				        
		while (ie1.hasNext()) {
			System.out.println(ie1.next().text()+"\t"+ie2.next().text());
		}
				
		System.out.println("============================================================");
	}

}

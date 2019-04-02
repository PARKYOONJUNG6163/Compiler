package HW01;
// 201602001 박윤정
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
//input 파일 경로 : C://Users//user//Desktop//test.hoo
//output 파일 경로 : C://Users//user//Desktop//test.c
public class hw01 {
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		FileReader fr; // 파일 경로 저장 변수
		String line = "";
		List<String> list = new ArrayList<String>(); 
		try {
			// hoo 파일 생성
			FileWriter fw = new FileWriter("C://Users//user//Desktop//test.hoo"); // 파일을 저장할 경로 설정
			BufferedWriter bw = new BufferedWriter(fw); 
			bw.write("[abc]:\n[def][e/f]:\n[ghi][h/i]: (2) print");
			bw.close();
			
			// 파일 읽어와서 리스트에 추가하는 부분
			fr = new FileReader("C://Users//user//Desktop//test.hoo"); // 파일을 읽어올 경로 설정												// 저장
			BufferedReader br = new BufferedReader(fr); // 버퍼 생성자 객체 br
			while((line = br.readLine()) != null) { // 파일 내용이 비어있을 때까지
				StringTokenizer st = new StringTokenizer(line," "); // " "을 기준으로 읽어온 line을 나눠준다
				while(st.hasMoreTokens()) { // token이 더 이상 없을 까지
					list.add(st.nextToken()); // 리스트에 추가
				}
			}
			fr.close();
			
			// 읽어온 내용 처리
			fw = new FileWriter("C://Users//user//Desktop//test.c"); // 파일을 저장할 경로 설정
			bw = new BufferedWriter(fw); 
			bw.write("#include <stdio.h>"); bw.newLine();
			bw.write("int main(){"); bw.newLine();
			int i=0;
			int number; // 반복 횟수
			boolean newLine = true; // 횟수 적용시 사용 할 새로운 라인인가?
			String result="";
			String tempStr="";
			while(i<list.size()) {
				int count=0;
				String str = list.get(i++);
				for(int j=0;j<str.length();j++) {
					if(str.charAt(j) == '[') count++;
				}
				if(count>2) throw new Exception("에러가 있는 파일입니다."); // 에러 처리
				int parenthesisIndex=str.indexOf('[',1); // '[' 인덱스
				int slashIndex = str.indexOf('/',parenthesisIndex); // '/' 인덱스
				if(str.endsWith(":")) { // 문자열에 해당하면
					tempStr = str.substring(1, str.indexOf(']')); // 문자열 뽑아놓기	
					if(parenthesisIndex != -1) { // '[' 가 두개면
						if(slashIndex != -1) { // '/' 가 존재하면
							tempStr = tempStr.replace(str.charAt(slashIndex+1),str.charAt(slashIndex-1)); // 치환
						}else { // '/'가 없으면 대문자 아니면 소문자로 만들어 결과에 이어 붙이기
							tempStr = (str.charAt(parenthesisIndex+1) == 'U') ? tempStr.toUpperCase():(str.charAt(parenthesisIndex+1) == 'L') ?tempStr.toLowerCase():tempStr;
						}
					}else if(tempStr.equals("")) {
						tempStr = "\\"+"n";
					}
					result += tempStr;
					if(list.get(i).endsWith(":")) newLine = false; // 다음이 또 문자열이면 새로운 라인이 아니므로 false로
				}else { // 명령어에 해당하면
					if(str.equals("print")) { // print이면 출력
						bw.write("printf(\"%s\",\""+result+"\");"); bw.newLine();
						result = "";
						newLine=true;
					}else if(str.equals("ignore")) { // ignore이면 무시
						result = "";
						newLine=true;
					}else if(str.charAt(0) == '(') { // (자연수)이면 자연수만큼 문자열 반복
						String temp = result;
						number = Integer.parseInt(str.substring(1,str.length()-1)); // String형으로 쓰여져 있는 숫자를 string을 int로 변환
						while(number != 1) { // number가 1이 될 때까지 반복하여 문자열을 결과에 이어 붙이기
							result = newLine? result+tempStr:result+temp;
							number--;
						}
					}
				}
			}
			bw.write("return 0;"); bw.newLine();
			bw.write("}"); bw.newLine();
			bw.close();
			
			// c코드 파일 출력하기
			System.out.println("출력");
			fr = new FileReader("C://Users//user//Desktop//test.c"); // 파일을 읽어올 경로 설정												// 저장
			br = new BufferedReader(fr); // 버퍼 생성자 객체 br
			while((line = br.readLine()) != null) { // 파일 내용이 비어있을 때까지
				System.out.println(line);
			}
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		} // 예외상황 처리
	}
}

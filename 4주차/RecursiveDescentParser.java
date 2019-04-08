package HW04;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class RecursiveDescentParser {
	private HashMap<String, ArrayList<String>> first = new HashMap<>();
	private HashMap<String, ArrayList<String>> follow = new HashMap<>();
	private ArrayList<String> terminal = new ArrayList<>();
	int index = 0;
	String[] inputSymbol;
	
	public RecursiveDescentParser(Grammar g, String input) { // 문법과 입력 문자열을 받아 파싱하여 파싱트리 생성
		terminal = g.terminal;
		first = g.findFirst();
		follow = g.findFollow();
		inputSymbol = input.split(" ");
		
		procedureE();
	}
	
	public ArrayList<String> resultLookahead(String... str) { // 가변인자 함수 lookahead만드는 함수
		ArrayList<String> lookahead = new ArrayList<>();
		if(terminal.contains(str[0])) {
			lookahead.add(str[0]);
		}else {
			lookahead.addAll(first.get(str[0]));
		}
		
		for(int i=1;i<str.length;i++) {
			if(lookahead.contains("e") && terminal.contains(str[i])) { // terminal인 경우
				lookahead.remove("e");
				lookahead.add(str[i]);
			}else if(lookahead.contains("e") && i == str.length) { // 마지막은 팔로우
				lookahead.remove("e");
				lookahead.addAll(follow.get(str[i]));
			}else if(lookahead.contains("e")){ // 마지막 전까지는 퍼스트
				lookahead.remove("e");
				lookahead.addAll(first.get(str[i]));
			}	
		}
		
		HashSet<String> temp = new HashSet<String>(lookahead); // hashSet을 이용한 중복제거
		ArrayList<String> result = new ArrayList<String>(temp);
		
		return result;
	}

	public void procedureE() {
		if(resultLookahead("T","E'","E").contains(inputSymbol[index])) {
			procedureT();
			procedureEquote();
		}
	}
	
	public void procedureEquote() {
		if(resultLookahead("+","T","E'","E'").contains(inputSymbol[index])) {
			index++;
			procedureT();
			procedureEquote();
		}else if(resultLookahead("e","E'").contains(inputSymbol[index])) {
		}
	}
	
	public void procedureT() {
		if(resultLookahead("F","T'","T").contains(inputSymbol[index])) {
			procedureF();
			procedureTquote();
		}
	}
	
	public void procedureTquote() {
		if(resultLookahead("*","F","T'","T'").contains(inputSymbol[index])) {
			index++;
			procedureF();
			procedureTquote();
		}else if(resultLookahead("e","T'").contains(inputSymbol[index])) {
		
		}
	}
	
	public void procedureF() {
		if(resultLookahead("(","E",")","F").contains(inputSymbol[index])) {
			index++;
			procedureE();
		}else if(resultLookahead("id","F").contains(inputSymbol[index])) {
			index++;
		}
	}
}

package HW04;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Grammar {
	private HashMap<String, ArrayList<String>> table = new HashMap<String, ArrayList<String>>();
	private HashMap<String, ArrayList<String>> first = new HashMap<>();
	private HashMap<String, ArrayList<String>> follow = new HashMap<>();
	public ArrayList<String> nonTerminal = new ArrayList<>();
	public ArrayList<String> terminal = new ArrayList<>();
	private String firstSymbol;
	boolean firstCheck = false;
	
	public HashMap<String, ArrayList<String>> getTable() {
		return table;
	}

	public void AddRule(String nonterminal, String rule) {
		if(!firstCheck) {
			firstCheck = true;
			firstSymbol = nonterminal;			
		}
		if (table.get(nonterminal) == null) {
			ArrayList<String> list = new ArrayList<String>();
			list.add(rule);
			table.put(nonterminal, list);
		} else {
			table.get(nonterminal).add(rule);
		}
	}

	public String getRule(String nonTerminal, int index) {
		return table.get(nonTerminal).get(index);
	}

	public ArrayList<String> getRules(String nonTerminal) {
		return table.get(nonTerminal);
	}

	public HashMap<String, ArrayList<String>> findFirst() { // 문법을 통해 각 터미널의 퍼스트를 출력
		divideGrammar();
		ArrayList<String> remainder_nonterminal = new ArrayList<>(); // 고려해야 되는 nontermianl모으기
		for (String lhs : table.keySet()) { // rhs에 첫번째로 terminal이 나오는 경우
			ArrayList<String> first_list = new ArrayList<String>(); // first terminal을 모아서 넣기 위한 list
			for (String rhs : table.get(lhs)) {
				String rhs_f = String.valueOf(rhs.charAt(0));
				int index = 1;
				while (!nonTerminal.contains(rhs_f) && index < rhs.length() && rhs.charAt(index) > 96 && rhs.charAt(index) < 123) {
					rhs_f += rhs.charAt(index++);
				} // terminal이 한글자가 아닌 단어인 경우 
				if (terminal.contains(rhs_f)) { // terminal이면
					first_list.add(rhs_f);
				} else { // rhs의 first가 terminal이 아니면
					remainder_nonterminal.add(lhs); // 다시 고려해야 하므로 list에 넣기
				}
			}
			first.put(lhs, first_list);
		}
		
		HashMap<String, ArrayList<String>> first_result = new HashMap<String, ArrayList<String>>();
		int count = 0; // 반복여부체크
		while (count < remainder_nonterminal.size()) {
			count = 0; // 반복여부 체크하기위해 다시 초기화
			for (String lhs : remainder_nonterminal) {
				for (String rhs : table.get(lhs)) {
					if(!terminal.contains(rhs)) {
					ArrayList<String> list2 = new ArrayList<String>(); // terminal을 모아서 넣기 위한 list
					list2.addAll(first.get(lhs));
					int i = 0;
					while (first.get(String.valueOf(rhs.charAt(i))) != null && first.get(String.valueOf(rhs.charAt(i))).contains('e')) {
						list2.addAll(first.get(String.valueOf(rhs.charAt(i++))));
						list2.remove("e");
					}
					list2.addAll(first.get(String.valueOf(rhs.charAt(i))));
					HashSet<String> temp = new HashSet<String>(list2); // hashSet을 이용한 중복제거
					ArrayList<String> result = new ArrayList<String>(temp);
					
					if (result.equals(first.get(lhs))) count++; // first값이 변하지 않으면 count 올리기
					first_result.put(lhs, result); // first값 넣기
				}
				}
			}
			first.putAll(first_result);
		}
		printFirst();
		return first;
	}
	
	public void printFirst() {
		for (String lhs : first.keySet()) {
			System.out.println("first(" + lhs + ") : {"+ first.get(lhs)+"}");
		}
	}
	
	public HashMap<String, ArrayList<String>> findFollow() { // 문법을 통해 각 터미널의 팔로우를 출력	
		ArrayList<String> remainder_nonterminal = new ArrayList<>(); // 고려해야 되는 nontermianl모으기
		ArrayList<String> arr_rhs = new ArrayList<String>(); // rhs부분들만 모아놓은 array
		for(String lhs : table.keySet()) {
			for(String rhs : table.get(lhs)) {
				arr_rhs.add(rhs);
			}
		}
		for (String lhs : table.keySet()) { // 처음에 바로 follow를 구할수 있는 경우
			ArrayList<String> follow_list = new ArrayList<String>(); // follow terminal을 모아서 넣기 위한 list
			if(lhs.equals(firstSymbol)) { // 만약 시작심볼이면 $을 넣고 시작
				follow_list.add("$");
			}
			for (String rhs : arr_rhs) {
				int rhs_Index = rhs.indexOf(lhs);
				if(lhs.length() == 1 && rhs_Index+1 < rhs.length() &&rhs.charAt(rhs_Index+1) == 39) {
					rhs_Index = -1;
				}
				if(rhs_Index != -1) {
					if(lhs.length() > 1 && rhs_Index+2<rhs.length() || lhs.length() == 1 && rhs_Index+1<rhs.length()) { // 뒤에 nonterminal이나 terminal이 존재
						String lhs_follow;
						if(rhs_Index+2 < rhs.length() &&rhs.charAt(rhs_Index+2) == 39) {
							lhs_follow = rhs.substring(rhs_Index+1,rhs_Index+3);
						}else {
							lhs_follow = String.valueOf(rhs.charAt(rhs_Index+1));
						}
						if(terminal.contains(lhs_follow)) { // lhs의 follow가 terminal인 경우
							follow_list.add(lhs_follow);
						}else { // nonterminal인 경우
							if(first.get(lhs_follow).contains("e")) { // null을 포함한 경우
								for(String item : first.get(lhs_follow)) { // null을 제외하고 followlist에 넣음
									if(!item.equals("e")) {
										follow_list.add(item);
									}else {  // null이 존재하므로 다시 고려해야함
										remainder_nonterminal.add(lhs);
									}
								}
							}else { // null을 포함하지 않은 경우
								follow_list.addAll(first.get(lhs_follow));
							}
						}
					}else {
						remainder_nonterminal.add(lhs); // 뒤가 비어있으므로  다시 고려해야함
					}
				}
			}
			HashSet<String> temp = new HashSet<String>(follow_list); // hashSet을 이용한 중복제거
			ArrayList<String> result = new ArrayList<String>(temp);
			follow.put(lhs, result);
		}
		
		HashSet<String> temp = new HashSet<String>(remainder_nonterminal); // hashSet을 이용한 중복제거
		ArrayList<String> remainder = new ArrayList<String>(temp);
		
		HashMap<String, ArrayList<String>> follow_result = new HashMap<String, ArrayList<String>>();
		int count = 0; // 반복여부 체크
		while (count < remainder_nonterminal.size()) {
			count = 0; 
			for(String lhs : remainder) {
				for (String lhskey : getLhs(lhs)) { // getLhs로 lhs속해있는 key 뽑아옴
					ArrayList<String> follow_list2 = new ArrayList<String>(); // terminal을 모아서 넣기 위한 list
					follow_list2.addAll(follow.get(lhs));
					follow_list2.addAll(follow.get(lhskey));
					HashSet<String> temp2 = new HashSet<String>(follow_list2); // hashSet을 이용한 중복제거
					ArrayList<String> result = new ArrayList<String>(temp2);
					if (result.equals(follow.get(lhs))) count++; // first값이 변하지 않으면 count 올리기
					follow_result.put(lhs, result);
				}
			}
			follow.putAll(follow_result);
		}
		printFollow();	
		return follow;
	}
	
	public void printFollow() {
		for (String lhs : follow.keySet()) {
			System.out.println("follow(" + lhs + ") : {"+ follow.get(lhs)+"}");
		}
	}
	
	public ArrayList<String> getLhs(String value) { // value로 key값을 포함하고 있는 lhs찾기
		ArrayList<String> result = new ArrayList<String>(); 
		for(String lhs : table.keySet()) {
			for(String rhs : table.get(lhs)) {
				int rhs_Index = rhs.indexOf(value);
				if(value.length() == 1 && rhs_Index+1 < rhs.length() &&rhs.charAt(rhs_Index+1) == 39) {
					rhs_Index = -1;
				}
				if(rhs_Index != -1) {
					for(String s1:table.keySet()) {
						for(String s2:table.get(s1)) {
							if(s2.equals(rhs)) result.add(s1);
						}
					}
			   }
		}
		}
		return result;
	}
	
	public void divideGrammar() { // terminal과 nonterminal로 구분
		for (String lhs : table.keySet()) {
			nonTerminal.add(lhs); // nonterminal배열에 추가
			for (String rhs : table.get(lhs)) {
				for (int i = 0; i < rhs.length(); i++) {
					if ((rhs.charAt(i) < 65 || rhs.charAt(i) > 90) && rhs.charAt(i) != 39) { // 대문자가 아닌 경우는 terminal
						if (rhs.charAt(i) > 96 && rhs.charAt(i) < 123) { // 소문자인 경우 단어인 경우에는 붙여서 넣어야함
							String result = String.valueOf(rhs.charAt(i));
							int index = i + 1;
							while (index < rhs.length() && rhs.charAt(index) > 96 && rhs.charAt(index) < 123) {
								result += rhs.charAt(index++);
								i = index;
							}
							if (!terminal.contains(result))
								terminal.add(result); // terminal배열에 추가
						} else if (!terminal.contains(String.valueOf(rhs.charAt(i)))) { // 소문자가 아닌 terminal
							terminal.add(String.valueOf(rhs.charAt(i))); // terminal배열에 추가
						}
					}
				}
			}
		}
	}

	public void viewGrammar() {
		int index = 1;
		for (String lhs : table.keySet()) {
			for (String rhs : table.get(lhs)) {
				System.out.println((index++) + "." + lhs + " ->  " + rhs);
			}
		}
	}
}

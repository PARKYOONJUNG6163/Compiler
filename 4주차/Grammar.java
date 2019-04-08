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

	public HashMap<String, ArrayList<String>> findFirst() { // ������ ���� �� �͹̳��� �۽�Ʈ�� ���
		divideGrammar();
		ArrayList<String> remainder_nonterminal = new ArrayList<>(); // ����ؾ� �Ǵ� nontermianl������
		for (String lhs : table.keySet()) { // rhs�� ù��°�� terminal�� ������ ���
			ArrayList<String> first_list = new ArrayList<String>(); // first terminal�� ��Ƽ� �ֱ� ���� list
			for (String rhs : table.get(lhs)) {
				String rhs_f = String.valueOf(rhs.charAt(0));
				int index = 1;
				while (!nonTerminal.contains(rhs_f) && index < rhs.length() && rhs.charAt(index) > 96 && rhs.charAt(index) < 123) {
					rhs_f += rhs.charAt(index++);
				} // terminal�� �ѱ��ڰ� �ƴ� �ܾ��� ��� 
				if (terminal.contains(rhs_f)) { // terminal�̸�
					first_list.add(rhs_f);
				} else { // rhs�� first�� terminal�� �ƴϸ�
					remainder_nonterminal.add(lhs); // �ٽ� ����ؾ� �ϹǷ� list�� �ֱ�
				}
			}
			first.put(lhs, first_list);
		}
		
		HashMap<String, ArrayList<String>> first_result = new HashMap<String, ArrayList<String>>();
		int count = 0; // �ݺ�����üũ
		while (count < remainder_nonterminal.size()) {
			count = 0; // �ݺ����� üũ�ϱ����� �ٽ� �ʱ�ȭ
			for (String lhs : remainder_nonterminal) {
				for (String rhs : table.get(lhs)) {
					if(!terminal.contains(rhs)) {
					ArrayList<String> list2 = new ArrayList<String>(); // terminal�� ��Ƽ� �ֱ� ���� list
					list2.addAll(first.get(lhs));
					int i = 0;
					while (first.get(String.valueOf(rhs.charAt(i))) != null && first.get(String.valueOf(rhs.charAt(i))).contains('e')) {
						list2.addAll(first.get(String.valueOf(rhs.charAt(i++))));
						list2.remove("e");
					}
					list2.addAll(first.get(String.valueOf(rhs.charAt(i))));
					HashSet<String> temp = new HashSet<String>(list2); // hashSet�� �̿��� �ߺ�����
					ArrayList<String> result = new ArrayList<String>(temp);
					
					if (result.equals(first.get(lhs))) count++; // first���� ������ ������ count �ø���
					first_result.put(lhs, result); // first�� �ֱ�
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
	
	public HashMap<String, ArrayList<String>> findFollow() { // ������ ���� �� �͹̳��� �ȷο츦 ���	
		ArrayList<String> remainder_nonterminal = new ArrayList<>(); // ����ؾ� �Ǵ� nontermianl������
		ArrayList<String> arr_rhs = new ArrayList<String>(); // rhs�κе鸸 ��Ƴ��� array
		for(String lhs : table.keySet()) {
			for(String rhs : table.get(lhs)) {
				arr_rhs.add(rhs);
			}
		}
		for (String lhs : table.keySet()) { // ó���� �ٷ� follow�� ���Ҽ� �ִ� ���
			ArrayList<String> follow_list = new ArrayList<String>(); // follow terminal�� ��Ƽ� �ֱ� ���� list
			if(lhs.equals(firstSymbol)) { // ���� ���۽ɺ��̸� $�� �ְ� ����
				follow_list.add("$");
			}
			for (String rhs : arr_rhs) {
				int rhs_Index = rhs.indexOf(lhs);
				if(lhs.length() == 1 && rhs_Index+1 < rhs.length() &&rhs.charAt(rhs_Index+1) == 39) {
					rhs_Index = -1;
				}
				if(rhs_Index != -1) {
					if(lhs.length() > 1 && rhs_Index+2<rhs.length() || lhs.length() == 1 && rhs_Index+1<rhs.length()) { // �ڿ� nonterminal�̳� terminal�� ����
						String lhs_follow;
						if(rhs_Index+2 < rhs.length() &&rhs.charAt(rhs_Index+2) == 39) {
							lhs_follow = rhs.substring(rhs_Index+1,rhs_Index+3);
						}else {
							lhs_follow = String.valueOf(rhs.charAt(rhs_Index+1));
						}
						if(terminal.contains(lhs_follow)) { // lhs�� follow�� terminal�� ���
							follow_list.add(lhs_follow);
						}else { // nonterminal�� ���
							if(first.get(lhs_follow).contains("e")) { // null�� ������ ���
								for(String item : first.get(lhs_follow)) { // null�� �����ϰ� followlist�� ����
									if(!item.equals("e")) {
										follow_list.add(item);
									}else {  // null�� �����ϹǷ� �ٽ� ����ؾ���
										remainder_nonterminal.add(lhs);
									}
								}
							}else { // null�� �������� ���� ���
								follow_list.addAll(first.get(lhs_follow));
							}
						}
					}else {
						remainder_nonterminal.add(lhs); // �ڰ� ��������Ƿ�  �ٽ� ����ؾ���
					}
				}
			}
			HashSet<String> temp = new HashSet<String>(follow_list); // hashSet�� �̿��� �ߺ�����
			ArrayList<String> result = new ArrayList<String>(temp);
			follow.put(lhs, result);
		}
		
		HashSet<String> temp = new HashSet<String>(remainder_nonterminal); // hashSet�� �̿��� �ߺ�����
		ArrayList<String> remainder = new ArrayList<String>(temp);
		
		HashMap<String, ArrayList<String>> follow_result = new HashMap<String, ArrayList<String>>();
		int count = 0; // �ݺ����� üũ
		while (count < remainder_nonterminal.size()) {
			count = 0; 
			for(String lhs : remainder) {
				for (String lhskey : getLhs(lhs)) { // getLhs�� lhs�����ִ� key �̾ƿ�
					ArrayList<String> follow_list2 = new ArrayList<String>(); // terminal�� ��Ƽ� �ֱ� ���� list
					follow_list2.addAll(follow.get(lhs));
					follow_list2.addAll(follow.get(lhskey));
					HashSet<String> temp2 = new HashSet<String>(follow_list2); // hashSet�� �̿��� �ߺ�����
					ArrayList<String> result = new ArrayList<String>(temp2);
					if (result.equals(follow.get(lhs))) count++; // first���� ������ ������ count �ø���
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
	
	public ArrayList<String> getLhs(String value) { // value�� key���� �����ϰ� �ִ� lhsã��
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
	
	public void divideGrammar() { // terminal�� nonterminal�� ����
		for (String lhs : table.keySet()) {
			nonTerminal.add(lhs); // nonterminal�迭�� �߰�
			for (String rhs : table.get(lhs)) {
				for (int i = 0; i < rhs.length(); i++) {
					if ((rhs.charAt(i) < 65 || rhs.charAt(i) > 90) && rhs.charAt(i) != 39) { // �빮�ڰ� �ƴ� ���� terminal
						if (rhs.charAt(i) > 96 && rhs.charAt(i) < 123) { // �ҹ����� ��� �ܾ��� ��쿡�� �ٿ��� �־����
							String result = String.valueOf(rhs.charAt(i));
							int index = i + 1;
							while (index < rhs.length() && rhs.charAt(index) > 96 && rhs.charAt(index) < 123) {
								result += rhs.charAt(index++);
								i = index;
							}
							if (!terminal.contains(result))
								terminal.add(result); // terminal�迭�� �߰�
						} else if (!terminal.contains(String.valueOf(rhs.charAt(i)))) { // �ҹ��ڰ� �ƴ� terminal
							terminal.add(String.valueOf(rhs.charAt(i))); // terminal�迭�� �߰�
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

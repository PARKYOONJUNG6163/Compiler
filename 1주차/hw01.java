package HW01;
// 201602001 ������
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
//input ���� ��� : C://Users//user//Desktop//test.hoo
//output ���� ��� : C://Users//user//Desktop//test.c
public class hw01 {
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		FileReader fr; // ���� ��� ���� ����
		String line = "";
		List<String> list = new ArrayList<String>(); 
		try {
			// hoo ���� ����
			FileWriter fw = new FileWriter("C://Users//user//Desktop//test.hoo"); // ������ ������ ��� ����
			BufferedWriter bw = new BufferedWriter(fw); 
			bw.write("[abc]:\n[def][e/f]:\n[ghi][h/i]: (2) print");
			bw.close();
			
			// ���� �о�ͼ� ����Ʈ�� �߰��ϴ� �κ�
			fr = new FileReader("C://Users//user//Desktop//test.hoo"); // ������ �о�� ��� ����												// ����
			BufferedReader br = new BufferedReader(fr); // ���� ������ ��ü br
			while((line = br.readLine()) != null) { // ���� ������ ������� ������
				StringTokenizer st = new StringTokenizer(line," "); // " "�� �������� �о�� line�� �����ش�
				while(st.hasMoreTokens()) { // token�� �� �̻� ���� ����
					list.add(st.nextToken()); // ����Ʈ�� �߰�
				}
			}
			fr.close();
			
			// �о�� ���� ó��
			fw = new FileWriter("C://Users//user//Desktop//test.c"); // ������ ������ ��� ����
			bw = new BufferedWriter(fw); 
			bw.write("#include <stdio.h>"); bw.newLine();
			bw.write("int main(){"); bw.newLine();
			int i=0;
			int number; // �ݺ� Ƚ��
			boolean newLine = true; // Ƚ�� ����� ��� �� ���ο� �����ΰ�?
			String result="";
			String tempStr="";
			while(i<list.size()) {
				int count=0;
				String str = list.get(i++);
				for(int j=0;j<str.length();j++) {
					if(str.charAt(j) == '[') count++;
				}
				if(count>2) throw new Exception("������ �ִ� �����Դϴ�."); // ���� ó��
				int parenthesisIndex=str.indexOf('[',1); // '[' �ε���
				int slashIndex = str.indexOf('/',parenthesisIndex); // '/' �ε���
				if(str.endsWith(":")) { // ���ڿ��� �ش��ϸ�
					tempStr = str.substring(1, str.indexOf(']')); // ���ڿ� �̾Ƴ���	
					if(parenthesisIndex != -1) { // '[' �� �ΰ���
						if(slashIndex != -1) { // '/' �� �����ϸ�
							tempStr = tempStr.replace(str.charAt(slashIndex+1),str.charAt(slashIndex-1)); // ġȯ
						}else { // '/'�� ������ �빮�� �ƴϸ� �ҹ��ڷ� ����� ����� �̾� ���̱�
							tempStr = (str.charAt(parenthesisIndex+1) == 'U') ? tempStr.toUpperCase():(str.charAt(parenthesisIndex+1) == 'L') ?tempStr.toLowerCase():tempStr;
						}
					}else if(tempStr.equals("")) {
						tempStr = "\\"+"n";
					}
					result += tempStr;
					if(list.get(i).endsWith(":")) newLine = false; // ������ �� ���ڿ��̸� ���ο� ������ �ƴϹǷ� false��
				}else { // ��ɾ �ش��ϸ�
					if(str.equals("print")) { // print�̸� ���
						bw.write("printf(\"%s\",\""+result+"\");"); bw.newLine();
						result = "";
						newLine=true;
					}else if(str.equals("ignore")) { // ignore�̸� ����
						result = "";
						newLine=true;
					}else if(str.charAt(0) == '(') { // (�ڿ���)�̸� �ڿ�����ŭ ���ڿ� �ݺ�
						String temp = result;
						number = Integer.parseInt(str.substring(1,str.length()-1)); // String������ ������ �ִ� ���ڸ� string�� int�� ��ȯ
						while(number != 1) { // number�� 1�� �� ������ �ݺ��Ͽ� ���ڿ��� ����� �̾� ���̱�
							result = newLine? result+tempStr:result+temp;
							number--;
						}
					}
				}
			}
			bw.write("return 0;"); bw.newLine();
			bw.write("}"); bw.newLine();
			bw.close();
			
			// c�ڵ� ���� ����ϱ�
			System.out.println("���");
			fr = new FileReader("C://Users//user//Desktop//test.c"); // ������ �о�� ��� ����												// ����
			br = new BufferedReader(fr); // ���� ������ ��ü br
			while((line = br.readLine()) != null) { // ���� ������ ������� ������
				System.out.println(line);
			}
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		} // ���ܻ�Ȳ ó��
	}
}

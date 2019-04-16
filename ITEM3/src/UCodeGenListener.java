
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.regex.Pattern;

import org.antlr.v4.runtime.tree.ParseTreeProperty;

//201602001 ������
public class UCodeGenListener extends MiniGoBaseListener {
	HashMap<String, Variable> globalDecl = new HashMap<String, Variable>(); // ���� ���� ��Ƴ��� �ؽ� ��
	HashMap<String, Variable> localDecl = new HashMap<String, Variable>(); // ���� ���� ��Ƴ��� �ؽ� ��
	HashMap<String, ArrayList<String>> funReturnType = new HashMap<String, ArrayList<String>>(); // return Ÿ���� �Լ���� �Բ� ��Ƴ���
	HashMap<String, ArrayList<String>> funArgsType = new HashMap<String, ArrayList<String>>(); // args Ÿ���� �Լ���� �Բ� ��Ƴ���
	HashMap<String, ArrayList<String>> funParamsType = new HashMap<String, ArrayList<String>>(); // params Ÿ���� �Լ���� �Բ� ��Ƴ���
	ParseTreeProperty<String> newTexts = new ParseTreeProperty<String>();
	Stack<Integer> stack = new Stack<Integer>();
	int whereJump = 0; // ���� �����ؾ��ϴ°�?
	int globalCount = 1;
	int localCount = 1;

	public String firstSpace() { // ó�� ��ɾ�� main���� 7�� ����
		return "       ";
	}

	public String jumpSpace() { // jump�Ҷ� 8�� ����
		return "        ";
	}

	public String Space() { // ó�� ���Ŀ��� 11�� ����
		return "           ";
	}

	public void createFile(String result) {
		try { // ����� ���Ϸ� ����
			FileWriter fw = new FileWriter("C://Users//user//Desktop//item3.uco"); // ������ ������ ��� ����
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(result);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		} // ���ܻ�Ȳ ó��
	}

	public void exitProgram(MiniGoParser.ProgramContext ctx) { // ���α׷� �����
		String result = "";
		for (int i = 0; i < ctx.decl().size(); i++) {
			result += newTexts.get(ctx.decl(i));
		}

		result += "\n" + this.Space() + "bgn " + (globalCount - 1);
		result += "\n" + this.Space() + "ldp";
		result += "\n" + this.Space() + "call main";
		result += "\n" + this.Space() + "end";
		newTexts.put(ctx, result);
		System.out.println(newTexts.get(ctx));
		createFile(newTexts.get(ctx));
	}

	public void exitDecl(MiniGoParser.DeclContext ctx) {
		String result = "";
		if (ctx.var_decl() != null) // decl�� var_decl�� ���
			result += newTexts.get(ctx.var_decl());
		else // decl�� fun_decl�� ���
			result += newTexts.get(ctx.fun_decl());

		newTexts.put(ctx, result);
	}

	public void exitVar_decl(MiniGoParser.Var_declContext ctx) { // ���� �κ�
		String result = "";
		if (ctx.getChildCount() == 3) { // VAR IDENT type_spec
			result += "\n" + this.Space() + "sym 1 " + globalCount + " 1";
			globalDecl.put(ctx.getChild(1).getText(),
					new Variable(ctx.type_spec().getText(), null, "2", globalCount + "", "1"));
			globalCount += 1;
		} else if (ctx.getChildCount() == 5) { // VAR IDENT ',' IDENT type_spec
			result += "\n" + this.Space() + "sym 1 " + globalCount + " 1";
			globalDecl.put(ctx.getChild(1).getText(),
					new Variable(ctx.type_spec().getText(), null, "2", globalCount + "", "1"));
			globalCount += 1;
		} else { // VAR IDENT '[' LITERAL ']' type_spec
			result += "\n" + this.Space() + "sym 1 " + globalCount + " " + ctx.getChild(3).getText().trim();
			globalDecl.put(ctx.getChild(1).getText(), new Variable(ctx.type_spec().getText(), null, "2",
					globalCount + "", ctx.getChild(3).getText().trim() + ""));
			globalCount += Integer.parseInt(ctx.getChild(3).getText().trim());
		}
		newTexts.put(ctx, result);
	}

	public void enterFun_decl(MiniGoParser.Fun_declContext ctx) { // �Լ� ���ۺκп��� localCount�� �ʱ�ȭ���־����
		localCount = 1; // �������� �ʱ�ȭ
		localDecl = new HashMap<String, Variable>(); // �������� �ʱ�ȭ
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < ctx.type_spec().size(); i++) {
			list.add(ctx.type_spec(i).getText());
		}
		funReturnType.put(ctx.getChild(1).getText(), list);
	}

	public void exitFun_decl(MiniGoParser.Fun_declContext ctx) { // �Լ� �κ�
		String result = "\n";
		String space = "";
		String fun_name = ctx.getChild(1).getText();
		for (int i = 0; i < 11 - fun_name.length(); i++) { // �Լ� ��ɾ 12��° �;��ϹǷ� space�� ������
			space += " ";
		}
		result += ctx.getChild(1).getText() + space + "proc " + (globalCount + localCount) + " 2 2"; // �Լ������ �̸�

		if (ctx.getChildCount() == 7) { // FUNC IDENT '(' params ')' type_spec compound_stmt
			result += newTexts.get(ctx.params());
			result += newTexts.get(ctx.compound_stmt());
		} else { // FUNC IDENT '(' params ')' '(' type_spec ',' type_spec ')' compound_stmt
			result += newTexts.get(ctx.params());
			result += newTexts.get(ctx.compound_stmt());
		}
		// ���� Ÿ�Կ� ���� ret���� retv���� ����
		if (ctx.type_spec().equals("void") || ctx.type_spec() == null) {  // ���ϰ��� ���� ret�� ���
			result += "\n" + this.Space() + "ret";
		} else {  // ���ϰ��� �ִ� retv�� ��� 
			result += "\n" + this.Space() + "retv";
		}
		result += "\n" + this.Space() + "end";
		newTexts.put(ctx, result);
	}

	public void exitLocal_decl(MiniGoParser.Local_declContext ctx) { // ���� ���� �κ�
		String result = "";
		if (ctx.getChildCount() == 3) { // VAR IDENT type_spec
			result += "\n" + this.Space() + "sym 2 " + localCount + " 1";
			localDecl.put(ctx.getChild(1).getText(),
					new Variable(ctx.type_spec().getText(), null, "2", localCount + "", "1"));
			localCount += 1;
		} else { // VAR IDENT '[' LITERAL ']' type_spec
			result += "\n" + this.Space() + "sym 2 " + localCount + " 1";
			localDecl.put(ctx.getChild(1).getText(), new Variable(ctx.type_spec().getText(), null, "2", localCount + "",
					ctx.getChild(3).getText().replace(" ", "") + ""));
			localCount += Integer.parseInt(ctx.getChild(3).getText().replace(" ", ""));
		}
		newTexts.put(ctx, result);
	}

	public void exitStmt(MiniGoParser.StmtContext ctx) {
		String result = "";
		if (ctx.getChild(0) == ctx.expr_stmt()) { // expr_stmt
			result += newTexts.get(ctx.expr_stmt().getChild(0));
		} else if (ctx.getChild(0) == ctx.compound_stmt()) { // compound_stmt
			result += newTexts.get(ctx.compound_stmt());
		} else if (ctx.getChild(0) == ctx.assign_stmt()) { // assign_stmt
			result += newTexts.get(ctx.assign_stmt());
		} else if (ctx.getChild(0) == ctx.if_stmt()) { // if_stmt
			result += newTexts.get(ctx.if_stmt());
		} else if (ctx.getChild(0) == ctx.for_stmt()) { // for_stmt
			result += newTexts.get(ctx.for_stmt()) + "\n$$" + ++whereJump + this.jumpSpace() + "nop";
		} else { // return_stmt
			result += newTexts.get(ctx.return_stmt());
		}
		newTexts.put(ctx, result);
	}

	boolean isBinaryOperation(MiniGoParser.ExprContext ctx) {
		return ctx.getChildCount() == 3 && ctx.getChild(1) != ctx.expr(0);
	}

	String whatBinaryOperation(String op) {
		String result = "";
		switch (op) {
		case "+":
			result = "add";
			break;
		case "-":
			result = "sub";
			break;
		case "*":
			result = "mul";
			break;
		case "/":
			result = "div";
			break;
		case "%":
			result = "mod";
			break;
		case ">":
			result = "gt";
			break;
		case "<":
			result = "lt";
			break;
		case ">=":
			result = "ge";
			break;
		case "<=":
			result = "le";
			break;
		case "==":
			result = "eq";
			break;
		case "!=":
			result = "ne";
			break;
		case "&&":
			result = "and";
			break;
		case "||":
			result = "or";
			break;
		case "++":
			result = "inc";
			break;
		case "--":
			result = "dec";
			break;
		}
		return result;
	}

	public boolean opVarChecking(String type1, String type2) { // op������ ���� �� ���� Ÿ�� üũ
		if (type1.equals(type2)) {
			System.out.println("�� Ÿ���� �����ϴ�.");
			return true;
		}
		System.out.println("�� Ÿ���� ���� �ʽ��ϴ�.");
		System.exit(0);
		return false;
	}

	public void exitExpr(MiniGoParser.ExprContext ctx) {
		String op = null;
		String result = "";
		if (ctx.getChildCount() == 1) { // (LITERAL|IDENT)
			result += ctx.getChild(0).getText().replace("\r", "");
		}

		if (isBinaryOperation(ctx)) { // +,-,=���� ��ȣ�� �������� ���
			if (ctx.expr(1) == null) { // IDENT = expr (A=3)
				Variable s1 = null;
				Variable s2 = null;
				boolean check = false;
				// IDENTó���κ�
				for (int i = 0; i < ctx.expr().size(); i++) {
					if (ctx.getChild(0).getText().equals(ctx.expr(i).getText()))
						check = true;
				}
				if (globalDecl.containsKey(ctx.getChild(0).getText())) { // IDENT�� ��������
					s1 = globalDecl.get(ctx.getChild(0).getText());
				} else { // IDENT�� ��������
					s1 = localDecl.get(ctx.getChild(0).getText());
				}
				if (check) {
					result += "\n" + this.Space() + "lod " + s1.getBase() + " " + s1.getOffset();
				}
				// expró���κ�
				String value;
				if (globalDecl.containsKey(ctx.expr(0).getText())) { // expr�� ��������
					s2 = globalDecl.get(ctx.expr(0).getText());
					result += "\n" + this.Space() + "lod " + s2.getBase() + " " + s2.getOffset();
					value = s2.getValue();
				} else if (localDecl.containsKey(ctx.expr(0).getText())) { // expr�� ��������
					s2 = localDecl.get(ctx.expr(0).getText());
					result += "\n" + this.Space() + "lod " + s2.getBase() + " " + s2.getOffset();
					value = s2.getValue();
				} else { // ���
					result += "\n" + this.Space() + "ldc " + newTexts.get(ctx.expr(0));
					value = newTexts.get(ctx.expr(0));
				}
				
				if(assignTypeChecking(s1.getType(), value)) {
					// �� ����
					if (globalDecl.containsKey(ctx.getChild(0).getText())) { // IDENT�� ��������
						globalDecl.put(ctx.getChild(0).getText(),
								new Variable(s1.getType(), value, s1.getBase(), s1.getOffset(), s1.getSize()));
					} else { // IDENT�� ��������
						localDecl.put(ctx.getChild(0).getText(),
								new Variable(s1.getType(), value, s1.getBase(), s1.getOffset(), s1.getSize()));
					}
					result += "\n" + this.Space() + "str " + s1.getBase() + " " + s1.getOffset();					
				}
				
			} else { // �� �κ��� expr�� ���(expr + expr)
				Variable s1 = null;
				Variable s2 = null;
				// ù��° expr���ϱ�
				op = ctx.getChild(1).getText();
				if (globalDecl.containsKey(ctx.expr(0).getText())) { // ��������
					s1 = globalDecl.get(ctx.expr(0).getText());
					result += "\n" + this.Space() + "lod " + s1.getBase() + " " + s1.getOffset();
				} else if (localDecl.containsKey(ctx.expr(0).getText())) { // ��������
					s1 = localDecl.get(ctx.expr(0).getText());
					result += "\n" + this.Space() + "lod " + s1.getBase() + " " + s1.getOffset();
				} else { // ���
					result += "\n" + this.Space() + "ldc " + newTexts.get(ctx.expr(0));
				}
				// �ι�° expr���ϱ�
				if (globalDecl.containsKey(ctx.expr(1).getText())) { // ��������
					s2 = globalDecl.get(ctx.expr(1).getText());
					result += "\n" + this.Space() + "lod " + s2.getBase() + " " + s2.getOffset();
				} else if (localDecl.containsKey(ctx.expr(1).getText())) { // ��������
					s2 = localDecl.get(ctx.expr(1).getText());
					result += "\n" + this.Space() + "lod " + s2.getBase() + " " + s2.getOffset();
				} else { // ���
					result += "\n" + this.Space() + "ldc " + newTexts.get(ctx.expr(1));
				}
				if (opVarChecking(s1.getType(), s2.getType())) { // �� Ÿ���� ������ ��
					result += "\n" + this.Space() + this.whatBinaryOperation(op);
				}
			}
		} else if (ctx.getChildCount() == 2) { // ���� �����ڿ� �ǿ������� ��� (++x)
			Variable s1;
			op = ctx.getChild(0).getText(); // ���� ������
			if (globalDecl.containsKey(ctx.expr(0).getText())) { // expr�� ��������
				s1 = globalDecl.get(ctx.expr(0).getText());
			} else { // expr�� ��������
				s1 = localDecl.get(ctx.expr(0).getText());
			}
			result += "\n" + this.Space() + "lod " + s1.getBase() + " " + s1.getOffset();
			result += "\n" + this.Space() + this.whatBinaryOperation(op);
			result += "\n" + this.Space() + "str 2" + " " + s1.getOffset();
		} else if (ctx.getChildCount() == 3) { // ��ȣ�� �ִ� ��� '(' expr ')'
			Variable s1;
			if (globalDecl.containsKey(ctx.expr(0).getText())) { // expr�� ��������
				s1 = globalDecl.get(ctx.expr(0).getText());
			} else { // expr�� ��������
				s1 = localDecl.get(ctx.expr(0).getText());
			}
			result += "\n" + this.Space() + "lod " + s1.getBase() + " " + s1.getOffset();
		} else if (ctx.getChildCount() == 4) {
			if (ctx.expr(0) != null) { // IDENT '[' expr ']'
				Variable s1;
				// IDENT���ϱ�
				if (globalDecl.containsKey(ctx.getChild(0).getText())) { // IDENT ��������
					s1 = globalDecl.get(ctx.getChild(0).getText());
				} else { // IDENT ��������
					s1 = localDecl.get(ctx.getChild(0).getText());
				}
				result += "\n" + this.Space() + "ldc " + newTexts.get(ctx.expr(0));
				result += "\n" + this.Space() + "lda " + s1.getBase() + " " + s1.getOffset();
				result += "\n" + this.Space() + "add "; // �迭�� �ε��� ���ؼ� �ڸ� ã��
			} else { // IDENT '(' args ')'
				String funName = ctx.getChild(0).getText();
				boolean check = true;
				if (funParamsType.containsKey(funName)) {
					for (int i = 0; i < funParamsType.get(funName).size(); i++) {
						if (!assignTypeChecking(funParamsType.get(funName).get(i), funArgsType.get(funName).get(i))) {
							check = false;
						}
					}
					if (check) {
						result += "\n" + this.Space() + "ldp ";
						result += newTexts.get(ctx.args());
						result += "\n" + this.Space() + "call " + funName;
					}
				}
			}
		} else if (ctx.getChildCount() == 6) {
			if (ctx.getChild(0).getText().equals("fmt")) { // FMT '.' IDENT '(' args ')'
				result += "\n" + this.Space() + "ldp";
				result += newTexts.get(ctx.args());
				result += "\n" + this.Space() + "call write";
			} else { // IDENT '[' expr ']' '=' expr
				Variable s1;
				// IDENT ���ϱ�
				if (globalDecl.containsKey(ctx.getChild(0).getText())) { // IDENT�� ��������
					s1 = globalDecl.get(ctx.getChild(0).getText());
				} else { // IDENT�� ��������
					s1 = localDecl.get(ctx.getChild(0).getText());
				}
				result += "\n" + this.Space() + "lod " + s1.getBase() + " " + s1.getOffset();
				Variable s2;
				String value;
				Variable s3;
				// 1��° expr�� ���ϱ�
				if (globalDecl.containsKey(ctx.expr(0).getText())) { // expr�� ��������
					s2 = globalDecl.get(ctx.expr(0).getText());
					result += "\n" + this.Space() + "lod " + s2.getBase() + " " + s2.getOffset();
				} else if (localDecl.containsKey(ctx.expr(0).getText())) { // expr�� ��������
					s2 = localDecl.get(ctx.expr(0).getText());
					result += "\n" + this.Space() + "lod " + s2.getBase() + " " + s2.getOffset();
				} else { // ���
					result += "\n" + this.Space() + "ldc " + newTexts.get(ctx.expr(0));
				}
				result += "\n" + this.Space() + "lda " + s1.getBase() + " " + s1.getOffset();
				result += "\n" + this.Space() + "add "; // �迭�� �ε��� ���ؼ� �ڸ� ã��
				// 2��° expr�� ���ϱ�
				if (globalDecl.containsKey(ctx.expr(0).getText())) { // expr�� ��������
					s3 = globalDecl.get(ctx.expr(0).getText());
					result += "\n" + this.Space() + "lod " + s3.getBase() + " " + s3.getOffset();
					value = s3.getValue();
				} else if (localDecl.containsKey(ctx.expr(0).getText())) { // expr�� ��������
					s3 = localDecl.get(ctx.expr(0).getText());
					result += "\n" + this.Space() + "lod " + s3.getBase() + " " + s3.getOffset();
					value = s3.getValue();
				} else { // ���
					result += "\n" + this.Space() + "ldc " + newTexts.get(ctx.expr(1));
					value = newTexts.get(ctx.expr(1)).trim();
				}
				if (assignTypeChecking(s1.getType(), value)) { // typeüũ
					if (globalDecl.containsKey(ctx.getChild(0).getText())) { // IDENT�� ��������
						globalDecl.put(ctx.getChild(0).getText(),
								new Variable(s1.getType(), value, s1.getBase(), s1.getOffset(), s1.getSize()));
					} else { // IDENT�� ��������
						localDecl.put(ctx.getChild(0).getText(),
								new Variable(s1.getType(), value, s1.getBase(), s1.getOffset(), s1.getSize()));
					}
					result += "\n" + this.Space() + "sti";
				}
			}

		}
		newTexts.put(ctx, result);
	}

	public void exitCompound_stmt(MiniGoParser.Compound_stmtContext ctx) {
		String result = "";
		int localIndex = 0; // �������� ���� �ε���
		int stmtIndex = 0; // stmt���� �ε���
		for (int i = 1; i < ctx.getChildCount() - 1; i++) { // {�� }�� ���־�� �ϹǷ� 1���ͽ���, ���� -1���� �ݺ�
			if (ctx.local_decl().contains(ctx.getChild(i))) { // local_decl�� ���
				result += newTexts.get(ctx.local_decl(localIndex));
				localIndex++;
			} else { // compound_stmt�� ���
				result += newTexts.get(ctx.stmt(stmtIndex));
				stmtIndex++;
			}
		}
		newTexts.put(ctx, result);
	}

	public boolean TypeChecking(String pattern, String value) { // Ÿ�԰� ���� ������ �´��� üũ�Ͽ� ��� ����
		return Pattern.matches(pattern, value);
	}

	public boolean NumChecking(String type, String value) { // �ش��ϴ� Ÿ���� ������ �´��� Ȯ���Ͽ� ��� ����
		boolean result = false;
		switch (type) {
		case "int16": // short��
			short a = Short.parseShort(value);
			if (Short.MIN_VALUE <= a && Short.MAX_VALUE >= a)
				result = true;
			break;
		case "int32": // int��
			int b = Integer.parseInt(value);
			if (Integer.MIN_VALUE <= b && Integer.MAX_VALUE >= b)
				result = true;
			break;
		case "int64": // long��
			long c = Long.parseLong(value);
			if (Long.MIN_VALUE <= c && Long.MAX_VALUE >= c)
				result = true;
			break;
		case "float32": // float��
			float d = Float.parseFloat(value);
			if (Float.MIN_VALUE <= d && Float.MAX_VALUE >= d)
				result = true;
			break;
		case "float64": // double��
			double e = Double.parseDouble(value);
			if (Double.parseDouble(value) <= e && Double.MAX_VALUE >= e)
				result = true;
			break;
		}
		return result;
	}

	public String whatPattern(String type) { // Ÿ�Կ� ���� ������ ����
		String pattern = "";
		if (type.equals("int16") || type.equals("int32") || type.equals("int64")) {
			pattern = "^[0-9]+$";
		} else if (type.equals("float32") || type.equals("float64")) {
			pattern = "^[0-9]+\\.?[0-9]+$";
		}
		return pattern;
	}

	public boolean assignTypeChecking(String type, String value) { // Ÿ�԰� �� ���� üũ
		if (TypeChecking(whatPattern(type), value)) {
			if (NumChecking(type, value)) { // ������ ������ �ڷ��� ��
				System.out.println("����� type�� �ش��ϴ� ���� �½��ϴ�.");
				return true;
			} else {
				System.out.println("����� type�� �ش��ϴ� ���� �ƴմϴ�.");
				System.exit(0);
			}
		} else {
			System.out.println("����� type�� �ش��ϴ� ���� �ƴմϴ�.");
			System.exit(0);
		}
		return false;
	}

	public void exitAssign_stmt(MiniGoParser.Assign_stmtContext ctx) { // assign��
		String result = "";
		if (ctx.getChild(0) == ctx.VAR()) { // �� ���� var (���� ���� ����)
			if (ctx.getChildCount() == 9) { // VAR IDENT ',' IDENT type_spec '=' LITERAL ',' LITERAL
				for (int i = 0; i < 2; i++) {
					String type = ctx.type_spec().getText();
					String value = ctx.LITERAL(i).getText();
					if (assignTypeChecking(type, value)) { // type üũ
						result += "\n" + this.Space() + "sym 2 " + localCount + " 1"; // ù��° ���� ���� ������
						result += "\n" + this.Space() + "ldc " + value;
						result += "\n" + this.Space() + "str 2 " + localCount;
						localDecl.put(ctx.IDENT(i).getText(), new Variable(type, value, "2", localCount + "", "1"));
						localCount += 1; // �������� 1 ����
					}
				}
			} else if (ctx.getChildCount() == 5) { // VAR IDENT type_spec '=' expr
				String type = ctx.type_spec().getText();
				Variable s1 = null;
				String value;
				// expr�� ���ϱ�
				if (globalDecl.containsKey(ctx.expr(0).getText())) { // expr�� ��������
					s1 = globalDecl.get(ctx.expr(0).getText());
					value = s1.getValue();
				} else if (localDecl.containsKey(ctx.expr(0).getText())) { // expr�� ��������
					s1 = localDecl.get(ctx.expr(0).getText());
					value = s1.getValue();
				} else { // ���
					value = newTexts.get(ctx.expr(0));
				}
				if (assignTypeChecking(type, value)) { // typeüũ
					result += "\n" + this.Space() + "sym 2 " + localCount + " 1";
					if (s1 != null) {
						result += "\n" + this.Space() + "lod " + s1.getBase() + " " + s1.getOffset();
					} else {
						result += "\n" + this.Space() + "ldc " + newTexts.get(ctx.expr(0));
					}
					result += "\n" + this.Space() + "str 2 " + localCount;
					localDecl.put(ctx.IDENT(0).getText(), new Variable(type, value, "2", localCount + "", "1"));
					localCount += 1; // �������� 1 ����
				}

			}
		} else { // �� ���� IDENT (������ �̹� �Ǿ�����)
			Variable s1;
			// IDENT ���ϱ�
			if (globalDecl.containsKey(ctx.getChild(0).getText())) { // IDENT�� ��������
				s1 = globalDecl.get(ctx.getChild(0).getText());
			} else { // IDENT�� ��������
				s1 = localDecl.get(ctx.getChild(0).getText());
			}
			result += "\n" + this.Space() + "lod " + s1.getBase() + " " + s1.getOffset();
			Variable s2;
			String value;
			if (ctx.type_spec() != null) { // IDENT type_spec '=' expr
				String type = ctx.type_spec().getText();
				// expr�� ���ϱ�
				if (globalDecl.containsKey(ctx.expr(0).getText())) { // expr�� ��������
					s2 = globalDecl.get(ctx.expr(0).getText());
					result += "\n" + this.Space() + "lod " + s2.getBase() + " " + s2.getOffset();
					value = s2.getValue();
				} else if (localDecl.containsKey(ctx.expr(0).getText())) { // expr�� ��������
					s2 = localDecl.get(ctx.expr(0).getText());
					result += "\n" + this.Space() + "lod " + s2.getBase() + " " + s2.getOffset();
					value = s2.getValue();
				} else { // ���
					result += "\n" + this.Space() + "ldc " + newTexts.get(ctx.expr(0));
					value = newTexts.get(ctx.expr(0));
				}
				if (assignTypeChecking(type, value)) { // typeüũ
					result += "\n" + this.Space() + "str " + s1.getBase() + " " + s1.getOffset();
					if (globalDecl.containsKey(ctx.getChild(0).getText())) { // IDENT�� ��������
						globalDecl.put(ctx.IDENT(0).getText(),
								new Variable(type, value, s1.getBase(), s1.getOffset(), s1.getSize()));
					} else { // IDENT�� ��������
						localDecl.put(ctx.IDENT(0).getText(),
								new Variable(type, value, s1.getBase(), s1.getOffset(), s1.getSize()));
					}
				}
			} else { // IDENT '[' expr ']' '=' expr
				Variable s3;
				// 1��° expr�� ���ϱ�
				if (globalDecl.containsKey(ctx.expr(0).getText())) { // expr�� ��������
					s2 = globalDecl.get(ctx.expr(0).getText());
					result += "\n" + this.Space() + "lod " + s2.getBase() + " " + s2.getOffset();
				} else if (localDecl.containsKey(ctx.expr(0).getText())) { // expr�� ��������
					s2 = localDecl.get(ctx.expr(0).getText());
					result += "\n" + this.Space() + "lod " + s2.getBase() + " " + s2.getOffset();
				} else { // ���
					result += "\n" + this.Space() + "ldc " + newTexts.get(ctx.expr(0));
				}
				result += "\n" + this.Space() + "lda " + s1.getBase() + " " + s1.getOffset();
				result += "\n" + this.Space() + "add "; // �迭�� �ε��� ���ؼ� �ڸ� ã��
				// 2��° expr�� ���ϱ�
				if (globalDecl.containsKey(ctx.expr(0).getText())) { // expr�� ��������
					s3 = globalDecl.get(ctx.expr(0).getText());
					result += "\n" + this.Space() + "lod " + s3.getBase() + " " + s3.getOffset();
					value = s3.getValue();
				} else if (localDecl.containsKey(ctx.expr(0).getText())) { // expr�� ��������
					s3 = localDecl.get(ctx.expr(0).getText());
					result += "\n" + this.Space() + "lod " + s3.getBase() + " " + s3.getOffset();
					value = s3.getValue();
				} else { // ���
					result += "\n" + this.Space() + "ldc " + newTexts.get(ctx.expr(0));
					value = newTexts.get(ctx.expr(0));
				}
				if (assignTypeChecking(s1.getType(), value)) { // typeüũ
					if (globalDecl.containsKey(ctx.getChild(0).getText())) { // IDENT�� ��������
						globalDecl.put(ctx.IDENT(0).getText(),
								new Variable(s1.getType(), value, s1.getBase(), s1.getOffset(), s1.getSize()));
					} else { // IDENT�� ��������
						localDecl.put(ctx.IDENT(0).getText(),
								new Variable(s1.getType(), value, s1.getBase(), s1.getOffset(), s1.getSize()));
					}
					result += "\n" + this.Space() + "sti";
				}
			}
		}
		newTexts.put(ctx, result);
	}

	public void exitIf_stmt(MiniGoParser.If_stmtContext ctx) { // if��
		String result = ""; // IF expr compound_stmt
		if (whereJump == 0) {
			result += "\n$$" + whereJump++ + this.jumpSpace() + "nop";
		}
		result += newTexts.get(ctx.expr());
		result += "\n" + this.Space() + "fjp $$" + whereJump;
		result += newTexts.get(ctx.compound_stmt(0));
		stack.push(whereJump);
		if (ctx.ELSE() != null) { // IF expr compound_stmt ELSE compound_stmt
			result += "\n$$" + whereJump + this.jumpSpace();
			result += this.Space() + "fjp $$" + ++whereJump;
			result += newTexts.get(ctx.compound_stmt(0)); // stmt
		}
		newTexts.put(ctx, result);
	}

	public void exitFor_stmt(MiniGoParser.For_stmtContext ctx) { // for��
		String result = "";
		if (stack.isEmpty()) {
			result += "\n$$" + whereJump + this.jumpSpace() + "nop";
		} else {
			result += "\n$$" + stack.peek() + this.jumpSpace() + "nop";
			if (stack.pop() != (whereJump)) {
				result += "\n$$" + (whereJump) + this.jumpSpace() + "nop";
			}
		}
		result += newTexts.get(ctx.expr());
		result += "\n" + this.Space() + "fjp $$" + ++whereJump;
		result += newTexts.get(ctx.compound_stmt());
		result += "\n" + this.Space() + "ujp $$" + --whereJump;
		stack.push(whereJump);
		newTexts.put(ctx, result);
	}

	public void exitReturn_stmt(MiniGoParser.Return_stmtContext ctx) {
		String result = "";
		Variable s1;
		if (ctx.getChildCount() == 4) { // ���ϰ��� 2��
			String funName = ctx.expr(0).getParent().getParent().getParent().getParent().getChild(1).getText();
			if (funReturnType.containsKey(funName) && funReturnType.get(funName).size() < 2) {
				System.out.println("���� �Ϸ��� ���� 2���ε� �Լ��� ���� ������ �׺��� �۽��ϴ�.");
				System.exit(0);
			}
			for (int i = 0; i < ctx.expr().size(); i++) {
				String returnType = funReturnType.get(funName).get(i);
				s1 = localDecl.get(ctx.expr(i).getText());
				if (s1.getType().equals(returnType) && assignTypeChecking(returnType, s1.getValue())) {
					result += "\n" + this.Space() + "lod " + s1.getBase() + " " + s1.getOffset();
				} else {
					System.out.println("�Լ��� ���ϰ��� �����Ϸ��� ���� Ÿ���� ��ġ���� �ʽ��ϴ�.");
					System.exit(0);
				}
			}
		} else if (ctx.getChildCount() == 2) { // ���ϰ��� 1��
			String funName = ctx.expr(0).getParent().getParent().getParent().getParent().getChild(1).getText();
			String returnType = funReturnType.get(funName).get(0);
			s1 = localDecl.get(ctx.expr(0).getText());
			if (s1.getType().equals(returnType) && assignTypeChecking(returnType, s1.getValue())) {
				result += "\n" + this.Space() + "lod " + s1.getBase() + " " + s1.getOffset();
			} else {
				System.out.println("�Լ��� ���ϰ��� �����Ϸ��� ���� Ÿ���� ��ġ���� �ʽ��ϴ�.");
				System.exit(0);
			}
		} 
		newTexts.put(ctx, result);
	}

	public void exitParam(MiniGoParser.ParamContext ctx) { // param (IDENT type_spec / IDENT '[' ']' type_spec)
		String result = "";
		result += "\n" + this.Space() + "sym 2 " + localCount + " 1";
		localDecl.put(ctx.getChild(0).getText(),
				new Variable(ctx.type_spec().getText(), null, "2", localCount + "", "1"));
		localCount += 1;
		newTexts.put(ctx, result);
	}

	public void exitParams(MiniGoParser.ParamsContext ctx) { // params
		String result = ""; // param(',' param)*
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < ctx.param().size(); i++) { // �������ϱ� �ݺ�������
			result += newTexts.get(ctx.param(i));
			list.add(ctx.param(i).getChild(1).getText());
		}
		funParamsType.put(ctx.getParent().getChild(1).getText(), list);
		newTexts.put(ctx, result);
	}

	public void exitArgs(MiniGoParser.ArgsContext ctx) { // args
		String result = ""; // expr (',' expr) *
		Variable s1;
		ArrayList<String> list = new ArrayList<String>();
		String funName = ctx.getParent().getChild(0).getText();
		for (int i = 0; i < ctx.expr().size(); i++) { // �������ϱ� �ݺ�������
			if (globalDecl.containsKey(ctx.expr(i).getText())) { // IDENT�� ��������
				s1 = globalDecl.get(ctx.expr(i).getText());
				if (Integer.parseInt(s1.getSize()) > 1) { // �迭�̶�� ��
					result += "\n" + this.Space() + "lda " + s1.getBase() + " " + s1.getOffset();
				} else {
					result += "\n" + this.Space() + "lod " + s1.getBase() + " " + s1.getOffset();
				}
				list.add(s1.getValue());
			} else if (localDecl.containsKey(ctx.expr(i).getText())) { // IDENT�� ��������
				s1 = localDecl.get(ctx.expr(i).getText());
				if (Integer.parseInt(s1.getSize()) > 1) { // �迭�̶�� ��
					result += "\n" + this.Space() + "lda " + s1.getBase() + " " + s1.getOffset();
				} else {
					result += "\n" + this.Space() + "lod " + s1.getBase() + " " + s1.getOffset();
				}
				list.add(s1.getValue());
			} else { // ���
				result += "\n" + this.Space() + "ldc " + newTexts.get(ctx.expr(i));
				list.add(newTexts.get(ctx.expr(i)));
			}
		}
		funArgsType.put(funName, list);
		newTexts.put(ctx, result);
	}
}

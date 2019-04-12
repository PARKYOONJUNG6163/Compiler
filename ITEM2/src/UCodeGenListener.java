
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Stack;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

//201602001 ������
public class UCodeGenListener extends MiniGoBaseListener {
	HashMap<String, Variable> globalDecl = new HashMap<String, Variable>(); // ���� ���� ��Ƴ��� �ؽ� ��
	HashMap<String, Variable> localDecl = new HashMap<String, Variable>(); // ���� ���� ��Ƴ��� �ؽ� ��
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
			FileWriter fw = new FileWriter("C://Users//user//Desktop//item2.uco"); // ������ ������ ��� ����
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
			globalDecl.put(ctx.getChild(1).getText(), new Variable("2", globalCount + "", "1"));
			globalCount += 1;
		} else if (ctx.getChildCount() == 5) { // VAR IDENT ',' IDENT type_spec
			result += "\n" + this.Space() + "sym 1 " + globalCount + " 1";
			globalDecl.put(ctx.getChild(1).getText(), new Variable("2", globalCount + "", "1"));
			globalCount += 1;
		} else { // VAR IDENT '[' LITERAL ']' type_spec
			result += "\n" + this.Space() + "sym 1 " + globalCount + " " + ctx.getChild(3).getText();
			globalDecl.put(ctx.getChild(1).getText(),
					new Variable("2", globalCount + "", ctx.getChild(3).getText() + ""));
			globalCount += Integer.parseInt(ctx.getChild(3).getText());
		}
		newTexts.put(ctx, result);
	}

	public void enterFun_decl(MiniGoParser.Fun_declContext ctx) { // �Լ� ���ۺκп��� localCount�� �ʱ�ȭ���־����
		localCount = 1; // �������� �ʱ�ȭ
		localDecl = new HashMap<String, Variable>(); // �������� �ʱ�ȭ
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
		if (ctx.type_spec().equals("int")) { // ���ϰ��� �ִ� retv�� ���
			result += "\n" + this.Space() + "retv";
		} else { // ���ϰ��� ���� ret�� ���
			result += "\n" + this.Space() + "ret";
		}
		result += "\n" + this.Space() + "end";
		newTexts.put(ctx, result);
	}

	public void exitLocal_decl(MiniGoParser.Local_declContext ctx) { // ���� ���� �κ�
		String result = "";
		if (ctx.getChildCount() == 3) { // VAR IDENT type_spec
			result += "\n" + this.Space() + "sym 2 " + localCount + " 1";
			localDecl.put(ctx.getChild(1).getText(), new Variable("2", localCount + "", "1"));
			localCount += 1;
		} else { // VAR IDENT '[' LITERAL ']' type_spec
			result += "\n" + this.Space() + "sym 2 " + localCount + " 1";
			localDecl.put(ctx.getChild(1).getText(),
					new Variable("2", localCount + "", ctx.getChild(3).getText() + ""));
			localCount += Integer.parseInt(ctx.getChild(3).getText());
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
			result += newTexts.get(ctx.for_stmt())  + "\n$$" + ++whereJump + this.jumpSpace() + "nop";
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

	public void exitExpr(MiniGoParser.ExprContext ctx) {
		String op = null;
		String result = "";
		if (ctx.getChildCount() == 1) { // (LITERAL|IDENT)
			if (ctx.LITERAL(0) != null) { // LITERAL
				result += "\n" + this.Space() + "ldc " + ctx.getChild(0).getText();
			} else { // IEDNT
				result += ctx.getChild(0).getText();
			}
		}

		if (isBinaryOperation(ctx)) { // +,-,=���� ��ȣ�� �������� ���
			if (ctx.expr(1) == null) { // IDENT = expr (A=3)
				Variable s1 = null;
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
				if (globalDecl.containsKey(ctx.expr(0).getText())) { // expr�� ��������
					s1 = globalDecl.get(ctx.expr(0).getText());
					result += "\n" + this.Space() + "lod " + s1.getBase() + " " + s1.getOffset();
				} else if (localDecl.containsKey(ctx.expr(0).getText())) { // expr�� ��������
					s1 = localDecl.get(ctx.expr(0).getText());
					result += "\n" + this.Space() + "lod " + s1.getBase() + " " + s1.getOffset();
				} else { // ���
					result += newTexts.get(ctx.expr(0));
				}
				// �� ����
				result += "\n" + this.Space() + "str " + s1.getBase() + " " + s1.getOffset();
			} else { // �� �κ��� expr�� ���(expr + expr)
				Variable s1;
				op = ctx.getChild(1).getText();
				for (int i = 0; i < ctx.expr().size(); i++) {
					if (globalDecl.containsKey(ctx.expr(i).getText())) { // ��������
						s1 = globalDecl.get(ctx.expr(i).getText());
						result += "\n" + this.Space() + "lod " + s1.getBase() + " " + s1.getOffset();
					} else if (localDecl.containsKey(ctx.expr(i).getText())) { // ��������
						s1 = localDecl.get(ctx.expr(i).getText());
						result += "\n" + this.Space() + "lod " + s1.getBase() + " " + s1.getOffset();
					} else { // ���
						result += newTexts.get(ctx.expr(i));
					}
				}
				result += "\n" + this.Space() + this.whatBinaryOperation(op);
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
				String s2 = newTexts.get(ctx.expr(0));
				result += "\n" + this.Space() + "ldc " + s2;
				result += "\n" + this.Space() + "lda " + s1.getBase() + " " + s1.getOffset();
				result += "\n" + this.Space() + "add "; // �迭�� �ε��� ���ؼ� �ڸ� ã��
			} else { // IDENT '(' args ')'
				result += "\n" + this.Space() + "ldp ";
				result += newTexts.get(ctx.args());
				result += "\n" + this.Space() + "call " + ctx.getChild(0).getText();
			}
		} else if (ctx.getChildCount() == 6) {
			if (ctx.getChild(0).getText().equals("fmt")) { // FMT '.' IDENT '(' args ')'
				result += "\n" + this.Space() + "ldp";
				result += newTexts.get(ctx.args());
				result += "\n" + this.Space() + "call write";
			} else { // IDENT '[' expr ']' '=' expr
				Variable s1;
				// IDENT���ϱ�
				if (globalDecl.containsKey(ctx.getChild(0).getText())) { // IDENT ��������
					s1 = globalDecl.get(ctx.getChild(0).getText());
				} else { // IDENT ��������
					s1 = localDecl.get(ctx.getChild(0).getText());
				}
				result += newTexts.get(ctx.expr(0));
				result += "\n" + this.Space() + "lda " + s1.getBase() + " " + s1.getOffset();
				result += "\n" + this.Space() + "add "; // �迭�� �ε��� ���ؼ� �ڸ� ã��
				result += newTexts.get(ctx.expr(1));
				result += "\n" + this.Space() + "sti";
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

	public void exitAssign_stmt(MiniGoParser.Assign_stmtContext ctx) { // assign��
		String result = "";
		if (ctx.getChild(0) == ctx.VAR()) { // �� ���� var
			if (ctx.getChildCount() == 9) { // VAR IDENT ',' IDENT type_spec '=' LITERAL ',' LITERAL
				result += "\n" + this.Space() + "sym 2 " + localCount + " 1"; // ù��° ���� ���� ������
				result += "\n" + this.Space() + "ldc " + ctx.getChild(6).getText();
				result += "\n" + this.Space() + "str 2 " + localCount;
				localDecl.put(ctx.getChild(1).getText(), new Variable("2", localCount + "", "1"));
				localCount += 1; // �������� 1 ����
				result += "\n" + this.Space() + "sym 2 " + localCount + " 1"; // �ι�° ���� ���� ������
				result += "\n" + this.Space() + "ldc " + ctx.getChild(8).getText();
				result += "\n" + this.Space() + "str 2 " + localCount;
				localDecl.put(ctx.getChild(3).getText(), new Variable("2", localCount + "", "1"));
				localCount += 1; // �������� 1 ����
			} else { // VAR IDENT type_spec '=' expr
				result += "\n" + this.Space() + "sym 2 " + localCount + " 1";
				result += newTexts.get(ctx.expr(0));
				result += "\n" + this.Space() + "str 2 " + localCount;
				localDecl.put(ctx.getChild(1).getText(), new Variable("2", localCount + "", "1"));
				localCount += 1; // �������� 1 ����
			}
		} else { // �� ���� IDENT
			Variable s1;
			// IDENT ���ϱ�
			if (globalDecl.containsKey(ctx.getChild(0).getText())) { // IDENT�� ��������
				s1 = globalDecl.get(ctx.getChild(0).getText());
			} else { // IDENT�� ��������
				s1 = localDecl.get(ctx.getChild(0).getText());
			}

			if (ctx.type_spec() != null) { // IDENT type_spec '=' expr
				result += "\n" + this.Space() + "lod " + s1.getBase() + " " + s1.getOffset();
				result += newTexts.get(ctx.expr(0));
				result += "\n" + this.Space() + "str " + s1.getBase() + " " + s1.getOffset();
			} else { // IDENT '[' expr ']' '=' expr
				Variable s2;
				if (globalDecl.containsKey(ctx.expr(0).getText())) { // expr�� ��������
					s2 = globalDecl.get(ctx.getChild(0).getText());
				} else { // expr�� ��������
					s2 = localDecl.get(ctx.getChild(0).getText());
				}
				result += newTexts.get(ctx.expr(0));
				result += "\n" + this.Space() + "lda " + s2.getBase() + " " + s2.getOffset();
				result += "\n" + this.Space() + "add "; // �迭�� �ε��� ���ؼ� �ڸ� ã��
				result += newTexts.get(ctx.expr(1));
				result += "\n" + this.Space() + "sti";
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
		for (int i = 0; i < ctx.expr().size(); i++) {
			s1 = localDecl.get(ctx.expr(i).getText());
			result += "\n" + this.Space() + "lod " + s1.getBase() + " " + s1.getOffset();
		}
		newTexts.put(ctx, result);
	}

	public void exitParam(MiniGoParser.ParamContext ctx) { // param (IDENT type_spec / IDENT '[' ']' type_spec)
		String result = "";
		result += "\n" + this.Space() + "sym 2 " + localCount + " 1";
		localDecl.put(ctx.getChild(0).getText(), new Variable("2", localCount + "", "1"));
		localCount += 1;
		newTexts.put(ctx, result);
	}

	public void exitParams(MiniGoParser.ParamsContext ctx) { // params
		String result = ""; // param(',' param)*
		for (int i = 0; i < ctx.param().size(); i++) { // �������ϱ� �ݺ�������
			result += newTexts.get(ctx.param(i));
		}
		newTexts.put(ctx, result);
	}

	public void exitArgs(MiniGoParser.ArgsContext ctx) { // args
		String result = ""; // expr (',' expr) *
		Variable s1;
		for (int i = 0; i < ctx.expr().size(); i++) { // �������ϱ� �ݺ�������
			if (globalDecl.containsKey(ctx.expr(i).getText())) { // IDENT�� ��������
				s1 = globalDecl.get(ctx.expr(i).getText());
				if (Integer.parseInt(s1.getSize()) > 1) { // �迭�̶�� ��
					result += "\n" + this.Space() + "lda " + s1.getBase() + " " + s1.getOffset();
				} else {
					result += "\n" + this.Space() + "lod " + s1.getBase() + " " + s1.getOffset();
				}
			} else if (localDecl.containsKey(ctx.expr(i).getText())) { // IDENT�� ��������
				s1 = localDecl.get(ctx.expr(i).getText());
				if (Integer.parseInt(s1.getSize()) > 1) { // �迭�̶�� ��
					result += "\n" + this.Space() + "lda " + s1.getBase() + " " + s1.getOffset();
				} else {
					result += "\n" + this.Space() + "lod " + s1.getBase() + " " + s1.getOffset();
				}
			} else { // ���
				result += newTexts.get(ctx.expr(i));
			}
		}
		newTexts.put(ctx, result);
	}

}

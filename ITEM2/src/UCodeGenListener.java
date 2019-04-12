
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Stack;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

//201602001 박윤정
public class UCodeGenListener extends MiniGoBaseListener {
	HashMap<String, Variable> globalDecl = new HashMap<String, Variable>(); // 전역 변수 담아놓는 해시 맵
	HashMap<String, Variable> localDecl = new HashMap<String, Variable>(); // 지역 변수 담아놓는 해시 맵
	ParseTreeProperty<String> newTexts = new ParseTreeProperty<String>();
	Stack<Integer> stack = new Stack<Integer>();
	int whereJump = 0; // 어디로 점프해야하는가?
	int globalCount = 1;
	int localCount = 1;

	public String firstSpace() { // 처음 명령어는 main제외 7번 띄어쓰기
		return "       ";
	}

	public String jumpSpace() { // jump할때 8번 띄어쓰기
		return "        ";
	}

	public String Space() { // 처음 이후에는 11번 띄어쓰기
		return "           ";
	}

	public void createFile(String result) {
		try { // 결과를 파일로 생성
			FileWriter fw = new FileWriter("C://Users//user//Desktop//item2.uco"); // 파일을 저장할 경로 설정
			BufferedWriter bw = new BufferedWriter(fw); 
			bw.write(result);	
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		} // 예외상황 처리
	}
	
	public void exitProgram(MiniGoParser.ProgramContext ctx) { // 프로그램 종료시
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
		if (ctx.var_decl() != null) // decl이 var_decl인 경우
			result += newTexts.get(ctx.var_decl());
		else // decl이 fun_decl인 경우
			result += newTexts.get(ctx.fun_decl());

		newTexts.put(ctx, result);
	}

	public void exitVar_decl(MiniGoParser.Var_declContext ctx) { // 변수 부분
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

	public void enterFun_decl(MiniGoParser.Fun_declContext ctx) { // 함수 시작부분에서 localCount를 초기화해주어야함
		localCount = 1; // 지역변수 초기화
		localDecl = new HashMap<String, Variable>(); // 지역변수 초기화
	}

	public void exitFun_decl(MiniGoParser.Fun_declContext ctx) { // 함수 부분
		String result = "\n";
		String space = "";
		String fun_name = ctx.getChild(1).getText();
		for (int i = 0; i < 11 - fun_name.length(); i++) { // 함수 명령어가 12번째 와야하므로 space를 정해줌
			space += " ";
		}
		result += ctx.getChild(1).getText() + space + "proc " + (globalCount + localCount) + " 2 2"; // 함수선언과 이름
		if (ctx.getChildCount() == 7) { // FUNC IDENT '(' params ')' type_spec compound_stmt
			result += newTexts.get(ctx.params());
			result += newTexts.get(ctx.compound_stmt());
		} else { // FUNC IDENT '(' params ')' '(' type_spec ',' type_spec ')' compound_stmt
			result += newTexts.get(ctx.params());
			result += newTexts.get(ctx.compound_stmt());
		}
		// 리턴 타입에 따라 ret인지 retv인지 구분
		if (ctx.type_spec().equals("int")) { // 리턴값이 있는 retv인 경우
			result += "\n" + this.Space() + "retv";
		} else { // 리턴값이 없는 ret인 경우
			result += "\n" + this.Space() + "ret";
		}
		result += "\n" + this.Space() + "end";
		newTexts.put(ctx, result);
	}

	public void exitLocal_decl(MiniGoParser.Local_declContext ctx) { // 지역 변수 부분
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

		if (isBinaryOperation(ctx)) { // +,-,=같이 부호가 연산자인 경우
			if (ctx.expr(1) == null) { // IDENT = expr (A=3)
				Variable s1 = null;
				boolean check = false;
				// IDENT처리부분
				for (int i = 0; i < ctx.expr().size(); i++) {
					if (ctx.getChild(0).getText().equals(ctx.expr(i).getText()))
						check = true;
				}
				if (globalDecl.containsKey(ctx.getChild(0).getText())) { // IDENT가 전역변수
					s1 = globalDecl.get(ctx.getChild(0).getText());
				} else { // IDENT가 지역변수
					s1 = localDecl.get(ctx.getChild(0).getText());
				}
				if (check) {
					result += "\n" + this.Space() + "lod " + s1.getBase() + " " + s1.getOffset();
				}
				// expr처리부분
				if (globalDecl.containsKey(ctx.expr(0).getText())) { // expr가 전역변수
					s1 = globalDecl.get(ctx.expr(0).getText());
					result += "\n" + this.Space() + "lod " + s1.getBase() + " " + s1.getOffset();
				} else if (localDecl.containsKey(ctx.expr(0).getText())) { // expr가 지역변수
					s1 = localDecl.get(ctx.expr(0).getText());
					result += "\n" + this.Space() + "lod " + s1.getBase() + " " + s1.getOffset();
				} else { // 상수
					result += newTexts.get(ctx.expr(0));
				}
				// 값 저장
				result += "\n" + this.Space() + "str " + s1.getBase() + " " + s1.getOffset();
			} else { // 두 부분이 expr인 경우(expr + expr)
				Variable s1;
				op = ctx.getChild(1).getText();
				for (int i = 0; i < ctx.expr().size(); i++) {
					if (globalDecl.containsKey(ctx.expr(i).getText())) { // 전역변수
						s1 = globalDecl.get(ctx.expr(i).getText());
						result += "\n" + this.Space() + "lod " + s1.getBase() + " " + s1.getOffset();
					} else if (localDecl.containsKey(ctx.expr(i).getText())) { // 지역변수
						s1 = localDecl.get(ctx.expr(i).getText());
						result += "\n" + this.Space() + "lod " + s1.getBase() + " " + s1.getOffset();
					} else { // 상수
						result += newTexts.get(ctx.expr(i));
					}
				}
				result += "\n" + this.Space() + this.whatBinaryOperation(op);
			}
		} else if (ctx.getChildCount() == 2) { // 전위 연산자와 피연산자인 경우 (++x)
			Variable s1;
			op = ctx.getChild(0).getText(); // 전위 연산자
			if (globalDecl.containsKey(ctx.expr(0).getText())) { // expr이 전역변수
				s1 = globalDecl.get(ctx.expr(0).getText());
			} else { // expr이 지역변수
				s1 = localDecl.get(ctx.expr(0).getText());
			}
			result += "\n" + this.Space() + "lod " + s1.getBase() + " " + s1.getOffset();
			result += "\n" + this.Space() + this.whatBinaryOperation(op);
			result += "\n" + this.Space() + "str 2" + " " + s1.getOffset();
		} else if (ctx.getChildCount() == 3) { // 괄호가 있는 경우 '(' expr ')'
			Variable s1;
			if (globalDecl.containsKey(ctx.expr(0).getText())) { // expr이 전역변수
				s1 = globalDecl.get(ctx.expr(0).getText());
			} else { // expr이 지역변수
				s1 = localDecl.get(ctx.expr(0).getText());
			}
			result += "\n" + this.Space() + "lod " + s1.getBase() + " " + s1.getOffset();
		} else if (ctx.getChildCount() == 4) {
			if (ctx.expr(0) != null) { // IDENT '[' expr ']'
				Variable s1;
				// IDENT구하기
				if (globalDecl.containsKey(ctx.getChild(0).getText())) { // IDENT 전역변수
					s1 = globalDecl.get(ctx.getChild(0).getText());
				} else { // IDENT 지역변수
					s1 = localDecl.get(ctx.getChild(0).getText());
				}
				String s2 = newTexts.get(ctx.expr(0));
				result += "\n" + this.Space() + "ldc " + s2;
				result += "\n" + this.Space() + "lda " + s1.getBase() + " " + s1.getOffset();
				result += "\n" + this.Space() + "add "; // 배열과 인덱스 더해서 자리 찾기
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
				// IDENT구하기
				if (globalDecl.containsKey(ctx.getChild(0).getText())) { // IDENT 전역변수
					s1 = globalDecl.get(ctx.getChild(0).getText());
				} else { // IDENT 지역변수
					s1 = localDecl.get(ctx.getChild(0).getText());
				}
				result += newTexts.get(ctx.expr(0));
				result += "\n" + this.Space() + "lda " + s1.getBase() + " " + s1.getOffset();
				result += "\n" + this.Space() + "add "; // 배열과 인덱스 더해서 자리 찾기
				result += newTexts.get(ctx.expr(1));
				result += "\n" + this.Space() + "sti";
			}
		}
		newTexts.put(ctx, result);
	}

	public void exitCompound_stmt(MiniGoParser.Compound_stmtContext ctx) {
		String result = "";
		int localIndex = 0; // 지역변수 문장 인덱스
		int stmtIndex = 0; // stmt문장 인덱스
		for (int i = 1; i < ctx.getChildCount() - 1; i++) { // {와 }를 빼주어야 하므로 1부터시작, 개수 -1까지 반복
			if (ctx.local_decl().contains(ctx.getChild(i))) { // local_decl인 경우
				result += newTexts.get(ctx.local_decl(localIndex));
				localIndex++;
			} else { // compound_stmt인 경우
				result += newTexts.get(ctx.stmt(stmtIndex));
				stmtIndex++;
			}
		}
		newTexts.put(ctx, result);
	}

	public void exitAssign_stmt(MiniGoParser.Assign_stmtContext ctx) { // assign문
		String result = "";
		if (ctx.getChild(0) == ctx.VAR()) { // 맨 앞이 var
			if (ctx.getChildCount() == 9) { // VAR IDENT ',' IDENT type_spec '=' LITERAL ',' LITERAL
				result += "\n" + this.Space() + "sym 2 " + localCount + " 1"; // 첫번째 값에 대한 값셋팅
				result += "\n" + this.Space() + "ldc " + ctx.getChild(6).getText();
				result += "\n" + this.Space() + "str 2 " + localCount;
				localDecl.put(ctx.getChild(1).getText(), new Variable("2", localCount + "", "1"));
				localCount += 1; // 지역변수 1 증가
				result += "\n" + this.Space() + "sym 2 " + localCount + " 1"; // 두번째 값에 대한 값셋팅
				result += "\n" + this.Space() + "ldc " + ctx.getChild(8).getText();
				result += "\n" + this.Space() + "str 2 " + localCount;
				localDecl.put(ctx.getChild(3).getText(), new Variable("2", localCount + "", "1"));
				localCount += 1; // 지역변수 1 증가
			} else { // VAR IDENT type_spec '=' expr
				result += "\n" + this.Space() + "sym 2 " + localCount + " 1";
				result += newTexts.get(ctx.expr(0));
				result += "\n" + this.Space() + "str 2 " + localCount;
				localDecl.put(ctx.getChild(1).getText(), new Variable("2", localCount + "", "1"));
				localCount += 1; // 지역변수 1 증가
			}
		} else { // 맨 앞이 IDENT
			Variable s1;
			// IDENT 구하기
			if (globalDecl.containsKey(ctx.getChild(0).getText())) { // IDENT가 전역변수
				s1 = globalDecl.get(ctx.getChild(0).getText());
			} else { // IDENT가 지역변수
				s1 = localDecl.get(ctx.getChild(0).getText());
			}

			if (ctx.type_spec() != null) { // IDENT type_spec '=' expr
				result += "\n" + this.Space() + "lod " + s1.getBase() + " " + s1.getOffset();
				result += newTexts.get(ctx.expr(0));
				result += "\n" + this.Space() + "str " + s1.getBase() + " " + s1.getOffset();
			} else { // IDENT '[' expr ']' '=' expr
				Variable s2;
				if (globalDecl.containsKey(ctx.expr(0).getText())) { // expr이 전역변수
					s2 = globalDecl.get(ctx.getChild(0).getText());
				} else { // expr이 지역변수
					s2 = localDecl.get(ctx.getChild(0).getText());
				}
				result += newTexts.get(ctx.expr(0));
				result += "\n" + this.Space() + "lda " + s2.getBase() + " " + s2.getOffset();
				result += "\n" + this.Space() + "add "; // 배열과 인덱스 더해서 자리 찾기
				result += newTexts.get(ctx.expr(1));
				result += "\n" + this.Space() + "sti";
			}
		}
		newTexts.put(ctx, result);
	}

	public void exitIf_stmt(MiniGoParser.If_stmtContext ctx) { // if문
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

	public void exitFor_stmt(MiniGoParser.For_stmtContext ctx) { // for문
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
		for (int i = 0; i < ctx.param().size(); i++) { // 여러개니까 반복분으로
			result += newTexts.get(ctx.param(i));
		}
		newTexts.put(ctx, result);
	}

	public void exitArgs(MiniGoParser.ArgsContext ctx) { // args
		String result = ""; // expr (',' expr) *
		Variable s1;
		for (int i = 0; i < ctx.expr().size(); i++) { // 여러개니까 반복분으로
			if (globalDecl.containsKey(ctx.expr(i).getText())) { // IDENT가 전역변수
				s1 = globalDecl.get(ctx.expr(i).getText());
				if (Integer.parseInt(s1.getSize()) > 1) { // 배열이라는 뜻
					result += "\n" + this.Space() + "lda " + s1.getBase() + " " + s1.getOffset();
				} else {
					result += "\n" + this.Space() + "lod " + s1.getBase() + " " + s1.getOffset();
				}
			} else if (localDecl.containsKey(ctx.expr(i).getText())) { // IDENT가 지역변수
				s1 = localDecl.get(ctx.expr(i).getText());
				if (Integer.parseInt(s1.getSize()) > 1) { // 배열이라는 뜻
					result += "\n" + this.Space() + "lda " + s1.getBase() + " " + s1.getOffset();
				} else {
					result += "\n" + this.Space() + "lod " + s1.getBase() + " " + s1.getOffset();
				}
			} else { // 상수
				result += newTexts.get(ctx.expr(i));
			}
		}
		newTexts.put(ctx, result);
	}

}


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.regex.Pattern;

import org.antlr.v4.runtime.tree.ParseTreeProperty;

//201602001 박윤정
public class UCodeGenListener extends MiniGoBaseListener {
	HashMap<String, Variable> globalDecl = new HashMap<String, Variable>(); // 전역 변수 담아놓는 해시 맵
	HashMap<String, Variable> localDecl = new HashMap<String, Variable>(); // 지역 변수 담아놓는 해시 맵
	HashMap<String, ArrayList<String>> funReturnType = new HashMap<String, ArrayList<String>>(); // return 타입을 함수명과 함께 담아놓음
	HashMap<String, ArrayList<String>> funArgsType = new HashMap<String, ArrayList<String>>(); // args 타입을 함수명과 함께 담아놓음
	HashMap<String, ArrayList<String>> funParamsType = new HashMap<String, ArrayList<String>>(); // params 타입을 함수명과 함께 담아놓음
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
			FileWriter fw = new FileWriter("C://Users//user//Desktop//item3.uco"); // 파일을 저장할 경로 설정
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

	public void enterFun_decl(MiniGoParser.Fun_declContext ctx) { // 함수 시작부분에서 localCount를 초기화해주어야함
		localCount = 1; // 지역변수 초기화
		localDecl = new HashMap<String, Variable>(); // 지역변수 초기화
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < ctx.type_spec().size(); i++) {
			list.add(ctx.type_spec(i).getText());
		}
		funReturnType.put(ctx.getChild(1).getText(), list);
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
		if (ctx.type_spec().equals("void") || ctx.type_spec() == null) {  // 리턴값이 없는 ret인 경우
			result += "\n" + this.Space() + "ret";
		} else {  // 리턴값이 있는 retv인 경우 
			result += "\n" + this.Space() + "retv";
		}
		result += "\n" + this.Space() + "end";
		newTexts.put(ctx, result);
	}

	public void exitLocal_decl(MiniGoParser.Local_declContext ctx) { // 지역 변수 부분
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

	public boolean opVarChecking(String type1, String type2) { // op연산을 위한 두 값의 타입 체크
		if (type1.equals(type2)) {
			System.out.println("두 타입이 같습니다.");
			return true;
		}
		System.out.println("두 타입이 같지 않습니다.");
		System.exit(0);
		return false;
	}

	public void exitExpr(MiniGoParser.ExprContext ctx) {
		String op = null;
		String result = "";
		if (ctx.getChildCount() == 1) { // (LITERAL|IDENT)
			result += ctx.getChild(0).getText().replace("\r", "");
		}

		if (isBinaryOperation(ctx)) { // +,-,=같이 부호가 연산자인 경우
			if (ctx.expr(1) == null) { // IDENT = expr (A=3)
				Variable s1 = null;
				Variable s2 = null;
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
				String value;
				if (globalDecl.containsKey(ctx.expr(0).getText())) { // expr가 전역변수
					s2 = globalDecl.get(ctx.expr(0).getText());
					result += "\n" + this.Space() + "lod " + s2.getBase() + " " + s2.getOffset();
					value = s2.getValue();
				} else if (localDecl.containsKey(ctx.expr(0).getText())) { // expr가 지역변수
					s2 = localDecl.get(ctx.expr(0).getText());
					result += "\n" + this.Space() + "lod " + s2.getBase() + " " + s2.getOffset();
					value = s2.getValue();
				} else { // 상수
					result += "\n" + this.Space() + "ldc " + newTexts.get(ctx.expr(0));
					value = newTexts.get(ctx.expr(0));
				}
				
				if(assignTypeChecking(s1.getType(), value)) {
					// 값 저장
					if (globalDecl.containsKey(ctx.getChild(0).getText())) { // IDENT가 전역변수
						globalDecl.put(ctx.getChild(0).getText(),
								new Variable(s1.getType(), value, s1.getBase(), s1.getOffset(), s1.getSize()));
					} else { // IDENT가 지역변수
						localDecl.put(ctx.getChild(0).getText(),
								new Variable(s1.getType(), value, s1.getBase(), s1.getOffset(), s1.getSize()));
					}
					result += "\n" + this.Space() + "str " + s1.getBase() + " " + s1.getOffset();					
				}
				
			} else { // 두 부분이 expr인 경우(expr + expr)
				Variable s1 = null;
				Variable s2 = null;
				// 첫번째 expr구하기
				op = ctx.getChild(1).getText();
				if (globalDecl.containsKey(ctx.expr(0).getText())) { // 전역변수
					s1 = globalDecl.get(ctx.expr(0).getText());
					result += "\n" + this.Space() + "lod " + s1.getBase() + " " + s1.getOffset();
				} else if (localDecl.containsKey(ctx.expr(0).getText())) { // 지역변수
					s1 = localDecl.get(ctx.expr(0).getText());
					result += "\n" + this.Space() + "lod " + s1.getBase() + " " + s1.getOffset();
				} else { // 상수
					result += "\n" + this.Space() + "ldc " + newTexts.get(ctx.expr(0));
				}
				// 두번째 expr구하기
				if (globalDecl.containsKey(ctx.expr(1).getText())) { // 전역변수
					s2 = globalDecl.get(ctx.expr(1).getText());
					result += "\n" + this.Space() + "lod " + s2.getBase() + " " + s2.getOffset();
				} else if (localDecl.containsKey(ctx.expr(1).getText())) { // 지역변수
					s2 = localDecl.get(ctx.expr(1).getText());
					result += "\n" + this.Space() + "lod " + s2.getBase() + " " + s2.getOffset();
				} else { // 상수
					result += "\n" + this.Space() + "ldc " + newTexts.get(ctx.expr(1));
				}
				if (opVarChecking(s1.getType(), s2.getType())) { // 두 타입이 같은지 비교
					result += "\n" + this.Space() + this.whatBinaryOperation(op);
				}
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
				result += "\n" + this.Space() + "ldc " + newTexts.get(ctx.expr(0));
				result += "\n" + this.Space() + "lda " + s1.getBase() + " " + s1.getOffset();
				result += "\n" + this.Space() + "add "; // 배열과 인덱스 더해서 자리 찾기
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
				// IDENT 구하기
				if (globalDecl.containsKey(ctx.getChild(0).getText())) { // IDENT가 전역변수
					s1 = globalDecl.get(ctx.getChild(0).getText());
				} else { // IDENT가 지역변수
					s1 = localDecl.get(ctx.getChild(0).getText());
				}
				result += "\n" + this.Space() + "lod " + s1.getBase() + " " + s1.getOffset();
				Variable s2;
				String value;
				Variable s3;
				// 1번째 expr을 구하기
				if (globalDecl.containsKey(ctx.expr(0).getText())) { // expr가 전역변수
					s2 = globalDecl.get(ctx.expr(0).getText());
					result += "\n" + this.Space() + "lod " + s2.getBase() + " " + s2.getOffset();
				} else if (localDecl.containsKey(ctx.expr(0).getText())) { // expr가 지역변수
					s2 = localDecl.get(ctx.expr(0).getText());
					result += "\n" + this.Space() + "lod " + s2.getBase() + " " + s2.getOffset();
				} else { // 상수
					result += "\n" + this.Space() + "ldc " + newTexts.get(ctx.expr(0));
				}
				result += "\n" + this.Space() + "lda " + s1.getBase() + " " + s1.getOffset();
				result += "\n" + this.Space() + "add "; // 배열과 인덱스 더해서 자리 찾기
				// 2번째 expr을 구하기
				if (globalDecl.containsKey(ctx.expr(0).getText())) { // expr가 전역변수
					s3 = globalDecl.get(ctx.expr(0).getText());
					result += "\n" + this.Space() + "lod " + s3.getBase() + " " + s3.getOffset();
					value = s3.getValue();
				} else if (localDecl.containsKey(ctx.expr(0).getText())) { // expr가 지역변수
					s3 = localDecl.get(ctx.expr(0).getText());
					result += "\n" + this.Space() + "lod " + s3.getBase() + " " + s3.getOffset();
					value = s3.getValue();
				} else { // 상수
					result += "\n" + this.Space() + "ldc " + newTexts.get(ctx.expr(1));
					value = newTexts.get(ctx.expr(1)).trim();
				}
				if (assignTypeChecking(s1.getType(), value)) { // type체크
					if (globalDecl.containsKey(ctx.getChild(0).getText())) { // IDENT가 전역변수
						globalDecl.put(ctx.getChild(0).getText(),
								new Variable(s1.getType(), value, s1.getBase(), s1.getOffset(), s1.getSize()));
					} else { // IDENT가 지역변수
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

	public boolean TypeChecking(String pattern, String value) { // 타입과 값의 패턴이 맞는지 체크하여 결과 리턴
		return Pattern.matches(pattern, value);
	}

	public boolean NumChecking(String type, String value) { // 해당하는 타입의 범위에 맞는지 확인하여 결과 리턴
		boolean result = false;
		switch (type) {
		case "int16": // short형
			short a = Short.parseShort(value);
			if (Short.MIN_VALUE <= a && Short.MAX_VALUE >= a)
				result = true;
			break;
		case "int32": // int형
			int b = Integer.parseInt(value);
			if (Integer.MIN_VALUE <= b && Integer.MAX_VALUE >= b)
				result = true;
			break;
		case "int64": // long형
			long c = Long.parseLong(value);
			if (Long.MIN_VALUE <= c && Long.MAX_VALUE >= c)
				result = true;
			break;
		case "float32": // float형
			float d = Float.parseFloat(value);
			if (Float.MIN_VALUE <= d && Float.MAX_VALUE >= d)
				result = true;
			break;
		case "float64": // double형
			double e = Double.parseDouble(value);
			if (Double.parseDouble(value) <= e && Double.MAX_VALUE >= e)
				result = true;
			break;
		}
		return result;
	}

	public String whatPattern(String type) { // 타입에 따른 패턴을 리턴
		String pattern = "";
		if (type.equals("int16") || type.equals("int32") || type.equals("int64")) {
			pattern = "^[0-9]+$";
		} else if (type.equals("float32") || type.equals("float64")) {
			pattern = "^[0-9]+\\.?[0-9]+$";
		}
		return pattern;
	}

	public boolean assignTypeChecking(String type, String value) { // 타입과 값 최종 체크
		if (TypeChecking(whatPattern(type), value)) {
			if (NumChecking(type, value)) { // 숫자의 범위와 자료형 비교
				System.out.println("선언된 type에 해당하는 값이 맞습니다.");
				return true;
			} else {
				System.out.println("선언된 type에 해당하는 값이 아닙니다.");
				System.exit(0);
			}
		} else {
			System.out.println("선언된 type에 해당하는 값이 아닙니다.");
			System.exit(0);
		}
		return false;
	}

	public void exitAssign_stmt(MiniGoParser.Assign_stmtContext ctx) { // assign문
		String result = "";
		if (ctx.getChild(0) == ctx.VAR()) { // 맨 앞이 var (변수 선언 포함)
			if (ctx.getChildCount() == 9) { // VAR IDENT ',' IDENT type_spec '=' LITERAL ',' LITERAL
				for (int i = 0; i < 2; i++) {
					String type = ctx.type_spec().getText();
					String value = ctx.LITERAL(i).getText();
					if (assignTypeChecking(type, value)) { // type 체크
						result += "\n" + this.Space() + "sym 2 " + localCount + " 1"; // 첫번째 값에 대한 값셋팅
						result += "\n" + this.Space() + "ldc " + value;
						result += "\n" + this.Space() + "str 2 " + localCount;
						localDecl.put(ctx.IDENT(i).getText(), new Variable(type, value, "2", localCount + "", "1"));
						localCount += 1; // 지역변수 1 증가
					}
				}
			} else if (ctx.getChildCount() == 5) { // VAR IDENT type_spec '=' expr
				String type = ctx.type_spec().getText();
				Variable s1 = null;
				String value;
				// expr을 구하기
				if (globalDecl.containsKey(ctx.expr(0).getText())) { // expr가 전역변수
					s1 = globalDecl.get(ctx.expr(0).getText());
					value = s1.getValue();
				} else if (localDecl.containsKey(ctx.expr(0).getText())) { // expr가 지역변수
					s1 = localDecl.get(ctx.expr(0).getText());
					value = s1.getValue();
				} else { // 상수
					value = newTexts.get(ctx.expr(0));
				}
				if (assignTypeChecking(type, value)) { // type체크
					result += "\n" + this.Space() + "sym 2 " + localCount + " 1";
					if (s1 != null) {
						result += "\n" + this.Space() + "lod " + s1.getBase() + " " + s1.getOffset();
					} else {
						result += "\n" + this.Space() + "ldc " + newTexts.get(ctx.expr(0));
					}
					result += "\n" + this.Space() + "str 2 " + localCount;
					localDecl.put(ctx.IDENT(0).getText(), new Variable(type, value, "2", localCount + "", "1"));
					localCount += 1; // 지역변수 1 증가
				}

			}
		} else { // 맨 앞이 IDENT (선언이 이미 되어있음)
			Variable s1;
			// IDENT 구하기
			if (globalDecl.containsKey(ctx.getChild(0).getText())) { // IDENT가 전역변수
				s1 = globalDecl.get(ctx.getChild(0).getText());
			} else { // IDENT가 지역변수
				s1 = localDecl.get(ctx.getChild(0).getText());
			}
			result += "\n" + this.Space() + "lod " + s1.getBase() + " " + s1.getOffset();
			Variable s2;
			String value;
			if (ctx.type_spec() != null) { // IDENT type_spec '=' expr
				String type = ctx.type_spec().getText();
				// expr을 구하기
				if (globalDecl.containsKey(ctx.expr(0).getText())) { // expr가 전역변수
					s2 = globalDecl.get(ctx.expr(0).getText());
					result += "\n" + this.Space() + "lod " + s2.getBase() + " " + s2.getOffset();
					value = s2.getValue();
				} else if (localDecl.containsKey(ctx.expr(0).getText())) { // expr가 지역변수
					s2 = localDecl.get(ctx.expr(0).getText());
					result += "\n" + this.Space() + "lod " + s2.getBase() + " " + s2.getOffset();
					value = s2.getValue();
				} else { // 상수
					result += "\n" + this.Space() + "ldc " + newTexts.get(ctx.expr(0));
					value = newTexts.get(ctx.expr(0));
				}
				if (assignTypeChecking(type, value)) { // type체크
					result += "\n" + this.Space() + "str " + s1.getBase() + " " + s1.getOffset();
					if (globalDecl.containsKey(ctx.getChild(0).getText())) { // IDENT가 전역변수
						globalDecl.put(ctx.IDENT(0).getText(),
								new Variable(type, value, s1.getBase(), s1.getOffset(), s1.getSize()));
					} else { // IDENT가 지역변수
						localDecl.put(ctx.IDENT(0).getText(),
								new Variable(type, value, s1.getBase(), s1.getOffset(), s1.getSize()));
					}
				}
			} else { // IDENT '[' expr ']' '=' expr
				Variable s3;
				// 1번째 expr을 구하기
				if (globalDecl.containsKey(ctx.expr(0).getText())) { // expr가 전역변수
					s2 = globalDecl.get(ctx.expr(0).getText());
					result += "\n" + this.Space() + "lod " + s2.getBase() + " " + s2.getOffset();
				} else if (localDecl.containsKey(ctx.expr(0).getText())) { // expr가 지역변수
					s2 = localDecl.get(ctx.expr(0).getText());
					result += "\n" + this.Space() + "lod " + s2.getBase() + " " + s2.getOffset();
				} else { // 상수
					result += "\n" + this.Space() + "ldc " + newTexts.get(ctx.expr(0));
				}
				result += "\n" + this.Space() + "lda " + s1.getBase() + " " + s1.getOffset();
				result += "\n" + this.Space() + "add "; // 배열과 인덱스 더해서 자리 찾기
				// 2번째 expr을 구하기
				if (globalDecl.containsKey(ctx.expr(0).getText())) { // expr가 전역변수
					s3 = globalDecl.get(ctx.expr(0).getText());
					result += "\n" + this.Space() + "lod " + s3.getBase() + " " + s3.getOffset();
					value = s3.getValue();
				} else if (localDecl.containsKey(ctx.expr(0).getText())) { // expr가 지역변수
					s3 = localDecl.get(ctx.expr(0).getText());
					result += "\n" + this.Space() + "lod " + s3.getBase() + " " + s3.getOffset();
					value = s3.getValue();
				} else { // 상수
					result += "\n" + this.Space() + "ldc " + newTexts.get(ctx.expr(0));
					value = newTexts.get(ctx.expr(0));
				}
				if (assignTypeChecking(s1.getType(), value)) { // type체크
					if (globalDecl.containsKey(ctx.getChild(0).getText())) { // IDENT가 전역변수
						globalDecl.put(ctx.IDENT(0).getText(),
								new Variable(s1.getType(), value, s1.getBase(), s1.getOffset(), s1.getSize()));
					} else { // IDENT가 지역변수
						localDecl.put(ctx.IDENT(0).getText(),
								new Variable(s1.getType(), value, s1.getBase(), s1.getOffset(), s1.getSize()));
					}
					result += "\n" + this.Space() + "sti";
				}
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
		if (ctx.getChildCount() == 4) { // 리턴값이 2개
			String funName = ctx.expr(0).getParent().getParent().getParent().getParent().getChild(1).getText();
			if (funReturnType.containsKey(funName) && funReturnType.get(funName).size() < 2) {
				System.out.println("리턴 하려는 값이 2개인데 함수의 리턴 개수가 그보다 작습니다.");
				System.exit(0);
			}
			for (int i = 0; i < ctx.expr().size(); i++) {
				String returnType = funReturnType.get(funName).get(i);
				s1 = localDecl.get(ctx.expr(i).getText());
				if (s1.getType().equals(returnType) && assignTypeChecking(returnType, s1.getValue())) {
					result += "\n" + this.Space() + "lod " + s1.getBase() + " " + s1.getOffset();
				} else {
					System.out.println("함수의 리턴값과 리턴하려는 값의 타입이 일치히지 않습니다.");
					System.exit(0);
				}
			}
		} else if (ctx.getChildCount() == 2) { // 리턴값이 1개
			String funName = ctx.expr(0).getParent().getParent().getParent().getParent().getChild(1).getText();
			String returnType = funReturnType.get(funName).get(0);
			s1 = localDecl.get(ctx.expr(0).getText());
			if (s1.getType().equals(returnType) && assignTypeChecking(returnType, s1.getValue())) {
				result += "\n" + this.Space() + "lod " + s1.getBase() + " " + s1.getOffset();
			} else {
				System.out.println("함수의 리턴값과 리턴하려는 값의 타입이 일치히지 않습니다.");
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
		for (int i = 0; i < ctx.param().size(); i++) { // 여러개니까 반복분으로
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
		for (int i = 0; i < ctx.expr().size(); i++) { // 여러개니까 반복분으로
			if (globalDecl.containsKey(ctx.expr(i).getText())) { // IDENT가 전역변수
				s1 = globalDecl.get(ctx.expr(i).getText());
				if (Integer.parseInt(s1.getSize()) > 1) { // 배열이라는 뜻
					result += "\n" + this.Space() + "lda " + s1.getBase() + " " + s1.getOffset();
				} else {
					result += "\n" + this.Space() + "lod " + s1.getBase() + " " + s1.getOffset();
				}
				list.add(s1.getValue());
			} else if (localDecl.containsKey(ctx.expr(i).getText())) { // IDENT가 지역변수
				s1 = localDecl.get(ctx.expr(i).getText());
				if (Integer.parseInt(s1.getSize()) > 1) { // 배열이라는 뜻
					result += "\n" + this.Space() + "lda " + s1.getBase() + " " + s1.getOffset();
				} else {
					result += "\n" + this.Space() + "lod " + s1.getBase() + " " + s1.getOffset();
				}
				list.add(s1.getValue());
			} else { // 상수
				result += "\n" + this.Space() + "ldc " + newTexts.get(ctx.expr(i));
				list.add(newTexts.get(ctx.expr(i)));
			}
		}
		funArgsType.put(funName, list);
		newTexts.put(ctx, result);
	}
}

import org.antlr.v4.runtime.tree.ParseTreeProperty;
// 201602001 ������
public class MiniGoPrintListener extends MiniGoBaseListener {
	ParseTreeProperty<String> newTexts = new ParseTreeProperty<String>();
	int count = 0; // ��ø �鿩���� Ƚ�� ī��Ʈ�� ����
	
	public String printDot(int n) { // �鿩���� �κ� ���
		String dot="";
		for(int i=0;i<n;i++) {
			dot += "....";
		}
		return dot;
	}
	
	boolean isBinaryOperation(MiniGoParser.ExprContext ctx) {
		return ctx.getChildCount() == 3 && ctx.getChild(1) != ctx.expr(0);
	}
	
	public void exitProgram(MiniGoParser.ProgramContext ctx) { 
		for(int i =0;ctx.decl(i) != null;i++) {
			System.out.println(newTexts.get(ctx.decl(i).getChild(0)));
		}
	}
	
	public void exitVar_decl(MiniGoParser.Var_declContext ctx) { // ���� �κ�
		if(ctx.type_spec() != null) { // dec_spec IDENT type_spec
			if(ctx.getChildCount() == 3) {  // dec_spec IDENT type_spec
				newTexts.put(ctx, ctx.dec_spec().getText() +" "+ctx.IDENT()+" "+ctx.type_spec().getText());
			}else { // dec_spec IDENT type_spec '=' LITERAL 
				newTexts.put(ctx, ctx.dec_spec().getText() +" "+ctx.IDENT()+" "+ctx.type_spec().getText() + "=" + ctx.LITERAL());
			}
		}else { // dec_spec IDENT '[' LITERAL ']' type_spec 
			newTexts.put(ctx, ctx.dec_spec() +" "+ctx.IDENT()+"["+ctx.LITERAL() + "]" + ctx.type_spec());
		}
	}
	
	public void exitFun_decl(MiniGoParser.Fun_declContext ctx) {
		String result = "";	// FUNC IDENT '(' params ')' type_spec compound_stmt 
		result += ctx.FUNC() + " " + ctx.getChild(1).getText(); // �Լ������ �̸�
		result += "(" + newTexts.get(ctx.params()) + "){\n"; // ���� �κ�
		int i=1; // ��ȣ ���ķ�
		while(ctx.compound_stmt().getChild(i) instanceof MiniGoParser.Local_declContext || ctx.compound_stmt().getChild(i) instanceof MiniGoParser.StmtContext) { // compound_stmt �� local_decl �̰ų� stmt�̸�
			result += newTexts.get(ctx.compound_stmt().getChild(i)) +"\n" ;
			i++;
		}
		result += "}";
		newTexts.put(ctx, result);
	}
	 
	public void exitExpr(MiniGoParser.ExprContext ctx) {
		String s1 = null, s2 = null, op = null;
		if(ctx.getChildCount() == 1){ // (LITERAL|IDENT)
			newTexts.put(ctx, ctx.getChild(0).getText()); // newTexts�� expr���� �־���
		}
		if(isBinaryOperation(ctx)) { // +,-,=���� ��ȣ�� �������� ���
			if(ctx.expr(1) == null) { // IDENT = expr (A=3)
				s1 = ctx.getChild(0).getText();
				s2 = newTexts.get(ctx.expr(0));
				op = ctx.getChild(1).getText();
				newTexts.put(ctx, s1 + " " + op + " " + s2);
			}else { // �� �κ��� expr�� ���(expr + expr)
				s1 = newTexts.get(ctx.expr(0));
				s2 = newTexts.get(ctx.expr(1));
				op = ctx.getChild(1).getText();
				newTexts.put(ctx, s1 + " " + op + " " + s2);
			}
		}else if(ctx.getChildCount() == 2){ // ���� �����ڿ� �ǿ������� ��� (++x)
			s1 = newTexts.get(ctx.expr(0)); // expr
			op = ctx.getChild(0).getText(); // ���� ������
			newTexts.put(ctx,  op +s1);
		}else if(ctx.getChildCount() == 3) { // ��ȣ�� �ִ� ��� '(' expr ')' 
			s1 = newTexts.get(ctx.expr(0));
			newTexts.put(ctx, "("  + s1 + ")");
		}else if(ctx.getChildCount() == 4) { 
			if(ctx.expr(0) != null) { // IDENT '[' expr ']' 
				s1 = ctx.getChild(0).getText();
				s2 = newTexts.get(ctx.expr(0));
				newTexts.put(ctx, s1 + "[" + s2 + "]");
			}else { // IDENT '(' args ')' 
				s1 = ctx.getChild(0).getText();
				s2 = newTexts.get(ctx.args());
				newTexts.put(ctx, s1 + "(" + s2 + ")");
			}
		}else if(ctx.getChildCount() == 6) {
			if(ctx.getChild(0).getText().equals("fmt")) { // FMT '.' IDENT '(' args ')' 
				s1 = ctx.getChild(2).getText();
				s2 = newTexts.get(ctx.args());
				newTexts.put(ctx, ctx.getChild(0).getText()+"."+s1+"("+ s2+")");
			}else { // IDENT '[' expr ']' '=' expr
				s1 = newTexts.get(ctx.expr(0));
				s2 = newTexts.get(ctx.expr(1));
				newTexts.put(ctx, ctx.getChild(0)+"["+s1+"]"+ " = " + s2);
			}
		}
	}
	
	public void enterCompound_stmt(MiniGoParser.Compound_stmtContext ctx) {
		count++;
	}
	
	public void exitCompound_stmt(MiniGoParser.Compound_stmtContext ctx) { //'{' local_decl* stmt* '}'
		String result = "";
		result+="{\n";			
		
		int i = 0;
		while(ctx.local_decl(i)!= null){
			result += newTexts.get(ctx.local_decl(i))+"\n";
			i++;		
		}
		
		int j = 0;
		while(ctx.stmt(j)!=null){		
			result += newTexts.get(ctx.stmt(j))+"\n";
			j++;
		}
		count--;
		result+=printDot(count)+"}";		
		newTexts.put(ctx, result);
	} // ��¹� ������ֱ�
	 
	public void exitIf_stmt(MiniGoParser.If_stmtContext ctx) { 
		String result = "";	
		if(ctx.ELSE() == null) { // IF expr stmt
			result += ctx.IF() + " " + newTexts.get(ctx.expr()) +"\n"; // if�� ����� ���ǹ� �κ�
			result += newTexts.get(ctx.stmt(0));
			newTexts.put(ctx, result);
		}else { // IF expr stmt ELSE stmt 
			result += ctx.IF() + " " + newTexts.get(ctx.expr()) +"\n"; // if�� ����� ���ǹ� �κ�
			result += newTexts.get(ctx.stmt(0));
			result += ctx.ELSE() + "\n"+ newTexts.get(ctx.stmt(1)); 
			newTexts.put(ctx, result);
		}
	}
	
	public void exitFor_stmt(MiniGoParser.For_stmtContext ctx) {  
		if(ctx.loop_expr() != null) { // FOR loop_expr stmt
			newTexts.put(ctx.loop_expr(), newTexts.get(ctx.loop_expr().expr(0)) +"; "+newTexts.get(ctx.loop_expr().expr(1))+"; "+newTexts.get(ctx.loop_expr().expr(2))+ctx.loop_expr().getChild(ctx.loop_expr().getChildCount()-1).getText());
			 // loop_expr�κ� ���� �־��ֱ�
			newTexts.put(ctx, ctx.FOR() +" "+newTexts.get(ctx.loop_expr())+"\n"+newTexts.get(ctx.stmt()));
		}else { // FOR expr stmt	
			newTexts.put(ctx, ctx.FOR() +" "+newTexts.get(ctx.expr())+"\n"+newTexts.get(ctx.stmt()));
		} 	
	}
	
	public void exitReturn_stmt(MiniGoParser.Return_stmtContext ctx) { 
		if(ctx.getChildCount() == 1) { // RETURN 
			newTexts.put(ctx, ctx.RETURN().getText());
		}else if(ctx.getChildCount() == 2) { // RETURN expr
			newTexts.put(ctx, ctx.RETURN()+" "+newTexts.get(ctx.expr(0)));
		}else { // RETURN expr ',' expr	
			newTexts.put(ctx, ctx.RETURN()+" "+newTexts.get(ctx.expr(0))+","+newTexts.get(ctx.expr(1)));
		}
	}
	
	public void exitStmt(MiniGoParser.StmtContext ctx) {
		if(ctx.getChild(0) == ctx.expr_stmt()){		// expr_stmt	
			newTexts.put(ctx, printDot(count)+newTexts.get(ctx.expr_stmt().getChild(0)));
		}
		else if(ctx.getChild(0) == ctx.compound_stmt()){	// compound_stmt	
			newTexts.put(ctx, printDot(count)+newTexts.get(ctx.compound_stmt()));
		}
		else if(ctx.getChild(0) == ctx.if_stmt()){			// if_stmt
			newTexts.put(ctx, printDot(count)+newTexts.get(ctx.if_stmt()));
		}
		else if(ctx.getChild(0) == ctx.for_stmt()){		// for_stmt
			newTexts.put(ctx, printDot(count)+newTexts.get(ctx.for_stmt()));
		}
		else{												// return_stmt
			newTexts.put(ctx, printDot(count)+newTexts.get(ctx.return_stmt()));
		}
	}
	
	public void enterLocal_decl(MiniGoParser.Local_declContext ctx) { // compound_stmt ���� local_decl �κ�
		if(ctx.type_spec() != null) { // dec_spec IDENT type_spec
			if(ctx.getChildCount() == 3) {  // dec_spec IDENT type_spec
				newTexts.put(ctx, printDot(count)+ctx.dec_spec().getText() +" "+ctx.IDENT()+" "+ctx.type_spec().getText());
			}else { // dec_spec IDENT type_spec '=' LITERAL 
				newTexts.put(ctx, printDot(count)+ctx.dec_spec().getText() +" "+ctx.IDENT()+" "+ctx.type_spec().getText() + "=" + ctx.LITERAL());
			}
		}else { // dec_spec IDENT '[' LITERAL ']' type_spec 
			newTexts.put(ctx, printDot(count)+ctx.dec_spec() +" "+ctx.IDENT()+"["+ctx.LITERAL() + "]" + ctx.type_spec());
		}
	}

	public void exitParams(MiniGoParser.ParamsContext ctx) { // params
		if(ctx.getChildCount() > 1) { // param(',' param)*
			for(int i =0;ctx.param(i+1) != null;i++) { // �������ϱ� �ݺ������� 
				newTexts.put(ctx,newTexts.get(ctx.param(i)) + "," + newTexts.get(ctx.param(i+1)));
			}
		}else if(ctx.getChildCount() == 1){ // �Ķ���� �Ѱ� 
			newTexts.put(ctx,newTexts.get(ctx.param(0)));
		}else { // �Ķ���� ���� ��
			newTexts.put(ctx,"");
		}
	}
	
	public void exitParam(MiniGoParser.ParamContext ctx) {  // param
		if(ctx.getChildCount() == 2){	// IDENT type_spec
			newTexts.put(ctx,ctx.getChild(0) + " " + ctx.getChild(1).getChild(0));
		}else { // IDENT
			newTexts.put(ctx,ctx.getChild(0).getText());
		}
	}
	
	public void exitArgs(MiniGoParser.ArgsContext ctx) { // args
		if(ctx.getChildCount() > 1) { // expr (',' expr) *
			for(int i =0;ctx.expr(i+1) != null;i++) { // �������ϱ� �ݺ������� 
				newTexts.put(ctx,newTexts.get(ctx.expr(i)) + "," + newTexts.get(ctx.expr(i+1)));
			}
		}else if(ctx.getChildCount() == 1){ // ���� �Ѱ� 
			newTexts.put(ctx,newTexts.get(ctx.expr(0)));
		}else { // �Ķ���� ���� ��
			newTexts.put(ctx,"");
		}
	}
	
}

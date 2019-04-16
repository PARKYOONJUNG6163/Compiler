// Generated from MiniGo.g4 by ANTLR 4.4
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MiniGoParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.4", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__18=1, T__17=2, T__16=3, T__15=4, T__14=5, T__13=6, T__12=7, T__11=8, 
		T__10=9, T__9=10, T__8=11, T__7=12, T__6=13, T__5=14, T__4=15, T__3=16, 
		T__2=17, T__1=18, T__0=19, VAR=20, FUNC=21, FMT=22, FOR=23, IF=24, ELSE=25, 
		RETURN=26, OR=27, AND=28, LE=29, GE=30, EQ=31, NE=32, VOID=33, FLOAT32=34, 
		FLOAT64=35, INT64=36, INT32=37, INT16=38, IDENT=39, LITERAL=40, DecimalConstant=41, 
		OctalConstant=42, HexadecimalConstant=43, RealConstant=44, WS=45;
	public static final String[] tokenNames = {
		"<INVALID>", "'/'", "'{'", "'['", "'<'", "'++'", "'--'", "'}'", "'='", 
		"']'", "'>'", "'!'", "'%'", "'('", "')'", "'*'", "'+'", "','", "'-'", 
		"'.'", "'var'", "'func'", "'fmt'", "'for'", "'if'", "'else'", "'return'", 
		"'or'", "'and'", "'<='", "'>='", "'=='", "'!='", "'void'", "'float32'", 
		"'float64'", "'int64'", "'int32'", "'int16'", "IDENT", "LITERAL", "DecimalConstant", 
		"OctalConstant", "HexadecimalConstant", "RealConstant", "WS"
	};
	public static final int
		RULE_program = 0, RULE_decl = 1, RULE_var_decl = 2, RULE_type_spec = 3, 
		RULE_fun_decl = 4, RULE_params = 5, RULE_param = 6, RULE_stmt = 7, RULE_expr_stmt = 8, 
		RULE_assign_stmt = 9, RULE_compound_stmt = 10, RULE_if_stmt = 11, RULE_for_stmt = 12, 
		RULE_return_stmt = 13, RULE_local_decl = 14, RULE_expr = 15, RULE_args = 16;
	public static final String[] ruleNames = {
		"program", "decl", "var_decl", "type_spec", "fun_decl", "params", "param", 
		"stmt", "expr_stmt", "assign_stmt", "compound_stmt", "if_stmt", "for_stmt", 
		"return_stmt", "local_decl", "expr", "args"
	};

	@Override
	public String getGrammarFileName() { return "MiniGo.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public MiniGoParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class ProgramContext extends ParserRuleContext {
		public DeclContext decl(int i) {
			return getRuleContext(DeclContext.class,i);
		}
		public List<DeclContext> decl() {
			return getRuleContexts(DeclContext.class);
		}
		public ProgramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_program; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MiniGoListener ) ((MiniGoListener)listener).enterProgram(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MiniGoListener ) ((MiniGoListener)listener).exitProgram(this);
		}
	}

	public final ProgramContext program() throws RecognitionException {
		ProgramContext _localctx = new ProgramContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_program);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(35); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(34); decl();
				}
				}
				setState(37); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==VAR || _la==FUNC );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DeclContext extends ParserRuleContext {
		public Fun_declContext fun_decl() {
			return getRuleContext(Fun_declContext.class,0);
		}
		public Var_declContext var_decl() {
			return getRuleContext(Var_declContext.class,0);
		}
		public DeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_decl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MiniGoListener ) ((MiniGoListener)listener).enterDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MiniGoListener ) ((MiniGoListener)listener).exitDecl(this);
		}
	}

	public final DeclContext decl() throws RecognitionException {
		DeclContext _localctx = new DeclContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_decl);
		try {
			setState(41);
			switch (_input.LA(1)) {
			case VAR:
				enterOuterAlt(_localctx, 1);
				{
				setState(39); var_decl();
				}
				break;
			case FUNC:
				enterOuterAlt(_localctx, 2);
				{
				setState(40); fun_decl();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Var_declContext extends ParserRuleContext {
		public TerminalNode VAR() { return getToken(MiniGoParser.VAR, 0); }
		public Type_specContext type_spec() {
			return getRuleContext(Type_specContext.class,0);
		}
		public TerminalNode IDENT(int i) {
			return getToken(MiniGoParser.IDENT, i);
		}
		public TerminalNode LITERAL() { return getToken(MiniGoParser.LITERAL, 0); }
		public List<TerminalNode> IDENT() { return getTokens(MiniGoParser.IDENT); }
		public Var_declContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_var_decl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MiniGoListener ) ((MiniGoListener)listener).enterVar_decl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MiniGoListener ) ((MiniGoListener)listener).exitVar_decl(this);
		}
	}

	public final Var_declContext var_decl() throws RecognitionException {
		Var_declContext _localctx = new Var_declContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_var_decl);
		try {
			setState(57);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(43); match(VAR);
				setState(44); match(IDENT);
				setState(45); type_spec();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(46); match(VAR);
				setState(47); match(IDENT);
				setState(48); match(T__2);
				setState(49); match(IDENT);
				setState(50); type_spec();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(51); match(VAR);
				setState(52); match(IDENT);
				setState(53); match(T__16);
				setState(54); match(LITERAL);
				setState(55); match(T__10);
				setState(56); type_spec();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Type_specContext extends ParserRuleContext {
		public TerminalNode INT16() { return getToken(MiniGoParser.INT16, 0); }
		public TerminalNode INT64() { return getToken(MiniGoParser.INT64, 0); }
		public TerminalNode FLOAT64() { return getToken(MiniGoParser.FLOAT64, 0); }
		public TerminalNode FLOAT32() { return getToken(MiniGoParser.FLOAT32, 0); }
		public TerminalNode VOID() { return getToken(MiniGoParser.VOID, 0); }
		public TerminalNode INT32() { return getToken(MiniGoParser.INT32, 0); }
		public Type_specContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type_spec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MiniGoListener ) ((MiniGoListener)listener).enterType_spec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MiniGoListener ) ((MiniGoListener)listener).exitType_spec(this);
		}
	}

	public final Type_specContext type_spec() throws RecognitionException {
		Type_specContext _localctx = new Type_specContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_type_spec);
		try {
			setState(66);
			switch (_input.LA(1)) {
			case INT16:
				enterOuterAlt(_localctx, 1);
				{
				setState(59); match(INT16);
				}
				break;
			case INT32:
				enterOuterAlt(_localctx, 2);
				{
				setState(60); match(INT32);
				}
				break;
			case INT64:
				enterOuterAlt(_localctx, 3);
				{
				setState(61); match(INT64);
				}
				break;
			case FLOAT32:
				enterOuterAlt(_localctx, 4);
				{
				setState(62); match(FLOAT32);
				}
				break;
			case FLOAT64:
				enterOuterAlt(_localctx, 5);
				{
				setState(63); match(FLOAT64);
				}
				break;
			case VOID:
				enterOuterAlt(_localctx, 6);
				{
				setState(64); match(VOID);
				}
				break;
			case EOF:
			case T__17:
			case T__14:
			case T__13:
			case T__12:
			case T__11:
			case T__8:
			case T__6:
			case T__5:
			case T__3:
			case T__2:
			case T__1:
			case VAR:
			case FUNC:
			case FMT:
			case FOR:
			case IF:
			case RETURN:
			case IDENT:
			case LITERAL:
				enterOuterAlt(_localctx, 7);
				{
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Fun_declContext extends ParserRuleContext {
		public ParamsContext params() {
			return getRuleContext(ParamsContext.class,0);
		}
		public List<Type_specContext> type_spec() {
			return getRuleContexts(Type_specContext.class);
		}
		public Type_specContext type_spec(int i) {
			return getRuleContext(Type_specContext.class,i);
		}
		public TerminalNode FUNC() { return getToken(MiniGoParser.FUNC, 0); }
		public TerminalNode IDENT() { return getToken(MiniGoParser.IDENT, 0); }
		public Compound_stmtContext compound_stmt() {
			return getRuleContext(Compound_stmtContext.class,0);
		}
		public Fun_declContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fun_decl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MiniGoListener ) ((MiniGoListener)listener).enterFun_decl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MiniGoListener ) ((MiniGoListener)listener).exitFun_decl(this);
		}
	}

	public final Fun_declContext fun_decl() throws RecognitionException {
		Fun_declContext _localctx = new Fun_declContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_fun_decl);
		try {
			setState(88);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(68); match(FUNC);
				setState(69); match(IDENT);
				setState(70); match(T__6);
				setState(71); params();
				setState(72); match(T__5);
				setState(73); type_spec();
				setState(74); compound_stmt();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(76); match(FUNC);
				setState(77); match(IDENT);
				setState(78); match(T__6);
				setState(79); params();
				setState(80); match(T__5);
				setState(81); match(T__6);
				setState(82); type_spec();
				setState(83); match(T__2);
				setState(84); type_spec();
				setState(85); match(T__5);
				setState(86); compound_stmt();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParamsContext extends ParserRuleContext {
		public List<ParamContext> param() {
			return getRuleContexts(ParamContext.class);
		}
		public ParamContext param(int i) {
			return getRuleContext(ParamContext.class,i);
		}
		public ParamsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_params; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MiniGoListener ) ((MiniGoListener)listener).enterParams(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MiniGoListener ) ((MiniGoListener)listener).exitParams(this);
		}
	}

	public final ParamsContext params() throws RecognitionException {
		ParamsContext _localctx = new ParamsContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_params);
		int _la;
		try {
			setState(99);
			switch (_input.LA(1)) {
			case T__5:
				enterOuterAlt(_localctx, 1);
				{
				}
				break;
			case IDENT:
				enterOuterAlt(_localctx, 2);
				{
				setState(91); param();
				setState(96);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__2) {
					{
					{
					setState(92); match(T__2);
					setState(93); param();
					}
					}
					setState(98);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParamContext extends ParserRuleContext {
		public Type_specContext type_spec() {
			return getRuleContext(Type_specContext.class,0);
		}
		public TerminalNode IDENT() { return getToken(MiniGoParser.IDENT, 0); }
		public ParamContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_param; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MiniGoListener ) ((MiniGoListener)listener).enterParam(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MiniGoListener ) ((MiniGoListener)listener).exitParam(this);
		}
	}

	public final ParamContext param() throws RecognitionException {
		ParamContext _localctx = new ParamContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_param);
		try {
			setState(107);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(101); match(IDENT);
				setState(102); type_spec();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(103); match(IDENT);
				setState(104); match(T__16);
				setState(105); match(T__10);
				setState(106); type_spec();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StmtContext extends ParserRuleContext {
		public Assign_stmtContext assign_stmt() {
			return getRuleContext(Assign_stmtContext.class,0);
		}
		public For_stmtContext for_stmt() {
			return getRuleContext(For_stmtContext.class,0);
		}
		public If_stmtContext if_stmt() {
			return getRuleContext(If_stmtContext.class,0);
		}
		public Return_stmtContext return_stmt() {
			return getRuleContext(Return_stmtContext.class,0);
		}
		public Compound_stmtContext compound_stmt() {
			return getRuleContext(Compound_stmtContext.class,0);
		}
		public Expr_stmtContext expr_stmt() {
			return getRuleContext(Expr_stmtContext.class,0);
		}
		public StmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MiniGoListener ) ((MiniGoListener)listener).enterStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MiniGoListener ) ((MiniGoListener)listener).exitStmt(this);
		}
	}

	public final StmtContext stmt() throws RecognitionException {
		StmtContext _localctx = new StmtContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_stmt);
		try {
			setState(115);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(109); expr_stmt();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(110); compound_stmt();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(111); assign_stmt();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(112); if_stmt();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(113); for_stmt();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(114); return_stmt();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Expr_stmtContext extends ParserRuleContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public Expr_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MiniGoListener ) ((MiniGoListener)listener).enterExpr_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MiniGoListener ) ((MiniGoListener)listener).exitExpr_stmt(this);
		}
	}

	public final Expr_stmtContext expr_stmt() throws RecognitionException {
		Expr_stmtContext _localctx = new Expr_stmtContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_expr_stmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(117); expr(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Assign_stmtContext extends ParserRuleContext {
		public TerminalNode VAR() { return getToken(MiniGoParser.VAR, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public Type_specContext type_spec() {
			return getRuleContext(Type_specContext.class,0);
		}
		public TerminalNode IDENT(int i) {
			return getToken(MiniGoParser.IDENT, i);
		}
		public TerminalNode LITERAL(int i) {
			return getToken(MiniGoParser.LITERAL, i);
		}
		public List<TerminalNode> LITERAL() { return getTokens(MiniGoParser.LITERAL); }
		public List<TerminalNode> IDENT() { return getTokens(MiniGoParser.IDENT); }
		public Assign_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assign_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MiniGoListener ) ((MiniGoListener)listener).enterAssign_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MiniGoListener ) ((MiniGoListener)listener).exitAssign_stmt(this);
		}
	}

	public final Assign_stmtContext assign_stmt() throws RecognitionException {
		Assign_stmtContext _localctx = new Assign_stmtContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_assign_stmt);
		try {
			setState(147);
			switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(119); match(VAR);
				setState(120); match(IDENT);
				setState(121); match(T__2);
				setState(122); match(IDENT);
				setState(123); type_spec();
				setState(124); match(T__11);
				setState(125); match(LITERAL);
				setState(126); match(T__2);
				setState(127); match(LITERAL);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(129); match(VAR);
				setState(130); match(IDENT);
				setState(131); type_spec();
				setState(132); match(T__11);
				setState(133); expr(0);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(135); match(IDENT);
				setState(136); type_spec();
				setState(137); match(T__11);
				setState(138); expr(0);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(140); match(IDENT);
				setState(141); match(T__16);
				setState(142); expr(0);
				setState(143); match(T__10);
				setState(144); match(T__11);
				setState(145); expr(0);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Compound_stmtContext extends ParserRuleContext {
		public List<Local_declContext> local_decl() {
			return getRuleContexts(Local_declContext.class);
		}
		public StmtContext stmt(int i) {
			return getRuleContext(StmtContext.class,i);
		}
		public List<StmtContext> stmt() {
			return getRuleContexts(StmtContext.class);
		}
		public Local_declContext local_decl(int i) {
			return getRuleContext(Local_declContext.class,i);
		}
		public Compound_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compound_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MiniGoListener ) ((MiniGoListener)listener).enterCompound_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MiniGoListener ) ((MiniGoListener)listener).exitCompound_stmt(this);
		}
	}

	public final Compound_stmtContext compound_stmt() throws RecognitionException {
		Compound_stmtContext _localctx = new Compound_stmtContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_compound_stmt);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(149); match(T__17);
			setState(153);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(150); local_decl();
					}
					} 
				}
				setState(155);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
			}
			setState(159);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__17) | (1L << T__14) | (1L << T__13) | (1L << T__8) | (1L << T__6) | (1L << T__3) | (1L << T__1) | (1L << VAR) | (1L << FMT) | (1L << FOR) | (1L << IF) | (1L << RETURN) | (1L << IDENT) | (1L << LITERAL))) != 0)) {
				{
				{
				setState(156); stmt();
				}
				}
				setState(161);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(162); match(T__12);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class If_stmtContext extends ParserRuleContext {
		public Compound_stmtContext compound_stmt(int i) {
			return getRuleContext(Compound_stmtContext.class,i);
		}
		public TerminalNode ELSE() { return getToken(MiniGoParser.ELSE, 0); }
		public TerminalNode IF() { return getToken(MiniGoParser.IF, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public List<Compound_stmtContext> compound_stmt() {
			return getRuleContexts(Compound_stmtContext.class);
		}
		public If_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_if_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MiniGoListener ) ((MiniGoListener)listener).enterIf_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MiniGoListener ) ((MiniGoListener)listener).exitIf_stmt(this);
		}
	}

	public final If_stmtContext if_stmt() throws RecognitionException {
		If_stmtContext _localctx = new If_stmtContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_if_stmt);
		try {
			setState(174);
			switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(164); match(IF);
				setState(165); expr(0);
				setState(166); compound_stmt();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(168); match(IF);
				setState(169); expr(0);
				setState(170); compound_stmt();
				setState(171); match(ELSE);
				setState(172); compound_stmt();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class For_stmtContext extends ParserRuleContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode FOR() { return getToken(MiniGoParser.FOR, 0); }
		public Compound_stmtContext compound_stmt() {
			return getRuleContext(Compound_stmtContext.class,0);
		}
		public For_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_for_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MiniGoListener ) ((MiniGoListener)listener).enterFor_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MiniGoListener ) ((MiniGoListener)listener).exitFor_stmt(this);
		}
	}

	public final For_stmtContext for_stmt() throws RecognitionException {
		For_stmtContext _localctx = new For_stmtContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_for_stmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(176); match(FOR);
			setState(177); expr(0);
			setState(178); compound_stmt();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Return_stmtContext extends ParserRuleContext {
		public TerminalNode RETURN() { return getToken(MiniGoParser.RETURN, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public Return_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_return_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MiniGoListener ) ((MiniGoListener)listener).enterReturn_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MiniGoListener ) ((MiniGoListener)listener).exitReturn_stmt(this);
		}
	}

	public final Return_stmtContext return_stmt() throws RecognitionException {
		Return_stmtContext _localctx = new Return_stmtContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_return_stmt);
		try {
			setState(188);
			switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(180); match(RETURN);
				setState(181); expr(0);
				setState(182); match(T__2);
				setState(183); expr(0);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(185); match(RETURN);
				setState(186); expr(0);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(187); match(RETURN);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Local_declContext extends ParserRuleContext {
		public TerminalNode VAR() { return getToken(MiniGoParser.VAR, 0); }
		public Type_specContext type_spec() {
			return getRuleContext(Type_specContext.class,0);
		}
		public TerminalNode LITERAL() { return getToken(MiniGoParser.LITERAL, 0); }
		public TerminalNode IDENT() { return getToken(MiniGoParser.IDENT, 0); }
		public Local_declContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_local_decl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MiniGoListener ) ((MiniGoListener)listener).enterLocal_decl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MiniGoListener ) ((MiniGoListener)listener).exitLocal_decl(this);
		}
	}

	public final Local_declContext local_decl() throws RecognitionException {
		Local_declContext _localctx = new Local_declContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_local_decl);
		try {
			setState(199);
			switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(190); match(VAR);
				setState(191); match(IDENT);
				setState(192); type_spec();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(193); match(VAR);
				setState(194); match(IDENT);
				setState(195); match(T__16);
				setState(196); match(LITERAL);
				setState(197); match(T__10);
				setState(198); type_spec();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExprContext extends ParserRuleContext {
		public ExprContext left;
		public Token op;
		public ExprContext right;
		public TerminalNode GE() { return getToken(MiniGoParser.GE, 0); }
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode LITERAL(int i) {
			return getToken(MiniGoParser.LITERAL, i);
		}
		public TerminalNode LE() { return getToken(MiniGoParser.LE, 0); }
		public TerminalNode FMT() { return getToken(MiniGoParser.FMT, 0); }
		public TerminalNode OR() { return getToken(MiniGoParser.OR, 0); }
		public List<TerminalNode> LITERAL() { return getTokens(MiniGoParser.LITERAL); }
		public TerminalNode IDENT() { return getToken(MiniGoParser.IDENT, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public TerminalNode AND() { return getToken(MiniGoParser.AND, 0); }
		public TerminalNode EQ() { return getToken(MiniGoParser.EQ, 0); }
		public TerminalNode NE() { return getToken(MiniGoParser.NE, 0); }
		public ArgsContext args() {
			return getRuleContext(ArgsContext.class,0);
		}
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MiniGoListener ) ((MiniGoListener)listener).enterExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MiniGoListener ) ((MiniGoListener)listener).exitExpr(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		return expr(0);
	}

	private ExprContext expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExprContext _localctx = new ExprContext(_ctx, _parentState);
		ExprContext _prevctx = _localctx;
		int _startState = 30;
		enterRecursionRule(_localctx, 30, RULE_expr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(239);
			switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
			case 1:
				{
				setState(202);
				((ExprContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__14) | (1L << T__13) | (1L << T__8) | (1L << T__3) | (1L << T__1))) != 0)) ) {
					((ExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				consume();
				setState(203); expr(7);
				}
				break;
			case 2:
				{
				setState(204); match(IDENT);
				setState(205); match(T__11);
				setState(206); expr(2);
				}
				break;
			case 3:
				{
				setState(207);
				_la = _input.LA(1);
				if ( !(_la==IDENT || _la==LITERAL) ) {
				_errHandler.recoverInline(this);
				}
				consume();
				}
				break;
			case 4:
				{
				setState(208); match(T__6);
				setState(209); expr(0);
				setState(210); match(T__5);
				}
				break;
			case 5:
				{
				setState(212); match(IDENT);
				setState(213); match(T__16);
				setState(214); expr(0);
				setState(215); match(T__10);
				}
				break;
			case 6:
				{
				setState(217); match(IDENT);
				setState(218); match(T__6);
				setState(219); args();
				setState(220); match(T__5);
				}
				break;
			case 7:
				{
				setState(222); match(FMT);
				setState(223); match(T__0);
				setState(224); match(IDENT);
				setState(225); match(T__6);
				setState(226); args();
				setState(227); match(T__5);
				}
				break;
			case 8:
				{
				setState(229); match(LITERAL);
				setState(230); match(T__2);
				setState(231); match(LITERAL);
				}
				break;
			case 9:
				{
				setState(232); match(IDENT);
				setState(233); match(T__16);
				setState(234); expr(0);
				setState(235); match(T__10);
				setState(236); match(T__11);
				setState(237); expr(0);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(252);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,17,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(250);
					switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
					case 1:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						_localctx.left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(241);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(242);
						((ExprContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__18) | (1L << T__7) | (1L << T__4))) != 0)) ) {
							((ExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						consume();
						setState(243); ((ExprContext)_localctx).right = expr(7);
						}
						break;
					case 2:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						_localctx.left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(244);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(245);
						((ExprContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__3 || _la==T__1) ) {
							((ExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						consume();
						setState(246); ((ExprContext)_localctx).right = expr(6);
						}
						break;
					case 3:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						_localctx.left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(247);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(248);
						((ExprContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__15) | (1L << T__9) | (1L << OR) | (1L << AND) | (1L << LE) | (1L << GE) | (1L << EQ) | (1L << NE))) != 0)) ) {
							((ExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						consume();
						setState(249); ((ExprContext)_localctx).right = expr(5);
						}
						break;
					}
					} 
				}
				setState(254);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,17,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class ArgsContext extends ParserRuleContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public ArgsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_args; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MiniGoListener ) ((MiniGoListener)listener).enterArgs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MiniGoListener ) ((MiniGoListener)listener).exitArgs(this);
		}
	}

	public final ArgsContext args() throws RecognitionException {
		ArgsContext _localctx = new ArgsContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_args);
		int _la;
		try {
			setState(264);
			switch (_input.LA(1)) {
			case T__14:
			case T__13:
			case T__8:
			case T__6:
			case T__3:
			case T__1:
			case FMT:
			case IDENT:
			case LITERAL:
				enterOuterAlt(_localctx, 1);
				{
				setState(255); expr(0);
				setState(260);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__2) {
					{
					{
					setState(256); match(T__2);
					setState(257); expr(0);
					}
					}
					setState(262);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case T__5:
				enterOuterAlt(_localctx, 2);
				{
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 15: return expr_sempred((ExprContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0: return precpred(_ctx, 6);
		case 1: return precpred(_ctx, 5);
		case 2: return precpred(_ctx, 4);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3/\u010d\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\3\2\6\2&\n\2\r\2\16\2\'\3\3\3\3\5\3,\n\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4"+
		"\3\4\3\4\3\4\3\4\3\4\3\4\3\4\5\4<\n\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\5\5"+
		"E\n\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6"+
		"\3\6\3\6\3\6\3\6\5\6[\n\6\3\7\3\7\3\7\3\7\7\7a\n\7\f\7\16\7d\13\7\5\7"+
		"f\n\7\3\b\3\b\3\b\3\b\3\b\3\b\5\bn\n\b\3\t\3\t\3\t\3\t\3\t\3\t\5\tv\n"+
		"\t\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13"+
		"\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13"+
		"\3\13\3\13\5\13\u0096\n\13\3\f\3\f\7\f\u009a\n\f\f\f\16\f\u009d\13\f\3"+
		"\f\7\f\u00a0\n\f\f\f\16\f\u00a3\13\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r\3"+
		"\r\3\r\3\r\3\r\5\r\u00b1\n\r\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3"+
		"\17\3\17\3\17\3\17\5\17\u00bf\n\17\3\20\3\20\3\20\3\20\3\20\3\20\3\20"+
		"\3\20\3\20\5\20\u00ca\n\20\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21"+
		"\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21"+
		"\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21"+
		"\3\21\5\21\u00f2\n\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\7\21"+
		"\u00fd\n\21\f\21\16\21\u0100\13\21\3\22\3\22\3\22\7\22\u0105\n\22\f\22"+
		"\16\22\u0108\13\22\3\22\5\22\u010b\n\22\3\22\2\3 \23\2\4\6\b\n\f\16\20"+
		"\22\24\26\30\32\34\36 \"\2\7\6\2\7\b\r\r\22\22\24\24\3\2)*\5\2\3\3\16"+
		"\16\21\21\4\2\22\22\24\24\5\2\6\6\f\f\35\"\u0124\2%\3\2\2\2\4+\3\2\2\2"+
		"\6;\3\2\2\2\bD\3\2\2\2\nZ\3\2\2\2\fe\3\2\2\2\16m\3\2\2\2\20u\3\2\2\2\22"+
		"w\3\2\2\2\24\u0095\3\2\2\2\26\u0097\3\2\2\2\30\u00b0\3\2\2\2\32\u00b2"+
		"\3\2\2\2\34\u00be\3\2\2\2\36\u00c9\3\2\2\2 \u00f1\3\2\2\2\"\u010a\3\2"+
		"\2\2$&\5\4\3\2%$\3\2\2\2&\'\3\2\2\2\'%\3\2\2\2\'(\3\2\2\2(\3\3\2\2\2)"+
		",\5\6\4\2*,\5\n\6\2+)\3\2\2\2+*\3\2\2\2,\5\3\2\2\2-.\7\26\2\2./\7)\2\2"+
		"/<\5\b\5\2\60\61\7\26\2\2\61\62\7)\2\2\62\63\7\23\2\2\63\64\7)\2\2\64"+
		"<\5\b\5\2\65\66\7\26\2\2\66\67\7)\2\2\678\7\5\2\289\7*\2\29:\7\13\2\2"+
		":<\5\b\5\2;-\3\2\2\2;\60\3\2\2\2;\65\3\2\2\2<\7\3\2\2\2=E\7(\2\2>E\7\'"+
		"\2\2?E\7&\2\2@E\7$\2\2AE\7%\2\2BE\7#\2\2CE\3\2\2\2D=\3\2\2\2D>\3\2\2\2"+
		"D?\3\2\2\2D@\3\2\2\2DA\3\2\2\2DB\3\2\2\2DC\3\2\2\2E\t\3\2\2\2FG\7\27\2"+
		"\2GH\7)\2\2HI\7\17\2\2IJ\5\f\7\2JK\7\20\2\2KL\5\b\5\2LM\5\26\f\2M[\3\2"+
		"\2\2NO\7\27\2\2OP\7)\2\2PQ\7\17\2\2QR\5\f\7\2RS\7\20\2\2ST\7\17\2\2TU"+
		"\5\b\5\2UV\7\23\2\2VW\5\b\5\2WX\7\20\2\2XY\5\26\f\2Y[\3\2\2\2ZF\3\2\2"+
		"\2ZN\3\2\2\2[\13\3\2\2\2\\f\3\2\2\2]b\5\16\b\2^_\7\23\2\2_a\5\16\b\2`"+
		"^\3\2\2\2ad\3\2\2\2b`\3\2\2\2bc\3\2\2\2cf\3\2\2\2db\3\2\2\2e\\\3\2\2\2"+
		"e]\3\2\2\2f\r\3\2\2\2gh\7)\2\2hn\5\b\5\2ij\7)\2\2jk\7\5\2\2kl\7\13\2\2"+
		"ln\5\b\5\2mg\3\2\2\2mi\3\2\2\2n\17\3\2\2\2ov\5\22\n\2pv\5\26\f\2qv\5\24"+
		"\13\2rv\5\30\r\2sv\5\32\16\2tv\5\34\17\2uo\3\2\2\2up\3\2\2\2uq\3\2\2\2"+
		"ur\3\2\2\2us\3\2\2\2ut\3\2\2\2v\21\3\2\2\2wx\5 \21\2x\23\3\2\2\2yz\7\26"+
		"\2\2z{\7)\2\2{|\7\23\2\2|}\7)\2\2}~\5\b\5\2~\177\7\n\2\2\177\u0080\7*"+
		"\2\2\u0080\u0081\7\23\2\2\u0081\u0082\7*\2\2\u0082\u0096\3\2\2\2\u0083"+
		"\u0084\7\26\2\2\u0084\u0085\7)\2\2\u0085\u0086\5\b\5\2\u0086\u0087\7\n"+
		"\2\2\u0087\u0088\5 \21\2\u0088\u0096\3\2\2\2\u0089\u008a\7)\2\2\u008a"+
		"\u008b\5\b\5\2\u008b\u008c\7\n\2\2\u008c\u008d\5 \21\2\u008d\u0096\3\2"+
		"\2\2\u008e\u008f\7)\2\2\u008f\u0090\7\5\2\2\u0090\u0091\5 \21\2\u0091"+
		"\u0092\7\13\2\2\u0092\u0093\7\n\2\2\u0093\u0094\5 \21\2\u0094\u0096\3"+
		"\2\2\2\u0095y\3\2\2\2\u0095\u0083\3\2\2\2\u0095\u0089\3\2\2\2\u0095\u008e"+
		"\3\2\2\2\u0096\25\3\2\2\2\u0097\u009b\7\4\2\2\u0098\u009a\5\36\20\2\u0099"+
		"\u0098\3\2\2\2\u009a\u009d\3\2\2\2\u009b\u0099\3\2\2\2\u009b\u009c\3\2"+
		"\2\2\u009c\u00a1\3\2\2\2\u009d\u009b\3\2\2\2\u009e\u00a0\5\20\t\2\u009f"+
		"\u009e\3\2\2\2\u00a0\u00a3\3\2\2\2\u00a1\u009f\3\2\2\2\u00a1\u00a2\3\2"+
		"\2\2\u00a2\u00a4\3\2\2\2\u00a3\u00a1\3\2\2\2\u00a4\u00a5\7\t\2\2\u00a5"+
		"\27\3\2\2\2\u00a6\u00a7\7\32\2\2\u00a7\u00a8\5 \21\2\u00a8\u00a9\5\26"+
		"\f\2\u00a9\u00b1\3\2\2\2\u00aa\u00ab\7\32\2\2\u00ab\u00ac\5 \21\2\u00ac"+
		"\u00ad\5\26\f\2\u00ad\u00ae\7\33\2\2\u00ae\u00af\5\26\f\2\u00af\u00b1"+
		"\3\2\2\2\u00b0\u00a6\3\2\2\2\u00b0\u00aa\3\2\2\2\u00b1\31\3\2\2\2\u00b2"+
		"\u00b3\7\31\2\2\u00b3\u00b4\5 \21\2\u00b4\u00b5\5\26\f\2\u00b5\33\3\2"+
		"\2\2\u00b6\u00b7\7\34\2\2\u00b7\u00b8\5 \21\2\u00b8\u00b9\7\23\2\2\u00b9"+
		"\u00ba\5 \21\2\u00ba\u00bf\3\2\2\2\u00bb\u00bc\7\34\2\2\u00bc\u00bf\5"+
		" \21\2\u00bd\u00bf\7\34\2\2\u00be\u00b6\3\2\2\2\u00be\u00bb\3\2\2\2\u00be"+
		"\u00bd\3\2\2\2\u00bf\35\3\2\2\2\u00c0\u00c1\7\26\2\2\u00c1\u00c2\7)\2"+
		"\2\u00c2\u00ca\5\b\5\2\u00c3\u00c4\7\26\2\2\u00c4\u00c5\7)\2\2\u00c5\u00c6"+
		"\7\5\2\2\u00c6\u00c7\7*\2\2\u00c7\u00c8\7\13\2\2\u00c8\u00ca\5\b\5\2\u00c9"+
		"\u00c0\3\2\2\2\u00c9\u00c3\3\2\2\2\u00ca\37\3\2\2\2\u00cb\u00cc\b\21\1"+
		"\2\u00cc\u00cd\t\2\2\2\u00cd\u00f2\5 \21\t\u00ce\u00cf\7)\2\2\u00cf\u00d0"+
		"\7\n\2\2\u00d0\u00f2\5 \21\4\u00d1\u00f2\t\3\2\2\u00d2\u00d3\7\17\2\2"+
		"\u00d3\u00d4\5 \21\2\u00d4\u00d5\7\20\2\2\u00d5\u00f2\3\2\2\2\u00d6\u00d7"+
		"\7)\2\2\u00d7\u00d8\7\5\2\2\u00d8\u00d9\5 \21\2\u00d9\u00da\7\13\2\2\u00da"+
		"\u00f2\3\2\2\2\u00db\u00dc\7)\2\2\u00dc\u00dd\7\17\2\2\u00dd\u00de\5\""+
		"\22\2\u00de\u00df\7\20\2\2\u00df\u00f2\3\2\2\2\u00e0\u00e1\7\30\2\2\u00e1"+
		"\u00e2\7\25\2\2\u00e2\u00e3\7)\2\2\u00e3\u00e4\7\17\2\2\u00e4\u00e5\5"+
		"\"\22\2\u00e5\u00e6\7\20\2\2\u00e6\u00f2\3\2\2\2\u00e7\u00e8\7*\2\2\u00e8"+
		"\u00e9\7\23\2\2\u00e9\u00f2\7*\2\2\u00ea\u00eb\7)\2\2\u00eb\u00ec\7\5"+
		"\2\2\u00ec\u00ed\5 \21\2\u00ed\u00ee\7\13\2\2\u00ee\u00ef\7\n\2\2\u00ef"+
		"\u00f0\5 \21\2\u00f0\u00f2\3\2\2\2\u00f1\u00cb\3\2\2\2\u00f1\u00ce\3\2"+
		"\2\2\u00f1\u00d1\3\2\2\2\u00f1\u00d2\3\2\2\2\u00f1\u00d6\3\2\2\2\u00f1"+
		"\u00db\3\2\2\2\u00f1\u00e0\3\2\2\2\u00f1\u00e7\3\2\2\2\u00f1\u00ea\3\2"+
		"\2\2\u00f2\u00fe\3\2\2\2\u00f3\u00f4\f\b\2\2\u00f4\u00f5\t\4\2\2\u00f5"+
		"\u00fd\5 \21\t\u00f6\u00f7\f\7\2\2\u00f7\u00f8\t\5\2\2\u00f8\u00fd\5 "+
		"\21\b\u00f9\u00fa\f\6\2\2\u00fa\u00fb\t\6\2\2\u00fb\u00fd\5 \21\7\u00fc"+
		"\u00f3\3\2\2\2\u00fc\u00f6\3\2\2\2\u00fc\u00f9\3\2\2\2\u00fd\u0100\3\2"+
		"\2\2\u00fe\u00fc\3\2\2\2\u00fe\u00ff\3\2\2\2\u00ff!\3\2\2\2\u0100\u00fe"+
		"\3\2\2\2\u0101\u0106\5 \21\2\u0102\u0103\7\23\2\2\u0103\u0105\5 \21\2"+
		"\u0104\u0102\3\2\2\2\u0105\u0108\3\2\2\2\u0106\u0104\3\2\2\2\u0106\u0107"+
		"\3\2\2\2\u0107\u010b\3\2\2\2\u0108\u0106\3\2\2\2\u0109\u010b\3\2\2\2\u010a"+
		"\u0101\3\2\2\2\u010a\u0109\3\2\2\2\u010b#\3\2\2\2\26\'+;DZbemu\u0095\u009b"+
		"\u00a1\u00b0\u00be\u00c9\u00f1\u00fc\u00fe\u0106\u010a";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
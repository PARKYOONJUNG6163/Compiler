import java.io.IOException;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class UcodeGenerator {
	static void minigo2ucode(String mgFile) throws IOException{
		MiniGoLexer lexer = new MiniGoLexer(CharStreams.fromFileName("test.go"));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		MiniGoParser parser = new MiniGoParser(tokens);
		ParseTree tree = parser.program();
		
		ParseTreeWalker walker = new ParseTreeWalker();
		walker.walk(new UCodeGenListener(), tree);
	}
	
	public static void main(String[] args) throws IOException {
		minigo2ucode("test.go");
	} // 프로그램을 실행시키기 위한 메인 함수
}

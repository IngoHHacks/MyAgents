// Generated from C:/Users/iheij/Documents/GitHub/MyAgents/src/main/antlr/EnvironmentLexer.g4 by ANTLR 4.13.2

package net.ingoh.myagents.lang.lexers;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class EnvironmentLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		ENVIRONMENT=1, AGENTS=2, TIME_LITERAL=3, TICK=4, PRINT=5, RETURN=6, BOOL_LITERAL=7, 
		TYPE=8, NAME=9, STRING_LITERAL=10, ID=11, LPAREN=12, RPAREN=13, LBRACE=14, 
		RBRACE=15, SEMICOLON=16, WS=17;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"ENVIRONMENT", "AGENTS", "TIME_LITERAL", "TICK", "PRINT", "RETURN", "BOOL_LITERAL", 
			"TYPE", "NAME", "STRING_LITERAL", "ID", "LPAREN", "RPAREN", "LBRACE", 
			"RBRACE", "SEMICOLON", "WS"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			"'('", "')'", "'{'", "'}'", "';'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "ENVIRONMENT", "AGENTS", "TIME_LITERAL", "TICK", "PRINT", "RETURN", 
			"BOOL_LITERAL", "TYPE", "NAME", "STRING_LITERAL", "ID", "LPAREN", "RPAREN", 
			"LBRACE", "RBRACE", "SEMICOLON", "WS"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public EnvironmentLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "EnvironmentLexer.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\u0004\u0000\u0011\u0085\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002"+
		"\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002"+
		"\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002"+
		"\u0007\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002"+
		"\u000b\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e"+
		"\u0002\u000f\u0007\u000f\u0002\u0010\u0007\u0010\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003"+
		"\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0003\u0006W\b\u0006\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001"+
		"\t\u0001\t\u0001\t\u0001\t\u0005\tg\b\t\n\t\f\tj\t\t\u0001\t\u0001\t\u0001"+
		"\n\u0001\n\u0005\np\b\n\n\n\f\ns\t\n\u0001\u000b\u0001\u000b\u0001\f\u0001"+
		"\f\u0001\r\u0001\r\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0001"+
		"\u0010\u0004\u0010\u0080\b\u0010\u000b\u0010\f\u0010\u0081\u0001\u0010"+
		"\u0001\u0010\u0000\u0000\u0011\u0001\u0001\u0003\u0002\u0005\u0003\u0007"+
		"\u0004\t\u0005\u000b\u0006\r\u0007\u000f\b\u0011\t\u0013\n\u0015\u000b"+
		"\u0017\f\u0019\r\u001b\u000e\u001d\u000f\u001f\u0010!\u0011\u0001\u0000"+
		"\u0016\u0002\u0000EEee\u0002\u0000NNnn\u0002\u0000VVvv\u0002\u0000IIi"+
		"i\u0002\u0000RRrr\u0002\u0000OOoo\u0002\u0000MMmm\u0002\u0000TTtt\u0002"+
		"\u0000AAaa\u0002\u0000GGgg\u0002\u0000SSss\u0002\u0000CCcc\u0002\u0000"+
		"KKkk\u0002\u0000PPpp\u0002\u0000UUuu\u0002\u0000FFff\u0002\u0000LLll\u0002"+
		"\u0000YYyy\u0002\u0000\"\"\\\\\u0003\u0000AZ__az\u0004\u000009AZ__az\u0003"+
		"\u0000\t\n\r\r  \u0089\u0000\u0001\u0001\u0000\u0000\u0000\u0000\u0003"+
		"\u0001\u0000\u0000\u0000\u0000\u0005\u0001\u0000\u0000\u0000\u0000\u0007"+
		"\u0001\u0000\u0000\u0000\u0000\t\u0001\u0000\u0000\u0000\u0000\u000b\u0001"+
		"\u0000\u0000\u0000\u0000\r\u0001\u0000\u0000\u0000\u0000\u000f\u0001\u0000"+
		"\u0000\u0000\u0000\u0011\u0001\u0000\u0000\u0000\u0000\u0013\u0001\u0000"+
		"\u0000\u0000\u0000\u0015\u0001\u0000\u0000\u0000\u0000\u0017\u0001\u0000"+
		"\u0000\u0000\u0000\u0019\u0001\u0000\u0000\u0000\u0000\u001b\u0001\u0000"+
		"\u0000\u0000\u0000\u001d\u0001\u0000\u0000\u0000\u0000\u001f\u0001\u0000"+
		"\u0000\u0000\u0000!\u0001\u0000\u0000\u0000\u0001#\u0001\u0000\u0000\u0000"+
		"\u0003/\u0001\u0000\u0000\u0000\u00056\u0001\u0000\u0000\u0000\u0007;"+
		"\u0001\u0000\u0000\u0000\t@\u0001\u0000\u0000\u0000\u000bF\u0001\u0000"+
		"\u0000\u0000\rV\u0001\u0000\u0000\u0000\u000fX\u0001\u0000\u0000\u0000"+
		"\u0011]\u0001\u0000\u0000\u0000\u0013b\u0001\u0000\u0000\u0000\u0015m"+
		"\u0001\u0000\u0000\u0000\u0017t\u0001\u0000\u0000\u0000\u0019v\u0001\u0000"+
		"\u0000\u0000\u001bx\u0001\u0000\u0000\u0000\u001dz\u0001\u0000\u0000\u0000"+
		"\u001f|\u0001\u0000\u0000\u0000!\u007f\u0001\u0000\u0000\u0000#$\u0007"+
		"\u0000\u0000\u0000$%\u0007\u0001\u0000\u0000%&\u0007\u0002\u0000\u0000"+
		"&\'\u0007\u0003\u0000\u0000\'(\u0007\u0004\u0000\u0000()\u0007\u0005\u0000"+
		"\u0000)*\u0007\u0001\u0000\u0000*+\u0007\u0006\u0000\u0000+,\u0007\u0000"+
		"\u0000\u0000,-\u0007\u0001\u0000\u0000-.\u0007\u0007\u0000\u0000.\u0002"+
		"\u0001\u0000\u0000\u0000/0\u0007\b\u0000\u000001\u0007\t\u0000\u00001"+
		"2\u0007\u0000\u0000\u000023\u0007\u0001\u0000\u000034\u0007\u0007\u0000"+
		"\u000045\u0007\n\u0000\u00005\u0004\u0001\u0000\u0000\u000067\u0007\u0007"+
		"\u0000\u000078\u0007\u0003\u0000\u000089\u0007\u0006\u0000\u00009:\u0007"+
		"\u0000\u0000\u0000:\u0006\u0001\u0000\u0000\u0000;<\u0007\u0007\u0000"+
		"\u0000<=\u0007\u0003\u0000\u0000=>\u0007\u000b\u0000\u0000>?\u0007\f\u0000"+
		"\u0000?\b\u0001\u0000\u0000\u0000@A\u0007\r\u0000\u0000AB\u0007\u0004"+
		"\u0000\u0000BC\u0007\u0003\u0000\u0000CD\u0007\u0001\u0000\u0000DE\u0007"+
		"\u0007\u0000\u0000E\n\u0001\u0000\u0000\u0000FG\u0007\u0004\u0000\u0000"+
		"GH\u0007\u0000\u0000\u0000HI\u0007\u0007\u0000\u0000IJ\u0007\u000e\u0000"+
		"\u0000JK\u0007\u0004\u0000\u0000KL\u0007\u0001\u0000\u0000L\f\u0001\u0000"+
		"\u0000\u0000MN\u0007\u0007\u0000\u0000NO\u0007\u0004\u0000\u0000OP\u0007"+
		"\u000e\u0000\u0000PW\u0007\u0000\u0000\u0000QR\u0007\u000f\u0000\u0000"+
		"RS\u0007\b\u0000\u0000ST\u0007\u0010\u0000\u0000TU\u0007\n\u0000\u0000"+
		"UW\u0007\u0000\u0000\u0000VM\u0001\u0000\u0000\u0000VQ\u0001\u0000\u0000"+
		"\u0000W\u000e\u0001\u0000\u0000\u0000XY\u0007\u0007\u0000\u0000YZ\u0007"+
		"\u0011\u0000\u0000Z[\u0007\r\u0000\u0000[\\\u0007\u0000\u0000\u0000\\"+
		"\u0010\u0001\u0000\u0000\u0000]^\u0007\u0001\u0000\u0000^_\u0007\b\u0000"+
		"\u0000_`\u0007\u0006\u0000\u0000`a\u0007\u0000\u0000\u0000a\u0012\u0001"+
		"\u0000\u0000\u0000bh\u0005\"\u0000\u0000cg\b\u0012\u0000\u0000de\u0005"+
		"\\\u0000\u0000eg\t\u0000\u0000\u0000fc\u0001\u0000\u0000\u0000fd\u0001"+
		"\u0000\u0000\u0000gj\u0001\u0000\u0000\u0000hf\u0001\u0000\u0000\u0000"+
		"hi\u0001\u0000\u0000\u0000ik\u0001\u0000\u0000\u0000jh\u0001\u0000\u0000"+
		"\u0000kl\u0005\"\u0000\u0000l\u0014\u0001\u0000\u0000\u0000mq\u0007\u0013"+
		"\u0000\u0000np\u0007\u0014\u0000\u0000on\u0001\u0000\u0000\u0000ps\u0001"+
		"\u0000\u0000\u0000qo\u0001\u0000\u0000\u0000qr\u0001\u0000\u0000\u0000"+
		"r\u0016\u0001\u0000\u0000\u0000sq\u0001\u0000\u0000\u0000tu\u0005(\u0000"+
		"\u0000u\u0018\u0001\u0000\u0000\u0000vw\u0005)\u0000\u0000w\u001a\u0001"+
		"\u0000\u0000\u0000xy\u0005{\u0000\u0000y\u001c\u0001\u0000\u0000\u0000"+
		"z{\u0005}\u0000\u0000{\u001e\u0001\u0000\u0000\u0000|}\u0005;\u0000\u0000"+
		"} \u0001\u0000\u0000\u0000~\u0080\u0007\u0015\u0000\u0000\u007f~\u0001"+
		"\u0000\u0000\u0000\u0080\u0081\u0001\u0000\u0000\u0000\u0081\u007f\u0001"+
		"\u0000\u0000\u0000\u0081\u0082\u0001\u0000\u0000\u0000\u0082\u0083\u0001"+
		"\u0000\u0000\u0000\u0083\u0084\u0006\u0010\u0000\u0000\u0084\"\u0001\u0000"+
		"\u0000\u0000\u0006\u0000Vfhq\u0081\u0001\u0006\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
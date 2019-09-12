/*
 *  The scanner definition for COOL.
 */
package compiler;
import java_cup.runtime.Symbol;

%%

%{

/*  Stuff enclosed in %{ %} is copied verbatim to the lexer class
 *  definition, all the extra variables/functions you want to use in the
 *  lexer actions should go here.  Don't remove or modify anything that
 *  was there initially.  */

    // Max size of string constants
    static int MAX_STR_CONST = 1025;

    // For assembling string constants
    StringBuffer string_buf = new StringBuffer();
    static boolean last_str_escape = false;
    
    /* Need to add to lex */
    static boolean str_stop = false;
    static String str_errmsg;
    static int comment_open_num = 0;


    private int curr_lineno = 1;
    int get_curr_lineno() {
	return curr_lineno;
    }

    private AbstractSymbol filename;

    void set_filename(String fname) {
	filename = AbstractTable.stringtable.addString(fname);
    }

    AbstractSymbol curr_filename() {
	return filename;
    }

    void clear_global_status() {
        string_buf.setLength(0);
        yybegin(YYINITIAL);
        str_errmsg = "";
        str_stop = false;
        last_str_escape = false;
    }
%}

%init{

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */

    // empty for now
%init}

%eofval{

/*  Stuff enclosed in %eofval{ %eofval} specifies java code that is
 *  executed when end-of-file is reached.  If you use multiple lexical
 *  states and want to do something special if an EOF is encountered in
 *  one of those states, place your code in the switch statement.
 *  Ultimately, you should return the EOF symbol, or your lexer won't
 *  work.  */

    switch(yy_lexical_state) {
        case YYINITIAL:
            /* nothing special to do in the initial state */
            break;
	    case COMMENTOPEN:
            yybegin(YYINITIAL);
            return new Symbol(TokenConstants.ERROR, "EOF in comment");
        case STROPEN:
            yybegin(YYINITIAL);
            return new Symbol(TokenConstants.ERROR, "EOF in string constant");
    }
    return new Symbol(TokenConstants.EOF);
%eofval}

%class CoolLexer
%cup
%notunix

%state STROPEN
%state COMMENTOPEN
%state LINECOMMENT
ALPHA=[A-Za-z]
DIGIT=[0-9]
NONNEWLINE_WHITE_SPACE_CHAR=[\ \t\b\r\f\x0b]

%%

<YYINITIAL> \n {
    curr_lineno++;
}

<YYINITIAL> {NONNEWLINE_WHITE_SPACE_CHAR}+ { 
}

<YYINITIAL> \"   {
    yybegin(STROPEN);
}

<YYINITIAL> "--" {
    yybegin(LINECOMMENT);
}

<LINECOMMENT> .* {
}

<LINECOMMENT> \n {
    yybegin(YYINITIAL);
    curr_lineno++;
}

<STROPEN> .|\n|\r|\f|\x1b {
    String text = yytext();
    if (string_buf.length() >= MAX_STR_CONST) {
        str_errmsg = "String constant too long";
        str_stop = true;
    }  
    if (text.length() == 1 && !last_str_escape && text.charAt(0) == '"') {
        if (str_stop) {
            Symbol symbol = new Symbol(TokenConstants.ERROR, str_errmsg);
            clear_global_status();
            return symbol;
        }
        else {
            Symbol symbol = new Symbol(TokenConstants.STR_CONST,
                        AbstractTable.stringtable.addString(string_buf.toString()));
            clear_global_status();
            return symbol;
        }
    }
    if (text.length() == 1 && text.charAt(0) == '\n') {
        curr_lineno++;
        if (last_str_escape) {
            string_buf.append('\n');
            last_str_escape = false;
        }else {
            Symbol symbol = new Symbol(TokenConstants.ERROR, "Unterminated string constant");
            clear_global_status();
            return symbol;
        }
    }else if (!str_stop) {
        if (text.length() == 1 && text.charAt(0) == '\0') {
            str_errmsg = "String contains null character";
            str_stop = true;
        } else if (last_str_escape) {
            switch (text.charAt(0)) {
            case 'n':
                string_buf.append('\n');
                break;
            case '0':
                string_buf.append('0');
                break;
            case 't':
                string_buf.append('\t');
                break;
            case 'f':
                string_buf.append('\f');
                break;
            case 'b':
                string_buf.append('\b');
                break;
            case '\\':
                string_buf.append('\\');
                break;
            case '\r':
                string_buf.append('\r');
                break;
            case (char)27:
                string_buf.append((char)27);
                break;
            default:
                string_buf.append(text.charAt(0));
            }
            // 上一次已经是转义,这一次不管怎么样转义的效果要消除
            if(last_str_escape){
                last_str_escape = false;
            }
        } else {
            if(yytext().charAt(0) == '\\'){
                last_str_escape = true;
            }else{
                string_buf.append(text);
            }
        }
    }
}

<YYINITIAL> (\\|\!|\#|\$|\_|>|%|\&|\?|\`|\x01|\x02|\x03|\x04|\|) {
    return new Symbol(TokenConstants.ERROR, yytext());
}

<YYINITIAL> "^" {
    return new Symbol(TokenConstants.ERROR, yytext());
}


<YYINITIAL> "(*"   {
    comment_open_num++;
    yybegin(COMMENTOPEN);
}

<COMMENTOPEN> "(*" {
    comment_open_num ++;
}

<COMMENTOPEN> \\|.|{NONNEWLINE_WHITE_SPACE_CHAR}|\\"*)" {
    // comment we do nothing
}

<COMMENTOPEN> \n {
    curr_lineno++;
}

<COMMENTOPEN> \\"(*" {
    // do nothing
}

<COMMENTOPEN> "*)"  {
    comment_open_num--;
    if(comment_open_num == 0){
        yybegin(YYINITIAL);
    }
}

<YYINITIAL> \"   {
    return new Symbol(TokenConstants.LPAREN);
}

<YYINITIAL> "("   {
    return new Symbol(TokenConstants.LPAREN);
}

<YYINITIAL> ")"   {
    return new Symbol(TokenConstants.RPAREN);
}

<YYINITIAL> ","   {
    return new Symbol(TokenConstants.COMMA);
}

<YYINITIAL> "."   {
    return new Symbol(TokenConstants.DOT);
}

<YYINITIAL> ";"   {
    return new Symbol(TokenConstants.SEMI);
}

<YYINITIAL> [c|C][a|A][s|S][e|E]  {
     return new Symbol(TokenConstants.CASE);
}

<YYINITIAL> "-"   {
    return new Symbol(TokenConstants.MINUS);
}


<YYINITIAL> [n|N][o|O][t|T]   {
    return new Symbol(TokenConstants.NOT);
}


<YYINITIAL> "<"   {
    return new Symbol(TokenConstants.LT);
}

<YYINITIAL> "*"   {
    return new Symbol(TokenConstants.MULT);
}

<YYINITIAL> [i|I][n|N][h|H][e|E][r|R][i|I][t|T][s|S]   {
    return new Symbol(TokenConstants.INHERITS);
}

<YYINITIAL> "-" {
    return new Symbol(TokenConstants.MINUS);
}

<YYINITIAL> [i|I][n|N] {
    return new Symbol(TokenConstants.IN);
}

<YYINITIAL> [c|C][l|L][a|A][s|S][s|S] {
    return new Symbol(TokenConstants.CLASS);
}

<YYINITIAL> [f|F][i|I] {
    return new Symbol(TokenConstants.FI);
}

<YYINITIAL> [l|L][o|O][o|O][p|P] {
    return new Symbol(TokenConstants.LOOP);
}

<YYINITIAL> "+" {
    return new Symbol(TokenConstants.PLUS);
}

<YYINITIAL> "<-" {
    return new Symbol(TokenConstants.ASSIGN);
}

<YYINITIAL> [i|I][f|F] {
    return new Symbol(TokenConstants.IF);
}

<YYINITIAL> "<=" {
    return new Symbol(TokenConstants.LE);
}

<YYINITIAL> [n|N][e|E][w|W] {
    return new Symbol(TokenConstants.NEW);
}

<YYINITIAL> [i|I][s|S][v|V][o|O][i|I][d|D] {
    return new Symbol(TokenConstants.ISVOID);
}

<YYINITIAL> "=" {
    return new Symbol(TokenConstants.EQ);
}

<YYINITIAL> ":" {
    return new Symbol(TokenConstants.COLON);
}

<YYINITIAL> "~" {
    return new Symbol(TokenConstants.NEG);
}

<YYINITIAL> "{" {
    return new Symbol(TokenConstants.LBRACE);
}

<YYINITIAL> "}" {
    return new Symbol(TokenConstants.RBRACE);
}

<YYINITIAL> [e|E][l|L][s|S][e|E] {
    return new Symbol(TokenConstants.ELSE);
}

<YYINITIAL> [w|W][h|H][i|I][l|L][e|E] {
    return new Symbol(TokenConstants.WHILE);
}

<YYINITIAL> [eE][sS][aA][cC] {
    return new Symbol(TokenConstants.ESAC);
}

<YYINITIAL> [lL][eE][tT] {
    return new Symbol(TokenConstants.LET);
}

<YYINITIAL> [tT][hH][eE][nN] {
    return new Symbol(TokenConstants.THEN);
}

<YYINITIAL> "@" {
    return new Symbol(TokenConstants.AT);
}

<YYINITIAL> "/" {
    return new Symbol(TokenConstants.DIV);
}

<YYINITIAL> [pP][oO][oO][lL] {
    return new Symbol(TokenConstants.POOL);
}

<YYINITIAL> [oO][fF] {
    return new Symbol(TokenConstants.OF);
}


<YYINITIAL> t[rR][uU][eE] {
    return new Symbol(TokenConstants.BOOL_CONST, new Boolean(true));
}

<YYINITIAL> f[aA][lL][sS][eE] {
    return new Symbol(TokenConstants.BOOL_CONST, new Boolean(false));
}

<YYINITIAL> [a-z]+[0-9a-zA-Z_]* {
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}

<YYINITIAL> [A-Z]+[0-9a-zA-Z_]* {
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}

<YYINITIAL> [0-9]+ {
    return new Symbol(TokenConstants.INT_CONST, AbstractTable.inttable.addString(yytext()));
}

<YYINITIAL> "*)" {
    return new Symbol(TokenConstants.ERROR, "Unmatched *)");
}


<YYINITIAL> T[rR][uU][eE] {
    return new Symbol(TokenConstants.ERROR, "The first character in true should not be capital letter");
}

<YYINITIAL> F[aA][lL][sS][eE] {
    return new Symbol(TokenConstants.ERROR, "The first character in false should not be capital letter");
}

<YYINITIAL>"=>" { /* Sample lexical rule for "=>" arrow.
                                     Further lexical rules should be defined
                                     here, after the last %% separator */
                                  return new Symbol(TokenConstants.DARROW); }

<YYINITIAL> [\'\[\]\>] {
    return new Symbol(TokenConstants.ERROR, yytext());
} 

<YYINITIAL> . { /* This rule should be the very last
    in your lexical specification and
    will match match everything not
    matched by other lexical rules. */
    System.err.println("LEXER BUG - UNMATCHED: " + yytext()); 
}

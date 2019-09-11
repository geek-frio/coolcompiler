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
    
    /* Need to add to lex */
    static boolean str_stop = false;
    static String str_errmsg;


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

     /**
     * 重置string字符串处理全局状态
     */
    void clear_global_status() {
        string_buf.setLength(0);
        yybegin(YYINITIAL);
        str_errmsg = "";
        str_stop = false;
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
ALPHA=[A-Za-z]
DIGIT=[0-9]
NONNEWLINE_WHITE_SPACE_CHAR=[\ \t\b]

%%

<YYINITIAL> \n {
    curr_lineno++;
}

<YYINITIAL> {NONNEWLINE_WHITE_SPACE_CHAR}+ { 
}

<YYINITIAL> \"   {
    yybegin(STROPEN);
}

<STROPEN> .|\n {
    String text = yytext();
    boolean last_str_escape = string_buf.length() >= 1 && string_buf.charAt(string_buf.length() - 1) == '\\';
    // 去掉string_buf中的尾部的字符\
    if (last_str_escape) {
        string_buf.setLength(string_buf.length() - 1);
    }
    // 是否已经到string的尾部
    if (text.length() == 1 && !last_str_escape && text.charAt(0) == '"') {
        // 如果前面已有异常,则返回异常token, 否则返回异常
        if (str_stop) {
            Symbol symbol = new Symbol(TokenConstants.ERROR, str_errmsg);
            // 重置全局状态
            clear_global_status();
            return symbol;
        }
        // 获得string常量
        else {
            Symbol symbol = new Symbol(TokenConstants.STR_CONST,
                        AbstractTable.stringtable.addString(string_buf.toString()));
            // 重置全局状态
            clear_global_status();
            return symbol;
        }
    }
    // 如果直接是换行符号,那么前面必须要有转义
    if (text.length() == 1 && text.charAt(0) == '\n') {
        curr_lineno++;
        // 如果有转义则直接加入换行字符
        if (last_str_escape) {
            string_buf.append('\n');
        }
        // 没有反斜杠转译,返回错误,退出字符串拼接步骤,下一行重新进入YYINITIAL状态
        else {
            Symbol symbol = new Symbol(TokenConstants.ERROR, "Unterminated string constant");
            clear_global_status();
            return symbol;
        }
    }
    // 如果已超过最大长度,什么也不做,skip掉后续所有的字符,直到"出现或者\n出现
    if (!str_stop) {
        // 如果超过字符串最大长度,设置超长标识,等待
        if (string_buf.length() > MAX_STR_CONST) {
            str_errmsg = "String constant too long";
            str_stop = true;
        } else if (text.length() == 1 && text.charAt(0) == '\0') {
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
            case 'r':
                string_buf.append('\r');
                break;
            case 'f':
                string_buf.append('\f');
                break;
            case '\\':
                string_buf.append("\\");
            default:
                string_buf.append(text.charAt(0));
            }
        } else {
            string_buf.append(text);
        }
    }
}

<YYINITIAL> "(*"   {
    yybegin(COMMENTOPEN);
}

<COMMENTOPEN> .|{NONNEWLINE_WHITE_SPACE_CHAR} {
    // comment we do nothing
}

<COMMENTOPEN> \n {
    curr_lineno++;
}

<COMMENTOPEN> \*\)  {
    yybegin(YYINITIAL);
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

<YYINITIAL> ">"   {
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

<YYINITIAL> [a-z\_]+[0-9a-zA-Z_]* {
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}

<YYINITIAL> [A-Z\_]+[0-9a-zA-Z_]* {
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
    return new Symbol(TokenConstants.ERROR, "not supported operator:" + yytext());
} 

<YYINITIAL> . { /* This rule should be the very last
    in your lexical specification and
    will match match everything not
    matched by other lexical rules. */
    System.err.println("LEXER BUG - UNMATCHED: " + yytext()); 
}
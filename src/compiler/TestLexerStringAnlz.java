package compiler;

import org.junit.Test;

import compiler.*;
import java_cup.runtime.Symbol;

public class TestLexerStringAnlz {
    static int MAX_STR_CONST = 50;
    static int YYINITIAL = 0;
    // test use
    static String test_str = "adsfasdfad\nasdfafdas\\n\"";
    static int idx = 0;

    /* mock yytext() */
    public static String yytext() {
        if (idx > test_str.length() - 1) {
            return null;
        }
        String result = String.valueOf(test_str.charAt(idx));
        idx++;
        return result;
    }

    static int get_curr_lineno() {
        return 1;
    }

    /* mock donothing */
    static void yybegin(int x) {
    }

    static StringBuffer string_buf = new StringBuffer();

    /**
     * 重置string字符串处理全局状态
     */
    static void clear_global_status() {
        string_buf.setLength(0);
        yybegin(YYINITIAL);
        str_errmsg = "";
        str_stop = false;
    }

    /* Need to add to lex */
    static boolean str_stop = false;
    static String str_errmsg;

    /* mock nextToken method */
    public static Symbol nextToken() {
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
        // not copy this code to lex file
        return null;

    }

    @Test
    public void testLexerStringAnlyz() {
        /* stage1: 测试正常字符串及其结尾 */
        test_str = "abcedf\"";
        while (true) {
            Symbol s = nextToken();
            if (s == null) {
                continue;
            } else {
                if (!s.value.equals(test_str.substring(0, test_str.length() - 1))) {
                    System.err.println("stage1 failed!");
                    System.exit(-1);
                }
                System.out.println("stage1 测试成功!");
                break;
            }
        }
        idx = 0;
        /* stage1 end */

        /* stage2: 测试过长的字符 */
        test_str = "abcedfadfafsaadsfdfafasfdsafasfasdfasasdfasfadsfdsafdasfasdfdasfasdfsdafdsafdsafdsfsdafdsfdsafadsfasdfdsfdsafdsafsafdsafdsfsdafdsafdsafdsafdsfdas\"";
        while (true) {
            Symbol s = nextToken();
            if (s == null) {
                continue;
            } else {
                if (s.sym != TokenConstants.ERROR) {
                    System.err.println("stage2_1 failed!");
                    System.exit(-1);
                }
                // 过长的字符要从"或者\n后继续进行词法分析
                if (idx != test_str.length()) {
                    System.err.println("stage2_1 failed!");
                    System.exit(-1);
                }
                System.out.println("stage2_1 测试成功!");
                break;
            }
        }
        idx = 0;
        // 以\n结尾测试
        test_str = "abcedfadfafsaadsfdfafasfdsafasfasdfasasdfasfadsfdsafdasfasdfdasfasdfsdafdsafdsafdsfsdafdsfdsafadsfasdfdsfdsafdsafsafdsafdsfsdafdsafdsafdsafdsfdas\n";
        while (true) {
            Symbol s = nextToken();
            if (s == null) {
                continue;
            } else {
                if (s.sym != TokenConstants.ERROR) {
                    System.err.println("stage2_2 failed!");
                    System.exit(-1);
                }
                // 过长的字符要从"或者\n后继续进行词法分析
                if (idx != test_str.length()) {
                    System.err.println("stage2_2 failed!");
                    System.exit(-1);
                }
                System.out.println("stage2_2 测试成功!");
                break;
            }
        }
        idx = 0;
        /* stage2 end */

        /* stage3: 测试包含 \n 的串 */
        test_str = "abcedfadfaf\\nsaadsf\"";
        while (true) {
            Symbol s = nextToken();
            if (s == null) {
                continue;
            } else {
                if (s.sym != TokenConstants.STR_CONST && s.value.toString().length() != test_str.length() - 1) {
                    System.err.println("stage3 failed!");
                    System.exit(-1);
                }
                System.out.println("解析出来的字符串:" + s.value);
                // 过长的字符要从"或者\n后继续进行词法分析
                if (idx != test_str.length()) {
                    System.err.println("stage3 failed!");
                    System.exit(-1);
                }
                System.out.println("stage3 测试成功!");
                break;
            }
        }
        idx = 0;
        /* stage3 end */

        /* stage4: 测试包含 \0 的串 */
        StringBuilder sb = new StringBuilder();
        sb.append("abc").append('\0').append("asfdfad\"");
        test_str = sb.toString();
        while (true) {
            Symbol s = nextToken();
            if (s == null) {
                continue;
            } else {
                if (s.sym != TokenConstants.ERROR) {
                    System.err.println("stage4 failed!");
                    System.exit(-1);
                }
                System.out.println("解析出来的字符串:" + s.value);
                // 过长的字符要从"或者\n后继续进行词法分析
                if (idx != test_str.length()) {
                    System.err.println("stage4 failed!");
                    System.exit(-1);
                }
                System.out.println("stage4 测试成功!");
                break;
            }
        }
        idx = 0;
        /* stage4 end */
    }

}
/*
 *  The scanner definition for COOL.
 */
package compiler;
import java_cup.runtime.Symbol;


class CoolLexer implements java_cup.runtime.Scanner {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 128;
	private final int YY_EOF = 129;

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
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private boolean yy_at_bol;
	private int yy_lexical_state;

	CoolLexer (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	CoolLexer (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private CoolLexer () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */
    // empty for now
	}

	private boolean yy_eof_done = false;
	private final int STROPEN = 1;
	private final int YYINITIAL = 0;
	private final int COMMENTOPEN = 2;
	private final int yy_state_dtrans[] = {
		0,
		180,
		183
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private boolean yy_last_was_cr=false;
	private void yy_mark_start () {
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_acpt[] = {
		/* 0 */ YY_NOT_ACCEPT,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NO_ANCHOR,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_NO_ANCHOR,
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_NO_ANCHOR,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NO_ANCHOR,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NO_ANCHOR,
		/* 35 */ YY_NO_ANCHOR,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_NO_ANCHOR,
		/* 38 */ YY_NO_ANCHOR,
		/* 39 */ YY_NO_ANCHOR,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NO_ANCHOR,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NO_ANCHOR,
		/* 55 */ YY_NOT_ACCEPT,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NO_ANCHOR,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NO_ANCHOR,
		/* 62 */ YY_NO_ANCHOR,
		/* 63 */ YY_NO_ANCHOR,
		/* 64 */ YY_NO_ANCHOR,
		/* 65 */ YY_NO_ANCHOR,
		/* 66 */ YY_NO_ANCHOR,
		/* 67 */ YY_NO_ANCHOR,
		/* 68 */ YY_NO_ANCHOR,
		/* 69 */ YY_NO_ANCHOR,
		/* 70 */ YY_NO_ANCHOR,
		/* 71 */ YY_NO_ANCHOR,
		/* 72 */ YY_NO_ANCHOR,
		/* 73 */ YY_NO_ANCHOR,
		/* 74 */ YY_NO_ANCHOR,
		/* 75 */ YY_NO_ANCHOR,
		/* 76 */ YY_NO_ANCHOR,
		/* 77 */ YY_NOT_ACCEPT,
		/* 78 */ YY_NO_ANCHOR,
		/* 79 */ YY_NO_ANCHOR,
		/* 80 */ YY_NO_ANCHOR,
		/* 81 */ YY_NO_ANCHOR,
		/* 82 */ YY_NO_ANCHOR,
		/* 83 */ YY_NO_ANCHOR,
		/* 84 */ YY_NO_ANCHOR,
		/* 85 */ YY_NO_ANCHOR,
		/* 86 */ YY_NO_ANCHOR,
		/* 87 */ YY_NO_ANCHOR,
		/* 88 */ YY_NO_ANCHOR,
		/* 89 */ YY_NO_ANCHOR,
		/* 90 */ YY_NO_ANCHOR,
		/* 91 */ YY_NO_ANCHOR,
		/* 92 */ YY_NOT_ACCEPT,
		/* 93 */ YY_NO_ANCHOR,
		/* 94 */ YY_NO_ANCHOR,
		/* 95 */ YY_NO_ANCHOR,
		/* 96 */ YY_NO_ANCHOR,
		/* 97 */ YY_NO_ANCHOR,
		/* 98 */ YY_NO_ANCHOR,
		/* 99 */ YY_NO_ANCHOR,
		/* 100 */ YY_NO_ANCHOR,
		/* 101 */ YY_NOT_ACCEPT,
		/* 102 */ YY_NO_ANCHOR,
		/* 103 */ YY_NO_ANCHOR,
		/* 104 */ YY_NO_ANCHOR,
		/* 105 */ YY_NO_ANCHOR,
		/* 106 */ YY_NO_ANCHOR,
		/* 107 */ YY_NOT_ACCEPT,
		/* 108 */ YY_NO_ANCHOR,
		/* 109 */ YY_NO_ANCHOR,
		/* 110 */ YY_NO_ANCHOR,
		/* 111 */ YY_NOT_ACCEPT,
		/* 112 */ YY_NO_ANCHOR,
		/* 113 */ YY_NO_ANCHOR,
		/* 114 */ YY_NOT_ACCEPT,
		/* 115 */ YY_NO_ANCHOR,
		/* 116 */ YY_NO_ANCHOR,
		/* 117 */ YY_NOT_ACCEPT,
		/* 118 */ YY_NO_ANCHOR,
		/* 119 */ YY_NO_ANCHOR,
		/* 120 */ YY_NOT_ACCEPT,
		/* 121 */ YY_NO_ANCHOR,
		/* 122 */ YY_NO_ANCHOR,
		/* 123 */ YY_NOT_ACCEPT,
		/* 124 */ YY_NO_ANCHOR,
		/* 125 */ YY_NO_ANCHOR,
		/* 126 */ YY_NOT_ACCEPT,
		/* 127 */ YY_NO_ANCHOR,
		/* 128 */ YY_NO_ANCHOR,
		/* 129 */ YY_NOT_ACCEPT,
		/* 130 */ YY_NO_ANCHOR,
		/* 131 */ YY_NO_ANCHOR,
		/* 132 */ YY_NOT_ACCEPT,
		/* 133 */ YY_NO_ANCHOR,
		/* 134 */ YY_NO_ANCHOR,
		/* 135 */ YY_NOT_ACCEPT,
		/* 136 */ YY_NO_ANCHOR,
		/* 137 */ YY_NO_ANCHOR,
		/* 138 */ YY_NOT_ACCEPT,
		/* 139 */ YY_NO_ANCHOR,
		/* 140 */ YY_NO_ANCHOR,
		/* 141 */ YY_NOT_ACCEPT,
		/* 142 */ YY_NO_ANCHOR,
		/* 143 */ YY_NO_ANCHOR,
		/* 144 */ YY_NOT_ACCEPT,
		/* 145 */ YY_NO_ANCHOR,
		/* 146 */ YY_NO_ANCHOR,
		/* 147 */ YY_NOT_ACCEPT,
		/* 148 */ YY_NO_ANCHOR,
		/* 149 */ YY_NO_ANCHOR,
		/* 150 */ YY_NOT_ACCEPT,
		/* 151 */ YY_NO_ANCHOR,
		/* 152 */ YY_NO_ANCHOR,
		/* 153 */ YY_NOT_ACCEPT,
		/* 154 */ YY_NO_ANCHOR,
		/* 155 */ YY_NO_ANCHOR,
		/* 156 */ YY_NOT_ACCEPT,
		/* 157 */ YY_NO_ANCHOR,
		/* 158 */ YY_NO_ANCHOR,
		/* 159 */ YY_NOT_ACCEPT,
		/* 160 */ YY_NO_ANCHOR,
		/* 161 */ YY_NO_ANCHOR,
		/* 162 */ YY_NOT_ACCEPT,
		/* 163 */ YY_NO_ANCHOR,
		/* 164 */ YY_NO_ANCHOR,
		/* 165 */ YY_NOT_ACCEPT,
		/* 166 */ YY_NO_ANCHOR,
		/* 167 */ YY_NO_ANCHOR,
		/* 168 */ YY_NOT_ACCEPT,
		/* 169 */ YY_NO_ANCHOR,
		/* 170 */ YY_NO_ANCHOR,
		/* 171 */ YY_NOT_ACCEPT,
		/* 172 */ YY_NO_ANCHOR,
		/* 173 */ YY_NO_ANCHOR,
		/* 174 */ YY_NOT_ACCEPT,
		/* 175 */ YY_NO_ANCHOR,
		/* 176 */ YY_NO_ANCHOR,
		/* 177 */ YY_NOT_ACCEPT,
		/* 178 */ YY_NO_ANCHOR,
		/* 179 */ YY_NO_ANCHOR,
		/* 180 */ YY_NOT_ACCEPT,
		/* 181 */ YY_NO_ANCHOR,
		/* 182 */ YY_NO_ANCHOR,
		/* 183 */ YY_NOT_ACCEPT,
		/* 184 */ YY_NO_ANCHOR,
		/* 185 */ YY_NO_ANCHOR,
		/* 186 */ YY_NO_ANCHOR,
		/* 187 */ YY_NO_ANCHOR,
		/* 188 */ YY_NO_ANCHOR,
		/* 189 */ YY_NO_ANCHOR,
		/* 190 */ YY_NO_ANCHOR,
		/* 191 */ YY_NO_ANCHOR,
		/* 192 */ YY_NO_ANCHOR,
		/* 193 */ YY_NO_ANCHOR,
		/* 194 */ YY_NOT_ACCEPT,
		/* 195 */ YY_NO_ANCHOR,
		/* 196 */ YY_NO_ANCHOR,
		/* 197 */ YY_NO_ANCHOR,
		/* 198 */ YY_NO_ANCHOR,
		/* 199 */ YY_NO_ANCHOR,
		/* 200 */ YY_NO_ANCHOR,
		/* 201 */ YY_NOT_ACCEPT,
		/* 202 */ YY_NO_ANCHOR,
		/* 203 */ YY_NO_ANCHOR,
		/* 204 */ YY_NO_ANCHOR,
		/* 205 */ YY_NO_ANCHOR,
		/* 206 */ YY_NO_ANCHOR,
		/* 207 */ YY_NO_ANCHOR,
		/* 208 */ YY_NO_ANCHOR,
		/* 209 */ YY_NO_ANCHOR,
		/* 210 */ YY_NO_ANCHOR,
		/* 211 */ YY_NO_ANCHOR,
		/* 212 */ YY_NO_ANCHOR,
		/* 213 */ YY_NO_ANCHOR,
		/* 214 */ YY_NO_ANCHOR,
		/* 215 */ YY_NO_ANCHOR,
		/* 216 */ YY_NO_ANCHOR,
		/* 217 */ YY_NO_ANCHOR,
		/* 218 */ YY_NO_ANCHOR,
		/* 219 */ YY_NO_ANCHOR,
		/* 220 */ YY_NO_ANCHOR,
		/* 221 */ YY_NO_ANCHOR,
		/* 222 */ YY_NO_ANCHOR,
		/* 223 */ YY_NO_ANCHOR,
		/* 224 */ YY_NO_ANCHOR,
		/* 225 */ YY_NO_ANCHOR,
		/* 226 */ YY_NO_ANCHOR,
		/* 227 */ YY_NO_ANCHOR,
		/* 228 */ YY_NO_ANCHOR,
		/* 229 */ YY_NO_ANCHOR,
		/* 230 */ YY_NO_ANCHOR,
		/* 231 */ YY_NO_ANCHOR,
		/* 232 */ YY_NO_ANCHOR,
		/* 233 */ YY_NO_ANCHOR,
		/* 234 */ YY_NO_ANCHOR,
		/* 235 */ YY_NO_ANCHOR,
		/* 236 */ YY_NO_ANCHOR,
		/* 237 */ YY_NO_ANCHOR,
		/* 238 */ YY_NO_ANCHOR,
		/* 239 */ YY_NO_ANCHOR,
		/* 240 */ YY_NO_ANCHOR,
		/* 241 */ YY_NO_ANCHOR,
		/* 242 */ YY_NO_ANCHOR,
		/* 243 */ YY_NO_ANCHOR,
		/* 244 */ YY_NO_ANCHOR,
		/* 245 */ YY_NO_ANCHOR,
		/* 246 */ YY_NO_ANCHOR,
		/* 247 */ YY_NO_ANCHOR,
		/* 248 */ YY_NO_ANCHOR,
		/* 249 */ YY_NO_ANCHOR,
		/* 250 */ YY_NO_ANCHOR,
		/* 251 */ YY_NO_ANCHOR,
		/* 252 */ YY_NO_ANCHOR,
		/* 253 */ YY_NO_ANCHOR,
		/* 254 */ YY_NO_ANCHOR,
		/* 255 */ YY_NO_ANCHOR,
		/* 256 */ YY_NO_ANCHOR,
		/* 257 */ YY_NO_ANCHOR,
		/* 258 */ YY_NO_ANCHOR,
		/* 259 */ YY_NO_ANCHOR,
		/* 260 */ YY_NO_ANCHOR,
		/* 261 */ YY_NO_ANCHOR,
		/* 262 */ YY_NO_ANCHOR,
		/* 263 */ YY_NO_ANCHOR,
		/* 264 */ YY_NO_ANCHOR,
		/* 265 */ YY_NO_ANCHOR,
		/* 266 */ YY_NO_ANCHOR,
		/* 267 */ YY_NO_ANCHOR,
		/* 268 */ YY_NO_ANCHOR,
		/* 269 */ YY_NO_ANCHOR,
		/* 270 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"4:8,2:2,1,4:2,0,4:18,2,4,3,4:4,61,5,7,6,28,8,16,9,38,58:10,33,10,20,29,21,4" +
",37,12,59,11,32,15,26,59,23,22,59:2,25,59,17,18,27,59,24,14,19,40,31,30,59:" +
"3,61,4,61,4,60,4,43,42,44,45,46,41,42,47,48,42:2,49,42,50,51,52,42,53,54,39" +
",55,56,57,42:3,35,13,36,34,4,62:2")[0];

	private int yy_rmap[] = unpackFromString(1,271,
"0,1,2,1:2,3,4,1:4,5,1,6,1:2,7,1:6,8,9,1:4,10,1:2,11,1:3,12,1,11,1:2,13,11:3" +
",14,1:2,14,15,1:5,16,17,11,18,19,11:2,14,1,11,14,11:2,1,14:3,11:2,1,11,20,2" +
"1,22,23,24,14:2,25,14,13,14,11,26,14,11,14,27,28,29,30,11,31,14:3,32,33,34," +
"35,14,36,37,38,39,14,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57," +
"25,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,13,78,79,80," +
"81,82,83,84,85,36,86,87,88,89,90,91,92,93,94,95,96,26,97,98,15,99,100,101,1" +
"02,103,104,105,106,107,108,109,110,111,112,113,114,115,116:2,117,118,116:5," +
"119,116:9,120,116,121,122,116,123,124,125,126,127,128,129,130,131,132,133,1" +
"34,135,136,137,138,139,140,141,142,143,144,145,146,147,148,149,150,151,152," +
"153,154,155,156,116,157,158,159,160,161,162,163,164,165,166,167,168,169,170" +
",116,171,172,173,168,14")[0];

	private int yy_nxt[][] = unpackFromString(174,63,
"-1,1,2,3,4,5,6,7,8,9,10,11,57,56,57,190,12,245,78,266,13,14,93,57:2,249,102" +
",268,15,16,253,57:2,17,18,19,20,21,22,23,57,58,265:2,79,265,94,265,103,109," +
"214,113,267,265:4,116,24,57,269,25,26,-1:65,2,-1:66,27,-1:63,28,-1:66,57,10" +
"8,55,57:2,-1,57:3,-1:2,57:3,112,57:2,-1:2,57:3,-1:6,57:4,108,57:5,112,57:11" +
",-1:18,33,-1:12,34,-1:54,35,-1:52,270:2,-1,270:2,-1,270:3,-1:2,270,191,246," +
"270:3,-1:2,270:3,-1:6,250,270,250:6,254,250:5,255,250:4,270:2,250,-1:60,24," +
"-1:16,123,36,120,-1:3,129,63,-1:2,132,135,-1:6,37,138,-1:7,63,-1:3,123,-1:3" +
",135,132,-1:2,129,-1:2,120,-1,138,37,-1:16,57:2,-1,57:2,-1,57:3,-1:2,57:6,-" +
"1:2,57:3,-1:6,57:22,-1:15,97,150,106,-1:2,153,-1:6,156,-1,40,-1:18,106,-1:2" +
",156,-1,153,40,-1,150,-1:21,46:2,-1:39,46,-1:19,270:2,-1,270:2,-1,270:3,-1:" +
"2,270:6,-1:2,270:3,-1:6,270:22,-1:15,177,-1:5,177,-1:19,177,-1:35,123,126,1" +
"20,-1:28,123,-1:10,120,-1:20,77,29,92,101,-1,59,107,-1:3,30,111,-1,194,31,-" +
"1:14,31,-1,77,-1:2,101,111,30,194,59,107,-1:2,92,-1:19,270,256,30,270:2,-1," +
"270:3,-1:2,81,270:5,-1:2,270:3,-1:6,250,270,250:2,257,250:4,202,250:9,270:2" +
",250,-1:15,135,-1:9,135,-1:23,135,-1:22,54,-1:68,120:2,-1:39,120,-1:19,57:2" +
",-1,57:2,-1,57:3,-1:2,57:4,32,57,-1:2,57:3,-1:6,57:2,32,57:19,-1:13,270,119" +
",55,270:2,-1,270:3,-1:2,270:3,122,270:2,-1:2,270:3,-1:6,250,270,250:2,216,2" +
"50:5,217,250:8,270:2,250,-1:15,147,-1:9,135,-1:7,138,-1:15,135,-1:8,138,-1:" +
"19,40,-1:13,40,-1:24,40,-1:23,49,-1:8,174,-1:9,74,-1:12,74,-1:2,174,-1:27,1" +
"38,-1:17,138,-1:24,138,-1:17,57:2,80,121,57,-1,95,57:2,-1:2,57:4,61,57,-1:2" +
",57:3,-1:6,57:2,61,57:8,95,57:3,121,57:6,-1:13,270:2,201,258,270,-1,270:3,-" +
"1:2,270:3,248,270:2,-1:2,270:3,-1:6,250,270,250:8,252,250:4,259,250:3,270:2" +
",250,-1:13,57:2,135,57:2,-1,57:3,-1:2,57,148,57:4,-1:2,57:3,-1:6,57:8,148,5" +
"7:13,-1:15,88,46,47,-1:6,165,-1,168,-1:21,47,-1,165,-1:4,168,46,-1:21,37,-1" +
":16,37,-1:26,37,-1:16,57:2,30,57:2,-1,57:3,-1:2,60,57:5,-1:2,57:3,-1:6,57:9" +
",60,57:12,-1:13,270:2,80,125,270,-1,104,270:2,-1:2,270:4,82,270,-1:2,270:3," +
"-1:6,250,270,203,250:8,211,250:3,218,250:3,270:2,250,-1:13,270:2,135,270:2," +
"-1,270:3,-1:2,270,164,270:4,-1:2,270:3,-1:6,270:8,164,270:13,-1:15,168,-1:1" +
"0,168,-1:28,168,-1:22,83,-1:4,129,63,-1:19,63,-1:11,129,-1:22,57:2,120,133," +
"57,-1,57:3,-1:2,57:6,-1:2,57:3,-1:6,57:15,133,57:6,-1:13,270:2,117,270,128," +
"-1,270,131,270,-1:2,270:6,-1:2,270:3,-1:6,250,270,250:5,220,250:4,221,250:6" +
",270:2,250,-1:15,132,-1:8,132,-1:25,132,-1:25,57,136,123,57:2,-1,57:3,-1:2," +
"57:6,-1:2,57:3,-1:6,57:4,136,57:17,-1:13,270:2,-1,270:2,-1,270:3,-1:2,270:4" +
",62,270,-1:2,270:3,-1:6,250,270,196,250:16,270:2,250,-1:15,63,-1:5,63,-1:10" +
",37,-1:8,63,-1:17,37,-1:16,57:2,37,57:2,-1,57:3,-1:2,57:6,-1:2,64,57:2,-1:6" +
",57:18,64,57:3,-1:13,270:2,111,270:2,-1,270:3,-1:2,270,140,270:4,-1:2,270:3" +
",-1:6,250,270,250:6,224,250:10,270:2,250,-1:15,129,-1:4,129,-1:32,129,-1:22" +
",57:2,63,57:2,-1,57:2,96,-1:2,57:6,-1:2,57:3,-1:6,96,57:21,-1:13,270:2,120," +
"149,270,-1,270:3,-1:2,270:6,-1:2,270:3,-1:6,270:15,149,270:6,-1:15,39,-1,39" +
",-1:30,39,-1:27,57:2,138,57:2,-1,57:3,-1:2,57:6,-1:2,57,219,57,-1:6,57:17,2" +
"19,57:4,-1:13,270,152,123,270:2,-1,270:3,-1:2,270:6,-1:2,270:3,-1:6,270:4,1" +
"52,270:17,-1:15,150:2,-1:39,150,-1:19,57:2,-1,57:2,-1,57:2,38,-1:2,57:6,-1:" +
"2,57:3,-1:6,38,57:21,-1:13,270:2,138,270:2,-1,270:3,-1:2,270:6,-1:2,270,161" +
",270,-1:6,270:17,161,270:4,-1:15,85,150,39,-1:30,39,-1:7,150,-1:19,57:2,129" +
",57:2,-1,57,151,57,-1:2,57:6,-1:2,57:3,-1:6,57:12,151,57:9,-1:13,270:2,-1,2" +
"70:2,-1,270:2,65,-1:2,270:6,-1:2,270:3,-1:6,65,270:21,-1:13,57:2,132,57:2,-" +
"1,57:3,-1:2,157,57:5,-1:2,57:3,-1:6,57:9,157,57:12,-1:13,270:2,129,270:2,-1" +
",270,167,270,-1:2,270:6,-1:2,270:3,-1:6,270:12,167,270:9,-1:15,156,-1:11,15" +
"6,-1:23,156,-1:24,57:2,39,57,66,-1,57:3,-1:2,57:6,-1:2,57:3,-1:6,57:7,66,57" +
":14,-1:13,270:2,37,270:2,-1,270:3,-1:2,270:6,-1:2,84,270:2,-1:6,270:18,84,2" +
"70:3,-1:15,159,-1,159,-1:30,159,-1:27,57:2,150,160,57,-1,57:3,-1:2,57:6,-1:" +
"2,57:3,-1:6,57:15,160,57:6,-1:13,270:2,63,270:2,-1,270:2,105,-1:2,270:6,-1:" +
"2,270:3,-1:6,105,270:21,-1:15,153,-1:4,153,-1:32,153,-1:22,42,57,-1,57:2,-1" +
",57:3,-1:2,57:6,-1:2,57:3,-1:6,57:5,42,57:16,-1:13,270:2,132,270:2,-1,270:3" +
",-1:2,173,270:5,-1:2,270:3,-1:6,270:9,173,270:12,-1:15,41,150,68,-1:30,68,-" +
"1:7,150,-1:19,57:2,68,57,87,-1,57:3,-1:2,57:6,-1:2,57:3,-1:6,57:7,87,57:14," +
"-1:13,270:2,-1,270:2,-1,70,270:2,-1:2,270:6,-1:2,270:3,-1:6,270:11,70,270:1" +
"0,-1:15,68,-1,68,-1:30,68,-1:27,57:2,-1,57:2,-1,43,57:2,-1:2,57:6,-1:2,57:3" +
",-1:6,57:11,43,57:10,-1:13,270:2,-1,270,45,-1,270:3,-1:2,270:6,-1:2,270:3,-" +
"1:6,270:7,45,270:14,-1:15,162,-1,159,-1:2,153,-1:27,159,-1:4,153,-1:22,57:2" +
",159,57,166,-1,57:3,-1:2,57:6,-1:2,57:3,-1:6,57:7,166,57:14,-1:13,270:2,39," +
"270,110,-1,270:3,-1:2,270:6,-1:2,270:3,-1:6,270:7,110,270:14,-1:13,57:2,40," +
"57:2,-1,57:3,-1:2,57:5,67,-1:2,57:3,-1:6,57:13,67,57:8,-1:13,270:2,150,179," +
"270,-1,270:3,-1:2,270:6,-1:2,270:3,-1:6,270:15,179,270:6,-1:15,165,-1:8,165" +
",-1:25,165,-1:25,57:2,-1,57:2,-1,57:3,-1:2,57:3,44,57:2,-1:2,57:3,-1:6,57:1" +
"0,44,57:11,-1:13,69,270,-1,270:2,-1,270:3,-1:2,270:6,-1:2,270:3,-1:6,270:5," +
"69,270:16,-1:15,47,-1,47,-1:30,47,-1:27,57:2,156,57:2,-1,57:3,-1:2,57:3,169" +
",57:2,-1:2,57:3,-1:6,57:10,169,57:11,-1:13,270:2,68,270,98,-1,270:3,-1:2,27" +
"0:6,-1:2,270:3,-1:6,270:7,98,270:14,-1:13,57:2,46,72,57,-1,57:3,-1:2,57:6,-" +
"1:2,57:3,-1:6,57:15,72,57:6,-1:13,270:2,153,270:2,-1,270,182,270,-1:2,270:6" +
",-1:2,270:3,-1:6,270:12,182,270:9,-1:15,171,-1:8,165,-1,168,-1:23,165,-1:4," +
"168,-1:20,57:2,165,57:2,-1,57:3,-1:2,172,57:5,-1:2,57:3,-1:6,57:9,172,57:12" +
",-1:13,270:2,159,270,184,-1,270:3,-1:2,270:6,-1:2,270:3,-1:6,270:7,184,270:" +
"14,-1:15,74,-1:18,74,-1:12,74,-1:28,57:2,168,57:2,-1,57:3,-1:2,57:2,175,57:" +
"3,-1:2,57:3,-1:6,57:14,175,57:7,-1:13,270:2,40,270:2,-1,270:3,-1:2,270:5,86" +
",-1:2,270:3,-1:6,270:13,86,270:8,-1:15,174,-1:8,174,-1:25,174,-1:25,57:2,47" +
",57,73,-1,57:3,-1:2,57:6,-1:2,57:3,-1:6,57:7,73,57:14,-1:13,270:2,-1,270:2," +
"-1,270:3,-1:2,270:3,71,270:2,-1:2,270:3,-1:6,270:10,71,270:11,-1:13,57:2,74" +
",57:2,-1,57:3,-1:2,57:6,-1:2,57:2,90,-1:6,57:6,90,57:15,-1:13,270:2,156,270" +
":2,-1,270:3,-1:2,270:3,185,270:2,-1:2,270:3,-1:6,270:10,185,270:11,-1:13,57" +
":2,174,57:2,-1,57:3,-1:2,178,57:5,-1:2,57:3,-1:6,57:9,178,57:12,-1:13,270:2" +
",-1,270,48,-1,270:3,-1:2,270:6,-1:2,270:3,-1:6,270:7,48,270:14,-1:15,50:2,-" +
"1:39,50,-1:19,57:2,177,57:2,-1,57:2,181,-1:2,57:6,-1:2,57:3,-1:6,181,57:21," +
"-1:13,270:2,46,99,270,-1,270:3,-1:2,270:6,-1:2,270:3,-1:6,270:15,99,270:6,-" +
"1:3,51:61,26,-1:11,57:2,50,75,57,-1,57:3,-1:2,57:6,-1:2,57:3,-1:6,57:15,75," +
"57:6,-1:13,270:2,165,270:2,-1,270:3,-1:2,186,270:5,-1:2,270:3,-1:6,270:9,18" +
"6,270:12,-1:3,52,53:4,76,53:55,26,-1:11,270:2,168,270:2,-1,270:3,-1:2,270:2" +
",187,270:3,-1:2,270:3,-1:6,270:14,187,270:7,-1:13,270:2,47,270,89,-1,270:3," +
"-1:2,270:6,-1:2,270:3,-1:6,270:7,89,270:14,-1:13,270:2,74,270:2,-1,270:3,-1" +
":2,270:6,-1:2,270:2,100,-1:6,270:6,100,270:15,-1:13,270:2,174,270:2,-1,270:" +
"3,-1:2,188,270:5,-1:2,270:3,-1:6,270:9,188,270:12,-1:13,270:2,177,270:2,-1," +
"270:2,189,-1:2,270:6,-1:2,270:3,-1:6,189,270:21,-1:13,270:2,50,91,270,-1,27" +
"0:3,-1:2,270:6,-1:2,270:3,-1:6,270:15,91,270:6,-1:13,57:2,201,195,57,-1,57:" +
"3,-1:2,57:3,213,57:2,-1:2,57:3,-1:6,57:10,213,57:4,195,57:6,-1:13,270:2,-1," +
"270,143,-1,270:3,-1:2,270:6,-1:2,270:3,-1:6,270:7,143,270:14,-1:13,270:2,-1" +
",270:2,-1,270:3,-1:2,270:6,-1:2,270:3,-1:6,250,270,250:17,270:2,250,-1:14,1" +
"23,141,144,-1:28,123,-1:10,144,-1:19,57,139,-1,57:2,-1,57:3,-1:2,57:6,-1:2," +
"57:3,-1:6,57:4,139,57:17,-1:15,144:2,-1:39,144,-1:19,270:2,135,270:2,-1,270" +
":3,-1:2,270,164,270:4,-1:2,270:3,-1:6,250,270,250:6,232,250:10,270:2,250,-1" +
":13,57:2,144,142,57,-1,57:3,-1:2,57:6,-1:2,57:3,-1:6,57:15,142,57:6,-1:13,2" +
"70:2,114,270,134,-1,270,137,270,-1:2,270:6,-1:2,270:3,-1:6,250,270,250:5,22" +
"2,250:4,223,250:6,270:2,250,-1:13,270:2,120,149,270,-1,270:3,-1:2,270:6,-1:" +
"2,270:3,-1:6,250,270,250:13,227,250:3,270:2,250,-1:13,270,152,123,270:2,-1," +
"270:3,-1:2,270:6,-1:2,270:3,-1:6,250,270,250:2,228,250:14,270:2,250,-1:13,2" +
"70:2,138,270:2,-1,270:3,-1:2,270:6,-1:2,270,161,270,-1:6,250,270,250:15,231" +
",250,270:2,250,-1:13,57:2,153,57:2,-1,57,163,57,-1:2,57:6,-1:2,57:3,-1:6,57" +
":12,163,57:9,-1:13,270:2,-1,270:2,-1,270:2,65,-1:2,270:6,-1:2,270:3,-1:6,19" +
"7,270,250:17,270:2,250,-1:13,270:2,129,270:2,-1,270,167,270,-1:2,270:6,-1:2" +
",270:3,-1:6,250,270,250:10,233,250:6,270:2,250,-1:13,270:2,37,270:2,-1,270:" +
"3,-1:2,270:6,-1:2,84,270:2,-1:6,250,270,250:16,204,270:2,250,-1:13,270:2,63" +
",270:2,-1,270:2,105,-1:2,270:6,-1:2,270:3,-1:6,212,270,250:17,270:2,250,-1:" +
"13,270:2,132,270:2,-1,270:3,-1:2,173,270:5,-1:2,270:3,-1:6,250,270,250:7,23" +
"5,250:9,270:2,250,-1:13,270:2,-1,270:2,-1,70,270:2,-1:2,270:6,-1:2,270:3,-1" +
":6,250,270,250:9,199,250:7,270:2,250,-1:13,270:2,-1,270,45,-1,270:3,-1:2,27" +
"0:6,-1:2,270:3,-1:6,250,270,250:5,192,250:11,270:2,250,-1:13,270:2,39,270,1" +
"10,-1,270:3,-1:2,270:6,-1:2,270:3,-1:6,250,270,250:5,215,250:11,270:2,250,-" +
"1:13,270:2,150,179,270,-1,270:3,-1:2,270:6,-1:2,270:3,-1:6,250,270,250:13,2" +
"37,250:3,270:2,250,-1:13,69,270,-1,270:2,-1,270:3,-1:2,270:6,-1:2,270:3,-1:" +
"6,250,270,250:3,198,250:13,270:2,250,-1:13,270:2,68,270,98,-1,270:3,-1:2,27" +
"0:6,-1:2,270:3,-1:6,250,270,250:5,208,250:11,270:2,250,-1:13,270:2,153,270:" +
"2,-1,270,182,270,-1:2,270:6,-1:2,270:3,-1:6,250,270,250:10,238,250:6,270:2," +
"250,-1:13,270:2,159,270,184,-1,270:3,-1:2,270:6,-1:2,270:3,-1:6,250,270,250" +
":5,239,250:11,270:2,250,-1:13,270:2,40,270:2,-1,270:3,-1:2,270:5,86,-1:2,27" +
"0:3,-1:6,250,270,250:11,205,250:5,270:2,250,-1:13,270:2,-1,270:2,-1,270:3,-" +
"1:2,270:3,71,270:2,-1:2,270:3,-1:6,250,270,250:8,200,250:8,270:2,250,-1:13," +
"270:2,156,270:2,-1,270:3,-1:2,270:3,185,270:2,-1:2,270:3,-1:6,250,270,250:8" +
",240,250:8,270:2,250,-1:13,270:2,-1,270,48,-1,270:3,-1:2,270:6,-1:2,270:3,-" +
"1:6,250,270,250:5,193,250:11,270:2,250,-1:13,270:2,46,99,270,-1,270:3,-1:2," +
"270:6,-1:2,270:3,-1:6,250,270,250:13,209,250:3,270:2,250,-1:13,270:2,165,27" +
"0:2,-1,270:3,-1:2,186,270:5,-1:2,270:3,-1:6,250,270,250:7,241,250:9,270:2,2" +
"50,-1:13,270:2,168,270:2,-1,270:3,-1:2,270:2,187,270:3,-1:2,270:3,-1:6,250," +
"270,250:12,242,250:4,270:2,250,-1:13,270:2,47,270,89,-1,270:3,-1:2,270:6,-1" +
":2,270:3,-1:6,250,270,250:5,206,250:11,270:2,250,-1:13,270:2,74,270:2,-1,27" +
"0:3,-1:2,270:6,-1:2,270:2,100,-1:6,250,270,250:4,210,250:12,270:2,250,-1:13" +
",270:2,174,270:2,-1,270:3,-1:2,188,270:5,-1:2,270:3,-1:6,250,270,250:7,243," +
"250:9,270:2,250,-1:13,270:2,177,270:2,-1,270:2,189,-1:2,270:6,-1:2,270:3,-1" +
":6,244,270,250:17,270:2,250,-1:13,270:2,50,91,270,-1,270:3,-1:2,270:6,-1:2," +
"270:3,-1:6,250,270,250:13,207,250:3,270:2,250,-1:13,57:2,114,57,115,-1,57,1" +
"18,57,-1:2,57:6,-1:2,57:3,-1:6,57:7,115,57:4,118,57:9,-1:13,270:2,-1,270:2," +
"-1,270:3,-1:2,270:6,-1:2,270:3,-1:6,270,146,270:14,146,270:5,-1:13,57:2,-1," +
"57,145,-1,57:3,-1:2,57:6,-1:2,57:3,-1:6,57:7,145,57:14,-1:13,270:2,144,158," +
"270,-1,270:3,-1:2,270:6,-1:2,270:3,-1:6,270:15,158,270:6,-1:13,57:2,117,57," +
"124,-1,57,127,57,-1:2,57:6,-1:2,57:3,-1:6,57:7,124,57:4,127,57:9,-1:13,57:2" +
",-1,57:2,-1,57,154,57,-1:2,57:6,-1:2,57:3,-1:6,57:12,154,57:9,-1:13,270:2,1" +
"44,158,270,-1,270:3,-1:2,270:6,-1:2,270:3,-1:6,250,270,250:13,230,250:3,270" +
":2,250,-1:13,57:2,111,57:2,-1,57:3,-1:2,57,130,57:4,-1:2,57:3,-1:6,57:8,130" +
",57:13,-1:13,270:2,-1,270,143,-1,270:3,-1:2,270:6,-1:2,270:3,-1:6,250,270,2" +
"50:5,225,250:11,270:2,250,-1:13,270:2,-1,270:2,-1,270:3,-1:2,270:6,-1:2,270" +
":3,-1:6,250,146,250:14,226,250:2,270:2,250,-1:13,270:2,-1,270:2,-1,270:3,-1" +
":2,270:3,263,270:2,-1:2,270:3,-1:6,270:10,263,270:11,-1:13,270:2,-1,270:2,-" +
"1,270:3,-1:2,270:3,263,270:2,-1:2,270:3,-1:6,250,270,250:8,264,250:8,270:2," +
"250,-1:13,270,155,-1,270:2,-1,270:3,-1:2,270:6,-1:2,270:3,-1:6,270:4,155,27" +
"0:17,-1:13,270,155,-1,270:2,-1,270:3,-1:2,270:6,-1:2,270:3,-1:6,250,270,250" +
":2,229,250:14,270:2,250,-1:13,270:2,-1,270:2,-1,270,170,270,-1:2,270:6,-1:2" +
",270:3,-1:6,270:12,170,270:9,-1:13,270:2,-1,270:2,-1,270,170,270,-1:2,270:6" +
",-1:2,270:3,-1:6,250,270,250:10,234,250:6,270:2,250,-1:13,262:2,-1,262:2,-1" +
",262:3,-1:2,262:6,-1:2,262:3,-1:6,262:22,-1:13,270:2,-1,176,270,-1,270:3,-1" +
":2,270:6,-1:2,270:3,-1:6,270:15,176,270:6,-1:13,270:2,-1,176,270,-1,270:3,-" +
"1:2,270:6,-1:2,270:3,-1:6,250,270,250:13,236,250:3,270:2,250,-1:13,57:2,-1," +
"57:2,-1,57:3,-1:2,57,247,57:4,-1:2,57:3,-1:6,57:8,247,57:13,-1:13,270:2,-1," +
"270:2,-1,270,260,270,-1:2,270:6,-1:2,270:3,-1:6,250,270,250:10,261,250:6,27" +
"0:2,250,-1:13,57:2,-1,57:2,-1,57,251,57,-1:2,57:6,-1:2,57:3,-1:6,57:12,251," +
"57:9,-1:2");

	public java_cup.runtime.Symbol next_token ()
		throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {

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
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 1:
						{
    curr_lineno++;
}
					case -2:
						break;
					case 2:
						{ 
}
					case -3:
						break;
					case 3:
						{
    yybegin(STROPEN);
}
					case -4:
						break;
					case 4:
						{ /* This rule should be the very last
    in your lexical specification and
    will match match everything not
    matched by other lexical rules. */
    System.err.println("LEXER BUG - UNMATCHED: " + yytext()); 
}
					case -5:
						break;
					case 5:
						{
    return new Symbol(TokenConstants.LPAREN);
}
					case -6:
						break;
					case 6:
						{
    return new Symbol(TokenConstants.MULT);
}
					case -7:
						break;
					case 7:
						{
    return new Symbol(TokenConstants.RPAREN);
}
					case -8:
						break;
					case 8:
						{
    return new Symbol(TokenConstants.COMMA);
}
					case -9:
						break;
					case 9:
						{
    return new Symbol(TokenConstants.DOT);
}
					case -10:
						break;
					case 10:
						{
    return new Symbol(TokenConstants.SEMI);
}
					case -11:
						break;
					case 11:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -12:
						break;
					case 12:
						{
    return new Symbol(TokenConstants.MINUS);
}
					case -13:
						break;
					case 13:
						{
    return new Symbol(TokenConstants.LT);
}
					case -14:
						break;
					case 14:
						{
    return new Symbol(TokenConstants.LT);
}
					case -15:
						break;
					case 15:
						{
    return new Symbol(TokenConstants.PLUS);
}
					case -16:
						break;
					case 16:
						{
    return new Symbol(TokenConstants.EQ);
}
					case -17:
						break;
					case 17:
						{
    return new Symbol(TokenConstants.COLON);
}
					case -18:
						break;
					case 18:
						{
    return new Symbol(TokenConstants.NEG);
}
					case -19:
						break;
					case 19:
						{
    return new Symbol(TokenConstants.LBRACE);
}
					case -20:
						break;
					case 20:
						{
    return new Symbol(TokenConstants.RBRACE);
}
					case -21:
						break;
					case 21:
						{
    return new Symbol(TokenConstants.AT);
}
					case -22:
						break;
					case 22:
						{
    return new Symbol(TokenConstants.DIV);
}
					case -23:
						break;
					case 23:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -24:
						break;
					case 24:
						{
    return new Symbol(TokenConstants.INT_CONST, AbstractTable.inttable.addString(yytext()));
}
					case -25:
						break;
					case 25:
						{
    return new Symbol(TokenConstants.ERROR, "not supported operator:" + yytext());
}
					case -26:
						break;
					case 26:
						
					case -27:
						break;
					case 27:
						{
    yybegin(COMMENTOPEN);
}
					case -28:
						break;
					case 28:
						{
    return new Symbol(TokenConstants.ERROR, "Unmatched *)");
}
					case -29:
						break;
					case 29:
						{
    return new Symbol(TokenConstants.IN);
}
					case -30:
						break;
					case 30:
						{
    return new Symbol(TokenConstants.FI);
}
					case -31:
						break;
					case 31:
						{
    return new Symbol(TokenConstants.IF);
}
					case -32:
						break;
					case 32:
						{
    return new Symbol(TokenConstants.OF);
}
					case -33:
						break;
					case 33:
						{
    return new Symbol(TokenConstants.ASSIGN);
}
					case -34:
						break;
					case 34:
						{
    return new Symbol(TokenConstants.LE);
}
					case -35:
						break;
					case 35:
						{ /* Sample lexical rule for "=>" arrow.
                                     Further lexical rules should be defined
                                     here, after the last %% separator */
                                  return new Symbol(TokenConstants.DARROW); }
					case -36:
						break;
					case 36:
						{
    return new Symbol(TokenConstants.NOT);
}
					case -37:
						break;
					case 37:
						{
    return new Symbol(TokenConstants.NEW);
}
					case -38:
						break;
					case 38:
						{
    return new Symbol(TokenConstants.LET);
}
					case -39:
						break;
					case 39:
						{
     return new Symbol(TokenConstants.CASE);
}
					case -40:
						break;
					case 40:
						{
    return new Symbol(TokenConstants.LOOP);
}
					case -41:
						break;
					case 41:
						{
    return new Symbol(TokenConstants.ELSE);
}
					case -42:
						break;
					case 42:
						{
    return new Symbol(TokenConstants.ESAC);
}
					case -43:
						break;
					case 43:
						{
    return new Symbol(TokenConstants.THEN);
}
					case -44:
						break;
					case 44:
						{
    return new Symbol(TokenConstants.POOL);
}
					case -45:
						break;
					case 45:
						{
    return new Symbol(TokenConstants.BOOL_CONST, new Boolean(true));
}
					case -46:
						break;
					case 46:
						{
    return new Symbol(TokenConstants.CLASS);
}
					case -47:
						break;
					case 47:
						{
    return new Symbol(TokenConstants.WHILE);
}
					case -48:
						break;
					case 48:
						{
    return new Symbol(TokenConstants.BOOL_CONST, new Boolean(false));
}
					case -49:
						break;
					case 49:
						{
    return new Symbol(TokenConstants.ISVOID);
}
					case -50:
						break;
					case 50:
						{
    return new Symbol(TokenConstants.INHERITS);
}
					case -51:
						break;
					case 51:
						{
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
					case -52:
						break;
					case 52:
						{
    curr_lineno++;
}
					case -53:
						break;
					case 53:
						{
    // comment we do nothing
}
					case -54:
						break;
					case 54:
						{
    yybegin(YYINITIAL);
}
					case -55:
						break;
					case 56:
						{ /* This rule should be the very last
    in your lexical specification and
    will match match everything not
    matched by other lexical rules. */
    System.err.println("LEXER BUG - UNMATCHED: " + yytext()); 
}
					case -56:
						break;
					case 57:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -57:
						break;
					case 58:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -58:
						break;
					case 59:
						{
    return new Symbol(TokenConstants.IN);
}
					case -59:
						break;
					case 60:
						{
    return new Symbol(TokenConstants.FI);
}
					case -60:
						break;
					case 61:
						{
    return new Symbol(TokenConstants.IF);
}
					case -61:
						break;
					case 62:
						{
    return new Symbol(TokenConstants.OF);
}
					case -62:
						break;
					case 63:
						{
    return new Symbol(TokenConstants.NOT);
}
					case -63:
						break;
					case 64:
						{
    return new Symbol(TokenConstants.NEW);
}
					case -64:
						break;
					case 65:
						{
    return new Symbol(TokenConstants.LET);
}
					case -65:
						break;
					case 66:
						{
     return new Symbol(TokenConstants.CASE);
}
					case -66:
						break;
					case 67:
						{
    return new Symbol(TokenConstants.LOOP);
}
					case -67:
						break;
					case 68:
						{
    return new Symbol(TokenConstants.ELSE);
}
					case -68:
						break;
					case 69:
						{
    return new Symbol(TokenConstants.ESAC);
}
					case -69:
						break;
					case 70:
						{
    return new Symbol(TokenConstants.THEN);
}
					case -70:
						break;
					case 71:
						{
    return new Symbol(TokenConstants.POOL);
}
					case -71:
						break;
					case 72:
						{
    return new Symbol(TokenConstants.CLASS);
}
					case -72:
						break;
					case 73:
						{
    return new Symbol(TokenConstants.WHILE);
}
					case -73:
						break;
					case 74:
						{
    return new Symbol(TokenConstants.ISVOID);
}
					case -74:
						break;
					case 75:
						{
    return new Symbol(TokenConstants.INHERITS);
}
					case -75:
						break;
					case 76:
						{
    // comment we do nothing
}
					case -76:
						break;
					case 78:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -77:
						break;
					case 79:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -78:
						break;
					case 80:
						{
    return new Symbol(TokenConstants.IN);
}
					case -79:
						break;
					case 81:
						{
    return new Symbol(TokenConstants.FI);
}
					case -80:
						break;
					case 82:
						{
    return new Symbol(TokenConstants.IF);
}
					case -81:
						break;
					case 83:
						{
    return new Symbol(TokenConstants.NOT);
}
					case -82:
						break;
					case 84:
						{
    return new Symbol(TokenConstants.NEW);
}
					case -83:
						break;
					case 85:
						{
     return new Symbol(TokenConstants.CASE);
}
					case -84:
						break;
					case 86:
						{
    return new Symbol(TokenConstants.LOOP);
}
					case -85:
						break;
					case 87:
						{
    return new Symbol(TokenConstants.ELSE);
}
					case -86:
						break;
					case 88:
						{
    return new Symbol(TokenConstants.CLASS);
}
					case -87:
						break;
					case 89:
						{
    return new Symbol(TokenConstants.WHILE);
}
					case -88:
						break;
					case 90:
						{
    return new Symbol(TokenConstants.ISVOID);
}
					case -89:
						break;
					case 91:
						{
    return new Symbol(TokenConstants.INHERITS);
}
					case -90:
						break;
					case 93:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -91:
						break;
					case 94:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -92:
						break;
					case 95:
						{
    return new Symbol(TokenConstants.IN);
}
					case -93:
						break;
					case 96:
						{
    return new Symbol(TokenConstants.NOT);
}
					case -94:
						break;
					case 97:
						{
     return new Symbol(TokenConstants.CASE);
}
					case -95:
						break;
					case 98:
						{
    return new Symbol(TokenConstants.ELSE);
}
					case -96:
						break;
					case 99:
						{
    return new Symbol(TokenConstants.CLASS);
}
					case -97:
						break;
					case 100:
						{
    return new Symbol(TokenConstants.ISVOID);
}
					case -98:
						break;
					case 102:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -99:
						break;
					case 103:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -100:
						break;
					case 104:
						{
    return new Symbol(TokenConstants.IN);
}
					case -101:
						break;
					case 105:
						{
    return new Symbol(TokenConstants.NOT);
}
					case -102:
						break;
					case 106:
						{
     return new Symbol(TokenConstants.CASE);
}
					case -103:
						break;
					case 108:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -104:
						break;
					case 109:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -105:
						break;
					case 110:
						{
     return new Symbol(TokenConstants.CASE);
}
					case -106:
						break;
					case 112:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -107:
						break;
					case 113:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -108:
						break;
					case 115:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -109:
						break;
					case 116:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -110:
						break;
					case 118:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -111:
						break;
					case 119:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -112:
						break;
					case 121:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -113:
						break;
					case 122:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -114:
						break;
					case 124:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -115:
						break;
					case 125:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -116:
						break;
					case 127:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -117:
						break;
					case 128:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -118:
						break;
					case 130:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -119:
						break;
					case 131:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -120:
						break;
					case 133:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -121:
						break;
					case 134:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -122:
						break;
					case 136:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -123:
						break;
					case 137:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -124:
						break;
					case 139:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -125:
						break;
					case 140:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -126:
						break;
					case 142:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -127:
						break;
					case 143:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -128:
						break;
					case 145:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -129:
						break;
					case 146:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -130:
						break;
					case 148:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -131:
						break;
					case 149:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -132:
						break;
					case 151:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -133:
						break;
					case 152:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -134:
						break;
					case 154:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -135:
						break;
					case 155:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -136:
						break;
					case 157:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -137:
						break;
					case 158:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -138:
						break;
					case 160:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -139:
						break;
					case 161:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -140:
						break;
					case 163:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -141:
						break;
					case 164:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -142:
						break;
					case 166:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -143:
						break;
					case 167:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -144:
						break;
					case 169:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -145:
						break;
					case 170:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -146:
						break;
					case 172:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -147:
						break;
					case 173:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -148:
						break;
					case 175:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -149:
						break;
					case 176:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -150:
						break;
					case 178:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -151:
						break;
					case 179:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -152:
						break;
					case 181:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -153:
						break;
					case 182:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -154:
						break;
					case 184:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -155:
						break;
					case 185:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -156:
						break;
					case 186:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -157:
						break;
					case 187:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -158:
						break;
					case 188:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -159:
						break;
					case 189:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -160:
						break;
					case 190:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -161:
						break;
					case 191:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -162:
						break;
					case 192:
						{
    return new Symbol(TokenConstants.BOOL_CONST, new Boolean(true));
}
					case -163:
						break;
					case 193:
						{
    return new Symbol(TokenConstants.BOOL_CONST, new Boolean(false));
}
					case -164:
						break;
					case 195:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -165:
						break;
					case 196:
						{
    return new Symbol(TokenConstants.OF);
}
					case -166:
						break;
					case 197:
						{
    return new Symbol(TokenConstants.LET);
}
					case -167:
						break;
					case 198:
						{
    return new Symbol(TokenConstants.ESAC);
}
					case -168:
						break;
					case 199:
						{
    return new Symbol(TokenConstants.THEN);
}
					case -169:
						break;
					case 200:
						{
    return new Symbol(TokenConstants.POOL);
}
					case -170:
						break;
					case 202:
						{
    return new Symbol(TokenConstants.FI);
}
					case -171:
						break;
					case 203:
						{
    return new Symbol(TokenConstants.IF);
}
					case -172:
						break;
					case 204:
						{
    return new Symbol(TokenConstants.NEW);
}
					case -173:
						break;
					case 205:
						{
    return new Symbol(TokenConstants.LOOP);
}
					case -174:
						break;
					case 206:
						{
    return new Symbol(TokenConstants.WHILE);
}
					case -175:
						break;
					case 207:
						{
    return new Symbol(TokenConstants.INHERITS);
}
					case -176:
						break;
					case 208:
						{
    return new Symbol(TokenConstants.ELSE);
}
					case -177:
						break;
					case 209:
						{
    return new Symbol(TokenConstants.CLASS);
}
					case -178:
						break;
					case 210:
						{
    return new Symbol(TokenConstants.ISVOID);
}
					case -179:
						break;
					case 211:
						{
    return new Symbol(TokenConstants.IN);
}
					case -180:
						break;
					case 212:
						{
    return new Symbol(TokenConstants.NOT);
}
					case -181:
						break;
					case 213:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -182:
						break;
					case 214:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -183:
						break;
					case 215:
						{
     return new Symbol(TokenConstants.CASE);
}
					case -184:
						break;
					case 216:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -185:
						break;
					case 217:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -186:
						break;
					case 218:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -187:
						break;
					case 219:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -188:
						break;
					case 220:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -189:
						break;
					case 221:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -190:
						break;
					case 222:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -191:
						break;
					case 223:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -192:
						break;
					case 224:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -193:
						break;
					case 225:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -194:
						break;
					case 226:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -195:
						break;
					case 227:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -196:
						break;
					case 228:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -197:
						break;
					case 229:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -198:
						break;
					case 230:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -199:
						break;
					case 231:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -200:
						break;
					case 232:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -201:
						break;
					case 233:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -202:
						break;
					case 234:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -203:
						break;
					case 235:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -204:
						break;
					case 236:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -205:
						break;
					case 237:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -206:
						break;
					case 238:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -207:
						break;
					case 239:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -208:
						break;
					case 240:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -209:
						break;
					case 241:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -210:
						break;
					case 242:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -211:
						break;
					case 243:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -212:
						break;
					case 244:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -213:
						break;
					case 245:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -214:
						break;
					case 246:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -215:
						break;
					case 247:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -216:
						break;
					case 248:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -217:
						break;
					case 249:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -218:
						break;
					case 250:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -219:
						break;
					case 251:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -220:
						break;
					case 252:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -221:
						break;
					case 253:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -222:
						break;
					case 254:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -223:
						break;
					case 255:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -224:
						break;
					case 256:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -225:
						break;
					case 257:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -226:
						break;
					case 258:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -227:
						break;
					case 259:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -228:
						break;
					case 260:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -229:
						break;
					case 261:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -230:
						break;
					case 262:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -231:
						break;
					case 263:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -232:
						break;
					case 264:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -233:
						break;
					case 265:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -234:
						break;
					case 266:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -235:
						break;
					case 267:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -236:
						break;
					case 268:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -237:
						break;
					case 269:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -238:
						break;
					case 270:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -239:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}

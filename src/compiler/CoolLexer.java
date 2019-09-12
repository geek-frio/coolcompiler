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
	private final int LINECOMMENT = 3;
	private final int COMMENTOPEN = 2;
	private final int yy_state_dtrans[] = {
		0,
		184,
		187,
		56
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
		/* 55 */ YY_NO_ANCHOR,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NOT_ACCEPT,
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
		/* 77 */ YY_NO_ANCHOR,
		/* 78 */ YY_NO_ANCHOR,
		/* 79 */ YY_NO_ANCHOR,
		/* 80 */ YY_NO_ANCHOR,
		/* 81 */ YY_NOT_ACCEPT,
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
		/* 92 */ YY_NO_ANCHOR,
		/* 93 */ YY_NO_ANCHOR,
		/* 94 */ YY_NO_ANCHOR,
		/* 95 */ YY_NO_ANCHOR,
		/* 96 */ YY_NOT_ACCEPT,
		/* 97 */ YY_NO_ANCHOR,
		/* 98 */ YY_NO_ANCHOR,
		/* 99 */ YY_NO_ANCHOR,
		/* 100 */ YY_NO_ANCHOR,
		/* 101 */ YY_NO_ANCHOR,
		/* 102 */ YY_NO_ANCHOR,
		/* 103 */ YY_NO_ANCHOR,
		/* 104 */ YY_NO_ANCHOR,
		/* 105 */ YY_NOT_ACCEPT,
		/* 106 */ YY_NO_ANCHOR,
		/* 107 */ YY_NO_ANCHOR,
		/* 108 */ YY_NO_ANCHOR,
		/* 109 */ YY_NO_ANCHOR,
		/* 110 */ YY_NO_ANCHOR,
		/* 111 */ YY_NOT_ACCEPT,
		/* 112 */ YY_NO_ANCHOR,
		/* 113 */ YY_NO_ANCHOR,
		/* 114 */ YY_NO_ANCHOR,
		/* 115 */ YY_NOT_ACCEPT,
		/* 116 */ YY_NO_ANCHOR,
		/* 117 */ YY_NO_ANCHOR,
		/* 118 */ YY_NOT_ACCEPT,
		/* 119 */ YY_NO_ANCHOR,
		/* 120 */ YY_NO_ANCHOR,
		/* 121 */ YY_NOT_ACCEPT,
		/* 122 */ YY_NO_ANCHOR,
		/* 123 */ YY_NO_ANCHOR,
		/* 124 */ YY_NOT_ACCEPT,
		/* 125 */ YY_NO_ANCHOR,
		/* 126 */ YY_NO_ANCHOR,
		/* 127 */ YY_NOT_ACCEPT,
		/* 128 */ YY_NO_ANCHOR,
		/* 129 */ YY_NO_ANCHOR,
		/* 130 */ YY_NOT_ACCEPT,
		/* 131 */ YY_NO_ANCHOR,
		/* 132 */ YY_NO_ANCHOR,
		/* 133 */ YY_NOT_ACCEPT,
		/* 134 */ YY_NO_ANCHOR,
		/* 135 */ YY_NO_ANCHOR,
		/* 136 */ YY_NOT_ACCEPT,
		/* 137 */ YY_NO_ANCHOR,
		/* 138 */ YY_NO_ANCHOR,
		/* 139 */ YY_NOT_ACCEPT,
		/* 140 */ YY_NO_ANCHOR,
		/* 141 */ YY_NO_ANCHOR,
		/* 142 */ YY_NOT_ACCEPT,
		/* 143 */ YY_NO_ANCHOR,
		/* 144 */ YY_NO_ANCHOR,
		/* 145 */ YY_NOT_ACCEPT,
		/* 146 */ YY_NO_ANCHOR,
		/* 147 */ YY_NO_ANCHOR,
		/* 148 */ YY_NOT_ACCEPT,
		/* 149 */ YY_NO_ANCHOR,
		/* 150 */ YY_NO_ANCHOR,
		/* 151 */ YY_NOT_ACCEPT,
		/* 152 */ YY_NO_ANCHOR,
		/* 153 */ YY_NO_ANCHOR,
		/* 154 */ YY_NOT_ACCEPT,
		/* 155 */ YY_NO_ANCHOR,
		/* 156 */ YY_NO_ANCHOR,
		/* 157 */ YY_NOT_ACCEPT,
		/* 158 */ YY_NO_ANCHOR,
		/* 159 */ YY_NO_ANCHOR,
		/* 160 */ YY_NOT_ACCEPT,
		/* 161 */ YY_NO_ANCHOR,
		/* 162 */ YY_NO_ANCHOR,
		/* 163 */ YY_NOT_ACCEPT,
		/* 164 */ YY_NO_ANCHOR,
		/* 165 */ YY_NO_ANCHOR,
		/* 166 */ YY_NOT_ACCEPT,
		/* 167 */ YY_NO_ANCHOR,
		/* 168 */ YY_NO_ANCHOR,
		/* 169 */ YY_NOT_ACCEPT,
		/* 170 */ YY_NO_ANCHOR,
		/* 171 */ YY_NO_ANCHOR,
		/* 172 */ YY_NOT_ACCEPT,
		/* 173 */ YY_NO_ANCHOR,
		/* 174 */ YY_NO_ANCHOR,
		/* 175 */ YY_NOT_ACCEPT,
		/* 176 */ YY_NO_ANCHOR,
		/* 177 */ YY_NO_ANCHOR,
		/* 178 */ YY_NOT_ACCEPT,
		/* 179 */ YY_NO_ANCHOR,
		/* 180 */ YY_NO_ANCHOR,
		/* 181 */ YY_NOT_ACCEPT,
		/* 182 */ YY_NO_ANCHOR,
		/* 183 */ YY_NO_ANCHOR,
		/* 184 */ YY_NOT_ACCEPT,
		/* 185 */ YY_NO_ANCHOR,
		/* 186 */ YY_NO_ANCHOR,
		/* 187 */ YY_NOT_ACCEPT,
		/* 188 */ YY_NO_ANCHOR,
		/* 189 */ YY_NO_ANCHOR,
		/* 190 */ YY_NO_ANCHOR,
		/* 191 */ YY_NO_ANCHOR,
		/* 192 */ YY_NO_ANCHOR,
		/* 193 */ YY_NO_ANCHOR,
		/* 194 */ YY_NO_ANCHOR,
		/* 195 */ YY_NO_ANCHOR,
		/* 196 */ YY_NO_ANCHOR,
		/* 197 */ YY_NO_ANCHOR,
		/* 198 */ YY_NOT_ACCEPT,
		/* 199 */ YY_NO_ANCHOR,
		/* 200 */ YY_NO_ANCHOR,
		/* 201 */ YY_NO_ANCHOR,
		/* 202 */ YY_NO_ANCHOR,
		/* 203 */ YY_NO_ANCHOR,
		/* 204 */ YY_NO_ANCHOR,
		/* 205 */ YY_NOT_ACCEPT,
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
		/* 270 */ YY_NO_ANCHOR,
		/* 271 */ YY_NO_ANCHOR,
		/* 272 */ YY_NO_ANCHOR,
		/* 273 */ YY_NO_ANCHOR,
		/* 274 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"5:8,2:2,1,5:2,0,5:18,2,5,3,5:4,61,6,8,7,28,9,4,10,38,58:10,33,11,20,29,21,5" +
",37,13,59,12,32,16,26,59,23,22,59:2,25,59,17,18,27,59,24,15,19,40,31,30,59:" +
"3,61,5,61,5,60,5,43,42,44,45,46,41,42,47,48,42:2,49,42,50,51,52,42,53,54,39" +
",55,56,57,42:3,35,14,36,34,5,62:2")[0];

	private int yy_rmap[] = unpackFromString(1,275,
"0,1,2,1,3,1,4,5,1:4,6,7,1:2,8,1:6,9,10,1:5,11,1:2,12,1:3,13,1,12,1:2,14,12:" +
"3,15,1:2,15,16,1:5,17,1,18,19,12,20,21,12:2,15,1,12,15,12:2,1,15:3,12:2,1,1" +
"2,22,23,24,25,26,27,15:2,28,15,14,15,12,29,15,12,15,30,31,32,33,12,34,15:3," +
"35,36,37,38,15,39,40,41,42,15,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57," +
"58,59,60,28,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,14," +
"81,82,83,84,85,86,87,88,39,89,90,91,92,93,94,95,96,97,98,99,29,100,101,16,1" +
"02,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119:2,12" +
"0,121,119:5,122,119:9,123,119,124,125,119,126,127,128,129,130,131,132,133,1" +
"34,135,136,137,138,139,140,141,142,143,144,145,146,147,148,149,150,151,152," +
"153,154,155,156,157,158,159,119,160,161,162,163,164,165,166,167,168,169,170" +
",171,172,173,119,174,175,176,171,15")[0];

	private int yy_nxt[][] = unpackFromString(177,63,
"-1,1,2,3,4,5,6,7,8,9,10,11,12,60,59,60,194,249,82,270,13,14,97,60:2,253,106" +
",272,15,16,257,60:2,17,18,19,20,21,22,23,60,61,269:2,83,269,98,269,107,113," +
"218,117,271,269:4,120,24,60,273,25,26,-1:65,2,-1:64,27,-1:65,28,-1:63,29,-1" +
":66,60,112,58,60:5,-1:2,60:3,116,60:2,-1:2,60:3,-1:6,60:4,112,60:5,116,60:1" +
"1,-1:6,34,-1:24,35,-1:54,36,-1:53,274:2,-1,274:5,-1:2,274,195,250,274:3,-1:" +
"2,274:3,-1:6,254,274,254:6,258,254:5,259,254:4,274:2,254,-1:60,24,-1:17,127" +
",37,124,-1:2,133,66,-1:2,136,139,-1:6,38,142,-1:7,66,-1:3,127,-1:3,139,136," +
"-1:2,133,-1:2,124,-1,142,38,-1:17,60:2,-1,60:5,-1:2,60:6,-1:2,60:3,-1:6,60:" +
"22,-1:16,101,154,110,-1,157,-1:6,160,-1,41,-1:18,110,-1:2,160,-1,157,41,-1," +
"154,-1:22,47:2,-1:38,47,-1:20,274:2,-1,274:5,-1:2,274:6,-1:2,274:3,-1:6,274" +
":22,-1:16,181,-1:4,181,-1:19,181,-1:24,57,80:60,26,-1:13,127,130,124,-1:27," +
"127,-1:10,124,-1:21,81,30,96,105,62,111,-1:3,31,115,-1,198,32,-1:14,32,-1,8" +
"1,-1:2,105,115,31,198,62,111,-1:2,96,-1:20,274,260,31,274:5,-1:2,85,274:5,-" +
"1:2,274:3,-1:6,254,274,254:2,261,254:4,206,254:9,274:2,254,-1:16,139,-1:8,1" +
"39,-1:23,139,-1:23,55,-1:56,80:60,-1:15,124:2,-1:38,124,-1:20,60:2,-1,60:5," +
"-1:2,60:4,33,60,-1:2,60:3,-1:6,60:2,33,60:19,-1:14,274,123,58,274:5,-1:2,27" +
"4:3,126,274:2,-1:2,274:3,-1:6,254,274,254:2,220,254:5,221,254:8,274:2,254,-" +
"1:16,151,-1:8,139,-1:7,142,-1:15,139,-1:8,142,-1:20,41,-1:12,41,-1:24,41,-1" +
":24,50,-1:7,178,-1:9,77,-1:12,77,-1:2,178,-1:28,142,-1:16,142,-1:24,142,-1:" +
"18,60:2,84,125,60,99,60:2,-1:2,60:4,64,60,-1:2,60:3,-1:6,60:2,64,60:8,99,60" +
":3,125,60:6,-1:14,274:2,205,262,274:4,-1:2,274:3,252,274:2,-1:2,274:3,-1:6," +
"254,274,254:8,256,254:4,263,254:3,274:2,254,-1:14,60:2,139,60:5,-1:2,60,152" +
",60:4,-1:2,60:3,-1:6,60:8,152,60:13,-1:16,92,47,48,-1:5,169,-1,172,-1:21,48" +
",-1,169,-1:4,172,47,-1:22,38,-1:15,38,-1:26,38,-1:17,60:2,31,60:5,-1:2,63,6" +
"0:5,-1:2,60:3,-1:6,60:9,63,60:12,-1:14,274:2,84,129,274,108,274:2,-1:2,274:" +
"4,86,274,-1:2,274:3,-1:6,254,274,207,254:8,215,254:3,222,254:3,274:2,254,-1" +
":14,274:2,139,274:5,-1:2,274,168,274:4,-1:2,274:3,-1:6,274:8,168,274:13,-1:" +
"16,172,-1:9,172,-1:28,172,-1:23,87,-1:3,133,66,-1:19,66,-1:11,133,-1:23,60:" +
"2,124,137,60:4,-1:2,60:6,-1:2,60:3,-1:6,60:15,137,60:6,-1:14,274:2,121,274," +
"132,274,135,274,-1:2,274:6,-1:2,274:3,-1:6,254,274,254:5,224,254:4,225,254:" +
"6,274:2,254,-1:16,136,-1:7,136,-1:25,136,-1:26,60,140,127,60:5,-1:2,60:6,-1" +
":2,60:3,-1:6,60:4,140,60:17,-1:14,274:2,-1,274:5,-1:2,274:4,65,274,-1:2,274" +
":3,-1:6,254,274,200,254:16,274:2,254,-1:16,66,-1:4,66,-1:10,38,-1:8,66,-1:1" +
"7,38,-1:17,60:2,38,60:5,-1:2,60:6,-1:2,67,60:2,-1:6,60:18,67,60:3,-1:14,274" +
":2,115,274:5,-1:2,274,144,274:4,-1:2,274:3,-1:6,254,274,254:6,228,254:10,27" +
"4:2,254,-1:16,133,-1:3,133,-1:32,133,-1:23,60:2,66,60:4,100,-1:2,60:6,-1:2," +
"60:3,-1:6,100,60:21,-1:14,274:2,124,153,274:4,-1:2,274:6,-1:2,274:3,-1:6,27" +
"4:15,153,274:6,-1:16,40,-1,40,-1:29,40,-1:28,60:2,142,60:5,-1:2,60:6,-1:2,6" +
"0,223,60,-1:6,60:17,223,60:4,-1:14,274,156,127,274:5,-1:2,274:6,-1:2,274:3," +
"-1:6,274:4,156,274:17,-1:16,154:2,-1:38,154,-1:20,60:2,-1,60:4,39,-1:2,60:6" +
",-1:2,60:3,-1:6,39,60:21,-1:14,274:2,142,274:5,-1:2,274:6,-1:2,274,165,274," +
"-1:6,274:17,165,274:4,-1:16,89,154,40,-1:29,40,-1:7,154,-1:20,60:2,133,60:3" +
",155,60,-1:2,60:6,-1:2,60:3,-1:6,60:12,155,60:9,-1:14,274:2,-1,274:4,68,-1:" +
"2,274:6,-1:2,274:3,-1:6,68,274:21,-1:14,60:2,136,60:5,-1:2,161,60:5,-1:2,60" +
":3,-1:6,60:9,161,60:12,-1:14,274:2,133,274:3,171,274,-1:2,274:6,-1:2,274:3," +
"-1:6,274:12,171,274:9,-1:16,160,-1:10,160,-1:23,160,-1:25,60:2,40,60,69,60:" +
"3,-1:2,60:6,-1:2,60:3,-1:6,60:7,69,60:14,-1:14,274:2,38,274:5,-1:2,274:6,-1" +
":2,88,274:2,-1:6,274:18,88,274:3,-1:16,163,-1,163,-1:29,163,-1:28,60:2,154," +
"164,60:4,-1:2,60:6,-1:2,60:3,-1:6,60:15,164,60:6,-1:14,274:2,66,274:4,109,-" +
"1:2,274:6,-1:2,274:3,-1:6,109,274:21,-1:16,157,-1:3,157,-1:32,157,-1:23,43," +
"60,-1,60:5,-1:2,60:6,-1:2,60:3,-1:6,60:5,43,60:16,-1:14,274:2,136,274:5,-1:" +
"2,177,274:5,-1:2,274:3,-1:6,274:9,177,274:12,-1:16,42,154,71,-1:29,71,-1:7," +
"154,-1:20,60:2,71,60,91,60:3,-1:2,60:6,-1:2,60:3,-1:6,60:7,91,60:14,-1:14,2" +
"74:2,-1,274:2,73,274:2,-1:2,274:6,-1:2,274:3,-1:6,274:11,73,274:10,-1:16,71" +
",-1,71,-1:29,71,-1:28,60:2,-1,60:2,44,60:2,-1:2,60:6,-1:2,60:3,-1:6,60:11,4" +
"4,60:10,-1:14,274:2,-1,274,46,274:3,-1:2,274:6,-1:2,274:3,-1:6,274:7,46,274" +
":14,-1:16,166,-1,163,-1,157,-1:27,163,-1:4,157,-1:23,60:2,163,60,170,60:3,-" +
"1:2,60:6,-1:2,60:3,-1:6,60:7,170,60:14,-1:14,274:2,40,274,114,274:3,-1:2,27" +
"4:6,-1:2,274:3,-1:6,274:7,114,274:14,-1:14,60:2,41,60:5,-1:2,60:5,70,-1:2,6" +
"0:3,-1:6,60:13,70,60:8,-1:14,274:2,154,183,274:4,-1:2,274:6,-1:2,274:3,-1:6" +
",274:15,183,274:6,-1:16,169,-1:7,169,-1:25,169,-1:26,60:2,-1,60:5,-1:2,60:3" +
",45,60:2,-1:2,60:3,-1:6,60:10,45,60:11,-1:14,72,274,-1,274:5,-1:2,274:6,-1:" +
"2,274:3,-1:6,274:5,72,274:16,-1:16,48,-1,48,-1:29,48,-1:28,60:2,160,60:5,-1" +
":2,60:3,173,60:2,-1:2,60:3,-1:6,60:10,173,60:11,-1:14,274:2,71,274,102,274:" +
"3,-1:2,274:6,-1:2,274:3,-1:6,274:7,102,274:14,-1:14,60:2,47,75,60:4,-1:2,60" +
":6,-1:2,60:3,-1:6,60:15,75,60:6,-1:14,274:2,157,274:3,186,274,-1:2,274:6,-1" +
":2,274:3,-1:6,274:12,186,274:9,-1:16,175,-1:7,169,-1,172,-1:23,169,-1:4,172" +
",-1:21,60:2,169,60:5,-1:2,176,60:5,-1:2,60:3,-1:6,60:9,176,60:12,-1:14,274:" +
"2,163,274,188,274:3,-1:2,274:6,-1:2,274:3,-1:6,274:7,188,274:14,-1:16,77,-1" +
":17,77,-1:12,77,-1:29,60:2,172,60:5,-1:2,60:2,179,60:3,-1:2,60:3,-1:6,60:14" +
",179,60:7,-1:14,274:2,41,274:5,-1:2,274:5,90,-1:2,274:3,-1:6,274:13,90,274:" +
"8,-1:16,178,-1:7,178,-1:25,178,-1:26,60:2,48,60,76,60:3,-1:2,60:6,-1:2,60:3" +
",-1:6,60:7,76,60:14,-1:14,274:2,-1,274:5,-1:2,274:3,74,274:2,-1:2,274:3,-1:" +
"6,274:10,74,274:11,-1:14,60:2,77,60:5,-1:2,60:6,-1:2,60:2,94,-1:6,60:6,94,6" +
"0:15,-1:14,274:2,160,274:5,-1:2,274:3,189,274:2,-1:2,274:3,-1:6,274:10,189," +
"274:11,-1:14,60:2,178,60:5,-1:2,182,60:5,-1:2,60:3,-1:6,60:9,182,60:12,-1:1" +
"4,274:2,-1,274,49,274:3,-1:2,274:6,-1:2,274:3,-1:6,274:7,49,274:14,-1:16,51" +
":2,-1:38,51,-1:20,60:2,181,60:4,185,-1:2,60:6,-1:2,60:3,-1:6,185,60:21,-1:1" +
"4,274:2,47,103,274:4,-1:2,274:6,-1:2,274:3,-1:6,274:15,103,274:6,-1:3,52:61" +
",26,-1:12,60:2,51,78,60:4,-1:2,60:6,-1:2,60:3,-1:6,60:15,78,60:6,-1:14,274:" +
"2,169,274:5,-1:2,190,274:5,-1:2,274:3,-1:6,274:9,190,274:12,-1:3,53,54:5,79" +
",54:54,26,-1:12,274:2,172,274:5,-1:2,274:2,191,274:3,-1:2,274:3,-1:6,274:14" +
",191,274:7,-1:14,274:2,48,274,93,274:3,-1:2,274:6,-1:2,274:3,-1:6,274:7,93," +
"274:14,-1:14,274:2,77,274:5,-1:2,274:6,-1:2,274:2,104,-1:6,274:6,104,274:15" +
",-1:14,274:2,178,274:5,-1:2,192,274:5,-1:2,274:3,-1:6,274:9,192,274:12,-1:1" +
"4,274:2,181,274:4,193,-1:2,274:6,-1:2,274:3,-1:6,193,274:21,-1:14,274:2,51," +
"95,274:4,-1:2,274:6,-1:2,274:3,-1:6,274:15,95,274:6,-1:14,60:2,205,199,60:4" +
",-1:2,60:3,217,60:2,-1:2,60:3,-1:6,60:10,217,60:4,199,60:6,-1:14,274:2,-1,2" +
"74,147,274:3,-1:2,274:6,-1:2,274:3,-1:6,274:7,147,274:14,-1:14,274:2,-1,274" +
":5,-1:2,274:6,-1:2,274:3,-1:6,254,274,254:17,274:2,254,-1:15,127,145,148,-1" +
":27,127,-1:10,148,-1:20,60,143,-1,60:5,-1:2,60:6,-1:2,60:3,-1:6,60:4,143,60" +
":17,-1:16,148:2,-1:38,148,-1:20,274:2,139,274:5,-1:2,274,168,274:4,-1:2,274" +
":3,-1:6,254,274,254:6,236,254:10,274:2,254,-1:14,60:2,148,146,60:4,-1:2,60:" +
"6,-1:2,60:3,-1:6,60:15,146,60:6,-1:14,274:2,118,274,138,274,141,274,-1:2,27" +
"4:6,-1:2,274:3,-1:6,254,274,254:5,226,254:4,227,254:6,274:2,254,-1:14,274:2" +
",124,153,274:4,-1:2,274:6,-1:2,274:3,-1:6,254,274,254:13,231,254:3,274:2,25" +
"4,-1:14,274,156,127,274:5,-1:2,274:6,-1:2,274:3,-1:6,254,274,254:2,232,254:" +
"14,274:2,254,-1:14,274:2,142,274:5,-1:2,274:6,-1:2,274,165,274,-1:6,254,274" +
",254:15,235,254,274:2,254,-1:14,60:2,157,60:3,167,60,-1:2,60:6,-1:2,60:3,-1" +
":6,60:12,167,60:9,-1:14,274:2,-1,274:4,68,-1:2,274:6,-1:2,274:3,-1:6,201,27" +
"4,254:17,274:2,254,-1:14,274:2,133,274:3,171,274,-1:2,274:6,-1:2,274:3,-1:6" +
",254,274,254:10,237,254:6,274:2,254,-1:14,274:2,38,274:5,-1:2,274:6,-1:2,88" +
",274:2,-1:6,254,274,254:16,208,274:2,254,-1:14,274:2,66,274:4,109,-1:2,274:" +
"6,-1:2,274:3,-1:6,216,274,254:17,274:2,254,-1:14,274:2,136,274:5,-1:2,177,2" +
"74:5,-1:2,274:3,-1:6,254,274,254:7,239,254:9,274:2,254,-1:14,274:2,-1,274:2" +
",73,274:2,-1:2,274:6,-1:2,274:3,-1:6,254,274,254:9,203,254:7,274:2,254,-1:1" +
"4,274:2,-1,274,46,274:3,-1:2,274:6,-1:2,274:3,-1:6,254,274,254:5,196,254:11" +
",274:2,254,-1:14,274:2,40,274,114,274:3,-1:2,274:6,-1:2,274:3,-1:6,254,274," +
"254:5,219,254:11,274:2,254,-1:14,274:2,154,183,274:4,-1:2,274:6,-1:2,274:3," +
"-1:6,254,274,254:13,241,254:3,274:2,254,-1:14,72,274,-1,274:5,-1:2,274:6,-1" +
":2,274:3,-1:6,254,274,254:3,202,254:13,274:2,254,-1:14,274:2,71,274,102,274" +
":3,-1:2,274:6,-1:2,274:3,-1:6,254,274,254:5,212,254:11,274:2,254,-1:14,274:" +
"2,157,274:3,186,274,-1:2,274:6,-1:2,274:3,-1:6,254,274,254:10,242,254:6,274" +
":2,254,-1:14,274:2,163,274,188,274:3,-1:2,274:6,-1:2,274:3,-1:6,254,274,254" +
":5,243,254:11,274:2,254,-1:14,274:2,41,274:5,-1:2,274:5,90,-1:2,274:3,-1:6," +
"254,274,254:11,209,254:5,274:2,254,-1:14,274:2,-1,274:5,-1:2,274:3,74,274:2" +
",-1:2,274:3,-1:6,254,274,254:8,204,254:8,274:2,254,-1:14,274:2,160,274:5,-1" +
":2,274:3,189,274:2,-1:2,274:3,-1:6,254,274,254:8,244,254:8,274:2,254,-1:14," +
"274:2,-1,274,49,274:3,-1:2,274:6,-1:2,274:3,-1:6,254,274,254:5,197,254:11,2" +
"74:2,254,-1:14,274:2,47,103,274:4,-1:2,274:6,-1:2,274:3,-1:6,254,274,254:13" +
",213,254:3,274:2,254,-1:14,274:2,169,274:5,-1:2,190,274:5,-1:2,274:3,-1:6,2" +
"54,274,254:7,245,254:9,274:2,254,-1:14,274:2,172,274:5,-1:2,274:2,191,274:3" +
",-1:2,274:3,-1:6,254,274,254:12,246,254:4,274:2,254,-1:14,274:2,48,274,93,2" +
"74:3,-1:2,274:6,-1:2,274:3,-1:6,254,274,254:5,210,254:11,274:2,254,-1:14,27" +
"4:2,77,274:5,-1:2,274:6,-1:2,274:2,104,-1:6,254,274,254:4,214,254:12,274:2," +
"254,-1:14,274:2,178,274:5,-1:2,192,274:5,-1:2,274:3,-1:6,254,274,254:7,247," +
"254:9,274:2,254,-1:14,274:2,181,274:4,193,-1:2,274:6,-1:2,274:3,-1:6,248,27" +
"4,254:17,274:2,254,-1:14,274:2,51,95,274:4,-1:2,274:6,-1:2,274:3,-1:6,254,2" +
"74,254:13,211,254:3,274:2,254,-1:14,60:2,118,60,119,60,122,60,-1:2,60:6,-1:" +
"2,60:3,-1:6,60:7,119,60:4,122,60:9,-1:14,274:2,-1,274:5,-1:2,274:6,-1:2,274" +
":3,-1:6,274,150,274:14,150,274:5,-1:14,60:2,-1,60,149,60:3,-1:2,60:6,-1:2,6" +
"0:3,-1:6,60:7,149,60:14,-1:14,274:2,148,162,274:4,-1:2,274:6,-1:2,274:3,-1:" +
"6,274:15,162,274:6,-1:14,60:2,121,60,128,60,131,60,-1:2,60:6,-1:2,60:3,-1:6" +
",60:7,128,60:4,131,60:9,-1:14,60:2,-1,60:3,158,60,-1:2,60:6,-1:2,60:3,-1:6," +
"60:12,158,60:9,-1:14,274:2,148,162,274:4,-1:2,274:6,-1:2,274:3,-1:6,254,274" +
",254:13,234,254:3,274:2,254,-1:14,60:2,115,60:5,-1:2,60,134,60:4,-1:2,60:3," +
"-1:6,60:8,134,60:13,-1:14,274:2,-1,274,147,274:3,-1:2,274:6,-1:2,274:3,-1:6" +
",254,274,254:5,229,254:11,274:2,254,-1:14,274:2,-1,274:5,-1:2,274:6,-1:2,27" +
"4:3,-1:6,254,150,254:14,230,254:2,274:2,254,-1:14,274:2,-1,274:5,-1:2,274:3" +
",267,274:2,-1:2,274:3,-1:6,274:10,267,274:11,-1:14,274:2,-1,274:5,-1:2,274:" +
"3,267,274:2,-1:2,274:3,-1:6,254,274,254:8,268,254:8,274:2,254,-1:14,274,159" +
",-1,274:5,-1:2,274:6,-1:2,274:3,-1:6,274:4,159,274:17,-1:14,274,159,-1,274:" +
"5,-1:2,274:6,-1:2,274:3,-1:6,254,274,254:2,233,254:14,274:2,254,-1:14,274:2" +
",-1,274:3,174,274,-1:2,274:6,-1:2,274:3,-1:6,274:12,174,274:9,-1:14,274:2,-" +
"1,274:3,174,274,-1:2,274:6,-1:2,274:3,-1:6,254,274,254:10,238,254:6,274:2,2" +
"54,-1:14,266:2,-1,266:5,-1:2,266:6,-1:2,266:3,-1:6,266:22,-1:14,274:2,-1,18" +
"0,274:4,-1:2,274:6,-1:2,274:3,-1:6,274:15,180,274:6,-1:14,274:2,-1,180,274:" +
"4,-1:2,274:6,-1:2,274:3,-1:6,254,274,254:13,240,254:3,274:2,254,-1:14,60:2," +
"-1,60:5,-1:2,60,251,60:4,-1:2,60:3,-1:6,60:8,251,60:13,-1:14,274:2,-1,274:3" +
",264,274,-1:2,274:6,-1:2,274:3,-1:6,254,274,254:10,265,254:6,274:2,254,-1:1" +
"4,60:2,-1,60:3,255,60,-1:2,60:6,-1:2,60:3,-1:6,60:12,255,60:9,-1:2");

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
						{
    return new Symbol(TokenConstants.MINUS);
}
					case -5:
						break;
					case 5:
						{ /* This rule should be the very last
    in your lexical specification and
    will match match everything not
    matched by other lexical rules. */
    System.err.println("LEXER BUG - UNMATCHED: " + yytext()); 
}
					case -6:
						break;
					case 6:
						{
    return new Symbol(TokenConstants.LPAREN);
}
					case -7:
						break;
					case 7:
						{
    return new Symbol(TokenConstants.MULT);
}
					case -8:
						break;
					case 8:
						{
    return new Symbol(TokenConstants.RPAREN);
}
					case -9:
						break;
					case 9:
						{
    return new Symbol(TokenConstants.COMMA);
}
					case -10:
						break;
					case 10:
						{
    return new Symbol(TokenConstants.DOT);
}
					case -11:
						break;
					case 11:
						{
    return new Symbol(TokenConstants.SEMI);
}
					case -12:
						break;
					case 12:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
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
    yybegin(LINECOMMENT);
}
					case -28:
						break;
					case 28:
						{
    yybegin(COMMENTOPEN);
}
					case -29:
						break;
					case 29:
						{
    return new Symbol(TokenConstants.ERROR, "Unmatched *)");
}
					case -30:
						break;
					case 30:
						{
    return new Symbol(TokenConstants.IN);
}
					case -31:
						break;
					case 31:
						{
    return new Symbol(TokenConstants.FI);
}
					case -32:
						break;
					case 32:
						{
    return new Symbol(TokenConstants.IF);
}
					case -33:
						break;
					case 33:
						{
    return new Symbol(TokenConstants.OF);
}
					case -34:
						break;
					case 34:
						{
    return new Symbol(TokenConstants.ASSIGN);
}
					case -35:
						break;
					case 35:
						{
    return new Symbol(TokenConstants.LE);
}
					case -36:
						break;
					case 36:
						{ /* Sample lexical rule for "=>" arrow.
                                     Further lexical rules should be defined
                                     here, after the last %% separator */
                                  return new Symbol(TokenConstants.DARROW); }
					case -37:
						break;
					case 37:
						{
    return new Symbol(TokenConstants.NOT);
}
					case -38:
						break;
					case 38:
						{
    return new Symbol(TokenConstants.NEW);
}
					case -39:
						break;
					case 39:
						{
    return new Symbol(TokenConstants.LET);
}
					case -40:
						break;
					case 40:
						{
     return new Symbol(TokenConstants.CASE);
}
					case -41:
						break;
					case 41:
						{
    return new Symbol(TokenConstants.LOOP);
}
					case -42:
						break;
					case 42:
						{
    return new Symbol(TokenConstants.ELSE);
}
					case -43:
						break;
					case 43:
						{
    return new Symbol(TokenConstants.ESAC);
}
					case -44:
						break;
					case 44:
						{
    return new Symbol(TokenConstants.THEN);
}
					case -45:
						break;
					case 45:
						{
    return new Symbol(TokenConstants.POOL);
}
					case -46:
						break;
					case 46:
						{
    return new Symbol(TokenConstants.BOOL_CONST, new Boolean(true));
}
					case -47:
						break;
					case 47:
						{
    return new Symbol(TokenConstants.CLASS);
}
					case -48:
						break;
					case 48:
						{
    return new Symbol(TokenConstants.WHILE);
}
					case -49:
						break;
					case 49:
						{
    return new Symbol(TokenConstants.BOOL_CONST, new Boolean(false));
}
					case -50:
						break;
					case 50:
						{
    return new Symbol(TokenConstants.ISVOID);
}
					case -51:
						break;
					case 51:
						{
    return new Symbol(TokenConstants.INHERITS);
}
					case -52:
						break;
					case 52:
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
					case -53:
						break;
					case 53:
						{
    curr_lineno++;
}
					case -54:
						break;
					case 54:
						{
    // comment we do nothing
}
					case -55:
						break;
					case 55:
						{
    yybegin(YYINITIAL);
}
					case -56:
						break;
					case 56:
						{
}
					case -57:
						break;
					case 57:
						{
    yybegin(YYINITIAL);
}
					case -58:
						break;
					case 59:
						{ /* This rule should be the very last
    in your lexical specification and
    will match match everything not
    matched by other lexical rules. */
    System.err.println("LEXER BUG - UNMATCHED: " + yytext()); 
}
					case -59:
						break;
					case 60:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -60:
						break;
					case 61:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -61:
						break;
					case 62:
						{
    return new Symbol(TokenConstants.IN);
}
					case -62:
						break;
					case 63:
						{
    return new Symbol(TokenConstants.FI);
}
					case -63:
						break;
					case 64:
						{
    return new Symbol(TokenConstants.IF);
}
					case -64:
						break;
					case 65:
						{
    return new Symbol(TokenConstants.OF);
}
					case -65:
						break;
					case 66:
						{
    return new Symbol(TokenConstants.NOT);
}
					case -66:
						break;
					case 67:
						{
    return new Symbol(TokenConstants.NEW);
}
					case -67:
						break;
					case 68:
						{
    return new Symbol(TokenConstants.LET);
}
					case -68:
						break;
					case 69:
						{
     return new Symbol(TokenConstants.CASE);
}
					case -69:
						break;
					case 70:
						{
    return new Symbol(TokenConstants.LOOP);
}
					case -70:
						break;
					case 71:
						{
    return new Symbol(TokenConstants.ELSE);
}
					case -71:
						break;
					case 72:
						{
    return new Symbol(TokenConstants.ESAC);
}
					case -72:
						break;
					case 73:
						{
    return new Symbol(TokenConstants.THEN);
}
					case -73:
						break;
					case 74:
						{
    return new Symbol(TokenConstants.POOL);
}
					case -74:
						break;
					case 75:
						{
    return new Symbol(TokenConstants.CLASS);
}
					case -75:
						break;
					case 76:
						{
    return new Symbol(TokenConstants.WHILE);
}
					case -76:
						break;
					case 77:
						{
    return new Symbol(TokenConstants.ISVOID);
}
					case -77:
						break;
					case 78:
						{
    return new Symbol(TokenConstants.INHERITS);
}
					case -78:
						break;
					case 79:
						{
    // comment we do nothing
}
					case -79:
						break;
					case 80:
						{
}
					case -80:
						break;
					case 82:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -81:
						break;
					case 83:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -82:
						break;
					case 84:
						{
    return new Symbol(TokenConstants.IN);
}
					case -83:
						break;
					case 85:
						{
    return new Symbol(TokenConstants.FI);
}
					case -84:
						break;
					case 86:
						{
    return new Symbol(TokenConstants.IF);
}
					case -85:
						break;
					case 87:
						{
    return new Symbol(TokenConstants.NOT);
}
					case -86:
						break;
					case 88:
						{
    return new Symbol(TokenConstants.NEW);
}
					case -87:
						break;
					case 89:
						{
     return new Symbol(TokenConstants.CASE);
}
					case -88:
						break;
					case 90:
						{
    return new Symbol(TokenConstants.LOOP);
}
					case -89:
						break;
					case 91:
						{
    return new Symbol(TokenConstants.ELSE);
}
					case -90:
						break;
					case 92:
						{
    return new Symbol(TokenConstants.CLASS);
}
					case -91:
						break;
					case 93:
						{
    return new Symbol(TokenConstants.WHILE);
}
					case -92:
						break;
					case 94:
						{
    return new Symbol(TokenConstants.ISVOID);
}
					case -93:
						break;
					case 95:
						{
    return new Symbol(TokenConstants.INHERITS);
}
					case -94:
						break;
					case 97:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -95:
						break;
					case 98:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -96:
						break;
					case 99:
						{
    return new Symbol(TokenConstants.IN);
}
					case -97:
						break;
					case 100:
						{
    return new Symbol(TokenConstants.NOT);
}
					case -98:
						break;
					case 101:
						{
     return new Symbol(TokenConstants.CASE);
}
					case -99:
						break;
					case 102:
						{
    return new Symbol(TokenConstants.ELSE);
}
					case -100:
						break;
					case 103:
						{
    return new Symbol(TokenConstants.CLASS);
}
					case -101:
						break;
					case 104:
						{
    return new Symbol(TokenConstants.ISVOID);
}
					case -102:
						break;
					case 106:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -103:
						break;
					case 107:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -104:
						break;
					case 108:
						{
    return new Symbol(TokenConstants.IN);
}
					case -105:
						break;
					case 109:
						{
    return new Symbol(TokenConstants.NOT);
}
					case -106:
						break;
					case 110:
						{
     return new Symbol(TokenConstants.CASE);
}
					case -107:
						break;
					case 112:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -108:
						break;
					case 113:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -109:
						break;
					case 114:
						{
     return new Symbol(TokenConstants.CASE);
}
					case -110:
						break;
					case 116:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -111:
						break;
					case 117:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -112:
						break;
					case 119:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -113:
						break;
					case 120:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -114:
						break;
					case 122:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -115:
						break;
					case 123:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -116:
						break;
					case 125:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -117:
						break;
					case 126:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -118:
						break;
					case 128:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -119:
						break;
					case 129:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -120:
						break;
					case 131:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -121:
						break;
					case 132:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -122:
						break;
					case 134:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -123:
						break;
					case 135:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -124:
						break;
					case 137:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -125:
						break;
					case 138:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -126:
						break;
					case 140:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -127:
						break;
					case 141:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -128:
						break;
					case 143:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -129:
						break;
					case 144:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -130:
						break;
					case 146:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -131:
						break;
					case 147:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -132:
						break;
					case 149:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -133:
						break;
					case 150:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -134:
						break;
					case 152:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -135:
						break;
					case 153:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -136:
						break;
					case 155:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -137:
						break;
					case 156:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -138:
						break;
					case 158:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -139:
						break;
					case 159:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -140:
						break;
					case 161:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -141:
						break;
					case 162:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -142:
						break;
					case 164:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -143:
						break;
					case 165:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -144:
						break;
					case 167:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -145:
						break;
					case 168:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -146:
						break;
					case 170:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -147:
						break;
					case 171:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -148:
						break;
					case 173:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -149:
						break;
					case 174:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -150:
						break;
					case 176:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -151:
						break;
					case 177:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -152:
						break;
					case 179:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -153:
						break;
					case 180:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -154:
						break;
					case 182:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -155:
						break;
					case 183:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -156:
						break;
					case 185:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -157:
						break;
					case 186:
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
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
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
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -163:
						break;
					case 193:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -164:
						break;
					case 194:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -165:
						break;
					case 195:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -166:
						break;
					case 196:
						{
    return new Symbol(TokenConstants.BOOL_CONST, new Boolean(true));
}
					case -167:
						break;
					case 197:
						{
    return new Symbol(TokenConstants.BOOL_CONST, new Boolean(false));
}
					case -168:
						break;
					case 199:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -169:
						break;
					case 200:
						{
    return new Symbol(TokenConstants.OF);
}
					case -170:
						break;
					case 201:
						{
    return new Symbol(TokenConstants.LET);
}
					case -171:
						break;
					case 202:
						{
    return new Symbol(TokenConstants.ESAC);
}
					case -172:
						break;
					case 203:
						{
    return new Symbol(TokenConstants.THEN);
}
					case -173:
						break;
					case 204:
						{
    return new Symbol(TokenConstants.POOL);
}
					case -174:
						break;
					case 206:
						{
    return new Symbol(TokenConstants.FI);
}
					case -175:
						break;
					case 207:
						{
    return new Symbol(TokenConstants.IF);
}
					case -176:
						break;
					case 208:
						{
    return new Symbol(TokenConstants.NEW);
}
					case -177:
						break;
					case 209:
						{
    return new Symbol(TokenConstants.LOOP);
}
					case -178:
						break;
					case 210:
						{
    return new Symbol(TokenConstants.WHILE);
}
					case -179:
						break;
					case 211:
						{
    return new Symbol(TokenConstants.INHERITS);
}
					case -180:
						break;
					case 212:
						{
    return new Symbol(TokenConstants.ELSE);
}
					case -181:
						break;
					case 213:
						{
    return new Symbol(TokenConstants.CLASS);
}
					case -182:
						break;
					case 214:
						{
    return new Symbol(TokenConstants.ISVOID);
}
					case -183:
						break;
					case 215:
						{
    return new Symbol(TokenConstants.IN);
}
					case -184:
						break;
					case 216:
						{
    return new Symbol(TokenConstants.NOT);
}
					case -185:
						break;
					case 217:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
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
     return new Symbol(TokenConstants.CASE);
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
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
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
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
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
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
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
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
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
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
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
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
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
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
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
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -239:
						break;
					case 271:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -240:
						break;
					case 272:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -241:
						break;
					case 273:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -242:
						break;
					case 274:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -243:
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

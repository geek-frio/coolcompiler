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
		186,
		189,
		59
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
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NO_ANCHOR,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NOT_ACCEPT,
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
		/* 81 */ YY_NO_ANCHOR,
		/* 82 */ YY_NO_ANCHOR,
		/* 83 */ YY_NO_ANCHOR,
		/* 84 */ YY_NOT_ACCEPT,
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
		/* 96 */ YY_NO_ANCHOR,
		/* 97 */ YY_NO_ANCHOR,
		/* 98 */ YY_NO_ANCHOR,
		/* 99 */ YY_NO_ANCHOR,
		/* 100 */ YY_NOT_ACCEPT,
		/* 101 */ YY_NO_ANCHOR,
		/* 102 */ YY_NO_ANCHOR,
		/* 103 */ YY_NO_ANCHOR,
		/* 104 */ YY_NO_ANCHOR,
		/* 105 */ YY_NO_ANCHOR,
		/* 106 */ YY_NO_ANCHOR,
		/* 107 */ YY_NO_ANCHOR,
		/* 108 */ YY_NO_ANCHOR,
		/* 109 */ YY_NO_ANCHOR,
		/* 110 */ YY_NOT_ACCEPT,
		/* 111 */ YY_NO_ANCHOR,
		/* 112 */ YY_NO_ANCHOR,
		/* 113 */ YY_NO_ANCHOR,
		/* 114 */ YY_NO_ANCHOR,
		/* 115 */ YY_NO_ANCHOR,
		/* 116 */ YY_NOT_ACCEPT,
		/* 117 */ YY_NO_ANCHOR,
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
		/* 186 */ YY_NOT_ACCEPT,
		/* 187 */ YY_NO_ANCHOR,
		/* 188 */ YY_NO_ANCHOR,
		/* 189 */ YY_NOT_ACCEPT,
		/* 190 */ YY_NO_ANCHOR,
		/* 191 */ YY_NO_ANCHOR,
		/* 192 */ YY_NOT_ACCEPT,
		/* 193 */ YY_NO_ANCHOR,
		/* 194 */ YY_NO_ANCHOR,
		/* 195 */ YY_NOT_ACCEPT,
		/* 196 */ YY_NO_ANCHOR,
		/* 197 */ YY_NO_ANCHOR,
		/* 198 */ YY_NO_ANCHOR,
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
		/* 226 */ YY_NOT_ACCEPT,
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
		/* 257 */ YY_NOT_ACCEPT,
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
		/* 274 */ YY_NO_ANCHOR,
		/* 275 */ YY_NO_ANCHOR,
		/* 276 */ YY_NO_ANCHOR,
		/* 277 */ YY_NO_ANCHOR,
		/* 278 */ YY_NO_ANCHOR,
		/* 279 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"5,8:4,5:3,6:2,1,6:2,2,5:18,6,8,3,8:4,65,13,15,14,33,16,4,17,43,63:10,38,18," +
"26,34,10,8,42,20,64,19,37,22,31,64,28,27,64:2,30,64,23,24,32,64,29,21,25,45" +
",36,35,64:3,65,7,65,12,9,8,47,48,49,50,51,46,48,52,53,48:2,54,48,55,56,57,4" +
"8,58,59,44,60,61,62,48:3,40,11,41,39,5,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,280,
"0,1:2,2,1,3,1:3,4,5,1:4,6,7,1,8,1:6,9,10,1:2,11,1:4,12,1:3,13,1,12,14,1,15," +
"12:3,16,17,1,16,18,1:7,19,1,20,21,12,22,23,12:2,16,1,12,16,24,12,1,16:3,1,1" +
"2,1,12,25,26,27,28,29,30,16:2,31,16,1,16,12:2,16,12,16,32,33,34,35,36,12,15" +
",16:3,37,38,39,40,41,16,12,42,43,44,16,45,46,47,48,49,50,51,52,53,54,55,56," +
"31,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,15," +
"80,81,82,83,84,85,86,87,24,88,89,90,91,92,93,94,95,96,97,98,17,99,100,18,10" +
"1,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,1" +
"20,121,122,123:2,124,125,123:14,126,123,127,128,123,129,130,131,132,133,134" +
",135,136,137,138,139,140,141,142,143,144,145,146,147,148,149,150,151,152,15" +
"3,154,155,156,157,158,159,160,161,162,163,123,164,165,166,167,168,169,170,1" +
"71,172,173,174,175,176,123,177,178,179,16")[0];

	private int yy_nxt[][] = unpackFromString(180,66,
"1,2,3,4,5,6,3,7:4,62,8,9,10,11,12,13,14,15,63:2,201,255,85,276,16,101,63:2," +
"260,111,278,17,18,264,63:2,19,20,21,22,23,24,25,63,64,275:2,86,275,102,275," +
"112,118,224,122,277,275:4,125,26,63,27,-1:68,3,-1:3,3,-1:63,28,-1:75,32,-1:" +
"66,33,-1:59,63,-1,226,-1:7,63,117,63:5,-1,63:3,121,63:2,-1:2,63:3,-1:6,63:3" +
",117,63:6,121,63:10,-1:5,35,-1:29,36,-1:41,37,-1:64,279,-1:9,279:7,-1,279,2" +
"02,256,279:3,-1:2,279:3,-1:6,261,279,261:6,265,261:5,266,261:4,279:2,-1:64," +
"26,-1:13,38,-1:8,257,129,-1:2,132,69,-1,135,138,-1:6,39,141,-1:7,69,-1:2,25" +
"7,-1:4,138,135,-1:2,132,-1:2,129,-1,141,39,-1:12,63,-1:9,63:7,-1,63:6,-1:2," +
"63:3,-1:6,63:21,-1:12,41,-1:9,156,72,-1,159,-1:5,162,-1,42,-1:18,72,-1:2,16" +
"2,-1,159,42,-1,156,-1:17,48,-1:9,78,49,-1:4,171,-1,174,-1:21,49,-1,171,-1:4" +
",174,78,-1:17,78,-1:9,78,-1:37,78,-1:15,279,-1:9,279:7,-1,279:6,-1:2,279:3," +
"-1:6,279:21,-1:12,51,-1:15,180,-1:9,80,-1:12,80,-1:2,180,-1:23,183,-1:13,18" +
"3,-1:18,183,-1:21,1,60,-1,83:63,-1:11,129,-1:9,129,-1:37,129,-1:17,29,-1:8," +
"61,84,100,65,110,-1:2,30,116,-1,120,31,-1:14,31,61,-1:3,100,116,30,120,65,1" +
"10,-1:2,84,-1:15,279,-1,30,-1:7,279,267,279:5,-1,88,279:5,-1:2,279:3,-1:6,2" +
"61,279,261,268,261:5,212,261:9,279:2,-1:12,138,-1:16,138,-1:23,138,-1:24,17" +
"4,-1:17,174,-1:28,174,-1:20,192,195,-1:54,83:63,-1:11,141,-1:24,141,-1:24,1" +
"41,-1:13,63,-1:9,63:7,-1,63:4,34,63,-1:2,63:3,-1:6,63:2,34,63:18,-1:10,279," +
"-1,226,-1:7,279,128,279:5,-1,279:3,131,279:2,-1:2,279:3,-1:6,261,279,261,22" +
"7,261:6,228,261:8,279:2,-1:12,153,-1:16,138,-1:7,141,-1:15,138,-1:8,141,-1:" +
"15,42,-1:20,42,-1:24,42,-1:22,56,-1:62,39,-1:23,39,-1:26,39,-1:12,63,-1,87," +
"-1:7,63:2,130,63,103,63:2,-1,63:4,67,63,-1:2,63:3,-1:6,63:2,67,63:8,103,63:" +
"3,130,63:5,-1:10,279,-1,205,-1:7,279:2,269,279:4,-1,279:3,259,279:2,-1:2,27" +
"9:3,-1:6,261,279,261:8,263,261:4,270,261:3,279:2,-1:10,63,-1,138,-1:7,63:7," +
"-1,63,160,63:4,-1:2,63:3,-1:6,63:8,160,63:12,-1:16,57,-1:61,90,-1:12,132,69" +
",-1:18,69,-1:11,132,-1:18,63,-1,30,-1:7,63:7,-1,66,63:5,-1:2,63:3,-1:6,63:9" +
",66,63:11,-1:10,279,-1,87,-1:7,279:2,134,279,113,279:2,-1,279:4,89,279,-1:2" +
",279:3,-1:6,261,279,213,261:8,221,261:3,229,261:3,279:2,-1:10,279,-1,138,-1" +
":7,279:7,-1,279,173,279:4,-1:2,279:3,-1:6,279:8,173,279:12,-1:12,135,-1:15," +
"135,-1:25,135,-1:21,63,-1,129,-1:7,63:2,142,63:4,-1,63:6,-1:2,63:3,-1:6,63:" +
"15,142,63:5,-1:10,279,-1,126,-1:7,279:3,137,279,140,279,-1,279:6,-1:2,279:3" +
",-1:6,261,279,261:5,230,261:4,231,261:6,279:2,-1:12,144,-1:8,257,147,-1:25," +
"257,-1:11,147,-1:15,63,-1,257,-1:7,63,145,63:5,-1,63:6,-1:2,63:3,-1:6,63:3," +
"145,63:17,-1:10,279,-1:9,279:7,-1,279:4,68,279,-1:2,279:3,-1:6,261,279,207," +
"261:16,279:2,-1:12,69,-1:13,69,-1:9,39,-1:8,69,-1:17,39,-1:12,63,-1,39,-1:7" +
",63:7,-1,63:6,-1:2,70,63:2,-1:6,63:18,70,63:2,-1:10,279,-1,116,-1:7,279:7,-" +
"1,279,149,279:4,-1:2,279:3,-1:6,261,279,261:6,234,261:10,279:2,-1:12,132,-1" +
":12,132,-1:31,132,-1:18,63,-1,69,-1:7,63:6,104,-1,63:6,-1:2,63:3,-1:6,104,6" +
"3:20,-1:10,279,-1,129,-1:7,279:2,158,279:4,-1,279:6,-1:2,279:3,-1:6,279:15," +
"158,279:5,-1:12,92,-1:10,92,-1:28,92,-1:23,63,-1,141,-1:7,63:7,-1,63:6,-1:2" +
",63,157,63,-1:6,63:17,157,63:3,-1:10,279,-1,257,-1:7,279,161,279:5,-1,279:6" +
",-1:2,279:3,-1:6,279:3,161,279:17,-1:10,63,-1:9,63:6,40,-1,63:6,-1:2,63:3,-" +
"1:6,40,63:20,-1:10,279,-1,141,-1:7,279:7,-1,279:6,-1:2,279,170,279,-1:6,279" +
":17,170,279:3,-1:12,162,-1:18,162,-1:23,162,-1:20,63,-1,132,-1:7,63:5,163,6" +
"3,-1,63:6,-1:2,63:3,-1:6,63:12,163,63:8,-1:10,279,-1:9,279:6,71,-1,279:6,-1" +
":2,279:3,-1:6,71,279:20,-1:12,165,-1:10,165,-1:28,165,-1:23,63,-1,135,-1:7," +
"63:7,-1,169,63:5,-1:2,63:3,-1:6,63:9,169,63:11,-1:10,279,-1,132,-1:7,279:5," +
"176,279,-1,279:6,-1:2,279:3,-1:6,279:12,176,279:8,-1:12,159,-1:12,159,-1:31" +
",159,-1:18,63,-1,92,-1:7,63:3,115,63:3,-1,63:6,-1:2,63:3,-1:6,63:7,115,63:1" +
"3,-1:10,279,-1,39,-1:7,279:7,-1,279:6,-1:2,91,279:2,-1:6,279:18,91,279:2,-1" +
":12,43,-1:9,156,74,-1:28,74,-1:7,156,-1:15,63,-1,156,-1:7,63:2,172,63:4,-1," +
"63:6,-1:2,63:3,-1:6,63:15,172,63:5,-1:10,279,-1,69,-1:7,279:6,114,-1,279:6," +
"-1:2,279:3,-1:6,114,279:20,-1:12,74,-1:10,74,-1:28,74,-1:23,63,-1:9,44,63:6" +
",-1,63:6,-1:2,63:3,-1:6,63:5,44,63:15,-1:10,279,-1,135,-1:7,279:7,-1,182,27" +
"9:5,-1:2,279:3,-1:6,279:9,182,279:11,-1:12,105,-1:9,156,92,-1:28,92,-1:7,15" +
"6,-1:15,63,-1,74,-1:7,63:3,94,63:3,-1,63:6,-1:2,63:3,-1:6,63:7,94,63:13,-1:" +
"10,279,-1:9,279:4,76,279:2,-1,279:6,-1:2,279:3,-1:6,279:11,76,279:9,-1:12,1" +
"68,-1:10,165,-1,159,-1:26,165,-1:4,159,-1:18,63,-1:9,63:4,45,63:2,-1,63:6,-" +
"1:2,63:3,-1:6,63:11,45,63:9,-1:10,279,-1:9,279:3,47,279:3,-1,279:6,-1:2,279" +
":3,-1:6,279:7,47,279:13,-1:10,63,-1,159,-1:7,63:5,175,63,-1,63:6,-1:2,63:3," +
"-1:6,63:12,175,63:8,-1:10,279,-1,92,-1:7,279:3,119,279:3,-1,279:6,-1:2,279:" +
"3,-1:6,279:7,119,279:13,-1:12,171,-1:15,171,-1:25,171,-1:21,63,-1,165,-1:7," +
"63:3,178,63:3,-1,63:6,-1:2,63:3,-1:6,63:7,178,63:13,-1:10,279,-1,156,-1:7,2" +
"79:2,188,279:4,-1,279:6,-1:2,279:3,-1:6,279:15,188,279:5,-1:12,49,-1:10,49," +
"-1:28,49,-1:23,63,-1,42,-1:7,63:7,-1,63:5,73,-1:2,63:3,-1:6,63:13,73,63:7,-" +
"1:10,279,-1:9,75,279:6,-1,279:6,-1:2,279:3,-1:6,279:5,75,279:15,-1:10,63,-1" +
":9,63:7,-1,63:3,46,63:2,-1:2,63:3,-1:6,63:10,46,63:10,-1:10,279,-1,74,-1:7," +
"279:3,106,279:3,-1,279:6,-1:2,279:3,-1:6,279:7,106,279:13,-1:12,177,-1:15,1" +
"71,-1,174,-1:23,171,-1:4,174,-1:16,63,-1,162,-1:7,63:7,-1,63:3,181,63:2,-1:" +
"2,63:3,-1:6,63:10,181,63:10,-1:10,279,-1,159,-1:7,279:5,191,279,-1,279:6,-1" +
":2,279:3,-1:6,279:12,191,279:8,-1:12,80,-1:25,80,-1:12,80,-1:24,63,-1,78,-1" +
":7,63:2,95,63:4,-1,63:6,-1:2,63:3,-1:6,63:15,95,63:5,-1:10,279,-1,165,-1:7," +
"279:3,194,279:3,-1,279:6,-1:2,279:3,-1:6,279:7,194,279:13,-1:12,180,-1:15,1" +
"80,-1:25,180,-1:21,63,-1,171,-1:7,63:7,-1,184,63:5,-1:2,63:3,-1:6,63:9,184," +
"63:11,-1:10,279,-1,42,-1:7,279:7,-1,279:5,93,-1:2,279:3,-1:6,279:13,93,279:" +
"7,-1:10,63,-1,174,-1:7,63:7,-1,63:2,187,63:3,-1:2,63:3,-1:6,63:14,187,63:6," +
"-1:10,279,-1:9,279:7,-1,279:3,77,279:2,-1:2,279:3,-1:6,279:10,77,279:10,-1:" +
"10,63,-1,49,-1:7,63:3,79,63:3,-1,63:6,-1:2,63:3,-1:6,63:7,79,63:13,-1:10,27" +
"9,-1,162,-1:7,279:7,-1,279:3,196,279:2,-1:2,279:3,-1:6,279:10,196,279:10,-1" +
":12,52,-1:9,52,-1:37,52,-1:15,63,-1,80,-1:7,63:7,-1,63:6,-1:2,63:2,97,-1:6," +
"63:6,97,63:14,-1:10,279,-1:9,279:3,50,279:3,-1,279:6,-1:2,279:3,-1:6,279:7," +
"50,279:13,-1,1,53:65,-1:9,63,-1,180,-1:7,63:7,-1,190,63:5,-1:2,63:3,-1:6,63" +
":9,190,63:11,-1:10,279,-1,78,-1:7,279:2,107,279:4,-1,279:6,-1:2,279:3,-1:6," +
"279:15,107,279:5,-1,1,54,55:5,82,55:5,99,109,55:51,-1:9,63,-1,183,-1:7,63:6" +
",193,-1,63:6,-1:2,63:3,-1:6,193,63:20,-1:10,279,-1,171,-1:7,279:7,-1,197,27" +
"9:5,-1:2,279:3,-1:6,279:9,197,279:11,-1:15,58,-1:60,63,-1,52,-1:7,63:2,81,6" +
"3:4,-1,63:6,-1:2,63:3,-1:6,63:15,81,63:5,-1:10,279,-1,174,-1:7,279:7,-1,279" +
":2,198,279:3,-1:2,279:3,-1:6,279:14,198,279:6,-1:16,55,-1:59,279,-1,49,-1:7" +
",279:3,96,279:3,-1,279:6,-1:2,279:3,-1:6,279:7,96,279:13,-1:10,279,-1,80,-1" +
":7,279:7,-1,279:6,-1:2,279:2,108,-1:6,279:6,108,279:14,-1:10,279,-1,180,-1:" +
"7,279:7,-1,199,279:5,-1:2,279:3,-1:6,279:9,199,279:11,-1:10,279,-1,183,-1:7" +
",279:6,200,-1,279:6,-1:2,279:3,-1:6,200,279:20,-1:10,279,-1,52,-1:7,279:2,9" +
"8,279:4,-1,279:6,-1:2,279:3,-1:6,279:15,98,279:5,-1:10,63,-1,205,-1:7,63:2," +
"206,63:4,-1,63:3,223,63:2,-1:2,63:3,-1:6,63:10,223,63:4,206,63:5,-1:10,279," +
"-1:9,279:3,152,279:3,-1,279:6,-1:2,279:3,-1:6,279:7,152,279:13,-1:10,279,-1" +
":9,279:7,-1,279:6,-1:2,279:3,-1:6,261,279,261:17,279:2,-1:12,147,-1:9,147,-" +
"1:37,147,-1:15,63,-1:9,63,148,63:5,-1,63:6,-1:2,63:3,-1:6,63:3,148,63:17,-1" +
":10,279,-1,138,-1:7,279:7,-1,279,173,279:4,-1:2,279:3,-1:6,261,279,261:6,24" +
"2,261:10,279:2,-1:10,63,-1,147,-1:7,63:2,151,63:4,-1,63:6,-1:2,63:3,-1:6,63" +
":15,151,63:5,-1:10,279,-1,123,-1:7,279:3,143,279,146,279,-1,279:6,-1:2,279:" +
"3,-1:6,261,279,261:5,232,261:4,233,261:6,279:2,-1:12,150,-1:8,257,129,-1:25" +
",257,-1:11,129,-1:15,279,-1,129,-1:7,279:2,158,279:4,-1,279:6,-1:2,279:3,-1" +
":6,261,279,261:13,237,261:3,279:2,-1:10,279,-1,257,-1:7,279,161,279:5,-1,27" +
"9:6,-1:2,279:3,-1:6,261,279,261,238,261:15,279:2,-1:10,279,-1,141,-1:7,279:" +
"7,-1,279:6,-1:2,279,170,279,-1:6,261,279,261:15,241,261,279:2,-1:10,279,-1:" +
"9,279:6,71,-1,279:6,-1:2,279:3,-1:6,208,279,261:17,279:2,-1:10,279,-1,132,-" +
"1:7,279:5,176,279,-1,279:6,-1:2,279:3,-1:6,261,279,261:10,243,261:6,279:2,-" +
"1:10,279,-1,39,-1:7,279:7,-1,279:6,-1:2,91,279:2,-1:6,261,279,261:16,214,27" +
"9:2,-1:10,279,-1,69,-1:7,279:6,114,-1,279:6,-1:2,279:3,-1:6,222,279,261:17," +
"279:2,-1:10,279,-1,135,-1:7,279:7,-1,182,279:5,-1:2,279:3,-1:6,261,279,261:" +
"7,245,261:9,279:2,-1:10,279,-1:9,279:4,76,279:2,-1,279:6,-1:2,279:3,-1:6,26" +
"1,279,261:9,210,261:7,279:2,-1:10,279,-1:9,279:3,47,279:3,-1,279:6,-1:2,279" +
":3,-1:6,261,279,261:5,203,261:11,279:2,-1:10,279,-1,92,-1:7,279:3,119,279:3" +
",-1,279:6,-1:2,279:3,-1:6,261,279,261:5,225,261:11,279:2,-1:10,279,-1,156,-" +
"1:7,279:2,188,279:4,-1,279:6,-1:2,279:3,-1:6,261,279,261:13,247,261:3,279:2" +
",-1:10,279,-1:9,75,279:6,-1,279:6,-1:2,279:3,-1:6,261,279,261:3,209,261:13," +
"279:2,-1:10,279,-1,74,-1:7,279:3,106,279:3,-1,279:6,-1:2,279:3,-1:6,261,279" +
",261:5,218,261:11,279:2,-1:10,279,-1,159,-1:7,279:5,191,279,-1,279:6,-1:2,2" +
"79:3,-1:6,261,279,261:10,248,261:6,279:2,-1:10,279,-1,165,-1:7,279:3,194,27" +
"9:3,-1,279:6,-1:2,279:3,-1:6,261,279,261:5,249,261:11,279:2,-1:10,279,-1,42" +
",-1:7,279:7,-1,279:5,93,-1:2,279:3,-1:6,261,279,261:11,215,261:5,279:2,-1:1" +
"0,279,-1:9,279:7,-1,279:3,77,279:2,-1:2,279:3,-1:6,261,279,261:8,211,261:8," +
"279:2,-1:10,279,-1,162,-1:7,279:7,-1,279:3,196,279:2,-1:2,279:3,-1:6,261,27" +
"9,261:8,250,261:8,279:2,-1:10,279,-1:9,279:3,50,279:3,-1,279:6,-1:2,279:3,-" +
"1:6,261,279,261:5,204,261:11,279:2,-1:10,279,-1,78,-1:7,279:2,107,279:4,-1," +
"279:6,-1:2,279:3,-1:6,261,279,261:13,219,261:3,279:2,-1:10,279,-1,171,-1:7," +
"279:7,-1,197,279:5,-1:2,279:3,-1:6,261,279,261:7,251,261:9,279:2,-1:10,279," +
"-1,174,-1:7,279:7,-1,279:2,198,279:3,-1:2,279:3,-1:6,261,279,261:12,252,261" +
":4,279:2,-1:10,279,-1,49,-1:7,279:3,96,279:3,-1,279:6,-1:2,279:3,-1:6,261,2" +
"79,261:5,216,261:11,279:2,-1:10,279,-1,80,-1:7,279:7,-1,279:6,-1:2,279:2,10" +
"8,-1:6,261,279,261:4,220,261:12,279:2,-1:10,279,-1,180,-1:7,279:7,-1,199,27" +
"9:5,-1:2,279:3,-1:6,261,279,261:7,253,261:9,279:2,-1:10,279,-1,183,-1:7,279" +
":6,200,-1,279:6,-1:2,279:3,-1:6,254,279,261:17,279:2,-1:10,279,-1,52,-1:7,2" +
"79:2,98,279:4,-1,279:6,-1:2,279:3,-1:6,261,279,261:13,217,261:3,279:2,-1:10" +
",63,-1,123,-1:7,63:3,124,63,127,63,-1,63:6,-1:2,63:3,-1:6,63:7,124,63:4,127" +
",63:8,-1:10,279,-1:9,279:7,-1,279:6,-1:2,279:3,-1:6,279,155,279:14,155,279:" +
"4,-1:12,156,-1:9,156,-1:37,156,-1:15,63,-1:9,63:3,154,63:3,-1,63:6,-1:2,63:" +
"3,-1:6,63:7,154,63:13,-1:10,279,-1,147,-1:7,279:2,167,279:4,-1,279:6,-1:2,2" +
"79:3,-1:6,279:15,167,279:5,-1:10,63,-1,126,-1:7,63:3,133,63,136,63,-1,63:6," +
"-1:2,63:3,-1:6,63:7,133,63:4,136,63:8,-1:10,63,-1:9,63:5,166,63,-1,63:6,-1:" +
"2,63:3,-1:6,63:12,166,63:8,-1:10,279,-1,147,-1:7,279:2,167,279:4,-1,279:6,-" +
"1:2,279:3,-1:6,261,279,261:13,240,261:3,279:2,-1:10,63,-1,116,-1:7,63:7,-1," +
"63,139,63:4,-1:2,63:3,-1:6,63:8,139,63:12,-1:10,279,-1:9,279:3,152,279:3,-1" +
",279:6,-1:2,279:3,-1:6,261,279,261:5,235,261:11,279:2,-1:10,279,-1:9,279:7," +
"-1,279:6,-1:2,279:3,-1:6,261,155,261:14,236,261:2,279:2,-1:10,279,-1:9,279:" +
"7,-1,279:3,273,279:2,-1:2,279:3,-1:6,279:10,273,279:10,-1:10,279,-1:9,279:7" +
",-1,279:3,273,279:2,-1:2,279:3,-1:6,261,279,261:8,274,261:8,279:2,-1:10,279" +
",-1:9,279,164,279:5,-1,279:6,-1:2,279:3,-1:6,279:3,164,279:17,-1:10,279,-1:" +
"9,279,164,279:5,-1,279:6,-1:2,279:3,-1:6,261,279,261,239,261:15,279:2,-1:10" +
",279,-1:9,279:5,179,279,-1,279:6,-1:2,279:3,-1:6,279:12,179,279:8,-1:10,279" +
",-1:9,279:5,179,279,-1,279:6,-1:2,279:3,-1:6,261,279,261:10,244,261:6,279:2" +
",-1:10,279,-1:9,279:2,185,279:4,-1,279:6,-1:2,279:3,-1:6,279:15,185,279:5,-" +
"1:10,279,-1:9,279:2,185,279:4,-1,279:6,-1:2,279:3,-1:6,261,279,261:13,246,2" +
"61:3,279:2,-1:10,63,-1:9,63:7,-1,63,258,63:4,-1:2,63:3,-1:6,63:8,258,63:12," +
"-1:10,279,-1:9,279:5,271,279,-1,279:6,-1:2,279:3,-1:6,261,279,261:10,272,26" +
"1:6,279:2,-1:10,63,-1:9,63:5,262,63,-1,63:6,-1:2,63:3,-1:6,63:12,262,63:8,-" +
"1");

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
						
					case -2:
						break;
					case 2:
						{
    curr_lineno++;
}
					case -3:
						break;
					case 3:
						{ 
}
					case -4:
						break;
					case 4:
						{
    yybegin(STROPEN);
}
					case -5:
						break;
					case 5:
						{
    return new Symbol(TokenConstants.MINUS);
}
					case -6:
						break;
					case 6:
						{ /* This rule should be the very last
    in your lexical specification and
    will match match everything not
    matched by other lexical rules. */
    System.err.println("LEXER BUG - UNMATCHED: " + yytext()); 
}
					case -7:
						break;
					case 7:
						{
    return new Symbol(TokenConstants.ERROR, yytext());
}
					case -8:
						break;
					case 8:
						{
    return new Symbol(TokenConstants.ERROR, yytext());
}
					case -9:
						break;
					case 9:
						{
    return new Symbol(TokenConstants.LPAREN);
}
					case -10:
						break;
					case 10:
						{
    return new Symbol(TokenConstants.MULT);
}
					case -11:
						break;
					case 11:
						{
    return new Symbol(TokenConstants.RPAREN);
}
					case -12:
						break;
					case 12:
						{
    return new Symbol(TokenConstants.COMMA);
}
					case -13:
						break;
					case 13:
						{
    return new Symbol(TokenConstants.DOT);
}
					case -14:
						break;
					case 14:
						{
    return new Symbol(TokenConstants.SEMI);
}
					case -15:
						break;
					case 15:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -16:
						break;
					case 16:
						{
    return new Symbol(TokenConstants.LT);
}
					case -17:
						break;
					case 17:
						{
    return new Symbol(TokenConstants.PLUS);
}
					case -18:
						break;
					case 18:
						{
    return new Symbol(TokenConstants.EQ);
}
					case -19:
						break;
					case 19:
						{
    return new Symbol(TokenConstants.COLON);
}
					case -20:
						break;
					case 20:
						{
    return new Symbol(TokenConstants.NEG);
}
					case -21:
						break;
					case 21:
						{
    return new Symbol(TokenConstants.LBRACE);
}
					case -22:
						break;
					case 22:
						{
    return new Symbol(TokenConstants.RBRACE);
}
					case -23:
						break;
					case 23:
						{
    return new Symbol(TokenConstants.AT);
}
					case -24:
						break;
					case 24:
						{
    return new Symbol(TokenConstants.DIV);
}
					case -25:
						break;
					case 25:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -26:
						break;
					case 26:
						{
    return new Symbol(TokenConstants.INT_CONST, AbstractTable.inttable.addString(yytext()));
}
					case -27:
						break;
					case 27:
						{
    return new Symbol(TokenConstants.ERROR, yytext());
}
					case -28:
						break;
					case 28:
						{
    yybegin(LINECOMMENT);
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
    comment_open_num++;
    yybegin(COMMENTOPEN);
}
					case -33:
						break;
					case 33:
						{
    return new Symbol(TokenConstants.ERROR, "Unmatched *)");
}
					case -34:
						break;
					case 34:
						{
    return new Symbol(TokenConstants.OF);
}
					case -35:
						break;
					case 35:
						{
    return new Symbol(TokenConstants.ASSIGN);
}
					case -36:
						break;
					case 36:
						{
    return new Symbol(TokenConstants.LE);
}
					case -37:
						break;
					case 37:
						{ /* Sample lexical rule for "=>" arrow.
                                     Further lexical rules should be defined
                                     here, after the last %% separator */
                                  return new Symbol(TokenConstants.DARROW); }
					case -38:
						break;
					case 38:
						{
    return new Symbol(TokenConstants.NOT);
}
					case -39:
						break;
					case 39:
						{
    return new Symbol(TokenConstants.NEW);
}
					case -40:
						break;
					case 40:
						{
    return new Symbol(TokenConstants.LET);
}
					case -41:
						break;
					case 41:
						{
     return new Symbol(TokenConstants.CASE);
}
					case -42:
						break;
					case 42:
						{
    return new Symbol(TokenConstants.LOOP);
}
					case -43:
						break;
					case 43:
						{
    return new Symbol(TokenConstants.ELSE);
}
					case -44:
						break;
					case 44:
						{
    return new Symbol(TokenConstants.ESAC);
}
					case -45:
						break;
					case 45:
						{
    return new Symbol(TokenConstants.THEN);
}
					case -46:
						break;
					case 46:
						{
    return new Symbol(TokenConstants.POOL);
}
					case -47:
						break;
					case 47:
						{
    return new Symbol(TokenConstants.BOOL_CONST, new Boolean(true));
}
					case -48:
						break;
					case 48:
						{
    return new Symbol(TokenConstants.CLASS);
}
					case -49:
						break;
					case 49:
						{
    return new Symbol(TokenConstants.WHILE);
}
					case -50:
						break;
					case 50:
						{
    return new Symbol(TokenConstants.BOOL_CONST, new Boolean(false));
}
					case -51:
						break;
					case 51:
						{
    return new Symbol(TokenConstants.ISVOID);
}
					case -52:
						break;
					case 52:
						{
    return new Symbol(TokenConstants.INHERITS);
}
					case -53:
						break;
					case 53:
						{
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
					case -54:
						break;
					case 54:
						{
    curr_lineno++;
}
					case -55:
						break;
					case 55:
						{
    // comment we do nothing
}
					case -56:
						break;
					case 56:
						{
    comment_open_num ++;
}
					case -57:
						break;
					case 57:
						{
    comment_open_num--;
    if(comment_open_num == 0){
        yybegin(YYINITIAL);
    }
}
					case -58:
						break;
					case 58:
						{
    // do nothing
}
					case -59:
						break;
					case 59:
						{
}
					case -60:
						break;
					case 60:
						{
    yybegin(YYINITIAL);
    curr_lineno++;
}
					case -61:
						break;
					case 62:
						{
    return new Symbol(TokenConstants.ERROR, yytext());
}
					case -62:
						break;
					case 63:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -63:
						break;
					case 64:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -64:
						break;
					case 65:
						{
    return new Symbol(TokenConstants.IN);
}
					case -65:
						break;
					case 66:
						{
    return new Symbol(TokenConstants.FI);
}
					case -66:
						break;
					case 67:
						{
    return new Symbol(TokenConstants.IF);
}
					case -67:
						break;
					case 68:
						{
    return new Symbol(TokenConstants.OF);
}
					case -68:
						break;
					case 69:
						{
    return new Symbol(TokenConstants.NOT);
}
					case -69:
						break;
					case 70:
						{
    return new Symbol(TokenConstants.NEW);
}
					case -70:
						break;
					case 71:
						{
    return new Symbol(TokenConstants.LET);
}
					case -71:
						break;
					case 72:
						{
     return new Symbol(TokenConstants.CASE);
}
					case -72:
						break;
					case 73:
						{
    return new Symbol(TokenConstants.LOOP);
}
					case -73:
						break;
					case 74:
						{
    return new Symbol(TokenConstants.ELSE);
}
					case -74:
						break;
					case 75:
						{
    return new Symbol(TokenConstants.ESAC);
}
					case -75:
						break;
					case 76:
						{
    return new Symbol(TokenConstants.THEN);
}
					case -76:
						break;
					case 77:
						{
    return new Symbol(TokenConstants.POOL);
}
					case -77:
						break;
					case 78:
						{
    return new Symbol(TokenConstants.CLASS);
}
					case -78:
						break;
					case 79:
						{
    return new Symbol(TokenConstants.WHILE);
}
					case -79:
						break;
					case 80:
						{
    return new Symbol(TokenConstants.ISVOID);
}
					case -80:
						break;
					case 81:
						{
    return new Symbol(TokenConstants.INHERITS);
}
					case -81:
						break;
					case 82:
						{
    // comment we do nothing
}
					case -82:
						break;
					case 83:
						{
}
					case -83:
						break;
					case 85:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -84:
						break;
					case 86:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -85:
						break;
					case 87:
						{
    return new Symbol(TokenConstants.IN);
}
					case -86:
						break;
					case 88:
						{
    return new Symbol(TokenConstants.FI);
}
					case -87:
						break;
					case 89:
						{
    return new Symbol(TokenConstants.IF);
}
					case -88:
						break;
					case 90:
						{
    return new Symbol(TokenConstants.NOT);
}
					case -89:
						break;
					case 91:
						{
    return new Symbol(TokenConstants.NEW);
}
					case -90:
						break;
					case 92:
						{
     return new Symbol(TokenConstants.CASE);
}
					case -91:
						break;
					case 93:
						{
    return new Symbol(TokenConstants.LOOP);
}
					case -92:
						break;
					case 94:
						{
    return new Symbol(TokenConstants.ELSE);
}
					case -93:
						break;
					case 95:
						{
    return new Symbol(TokenConstants.CLASS);
}
					case -94:
						break;
					case 96:
						{
    return new Symbol(TokenConstants.WHILE);
}
					case -95:
						break;
					case 97:
						{
    return new Symbol(TokenConstants.ISVOID);
}
					case -96:
						break;
					case 98:
						{
    return new Symbol(TokenConstants.INHERITS);
}
					case -97:
						break;
					case 99:
						{
    // comment we do nothing
}
					case -98:
						break;
					case 101:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -99:
						break;
					case 102:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -100:
						break;
					case 103:
						{
    return new Symbol(TokenConstants.IN);
}
					case -101:
						break;
					case 104:
						{
    return new Symbol(TokenConstants.NOT);
}
					case -102:
						break;
					case 105:
						{
     return new Symbol(TokenConstants.CASE);
}
					case -103:
						break;
					case 106:
						{
    return new Symbol(TokenConstants.ELSE);
}
					case -104:
						break;
					case 107:
						{
    return new Symbol(TokenConstants.CLASS);
}
					case -105:
						break;
					case 108:
						{
    return new Symbol(TokenConstants.ISVOID);
}
					case -106:
						break;
					case 109:
						{
    // comment we do nothing
}
					case -107:
						break;
					case 111:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -108:
						break;
					case 112:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -109:
						break;
					case 113:
						{
    return new Symbol(TokenConstants.IN);
}
					case -110:
						break;
					case 114:
						{
    return new Symbol(TokenConstants.NOT);
}
					case -111:
						break;
					case 115:
						{
     return new Symbol(TokenConstants.CASE);
}
					case -112:
						break;
					case 117:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -113:
						break;
					case 118:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -114:
						break;
					case 119:
						{
     return new Symbol(TokenConstants.CASE);
}
					case -115:
						break;
					case 121:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -116:
						break;
					case 122:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -117:
						break;
					case 124:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -118:
						break;
					case 125:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -119:
						break;
					case 127:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -120:
						break;
					case 128:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -121:
						break;
					case 130:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -122:
						break;
					case 131:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -123:
						break;
					case 133:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -124:
						break;
					case 134:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -125:
						break;
					case 136:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -126:
						break;
					case 137:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -127:
						break;
					case 139:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -128:
						break;
					case 140:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -129:
						break;
					case 142:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -130:
						break;
					case 143:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -131:
						break;
					case 145:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -132:
						break;
					case 146:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -133:
						break;
					case 148:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -134:
						break;
					case 149:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -135:
						break;
					case 151:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -136:
						break;
					case 152:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -137:
						break;
					case 154:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -138:
						break;
					case 155:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -139:
						break;
					case 157:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -140:
						break;
					case 158:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -141:
						break;
					case 160:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -142:
						break;
					case 161:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -143:
						break;
					case 163:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -144:
						break;
					case 164:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -145:
						break;
					case 166:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -146:
						break;
					case 167:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -147:
						break;
					case 169:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -148:
						break;
					case 170:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -149:
						break;
					case 172:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -150:
						break;
					case 173:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -151:
						break;
					case 175:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -152:
						break;
					case 176:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -153:
						break;
					case 178:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -154:
						break;
					case 179:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -155:
						break;
					case 181:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -156:
						break;
					case 182:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -157:
						break;
					case 184:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -158:
						break;
					case 185:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -159:
						break;
					case 187:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -160:
						break;
					case 188:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -161:
						break;
					case 190:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -162:
						break;
					case 191:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -163:
						break;
					case 193:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -164:
						break;
					case 194:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -165:
						break;
					case 196:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -166:
						break;
					case 197:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -167:
						break;
					case 198:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -168:
						break;
					case 199:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -169:
						break;
					case 200:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -170:
						break;
					case 201:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -171:
						break;
					case 202:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -172:
						break;
					case 203:
						{
    return new Symbol(TokenConstants.BOOL_CONST, new Boolean(true));
}
					case -173:
						break;
					case 204:
						{
    return new Symbol(TokenConstants.BOOL_CONST, new Boolean(false));
}
					case -174:
						break;
					case 206:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -175:
						break;
					case 207:
						{
    return new Symbol(TokenConstants.OF);
}
					case -176:
						break;
					case 208:
						{
    return new Symbol(TokenConstants.LET);
}
					case -177:
						break;
					case 209:
						{
    return new Symbol(TokenConstants.ESAC);
}
					case -178:
						break;
					case 210:
						{
    return new Symbol(TokenConstants.THEN);
}
					case -179:
						break;
					case 211:
						{
    return new Symbol(TokenConstants.POOL);
}
					case -180:
						break;
					case 212:
						{
    return new Symbol(TokenConstants.FI);
}
					case -181:
						break;
					case 213:
						{
    return new Symbol(TokenConstants.IF);
}
					case -182:
						break;
					case 214:
						{
    return new Symbol(TokenConstants.NEW);
}
					case -183:
						break;
					case 215:
						{
    return new Symbol(TokenConstants.LOOP);
}
					case -184:
						break;
					case 216:
						{
    return new Symbol(TokenConstants.WHILE);
}
					case -185:
						break;
					case 217:
						{
    return new Symbol(TokenConstants.INHERITS);
}
					case -186:
						break;
					case 218:
						{
    return new Symbol(TokenConstants.ELSE);
}
					case -187:
						break;
					case 219:
						{
    return new Symbol(TokenConstants.CLASS);
}
					case -188:
						break;
					case 220:
						{
    return new Symbol(TokenConstants.ISVOID);
}
					case -189:
						break;
					case 221:
						{
    return new Symbol(TokenConstants.IN);
}
					case -190:
						break;
					case 222:
						{
    return new Symbol(TokenConstants.NOT);
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
     return new Symbol(TokenConstants.CASE);
}
					case -194:
						break;
					case 227:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -195:
						break;
					case 228:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -196:
						break;
					case 229:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -197:
						break;
					case 230:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -198:
						break;
					case 231:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -199:
						break;
					case 232:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -200:
						break;
					case 233:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -201:
						break;
					case 234:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -202:
						break;
					case 235:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -203:
						break;
					case 236:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -204:
						break;
					case 237:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -205:
						break;
					case 238:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -206:
						break;
					case 239:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -207:
						break;
					case 240:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -208:
						break;
					case 241:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -209:
						break;
					case 242:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -210:
						break;
					case 243:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -211:
						break;
					case 244:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -212:
						break;
					case 245:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -213:
						break;
					case 246:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -214:
						break;
					case 247:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -215:
						break;
					case 248:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -216:
						break;
					case 249:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -217:
						break;
					case 250:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -218:
						break;
					case 251:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -219:
						break;
					case 252:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -220:
						break;
					case 253:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -221:
						break;
					case 254:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -222:
						break;
					case 255:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -223:
						break;
					case 256:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -224:
						break;
					case 258:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -225:
						break;
					case 259:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -226:
						break;
					case 260:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -227:
						break;
					case 261:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -228:
						break;
					case 262:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -229:
						break;
					case 263:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -230:
						break;
					case 264:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -231:
						break;
					case 265:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -232:
						break;
					case 266:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -233:
						break;
					case 267:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -234:
						break;
					case 268:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -235:
						break;
					case 269:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -236:
						break;
					case 270:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -237:
						break;
					case 271:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -238:
						break;
					case 272:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -239:
						break;
					case 273:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -240:
						break;
					case 274:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -241:
						break;
					case 275:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -242:
						break;
					case 276:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -243:
						break;
					case 277:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -244:
						break;
					case 278:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.inttable.addString(yytext()));
}
					case -245:
						break;
					case 279:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -246:
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

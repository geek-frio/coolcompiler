/*
Copyright (c) 2000 The Regents of the University of California.
All rights reserved.

Permission to use, copy, modify, and distribute this software for any
purpose, without fee, and without written agreement is hereby granted,
provided that the above copyright notice and the following two
paragraphs appear in all copies of this software.

IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR
DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES ARISING OUT
OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF THE UNIVERSITY OF
CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
AND FITNESS FOR A PARTICULAR PURPOSE.  THE SOFTWARE PROVIDED HEREUNDER IS
ON AN "AS IS" BASIS, AND THE UNIVERSITY OF CALIFORNIA HAS NO OBLIGATION TO
PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
*/

import java.io.*;
import java.net.URISyntaxException;

/**
 * Static semantics driver class
 */
class Semant {

    /**
     * Reads AST from from consosle, and outputs the new AST
     */
    public static void main(String[] args) throws URISyntaxException, IOException {
        args = Flags.handleFlags(args);
        long t1 = System.currentTimeMillis();
        FileWriter fileWriter = new FileWriter(new File(System.getProperty("user.dir") + "/Time.output"), true);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            InputStream inputStream;
            if (Flags.input_file) {
                inputStream = Semant.class.getResourceAsStream("TestInput");
            } else {
                inputStream = System.in;
            }
            // 增加重定向inputstream到暂存空间中去,如果发生了读取故障,可以读取到输入的input
            TeeInputStream filterInputStream = new TeeInputStream(inputStream, byteArrayOutputStream);
            // end
            ASTLexer lexer = new ASTLexer(new InputStreamReader(filterInputStream));
            ASTParser parser = new ASTParser(lexer);
            Object result = parser.parse().value;
            ((Program) result).semant();
            ((Program) result).dump_with_types(System.out, 0);
        } catch (Exception ex) {
            if(!ex.getMessage().equals("noexit")){
                ex.printStackTrace(System.err);
            }
        } finally {
            long usedTime = System.currentTimeMillis() - t1;
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("all time:" + usedTime + "\n");
            if(usedTime > 200){
                bufferedWriter.write("Cost too long time, print the origin string....");
                bufferedWriter.write("===========\n");
                bufferedWriter.write(byteArrayOutputStream.toString());
                bufferedWriter.write("===========\n");
            }
            bufferedWriter.flush();
            bufferedWriter.close();
        }
    }

    /**
     * 分支流
     */
    static class TeeInputStream extends FilterInputStream {
        OutputStream branchStream;

        protected TeeInputStream(InputStream in, OutputStream outputStream) {
            super(in);
            this.branchStream = outputStream;
        }

        @Override
        public int read() throws IOException {
            final int ch = super.read();
            if (ch != -1) {
                branchStream.write(ch);
            }
            return ch;
        }

        @Override
        public int read(final byte[] bts, final int st, final int end) throws IOException {
            final int n = super.read(bts, st, end);
            if (n != -1) {
                branchStream.write(bts, st, n);
            }
            return n;
        }

        @Override
        public int read(final byte[] bts) throws IOException {
            final int n = super.read(bts);
            if (n != -1) {
                branchStream.write(bts, 0, n);
            }
            return n;
        }
    }
}

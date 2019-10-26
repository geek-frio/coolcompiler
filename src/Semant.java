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

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Static semantics driver class
 */
class Semant {

    /**
     * Reads AST from from consosle, and outputs the new AST
     */
    public static void main(String[] args) {
        args = Flags.handleFlags(args);
        InputStream in = Semant.class.getClassLoader()
                .getResourceAsStream("TestInput");
        try {
            long t1 = System.currentTimeMillis();
            ASTLexer lexer = new ASTLexer(new InputStreamReader(in));
            ASTParser parser = new ASTParser(lexer);
            Object result = parser.parse().value;
            ((Program) result).semant();
            ((Program) result).dump_with_types(System.out, 0);
            System.err.println("all time:" + (System.currentTimeMillis() - t1));
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
    }
}

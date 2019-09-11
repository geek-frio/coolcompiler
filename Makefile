ASSN = 2J
CLASSDIR= /usr/class
LIB= -L/usr/pubsw/lib -lfl -R/usr/pubsw/lib

SRC= cool.lex test.cl README
CSRC=	\
	src/compiler/AbstractSymbol.java \
	src/compiler/AbstractTable.java \
	src/compiler/BoolConst.java \
	src/compiler/CgenSupport.java \
	src/compiler/Flags.java \
	src/compiler/IdSymbol.java \
	src/compiler/IdTable.java \
	src/compiler/IntSymbol.java \
	src/compiler/IntTable.java \
	src/compiler/Lexer.java \
	src/compiler/StringSymbol.java \
	src/compiler/StringTable.java \
	src/compiler/TokenConstants.java \
	src/compiler/Utilities.java
TSRC= mycoolc
CGEN= src/compiler/CoolLexer.java
LIBS= parser semant cgen
CFIL= ${CSRC} ${CGEN}
LSRC= Makefile
CLS=	\
	bin/compiler/AbstractSymbol.class \
	bin/compiler/AbstractTable.class \
	bin/compiler/BoolConst.class \
	bin/compiler/CgenSupport.class \
	bin/compiler/Flags.class \
	bin/compiler/IdSymbol.class \
	bin/compiler/IdTable.class \
	bin/compiler/IntSymbol.class \
	bin/compiler/IntTable.class \
	bin/compiler/Lexer.class \
	bin/compiler/StringSymbol.class \
	bin/compiler/StringTable.class \
	bin/compiler/TokenConstants.class \
	bin/compiler/Utilities.class
OUTPUT= test.output

CUPCLASSPATH := ./libs/java-cup-11a.jar:./libs/jlex.jar

JAVAC := javac
JLEX := java -classpath ./libs/jlex.jar JLex.Main

# sm: we need rt.jar on Solaris/x86, and I'm hoping it won't
# cause a problem elsewhere
CLASSPATH := ${CUPCLASSPATH}:./bin/

source : ${SRC} ${TSRC} ${LSRC} ${LIBS} ${CSRC}

${OUTPUT}:	lexer test.cl
	@rm -f test.output
	-./lexer test.cl >test.output 2>&1 

lexer: Makefile ${CLS}
	@rm -f lexer
	echo '#!/bin/sh' >> lexer
	echo 'java -classpath ${CLASSPATH}:`dirname $$0` compiler.Lexer $$*' >> lexer
	chmod 755 lexer

CoolLexer.java: cool.lex
	${JLEX} $<
	mv cool.lex.java src/compiler/CoolLexer.java

dotest:	lexer test.cl
	./lexer test.cl

${CLS}: ${CFIL}
	${JAVAC} -d ./bin/ -classpath  ${CLASSPATH} ${CFIL}

${LIBS}:
	${CLASSDIR}/etc/link-object ${ASSN} $@

${TSRC} ${CSRC}:
	-ln -s ${CLASSDIR}/src/PA${ASSN}/$@ $@

clean :
	-rm -f *.s ${OUTPUT} ${CLS} *~ lexer parser cgen semant



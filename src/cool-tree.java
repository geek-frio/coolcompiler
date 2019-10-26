// -*- mode: java -*- 
//
// file: cool-tree.m4
//
// This file defines the AST
//
//////////////////////////////////////////////////////////

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

/**
 * Defines simple phylum Program
 */
abstract class Program extends TreeNode {
    protected Program(int lineNumber) {
        super(lineNumber);
    }

    public abstract void dump_with_types(PrintStream out, int n);

    public abstract void semant();

}

/**
 * Defines simple phylum Class_
 */
abstract class Class_ extends TreeNode {
    protected Class_(int lineNumber) {
        super(lineNumber);
    }

    public abstract void dump_with_types(PrintStream out, int n);

}

/**
 * Defines list phylum Classes
 * <p>
 * See <a href="ListNode.html">ListNode</a> for full documentation.
 */
class Classes extends ListNode {
    public final static Class elementClass = Class_.class;

    /**
     * Returns class of this lists's elements
     */
    public Class getElementClass() {
        return elementClass;
    }

    protected Classes(int lineNumber, Vector elements) {
        super(lineNumber, elements);
    }

    /**
     * Creates an empty "Classes" list
     */
    public Classes(int lineNumber) {
        super(lineNumber);
    }

    /**
     * Appends "Class_" element to this list
     */
    public Classes appendElement(TreeNode elem) {
        addElement(elem);
        return this;
    }

    public TreeNode copy() {
        return new Classes(lineNumber, copyElements());
    }
}

/**
 * Defines simple phylum Feature
 */
abstract class Feature extends TreeNode {
    protected Feature(int lineNumber) {
        super(lineNumber);
    }

    public abstract void dump_with_types(PrintStream out, int n);

}

/**
 * Defines list phylum Features
 * <p>
 * See <a href="ListNode.html">ListNode</a> for full documentation.
 */
class Features extends ListNode {
    public final static Class elementClass = Feature.class;

    /**
     * Returns class of this lists's elements
     */
    public Class getElementClass() {
        return elementClass;
    }

    protected Features(int lineNumber, Vector elements) {
        super(lineNumber, elements);
    }

    /**
     * Creates an empty "Features" list
     */
    public Features(int lineNumber) {
        super(lineNumber);
    }

    /**
     * Appends "Feature" element to this list
     */
    public Features appendElement(TreeNode elem) {
        addElement(elem);
        return this;
    }

    public TreeNode copy() {
        return new Features(lineNumber, copyElements());
    }
}

/**
 * Defines simple phylum Formal
 */
abstract class Formal extends TreeNode {
    protected Formal(int lineNumber) {
        super(lineNumber);
    }

    public abstract void dump_with_types(PrintStream out, int n);

}

/**
 * Defines list phylum Formals
 * <p>
 * See <a href="ListNode.html">ListNode</a> for full documentation.
 */
class Formals extends ListNode {
    public final static Class elementClass = Formal.class;

    /**
     * Returns class of this lists's elements
     */
    public Class getElementClass() {
        return elementClass;
    }

    protected Formals(int lineNumber, Vector elements) {
        super(lineNumber, elements);
    }

    /**
     * Creates an empty "Formals" list
     */
    public Formals(int lineNumber) {
        super(lineNumber);
    }

    /**
     * Appends "Formal" element to this list
     */
    public Formals appendElement(TreeNode elem) {
        addElement(elem);
        return this;
    }

    public TreeNode copy() {
        return new Formals(lineNumber, copyElements());
    }
}

/**
 * Defines simple phylum Expression
 */
abstract class Expression extends TreeNode {
    protected Expression(int lineNumber) {
        super(lineNumber);
    }

    private AbstractSymbol type = null;

    public AbstractSymbol get_type() {
        return type;
    }

    public Expression set_type(AbstractSymbol s) {
        type = s;
        return this;
    }

    public abstract void dump_with_types(PrintStream out, int n);

    public void dump_type(PrintStream out, int n) {
        if (type != null) {
            out.println(Utilities.pad(n) + ": " + type.getString());
        } else {
            out.println(Utilities.pad(n) + ": _no_type");
        }
    }

    public ClassTable.CoolClass.Type semant0(SymbolTable symbolTable){
        ClassTable.CoolClass.Type type = this.semant(symbolTable);
        this.type = AbstractTable.idtable.addString(type.getClassName());
        return type;
    }

}

/**
 * Defines list phylum Expressions
 * <p>
 * See <a href="ListNode.html">ListNode</a> for full documentation.
 */
class Expressions extends ListNode {
    public final static Class elementClass = Expression.class;

    /**
     * Returns class of this lists's elements
     */
    public Class getElementClass() {
        return elementClass;
    }

    protected Expressions(int lineNumber, Vector elements) {
        super(lineNumber, elements);
    }

    /**
     * Creates an empty "Expressions" list
     */
    public Expressions(int lineNumber) {
        super(lineNumber);
    }

    /**
     * Appends "Expression" element to this list
     */
    public Expressions appendElement(TreeNode elem) {
        addElement(elem);
        return this;
    }

    public TreeNode copy() {
        return new Expressions(lineNumber, copyElements());
    }
}

/**
 * Defines simple phylum Case
 */
abstract class Case extends TreeNode {
    protected Case(int lineNumber) {
        super(lineNumber);
    }

    public abstract void dump_with_types(PrintStream out, int n);

}

/**
 * Defines list phylum Cases
 * <p>
 * See <a href="ListNode.html">ListNode</a> for full documentation.
 */
class Cases extends ListNode {
    public final static Class elementClass = Case.class;

    /**
     * Returns class of this lists's elements
     */
    public Class getElementClass() {
        return elementClass;
    }

    protected Cases(int lineNumber, Vector elements) {
        super(lineNumber, elements);
    }

    /**
     * Creates an empty "Cases" list
     */
    public Cases(int lineNumber) {
        super(lineNumber);
    }

    /**
     * Appends "Case" element to this list
     */
    public Cases appendElement(TreeNode elem) {
        addElement(elem);
        return this;
    }

    public TreeNode copy() {
        return new Cases(lineNumber, copyElements());
    }
}

/**
 * Defines AST constructor 'programc'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class programc extends Program {
    protected Classes classes;

    /**
     * Creates "programc" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a1         initial value for classes
     */
    public programc(int lineNumber, Classes a1) {
        super(lineNumber);
        classes = a1;
    }

    public TreeNode copy() {
        return new programc(lineNumber, (Classes) classes.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "programc\n");
        classes.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_program");
        for (Enumeration e = classes.getElements(); e.hasMoreElements(); ) {
            // sm: changed 'n + 1' to 'n + 2' to match changes elsewhere
            ((Class_) e.nextElement()).dump_with_types(out, n + 2);
        }
    }

    /**
     * This method is the entry point to the semantic checker. You will need to
     * complete it in programming assignment 4.
     * <p>
     * Your checker should do the following two things:
     * <ol>
     * <li>Check that the program is semantically correct
     * <li>Decorate the abstract syntax tree with type information by setting the
     * type field in each Expression node. (see tree.h)
     * </ol>
     * <p>
     * You are free to first do (1) and make sure you catch all semantic errors.
     * Part (2) can be done in a second stage when you want to test the complete
     * compiler.
     */
    public void semant() {
        /* ClassTable constructor may do some semantic analysis */
        try {
            ClassTable classTable = new ClassTable(classes);
            SymbolTable symbolTable = new SymbolTable(classTable);
            /* some semantic analysis code may go here */
            Enumeration enumeration = classes.getElements();
            // 遍历AST树内的所有class节点,开始由上往下进行递归TypeChecking
            while (enumeration.hasMoreElements()) {
                class_c cls = (class_c) enumeration.nextElement();
                symbolTable.setCurrentClassNode(cls);
                cls.semant(symbolTable);
            }
            // 如果有异常, 退出执行状态
            if (classTable.errors()) {
                System.err.println("Compilation halted due to static semantic errors.");
                System.exit(1);
            }
        }catch (RuntimeException e){
            e.printStackTrace();
            System.err.println("Compilation halted due to static semantic errors.");
            System.exit(1);
        }
    }

}

/**
 * Defines AST constructor 'class_c'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class class_c extends Class_ {
    protected AbstractSymbol name;
    protected AbstractSymbol parent;
    protected Features features;
    protected AbstractSymbol filename;

    /**
     * Creates "class_c" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a1         initial value for name
     * @param a1         initial value for parent
     * @param a2         initial value for features
     * @param a3         initial value for filename
     */
    public class_c(int lineNumber, AbstractSymbol a1, AbstractSymbol a2, Features a3, AbstractSymbol a4) {
        super(lineNumber);
        name = a1;
        parent = a2;
        features = a3;
        filename = a4;
    }

    public TreeNode copy() {
        return new class_c(lineNumber, copy_AbstractSymbol(name), copy_AbstractSymbol(parent),
                (Features) features.copy(), copy_AbstractSymbol(filename));
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "class_c\n");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, parent);
        features.dump(out, n + 2);
        dump_AbstractSymbol(out, n + 2, filename);
    }

    public AbstractSymbol getFilename() {
        return filename;
    }

    public AbstractSymbol getName() {
        return name;
    }

    public AbstractSymbol getParent() {
        return parent;
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_class");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, parent);
        out.print(Utilities.pad(n + 2) + "\"");
        Utilities.printEscapedString(out, filename.getString());
        out.println("\"\n" + Utilities.pad(n + 2) + "(");
        for (Enumeration e = features.getElements(); e.hasMoreElements(); ) {
            ((Feature) e.nextElement()).dump_with_types(out, n + 2);
        }
        out.println(Utilities.pad(n + 2) + ")");
    }

    public ClassTable.CoolClass.Type semant(SymbolTable symbolTable) {
        for (Enumeration e = features.getElements(); e.hasMoreElements(); ) {
            ((Feature) e.nextElement()).semant(symbolTable);
        }
        return null;
    }

}

/**
 * Defines AST constructor 'method'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class method extends Feature {
    protected AbstractSymbol name;
    protected Formals formals;
    protected AbstractSymbol return_type;
    protected Expression expr;

    /**
     * Creates "method" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a1         initial value for name
     * @param a1         initial value for formals
     * @param a2         initial value for return_type
     * @param a3         initial value for expr
     */
    public method(int lineNumber, AbstractSymbol a1, Formals a2, AbstractSymbol a3, Expression a4) {
        super(lineNumber);
        name = a1;
        formals = a2;
        return_type = a3;
        expr = a4;
    }

    public TreeNode copy() {
        return new method(lineNumber, copy_AbstractSymbol(name), (Formals) formals.copy(),
                copy_AbstractSymbol(return_type), (Expression) expr.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "method\n");
        dump_AbstractSymbol(out, n + 2, name);
        formals.dump(out, n + 2);
        dump_AbstractSymbol(out, n + 2, return_type);
        expr.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_method");
        dump_AbstractSymbol(out, n + 2, name);
        for (Enumeration e = formals.getElements(); e.hasMoreElements(); ) {
            ((Formal) e.nextElement()).dump_with_types(out, n + 2);
        }
        dump_AbstractSymbol(out, n + 2, return_type);
        expr.dump_with_types(out, n + 2);
    }

    public ClassTable.CoolClass.Type semant(SymbolTable symbolTable) {
        // 获取当前正在TypeChecking的CoolClass
        ClassTable.CoolClass coolClass = symbolTable.getClassTable().getCoolClass(symbolTable.getCurrentClassNode().name.toString());
        // 需要将参数信息加入到SymbolTable中去
        // 提取每个参数的类型
        symbolTable.enterScope();
        // 加入self的type映射关系
        symbolTable.addId(TreeConstants.self, new ClassTable.CoolClass.Type(symbolTable.getCurrentClassNode().getName().toString()));
        Enumeration e = formals.getElements();
        while (e.hasMoreElements()) {
            formalc fc = (formalc) e.nextElement();
            String type = fc.type_decl.toString();
            if (type.equals(TreeConstants.SELF_TYPE.toString())) {
                // 方法参数不能传递 Self Type的类型
                symbolTable.getClassTable().semantError(symbolTable.getCurrentClassNode().filename, fc.lineNumber, "method argument type can not be SELF_TYPE!");
            }
            symbolTable.addId(fc.name, new ClassTable.CoolClass.Type(fc.type_decl.toString()));
        }
        // method return type
        ClassTable.CoolClass.Type returnType;
        if(this.return_type.toString().equals(TreeConstants.SELF_TYPE.toString())){
            returnType = new ClassTable.CoolClass.Type(symbolTable.getCurrentClassNode().name.toString());
        }else{
            returnType = new ClassTable.CoolClass.Type(return_type.toString());
        }
        // type checking type
        ClassTable.CoolClass.Type checkingType = symbolTable.typeCheckingExpression(expr);
        // 方法的body返回类型应该为方法定义类型的子类型
        if(!symbolTable.getClassTable().checkSub(checkingType, returnType)){
            symbolTable.getClassTable().semantError(symbolTable.getCurrentClassNode().filename, this.lineNumber, "method return type is not subtype of declaring type!");
        }
        symbolTable.exitScope();
        // 方法体没有返回类型
        return null;
    }

}

/**
 * Defines AST constructor 'attr'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class attr extends Feature {
    protected AbstractSymbol name;
    protected AbstractSymbol type_decl;
    protected Expression init;

    /**
     * Creates "attr" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a1         initial value for name
     * @param a1         initial value for type_decl
     * @param a2         initial value for init
     */
    public attr(int lineNumber, AbstractSymbol a1, AbstractSymbol a2, Expression a3) {
        super(lineNumber);
        name = a1;
        type_decl = a2;
        init = a3;
    }

    public TreeNode copy() {
        return new attr(lineNumber, copy_AbstractSymbol(name), copy_AbstractSymbol(type_decl),
                (Expression) init.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "attr\n");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, type_decl);
        init.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_attr");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, type_decl);
        init.dump_with_types(out, n + 2);
    }

    public ClassTable.CoolClass.Type semant(SymbolTable symbolTable) {
        // atrr type checking 分两种情况,
        // 没有 init expression 的情况
        // 不需要做多余的事情, 也不需要返回类型
        if (this.init instanceof no_expr) {
            return null;
        }
        // 有 init expression 的情况
        // 1), 获取属性对应的类型 T0
        ClassTable.CoolClass.Type type = (ClassTable.CoolClass.Type) symbolTable.lookup(this.name);
        // 2), 环境变量中加入self -> SELF_TYPE
        symbolTable.enterScope();
        symbolTable.addId(TreeConstants.self, new ClassTable.CoolClass.Type(symbolTable.getCurrentClassNode().name.toString()));
        // 3), 执行TypeChecking init的Expression
        ClassTable.CoolClass.Type returnType = init.semant0(symbolTable);
        symbolTable.exitScope();
        // 4), 返回类型, 查询返回类型是否为 T0 的子类或者等于 T0
        if (!symbolTable.getClassTable().checkSub(returnType, type)) {
            symbolTable.getClassTable().semantError(symbolTable.getCurrentClassNode().filename, this.lineNumber, "init expr type should be subtype of declaring type");
        }
        return null;
    }
}

/**
 * Defines AST constructor 'formalc'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class formalc extends Formal {
    protected AbstractSymbol name;
    protected AbstractSymbol type_decl;

    /**
     * Creates "formalc" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a1         initial value for name
     * @param a1         initial value for type_decl
     */
    public formalc(int lineNumber, AbstractSymbol a1, AbstractSymbol a2) {
        super(lineNumber);
        name = a1;
        type_decl = a2;
    }

    public TreeNode copy() {
        return new formalc(lineNumber, copy_AbstractSymbol(name), copy_AbstractSymbol(type_decl));
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "formalc\n");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, type_decl);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_formal");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, type_decl);
    }

}

/**
 * Defines AST constructor 'branch'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class branch extends Case {
    protected AbstractSymbol name;
    protected AbstractSymbol type_decl;
    protected Expression expr;

    /**
     * Creates "branch" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a1         initial value for name
     * @param a1         initial value for type_decl
     * @param a2         initial value for expr
     */
    public branch(int lineNumber, AbstractSymbol a1, AbstractSymbol a2, Expression a3) {
        super(lineNumber);
        name = a1;
        type_decl = a2;
        expr = a3;
    }

    public TreeNode copy() {
        return new branch(lineNumber, copy_AbstractSymbol(name), copy_AbstractSymbol(type_decl),
                (Expression) expr.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "branch\n");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, type_decl);
        expr.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_branch");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, type_decl);
        expr.dump_with_types(out, n + 2);
    }

    public ClassTable.CoolClass.Type semant(SymbolTable symbolTable) {
        symbolTable.enterScope();
        if (type_decl.toString().equals(TreeConstants.SELF_TYPE.toString())) {
            symbolTable.getClassTable().semantError(symbolTable.getCurrentClassNode().filename, this.lineNumber, "declaring type should not be SELF_TYPE");
            return new ClassTable.CoolClass.Type(TreeConstants.Object_.toString());
        }
        symbolTable.addId(name, new ClassTable.CoolClass.Type(type_decl.toString()));
        ClassTable.CoolClass.Type type = expr.semant0(symbolTable);
        symbolTable.exitScope();
        return type;
    }
}

/**
 * Defines AST constructor 'assign'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class assign extends Expression {
    protected AbstractSymbol name;
    protected Expression expr;

    /**
     * Creates "assign" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a1         initial value for name
     * @param a1         initial value for expr
     */
    public assign(int lineNumber, AbstractSymbol a1, Expression a2) {
        super(lineNumber);
        name = a1;
        expr = a2;
    }

    public TreeNode copy() {
        return new assign(lineNumber, copy_AbstractSymbol(name), (Expression) expr.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "assign\n");
        dump_AbstractSymbol(out, n + 2, name);
        expr.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_assign");
        dump_AbstractSymbol(out, n + 2, name);
        expr.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    public ClassTable.CoolClass.Type semant(SymbolTable symbolTable) {
        ClassTable.CoolClass.Type t0 = (ClassTable.CoolClass.Type) symbolTable.lookup(name);
        ClassTable.CoolClass.Type t1 = expr.semant0(symbolTable);
        if (!symbolTable.getClassTable().checkSub(t1, t0)) {
            symbolTable.getClassTable().semantError(symbolTable.getCurrentClassNode().filename, this.lineNumber, "Assign expr type should be the subtype of declaring type!");
            return new ClassTable.CoolClass.Type(TreeConstants.Object_.toString());
        }
        return t1;
    }

}

/**
 * Defines AST constructor 'static_dispatch'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class static_dispatch extends Expression {
    protected Expression expr;
    protected AbstractSymbol type_name;
    protected AbstractSymbol name;
    protected Expressions actual;

    /**
     * Creates "static_dispatch" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a1         initial value for expr
     * @param a1         initial value for type_name
     * @param a2         initial value for name
     * @param a3         initial value for actual
     */
    public static_dispatch(int lineNumber, Expression a1, AbstractSymbol a2, AbstractSymbol a3, Expressions a4) {
        super(lineNumber);
        expr = a1;
        type_name = a2; // 父类类型
        name = a3;
        actual = a4;
    }

    public TreeNode copy() {
        return new static_dispatch(lineNumber, (Expression) expr.copy(), copy_AbstractSymbol(type_name),
                copy_AbstractSymbol(name), (Expressions) actual.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "static_dispatch\n");
        expr.dump(out, n + 2);
        dump_AbstractSymbol(out, n + 2, type_name);
        dump_AbstractSymbol(out, n + 2, name);
        actual.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_static_dispatch");
        expr.dump_with_types(out, n + 2);
        dump_AbstractSymbol(out, n + 2, type_name);
        dump_AbstractSymbol(out, n + 2, name);
        out.println(Utilities.pad(n + 2) + "(");
        for (Enumeration e = actual.getElements(); e.hasMoreElements(); ) {
            ((Expression) e.nextElement()).dump_with_types(out, n + 2);
        }
        out.println(Utilities.pad(n + 2) + ")");
        dump_type(out, n);
    }

    public ClassTable.CoolClass.Type semant(SymbolTable symbolTable) {
        // 获取caller type, 如果caller type 定义为
        ClassTable.CoolClass.Type callerType = symbolTable.typeCheckingExpression(expr);
        // 循环 type checking 每个参数的类型
        List<ClassTable.CoolClass.Type> argTypes = symbolTable.typeCheckMethodArguments(actual);
        // 取出方法声明的所有Types, 校验类型是否匹配, 即argTypes中的参数是否为声明的类型的子类型
        // 从前面的调用方的类中取出对应的方法
        ClassTable.CoolClass coolClass = symbolTable.getClassTable().getCoolClass(type_name.toString());
        ClassTable.CoolClass.Method method = coolClass.getM(name.toString());
        if(method == null){
            symbolTable.getClassTable().semantError(symbolTable.getCurrentClassNode().filename, this.lineNumber, "method:" + name.toString() + "does no exist!");
            return new ClassTable.CoolClass.Type(TreeConstants.Object_.toString());
        }
        // 我们需要校验方法实际传递参数的类型应该为定义类型的子类
        StringBuilder sb = new StringBuilder();
        if (!symbolTable.checkArguments(argTypes, method, sb)) {
            symbolTable.getClassTable().semantError(symbolTable.getCurrentClassNode().filename, this.lineNumber, sb.toString());
            return new ClassTable.CoolClass.Type(TreeConstants.Object_.toString());
        }
        // 增加校验 callerType 应该为 type_name 的子类
        if (!symbolTable.getClassTable().checkSub(callerType, new ClassTable.CoolClass.Type(type_name.toString()))) {
            symbolTable.getClassTable().semantError(symbolTable.getCurrentClassNode().filename, this.lineNumber,
                    "caller expression's type should be the subtype of declaration type");
            return new ClassTable.CoolClass.Type(TreeConstants.Object_.toString());
        }
        // 如果声明的返回类型为SELF_TYPE,那么返回调用方 expr 类型
        ClassTable.CoolClass.Type returnType;
        if (method.getReturnType().getClassName().equals(TreeConstants.SELF_TYPE.toString())) {
            returnType = callerType;
        } else {
            returnType = method.getReturnType();
        }
        return returnType;
    }
}

/**
 * Defines AST constructor 'dispatch'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class dispatch extends Expression {
    protected Expression expr;
    protected AbstractSymbol name;
    protected Expressions actual;

    /**
     * Creates "dispatch" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a1         initial value for expr
     * @param a1         initial value for name
     * @param a2         initial value for actual
     */
    public dispatch(int lineNumber, Expression a1, AbstractSymbol a2, Expressions a3) {
        super(lineNumber);
        expr = a1;
        name = a2;
        actual = a3;
    }

    public TreeNode copy() {
        return new dispatch(lineNumber, (Expression) expr.copy(), copy_AbstractSymbol(name),
                (Expressions) actual.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "dispatch\n");
        expr.dump(out, n + 2);
        dump_AbstractSymbol(out, n + 2, name);
        actual.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_dispatch");
        expr.dump_with_types(out, n + 2);
        dump_AbstractSymbol(out, n + 2, name);
        out.println(Utilities.pad(n + 2) + "(");
        for (Enumeration e = actual.getElements(); e.hasMoreElements(); ) {
            ((Expression) e.nextElement()).dump_with_types(out, n + 2);
        }
        out.println(Utilities.pad(n + 2) + ")");
        dump_type(out, n);
    }

    public ClassTable.CoolClass.Type semant(SymbolTable symbolTable) {
        // 获取caller type, 如果caller type 定义为
        ClassTable.CoolClass.Type callerType = symbolTable.typeCheckingExpression(expr);
        // 循环 type checking 每个参数的类型
        List<ClassTable.CoolClass.Type> argTypes = symbolTable.typeCheckMethodArguments(actual);
        // 取出方法声明的所有Types, 校验类型是否匹配, 即argTypes中的参数是否为声明的类型的子类型
        // 从前面的调用方的类中取出对应的方法
        ClassTable.CoolClass coolClass = symbolTable.getClassTable().getCoolClass(callerType.getClassName());
        ClassTable.CoolClass.Method method = coolClass.getM(name.toString());
        if(method == null){
            symbolTable.getClassTable().semantError(symbolTable.getCurrentClassNode().filename, this.lineNumber, "method:" + name.toString() + " does not exist!");
            return new ClassTable.CoolClass.Type(TreeConstants.Object_.toString());
        }
        // 我们需要校验方法实际传递参数的类型应该为定义类型的子类
        StringBuilder sb = new StringBuilder();
        if(!symbolTable.checkArguments(argTypes, method, sb)){
            symbolTable.getClassTable().semantError(symbolTable.getCurrentClassNode().filename, this.lineNumber, sb.toString());
            return new ClassTable.CoolClass.Type(TreeConstants.Object_.toString());
        }
        // 如果声明的返回类型为SELF_TYPE,那么返回 expr 进行 TypeChecking 后的类型
        ClassTable.CoolClass.Type returnType;
        if (method.getReturnType().getClassName().equals(TreeConstants.SELF_TYPE.toString())) {
            returnType = callerType;
        } else {
            returnType = method.getReturnType();
        }
        return returnType;
    }
}

/**
 * Defines AST constructor 'cond'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class cond extends Expression {
    protected Expression pred;
    protected Expression then_exp;
    protected Expression else_exp;

    /**
     * Creates "cond" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a1         initial value for pred
     * @param a1         initial value for then_exp
     * @param a2         initial value for else_exp
     */
    public cond(int lineNumber, Expression a1, Expression a2, Expression a3) {
        super(lineNumber);
        pred = a1;
        then_exp = a2;
        else_exp = a3;
    }

    public TreeNode copy() {
        return new cond(lineNumber, (Expression) pred.copy(), (Expression) then_exp.copy(),
                (Expression) else_exp.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "cond\n");
        pred.dump(out, n + 2);
        then_exp.dump(out, n + 2);
        else_exp.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_cond");
        pred.dump_with_types(out, n + 2);
        then_exp.dump_with_types(out, n + 2);
        else_exp.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    public ClassTable.CoolClass.Type semant(SymbolTable symbolTable) {
        ClassTable.CoolClass.Type type = pred.semant0(symbolTable);
        if (type == null || !type.getClassName().equals(TreeConstants.Bool.toString())) {
            symbolTable.getClassTable().semantError(symbolTable.getCurrentClassNode().filename, this.lineNumber, "if condition expression type should be boolean");
            return new ClassTable.CoolClass.Type(TreeConstants.Object_.toString());
        }
        ClassTable.CoolClass.Type ifBranchType = then_exp.semant0(symbolTable);
        ClassTable.CoolClass.Type elseBranchType = else_exp.semant0(symbolTable);
        // 取两者往上的共同父类
        ClassTable.CoolClass.Type lubType = symbolTable.getClassTable().lub(ifBranchType, elseBranchType);
        if (lubType == null) {
            symbolTable.getClassTable().semantError(symbolTable.getCurrentClassNode().filename, this.lineNumber,
                    "If branch's type and else branch's type can't find common parent's type");
            return new ClassTable.CoolClass.Type(TreeConstants.Object_.toString());
        }
        return lubType;
    }
}

/**
 * Defines AST constructor 'loop'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class loop extends Expression {
    protected Expression pred;
    protected Expression body;

    /**
     * Creates "loop" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a1         initial value for pred
     * @param a1         initial value for body
     */
    public loop(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        pred = a1;
        body = a2;
    }

    public TreeNode copy() {
        return new loop(lineNumber, (Expression) pred.copy(), (Expression) body.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "loop\n");
        pred.dump(out, n + 2);
        body.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_loop");
        pred.dump_with_types(out, n + 2);
        body.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    public ClassTable.CoolClass.Type semant(SymbolTable symbolTable) {
        ClassTable.CoolClass.Type condType = pred.semant0(symbolTable);
        if (!condType.getClassName().equals(TreeConstants.Bool.toString())) {
            symbolTable.getClassTable().semantError(symbolTable.getCurrentClassNode().filename, this.lineNumber, "while condition's type should be boolean");
            return new ClassTable.CoolClass.Type(TreeConstants.Object_.toString());
        }
        // 只做执行,不依赖这个结果
        body.semant0(symbolTable);
        return new ClassTable.CoolClass.Type(TreeConstants.Object_.toString());
    }

}

/**
 * Defines AST constructor 'typcase'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class typcase extends Expression {
    protected Expression expr;
    protected Cases cases;

    /**
     * Creates "typcase" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a1         initial value for expr
     * @param a1         initial value for cases
     */
    public typcase(int lineNumber, Expression a1, Cases a2) {
        super(lineNumber);
        expr = a1;
        cases = a2;
    }

    public TreeNode copy() {
        return new typcase(lineNumber, (Expression) expr.copy(), (Cases) cases.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "typcase\n");
        expr.dump(out, n + 2);
        cases.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_typcase");
        expr.dump_with_types(out, n + 2);
        for (Enumeration e = cases.getElements(); e.hasMoreElements(); ) {
            ((Case) e.nextElement()).dump_with_types(out, n + 2);
        }
        dump_type(out, n);
    }

    public ClassTable.CoolClass.Type semant(SymbolTable symbolTable) {
        ClassTable.CoolClass.Type t0 = expr.semant0(symbolTable);
        List<ClassTable.CoolClass.Type> types = new ArrayList<>();
        // 获取cases的所有types数组列表
        Enumeration enumeration;
        while ((enumeration = cases.getElements()).hasMoreElements()) {
            branch b = (branch) enumeration.nextElement();
            types.add(b.semant(symbolTable));
        }
        // 取所有类型的父类
        ClassTable.CoolClass.Type type = symbolTable.getClassTable().lub(types);
        return type;
    }

}

/**
 * Defines AST constructor 'block'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class block extends Expression {
    protected Expressions body;

    /**
     * Creates "block" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a1         initial value for body
     */
    public block(int lineNumber, Expressions a1) {
        super(lineNumber);
        body = a1;
    }

    public TreeNode copy() {
        return new block(lineNumber, (Expressions) body.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "block\n");
        body.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_block");
        for (Enumeration e = body.getElements(); e.hasMoreElements(); ) {
            ((Expression) e.nextElement()).dump_with_types(out, n + 2);
        }
        dump_type(out, n);
    }

    public ClassTable.CoolClass.Type semant(SymbolTable symbolTable) {
        if (body != null) {
            List<ClassTable.CoolClass.Type> types = new ArrayList<>();
            Enumeration elements = body.getElements();
            while (elements.hasMoreElements()) {
                Expression expr = (Expression) elements.nextElement();
                ClassTable.CoolClass.Type type = expr.semant0(symbolTable);
                types.add(type);
            }
            if(types.size() > 0){
                return types.get(types.size() - 1);
            }
        }
        return new ClassTable.CoolClass.Type(TreeConstants.Object_.toString());
    }
}

/**
 * Defines AST constructor 'let'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class let extends Expression {
    protected AbstractSymbol identifier;
    protected AbstractSymbol type_decl;
    protected Expression init;
    protected Expression body;

    /**
     * Creates "let" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a1         initial value for identifier
     * @param a1         initial value for type_decl
     * @param a2         initial value for init
     * @param a3         initial value for body
     */
    public let(int lineNumber, AbstractSymbol a1, AbstractSymbol a2, Expression a3, Expression a4) {
        super(lineNumber);
        identifier = a1;
        type_decl = a2;
        init = a3;
        body = a4;
    }

    public TreeNode copy() {
        return new let(lineNumber, copy_AbstractSymbol(identifier), copy_AbstractSymbol(type_decl),
                (Expression) init.copy(), (Expression) body.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "let\n");
        dump_AbstractSymbol(out, n + 2, identifier);
        dump_AbstractSymbol(out, n + 2, type_decl);
        init.dump(out, n + 2);
        body.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_let");
        dump_AbstractSymbol(out, n + 2, identifier);
        dump_AbstractSymbol(out, n + 2, type_decl);
        init.dump_with_types(out, n + 2);
        body.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    public ClassTable.CoolClass.Type semant(SymbolTable symbolTable) {
        // 如果声明类型为SelfType,我们要进行转化为实际的类型
        ClassTable.CoolClass.Type t0;
        if (type_decl.toString().equals(TreeConstants.SELF_TYPE)) {
            t0 = new ClassTable.CoolClass.Type(symbolTable.getCurrentClassNode().name.toString());
        } else {
            t0 = new ClassTable.CoolClass.Type(type_decl.toString());
        }
        // 如果init expr不为空,那么我们应该校验的是这个expr返回的类型是否和声明的类型一致
        if (!(init instanceof no_expr)) {
            ClassTable.CoolClass.Type initType = init.semant0(symbolTable);
            if (!symbolTable.getClassTable().checkSub(initType, t0)) {
                symbolTable.getClassTable().semantError(symbolTable.getCurrentClassNode().filename, init.lineNumber,
                        "Init Expression's type should be subtype of declaration's type");
                return new ClassTable.CoolClass.Type(TreeConstants.Object_.toString());
            }
        }
        // 加入新声明的 x:T0 到 scope 中去
        symbolTable.enterScope();
        symbolTable.addId(identifier, t0);
        ClassTable.CoolClass.Type t2 = body.semant0(symbolTable);
        symbolTable.exitScope();
        return t2;
    }

}

/**
 * Defines AST constructor 'plus'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class plus extends Expression {
    protected Expression e1;
    protected Expression e2;

    /**
     * Creates "plus" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a1         initial value for e1
     * @param a1         initial value for e2
     */
    public plus(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }

    public TreeNode copy() {
        return new plus(lineNumber, (Expression) e1.copy(), (Expression) e2.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "plus\n");
        e1.dump(out, n + 2);
        e2.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_plus");
        e1.dump_with_types(out, n + 2);
        e2.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    public ClassTable.CoolClass.Type semant(SymbolTable symbolTable) {
        ClassTable.CoolClass.Type t1 = e1.semant0(symbolTable);
        ClassTable.CoolClass.Type t2 = e2.semant0(symbolTable);
        if (!t1.getClassName().equals(TreeConstants.Int.toString())) {
            symbolTable.getClassTable().semantError(symbolTable.getCurrentClassNode().filename,
                    this.lineNumber, "Left expression's type should be Int");
            return new ClassTable.CoolClass.Type(TreeConstants.Object_.toString());
        }
        if (!t2.getClassName().equals(TreeConstants.Int.toString())) {
            symbolTable.getClassTable().semantError(symbolTable.getCurrentClassNode().filename,
                    this.lineNumber, "Right expression's type should be Int");
            return new ClassTable.CoolClass.Type(TreeConstants.Object_.toString());
        }
        return new ClassTable.CoolClass.Type(TreeConstants.Int.toString());
    }

}

/**
 * Defines AST constructor 'sub'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class sub extends Expression {
    protected Expression e1;
    protected Expression e2;

    /**
     * Creates "sub" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a1         initial value for e1
     * @param a1         initial value for e2
     */
    public sub(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }

    public TreeNode copy() {
        return new sub(lineNumber, (Expression) e1.copy(), (Expression) e2.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "sub\n");
        e1.dump(out, n + 2);
        e2.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_sub");
        e1.dump_with_types(out, n + 2);
        e2.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    public ClassTable.CoolClass.Type semant(SymbolTable symbolTable) {
        ClassTable.CoolClass.Type t1 = e1.semant0(symbolTable);
        ClassTable.CoolClass.Type t2 = e2.semant0(symbolTable);
        if (!t1.getClassName().equals(TreeConstants.Int.toString())) {
            symbolTable.getClassTable().semantError(symbolTable.getCurrentClassNode().filename,
                    this.lineNumber, "Left expression's type should be Int");
            return new ClassTable.CoolClass.Type(TreeConstants.Object_.toString());
        }
        if (!t2.getClassName().equals(TreeConstants.Int.toString())) {
            symbolTable.getClassTable().semantError(symbolTable.getCurrentClassNode().filename,
                    this.lineNumber, "Right expression's type should be Int");
            return new ClassTable.CoolClass.Type(TreeConstants.Object_.toString());
        }
        return new ClassTable.CoolClass.Type(TreeConstants.Int.toString());
    }
}

/**
 * Defines AST constructor 'mul'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class mul extends Expression {
    protected Expression e1;
    protected Expression e2;

    /**
     * Creates "mul" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a1         initial value for e1
     * @param a1         initial value for e2
     */
    public mul(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }

    public TreeNode copy() {
        return new mul(lineNumber, (Expression) e1.copy(), (Expression) e2.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "mul\n");
        e1.dump(out, n + 2);
        e2.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_mul");
        e1.dump_with_types(out, n + 2);
        e2.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    public ClassTable.CoolClass.Type semant(SymbolTable symbolTable) {
        ClassTable.CoolClass.Type t1 = e1.semant0(symbolTable);
        ClassTable.CoolClass.Type t2 = e2.semant0(symbolTable);
        if (!t1.getClassName().equals(TreeConstants.Int.toString())) {
            symbolTable.getClassTable().semantError(symbolTable.getCurrentClassNode().filename,
                    this.lineNumber, "Left expression's type should be Int");
            return new ClassTable.CoolClass.Type(TreeConstants.Object_.toString());
        }
        if (!t2.getClassName().equals(TreeConstants.Int.toString())) {
            symbolTable.getClassTable().semantError(symbolTable.getCurrentClassNode().filename,
                    this.lineNumber, "Right expression's type should be Int");
            return new ClassTable.CoolClass.Type(TreeConstants.Object_.toString());
        }
        return new ClassTable.CoolClass.Type(TreeConstants.Int.toString());
    }
}

/**
 * Defines AST constructor 'divide'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class divide extends Expression {
    protected Expression e1;
    protected Expression e2;

    /**
     * Creates "divide" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a1         initial value for e1
     * @param a1         initial value for e2
     */
    public divide(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }

    public TreeNode copy() {
        return new divide(lineNumber, (Expression) e1.copy(), (Expression) e2.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "divide\n");
        e1.dump(out, n + 2);
        e2.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_divide");
        e1.dump_with_types(out, n + 2);
        e2.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    public ClassTable.CoolClass.Type semant(SymbolTable symbolTable) {
        ClassTable.CoolClass.Type t1 = e1.semant0(symbolTable);
        ClassTable.CoolClass.Type t2 = e2.semant0(symbolTable);
        if (!t1.getClassName().equals(TreeConstants.Int.toString())) {
            symbolTable.getClassTable().semantError(symbolTable.getCurrentClassNode().filename,
                    this.lineNumber, "Left expression's type should be Int");
            return new ClassTable.CoolClass.Type(TreeConstants.Object_.toString());
        }
        if (!t2.getClassName().equals(TreeConstants.Int.toString())) {
            symbolTable.getClassTable().semantError(symbolTable.getCurrentClassNode().filename,
                    this.lineNumber, "Right expression's type should be Int");
            return new ClassTable.CoolClass.Type(TreeConstants.Object_.toString());
        }
        return new ClassTable.CoolClass.Type(TreeConstants.Int.toString());
    }

}

/**
 * Defines AST constructor 'neg'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class neg extends Expression {
    protected Expression e1;

    /**
     * Creates "neg" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a1         initial value for e1
     */
    public neg(int lineNumber, Expression a1) {
        super(lineNumber);
        e1 = a1;
    }

    public TreeNode copy() {
        return new neg(lineNumber, (Expression) e1.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "neg\n");
        e1.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_neg");
        e1.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    public ClassTable.CoolClass.Type semant(SymbolTable symbolTable) {
        ClassTable.CoolClass.Type t = e1.semant0(symbolTable);
        if (!t.getClassName().equals(TreeConstants.Int.toString())) {
            symbolTable.getClassTable().semantError(symbolTable.getCurrentClassNode().filename,
                    this.lineNumber, "Neg expression's has to be Int");
            return new ClassTable.CoolClass.Type(TreeConstants.Object_.toString());
        }
        return t;
    }

}

/**
 * Defines AST constructor 'lt'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class lt extends Expression {
    protected Expression e1;
    protected Expression e2;

    /**
     * Creates "lt" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a1         initial value for e1
     * @param a1         initial value for e2
     */
    public lt(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }

    public TreeNode copy() {
        return new lt(lineNumber, (Expression) e1.copy(), (Expression) e2.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "lt\n");
        e1.dump(out, n + 2);
        e2.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_lt");
        e1.dump_with_types(out, n + 2);
        e2.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    public ClassTable.CoolClass.Type semant(SymbolTable symbolTable) {
        ClassTable.CoolClass.Type t1 = e1.semant0(symbolTable);
        ClassTable.CoolClass.Type t2 = e2.semant0(symbolTable);
        if (!t1.getClassName().equals(TreeConstants.Int.toString())) {
            symbolTable.getClassTable().semantError(symbolTable.getCurrentClassNode().filename,
                    this.lineNumber, "Left expression's type should be Int");
            return new ClassTable.CoolClass.Type(TreeConstants.Object_.toString());
        }
        if (!t2.getClassName().equals(TreeConstants.Int.toString())) {
            symbolTable.getClassTable().semantError(symbolTable.getCurrentClassNode().filename,
                    this.lineNumber, "Right expression's type should be Int");
            return new ClassTable.CoolClass.Type(TreeConstants.Object_.toString());
        }
        return new ClassTable.CoolClass.Type(TreeConstants.Bool.toString());
    }
}

/**
 * Defines AST constructor 'eq'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class eq extends Expression {
    protected Expression e1;
    protected Expression e2;

    /**
     * Creates "eq" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a1         initial value for e1
     * @param a1         initial value for e2
     */
    public eq(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }

    public TreeNode copy() {
        return new eq(lineNumber, (Expression) e1.copy(), (Expression) e2.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "eq\n");
        e1.dump(out, n + 2);
        e2.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_eq");
        e1.dump_with_types(out, n + 2);
        e2.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    public ClassTable.CoolClass.Type semant(SymbolTable symbolTable) {
        ClassTable.CoolClass.Type t1 = e1.semant0(symbolTable);
        ClassTable.CoolClass.Type t2 = e2.semant0(symbolTable);
        if (!t1.getClassName().equals(TreeConstants.Int.toString())
                && !t1.getClassName().equals(TreeConstants.Str.toString())
                && !t1.getClassName().equals(TreeConstants.Bool.toString())) {
            symbolTable.getClassTable().semantError(symbolTable.getCurrentClassNode().filename,
                    this.lineNumber, "Left expression's type should be Int, Str, Bool");
            return new ClassTable.CoolClass.Type(TreeConstants.Object_.toString());
        }
        if (!t2.getClassName().equals(TreeConstants.Int.toString())
                && !t2.getClassName().equals(TreeConstants.Str.toString())
                && !t2.getClassName().equals(TreeConstants.Bool.toString())) {
            symbolTable.getClassTable().semantError(symbolTable.getCurrentClassNode().filename,
                    this.lineNumber, "Right expression's type should be Int, Str, Bool");
            return new ClassTable.CoolClass.Type(TreeConstants.Object_.toString());
        }
        if (!t1.equals(t2)) {
            symbolTable.getClassTable().semantError(symbolTable.getCurrentClassNode().filename,
                    this.lineNumber, "Two sub expressions should have the same type!");
            return new ClassTable.CoolClass.Type(TreeConstants.Object_.toString());
        }
        return new ClassTable.CoolClass.Type(TreeConstants.Bool.toString());
    }

}

/**
 * Defines AST constructor 'leq'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class leq extends Expression {
    protected Expression e1;
    protected Expression e2;

    /**
     * Creates "leq" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a1         initial value for e1
     * @param a1         initial value for e2
     */
    public leq(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }

    public TreeNode copy() {
        return new leq(lineNumber, (Expression) e1.copy(), (Expression) e2.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "leq\n");
        e1.dump(out, n + 2);
        e2.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_leq");
        e1.dump_with_types(out, n + 2);
        e2.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    public ClassTable.CoolClass.Type semant(SymbolTable symbolTable) {
        ClassTable.CoolClass.Type t1 = e1.semant0(symbolTable);
        ClassTable.CoolClass.Type t2 = e2.semant0(symbolTable);
        if (!t1.getClassName().equals(TreeConstants.Int.toString())) {
            symbolTable.getClassTable().semantError(symbolTable.getCurrentClassNode().filename,
                    this.lineNumber, "Left expression's type should be Int");
            return new ClassTable.CoolClass.Type(TreeConstants.Object_.toString());
        }
        if (!t2.getClassName().equals(TreeConstants.Int.toString())) {
            symbolTable.getClassTable().semantError(symbolTable.getCurrentClassNode().filename,
                    this.lineNumber, "Right expression's type should be Int");
            return new ClassTable.CoolClass.Type(TreeConstants.Object_.toString());
        }
        return new ClassTable.CoolClass.Type(TreeConstants.Bool.toString());
    }
}

/**
 * Defines AST constructor 'comp'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class comp extends Expression {
    protected Expression e1;

    /**
     * Creates "comp" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a1         initial value for e1
     */
    public comp(int lineNumber, Expression a1) {
        super(lineNumber);
        e1 = a1;
    }

    public TreeNode copy() {
        return new comp(lineNumber, (Expression) e1.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "comp\n");
        e1.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_comp");
        e1.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    public ClassTable.CoolClass.Type semant(SymbolTable symbolTable) {
        ClassTable.CoolClass.Type type = e1.semant0(symbolTable);
        if (!type.getClassName().equals(TreeConstants.Bool.toString())) {
            symbolTable.getClassTable().semantError(symbolTable.getCurrentClassNode().filename,
                    this.lineNumber, "Expression's type should be boolean");
            return new ClassTable.CoolClass.Type(TreeConstants.Object_.toString());
        }
        return type;
    }
}

/**
 * Defines AST constructor 'int_const'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class int_const extends Expression {
    protected AbstractSymbol token;

    /**
     * Creates "int_const" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a1         initial value for token
     */
    public int_const(int lineNumber, AbstractSymbol a1) {
        super(lineNumber);
        token = a1;
    }

    public TreeNode copy() {
        return new int_const(lineNumber, copy_AbstractSymbol(token));
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "int_const\n");
        dump_AbstractSymbol(out, n + 2, token);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_int");
        dump_AbstractSymbol(out, n + 2, token);
        dump_type(out, n);
    }

    public ClassTable.CoolClass.Type semant(SymbolTable symbolTable) {
        return new ClassTable.CoolClass.Type(TreeConstants.Int.toString());
    }

}

/**
 * Defines AST constructor 'bool_const'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class bool_const extends Expression {
    protected Boolean val;

    /**
     * Creates "bool_const" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a1         initial value for val
     */
    public bool_const(int lineNumber, Boolean a1) {
        super(lineNumber);
        val = a1;
    }

    public TreeNode copy() {
        return new bool_const(lineNumber, copy_Boolean(val));
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "bool_const\n");
        dump_Boolean(out, n + 2, val);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_bool");
        dump_Boolean(out, n + 2, val);
        dump_type(out, n);
    }

    public ClassTable.CoolClass.Type semant(SymbolTable symbolTable) {
        return new ClassTable.CoolClass.Type(TreeConstants.Bool.toString());
    }

}

/**
 * Defines AST constructor 'string_const'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class string_const extends Expression {
    protected AbstractSymbol token;

    /**
     * Creates "string_const" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a1         initial value for token
     */
    public string_const(int lineNumber, AbstractSymbol a1) {
        super(lineNumber);
        token = a1;
    }

    public TreeNode copy() {
        return new string_const(lineNumber, copy_AbstractSymbol(token));
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "string_const\n");
        dump_AbstractSymbol(out, n + 2, token);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_string");
        out.print(Utilities.pad(n + 2) + "\"");
        Utilities.printEscapedString(out, token.getString());
        out.println("\"");
        dump_type(out, n);
    }

    public ClassTable.CoolClass.Type semant(SymbolTable symbolTable) {
        return new ClassTable.CoolClass.Type(TreeConstants.Str.toString());
    }
}

/**
 * Defines AST constructor 'new_'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class new_ extends Expression {
    protected AbstractSymbol type_name;

    /**
     * Creates "new_" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a1         initial value for type_name
     */
    public new_(int lineNumber, AbstractSymbol a1) {
        super(lineNumber);
        type_name = a1;
    }

    public TreeNode copy() {
        return new new_(lineNumber, copy_AbstractSymbol(type_name));
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "new_\n");
        dump_AbstractSymbol(out, n + 2, type_name);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_new");
        dump_AbstractSymbol(out, n + 2, type_name);
        dump_type(out, n);
    }

    public ClassTable.CoolClass.Type semant(SymbolTable symbolTable) {
        // 分两种情况
        // type_name 为SELF_TYPE的情况
        if (type_name.equals(TreeConstants.SELF_TYPE)) {
            return new ClassTable.CoolClass.Type(symbolTable.getCurrentClassNode().getName().toString());
        }
        // type_name 为正常类型的情况
        else {
            return new ClassTable.CoolClass.Type(type_name.toString());
        }
    }
}

/**
 * Defines AST constructor 'isvoid'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class isvoid extends Expression {
    protected Expression e1;

    /**
     * Creates "isvoid" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a1         initial value for e1
     */
    public isvoid(int lineNumber, Expression a1) {
        super(lineNumber);
        e1 = a1;
    }

    public TreeNode copy() {
        return new isvoid(lineNumber, (Expression) e1.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "isvoid\n");
        e1.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_isvoid");
        e1.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    public ClassTable.CoolClass.Type semant(SymbolTable symbolTable) {
        // isvoid 返回类型应该是布尔类型
        e1.semant0(symbolTable);
        return new ClassTable.CoolClass.Type(TreeConstants.Bool.toString());
    }
}

/**
 * Defines AST constructor 'no_expr'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class no_expr extends Expression {
    /**
     * Creates "no_expr" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     */
    public no_expr(int lineNumber) {
        super(lineNumber);
    }

    public TreeNode copy() {
        return new no_expr(lineNumber);
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "no_expr\n");
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_no_expr");
        dump_type(out, n);
    }

}

/**
 * Defines AST constructor 'object'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class object extends Expression {
    protected AbstractSymbol name;

    /**
     * Creates "object" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a1         initial value for name
     */
    public object(int lineNumber, AbstractSymbol a1) {
        super(lineNumber);
        name = a1;
    }

    public TreeNode copy() {
        return new object(lineNumber, copy_AbstractSymbol(name));
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "object\n");
        dump_AbstractSymbol(out, n + 2, name);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_object");
        dump_AbstractSymbol(out, n + 2, name);
        dump_type(out, n);
    }

    public ClassTable.CoolClass.Type semant(SymbolTable symbolTable) {
        ClassTable.CoolClass.Type type = (ClassTable.CoolClass.Type) symbolTable.lookup(name);
        if (type == null) {
            symbolTable.getClassTable().semantError(symbolTable.getCurrentClassNode().filename,
                    this.lineNumber, "Can't find the class!");
            return new ClassTable.CoolClass.Type(TreeConstants.Object_.toString());
        }
        return type;
    }

}

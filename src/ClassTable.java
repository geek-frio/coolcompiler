import java.io.PrintStream;
import java.util.*;

/**
 * This class may be used to contain the semantic information such as the
 * inheritance graph. You may use it or not as you like: it is only here to
 * provide a container for the supplied methods.
 */
class ClassTable {
    private int semantErrors;
    private PrintStream errorStream;
    // 类名和AST节点映射关系
    private Map<String, class_c> classcMap = new HashMap<>();
    // 类名和抽象类映射关系
    private Map<String, CoolClass> coolClassMap = new HashMap();

    /**
     * Creates data structures representing basic Cool classes (Object, IO, Int,
     * Bool, String). Please note: as is this method does not do anything useful;
     * you will need to edit it to make if do what you want.
     */
    private void installBasicClasses() {
        AbstractSymbol filename = AbstractTable.stringtable.addString("<basic class>");

        // The following demonstrates how to create dummy parse trees to
        // refer to basic Cool classes. There's no need for method
        // bodies -- these are already built into the runtime system.

        // IMPORTANT: The results of the following expressions are
        // stored in local variables. You will want to do something
        // with those variables at the end of this method to make this
        // code meaningful.

        // The Object class has no parent class. Its methods are
        // cool_abort() : Object aborts the program
        // type_name() : Str returns a string representation
        // of class name
        // copy() : SELF_TYPE returns a copy of the object

        class_c Object_class = new class_c(0, TreeConstants.Object_, TreeConstants.No_class, new Features(0)
                .appendElement(
                        new method(0, TreeConstants.cool_abort, new Formals(0), TreeConstants.Object_, new no_expr(0)))
                .appendElement(
                        new method(0, TreeConstants.type_name, new Formals(0), TreeConstants.Str, new no_expr(0)))
                .appendElement(
                        new method(0, TreeConstants.copy, new Formals(0), TreeConstants.SELF_TYPE, new no_expr(0))),
                filename);
        classcMap.put(Object_class.name.toString(), Object_class);
        // The IO class inherits from Object. Its methods are
        // out_string(Str) : SELF_TYPE writes a string to the output
        // out_int(Int) : SELF_TYPE " an int " " "
        // in_string() : Str reads a string from the input
        // in_int() : Int " an int " " "

        class_c IO_class = new class_c(0, TreeConstants.IO, TreeConstants.Object_,
                new Features(0)
                        .appendElement(new method(0, TreeConstants.out_string,
                                new Formals(0).appendElement(new formalc(0, TreeConstants.arg, TreeConstants.Str)),
                                TreeConstants.SELF_TYPE, new no_expr(0)))
                        .appendElement(new method(0, TreeConstants.out_int,
                                new Formals(0).appendElement(new formalc(0, TreeConstants.arg, TreeConstants.Int)),
                                TreeConstants.SELF_TYPE, new no_expr(0)))
                        .appendElement(new method(0, TreeConstants.in_string, new Formals(0), TreeConstants.Str,
                                new no_expr(0)))
                        .appendElement(
                                new method(0, TreeConstants.in_int, new Formals(0), TreeConstants.Int, new no_expr(0))),
                filename);
        classcMap.put(IO_class.name.toString(), IO_class);
        // The Int class has no methods and only a single attribute, the
        // "val" for the integer.

        class_c Int_class = new class_c(0, TreeConstants.Int, TreeConstants.Object_,
                new Features(0).appendElement(new attr(0, TreeConstants.val, TreeConstants.prim_slot, new no_expr(0))),
                filename);
        classcMap.put(Int_class.name.toString(), Int_class);
        // Bool also has only the "val" slot.
        class_c Bool_class = new class_c(0, TreeConstants.Bool, TreeConstants.Object_,
                new Features(0).appendElement(new attr(0, TreeConstants.val, TreeConstants.prim_slot, new no_expr(0))),
                filename);
        // The class Str has a number of slots and operations:
        // val the length of the string
        // str_field the string itself
        // length() : Int returns length of the string
        // concat(arg: Str) : Str performs string concatenation
        // substr(arg: Int, arg2: Int): Str substring selection
        classcMap.put(Bool_class.name.toString(), Bool_class);

        class_c Str_class = new class_c(0, TreeConstants.Str, TreeConstants.Object_, new Features(0)
                .appendElement(new attr(0, TreeConstants.val, TreeConstants.Int, new no_expr(0)))
                .appendElement(new attr(0, TreeConstants.str_field, TreeConstants.prim_slot, new no_expr(0)))
                .appendElement(new method(0, TreeConstants.length, new Formals(0), TreeConstants.Int, new no_expr(0)))
                .appendElement(new method(0, TreeConstants.concat,
                        new Formals(0).appendElement(new formalc(0, TreeConstants.arg, TreeConstants.Str)),
                        TreeConstants.Str, new no_expr(0)))
                .appendElement(new method(0, TreeConstants.substr,
                        new Formals(0).appendElement(new formalc(0, TreeConstants.arg, TreeConstants.Int))
                                .appendElement(new formalc(0, TreeConstants.arg2, TreeConstants.Int)),
                        TreeConstants.Str, new no_expr(0))),
                filename);
        classcMap.put(Str_class.name.toString(), Str_class);
        // 将base class 加入映射关系map中
    }

    /**
     * 搜集 program 下所有的 class
     * 建立类名和AST类节点的映射关系
     *
     * @param classes
     */
    public void collectAllProgramClasses(Classes classes) {
        if (null == classes) {
            return;
        }
        Enumeration enumeration = classes.getElements();
        while (enumeration.hasMoreElements()) {
            class_c classc = (class_c) enumeration.nextElement();
            classcMap.put(classc.getName().toString(), classc);
        }
    }

    /**
     * 生成每个Class的详细结构
     * 1. 继承的所有属性值
     * 2. 继承的所有方法, 如果有重新定义的方法, 则进行覆盖
     *
     * @throws RuntimeException 不能满足某些约束的时候抛出异常
     */
    public void analyzeInheritsStructure() {
        for (Map.Entry<String, class_c> e : classcMap.entrySet()) {
            // 获取当前需要完善父类信息的类的名称
            String className = e.getKey();
            // 对于之前已经作为别的子类的父类已经被分析过了, 则不需要进行再一次的重新分析
            if(coolClassMap.get(className) != null){
                continue;
            }
            // 分析每个 Class 的属性和方法列表, 向上寻找父类, 获取属性和方法信息
            // 初始化栈数据结构, 存放每一层父类信息
            Stack<class_c> stack = new Stack<>();
            // 生成继承结构的classc的栈,从头到底为最上层的父类到类自己
            assembleInheriteClassStack(e, stack);
            // 开始进入 ClassTable.CoolClass 组装阶段
            // 初始化 attrName -> Attr 映射 attrsMap
            TreeMap<String, CoolClass.Attr> attrsMap = new TreeMap<>();
            // 初始化 methodName -> Method 映射 methodMap
            TreeMap<String, CoolClass.Method> methodsMap = new TreeMap<>();
            // "循环" pop stack 如果不为空
            class_c top = stack.pop();
            while (top != null) {
                //  获取当前正在操作的类
                String currentClassName = top.getName().toString();
                // 	循环遍历class_c的所有feature
                Enumeration enumeration = top.features.getElements();
                while (enumeration.hasMoreElements()) {
                    Feature feature = (Feature) enumeration.nextElement();
                    // 如果feature是attr的feature
                    if (feature instanceof attr) {
                        attrsAssembleOperation(attrsMap, currentClassName, feature);
                    }
                    // 如果feature是method的feature
                    else if (feature instanceof method) {
                        methodsAssembleOperation(methodsMap, currentClassName, feature);
                    } else {
                        throw new RuntimeException("illegal type!");
                    }
                }
                // 在栈从上往下不断进行Pop父类的过程中,这些父类也应该不需要再次进行分析了
                parentCoolClassGen(attrsMap, methodsMap, currentClassName);
                // 推出栈头部的class进行继续分析
                top = stack.pop();
            }
            //	使用 attrs, attrsMap, methodMap生成新的CoolClass对象
            CoolClass coolClass =  new CoolClass(new CoolClass.Type(className), attrsMap, methodsMap);
            //4. 将 ClassTable.CoolClass 对象和名称映射放入 coolClassMap中去
            coolClassMap.put(className, coolClass);
        }
    }

    private void parentCoolClassGen(TreeMap<String, CoolClass.Attr> attrsMap, TreeMap<String, CoolClass.Method> methodsMap, String currentClassName) {
        // 这里是shallow copy,共用一套key, value
        TreeMap<String, CoolClass.Attr> cloneAttrMap = (TreeMap<String, CoolClass.Attr>)attrsMap.clone();
        TreeMap<String, CoolClass.Method> cloneMethodMap = (TreeMap<String, CoolClass.Method>)methodsMap.clone();
        CoolClass coolClass =  new CoolClass(new CoolClass.Type(currentClassName), cloneAttrMap, cloneMethodMap);
        coolClassMap.put(currentClassName, coolClass);
    }

    private void methodsAssembleOperation(TreeMap<String, CoolClass.Method> methodsMap, String currentClassName, Feature feature) {
        method node = (method) feature;
        CoolClass.Method method = new CoolClass.Method();
        method.setOriginType(new CoolClass.Type(currentClassName));
        //获取方法返回类型组装(结合currentClassName)
        method.setReturnType(new CoolClass.Type(node.return_type.toString()));
        //获取组装参数(结合currentClassName)
        if(node.formals != null){
            Enumeration formalE = node.formals.getElements();
            List<CoolClass.Attr> args = new ArrayList<>();
            while(formalE.hasMoreElements()){
                formalc fc = (formalc)formalE.nextElement();
                CoolClass.Attr attr = new CoolClass.Attr();
                attr.setName(fc.name.toString());
                // 校验方法的参数的类型不能为SELF_TYPE
                if(fc.type_decl.equals(TreeConstants.SELF_TYPE)){
                    throw new RuntimeException(String.format("arg:%s, type should not be SELF_TYPE, linenum:%d", attr.getName(), fc.lineNumber));
                }
                attr.setType(new CoolClass.Type(fc.type_decl.toString()));
                args.add(attr);
            }
        }
        String methodName = node.name.toString();
        method.setMethodName((node.name.toString()));
        //查询方法映射map里面是否已经有了对应的Method, 如果有进行equals判断
        CoolClass.Method old;
        if((old = methodsMap.get(methodName)) != null){
            // 方法在子类中重新实现,但是参数或者返回类型并不与父类相匹配
            if(!old.equals(method)){
                throw new RuntimeException(String.format("method:%s redifine failed, linenum:%d", methodName, feature.lineNumber));
            }
            // IO 基础类的方法不能被重新实现
            if(old.getOriginType().className.equals(TreeConstants.IO.toString())){
                throw new RuntimeException(String.format("IO method can not be redifined, linenum:%d", feature.lineNumber));
            }
            // 子类进行方法的重新声明
            methodsMap.put(methodName, method);
        }
    }

    private void attrsAssembleOperation(TreeMap<String, CoolClass.Attr> attrsMap, String currentClassName, Feature feature) {
        attr node = (attr) feature;
        // 创建Attr对象, 添加进入attrs数组
        CoolClass.Attr attr = new CoolClass.Attr();
        String name = node.name.toString();
        String type = node.type_decl.toString();
        String originType = currentClassName;
        attr.setName(name);
        attr.setType(new CoolClass.Type(type));
        attr.setOriginType(new CoolClass.Type(originType));
        // 查看attrName -> Attr的映射关系是否已经存在,如果有,抛出 RuntimeException
        if (attrsMap.get(name) != null) {
            throw new RuntimeException(String.format("attr:%s can not be redefined, linenum:%d", node.name.toString(), feature.lineNumber));
        }
        //创建attrName -> Attr的映射关系
        attrsMap.put(name, attr);
    }

    private void assembleInheriteClassStack(Map.Entry<String, class_c> nameClassc, Stack<class_c> stack) {
        // 生成指向当前栈头部classc的指针
        class_c cls = nameClassc.getValue();
        stack.push(cls);
        // 2.1 "循环" 向上父类结构, 将父类信息放入栈结构中
        while (cls.getParent() != null) {
            String parentName = cls.getParent().toString();
            /**
             * Base class 约束校验
             * 1. Int, Str, Bool 不能被继承
             * 2. IO 可以被继承, 但是不能被重新定义方法
             */
            if(parentName.equals(TreeConstants.Int.toString())
                    || parentName.equals(TreeConstants.Str.toString())
                    || parentName.equals(TreeConstants.Bool.toString())){
                throw new RuntimeException(String.format("%s can not be inherited, linenum:%d", parentName, cls.lineNumber));
            }
            class_c parent = classcMap.get(parentName);
            // 如果找不到 class 的对应的父类, 应该抛出异常
            if (parent == null) {
                throw new RuntimeException(String.format("% class does not exists, linenum", cls.getParent(), cls.lineNumber));
            }
            // 否则,将新发现的父类加入栈,操作栈头的父类
            else {
                stack.push(parent);
                // 将指针指向栈的头部
                cls = parent;
            }
        }
        // 为最终栈头部的类默认 push 加入 Object 类的继承, 如果已经加入Object作为继承, 则不需要此项操作
        if (!cls.getName().toString().equals(TreeConstants.Object_.toString())) {
            stack.push(classcMap.get(TreeConstants.Object_.toString()));
        }
    }

    public ClassTable(Classes cls) {
        semantErrors = 0;
        errorStream = System.err;

        /* fill this in */
    }

    /**
     * Prints line number and file name of the given class.
     * <p>
     * Also increments semantic error count.
     *
     * @param c the class
     * @return a print stream to which the rest of the error message is to be
     * printed.
     */
    public PrintStream semantError(class_c c) {
        return semantError(c.getFilename(), c);
    }

    /**
     * Prints the file name and the line number of the given tree node.
     * <p>
     * Also increments semantic error count.
     *
     * @param filename the file name
     * @param t        the tree node
     * @return a print stream to which the rest of the error message is to be
     * printed.
     */
    public PrintStream semantError(AbstractSymbol filename, TreeNode t) {
        errorStream.print(filename + ":" + t.getLineNumber() + ": ");
        return semantError();
    }

    /**
     * Increments semantic error count and returns the print stream for error
     * messages.
     *
     * @return a print stream to which the error message is to be printed.
     */
    public PrintStream semantError() {
        semantErrors++;
        return errorStream;
    }

    /**
     * Returns true if there are any static semantic errors.
     */
    public boolean errors() {
        return semantErrors != 0;
    }

    static class CoolClass {
        private Type type;
        private TreeMap<String, Method> methodMap;
        private TreeMap<String, Attr> attrMap;

        public CoolClass(Type type, TreeMap<String, Attr> attrs, TreeMap<String, Method> methods) {
            this.type = type;
            this.attrMap = attrs;
            this.methodMap = methods;
        }

        public Method getM(String methodIdentifier) {
            return methodMap.get(methodIdentifier);
        }

        public Attr getGlobalO(String attrIdentifier) {
            return attrMap.get(attrIdentifier);
        }

        public Type getType() {
            return type;
        }

        public void setType(Type type) {
            this.type = type;
        }

        public TreeMap<String, Method> getMethodMap() {
            return methodMap;
        }

        public void setMethodMap(TreeMap<String, Method> methodMap) {
            this.methodMap = methodMap;
        }

        public TreeMap<String, Attr> getAttrMap() {
            return attrMap;
        }

        public void setAttrMap(TreeMap<String, Attr> attrMap) {
            this.attrMap = attrMap;
        }

        static class Type {
            private String className;

            public Type(String className) {
                this.className = className;
            }

            public boolean equals(Type obj) {
                return this.className.equals(obj.getClassName());
            }

            public String getClassName() {
                return className;
            }

            public void setClassName(String className) {
                this.className = className;
            }
        }

        static class Attr {
            private Type type;
            private String name;
			private Type originType;

            public boolean equals(Attr obj) {
                return this.name.equals(obj.getName());
            }

            public Type getType() {
                return type;
            }

            public void setType(Type type) {
                this.type = type;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

			public Type getOriginType() {
				return originType;
			}

			public void setOriginType(Type originType) {
				this.originType = originType;
			}
		}

        static class Method {
            private Type originType;
            private String methodName;
            private List<Attr> argType;
            // 包含 SELF_TYPE 的情况
            private Type returnType;

            public List<Attr> getArgType() {
                return argType;
            }

            public void setArgType(List<Attr> argType) {
                this.argType = argType;
            }

            public boolean equals(Method obj) {
                if (this.methodName.equals(obj.getMethodName())) {
                    if (this.argType.size() == obj.getArgType().size()) {
                        for (int i = 0; i < argType.size(); i++) {
                            if (!argType.get(i).equals(obj.getArgType().get(i))) {
                                return false;
                            }
                        }
                    }
                    if (!this.returnType.equals(obj.getReturnType())) {
                        return false;
                    }
                }
                return true;
            }

            public Type getOriginType() {
                return originType;
            }

            public void setOriginType(Type originType) {
                this.originType = originType;
            }

            public String getMethodName() {
                return methodName;
            }

            public void setMethodName(String methodName) {
                this.methodName = methodName;
            }

            public Type getReturnType() {
                return returnType;
            }

            public void setReturnType(Type returnType) {
                this.returnType = returnType;
            }
        }
    }
}

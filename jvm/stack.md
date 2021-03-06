# Stack

- 概述
  ```text
    Each Java Virtual Machine thread has a private Java Virtual Machine stack, created
    at the same time as the thread. A Java Virtual Machine stack stores frames (§2.6).
    A Java Virtual Machine stack is analogous to the stack of a conventional language
    such as C: it holds local variables and partial results, and plays a part in method
    invocation and return. Because the Java Virtual Machine stack is never manipulated
    directly except to push and pop frames, frames may be heap allocated. The memory
    for a Java Virtual Machine stack does not need to be contiguous.
    In the First Edition of The Java® Virtual Machine Specification, the Java Virtual Machine
    stack was known as the Java stack.
    This specification permits Java Virtual Machine stacks either to be of a fixed size
    or to dynamically expand and contract as required by the computation. If the Java
    Virtual Machine stacks are of a fixed size, the size of each Java Virtual Machine
    stack may be chosen independently when that stack is created.
    A Java Virtual Machine implementation may provide the programmer or the user control
    over the initial size of Java Virtual Machine stacks, as well as, in the case of dynamically
    expanding or contracting Java Virtual Machine stacks, control over the maximum and
    minimum sizes.
    The following exceptional conditions are associated with Java Virtual Machine
    stacks:
    • If the computation in a thread requires a larger Java Virtual Machine stack than
    is permitted, the Java Virtual Machine throws a StackOverflowError.
    • If Java Virtual Machine stacks can be dynamically expanded, and expansion is
    attempted but insufficient memory can be made available to effect the expansion,
    or if insufficient memory can be made available to create the initial Java
    THE STRUCTURE OF THE JAVA VIRTUAL MACHINE Run-Time Data Areas 2.5
    13
    Virtual Machine stack for a new thread, the Java Virtual Machine throws an
    OutOfMemoryError
    由于跨平台性的设计，java的指令都是根据栈来设计的，不同平台cpu架构不同，所以不能设计为基于寄存器的
    优点是跨平台，指令集小，编译器容易实现，缺点是性能下降，实现同样的功能需要更多的指令
  ```
    - 栈是运行是的单位，而堆是存储的单位
    - 栈解决程序的运行问题，即程序如何执行
    - 堆解决数据存储问题，即数据怎么放，放到哪
        - 局部变量，基本数据类型，引用类型的地址，在栈内
        - 引用类型都在堆空间
    - JVM stack 每个线程在创建时都会创建一个JVM stack,内部保存一个个stack frame,对应一次次的java方法调用
    - 生命周期与线程一致
    - 主管java程序的运行，保存方法的局部变量(8种基本数据类型、对象的引用地址)、部分结果。并参与方法的调用和返回
    - 栈特点
        - 栈是一种快速有效的分配存储方式，访问速度仅次于PC寄存器
        - JVM对stack的操作只有两个
            1. 每个方法执行，入栈
            2. 执行结束后，出栈
        - 栈不存在垃圾回收问题
    - 栈可能出现的异常stackoverflowError
        - JVM规范运行stack的大小是动态的或者是固定不变的
        - 固定大小的stack,每一个线程的JVM stack容量可以在线程创建的时候独立选定，如果线程请求分配的stack容量超过JVM stack允许的最大容量，JVM将抛出StackOverflowError异常
        - 动态扩展的stack，当尝试扩展的时候无法申请到足够的内存，或者在创建新的线程时没有足够的内存去创建对应的JVM stack JVM将抛出OutOfMemoryError异常
- 存储单位
    - 栈中的数据都是以stack frame的格式存在
    - 在一个线程上正在执行的每个方法都各自对应一个stack frame
        - 一个方法的执行对应一个stack frame的入栈
        - 一个方法的结束对应一个stack frame的出栈
    - stack frame是一个内存区块，是一个数据集，维系着方法执行过程中的各种数据信息
    - 在一条活动线程中，一个时间点上只会有一个活动的栈帧，即当前正在执行的方法的栈帧（栈顶栈帧）是有效的，这个栈帧成为当前栈帧（current frame）
    - 当前栈帧对应的方法是当前方法（current method）
    - 定义这个方法的类就是当前类（current class）
    - Execution Engine执行引擎运行的所有字节码指令只针对current frame进行操作
    - 如果在该方法中调用了其他方法，对应的心的stack frame会被创建出来，放在栈顶，成为新的current frame
    - JAVA 方法两种返回函数方式，栈帧出栈
        - 正常函数返回使用return指令
        - 抛出异常（未用try-catch捕获处理）
    - stack frame包含
        1. local variables 局部变量表 影响stack frame的大小
        2. operand stack 操作数栈/表达式栈 影响stack frame的大小
        3. dynamic linking（指向运行时常量池的方法引用）
        4. return address（方法正常退出或异常退出的定义）
        5. 附加信息

## local variables

- 局部变量表/局部变量数组/本地变量表
- 定义为一个数字数组主要用于存储方法参数和定义在方法体内的局部变量，数据类型包括8种基本数据类型，对象引用reference以及 return address数据类型
    - slot数组，一个slot32位，long和double64位 占两个slot
- 局部变量表建立在线程上是线程的私有数据，因此不存在数据安全问题
- local variables所需的容量大小是在编译期确定下来的，并保存在方法的Code属性的maximum local variables数据项中，在方法运行期间是不会改变局部变量表的大小的
- 方法嵌套调用的次数由stack的大小决定
    - stack越大，方法嵌套调用次数越多
    - 对一个函数来说，参数和局部变量越多，local variables越大，则其stack frame越大，函数调用会占用更多的stack空间，导致嵌套调用次数减少
    - local variables中的变量只在当前方法调用中有效
        - 方法执行时，JVM通过使用local variables来完成参数值到参数变量列表的传递。
        - 方法调用结束后，随着方法栈帧的销毁，local variables也会随之销毁
- 在stack frame中与性能调优有关的主要是local variables，
- local variables中的变量也是重要的垃圾回收根结点，只要被local variables中直接或间接引用的对象都不会被回收
- slot
    - local variables的基本存储单元，
    - 参数值的存放总是从local variables数组的index0开始，到数组.length-1索引结束
    - local variables中存放编译期可知的8种基本数据类型，对象引用reference以及 return address数据类型
    - 在local variables里32位以内的类型(byte、short、char存储前转换为int,boolean也转为int 0表示false，非0表示true)只占一个slot（包括returnAddress类型）
    - 在local variables里64位的类型（long和double）占两个slot
    - JVM会为局部变量表中的每一个slot分配一个访问索引，通过索引访问到local variables中指定的局部变量值
        - long或double类型占两个slot,使用其占用的第一个slot的索引即可
    - 当一个实例方法被调用时，方法参数和方法体内部定义的局部变量将会按照声明顺序被复制到local variables中的每一个slot
        - 如果current frame是由构造方法或者实例方法创建的，那么该对象引用this将会存放在index为0的slot处，其余参数按照参数表顺序继续排列
        - 静态方法内不存在对象引用this，其local variables内没有保存this,所以静态方法中不能使用this
    - 如果一个局部变量过了其作用域，那么在其作用域之后申明的新的局部变量就很可能会复用过期局部变量的slot,从而达到节省资源的目的
    - 基本数据类型

  |类型|字节数(Byte)|位数(bit)|取值范围|
      |:---:|:---:|:---:|:---:|
  |byte|1|8|-2^7 ~ 2^7-1|
  |short|2|16|-2^15 ~ 2^15-1|
  |int|4|32|-2^31 ~ 2^31-1|
  |long|8|64|-2^63 ~ 2^63-1|
  |boolean|1|8|true和false|
  |char|2|16|unicode编码，前128字节与ASCII兼容字符存储范围在 \u0000~\uFFFF|
  |float|4|32|3.402823e+38 ~ 1.401298e-45（e+38表示是乘以10的38次方，同样，e-45表示乘以10的负45次方）|
  |double|8|64|1.797693e+308~ 4.9000000e-324|
- 变量分类
    - 按照数据类型分
        - 基本数据类型
        - 引用数据类型
    - 按照类中声明位置分
        - 成员变量: 使用前都经历过，默认初始化赋值
            - 类变量（static修饰）：linking的prepare阶段会给类变量赋默认值---->Initialization阶段显示赋值(静态代码块赋值)
            - 实例变量：随着对象的创建，会在heap空间中分配实例变量空间并进行默认赋值
        - 局部变量：使用前必须显示赋值，否则编译不通过

## operand stack

- 概述
    - 每一个独立的stack frame中除了包含局部变量表以外，还包含一个后进先出(Last-In-First-Out/先进后出First-In-Last-Out)的操作数栈operand stack(表达式栈Expression
      stack)
    - operand stack 在方法执行过程中，根据字节码指令，往栈中写入数据或提取数据，即入栈push或出栈pop
    - 某些字节码指令将值压入operand stack，其余的字节码指令将操作数取出operand stack，使用（复制、交换、求和）后把结果压入operand stack
    - 如果被调用的方法带有返回值，其返回值将会被压入当前stack frame的operand stack中，并更新ProgramCounterRegister中下一条需要执行的字节码指令
    - operand stack中元素的数据类型必须与字节码指令的序列严格匹配，这由编译器在编译期间进行验证，同时在类加载过程中的类检验阶段的数据流分析阶段要再次验证
    - JVM的解释引擎是基于operand stack的execution engine
    - operand stack主要保存计算过程的中间结果，同时作为计算过程中变量临时的存储空间
    - operand stack是JVM execution engine的一个工作区，当一个方法刚开始执行的时候，一个新的stack frame也会随之被创建，这个方法的operand stack是空的（已创建）
    - 每一个operand stack 都会拥有一个明确的栈深度用于存储数值，其所需的最大深度在编译期就定义好了，保存在方法的Code属性中，为max_stack的值，与local variables大小
    - operand stack中任何一个元素都是任意的Java数据类型，与local variables的slot类似
        - 32bit的类型占用一个operand stack单位深度
        - 64bit的类型占用两个operand stack单位深度
    - 数组实现operand stack, 并非采用访问index的方式来进行数据访问，而是只能通过标准的入栈push和出栈pop操作来完成一次数据访问

## dynamic linking

## return address

## 附加信息

## 栈顶缓存技术
- 原因
    ```text
    JVM基于栈式架构（参考README.md:指令集架构），使用的零地址指令更加紧凑，完成一项操作的时候必然需要使用更多的入栈和出栈指令
    意味着将需要更多的指令分派（instruction dispatch）次数和内存读写次数
    由于操作数是存储在内存中，因此频繁地执行内存读写操作必然会影响执行速度，
    为了解决此问题，HotSpot JVM的设计者们提出栈顶缓存技术（ToS Top-of-Stack Cashing）
    ```
- Top-of-Stack Cashing
    ```text
    将栈顶元素全部缓存在物理cpu的寄存器中，以此降低对内存的读写次数，提升execution engine的执行效率
    （CPU寄存器:指令更少，执行速度快）
    需要在HotSpot JVM具体进行测试才能运用
    ```
## 方法调用

## 面试相关




























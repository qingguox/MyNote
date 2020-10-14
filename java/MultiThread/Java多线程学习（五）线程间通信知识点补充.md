
## Java多线程学习（五）线程间通信知识点补充

本节思维导图：
![本节思维导图](https://github.com/1367379258/BigDataEd/blob/master/java/photo/%E5%A4%9A%E7%BA%BF%E7%A8%8B%E4%BA%94%20%E7%BA%BF%E7%A8%8B%E9%97%B4%E9%80%9A%E4%BF%A1%E7%9F%A5%E8%AF%86%E7%82%B9%E8%A1%A5%E5%85%85.jpg)
   **注意：** ThreadLocal类主要解决的就是让每个线程绑定自己的值，可以将ThreadLocal类形象的比喻成存放数据的盒子，盒子中可以存储每个线程的私有数据。

我们通过之前几章的学习已经知道在线程间通信用到的**synchronized关键字、volatile关键字以及等待/通知（wait/notify）机制。**今天我们就来讲一下线程间通信的其他知识点：**管道输入/输出流、Thread.join()的使用、ThreadLocal的使用。**

### 一 管道输入/输出流
管道输入/输出流和普通文件的输入/输出流或者网络输入、输出流不同之处在于**管道输入/输出流主要用于线程之间的数据传输**，而且传输的媒介为**内存。**

管道输入/输出流主要包括下列两类的实现：

面向字节： PipedOutputStream、 PipedInputStream

面向字符: PipedWriter、 PipedReader
					注意 要 建立连接  outStream.connect(inputstream) / in.connect（out）
#### 1.1 第一个管道输入/输出流实例
完整代码：https://github.com/Snailclimb/threadDemo/tree/master/src/pipedInputOutput

writeMethod方法

    public void writeMethod(PipedOutputStream out) {
        try {
            System.out.println("write :");
            for (int i = 0; i < 300; i++) {
                String outData = "" + (i + 1);
                out.write(outData.getBytes());
                System.out.print(outData);
            }
            System.out.println();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

readMethod方法

    public void readMethod(PipedInputStream input) {
        try {
            System.out.println("read  :");
            byte[] byteArray = new byte[20];
            int readLength = input.read(byteArray);
            while (readLength != -1) {
                String newData = new String(byteArray, 0, readLength);
                System.out.print(newData);
                readLength = input.read(byteArray);
            }
            System.out.println();
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
测试方法

    public static void main(String[] args) {
        try {
            WriteData writeData = new WriteData();
            ReadData readData = new ReadData();
            PipedInputStream inputStream = new PipedInputStream();
            PipedOutputStream outputStream = new PipedOutputStream();
            // inputStream.connect(outputStream);
            outputStream.connect(inputStream);
            ThreadRead threadRead = new ThreadRead(readData, inputStream);
            threadRead.start();
            Thread.sleep(2000);
            ThreadWrite threadWrite = new ThreadWrite(writeData, outputStream);
            threadWrite.start();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
	
我们上面定义了两个方法**writeMethod和readMethod**,前者用于**写字节/字符**（取决于你用的是PipedOuputStream还是PipedWriter），
后者用于**读取字节/字符**（取决于你用的是**PipedInputStream**还是**PipedReader**）.我们定义了两个线程**threadRead**和**threadWrite** ，threadRead线程运行readMethod方法，
threadWrite运行writeMethod方法。然后 **通过outputStream.connect(inputStream)或inputStream.connect(outputStream)使两个管道流产生链接**，这样就可以将数据进行输入与输出了。

运行结果：

	read : 
	Writer:  
	123456789101112131415161718192021222324252627282930
	123456789101112131415161718192021222324252627282930

### 二 Thread.join()的使用     注意是 子线程调用join方法（） 主线程等待
在很多情况下，主线程生成并起动了子线程，如果子线程里要进行大量的耗时的运算，**主线程往往将于子线程之前结束，但是如果主线程处理完其他的事务后，需要用到子线程的处理结果，也就是主线程需要等待子线程执行完成之后再结束，这个时候就要用到join()方法了。另外，一个线程需要等待另一个线程也需要用到join()方法。**

Thread类除了提供**join()**方法之外，还提供了**join(long millis)、join(long millis, int nanos)**两个具有**超时特性**的方法。**这两个超时方法表示，如果线程thread在指定的超时时间没有终止，那么将会从该超时方法中返回。**

#### 2.1 join方法使用
不使用join方法的弊端演示：

Test.java

	public class Test {

		public static void main(String[] args) throws InterruptedException {

			MyThread threadTest = new MyThread();
			threadTest.start();

			//Thread.sleep(?);//因为不知道子线程要花的时间这里不知道填多少时间
			System.out.println("我想当threadTest对象执行完毕后我再执行");
		}
		static public class MyThread extends Thread {

			@Override
			public void run() {
				System.out.println("我想先执行");
			}
		}
	}
	
运行结果:

	我想当threadTest对象执行完毕后我再执行
	我想先执行

可以看到子线程中后被执行，这里的例子只是一个简单的演示，我们想一下：假如子线程运行的结果被主线程运行需要怎么办？ sleep方法？ 当然可以，但是子线程运行需要的时间是不确定的，所以sleep多长时间当然也就不确定了。这里就需要使用join方法解决上面的问题。

使用join方法解决上面的问题：

Test.java

	public class Test {

		public static void main(String[] args) throws InterruptedException {

			MyThread threadTest = new MyThread();
			threadTest.start();

			//Thread.sleep(?);//因为不知道子线程要花的时间这里不知道填多少时间
			threadTest.join();
			System.out.println("我想当threadTest对象执行完毕后我再执行");
		}
		static public class MyThread extends Thread {

			@Override
			public void run() {
				System.out.println("我想先执行");
			}

		}
	}

上面的代码仅仅加上了一句：threadTest.join();。在这里join方法的作用就是主线程需要等待子线程执行完成之后再结束。

#### 2.2 join(long millis)方法的使用
join(long millis)中的参数就是设定的等待时间。

JoinLongTest.java

	public class JoinLongTest {

		public static void main(String[] args) {
			try {
				MyThread threadTest = new MyThread();
				threadTest.start();

				threadTest.join(2000);// 只等2秒
				//Thread.sleep(2000);

				System.out.println("  end timer=" + System.currentTimeMillis());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		static public class MyThread extends Thread {

			@Override
			public void run() {
				try {
					System.out.println("begin Timer=" + System.currentTimeMillis());
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}
	}

运行结果：

	begin ===1577003949085
	end 主 ===1577003951085
	
不管是运行threadTest.join(2000)还是Thread.sleep(2000)， “end timer=1522036620288”语句的输出都是间隔两秒，“end timer=1522036620288”语句输出后该程序还会运行一段时间，因为线程中的run方法中有Thread.sleep(10000)语句。

另外threadTest.join(2000) 和Thread.sleep(2000) 和区别在于：**Thread.sleep(2000)不会释放锁，threadTest.join(2000)会释放锁 。**

### 三 ThreadLocal的使用
变量值的共享可以使用public static变量的形式，所有线程都使用一个public static变量。如果想**实现每一个线程都有自己的共享变量该如何解决呢？**JDK中提供的**ThreadLocal**类正是为了解决这样的问题。ThreadLocal类主要解决的就是让每个线程绑定自己的值，可以将ThreadLocal类形象的比喻成存放数据的盒子，盒子中可以存储每个线程的私有数据。

再举个简单的例子：
比如有两个人去宝屋收集宝物，这两个共用一个袋子的话肯定会产生争执，但是给他们两个人每个人分配一个袋子的话就不会出现这样的问题。如果把这两个人比作线程的话，那么ThreadLocal就是用来这两个线程竞争的。

ThreadLocal类相关方法：

方法名称		描述
get()			返回当前线程的此线程局部变量的副本中的值。
set(T value)	将当前线程的此线程局部变量的副本设置为指定的值
remove()		删除此线程局部变量的当前线程的值。
initialValue()	返回此线程局部变量的当前线程的“初始值”


#### 3.1 ThreadLocal类的初试
Test1.java

	public class Test1 {
		public static ThreadLocal<String> t1 = new ThreadLocal<String>();

		public static void main(String[] args) {
			if (t1.get() == null) {
				System.out.println("为ThreadLocal类对象放入值:aaa");
				t1.set("aaaֵ");
			}
			System.out.println(t1.get());//aaa
			System.out.println(t1.get());//aaa
		}

	}

	为threadlocal 加入值 
	aaa
	aaa

从运行结果可以看出，第一次调用ThreadLocal对象的get()方法时返回的值是null,通过调用set()方法可以为ThreadLocal对象赋值。

如果想要解决get()方法null的问题，可以使用ThreadLocal对象的initialValue方法。如下：

Test2.java

	public class Test2 {
		public static ThreadLocalExt t1 = new ThreadLocalExt();
		public static void main(String[] args) {
			if (t1.get() == null) {
				System.out.println("从未放过值");
				t1.set("我的值");
			}
			System.out.println(t1.get());
			System.out.println(t1.get());
		}
		static public class ThreadLocalExt extends ThreadLocal {
			@Override
			protected Object initialValue() {
				return "我是默认值 第一次get不再为null";
			}
		}
	}

#### 3.2 验证线程变量间的隔离性
Test3.java

	/**
	 *TODO 验证线程变量间的隔离性
	 */
	public class Test3 {

		public static void main(String[] args) {
			try {
				for (int i = 0; i < 10; i++) {
					System.out.println("       在Main线程中取值=" + Tools.tl.get());
					Thread.sleep(100);
				}
				Thread.sleep(5000);
				ThreadA a = new ThreadA();
				a.start();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		static public class Tools {
			public static ThreadLocalExt tl = new ThreadLocalExt();
		}
		static public class ThreadLocalExt extends ThreadLocal {
			@Override
			protected Object initialValue() {
				return new Date().getTime();
			}
		}

		static public class ThreadA extends Thread {

			@Override
			public void run() {
				try {
					for (int i = 0; i < 10; i++) {
						System.out.println("在ThreadA线程中取值=" + Tools.tl.get());
						Thread.sleep(100);
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}

从运行结果可以看出子线程和父线程各自拥有各自的值。

运行结果：

	主线程 ，，，执行 1577005394748
	主线程 ，，，执行 1577005394748
	主线程 ，，，执行 1577005394748
	主线程 ，，，执行 1577005394748
	主线程 ，，，执行 1577005394748
	子线程 ，，，执行 1577005400758
	子线程 ，，，执行 1577005400758
	子线程 ，，，执行 1577005400758
	子线程 ，，，执行 1577005400758
	子线程 ，，，执行 1577005400758

#### 3.3 InheritableThreadLocal
ThreadLocal类固然很好，但是子线程并不能取到父线程的ThreadLocal类的变量，InheritableThreadLocal类就是解决这个问题的。

**取父线程的值：**

修改Test3.java的内部类Tools 和ThreadLocalExt类如下：

	static public class Tools {
		public static InheritableThreadLocalExt tl = new InheritableThreadLocalExt();
	}
	static public class InheritableThreadLocalExt extends InheritableThreadLocal {
		@Override
		protected Object initialValue() {
			return new Date().getTime();
		}
	}
	
运行结果：

	主线程 ，，，执行 1577005673319
	主线程 ，，，执行 1577005673319
	主线程 ，，，执行 1577005673319
	子线程 ，，，执行 1577005673319
	子线程 ，，，执行 1577005673319
	子线程 ，，，执行 1577005673319

**取父线程的值并修改：**

修改Test3.java的内部类Tools 和InheritableThreadLocalExt类如下：

    static public class Tools {
        public static InheritableThreadLocalExt tl = new InheritableThreadLocalExt();
    }
    static public class InheritableThreadLocalExt extends InheritableThreadLocal {
        @Override
        protected Object initialValue() {
            return new Date().getTime();
        }

        @Override
        protected Object childValue(Object parentValue) {
            return parentValue + " 我在子线程加的~!";
        }
    }

运行结果：

	主线程 ，，，执行 1577005772197
	主线程 ，，，执行 1577005772197
	主线程 ，，，执行 1577005772197
	子线程 ，，，执行 1577005772197 我是子线程加上去的点 ，但是父进程的共享变量没变哦
	子线程 ，，，执行 1577005772197 我是子线程加上去的点 ，但是父进程的共享变量没变哦
	子线程 ，，，执行 1577005772197 我是子线程加上去的点 ，但是父进程的共享变量没变哦

在使用InheritableThreadLocal类需要注意的一点是：**如果子线程在取得值的同时，主线程将InheritableThreadLocal中的值进行更改，那么子线程取到的还是旧值。**
参考：

《Java多线程编程核心技术》

《Java并发编程的艺术》

如果你觉得博主的文章不错，欢迎转发点赞。你能从中学到知识就是我最大的幸运。
欢迎关注我的微信公众号：“Java面试通关手册”（分享各种Java学习资源，面试题，以及企业级Java实战项目回复关键字免费领取）。
————————————————
版权声明：本文为CSDN博主「SnailClimb在csdn」的原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/qq_34337272/article/details/79694226

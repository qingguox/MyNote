转载请备注地址：https://blog.csdn.net/qq_34337272/article/details/79670775

<font color="red">synchronized关键字加到static静态方法和synchronized(class)代码块上都是是给Class类上锁，而synchronized关键字加到非static静态方法上是给对象上锁。</font>

(2) synchronized同步语句块
本节思维导图：

![思维导图](https://github.com/1367379258/BigDataEd/blob/master/java/photo/%E5%A4%9A%E7%BA%BF%E7%A8%8B%E4%BA%8Csynchronize%E5%85%B3%E9%94%AE%E5%AD%972.jpg)

#### 一 synchronized方法的缺点
使用synchronized关键字声明方法有些时候是有很大的弊端的，比如我们有两个线程一个线程A调用同步方法后获得锁，那么另一个线程B就需要等待A执行完，但是如果说A执行的是一个很费时间的任务的话这样就会很耗时。

先来看一个暴露synchronized方法的缺点实例，然后在看看如何通过synchronized同步语句块解决这个问题。

Task.java

	public class Task {
		private String getData1;
		private String getData2;
		public synchronized void doLongTimeTask() {
			try {
				System.out.println("begin task");
				Thread.sleep(3000);
				getData1 = "长时间处理任务后从远程返回的值1 threadName="
						+ Thread.currentThread().getName();
				getData2 = "长时间处理任务后从远程返回的值2 threadName="
						+ Thread.currentThread().getName();
				System.out.println(getData1);
				System.out.println(getData2);
				System.out.println("end task");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
CommonUtils.java

	public class CommonUtils {

		public static long beginTime1;
		public static long endTime1;

		public static long beginTime2;
		public static long endTime2;
	}

MyThread1.java

	public class MyThread1 extends Thread {
		private Task task;
		public MyThread1(Task task) {
			super();
			this.task = task;
		}
		@Override
		public void run() {
			super.run();
			CommonUtils.beginTime1 = System.currentTimeMillis();
			task.doLongTimeTask();
			CommonUtils.endTime1 = System.currentTimeMillis();
		}
	}

MyThread2.java

	public class MyThread2 extends Thread {
		private Task task;
		public MyThread2(Task task) {
			super();
			this.task = task;
		}
		@Override
		public void run() {
			super.run();
			CommonUtils.beginTime2 = System.currentTimeMillis();
			task.doLongTimeTask();
			CommonUtils.endTime2 = System.currentTimeMillis();
		}
	}
Run.java
	public class Run {

		public static void main(String[] args) {
			Task task = new Task();

			MyThread1 thread1 = new MyThread1(task);
			thread1.start();

			MyThread2 thread2 = new MyThread2(task);
			thread2.start();

			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			long beginTime = CommonUtils.beginTime1;
			if (CommonUtils.beginTime2 < CommonUtils.beginTime1) {
				beginTime = CommonUtils.beginTime2;
			}

			long endTime = CommonUtils.endTime1;
			if (CommonUtils.endTime2 > CommonUtils.endTime1) {
				endTime = CommonUtils.endTime2;
			}

			System.out.println("耗时：" + ((endTime - beginTime) / 1000));
		}
	}
运行结果：               同步 进行  就是有点耗时

	begin task 
	长时间处理任务后 从远程返回的值1 threadName=Thread-0
	长时间处理任务后 从远程返回的值2 threadName=Thread-0
	end task 
	begin task 
	长时间处理任务后 从远程返回的值1 threadName=Thread-1
	长时间处理任务后 从远程返回的值2 threadName=Thread-1
	end task 
	耗时： 6

从运行时间上来看，synchronized方法的问题很明显。可以使用synchronized同步块来解决这个问题。
但是要注意synchronized同步块的使用方式，如果synchronized同步块使用不好的话并不会带来效率的提升。

#### 二 synchronized（this）同步代码块的使用
修改上例中的Task.java如下：

	public class Task {
		private String getData1;
		private String getData2;
		public void doLongTimeTask() {
			try {
				System.out.println("begin task");
				Thread.sleep(3000);

				String privateGetData1 = "长时间处理任务后从远程返回的值1 threadName="
						+ Thread.currentThread().getName();
				String privateGetData2 = "长时间处理任务后从远程返回的值2 threadName="
						+ Thread.currentThread().getName();

				synchronized (this) {
					getData1 = privateGetData1;
					getData2 = privateGetData2;
				}

				System.out.println(getData1);
				System.out.println(getData2);
				System.out.println("end task");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


运行结果：     没有对象锁了  === 是 锁住了 方法中的代码块 所以 两个线程都可以 访问 不过到sy那块阻塞。
	
	begin task 
	begin task 
	长时间处理任务后 从远程返回的值1 threadName=Thread-1
	长时间处理任务后 从远程返回的值2 threadName=Thread-0
	end task 
	长时间处理任务后 从远程返回的值1 threadName=Thread-0
	长时间处理任务后 从远程返回的值2 threadName=Thread-0
	end task 
	耗时： 3

从上面代码可以看出当一个线程访问一个对象的synchronized同步代码块时，另一个线程任然可以访问该对象非synchronized同步代码块。

时间虽然缩短了，但是大家考虑一下synchronized代码块真的是同步的吗？它真的持有当前调用对象的锁吗？

是的。不在synchronized代码块中就异步执行，在synchronized代码块中就是同步执行。

验证代码：synchronizedDemo1包下



#### 三 synchronized（object）代码块间使用
MyObject.java

	public class MyObject {
	}

Service.java

public class Service {

    public void testMethod1(MyObject object) {
			synchronized (object) {
				try {
					System.out.println("testMethod1 ____getLock time="
							+ System.currentTimeMillis() + " run ThreadName="
							+ Thread.currentThread().getName());
					Thread.sleep(2000);
					System.out.println("testMethod1 releaseLock time="
							+ System.currentTimeMillis() + " run ThreadName="
							+ Thread.currentThread().getName());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}


ThreadA.java

	public class ThreadA extends Thread {

		private Service service;
		private MyObject object;

		public ThreadA(Service service, MyObject object) {
			super();
			this.service = service;
			this.object = object;
		}

		@Override
		public void run() {
			super.run();
			service.testMethod1(object);
		}
	}


ThreadB.java

	public class ThreadB extends Thread {
		private Service service;
		private MyObject object;

		public ThreadB(Service service, MyObject object) {
			super();
			this.service = service;
			this.object = object;
		}

		@Override
		public void run() {
			super.run();
			service.testMethod1(object);
		}

	}
	
Run1_1.java

	public class Run1_1 {

		public static void main(String[] args) {
			Service service = new Service();
			MyObject object = new MyObject();

			ThreadA a = new ThreadA(service, object);
			a.setName("a");
			a.start();

			ThreadB b = new ThreadB(service, object);
			b.setName("b");
			b.start();
		}
	}

运行结果：

	TestMethod _getLock time =1576913721753Run ThreadName a
	TestMethod releaseLock time =1576913723754Run ThreadName  a
	TestMethod _getLock time =1576913723754Run ThreadName b
	TestMethod releaseLock time =1576913725755Run ThreadName  b
可以看出如下图所示，两个线程使用了同一个“对象监视器”,所以运行结果是同步的。
那么，如果使用不同的对象监视器会出现什么效果呢？

修改Run1_1.java如下

	public class Run1_2 {

		public static void main(String[] args) {
			Service service = new Service();
			MyObject object1 = new MyObject();
			MyObject object2 = new MyObject();

			ThreadA a = new ThreadA(service, object1);
			a.setName("a");
			a.start();

			ThreadB b = new ThreadB(service, object2);
			b.setName("b");
			b.start();
		}
	}

运行结果：

	TestMethod _getLock time =1576913853972Run ThreadName a
	TestMethod _getLock time =1576913853972Run ThreadName b
	TestMethod releaseLock time =1576913855973Run ThreadName  a
	TestMethod releaseLock time =1576913855973Run ThreadName  b
可以看出如下图所示，两个线程使用了不同的“对象监视器”,所以运行结果不是同步的了。


####  四 synchronized代码块间的同步性
当一个对象访问synchronized(this)代码块时，其他线程对同一个对象中所有其他synchronized(this)代码块代码块的访问将被阻塞，这说明synchronized(this)代码块使用的“对象监视器”是一个。
也就是说和synchronized方法一样，synchronized(this)代码块也是锁定当前对象的。

另外通过上面的学习我们可以得出两个结论。

	其他线程执行对象中synchronized同步方法（上一节我们介绍过，需要回顾的可以看上一节的文章）和synchronized(this)代码块时呈现同步效果;
	如果两个线程使用了同一个“对象监视器”,运行结果同步，否则不同步.
#### 五 静态同步synchronized方法与synchronized(class)代码块
synchronized关键字加到static静态方法和synchronized(class)代码块上都是是给Class类上锁，而synchronized关键字加到非static静态方法上是给对象上锁。

Service.java

	public class Service {

		public static void printA() {
			synchronized (Service.class) {
				try {
					System.out.println(
							"线程名称为：" + Thread.currentThread().getName() + "在" + System.currentTimeMillis() + "进入printA");
					Thread.sleep(3000);
					System.out.println(
							"线程名称为：" + Thread.currentThread().getName() + "在" + System.currentTimeMillis() + "离开printA");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		synchronized public static void printB() {
			System.out.println("线程名称为：" + Thread.currentThread().getName() + "在" + System.currentTimeMillis() + "进入printB");
			System.out.println("线程名称为：" + Thread.currentThread().getName() + "在" + System.currentTimeMillis() + "离开printB");
		}

		synchronized public void printC() {
			System.out.println("线程名称为：" + Thread.currentThread().getName() + "在" + System.currentTimeMillis() + "进入printC");
			System.out.println("线程名称为：" + Thread.currentThread().getName() + "在" + System.currentTimeMillis() + "离开printC");
		}

	}
	
ThreadA.java

	public class ThreadA extends Thread {
		private Service service;
		public ThreadA(Service service) {
			super();
			this.service = service;
		}
		@Override
		public void run() {
			service.printA();
		}
	}
	
ThreadB.java

	public class ThreadB extends Thread {
		private Service service;
		public ThreadB(Service service) {
			super();
			this.service = service;
		}
		
		@SuppressWarnings("static-access")
		@Override
		public void run() {
			service.printB();
		}
	}

ThreadC.java

	public class ThreadC extends Thread {
		private Service service;
		public ThreadC(Service service) {
			super();
			this.service = service;
		}
		@Override
		public void run() {
			service.printC();
		}
	}
	
Run.java

	public class Run {
		public static void main(String[] args) {
			Service service = new Service();
			ThreadA a = new ThreadA(service);
			a.setName("A");
			a.start();

			ThreadB b = new ThreadB(service);
			b.setName("B");
			b.start();

			ThreadC c = new ThreadC(service);
			c.setName("C");
			c.start();
		}
	}

运行结果：

	线程名称为: a， 在1576918032164 进入printA方法
	线程名称为: c， 在1576918032167 进入printC方法
	线程名称为: c， 在1576918032168 出printC方法
	线程名称为: a， 在1576918035165 出printA方法
	线程名称为: b， 在1576918035165 进入printB方法
	线程名称为: b， 在1576918035165 出printB方法

	从运行结果可以看出:静态同步synchronized方法与synchronized(class)代码块持有的锁一样，都是Class锁，Class锁对对象的所有实例起作用。
	synchronized关键字加到非static静态方法上持有的是对象锁。

线程A,B和线程C持有的锁不一样，所以A和B运行同步，但是和C运行不同步。@SuppressWarnings("static-access") 加了这句 b才能输出来

<font color="#FF0000">
1. 当synchronized修饰一个static方法时，多线程下，获取的是类锁（即Class本身，注意：不是实例），作用范围是整个静态方法，作用的对象是这个类的所有对象。

2. 当synchronized修饰一个非static方法时，多线程下，获取的是对象锁（即类的实例对象），作用范围是整个方法，作用对象是调用该方法的对象。
</font>

#### 六 数据类型String的常量池属性
在Jvm中具有String常量池缓存的功能

    String s1 = "a";
    String s2="a";
    System.out.println(s1==s2);//true

上面代码输出为true.这是为什么呢？

	字符串常量池中的字符串只存在一份！ 即执行完第一行代码后，常量池中已存在 “a”，那么s2不会在常量池中申请新的空间，
	而是直接把已存在的字符串内存地址返回给s2。

	因为数据类型String的常量池属性，所以synchronized(string)在使用时某些情况下会出现一些问题，比如两个线程运行
	synchronized(“abc”)｛
	｝和
	synchronized(“abc”)｛
	｝修饰的方法时，这两个线程就会持有相同的锁，导致某一时刻只有一个线程能运行。所以尽量不要使用synchronized(string)而使用synchronized(object)

>
	参考：

	《Java多线程编程核心技术》
	《Java并发编程的艺术》
	————————————————
	版权声明：本文为CSDN博主「SnailClimb在csdn」的原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接及本声明。
	原文链接：https://blog.csdn.net/qq_34337272/article/details/79670775




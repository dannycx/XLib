
# Handler

## handler是什么
-handler是更新UI界面的机制，也是消息处理的机制,我们可以发送消息，也可以处理消息
 
## 为什么要有Handler
-Android在设计的时候，封装了一套消息创建、传递、处理机制，如果不遵循这样的机制就没办法更新UI信息，就会抛出异常。
 
## handler怎么用
-1、post(Runnable);
-2、postDelayed(Runnable ,long);
-3、sentMessage
-4、sentMessageDelayed
 
## Android为什么要设置只能通过Handler机制更新UI
-最根本的问题解决多线程并发的问题；
      假设如果在一个Activity中，有多个线程去更新UI，并且都没有加锁机制，马么会产生生么样的问题？——更新界面混乱；
      如果对更新UI的操作都加锁处理的话会产生什么样子的问题？——性能下降
      对于上述问题的考虑，Android提供了一套更新UI的机制，我们只需要遵循这样的机制就好了。
      不用关心多线程的问题，更新UI的操作，都是在主线程的消息队列当中轮询处理的。
 
## handler的原理是什么
 1、handler封装消息的发送（主要包括消息发送给谁）
 2、Looper——消息封装的载体。
       （1）内部包含一个MessageQueue，所有的Handler发送的消息都走向这个消息队列；
       （2）Looper.Looper方法，就是一个死循环，不断地从MessageQueue取消息，如果有消息就处理消息，没有消息就阻塞。
 3、MessageQueue，一个消息队列，添加消息，处理消息
 4、handler内部与Looper关联，handler->Looper->MessageQueue,handler发送消息就是向MessageQueue队列发送消息。
 总结：handler负责发送消息，Looper负责接收handler发送的消息，并把消息回传给handler自己。MessageQueue存储消息的容器。
 
## HandlerThread的作用是什么
-HandlerThread thread=new HandlerThread("handler thread");自动包含等待机制，等Looper创建好了，才创建Handler，避免出现空指针异常。
 
## 主线程
* ActivityThread 默认创建main线程，main中默认创建Looper，Looper默认创建MessageQueue
* threadLocal保存线程的变量信息，方法包括：set，get
 
## Android更新UI的方式
 1、runOnUIThread
 2、handler post
 3、handler sendMessage
 4、view post
 
## 非UI线程真的不能更新UI吗
-不一定，之所以子线程不能更新界面，是因为Android在线程的方法里面采用checkThread进行判断是否是主线程，而这个方法是在ViewRootImpl中的，
-这个类是在onResume里面才生成的，因此，如果这个时候子线程在onCreate方法里面生成更新UI，而且没有做阻塞，就是耗时多的操作，还是可以更新UI的。
 
## 使用Handler遇到的问题
-比如说子线程更新UI，是因为触发了checkThread方法检查是否在主线程更新UI，还有就是子线程中没有Looper，这个原因是因为Handler的机制引起的
-因为Handler发送Message的时候，需要将Message放到MessageQueue里面，而这个时候如果没有Looper的话，就无法循环输出MessageQueue了
-这个时候就会报Looper为空的错误。
 
## 主线程怎么通知子线程
-可以利用HandlerThread进行生成一个子线程的Handler，并且实现handlerMessage方法，然后在主线程里面也生成一个Handler
-然后通过调用sendMessage方法进行通知子线程。同样，子线程里面也可以调用sendMessage方法进行通知主线程。
-这样做的好处比如有些图片的加载啊，网络的访问啊可能会比较耗时，所以放到子线程里面做是比较合适的。

Handler的使用
方式一： post(Runnable)
    创建一个工作线程，实现 Runnable 接口，实现 run 方法，处理耗时操作
    创建一个 handler，通过 handler.post/postDelay，投递创建的 Runnable，在 run 方法中进行更新 UI 操作。
    new Thread(new Runnable() {
    @Override
    public void run() {

      /**
         耗时操作
       */
     handler.post(new Runnable() {
         @Override
         public void run() {
             /**
               更新UI
              */
         }
     });

    }
    }).start();

方式二： sendMessage(Message)

    创建一个工作线程，继承 Thread，重新 run 方法，处理耗时操作
    创建一个 Message 对象，设置 what 标志及数据
    通过 sendMessage 进行投递消息

    创建一个handler，重写 handleMessage 方法，根据 msg.what 信息判断，接收对应的信息，再在这里更新 UI。

    private Handler handler = new Handler(){

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {      //判断标志位
            case 1:
                /**
                  获取数据，更新UI
                 */
                break;
        }
     }

    };

    public class WorkThread extends Thread {

    @Override

     public void run() {
       super.run();
    /**
      耗时操作
     */
       

    Message msg =Message.obtain(); //从全局池中返回一个message实例，避免多次创建message（如new Message）
    msg.obj = data;
    msg.what=1; //标志消息的标志
    handler.sendMessage(msg);
    }

    new WorkThread().start();

## Testing Asynchronous Callbacks in Kotlin

Long introduction alert!


Kotlin, kotlin, kotlin... If you are involved in the software business, whether directly or indirectly,
you've probably heard about the new kid on the block who's causing a big commotion. As an android 
engineer, I was really reluctant to adopt at first because I had no reason to adopt. I mean, JAVA worked. 
It got the job done beautifully (only with Java 8 :stuck_out_tongue: ). But then Google started to 
really promote kotlin. Their documentations began to show kotlin examples and code, the android weekly
was full kotlin articles and almost every tutorial started using kotlin for demo code. The reason as to
why Google decided to go down this path eludes me (I'm suspecting the Oracle law suites :thinking: ).


Anyway, I started porting most of my Java code to kotlin. The online community is really active, so
whenever I got stuck, there was an answered question somewhere or an article written about the problem
I was facing. Then there's the nifty conversion tool for IntellJ which came in handy from time to time.
But then the storm came. Testing!! Java has really robust and mature testing frameworks, kotlin on the 
other hand, not so much. The Java frameworks could easily test kotlin code with a **_few_** hacks here and there.
But then these hacks started consuming a lot of time, so I started looking for alternatives. Then I discovered
[Mockk](http://mockk.io/), a kotlin specific library built to handle all the difficulties of testing in kotlin.
It's a mockito equivalent for kotlin with all the kotlin goodness!

I was able to get my tests up and running in no time without all the hacks. Things were going pretty well
till I had to test a method which uses a class from Java library. This is usually easy to do with sychronous code
but with asynchronous code..... Let me just say that this article is a victory story after a week of struggles :sweat_smile:.
Let's face it, Java is here to stay for a really long time. It will take decades before most libraries are ported to kotlin, so we will
have to deal a lot of this intermingling for a couple of decades. To cut this long story short :stuck_out_tongue:, here's a snippet of code
that you can use to test integration of Java callbacks.
```

class ConsumerTest {

    @MockK(relaxUnitFun = true)
    lateinit var task: BackgroundTask

    lateinit var consumer: Consumer

    lateinit var spyConsumer: Consumer

    val callbackSlot = slot<Callback>()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        consumer = Consumer(task)
        spyConsumer = spyk(consumer, recordPrivateCalls = true)
    }

    @Test
    fun `test success`() {
        spyConsumer.doBackgroundTask()

        verify { task.doBackground(capture(callbackSlot)) }
        callbackSlot.captured.onDone(true, null)

        verifyOrder {
            spyConsumer["printStarting"]()
            spyConsumer["printSuccess"]()
        }
    }

    @Test
    fun `test failure`() {
        spyConsumer.doBackgroundTask()

        verify { task.doBackground(capture(callbackSlot)) }
        callbackSlot.captured.onDone(false, null)

        verifyOrder {
            spyConsumer["printStarting"]()
            spyConsumer["printFailed"]()
        }
    }
}

``` 

You can follow through the rest code in the repo. Feel free to ask a question by raising an issue.

Cheers!!
package e.michael.mockkcallback

import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Test

class ConsumerTest {

    @MockK(relaxUnitFun = true) // In order to avoid the strict check by Mockk, a relaxed mock is required
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
        // Handle what happens when task.doBackground... is called
        // Capture callback and trigger it with the necessary data
        every { task.doBackground(capture(callbackSlot)) } answers {
            callbackSlot.captured.onDone(true, null)
        }

        spyConsumer.doBackgroundTask()

        verifyOrder {
            spyConsumer["printStarting"]()
            spyConsumer["printSuccess"]()
        }
    }

    @Test
    fun `test failure`() {
        every { task.doBackground(capture(callbackSlot)) } answers {
            callbackSlot.captured.onDone(false, null)
        }

        spyConsumer.doBackgroundTask()

        verifyOrder {
            spyConsumer["printStarting"]()
            spyConsumer["printFailed"]()
        }
    }
}
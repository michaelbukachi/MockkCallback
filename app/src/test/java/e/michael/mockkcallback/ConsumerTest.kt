package e.michael.mockkcallback

import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Test

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
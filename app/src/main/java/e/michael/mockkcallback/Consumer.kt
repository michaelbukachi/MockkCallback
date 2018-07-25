package e.michael.mockkcallback

class Consumer(val task: BackgroundTask) {

    fun doBackgroundTask() {
        printStarting()
        task.doBackground { success, message ->
            if (success) {
                printSuccess()
            } else {
                printFailed()
            }
        }
    }

    private fun printStarting() {
        println("Starting")
    }

    private fun printSuccess() {
        println("Successful")
    }

    private fun printFailed() {
        print("Failed")
    }
}
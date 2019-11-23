package test

import kotlinx.remote.compiler.*
import java.net.ServerSocket

class SomeRemoteImplementation : SomeRemoteInterface {
    override fun doSomething() {
        println("doSomething")
    }

    override fun doSomethingElse() {
        println("doSomethingElse")
    }
}

fun main() {
    val socketServer = ServerSocket(6789)

    val connection = socketServer.acceptKTRConnection()

    val someImplementation = SomeRemoteImplementation()
    connection.registerService(SomeRemoteInterface_r, someImplementation)

    Thread.sleep(100000)
}

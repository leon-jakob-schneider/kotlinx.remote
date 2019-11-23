package test

import io.ktor.network.selector.ActorSelectorManager
import io.ktor.network.sockets.aSocket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
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

fun main() = runBlocking{
    val socketServer = aSocket(ActorSelectorManager(Dispatchers.IO)).tcp().bind("localhost", 6789)

    val connection = socketServer.acceptKTRConnection()

    val someImplementation = SomeRemoteImplementation()
    connection.registerService(SomeRemoteInterface_r, someImplementation)

    delay(100000)
}

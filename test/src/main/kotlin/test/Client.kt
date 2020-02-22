package test

import io.ktor.network.selector.ActorSelectorManager
import io.ktor.network.sockets.aSocket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.remote.compiler.RemoteFactory
import kotlinx.remote.compiler.asKTRConnection

//fun generate():Nothing = TODO()
//
//val someValue: RemoteFactory<SomeRemoteInterface> = generate()

suspend fun main() = runBlocking{
    val socket = aSocket(ActorSelectorManager(Dispatchers.IO)).tcp().connect("localhost", 6789)

    val connection = socket.asKTRConnection()

    val someRemoteImplementation = connection.getRemoteService(SomeRemoteInterface_r)

    delay(1000)
    someRemoteImplementation.doSomething()
    delay(1000)
    someRemoteImplementation.doSomethingElse()
    delay(1000)
}

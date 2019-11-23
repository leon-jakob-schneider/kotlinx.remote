package test

import kotlinx.remote.compiler.acceptKTRConnection
import kotlinx.remote.compiler.asKTRConnection
import java.net.ServerSocket
import java.net.Socket


fun main() {
    val socket = Socket("localhost", 6789)

    val connection = socket.asKTRConnection()

    val someRemoteImplementation = connection.getRemoteService(SomeRemoteInterface_r)

    Thread.sleep(1000)
    someRemoteImplementation.doSomething()
    someRemoteImplementation.doSomethingElse()
    Thread.sleep(1000)
}

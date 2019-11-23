package test

import kotlinx.remote.compiler.*

@Remote
interface SomeRemoteInterface{

    fun doSomething()
    fun doSomethingElse()
}

class ServiceRegistry{

    var mainListener: (ByteArray) -> Unit = {packet -> println(packet.joinToString { String.format("%02X", it) })}

    fun <T> getRemoteService(remoteFactory: RemoteFactory<T>):T = remoteFactory.createRemoteService(object : RemoteService {
        override var listener: (ByteArray) -> Unit = mainListener
    })
}

fun main() {
    val serviceRegistry = ServiceRegistry()
    val remote: SomeRemoteInterface = serviceRegistry.getRemoteService(SomeRemoteInterface)

    remote.doSomething()
    remote.doSomethingElse()
    remote.doSomething()
    remote.doSomething()
    println("finished")
}

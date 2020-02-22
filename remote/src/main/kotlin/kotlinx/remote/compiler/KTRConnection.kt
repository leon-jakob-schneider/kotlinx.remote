package kotlinx.remote.compiler

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.remote.compiler.packet.Action
import kotlinx.remote.compiler.packet.Call
import kotlinx.remote.compiler.packet.EmptyCall

class KTRConnection(val output: suspend (ByteArray) -> Unit) : IKTRConnection {

    val services = mutableListOf<RemoteImplementation>()

    override fun <T : Any> getRemoteService(remoteFactory: RemoteFactory<T>): T = remoteFactory.createRemoteService(object : RemoteService {
        override var listener: (Action) -> Unit = {
            GlobalScope.launch {
                when (it) {
                    is EmptyCall -> output(byteArrayOf(it.id))
                    is Call -> output(byteArrayOf(it.id) + it.payload)
                }
            }
        }
    })

    override fun <T : Any> registerService(remoteFactory: RemoteFactory<T>, t: T) {
        services.add(remoteFactory.registerRemoteService(t))
    }

    fun receiveData(data: ByteArray) {
        services.forEach {
            it.call(data[0], object : CallScope {
                override suspend fun receive(numberBytes: Int): ByteArray = TODO()
            })
        }
    }
}
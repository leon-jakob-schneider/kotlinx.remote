package kotlinx.remote.compiler

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class KTRConnection(val output: suspend (ByteArray) -> Unit) : IKTRConnection {

    val services = mutableListOf<RemoteImplementation>()

    override fun <T : Any> getRemoteService(remoteFactory: RemoteFactory<T>): T = remoteFactory.createRemoteService(object : RemoteService {
        override var listener: (ByteArray) -> Unit = { GlobalScope.launch { output(it) } }
    })

    override fun <T : Any> registerService(remoteFactory: RemoteFactory<T>, t: T) {
        services.add(remoteFactory.registerRemoteService(t))
    }

    fun receiveData(data: ByteArray) {
        services.forEach { it.receiveData(data) }
    }
}
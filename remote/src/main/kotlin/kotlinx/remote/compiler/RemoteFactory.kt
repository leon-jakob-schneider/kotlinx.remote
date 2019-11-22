package kotlinx.remote.compiler


interface RemoteFactory<T>{
    fun createRemoteService(remoteService: RemoteService):T
    fun registerRemoteService(service: T): RemoteImplementation
}
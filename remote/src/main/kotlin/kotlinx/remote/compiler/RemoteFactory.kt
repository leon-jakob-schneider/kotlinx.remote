package kotlinx.remote.compiler


interface RemoteFactory<T>{
    fun createRemoteService(remoteService: RemoteService):T = TODO()
    fun registerRemoteService(service: T): RemoteImplementation = TODO()
}
package kotlinx.remote.compiler


interface IKTRConnection {
    fun <T:Any> getRemoteService(remoteFactory: RemoteFactory<T>):T
    fun <T:Any> registerService(remoteFactory: RemoteFactory<T>, t:T)
}
package kotlinx.remote.compiler


interface IKTRConnection {
    fun <T> getRemoteService(remoteFactory: RemoteFactory<T>):T

}
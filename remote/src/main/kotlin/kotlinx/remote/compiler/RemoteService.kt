package kotlinx.remote.compiler

interface RemoteService{
    var listener: (ByteArray) -> Unit
}
package kotlinx.remote.compiler

interface RemoteImplementation{
    fun receiveData(data: ByteArray)
}
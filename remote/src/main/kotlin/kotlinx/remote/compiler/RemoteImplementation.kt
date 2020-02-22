package kotlinx.remote.compiler

interface RemoteImplementation{
    fun call(id: Byte, callScope: CallScope)
}
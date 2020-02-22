package kotlinx.remote.compiler

interface CallScope{
    suspend fun receive(numberBytes: Int):ByteArray
}
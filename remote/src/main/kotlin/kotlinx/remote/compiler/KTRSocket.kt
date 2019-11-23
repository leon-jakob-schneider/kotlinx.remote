package kotlinx.remote.compiler

import io.ktor.network.sockets.ServerSocket
import io.ktor.network.sockets.Socket
import io.ktor.network.sockets.openReadChannel
import io.ktor.network.sockets.openWriteChannel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.io.readAvailable
import kotlinx.coroutines.launch


suspend fun ServerSocket.acceptKTRConnection(): IKTRConnection{
    println("waiting for client")
    return accept().asKTRConnection()
}

suspend fun Socket.asKTRConnection(): IKTRConnection{
    println("converting socket to ktrconnection")

    val inFromClient = openReadChannel()
    val outToClient = openWriteChannel()
    val output:suspend (ByteArray) -> Unit = {
        bytes -> println("out: ${bytes.joinToString { String.format("%02X", it) }}")
        outToClient.writeFully(bytes, 0, bytes.size)
        outToClient.flush()
    }

    val ktrConnection = KTRConnection(output)

    GlobalScope.launch {
        try {
            val buffer = ByteArray(8000)
            while (true) {
                val bytesRead = inFromClient.readAvailable(buffer)
                val bytes = buffer.slice(0 until bytesRead).toByteArray()
                println("in: ${bytes.joinToString { String.format("%02X", it) }}")
                ktrConnection.receiveData(bytes)
            }
        } catch (e: Throwable) { }
    }

    return ktrConnection
}
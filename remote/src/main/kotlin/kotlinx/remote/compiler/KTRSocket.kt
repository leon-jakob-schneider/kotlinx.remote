package kotlinx.remote.compiler

import java.io.DataOutputStream
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException


fun ServerSocket.acceptKTRConnection(): IKTRConnection{
    println("waiting for client")
    return accept().asKTRConnection()
}

fun Socket.asKTRConnection(): IKTRConnection{
    println("converting socket to ktrconnection")

    val inFromClient = getInputStream()
    val outToClient = DataOutputStream(getOutputStream())
    val output:(ByteArray) -> Unit = {bytes -> println("out: ${bytes.joinToString { String.format("%02X", it) }}");outToClient.write(bytes);outToClient.flush() }

    val ktrConnection = KTRConnection(output)
    Thread{
        try {
            val buffer = ByteArray(8192)
            while (true) {
                val bytesRead = inFromClient.read(buffer)
                val bytes = buffer.slice(0 until bytesRead).toByteArray()
                println("out: ${bytes.joinToString { String.format("%02X", it) }}")
                if (bytes.isEmpty())
                    Thread.sleep(10)
                else
                    ktrConnection.receiveData(bytes)
            }
        }catch (e: SocketException){}
    }.run { isDaemon=true; start() }
    return ktrConnection
}
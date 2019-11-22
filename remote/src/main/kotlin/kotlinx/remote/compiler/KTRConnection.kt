package kotlinx.remote.compiler

import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.InputStream
import java.io.OutputStream

class KTRConnection(input: InputStream, output: OutputStream) {

    val inputStream = BufferedInputStream(input)
    val outputStream = BufferedOutputStream(output)

    var mainListener: (ByteArray) -> Unit = {packet ->
        println(packet.joinToString { String.format("%02X", it) })
        outputStream.write(packet)
        outputStream.flush()
    }

    fun <T> getRemoteService(remoteFactory: RemoteFactory<T>):T = remoteFactory.createRemoteService(object : RemoteService {
        override var listener: (ByteArray) -> Unit = mainListener
    })
}
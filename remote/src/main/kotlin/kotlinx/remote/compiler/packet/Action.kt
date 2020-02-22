package kotlinx.remote.compiler.packet
sealed class Action{
    abstract val id: Byte

}

class EmptyCall(override val id: Byte) : Action()
class Call(override val id: Byte, val payload: ByteArray) : Action()
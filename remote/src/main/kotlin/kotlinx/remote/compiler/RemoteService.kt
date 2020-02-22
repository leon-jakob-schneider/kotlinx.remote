package kotlinx.remote.compiler

import kotlinx.remote.compiler.packet.Action

interface RemoteService{
    var listener: (Action) -> Unit
}
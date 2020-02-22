package test

import kotlinx.remote.compiler.*
import kotlinx.remote.compiler.packet.*

@Remote
interface SomeRemoteInterface{
    fun doSomething()
    fun doSomethingElse()
}

val SomeRemoteInterface_r:RemoteFactory<SomeRemoteInterface> = SomeRemoteInterface
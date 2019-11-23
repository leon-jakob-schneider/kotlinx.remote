package test

import kotlinx.remote.compiler.*

@Remote
interface SomeRemoteInterface{
    fun doSomething()
    fun doSomethingElse()
}

val SomeRemoteInterface_r:RemoteFactory<SomeRemoteInterface> = SomeRemoteInterface
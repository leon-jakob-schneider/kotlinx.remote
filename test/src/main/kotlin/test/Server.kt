package test

import kotlinx.remote.compiler.*


fun main() {
    val serviceRegistry = ServiceRegistry()
    val remote: SomeRemoteInterface = serviceRegistry.getRemoteService(SomeRemoteInterface)

    remote.doSomething()
    remote.doSomethingElse()
    println("finished")
}

package kotlinx.remote.compiler

import arrow.meta.Meta
import arrow.meta.Plugin
import arrow.meta.phases.CompilerContext
import kotlin.contracts.ExperimentalContracts

open class Plugin : Meta {
    @ExperimentalContracts
    override fun intercept(ctx: CompilerContext): List<Plugin> =
        listOf(
            remote
        )
}
package kotlinx.remote.compiler

import arrow.meta.Meta
import arrow.meta.Plugin
import arrow.meta.invoke
import arrow.meta.phases.analysis.ElementScope
import arrow.meta.phases.analysis.bodySourceAsExpression
import arrow.meta.quotes.*
import arrow.meta.quotes.Scope.Companion.empty
import arrow.meta.quotes.declaration.PropertyAccessor
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.KtPropertyAccessor
import org.jetbrains.kotlin.psi.psiUtil.getAssignmentByLHS

val Meta.test: Plugin
    get() =
        "Test" {
            meta(
                    property(::isDummyProperty) { e ->
                        Transform.replace(
                                replacing = e,
                                newDeclaration = """val $name $returnType get() = SomeRemoteInterface""".property
                        )
                    }
            )
        }

fun isDummyProperty(property: KtProperty): Boolean {
    return property.bodySourceAsExpression().toString().contains("generate()")
}

fun ElementScope.mapDummy(ac: KtProperty) = Transform.replace<KtProperty>(
        replacing = ac,
        newDeclaration = """val someValue:Int get() = 1234 """.propertyAccessorGet.syntheticScope
)

//{returnType.toString()
//        .replace("RemoteFactory<", "")
//        .replace(">", "")
//        .replace(":", "")}

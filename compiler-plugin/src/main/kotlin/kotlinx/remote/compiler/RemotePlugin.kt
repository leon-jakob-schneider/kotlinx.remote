package kotlinx.remote.compiler

import arrow.meta.Meta
import arrow.meta.Plugin
import arrow.meta.invoke
import arrow.meta.plugins.higherkind.kindName
import arrow.meta.plugins.higherkind.kindTypeAliasName
import arrow.meta.quotes.Transform
import arrow.meta.quotes.classDeclaration
import org.jetbrains.kotlin.diagnostics.Diagnostic
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameSafe
import org.jetbrains.kotlin.types.KotlinType
import org.jetbrains.kotlin.types.checker.KotlinTypeChecker
import org.jetbrains.kotlin.types.getAbbreviation
import org.jetbrains.kotlin.types.typeUtil.getImmediateSuperclassNotAny


val Meta.remote: Plugin
    get() =
        "Hello World" {
            meta(
                    classDeclaration({ annotationEntries.any { text.contains("@Remote") } }) { Transform.replace(it, newDeclaration = remoteConverte(it).`class`.syntheticScope) },
                    typeChecker {
                        if (it !is RemoteFactoryAwareTypeChecker) RemoteFactoryAwareTypeChecker(it)
                        else it
                    },
                    suppressDiagnostic(Diagnostic::suppressAll)
            )
        }

fun remoteConverte(clazz: KtClass) = """ 
    interface ${clazz.name} {
        companion object : RemoteFactory<${clazz.name}>{ 
                          ${overrideRegisterRemoteService(clazz)}                                                                              
        }               ${overrideCreateRemoteService(clazz)}
        ${clazz.body?.functions?.joinToString(separator = "\n\t") { it.text }}
    }
""".trimIndent()

fun overrideCreateRemoteService(clazz: KtClass) = """
    override fun createRemoteService(remoteService: RemoteService) = object : ${clazz.name}{
        ${clazz.body?.functions?.mapIndexed(::overrideServiceFunction).merge()}
    }
    """.trimMargin()

fun overrideServiceFunction(index: Int, ktFunction: KtNamedFunction) = """
    |override ${ktFunction.text}{
    |   //remoteService.listener(EmptyCall($index))
    |   //println("${ktFunction.name}")
    |}
""".trimMargin()

fun overrideRegisterRemoteService(clazz: KtClass) = """ 
    override fun registerRemoteService(service: ${clazz.name}):RemoteImplementation = object : RemoteImplementation{
        override fun call(id: Byte, callScope: CallScope){
            when(id){
                ${clazz.body?.functions?.mapIndexed(::parseFunctionCall)?.merge()}
                else -> throw Exception()
            }
        }
    }
""".trimIndent()

fun parseFunctionCall(index: Int, ktFunction: KtNamedFunction) = """
    $index -> service.${ktFunction.name}()
""".trimMargin()

fun List<String>?.merge() = this?.joinToString(separator = "\n") ?: ""

fun Diagnostic.suppressAll(): Boolean = true

class RemoteFactoryAwareTypeChecker(val typeChecker: KotlinTypeChecker) : KotlinTypeChecker by typeChecker {

    fun KotlinType.isKindReference(other: KotlinType): Boolean {
        return arguments.isNotEmpty() &&
                other.arguments.isNotEmpty() &&
                (kindRegex().matches(other.toString()) ||
                        kindErrorRegex().matches(other.toString()) ||
                        kindAliasRegex().matches(other.toString()))
    }

    private fun KotlinType.kindAliasRegex() =
            "${constructor.declarationDescriptor?.name}Of<${arguments.joinToString(", ")}>".toRegex()

    private fun KotlinType.kindErrorRegex() =
            "Kind<\\[ERROR : For${constructor.declarationDescriptor?.name}], ${arguments.joinToString(", ")}>".toRegex()

    private fun KotlinType.kindRegex() =
            "Kind<For${constructor.declarationDescriptor?.name}, ${arguments.joinToString(", ")}>".toRegex()

    override fun isSubtypeOf(p0: KotlinType, p1: KotlinType): Boolean {
        val underlyingResult = typeChecker.isSubtypeOf(p0, p1)
        return if (!underlyingResult) {
            val subType = p0
            val superType = p1
            val isKind: Boolean = subType.isKindReference(superType) || superType.isKindReference(subType)
            val result = isKind
            //println("KindAwareTypeChecker.isSubtypeOf: $p0 <-> $p1 = $result, subtype supertypes: ${subType.supertypes()}")
            result
        } else underlyingResult
    }

    override fun equalTypes(p0: KotlinType, p1: KotlinType): Boolean {
        //println("KindAwareTypeChecker.equalTypes: $p0 <-> $p1")
        val result = typeChecker.equalTypes(p0, p1)
        //println("KindAwareTypeChecker.equalTypes: $p0 <-> $p1 = $result")
        return result
    }

    private fun KotlinType.isKind(): Boolean =
            constructor.declarationDescriptor?.fqNameSafe == kindName

    private fun KotlinType.typeAliasMatch(other: KotlinType): Boolean =
            try {
                val a = getAbbreviation()?.constructor?.declarationDescriptor?.fqNameSafe?.shortName()
                val b = other.constructor.declarationDescriptor?.fqNameSafe?.shortName()
                        ?: other.getImmediateSuperclassNotAny()?.constructor?.declarationDescriptor?.fqNameSafe?.kindTypeAliasName
                a == b || a == other.getImmediateSuperclassNotAny()?.constructor?.declarationDescriptor?.fqNameSafe?.kindTypeAliasName
            } catch (e: IllegalStateException) { //rarely shortName throws
                false
            }

}

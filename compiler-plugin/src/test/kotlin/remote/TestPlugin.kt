package remote

import arrow.meta.plugin.testing.CompilerTest
import arrow.meta.plugin.testing.assertThis
import org.junit.jupiter.api.Test

class Test {

    @Test
    fun test0() {
        val codeSnippet = """
        |@Remote
        |interface SomeRemoteInterface{
        |    fun doSomething()
        |    fun doSomethingElse()
        |}
        |   
      """
        assertThis(CompilerTest(
                config = { listOf() },
                code = { codeSnippet.source },
                assert = {
                    allOf(
                            quoteOutputMatches(
                                    """
                                        
                                    """.source
                            )
                    )
                }
        )

        )

    }
}
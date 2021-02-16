import com.iajrz.undermark.Undermark
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ParserTest {
    @Test
    fun simplestContent() {
        val simpleContent =
            "Simple content that needs not be, and should not be, modified by the parser. Except to add paragraph tags."
        val md = Undermark(simpleContent)
        assertEquals("<p>$simpleContent</p>", md.parse())
    }

    fun hn(n: Int) {
        val hPrefix = "#".repeat(n)
        val title = "Title!"
        val md = Undermark("$hPrefix$title")
        assertEquals("<h$n>$title</h$n>", md.parse())
    }

    @Test
    fun h1_through_6() {
        hn(1)
        hn(2)
        hn(3)
        hn(4)
        hn(5)
        hn(6)
    }

    @Test
    fun h7() {
        val n = 6
        val hPrefix = "#".repeat(n + 1)
        val title = "Title!"

        val md = Undermark("$hPrefix$title")
        assertEquals("<h$n>#$title</h$n>", md.parse())
    }

    @Test
    fun h1WithSpacePrefix() {
        val h1Prefix = "# "
        val title = "Title!"
        val md = Undermark("$h1Prefix$title")
        assertEquals("<h1>$title</h1>", md.parse())
    }

    @Test
    fun contentWithANumberSymbol() {
        val numberSymbol = "#"
        val firstHalf = "First Half Of The Content"
        val secondHalf = "Second Half of the Content"
        val md = Undermark("$firstHalf$numberSymbol$secondHalf")
        assertEquals(
            "<p>$firstHalf$numberSymbol$secondHalf</p>",
            md.parse()
        )
    }

    @Test
    fun paragraphThenHeaderThenParagraph() {
        val firstHalf = "First Half Of The Content"
        val newLine = '\n'
        val title = "Title!"
        val numberSymbol = '#'
        val secondHalf = "Second Half of the Content"
        val md = Undermark("$firstHalf$newLine$numberSymbol$title$newLine$secondHalf")
        assertEquals(
            "<p>$firstHalf</p><h1>$title</h1><p>$secondHalf</p>",
            md.parse()
        )
    }

    @Test
    fun unorderedList() {
        val ul = """* first element
            |* second element
            |* third element's the charm!
        """.trimMargin()
        val md = Undermark(ul)
        assertEquals(
            "<ul><li>first element</li><li>second element</li><li>third element's the charm!</li></ul>",
            md.parse()
        )
    }

    @Test
    fun unorderedListStops() {
        val content = """* First day I thought nothing would happen
* Second day, I also thought nothing would happen
* Third day, it all happened!

Yes sir, it all happened.
"""
        val md = Undermark(content)
        assertEquals(
            """<ul><li>First day I thought nothing would happen</li><li>Second day, I also thought nothing would happen</li><li>Third day, it all happened!</li></ul><p>Yes sir, it all happened.</p>""",
            md.parse()
        )
    }

    @Test
    fun unorderedListIntegratesParagraph() {
        val content = """* First day I thought nothing would happen
* Second day, I also thought nothing would happen
* Third day, it all happened!
Yes sir, it all happened.
"""
        val md = Undermark(content)
        assertEquals(
            """<ul><li>First day I thought nothing would happen</li><li>Second day, I also thought nothing would happen</li><li>Third day, it all happened!<p>Yes sir, it all happened.</p></li></ul>""",
            md.parse()
        )
    }
}
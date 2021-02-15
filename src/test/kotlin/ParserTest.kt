import com.iajrz.undermark.UnderMark
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ParserTest {
    @Test
    fun simplestContent() {
        val simpleContent =
            "Simple content that needs not be, and should not be, modified by the parser. Except to add paragraph tags."
        val md = UnderMark(simpleContent)
        assertEquals("<p>$simpleContent</p>", md.parse())
    }

    fun hn(n: Int) {
        val hPrefix = "#".repeat(n)
        val title = "Title!"
        val md = UnderMark("$hPrefix$title")
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

        val md = UnderMark("$hPrefix$title")
        assertEquals("<h$n>#$title</h$n>", md.parse())
    }

    @Test
    fun h1WithSpacePrefix() {
        val h1Prefix = "# "
        val title = "Title!"
        val md = UnderMark("$h1Prefix$title")
        assertEquals("<h1>$title</h1>", md.parse())
    }

    @Test
    fun contentWithANumberSymbol() {
        val numberSymbol = "#"
        val firstHalf = "First Half Of The Content"
        val secondHalf = "Second Half of the Content"
        val md = UnderMark("$firstHalf$numberSymbol$secondHalf")
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
        val md = UnderMark("$firstHalf$newLine$numberSymbol$title$newLine$secondHalf")
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
        val md = UnderMark(ul)
        assertEquals(
            "<ul><li>first element</li><li>second element</li><li>third element's the charm!</li></ul>",
            md.parse()
        )
    }
}
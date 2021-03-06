package com.iajrz.undermark

class Undermark(private val string: String) {

    fun parse(): String {
        val parser = NakedDocument(ParseableDoc(string))
        parser.parse()
        return parser.doc.result
    }
}

class ParseableDoc(val src: String, var cursor: Int = -1, var result: String = "") {
    fun peekNext(): Char {
        return src[cursor + 1]
    }

    fun next(): Char {
        cursor++
        return src[cursor]
    }

    fun prev(): Char {
        cursor--
        return src[cursor]
    }

    fun hasNext(): Boolean {
        return (cursor + 1) < src.length
    }
}

abstract class UndermarkParser(var doc: ParseableDoc) {
    abstract fun parse()
}

class NakedDocument(doc: ParseableDoc) : UndermarkParser(doc) {
    private var isStartOfLine = true

    override fun parse() {
        while (doc.hasNext()) {
            val currentCharacter = doc.peekNext()

            if (isStartOfLine) {
                if (currentCharacter == '#') {
                    Header(doc).parse()
                } else if (currentCharacter == '*') {
                    UnorderedList(doc).parse()
                } else if (currentCharacter != '\n') {
                    Paragraph(doc).parse()
                }
                isStartOfLine = false
                continue // every time we "parse", we should skip the rest of the current iteration!
            }

            isStartOfLine = currentCharacter == '\n'
            if (currentCharacter == '\n') {
                doc.next()
            } else {
                doc.result += doc.next()
            }
        }
    }
}

class Paragraph(doc: ParseableDoc) : UndermarkParser(doc) {
    override fun parse() {
        doc.result += "<p>"
        while (doc.hasNext() && doc.peekNext() != '\n') {
            doc.result += doc.next()
        }
        doc.result += "</p>"
    }

}

class UnorderedList(doc: ParseableDoc) : UndermarkParser(doc) {
    override fun parse() {
        doc.result += "<ul>"
        var lastWasBlank = false
        while (doc.hasNext()) {
            if (doc.peekNext() == '\n') {
                if (lastWasBlank) {
                    break
                }
                doc.next()
                lastWasBlank = true
            } else if (doc.peekNext() == '*') {
                lastWasBlank = false
                UnorderedListElement(doc).parse()
            }
        }
        doc.result += "</ul>"
    }

}

class UnorderedListElement(doc: ParseableDoc) : UndermarkParser(doc) {
    override fun parse() {
        doc.result += "<li>"
        if (doc.peekNext() == '*') doc.next()
        while (doc.peekNext() == ' ') doc.next()

        while (doc.hasNext() && doc.peekNext() != '\n') {
            doc.result += doc.next()
        }
        if (doc.hasNext() && doc.peekNext() == '\n') {
            doc.next()
            if (doc.hasNext()
                && doc.peekNext() != '\n'
                && doc.peekNext() != '*'
                && doc.peekNext() != '#') {
                Paragraph(doc).parse()
            } else {
                doc.prev()
            }
        }
        doc.result += "</li>"
    }
}

class Header(doc: ParseableDoc) : UndermarkParser(doc) {
    override fun parse() {
        var depth = 0
        while (doc.hasNext() && doc.peekNext() == '#' && depth < 6) {
            depth++
            doc.next()
        }
        doc.result += "<h${depth}>"
        while (doc.hasNext() && doc.peekNext() == ' ') {
            doc.next()
        }

        while (doc.hasNext() && doc.peekNext() != '\n') { // this should be interruptible by * and ** besides \n
            doc.result += doc.next()
        }
        doc.result += "</h${depth}>"
    }
}
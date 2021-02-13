package com.iajrz.undermark

class UnderMark(private val string: String) {

    enum class State {
        NAKED_DOCUMENT, PARAGRAPH, HEADER, HEADER_START
    }

    private var state = ArrayDeque<State>()
    private var cursor = 0

    fun parse(): String {
        var r = ""
        state.addFirst(State.NAKED_DOCUMENT)

        while (cursor < string.length) {
            val c = string[cursor]
            when (state.first()) {
                State.NAKED_DOCUMENT -> {
                    when (c) {
                        '#' -> {
                            state.addFirst(State.HEADER_START)
                        }
                        else -> {
                            r += "<p>$c"
                            state.addFirst(State.PARAGRAPH)
                        }
                    }
                }
                State.HEADER_START -> {
                    if (c != ' ') {
                        r += "<h1>$c"
                        state.removeFirst()
                        state.addFirst(State.HEADER)
                    }
                }
                State.HEADER -> {
                    when (c) {
                        '\n' -> {
                            r += "</h1>"
                            state.removeFirst()
                        }
                        else -> {
                            r += c
                        }
                    }
                }
                State.PARAGRAPH -> {
                    if (c == '\n') {
                        r += "</p>"
                        state.removeFirst()
                    } else {
                        r += c
                    }
                }
            }
            cursor++
        }
        while (state.size > 0) {
            when (state.first()) {
                State.PARAGRAPH -> {
                    r += "</p>"
                }
                State.HEADER -> {
                    r += "</h1>"
                }
                else -> {
                }
            }
            state.removeFirst()
        }
        return r
    }
}
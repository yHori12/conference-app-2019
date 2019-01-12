package io.github.droidkaigi.confsched2019.model

data class Filters(
    val rooms: Set<Room> = mutableSetOf(),
    val categories: Set<Category> = mutableSetOf(),
    val langs: Set<Lang> = mutableSetOf()
) {
    fun isPass(
        session: Session
    ): Boolean {
        if (session is Session.ServiceSession && !session.sessionType.isFilterable) return true
        val roomFilterOk = run {
            if (rooms.isEmpty()) return@run true
            return@run rooms.contains(session.room)
        }
        val categoryFilterOk = run {
            if (categories.isEmpty()) return@run true
            return@run session is Session.SpeechSession && categories.contains(session.category)
        }
        val langFilterOk = run {
            if (langs.isEmpty()) return@run true
            return@run session is Session.SpeechSession &&
                langs.map { it.text }.contains(session.language)
        }
        return roomFilterOk && categoryFilterOk && langFilterOk
    }

    fun isFiltered(): Boolean {
        return rooms.isNotEmpty() || categories.isNotEmpty() || langs.isNotEmpty()
    }
}

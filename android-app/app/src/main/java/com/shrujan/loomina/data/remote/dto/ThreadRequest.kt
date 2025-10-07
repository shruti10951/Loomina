package com.shrujan.loomina.data.remote.dto

data class ThreadRequest(
    val threadTitle: String,
    val prompt: String,
    val coverImage: String? = null,
    val genre: List<String>,
    val tags: List<String>
) {
    fun isValid(): Boolean {
        return threadTitle.trim().isNotEmpty() &&
                prompt.trim().isNotEmpty() &&
                threadTitle.length <= 100 &&
                prompt.length <= 500 &&
                genre.isNotEmpty() &&              // must select at least one genre
                tags.isNotEmpty() &&               // must add at least one tag
                genre.size <= 4 &&                 // example: limit max genres
                tags.size <= 20
    }


    fun sanitized(): ThreadRequest = copy(
        threadTitle = threadTitle.trim(),
        prompt = prompt.trim(),
        coverImage = coverImage?.trim()?.ifBlank { null },
        genre = genre.distinct(),
        tags = tags.map { it.trim() }.distinct()
    )
}

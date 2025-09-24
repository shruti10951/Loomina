package com.shrujan.loomina.data.remote.dto

data class StoryRequest(
    val storyTitle: String,
    val storySynopsis: String,
    val coverImage: String? = null,
    val genre: List<String>,
    val tags: List<String>,
) {
    fun isValid(): Boolean {
        return storyTitle.trim().isNotEmpty() &&
                storySynopsis.trim().isNotEmpty() &&
                storyTitle.length <= 100 &&
                storySynopsis.length <= 500 &&
                genre.isNotEmpty() &&              // must select at least one genre
                tags.isNotEmpty() &&               // must add at least one tag
                genre.size <= 4 &&                 // example: limit max genres
                tags.size <= 20
    }


    fun sanitized(): StoryRequest = copy(
        storyTitle = storyTitle.trim(),
        storySynopsis = storySynopsis.trim(),
        coverImage = coverImage?.trim()?.ifBlank { null },
        genre = genre.distinct(),
        tags = tags.map { it.trim() }.distinct()
    )
}

/*
 * CS349 Assignment Example Solution
 * NOTICE: It is an academic offence to share, reproduce,
 * or disseminate any lab, assignment, or exam solution unless
 * explicitly permitted by the instructor.
 */

// simple data class for a single note
package com.example.Note

data class Note(
    val id: Long?,
    var title: String = "",
    var body: String = "",
    var important: Boolean = false,
    var isSelected: Boolean = false
)
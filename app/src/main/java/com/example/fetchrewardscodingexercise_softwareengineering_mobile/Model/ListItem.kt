package com.example.fetchrewardscodingexercise_softwareengineering_mobile.Model

/**
 * Data class that represents an individual item in a list. It encapsulates the data of an item
 * and provides a structured way to access and handle the data. Makes the values from the data
 * easier to use by converting them into an object
 *
 * @property id : The unique id for an item
 * @property listId: The id of a item which identifies its group
 * @property name: the name of the item
 */
data class ListItem(
    val id: Int? = 0,
    val listId : Int? = 0,
    val name: String? = ""
)

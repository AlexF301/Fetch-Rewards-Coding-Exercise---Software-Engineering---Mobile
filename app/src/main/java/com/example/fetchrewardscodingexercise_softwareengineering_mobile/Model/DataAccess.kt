package com.example.fetchrewardscodingexercise_softwareengineering_mobile.Model

import com.google.gson.Gson
import java.net.URL

/**
 * Business logic class responsible for handling the collection and processing of data. It provides
 * methods for retrieving and filtering data from an external source
 *
 * This class is only worried about retrieving and cleaning up the data
 */
class DataAccess {

    /**
     * Gets the data in a List data structure. The List will be of type ListItem, a custom data
     * class used to map out each individual JSON item to a Kotlin Object
     *
     * @return A List of type [ListItem] representing all the items from the data that is not null
     */
    fun getDataAsItemList(): List<ListItem> {
        // Get the JSON data
        val jsonData = getJSONData()

        // Convert raw JSON data into a usable List using the [ListItem] data class for
        // a more convenient way of processing the data
        val itemsList = convertJsonDataToList(jsonData)

        // filter out items that are empty. remove any item where "name" is empty or null
        return filterOutEmptyItems(itemsList)
    }

    /**
     * Makes an HTTP request to the specified URL to collect the raw items JSON data
     *
     * @return The JSON data of items in String format
     */
    private fun getJSONData(): String {
        // Given that, that is a small dataset and this code will really only run once.
        // the extension function .readText() suffices
        return URL("https://fetch-hiring.s3.amazonaws.com/hiring.json").readText()
    }

    /**
     * Parse JSON data into List of type ListItem
     *
     * Uses the GSON library to convert each item in the JSON consisting of key value relationships
     * to our custom data class object [ListItem]
     *
     * @param jsonData: The item data in the form of a JSON string
     * @return item data as a List<ListItem>
     */
    private fun convertJsonDataToList(jsonData: String): List<ListItem> {
        return Gson().fromJson(jsonData, Array<ListItem>::class.java).asList()
    }

    /**
     * Filters out items from the list whose names are either null or blank
     *
     * @param itemsList:
     * @return List<ListItem> where there is no item where "name" was blank or null
     */
    private fun filterOutEmptyItems(itemsList: List<ListItem>): List<ListItem> {
        return itemsList.filter { item -> !item.name.isNullOrBlank() }
    }
}
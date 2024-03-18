package com.example.fetchrewardscodingexercise_softwareengineering_mobile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fetchrewardscodingexercise_softwareengineering_mobile.Model.DataAccess
import com.example.fetchrewardscodingexercise_softwareengineering_mobile.Model.ListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.UnknownHostException

/**
 * ViewModel class that supports the MainActivity. This class acts as an intermediary between the
 * MainActivity responsible for the UI and the DataAccess business logic class which gets the
 * data used to populate the UI
 */
class MainViewModel : ViewModel() {

    /**
     * When this viewmodel is initialized, retrieve the data to display on the UI
     */
    init {
        getData()
    }

    /**
     * Represents a flow of list items. This flow emits a list of [ListItem] objects.
     */
    private val _itemsList = MutableStateFlow<List<ListItem>?>(emptyList())
    val itemsList: StateFlow<List<ListItem>?> = _itemsList.asStateFlow()

    /**
     * Makes an asynchronous call to the DataAccess class to get data as a List data structure
     * of type [ListItem] which represents each item from the original JSON data. This List
     * has filtered out all items with no name
     */
    private fun getData() {
        // instantiate the DataAccess class
        val dataAccess = DataAccess()
        // launches a coroutine (asynchronous task on the view-models lifecycle)
        viewModelScope.launch {
            // Java's HTTP request needs to run away from the main thread. Dispatchers.IO is
            // optimized to perform network I/O outside of the main UI thread.
            withContext(Dispatchers.IO) {
                try {
                    // Update the mutable state flow list with the items data List. This list
                    // has filtered out all items with no name
                    _itemsList.value = dataAccess.getDataAsItemList()
                } catch (exception : UnknownHostException) {
                    // handle the scenario when user is not connected to the internet
                    _itemsList.value = emptyList()
                }
            }
        }
    }

    /**
     * Groups the items in the list by their listId key value and sorts the keys in
     * increasing order
     *
     * Also sorts the List items within their own group. Because the names match with their id
     * property, we can use the [ListItem] property 'Id' to sort the items in ascending order
     *
     * @param items: the full list of items in the data
     * @return The items grouped by their listId as a sorted Map with listId in increasing order
     */
    fun groupAndSortByListId(items : List<ListItem>?): Map<Int?, List<ListItem>>? {
        return items
            ?.sortedBy { item -> item.id } // Sort items by id in increasing order
            ?.groupBy { item -> item.listId }  // Group items by listId
            ?.toSortedMap(compareBy { groupKeyId -> groupKeyId }) // Sort the map by keys in increasing order
    }
}
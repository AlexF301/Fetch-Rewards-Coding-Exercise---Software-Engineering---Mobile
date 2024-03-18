package com.example.fetchrewardscodingexercise_softwareengineering_mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fetchrewardscodingexercise_softwareengineering_mobile.Model.ListItem
import com.example.fetchrewardscodingexercise_softwareengineering_mobile.ui.theme.FetchRewardsCodingExerciseSoftwareEngineeringMobileTheme

/**
 * The activity which hosts the UI that displays the lists of items.
 *
 * The items from the list display in groups based off their "listId" in addition to
 * the groups being sorted in ascending order. The items within each group are also sorted in
 * ascending order. The initial JSON data had items with missing names. These items were filtered
 * out from displaying.
 */
class MainActivity : ComponentActivity() {

    /**
     * An instance of MainViewModel class that holds and manages the data for the MainActivity
     */
    private val mainViewModel: MainViewModel by viewModels()

    /**
     * Called when the activity is starting. Sets up the content view of the activity
     * with the list of items data
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FetchRewardsCodingExerciseSoftwareEngineeringMobileTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // The collectAsState() function is used to collect the latest value emitted
                    // by the StateFlow and converts it into a State object, allowing Compose to
                    // observe changes to the list
                    val list = mainViewModel.itemsList.collectAsState()
                    // Displays the items list using the ItemsList composable
                    ItemsListScreen(items = list.value, modifier = Modifier.fillMaxSize())
                }
            }
        }
    }


    /**
     * The UI for the entire list of items in the data. Populates the data on a scrollable
     * vertical component
     *
     * @param items: The List of items collected from the backend
     * @param modifier: Applies layout constraints and styling to the ItemsList composable.
     */
    @Composable
    fun ItemsListScreen(
        items: List<ListItem>?,
        modifier: Modifier
    ) {
        Box(modifier) {
            // Creates the vertically scrolling list to view our items
            LazyColumn(Modifier.fillMaxWidth()) {
                // Map our items to be able to group them by their listId property
                val groupedItems = mainViewModel.groupAndSortByListId(items)

                // intItemId represents the group each item belongs to
                groupedItems?.forEach { (intItemId, items) ->
                    item {
                        // pass in the the itemId and the max width of this components parent width
                        ItemHeader(intItemId, Modifier.fillParentMaxWidth())
                    }
                    // Populates the list with the items in their group
                    items(items) { item ->
                        // pass in a singular item and the max width of this components parent width
                        ListItem(item, Modifier.fillParentMaxWidth())
                    }
                }
            }
        }
    }

    /**
     * Represents the UI for a single Item in the list
     *
     * @param item: An item to display
     * @param modifier: the width this UI component should extend to
     */
    @Composable
    fun ListItem(item: ListItem, modifier: Modifier) {
        Box(
            modifier = modifier
                .background(Color.White)
        ) {
            item.name?.let {
                Text(
                    text = "Name: $it", fontSize = 16.sp, modifier = modifier
                        .padding(10.dp)
                )
            }
        }
    }

    /**
     * The UI component for the header which separates the group of items by their listId key
     *
     * @param groupId: the group which an item belongs to. The listId key in the JSON
     * @param modifier: the width this UI component should extend to
     */
    @Composable
    fun ItemHeader(groupId: Int?, modifier: Modifier) {
        Box(
            modifier = modifier
                .background(colorResource(id = R.color.orange))
        ) {
            Text(
                text = "Group: " + groupId.toString(),
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = modifier
                    .padding(16.dp)
            )
        }
    }
}
package com.karansyd4.composelearn

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.karansyd4.composelearn.ui.theme.ComposeLearnTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.format.TextStyle

private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeLearnTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    PlaygroundList(::onclick)
//                    ComposeUIScreen(getPreviewItemState())
                }
            }
        }
    }
}

@Composable
fun PlaygroundList(onClick: (Int) -> Unit) {
    Column {
        Text(
            text = "Playground",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp)
        )

        TextButton(
            onClick = { onClick(PlaygroundItems.HETEROGENEOUS_LIST.ordinal) },
            modifier = Modifier
                .wrapContentSize()
                .padding(top = 16.dp, start = 10.dp),
        ) {
            Text(text = "Heterogeneous List Demo", textAlign = TextAlign.Start)
        }

        TextButton(
            onClick = { onClick(PlaygroundItems.HETEROGENEOUS_LIST.ordinal) },
            modifier = Modifier.padding(top = 8.dp, start = 10.dp)
        ) {
            Text(text = "Canvas Demo")
        }
    }
}

fun onclick(id: Int) {
    when (id) {
        PlaygroundItems.HETEROGENEOUS_LIST.ordinal -> {
            // navigate to Heterogeneous List Screen
        }
        PlaygroundItems.CANVAS.ordinal -> {

        }
    }
}

enum class PlaygroundItems {
    HETEROGENEOUS_LIST, CANVAS
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposeLearnTheme {
        Greeting("Android")
    }
}


@Composable
fun ComposeUIScreen(state: ItemsState) {

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    // Faking the load with 2 second delay, this state needs to come from ViewModel
    var isRefreshing by remember { mutableStateOf(false) }
    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            delay(2000)
            isRefreshing = false
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize(), scaffoldState = scaffoldState) {
        MaterialTheme {
            Column(modifier = Modifier.fillMaxHeight()) {
                Text(
                    "List in Compose",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(8.dp)
                        .wrapContentSize()
                        .semantics { heading() })
                SwipeRefresh(
                    state = rememberSwipeRefreshState(isRefreshing),
                    onRefresh = { isRefreshing = true },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    indicator = { state, trigger ->
                        GlowIndicator(
                            swipeRefreshState = state,
                            refreshTriggerDistance = trigger
                        )
                    }
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        state.itemsList.forEach { itemType ->
                            when (itemType) {
                                is ItemType.Footer -> {
                                    item {
                                        ListFooterItemContent(itemType)
                                    }
                                }
                                is ItemType.Header -> item {
                                    ListHeaderItemContent(itemType)
                                }
                                is ItemType.Item -> itemType.list.forEach {
                                    item {
                                        ListItemContent(it, scope, scaffoldState)
                                    }
                                }
                            }
                        }
                    }
                }
                LowActionButton(text = "Low Emphasis Button", onClick = ::onclick)
                Spacer(Modifier.padding(8.dp))
            }
        }
    }
}

@Composable
private fun ListItemContent(
    it: String,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState
) {
    Text(text = it, modifier = Modifier
        .clickable {
            scope.launch {
                scaffoldState.snackbarHostState.showSnackbar(
                    "Snackbar: $it",
                    duration = SnackbarDuration.Short
                )
            }
        }
        .padding(8.dp), fontSize = 16.sp)
}

@Composable
private fun ListFooterItemContent(itemType: ItemType.Footer) {
    Text(
        text = itemType.text,
        fontSize = 18.sp,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .semantics { heading() })
}

@Composable
private fun ListHeaderItemContent(itemType: ItemType.Header) {
    Text(
        text = itemType.text,
        textAlign = TextAlign.Center,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .semantics { heading() })
    Image(
        painter = painterResource(id = R.drawable.ic_launcher_background),
        alignment = Alignment.Center,
        modifier = Modifier.fillMaxWidth(), contentDescription = "Empty Box"
    )
}

sealed class ItemType {
    data class Header(val text: String) : ItemType()
    data class Item(val list: List<String>) : ItemType()
    data class Footer(val text: String) : ItemType()
}

data class ItemsState(val itemsList: List<ItemType>)

fun onclick() {
    Log.d(TAG, "onclick: Button clicked")
}

private fun getPreviewItemState() = ItemsState(
    listOf(
        ItemType.Header("List Header"),
        ItemType.Item(
            getPreviewStringList()
        ),
        ItemType.Footer("List Footer")
    )
)

@Preview(showSystemUi = true)
@Composable
fun PreviewComposeUIScreen() {
    ComposeUIScreen(getPreviewItemState())
}

private fun getPreviewStringList() = listOf(
    "Item1",
    "Item2",
    "Item3",
    "Item4",
    "Item5",
    "Item6",
    "Item7",
    "Item8",
    "Item9",
    "Item10",
    "Item11",
    "Item12",
    "Item13",
    "Item14",
    "Item15",
    "Item16"
)

/*
* A custom indicator which displays a glow and progress indicator
*/
@Composable
fun GlowIndicator(
    swipeRefreshState: SwipeRefreshState,
    refreshTriggerDistance: Dp,
    color: Color = MaterialTheme.colors.primary,
) {
    Box(
        Modifier
            .drawWithCache {
                onDrawBehind {
                    val distance = refreshTriggerDistance.toPx()
                    val progress = (swipeRefreshState.indicatorOffset / distance).coerceIn(0f, 1f)
                    // We draw a translucent glow
                    val brush = Brush.verticalGradient(
                        0f to color.copy(alpha = 0.45f),
                        1f to color.copy(alpha = 0f)
                    )
                    // And fade the glow in/out based on the swipe progress
                    drawRect(brush = brush, alpha = FastOutSlowInEasing.transform(progress))
                }
            }
            .fillMaxWidth()
            .height(72.dp)
    ) {
        if (swipeRefreshState.isRefreshing) {
            // If we're refreshing, show an indeterminate progress indicator
            LinearProgressIndicator(Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun LowActionButton(text: String, onClick: () -> Unit) {

    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val isPressed by interactionSource.collectIsPressedAsState()
    val buttonText = remember { text }

    TextButton(
        onClick = onClick,
        modifier = Modifier
            .wrapContentSize()
            .defaultMinSize(48.dp)
            .padding(top = 16.dp, bottom = 16.dp, start = 4.dp, end = 4.dp),
    ) {
        Text(
            text = buttonText,
            color = MaterialTheme.colors.primary,
            textAlign = TextAlign.Start,
            fontSize = 16.sp,
            lineHeight = 1.sp,
            modifier = when {
                isFocused -> Modifier.border(
                    width = 2.dp,
                    color = Color.Black,
                    shape = RectangleShape
                )
                isPressed -> Modifier
                else -> Modifier
            }
        )
    }
}

// Preview Methods below

@Preview(showBackground = true)
@Composable
fun PreviewActionButton() {
    MaterialTheme {
        LowActionButton(text = "Low Emphasis Action Button") {}
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewPlaygroundScreen() {
    PlaygroundList {}
}

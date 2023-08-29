package com.alpenraum.shimstack.ui.main.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alpenraum.shimstack.R
import com.alpenraum.shimstack.data.bike.Bike
import com.alpenraum.shimstack.data.bike.Tire
import com.alpenraum.shimstack.data.cardsetup.CardSetup
import com.alpenraum.shimstack.data.cardsetup.CardType
import com.alpenraum.shimstack.ui.base.use
import com.alpenraum.shimstack.ui.compose.AttachToLifeCycle
import com.alpenraum.shimstack.ui.compose.CARD_DIMENSION
import com.alpenraum.shimstack.ui.compose.VerticalDivider
import com.alpenraum.shimstack.ui.compose.shimstackRoundedCornerShape
import com.alpenraum.shimstack.ui.theme.AppTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.fade
import com.google.accompanist.placeholder.material.placeholder
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier,
    viewModel: HomeScreenViewModel = hiltViewModel(),
    windowSizeClass: WindowSizeClass
) {
    AttachToLifeCycle(viewModel = viewModel)
    val (state, intents, event) = use(viewModel = viewModel)
    val isLoading = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(0)
    val gridState = rememberLazyGridState()
    val snackState = remember { SnackbarHostState() }
    LaunchedEffect(event.collectAsState(HomeScreenContract.Event.Loading)) {
        event.collectLatest {
            when (it) {
                HomeScreenContract.Event.Error -> scope.launch { snackState.showSnackbar("ERROR") }
                HomeScreenContract.Event.FinishedLoading -> scope.launch {
                    isLoading.value = false
                }

                HomeScreenContract.Event.Loading -> scope.launch {
                    isLoading.value = true
                }

                HomeScreenContract.Event.NewPageSelected -> scope.launch {
                }
            }
        }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top
    ) {
        BikePager(
            modifier = Modifier
                .padding(top = 32.dp, bottom = 16.dp),
            showPlaceholder = isLoading.value,
            state = state,
            intents = intents,
            pagerState = pagerState
        )
        state.getBike(pagerState.currentPage)?.let {
            BikeDetails(bike = it, cardSetup = state.detailCardsSetup, intents, gridState)
        }
        SnackbarHost(hostState = snackState)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BikeDetails(
    bike: Bike,
    cardSetup: List<CardSetup>,
    intents: (HomeScreenContract.Intent) -> Unit,
    state: LazyGridState
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        cardSetup.forEach {
            when (it.type) {
                CardType.TIRES -> TireDetails(bigCard = it.bigCard, bike = bike)
                else -> TireDetails(bigCard = false, bike = bike)
                // CardType.FORK ->{} // TODO
                // CardType.FORK_DETAILED ->{} // TODO
                // CardType.SHOCK ->{} // TODO
                // CardType.SHOCK_DETAILED ->{} // TODO
            }
        }
    }
    // LazyVerticalGrid(
    //     state = state,
    //     columns = GridCells.Fixed(2),
    //     verticalArrangement = Arrangement.spacedBy(16.dp),
    //     horizontalArrangement = Arrangement.spacedBy(16.dp),
    //     modifier = Modifier.padding(horizontal = 16.dp)
    // ) {
    //     cardSetup.forEach {
    //         val gridSpan = if (it.bigCard) 2 else 1
    //         item(span = { GridItemSpan(gridSpan) }) {
    //             when (it.type) {
    //                 CardType.TIRES -> TireDetails(bigCard = it.bigCard, bike = bike)
    //                 else -> TireDetails(bigCard = false, bike = bike)
    //                 // CardType.FORK ->{} // TODO
    //                 // CardType.FORK_DETAILED ->{} // TODO
    //                 // CardType.SHOCK ->{} // TODO
    //                 // CardType.SHOCK_DETAILED ->{} // TODO
    //             }
    //         }
    //     }
    // }
}

@Composable
private fun TireDetails(bigCard: Boolean, bike: Bike) {
    DetailsCard(title = R.string.tire, bigCard) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp).padding(top = 16.dp).weight(1.0f),
            horizontalArrangement = Arrangement.Center
        ) {
            TireDetailsTextPair(heading = R.string.front, data = bike.frontTire, bigCard = bigCard)
            VerticalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            TireDetailsTextPair(heading = R.string.rear, data = bike.rearTire, bigCard = bigCard)
        }
    }
}

@Composable
private fun TireDetailsTextPair(@StringRes heading: Int, data: Tire, bigCard: Boolean) =
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(id = heading),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.outline
            ),
            modifier = Modifier.padding(bottom = 8.dp),
            textAlign = TextAlign.Center
        )
        if (bigCard) {
            // TODO
        } else {
            Text(
                text = data.getFormattedPressure(LocalContext.current),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }

@Composable
private fun DetailsCard(
    @StringRes title: Int,
    bigCard: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable() (ColumnScope.() -> Unit)
) {
    Card(modifier = modifier.height(CARD_DIMENSION).aspectRatio(if (bigCard) 2.0f else 1.0f)) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier.fillMaxWidth()
        ) {
            content()
            Text(
                text = stringResource(id = title),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp, top = 4.dp)
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun BikePager(
    modifier: Modifier,
    state: HomeScreenContract.State,
    intents: (HomeScreenContract.Intent) -> Unit,
    showPlaceholder: Boolean,
    pagerState: PagerState
) {
    val itemWidth = 200.dp

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.distinctUntilChanged().collect { page ->
            intents(HomeScreenContract.Intent.OnViewPagerSelectionChanged(page))
        }
    }
    Column(modifier = modifier) {
        HorizontalPager(
            state.bikes.size,
            modifier = Modifier,
            pagerState,
            contentPadding = PaddingValues(horizontal = itemWidth / 2 + 20.dp / 2),
            verticalAlignment = Alignment.Top,
            userScrollEnabled = !showPlaceholder
        ) { page ->
            BikeCard(
                modifier = Modifier.width(itemWidth)
                    .graphicsLayer {
                        val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue
                        lerp(
                            start = 0.8f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        ).also { scale ->
                            this.scaleX = scale
                            this.scaleY = scale
                        }
                    }
                    .width(itemWidth),
                bike = state.bikes[page],
                showPlaceholder = showPlaceholder
            )
        }
        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        )
    }
}

@Composable
private fun BikeCard(modifier: Modifier, bike: Bike?, showPlaceholder: Boolean) {
    Surface(
        modifier = modifier
            .height(240.dp)
            .placeholder(
                visible = showPlaceholder,
                highlight = PlaceholderHighlight.fade(),
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = shimstackRoundedCornerShape()
            ),
        shape = shimstackRoundedCornerShape(),
        tonalElevation = 10.dp
    ) {
        Text(
            bike?.name ?: "",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showSystemUi = true)
@Composable
fun Preview() {
    AppTheme {
        HomeScreen(
            modifier = Modifier,
            viewModel = HomeScreenViewModel(),
            WindowSizeClass.calculateFromSize(DpSize(400.dp, 400.dp))
        )
    }
}

@Preview()
@Composable
fun Preview2() {
    AppTheme {
        BikeCard(
            modifier = Modifier.width(200.dp),
            Bike(
                name = "5010",
                type = Bike.Type.TRAIL,
                frontTire = Tire(),
                rearTire = Tire(),
                isEBike = false
            ),
            showPlaceholder = false
        )
    }
}

@Preview()
@Composable
fun PreviewTireData() {
    AppTheme {
        TireDetails(bigCard = false, Bike.empty())
    }
}

@Preview()
@Composable
fun PreviewTireDataBig() {
    AppTheme {
        TireDetails(bigCard = true, Bike.empty())
    }
}

package com.alpenraum.shimstack.home.overview

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.invisibleToUser
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.alpenraum.shimstack.home.R
import com.alpenraum.shimstack.model.bike.Bike
import com.alpenraum.shimstack.model.cardsetup.CardSetup
import com.alpenraum.shimstack.model.cardsetup.CardType
import com.alpenraum.shimstack.ui.base.use
import com.alpenraum.shimstack.ui.compose.AttachToLifeCycle
import com.alpenraum.shimstack.ui.compose.CARD_MARGIN
import com.alpenraum.shimstack.ui.compose.compositionlocal.LocalWindowSizeClass
import com.alpenraum.shimstack.ui.compose.shimstackRoundedCornerShape
import com.alpenraum.shimstack.ui.theme.AppTheme
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.fade
import com.google.accompanist.placeholder.material.placeholder
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.max

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.initVm()
    }
    AttachToLifeCycle(viewModel = viewModel)
    val (state, intents, event) = use(viewModel = viewModel, navController)
    HomeScreenContent(
        state = state,
        event = event,
        intents = intents
    )
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun HomeScreenContent(
    state: HomeScreenContract.State,
    event: SharedFlow<HomeScreenContract.Event>,
    intents: (HomeScreenContract.Intent) -> Unit
) {
    val isLoading = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val pagerState =
        androidx.compose.foundation.pager
            .rememberPagerState(initialPage = 0) { state.bikes.size + 1 }
    val lastPagerPosition = remember { mutableIntStateOf(0) }
    val snackState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        event.collectLatest {
            when (it) {
                HomeScreenContract.Event.Error -> scope.launch { snackState.showSnackbar("ERROR") }
                HomeScreenContract.Event.FinishedLoading -> isLoading.value = false
                HomeScreenContract.Event.Loading -> isLoading.value = true
                HomeScreenContract.Event.NewPageSelected -> {}
            }
        }
    }
    val windowSizeClass = LocalWindowSizeClass.current
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BikePager(
            modifier =
                Modifier
                    .padding(top = 32.dp, bottom = 16.dp),
            showPlaceholder = isLoading.value,
            state = state,
            intents = intents,
            pagerState = pagerState
        )

        AnimatedContent(
            state.getBike(pagerState.currentPage),
            modifier = Modifier.fillMaxHeight(),
            label = "BikeDetails",
            transitionSpec = {
                if (lastPagerPosition.intValue > pagerState.currentPage) {
                    // scroll to left
                    (slideInHorizontally { x -> -x } + fadeIn()).togetherWith(
                        slideOutHorizontally { x -> x } + fadeOut()
                    )
                } else {
                    (slideInHorizontally { x -> x } + fadeIn()).togetherWith(
                        slideOutHorizontally { x -> -x } + fadeOut()
                    )
                }
            }
        ) { bike ->
            SideEffect {
                lastPagerPosition.intValue = pagerState.currentPage
            }
            bike?.let { bike1 ->
                Spacer(modifier = Modifier.height(16.dp))
                BikeDetails(
                    bike = bike1,
                    cardSetup = state.detailCardsSetup,
                    intents
                )
            } ?: EmptyDetailsEyeCandy()
        }
    }
    SnackbarHost(hostState = snackState)
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun EmptyDetailsEyeCandy() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxHeight()
    ) {
        Image(
            painter = painterResource(id = R.drawable.il_empty_mountain),
            contentDescription = null,
            modifier =
                Modifier
                    .semantics {
                        invisibleToUser()
                    }.fillMaxSize(0.6f)
                    .padding(bottom = 8.dp)
        )
        Text(
            text = stringResource(id = R.string.copy_add_new_bike),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.secondary,
            fontStyle = FontStyle.Italic
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BikeDetails(
    bike: Bike,
    cardSetup: ImmutableList<CardSetup>,
    intents: (HomeScreenContract.Intent) -> Unit
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(
                    rememberScrollState()
                ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(CARD_MARGIN, alignment = Alignment.Start)
        ) {
            cardSetup.forEach {
                when (it.type) {
                    CardType.TIRES -> TireDetails(bigCard = it.bigCard, bike = bike)
                    CardType.FORK -> ForkDetails(bigCard = it.bigCard, bike = bike)
                    CardType.SHOCK -> ShockDetails(bigCard = it.bigCard, bike = bike)
                }
            }
        }
        HorizontalDivider(
            modifier =
                Modifier
                    .padding(vertical = 8.dp)
                    .clip(RoundedCornerShape(100)),
            thickness = 2.dp
        )

        Button(
            onClick = {} // TODO: editable detail cards config
        ) {
            Text("Edit detail cards")
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun BikePager(
    modifier: Modifier = Modifier,
    state: HomeScreenContract.State,
    intents: (HomeScreenContract.Intent) -> Unit,
    showPlaceholder: Boolean,
    pagerState: PagerState
) {
    val itemSize = 200.dp

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.distinctUntilChanged().collect {
            intents(HomeScreenContract.Intent.OnViewPagerSelectionChanged)
        }
    }
    val isLandscapeScreen =
        LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    Column(modifier = modifier) {
        HorizontalPager(
            pagerState,
            modifier = Modifier,
            contentPadding =
                PaddingValues(
                    horizontal = calculatePagerItemPadding(itemWidth = itemSize)
                ),
            verticalAlignment = Alignment.Top,
            userScrollEnabled = !showPlaceholder
        ) { page ->
            val bike = state.bikes.getOrNull(page)

            val content: @Composable (modifier: Modifier) -> Unit =
                if (page == state.bikes.size) {
                    { AddNewBikeCardContent(intents = intents) }
                } else {
                    { BikeCardContent(bike = bike, it) }
                }

            BikeCard(
                modifier =
                    Modifier
                        .size(itemSize)
                        .graphicsLayer {
                            val pageOffset = ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue
                            lerp(
                                start = 0.8f,
                                stop = 1f,
                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                            ).also { scale ->
                                this.scaleX = scale
                                this.scaleY = scale
                            }
                        }.clickable {
                            bike?.let {
                                intents(
                                    HomeScreenContract.Intent.OnBikeDetailsClicked(it)
                                )
                            }
                        },
                showPlaceholder = showPlaceholder,
                content = content
            )
        }
        HorizontalPagerIndicator(
            pagerState = pagerState,
            pageCount = pagerState.pageCount,
            modifier =
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp),
            activeColor = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun calculatePagerItemPadding(itemWidth: Dp) =
    max(
        ((LocalConfiguration.current.screenWidthDp - itemWidth.value) / 2.0f),
        0.0f
    ).dp

@Composable
fun BikeCard(
    modifier: Modifier = Modifier,
    showPlaceholder: Boolean,
    content: @Composable (modifier: Modifier) -> Unit
) {
    Surface(
        modifier =
            modifier
                .placeholder(
                    visible = showPlaceholder,
                    highlight = PlaceholderHighlight.fade(),
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = shimstackRoundedCornerShape()
                ),
        shape = shimstackRoundedCornerShape(),
        tonalElevation = 10.dp,
        color = MaterialTheme.colorScheme.secondaryContainer
    ) {
        content(Modifier.padding(8.dp))
    }
}

@Composable
fun BikeCardContent(
    bike: Bike?,
    modifier: Modifier = Modifier
) {
    Text(
        bike?.name ?: "",
        style = MaterialTheme.typography.bodyMedium,
        modifier = modifier
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun AddNewBikeCardContent(
    modifier: Modifier = Modifier,
    intents: (HomeScreenContract.Intent) -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier =
            modifier
                .fillMaxSize()
                .clickable {
                    intents(HomeScreenContract.Intent.OnAddNewBike)
                }.semantics(mergeDescendants = true) {}
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Rounded.Add,
                contentDescription = "",
                modifier =
                    Modifier
                        .size(100.dp)
                        .semantics { invisibleToUser() },
                tint = MaterialTheme.colorScheme.surfaceTint.copy(alpha = 0.8f)
            )
            Text(
                text = stringResource(id = R.string.fab_add_bike),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    AppTheme {
        HomeScreenContent(
            state = HomeScreenContract.State(persistentListOf(), persistentListOf()),
            event = MutableSharedFlow(),
            intents = {}
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun PreviewData() {
    AppTheme {
        HomeScreenContent(
            state =
                HomeScreenContract.State(
                    persistentListOf(Bike.empty()),
                    CardSetup.defaultConfig()
                ),
            event = MutableSharedFlow(),
            intents = {}
        )
    }
}
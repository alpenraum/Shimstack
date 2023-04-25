package com.alpenraum.shimstack.ui.main.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.lifecycle.LifecycleOwner
import com.alpenraum.shimstack.data.bike.Bike
import com.alpenraum.shimstack.data.bike.Tire
import com.alpenraum.shimstack.ui.base.BaseViewModel
import com.alpenraum.shimstack.ui.compose.shimstackRoundedCornerShape
import com.alpenraum.shimstack.ui.theme.AppTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.fade
import com.google.accompanist.placeholder.material.placeholder
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.math.absoluteValue
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(modifier: Modifier, viewModel: HomeScreenViewModel? = null) {
    val isLoading = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val snackState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel?.events()?.collectAsState(HomeScreenViewModel.Events.Loading)) {
        viewModel?.events()?.collectLatest {
            when (it) {
                HomeScreenViewModel.Events.Error -> scope.launch { snackState.showSnackbar("ERROR") }
                HomeScreenViewModel.Events.FinishedLoading -> scope.launch {
                    snackState.showSnackbar(
                        "FINISHED LOADING"
                    )
                }
                HomeScreenViewModel.Events.Loading -> scope.launch {
                    snackState.showSnackbar(
                        "LOADING"
                    )
                }
                HomeScreenViewModel.Events.NewPageSelected -> scope.launch {
                    snackState.showSnackbar(
                        "New Bike selected!"
                    )
                }
            }
        }
    }

    Column(modifier = modifier) {
        BikePager(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, bottom = 16.dp),
            viewModel = viewModel,
            showPlaceholder = isLoading.value
        )
        SnackbarHost(hostState = snackState)
    }
}

@Preview(showSystemUi = true)
@Composable
fun Preview() {
    AppTheme {
        HomeScreen(modifier = Modifier, viewModel = HomeScreenViewModel())
    }
}

@Preview(showBackground = true)
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

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun BikePager(modifier: Modifier, viewModel: HomeScreenViewModel?, showPlaceholder: Boolean) {
    val pagerState = rememberPagerState(0)
    val data = viewModel?.data()?.collectAsState() ?: return
    val itemWidth = 200.dp

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.distinctUntilChanged().collect { page ->
            viewModel.onViewPagerSelectionChanged(page)
        }
    }
    Column(modifier = modifier) {
        HorizontalPager(
            data.value.size,
            modifier = Modifier,
            pagerState,
            contentPadding = PaddingValues(horizontal = itemWidth / 2 + 20.dp / 2),
            verticalAlignment = Alignment.Top
        ) { page ->
            BikeCard(
                modifier = Modifier
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
                bike = data.value[page],
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
private fun BikeCard(modifier: Modifier, bike: Bike, showPlaceholder: Boolean) {
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
            bike.name,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@HiltViewModel
class HomeScreenViewModel @Inject constructor() :
    BaseViewModel() {
    sealed class Events {
        object Loading : Events()
        object FinishedLoading : Events()
        object Error : Events()
        object NewPageSelected : Events()
    }

    private val _events = MutableSharedFlow<Events>()
    fun events() = _events.asSharedFlow()
    fun data() = _bikes.asStateFlow()

    private val testBikes = listOf(
        Bike(
            name = "Bike1",
            frontTire = Tire(23.0, 23.0, 23.0),
            rearTire = Tire(23.0, 23.0, 23.0),
            type = Bike.Type.TRAIL,
            isEBike = false
        ),
        Bike(
            name = "Bike2",
            frontTire = Tire(23.0, 23.0, 23.0),
            rearTire = Tire(23.0, 23.0, 23.0),
            type = Bike.Type.TRAIL,
            isEBike = false
        ),
        Bike(
            name = "Bike3",
            frontTire = Tire(23.0, 23.0, 23.0),
            rearTire = Tire(23.0, 23.0, 23.0),
            type = Bike.Type.TRAIL,
            isEBike = false
        )
    )
    private val _bikes = MutableStateFlow<List<Bike>>(emptyList())

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)

        viewModelScope.launch {
            // todo: find out why this doesn't do anything
            _events.emit(Events.Loading)
            delay(3000)
            _bikes.emit(testBikes)

            _events.emit(Events.FinishedLoading)
        }
    }

    fun onViewPagerSelectionChanged(page: Int) {
        viewModelScope.launch { _events.emit(Events.NewPageSelected) }
    }
}

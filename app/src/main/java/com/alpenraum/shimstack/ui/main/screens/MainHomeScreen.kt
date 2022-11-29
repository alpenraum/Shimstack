package com.alpenraum.shimstack.ui.main.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.opensky.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@Composable
fun HomeScreen(modifier: Modifier, viewModel: HomeScreenViewModel? = null) {
    Text("HomeScreen")
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    HomeScreen(modifier = Modifier)
}

@HiltViewModel
class HomeScreenViewModel @Inject constructor() :
    BaseViewModel() {
    sealed class Events {
        object Loading : Events()
        object FinishedLoading : Events()
        object Error : Events()
    }
}

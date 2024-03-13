package com.danielrotaru.petdemo.views

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.danielrotaru.petdemo.R
import com.danielrotaru.petdemo.model.Animal
import com.danielrotaru.petdemo.views.viewmodel.PetViewModel


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun AnimalsOverviewScreen(
    viewModel: PetViewModel,
    onItemClick: (Animal) -> Unit? = {}
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val animals by viewModel.totalAnimals.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getAnimals()
    }
    when (uiState) {
        is AnimalsUiState.Error -> Toast.makeText(context,
            (uiState as AnimalsUiState.Error).error, Toast.LENGTH_LONG).show()

        is AnimalsUiState.Loaded -> {
            Column(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier
                        .padding(16.dp)
                        .weight(1f)
                        .padding(16.dp),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    state = rememberLazyListState()
                ) {
                    items(items = animals.animals) { animal ->
                        Card(modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp)
                            .padding(vertical = 8.dp)
                            .clickable { onItemClick(animal) }) {

                            Box {
                                /*  AsyncImage(
                                      model = animal.photos.firstOrNull()?.small,
                                      placeholder = painterResource(id = R.drawable.ic_launcher_background),
                                      onLoading = {},
                                      fallback = painterResource(id = R.drawable.ic_launcher_foreground),
                                      contentDescription = null,
                                  )
                                */
                                GlideImage(
                                    loading = placeholder(R.drawable.ic_launcher_background),
                                    failure = placeholder(R.drawable.ic_launcher_foreground),
                                    model = animal.photos.firstOrNull()?.small,
                                    contentDescription = "",
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                }
                if (animals.pagination != null) {
                    Button(
                        onClick = {
                            viewModel.getAnimals(
                                page = (animals.pagination?.currentPage ?: 1) + 1
                            )
                        },
                        enabled = animals.pagination?.currentPage != animals.pagination?.totalPages,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(text = "Load more")
                    }
                }

            }
        }

        is AnimalsUiState.Loading -> {
            LoadingIndicator()
        }

        else -> {}
    }
}
package com.danielrotaru.petdemo.views

import android.app.Activity
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.danielrotaru.petdemo.model.Animal
import com.danielrotaru.petdemo.views.viewmodel.PetViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetailsScreen(navController: NavController,
                  viewModel: PetViewModel,
                  animalId: Int) {
    val uiState by viewModel.uiState.collectAsState()
    val currentContext = LocalContext.current as Activity
    val animalObject by viewModel.currentAnimal.collectAsState()
    val animal = animalObject.animal

    LaunchedEffect(Unit) {
        viewModel.getAnimal(animalId)
    }

    when (uiState) {
        is AnimalsUiState.Error -> Toast.makeText(currentContext, (uiState as AnimalsUiState.Error).error, Toast.LENGTH_LONG).show()

        AnimalsUiState.Loading -> {}
        is AnimalsUiState.Loaded -> {
              AnimalsDetailLayout(

                  animal = if(animal.isNotEmpty())animal.first() else null,
              )
        }
    }

    BackHandler {
        navController.popBackStack()
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun AnimalsDetailLayout(
    animal: Animal?
) {

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(vertical = 8.dp)
        ) {

            Box {
                GlideImage(
                    model = animal?.photos?.firstOrNull()?.full,
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.size(16.dp))
        Text(text = animal?.name.orEmpty(), fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text(text = "Status: ${animal?.status}")
        Text(text = "Breed: ${animal?.breeds?.primary}")
        Text(text = "Size: ${animal?.size}")
        Text(text = "Gender: ${animal?.gender}")
        Text(text = "Distance: ${animal?.distance}")
    }
}
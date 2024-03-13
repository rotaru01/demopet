package com.danielrotaru.petdemo.views.viewmodel

import com.danielrotaru.petdemo.model.Animals
import com.danielrotaru.petdemo.model.Pagination
import com.danielrotaru.petdemo.model.Token
import com.danielrotaru.petdemo.repository.PetRepository
import com.danielrotaru.petdemo.retrofit.TokenManager
import com.danielrotaru.petdemo.views.AnimalsUiState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class PetViewModelTest {

    @MockK
    private lateinit var petRepository: PetRepository
    @MockK
    private lateinit var tokenManager: TokenManager

    private lateinit var viewModel: PetViewModel

    private lateinit var mockUiState: MutableStateFlow<AnimalsUiState>

    @Before
    fun setup() {
        val testDispatcher = TestCoroutineDispatcher()
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this)
        viewModel = PetViewModel(petRepository)
        mockUiState = mockk(relaxed = true)
        // Set the coroutine dispatcher for testing

    }

    @After
    fun cleanup() {
        // Reset the main dispatcher after the test
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN getAnimals is called WHEN tokenManager is not null and request is success THEN ui state is LOADED`() {

        val tokenManagerLocal = TokenManager
        tokenManagerLocal.updateTokenValue(Token("","",""))
        every { tokenManager.token } returns tokenManagerLocal.token
        coEvery { petRepository.getAnimals(1) } returns Animals(emptyList(), Pagination(1,2,1,2))
        viewModel.getAnimals(1)

       assertEquals(AnimalsUiState.Loaded, viewModel.uiState.value)
    }

    @Test
    fun `GIVEN getAnimals is called WHEN tokenManager is not null and request is fail THEN ui state is ERROR`() {

        val tokenManagerLocal = TokenManager
        tokenManagerLocal.updateTokenValue(Token("","",""))
        every { tokenManager.token } returns tokenManagerLocal.token
        coEvery { petRepository.getAnimals(1) } throws Exception("ERR-00001 ")
        viewModel.getAnimals(1)

        assertEquals(AnimalsUiState.Error("The request has missing parameters."), viewModel.uiState.value)
    }

    @Test
    fun `GIVEN getAnimals is called WHEN tokenManager is not null and token request is fail THEN ui state is ERROR`() {

        val tokenManagerLocal = TokenManager
        tokenManagerLocal.updateTokenValue(Token("","",""))
        every { tokenManager.token } returns tokenManagerLocal.token
        coEvery { petRepository.getAnimals(1) } throws Exception("HTTP 401 ")
        viewModel.getAnimals(1)

        assertEquals(AnimalsUiState.Error("Bad Credentials"), viewModel.uiState.value)
    }

}

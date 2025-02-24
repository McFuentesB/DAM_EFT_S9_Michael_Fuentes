package com.example.examenfinal


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("recover_password") { RecoverPasswordScreen(navController) }
        composable("main") { MainScreen(navController) }
        composable("text_to_speech") {TextToSpeechScreen(navController)}
        composable("speech_recognizer") {SpeechRecognizerScreen(navController)}
        composable("about_us") {AboutUsScreen(navController)}
        composable("location_screen") {
            val context = androidx.compose.ui.platform.LocalContext.current
            LocationScreen(context, navController) }



    }
}

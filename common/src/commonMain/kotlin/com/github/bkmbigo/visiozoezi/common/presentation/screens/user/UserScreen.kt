package com.github.bkmbigo.visiozoezi.common.presentation.screens.user

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen

class UserScreen: Screen {

    @Composable
    override fun Content() {
        Text(text = "User Screen")
    }
}
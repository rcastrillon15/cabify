package com.cabify.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cabify.R
import com.cabify.ui.theme.Purple700
import com.cabify.ui.theme.medium
import com.cabify.ui.theme.normal
import com.cabify.ui.theme.small
import com.cabify.ui.theme.xsmall

@Composable
fun ContentBottomSheet(
    totalToPay: String,
    showLoading: Boolean,
    checkout: () -> Unit,
    clearFocus: () -> Unit,
    hideBottomSheet: () -> Unit
) {
    var state by remember { mutableStateOf(CardFace.Front) }
    var cardNumber by remember { mutableStateOf("8547 9658 6325 4521") }
    var cardHolder by remember { mutableStateOf("RODRIGO CASTRILLON") }
    var cardExpiration by remember { mutableStateOf("02/25") }
    var cardCVV by remember { mutableStateOf("123") }

    Column(
        modifier = Modifier
            .padding(medium)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {

        IconButton(
            enabled = !showLoading,
            onClick = {
                clearFocus()
                hideBottomSheet()
            },
            modifier = Modifier.padding(vertical = xsmall, horizontal = small)
        ) {
            Icon(
                Icons.Default.Close,
                contentDescription = "Close",
                tint = Purple700
            )
        }

        CreditCard(
            cardFace = state,
            axis = RotationAxis.AxisY,
            onClick = { state = it.next },
            cardNumber = cardNumber,
            cardHolder = cardHolder,
            cardExpiration = cardExpiration,
            cardCVV = cardCVV
        )

        TextField(
            enabled = !showLoading,
            value = cardNumber,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = normal),
            singleLine = true,
            onValueChange = {
                if (it.length <= 19) cardNumber = it
            },
            label = { Text("Card number") },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White
            )
        )

        TextField(
            enabled = !showLoading,
            value = cardHolder.uppercase(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            onValueChange = {
                if (it.length <= 25) cardHolder = it
            },
            label = { Text("Card holder") },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White
            ),
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters)
        )

        TextField(
            enabled = !showLoading,
            value = cardExpiration,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            onValueChange = {
                if (it.length <= 5) cardExpiration = it
            },
            label = { Text("Card expiration") },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White
            )
        )

        TextField(
            enabled = !showLoading,
            value = cardCVV,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { state = state.next },
            singleLine = true,
            onValueChange = {
                if (it.length <= 3) cardCVV = it
                if (it.length == 3) state = state.next
            },
            label = { Text("Card cvv") },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White
            )
        )

        Button(
            enabled = !showLoading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = medium),
            onClick = {
                clearFocus()
                checkout()
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Purple700),
            shape = RoundedCornerShape(28.dp)
        ) {

            if (showLoading) {
                CircularProgressCustom()
            } else {
                Text(
                    text = stringResource(
                        id = R.string.pay, totalToPay
                    ),
                    style = MaterialTheme.typography.subtitle2.copy(
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold
                    ),
                    modifier = Modifier.padding(vertical = normal)
                )
            }
        }
    }
}
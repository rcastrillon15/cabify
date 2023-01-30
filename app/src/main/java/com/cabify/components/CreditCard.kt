package com.cabify.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cabify.R
import com.cabify.ui.theme.Purple700
import com.cabify.ui.theme.Yellow_FFFFC107
import com.cabify.ui.theme.medium
import com.cabify.ui.theme.normal
import com.cabify.ui.theme.small

@Composable
fun CreditCard(cardNumber: String, cardHolder: String, cardExpiration: String) {

    Card(
        backgroundColor = Purple700,
        shape = RoundedCornerShape(8.dp),
        elevation = 0.dp
    ) {
        Column(modifier = Modifier
            .padding(medium)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = normal),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(
                        id = R.string.bank_name
                    ),
                    style = MaterialTheme.typography.subtitle2.copy(
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold
                    )
                )

                Icon(
                    painter = painterResource(id = R.drawable.ic_contactless_payment),
                    contentDescription = "Contactless payment",
                    modifier = Modifier.rotate(90F),
                    tint = Color.LightGray
                )
            }

            Icon(
                painter = painterResource(id = R.drawable.ic_chip),
                contentDescription = "Credit card chip",
                modifier = Modifier.padding(horizontal = medium),
                tint = Yellow_FFFFC107
            )

            Text(
                text = cardNumber,
                modifier = Modifier.padding(vertical = normal),
                style = MaterialTheme.typography.subtitle2.copy(
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold
                )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = small),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "VALID\nTHRU",
                    style = MaterialTheme.typography.caption.copy(
                        textAlign = TextAlign.Center,
                        color = Color.Gray
                    )
                )

                Text(
                    text = cardExpiration,
                    style = MaterialTheme.typography.body1.copy(
                        textAlign = TextAlign.Center,
                        color = Color.LightGray,
                        fontWeight = FontWeight.ExtraBold
                    )
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = normal),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = cardHolder,
                    style = MaterialTheme.typography.body1.copy(
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold
                    )
                )

                Image(
                    painter = painterResource(id = R.drawable.ic_mastercard),
                    contentDescription = "Payment",
                    modifier = Modifier.size(50.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun CreditCardPreview() {
    CreditCard(cardNumber = "8547 9658 6325 4521", "RODRIGO CASTRILLON", cardExpiration = "02/25")
}
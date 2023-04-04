@file:OptIn(ExperimentalComposeUiApi::class)

package com.michaelrichards.tipapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.michaelrichards.tipapp.Components.InputField
import com.michaelrichards.tipapp.Widgets.RoundIConButton
import com.michaelrichards.tipapp.ui.theme.TipAppTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
            }
        }
    }
}

private const val TAG = "MainActivity"


@ExperimentalComposeUiApi
//@Preview(showBackground = true)
@Composable
fun DefaultPreview() {

    MyApp {
        Text(text = "Hello Again")
    }

}

@ExperimentalComposeUiApi
@Composable
fun MyApp(content: @Composable () -> Unit) {
    TipAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {

            Column {
                Header()
                MainContent()
            }


        }
    }
}


//@Preview
@Composable
fun Header(totalPerPerson: Double = 134.00) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clip(shape = RoundedCornerShape(corner = CornerSize(12.dp))),
        color = Color(0xF0E0D7F7)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        )
        {
            val total = "%.2f".format(totalPerPerson)
            Text(text = "Total Per Person", style = MaterialTheme.typography.h4)
            Text(
                text = "$$total",
                style = MaterialTheme.typography.h3,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}


@ExperimentalComposeUiApi
@Preview
@Composable
fun MainContent() {

    BillForm() { billAmount ->
        Log.d(TAG, "MainContent: $billAmount")
    }


}

@ExperimentalComposeUiApi
@Composable
fun BillForm(
    modifier: Modifier = Modifier,
    onValChange: (String) -> Unit = {},
) {
    val billState = remember {
        mutableStateOf("")
    }

    val isValidState = remember(billState.value) {
        billState.value.trim().isNotEmpty()
    }

    val keyBoardController = LocalSoftwareKeyboardController.current

    Surface(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(corner = CornerSize(10.dp)),
        border = BorderStroke(width = 2.dp, color = Color.Black)
    )
    {
        Column(
            modifier = Modifier.padding(6.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        )
        {
            InputField(valueState = billState,
                labelId = "Enter Bill",
                enabled = true, isSingleLine = true,
                onAction = KeyboardActions {
                    if (!isValidState) return@KeyboardActions
                    onValChange(billState.value.trim())
                    keyBoardController?.hide()
                })

            if (isValidState) {
                Row(modifier = Modifier.padding(3.dp), horizontalArrangement = Arrangement.Start) {
                    Text(text = "Split", modifier = Modifier.align(Alignment.CenterVertically))
                    Spacer(modifier = Modifier.width(120.dp))
                    Row(
                        modifier = Modifier.padding(horizontal = 3.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        RoundIConButton(imageVector = Icons.Default.Remove, onClick = { /*TODO*/ })
                        RoundIConButton(imageVector = Icons.Default.Add, onClick = { /*TODO*/ })
                    }
                }
            } else {
                Box() {

                }
            }
        }
    }
}
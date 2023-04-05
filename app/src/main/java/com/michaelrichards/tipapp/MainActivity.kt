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
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.michaelrichards.tipapp.Components.InputField
import com.michaelrichards.tipapp.Utils.calcTotalPerPerson
import com.michaelrichards.tipapp.Utils.calculateTip
import com.michaelrichards.tipapp.Widgets.RoundIConButton
import com.michaelrichards.tipapp.ui.theme.TipAppTheme
import kotlin.math.round

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

    var totalPerPerson by remember {
        mutableStateOf(0.0)
    }

    TipAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {

            Column {
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
            .padding(20.dp)
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

    val billState = remember {
        mutableStateOf("0.00")
    }

    var splitBy = remember {
        mutableStateOf(1)
    }


    var tipAmount = remember {
        mutableStateOf(0.0)
    }

    var totalPerPerson = remember {
        mutableStateOf(0.0)
    }


    BillForm(splitBy = splitBy, billState = billState, tipAmount = tipAmount, totalPerPerson = totalPerPerson) { billAmount ->

    }


}

@ExperimentalComposeUiApi
@Composable
fun BillForm(
    modifier: Modifier = Modifier,
    splitBy: MutableState<Int>,
    tipAmount: MutableState<Double>,
    totalPerPerson: MutableState<Double>,
    billState: MutableState<String>,
    onValChange: (String) -> Unit = {},
) {

    var sliderPositionState by remember {
        mutableStateOf(0f)
    }
    val isValidState = remember(billState.value) {
        billState.value.trim().isNotEmpty()
    }


    val keyBoardController = LocalSoftwareKeyboardController.current


    Surface(
        modifier = modifier
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
            Header(totalPerPerson.value)
            InputField(
                valueState = billState,
                labelId = "Enter Bill",
                enabled = true, isSingleLine = true,
                onAction = KeyboardActions {
                    if (!isValidState) return@KeyboardActions

                    onValChange(billState.value.trim())
                    keyBoardController?.hide()

                })

            if (isValidState) {
                Row(modifier = Modifier.padding(3.dp), horizontalArrangement = Arrangement.Start)
                {
                    Text(text = "Split", modifier = Modifier.align(CenterVertically))
                    Spacer(modifier = Modifier.width(120.dp))
                    Row(
                        modifier = Modifier.padding(horizontal = 3.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        RoundIConButton(imageVector = Icons.Default.Remove, onClick = {
                            if (splitBy.value > 1)
                                splitBy.value--
                            totalPerPerson.value = calcTotalPerPerson(
                                billAmount = billState.value.toDouble(),
                                splitBy = splitBy.value,
                                tipPercentage = sliderPositionState
                            )
                        })
                        Text(
                            text = splitBy.value.toString(), modifier = Modifier
                                .align(CenterVertically)
                                .padding(10.dp)
                        )
                        RoundIConButton(imageVector = Icons.Default.Add, onClick = {
                            if (splitBy.value < 25) {
                                splitBy.value++
                                totalPerPerson.value = calcTotalPerPerson(
                                    billAmount = billState.value.toDouble(),
                                    splitBy = splitBy.value,
                                    tipPercentage = sliderPositionState
                                )
                            }
                        })
                    }
                }

                //Tip Row
                Row(modifier = Modifier.padding(horizontal = 3.dp, vertical = 12.dp))
                {
                    Text(text = "Tip Amount", modifier = Modifier.align(CenterVertically))
                    Spacer(modifier = Modifier.width(200.dp))
                    Text(text = "$${String.format("%.2f", tipAmount.value)}", modifier = Modifier.align(CenterVertically))
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                )
                {
                    Text(text = "${sliderPositionState.toString().substringBefore('.')}%")
                    Spacer(modifier = Modifier.height(15.dp))

                    Slider(
                        value = sliderPositionState, valueRange = 0f..100f, steps = 9,
                        onValueChange = { newVal ->
                           // sliderPositionState = round(newVal)
                            sliderPositionState = round( newVal)
                            tipAmount.value = calculateTip(billAmount = billState.value.toDouble(), tipPercentage = sliderPositionState)
                            totalPerPerson.value = calcTotalPerPerson(billAmount = billState.value.toDouble(), splitBy = splitBy.value, tipPercentage = sliderPositionState)
                        },
                    )
                }
            } else {
                Box() {

                }
            }


        }
    }
}



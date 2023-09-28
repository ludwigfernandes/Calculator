package com.example.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Backspace
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculator.ui.theme.Background
import com.example.calculator.ui.theme.BackspaceColor
import com.example.calculator.ui.theme.CalculatorTheme
import com.example.calculator.ui.theme.ClearColor
import com.example.calculator.ui.theme.EqualBG
import com.example.calculator.ui.theme.EqualColor
import com.example.calculator.ui.theme.NumberColor
import com.example.calculator.ui.theme.SymbolColor
import com.example.calculator.ui.theme.TransparentBG
import net.objecthunter.exp4j.ExpressionBuilder
import java.text.DecimalFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CalculatorApp()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun CalculatorApp() {
    var input by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    var hasDecimal by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            TextField(
                value = input,
                onValueChange = {input=it},
                textStyle = TextStyle(fontSize = 50.sp, fontWeight = FontWeight.Light, textAlign = TextAlign.End, color = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusProperties {
                        canFocus=false
                        enter={
                            FocusRequester.Cancel
                        }
                    },
                maxLines = 1,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 16.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            TextField(
                value = result,
                onValueChange = {},
                textStyle = TextStyle(fontSize = 50.sp, fontWeight = FontWeight.Normal, textAlign = TextAlign.End, color = Color.Gray),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusProperties {
                        canFocus=false
                        enter={
                            FocusRequester.Cancel
                        }
                    },
                maxLines = 1,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            Row(
                modifier= Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp),
                horizontalArrangement = Arrangement.End
            ){
                IconButton(
                    onClick = {
                        if (input.isNotEmpty()) {
                            if(input.lastOrNull()=='.'){
                                input=input.dropLast(1)
                                hasDecimal=false
                            }else{
                            input=input.dropLast(1)
                            }
                        }
                        //input=backspace(input)
                        result = evaluateExpression(input)
                    }
                ) {
                    Icon(
                        Icons.Outlined.Backspace,
                        contentDescription = "Backspace",
                        tint = BackspaceColor,
                        modifier= Modifier.size(30.dp)
                    )
                }
            }
            Divider(
                modifier=Modifier.padding(top = 16.dp, bottom = 16.dp),
                thickness=3.dp,
                color = TransparentBG
            )
            /*Spacer(modifier = Modifier.height(9.dp))*/
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                CalculatorButton("(", SymbolColor, TransparentBG, modifier = Modifier.weight(1f)) {
                    input = appendInput(input, "(")
                }
                CalculatorButton(")", SymbolColor, TransparentBG, modifier = Modifier.weight(1f)) {
                    input = appendInput(input, ")")
                }
                CalculatorButton("%", SymbolColor, TransparentBG, modifier = Modifier.weight(1f)) {
                    input = containsCharacter(input, "%")
                    hasDecimal=false
                }
                CalculatorButton("/", SymbolColor, TransparentBG, modifier = Modifier.weight(1f)) {
                    input = containsCharacter(input, "/")
                    hasDecimal=false
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                CalculatorButton("7", NumberColor, TransparentBG, modifier = Modifier.weight(1f)) {
                    input = appendInput(input, "7")
                    result = evaluateExpression(input)
                }
                CalculatorButton("8", NumberColor, TransparentBG, modifier = Modifier.weight(1f)) {
                    input = appendInput(input, "8")
                    result = evaluateExpression(input)
                }
                CalculatorButton("9", NumberColor, TransparentBG, modifier = Modifier.weight(1f)) {
                    input = appendInput(input, "9")
                    result = evaluateExpression(input)
                }
                CalculatorButton("*", SymbolColor, TransparentBG, modifier = Modifier.weight(1f)) {
                    input = containsCharacter(input, "*")
                    hasDecimal=false
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                CalculatorButton("4", NumberColor, TransparentBG, modifier = Modifier.weight(1f)) {
                    input = appendInput(input, "4")
                    result = evaluateExpression(input)
                }
                CalculatorButton("5", NumberColor, TransparentBG, modifier = Modifier.weight(1f)) {
                    input = appendInput(input, "5")
                    result = evaluateExpression(input)
                }
                CalculatorButton("6", NumberColor, TransparentBG, modifier = Modifier.weight(1f)) {
                    input = appendInput(input, "6")
                    result = evaluateExpression(input)
                }
                CalculatorButton("-", SymbolColor, TransparentBG, modifier = Modifier.weight(1f)) {
                    input = containsCharacter(input, "-")
                    hasDecimal=false
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                CalculatorButton("1", NumberColor, TransparentBG, modifier = Modifier.weight(1f)) {
                    input = appendInput(input, "1")
                    result = evaluateExpression(input)
                }
                CalculatorButton("2", NumberColor, TransparentBG, modifier = Modifier.weight(1f)) {
                    input = appendInput(input, "2")
                    result = evaluateExpression(input)
                }
                CalculatorButton("3", NumberColor, TransparentBG, modifier = Modifier.weight(1f)) {
                    input = appendInput(input, "3")
                    result = evaluateExpression(input)
                }
                CalculatorButton("+", SymbolColor, TransparentBG, modifier = Modifier.weight(1f)) {
                    input = containsCharacter(input, "+")
                    hasDecimal=false
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                //CalculatorButton("±", NumberColor, TransparentBG, modifier = Modifier.weight(1f), onClick = {  })
                CalculatorButton("C", ClearColor, TransparentBG, modifier = Modifier.weight(1f)) {
                    input = ""
                    result = ""
                    hasDecimal=false
                }
                CalculatorButton("0", NumberColor, TransparentBG, modifier = Modifier.weight(1f)) {
                    input = appendInput(input, "0",)
                    result = evaluateExpression(input)
                }
                CalculatorButton(".", NumberColor, TransparentBG, modifier = Modifier.weight(1f)) {
                    if(hasDecimal==false){
                        input = containsCharacter(input, ".")
                    }
                    hasDecimal=true
                }
                CalculatorButton("=", EqualColor, EqualBG, modifier = Modifier.weight(1f)) {
                    input=result
                    result = ""
                    if(input.contains('.')){
                        hasDecimal=false
                    }
                }
            }
        }
    }
}

@Composable
fun CalculatorButton(symbol: String, content: Color, btnBG: Color, modifier: Modifier, onClick: ()-> Unit ) {
    OutlinedButton(
        modifier = Modifier
            .padding(3.dp)
            .then(modifier),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = btnBG,
            contentColor = content
        ),
        border = null,
        onClick = onClick
    ) {
        Text(
            text = symbol,
            fontWeight = FontWeight.Light,
            fontSize = 40.sp,
            textAlign = TextAlign.Center ,
            modifier=Modifier.align(Alignment.CenterVertically)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CalculatorTheme {
        CalculatorApp()
    }
}

/// OP FUNCTIONS
fun appendInput(inputSH: String, symbol: String): String {
    return inputSH+symbol
}
fun containsCharacter(inputSH: String, symbol: String): String{
    return when(symbol){
        "+", "-", "*", "/", "%" -> {
            if(!inputSH.endsWith("+") && !inputSH.endsWith("-") && !inputSH.endsWith("*") && !inputSH.endsWith("/") && !inputSH.endsWith("%") ){
                inputSH+symbol
            }else{
                inputSH
            }
        }
        "." -> {
            inputSH+symbol
        }
        else -> inputSH
    }
}
fun evaluateExpression(expressions: String): String {
    try{
        if(expressions.contains("+") ||
            expressions.contains("-") ||
            expressions.contains("*") ||
            expressions.contains("/") ||
            expressions.contains("%")
            ){
            val expression=ExpressionBuilder(expressions).build().evaluate()
            val decimalFormat=DecimalFormat("#.##")
            val formatted=decimalFormat.format(expression)
            return formatted.toString()
        }else{
            return ""
        }
    }catch (e: ArithmeticException){
        return ""
    }catch (e: Exception){
        return ""
    }
}
fun backspace(inputSH: String): String {
    return if (inputSH.isNotEmpty()) {
        inputSH.dropLast(1)
    }else{
        inputSH
    }
}

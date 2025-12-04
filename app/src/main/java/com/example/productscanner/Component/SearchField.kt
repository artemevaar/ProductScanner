package com.example.productscanner.Component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.productscanner.R
import com.example.productscanner.ui.theme.manropeFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(
        imeAction = ImeAction.Done
    ),
    modifier: Modifier,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    textStyle: TextStyle = TextStyle.Default.copy(
        fontFamily = manropeFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        color = Color.Black
    ),
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    val context = LocalContext.current
    val textInputColor = Color(ContextCompat.getColor(context, R.color.text_inp_grey))

    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = label,
                style = textStyle.copy(color = textInputColor)
            )
        },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.icon_search),
                contentDescription = "Search Icon"
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(RoundedCornerShape(14.dp)),
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.White,
            cursorColor = Color.Black,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        textStyle = textStyle,
        visualTransformation = visualTransformation
    )
}

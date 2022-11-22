package com.example.petschedule.composables

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.petschedule.entities.Group
import org.json.JSONObject
import java.io.File


@Preview
@Composable
fun MyGroups() {
    val token =
        "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyMW1vayIsImlhdCI6MTY2ODUxNDA1NCwiZXhwIjoxNjY5MTE4ODU0fQ.3M3pVQ4ehW9SpZ2murk7K9p5Th9WLDB51zIrZnQPh00"
    val context = LocalContext.current
    val state = remember {
        mutableStateOf(Group("", ""))
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.1f)
    ) {
        Button(
            onClick = { createGroup(token, "groupName", state, context) },
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.White,
                contentColor = Color.Gray
            )
        ) {
            Text(
                text = "Создать группу",
                color = Color.Blue,
                fontSize = 30.sp,
            )
        }
    }
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 100.dp)
    ) {
        itemsIndexed(
            listOf(state.value.id, state.value.name)
        ) { _, item ->
            Button(
                onClick = { /*TODO*/ },
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier.padding(5.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.White,
                    contentColor = Color.Gray
                )
            ) {
                Text(
                    text = item,
                    color = Color.Blue,
                    fontSize = 30.sp,
                )
            }
        }
    }
}

private fun createGroup(
    token: String,
    name: String,
    groupState: MutableState<Group>,
    context: Context
) {
    var url = "http://localhost:8091/groups/create" +
            "?name=$name"
    //val newUrl = "https://ya.ru"
    val queue = Volley.newRequestQueue(context)
    val stringRequest = object : StringRequest(
        Method.POST,
        url,
        { response ->
            var obj = JSONObject(response)
            groupState.value = Group(obj.get("id").toString(), obj.get("name").toString())
        },
        { error ->
            Log.d("MyLog", "Error $error")
        }) {
        override fun getHeaders(): MutableMap<String, String> {
            val headers = HashMap<String, String>()
            headers["Authorization"] = token
            return headers
        }
    }
    queue.add(stringRequest)
}
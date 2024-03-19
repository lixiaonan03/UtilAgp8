package com.lxn.util

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.magnifier
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import com.lxn.util.ui.theme.UtilAgp8Theme
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.updatePadding
import com.lxn.util.glide.GlideActivity
import com.lxn.util.lifecycle.LifeCycleActivity
import com.lxn.util.net.NetActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
//            UtilAgp8Theme {
            // A surface container using the 'background' color from the theme
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = colorResource(id = R.color.white)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colorResource(id = R.color.white))
//                            .height(10.dp)
//                            .width(IntrinsicSize.Max)
                ) {
                    Row(
                        modifier = Modifier
                            .height(40.dp)
                            .background(colorResource(id = R.color.colorLogo))
                    ) {
                        Text(
                            text = "Util项目使用AGP8 重学",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                    }
                    ContentComposable()
                }
            }
//            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun ContentComposable() {
    val context = LocalContext.current
    // 创建一个垂直滚动的状态
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = scrollState)
    ) {
        // 你的内容在这里
        // 示例文本，你可以替换为你的实际内容
        Size(
            width = 0f,
            height = 10f
        )
        ItemComposable("Glide 学习") {
            //跳转Glide 学习的
            GlideActivity.startActivity(context as MainActivity)
        }
        ItemComposable("lifeCycle 学习") {
            //生命周期 学习的
            LifeCycleActivity.startActivity(context as MainActivity)
        }
        ItemComposable("网络库 学习") {
            //网络库 学习的
            NetActivity.startActivity(context as MainActivity)
        }

        repeat(20) {
            ItemComposable("Item $it", onClick = {

            })
        }
    }
}

@Composable
fun ItemComposable(name: String, onClick: () -> Unit) {

    BoxWithConstraints(
        modifier = Modifier
            .height(65.dp)
            .fillMaxWidth()
            .padding(top = 10.dp)
            .background(Color.White)
            .clickable(onClick = onClick)

    ) {
        Column(
            verticalArrangement = Arrangement.Center,// 将文本垂直居中
            modifier = Modifier.fillMaxHeight() // 添加 fillMaxHeight
        ) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .padding(top = 1.dp)
                .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = name,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    color =  Color.Black,
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(color = colorResource(id = R.color.colorLogo))
            )
        }
    }

}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    UtilAgp8Theme {
        Greeting("AndroidLxn")
    }
}
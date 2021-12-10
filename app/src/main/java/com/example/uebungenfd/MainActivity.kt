package com.example.uebungenfd

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import com.example.uebungenfd.ui.theme.UebungenFDTheme
import com.example.uebungenfd.viewModel.MainViewModel
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.uebungenfd.srhtest.LengthValue
import kotlin.math.exp

//library implementation hinzufügen des links der library in gradle scripts build.gradle MODULE
// --- ALT ENTER !

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()


    sealed class ScreenData(
        val route: String,
        @StringRes val resourceId: Int,
        val icon: ImageVector
    ) {
        object LoremIpsum : ScreenData("lorem", R.string.lorem, Icons.Filled.Favorite)
        object Settings : ScreenData("settings", R.string.settings, Icons.Filled.Settings)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            UebungenFDTheme {
                Surface(color = MaterialTheme.colors.background) {
                    Scaffold(bottomBar = {
                        BottomNavigation {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentDestination = navBackStackEntry?.destination
                            listOf(ScreenData.LoremIpsum, ScreenData.Settings).forEach { item ->
                                BottomNavigationItem(
                                    icon = { Icon(item.icon, contentDescription = null) },
                                    label = { Text(text = stringResource(id = item.resourceId)) },
                                    selected = currentDestination?.hierarchy?.any {
                                        it.route == item.route
                                    } == true,
                                    onClick = {
                                        navController.navigate(item.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                    ) { innerPadding ->
                        NavHost(
                            navController, startDestination = ScreenData.LoremIpsum.route,
                            Modifier.padding(innerPadding)
                        ) {
                            composable(ScreenData.LoremIpsum.route) {
                                MainContent(navController)
                            }
                            composable(ScreenData.Settings.route) {
                                SettingContent(navController)
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun FullScreenDialog(text: String, onClose: () -> Unit) {
        Text(text = "Insert the amount of paragraphs you want to load:")
    }


    @Composable
    fun SettingContent(navController: NavController) {

        //Dropdown Menu test
        var expanded by remember { mutableStateOf(false) }
        val suggestions = listOf("Short", "Medium", "Long")
        var selectedText by remember { mutableStateOf("") }


        Button(

            onClick = {
                expanded = !expanded
            },
            modifier = Modifier.padding(10.dp)
        ) {
            Text(text = "Paragraph length")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.padding(10.dp)
        ) {
            LengthValue.values().forEach { label ->
                DropdownMenuItem(onClick = {
                    selectedText = label.toString()
                }) {
                   // Text(text = label)
                }
            }
        }
    }


    @Composable
    fun MainContent(navController: NavController) {


        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var paragraphValue by remember { mutableStateOf("") }
            var isLoading by remember { mutableStateOf(false) }

            val loremIpsum by viewModel.loremIpsumText.observeAsState("")

            Text(
                text = "Insert the amount of paragraphs you want to load:",
                modifier = Modifier.padding(10.dp),
                // style = TextStyle.Default
            )

            Text(
                text = loremIpsum,
                modifier = Modifier.padding(8.dp),
                // style = TextStyle.Default
            )

            TextField(value = paragraphValue, onValueChange = { paragraphValue = it })

            Button(

                onClick = {
                    try {
                        viewModel.loadParagraphs(paragraphValue.toInt())
                        isLoading = true;
                    } catch (exception: NumberFormatException) {
                        Toast.makeText(
                            applicationContext,
                            "Invalid input!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                },
                modifier = Modifier.padding(top = 8.dp),
            ) {
                Text(text = "Load")
            }

            Button(
                onClick = { },
                modifier = Modifier.padding(top = 8.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
            ) {
                Text(text = "Delete")
            }

            if (isLoading) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(Modifier.size(48.dp))
                }
            }

            if (loremIpsum.isNotEmpty()) {

                FullScreenDialog(text = loremIpsum)
                {
                    viewModel.reset()
                }

                isLoading = false

            }

        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        UebungenFDTheme {
            Column {
                Row(horizontalArrangement = Arrangement.Center) {

                }
            }
        }
    }
}

/*      TEST PREVIEW ETC
@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

//UI funktion ui element
@Composable
fun GUIButton() {
    Row(modifier = Modifier
        .height(36.dp)
        .background(Color.Red)
        .padding(10.dp)) {
      Text("zeile1")
      Spacer(modifier = Modifier.height(10.dp))
      Text(text = "zeile2")
    }
}

//macht das man sachen im editor anzeigen lassen kann
@Preview(showBackground = true)
@Composable
fun GUIButtonPreview() {
    GUIButton()
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    UebungenFDTheme {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            GUIButton()
            GUIButton()
            GUIButton()
            GUIButton()
            GUIButton()

        }
    }
}

 */
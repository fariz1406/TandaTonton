package com.fariznst0075.tandatonton.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.fariznst0075.tandatonton.R
import com.fariznst0075.tandatonton.model.Film
import com.fariznst0075.tandatonton.navigation.Screen
import com.fariznst0075.tandatonton.ui.theme.TandaTontonTheme
import com.fariznst0075.tandatonton.util.SettingsDataStore
import com.fariznst0075.tandatonton.util.ViewModelFactory
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    val context = LocalContext.current
    val settings = remember { SettingsDataStore(context) }
    val showList by settings.showListFlow.collectAsState(initial = true)
    var selectedFilter by rememberSaveable { mutableStateOf("Semua") }
    val filterOptions = listOf("Semua", "Film", "Series", "Belum Ditonton", "Sedang Ditonton", "Selesai")


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                actions = {
                    val coroutineScope = rememberCoroutineScope()

                    IconButton(onClick = {
                        val newValue = !showList
                        coroutineScope.launch {
                            settings.saveLayout(newValue)
                        }
                    }) {
                        Icon(
                            painter = painterResource(
                                id = if (showList) R.drawable.ic_view_grid else R.drawable.ic_view_list
                            ),
                            contentDescription = stringResource(
                                if (showList) R.string.tampilan_grid else R.string.tampilan_list
                            )
                        )
                    }

                }
            )
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.FormBaru.route)
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.tambah_catatan),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    ) {
        val paddingValues = it

        Column(modifier = Modifier.padding(paddingValues)) {
            var expanded by remember { mutableStateOf(false) }

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "Tampilkan: $selectedFilter",
                    modifier = Modifier
                        .menuAnchor(type = MenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth()
                        .padding(12.dp),
                    style = MaterialTheme.typography.titleMedium
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.exposedDropdownSize()
                ) {
                    filterOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                selectedFilter = option
                                expanded = false
                            }
                        )
                    }
                }
            }

            ScreenContent(
                modifier = Modifier.weight(1f),
                navController = navController,
                showList = showList,
                selectedFilter = selectedFilter
            )
        }

    }
}


@Composable
fun ScreenContent(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    showList: Boolean,
    selectedFilter: String
)
 {
    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val viewModel: MainViewModel = viewModel(factory = factory)
    val rawData by viewModel.data.collectAsState()

    val data = when (selectedFilter) {
         "Film" -> rawData.filter { it.jenis == "Film" }
         "Series" -> rawData.filter { it.jenis == "Series" }
         "Belum Ditonton" -> rawData.filter { it.status == "Belum Ditonton" }
         "Sedang Ditonton" -> rawData.filter { it.status == "Sedang Ditonton" }
         "Selesai" -> rawData.filter { it.status == "Selesai" }
         else -> rawData
    }



     if (data.isEmpty()) {
        Column(
            modifier = modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(text = stringResource(id = R.string.list_kosong))
        }
    }
    else {
        if (showList) {
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 84.dp)
            ) {
                items(data) {
                    ListItem(film = it) {
                        navController.navigate(Screen.FormUbah.withId(it.id))
                    }
                    HorizontalDivider()
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(150.dp),
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(data) {
                    GridItem(film = it) {
                        navController.navigate(Screen.FormUbah.withId(it.id))
                    }
                }
            }
        }
    }
}

@Composable
fun ListItem(film: Film, onClick: () -> Unit) {
    val tanggalFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    val tanggalString = tanggalFormat.format(Date(film.tanggal))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable { onClick() },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = film.judul,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "Jenis: ${film.jenis}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Status: ${film.status}",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = when (film.status) {
                        "Belum Ditonton" -> MaterialTheme.colorScheme.primary
                        "Sedang Ditonton" -> MaterialTheme.colorScheme.tertiary
                        "Selesai" -> MaterialTheme.colorScheme.secondary
                        else -> MaterialTheme.colorScheme.onSurface
                    }
                )
            )

            Text(
                text = "Ditambahkan: $tanggalString",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
fun GridItem(film: Film, onClick: () -> Unit) {
    val tanggalString = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(film.tanggal))

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = film.judul,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = film.jenis,
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = film.status,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = when (film.status) {
                        "Belum Ditonton" -> MaterialTheme.colorScheme.primary
                        "Sedang Ditonton" -> MaterialTheme.colorScheme.tertiary
                        "Selesai" -> MaterialTheme.colorScheme.secondary
                        else -> MaterialTheme.colorScheme.onSurface
                    }
                )
            )
            Text(
                text = tanggalString,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun MainScreenPreview() {
    TandaTontonTheme {
        MainScreen(rememberNavController())
    }
}
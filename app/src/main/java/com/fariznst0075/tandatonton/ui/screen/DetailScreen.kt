package com.fariznst0075.tandatonton.ui.screen

import android.app.DatePickerDialog
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.fariznst0075.tandatonton.R
import com.fariznst0075.tandatonton.model.Film
import com.fariznst0075.tandatonton.ui.theme.TandaTontonTheme
import com.fariznst0075.tandatonton.util.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

const val KEY_ID_CATATAN = "idCatatan"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavHostController, id: Long? = null) {
    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val viewModel: DetailViewModel = viewModel(factory = factory)
    var showMenu by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }


    var judul by remember { mutableStateOf("") }
    var jenis by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }
    var tanggal by remember { mutableStateOf(System.currentTimeMillis()) }

    LaunchedEffect(id) {
        if (id != null) {
            val data = viewModel.getFilm(id)
            if (data != null) {
                judul = data.judul
                jenis = data.jenis
                status = data.status
                tanggal = data.tanggal
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = {navController.popBackStack()}) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.kembali),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                title = {
                    if (id == null)
                        Text(text = stringResource(id = R.string.tambah_catatan))
                    else
                        Text(text = stringResource(id = R.string.edit_catatan))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                actions = {
                    IconButton(onClick = {
                        if (judul == "") {
                            Toast.makeText(context, R.string.invalid, Toast.LENGTH_LONG).show()
                            return@IconButton
                        }

                        if (id == null) {
                            viewModel.insert(judul, jenis, status, tanggal)
                        } else {
                            viewModel.update(id, judul, jenis, status, tanggal)
                        }

                        navController.popBackStack()}) {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = stringResource(R.string.simpan),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    if (id != null) {
                        IconButton(onClick = { showMenu = !showMenu }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Menu",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            DropdownMenu(
                                expanded = showMenu,
                                onDismissRequest = { showMenu = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Hapus") },
                                    onClick = {
                                        showDeleteDialog = true
                                    }
                                )
                            }
                        }
                    }

                }
            )
        }
    ) { padding ->
        FormFilm(
            title = judul,
            onTitleChange = { judul = it },
            jenis = jenis,
            onJenisChange = { jenis = it },
            status = status,
            onStatusChange = { status = it },
            tanggal = tanggal,
            onTanggalChange = { tanggal = it },
            modifier = Modifier.padding(padding)
        )
        DisplayAlertDialog(
            openDialog = showDeleteDialog,
            onDismissRequest = { showDeleteDialog = false },
            onConfirmation = {
                showDeleteDialog = false
                viewModel.delete(Film(id = id!!, judul = judul, jenis = jenis, status = status, tanggal = tanggal))
                navController.popBackStack()
            }
        )
    }
}

@Composable
fun FormFilm(
    title: String,
    onTitleChange: (String) -> Unit,
    jenis: String,
    onJenisChange: (String) -> Unit,
    status: String,
    onStatusChange: (String) -> Unit,
    tanggal: Long,
    onTanggalChange: (Long) -> Unit,
    modifier: Modifier
)

 {
    val jenisOptions = listOf("Film", "Series")
    val statusOptions = listOf("Belum Ditonton", "Sedang Ditonton", "Selesai")

    var selectedJenis by remember { mutableStateOf(jenisOptions.first()) }
    var selectedStatus by remember { mutableStateOf(statusOptions.first()) }

// Tanggal
     val context = LocalContext.current
     val calendar = remember { Calendar.getInstance().apply { timeInMillis = tanggal } }

     val dateFormat = remember { SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()) }
     val formattedDate = remember(tanggal) { dateFormat.format(Date(tanggal)) }

     val datePickerDialog = remember {
         DatePickerDialog(
             context,
             { _, year, month, day ->
                 calendar.set(year, month, day)
                 onTanggalChange(calendar.timeInMillis)
             },
             calendar.get(Calendar.YEAR),
             calendar.get(Calendar.MONTH),
             calendar.get(Calendar.DAY_OF_MONTH)
         )
     }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Judul
        OutlinedTextField(
            value = title,
            onValueChange = { onTitleChange(it) },
            label = { Text(text = stringResource(R.string.judul)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

// Jenis
        ExposedDropdownMenuBoxSample(
            label = "Jenis",
            options = listOf("Film", "Series"),
            selectedOption = jenis,
            onOptionSelected = { onJenisChange(it) }
        )

// Status
        ExposedDropdownMenuBoxSample(
            label = "Status",
            options = listOf("Belum Ditonton", "Sedang Ditonton", "Selesai"),
            selectedOption = status,
            onOptionSelected = { onStatusChange(it) }
        )
//tanggal
        OutlinedTextField(
            value = formattedDate,
            onValueChange = {},
            readOnly = true,
            label = { Text("Tanggal Ditambahkan") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { datePickerDialog.show() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExposedDropdownMenuBoxSample(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(type = MenuAnchorType.PrimaryNotEditable)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        onOptionSelected(selectionOption)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun DetailScreenPreview() {
    TandaTontonTheme {
        DetailScreen(rememberNavController())
    }
}
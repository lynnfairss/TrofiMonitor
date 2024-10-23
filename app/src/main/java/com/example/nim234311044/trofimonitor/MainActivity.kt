package com.example.nim234311044.trofimonitor

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nim234311044.trofimonitor.ui.theme.TrofiMonitorTheme
import com.example.nim234311044.trofimonitor.R
import com.example.nim234311044.trofimonitor.R.drawable.default_club

class MainActivity() : ComponentActivity(), Parcelable {
    constructor(parcel: Parcel) : this()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrofiMonitorTheme {
                ClubScreen()
            }
        }
    }

    // Tambahkan properti imageResId untuk menyimpan referensi gambar klub
    data class Club(val name: String, val totalTrophies: Int, val internationalTrophies: Int, val imageResId: Int)

    @Composable
    fun ClubScreen() {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Tambahkan gambar latar belakang
            Image(
                painter = painterResource(id = R.drawable.putih), // Ganti dengan nama file gambar di drawable
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Konten klub di atas latar belakang
            var clubs by remember {
                mutableStateOf(
                    listOf(
                        Club("Liverpool", 95, 10, R.drawable.liverpool), // Tambahkan gambar Liverpool
                        Club("Chelsea", 91, 7, R.drawable.chelsea), // Tambahkan gambar Chelsea
                        Club("Manchester United", 68, 8, R.drawable.manutd), // Tambahkan gambar Manchester United
                        Club("Manchester City", 100, 5, R.drawable.mancirty), // Tambahkan gambar Manchester City
                        Club("Arsenal", 70, 3, R.drawable.arsenal), // Tambahkan gambar Arsenal
                        Club("Tottenham Hotspur", 50, 1, R.drawable.tottenham), // Tambahkan gambar Tottenham Hotspur
                        Club("Bayern Munich", 83, 15, R.drawable.bayernmunich) // Tambahkan gambar Bayern Munich
                    )
                )
            }

            var showAddDialog by remember { mutableStateOf(false) }

            // Ganti Column dengan LazyColumn
            LazyColumn(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                item {
                    StudentInfoScreen()
                }
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
                item {
                    Text(
                        text = "Daftar Klub Sepak Bola",
                        fontSize = 24.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
                // Tampilkan daftar klub beserta gambar mereka
                items(clubs) { club ->
                    Row(modifier = Modifier.padding(bottom = 16.dp)) {
                        // Gambar klub
                        Image(
                            painter = painterResource(id = club.imageResId),
                            contentDescription = "${club.name} logo",
                            modifier = Modifier
                                .size(64.dp) // Ukuran gambar
                                .padding(end = 16.dp) // Jarak antara gambar dan teks
                        )
                        // Teks informasi klub
                        Column {
                            Text(
                                text = club.name,
                                fontSize = 18.sp,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            Text(
                                text = "${club.totalTrophies} total trofi, ${club.internationalTrophies} trofi internasional, ${club.internationalTrophies} trofi internasional",
                                fontSize = 16.sp
                            )
                        }
                    }
                }
                // Keterangan untuk klub yang memiliki lebih dari 45 trofi
                val clubsWithMoreThan45Trophies = clubs.filter { it.totalTrophies > 45 }
                if (clubsWithMoreThan45Trophies.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Klub-klub yang memiliki lebih dari 45 trofi:",
                            fontSize = 18.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    items(clubsWithMoreThan45Trophies) { club ->
                        Text(
                            text = "${club.name}: ${club.totalTrophies} trofi",
                            fontSize = 16.sp,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item {
                    AddClubButton(onClick = { showAddDialog = true })
                }
            }

            if (showAddDialog) {
                AddMultipleClubsDialog(
                    onDismiss = { showAddDialog = false },
                    onAdd = { newClubs ->
                        clubs = clubs + newClubs
                        showAddDialog = false
                    }
                )
            }
        }
    }

    @Composable
    fun StudentInfoScreen() {
        val studentName = "Lyan Fairus Athallah"
        val studentID = "234311044"

        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Nama: $studentName",
                fontSize = 21.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "NIM: $studentID",
                fontSize = 17.sp
            )
        }
    }

    @Composable
    fun AddClubButton(onClick: () -> Unit) {
        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Tambah Club")
        }
    }

    @Composable
    fun AddMultipleClubsDialog(onDismiss: () -> Unit, onAdd: (List<Club>) -> Unit) {
        var clubData by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                Button(
                    onClick = {
                        if (clubData.isNotEmpty()) {
                            val newClubs = clubData.split(";").mapNotNull { entry ->
                                val parts = entry.split(",")
                                if (parts.size == 4) {
                                    val name = parts[0].trim()
                                    val totalTrophies = parts[1].trim().toIntOrNull() ?: 0
                                    val internationalTrophies = parts[2].trim().toIntOrNull() ?: 0
                                    val imageName = parts[3].trim()
                                    val imageResId = when (imageName) {
                                        "liverpool" -> R.drawable.liverpool
                                        "chelsea" -> R.drawable.chelsea
                                        "manutd" -> R.drawable.manutd
                                        "mancirty" -> R.drawable.mancirty
                                        "arsenal" -> R.drawable.arsenal
                                        "tottenham" -> R.drawable.tottenham
                                        "bayernmunich" -> R.drawable.bayernmunich
                                        else -> R.drawable.default_club // Gunakan gambar default jika tidak ditemukan
                                    }
                                    Club(name, totalTrophies, internationalTrophies, imageResId)
                                } else {
                                    null
                                }
                            }
                            onAdd(newClubs)
                        }
                    }
                ) {
                    Text("Tambah")
                }
            },
            dismissButton = {
                Button(onClick = onDismiss) {
                    Text("Batal")
                }
            },
            title = { Text("Tambah Klub Baru") },
            text = {
                Column {
                    OutlinedTextField(
                        value = clubData,
                        onValueChange = { clubData = it },
                        label = { Text("Data Klub (Nama, Total Trofi, Trofi Internasional, Trofi UCL;...)") },
                        placeholder = { Text("Contoh: Liverpool, 10, 2, liverpool; Chelsea, 5, 1, chelsea") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        )
    }


    override fun writeToParcel(parcel: Parcel, flags: Int) {}

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<MainActivity> {
        override fun createFromParcel(parcel: Parcel): MainActivity {
            return MainActivity(parcel)
        }

        override fun newArray(size: Int): Array<MainActivity?> {
            return arrayOfNulls(size)
        }
    }

    @Composable
    fun ClubScreenPreview() {
        TrofiMonitorTheme {
            ClubScreen()
        }
    }
}
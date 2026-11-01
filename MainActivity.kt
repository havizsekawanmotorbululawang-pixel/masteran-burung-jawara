package id.masteranburung.premium

import android.content.ComponentName
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.MoreExecutors

private data class BirdTrack(val title: String, val species: String, val url: String)

class MainActivity : ComponentActivity() {
    private var controller: MediaController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val token = SessionToken(this, ComponentName(this, PlaybackService::class.java))
        val future = MediaController.Builder(this, token).buildAsync()
        future.addListener({ controller = future.get() }, MoreExecutors.directExecutor())

        setContent {
            MaterialTheme(colorScheme = darkColorScheme(
                primary = Color(0xFFB9D58B),
                secondary = Color(0xFFD6B66D),
                background = Color(0xFF101812),
                surface = Color(0xFF1B2920)
            )) {
                MasteranApp(
                    onPlay = { track -> controller?.setMediaItem(MediaItem.fromUri(track.url)); controller?.prepare(); controller?.play() },
                    onPause = { controller?.pause() },
                    onResume = { controller?.play() }
                )
            }
        }
    }

    override fun onDestroy() {
        controller?.release()
        super.onDestroy()
    }
}

@Composable
private fun MasteranApp(onPlay: (BirdTrack) -> Unit, onPause: () -> Unit, onResume: () -> Unit) {
    val tracks = listOf(
        BirdTrack("Murai Batu – contoh streaming", "Murai Batu", "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"),
        BirdTrack("Kacer – contoh streaming", "Kacer", "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3"),
        BirdTrack("Cucak Ijo – contoh streaming", "Cucak Ijo", "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3"),
        BirdTrack("Kenari – contoh streaming", "Kenari", "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-4.mp3")
    )
    var selectedTab by remember { mutableStateOf("Beranda") }
    var search by remember { mutableStateOf("") }
    var playing by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf<BirdTrack?>(null) }

    Column(Modifier.fillMaxSize().background(Color(0xFF101812))) {
        Column(
            Modifier.fillMaxWidth()
                .background(Brush.verticalGradient(listOf(Color(0xFF294B35), Color(0xFF101812))))
                .padding(20.dp)
        ) {
            Text("MASTERAN BURUNG", color = Color(0xFFD6B66D), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
            Text("Premium", color = Color.White, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.ExtraBold)
            Text("Teman kicau harian • Online & Offline", color = Color(0xFFD5DED5))
            Spacer(Modifier.height(18.dp))
            Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF344C38)), shape = RoundedCornerShape(22.dp)) {
                Column(Modifier.padding(18.dp)) {
                    Text("Suasana alam, suara pilihan", color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("Putar masteran favorit. Audio online dapat diputar saat terhubung internet.", color = Color(0xFFD5DED5))
                }
            }
        }
        OutlinedTextField(
            value = search, onValueChange = { search = it },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            placeholder = { Text("Cari jenis burung atau suara...") },
            leadingIcon = { Icon(Icons.Default.Search, null) },
            singleLine = true
        )
        Row(Modifier.padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Semua", "Murai Batu", "Kacer", "Cucak Ijo", "Kenari").forEach { label ->
                AssistChip(onClick = { search = if (label == "Semua") "" else label }, label = { Text(label) })
            }
        }
        Text("Koleksi suara", Modifier.padding(start = 18.dp, top = 14.dp, bottom = 6.dp), color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        LazyColumn(Modifier.weight(1f), contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(tracks.filter { it.title.contains(search, true) || it.species.contains(search, true) }) { track ->
                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF1B2920)), shape = RoundedCornerShape(16.dp)) {
                    Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.MusicNote, null, tint = Color(0xFFD6B66D), modifier = Modifier.size(34.dp))
                        Column(Modifier.weight(1f).padding(horizontal = 10.dp)) {
                            Text(track.species, color = Color.White, fontWeight = FontWeight.Bold)
                            Text(track.title, color = Color(0xFFB8C7B9), style = MaterialTheme.typography.bodySmall)
                        }
                        IconButton(onClick = { selected = track; playing = true; onPlay(track) }) {
                            Icon(Icons.Default.PlayCircle, "Putar", tint = Color(0xFFB9D58B), modifier = Modifier.size(34.dp))
                        }
                    }
                }
            }
        }
        selected?.let {
            Row(Modifier.fillMaxWidth().background(Color(0xFF26382B)).padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Sedang dipilih", color = Color(0xFFB9D58B), style = MaterialTheme.typography.labelSmall)
                    Text(it.species, color = Color.White, fontWeight = FontWeight.Bold)
                }
                IconButton(onClick = { if (playing) { onPause(); playing = false } else { onResume(); playing = true } }) {
                    Icon(if (playing) Icons.Default.Pause else Icons.Default.PlayArrow, "Play/Pause", tint = Color.White)
                }
            }
        }
        NavigationBar(containerColor = Color(0xFF17231B)) {
            listOf("Beranda", "Kategori", "Download", "Favorit").forEach { tab ->
                NavigationBarItem(
                    selected = selectedTab == tab,
                    onClick = { selectedTab = tab },
                    icon = { Icon(when(tab) { "Beranda" -> Icons.Default.Home; "Kategori" -> Icons.Default.Category; "Download" -> Icons.Default.Download; else -> Icons.Default.Favorite }, null) },
                    label = { Text(tab) }
                )
            }
        }
    }
}

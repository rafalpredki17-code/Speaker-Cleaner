package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.TextButton
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

enum class ThemeMode { AUTO, LIGHT, DARK }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var currentTheme by remember { mutableStateOf(ThemeMode.AUTO) }
            var currentLanguage by remember { mutableStateOf(AppLanguage.PL) }

            val isDark = when(currentTheme) {
                ThemeMode.AUTO -> isSystemInDarkTheme()
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
            }

            MyApplicationTheme(darkTheme = isDark) {
                SpeakerCleanerApp(
                    currentTheme = currentTheme,
                    onThemeChange = { currentTheme = it },
                    currentLanguage = currentLanguage,
                    onLanguageChange = { currentLanguage = it }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpeakerCleanerApp(
    currentTheme: ThemeMode,
    onThemeChange: (ThemeMode) -> Unit,
    currentLanguage: AppLanguage,
    onLanguageChange: (AppLanguage) -> Unit
) {
    val strings = LocalizedStrings[currentLanguage] ?: LocalizedStrings[AppLanguage.EN]!!
    
    val coroutineScope = rememberCoroutineScope()
    val audioPlayer = remember { AudioPlayer() }
    var isPlaying by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }
    var selectedFrequency by remember { mutableFloatStateOf(165f) }

    DisposableEffect(Unit) {
        onDispose {
            if (isPlaying) {
                audioPlayer.stopPlaying()
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { 
                    Text(strings.appName, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onBackground) 
                },
                navigationIcon = {
                    var showMenu by remember { mutableStateOf(false) }
                    Box {
                        IconButton(onClick = { showMenu = !showMenu }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        androidx.compose.material3.DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            androidx.compose.material3.DropdownMenuItem(
                                text = { Text(strings.settings) },
                                onClick = { 
                                    showMenu = false
                                    showSettingsDialog = true
                                }
                            )
                            androidx.compose.material3.DropdownMenuItem(
                                text = { Text(strings.history) },
                                onClick = { showMenu = false }
                            )
                            androidx.compose.material3.DropdownMenuItem(
                                text = { Text(strings.about) },
                                onClick = { showMenu = false }
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Info, contentDescription = "Info", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { innerPadding ->
        if (showSettingsDialog) {
            AlertDialog(
                onDismissRequest = { showSettingsDialog = false },
                title = { Text(strings.settings) },
                text = {
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        Text("${strings.freqLabel}: ${selectedFrequency.toInt()} Hz")
                        Spacer(modifier = Modifier.height(16.dp))
                        Slider(
                            value = selectedFrequency,
                            onValueChange = { selectedFrequency = it },
                            valueRange = 100f..300f,
                            steps = 39 // To allow increments of 5Hz
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // Theme Switcher
                        Text(strings.themeLabel, fontWeight = FontWeight.Bold)
                        var themeDropdownExpanded by remember { mutableStateOf(false) }
                        Box {
                            TextButton(onClick = { themeDropdownExpanded = true }) {
                                val currentThemeLabel = when(currentTheme) {
                                    ThemeMode.AUTO -> strings.themeAuto
                                    ThemeMode.LIGHT -> strings.themeLight
                                    ThemeMode.DARK -> strings.themeDark
                                }
                                Text(currentThemeLabel)
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                            }
                            androidx.compose.material3.DropdownMenu(
                                expanded = themeDropdownExpanded,
                                onDismissRequest = { themeDropdownExpanded = false }
                            ) {
                                androidx.compose.material3.DropdownMenuItem(
                                    text = { Text(strings.themeAuto) },
                                    onClick = { onThemeChange(ThemeMode.AUTO); themeDropdownExpanded = false }
                                )
                                androidx.compose.material3.DropdownMenuItem(
                                    text = { Text(strings.themeLight) },
                                    onClick = { onThemeChange(ThemeMode.LIGHT); themeDropdownExpanded = false }
                                )
                                androidx.compose.material3.DropdownMenuItem(
                                    text = { Text(strings.themeDark) },
                                    onClick = { onThemeChange(ThemeMode.DARK); themeDropdownExpanded = false }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Language Switcher
                        Text(strings.languageLabel, fontWeight = FontWeight.Bold)
                        var langDropdownExpanded by remember { mutableStateOf(false) }
                        Box {
                            TextButton(onClick = { langDropdownExpanded = true }) {
                                Text(currentLanguage.displayName)
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                            }
                            androidx.compose.material3.DropdownMenu(
                                expanded = langDropdownExpanded,
                                onDismissRequest = { langDropdownExpanded = false }
                            ) {
                                AppLanguage.entries.forEach { lang ->
                                    androidx.compose.material3.DropdownMenuItem(
                                        text = { Text(lang.displayName) },
                                        onClick = { onLanguageChange(lang); langDropdownExpanded = false }
                                    )
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showSettingsDialog = false }) {
                        Text(strings.close)
                    }
                }
            )
        }

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            // Text section
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if (isPlaying) strings.cleaningInProgress else strings.tapToStart,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = (-0.5).sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (isPlaying) strings.emittingTone(selectedFrequency.toInt()) else strings.readyToClear,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(280.dp)
                )
            }

            // Visual concentric circles
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .padding(16.dp)
            ) {
                // Static outer rings
                Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f), CircleShape))
                Box(modifier = Modifier.fillMaxSize(0.8f).background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f), CircleShape))
                
                if (isPlaying) {
                    RippleAnimation(color = MaterialTheme.colorScheme.primary)
                }

                // Inner core
                Box(
                    modifier = Modifier
                        .fillMaxSize(0.6f)
                        .background(MaterialTheme.colorScheme.secondary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${selectedFrequency.toInt()}Hz",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Instructions Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Instructions",
                                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = strings.instructionsTitle,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                letterSpacing = 1.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        InstructionItem(strings.instr1)
                        Spacer(modifier = Modifier.height(4.dp))
                        InstructionItem(strings.instr2)
                        Spacer(modifier = Modifier.height(4.dp))
                        InstructionItem(strings.instr3)
                    }
                }

                // Main Action Button
                Button(
                    onClick = {
                        isPlaying = !isPlaying
                        if (isPlaying) {
                            coroutineScope.launch {
                                audioPlayer.startPlaying(selectedFrequency.toDouble())
                            }
                        } else {
                            audioPlayer.stopPlaying()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("play_stop_button"),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isPlaying) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.primary,
                        contentColor = if (isPlaying) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isPlaying) strings.stopCleaning else strings.startCleaning,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun InstructionItem(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSecondaryContainer
    )
}

@Composable
fun RippleAnimation(color: Color) {
    val infiniteTransition = rememberInfiniteTransition(label = "ripple")
    val scale1 by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "scale1"
    )
    val alpha1 by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "alpha1"
    )

    val scale2 by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, delayMillis = 1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "scale2"
    )
    val alpha2 by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, delayMillis = 1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "alpha2"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(140.dp)
                .scale(scale1)
                .background(color.copy(alpha = alpha1), shape = CircleShape)
        )
        Box(
            modifier = Modifier
                .size(140.dp)
                .scale(scale2)
                .background(color.copy(alpha = alpha2), shape = CircleShape)
        )
    }
}

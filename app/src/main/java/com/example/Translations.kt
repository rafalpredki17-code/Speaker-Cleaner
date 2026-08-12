package com.example

enum class AppLanguage(val code: String, val displayName: String) {
    EN("en", "English"),
    PL("pl", "Polski"),
    DE("de", "Deutsch"),
    RU("ru", "Русский"),
    FR("fr", "Français"),
    ES("es", "Español"),
    PT("pt", "Português"),
    IT("it", "Italiano")
}

data class AppStrings(
    val appName: String,
    val settings: String,
    val history: String,
    val about: String,
    val cleaningInProgress: String,
    val tapToStart: String,
    val emittingTone: (Int) -> String,
    val readyToClear: String,
    val instructionsTitle: String,
    val instr1: String,
    val instr2: String,
    val instr3: String,
    val startCleaning: String,
    val stopCleaning: String,
    val freqLabel: String,
    val close: String,
    val themeLabel: String,
    val themeAuto: String,
    val themeLight: String,
    val themeDark: String,
    val languageLabel: String
)

val LocalizedStrings = mapOf(
    AppLanguage.EN to AppStrings(
        appName = "Speaker Cleaner", settings = "Settings", history = "History", about = "About",
        cleaningInProgress = "Cleaning in progress...", tapToStart = "Tap to start cleaning",
        emittingTone = { "Emitting ${it}Hz tone" }, readyToClear = "Ready to clear water and dust",
        instructionsTitle = "INSTRUCTIONS", instr1 = "1. Turn volume to maximum.",
        instr2 = "2. Disconnect headphones.", instr3 = "3. Face speaker downwards.",
        startCleaning = "START CLEANING", stopCleaning = "STOP CLEANING", freqLabel = "Frequency",
        close = "Close", themeLabel = "Theme", themeAuto = "System Auto", themeLight = "Light",
        themeDark = "Dark", languageLabel = "Language"
    ),
    AppLanguage.PL to AppStrings(
        appName = "Czyszczenie Głośnika", settings = "Ustawienia", history = "Historia", about = "O aplikacji",
        cleaningInProgress = "Czyszczenie w toku...", tapToStart = "Naciśnij, aby rozpocząć",
        emittingTone = { "Emitowanie ${it}Hz" }, readyToClear = "Gotowy do usunięcia wody i kurzu",
        instructionsTitle = "INSTRUKCJE", instr1 = "1. Ustaw głośność na maksimum.",
        instr2 = "2. Odłącz słuchawki.", instr3 = "3. Skieruj głośnik w dół.",
        startCleaning = "START", stopCleaning = "STOP", freqLabel = "Częstotliwość",
        close = "Zamknij", themeLabel = "Motyw", themeAuto = "Systemowy", themeLight = "Jasny",
        themeDark = "Ciemny", languageLabel = "Język"
    ),
    AppLanguage.DE to AppStrings(
        appName = "Lautsprecherreiniger", settings = "Einstellungen", history = "Verlauf", about = "Über",
        cleaningInProgress = "Reinigung läuft...", tapToStart = "Tippen zum Starten",
        emittingTone = { "${it}Hz Ton ausgeben" }, readyToClear = "Bereit, Wasser zu entfernen",
        instructionsTitle = "ANLEITUNG", instr1 = "1. Lautstärke auf Maximum stellen.",
        instr2 = "2. Kopfhörer trennen.", instr3 = "3. Lautsprecher nach unten richten.",
        startCleaning = "STARTEN", stopCleaning = "STOPPEN", freqLabel = "Frequenz",
        close = "Schließen", themeLabel = "Design", themeAuto = "System", themeLight = "Hell",
        themeDark = "Dunkel", languageLabel = "Sprache"
    ),
    AppLanguage.RU to AppStrings(
        appName = "Очистка динамика", settings = "Настройки", history = "История", about = "О приложении",
        cleaningInProgress = "Идет очистка...", tapToStart = "Нажмите для старта",
        emittingTone = { "Излучение ${it}Гц" }, readyToClear = "Готов к удалению воды",
        instructionsTitle = "ИНСТРУКЦИИ", instr1 = "1. Установите макс. громкость.",
        instr2 = "2. Отключите наушники.", instr3 = "3. Поверните динамик вниз.",
        startCleaning = "СТАРТ", stopCleaning = "СТОП", freqLabel = "Частота",
        close = "Закрыть", themeLabel = "Тема", themeAuto = "Системная", themeLight = "Светлая",
        themeDark = "Темная", languageLabel = "Язык"
    ),
    AppLanguage.FR to AppStrings(
        appName = "Nettoyeur Haut-parleur", settings = "Paramètres", history = "Historique", about = "À propos",
        cleaningInProgress = "Nettoyage en cours...", tapToStart = "Appuyez pour démarrer",
        emittingTone = { "Émission ${it}Hz" }, readyToClear = "Prêt à nettoyer l'eau",
        instructionsTitle = "INSTRUCTIONS", instr1 = "1. Mettez le volume au maximum.",
        instr2 = "2. Débranchez les écouteurs.", instr3 = "3. Dirigez le haut-parleur vers le bas.",
        startCleaning = "DÉMARRER", stopCleaning = "ARRÊTER", freqLabel = "Fréquence",
        close = "Fermer", themeLabel = "Thème", themeAuto = "Système", themeLight = "Clair",
        themeDark = "Sombre", languageLabel = "Langue"
    ),
    AppLanguage.ES to AppStrings(
        appName = "Limpiador de Altavoz", settings = "Ajustes", history = "Historial", about = "Acerca de",
        cleaningInProgress = "Limpieza en curso...", tapToStart = "Toca para iniciar",
        emittingTone = { "Emitiendo ${it}Hz" }, readyToClear = "Listo para limpiar agua",
        instructionsTitle = "INSTRUCCIONES", instr1 = "1. Sube el volumen al máximo.",
        instr2 = "2. Desconecta los auriculares.", instr3 = "3. Pon el altavoz hacia abajo.",
        startCleaning = "INICIAR", stopCleaning = "DETENER", freqLabel = "Frecuencia",
        close = "Cerrar", themeLabel = "Tema", themeAuto = "Sistema", themeLight = "Claro",
        themeDark = "Oscuro", languageLabel = "Idioma"
    ),
    AppLanguage.PT to AppStrings(
        appName = "Limpador de Alto-falante", settings = "Configurações", history = "Histórico", about = "Sobre",
        cleaningInProgress = "Limpeza em andamento...", tapToStart = "Toque para iniciar",
        emittingTone = { "Emitindo ${it}Hz" }, readyToClear = "Pronto para limpar água",
        instructionsTitle = "INSTRUÇÕES", instr1 = "1. Aumente o volume ao máximo.",
        instr2 = "2. Desconecte os fones.", instr3 = "3. Vire o alto-falante para baixo.",
        startCleaning = "INICIAR", stopCleaning = "PARAR", freqLabel = "Frequência",
        close = "Fechar", themeLabel = "Tema", themeAuto = "Sistema", themeLight = "Claro",
        themeDark = "Escuro", languageLabel = "Idioma"
    ),
    AppLanguage.IT to AppStrings(
        appName = "Pulizia Altoparlante", settings = "Impostazioni", history = "Cronologia", about = "Informazioni",
        cleaningInProgress = "Pulizia in corso...", tapToStart = "Tocca per iniziare",
        emittingTone = { "Emissione ${it}Hz" }, readyToClear = "Pronto per rimuovere l'acqua",
        instructionsTitle = "ISTRUZIONI", instr1 = "1. Alza il volume al massimo.",
        instr2 = "2. Scollega le cuffie.", instr3 = "3. Rivolgi l'altoparlante verso il basso.",
        startCleaning = "INIZIA", stopCleaning = "FERMA", freqLabel = "Frequenza",
        close = "Chiudi", themeLabel = "Tema", themeAuto = "Sistema", themeLight = "Chiaro",
        themeDark = "Scuro", languageLabel = "Lingua"
    )
)

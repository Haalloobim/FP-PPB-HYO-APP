package com.app.hyo.presentation.chatbot

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.hyo.R // Ensure you have placeholder drawables
import com.app.hyo.presentation.Dimens
import com.app.hyo.ui.theme.Poppins
import com.app.hyo.ui.theme.HyoTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChatbotScreen(
    viewModel: ChatbotViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val messages by viewModel.messages.collectAsState()
    var textState by remember { mutableStateOf(TextFieldValue("")) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Automatically scroll to the bottom when new messages are added
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            ChatbotTopAppBar(
                onNavigateBack = onNavigateBack,
                onVoiceInputClick = { /* TODO: Handle voice input */ },
                onMoreOptionsClick = { /* TODO: Handle more options */ }
            )
        },
        bottomBar = {
            ChatInputBar(
                textState = textState,
                onTextChange = { textState = it },
                onSendClick = {
                    if (textState.text.isNotBlank()) {
                        viewModel.onEvent(ChatbotEvent.SendMessage(textState.text))
                        textState = TextFieldValue("") // Clear input field
                        coroutineScope.launch { // Scroll to bottom after sending
                            if (messages.isNotEmpty()) listState.animateScrollToItem(messages.size)
                        }
                    }
                },
                onAddAttachmentClick = { /* TODO: Handle attachment */ },
                onEmojiClick = { /* TODO: Handle emoji */ },
                onMicClick = { /* TODO: Handle voice input from bottom bar */ }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = Dimens.SmallPadding2), // Consistent padding
            verticalArrangement = Arrangement.spacedBy(Dimens.SmallPadding1)
        ) {
            items(messages) { message ->
                ChatMessageItem(message = message)
            }
            // Suggested Replies
            if (viewModel.shouldShowSuggestions.value && messages.lastOrNull()?.isFromUser == false) {
                item {
                    SuggestedReplies(
                        suggestions = listOf("Let's do it", "Great!", "Tell me more"),
                        onSuggestionClick = { suggestion ->
                            viewModel.onEvent(ChatbotEvent.SendMessage(suggestion))
                            coroutineScope.launch { // Scroll to bottom after sending
                                if (messages.isNotEmpty()) listState.animateScrollToItem(messages.size)
                            }
                        }
                    )
                }
            }
            item { Spacer(modifier = Modifier.height(Dimens.SmallPadding1)) } // Padding at the end of list
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatbotTopAppBar(
    onNavigateBack: () -> Unit,
    onVoiceInputClick: () -> Unit,
    onMoreOptionsClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                "Konsultasi Nadya-chan",
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold, // As per figma
                fontSize = 18.sp, // Adjust as per figma
                color = MaterialTheme.colorScheme.onSurface // Or your desired color
            )
        },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface // Or your desired color
                )
            }
        },
        actions = {
            IconButton(onClick = onVoiceInputClick) {
                Icon(
                    imageVector = Icons.Filled.Call, // Mic icon
                    contentDescription = "Voice Input",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant // Or your desired color
                )
            }
            IconButton(onClick = onMoreOptionsClick) {
                Icon(
                    imageVector = Icons.Filled.MoreVert, // More options icon
                    contentDescription = "More Options",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant // Or your desired color
                )
            }
        },
//        colors = TopAppBarDefaults.topAppBarColors(
//            containerColor = MaterialTheme.colorScheme.surface // Or a slightly different color if needed
//        )
    )
}

@Composable
fun ChatMessageItem(message: ChatMessage) {
    val alignment = if (message.isFromUser) Alignment.CenterEnd else Alignment.CenterStart
    val backgroundColor = if (message.isFromUser) MaterialTheme.colorScheme.primaryContainer
    else MaterialTheme.colorScheme.secondaryContainer
    val textColor = if (message.isFromUser) MaterialTheme.colorScheme.onPrimaryContainer
    else MaterialTheme.colorScheme.onSecondaryContainer
    val bubbleShape = if (message.isFromUser) {
        RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 0.dp)
    } else {
        RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 0.dp, bottomEnd = 16.dp)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = if (message.isFromUser) 64.dp else 0.dp,
                end = if (message.isFromUser) 0.dp else 64.dp,
                top = Dimens.SmallPadding1,
                bottom = Dimens.SmallPadding1
            ) // Ensure messages don't take full width
    ) {
        Row(
            modifier = Modifier.align(alignment),
            verticalAlignment = Alignment.Bottom
        ) {
            if (!message.isFromUser) {
                Image(
                    painter = painterResource(id = R.drawable.ic_search_document), // Replace with your bot avatar
                    contentDescription = "Bot Avatar",
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(end = Dimens.SmallPadding1)
                )
            }
            Column {
                if (message.imageResId != null) {
                    Image(
                        painter = painterResource(id = message.imageResId),
                        contentDescription = "Sent image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .sizeIn(maxHeight = 200.dp, maxWidth = 200.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .padding(bottom = Dimens.SmallPadding1)
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(bubbleShape)
                        .background(backgroundColor)
                        .padding(Dimens.SmallPadding2)
                ) {
                    Text(
                        text = message.text,
                        color = textColor,
                        fontFamily = Poppins,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatInputBar(
    textState: TextFieldValue,
    onTextChange: (TextFieldValue) -> Unit,
    onSendClick: () -> Unit,
    onAddAttachmentClick: () -> Unit,
    onEmojiClick: () -> Unit,
    onMicClick: () -> Unit
) {
    Surface(
        tonalElevation = 4.dp, // Add some elevation
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(Dimens.SmallPadding1)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onAddAttachmentClick) {
                Icon(Icons.Filled.Add, contentDescription = "Add Attachment")
            }
            IconButton(onClick = onEmojiClick) {
                Icon(Icons.Filled.Face, contentDescription = "Emoji")
            }

            BasicTextField(
                value = textState,
                onValueChange = onTextChange,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = Dimens.SmallPadding1)
                    .heightIn(min = 40.dp, max = 120.dp) // Allow multi-line input
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = Dimens.SmallPadding2, vertical = Dimens.SmallPadding1),
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = Poppins,
                    fontSize = 15.sp
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                decorationBox = { innerTextField ->
                    Box(contentAlignment = Alignment.CenterStart) {
                        if (textState.text.isEmpty()) {
                            Text("Tt", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)) // Placeholder "Tt"
                        }
                        innerTextField()
                    }
                }
            )

            if (textState.text.isNotBlank()) {
                IconButton(onClick = onSendClick) {
                    Icon(Icons.Filled.Send, contentDescription = "Send")
                }
            } else {
                IconButton(onClick = onMicClick) {
                    Icon(Icons.Filled.Call, contentDescription = "Voice Input")
                }
            }
        }
    }
}

@Composable
fun SuggestedReplies(
    suggestions: List<String>,
    onSuggestionClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.SmallPadding1, horizontal = Dimens.MediumPadding1),
        horizontalArrangement = Arrangement.End // Align to the right like in Figma
    ) {
        suggestions.forEach { suggestion ->
            SuggestionChip(text = suggestion, onClick = { onSuggestionClick(suggestion) })
            Spacer(modifier = Modifier.width(Dimens.SmallPadding1))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuggestionChip(
    text: String,
    onClick: () -> Unit
) {
    AssistChip(
        onClick = onClick,
        label = { Text(text, fontFamily = Poppins, fontSize = 13.sp) },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            labelColor = MaterialTheme.colorScheme.onTertiaryContainer
        ),
        shape = RoundedCornerShape(20.dp), // Make it more rounded
        border = null // No border for a cleaner look like Figma
    )
}


@Preview(showBackground = true, name = "Chatbot Screen Light")
@Composable
fun ChatbotScreenPreviewLight() {
    HyoTheme(darkTheme = false) {
        ChatbotScreen(onNavigateBack = {})
    }
}

@Preview(showBackground = true, name = "Chatbot Screen Dark")
@Composable
fun ChatbotScreenPreviewDark() {
    HyoTheme(darkTheme = true) {
        ChatbotScreen(onNavigateBack = {})
    }
}

@Preview(showBackground = true, name = "Chat Message Item - User")
@Composable
fun ChatMessageItemUserPreview() {
    HyoTheme {
        ChatMessageItem(message = ChatMessage(1, "Apakah ini sehat untuk bayi 6 bulan?", true, R.drawable.ic_network))
    }
}

@Preview(showBackground = true, name = "Chat Message Item - Bot")
@Composable
fun ChatMessageItemBotPreview() {
    HyoTheme {
        ChatMessageItem(message = ChatMessage(2, "Tentunya tidak ya, sistem pencernaan bayi masih sangat sensitif dan berkembang", false))
    }
}

@Preview(showBackground = true, name = "Chat Input Bar Empty")
@Composable
fun ChatInputBarEmptyPreview() {
    HyoTheme {
        var text by remember { mutableStateOf(TextFieldValue("")) }
        ChatInputBar(
            textState = text,
            onTextChange = {text = it},
            onSendClick = { /*TODO*/ },
            onAddAttachmentClick = { /*TODO*/ },
            onEmojiClick = { /*TODO*/ },
            onMicClick = { /*TODO*/ })
    }
}

@Preview(showBackground = true, name = "Chat Input Bar With Text")
@Composable
fun ChatInputBarWithTextPreview() {
    HyoTheme {
        var text by remember { mutableStateOf(TextFieldValue("Hello Nadya!")) }
        ChatInputBar(
            textState = text,
            onTextChange = {text = it},
            onSendClick = { /*TODO*/ },
            onAddAttachmentClick = { /*TODO*/ },
            onEmojiClick = { /*TODO*/ },
            onMicClick = { /*TODO*/ })
    }
}

@Preview(showBackground = true, name = "Suggested Replies Preview")
@Composable
fun SuggestedRepliesPreview() {
    HyoTheme {
        SuggestedReplies(suggestions = listOf("Let's do it", "Great!"), onSuggestionClick = {})
    }
}


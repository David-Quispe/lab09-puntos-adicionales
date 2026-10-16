package com.example.lab09_puntosadicionales.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.example.lab09_puntosadicionales.data.Amphibian
import com.example.lab09_puntosadicionales.ui.theme.Coral
import com.example.lab09_puntosadicionales.ui.theme.Forest
import com.example.lab09_puntosadicionales.ui.theme.ForestDark
import com.example.lab09_puntosadicionales.ui.theme.Lagoon
import com.example.lab09_puntosadicionales.ui.theme.Lilac
import com.example.lab09_puntosadicionales.ui.theme.Mango

@Composable
fun AmphibiansScreen(
    uiState: AmphibiansUiState,
    onRetry: () -> Unit,
) {
    Scaffold(containerColor = MaterialTheme.colorScheme.background) { innerPadding ->
        when (uiState) {
            AmphibiansUiState.Loading -> StatusScreen(
                title = "Buscando anfibios…",
                message = "Estamos explorando las especies para ti.",
                modifier = Modifier.padding(innerPadding),
                isLoading = true,
            )

            AmphibiansUiState.Error -> StatusScreen(
                title = "No pudimos conectar",
                message = "Revisa tu conexión a internet y vuelve a intentarlo.",
                modifier = Modifier.padding(innerPadding),
                onRetry = onRetry,
            )

            is AmphibiansUiState.Success -> AmphibiansCatalog(
                amphibians = uiState.amphibians,
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}

@Composable
private fun StatusScreen(
    title: String,
    message: String,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    onRetry: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = Lagoon)
        } else {
            Box(
                modifier = Modifier.size(76.dp).background(Coral, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text("?", style = MaterialTheme.typography.displaySmall, color = ForestDark)
            }
        }
        Spacer(Modifier.height(24.dp))
        Text(title, style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))
        Text(message, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        if (onRetry != null) {
            Spacer(Modifier.height(24.dp))
            Button(onClick = onRetry, colors = ButtonDefaults.buttonColors(containerColor = Forest)) {
                Text("Intentar de nuevo")
            }
        }
    }
}

@Composable
private fun AmphibiansCatalog(
    amphibians: List<Amphibian>,
    modifier: Modifier = Modifier,
) {
    var query by rememberSaveable { mutableStateOf("") }
    var selectedType by rememberSaveable { mutableStateOf("") }
    val types = amphibians.map { it.type }.distinct().sorted()
    val visible = filterAmphibians(amphibians, query, selectedType)

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        item {
            CatalogHeader(amphibians.size)
        }
        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    label = { Text("Buscar anfibios") },
                    placeholder = { Text("Nombre o descripción") },
                    trailingIcon = if (query.isNotEmpty()) {
                        { TextButton(onClick = { query = "" }) { Text("Borrar") } }
                    } else null,
                    singleLine = true,
                    shape = RoundedCornerShape(18.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(12.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        TypeChip(
                            label = "Todos",
                            selected = selectedType.isEmpty(),
                            onClick = { selectedType = "" },
                        )
                    }
                    items(types) { type ->
                        TypeChip(
                            label = typeLabel(type),
                            selected = selectedType == type,
                            onClick = { selectedType = type },
                        )
                    }
                }
                Spacer(Modifier.height(18.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Explora especies",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.weight(1f),
                    )
                    Text(
                        "${visible.size} de ${amphibians.size}",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }

        if (visible.isEmpty()) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text("Sin coincidencias", style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(8.dp))
                    Text("Prueba otra búsqueda o cambia el filtro.")
                }
            }
        } else {
            items(visible, key = { it.name }) { amphibian ->
                AmphibianCard(
                    amphibian = amphibian,
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
            }
        }
    }
}

@Composable
private fun CatalogHeader(count: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
            .background(Forest)
            .padding(horizontal = 24.dp, vertical = 28.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Anfibios",
                style = MaterialTheme.typography.displaySmall,
                color = Color.White,
                modifier = Modifier.weight(1f),
            )
            Box(
                modifier = Modifier.size(42.dp).background(Mango, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text("A", style = MaterialTheme.typography.titleLarge, color = ForestDark)
            }
        }
        Spacer(Modifier.height(9.dp))
        Text(
            "Un mundo entre agua y tierra",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Conoce $count especies sorprendentes y descubre qué las hace únicas.",
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFFD4EEE3),
        )
    }
}

@Composable
private fun TypeChip(label: String, selected: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label) },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = Mango,
            selectedLabelColor = ForestDark,
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        shape = RoundedCornerShape(14.dp),
    )
}

@Composable
private fun AmphibianCard(amphibian: Amphibian, modifier: Modifier = Modifier) {
    var expanded by rememberSaveable(amphibian.name) { mutableStateOf(false) }
    val accent = typeColor(amphibian.type)

    Card(
        modifier = modifier.fillMaxWidth().clickable { expanded = !expanded },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
    ) {
        Box {
            SubcomposeAsyncImage(
                model = amphibian.imageUrl,
                contentDescription = "Imagen de ${amphibian.name}",
                contentScale = ContentScale.Crop,
                loading = {
                    Box(
                        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center,
                    ) { CircularProgressIndicator(color = Lagoon) }
                },
                error = {
                    Box(
                        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center,
                    ) { Text("Imagen no disponible") }
                },
                modifier = Modifier.fillMaxWidth().height(204.dp),
            )
            Surface(
                modifier = Modifier.align(Alignment.TopEnd).padding(14.dp),
                shape = RoundedCornerShape(100.dp),
                color = accent,
            ) {
                Text(
                    typeLabel(amphibian.type),
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = ForestDark,
                )
            }
        }
        Column(modifier = Modifier.padding(18.dp)) {
            Text(amphibian.name, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            Text(
                amphibian.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = if (expanded) Int.MAX_VALUE else 3,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(Modifier.height(10.dp))
            Text(
                if (expanded) "Mostrar menos" else "Leer descripción completa",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

private fun typeLabel(type: String): String = when (type.lowercase()) {
    "frog" -> "Rana"
    "toad" -> "Sapo"
    "salamander" -> "Salamandra"
    else -> type
}

private fun typeColor(type: String): Color = when (type.lowercase()) {
    "frog" -> Color(0xFFAFF0A1)
    "toad" -> Mango
    "salamander" -> Lilac
    else -> Coral
}

internal fun filterAmphibians(
    amphibians: List<Amphibian>,
    query: String,
    selectedType: String,
): List<Amphibian> {
    val search = query.trim()
    return amphibians.filter { amphibian ->
        (selectedType.isEmpty() || amphibian.type == selectedType) &&
            (search.isEmpty() || amphibian.name.contains(search, ignoreCase = true) ||
                amphibian.description.contains(search, ignoreCase = true))
    }
}

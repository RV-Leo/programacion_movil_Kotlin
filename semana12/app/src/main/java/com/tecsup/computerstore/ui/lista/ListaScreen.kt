package com.tecsup.computerstore.ui.lista

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.computerstore.domain.model.Item
import com.tecsup.computerstore.ui.UiState
import com.tecsup.computerstore.ui.components.ErrorView
import com.tecsup.computerstore.ui.components.StoreProductCard
import com.tecsup.computerstore.ui.theme.StoreBlue
import com.tecsup.computerstore.ui.theme.StoreMint
import com.tecsup.computerstore.ui.theme.StoreMuted
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaScreen(
    viewModel: ListaViewModel,
    onItemClick: (Int) -> Unit = {},
    onFavoritosClick: () -> Unit = {},
    usuario: String = "Leo",
    esAdmin: Boolean = false,
    onAdminClick: () -> Unit = {},
    onCartClick: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val aviso by viewModel.aviso.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    fun handleFavoriteToggle(id: Int) {
        val state = uiState
        if (state is UiState.Exito) {
            val item = state.datos.find { it.id == id }
            val eraFavorito = item?.esFavorito ?: false
            viewModel.cambiarFavorito(id)
            scope.launch {
                snackbarHostState.currentSnackbarData?.dismiss()
                val mensaje = if (!eraFavorito) "agregado" else "eliminado"
                snackbarHostState.showSnackbar(mensaje)
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                val esAgregado = data.visuals.message == "agregado"
                Snackbar(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.padding(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = if (esAgregado) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = null,
                            tint = if (esAgregado) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = if (esAgregado) "Producto agregado a favoritos" else "Producto eliminado de favoritos",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            StoreHeader(
                usuario = usuario,
                esAdmin = esAdmin,
                onFavoritosClick = onFavoritosClick,
                onAdminClick = onAdminClick,
                onCartClick = onCartClick,
            )
            when (val state = uiState) {
                UiState.Cargando -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
                is UiState.Error -> ErrorView(
                    mensaje = state.mensaje,
                    onReintentar = { viewModel.cargarProductos() },
                    modifier = Modifier.fillMaxSize()
                )
                is UiState.Exito -> StoreCatalog(
                    products = state.datos,
                    aviso = aviso,
                    onRetry = viewModel::cargarProductos,
                    onFavoriteClick = ::handleFavoriteToggle,
                    onItemClick = onItemClick
                )
            }
        }
    }
}

@Composable
private fun StoreHeader(
    usuario: String,
    esAdmin: Boolean,
    onFavoritosClick: () -> Unit,
    onAdminClick: () -> Unit,
    onCartClick: () -> Unit,
) {
    Column(Modifier.fillMaxWidth().padding(start = 20.dp, end = 12.dp, top = 10.dp, bottom = 12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("COMPUTER STORE", style = MaterialTheme.typography.labelMedium, color = StoreBlue, fontWeight = FontWeight.Bold)
                Text("Hola, $usuario", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
            IconButton(onClick = onFavoritosClick) { Icon(Icons.Default.FavoriteBorder, "Mis favoritos") }
            IconButton(onClick = onCartClick) { Icon(Icons.Default.ShoppingCart, "Carrito") }
            if (esAdmin) IconButton(onClick = onAdminClick) { Icon(Icons.Default.AdminPanelSettings, "Administración") }
        }
        Text("Encuentra tu próxima computadora", style = MaterialTheme.typography.bodyMedium, color = StoreMuted)
    }
}

@Composable
private fun StoreCatalog(
    products: List<Item>,
    aviso: String?,
    onRetry: () -> Unit,
    onFavoriteClick: (Int) -> Unit,
    onItemClick: (Int) -> Unit
) {
    var query by rememberSaveable { mutableStateOf("") }
    var selectedCategory by rememberSaveable { mutableStateOf("Todo") }
    val categories = remember(products) { listOf("Todo") + products.map { it.categoria }.distinct().sortedBy { it.lowercase() } }
    val filtered = remember(products, query, selectedCategory) {
        products.filter { item ->
            val matchesText = item.nombre.contains(query, ignoreCase = true) ||
                item.descripcion.contains(query, ignoreCase = true) || item.categoria.contains(query, ignoreCase = true)
            matchesText && (selectedCategory == "Todo" || item.categoria == selectedCategory)
        }
    }

    val componentCategories = setOf("Placas madre", "Procesadores", "Memoria RAM", "Almacenamiento", "Tarjetas gráficas", "Fuentes de poder", "Refrigeración", "Gabinetes")
    val accessoryCategories = setOf("Accesorios", "Teclados", "Mouse", "USB hubs", "Monitores portátiles", "Micrófonos", "Webcams", "Audífonos")
    val catalogGroups = listOf(
        "Componentes" to filtered.filter { it.categoria in componentCategories },
        "Accesorios y periféricos" to filtered.filter { it.categoria in accessoryCategories },
        "Laptops y tablets" to filtered.filter { it.categoria in setOf("Laptops", "Tablets") }
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 28.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) { SearchBox(query, { query = it }) }
        item(span = { GridItemSpan(maxLineSpan) }) { StoreBanner() }
        if (aviso != null) item(span = { GridItemSpan(maxLineSpan) }) {
            Surface(color = MaterialTheme.colorScheme.secondaryContainer, shape = RoundedCornerShape(14.dp)) {
                Row(Modifier.padding(horizontal = 14.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(aviso, Modifier.weight(1f), style = MaterialTheme.typography.bodySmall)
                    Button(onClick = onRetry) { Text("Reintentar") }
                }
            }
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            Column {
                Text("Explora por categoría", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(top = 10.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    categories.forEach { category ->
                        val selected = selectedCategory == category
                        Surface(
                            modifier = Modifier.clip(CircleShape).clickable { selectedCategory = category },
                            color = if (selected) StoreBlue else MaterialTheme.colorScheme.surface,
                            shape = CircleShape,
                            shadowElevation = if (selected) 0.dp else 1.dp
                        ) {
                            Text(
                                category.replaceFirstChar { it.uppercase() },
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                                color = if (selected) Color.White else MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("Productos", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                Text("${filtered.size} artículos", style = MaterialTheme.typography.bodySmall, color = StoreMuted)
            }
        }
        if (filtered.isEmpty()) item(span = { GridItemSpan(maxLineSpan) }) {
            Text("No encontramos productos con esa búsqueda.", color = StoreMuted, modifier = Modifier.padding(vertical = 24.dp))
        }
        catalogGroups.filter { it.second.isNotEmpty() }.forEach { (title, groupProducts) ->
            item(span = { GridItemSpan(maxLineSpan) }) {
                Row(Modifier.fillMaxWidth().padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    Text("${groupProducts.size} productos", style = MaterialTheme.typography.bodySmall, color = StoreMuted)
                }
            }
            items(groupProducts, key = { it.id }) { product ->
                StoreProductCard(
                    item = product,
                    onClick = { onItemClick(product.id) },
                    onFavoriteClick = { onFavoriteClick(product.id) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun SearchBox(value: String, onValueChange: (String) -> Unit) {
    Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(MaterialTheme.colorScheme.surface).padding(horizontal = 15.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.Search, contentDescription = null, tint = StoreMuted)
        Spacer(Modifier.width(8.dp))
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f),
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface),
            decorationBox = { inner ->
                Box {
                    if (value.isEmpty()) Text("Buscar RAM, teclados, monitores...", color = StoreMuted, style = MaterialTheme.typography.bodyMedium)
                    inner()
                }
            }
        )
    }
}

@Composable
private fun StoreBanner() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Row(
            Modifier.background(Brush.linearGradient(listOf(StoreBlue, Color(0xFF173B72), StoreMint))).padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text("COMPONENTES Y ACCESORIOS\nPARA TU SETUP", color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
                Text("Precios y stock de demostración", color = Color.White.copy(alpha = .86f), style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 6.dp))
            }
            Icon(Icons.Default.Computer, contentDescription = null, tint = Color.White.copy(alpha = .95f), modifier = Modifier.size(64.dp))
        }
    }
}

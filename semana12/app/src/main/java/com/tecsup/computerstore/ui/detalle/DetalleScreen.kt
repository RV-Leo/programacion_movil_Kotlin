package com.tecsup.computerstore.ui.detalle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.computerstore.ui.components.ItemImage
import com.tecsup.computerstore.domain.model.Item
import com.tecsup.computerstore.ui.UiState
import com.tecsup.computerstore.ui.components.ErrorView
import com.tecsup.computerstore.ui.theme.StoreMuted
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleScreen(
    viewModel: DetalleViewModel,
    onVolverClick: () -> Unit,
    onAddToCart: (Item, Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val item = (uiState as? UiState.Exito<Item>)?.datos
    var cantidad by rememberSaveable(item?.id) { mutableIntStateOf(1) }
    LaunchedEffect(item?.id, item?.stock) {
        cantidad = if ((item?.stock ?: 0) > 0) 1 else 0
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Detalle del producto", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold) },
                navigationIcon = { IconButton(onClick = onVolverClick) { Icon(Icons.Default.ArrowBack, "Volver") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        bottomBar = {
            item?.let { product ->
                val subtotal = product.precio * cantidad
                Surface(shadowElevation = 10.dp, color = MaterialTheme.colorScheme.surface) {
                    Row(
                        Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text("Subtotal · $cantidad ${if (cantidad == 1) "unidad" else "unidades"}", style = MaterialTheme.typography.labelMedium, color = StoreMuted)
                            Text(formatSoles(subtotal), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        }
                        Button(
                            onClick = { onAddToCart(product, cantidad) },
                            enabled = cantidad > 0 && product.stock > 0,
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Agregar al carrito", modifier = Modifier.padding(horizontal = 6.dp, vertical = 5.dp))
                        }
                    }
                }
            }
        }
    ) { padding ->
        when (val state = uiState) {
            UiState.Cargando -> Column(Modifier.fillMaxSize().padding(padding), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) { CircularProgressIndicator() }
            is UiState.Error -> ErrorView(state.mensaje, { viewModel.cargarDetalle() }, Modifier.fillMaxSize().padding(padding))
            is UiState.Exito -> ProductDetails(
                item = state.datos,
                cantidad = cantidad,
                onCantidadChange = { cantidad = it },
                esFavorito = state.datos.esFavorito,
                onFavoritoClick = viewModel::cambiarFavorito,
                modifier = Modifier.fillMaxSize().padding(padding)
            )
        }
    }
}

@Composable
private fun ProductDetails(item: Item, cantidad: Int, onCantidadChange: (Int) -> Unit, esFavorito: Boolean, onFavoritoClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier.verticalScroll(rememberScrollState()).padding(horizontal = 20.dp)) {
        Box {
            ItemImage(
                item = item,
                modifier = Modifier.fillMaxWidth().height(290.dp).clip(RoundedCornerShape(26.dp))
            )
            IconButton(
                onClick = onFavoritoClick,
                modifier = Modifier.align(Alignment.TopEnd).padding(12.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surface.copy(alpha = .96f))
            ) {
                Icon(
                    imageVector = if (esFavorito) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = if (esFavorito) "Quitar de favoritos" else "Agregar a favoritos",
                    tint = if (esFavorito) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Text(item.categoria.replaceFirstChar { it.uppercase() }, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 22.dp))
        Text(item.nombre, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 6.dp))
        Row(Modifier.padding(top = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(color = MaterialTheme.colorScheme.secondaryContainer, shape = RoundedCornerShape(20.dp)) {
                Text(if (item.stock > 0) "Disponible" else "Sin stock", modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.secondary)
            }
            Text("${item.stock} unidades", modifier = Modifier.padding(start = 10.dp), style = MaterialTheme.typography.bodySmall, color = StoreMuted)
        }
        Text("Descripción", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 24.dp, bottom = 8.dp))
        Text(item.descripcion, style = MaterialTheme.typography.bodyLarge, color = StoreMuted)
        Text("Cantidad", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 26.dp, bottom = 10.dp))
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(18.dp)) {
            QuantityButton(enabled = cantidad > 1, icon = Icons.Default.Remove, label = "Reducir cantidad") { onCantidadChange((cantidad - 1).coerceAtLeast(1)) }
            Text(cantidad.toString(), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            QuantityButton(enabled = cantidad < item.stock, icon = Icons.Default.Add, label = "Aumentar cantidad") { onCantidadChange((cantidad + 1).coerceAtMost(item.stock)) }
            Text("Máximo ${item.stock}", style = MaterialTheme.typography.bodySmall, color = StoreMuted)
        }
        Text("Precio unitario: ${formatSoles(item.precio)}", style = MaterialTheme.typography.bodyMedium, color = StoreMuted, modifier = Modifier.padding(top = 10.dp))
        Spacer(Modifier.height(28.dp))
    }
}

@Composable
private fun QuantityButton(enabled: Boolean, icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, onClick: () -> Unit) {
    IconButton(onClick = onClick, enabled = enabled, modifier = Modifier.clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer)) {
        Icon(icon, contentDescription = label, tint = MaterialTheme.colorScheme.primary)
    }
}

private fun formatSoles(value: Double): String = "S/ ${String.format(Locale("es", "PE"), "%.2f", value)}"

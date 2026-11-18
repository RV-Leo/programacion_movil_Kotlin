package com.ronda.campusgps.ui
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ronda.campusgps.model.LugarConDistancia
import com.ronda.campusgps.util.formatearDistancia
@Composable fun ListaLugares(lugares: List<LugarConDistancia>, seleccionado: Int?, onClick: (Int)->Unit, modifier: Modifier=Modifier) {
 LazyColumn(modifier) { items(lugares, key={it.lugar.id}) { item ->
  val l=item.lugar
  ElevatedCard(Modifier.fillMaxWidth().padding(horizontal=12.dp, vertical=3.dp).clickable { onClick(l.id) }, colors=CardDefaults.elevatedCardColors(containerColor=if(seleccionado==l.id) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface)) {
   Row(Modifier.padding(12.dp), horizontalArrangement=Arrangement.SpaceBetween) { Column(Modifier.weight(1f)) { Text(l.nombre, style=MaterialTheme.typography.titleSmall); Text(l.categoria, style=MaterialTheme.typography.bodySmall, color=MaterialTheme.colorScheme.onSurfaceVariant) }; Text(item.metros?.let(::formatearDistancia) ?: "Sin calcular") }
  }
 } }
}

package com.tecsup.computerstore.data.repository

import com.tecsup.computerstore.domain.model.Item

/** Catálogo de muestra para categorías de PC que la API pública no incluye. */
object DemoCatalogo {
    val productos = listOf(
        Item(10_001, "MSI PRO B550M-P GEN3", "Placa madre micro-ATX para procesadores AMD Ryzen, con ranura M.2 y soporte para memoria DDR4.", 389.90, "Placas madre", "", 12),
        Item(10_002, "ASUS PRIME B760M-K D4", "Placa madre micro-ATX para Intel, con memoria DDR4, conectividad M.2 y diseño compacto.", 529.90, "Placas madre", "", 9),
        Item(10_003, "AMD Ryzen 5 5600", "Procesador de 6 núcleos para equipos de trabajo y gaming. Incluye cooler de fábrica.", 579.90, "Procesadores", "", 15),
        Item(10_004, "Intel Core i5-12400F", "Procesador de 6 núcleos compatible con placas madre Intel de 12.ª generación.", 649.90, "Procesadores", "", 11),
        Item(10_005, "Kingston Fury Beast 16 GB", "Kit de memoria RAM DDR4 de 3200 MHz para actualizar equipos de escritorio.", 159.90, "Memoria RAM", "", 20),
        Item(10_006, "Corsair Vengeance 32 GB", "Kit de memoria RAM DDR5 de 5600 MHz, dos módulos de 16 GB.", 369.90, "Memoria RAM", "", 10),
        Item(10_007, "WD Blue SN580 NVMe 1 TB", "Unidad de estado sólido M.2 NVMe para sistema operativo, aplicaciones y juegos.", 279.90, "Almacenamiento", "", 16),
        Item(10_008, "Crucial P3 Plus NVMe 2 TB", "SSD M.2 PCIe 4.0 de 2 TB para ampliar el almacenamiento del equipo.", 499.90, "Almacenamiento", "", 8),
        Item(10_009, "MSI GeForce RTX 4060 Ventus", "Tarjeta gráfica de 8 GB para jugar, crear contenido y acelerar aplicaciones compatibles.", 1_499.90, "Tarjetas gráficas", "", 5),
        Item(10_010, "Sapphire Pulse Radeon RX 7600", "Tarjeta gráfica de 8 GB con refrigeración de doble ventilador.", 1_299.90, "Tarjetas gráficas", "", 6),
        Item(10_011, "Corsair CV650 650 W", "Fuente de poder de 650 W con certificación 80 PLUS Bronze para PC de escritorio.", 299.90, "Fuentes de poder", "", 14),
        Item(10_012, "DeepCool AK400", "Disipador de aire para procesador con ventilador de 120 mm.", 159.90, "Refrigeración", "", 10),
        Item(10_013, "Corsair 4000D Airflow", "Gabinete mid-tower con panel frontal ventilado y espacio para componentes ATX.", 429.90, "Gabinetes", "", 7),
        Item(10_014, "Logitech G213 Prodigy", "Teclado para gaming de tamaño completo con teclas resistentes a salpicaduras.", 229.90, "Teclados", "", 13),
        Item(10_015, "Redragon Kumara K552", "Teclado mecánico compacto TKL con iluminación y switches táctiles.", 169.90, "Teclados", "", 18),
        Item(10_016, "Logitech G203 Lightsync", "Mouse óptico con seis botones programables e iluminación RGB.", 119.90, "Mouse", "", 20),
        Item(10_017, "Razer DeathAdder Essential", "Mouse ergonómico con sensor óptico y cinco botones programables.", 99.90, "Mouse", "", 17),
        Item(10_018, "UGREEN Hub USB 3.0 de 4 puertos", "Expande un puerto USB-A a cuatro puertos USB 3.0 para periféricos.", 69.90, "USB hubs", "", 22),
        Item(10_019, "Anker Hub USB-C 7 en 1", "Hub USB-C con puertos USB, HDMI, lector de tarjetas y carga passthrough.", 189.90, "USB hubs", "", 9),
        Item(10_020, "Arzopa Monitor portátil 15.6 pulgadas FHD", "Monitor portátil IPS Full HD con conexión USB-C y HDMI para laptop o consola.", 649.90, "Monitores portátiles", "", 6),
        Item(10_021, "FIFINE A8 Micrófono USB", "Micrófono de condensador USB para reuniones, streaming y grabación en escritorio.", 239.90, "Micrófonos", "", 12),
        Item(10_022, "HyperX SoloCast", "Micrófono USB compacto con patrón cardioide para voz y streaming.", 289.90, "Micrófonos", "", 8),
        Item(10_023, "Logitech C920 HD Pro", "Webcam Full HD con micrófonos estéreo para videollamadas y clases virtuales.", 279.90, "Webcams", "", 11),
        Item(10_024, "HyperX Cloud Stinger 2", "Audífonos over-ear con micrófono abatible para PC y consolas compatibles.", 219.90, "Audífonos", "", 9)
    )
}

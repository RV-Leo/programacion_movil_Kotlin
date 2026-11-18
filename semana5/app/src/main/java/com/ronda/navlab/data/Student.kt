package com.ronda.navlab.data

data class Student(
    val id: Int,
    val studentCode: String,
    val name: String,
    val career: String,
    val email: String,
    val faculty: String,
    val bio: String,
    val phone: String = "+51 987 654 321",
    val cycle: String = "VI Ciclo",
    val avatarInitials: String = name.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString("")
)

object StudentRepository {
    val sampleStudents = listOf(
        Student(
            id = 1,
            studentCode = "2024-0101",
            name = "Leonardo Ronda",
            career = "Ingeniería de Software",
            email = "leonardo.ronda@tecsup.edu.pe",
            faculty = "Ingeniería y Tecnología",
            bio = "Estudiante destacado con interés en desarrollo Android, Jetpack Compose y arquitecturas móviles avanzadas.",
            phone = "+51 987 654 321",
            cycle = "VI Ciclo"
        ),
        Student(
            id = 2,
            studentCode = "2024-0102",
            name = "María García",
            career = "Arquitectura",
            email = "maria.garcia@tecsup.edu.pe",
            faculty = "Diseño y Arquitectura",
            bio = "Apasionada por el diseño sostenible, el modelado 3D y la arquitectura bioclimática urbana.",
            phone = "+51 981 234 567",
            cycle = "V Ciclo"
        ),
        Student(
            id = 3,
            studentCode = "2024-0103",
            name = "Carlos Pérez",
            career = "Medicina",
            email = "carlos.perez@tecsup.edu.pe",
            faculty = "Ciencias de la Salud",
            bio = "Enfocado en la investigación biomédica, telemedicina y el uso de tecnología en diagnóstico asistido.",
            phone = "+51 972 345 678",
            cycle = "VII Ciclo"
        ),
        Student(
            id = 4,
            studentCode = "2024-0104",
            name = "Ana López",
            career = "Derecho",
            email = "ana.lopez@tecsup.edu.pe",
            faculty = "Ciencias Jurídicas",
            bio = "Especializada en derecho digital, protección de datos personales y regulación de inteligencia artificial.",
            phone = "+51 963 456 789",
            cycle = "IV Ciclo"
        ),
        Student(
            id = 5,
            studentCode = "2024-0105",
            name = "Luis Ramírez",
            career = "Administración",
            email = "luis.ramirez@tecsup.edu.pe",
            faculty = "Gestión y Negocios",
            bio = "Interesado en la innovación empresarial, fintech, análisis de datos de negocios y liderazgo de proyectos.",
            phone = "+51 954 567 890",
            cycle = "VI Ciclo"
        ),
        Student(
            id = 6,
            studentCode = "2024-0106",
            name = "Valeria Mendoza",
            career = "Diseño Gráfico",
            email = "valeria.mendoza@tecsup.edu.pe",
            faculty = "Diseño y Comunicación",
            bio = "Especialista en UX/UI, diseño de sistemas de diseño para aplicaciones móviles y diseño de interacción.",
            phone = "+51 945 678 901",
            cycle = "V Ciclo"
        )
    )

    fun getStudentById(id: Int): Student {
        return sampleStudents.find { it.id == id } ?: sampleStudents.first()
    }

    val currentUser = sampleStudents.first() // Leonardo Ronda
}

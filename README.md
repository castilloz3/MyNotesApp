📝 My Notes App (CRUD Android)
Aplicación móvil desarrollada nativamente para Android que implementa un sistema completo CRUD (Crear, Leer, Actualizar y Eliminar) para la gestión de notas y tareas diarias. Este proyecto fue diseñado aplicando buenas prácticas de arquitectura moderna en Android.

🚀 Características Principales
Crear notas: Añade nuevas tareas o notas mediante un DialogFragment interactivo.
Leer en tiempo real: Visualiza tus notas al instante gracias al uso de Kotlin Flows conectados directamente a la interfaz.
Actualizar: Modifica el título y la descripción de tus notas existentes de forma sencilla.
Eliminar: Borra notas de la base de datos de manera rápida mediante un toque largo en la tarjeta.
Persistencia local: Utiliza Room Database para almacenar la información de forma segura en el dispositivo.

🛠️ Tecnologías y Herramientas Utilizadas
Lenguaje: Kotlin
Interfaz de usuario: XML Layouts, Material Design y View Binding.
Arquitectura de Datos:
Room Database (SQLite ORM) con patrón Singleton para evitar bloqueos de concurrencia.
Kotlin Coroutines & Flow para operaciones asíncronas y reactivas en segundo plano.

Componentes de Android:
RecyclerView optimizado con ListAdapter y DiffUtil para un rendimiento fluido.
DialogFragment para los formularios modulares.
AppCompatActivity.

📸 Capturas de Pantalla
<img width="394" height="860" alt="{34AEF9E4-DA83-48B6-B1A8-8A28BA5AF004}" src="https://github.com/user-attachments/assets/35515fe6-02a6-4041-ad96-ad3c349920ff" />
<img width="393" height="867" alt="{6AEE3451-AB45-4F59-A5E3-F7728F6A14E6}" src="https://github.com/user-attachments/assets/2fae62d5-db97-4733-a888-2c946041a658" />
<img width="431" height="858" alt="{168736DE-FDB3-4CBA-AB5F-7726FAAF5178}" src="https://github.com/user-attachments/assets/f646b064-8356-4a45-afa4-ed00cc92a2df" />

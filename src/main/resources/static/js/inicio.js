//obtener modo oscuro por su id
const modoOscuroBtn = document.getElementById("modoOscuroBtn");
//obtener body por su etiqueta
const body = document.querySelector("body");
// Función para activar o desactivar el modo oscuro
function toggleModoOscuro() {
    // Cambiar clase para alternar el modo oscuro
    body.classList.toggle("modo-oscuro");
    
    // Verificar si el modo oscuro está activado y almacenar la preferencia en el Local Storage
    if (body.classList.contains("modo-oscuro")) {
        localStorage.setItem('modoOscuro', 'activo');
    } else {
        localStorage.setItem('modoOscuro', 'inactivo');
    }
}

// Agregar controlador de eventos al darle clic a modo oscuro
modoOscuroBtn.addEventListener("click", toggleModoOscuro);

// Verificar si se ha almacenado una preferencia de modo oscuro al cargar la página
if (localStorage.getItem('modoOscuro') === 'activo') {
    // Aplicar el modo oscuro
    body.classList.add('modo-oscuro');
}

//codigo de reloj para pagina inicio
const time = document.getElementById('time');
const date = document.getElementById('date');

const monthNames = ["January", "February", "March", "April", "May", "June",
    "July", "August", "September", "October", "November", "December"
];

const interval = setInterval(() => {

    const local = new Date();
    
    let day = local.getDate(),
        month = local.getMonth(),
        year = local.getFullYear();

    time.innerHTML = local.toLocaleTimeString();
    date.innerHTML = `${day} ${monthNames[month]} ${year}`;

}, 1000);
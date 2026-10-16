package com.seatour.seatour.config;

import com.seatour.seatour.dto.*;
import com.seatour.seatour.model.*;
import com.seatour.seatour.repository.*;
import com.seatour.seatour.service.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

/** Carga ficticia opcional, exclusiva de la entrega academica. */
@Component
@Order(2)
@ConditionalOnProperty(name = "seatour.demo.habilitado", havingValue = "true")
public class DatosDemoInicial implements ApplicationRunner {
    private static final String MARCADOR = "DEMO Avance 2 - Naturaleza";
    private static final String IMAGEN = "00000000-0000-4000-8000-000000000002.png";
    private final CategoriaTourRepository categoriasRepo;
    private final TourRepository toursRepo;
    private final EmbarcacionRepository barcosRepo;
    private final UsuarioRepository usuariosRepo;
    private final CategoriaTourService categorias;
    private final TourService tours;
    private final EmbarcacionService barcos;
    private final UsuarioService usuarios;
    private final SalidaProgramadaService salidas;
    private final Path directorioImagenes;

    public DatosDemoInicial(CategoriaTourRepository categoriasRepo, TourRepository toursRepo,
            EmbarcacionRepository barcosRepo, UsuarioRepository usuariosRepo,
            CategoriaTourService categorias, TourService tours, EmbarcacionService barcos,
            UsuarioService usuarios, SalidaProgramadaService salidas,
            @Value("${seatour.embarcaciones.imagenes.directorio:./uploads/embarcaciones}") String directorio) {
        this.categoriasRepo = categoriasRepo;
        this.toursRepo = toursRepo;
        this.barcosRepo = barcosRepo;
        this.usuariosRepo = usuariosRepo;
        this.categorias = categorias;
        this.tours = tours;
        this.barcos = barcos;
        this.usuarios = usuarios;
        this.salidas = salidas;
        this.directorioImagenes = Path.of(directorio).toAbsolutePath().normalize();
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        if (categoriasRepo.existsByNombreIgnoreCase(MARCADOR)) return;
        String password = System.getenv("SEATOUR_DEMO_PASSWORD");
        if (password == null || password.isBlank() || password.length() < 12 || password.length() > 1024) {
            throw new IllegalStateException("Configura SEATOUR_DEMO_PASSWORD con 12 a 1024 caracteres para cargar datos ficticios");
        }
        // Fallar ante colisiones; nunca modificar ni adoptar cuentas preexistentes.
        for (int i = 1; i <= 2; i++) {
            if (usuariosRepo.existsByCorreo("operador" + i + "@example.test")
                    || barcosRepo.existsByMatriculaIgnoreCase("DEMO-AV2-00" + i)
                    || toursRepo.existsByNombreIgnoreCase("DEMO Avance 2 - Recorrido " + i)) {
                throw new IllegalStateException("Hay registros que colisionan con la carga ficticia; no se modificaran");
            }
        }
        if (categoriasRepo.existsByNombreIgnoreCase("DEMO Avance 2 - Paseos")) {
            throw new IllegalStateException("La categoria ficticia ya existe sin el marcador; no se modificara");
        }
        Files.createDirectories(directorioImagenes);
        Path imagen = directorioImagenes.resolve(IMAGEN);
        if (Files.exists(imagen)) {
            throw new IllegalStateException("La imagen ficticia ya existe; no se sobrescribira");
        }
        try (var entrada = new ClassPathResource("demo/embarcacion.png").getInputStream()) {
            Files.copy(entrada, imagen);
        }
        LocalDate fecha = LocalDate.now(ZoneId.of("America/Lima")).plusDays(1);
        for (int i = 1; i <= 2; i++) {
            var categoria = categorias.crear(new CategoriaTourCreacion(
                    i == 1 ? MARCADOR : "DEMO Avance 2 - Paseos", "Categoria ficticia para exposicion", true));
            var tour = tours.crear(new TourCreacion("DEMO Avance 2 - Recorrido " + i,
                    "Recorrido ficticio para demostrar reservas; no es una oferta comercial",
                    60, new BigDecimal(i == 1 ? "80.00" : "100.00"), true, categoria.id(),
                    "/images/demo/embarcacion.png", i == 1 ? ZonaMaritima.MANCORA : ZonaMaritima.LOS_ORGANOS));
            var barco = barcos.crear(new EmbarcacionSolicitud("DEMO Embarcacion " + i,
                    "DEMO-AV2-00" + i, "LANCHA", 12, true, "/api/embarcaciones/imagenes/" + IMAGEN));
            var operador = usuarios.crearOperador(new UsuarioCreacion("Operador " + i,
                    "Ficticio", "operador" + i + "@example.test", password));
            var salida = new SalidaProgramada();
            salida.setFecha(fecha);
            salida.setHoraSalida(LocalTime.of(i == 1 ? 10 : 14, 0));
            salida.setTour(toursRepo.findById(tour.id()).orElseThrow());
            salida.setEmbarcacion(barcosRepo.findById(barco.id()).orElseThrow());
            salida.setOperador(usuariosRepo.findById(operador.id()).orElseThrow());
            salidas.crear(salida);
        }
    }
}

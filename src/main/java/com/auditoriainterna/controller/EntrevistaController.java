package com.auditoriainterna.controller;

import java.io.FileNotFoundException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.auditoriainterna.model.Auditoria;
import com.auditoriainterna.model.Empleado;
import com.auditoriainterna.model.Entrevista;
import com.auditoriainterna.service.AuditoriaService;
import com.auditoriainterna.service.EmpleadoService;
import com.auditoriainterna.service.EntrevistaService;
import com.auditoriainterna.serviceInterfaces.IAuditoriaService;
import com.auditoriainterna.util.reportes.EntrevistaReportGenerator;

import net.sf.jasperreports.engine.JRException;

@Controller
public class EntrevistaController {

	@Autowired
	private EntrevistaService entrevistaService;

	@Autowired
	private EmpleadoService empleadoService;
	
	@Autowired
	private AuditoriaService auditoriaService;

	
	
	@Autowired
	UserDetailsService userdetailsService;

	@GetMapping("/entrevista/{codigoAuditoria}")
	public String listarEntrevistasPorCodigoAuditoria(@PathVariable int codigoAuditoria, Model model,
			Principal principal) {
		UserDetails userDetails = userdetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("user", userDetails);
		
		List<Entrevista> entrevistas = entrevistaService.obtenerEntrevistasPorCodigoAuditoria(codigoAuditoria);
		int codigo = codigoAuditoria;
		
		Optional<Auditoria> auditoria=auditoriaService.listarId(codigo);
		model.addAttribute("auditoriaPresente", auditoria.isPresent());
		model.addAttribute("auditoria", auditoria.orElse(null));
		
		model.addAttribute("codigo", codigo);
		model.addAttribute("entrevistas", entrevistas);
		
		return "entrevista";
	}

	// Para Entrevistas busqueda de dni y llenado de campos
	@GetMapping("/entrevista/crear/{codigo}")
	public String formularioEntrevista(@PathVariable int codigo, Model model,Principal principal) {
		UserDetails userDetails = userdetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("user", userDetails);
		int _codigo = codigo;
		model.addAttribute("codigo", _codigo);
		model.addAttribute("empleado", new Empleado());
		model.addAttribute("entrevista", new Entrevista());
		return "formEntrevista";
	}

	// aca podriamos cambiar
	@PostMapping("/entrevista/crear/{codigo}")
	public String buscarPorDNI(@PathVariable int codigo, @ModelAttribute Empleado e, Model model,Principal principal) {
		UserDetails userDetails = userdetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("user", userDetails);
		Empleado empleado = empleadoService.obtenerEmpleadoPorDni(e.getDNI());
		if (empleado != null) {
			model.addAttribute("empleadoDni", empleado);
			int _codigo = codigo;
			model.addAttribute("codigo", _codigo);
			model.addAttribute("entrevista", new Entrevista());

			String pregunta1 = "", pregunta2 = "", pregunta3 = "";
			/* AREA DE TI */
			if (empleado.getCargo().equals("Analista de Datos")) {
				pregunta1 = "¿Cuál es el proceso que sigues para identificar y recopilar datos relevantes sobre el comportamiento de los clientes en la venta de celulares? ";
				pregunta2 = "¿Explícanos cómo aplicarías un proceso de análisis de datos para identificar oportunidades de mejora en las estrategias de venta de celulares?";
				pregunta3 = "¿Cómo aseguras la calidad de los datos que utilizas en tus análisis, y qué medidas tomas para garantizar la consistencia y confiabilidad de la información en el ámbito de la venta de celulares?";
			} else if (empleado.getCargo().equals("Especialista en Desarrollo Web")) {
				pregunta1 = "Describe el proceso que sigues para implementar nuevas funcionalidades en el sitio web de True Tech, desde la conceptualización hasta la implementación y pruebas finales. ¿Cómo gestionas posibles problemas durante este proceso?";
				pregunta2 = "En el contexto de la venta de celulares en línea, ¿cómo abordarías el proceso de optimización del rendimiento del sitio web? ";
				pregunta3 = "¿Cómo documentarías y evaluarías la seguridad del sitio web de True Tech, especialmente en relación con las transacciones de venta de celulares?";
			} else if (empleado.getCargo().equals("Especialista en Soporte Técnico")) {
				pregunta1 = "Explica el proceso que sigues para gestionar y resolver problemas reportados por clientes en la compra de celulares. Incluye cómo categorizas y priorizas los problemas, así como la comunicación con los clientes durante el proceso";
				pregunta2 = "En el contexto de las ventas en línea de celulares, describe el proceso de proporcionar soporte técnico a clientes que pueden tener limitada experiencia técnica. ¿Cómo te aseguras de que comprendan las soluciones propuestas?";
				pregunta3 = "¿Cómo documentas y actualizas los procedimientos y soluciones comunes en el soporte técnico para garantizar la consistencia y eficiencia del proceso?";
			} else if (empleado.getCargo().equals("Administrador de Sistemas")) {
				pregunta1 = "Describe el proceso que implementas para garantizar la disponibilidad continua de los servidores que respaldan las operaciones en línea de True Tech, incluyendo medidas preventivas y reactivas ante posibles interrupciones.";
				pregunta2 = "Ante un aumento inesperado en el tráfico del sitio web, explícanos el proceso que seguirías para escalar la infraestructura de TI. Incluye consideraciones de seguridad y rendimiento.";
				pregunta3 = "En el contexto de una auditoría de sistemas, ¿cómo documentarías y evaluarías la efectividad de las prácticas de copia de seguridad y recuperación de datos críticos para la venta de celulares?";
			} else if (empleado.getCargo().equals("Ingeniero de Redes")) {
				pregunta1 = "Describe el proceso que seguirías para diseñar e implementar una red que conecte las tiendas físicas de True Tech y sus oficinas centrales, garantizando eficiencia y seguridad en la transmisión de datos, especialmente relacionados con las ventas de celulares.";
				pregunta2 = "Frente a un evento de alto tráfico, como el lanzamiento de un nuevo modelo de celular, explica cómo asegurarías la capacidad de la red para manejar la carga adicional, incluyendo medidas de monitoreo y optimización.";
				pregunta3 = "¿Cómo documentarías y evaluarías la efectividad de las medidas de seguridad implementadas en la infraestructura de red, especialmente en relación con la venta de celulares?";
			}
			/* AREA DE VENTAS */
			else if (empleado.getCargo().equals("Director de Ventas")) {
				pregunta1 = "En el contexto de True Tech, describe el proceso que lideras para desarrollar estrategias de ventas de celulares a nivel global. Incluye cómo identificas nuevos mercados y oportunidades de crecimiento.";
				pregunta2 = "¿Cómo estableces y comunicas los objetivos de ventas para el equipo, y qué medidas tomas para evaluar el rendimiento y realizar ajustes estratégicos en función de los resultados?";
				pregunta3 = "¿Cómo documentarías y evaluarías la efectividad de las estrategias implementadas para impulsar las ventas de celulares?";

			} else if (empleado.getCargo().equals("Ejecutivo de Ventas")) {
				pregunta1 = "Explica el proceso que sigues para identificar y cultivar relaciones con clientes clave en la venta de celulares. ¿Cómo adaptas tu enfoque según las necesidades y preferencias de los clientes?";
				pregunta2 = "En el contexto de True Tech, describe el proceso de cierre de acuerdos de venta de celulares. ¿Cómo abordas las objeciones de los clientes y garantizas la satisfacción del cliente durante el proceso de venta?";
				pregunta3 = "¿Cómo documentas y gestionas el seguimiento postventa con los clientes que adquieren celulares de True Tech para fomentar relaciones a largo plazo y posibles oportunidades de venta adicionales?";

			} else if (empleado.getCargo().equals("Analista de ventas")) {
				pregunta1 = "Describe el proceso que utilizas para analizar datos de ventas de celulares, identificar tendencias y proporcionar informes que ayuden a la toma de decisiones estratégicas en True Tech.";
				pregunta2 = "¿Cómo aplicas el análisis de ventas para evaluar la efectividad de diferentes canales de venta de celulares y proponer ajustes en la estrategia de distribución?";
				pregunta3 = "¿Cómo documentarías y evaluarías la precisión y relevancia de los informes de análisis de ventas proporcionados a la dirección?";

			} else if (empleado.getCargo().equals("Representante de ventas")) {
				pregunta1 = "Explica el proceso que sigues desde el primer contacto con un cliente potencial hasta el cierre de una venta de celulares. ¿Cómo personalizas tu enfoque según las necesidades individuales de los clientes?";
				pregunta2 = "En True Tech, ¿cómo manejarías una situación en la que un cliente expresa preocupaciones sobre la calidad o características de un celular específico? Incluye medidas para resolver inquietudes y cerrar la venta.";
				pregunta3 = "Cómo documentas y comunicas la retroalimentación de los clientes sobre los productos de True Tech, y cómo influye esta retroalimentación en las estrategias de venta y desarrollo de productos?";

				/* AREA DE RRHH */
			} else if (empleado.getCargo().equals("Gerente de Recursos Humanos")) {
				pregunta1 = "En True Tech, describe el proceso que lideras para asegurar un ambiente laboral positivo y productivo. ¿Cómo fomentas la cultura empresarial y qué medidas tomas para mantener altos niveles de satisfacción y compromiso de los empleados?";
				pregunta2 = "¿Cuál es tu enfoque para gestionar el reclutamiento y retención de talento en el área de tecnología, específicamente para roles relacionados con la venta y desarrollo de celulares?";
				pregunta3 = "¿Cómo documentarías y evaluarías la efectividad de las políticas y prácticas de recursos humanos implementadas en True Tech?";

			} else if (empleado.getCargo().equals("Especialista en contratación y reclutamiento")) {
				pregunta1 = "Explica el proceso que sigues para identificar y atraer talento especializado en tecnología para True Tech. ¿Cómo adaptas tus estrategias de reclutamiento para perfiles específicos relacionados con la venta y desarrollo de celulares?";
				pregunta2 = "¿Cómo evalúas la idoneidad de los candidatos para roles específicos en True Tech, particularmente aquellos relacionados con la venta de celulares, y qué medidas tomas para garantizar un proceso de selección eficiente?";
				pregunta3 = "En una auditoría de reclutamiento, ¿cómo documentarías y evaluarías la eficacia de las prácticas de atracción de talento y la calidad de las contrataciones realizadas para roles en el área de venta de celulares?";
				
			} else if (empleado.getCargo().equals("Especialista en formación y desarrollo")) {
				pregunta1 = "Describe el proceso que implementas para identificar las necesidades de formación en True Tech, especialmente para empleados en roles relacionados con la venta de celulares. ¿Cómo diseñas programas de desarrollo profesional específicos para estos roles?";
				pregunta2 = "¿Cómo evalúas el impacto de los programas de formación en el rendimiento de los empleados en áreas clave, como la mejora de habilidades de venta de celulares y el conocimiento de productos?";
				pregunta3 = "¿Cómo documentarías y evaluarías la alineación de los programas de capacitación con los objetivos estratégicos de True Tech y la efectividad en el desarrollo de habilidades para la venta de celulares?";
				
			} else if (empleado.getCargo().equals("Especialista en relaciones laborales")) {
				pregunta1 = "Explica el proceso que sigues para gestionar relaciones laborales efectivas en True Tech, especialmente en un entorno donde la tecnología y la venta de celulares son aspectos clave. ¿Cómo abordas posibles conflictos y promueves la comunicación abierta?";
				pregunta2 = "¿Cuáles son tus estrategias para garantizar el cumplimiento de las normativas laborales y la equidad en el trato de los empleados, especialmente en roles relacionados con la venta de celulares?";
				pregunta3 = "¿Cómo documentarías y evaluarías la efectividad de las políticas y prácticas implementadas para mantener un entorno laboral justo y colaborativo, especialmente en áreas vinculadas a la venta de celulares?";
			}

			/* AREA MARKETING */
			else if (empleado.getCargo().equals("Gerente de Marketing")) {
				pregunta1 = "En True Tech, describe el proceso que lideras para desarrollar estrategias de marketing que impulsen las ventas de celulares. ¿Cómo alineas estas estrategias con los objetivos comerciales y la identidad de la marca?";
				pregunta2 = "¿Cuál es tu enfoque para la investigación de mercado en el área de tecnología, específicamente en el mercado de venta de celulares? ¿Cómo utilizas estos insights para adaptar y mejorar las estrategias de marketing?";
				pregunta3 = "¿Cómo documentarías y evaluarías el retorno de inversión (ROI) de las campañas de marketing digital y tradicional relacionadas con la venta de celulares?";
				
			} else if (empleado.getCargo().equals("Especialista en Marketing Digital")) {
				pregunta1 = "Explica el proceso que sigues para desarrollar y ejecutar campañas de marketing digital en True Tech, con un enfoque particular en la venta de celulares. ¿Cómo seleccionas canales digitales y plataformas relevantes para el público objetivo?";
				pregunta2 = "¿Cómo mides y analizas el rendimiento de las campañas de marketing digital para celulares? Incluye métricas clave y cómo utilizas estos datos para realizar ajustes y mejoras.";
				pregunta3 = "¿Cómo documentarías y evaluarías la efectividad de las estrategias implementadas, especialmente en la generación de leads y conversiones de ventas de celulares?";
				
			} else if (empleado.getCargo().equals("Especialista en Comunicaciones de Marketing")) {
				pregunta1 = "Describe el proceso que utilizas para desarrollar mensajes de marketing efectivos para los productos de True Tech, centrándote en la venta de celulares. ¿Cómo garantizas la coherencia del mensaje a lo largo de diferentes canales y plataformas?";
				pregunta2 = "¿Cuál es tu enfoque para la gestión de la reputación de la marca en el contexto de la venta de celulares? ¿Cómo manejas las comunicaciones en situaciones que podrían afectar la percepción del público sobre los productos de True Tech?";
				pregunta3 = "¿Cómo documentarías y evaluarías la alineación de los mensajes con los valores de la marca y la efectividad de la comunicación en la venta de celulares?";
				
			} else if (empleado.getCargo().equals("Analista de Mercado")) {
				pregunta1 = "Explica el proceso que sigues para recopilar y analizar datos de mercado en la industria de la tecnología, específicamente en la venta de celulares. ¿Cómo identificas oportunidades y desafíos en el mercado?";
				pregunta2 = "¿Cuáles son tus métodos para evaluar la demanda de productos, especialmente celulares, en diferentes segmentos de mercado? ¿Cómo utilizas esta información para informar las estrategias de marketing de True Tech?";
				pregunta3 = "¿Cómo documentarías y evaluarías la precisión y relevancia de los informes de investigación de mercado proporcionados a la dirección de True Tech?";
			}

			model.addAttribute("pregunta1", pregunta1);
			model.addAttribute("pregunta2", pregunta2);
			model.addAttribute("pregunta3", pregunta3);

		} else {
			model.addAttribute("empleadoDni", empleado);
			int _codigo = codigo;
			model.addAttribute("codigo", _codigo);
			model.addAttribute("entrevista", new Entrevista());
			model.addAttribute("mensaje", "Empleado no encontrado");
		}
		return "formEntrevista";
	}

	@PostMapping("/entrevista/guardar")
	public String guardarEntrevista(@ModelAttribute("entrevista") Entrevista entrevista) {
		entrevistaService.crearEntrevista(entrevista);
		int codigo = entrevista.getAuditoria().getCodigo_auditoria();
		System.out.println("Valor de código: " + codigo);
		return "redirect:/entrevista/" + codigo;
	}

	@GetMapping("/entrevista/editar/{id}/{dni}")
	public String editarEntrevista(@PathVariable int id, @PathVariable int dni, Model model,Principal principal) {
		UserDetails userDetails = userdetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("user", userDetails);
		Optional<Entrevista> entrevista = entrevistaService.listaId(id);
		model.addAttribute("entrevistaPresente", entrevista.isPresent());
		model.addAttribute("entrevista", entrevista.orElse(null));

		Empleado empleado = empleadoService.obtenerEmpleadoPorDni(dni);
		model.addAttribute("empleado", empleado);

		// aca tambien va
		String pregunta1 = "", pregunta2 = "", pregunta3 = "";
		/* AREA DE TI */
		if (empleado.getCargo().equals("Analista de Datos")) {
			pregunta1 = "¿Cuál es el proceso que sigues para identificar y recopilar datos relevantes sobre el comportamiento de los clientes en la venta de celulares? ";
			pregunta2 = "¿Explícanos cómo aplicarías un proceso de análisis de datos para identificar oportunidades de mejora en las estrategias de venta de celulares?";
			pregunta3 = "¿Cómo aseguras la calidad de los datos que utilizas en tus análisis, y qué medidas tomas para garantizar la consistencia y confiabilidad de la información en el ámbito de la venta de celulares?";
		} else if (empleado.getCargo().equals("Especialista en Desarrollo Web")) {
			pregunta1 = "Describe el proceso que sigues para implementar nuevas funcionalidades en el sitio web de True Tech, desde la conceptualización hasta la implementación y pruebas finales. ¿Cómo gestionas posibles problemas durante este proceso?";
			pregunta2 = "En el contexto de la venta de celulares en línea, ¿cómo abordarías el proceso de optimización del rendimiento del sitio web? ";
			pregunta3 = "¿Cómo documentarías y evaluarías la seguridad del sitio web de True Tech, especialmente en relación con las transacciones de venta de celulares?";
		} else if (empleado.getCargo().equals("Especialista en Soporte Técnico")) {
			pregunta1 = "Explica el proceso que sigues para gestionar y resolver problemas reportados por clientes en la compra de celulares. Incluye cómo categorizas y priorizas los problemas, así como la comunicación con los clientes durante el proceso";
			pregunta2 = "En el contexto de las ventas en línea de celulares, describe el proceso de proporcionar soporte técnico a clientes que pueden tener limitada experiencia técnica. ¿Cómo te aseguras de que comprendan las soluciones propuestas?";
			pregunta3 = "¿Cómo documentas y actualizas los procedimientos y soluciones comunes en el soporte técnico para garantizar la consistencia y eficiencia del proceso?";
		} else if (empleado.getCargo().equals("Administrador de Sistemas")) {
			pregunta1 = "Describe el proceso que implementas para garantizar la disponibilidad continua de los servidores que respaldan las operaciones en línea de True Tech, incluyendo medidas preventivas y reactivas ante posibles interrupciones.";
			pregunta2 = "Ante un aumento inesperado en el tráfico del sitio web, explícanos el proceso que seguirías para escalar la infraestructura de TI. Incluye consideraciones de seguridad y rendimiento.";
			pregunta3 = "En el contexto de una auditoría de sistemas, ¿cómo documentarías y evaluarías la efectividad de las prácticas de copia de seguridad y recuperación de datos críticos para la venta de celulares?";
		} else if (empleado.getCargo().equals("Ingeniero de Redes")) {
			pregunta1 = "Describe el proceso que seguirías para diseñar e implementar una red que conecte las tiendas físicas de True Tech y sus oficinas centrales, garantizando eficiencia y seguridad en la transmisión de datos, especialmente relacionados con las ventas de celulares.";
			pregunta2 = "Frente a un evento de alto tráfico, como el lanzamiento de un nuevo modelo de celular, explica cómo asegurarías la capacidad de la red para manejar la carga adicional, incluyendo medidas de monitoreo y optimización.";
			pregunta3 = "¿Cómo documentarías y evaluarías la efectividad de las medidas de seguridad implementadas en la infraestructura de red, especialmente en relación con la venta de celulares?";
		}
		/* AREA DE VENTAS */
		else if (empleado.getCargo().equals("Director de Ventas")) {
			pregunta1 = "En el contexto de True Tech, describe el proceso que lideras para desarrollar estrategias de ventas de celulares a nivel global. Incluye cómo identificas nuevos mercados y oportunidades de crecimiento.";
			pregunta2 = "¿Cómo estableces y comunicas los objetivos de ventas para el equipo, y qué medidas tomas para evaluar el rendimiento y realizar ajustes estratégicos en función de los resultados?";
			pregunta3 = "¿Cómo documentarías y evaluarías la efectividad de las estrategias implementadas para impulsar las ventas de celulares?";

		} else if (empleado.getCargo().equals("Ejecutivo de Ventas")) {
			pregunta1 = "Explica el proceso que sigues para identificar y cultivar relaciones con clientes clave en la venta de celulares. ¿Cómo adaptas tu enfoque según las necesidades y preferencias de los clientes?";
			pregunta2 = "En el contexto de True Tech, describe el proceso de cierre de acuerdos de venta de celulares. ¿Cómo abordas las objeciones de los clientes y garantizas la satisfacción del cliente durante el proceso de venta?";
			pregunta3 = "¿Cómo documentas y gestionas el seguimiento postventa con los clientes que adquieren celulares de True Tech para fomentar relaciones a largo plazo y posibles oportunidades de venta adicionales?";

		} else if (empleado.getCargo().equals("Analista de ventas")) {
			pregunta1 = "Describe el proceso que utilizas para analizar datos de ventas de celulares, identificar tendencias y proporcionar informes que ayuden a la toma de decisiones estratégicas en True Tech.";
			pregunta2 = "¿Cómo aplicas el análisis de ventas para evaluar la efectividad de diferentes canales de venta de celulares y proponer ajustes en la estrategia de distribución?";
			pregunta3 = "¿Cómo documentarías y evaluarías la precisión y relevancia de los informes de análisis de ventas proporcionados a la dirección?";

		} else if (empleado.getCargo().equals("Representante de ventas")) {
			pregunta1 = "Explica el proceso que sigues desde el primer contacto con un cliente potencial hasta el cierre de una venta de celulares. ¿Cómo personalizas tu enfoque según las necesidades individuales de los clientes?";
			pregunta2 = "En True Tech, ¿cómo manejarías una situación en la que un cliente expresa preocupaciones sobre la calidad o características de un celular específico? Incluye medidas para resolver inquietudes y cerrar la venta.";
			pregunta3 = "Cómo documentas y comunicas la retroalimentación de los clientes sobre los productos de True Tech, y cómo influye esta retroalimentación en las estrategias de venta y desarrollo de productos?";

			/* AREA DE RRHH */
		} else if (empleado.getCargo().equals("Gerente de Recursos Humanos")) {
			pregunta1 = "En True Tech, describe el proceso que lideras para asegurar un ambiente laboral positivo y productivo. ¿Cómo fomentas la cultura empresarial y qué medidas tomas para mantener altos niveles de satisfacción y compromiso de los empleados?";
			pregunta2 = "¿Cuál es tu enfoque para gestionar el reclutamiento y retención de talento en el área de tecnología, específicamente para roles relacionados con la venta y desarrollo de celulares?";
			pregunta3 = "¿Cómo documentarías y evaluarías la efectividad de las políticas y prácticas de recursos humanos implementadas en True Tech?";

		} else if (empleado.getCargo().equals("Especialista en contratación y reclutamiento")) {
			pregunta1 = "Explica el proceso que sigues para identificar y atraer talento especializado en tecnología para True Tech. ¿Cómo adaptas tus estrategias de reclutamiento para perfiles específicos relacionados con la venta y desarrollo de celulares?";
			pregunta2 = "¿Cómo evalúas la idoneidad de los candidatos para roles específicos en True Tech, particularmente aquellos relacionados con la venta de celulares, y qué medidas tomas para garantizar un proceso de selección eficiente?";
			pregunta3 = "En una auditoría de reclutamiento, ¿cómo documentarías y evaluarías la eficacia de las prácticas de atracción de talento y la calidad de las contrataciones realizadas para roles en el área de venta de celulares?";
			
		} else if (empleado.getCargo().equals("Especialista en formación y desarrollo")) {
			pregunta1 = "Describe el proceso que implementas para identificar las necesidades de formación en True Tech, especialmente para empleados en roles relacionados con la venta de celulares. ¿Cómo diseñas programas de desarrollo profesional específicos para estos roles?";
			pregunta2 = "¿Cómo evalúas el impacto de los programas de formación en el rendimiento de los empleados en áreas clave, como la mejora de habilidades de venta de celulares y el conocimiento de productos?";
			pregunta3 = "¿Cómo documentarías y evaluarías la alineación de los programas de capacitación con los objetivos estratégicos de True Tech y la efectividad en el desarrollo de habilidades para la venta de celulares?";
			
		} else if (empleado.getCargo().equals("Especialista en relaciones laborales")) {
			pregunta1 = "Explica el proceso que sigues para gestionar relaciones laborales efectivas en True Tech, especialmente en un entorno donde la tecnología y la venta de celulares son aspectos clave. ¿Cómo abordas posibles conflictos y promueves la comunicación abierta?";
			pregunta2 = "¿Cuáles son tus estrategias para garantizar el cumplimiento de las normativas laborales y la equidad en el trato de los empleados, especialmente en roles relacionados con la venta de celulares?";
			pregunta3 = "¿Cómo documentarías y evaluarías la efectividad de las políticas y prácticas implementadas para mantener un entorno laboral justo y colaborativo, especialmente en áreas vinculadas a la venta de celulares?";
		}

		/* AREA MARKETING */
		else if (empleado.getCargo().equals("Gerente de Marketing")) {
			pregunta1 = "En True Tech, describe el proceso que lideras para desarrollar estrategias de marketing que impulsen las ventas de celulares. ¿Cómo alineas estas estrategias con los objetivos comerciales y la identidad de la marca?";
			pregunta2 = "¿Cuál es tu enfoque para la investigación de mercado en el área de tecnología, específicamente en el mercado de venta de celulares? ¿Cómo utilizas estos insights para adaptar y mejorar las estrategias de marketing?";
			pregunta3 = "¿Cómo documentarías y evaluarías el retorno de inversión (ROI) de las campañas de marketing digital y tradicional relacionadas con la venta de celulares?";
			
		} else if (empleado.getCargo().equals("Especialista en Marketing Digital")) {
			pregunta1 = "Explica el proceso que sigues para desarrollar y ejecutar campañas de marketing digital en True Tech, con un enfoque particular en la venta de celulares. ¿Cómo seleccionas canales digitales y plataformas relevantes para el público objetivo?";
			pregunta2 = "¿Cómo mides y analizas el rendimiento de las campañas de marketing digital para celulares? Incluye métricas clave y cómo utilizas estos datos para realizar ajustes y mejoras.";
			pregunta3 = "¿Cómo documentarías y evaluarías la efectividad de las estrategias implementadas, especialmente en la generación de leads y conversiones de ventas de celulares?";
			
		} else if (empleado.getCargo().equals("Especialista en Comunicaciones de Marketing")) {
			pregunta1 = "Describe el proceso que utilizas para desarrollar mensajes de marketing efectivos para los productos de True Tech, centrándote en la venta de celulares. ¿Cómo garantizas la coherencia del mensaje a lo largo de diferentes canales y plataformas?";
			pregunta2 = "¿Cuál es tu enfoque para la gestión de la reputación de la marca en el contexto de la venta de celulares? ¿Cómo manejas las comunicaciones en situaciones que podrían afectar la percepción del público sobre los productos de True Tech?";
			pregunta3 = "¿Cómo documentarías y evaluarías la alineación de los mensajes con los valores de la marca y la efectividad de la comunicación en la venta de celulares?";
			
		} else if (empleado.getCargo().equals("Analista de Mercado")) {
			pregunta1 = "Explica el proceso que sigues para recopilar y analizar datos de mercado en la industria de la tecnología, específicamente en la venta de celulares. ¿Cómo identificas oportunidades y desafíos en el mercado?";
			pregunta2 = "¿Cuáles son tus métodos para evaluar la demanda de productos, especialmente celulares, en diferentes segmentos de mercado? ¿Cómo utilizas esta información para informar las estrategias de marketing de True Tech?";
			pregunta3 = "¿Cómo documentarías y evaluarías la precisión y relevancia de los informes de investigación de mercado proporcionados a la dirección de True Tech?";
		}
		
		model.addAttribute("pregunta1", pregunta1);
		model.addAttribute("pregunta2", pregunta2);
		model.addAttribute("pregunta3", pregunta3);

		return "formActualizarEntrevista";
	}

	@GetMapping("/entrevista/eliminar/{id}/{codigo}")
	public String eliminar(Model model, @PathVariable int id, @PathVariable int codigo) {
		entrevistaService.eliminar(id);
		return "redirect:/entrevista/" + codigo;
	}

	@GetMapping("/entrevista/empleado/{dni}")
	public String listarEntrevistasPorDni(@PathVariable int dni, Model model, Principal principal) {
		UserDetails userDetails = userdetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("user", userDetails);
		
		Empleado empleado=empleadoService.obtenerEmpleadoPorDni(dni);
		model.addAttribute("empleado",empleado); 
		model.addAttribute("dni", dni);
		
		List<Entrevista> entrevistas = entrevistaService.obtenerEntrevistasPorDni(dni);
		
		model.addAttribute("entrevistas", entrevistas);
		return "listadni";
	}

	@GetMapping("/aprobar/{id}")
	public String aprobarEntrevista(@PathVariable int id) {
		// esto es para cambiar el estado a Aprobado
		entrevistaService.aprobarEntrevista(id);
		return "redirect:/entrevistas";
	}

	@GetMapping("/rechazar/{id}")
	public String rechazarEntrevista(@PathVariable int id) {
		// este para cambiar el estado a Rechazado
		entrevistaService.rechazarEntrevista(id);
		return "redirect:/entrevistas";
	}
	
	@GetMapping("/exportar-pdf/{id}/{dni}")
	public ResponseEntity<byte[]> exportPdf(@PathVariable int id,@PathVariable int dni) throws JRException, FileNotFoundException {
		Empleado empleado = empleadoService.obtenerEmpleadoPorDni(dni);
		
		String pregunta1 = "", pregunta2 = "", pregunta3 = "";
		
		/* AREA DE TI */
		if (empleado.getCargo().equals("Analista de Datos")) {
			pregunta1 = "¿Cuál es el proceso que sigues para identificar y recopilar datos relevantes sobre el comportamiento de los clientes en la venta de celulares? ";
			pregunta2 = "¿Explícanos cómo aplicarías un proceso de análisis de datos para identificar oportunidades de mejora en las estrategias de venta de celulares?";
			pregunta3 = "¿Cómo aseguras la calidad de los datos que utilizas en tus análisis, y qué medidas tomas para garantizar la consistencia y confiabilidad de la información en el ámbito de la venta de celulares?";
		} else if (empleado.getCargo().equals("Especialista en Desarrollo Web")) {
			pregunta1 = "Describe el proceso que sigues para implementar nuevas funcionalidades en el sitio web de True Tech, desde la conceptualización hasta la implementación y pruebas finales. ¿Cómo gestionas posibles problemas durante este proceso?";
			pregunta2 = "En el contexto de la venta de celulares en línea, ¿cómo abordarías el proceso de optimización del rendimiento del sitio web? ";
			pregunta3 = "¿Cómo documentarías y evaluarías la seguridad del sitio web de True Tech, especialmente en relación con las transacciones de venta de celulares?";
		} else if (empleado.getCargo().equals("Especialista en Soporte Técnico")) {
			pregunta1 = "Explica el proceso que sigues para gestionar y resolver problemas reportados por clientes en la compra de celulares. Incluye cómo categorizas y priorizas los problemas, así como la comunicación con los clientes durante el proceso";
			pregunta2 = "En el contexto de las ventas en línea de celulares, describe el proceso de proporcionar soporte técnico a clientes que pueden tener limitada experiencia técnica. ¿Cómo te aseguras de que comprendan las soluciones propuestas?";
			pregunta3 = "¿Cómo documentas y actualizas los procedimientos y soluciones comunes en el soporte técnico para garantizar la consistencia y eficiencia del proceso?";
		} else if (empleado.getCargo().equals("Administrador de Sistemas")) {
			pregunta1 = "Describe el proceso que implementas para garantizar la disponibilidad continua de los servidores que respaldan las operaciones en línea de True Tech, incluyendo medidas preventivas y reactivas ante posibles interrupciones.";
			pregunta2 = "Ante un aumento inesperado en el tráfico del sitio web, explícanos el proceso que seguirías para escalar la infraestructura de TI. Incluye consideraciones de seguridad y rendimiento.";
			pregunta3 = "En el contexto de una auditoría de sistemas, ¿cómo documentarías y evaluarías la efectividad de las prácticas de copia de seguridad y recuperación de datos críticos para la venta de celulares?";
		} else if (empleado.getCargo().equals("Ingeniero de Redes")) {
			pregunta1 = "Describe el proceso que seguirías para diseñar e implementar una red que conecte las tiendas físicas de True Tech y sus oficinas centrales, garantizando eficiencia y seguridad en la transmisión de datos, especialmente relacionados con las ventas de celulares.";
			pregunta2 = "Frente a un evento de alto tráfico, como el lanzamiento de un nuevo modelo de celular, explica cómo asegurarías la capacidad de la red para manejar la carga adicional, incluyendo medidas de monitoreo y optimización.";
			pregunta3 = "¿Cómo documentarías y evaluarías la efectividad de las medidas de seguridad implementadas en la infraestructura de red, especialmente en relación con la venta de celulares?";
		}
		/* AREA DE VENTAS */
		else if (empleado.getCargo().equals("Director de Ventas")) {
			pregunta1 = "En el contexto de True Tech, describe el proceso que lideras para desarrollar estrategias de ventas de celulares a nivel global. Incluye cómo identificas nuevos mercados y oportunidades de crecimiento.";
			pregunta2 = "¿Cómo estableces y comunicas los objetivos de ventas para el equipo, y qué medidas tomas para evaluar el rendimiento y realizar ajustes estratégicos en función de los resultados?";
			pregunta3 = "¿Cómo documentarías y evaluarías la efectividad de las estrategias implementadas para impulsar las ventas de celulares?";

		} else if (empleado.getCargo().equals("Ejecutivo de Ventas")) {
			pregunta1 = "Explica el proceso que sigues para identificar y cultivar relaciones con clientes clave en la venta de celulares. ¿Cómo adaptas tu enfoque según las necesidades y preferencias de los clientes?";
			pregunta2 = "En el contexto de True Tech, describe el proceso de cierre de acuerdos de venta de celulares. ¿Cómo abordas las objeciones de los clientes y garantizas la satisfacción del cliente durante el proceso de venta?";
			pregunta3 = "¿Cómo documentas y gestionas el seguimiento postventa con los clientes que adquieren celulares de True Tech para fomentar relaciones a largo plazo y posibles oportunidades de venta adicionales?";

		} else if (empleado.getCargo().equals("Analista de ventas")) {
			pregunta1 = "Describe el proceso que utilizas para analizar datos de ventas de celulares, identificar tendencias y proporcionar informes que ayuden a la toma de decisiones estratégicas en True Tech.";
			pregunta2 = "¿Cómo aplicas el análisis de ventas para evaluar la efectividad de diferentes canales de venta de celulares y proponer ajustes en la estrategia de distribución?";
			pregunta3 = "¿Cómo documentarías y evaluarías la precisión y relevancia de los informes de análisis de ventas proporcionados a la dirección?";

		} else if (empleado.getCargo().equals("Representante de ventas")) {
			pregunta1 = "Explica el proceso que sigues desde el primer contacto con un cliente potencial hasta el cierre de una venta de celulares. ¿Cómo personalizas tu enfoque según las necesidades individuales de los clientes?";
			pregunta2 = "En True Tech, ¿cómo manejarías una situación en la que un cliente expresa preocupaciones sobre la calidad o características de un celular específico? Incluye medidas para resolver inquietudes y cerrar la venta.";
			pregunta3 = "Cómo documentas y comunicas la retroalimentación de los clientes sobre los productos de True Tech, y cómo influye esta retroalimentación en las estrategias de venta y desarrollo de productos?";

			/* AREA DE RRHH */
		} else if (empleado.getCargo().equals("Gerente de Recursos Humanos")) {
			pregunta1 = "En True Tech, describe el proceso que lideras para asegurar un ambiente laboral positivo y productivo. ¿Cómo fomentas la cultura empresarial y qué medidas tomas para mantener altos niveles de satisfacción y compromiso de los empleados?";
			pregunta2 = "¿Cuál es tu enfoque para gestionar el reclutamiento y retención de talento en el área de tecnología, específicamente para roles relacionados con la venta y desarrollo de celulares?";
			pregunta3 = "¿Cómo documentarías y evaluarías la efectividad de las políticas y prácticas de recursos humanos implementadas en True Tech?";

		} else if (empleado.getCargo().equals("Especialista en contratación y reclutamiento")) {
			pregunta1 = "Explica el proceso que sigues para identificar y atraer talento especializado en tecnología para True Tech. ¿Cómo adaptas tus estrategias de reclutamiento para perfiles específicos relacionados con la venta y desarrollo de celulares?";
			pregunta2 = "¿Cómo evalúas la idoneidad de los candidatos para roles específicos en True Tech, particularmente aquellos relacionados con la venta de celulares, y qué medidas tomas para garantizar un proceso de selección eficiente?";
			pregunta3 = "En una auditoría de reclutamiento, ¿cómo documentarías y evaluarías la eficacia de las prácticas de atracción de talento y la calidad de las contrataciones realizadas para roles en el área de venta de celulares?";
			
		} else if (empleado.getCargo().equals("Especialista en formación y desarrollo")) {
			pregunta1 = "Describe el proceso que implementas para identificar las necesidades de formación en True Tech, especialmente para empleados en roles relacionados con la venta de celulares. ¿Cómo diseñas programas de desarrollo profesional específicos para estos roles?";
			pregunta2 = "¿Cómo evalúas el impacto de los programas de formación en el rendimiento de los empleados en áreas clave, como la mejora de habilidades de venta de celulares y el conocimiento de productos?";
			pregunta3 = "¿Cómo documentarías y evaluarías la alineación de los programas de capacitación con los objetivos estratégicos de True Tech y la efectividad en el desarrollo de habilidades para la venta de celulares?";
			
		} else if (empleado.getCargo().equals("Especialista en relaciones laborales")) {
			pregunta1 = "Explica el proceso que sigues para gestionar relaciones laborales efectivas en True Tech, especialmente en un entorno donde la tecnología y la venta de celulares son aspectos clave. ¿Cómo abordas posibles conflictos y promueves la comunicación abierta?";
			pregunta2 = "¿Cuáles son tus estrategias para garantizar el cumplimiento de las normativas laborales y la equidad en el trato de los empleados, especialmente en roles relacionados con la venta de celulares?";
			pregunta3 = "¿Cómo documentarías y evaluarías la efectividad de las políticas y prácticas implementadas para mantener un entorno laboral justo y colaborativo, especialmente en áreas vinculadas a la venta de celulares?";
		}

		/* AREA MARKETING */
		else if (empleado.getCargo().equals("Gerente de Marketing")) {
			pregunta1 = "En True Tech, describe el proceso que lideras para desarrollar estrategias de marketing que impulsen las ventas de celulares. ¿Cómo alineas estas estrategias con los objetivos comerciales y la identidad de la marca?";
			pregunta2 = "¿Cuál es tu enfoque para la investigación de mercado en el área de tecnología, específicamente en el mercado de venta de celulares? ¿Cómo utilizas estos insights para adaptar y mejorar las estrategias de marketing?";
			pregunta3 = "¿Cómo documentarías y evaluarías el retorno de inversión (ROI) de las campañas de marketing digital y tradicional relacionadas con la venta de celulares?";
			
		} else if (empleado.getCargo().equals("Especialista en Marketing Digital")) {
			pregunta1 = "Explica el proceso que sigues para desarrollar y ejecutar campañas de marketing digital en True Tech, con un enfoque particular en la venta de celulares. ¿Cómo seleccionas canales digitales y plataformas relevantes para el público objetivo?";
			pregunta2 = "¿Cómo mides y analizas el rendimiento de las campañas de marketing digital para celulares? Incluye métricas clave y cómo utilizas estos datos para realizar ajustes y mejoras.";
			pregunta3 = "¿Cómo documentarías y evaluarías la efectividad de las estrategias implementadas, especialmente en la generación de leads y conversiones de ventas de celulares?";
			
		} else if (empleado.getCargo().equals("Especialista en Comunicaciones de Marketing")) {
			pregunta1 = "Describe el proceso que utilizas para desarrollar mensajes de marketing efectivos para los productos de True Tech, centrándote en la venta de celulares. ¿Cómo garantizas la coherencia del mensaje a lo largo de diferentes canales y plataformas?";
			pregunta2 = "¿Cuál es tu enfoque para la gestión de la reputación de la marca en el contexto de la venta de celulares? ¿Cómo manejas las comunicaciones en situaciones que podrían afectar la percepción del público sobre los productos de True Tech?";
			pregunta3 = "¿Cómo documentarías y evaluarías la alineación de los mensajes con los valores de la marca y la efectividad de la comunicación en la venta de celulares?";
			
		} else if (empleado.getCargo().equals("Analista de Mercado")) {
			pregunta1 = "Explica el proceso que sigues para recopilar y analizar datos de mercado en la industria de la tecnología, específicamente en la venta de celulares. ¿Cómo identificas oportunidades y desafíos en el mercado?";
			pregunta2 = "¿Cuáles son tus métodos para evaluar la demanda de productos, especialmente celulares, en diferentes segmentos de mercado? ¿Cómo utilizas esta información para informar las estrategias de marketing de True Tech?";
			pregunta3 = "¿Cómo documentarías y evaluarías la precisión y relevancia de los informes de investigación de mercado proporcionados a la dirección de True Tech?";
		}
		
		  Map<String, Object> params = new HashMap<>();
	      params.put("parametro1", pregunta1);
	      params.put("parametro2", pregunta2);
	      params.put("parametro3", pregunta3);
	      params.put("logo", "classpath:static/img/truetech.png");
	      
	      
	      
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.setContentDispositionFormData("petsReport", "entrevistaReporte.pdf");
		return ResponseEntity.ok().headers(headers).body(entrevistaService.exportPdf(id,params));
	}
	

}

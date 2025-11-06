package com.example.museo_v2.build;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

/**
 * <h2>Pruebas de arquitectura del proyecto Museo_v2</h2>
 *
 * <p>Esta clase utiliza <b>ArchUnit</b> para validar las convenciones de arquitectura del
 * proyecto, asegurando una correcta separación por capas, el cumplimiento de las
 * convenciones de nombres y el uso apropiado de la inyección de dependencias.</p>
 *
 * <p>Estas reglas ayudan a mantener la mantenibilidad, coherencia y calidad del código
 * dentro del proyecto Spring Boot.</p>
 *
 * <p>El paquete analizado es: {@code com.example.museo_v2}</p>
 *
 * @author Juan
 * @version 1.0
 */
@AnalyzeClasses(packages = "com.example.museo_v2")
public class ArchitectureTest {

    /**
     * <h3>Regla 1: Cumplimiento de arquitectura por capas</h3>
     *
     * <p>Verifica que las capas del sistema estén correctamente organizadas y que
     * las dependencias sigan el flujo correcto:</p>
     *
     * <ul>
     *     <li>Controller → puede acceder a Service y Model</li>
     *     <li>Service → puede acceder a Repository y Model</li>
     *     <li>Repository → solo puede acceder a Model</li>
     *     <li>Model → no puede acceder a ninguna capa</li>
     * </ul>
     *
     * <p>Esto garantiza una estructura limpia, donde las capas superiores dependen de las inferiores,
     * evitando dependencias circulares o acoplamiento indebido.</p>
     */
    @ArchTest
    public static final ArchRule layeredArchitecture_should_beRespected =
        layeredArchitecture()
            .consideringAllDependencies()
            .layer("Controller").definedBy("..controller..")
            .layer("Service").definedBy("..service..")
            .layer("Repository").definedBy("..repository..")
            .layer("Model").definedBy("..model..")
            .whereLayer("Controller").mayOnlyAccessLayers("Service", "Model")
            .whereLayer("Service").mayOnlyAccessLayers("Repository", "Model")
            .whereLayer("Repository").mayOnlyAccessLayers("Model")
            .whereLayer("Model").mayNotAccessAnyLayer();

    /**
     * <h3>Regla 2.1: Convención de nombres para Controladores</h3>
     *
     * <p>Verifica que todas las clases dentro del paquete
     * {@code ..controller..} terminen con la palabra <b>"Controller"</b>.</p>
     *
     * <p>Esto facilita la identificación rápida de las clases que
     * manejan las peticiones HTTP.</p>
     */
    @ArchTest
    public static final ArchRule controllers_should_end_with_Controller =
        classes()
            .that().resideInAPackage("..controller..")
            .should().haveSimpleNameEndingWith("Controller");

    /**
     * <h3>Regla 2.2: Convención de nombres para Servicios</h3>
     *
     * <p>Verifica que las clases en el paquete {@code ..service..}
     * terminen con <b>"Service"</b> o <b>"ServiceImpl"</b>.</p>
     *
     * <p>Esto mantiene la coherencia en la capa de lógica de negocio
     * y permite distinguir implementaciones de interfaces.</p>
     */
    @ArchTest
    public static final ArchRule services_should_end_with_Service_or_ServiceImpl =
        classes()
            .that().resideInAPackage("..service..")
            .should().haveSimpleNameEndingWith("Service")
            .orShould().haveSimpleNameEndingWith("ServiceImpl");

    /**
     * <h3>Regla 2.3: Convención de nombres para Repositorios</h3>
     *
     * <p>Valida que todas las clases dentro de {@code ..repository..}
     * terminen con la palabra <b>"Repository"</b>.</p>
     *
     * <p>Esto asegura uniformidad en la capa de acceso a datos.</p>
     */
    @ArchTest
    public static final ArchRule repositories_should_end_with_Repository =
        classes()
            .that().resideInAPackage("..repository..")
            .should().haveSimpleNameEndingWith("Repository");

    /**
     * <h3>Regla 3: Prohibición de inyección por campo</h3>
     *
     * <p>Evita el uso de la anotación {@code @Autowired} directamente sobre campos
     * dentro de las capas {@code service} y {@code controller}.</p>
     *
     * <p>Motivo: la inyección por constructor promueve un código más limpio, testable
     * y compatible con buenas prácticas de desarrollo en Spring.</p>
     */
    @ArchTest
    public static final ArchRule no_field_injection_in_service_or_controller =
        fields()
            .that().areDeclaredInClassesThat()
            .resideInAnyPackage("..service..", "..controller..")
            .should().notBeAnnotatedWith("org.springframework.beans.factory.annotation.Autowired")
            .because("La inyección por constructor es más limpia y facilita el testing.");

    /**
     * <h3>Regla 4: Prueba de funcionamiento de ArchUnit</h3>
     *
     * <p>Esta regla <b>forzada a fallar</b> sirve para comprobar que
     * ArchUnit está ejecutando correctamente los tests.</p>
     *
     * <p>Busca una clase llamada {@code ClaseQueNoExiste} dentro del
     * paquete {@code ..controller..}, que obviamente no existe.</p>
     *
     * <p>Si ArchUnit reporta una violación aquí, significa que
     * está funcionando correctamente ✅.</p>
     */
    @ArchTest
    public static final ArchRule archunit_should_work =
        classes()
            .that().resideInAnyPackage("..controller..")
            .should().haveSimpleName("ClaseQueNoExiste");

}

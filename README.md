# Correoapp
Aplicación presentada como TFG. Usa microservicios REST de Spring Boot y Angular

# Aplicación web de microservicios rest de Spring y Angular

La aplicación web permite enviar correos HTML. La aplicación gestiona las empresas, los usuarios, los contactos y las plantillas. Un usuario de una empresa puede redactar una plantilla y ordenar su envío. La orden se enviará a los contactos de su empresa.

El motivo del desarrollo ha sido el aprendizaje de nuevas tecnologías y arquitecturas que el mercado laboral demanda. El backend tiene una arquitectura de microservicios desarrollados con Spring Boot. El frontend esta desarrollado en Angular.

Adicionalmente a estos frameworks, se le ha incorporado un framework de identidad, Oauth2, que genera tokens unívocos para los usuarios que se autentiquen.

## Las tecnologías usadas

* Spring Boot
* Angular 11
* Angular Material
* Open API
* Lombok
* SweetAlert 2
* Bootstrap 5
* Oauth

## El diseño de la aplicación

La aplicación tiene seis microservicios que cumplen distintas funcionalidades:

- Micro-config-server: Tiene la dependencia de Spring Cloud Config Server. El resto de los microservicios, salvo el de eureka, se comportan como clientes.
- Micro-eureka: En Eureka se reportan todas las instancias de los microservicios, salvo el anterior. Este reporte se conoce como _heartbeat_ y en la aplicación desarrollada, este periodo se ha reducido a 5 segundos por motivos de desarrollo.
- Micro-zuul-\*: Es el gateway para enlazar los distintos microservicios para una misma aplicación de backend. Cada instancia de cada microservicio corre en distinto puerto y cada microservicio puede cambiar su nombre interno. Zuul permite configurar que a cada microservicio se le asigne un endpoint. Este microservicio incorpora un servidor de autorización de Oauth, que verifica el acceso de los tokens a los recursos antes de darles acceso.
- Micro-oauth-\*: Es el encargado de validar las credenciales y generar los tokens cuando la autenticación es exitosa.
- Micro-usuarios: Es el servicio que gestiona las credenciales de los usuarios y tiene precargado tres tipos de roles distintos. 
- Micro-aplicacion: Es la aplicación en sí misma. Gestiona las empresas, los contactos, las plantillas, los usuarios y orden los envíos de correos.

### Microservicios reemplazables

Los microservicios son facilmente reemplazables, motivo por el cual he incuido dos microservicios extra con una variación mínima.

Para cifrar el token y que la aplicación siga con su funcionamiento normal, se requieren dos microservicios, el de autenticación Oauth y el de autorización, funcionalidad que esta incluida en el microservicio de Zuul. Por tanto, si se quiere cambiar el tipo de cifrado de los tokens, es necesario apagar el micro de zuul y el micro de oauth que esten levantados y levantar los otros dos. En caso de tener la sesión iniciada en la aplicación, será necesario cerrarla, pues el token que tiene el navegador no funcionará con los nuevos servicios levantados.

### Orden de despliegue

El primer servicio que se tiene que levantar es micro-config-server y el último micro-aplicación porque precarga datos en la aplicación. Para que la aplicación pueda funcionar, todos los servicios tienen que estar levantados.

### Archivos de configuración properties o yml

Cada microservicio cuenta con un archivo application.properties y/o application.yml desde el que modificar la configuración predeterminada y establecer valores para variables que el mismo código usa.

El hecho de implementar un servidor de configuración reduce esta configuración al mínimo y establece el resto de la configuración en los archivos del servidor.

Puede haber tantos archivos de configuración en el servidor como se requiera, motivo por el que hay más archivos que microservicios, como sql, comun o correo.

## Comunicación entre microservicios

La arquitectura de microservicios junto con Oauth hace que se vuelva un poco complejo seguir las llamadas de entre microservicios, aunque resumidamente el proceso es:

- Quiero autenticarme: Zuul manda la petición al servicio de autenticación y devuelve un token, que se queda en el navegador.
- Quiero operar con la aplicación: Zuul, el servidor de autorización manda el token del navegador al servidor de autenticación para que verifique el token. El servidor de autenticación devuelve el resultado de la verificación y Zuul concede permiso o no según lo recibido.
- Creo, actualizo o borro un perfil: El recurso, micro-aplicación configura el perfil y el usuario con los datos solicitados y manda una petición Feign via Zuul (servidor de autorización). Zuul permite la petición y micro-usuarios realiza la operación solicitada. Si no hay excepción en la devolución de la petición, micro-aplicacion continúa realizando la operación con el perfil.

## La seguridad entre el frontend y el backend

### Desde el ahorro de llamadas al backend

Los endpoints del backend están asegurados por el servidor de autorización, en el backend.

Por el lado del frontend, los guards permiten comprobar si el token del usuario autenticado tiene permisos para acceder a los endpoints del backend, de modo que hay un ahorro de llamadas al backend que éste hubiese denegado en caso de haberse realizado.

### Desde el acceso a los recursos

Para mejorar la seguridad y ahorrar recursos, es necesario estudiar que peticiones realiza el backend. En caso de no hacerlo, el frontend recupera más información de la debida del backend y puede necesitar realizar más llamadas de las necesarias para obtener los resultados deseados.

Un ejemplo contrario a ésto es el desarrollado al recuperar las plantillas. Sería deseable un endpoint que devuelva las plantillas solo de la empresa solicitada en vez de devolver todas y que el frontend haga el filtro correspondiente. Si este endpoint único no existe y solo puede ver las empresas registradas el administrador, hay que ajustar la seguridad de este endpoint, el que devuelve la lista de todas las plantillas, para que tambien lo pueda usar un usuario normal.

## Reemplazo de clases de servicios en Spring Boot

He incluido un servicio extra cuya única diferencia con el que esta establecido es que no agrega la firma de la empresa al cuerpo del mensaje. Este servicio adicional es el que procesa las órdenes y las convierte en mensajes, JavaMailSenderServiceAsyncImplSinFirma.

Una buena práctica en Spring es el inyectar las interfaces y no los servicios. Al arrancar la aplicación, Spring busca que servicio tiene declarada la interfaz para usarlo. Si hay varios servicios que implementan la misma interfaz, hay que indicar cual de ellos tiene que suar. Esto se puede hacer con dos anotaciones, con un @Qualifier, o con un @Primary. En mi caso he usado la última, que me parece más práctica, aunque de haber más servicios con la misma interfaz podría ser más recomendado la primera.

## Dependencias comunes

Una forma de facilitar la mantenibilidad es usar dependencias comunes. En el caso de la aplicación desarrollada, tiene dos: com-usuarios y com-rsa. La primera tiene los modelos de usuario y rol, que usan micro-aplicacion, micro-usuarios y micro-oauth-\*. La segunda tiene las claves privadas y públicas del cifrado del token y lo usan micro-oauth-asimetrico y micro-zuul-asimetrico.

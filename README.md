# PokeGroup


[![Build Status](https://travis-ci.org/joemccann/dillinger.svg?branch=master)](https://travis-ci.org/joemccann/dillinger)

pokegroup es un proyecto de informacion y pruebas con el uso de datos de la pokeApi para obtener informacion sobre los pokemon.



# Que puedo encontrar!

  - Informacion de pokemons segun su region
  - creacion de grupos de pokemon (aun pendiente)


Como puedo usar este proyecto:
  - Importar proyecto o descargarlo via zip o Cliente Git
  - Abrir proyecto con Android Studio y verificar gradle a version 3.3 +
  - Verificar SDK de Android actualizado ya se utiliza AndroidX en todo el proyecto

El proyecto usa patron de diseño MVP por lo que se recomienda conocimiento previo del patron para comprender la complejidad del desarrollo, ademas del uso de Dagguer2 como inyector de dependencias.


### Librerias

PokeGroup hace uso de librerias para facilitar el desarrollo, estas son algunas de las librerias usadas

* [Glide] - Cargador de Imagenes!
* [Retrofit] - Libreria de request URL
* [okHttp] - Cliente de request URL.
* [Facebook SDK] - Libreria que permite obtener datos de usuario e inicio de sesion por red social
* [Google Firebase Auth] - Libreria que permite obtener datos de usuario e inicio de sesion por red social y opciones centralizadas
* [Interceptors] - Librerias de intercepcion de solicitudes URL en el proyecto
* [RxJava] - Libreria que permite el uso de Observadores en procesos del proyecto
* [Crashlitycs] - Libreria de captura de errores
* [Gson] - Libreria de serializado de objetos Json
* [Gson] - Libreria de inyeccion de dependencias



### Requisitos

Pokegroup requires SDK Android actualizado, Gradle 3.3.2 o superior y Android Studio 3.3.2.


### Seguridad
* Se implementa seguridad de url encriptando en base64 asi evitar su posible explotacion en caso de ingenieria inversa.
* referencia o herramienta https://www.base64decode.org/

### Notas

 - Este proyecto no cuenta con pruebas Unitarias.
 - Se usan recursos extra para dar vistosidad al diseño del proyecto. 

Licencia
----

MIT


**Free Software



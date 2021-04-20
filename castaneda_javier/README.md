#Desafío Quality en Spring

Se desarrolló una API que permite visualizar listados de vuelos y hoteles, y también realizar reservaciones de vuelos o
de hoteles

Los datos se encuentran almacenados en los archivos Hoteles.csv y Vuelos.csv dentro de resources

---

###Hoteles 🏨

* Para la consulta en el listado de hoteles disponibles se usa el endpoint `/api/v1/hotels`

* Para la consulta en el listado de hoteles disponibles filtrados por destino se usa el endpoint `/api/v1/hotels?destination=Medellín`

* Para la consulta en el listado de hoteles disponibles filtrados por fechas se usa el endpoint `/api/v1/hotels?dateFrom=02/03/2021&dateTo=21/03/2021`
  y unicamente se filtra usando las dos fechas, no se puede con una sola.

* Se puede usar ambos filtros al mismo tiempo usando `/api/v1/hotels?destination=Medellín&dateFrom=02/03/2021&dateTo=21/03/2021`

Para realizar una reserva se usa el endpoint POST `/api/v1/booking` y dentro del cuerpo del request se envía un json con
el siguiente formato:

```json
{
  "userName" : "seba_gonzalez@unmail.com",
  "booking" : {
    "dateFrom" : "10/03/2021",
    "dateTo" : "20/03/2021",
    "destination" : "Buenos Aires",
    "hotelCode" : "BH-0002",
    "peopleAmount" : 2,
    "roomType" : "Doble",
    "people" : [
      {
        "dni" : "12345678",
        "name" : "Pepito",
        "lastName" : "Gomez",
        "birthDate" : "10/11/1982",
        "mail" : "pepitogomez@gmail.com"
      },
      {
        "dni" : "13345678",
        "name" : "Fulanito",
        "lastName" : "Gomez",
        "birthDate" : "10/11/1983",
        "mail" : "fulanitogomez@gmail.com"
      }
    ],
    "paymentMethod" : {
      "type" : "CREDIT",
      "number" : "1234-1234-1234-1234",
      "dues" : 5
    }
  }
}
```

El método de pago puede ser también de tipo `DEBIT`

Una vez realizada la reserva se cambia el estado del hotel reservado en memoria y en archivo, por lo que no se puede 
volver a reservar el mismo hotel.

---

###Vuelos ✈

️- Para consultar el listado de los vuelos se usa el endpoint `/api/v1/flights`  

️- Para consultar el listado de los vuelos filtrados por origen se usa el endpoint `/api/v1/flights?origin=Buenos Aires`  

️- Para consultar el listado de los vuelos filtrados por destino se usa el endpoint `/api/v1/flights?destination=Tucumán`   

️- Para consultar el listado de los vuelos filtrados por fechas (que las fechas puestas estén entre las del vuelo) se usa
el endpoint `/api/v1/flights?dateFrom=11/02/2021&dateTo=16/02/2021`

- Se pueden usar los filtros juntos usando el endpoint `/api/v1/flights?origin=Buenos Aires&destination=Tucumán&dateFrom=11/02/2021&dateTo=16/02/2021`

Para realizar una reserva de asientos en un vuelo se usa el endpoint POST `/api/v1/flight-reservation` y dentro del cuerpo
del request se envía un json con el siguiente formato:

```json
{
  "userName" : "seba_gonzalez@unmail.com",
  "flightReservation" : {
    "dateFrom" : "15/02/2021",
    "dateTo" : "28/02/2021",
    "origin" : "Bogotá",
    "destination" : "Buenos Aires",
    "flightNumber" : "BOBA-6567",
    "seats" : 2,
    "seatType" : "Business",
    "people" : [
      {
        "dni" : "12345678",
        "name" : "Pepito",
        "lastName" : "Gomez",
        "birthDate" : "10/11/1982",
        "mail" : "pepitogomez@gmail.com"
      },
      {
        "dni" : "13345678",
        "name" : "Fulanito",
        "lastName" : "Gomez",
        "birthDate" : "10/11/1983",
        "mail" : "fulanitogomez@gmail.com"
      }
    ],
    "paymentMethod" : {
      "type" : "CREDIT",
      "number" : "1234-1234-1234-1234",
      "dues" : 6
    }
  }
}
```

El método de pago también puede ser DEBIT

Se puede hacer más de una reserva en el mismo vuelo, no se escribe en ningún archivo ni en memoria.

---

###Test 🧪

Porcentaje de cobertura de los test realizados: 85% de las lineas.

No se realizó test del controller porque no se vio necesario ya que no tenía apenas lógica.

Si es posible, ejecutar los test del conjunto `ALL TEST` donde se excluyó a los DTO y las excepciones.
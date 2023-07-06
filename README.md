## Prueba- Ejercicio creacion api rest
## Qué usé?
* Java 17
* Docker
* Maven
* Spring boot 3.1.1
* Mapstruct 1.5.5
* Lombok
* Mysql 8.0.28

## Configuración inicial del proyecto
## 1. Configurar Base de datos en entorno docker.
En la raiz del proyecto se encuentra un archivo docker-compose.yml que levanta una base de datos en mysql en el puerto 3306.

```
 ./docker-compose.yml
```
Estando en la raiz del proyecto se procedera a abrir terminal y ejecutar el comando

```
docker compose up
```
si solicita algun permiso se le debera anteponer la palabra sudo para darle permisos de administrador.
```
sudo docker compose up
```
Esperar a que termine de levantar el ambiente docker
## 2. Ejecutar proyecto.
Despues de debera ejecutar el proyecto esta api esta corriendo en el puerto 8088.
## 3. Api disponible

* Data Rest: http://localhost:8088

## 4. Postman
En la raiz del proyecto se encuentra el archivo.
```
./Devsu.postman_collection.json
```
Este json se debera importar en postman para poder probar los siguientes endpoints.

## 5. Endpoints
### CLIENTES
```
http://localhost:8088/clientes
```
Este endpoints tiene expuesto 4 tipos de servicios 
#### CREATE de tipo POST: 
Con este endpoint se crea un Cliente puede ser de sexo (MASCULINO,FEMENINO O NO_ESPECIFICA) el ClienteId se genera Automatico, Acontinuacion ejemplo de JSON.
```
POST http://localhost:8088/clientes
```
Este es el json para un cliente de género MASCULINO
```
{
  "nombre": "Alberto lema",
  "genero": "MASCULINO",
  "edad": 14,
  "identificacion": "1143355346",
  "direccion": "Cotavalo sn y principal",
  "telefono": "098254785",
  "contrasena":"1234",
  "estado":true
}
```
Este es el json para un cliente de género FEMENINO
```
{
  "nombre": "Angy Tatiana Castaño Urrego",
  "genero": "FEMENINO",
  "edad": 14,
  "identificacion": "1143355346",
  "direccion": "Cotavalo sn y principal",
  "telefono": "098254785",
  "contrasena":"1234",
  "estado":true
}
```
Este es el json para un cliente de género NO_ESPECIFICA
```
{
  "nombre": "Alberto lema",
  "genero": "NO_ESPECIFICA",
  "edad": 14,
  "identificacion": "1143355346",
  "direccion": "Cotavalo sn y principal",
  "telefono": "098254785",
  "contrasena":"1234",
  "estado":true
}
```
#### UPDATE de tipo PUT: 
Con este endpoint se actualiza el Cliente, acontinuación ejemplo del JSON.
```
PUT http://localhost:8088/clientes
{
  "clienteId":1,
  "nombre": "Angie Castaño",
  "genero": "FEMENINO",
  "edad": 27,
  "identificacion": "1143355865",
  "direccion": "Calle 48 c sur #43a-50",
  "telefono": "3003803584",
  "contrasena":"1234",
  "estado":true
}
```
#### FIND ALL de tipo GET: 
Con este endpoint se consulta todos los clientes creados en la base de dados.
```
GET http://localhost:8088/clientes
```
#### FIND BY CLIENT ID de tipo GET: 
Con este endpoint se consulta un cliente concatenandole el clienteId.
```
GET http://localhost:8088/clientes/{clienteId}
```
Este resultado
#### UPDATE PARTIAL de tipo PATCH: 
Con este endpoint se actualiza parcialmente algunos campos de clientes, se debe setear el clienteId en la url
 ```
PATCH http://localhost:8088/clientes/{clienteId}
{
  "nombre": "Angie Tatiana Castaño Urrego",
  "genero":"FEMENINO",
  "identificacion": "1143355865",
  "estado":true
  }
```
#### DELETE de tipo Delete: 
Con este endpoint se elimina un cliente de la base de datos se le debara pasar el id para eliminarlo.
 ```
DELETE http://localhost:8088/clientes/{clienteId}
```

### CUENTAS
```
http://localhost:8088/cuentas
```
Este endpoints tiene expuesto 4 tipos de servicios
#### CREATE de tipo POST: 
Con este endpoint se crea una cuenta esta puede ser de tipo "AHORROS" o "CORRIENTE" el ID se genera Automatico, Acontinuacion ejemplo de JSON.
```
POST http://localhost:8088/cuentas
```
Este es el json para una cuenta de tipo Ahorros.
```
{
  "numeroCuenta": "225487",
  "tipoCuenta": "AHORROS",
  "saldoInicial": 500,
  "estado":true,
  "cliente" : {
      "identificacion":"1143355865"
  }
}
```
Este es el json para una cuenta de tipo Corriente.
```
{
  "numeroCuenta": "4728757",
  "tipoCuenta": "CORRIENTE",
  "saldoInicial": 2000,
  "estado":true,
  "cliente" : {
      "identificacion":"1143355865"
  }
}
```
#### UPDATE de tipo PUT: 
Con este endpoint se actualiza la Cuenta, acontinuacion ejemplo del JSON.
```
PUT http://localhost:8088/cuentas
{
  "id":2,
  "numeroCuenta": "4728757",
  "tipoCuenta": "AHORROS",
  "saldoInicial": 1500,
  "estado":true,
   "cliente" : {
      "identificacion":"1143355865"
  }
}
```
* FIND ALL de tipo GET: Con este endpoint se consulta todas las cuentas creadas en la base de dados.
```
GET http://localhost:8088/cuentas
```
#### FIND BY ID de tipo GET:
Con este endpoint se consulta una cuenta  concatenandole el id.
```
GET http://localhost:8088/cuentas/{id}
```
Este es el resultado que devuelve
```
{
    "id": 1,
    "numeroCuenta": "225487",
    "tipoCuenta": "AHORROS",
    "saldoInicial": 500.0,
    "estado": true,
    "cliente": {
        "clienteId": 1,
        "nombre": "Angie Tatiana Castaño Urrego",
        "genero": "FEMENINO",
        "edad": 27,
        "identificacion": "1143355865",
        "direccion": "Calle 48 c sur #43a-50",
        "telefono": "3003803584",
        "estado": true,
        "contrasena": "03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4"
    }
}
```
#### UPDATE PARTIAL de tipo PATCH: 
Con este endpoint se actualiza parcialmente algunos campos de cuenta, se debe setear el id en la url
 ```
PATCH http://localhost:8088/cuentas/{id}
{
  "numeroCuenta": "4728757",
  "saldoInicial": 1000,
          "tipoCuenta": "AHORROS"

}
```
* DELETE de tipo Delete: Con este endpoint se elimina un cliente de la base de datos se le debara pasar el id para eliminarlo.
 ```
DELETE http://localhost:8088/cuentas/{id}
```

### MOVIMIENTOS
```
http://localhost:8088/movimientos
```
Este endpoints tiene expuesto 3 tipos de servicios
#### CREATE de tipo POST: 
Con este endpoint se crea un movimienot esta puede ser de tipo "CREDITO" o "DEBITO" el ID se genera Automatico, Acontinuacion ejemplo de JSON.
```
POST http://localhost:8088/movimientos
```
Este es el json para un movimiento de tipo CREDITO.
```
{
  "tipoMovimiento": "CREDITO",
  "valor": 800,
  "cuenta" : {
      "numeroCuenta":"225487"
  }
}
```
Este es el json para un movimiento de tipo DEBITO.
```
{
  "tipoMovimiento": "DEBITO",
  "valor": 200,
  "cuenta" : {
      "numeroCuenta":"225487"
  }
}
```
#### UPDATE de tipo PUT: 
Con este endpoint se actualiza un movimiento, acontinuacion ejemplo del JSON.
```
PUT http://localhost:8088/movimientos
{
  "id":1,
  "tipoMovimiento": "CREDITO",
  "valor": 200,
  "cuenta" : {
      "numeroCuenta":"225487"
  }
}
```
#### FIND ALL de tipo GET: 
Con este endpoint se consulta todos las movimeintos creados en la base de dados.
```
GET http://localhost:8088/movimientos
```
#### FIND BY ID de tipo GET: c
Con este endpoint se consulta un movimiento en especifico  concatenandole el id.
```
GET http://localhost:8088/movimientos/{id}
```
Este es el resultado que devuelve
```
{
    "id": 1,
    "fecha": "2023-07-06T07:30:36.190156",
    "tipoMovimiento": "CREDITO",
    "valor": 200.0,
    "saldo": 499.0,
    "cuenta": {
        "id": 1,
        "numeroCuenta": "225487",
        "tipoCuenta": "AHORROS",
        "saldoInicial": 500.0,
        "estado": true,
        "cliente": {
            "clienteId": 1,
            "nombre": "Angie Tatiana Castaño Urrego",
            "genero": "FEMENINO",
            "edad": 27,
            "identificacion": "1143355865",
            "direccion": "Calle 48 c sur #43a-50",
            "telefono": "3003803584",
            "estado": true,
            "contrasena": "03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4"
        }
    }
}
```
#### DELETE de tipo Delete: 
Con este endpoint se elimina un Movimiento de la base de datos se le debara pasar el id para eliminarlo.
 ```
DELETE http://localhost:8088/movimientos/{id}
```

### REPORTES
```
http://localhost:8088/reportes
```
Este endpoints tiene expuesto 1 servicio de tipo get que es para generar un reporte de los movimientos de un cliente en un rango de fechas en especifico.
#### FIND BY CLIENTE BY ID de tipo GET: 
Con este endpoint se genera un reporte general de todos los movimientos de un cliente, este necesitaun clienteId una fecha de inicio y una fecha final.
```
GET http://localhost:8088/reportes?clienteId=1&fechaInicio=2023-07-06&fechaFinal=2023-07-06
```
Este es el resultado que arroja

```
{
    "clienteId": 1,
    "nombre": "Angie Tatiana Castaño Urrego",
    "identificacion": "1143355865",
    "cuentas": [
        {
            "numeroCuenta": "225487",
            "tipoCuenta": "AHORROS",
            "saldo": 499.0,
            "totalDebitos": 400.0,
            "totalCreditos": 399.0,
            "movimientos": [
                {
                    "tipoMovimiento": "CREDITO",
                    "fecha": "2023-07-06T07:30:36.190156",
                    "valor": 200.0,
                    "saldo": 499.0
                },
                {
                    "tipoMovimiento": "DEBITO",
                    "fecha": "2023-07-06T07:30:49.24053",
                    "valor": 200.0,
                    "saldo": 1100.0
                },
                {
                    "tipoMovimiento": "DEBITO",
                    "fecha": "2023-07-06T07:30:51.678629",
                    "valor": 200.0,
                    "saldo": 900.0
                },
                {
                    "tipoMovimiento": "CREDITO",
                    "fecha": "2023-07-06T07:31:13.004151",
                    "valor": 100.0,
                    "saldo": 1000.0
                },
                {
                    "tipoMovimiento": "CREDITO",
                    "fecha": "2023-07-06T07:31:51.656972",
                    "valor": 99.0,
                    "saldo": 1099.0
                }
            ]
        },
        {
            "numeroCuenta": "4728757",
            "tipoCuenta": "AHORROS",
            "saldo": 1500.0,
            "totalDebitos": 0.0,
            "totalCreditos": 0.0,
            "movimientos": []
        }
    ],
    "saldoTotalGeneral": 1999.0,
    "creditoTotalGeneral": 399.0,
    "debitoTotalGeneral": 400.0
}
```


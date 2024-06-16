El uso de colas de mensajería se usa para flujos asíncronos.

Rabbitmq es un proveedor de mensajería que consta de varias partes:


Exchange:
Es dónde se publica un mensaje. Para publicar un mensaje en el exchange hay que indicarle un routingKey.
Cuando llega un mensaje al exchange, este coge y manda un mensaje a cada cola que tiene bindeada con el mismo routingkey y a parte también envía dicho mensaje a las colas que tengan el comodín de routinKey "#".

Colas:
En las colas es donde el exchange manda un mensaje que se ha publicado en él. Se debe seguir este patrón de nombres para las colas: {nombreExchange}.{nombreCola}. En la cola se le indica mediante el routingKey que mensajes recibidos por el exchange quiere consumir. Si se quiere consumir cualquier mensaje que llegue al exchange independientemente del routingKey hay que poner el comodín "#".
Si se crean dos colas con el mismo nombre y distinto routingKey lo que realmente está pasando es que solo se está creando una cola que va a recibir los mensajes que lleguen al exchange con cualquiera de los dos routingKeys.

Producer:
Es el encargado de enviar un mensaje con un routingKey al exchange destino.

Consumer:
Es el encargado de consumir los mensajes de una cola.

DeadLetter:
Se puede indicar en cada cola una deadLetter que lo que hace es que en caso de no poder consumir bien un mensaje por parte del consumidor tras varios intentos reenviar ese mensaje a otro exchange para poder posteriormente consumirlo en una cola de errores y postetiormente analizar el problema.



Datos de interés:
Puede haber más de un consumidor por cola. Un mensaje de una cola solo se consume por un único consumidor.
Cuando un mensaje es consumido por un consumidor, este tiene que informar de que se ha consumido correctamente o de lo contrario volverá de nuevo a la cola dicho mensaje.
Un exchange puede tener bindeado varias colas diferentes provocando que un mismo mensaje se consuma varias veces (una por cola).


Ejemplo:
Vamos a crear un ejemplo de un topic (exchange) "user" que va a ser enviado a dos colas diferentes ("user.group1" y "user.group2"). La cola "user.group1" va a tener un routingKey con el comodín "#" mientras que la segunda cola "user.group2" va a tener como routingKey "GROUP2". También en las dos colas vamos a incorporarle una deadLetter para que en caso de fallo al consumir podamos monitorizarlo.


Capturas de código de configuración


Configuraciones adicionales:
Número de reintentos en caso de fallo antes de enviar a la deadLetter.
Tiempo entre reintentos.
Precarga de mensajes de la cola. Aunque se consuman los mensajes de uno en uno y se indiquen de uno en uno que se ha consumido bien cada mensaje, se puede configurar el número de mensajes que se precarga desde la cola. Con eso se evita un exceso de tráfico entre la cola y el consumidor.
El objeto o payLoad que se manda a un echange tiene que estar serializado por lo que tiene que implementar la interfaz Serializable.

Captura de configuración


Consumidores:
concurrency indica el número de consumidores activos para esa cola. Cuanto más consumidores más podemos paralelizar pero aumenta la sobrecarga en la plataforma.
También hemos agregado el consumidor de la deadLetter. Este consumidor no tiene por qué estar en el mismo proyecto pero lo hemos incluido aquí para el ejemplo.
Hemos agregado con el consumidor de la cola "user.group1" un caso para que produzca una excepción en caso de que el usuario tenga el id "5" y así podamos ver como llega un mensaje a la deadLetter.


Caso de uso:
Si levantamos el proyecto y mandamos un mensaje al topic "user" con exchange "#" y userId disntinto de 5, dicho mensaje solo será consumido por la cola "user.group1".
Si hacemos la misma prueba pero con exchange "GROUP2" vemos como el mensaje llegará tanto a la cola "user.group1" como a "user.group2".
Si mandamos un nuevo mensaje con routingKey "#" y con un userId = 5, al consumirlo por la cola "user.group1" saltará una excepción y tras varios reintentos acabará llegando a la deadLetter.

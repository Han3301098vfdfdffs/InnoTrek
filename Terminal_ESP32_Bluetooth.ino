#include <BluetoothSerial.h>
#include <stdlib.h> // Para rand()

#define LED 2

BluetoothSerial SerialBT;  // Instancia para Bluetooth serial

// Estructura para almacenar datos
struct SensorData {
  long temperatura1;
  long temperatura2;
};

// Función para generar datos de ejemplo
void generarDatosEjemplo(SensorData &datos) {
  datos.temperatura1 = random(1, 201); // Random entre 1-200
  datos.temperatura2 = random(1, 201);
}

void setup() {
  Serial.begin(9600);      // Serial para depuración por USB
  SerialBT.begin("ESP32_BT"); // Nombre del dispositivo Bluetooth
  pinMode(LED, OUTPUT);
  digitalWrite(LED, LOW);
  randomSeed(analogRead(0));

  Serial.println(" Bluetooth iniciado. Esperando conexión...");
}

void loop() {
  // Solo enviar si hay cliente Bluetooth conectado
  if (SerialBT.hasClient()) {
    digitalWrite(LED, HIGH);

    SensorData datos;
    generarDatosEjemplo(datos);

    // Enviar por Bluetooth
    SerialBT.print("tmp1: ");
    SerialBT.println(datos.temperatura1);
    Serial.print("tmp1: ");
    Serial.println(datos.temperatura1);

    SerialBT.print("tmp2: ");
    SerialBT.println(datos.temperatura2);
    Serial.print("tmp2: ");
    Serial.println(datos.temperatura2);

    delay(1000); // Esperar 1 segundo
  } else {
    digitalWrite(LED, LOW);
  }
}

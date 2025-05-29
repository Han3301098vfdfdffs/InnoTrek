#include <WiFi.h>
#include <stdlib.h> // Para rand()

#define LED 2

const char* SSID = "INFINITUM33E3";
const char* Password = "30xEtCFB5z";
const int tcpPort = 12345;

WiFiServer tcpServer(tcpPort);

// Estructura para almacenar datos
struct SensorData {
  long temperatura1;
  long temperatura2;
};

// Función para conexión WiFi (igual que antes)
void connectionWiFi() {
  WiFi.mode(WIFI_STA);
  WiFi.disconnect();
  delay(100);

  Serial.println("Conectando a ");
  Serial.println(SSID);
  Serial.println("...");

  WiFi.begin(SSID, Password);

  unsigned long startTime = millis();
  while (WiFi.status() != WL_CONNECTED && millis() - startTime < 20000) {
    delay(500);
    Serial.println(".");
  }

  if(WiFi.status() == WL_CONNECTED) {
    digitalWrite(LED, HIGH);
    Serial.print("\nConectado a ");
    Serial.print(SSID);
    Serial.println(" con éxito");
    Serial.print("IP: ");
    Serial.println(WiFi.localIP());
    Serial.print("Puerto: ");
    Serial.println(tcpPort);
    tcpServer.begin();
  } else {
    digitalWrite(LOW, HIGH);
    switch(WiFi.status()){
      case WL_IDLE_STATUS: Serial.println("Modo idle"); break;
      case WL_NO_SSID_AVAIL: Serial.println("SSID no disponible"); break;
      case WL_CONNECT_FAILED: Serial.println("Contraseña incorrecta"); break;
      case WL_DISCONNECTED: Serial.println("Desconectado"); break;
      default: Serial.println("Error desconocido");
    }
  }
}

// Función para generar datos de ejemplo (simplificada)
void generarDatosEjemplo(SensorData &datos) {
  datos.temperatura1 = random(1, 201); // Random entre 1-200
  datos.temperatura2 = random(1, 201);
}

void setup() {
  Serial.begin(115200);
  delay(1000);
  pinMode(LED, OUTPUT);
  randomSeed(analogRead(0));
  connectionWiFi();
}

void loop() {
  if (WiFi.status() != WL_CONNECTED) {
    connectionWiFi();
    delay(1000);
    return;
  }

  WiFiClient client = tcpServer.available();
  
  if (client) {
    Serial.println("Cliente Android conectado");
    
    while (client.connected()) {
      // Generar datos de ejemplo
      SensorData datos;
      generarDatosEjemplo(datos);
      
      // Enviar datos con el formato solicitado
      client.print("tmp1: ");
      client.println(datos.temperatura1);
      Serial.print("Enviado: tmp1: ");
      Serial.println(datos.temperatura1);
      
      client.print("tmp2: ");
      client.println(datos.temperatura2);
      Serial.print("Enviado: tmp2: ");
      Serial.println(datos.temperatura2);

      delay(1000); // Esperar 1 segundo entre envíos
    }
  if (WiFi.status() == WL_DISCONNECTED) {
    connectionWiFi();
    delay(1000);
    return;
  }
    client.stop();
    digitalWrite(LED, LOW);
    Serial.println("Cliente Android desconectado");    
  }
}
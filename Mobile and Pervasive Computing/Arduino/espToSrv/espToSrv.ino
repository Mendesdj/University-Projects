/*
 *  Simple HTTP get webclient test
 */
 
#include <ESP8266WiFi.h>
#include <SoftwareSerial.h>
 
const char* ssid     = "OpenFCT";
const char* password = "";

//const char* ssid     = "Share64";
//const char* password = "naoe1234";
 
const char* host = "lcworks.ddns.net";
//const char* host = "192.168.43.189";
const int httpPort = 80;

SoftwareSerial unoCon(13,12); //RX, TX
 
void setup() {


  unoCon.begin(9600);
  unoCon.println("ESP starting!");
  
  Serial.begin(115200);
  delay(100);
 
  // Connecting to a WiFi network
 
  Serial.println();
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);
  
  WiFi.begin(ssid);
  
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
 
  Serial.println("");
  Serial.println("WiFi connected");  
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
  Serial.print("Netmask: ");
  Serial.println(WiFi.subnetMask());
  Serial.print("Gateway: ");
  Serial.println(WiFi.gatewayIP());

  pinMode(16, INPUT);
  
  notifyServer();

}

// client is the WifiClient that it's used to connect to the server
WiFiClient client;

void notifyServer(){
  delay(5000);
 
  Serial.print("Connecting to ");
  Serial.println(host);

  //Delay required for the connection to be established
  delay(1000);
  
  if (!client.connect(host, httpPort)) {
    Serial.println("Connection Failed");
    return;
  }
  
  String url = "lcworks.ddns.net";
  Serial.print("Notifying server at: ");
  Serial.println(url);
  
  // Command to notify server of the ESP's socket
  // 8 corresponds to the ARDUINO_NOTIFICATION method
  byte serverCommand = 8;
  client.write(serverCommand);
  client.flush();
        
  delay(500);
  
  // Read all the lines of the reply from server and print them to Serial
  while(client.available()){
    String line = client.readStringUntil('\r');
    Serial.print(line);
    if(line.equals("open dooropen door")){
      Serial.print("OPEN ORDERED");
      unoCon.println("open");
    }   
  }
  
  Serial.println("Server Notified");

  
}

int counter = 0;
  
void loop() {

    //Read any stream comming from the server
    String s = "";
    while(client.available()){
      char c = client.read();
      s += c;
    }

    //If the stream contains "open" the order is sent to the Uno through unoCon
    if(s.indexOf("open") > 0){
        Serial.print("OPEN ORDERED");
        unoCon.println("open");
    }   



    //Read any stream comming from the Uno, if an RFID is received, forward that ID 
    //   to the server for registering
    boolean received = false;   //received required to control when to send to the server
    String rfidUno = "";
    while(unoCon.available()){
      if(unoCon.available())
        received = true;
      rfidUno = unoCon.readString();    
    }
    rfidUno += "\r";
    

    if(received){
      client.flush(); //Clear the channel
      client.print(rfidUno);
      client.flush();
      Serial.print("> Sent to server: " + rfidUno + "\r\n");  
      received = false; 
    }
 
      
}
    



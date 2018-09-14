//WIFI
#include <SoftwareSerial.h>

//RFID
#include "SPI.h"
#include "MFRC522.h"

//RFID
#define SS_PIN 10
#define RST_PIN 9
#define SP_PIN 8

//PIN SETUP
#define relay1 2
#define relay2 3

//RFID CARDS
#define authCard "AA:BB:CC:DD"
#define numOfCards 1

String cards[numOfCards] = {"AA:BB:CC:DD"};


//RFID
MFRC522 rfid(SS_PIN, RST_PIN);
MFRC522::MIFARE_Key key;


SoftwareSerial ESPConnection(7, 6);      // RX, TX for ESP8266

bool DEBUG = true;   //show more logs
int responseTime = 0; //communication timeout
int state = 0;

void setup()
{
  Serial.begin(9600);
  ///////////////////////RELAY SETUP////////////////////////////////////////////////////
  pinMode(relay1, OUTPUT);
  digitalWrite(relay1, LOW);
  pinMode(relay2, OUTPUT);
  digitalWrite(relay2, HIGH);
  pinMode(7, INPUT);
  //////////////////////////////////////////////////////////////////////////////////////

  
  ///////////////////////WIFI SETUP////////////////////////////////////////////////////
  ESPConnection.begin(9600);  
  //////////////////////////////////////////////////////////////////////////////////////


  /////////////////////RFID SETUP///////////////////////////////////////////////////////
  SPI.begin();
  rfid.PCD_Init();
  /////////////////////////////////////////////////////////////////////////////////////

}


void loop()
{

  rfidProcessing();

  // Processes the serial connection with the ESP
  if(ESPConnection.available() > 0){
    String command = ESPConnection.readString();
    Serial.println(command);
    //Open order received from the server trough the ESP
    if(command.equals("open\r\n")){
      doorControl();
    }
  }
  
  
}


void rfidProcessing(){

  //Serial.print("Loop");
  if (!rfid.PICC_IsNewCardPresent() || !rfid.PICC_ReadCardSerial())
    return;

  // Serial.print(F("PICC type: "));
  MFRC522::PICC_Type piccType = rfid.PICC_GetType(rfid.uid.sak);
  // Serial.println(rfid.PICC_GetTypeName(piccType));

  // Check is the PICC of Classic MIFARE type
  if (piccType != MFRC522::PICC_TYPE_MIFARE_MINI &&
    piccType != MFRC522::PICC_TYPE_MIFARE_1K &&
    piccType != MFRC522::PICC_TYPE_MIFARE_4K) {
    Serial.println(F("Your tag is not of type MIFARE Classic."));
    return;
  }

  String strID = "";
  for (byte i = 0; i < 4; i++) {
    strID +=
    (rfid.uid.uidByte[i] < 0x10 ? "0" : "") +
    String(rfid.uid.uidByte[i], HEX) +
    (i!=3 ? ":" : "");
  }
  strID.toUpperCase();

  
  Serial.print("Tap card key: ");
  Serial.println(strID);

  // If the card is on the authorized list it sends the rfid to the server and opens the door
  if(cardAuthorized(strID)){
    registerOnServer(strID + "\r");
    doorControl();    
  }
    

  rfid.PICC_HaltA();
  rfid.PCD_StopCrypto1();
  
}


//Simply sends the RFID to the ESP
void registerOnServer(String RFID){
  ESPConnection.print(RFID);
}


//Returns true if the RFID tag is authorized
boolean cardAuthorized(String card){
  
  int i;
  for(i = 0; i < numOfCards; i++){
    if(cards[i] == card)
      return true;
  }
  return false;
  
}


//Sequence of relay operations to rotate the motor to open/close the door
void doorControl(){

  if(state == 0){
    digitalWrite(relay1, HIGH);
    delay(190);
    digitalWrite(relay1, LOW);
    delay(5000);
    digitalWrite(relay2, LOW);
    delay(190);
    digitalWrite(relay2, HIGH);
    //state = 1;
  }
  else{

  }
  
}


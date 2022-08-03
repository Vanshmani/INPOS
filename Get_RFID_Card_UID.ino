#include <SPI.h>
#include <MFRC522.h>

int RST_Pin=9;
int SS_Pin=10;

MFRC522 mfrc522(SS_Pin, RST_Pin);

#define New_UID {0xDE, 0xAD, 0xBE, 0xEF}

MFRC522::MIFARE_Key key;

void setup() {
  
Serial.begin(9600);
while (!Serial);
SPI.begin();
mfrc522.PCD_Init();
Serial.println("Scan PICC to see UID and type..");
}

void loop() {
  // Look for new cards
  if(!mfrc522.PICC_IsNewCardPresent()){
    return;
  }
  // Select one of the cards
  if(!mfrc522.PICC_ReadCardSerial()){
    return;
  }

  //Dump debug Info about the card
  mfrc522.PICC_DumpToSerial(&(mfrc522.uid));
}

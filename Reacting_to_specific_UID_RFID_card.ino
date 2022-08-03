#include <SPI.h>
#include <MFRC522.h>

int SS_Pin=10;
int RST_Pin=9;
MFRC522 mfrc522(SS_Pin, RST_Pin);
int dt = 500;
void setup() {
  // put your setup code here, to run once:
Serial.begin(9600);
SPI.begin();
mfrc522.PCD_Init();
Serial.println("Reading card");
Serial.println();
}

void loop() {
  // Look for new cards
  if ( ! mfrc522.PICC_IsNewCardPresent()) 
  {
    return;
  }
  // Select one of the cards
  if ( ! mfrc522.PICC_ReadCardSerial()) 
  {
    return;
  }
  Serial.print("UID tag :");
  String content= "";
  byte letter;
  Serial.println();
  Serial.print("Message : ");
  content.toUpperCase();
  if (content.substring(1) == "xx xx xx xx") //change here the UID of the card/cards that you want to give access
  { //Write the command to be followed for the respective UIDs
    }
    delay(dt);
}

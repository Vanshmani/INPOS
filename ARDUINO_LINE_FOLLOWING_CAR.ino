#include <AFMotor.h>

#define left A0
#define right A1

//defining motors
AF_DCMotor leftmotor(1, MOTOR12_1KHZ); 
AF_DCMotor rightmotor(3, MOTOR34_1KHZ);

void setup() {
  //declaring pin types
  pinMode(left,INPUT);
  pinMode(right,INPUT);
  //begin serial communication
  Serial.begin(9600);
  
}

void loop(){
  //printing values of the sensors to the serial monitor
  Serial.println(digitalRead(left));
  
  Serial.println(digitalRead(right));
  
  line_following();
  
}

void line_following(){
   //line detected by both
  if(digitalRead(left)==0 && digitalRead(right)==0){
    //Forward
    leftmotor.run(FORWARD);
    leftmotor.setSpeed(150);
    
    rightmotor.run(FORWARD);
    rightmotor.setSpeed(150);    
  }
  //line detected by left sensor
  else if(digitalRead(left)==0 && !analogRead(right)==0){
    //turn left
    leftmotor.run(FORWARD);
    leftmotor.setSpeed(200);
    
    rightmotor.run(BACKWARD);
    rightmotor.setSpeed(200); 
  }
  //line detected by right sensor
  else if(!digitalRead(left)==0 && digitalRead(right)==0){
    //turn right
    leftmotor.run(BACKWARD);
    leftmotor.setSpeed(200);
    
    rightmotor.run(FORWARD);
    rightmotor.setSpeed(200);

  }
  //line detected by none
  else if(!digitalRead(left)==0 && !digitalRead(right)==0){
    //stop
    leftmotor.run(RELEASE);
    leftmotor.setSpeed(0);
    
    rightmotor.run(RELEASE);
    rightmotor.setSpeed(0);

  }
}

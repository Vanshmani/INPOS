
#include <Servo.h>
#include <AFMotor.h>
#include <NewPing.h>

#define trig 7
#define echo 6
#define max_dist 300
#define coll_dist 30
#define max_speed 160
#define max_speed_offset 40
#define turn_dist coll_dist+20  

AF_DCMotor leftMotor1(1, MOTOR12_1KHZ);
AF_DCMotor leftMotor2(2, MOTOR12_1KHZ);
AF_DCMotor rightMotor1(3, MOTOR34_1KHZ);
AF_DCMotor rightMotor2(4, MOTOR34_1KHZ);

Servo myservo;
NewPing sonar(trig,echo,max_dist);

int left_dist, right_dist;
int curDist=0;
String motorSet="";
int speedSet=0;


void setup() {
  myservo.attach(10);
  myservo.write(90);
  delay(1000);

}

void loop() {
  myservo.write(90);
  delay(100);
  curDist=readPing();
  if(curDist<coll_dist){
    changePath();
  }
  moveForward();
  delay(500);

}

void changePath(){
  moveStop();
  myservo.write(36);
  delay(500);
  right_dist=readPing();
  delay(500);
  myservo.write(144);
  delay(700);
  left_dist=readPing();
  delay(500);
  myservo.write(90);
  compareDist();

}

void compareDist(){
  if(right_dist>left_dist){
    turnRight();
  }
  else if(left_dist>right_dist){
    turnLeft();
  }
  else if(right_dist==left_dist){
    moveReverse();
    compareDist();
//    turnAround();
  }
}
int readPing(){
  delay(70);
  unsigned int uS=sonar.ping();
  int cm=uS/US_ROUNDTRIP_CM;
  return cm;
}
void moveStop(){
  // RELEASE command is used to stop the motors
  leftMotor1.run(RELEASE);
  leftMotor2.run(RELEASE);
  rightMotor1.run(RELEASE);
  rightMotor2.run(RELEASE);
}
void moveForward(){
  motorSet="Forward";
  leftMotor1.run(FORWARD);
  leftMotor2.run(FORWARD);
  rightMotor1.run(FORWARD);
  rightMotor2.run(FORWARD);
  for(speedSet=0;speedSet<max_speed;speedSet+=2){         // to increasse the speed gradually
    leftMotor1.setSpeed(speedSet);
    leftMotor2.setSpeed(speedSet);
    rightMotor1.setSpeed(speedSet);
    rightMotor2.setSpeed(speedSet);
    delay(5);
  }
}
void moveBackward(){
  motorSet="BACKWARD";
  leftMotor1.run(BACKWARD);
  leftMotor2.run(BACKWARD);
  rightMotor1.run(BACKWARD);
  rightMotor2.run(BACKWARD);
  for(speedSet=0;speedSet<max_speed;speedSet+=2){
    leftMotor1.setSpeed(speedSet);
    leftMotor2.setSpeed(speedSet);
    rightMotor1.setSpeed(speedSet);
    rightMotor2.setSpeed(speedSet);
    delay(5);
  }
}
void moveReverse(){
  motorSet="Reverse";
  leftMotor1.setSpeed(30);
  leftMotor2.setSpeed(30);
  rightMotor1.setSpeed(30);
  rightMotor2.setSpeed(30);
  leftMotor1.run(BACKWARD);
  leftMotor2.run(BACKWARD);
  rightMotor1.run(BACKWARD);
  rightMotor2.run(BACKWARD);
  delay(100);
}
void turnLeft(){
  motorSet="LEFT";
  rightMotor1.run(FORWARD);
  rightMotor2.run(FORWARD);
  leftMotor1.run(BACKWARD);
  leftMotor2.run(BACKWARD);
  leftMotor1.setSpeed(speedSet+max_speed_offset);
  leftMotor2.setSpeed(speedSet+max_speed_offset);
  motorSet="Forward";
  leftMotor1.run(FORWARD);
  leftMotor2.run(FORWARD);
  rightMotor1.run(FORWARD);
  rightMotor2.run(FORWARD);
}
void turnRight(){
  motorSet="RIGHT";
  leftMotor1.run(FORWARD);
  leftMotor2.run(FORWARD);
  rightMotor1.run(BACKWARD);
  rightMotor2.run(BACKWARD);
  rightMotor1.setSpeed(speedSet+max_speed_offset);
  rightMotor2.setSpeed(speedSet+max_speed_offset);
  delay(1500);
  motorSet="Forward";
  leftMotor1.run(FORWARD);
  leftMotor2.run(FORWARD);
  rightMotor1.run(FORWARD);
  rightMotor2.run(FORWARD);
}
void turnAround(){
  motorSet="RIGHT";
  leftMotor1.run(FORWARD);
  leftMotor2.run(FORWARD);
  rightMotor1.run(BACKWARD);
  rightMotor2.run(BACKWARD);
  rightMotor1.setSpeed(speedSet+max_speed_offset);
  rightMotor2.setSpeed(speedSet+max_speed_offset);
  delay(1700);
  motorSet="Forward";
  leftMotor1.run(FORWARD);
  leftMotor2.run(FORWARD);
  rightMotor1.run(FORWARD);
  rightMotor2.run(FORWARD);
}

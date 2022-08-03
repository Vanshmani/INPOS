#include <VirtualWire.h> //VirtualWire library declaration
#include <AFMotor.h>
byte msg[VW_MAX_MESSAGE_LEN];//Data flow is received
byte msgLen = VW_MAX_MESSAGE_LEN;
char forward[1] = {1};

AF_DCMotor motor1(1, MOTOR12_1KHZ);
AF_DCMotor motor2(2, MOTOR12_1KHZ);
AF_DCMotor motor3(3, MOTOR34_1KHZ);
AF_DCMotor motor4(4, MOTOR34_1KHZ);

void setup()
{
  Serial.begin(9600);
  Serial.println("READY..........");
  vw_setup(2048); // set the baud rate
  vw_set_rx_pin(2);// Set up digital pins to receive signals
  vw_rx_start();// started receiving the signal
}


void loop()
{
  if (vw_get_message(msg, &msgLen)) // if the signal is transmitted
  {
    Serial.print("got transmition");
    motor1.setSpeed(255);
    motor2.setSpeed(255);
    motor3.setSpeed(255);
    motor4.setSpeed(255);
    switch (msg[0])
    {
      case '0':
        {
          motor1.run(RELEASE);
          motor2.run(RELEASE);
          motor3.run(RELEASE);
          motor4.run(RELEASE);
          break;
        }

      case '1':
        {
          motor1.run(FORWARD);
          motor2.run(FORWARD);
          motor3.run(FORWARD);
          motor4.run(FORWARD);
          break;
        }

      case '2':
        {
          motor1.setSpeed(150);
          motor2.setSpeed(150);
          motor3.setSpeed(150);
          motor4.setSpeed(150);
          motor1.run(FORWARD);
          motor2.run(FORWARD);
          motor3.run(BACKWARD);
          motor4.run(BACKWARD);
          break;
        }

      case '3':
        {
          motor1.setSpeed(150);
          motor2.setSpeed(150);
          motor3.setSpeed(150);
          motor4.setSpeed(150);
          motor1.run(BACKWARD);
          motor2.run(BACKWARD);
          motor3.run(FORWARD);
          motor4.run(FORWARD);
          break;
        }

      case '4':
        {
          motor1.run(BACKWARD);
          motor2.run(BACKWARD);
          motor3.run(BACKWARD);
          motor4.run(BACKWARD);
          break;
        }

      case '5':
        {
          motor3.setSpeed(70);
          motor4.setSpeed(70);
          motor1.run(FORWARD);
          motor2.run(FORWARD);
          motor3.run(FORWARD);
          motor4.run(FORWARD);
          break;
        }

      case '6':
        {
          motor1.setSpeed(70);
          motor2.setSpeed(70);
          motor1.run(FORWARD);
          motor2.run(FORWARD);
          motor3.run(FORWARD);
          motor4.run(FORWARD);
          break;
        }
    }



}
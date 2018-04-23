int enA = 6;
int right1 = 3;
int right2 = 2;

int enB = 5;
int left1 = 7;
int left2 = 4;

int rx = 0;
int tx = 1;

void setup(){
  pinMode(enA, OUTPUT);
  pinMode(enB, OUTPUT);

  pinMode(left1, OUTPUT);
  pinMode(left2, OUTPUT);
  pinMode(right1, OUTPUT);
  pinMode(right2, OUTPUT);

  pinMode(rx, OUTPUT);
  pinMode(tx, OUTPUT);

  Serial.begin(9600);
}

void remoteControl(int value);
void loop() {
  if(Serial.available() > 0){
    int n = Serial.read(); 
    remoteControl(n);
  }
}
void remoteControl(int value){
  if (value<=100){
      value -= 50;
      if (value>0){
         digitalWrite(left1, HIGH);
         digitalWrite(left2, LOW);
         analogWrite(enA, value*4);
         return;
      }else{
         digitalWrite(left1, LOW);
         digitalWrite(left2, HIGH);
         analogWrite(enA, value*(-4));
         return;
      }
  }else{
      value -= 150;
      if (value>0){
         digitalWrite(right1, LOW);
         digitalWrite(right2, HIGH);
         analogWrite(enB, value*4);
         return;
      }else{
         digitalWrite(right1, HIGH);
         digitalWrite(right2, LOW);
         analogWrite(enB, value*(-4));
         return;
      }
  }
}


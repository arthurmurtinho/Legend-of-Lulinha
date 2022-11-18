class Enemy {
  PVector position;
  PVector velocity;
  float xoff = random(1000);

  Enemy () {
    position = new PVector(random(width), 0);
    velocity = new PVector(0, 0.8);
  }
  void run() {
    show();
    move();
  }

  void show() {
    imageMode(CENTER);
    image(luiz, position.x, position.y);
  }



  void move() {
    if (player.getBoolean("movement") == true) {
      float velx = map(noise(xoff), 0, 1, -1, 1);
      xoff += 0.01;
      PVector dodge = new PVector(velx, 0);
      dodge.limit(0.01);
      velocity.add(dodge);
    }
    position.add(velocity);
    if ((position.x > width) || (position.x < 0)) {
      velocity.x *= -1;
    }
  }

  boolean hasKilled() {
    if (position.y > height - 10) {
      return true;
    } else {
      return false;
    }
  }


  boolean isHit(Missile m) {
    float dist = PVector. dist(position, m.position);
    if (dist - (m.size/2) < 1) {
      return true;
    } else {
      return false;
    }
  }
}

class Particle {
  PVector pos;
  PVector velocity;
  PVector acceleration;
  PVector gravity = new PVector(0, 0.01);
  float health;
  PImage image;
  float xspeed = random(-1, 1);
  float yspeed = random(-1, 1);

  Particle(PVector p) {
    pos = p.copy();
    velocity = new PVector(0, 0);
    acceleration = new PVector(0, 0);
    health = 255;
    image = null;
  }

  void run() {
    pos.x += xspeed;
    pos.y += yspeed;
    velocity.add(gravity);
    pos.add(velocity);
    health -= 5;
  }
  void show() {
    colorMode(HSB);
    noStroke();
    fill(255, 255,255, health);
    ellipse(pos.x, pos.y, 5, 5);
  }

  void applyForce(PVector f) {
    acceleration.add(f);
  }
}

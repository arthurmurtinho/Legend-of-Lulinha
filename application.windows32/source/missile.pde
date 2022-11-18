class Missile {
  ArrayList<Flares> flares;
  private PVector position;
  PVector velocity;
  PVector target;
  PImage image;
  PImage explosion;
  float size;
  float health;
  int expS;
  boolean exploded = false;

  Missile(PVector t) {
    flares = new ArrayList<Flares>();
    position = new PVector(0, height);
    velocity = new PVector(0, 0);
    target = t.copy();
    image = loadImage("missile.png");
    explosion = loadImage("explosion.png");
    size = 10;
    expS = 5;
    explosion.resize(50, 5);
    health = 155;
  }

  void run() {
    show();
    seekTarget();
  }

  void show() {
    if (exploded) {
      pushMatrix();
      //image(explosion, position.x, position.y);
      pushMatrix();
      noStroke();
      fill(255, health);
      ellipseMode(CENTER);
      ellipse(position.x, position.y, size, size);
      pushMatrix();
      flares.add(new Flares(position));
      for (Flares f : flares) {
        f.run();
      }
      popMatrix();
      popMatrix();
      popMatrix();
    } else {
      imageMode(CENTER);
      image(image, position.x, position.y);
    }
  }

  void seekTarget() {
    PVector desired = PVector.sub(target, position);
    desired.setMag(0.1);
    velocity.add(desired);
    position.add(velocity);
    float distance = PVector.dist(target, position);
    if (distance<10) {
      velocity.mult(0);
      exploded = true;
      size+=2;
      expS+=2;
      explosion.resize(expS, expS);
      if (size > 50) {
        health -= 5;
        size = 50;
      }
    }
  }

  boolean isDone() {
    if (health <= 0) {
      return true;
    } else {
      return false;
    }
  }
}

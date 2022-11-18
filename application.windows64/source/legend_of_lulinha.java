import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.sound.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class legend_of_lulinha extends PApplet {



JSONObject player; 
SoundFile soundfile;
ArrayList<Enemy> enemies = new ArrayList<Enemy>();
Missile m;
int Phase = 0;
int numEnemy =  2;
boolean gameover = false;
boolean finished = false;
PImage luiz;
PImage background;
String backName;
String luizName;
String soundName;
float fade = 1;

public void setup() {
  try {
    player = loadJSONObject("data/player.json");
  } 
  catch(NullPointerException e) {
    player = new JSONObject();
    player.setBoolean("cleared", false);
    player.setInt("won", 0);
    player.setInt("lost", 0);
    player.setBoolean("movement", true);
    player.setBoolean("fullscreen", true);
    saveJSONObject(player, "data/player.json");
  }
  
  for (int i = 0; i <= numEnemy; i++) {
    enemies.add(new Enemy());
  }
}


public void draw() {
  if (Phase == 4) {
    finished = true;
    int won = player.getInt("won");
    won += 1;
    player.setInt("won", won);
    player.setBoolean("cleared", true);
    saveJSONObject(player, "data/player.json");
    background(0);
    textAlign(CENTER);
    text("FELIZ ANIVERSARIO LULU!", width/2, height/2);
    textSize(20);
    text("lov you", width/2, height/2 + 50);
    if (fade > 0.5f) {
      fade -= 0.01f;
    }
    soundfile.amp(fade);
    noLoop();
  } else 
  if (background == null) {
    background(0);
    textSize(30);
    fill(255);
    textAlign(CENTER);
    text("LEGEND OF LULINHA", width/2, height/2);
    textSize(20);
    text("press ENTER to begin", width/2, height/2 + 50);
  } else {
    background(background);
    for (int i = enemies.size()-1; i > 0; i--) {
      Enemy e = enemies.get(i);
      e.run();
      if ((m != null)&& (e.isHit(m) == true)) {
        enemies.remove(i);
      }
      if (e.hasKilled() == true) {
        gameover = true;
        int lost = player.getInt("lost");
        lost += 1;
        player.setInt("lost", lost);
        saveJSONObject(player, "data/player.json");
        fill(255);
        textSize(50);
        textAlign(CENTER);
        text("GAME OVER", width/2, height/2);
        noLoop();
      }
    }
    if (m != null) {
      m.run();
      if (m.isDone()) m = null;
    }
    if (enemies.size()-1 == 0) {
      fill(255);
      textSize(30);
      textAlign(CENTER);
      text("Phase " + (Phase + 1) + " complete!", width/2, height/2);
      textSize(20);
      text("press ENTER to continue", width/2, height/2 + 50);
      numEnemy++;
      Phase++;
      for (int i = 0; i < numEnemy; i++) {
        enemies.add(new Enemy());
      }
      noLoop();
    }
  }
}

public void mousePressed() {
  if (m == null) {
    m = new Missile(new PVector(mouseX, mouseY));
  }
}
public void keyPressed() {
  if (key == ENTER) {
    if ((!gameover) && (!finished)) {
      loop();
    } 
    changePhase();
    if ((gameover)|| (finished)) {
      exit();
    }
  }
  if (player.getBoolean("cleared")==true) {
    switch(key) {
    case '1': 
      Phase = 0;
      changePhase();
      break;
    case '2': 
      Phase = 1;
      changePhase();
      break;
    case '3': 
      Phase = 2;
      changePhase();
      break;
    case '4': 
      Phase = 3;
      changePhase();
      break;
    }
  }
  if (key == TAB) {
    showScoreboard();
    noLoop();
  }
}

public void changePhase() {
  int phaseCount = Phase +1;
  backName = "background" + phaseCount + ".png";
  println(backName);
  background = loadImage(backName);
  background.resize(width, height);

  luizName = "luiz" + phaseCount + ".png";
  println(luizName);
  luiz = loadImage(luizName);
  if (soundfile != null) {
    soundfile.amp(0);
  }
  soundName = "trilha" + phaseCount + ".wav";
  println(soundName);
  soundfile = new SoundFile(this, soundName);
  soundfile.loop();
  soundfile.amp(1);
}

public void showScoreboard() {
  println("showingScoreboard");
  int won = player.getInt("won");
  int lost =  player.getInt("lost");
  textSize(20);
  fill(255);
  text("times won: " + won, width/2, height/2);
  text("times lost: " + lost, width/2, height/2 + 50);
  textSize(15);
  text("press ENTER to continue", width/2, height/2 + 100);
}
class Flares {
  ArrayList<Particle> flares;
  PVector position;
  float angle = 0.0f;

  Flares(PVector p) {
    position = p.copy();
    flares = new ArrayList<Particle>();
    for (int i = 0; i < 1; i++){
      flares.add(new Particle(position));
    }
  }
  
  public void run() {
    for (Particle p : flares) {
      p.show();
      p.run();
    }
  }
}
class Particle {
  PVector pos;
  PVector velocity;
  PVector acceleration;
  PVector gravity = new PVector(0, 0.01f);
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

  public void run() {
    pos.x += xspeed;
    pos.y += yspeed;
    velocity.add(gravity);
    pos.add(velocity);
    health -= 5;
  }
  public void show() {
    colorMode(HSB);
    noStroke();
    fill(255, 255,255, health);
    ellipse(pos.x, pos.y, 5, 5);
  }

  public void applyForce(PVector f) {
    acceleration.add(f);
  }
}
class Enemy {
  PVector position;
  PVector velocity;
  float xoff = random(1000);

  Enemy () {
    position = new PVector(random(width), 0);
    velocity = new PVector(0, 0.8f);
  }
  public void run() {
    show();
    move();
  }

  public void show() {
    imageMode(CENTER);
    image(luiz, position.x, position.y);
  }



  public void move() {
    if (player.getBoolean("movement") == true) {
      float velx = map(noise(xoff), 0, 1, -1, 1);
      xoff += 0.01f;
      PVector dodge = new PVector(velx, 0);
      dodge.limit(0.01f);
      velocity.add(dodge);
    }
    position.add(velocity);
    if ((position.x > width) || (position.x < 0)) {
      velocity.x *= -1;
    }
  }

  public boolean hasKilled() {
    if (position.y > height - 10) {
      return true;
    } else {
      return false;
    }
  }


  public boolean isHit(Missile m) {
    float dist = PVector. dist(position, m.position);
    if (dist - (m.size/2) < 1) {
      return true;
    } else {
      return false;
    }
  }
}
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

  public void run() {
    show();
    seekTarget();
  }

  public void show() {
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

  public void seekTarget() {
    PVector desired = PVector.sub(target, position);
    desired.setMag(0.1f);
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

  public boolean isDone() {
    if (health <= 0) {
      return true;
    } else {
      return false;
    }
  }
}
  public void settings() {  size(400, 400); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "legend_of_lulinha" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}

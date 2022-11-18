import processing.sound.*;

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

void setup() {
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
  size(400, 400);
  for (int i = 0; i <= numEnemy; i++) {
    enemies.add(new Enemy());
  }
}


void draw() {
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
    if (fade > 0.5) {
      fade -= 0.01;
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

void mousePressed() {
  if (m == null) {
    m = new Missile(new PVector(mouseX, mouseY));
  }
}
void keyPressed() {
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

void changePhase() {
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

void showScoreboard() {
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

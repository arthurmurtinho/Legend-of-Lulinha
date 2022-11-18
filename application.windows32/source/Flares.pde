class Flares {
  ArrayList<Particle> flares;
  PVector position;
  float angle = 0.0;

  Flares(PVector p) {
    position = p.copy();
    flares = new ArrayList<Particle>();
    for (int i = 0; i < 1; i++){
      flares.add(new Particle(position));
    }
  }
  
  void run() {
    for (Particle p : flares) {
      p.show();
      p.run();
    }
  }
}

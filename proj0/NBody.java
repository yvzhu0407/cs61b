public class NBody {
    public static Planet[] readPlanets(String planetsTxtPath) {
        In in = new In(planetsTxtPath);
        int len = in.readInt();
        Planet[] planets = new Planet[len];
        double radius = in.readDouble();
        for(int i = 0;i < len;i++){
            planets[i] = new Planet(in.readDouble(),in.readDouble(),in.readDouble(),in.readDouble(),in.readDouble(),in.readString());
        }
        return planets;
    }

    public static double readRadius(String planetsTxtPath) {
        In in = new In(planetsTxtPath);
        int line = in.readInt();
        return in.readDouble();
    }

    public static void main(String[] args) {
//        String[] args = {"2e11","20000","data/planets.txt"};
        double T = Double.parseDouble(args[0]);
        double dt = Double.parseDouble(args[1]);
        String filename = args[2];

        Planet[] planets = readPlanets( filename);
        int len = planets.length;
        double[] xForces = new double[len];
        double[] yForces = new double[len];
        double radius = readRadius(filename);
        StdDraw.setScale(-radius,radius);
        StdDraw.clear();
        StdDraw.enableDoubleBuffering();
        double t= 0;
        while(t < T){
            for(int i = 0;i < len;i++){
                xForces[i] = planets[i].calcNetForceExertedByX(planets);
                yForces[i] = planets[i].calcNetForceExertedByY(planets);
            }
            for(int i = 0;i < len;i++){
                planets[i].update(dt,xForces[i],yForces[i]);
            }
            StdDraw.clear();
            for(Planet p : planets){
                p.draw();
            }
            StdDraw.show();
            StdDraw.pause(10);
            t += dt;
        }
        StdOut.printf("%d\n", planets.length);
        StdOut.printf("%.2e\n", radius);
        for (Planet planet : planets) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                    planet.xxPos, planet.yyPos, planet.xxVel,
                    planet.yyVel, planet.mass, planet.imgFileName);
        }
    }
}

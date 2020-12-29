public class Planet {
    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;
    private final double G = 6.67e-11;
    public Planet(double xP, double yP, double xV, double yV, double m, String img){
        this.xxPos = xP;
        this.yyPos = yP;
        this.xxVel = xV;
        this.yyVel = yV;
        this.mass = m;
        this.imgFileName = img;
    }
    public Planet(Planet p){
        this.xxPos = p.xxPos;
        this.yyPos = p.yyPos;
        this.xxVel = p.xxVel;
        this.yyVel = p.yyVel;
        this.mass = p.mass;
        this.imgFileName = p.imgFileName;
    }
    public void update(double interval,double xF,double yF){
        double xa = xF / mass;
        double ya = yF / mass;
        xxVel += interval * xa;
        yyVel += interval * ya;
        xxPos += interval * xxVel;
        yyPos += interval * yyVel;
    }

    public double calcForceExertedBy(Planet p2) {
        double r = calcDistance(p2);
        return G * mass * p2.mass / r / r;
    }

    public double calcForceExertedByX(Planet p2) {
        double F = calcForceExertedBy(p2);
        double r = calcDistance(p2);
        double dx = p2.xxPos - xxPos;
        return F * dx / r;
    }

    public double calcForceExertedByY(Planet p2) {
        double F = calcForceExertedBy(p2);
        double r = calcDistance(p2);
        double dy = p2.yyPos - yyPos;
        return F * dy / r;
    }

    public double calcDistance(Planet p2) {
        return Math.sqrt((p2.xxPos - xxPos) * (p2.xxPos - xxPos) + (p2.yyPos - yyPos) * (p2.yyPos - yyPos));
    }

    public double calcNetForceExertedByX(Planet[] planets) {
        double xF = 0;
        for(Planet p : planets){
            if(xxPos != p.xxPos || yyPos != p.yyPos){
                xF += calcForceExertedByX(p);
            }
        }
        return xF;
    }

    public double calcNetForceExertedByY(Planet[] planets) {
        double yF = 0;
        for(Planet p : planets){
            if(xxPos != p.xxPos || yyPos != p.yyPos){
                yF += calcForceExertedByY(p);
            }

        }
        return yF;
    }
    public void draw(){
        StdDraw.picture(xxPos,yyPos,"images/" + imgFileName);
    }
}

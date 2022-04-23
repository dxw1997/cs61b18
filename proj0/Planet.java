
public class Planet{

    static final double G = 6.67e-11;

    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;

    public Planet(double xP, double yP, double xV, double yV, double m, String img){
	xxPos = xP;
	yyPos = yP;
	xxVel = xV;
    	yyVel = yV;
	mass = m;
	imgFileName = img;
    }

    public Planet(Planet p){
	this.xxPos = p.xxPos;
	this.yyPos = p.yyPos;
	this.xxVel = p.xxVel;
	this.yyVel = p.yyVel;
	this.mass = p.mass;
	this.imgFileName = p.imgFileName;
    }

    public double calcDistance(Planet p){
	return Math.sqrt((p.xxPos-xxPos)*(p.xxPos-xxPos) + (p.yyPos-yyPos)*(p.yyPos-yyPos));
    }

    public double calcForceExertedBy(Planet p){
	///assert p != this
	if(p.xxPos == xxPos && p.yyPos == yyPos) return 0;
	return G*mass*p.mass/(calcDistance(p)*calcDistance(p));
    }

    public double calcForceExertedByX(Planet p){
	if(p.xxPos == xxPos && p.yyPos == yyPos) return 0;
	return calcForceExertedBy(p)*(p.xxPos-xxPos)/calcDistance(p);
    }

    public double calcForceExertedByY(Planet p){
	if(p.xxPos == xxPos && p.yyPos == yyPos) return 0;
	return calcForceExertedBy(p)*(p.yyPos-yyPos)/calcDistance(p);
    }

    public double calcNetForceExertedByX(Planet[] allPlanets){
	double res = 0;
	for(int i = 0;i < allPlanets.length;i++ ){
	    res += calcForceExertedByX(allPlanets[i]);
	}
	return res;
    }

    public double calcNetForceExertedByY(Planet[] allPlanets){
	double res = 0;
	for(int i = 0;i < allPlanets.length;i++ ){
	    res += calcForceExertedByY(allPlanets[i]);
	}
	return res;
    }

    public void update(double dt, double fx, double fy){
        xxVel = xxVel + fx/mass*dt;
	yyVel = yyVel + fy/mass*dt;
	xxPos = xxPos + xxVel*dt;
	yyPos = yyPos + yyVel*dt;
	return;
    }

    public void draw(){
	StdDraw.picture(xxPos ,yyPos ,"images/"+imgFileName);
    }

}


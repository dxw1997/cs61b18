
public class NBody{
    ///int N;
    ///double R;

    public static double readRadius(String file){
	In in = new In(file);
	in.readInt();
	return in.readDouble();////not handling exceptions here...
    }

    public static Planet[] readPlanets(String file){
	In in = new In(file);
	int N = in.readInt();
	Planet[] res = new Planet[N];
	in.readDouble();
	for(int i = 0;i < N;++i ){
	    double xx = in.readDouble();
	    double yy = in.readDouble();
	    double vx = in.readDouble();
	    double vy = in.readDouble();
	    double mass = in.readDouble();
	    String path = in.readString();
	    res[i] = new Planet(xx, yy, vx, vy, mass, path);
	}
	return res;
    }

    public static void main(String[] args){
	if(args.length != 3){
	    System.out.println("the number of args should be 3.");
	    return;
	}
	double T = Double.valueOf(args[0]);
	double dt = Double.valueOf(args[1]);
	String filename = args[2];
	Planet[] ps = readPlanets(filename);
	double radius = readRadius(filename);

	StdDraw.enableDoubleBuffering();
	StdDraw.setScale(-radius, radius);

	double nowT = 0;
	while(nowT < T){
	    double[] xForces = new double[ps.length];
	    double[] yForces = new double[ps.length];
	    for(int i = 0;i < xForces.length;++i ){
	    	xForces[i] = ps[i].calcNetForceExertedByX(ps);
		yForces[i] = ps[i].calcNetForceExertedByY(ps);
	    }
	    for(int i = 0;i < ps.length;++i ){
		ps[i].update(dt, xForces[i], yForces[i]);
	    }

	    StdDraw.picture(0 ,0 ,"images/starfield.jpg");
  	    for(int i = 0;i < ps.length;++i )
	    	ps[i].draw();
	    StdDraw.show();
	    StdDraw.pause(5);

	    nowT += dt;
	}///while
	StdOut.printf("%d\n", ps.length);
	StdOut.printf("%.2e\n", radius);
	for (int i = 0; i < ps.length; i++) {
	    StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
	                  ps[i].xxPos, ps[i].yyPos, ps[i].xxVel,
         	         ps[i].yyVel, ps[i].mass, ps[i].imgFileName);   
	}
	return;
    }

}



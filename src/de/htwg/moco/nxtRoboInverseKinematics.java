package de.htwg.moco;


public class nxtRoboInverseKinematics {

    private double l1;
    private double l2;
    private double q1;
    private double q2;

    public nxtRoboInverseKinematics(double l1, double l2){
        this.l1 = l1;
        this.l2 = l2;
    }

    public void doInverseKinematics(float x, float y){
        //calculate helping angle alpha
        double alpha = Math.atan2(y, x);

        //calculate helping angle beta with the law of cosines
        double beta = Math.acos((Math.pow(this.l1, 2) + Math.pow(getEuclideanNorm(x ,y), 2) - Math.pow(this.l2, 2)) / (2 * this.l1 * getEuclideanNorm(x, y)));

        //calculate helping angle gamma with the law of cosines
        double gamma = Math.acos((Math.pow(this.l1, 2) + Math.pow(this.l2, 2) - Math.pow(getEuclideanNorm(x, y), 2))/(2 * this.l1 * this.l2));

        this.q1 = alpha - beta;
        this.q2 = Math.PI - gamma;
    }

    private double getEuclideanNorm(float x, float y){
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    public double getQ1() {
        return q1;
    }

    public double getQ2() {
        return q2;
    }

}

package de.htwg.moco;

public class nxtRoboForwardKinematics {
    private double l1;
    private double l2;

    private double[][] transformation0To1;
    private double[][] transformation1To2;

    private double[] endPointArm1;
    private double[] endPointArm2;

    public double[] getEndPointArm1() {
        return endPointArm1;
    }

    public double[] getEndPointArm2() {
        return endPointArm2;
    }

    public nxtRoboForwardKinematics(double l1, double l2){
        this.l1 = l1;
        this.l2 = l2;
    }

    private void initMatrices(double q1, double q2){
        transformation0To1 = new double[][]{{Math.cos(q1), -Math.sin(q1), 0, this.l1 * Math.cos(q1)},
                {Math.sin(q1), Math.cos(q1), 0, this.l1 * Math.sin(q1)},
                {0, 0, 1, 0},
                {0, 0, 0, 1}};

        transformation1To2 = new double[][]{{Math.cos(q2), -Math.sin(q2), 0, this.l2 * Math.cos(q2)},
                {Math.sin(q2), Math.cos(q2), 0, this.l2 * Math.sin(q2)},
                {0, 0, 1, 0},
                {0, 0, 0, 1}};
    }

    public void doForwardKinematics(double q1, double q2){
        initMatrices(q1, q2);

        double[] origin = new double[]{0,0,0,1};

        endPointArm1 = Matrix.multiply(transformation0To1, origin);
        endPointArm2 = Matrix.multiply(Matrix.multiply(transformation0To1, transformation1To2), origin);
    }

}

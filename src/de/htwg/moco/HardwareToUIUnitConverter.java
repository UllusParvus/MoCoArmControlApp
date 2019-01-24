package de.htwg.moco;

/**
 * Provides helper methods to convert the degrees the joints should reach to actual motor encoder
 * positions (in radians). Therefore the motor gear ratios have to be taken into account.
 *
 * @author  Christoph Ulrich, Benjamin Schaefer
 */

public class HardwareToUIUnitConverter {

    private double joint1GearRatio;
    private double joint2GearRatio;
    private double gripperGearRatio;

    public HardwareToUIUnitConverter(){
        this.joint1GearRatio = 42;
        this.joint2GearRatio = 12.5;
        this.gripperGearRatio = 24;
    }

    /**
     * Converts from goal degrees to motor position radians.
     * @param goalDegrees goal in degrees joint 1 has to reach.
     * @return radians which have to be applied to motor 1 to reach the specified goal degrees.
     */
    public double getMotor1RotationRadians(double goalDegrees){
        return (Math.toRadians(360) * this.joint1GearRatio) / (Math.toRadians(360) / Math.toRadians(goalDegrees));
    }

    /**
     * Converts from goal degrees to motor position radians.
     * @param goalDegrees goal in degrees joint 2 has to reach.
     * @return radians which have to be applied to motor 2 to reach the specified goal degrees.
     */
    public double getMotor2RotationRadians(double goalDegrees){
        return (Math.toRadians(360) * this.joint2GearRatio) / (Math.toRadians(360) / Math.toRadians(goalDegrees));
    }

    /**
     * Converts from goal degrees to motor position radians.
     * @param goalDegrees goal in degrees joint 3 has to reach.
     * @return radians which have to be applied to motor 3 to reach the specified goal degrees.
     */
    public double getGripperRotationRadians(double goalDegrees){
        return (Math.toRadians(360) * this.gripperGearRatio) / (Math.toRadians(360) / Math.toRadians(goalDegrees));
    }

}

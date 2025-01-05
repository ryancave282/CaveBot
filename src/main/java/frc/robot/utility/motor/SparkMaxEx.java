package frc.robot.utility.motor;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import frc.robot.DroidRageConstants;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkAbsoluteEncoder;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;

public class SparkMaxEx extends CANMotorEx{

    private final SparkMax motor;
    private final SparkMaxConfig config = new SparkMaxConfig();
    
    private SparkMaxEx(SparkMax motor) {
        this.motor = motor;
    }

    public static DirectionBuilder create(int deviceID) {
        CANMotorEx motor = new SparkMaxEx(new SparkMax(deviceID, MotorType.kBrushless));
        motor.motorID = deviceID;
        motor.motor = Motor.SparkMax;
        return motor.new DirectionBuilder();
    }

    @Override
    public void setPower(double power) {
        if(isEnabledWriter.get()){
            motor.set(power);
        }
        if(DroidRageConstants.removeWriterWriter.get()){
            outputWriter.set(power);
        }
    }

    @Override
    public void setVoltage(double outputVolts) {
        if(isEnabledWriter.get()){
            motor.setVoltage(outputVolts);
        }
        if(DroidRageConstants.removeWriterWriter.get()){
            outputWriter.set(outputVolts);
        }
    }

    @Override
    public void setDirection(Direction direction) {
        config.inverted(switch (direction) {
                case Forward -> false;
                case Reversed -> true;
            });
    }

    @Override
    public void setIdleMode(ZeroPowerMode mode) {
        config.idleMode(switch (mode) {
            case Brake -> IdleMode.kBrake;
            case Coast -> IdleMode.kCoast;
        });
    }

    public SparkMax getSparkMax() {
        return motor;
    }

    public RelativeEncoder getEncoder() {
        return motor.getEncoder();
    }

    // public RelativeEncoder getAlternateEncoder(int countsPerRev) {
    //     return motor.getAlternateEncoder(countsPerRev);
    // }

    public RelativeEncoder getAlternateEncoder() {
        return motor.getAlternateEncoder();
    }

    @Override
    public double getVelocity() {
        return motor.getEncoder().getVelocity();
    }

    @Override
    public double getPosition() {
        return motor.getEncoder().getPosition();
    }

    // public SparkAbsoluteEncoder getAbsoluteEncoder(SparkAbsoluteEncoder.Type encoderType) {
    //     return motor.getAbsoluteEncoder(encoderType);
    // }

    public SparkAbsoluteEncoder getAbsoluteEncoder() {
        return motor.getAbsoluteEncoder();
    }

    public void follow(SparkMaxEx leader, boolean invert) {
        config.follow(leader.getSparkMax(), invert);
    }

    public void burnFlash() {
        motor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    @Override
    public int getDeviceID() {
        return motor.getDeviceId();
    }

    public double getSpeed(){
        return motor.get();
    }

    //Casting the double to an int
    @Override
    public void setSupplyCurrentLimit(double currentLimit) {
        config.smartCurrentLimit((int) currentLimit);
    }

    public void setStatorCurrentLimit(double currentLimit) {}
        
    public double getVoltage(){
        // return motor.getAppliedOutput();//motor controller's applied output duty cycle.
        // return motor.getBusVoltage();//voltage fed into the motor controller.
        return motor.getOutputCurrent();//motor controller's output current in Amps.
    }

    @Override
    public void resetEncoder(int num) {
        motor.getEncoder().setPosition(num);
    }
}

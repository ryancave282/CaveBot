package frc.robot.utility.encoder;

import com.revrobotics.spark.SparkAbsoluteEncoder;
import com.revrobotics.spark.config.EncoderConfig;

import frc.robot.DroidRageConstants.EncoderDirection;
import frc.robot.utility.motor.SparkMaxEx;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

public class SparkAbsoluteEncoderEx {
    protected final SparkAbsoluteEncoder encoder;
    EncoderConfig config;
    
    public ShuffleboardValue<Double> degreeWriter;
    public ShuffleboardValue<Double> radianWriter;
    public ShuffleboardValue<Double> rawWriter;
    public  ShuffleboardValue<Boolean> isConnectedWriter;
    public String name;
    
    private SparkAbsoluteEncoderEx(SparkAbsoluteEncoder encoder) {
        this.encoder = encoder;
    }
    
    public static DirectionBuilder create(SparkMaxEx motor) {
        SparkAbsoluteEncoderEx encoder = new SparkAbsoluteEncoderEx(motor.getAbsoluteEncoder());
        return encoder.new DirectionBuilder();
    }
    
    public class DirectionBuilder {
        public PositionConversionFactorBuilder withDirection(EncoderDirection direction) {
            setDirection(direction);
            return new PositionConversionFactorBuilder();
        }
    }

    public class PositionConversionFactorBuilder {
        public BaseWriter withPositionConversionFactor(double positionConversionFactor) {
            setPositionConversionFactor(positionConversionFactor);
            setVelocityConversionFactor(positionConversionFactor/60);
            return new BaseWriter();
        }
    }
    
    public class BaseWriter {
        @SuppressWarnings("unchecked")
        public <T extends SparkAbsoluteEncoderEx> T withSubsystemBase(String subsystemBaseName) {
            name = subsystemBaseName;
            rawWriter = ShuffleboardValue
                .create(0.0, name+"/Pos/Raw", name)
                .withSize(1, 2)
                .build();
            degreeWriter = ShuffleboardValue
                .create(0.0, name+"/Pos/Degree", name)
                .withSize(1, 2)
                .build();
            radianWriter = ShuffleboardValue
                .create(0.0, name+"/Pos/Radian", name)
                .withSize(1, 2)
                .build();
            return (T) SparkAbsoluteEncoderEx.this;
        }
    }

    public void periodic(){
        rawWriter.set(getPosition());
        degreeWriter.set(getDegrees());
        radianWriter.set(getRadian());
    }

    public double getPosition() {
        //Raw Position/Radian Position
        return encoder.getPosition();
    }

    public double getDegrees() {
        return encoder.getPosition()*(180/Math.PI);
    }

    public double getRadian() {
        return encoder.getPosition();
    }

    public double getVelocity() {
        return encoder.getVelocity();  
    }

    public void setDirection(EncoderDirection direction) {
        switch (direction) {
            case Reversed:
                config.inverted(true);
            case Forward:
                config.inverted(false);
        }
    }

    public void setPositionConversionFactor(double positionConversionFactor) {
        config.positionConversionFactor(positionConversionFactor);
    }
    public void setVelocityConversionFactor(double velocityConversionFactor) {
        config.velocityConversionFactor(velocityConversionFactor);
    }
}

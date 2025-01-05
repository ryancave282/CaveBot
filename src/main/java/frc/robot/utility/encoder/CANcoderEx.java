package frc.robot.utility.encoder;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.signals.SensorDirectionValue;
import frc.robot.utility.encoder.EncoderConstants.EncoderDirection;

public class CANcoderEx {
    protected EncoderDirection direction;
    public int deviceID;

    public enum EncoderRange {
        ZERO_TO_ONE,
        PLUS_MINUS_HALF
    }
    
    
    
    private final CANcoder encoder;
    private CANcoderConfiguration config;
    private double positionConversionFactor, velocityConversionFactor;

    public CANcoderEx(CANcoder encoder) {
        this.encoder = encoder;
    }

    public double getVelocity() {
        return encoder.getVelocity().getValueAsDouble()*velocityConversionFactor;
    }
        
    public double getPosition() {
        return encoder.getPosition().getValueAsDouble()*positionConversionFactor;
    }

    public static DirectionBuilder create(int deviceID) {
        CANcoderEx encoder = new CANcoderEx(new CANcoder(deviceID));
        encoder.deviceID = deviceID;
        return encoder.new DirectionBuilder();
    }

    public class DirectionBuilder {
        public RangeBuilder withDirection(EncoderDirection direction) {
            setDirection(direction);
            return new RangeBuilder();
        }
    }

    public class RangeBuilder {
        public OffsetBuilder withRange(EncoderRange range) {
            setRange(range);
            return new OffsetBuilder();
        }
    }

    public class OffsetBuilder {
        @SuppressWarnings("unchecked")
        public <T extends CANcoderEx> T withOffset(double offset) {
            setMagnetSensorOffset(offset);
            encoder.getConfigurator().apply(config);
            return (T) CANcoderEx.this;
        }
    }

    public void setDirection(EncoderDirection direction) {
        switch (direction) {
            case Reversed:
            config.MagnetSensor.SensorDirection = SensorDirectionValue.CounterClockwise_Positive;
            break;
            default:
            case Forward:
            config.MagnetSensor.SensorDirection = SensorDirectionValue.Clockwise_Positive;
            break;
        }
    }

    public void setRange(EncoderRange range) {
        switch (range) {
            case PLUS_MINUS_HALF:
            config.MagnetSensor.AbsoluteSensorDiscontinuityPoint = 0.5;
            break;
            default:
            case ZERO_TO_ONE:
            config.MagnetSensor.AbsoluteSensorDiscontinuityPoint = 1;
            break;
        }
    }

    public void setMagnetSensorOffset(double offset) {
        config.MagnetSensor.MagnetOffset = offset;
    }

    public double getAbsolutePosition() {
        return encoder.getAbsolutePosition().getValueAsDouble();
    }

    public int getDeviceID() {
        return encoder.getDeviceID();
    }
}



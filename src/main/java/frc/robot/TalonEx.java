package frc.robot;

import static edu.wpi.first.units.Units.Volts;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import net.droidrage.lib.DroidRageConstants;
import net.droidrage.lib.motor.CANMotorEx;
import net.droidrage.lib.motor.CANMotorEx.IdleModeBuilder;
import net.droidrage.lib.shuffleboard.ShuffleboardValue;

public class TalonEx extends TalonFX {
    protected TalonFXConfiguration configuration = new TalonFXConfiguration();
    protected MotorOutputConfigs motorOutputConfigs = new MotorOutputConfigs();
    protected ShuffleboardValue<Boolean> isEnabledWriter;
    protected ShuffleboardValue<Double> outputWriter;
    protected double positionConversionFactor;
    protected double velocityConversionFactor;
    protected String subSystemName;
    protected int deviceID;
    protected CANBus canbus;

    public enum Direction {
        Forward,
        Reversed,
    }

    public enum ZeroPowerMode {
        Brake,
        Coast,
    }

    private TalonEx(int deviceID, CANBus canbus) {
        super(deviceID, canbus);
        this.deviceID = deviceID;
        this.canbus = canbus;
    }

    private TalonEx(int deviceID) {
        super(deviceID);
        this.deviceID = deviceID;
    }

    private static TalonEx motor;

    public static DirectionBuilder create(int deviceID, CANBus canbus) {
        motor = new TalonEx(deviceID, canbus);
        return motor.new DirectionBuilder();
    }
    
    public static DirectionBuilder create(int deviceID) {
        motor = new TalonEx(deviceID);
        return motor.new DirectionBuilder();
    }


    public class DirectionBuilder {
        public IdleModeBuilder withDirection(Direction direction) {
            setDirection(direction);
            return new IdleModeBuilder();
        }
    }

    public class IdleModeBuilder {
        public PositionConversionFactorBuilder withIdleMode(ZeroPowerMode idleMode) {
            setIdleMode(idleMode);
            return new PositionConversionFactorBuilder();
        }
    }
    public class PositionConversionFactorBuilder {
        public SubstemNameBuilder withPositionConversionFactor(double positionConversionFactor) {
            setPositionConversionFactor(positionConversionFactor);
            setVelocityConversionFactor(positionConversionFactor/60);
            return new SubstemNameBuilder();
        }
    }

    protected void setPositionConversionFactor(double positionConversionFactor){
        this.positionConversionFactor=positionConversionFactor;
    }
    protected void setVelocityConversionFactor(double velocityConversionFactor){
        this.velocityConversionFactor=velocityConversionFactor;
    };

    public class SubstemNameBuilder {
        public IsEnabledBuilder withSubsystemName(String nameString) {
            subSystemName = nameString;
            return new IsEnabledBuilder();
        }
    }
    public class IsEnabledBuilder {
        public CurrentLimitBuilder withIsEnabled(boolean isEnabled) {
            isEnabledWriter = ShuffleboardValue
                    .create(isEnabled, deviceID + "/Is Enabled", subSystemName)
                    .withWidget(BuiltInWidgets.kToggleSwitch)
                    .build();
            outputWriter = ShuffleboardValue
                    .create(0.0, deviceID +"/Output", subSystemName)
                    .build();
            return new CurrentLimitBuilder();

        }
    }

    // public class VelocityConversionFactorBuilder {
    //     @SuppressWarnings("unchecked")
    //     public <T extends CANMotorEx> T withVelocityConversionFactor(double velocityConversionFactor) {
    //         setVelocityConversionFactor(positionConversionFactor);
    //         return (T) CANMotorEx.this;
    //     }
    // }


    public class CurrentLimitBuilder {
        @SuppressWarnings("unchecked")
        public <T extends TalonEx> T withCurrentLimit(double supply, double stator) {
            setSupplyCurrentLimit(supply);
            setStatorCurrentLimit(stator);
            return (T) TalonEx.this;
        }
    }

    public void setDirection(Direction direction) {
        motorOutputConfigs.Inverted = switch (direction) {
            case Forward -> InvertedValue.Clockwise_Positive;
            case Reversed -> InvertedValue.CounterClockwise_Positive;
        };
        this.getConfigurator().apply(motorOutputConfigs);

    }

    public void setIdleMode(ZeroPowerMode mode) {
        this.setNeutralMode(switch (mode) {
            case Brake -> NeutralModeValue.Brake;
            case Coast -> NeutralModeValue.Coast;
        });
    }

    public void setSupplyCurrentLimit(double currentLimit) {
        configuration.CurrentLimits.SupplyCurrentLimit = currentLimit;
        configuration.CurrentLimits.SupplyCurrentLimitEnable = true;
        this.getConfigurator().apply(configuration);
    }

    public void setStatorCurrentLimit(double statorCurrent){
        // CurrentLimitsConfigs configs = new CurrentLimitsConfigs();
        // configs.StatorCurrentLimit = 50;
        // this.getConfigurator().apply(configs);
        configuration.CurrentLimits.StatorCurrentLimit = statorCurrent;
        configuration.CurrentLimits.StatorCurrentLimitEnable = true;
        this.getConfigurator().apply(configuration);
    }

    public void setPower(double power) {
        if (isEnabledWriter.get()) {
            this.set(power);
        }
        if (DroidRageConstants.removeWriterWriter.get()) {
            outputWriter.set(power);
        }
    }

    public void setVoltage(double outputVolts) {
        if(isEnabledWriter.get()){
            this.setVoltage(outputVolts);
        }
        if(DroidRageConstants.removeWriterWriter.get()){//if(!DriverStation.isFMSAttached())
            outputWriter.set(outputVolts);
        }
    }

    public void setVoltage(Voltage voltage) {
        this.setVoltage(voltage.in(Volts)/ RobotController.getBatteryVoltage());
    }

    public void setPosition(double position) {
        motor.setPosition(position);
    }

    public double getVelocity() {
        return motor.getVelocity()*positionConversionFactor;
    }

    public double getSpeed() {-
        return this.get();
    }

    @Override
    public double getPosition() {
        return this.getPosition()*positionConversionFactor;
    }

    public int getDeviceID() {
        return this.getDeviceID();
    }

    public CANBus getCANBus() {
        return canbus;
    }

    public double getVoltage(){
        return this.getthisVoltage().getValueAsDouble();
    }

    public void resetEncoder(int num) {
        this.setPosition(num);
    }
}

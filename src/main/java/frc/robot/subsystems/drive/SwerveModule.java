package frc.robot.subsystems.drive;

import java.util.function.Supplier;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.configs.MagnetSensorConfigs;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.drive.SwerveDriveConstants.SwerveDriveConfig;
import frc.robot.utility.encoder.CANcoderEx;
import frc.robot.utility.encoder.EncoderEx.EncoderDirection;
import frc.robot.utility.encoder.EncoderEx.EncoderRange;
import frc.robot.utility.motor.SparkMaxEx;
import frc.robot.utility.motor.TalonEx;
import frc.robot.utility.motor.CANMotorEx.Direction;
import frc.robot.utility.motor.CANMotorEx.ZeroPowerMode;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

public class SwerveModule {
    public enum POD{
        FL,
        BL,
        FR,
        BR
    }

    public static class Constants {
        public static final double WHEEL_DIAMETER_METERS = Units.inchesToMeters(4);
        public static final double DRIVE_MOTOR_GEAR_RATIO = 1/6.75;//(50.0 / 16.0) * (16.0 / 28.0) * (45.0 / 15.0)=5.35714285714
        public static final double TURN_MOTOR_GEAR_RATIO = 1/ 21.42;

        public static final double DRIVE_ENCODER_ROT_2_METER = DRIVE_MOTOR_GEAR_RATIO * Math.PI * WHEEL_DIAMETER_METERS;
        public static final double DRIVE_ENCODER_RPM_2_METER_PER_SEC = DRIVE_ENCODER_ROT_2_METER / 60;
        public static final double READINGS_PER_REVOLUTION = 1;//4096

        //Used for the CANCoder
        public static final double TURN_ENCODER_ROT_2_RAD = 2 * Math.PI / READINGS_PER_REVOLUTION;
        public static final double TURN_ENCODER_ROT_2_RAD_SEC = TURN_ENCODER_ROT_2_RAD/60;

        //0.5 Change this to make the robot turn the turn motor as fast as possible
        //If strafing, the robot drifts to he front/back, then increase
                //.115

        public static final double PHYSICAL_MAX_SPEED_METERS_PER_SECOND = 4.47;
    }

    private final TalonEx driveMotor;
    
    // Turn motor and encoder
    private final SparkMaxEx turnMotor;
    private final CANcoderEx turnEncoder;

    private final PIDController turningPidController;
    private final SimpleMotorFeedforward feedforward;

    private ShuffleboardValue<Double> turnPositionWriter;
    private ShuffleboardValue<Double> drivePositionWriter;


    MagnetSensorConfigs magnetSensorConfigs = new MagnetSensorConfigs();
    // SwerveModuleState desiredState = new SwerveModuleState();

    public SwerveModule(int driveMotorId, CANBus canBus, 
        int turnMotorId, Direction driveMotorReversed, 
        Direction turningMotorReversed, int absoluteEncoderId, 
        Supplier<Double> absoluteEncoderOffsetRad, 
        EncoderDirection absoluteEncoderReversed, boolean isEnabled, POD podName) {
        
        // turnEncoder = new CANcoder(absoluteEncoderId);
        // CANcoderConfiguration config = new CANcoderConfiguration();
        // if(absoluteEncoderReversed){
        //     config.MagnetSensor.SensorDirection = SensorDirectionValue.CounterClockwise_Positive;
        // } else{
        //     config.MagnetSensor.SensorDirection = SensorDirectionValue.Clockwise_Positive;
        // }
        // config.MagnetSensor.MagnetOffset = absoluteEncoderOffsetRad.get()/Constants.TURN_ENCODER_ROT_2_RAD;
        // // config.MagnetSensor.AbsoluteSensorRange = AbsoluteSensorRangeValue.Unsigned_0To1;

        // //magnetSensorConfigs.AbsoluteSensorDiscontinuityPoint; Explains what 1 will do to the sensor
        // config.MagnetSensor.AbsoluteSensorDiscontinuityPoint = 1;
        // turnEncoder.getConfigurator().apply(config);

        turnEncoder = CANcoderEx.create(absoluteEncoderId, canBus)
            .withDirection(absoluteEncoderReversed)
            .withSubsystemBase("drive")
            .withRange(EncoderRange.ZERO_TO_ONE)
            .withOffset(absoluteEncoderOffsetRad.get()/Constants.TURN_ENCODER_ROT_2_RAD);

        driveMotor = TalonEx.create(driveMotorId, canBus)
            .withDirection(driveMotorReversed)
            .withIdleMode(ZeroPowerMode.Brake)
            .withPositionConversionFactor(Constants.DRIVE_ENCODER_ROT_2_METER)
            .withSubsystemName("Swerve")
            .withIsEnabled(isEnabled)
            .withCurrentLimit(50, 90);

        turnMotor = SparkMaxEx.create(turnMotorId)
            .withDirection(turningMotorReversed)
            .withIdleMode(ZeroPowerMode.Coast)
            .withPositionConversionFactor(Constants.TURN_ENCODER_ROT_2_RAD)
            .withSubsystemName("Swerve")
            .withIsEnabled(isEnabled)
            .withCurrentLimit(80);
        
        turningPidController = new PIDController(SwerveDriveConfig.TURN_KP.getValue(), 0.0, 0.0);
        turningPidController.enableContinuousInput(0, 2*Math.PI);//Was  -Math.PI, Math.PI but changed to 0 and 2PI

        feedforward = new SimpleMotorFeedforward(SwerveDriveConfig.DRIVE_KS.getValue(), SwerveDriveConfig.DRIVE_KV.getValue());

        turnPositionWriter = ShuffleboardValue.create(0.0, 
        "Module/Module " + podName.toString() + "/Turn Position (Radians)", 
            SwerveDrive.class.getSimpleName()).build();
        drivePositionWriter = ShuffleboardValue.create(0.0, 
            "Module/Module " + podName.toString() + "/Drive Position (Radians)", 
            SwerveDrive.class.getSimpleName()).build();
        resetDriveEncoder();

    }

    public double getDrivePos() {
            drivePositionWriter.write(driveMotor.getPosition());
            return driveMotor.getPosition();
        }
    
        public double getTurningPosition() {
            turnPositionWriter.write(turnEncoder.getAbsolutePosition()*Constants.TURN_ENCODER_ROT_2_RAD);
            return (turnEncoder.getAbsolutePosition()*Constants.TURN_ENCODER_ROT_2_RAD);
        }
    
        public double getDriveVelocity(){
            // return driveMotor.getEncoder().getVelocity();
            return driveMotor.getVelocity();
        }

        public void resetDriveEncoder(){
            // driveMotor.getEncoder().setPosition(0);
            driveMotor.setPosition(0);
        }
    
        public SwerveModulePosition getPosition() {
            return new SwerveModulePosition(getDrivePos(), new Rotation2d(getTurningPosition()));
        }
    
        public SwerveModuleState getState(){
            return new SwerveModuleState(getDriveVelocity(), new Rotation2d(getTurningPosition()));
        }
    
        public void setState(SwerveModuleState state) {
            SwerveModuleState desiredState = state;
            if (Math.abs(state.speedMetersPerSecond) < 0.001) {
                stop();
                return;
            }
            desiredState.optimize(getState().angle);
            desiredState.optimize(getState().angle);
            driveMotor.setPower(state.speedMetersPerSecond / Constants.PHYSICAL_MAX_SPEED_METERS_PER_SECOND);
            turnMotor.setPower((turningPidController.calculate(getTurningPosition(), desiredState.angle.getRadians()))*1);
            SmartDashboard.putString("Swerve[" + turnEncoder.getDeviceID() + "] state", desiredState.toString());
            SmartDashboard.putString("Swerve[" + turnMotor.getDeviceID() + "] state", desiredState.toString());
        }
    
        public void setFeedforwardState(SwerveModuleState state) {
            SwerveModuleState desiredState = state;
            if (Math.abs(state.speedMetersPerSecond) < 0.001) {
                stop();
                return;
            }
            desiredState.optimize(getState().angle);
            desiredState.optimize(getState().angle);
            driveMotor.setVoltage(feedforward.calculate(state.speedMetersPerSecond));
            turnMotor.setPower(turningPidController.calculate(getTurningPosition(), desiredState.angle.getRadians()));
            SmartDashboard.putString("Swerve[" + turnEncoder.getDeviceID() + "] state", desiredState.toString());
            SmartDashboard.putString("Swerve[" + turnMotor.getDeviceID() + "] state", desiredState.toString());
        }
    
        public void stop(){
            driveMotor.setPower(0);
            turnMotor.setPower(0);
        }
    
        public void coastMode() {
            driveMotor.setIdleMode(ZeroPowerMode.Coast);
            turnMotor.setIdleMode(ZeroPowerMode.Coast);
        }
    
        public void brakeMode() {
            driveMotor.setIdleMode(ZeroPowerMode.Brake);
            turnMotor.setIdleMode(ZeroPowerMode.Brake);
        }
    
        public void brakeAndCoastMode() {
            driveMotor.setIdleMode(ZeroPowerMode.Brake);
            turnMotor.setIdleMode(ZeroPowerMode.Coast);
        }

        public SparkMaxEx getTurnMotor(){
            return turnMotor;
        }
    
        public void getTurnVoltage(){
            turnMotor.getVoltage();
        }

    


}

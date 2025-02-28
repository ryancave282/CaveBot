package frc.robot.subsystems.drive;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.swerve.SwerveModuleConstants;
import com.ctre.phoenix6.swerve.SwerveModuleConstants.DriveMotorArrangement;
import com.ctre.phoenix6.swerve.SwerveModuleConstants.SteerMotorArrangement;
import com.ctre.phoenix6.swerve.SwerveModuleConstantsFactory;
import edu.wpi.first.math.util.Units;
import frc.utility.encoder.EncoderEx.EncoderDirection;
import frc.utility.motor.CANMotorEx.Direction;
import lombok.Setter;

public class Module {
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

        public static final double DRIVE_SUPPLY_CURRENT_LIMIT = 50;
        public static final double DRIVE_STATOR_CURRENT_LIMIT = 90;   
        public static final double TURN_SUPPLY_CURRENT_LIMIT = 80;
    }

    private static final TalonFXConfiguration driveMotorConfig = new TalonFXConfiguration();
    private static final TalonFXConfiguration turnMotorConfig = new TalonFXConfiguration();
    private static final CANcoderConfiguration encoderConfig = new CANcoderConfiguration();

    private static final Slot0Configs driveMotorGains = new Slot0Configs();
    private static final Slot0Configs turnMotorGains = new Slot0Configs();

    // private ShuffleboardValue<Double> turnPositionWriter;
    // private ShuffleboardValue<Double> drivePositionWriter;

    private static int driveMotorId;
    private static int turnMotorId;
    private static int encoderId;
    private static double encoderOffset;
    private static boolean driveMotorInverted;
    private static boolean steerMotorInverted;
    private static boolean encoderInverted;
    @Setter private static double xPos;
    @Setter private static double yPos;

    public Module(){
        /*
         * DRIVE MOTOR CONSTANTS
         */
        driveMotorConfig.CurrentLimits.SupplyCurrentLimit = Constants.DRIVE_SUPPLY_CURRENT_LIMIT;
        driveMotorConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
        driveMotorConfig.CurrentLimits.StatorCurrentLimit = Constants.DRIVE_STATOR_CURRENT_LIMIT;
        driveMotorConfig.CurrentLimits.StatorCurrentLimitEnable = true;
        driveMotorGains.kS = SwerveDriveConstants.SwerveDriveConfig.DRIVE_KS.getValue();
        driveMotorGains.kV = SwerveDriveConstants.SwerveDriveConfig.DRIVE_KV.getValue();

        /*
         * TURN MOTOR CONSTANTS
         */
        turnMotorConfig.CurrentLimits.SupplyCurrentLimit = Constants.TURN_SUPPLY_CURRENT_LIMIT;
        driveMotorConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
        turnMotorGains.kP = SwerveDriveConstants.SwerveDriveConfig.TURN_KP.getValue();
    
    }

    public static Module.PosBuilder create() {
        Module module = new Module();
        return module.new PosBuilder();
    }

    public class PosBuilder {
        public DriveIDBuilder withPositions(double xPos, double yPos) {
            setXPos(xPos);
            setYPos(yPos);
            return new DriveIDBuilder();
        }
    }
    
    public class DriveIDBuilder {
        public TurnIDBuilder withDriveMotor(int driveMotorID, Direction direction){ 
            Module.driveMotorId = driveMotorID;
            switch(direction) {
                case Forward -> Module.driveMotorInverted = false;
                case Reversed -> Module.driveMotorInverted = true;
            }
            return new TurnIDBuilder();

        }
    }
    public class TurnIDBuilder {
        public EncoderBuilder withTurnMotor(int steerMotorID, Direction direction){
            Module.turnMotorId = steerMotorID;
            switch(direction) {
                case Forward -> Module.steerMotorInverted = false;
                case Reversed -> Module.steerMotorInverted = true;
            }
            return new EncoderBuilder();
        }
    }
    public class EncoderBuilder{
        @SuppressWarnings("unchecked")
        public <T extends Module> T withEncoder(int encoderID, double encoderOffset, EncoderDirection direction){
            Module.encoderId = encoderID;
            Module.encoderOffset = encoderOffset;
            switch(direction) {
                case Forward -> Module.encoderInverted = false;
                case Reversed -> Module.encoderInverted = true;
            }
            return (T) Module.this;
        }
    }

    

    private static final SwerveModuleConstantsFactory<TalonFXConfiguration, TalonFXConfiguration, CANcoderConfiguration> constantCreator =
        new SwerveModuleConstantsFactory<TalonFXConfiguration, TalonFXConfiguration, CANcoderConfiguration>()
            .withDriveMotorGearRatio(Constants.DRIVE_MOTOR_GEAR_RATIO)
            .withSteerMotorGearRatio(Constants.TURN_MOTOR_GEAR_RATIO)
            .withWheelRadius(Constants.WHEEL_DIAMETER_METERS/2)
            .withSteerMotorGains(turnMotorGains)
            .withDriveMotorGains(driveMotorGains)
            .withDriveMotorType(DriveMotorArrangement.TalonFX_Integrated)
            .withSteerMotorType(SteerMotorArrangement.TalonFX_Integrated)
            .withDriveMotorInitialConfigs(driveMotorConfig)
            .withSteerMotorInitialConfigs(turnMotorConfig)
            .withEncoderInitialConfigs(encoderConfig);

    public SwerveModuleConstants<TalonFXConfiguration, TalonFXConfiguration, CANcoderConfiguration> getModule() {
        return constantCreator.createModuleConstants(
            turnMotorId, 
            driveMotorId, 
            encoderId, 
            encoderOffset, 
            yPos, 
            xPos, 
            driveMotorInverted, 
            steerMotorInverted, 
            encoderInverted
        );
    }

    // public double getDrivePos() {
    //     drivePositionWriter.write(driveMotor.getPosition());
    //     return driveMotor.getPosition();
    // }
    
    // public double getTurningPosition() {
    //     turnPositionWriter.write(turnEncoder.getAbsolutePosition()*Constants.TURN_ENCODER_ROT_2_RAD);
    //     return (turnEncoder.getAbsolutePosition()*Constants.TURN_ENCODER_ROT_2_RAD);
    // }

    // public double getDriveVelocity(){
    //     // return driveMotor.getEncoder().getVelocity();
    //     return driveMotor.getVelocity();
    // }

    // public void resetDriveEncoder(){
    //     // driveMotor.getEncoder().setPosition(0);
    //     driveMotor.setPosition(0);
    // }

    // public SwerveModulePosition getPosition() {
    //     return new SwerveModulePosition(getDrivePos(), new Rotation2d(getTurningPosition()));
    // }

    // public SwerveModuleState getState(){
    //     return new SwerveModuleState(getDriveVelocity(), new Rotation2d(getTurningPosition()));
    // }

    // public void setState(SwerveModuleState state) {
    //     SwerveModuleState desiredState = state;
    //     if (Math.abs(state.speedMetersPerSecond) < 0.001) {
    //         stop();
    //         return;
    //     }
    //     desiredState.optimize(getState().angle);
    //     desiredState.optimize(getState().angle);
    //     driveMotor.setPower(state.speedMetersPerSecond / Constants.PHYSICAL_MAX_SPEED_METERS_PER_SECOND);
    //     turnMotor.setPower((turningPidController.calculate(getTurningPosition(), desiredState.angle.getRadians()))*1);
    //     SmartDashboard.putString("Swerve[" + turnEncoder.getDeviceID() + "] state", desiredState.toString());
    //     SmartDashboard.putString("Swerve[" + turnMotor.getDeviceID() + "] state", desiredState.toString());
    // }

    // public void setFeedforwardState(SwerveModuleState state) {
    //     SwerveModuleState desiredState = state;
    //     if (Math.abs(state.speedMetersPerSecond) < 0.001) {
    //         stop();
    //         return;
    //     }
    //     desiredState.optimize(getState().angle);
    //     desiredState.optimize(getState().angle);
    //     driveMotor.setVoltage(feedforward.calculate(state.speedMetersPerSecond));
    //     turnMotor.setPower(turningPidController.calculate(getTurningPosition(), desiredState.angle.getRadians()));

    //     SmartDashboard.putString("Swerve[" + turnEncoder.getDeviceID() + "] state", desiredState.toString());
    //     SmartDashboard.putString("Swerve[" + turnMotor.getDeviceID() + "] state", desiredState.toString());
    // }

    // public void stop(){
    //     driveMotor.setPower(0);
    //     turnMotor.setPower(0);
    // }

    // public void coastMode() {
    //     driveMotor.setIdleMode(ZeroPowerMode.Coast);
    //     turnMotor.setIdleMode(ZeroPowerMode.Coast);
    // }

    // public void brakeMode() {
    //     driveMotor.setIdleMode(ZeroPowerMode.Brake);
    //     turnMotor.setIdleMode(ZeroPowerMode.Brake);
    // }

    // public void brakeAndCoastMode() {
    //     driveMotor.setIdleMode(ZeroPowerMode.Brake);
    //     turnMotor.setIdleMode(ZeroPowerMode.Coast);
    // }

    // public void getTurnVoltage(){
    //     turnMotor.getVoltage();
    // }

    // public void setTurnMotorIsEnabled(boolean isEnabled){
    //     turnMotor.setIsEnabled(isEnabled);
    // }
    
    // public void setDriveMotorIsEnabled(boolean isEnabled) {
    //     driveMotor.setIsEnabled(isEnabled);
    // }
}

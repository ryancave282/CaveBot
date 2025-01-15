package frc.robot.subsystems.drive;

import com.ctre.phoenix6.configs.MountPoseConfigs;
import com.ctre.phoenix6.hardware.Pigeon2;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.drive.SwerveDriveConstants.SwerveDriveConfig;
import frc.robot.subsystems.drive.SwerveDriveConstants.Speed;
import frc.robot.subsystems.drive.SwerveModule.POD;
import net.droidrage.lib.DroidRageConstants;
import net.droidrage.lib.encoder.EncoderEx.EncoderDirection;
import net.droidrage.lib.motor.CANMotorEx.Direction;
import net.droidrage.lib.motor.SparkMaxEx;
import net.droidrage.lib.shuffleboard.ShuffleboardValue;
import frc.robot.subsystems.drive.SwerveDriveConstants.DriveOptions;

//Set Voltage instead of set Power
//Set them to 90 to 100%
public class SwerveDrive extends SubsystemBase {
    public enum TippingState {
        NO_TIP_CORRECTION,
        ANTI_TIP,
        ;
    }

    public static final SwerveDriveKinematics DRIVE_KINEMATICS = new SwerveDriveKinematics(
        new Translation2d(SwerveDriveConfig.WHEEL_BASE.get() / 2, SwerveDriveConfig.TRACK_WIDTH.get() / 2),  // Front Left --
        new Translation2d(SwerveDriveConfig.WHEEL_BASE.get() / 2, -SwerveDriveConfig.TRACK_WIDTH.get() / 2),   // Front Right +-
        new Translation2d(-SwerveDriveConfig.WHEEL_BASE.get() / 2, SwerveDriveConfig.TRACK_WIDTH.get() / 2), // Back Left -+
        new Translation2d(-SwerveDriveConfig.WHEEL_BASE.get() / 2, -SwerveDriveConfig.TRACK_WIDTH.get() / 2)   // Back Right ++
    );

    
    private final SwerveModule frontRight = SwerveModule.create()
        .withSubsystemName(this, POD.FR)
        .withDriveMotor(3,Direction.Forward, true)
        .withTurnMotor(2, Direction.Reversed, true)
        .withEncoder(10, SwerveDriveConfig.FRONT_RIGHT_ABSOLUTE_ENCODER_OFFSET_RADIANS::get, EncoderDirection.Reversed);
        
    private final SwerveModule backRight = SwerveModule.create()
        .withSubsystemName(this, POD.BR)
        .withDriveMotor(5, Direction.Forward, true)
        .withTurnMotor(4, Direction.Reversed, true)
        .withEncoder(11, SwerveDriveConfig.BACK_RIGHT_ABSOLUTE_ENCODER_OFFSET_RADIANS::get,
                EncoderDirection.Reversed);

    private final SwerveModule backLeft = SwerveModule.create()
        .withSubsystemName(this, POD.BL)
        .withDriveMotor(7, Direction.Forward, true)
        .withTurnMotor(6, Direction.Reversed, true)
        .withEncoder(12, SwerveDriveConfig.BACK_LEFT_ABSOLUTE_ENCODER_OFFSET_RADIANS::get, EncoderDirection.Reversed);
    
    private final SwerveModule frontLeft = SwerveModule.create()
        .withSubsystemName(this, POD.FL)
        .withDriveMotor(9, Direction.Forward, true)
        .withTurnMotor(8, Direction.Reversed, true)
        .withEncoder(13, SwerveDriveConfig.FRONT_LEFT_ABSOLUTE_ENCODER_OFFSET_RADIANS::get, EncoderDirection.Reversed);
    
    private final SwerveModule[] swerveModules = { frontLeft, frontRight, backLeft, backRight };
    

    private final Pigeon2 pigeon2 = new Pigeon2(14);
    private final MountPoseConfigs poseConfigs  = new MountPoseConfigs();

    private final SwerveDriveOdometry odometry = new SwerveDriveOdometry (
        DRIVE_KINEMATICS, 
        new Rotation2d(0), 
        getModulePositions()
    );

    private volatile Speed speed = Speed.NORMAL;
    private volatile TippingState tippingState = TippingState.ANTI_TIP;
    ///////

    
    // Shuffleboard values
    private final ShuffleboardValue<String> tippingStateWriter = 
        ShuffleboardValue.create(tippingState.name(), "Current/State/Tipping State", this.getSubsystem()).build();
    private final ShuffleboardValue<String> speedStateWriter = 
        ShuffleboardValue.create(speed.name(), "Current/State/Speed", this.getSubsystem()).build();
    
    private final ShuffleboardValue<Double> headingWriter = 
        ShuffleboardValue.create(0.0, "Current/Gyro/Heading-Yaw (Degrees)", this.getSubsystem()).build();
    private final ShuffleboardValue<Double> rollWriter = 
        ShuffleboardValue.create(0.0, "Current/Gyro/Roll (Degrees)", this.getSubsystem()).build();
    private final ShuffleboardValue<Double> pitchWriter =   
        ShuffleboardValue.create(0.0, "Current/Gyro/Pitch (Degrees)", this.getSubsystem()).build();
    private final ShuffleboardValue<String> locationWriter = 
        ShuffleboardValue.create("", "Current/Robot Location", this.getSubsystem()).build();
    private final ShuffleboardValue<Boolean> isEnabledWriter = 
        ShuffleboardValue.create(true, "Is Drive Enabled", this.getSubsystem())
        .withWidget(BuiltInWidgets.kToggleSwitch)
        .build();
    protected final ShuffleboardValue<String> drivePoseWriter = ShuffleboardValue.create
        ("none", "SwerveDrive/Pose", this.getSubsystem()).build();
    private final ShuffleboardValue<Double> forwardVelocityWriter = 
        ShuffleboardValue.create(0.0, "Forward Velocity Writer", SwerveDrive.class.getSimpleName()).build();

    public SwerveDrive(Boolean isEnabled) {
        // field2d.se();
        for (SwerveModule swerveModule: swerveModules) {
            swerveModule.brakeMode();
            // swerveModule.coastMode();
            // swerveModule.brakeAndCoast^Mode();
        }

        poseConfigs.MountPosePitch = 0;//Up-Down//0
        poseConfigs.MountPoseRoll = 0;//Side-Side//90
        poseConfigs.MountPoseYaw = 180;//Heading//180;
        pigeon2.getConfigurator().apply(poseConfigs);   
        isEnabledWriter.set(isEnabled);     
    }

    
    @Override
    public void periodic() {
        odometry.update(
            getRotation2d(),
            getModulePositions()
        );

        drivePoseWriter.set(getPose().toString());
        headingWriter.set(getHeading());
        rollWriter.set(getRoll());
        pitchWriter.set(getPitch());
        locationWriter.set(getPose().getTranslation().toString());
        forwardVelocityWriter.write(getForwardVelocity());
    }

    @Override
    public void simulationPeriodic() {
        periodic();
    }

    public SwerveModulePosition[] getModulePositions() {
        return new SwerveModulePosition[] {
            frontLeft.getPosition(),
            frontRight.getPosition(),
            backLeft.getPosition(),
            backRight.getPosition()
        };
    }

    public TippingState getTippingState() {
        return tippingState;
    }

    public double getHeading() {//Yaw
        return Math.IEEEremainder(pigeon2.getYaw().getValueAsDouble(), 360);
    }

    public double getPitch() {
        return Math.IEEEremainder(pigeon2.getPitch().getValueAsDouble(), 360);
    }

    public double getRoll() {
        return Math.IEEEremainder(pigeon2.getRoll().getValueAsDouble(), 360);
    }

    public Rotation2d getRotation2d() {
        return Rotation2d.fromDegrees(getHeading());

    }

    public Pose2d getPose() {
        return odometry.getPoseMeters();
    }

    public boolean isFieldOriented() {
        return DriveOptions.IS_FIELD_ORIENTED.get();
    }

    public boolean isSquaredInputs() {
        return DriveOptions.IS_SQUARED_INPUTS.get();
    }

    public double getTranslationalSpeed() {
        return speed.getTranslationalSpeed();
    }

    public double getAngularSpeed() {
        return speed.getAngularSpeed();
    }

    public double getForwardVelocity() {
        return (frontLeft.getDriveVelocity() + frontRight.getDriveVelocity()) / 2;
    }

    // public double getHeadingOffset() {
    //     return SwerveConfig.HEADING_OFFSET.value.get();
    // }

    public void resetOdometry(Pose2d pose) {
        odometry.resetPosition(getRotation2d(), getModulePositions(), pose);
    }

    public void drive(double xSpeed, double ySpeed, double turnSpeed) {
        ChassisSpeeds chassisSpeeds = new ChassisSpeeds(xSpeed, ySpeed, turnSpeed);
        drive(chassisSpeeds);
    }
    public void drive(ChassisSpeeds chassisSpeeds) {
        SwerveModuleState[] states = SwerveDrive.DRIVE_KINEMATICS.toSwerveModuleStates(chassisSpeeds);
        setModuleStates(states);
    }

    public void setModuleStates(SwerveModuleState[] states) {
        if (!isEnabledWriter.get()) return;
        SwerveDriveKinematics.desaturateWheelSpeeds(
            states, 
            SwerveModule.Constants.PHYSICAL_MAX_SPEED_METERS_PER_SECOND
        );

        // swerveModules[1].setState(states[1]);
        for (int i = 0; i < 4; i++) {
            swerveModules[i].setState(states[i]);
        }
    }

    public void setFeedforwardModuleStates(ChassisSpeeds chassisSpeeds) {
        SwerveModuleState[] states = SwerveDrive.DRIVE_KINEMATICS.toSwerveModuleStates(chassisSpeeds);
        setFeedforwardModuleStates(states);
    }
    public void setFeedforwardModuleStates(SwerveModuleState[] states) {
        if (!isEnabledWriter.get()) return;
        SwerveDriveKinematics.desaturateWheelSpeeds(
            states, 
            SwerveModule.Constants.PHYSICAL_MAX_SPEED_METERS_PER_SECOND
        );

        for (int i = 0; i < 4; i++) {
            swerveModules[i].setFeedforwardState(states[i]);
        }
    }

    public void stop() {
        for (SwerveModule swerveModule: swerveModules) {
            swerveModule.stop();
        }
    }

    public void setTippingState(TippingState tippingState) {
        this.tippingState = tippingState;
        tippingStateWriter.set(tippingState.name());
    }

    public Command setSpeed(Speed speed) {
        return runOnce(() -> {
            this.speed = speed;
            speedStateWriter.set(speed.name());
        });
    }

    public Command resetEncoders() {
        return runOnce(() -> {
            for (SwerveModule swerveModule: swerveModules) {
                swerveModule.resetDriveEncoder();
                // pigeon2.setYaw(getAngularSpeed())
            }
        });
    }

    public Command setYawCommand(double degrees) {
        return runOnce(
            () -> pigeon2.setYaw(degrees, 5)
        );
    }

    public Command toggleFieldOriented() {
        return runOnce(() -> DriveOptions.IS_FIELD_ORIENTED.set(
                !isFieldOriented()
            )
        );
    }

    public Command toggleSquareInputs() {
        return runOnce(() -> DriveOptions.IS_SQUARED_INPUTS.set(
                !isSquaredInputs()
            )
        );
    }

    public Command runStop() {
        return runOnce(this::stop);
    }

    public TrapezoidProfile.Constraints getThetaConstraints() {
        return new TrapezoidProfile.Constraints(
            SwerveDriveConfig.MAX_ANGULAR_SPEED_RADIANS_PER_SECOND.get(),
            SwerveDriveConfig.MAX_ANGULAR_ACCELERATION_RADIANS_PER_SECOND_SQUARED.get());
    }

    public Command driveAutoReset(){
        return runOnce(()->setYawCommand(getRotation2d().rotateBy(Rotation2d.fromDegrees(0)).getDegrees()));
    }      

    public ChassisSpeeds getSpeeds() {//Is this Roboto Relative
        return DRIVE_KINEMATICS.toChassisSpeeds(getModuleStates());
    }
    public SwerveModuleState[] getModuleStates() {
        SwerveModuleState[] states = new SwerveModuleState[swerveModules.length];
        for (int i = 0; i < swerveModules.length; i++) {
            states[i] = swerveModules[i].getState();
        }
        return states;
    }

    public SparkMaxEx getTurnSparkMax(){
        return frontLeft.getTurnMotor();
    }

    public void changeAllianceRotation(){
        //Need to Turn on Bot(front) facing towards the red alliance on robot start   
        pigeon2.setYaw(
            pigeon2.getYaw().getValueAsDouble()+
            switch (DriverStation.getRawAllianceStation()) {
                case Unknown -> 0;//180
                case Blue1,Blue2,Blue3 -> 0;
                case Red1,Red2,Red3 -> 180;
            }
        );
    }

    
}

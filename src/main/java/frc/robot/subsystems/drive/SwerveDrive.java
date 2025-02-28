package frc.robot.subsystems.drive;

import com.ctre.phoenix6.configs.Pigeon2Configuration;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.swerve.SwerveDrivetrain;
import com.ctre.phoenix6.swerve.SwerveDrivetrainConstants;
import com.ctre.phoenix6.swerve.SwerveModuleConstants;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.subsystems.drive.SwerveDriveConstants.SwerveDriveConfig;
import frc.utility.encoder.EncoderEx.EncoderDirection;
import frc.utility.motor.CANMotorEx.Direction;

public class SwerveDrive extends SwerveDrivetrain<TalonFX,TalonFX,CANcoder> implements Subsystem {
    private static final Module frontRight = Module.create()
        .withPositions(-SwerveDriveConfig.WHEEL_BASE.getValue() / 2, -SwerveDriveConfig.TRACK_WIDTH.getValue() / 2)
        .withDriveMotor(3,Direction.Forward)
        .withTurnMotor(1, Direction.Reversed)
        .withEncoder(2, SwerveDriveConfig.FRONT_RIGHT_ABSOLUTE_ENCODER_OFFSET_RADIANS.getValue(), 
        EncoderDirection.Reversed);
        
    private static final Module backRight = Module.create()
        .withPositions(SwerveDriveConfig.WHEEL_BASE.getValue() / 2, -SwerveDriveConfig.TRACK_WIDTH.getValue() / 2)
        .withDriveMotor(6, Direction.Forward)
        .withTurnMotor(4, Direction.Reversed)
        .withEncoder(5, SwerveDriveConfig.BACK_RIGHT_ABSOLUTE_ENCODER_OFFSET_RADIANS.getValue(),
        EncoderDirection.Reversed);

    private static final Module backLeft = Module.create()
        .withPositions(SwerveDriveConfig.WHEEL_BASE.getValue() / 2, SwerveDriveConfig.TRACK_WIDTH.getValue() / 2)
        .withDriveMotor(9, Direction.Forward)
        .withTurnMotor(7, Direction.Reversed)
        .withEncoder(8, SwerveDriveConfig.BACK_LEFT_ABSOLUTE_ENCODER_OFFSET_RADIANS.getValue(), 
        EncoderDirection.Reversed);
    
    private static final Module frontLeft = Module.create()
        .withPositions(-SwerveDriveConfig.WHEEL_BASE.getValue() / 2, SwerveDriveConfig.TRACK_WIDTH.getValue() / 2)
        .withDriveMotor(12, Direction.Forward)
        .withTurnMotor(10, Direction.Reversed)
        .withEncoder(11, SwerveDriveConfig.FRONT_LEFT_ABSOLUTE_ENCODER_OFFSET_RADIANS.getValue(), 
        EncoderDirection.Reversed);

    private static final SwerveDrivetrainConstants drivetrainConstants =
        new SwerveDrivetrainConstants()
                .withCANBusName("drive")
                .withPigeon2Id(13)
                .withPigeon2Configs(new Pigeon2Configuration());

    private static SwerveModuleConstants<?,?,?>[] getSwerveModules() {
        return new SwerveModuleConstants[] {
            frontLeft.getModule(), 
            frontRight.getModule(), 
            backLeft.getModule(), 
            backRight.getModule()};
    }

    public SwerveDrive() {
        super(TalonFX::new, TalonFX::new, CANcoder::new, drivetrainConstants, getSwerveModules());
    }
}

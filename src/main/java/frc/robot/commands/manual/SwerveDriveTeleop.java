package frc.robot.commands.manual;

import java.util.function.Supplier;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.DroidRageConstants;
import frc.robot.subsystems.drive.SwerveDrive;
import frc.robot.subsystems.drive.SwerveDriveConstants;
import frc.robot.subsystems.drive.SwerveDriveConstants.Speed;
import frc.robot.subsystems.drive.SwerveModule;

public class SwerveDriveTeleop extends Command {
    private final SwerveDrive drive;
    private final Supplier<Double> x, y, turn;
    private volatile double xSpeed, ySpeed, turnSpeed;
    private Rotation2d heading;

    public SwerveDriveTeleop(SwerveDrive drive, CommandXboxController driver) {
        this.drive = drive;
        this.x = driver::getLeftX;
        this.y = driver::getLeftY;
        this.turn = driver::getRightX;

        driver.rightBumper().whileTrue(drive.setSpeed(Speed.SLOW))
            .whileFalse(drive.setSpeed(Speed.NORMAL));

        driver.b().onTrue(drive.setYawCommand(0));
        
        addRequirements(drive);
    }

    @Override
    public void initialize() {
        //Todo Test
        switch (DriverStation.getAlliance().get()) {
            case Red:
                drive.setYawCommand(drive.getHeading() + 180);
                break;
            case Blue:
                drive.setYawCommand(drive.getHeading());
                break;
        }
    }

    @Override
    public void execute() {
        xSpeed = -y.get();
        ySpeed = -x.get();
        turnSpeed = -turn.get();


        // Square inputs
        if (drive.isSquaredInputs()) {
            xSpeed = DroidRageConstants.squareInput(xSpeed);
            ySpeed = DroidRageConstants.squareInput(ySpeed);
            turnSpeed = DroidRageConstants.squareInput(turnSpeed);
        }

        // Apply Field Oriented
        if (drive.isFieldOriented()) {
            double modifiedXSpeed = xSpeed;
            double modifiedYSpeed = ySpeed;

            
            heading = drive.getRotation2d();

            modifiedXSpeed = xSpeed * heading.getCos() + ySpeed * heading.getSin();
            modifiedYSpeed = -xSpeed * heading.getSin() + ySpeed * heading.getCos();

            xSpeed = modifiedXSpeed;
            ySpeed = modifiedYSpeed;
        }

        // Apply deadband
        if (Math.abs(xSpeed) < DroidRageConstants.Gamepad.DRIVER_STICK_DEADZONE) xSpeed = 0;
        if (Math.abs(ySpeed) < DroidRageConstants.Gamepad.DRIVER_STICK_DEADZONE) ySpeed = 0;
        if (Math.abs(turnSpeed) < DroidRageConstants.Gamepad.DRIVER_STICK_DEADZONE) turnSpeed = 0;

        // Smooth driving and apply speed
        xSpeed = 
            xSpeed *
            SwerveModule.Constants.PHYSICAL_MAX_SPEED_METERS_PER_SECOND * 
            drive.getTranslationalSpeed();
        ySpeed = 
            ySpeed *
            SwerveModule.Constants.PHYSICAL_MAX_SPEED_METERS_PER_SECOND *
            drive.getTranslationalSpeed();
        turnSpeed = 
            turnSpeed *
            SwerveDriveConstants.SwerveDriveConfig.PHYSICAL_MAX_ANGULAR_SPEED_RADIANS_PER_SECOND.get() * 
            drive.getAngularSpeed();

        ChassisSpeeds chassisSpeeds = new ChassisSpeeds(xSpeed, ySpeed, turnSpeed);

        SwerveModuleState[] states = SwerveDrive.DRIVE_KINEMATICS.toSwerveModuleStates(chassisSpeeds);
        drive.setModuleStates(states);
    }

    @Override
    public void end(boolean interrupted) {
        drive.stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}

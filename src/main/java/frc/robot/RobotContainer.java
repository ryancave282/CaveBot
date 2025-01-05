package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.commands.manual.SwerveDriveTeleop;
import frc.robot.subsystems.coral.CoralSubsystem;
import frc.robot.subsystems.coral.CoralSubsystem.CoralValue;
import frc.robot.subsystems.drive.SwerveDrive;
import frc.robot.subsystems.vision.Vision;
import frc.robot.utility.InfoTracker.CycleTracker;

public class RobotContainer {
	private final CommandXboxController driver =
		new CommandXboxController(DroidRageConstants.Gamepad.DRIVER_CONTROLLER_PORT);
	
	private final CommandXboxController operator =
		new CommandXboxController(DroidRageConstants.Gamepad.OPERATOR_CONTROLLER_PORT);

	public RobotContainer() {
		DriverStation.silenceJoystickConnectionWarning(true);
	}

	//Add Reset encoder buttons
	//Add Manual Control
	public void configureTeleOpBindings(SwerveDrive drive, CoralSubsystem coral, CycleTracker cycleTracker, Vision vision) {
		drive.setDefaultCommand(
			new SwerveDriveTeleop( //Slow Mode and Gyro Reset in the Default Command
				drive,
				driver::getLeftX,
				driver::getLeftY,
				driver::getRightX,
				driver,
				false//No Work; Do no use this
				)
			);
		
		// Coral keybinds
		operator.a()
			.whileTrue(coral.setPositionCommand(CoralValue.L1));

		operator.b()
			.whileTrue(coral.setPositionCommand(CoralValue.L2));

		operator.x()
			.whileTrue(coral.setPositionCommand(CoralValue.L3));

		operator.y()
			.whileTrue(coral.setPositionCommand(CoralValue.L4));

		operator.leftBumper()
			.whileTrue(coral.setPositionCommand(CoralValue.INTAKE_HPS));

		operator.rightTrigger()
			.whileTrue(coral.setPositionCommand(CoralValue.INTAKE_GRND));
	}

	public void teleopPeriodic(){}

	public void testCommands() {}

	public void testDrive(SwerveDrive drive, Vision vision
	){
		drive.setDefaultCommand(
			new SwerveDriveTeleop( //Slow Mode and Gyro Reset in the Default Command
				drive,
				driver::getLeftX,
				driver::getLeftY,
				driver::getRightX,
				driver,
				false//No Work; Do no use this
				)
			);
		// drive.setDefaultCommand(new ManualSixWheel(drive, driver));
		driver.a().onTrue(new InstantCommand(()->drive.resetOdometry(vision.getPose())));
	}
}

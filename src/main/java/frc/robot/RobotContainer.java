package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.commands.IntakeElementInCommand;
import frc.robot.commands.manual.ManualElevator;
import frc.robot.commands.manual.SwerveDriveTeleop;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.algae.AlgaeSubsystem;
import frc.robot.subsystems.coral.CoralSubsystem;
import frc.robot.subsystems.coral.CoralSubsystem.CoralValue;
import frc.robot.subsystems.drive.SwerveDrive;
import frc.robot.subsystems.vision.Vision;

public class RobotContainer {
	private final CommandXboxController driver =
		new CommandXboxController(DroidRageConstants.Gamepad.DRIVER_CONTROLLER_PORT);
	
	private final CommandXboxController operator =
		new CommandXboxController(DroidRageConstants.Gamepad.OPERATOR_CONTROLLER_PORT);

	public RobotContainer(SwerveDrive drive, CoralSubsystem coralSubsystem, AlgaeSubsystem algaeSubsystem, Elevator elevator) {
		DriverStation.silenceJoystickConnectionWarning(true);
		configureTeleOpBindings(drive, coralSubsystem, algaeSubsystem, elevator);
	}

	//Add Reset encoder buttons
	//Add Manual Control
	public void configureTeleOpBindings(SwerveDrive drive, CoralSubsystem coral, AlgaeSubsystem algaeSubsystem, Elevator elevator) {
		// Drive Bindings
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

		driver.rightTrigger()
			.onTrue(new IntakeElementInCommand(driver, null, null));
		
		// Elevator Bindings
		elevator.setDefaultCommand(new ManualElevator(elevator, null));
		
		// Coral Bindings	
		operator.a()
				.onTrue(coral.setPositionCommand(CoralValue.L1));
		operator.b()
				.onTrue(coral.setPositionCommand(CoralValue.L2));
		operator.x()
				.onTrue(coral.setPositionCommand(CoralValue.L3));
		operator.y()
				.onTrue(coral.setPositionCommand(CoralValue.L4));
		operator.leftBumper()
				.onTrue(coral.setPositionCommand(CoralValue.INTAKE_HPS));
		operator.rightTrigger()
				.onTrue(coral.setPositionCommand(CoralValue.INTAKE_GRND));

		// Algae Bindings
		operator.povDown()
			.onTrue(algaeSubsystem.setPositionCommand(AlgaeSubsystem.ArmValue.GROUND));	
		operator.povRight()
			.onTrue(algaeSubsystem.setPositionCommand(AlgaeSubsystem.ArmValue.LOW));
		operator.povUp()
			.onTrue(algaeSubsystem.setPositionCommand(AlgaeSubsystem.ArmValue.HIGH));
		operator.povLeft()
			.onTrue(algaeSubsystem.setPositionCommand(AlgaeSubsystem.ArmValue.PROCESSOR));	
		operator.leftTrigger()
			.onTrue(algaeSubsystem.setPositionCommand(AlgaeSubsystem.ArmValue.SHOOT));	
	}

	public void teleopPeriodic(){}

	public void testCommands() {}

	public void testDrive(SwerveDrive drive, Vision vision){
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

		driver.a().onTrue(new InstantCommand(()->drive.resetOdometry(vision.getPose())));
	}
}

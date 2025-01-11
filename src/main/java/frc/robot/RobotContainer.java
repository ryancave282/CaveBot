package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.commands.IntakeElementInCommand;
import frc.robot.commands.manual.ManualElevator;
import frc.robot.commands.manual.SwerveDriveTeleop;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.carriage.Carriage;
import frc.robot.subsystems.carriage.Carriage.CoralValue;
import frc.robot.subsystems.drive.SwerveDrive;
import frc.robot.subsystems.vision.Vision;

public class RobotContainer {
	private final CommandXboxController driver =
		new CommandXboxController(DroidRageConstants.Gamepad.DRIVER_CONTROLLER_PORT);
	
	private final CommandXboxController operator =
		new CommandXboxController(DroidRageConstants.Gamepad.OPERATOR_CONTROLLER_PORT);

	public RobotContainer(){
		DriverStation.silenceJoystickConnectionWarning(true);
	}

	public void configureTeleOpBindings(
		SwerveDrive drive, 
		Carriage coralSubsystem, 
		Elevator elevator
		) {
		
		// Slow Mode and Gyro Reset in the Default Command
		drive.setDefaultCommand(new SwerveDriveTeleop( drive, driver));

		driver.rightTrigger()
			.onTrue(new IntakeElementInCommand(driver, coralSubsystem));
		
			
		elevator.setDefaultCommand(new ManualElevator(elevator, operator::getRightY));

		operator.leftBumper()
			.onTrue(elevator.setPositionCommand(Elevator.ElevatorValue.LOW))
			.onTrue(coralSubsystem.setPositionCommand(CoralValue.INTAKE_HPS));
		operator.rightTrigger()
			.onTrue(coralSubsystem.setPositionCommand(CoralValue.INTAKE_GRND));
		

		driver.b()
			.onTrue(DroidRageConstants.flipElement());

		//coral algae
		//ground ground pickup povDown 

		//hms coral povLeft
		//    

		// l1 processor  a
		//l2  x
		//l3 low  b
		//l4 high y

		

		//Button Toggle Positions
		// Coral Bindings	
		operator.a()
			.onTrue(elevator.setPositionCommand(Elevator.ElevatorValue.L1))
			.onTrue(coralSubsystem.setPositionCommand(CoralValue.L1));
		operator.b()
			.onTrue(elevator.setPositionCommand(Elevator.ElevatorValue.L2))
			.onTrue(coralSubsystem.setPositionCommand(CoralValue.L2));
		operator.x()
			.onTrue(elevator.setPositionCommand(Elevator.ElevatorValue.L3))
			.onTrue(coralSubsystem.setPositionCommand(CoralValue.L3));
		operator.y()
			.onTrue(elevator.setPositionCommand(Elevator.ElevatorValue.L4))
			.onTrue(coralSubsystem.setPositionCommand(CoralValue.L4));
		

		// Algae Bindings
		// operator.povDown()
		// 	.onTrue(elevator.setPositionCommand(Elevator.ElevatorValue.L4))
		// 	.onTrue(algaeSubsystem.setPositionCommand(AlgaeSubsystem.ArmValue.GROUND));	
		// operator.povRight()
		// 	.onTrue(elevator.setPositionCommand(Elevator.ElevatorValue.LOW))
		// 	.onTrue(algaeSubsystem.setPositionCommand(AlgaeSubsystem.ArmValue.LOW));
		// operator.povUp()
		// 	.onTrue(elevator.setPositionCommand(Elevator.ElevatorValue.HIGH))
		// 	.onTrue(algaeSubsystem.setPositionCommand(AlgaeSubsystem.ArmValue.HIGH));
		// operator.povLeft()
		// 	.onTrue(elevator.setPositionCommand(Elevator.ElevatorValue.GROUND))
		// 	.onTrue(algaeSubsystem.setPositionCommand(AlgaeSubsystem.ArmValue.PROCESSOR));	
		// operator.leftTrigger()
		// 	.onTrue(elevator.setPositionCommand(Elevator.ElevatorValue.GROUND))
		// 	.onTrue(algaeSubsystem.setPositionCommand(AlgaeSubsystem.ArmValue.SHOOT));	
	}

	public void testDrive(SwerveDrive drive, Vision vision){
		drive.setDefaultCommand(new SwerveDriveTeleop(drive, driver));
		driver.a().onTrue(new InstantCommand(()->drive.resetOdometry(vision.getPose())));
	}
}

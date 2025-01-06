// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.algae.AlgaeArm;
import frc.robot.subsystems.algae.AlgaeIntake;
import frc.robot.subsystems.algae.AlgaeShooter;
import frc.robot.subsystems.algae.AlgaeSubsystem;
import frc.robot.subsystems.coral.CoralArm;
import frc.robot.subsystems.coral.CoralIntake;
import frc.robot.subsystems.coral.CoralPivot;
import frc.robot.subsystems.coral.CoralSubsystem;
import frc.robot.subsystems.drive.SwerveDrive;
import frc.robot.subsystems.vision.Vision;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the manifest file in the resource
 * directory.
 */
//CAN 15 is skipped
//Test Vision
//Climb positions
//current stuff
public class Robot extends TimedRobot {
    private final Vision vision = new Vision();
    private final SwerveDrive drive = new SwerveDrive(false);//2-10 Works
    private final Elevator elevator = new Elevator();
   
    // Initialize Coral Subsystem
    private final CoralArm coralArm = new CoralArm();
    private final CoralPivot coralPivot = new CoralPivot();
    private final CoralIntake coralIntake = new CoralIntake(false);
    private final CoralSubsystem coralSubsystem = new CoralSubsystem(coralArm, coralPivot, coralIntake);

    private final AlgaeArm algaeArm = new AlgaeArm();
    private final AlgaeShooter algaeShooter = new AlgaeShooter(true);
    private final AlgaeIntake algaeIntake = new AlgaeIntake();
    private final AlgaeSubsystem algaeSubsystem = new AlgaeSubsystem(algaeArm, algaeIntake, algaeShooter);
    
    private RobotContainer robotContainer = new RobotContainer(drive, coralSubsystem, algaeSubsystem, elevator);

    private ShuffleboardValue<Double> matchTime = ShuffleboardValue.create
		(0.0, "Match Time", "Misc")
		.withWidget(BuiltInWidgets.kTextView)
		.build();
    private Command autonomousCommand;
  
  /**
     * This function is run when the robot is first started up and should be used for any
     * initialization code.
     * Instantiate our RobotContainer.  This will perform all our button bindings, and put our
     * autonomous chooser on the dashboard.
     */
    @Override
    public void robotInit() {
        // RobotController.setBrownoutVoltage(kDefaultPeriod);
        // 6.3V for Roborio1- Roborio2 is 6.75V
    }
    
    /**
     * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
     * that you want ran during disabled, autonomous, teleoperated and test.
     *
     * <p>This runs after the mode specific periodic functions, but before LiveWindow and
     * SmartDashboard integrated updating.
     * Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
     * commands, running already-scheduled commands, removing finished or interrupted commands,
     * and running subsystem periodic() methods.  This must be called from the robot's periodic
     * block in order for anything in the Command-based framework to work.
     */
    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
        // if(DriverStation.isEStopped()){ //Robot Estopped
        //     light.flashingColors(light.red, light.white);
        // }
        // light.setAllColor(light.yellow);
    }

    @Override
    public void disabledInit() {
        // cycleTracker.printAllData();
    }
    
    @Override
    public void disabledPeriodic() {
        //In Here, Try using controller to pick the auto
        // if(RobotController.getBatteryVoltage()<11.5){
        //     light.setAllColor(light.batteryBlue);
        //     // drive.playMusic(2);
        // } else{
        //     light.flashingColors(light.yellow, light.blue);
        // }
        // light.setAllColor(light.blue);
    }

    @Override
    public void autonomousInit() {
        CommandScheduler.getInstance().cancelAll();
        // autonomousCommand = autoChooser.getAutonomousCommand();
        autonomousCommand = new InstantCommand();

        if (autonomousCommand != null) {
            autonomousCommand.schedule();
        }
    }

    @Override
    public void autonomousPeriodic() {
        // if(DriverStation.isEStopped()){ //Robot Estopped
        //     light.flashingColors(light.red, light.white);
        // }
    }
    
    @Override
    public void teleopInit() {
        CommandScheduler.getInstance().cancelAll();
		DriverStation.silenceJoystickConnectionWarning(true);
        // ampMech.setTeleopStartPos();
        // drive.changeAllianceRotation();//Works
        // drive.runOnce(()->drive.setYawCommand(drive.getRotation2d().rotateBy(Rotation2d.fromDegrees(0)).getDegrees()));

		// drive.driveAutoReset();//TODO:Test
        // robotContainer.configureTeleOpBindings(drive, intake, shooter, ampMech, climb, cycleTracker,vision
        // );
        // test.setTargetPosition(Intake.Value.START.getAngle());
        // robotContainer.testCommands(test);

        // robotContainer.testCommands(vision, drive);
        robotContainer.testDrive(drive,vision);
        // teleopButtons.newTeleopButtons( climb, intake, shooter, ampMech , drive);
    }

    @Override
    public void teleopPeriodic() {
        // robotContainer.teleopPeriodic(intake,shooter);
        matchTime.set(DriverStation.getMatchTime());
    }
    
    @Override
    public void testInit() {
        CommandScheduler.getInstance().cancelAll();
    }
    @Override
    public void testPeriodic() {}

    @Override
    public void simulationInit() {}

    @Override
    public void simulationPeriodic() {}

    @Override
    public void teleopExit(){
        // cycleTracker.printAllData();
    }


        
}
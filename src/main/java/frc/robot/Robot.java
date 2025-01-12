package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Light;
import frc.robot.subsystems.carriage.Arm;
import frc.robot.subsystems.carriage.Intake;
import frc.robot.subsystems.carriage.Pivot;
import frc.robot.subsystems.carriage.Carriage;
import frc.robot.subsystems.drive.SwerveDrive;
import frc.robot.subsystems.vision.Vision;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

public class Robot extends TimedRobot {
    private final Vision vision = new Vision();
    private final SwerveDrive drive = new SwerveDrive(false);//2-10 Works
    private final Elevator elevator = new Elevator(false);
   
    private final Carriage carriage = new Carriage(
        new Arm(false), 
        new Pivot(false), 
        new Intake(false));
    private final Light light = new Light();
    
    private RobotContainer robotContainer = new RobotContainer();

    private ShuffleboardValue<Double> matchTime = ShuffleboardValue.create
		(0.0, "Match Time", "Misc")
		.withWidget(BuiltInWidgets.kTextView)
		.build();
    private Command autonomousCommand;
  
    @Override
    public void robotInit() {
    }
    
    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
        if(DriverStation.isEStopped()){ //Robot Estopped
            light.flashingColors(light.red, light.white);
        }
    }

    @Override
    public void disabledInit() {
        // cycleTracker.printAllData();
    }
    
    @Override
    public void disabledPeriodic() {
        //In Here, Try using controller to pick the auto

        if(RobotController.getBatteryVoltage()<11.5){
            light.setAllColor(light.batteryBlue);
            // drive.playMusic(2);
        } else{
            light.flashingColors(light.yellow, light.blue);
        }
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
        if(DriverStation.isEStopped()){ //Robot Estopped
            light.flashingColors(light.red, light.white);
        }
    }
    
    @Override
    public void teleopInit() {
        CommandScheduler.getInstance().cancelAll();
		DriverStation.silenceJoystickConnectionWarning(true);
        robotContainer.configureTeleOpBindings(drive, carriage, elevator);
        // robotContainer.testDrive(drive, vision);
    }

    @Override
    public void teleopPeriodic() {
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
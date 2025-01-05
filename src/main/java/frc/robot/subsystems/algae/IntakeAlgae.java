package frc.robot.subsystems.algae;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import frc.robot.DroidRageConstants.Control;
import frc.robot.utility.motor.CANMotorEx;
import frc.robot.utility.motor.TalonEx;
import frc.robot.utility.motor.CANMotorEx.Direction;
import frc.robot.utility.motor.CANMotorEx.ZeroPowerMode;
import frc.robot.utility.template.IntakeTemplate;

public class IntakeAlgae extends IntakeTemplate{
  
    public static class Constants {
        public static final double MIN_POSITION = 0;
        public static final double MAX_POSITION = 10;
    }
    public static TalonEx motor = TalonEx.create(0)
    .withDirection(Direction.Forward)
    .withIdleMode(ZeroPowerMode.Coast)
    .withPositionConversionFactor(1)
    .withSubsystemName("IntakeAlgae")
    .withIsEnabled(true)
    .withSupplyCurrentLimit(0);

    public IntakeAlgae () {
        super(
            new CANMotorEx[] {motor}, 
        new PIDController(0, 0, 0), 
        new SimpleMotorFeedforward(0, 0, 0, 0), 
        Constants.MAX_POSITION, 
        Constants.MIN_POSITION, 
        Control.PID, "IntakeAlgae", 0
        );
    }
}
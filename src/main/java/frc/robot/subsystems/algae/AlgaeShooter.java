package frc.robot.subsystems.algae;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import frc.robot.DroidRageConstants.Control;
import frc.robot.utility.motor.CANMotorEx;
import frc.robot.utility.motor.CANMotorEx.Direction;
import frc.robot.utility.motor.CANMotorEx.ZeroPowerMode;
import frc.robot.utility.motor.TalonEx;
import frc.robot.utility.template.IntakeTemplate;

public class AlgaeShooter extends IntakeTemplate {
    public static class Constants {
        public static final double MAX_SPEED = 0;
        public static final double MIN_SPEED = 10;
    }

    public static TalonEx rightMotor = TalonEx.create(0)
        .withDirection(Direction.Forward)
        .withIdleMode(ZeroPowerMode.Coast)
        .withPositionConversionFactor(1)
        .withSubsystemName("Algae")
        .withIsEnabled(true)
        .withCurrentLimit(0);
    
    public static TalonEx leftMotor = TalonEx.create(0)
        .withDirection(Direction.Forward)
        .withIdleMode(ZeroPowerMode.Coast)
        .withPositionConversionFactor(1)
        .withSubsystemName("Algae")
        .withIsEnabled(true)
        .withCurrentLimit(0);
    
    public AlgaeShooter (boolean isEnabled) {
        super(
            new CANMotorEx[] {rightMotor, leftMotor}, 
            new PIDController(0, 0, 0), 
            new SimpleMotorFeedforward(0, 0, 0, 0), 
            Constants.MAX_SPEED,    
            Constants.MIN_SPEED, 
            Control.PID, "Algae", 0
        );
    }

    

    
}

package frc.robot.subsystems.coral;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import frc.robot.DroidRageConstants.Control;
import frc.robot.utility.motor.CANMotorEx;
import frc.robot.utility.motor.CANMotorEx.Direction;
import frc.robot.utility.motor.CANMotorEx.ZeroPowerMode;
import frc.robot.utility.motor.TalonEx;
import frc.robot.utility.template.IntakeTemplate;

public class CoralIntake extends IntakeTemplate {
    private static class Constants {
        public static final double MAX_SPEED = 0;
        public static final double MIN_SPEED = 0;
    }
    
    private static TalonEx motor = TalonEx.create(0)
        .withDirection(Direction.Forward)
        .withIdleMode(ZeroPowerMode.Coast)
        .withPositionConversionFactor(1)
        .withSubsystemName("Coral")
        .withIsEnabled(true)
        .withSupplyCurrentLimit(50);

    public CoralIntake() {
        super(
        new CANMotorEx[]{motor}, 
        new PIDController(0,0,0), 
        new SimpleMotorFeedforward(0, 0, 0, 0), Constants.MAX_SPEED, Constants.MIN_SPEED, 
        Control.PID, "Coral", 0);
    }
}

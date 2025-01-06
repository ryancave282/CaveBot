package frc.robot.subsystems;

import edu.wpi.first.math.controller.ElevatorFeedforward;
import edu.wpi.first.math.controller.PIDController;
import frc.robot.DroidRageConstants.Control;
import frc.robot.utility.motor.CANMotorEx;
import frc.robot.utility.motor.CANMotorEx.Direction;
import frc.robot.utility.motor.CANMotorEx.ZeroPowerMode;
import frc.robot.utility.motor.TalonEx;
import frc.robot.utility.template.ElevatorTemplate;

public class Elevator extends ElevatorTemplate {
    public static class Constants {
        public static final double MIN_POSITION = 0;
        public static final double MAX_POSITION = 10;   
    }

    public enum ElevatorValue {
        START(0),
        ;

        private final double height;

        private ElevatorValue(double height) {
            this.height = height;
        }

        public double getHeight() {
            return height;
        }
    }

    private static TalonEx motor = TalonEx.create(0)
        .withDirection(Direction.Forward)
        .withIdleMode(ZeroPowerMode.Coast)
        .withPositionConversionFactor(1)
        .withSubsystemName("Elevator")
        .withIsEnabled(true)
        .withCurrentLimit(50);

    public Elevator(boolean isEnabled) {
        super(
        new CANMotorEx[]{motor}, 
        new PIDController(0, 0, 0), 
        new ElevatorFeedforward(0, 0, 0, 0), 
        Constants.MAX_POSITION, 
        Constants.MIN_POSITION, 
        Control.PID, "Elevator", 0);
        motor.setIsEnabled(isEnabled);
    }
}

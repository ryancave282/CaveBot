package frc.robot.subsystems;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.PIDController;
import net.droidrage.lib.DroidRageConstants.Control;
import net.droidrage.lib.encoder.EncoderEx.EncoderDirection;
import net.droidrage.lib.encoder.SparkAbsoluteEncoderEx;
import net.droidrage.lib.motor.CANMotorEx;
import net.droidrage.lib.motor.CANMotorEx.Direction;
import net.droidrage.lib.motor.CANMotorEx.ZeroPowerMode;
import net.droidrage.lib.motor.SparkMaxEx;
import net.droidrage.lib.template.ArmAbsoluteTemplate;

public class Climb extends ArmAbsoluteTemplate {
    public static class Constants {
        public static final double MAX_POSITION = 0;
        public static final double MIN_POSITION = 0;
        public static final double OFFSET = 0;
    }
    
    private static SparkMaxEx motor = SparkMaxEx.create(0)
        .withDirection(Direction.Forward)
        .withIdleMode(ZeroPowerMode.Coast)
        .withPositionConversionFactor(1)
        .withSubsystemName("Climb")
        .withIsEnabled(true)
        .withCurrentLimit(50);
    
    private static SparkAbsoluteEncoderEx encoder = SparkAbsoluteEncoderEx.create(motor)
        .withDirection(EncoderDirection.Forward)
        .withSubsystemBase("Climb");
        
    public Climb(boolean isEnabled) {
        super(
        new CANMotorEx[]{motor}, 
        new PIDController(0,0,0), 
        new ArmFeedforward(0, 0, 0, 0, 0), 
        Constants.MAX_POSITION, Constants.MIN_POSITION, Constants.OFFSET, 
        Control.PID, "Climb", 0, encoder);
        motor.setIsEnabled(isEnabled);
    }
}

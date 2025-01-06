package frc.robot.subsystems.algae;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.PIDController;
import frc.robot.utility.motor.TalonEx;
import frc.robot.DroidRageConstants.Control;
import frc.robot.DroidRageConstants.EncoderDirection;
import frc.robot.utility.encoder.AbsoluteDutyEncoderRIO;
import frc.robot.utility.motor.CANMotorEx;
import frc.robot.utility.motor.CANMotorEx.Direction;
import frc.robot.utility.motor.CANMotorEx.ZeroPowerMode;
import frc.robot.utility.template.ArmAbsoluteTemplate;


public class AlgaeArm extends ArmAbsoluteTemplate{
    public static class Constants {
        public static final double MIN_POSITION = 0;
        public static final double MAX_POSITION = 10;
        public static final double offset= 0;
        
    }
    public static TalonEx motor = TalonEx.create(0)
        .withDirection(Direction.Forward)
        .withIdleMode(ZeroPowerMode.Coast)
        .withPositionConversionFactor(1)
        .withSubsystemName("Algae")
        .withIsEnabled(true)
        .withCurrentLimit(0);

    public static AbsoluteDutyEncoderRIO encoder = AbsoluteDutyEncoderRIO.create(0) 
        .withDirection(EncoderDirection.Forward)
        .withOffset(0)
        .withSubsystemBase("Algae");

public AlgaeArm (boolean isEnabled) {
    super(
        new CANMotorEx[] {motor}, 
        new PIDController(0, 0, 0), 
        new ArmFeedforward(0, 0, 0, 0), 
        Constants.MAX_POSITION, 
        Constants.MIN_POSITION, 
        Constants.offset,
        Control.PID, "Algae",0,
        encoder);
    motor.setIsEnabled(isEnabled);
}
}
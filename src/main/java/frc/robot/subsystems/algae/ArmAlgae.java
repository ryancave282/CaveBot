package frc.robot.subsystems.algae;

import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.PIDController;
import frc.robot.utility.motor.TalonEx;
import frc.robot.DroidRageConstants.Control;
import frc.robot.utility.encoder.AbsoluteDutyEncoderRIO;
import frc.robot.utility.motor.CANMotorEx;
import frc.robot.utility.motor.CANMotorEx.Direction;
import frc.robot.utility.motor.CANMotorEx.ZeroPowerMode;
import frc.robot.utility.template.ArmAbsoluteTemplate;


public class ArmAlgae extends ArmAbsoluteTemplate{
    public static class Constants {
        public static final double MIN_POSITION = 0;
        public static final double MAX_POSITION = 10;
        public static final double offset= 0;
        
    }
    public static TalonEx motor = TalonEx.create(0)
    .withDirection(Direction.Forward)
    .withIdleMode(ZeroPowerMode.Coast)
    .withPositionConversionFactor(1)
    .withSubsystemName("ArmAlgae")
    .withIsEnabled(true)
    .withSupplyCurrentLimit(0);

    public static AbsoluteDutyEncoderRIO encoder = AbsoluteDutyEncoderRIO.create(0) 
    .withDirection(true)
    .withOffset(0)
    .withSubsystemBase("ArmAlgae");

public ArmAlgae () {
    super(
        new CANMotorEx[] {motor}, 
        new PIDController(0, 0, 0), 
        new ArmFeedforward(0, 0, 0, 0), 
        Constants.MAX_POSITION, 
        Constants.MIN_POSITION, 
        Constants.offset,
        Control.PID, "ArmAlgae",0,
        encoder);
}
}
package frc.robot.subsystems.coral;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.PIDController;
import frc.robot.DroidRageConstants.Control;
import frc.robot.utility.encoder.AbsoluteDutyEncoderRIO;
import frc.robot.utility.motor.CANMotorEx;
import frc.robot.utility.motor.CANMotorEx.Direction;
import frc.robot.utility.motor.CANMotorEx.ZeroPowerMode;
import frc.robot.utility.motor.TalonEx;
import frc.robot.utility.template.ArmAbsoluteTemplate;

public class CoralPivot extends ArmAbsoluteTemplate {
    private static TalonEx motor = TalonEx.create(0)
        .withDirection(Direction.Forward)
        .withIdleMode(ZeroPowerMode.Coast)
        .withPositionConversionFactor(1)
        .withSubsystemName("Coral Pivot")
        .withIsEnabled(true)
        .withSupplyCurrentLimit(50);
    
    private static AbsoluteDutyEncoderRIO encoder = AbsoluteDutyEncoderRIO.create(0)
        .withDirection(false)
        .withOffset(0)
        .withSubsystemBase("Coral Pivot");

    public CoralPivot() {
        super(
        new CANMotorEx[]{motor}, 
        new PIDController(0,0,0), 
        new ArmFeedforward(0, 0, 0, 0, 0), 0, 0, 0, 
        Control.PID, "Coral Pivot", 0, encoder);
        
    }
}

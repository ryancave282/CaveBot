package frc.robot.subsystems;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import net.droidrage.lib.DroidRageConstants.Control;
import net.droidrage.lib.motor.CANMotorEx;
import net.droidrage.lib.motor.CANMotorEx.Direction;
import net.droidrage.lib.motor.CANMotorEx.ZeroPowerMode;
import net.droidrage.lib.motor.TalonEx;
import net.droidrage.lib.template.IntakeTemplate;

public class FunnelIntake extends SubsystemBase {
    public enum FunnelValue{
        STOP(0,0),
        INTAKE(0,0);

        public final double intakeSpeed, beltSpeed;
        private FunnelValue(double intakeSpeed, double beltSpeed){
            this.intakeSpeed = intakeSpeed;
            this.beltSpeed = beltSpeed;
        }
    }

    private static TalonEx intakeLMotor = TalonEx.create(0)
        .withDirection(Direction.Forward)
        .withIdleMode(ZeroPowerMode.Coast)
        .withPositionConversionFactor(1)
        .withSubsystemName("funnel")
        .withIsEnabled(true)
        .withCurrentLimit(50);
    private static TalonEx intakeRMotor = TalonEx.create(0)
        .withDirection(Direction.Forward)
        .withIdleMode(ZeroPowerMode.Coast)
        .withPositionConversionFactor(1)
        .withSubsystemName("funnel")
        .withIsEnabled(true)
        .withCurrentLimit(50);
    private static TalonEx beltLMotor = TalonEx.create(0)
        .withDirection(Direction.Forward)
        .withIdleMode(ZeroPowerMode.Coast)
        .withPositionConversionFactor(1)
        .withSubsystemName("funnel")
        .withIsEnabled(true)
        .withCurrentLimit(50);
    private static TalonEx beltRMotor = TalonEx.create(0)
        .withDirection(Direction.Forward)
        .withIdleMode(ZeroPowerMode.Coast)
        .withPositionConversionFactor(1)
        .withSubsystemName("funnel")
        .withIsEnabled(true)
        .withCurrentLimit(50);

    private IntakeTemplate intake = new IntakeTemplate(
        new CANMotorEx[]{intakeLMotor, intakeRMotor}, 
        new PIDController(0,0,0), 
        new SimpleMotorFeedforward(0, 0, 0, 0), 1,0, 
        Control.PID, "carriage", 0);
    private IntakeTemplate beltIntake = new IntakeTemplate(
        new CANMotorEx[] { beltLMotor, beltRMotor },
        new PIDController(0, 0, 0),
        new SimpleMotorFeedforward(0, 0, 0, 0), 1,0,
        Control.PID, "carriage", 0);

    public FunnelIntake(boolean isEnabled) {
        intakeRMotor.setIsEnabled(isEnabled);
        intakeLMotor.setIsEnabled(isEnabled);
        beltLMotor.setIsEnabled(isEnabled);
        beltRMotor.setIsEnabled(isEnabled);
    }

    public Command setTargetPositionCommand(FunnelValue targetPos) {
        return new SequentialCommandGroup(
            intake.setTargetPositionCommand(targetPos.intakeSpeed),
            beltIntake.setTargetPositionCommand(targetPos.beltSpeed)
        );
    }
}

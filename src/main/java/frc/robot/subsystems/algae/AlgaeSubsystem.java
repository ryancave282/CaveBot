package frc.robot.subsystems.algae;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class AlgaeSubsystem {

    public enum ArmValue {
        START(0),
        GROUND(0),
        HIGH(0),
        LOW(0),
        PROCESSOR(0),
        SHOOT(0);

        private final double armAlgaePos;
        private ArmValue(double armAlgaePos) {
            this.armAlgaePos = armAlgaePos;
    
        }

        public double getArmAlgaePos() {
            return armAlgaePos;
        }
    }

    public enum IntakeValue{
        START(0),
        INTAKE(0),
        OUTTAKE(0),
        STOP(0);
    
    private final double intakeAlgaePos;
    private IntakeValue(double intakeAlgaePos) {
        this.intakeAlgaePos = intakeAlgaePos;
    }

    public double getIntakeAlgaePos() {
        return intakeAlgaePos;
        }
    }

    private final AlgaeArm armAlgae;
    private final AlgaeIntake intakeAlgae;
    private final DigitalInput algaeLimitSwitch;


    public AlgaeSubsystem (AlgaeArm armAlgae, AlgaeIntake intakeAlgae) {
        this.armAlgae = armAlgae;
        this.intakeAlgae = intakeAlgae;
        this.algaeLimitSwitch = new DigitalInput(0);
    }
    
    public AlgaeArm getArmAlgae() {
        return armAlgae;
    }


    public Command setPositionCommand(ArmValue targetPosition) {
        return switch (targetPosition) {
            case START, PROCESSOR, SHOOT -> 
                new SequentialCommandGroup(
                    armAlgae.setTargetPositionCommand(targetPosition.getArmAlgaePos())
                );
            case GROUND, HIGH, LOW ->
            new SequentialCommandGroup(
                armAlgae.setTargetPositionCommand(targetPosition.getArmAlgaePos())
                );

            default -> new SequentialCommandGroup(
                armAlgae.setTargetPositionCommand(targetPosition.getArmAlgaePos())
                );
        };
    }

    public Command setIntakePositionCommand(IntakeValue targetPosition) {
        return new SequentialCommandGroup(
            intakeAlgae.setTargetPositionCommand(targetPosition.getIntakeAlgaePos())
        );
    };
        

        public AlgaeIntake getAlgaeIntake() {
            return intakeAlgae;
        }
    
    public boolean isAlgaeIn() {
        return algaeLimitSwitch.get();
    }

}    
    
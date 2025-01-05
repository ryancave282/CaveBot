package frc.robot.subsystems.algae;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class AlgaeSystems {

    public enum Value {
        START(0, 0);

        private final double armAlgaePos;
        private final double intakeAlgaePos;

        private Value(double armAlgaePos, double intakeAlgaePos) {
            this.armAlgaePos = armAlgaePos;
            this.intakeAlgaePos = intakeAlgaePos;
        }

        public double getArmAlgaePos() {
            return armAlgaePos;
        }

        public double getIntakeAlgaePos() {
            return intakeAlgaePos;
        }
    }

    private final ArmAlgae armAlgae;
    private final IntakeAlgae intakeAlgae;


    public AlgaeSystems(ArmAlgae armAlgae, IntakeAlgae intakeAlgae) {
        this.armAlgae = armAlgae;
        this.intakeAlgae = intakeAlgae;
    }

    public ArmAlgae getArmAlgae() {
        return armAlgae;
    }

    public IntakeAlgae getIntakeAlgae() {
        return intakeAlgae;
    }

    public Command setPositionCommand(Value targetPosition) {
        return switch (targetPosition) {
            case START -> 
                new SequentialCommandGroup(
                armAlgae.runOnce(() -> armAlgae.setTargetPosition
                (targetPosition.getArmAlgaePos())),
                intakeAlgae.runOnce(() -> intakeAlgae.setTargetPosition
                (targetPosition.getIntakeAlgaePos()))
                );
    };
}
}
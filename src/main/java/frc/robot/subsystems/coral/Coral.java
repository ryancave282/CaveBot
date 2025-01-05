package frc.robot.subsystems.coral;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class Coral {
    public enum Value{
        START(0, 0, 0);

        private final double armAngle;
        private final double pivotAngle;
        private final double intakeSpeed;

        private Value(double armAngle, double pivotAngle, double intakeSpeed){
            this.armAngle = armAngle;
            this.pivotAngle = pivotAngle;
            this.intakeSpeed = intakeSpeed;
        }
        
        public double getArmAngle(){
            return armAngle;
        }

        public double getPivotAngle(){
            return pivotAngle;
        }

        public double getIntakeSpeed(){
            return intakeSpeed;
        }
    }

    public CoralArm arm;
    public CoralPivot pivot;
    public CoralIntake intake;

    private Value position = Value.START;

    public Coral(CoralArm arm, CoralPivot pivot, CoralIntake intake){
        this.arm = arm;
        this.pivot = pivot;
        this.intake = intake;
    }

    public Value getPosition() {
        return position;
    }
    
    public Command setPositionCommand(Value targetPos) {
        position = targetPos;
        return Commands.sequence(
            switch (targetPos) {
                case START -> 
                    new SequentialCommandGroup(
                        arm.setTargetPositionCommand(targetPos.getArmAngle()),
                        pivot.setTargetPositionCommand(targetPos.getPivotAngle()),
                        intake.setTargetPositionCommand(targetPos.getIntakeSpeed())
                    );
                default -> 
                    new ParallelCommandGroup(
                        arm.setTargetPositionCommand(targetPos.getArmAngle()),
                        pivot.setTargetPositionCommand(targetPos.getPivotAngle()),
                        intake.setTargetPositionCommand(targetPos.getIntakeSpeed())
                    );
            }
        );
    }

    public CoralArm getCoralArm(){
        return arm;
    }

    public CoralPivot getCoralPivot(){
        return pivot;
    }

    public CoralIntake getCoralIntake(){
        return intake;
    }
}

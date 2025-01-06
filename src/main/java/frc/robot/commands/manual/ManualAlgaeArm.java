package frc.robot.commands.manual;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.DroidRageConstants;
import frc.robot.subsystems.algae.AlgaeArm;

public class ManualAlgaeArm extends Command {
    private final AlgaeArm arm;
    private final Supplier<Double> armMove;
    
    public ManualAlgaeArm(AlgaeArm arm, Supplier<Double> armMove) {
        this.arm = arm;
        this.armMove= armMove;
        
        addRequirements(arm);
    }

    @Override
    public void initialize() {}

    @Override
    public void execute() {
        double move = -armMove.get();
        move = DroidRageConstants.squareInput(move);
        move = DroidRageConstants.applyDeadBand(move);
        arm.setTargetPosition(arm.getTargetPosition() + move * 0.05);//For Encoder
    }

    @Override
    public void end(boolean interrupted) {}

    @Override
    public boolean isFinished() {
        return false;
    }
}
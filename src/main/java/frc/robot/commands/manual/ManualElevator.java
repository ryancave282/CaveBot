package frc.robot.commands.manual;

import java.util.function.Supplier;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Elevator;
import net.droidrage.lib.DroidRageConstants;

public class ManualElevator extends Command {
    private final Elevator elevator;
    private final Supplier<Double> elevatorMove;
    
    public ManualElevator(Elevator elevator, Supplier<Double> elevatorMove) {
        this.elevator = elevator;
        this.elevatorMove = elevatorMove;
        
        addRequirements(elevator);
    }

    @Override
    public void initialize() {}

    @Override
    public void execute() {
        double move = -elevatorMove.get();
        move = DroidRageConstants.squareInput(move);
        move = DroidRageConstants.applyDeadBand(move);
        elevator.setTargetPosition(elevator.getTargetPosition() + move * 0.05);
    }

    @Override
    public void end(boolean interrupted) {}

    @Override
    public boolean isFinished() {
        return false;
    }
}

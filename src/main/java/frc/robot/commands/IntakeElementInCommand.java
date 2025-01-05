package frc.robot.commands;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.algae.AlgaeSystems;
import frc.robot.subsystems.coral.Coral;

public class IntakeElementInCommand extends ParallelCommandGroup {
    public IntakeElementInCommand(CommandXboxController driver, AlgaeSystems algaeSubSystem, Coral coralSubsystem) {
        addRequirements(algaeSubSystem.getAlgaeIntake(), coralSubsystem.getCoralIntake());
        addCommands(
            new ConditionalCommand(
                new SequentialCommandGroup(//true
                    algaeSubSystem.setIntakeCommand(AlgaeSystems.STOP),
                    new OperatorXboxControllerRumble(driver, RumbleType.kRightRumble,3)
                    // new InstantCommand(
                    //     ()->driver.getHID().setRumble(RumbleType.kBothRumble, 1)
                    // ).withTimeout(3)
                ),
                new SequentialCommandGroup(//false
                    algaeSubSystem.setIntakeCommand(AlgaeSystems.INTAKE)
                ),
                algaeSubSystem::isAlgaeIn),
            new ConditionalCommand(
                new SequentialCommandGroup(//true
                    coralSubsystem.setIntakeCommand(AlgaeSystems.STOP),
                    new OperatorXboxControllerRumble(driver, RumbleType.kLeftRumble,3)
                    // new InstantCommand(
                    //     ()->driver.getHID().setRumble(RumbleType.kBothRumble, 1)
                    // ).withTimeout(3)
                ),
                new SequentialCommandGroup(//false
                    coralSubsystem.setIntakeCommand(AlgaeSystems.INTAKE)
                ),
                coralSubsystem::isCoralIn)
            
        );
    }
}

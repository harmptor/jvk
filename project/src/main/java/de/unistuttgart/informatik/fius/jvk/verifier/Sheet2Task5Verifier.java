package de.unistuttgart.informatik.fius.jvk.verifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.stream.Collectors;

import de.unistuttgart.informatik.fius.icge.simulation.Position;
import de.unistuttgart.informatik.fius.icge.simulation.Simulation;
import de.unistuttgart.informatik.fius.icge.simulation.TaskVerifier;
import de.unistuttgart.informatik.fius.icge.simulation.actions.*;
import de.unistuttgart.informatik.fius.icge.ui.TaskInformation;
import de.unistuttgart.informatik.fius.icge.ui.TaskVerificationStatus;
import de.unistuttgart.informatik.fius.jvk.provided.BasicTaskInformation;
import de.unistuttgart.informatik.fius.jvk.provided.entity.Coin;
import de.unistuttgart.informatik.fius.jvk.provided.entity.Wall;


public class Sheet2Task5Verifier implements TaskVerifier {

    private BasicTaskInformation task;

    private BasicTaskInformation taskA = new BasicTaskInformation("a) Select this task", "Select this task.", TaskVerificationStatus.SUCCESSFUL);
    private BasicTaskInformation taskB = new BasicTaskInformation("b) Collect all coins", "Collect all coins.");
    private BasicTaskInformation taskC = new BasicTaskInformation("c) Place all coins in front of the right wall.", "Place all coins in front of the right wall.");
    private BasicTaskInformation taskD = new BasicTaskInformation("d) Draw a line", "Put down exactly one coin on each field in the middle row");
    private BasicTaskInformation taskE = new BasicTaskInformation("e) Draw a pattern", "Place on the left most playing field one coin, and increase the number of coins for each step to the right.");
    private BasicTaskInformation taskF = new BasicTaskInformation("f) Draw a pattern 2", "Same as e) but in steps of size two.");
    private BasicTaskInformation taskG = new BasicTaskInformation("g) Draw a pattern 3", "Same as d) but for each reachable playing field");

    private ActionLog actionLog;
    private Simulation sim;

    public Sheet2Task5Verifier() {
        System.out.println("test");

        List<BasicTaskInformation> subTasks = new ArrayList<>();
        subTasks.add(this.taskA);
        subTasks.add(this.taskB);
        subTasks.add(this.taskC);
        subTasks.add(this.taskD);
        subTasks.add(this.taskE);
        subTasks.add(this.taskF);
        subTasks.add(this.taskG);
        this.task = new BasicTaskInformation("Sheet 1 Task 5", "Learn how to use the Playfield modifier.", subTasks);
    }

    @Override
    public void attachToSimulation(Simulation sim) {
        this.actionLog = sim.getActionLog();
        this.sim = sim;
    }

    @Override
    public void verify() {
        if(testTaskB())
            this.taskB = this.taskB.updateStatus(TaskVerificationStatus.SUCCESSFUL);
        if(testTaskC())
            this.taskC = this.taskC.updateStatus(TaskVerificationStatus.SUCCESSFUL);
        if(testTaskD())
            this.taskD = this.taskD.updateStatus(TaskVerificationStatus.SUCCESSFUL);
        if(testTaskE())
            this.taskE = this.taskE.updateStatus(TaskVerificationStatus.SUCCESSFUL);
        if(testTaskF())
            this.taskF = this.taskF.updateStatus(TaskVerificationStatus.SUCCESSFUL);
        if(testTaskG())
            this.taskG = this.taskG.updateStatus(TaskVerificationStatus.SUCCESSFUL);

        List<BasicTaskInformation> subTasks = new ArrayList<>();
        subTasks.add(this.taskA);
        subTasks.add(this.taskB);
        subTasks.add(this.taskC);
        subTasks.add(this.taskD);
        subTasks.add(this.taskE);
        subTasks.add(this.taskF);
        subTasks.add(this.taskG);
        this.task = this.task.updateSubTasks(subTasks);
    }

    private boolean testTaskB(){
        return sim.getPlayfield().getEntitiesAt(new Position(1, 0)).isEmpty();
    }

    private boolean testTaskC(){
        List<EntityCollectAction> collectActions = actionLog.getActionsOfType(EntityCollectAction.class, true);
        return sim.getPlayfield().getEntitiesAt(new Position(1, 0)).isEmpty() &&
            sim.getPlayfield().getEntitiesAt(new Position(9, 0)).size() == collectActions.size();
    }

    private boolean testTaskD(){
        for(int i = 0; i < 10; i++){
            if(sim.getPlayfield().getEntitiesOfTypeAt(new Position(i, 0), Coin.class, true).size() != 1)
                return false;
        }
        return true;
    }

    private boolean testTaskE(){
        for(int i = 0; i < 10; i++){
            if(sim.getPlayfield().getEntitiesOfTypeAt(new Position(i, 0), Coin.class, true).size() != i+1)
                return false;
        }
        return true;
    }

    private boolean testTaskF(){
        for(int i = 0; i < 10; i++){
            if(i % 2 == 0 && sim.getPlayfield().getEntitiesOfTypeAt(new Position(i, 0), Coin.class, true).size() != i+1)
                return false;
            if(i % 2 == 1 && sim.getPlayfield().getEntitiesOfTypeAt(new Position(i, 0), Coin.class, true).size() > 0)
                return false;
        }
        return true;
    }

    private boolean testTaskG(){
        for(int j = -1; j < 2; j++){
            for(int i = 0; i < 10; i++){
                if(sim.getPlayfield().getEntitiesOfTypeAt(new Position(i, j), Wall.class, true).isEmpty() && 
                sim.getPlayfield().getEntitiesOfTypeAt(new Position(i, j), Coin.class, true).size() != 1)
                    return false;
            }
        }
        return true;
    }


    @Override
    public TaskInformation getTaskInformation() {
        return this.task;
    }



}

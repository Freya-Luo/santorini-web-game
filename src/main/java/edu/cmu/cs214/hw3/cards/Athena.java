package edu.cmu.cs214.hw3.cards;

import edu.cmu.cs214.hw3.models.Cell;
import edu.cmu.cs214.hw3.models.Game;
import edu.cmu.cs214.hw3.models.Worker;

import java.util.List;

public class Athena extends God {

    @Override
    public void doMove(Worker worker, Cell moveTo, Game game) {
        Cell oldPosition = worker.getCurPosition();
        super.doMove(worker, moveTo, game);

        if (moveTo.getHeight() > oldPosition.getHeight()) {
            setUsePowerToOpponent();
        } else {
            setNotUsePowerToOpponent();
        }
    }

     @Override
     public List<Cell> applyOpponentPower(List<Cell> possibleCells, Worker worker) {
        // Apply Athena God Rule;
         if (getPowerToOpponent()) {
             int currentHeight = worker.getCurPosition().getHeight();
             possibleCells.removeIf(movable -> currentHeight < movable.getHeight());
         }
         return possibleCells;
     }

}

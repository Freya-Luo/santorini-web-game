package edu.cmu.cs214.hw3.cards;

import edu.cmu.cs214.hw3.models.Game;
import edu.cmu.cs214.hw3.models.Cell;
import edu.cmu.cs214.hw3.models.Worker;

import java.util.List;

public class Minotaur extends God {

    private Cell pushOpponentTo(Cell workerPos, Cell pushTo, Game game) {
        // Apply Minotaur God Rule
        int[] relativePos = {workerPos.getX() - pushTo.getX(), workerPos.getY() - pushTo.getY()};
        int nextX = pushTo.getX() - relativePos[0];
        int nextY = pushTo.getY() - relativePos[1];
        return game.getBoard().getCell(nextX, nextY);
    }


    @Override
    public List<Cell> getMovableCells(Worker worker, Game game) {
        Cell workerPos = worker.getCurPosition();
        List<Cell> movableCells = super.getMovableCells(worker, game);

        for(Cell cell : game.getBoard().getNeighbors(workerPos)) {
            if (cell.isOccupied() && !cell.isCompleted() ) {
                if (game.getOpponentPlayer().getWorkerByPosition(cell) != null) {
                    Cell opponentNewPos = pushOpponentTo(workerPos, cell, game);
                    if (opponentNewPos != null && !opponentNewPos.isOccupied()) {
                        movableCells.add(cell);
                    }
                }
            }
        }
        return movableCells;
    }

    @Override
    public void doMove(Worker worker, Cell moveTo, Game game) {
        Worker opponentWorker = game.getOpponentPlayer().getWorkerByPosition(moveTo);
        if (opponentWorker != null) {
            Cell opponentNewPos = pushOpponentTo(worker.getCurPosition(), moveTo, game);
            opponentWorker.setCurPosition(opponentNewPos);
        }
        super.doMove(worker, moveTo, game);
    }

}

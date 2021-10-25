package edu.cmu.cs214.hw3.cards;

import edu.cmu.cs214.hw3.models.Game;
import edu.cmu.cs214.hw3.models.Cell;
import edu.cmu.cs214.hw3.models.Worker;

import java.util.List;

public class Minotaur extends God {


    private Cell pushOpponentTo(Cell workerPos, Cell pushTo, Game game) {
        // Apply Minotaur God Rule
        int[] relativePos = {workerPos.getX() - pushTo.getX(), workerPos.getY() - pushTo.getY()};
        int nextX = pushTo.getX() + relativePos[0];
        int nextY = pushTo.getY() + relativePos[1];
        return game.getBoard().getCell(nextX, nextY);
    }


    @Override
    public List<Cell> getMovableCells(Worker worker, Game game) {
        Cell workerPos = worker.getCurPosition();
        List<Cell> movableCells = super.getMovableCells(worker, game);

        for(Cell cell : movableCells) {
            if (cell.isOccupied() && !cell.isCompleted() ) {
                if (game.getNextPlayer().getWorkerByPosition(cell) != null) {
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
        super.doMove(worker, moveTo, game);
        Cell opponentNewPos = pushOpponentTo(worker.getCurPosition(), moveTo, game);
        game.getNextPlayer().getWorkerByPosition(moveTo).setCurPosition(opponentNewPos);
    }

}

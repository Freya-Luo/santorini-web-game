### Behavioral Contract

<table>
     <tr>
         <td><strong>Operation:</strong></td>
         <td>moveWorker(worker, board)</td>
     </tr>
     <tr >
         <td><strong>Preconditions:</strong></td>
         <td>
             <ul>
                <li>Game is running and winner does not determined yet. </li>
                <li>Worker's current space and all neighboring spaces checked cannot beyond the board border.</li>
                <li>At least 1 unoccupied neighboring space whose height is not higher 2 level than the current worker's height exists.</li>
              </ul>
         </td>
     </tr>
     <tr>
         <td><strong>Postconditions:</strong></td>
         <td>
            <ul>
                <li>Worker's current space is set to occupied and if he wins is checked.</li>
                <li>Worker's neighboring spaces are updated.</li>
                <li>Worker's old space is set to unoccupied.</li>
            </ul>
         </td>
     </tr>
</table>
document.getElementById('start-button').addEventListener('click', startGame);

function startGame() {
    const bingoBoard = document.getElementById('bingo-board');
    bingoBoard.innerHTML = '';
    createBingoBoard();
}

function createBingoBoard() {
    const bingoBoard = document.getElementById('bingo-board');
    const numbers = shuffle(Array.from({ length: 25 }, (_, i) => i + 1));

    for (let i = 0; i < 25; i++) {
        const cell = document.createElement('div');
        cell.className = 'bingo-cell';
        cell.textContent = numbers[i];
        cell.addEventListener('click', () => selectCell(cell));
        bingoBoard.appendChild(cell);
    }
}

function shuffle(array) {
    for (let i = array.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [array[i], array[j]] = [array[j], array[i]];
    }
    return array;
}

function selectCell(cell) {
    cell.classList.toggle('cell-selected');
}
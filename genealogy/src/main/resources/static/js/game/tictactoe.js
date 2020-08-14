/*
Game rules:
TODO
*/

var scores, activePlayer, gamePlaying;

scores = [0, 0];
init();

function haveWinner(playerMark) {
	if (document.getElementById('btn-cell-1-1').textContent === playerMark &&
		document.getElementById('btn-cell-2-1').textContent === playerMark &&
		document.getElementById('btn-cell-3-1').textContent === playerMark) {
		document.getElementById('btn-cell-1-1').style.color = '#555';
		document.getElementById('btn-cell-2-1').style.color = '#555';
		document.getElementById('btn-cell-3-1').style.color = '#555';
		return true;
	}
	
	if (document.getElementById('btn-cell-1-2').textContent === playerMark &&
		document.getElementById('btn-cell-2-2').textContent === playerMark &&
		document.getElementById('btn-cell-3-2').textContent === playerMark) {
		return true;
	}
	
	if (document.getElementById('btn-cell-1-3').textContent === playerMark &&
		document.getElementById('btn-cell-2-3').textContent === playerMark &&
		document.getElementById('btn-cell-3-3').textContent === playerMark) {
		return true;
	}
	
	if (document.getElementById('btn-cell-1-1').textContent === playerMark &&
		document.getElementById('btn-cell-1-2').textContent === playerMark &&
		document.getElementById('btn-cell-1-3').textContent === playerMark) {
		return true;
	}
	
	if (document.getElementById('btn-cell-2-1').textContent === playerMark &&
		document.getElementById('btn-cell-2-2').textContent === playerMark &&
		document.getElementById('btn-cell-2-3').textContent === playerMark) {
		return true;
	}

	if (document.getElementById('btn-cell-3-1').textContent === playerMark &&
		document.getElementById('btn-cell-3-2').textContent === playerMark &&
		document.getElementById('btn-cell-3-3').textContent === playerMark) {
		return true;
	}

	if (document.getElementById('btn-cell-1-1').textContent === playerMark &&
		document.getElementById('btn-cell-2-2').textContent === playerMark &&
		document.getElementById('btn-cell-3-3').textContent === playerMark) {
		return true;
	}

	if (document.getElementById('btn-cell-1-3').textContent === playerMark &&
		document.getElementById('btn-cell-2-2').textContent === playerMark &&
		document.getElementById('btn-cell-3-1').textContent === playerMark) {
		return true;
	}

	return false;
}

function cellSelected(row, column) {
	var playerMark = (activePlayer === 0 ? 'X' : 'O');

	var selectedCell = document.getElementById('btn-cell-' + row + '-' + column);
	
	if (selectedCell.textContent === ' ' && gamePlaying) {
		selectedCell.textContent = playerMark;
		
		if (haveWinner(playerMark)) {
			scores[activePlayer] = scores[activePlayer] + 1;
			document.querySelector('#score-' + activePlayer).textContent = scores[activePlayer];
			document.querySelector('#name-' + activePlayer).textContent = 'Winner!'; 
			gamePlaying = false;
		} else {
			//Next player
		    nextPlayer();		
		}
	} 
}

document.querySelector('#btn-cell-1-1').addEventListener('click', function() {
	cellSelected(1,1);
});

document.querySelector('#btn-cell-1-2').addEventListener('click', function() {
	cellSelected(1,2);
});

document.querySelector('#btn-cell-1-3').addEventListener('click', function() {
	cellSelected(1,3);
});

document.querySelector('#btn-cell-2-1').addEventListener('click', function() {
	cellSelected(2,1);
});

document.querySelector('#btn-cell-2-2').addEventListener('click', function() {
	cellSelected(2,2);
});

document.querySelector('#btn-cell-2-3').addEventListener('click', function() {
	cellSelected(2,3);
});

document.querySelector('#btn-cell-3-1').addEventListener('click', function() {
	cellSelected(3,1);
});

document.querySelector('#btn-cell-3-2').addEventListener('click', function() {
	cellSelected(3,2);
});

document.querySelector('#btn-cell-3-3').addEventListener('click', function() {
	cellSelected(3,3);
});

function nextPlayer() {
    //Next player
    activePlayer === 0 ? activePlayer = 1 : activePlayer = 0;

    document.querySelector('.player-0-panel').classList.toggle('active');
    document.querySelector('.player-1-panel').classList.toggle('active');
}

document.querySelector('.btn-new').addEventListener('click', init);

function init() {
    activePlayer = 0;
    gamePlaying = true;
  
    for (var i = 1; i <= 3; i++) {
    	for (var j = 1; j <= 3; j++) {
    		 document.getElementById('cell-' + i + '-' + j).style.display = 'block';
    		 document.getElementById('btn-cell-' + i + '-' + j).textContent = ' ';
    	}
    }

    document.getElementById('score-0').textContent = scores[0];
    document.getElementById('score-1').textContent = scores[1];
    document.getElementById('name-0').textContent = 'Player 1';
    document.getElementById('name-1').textContent = 'Player 2';
    document.querySelector('.player-0-panel').classList.remove('winner');
    document.querySelector('.player-1-panel').classList.remove('winner');
    document.querySelector('.player-0-panel').classList.remove('active');
    document.querySelector('.player-1-panel').classList.remove('active');
    document.querySelector('.player-0-panel').classList.add('active');
}

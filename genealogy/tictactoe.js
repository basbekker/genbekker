/******/ (function(modules) { // webpackBootstrap
/******/ 	// The module cache
/******/ 	var installedModules = {};
/******/
/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {
/******/
/******/ 		// Check if module is in cache
/******/ 		if(installedModules[moduleId]) {
/******/ 			return installedModules[moduleId].exports;
/******/ 		}
/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = installedModules[moduleId] = {
/******/ 			i: moduleId,
/******/ 			l: false,
/******/ 			exports: {}
/******/ 		};
/******/
/******/ 		// Execute the module function
/******/ 		modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);
/******/
/******/ 		// Flag the module as loaded
/******/ 		module.l = true;
/******/
/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}
/******/
/******/
/******/ 	// expose the modules object (__webpack_modules__)
/******/ 	__webpack_require__.m = modules;
/******/
/******/ 	// expose the module cache
/******/ 	__webpack_require__.c = installedModules;
/******/
/******/ 	// define getter function for harmony exports
/******/ 	__webpack_require__.d = function(exports, name, getter) {
/******/ 		if(!__webpack_require__.o(exports, name)) {
/******/ 			Object.defineProperty(exports, name, { enumerable: true, get: getter });
/******/ 		}
/******/ 	};
/******/
/******/ 	// define __esModule on exports
/******/ 	__webpack_require__.r = function(exports) {
/******/ 		if(typeof Symbol !== 'undefined' && Symbol.toStringTag) {
/******/ 			Object.defineProperty(exports, Symbol.toStringTag, { value: 'Module' });
/******/ 		}
/******/ 		Object.defineProperty(exports, '__esModule', { value: true });
/******/ 	};
/******/
/******/ 	// create a fake namespace object
/******/ 	// mode & 1: value is a module id, require it
/******/ 	// mode & 2: merge all properties of value into the ns
/******/ 	// mode & 4: return value when already ns object
/******/ 	// mode & 8|1: behave like require
/******/ 	__webpack_require__.t = function(value, mode) {
/******/ 		if(mode & 1) value = __webpack_require__(value);
/******/ 		if(mode & 8) return value;
/******/ 		if((mode & 4) && typeof value === 'object' && value && value.__esModule) return value;
/******/ 		var ns = Object.create(null);
/******/ 		__webpack_require__.r(ns);
/******/ 		Object.defineProperty(ns, 'default', { enumerable: true, value: value });
/******/ 		if(mode & 2 && typeof value != 'string') for(var key in value) __webpack_require__.d(ns, key, function(key) { return value[key]; }.bind(null, key));
/******/ 		return ns;
/******/ 	};
/******/
/******/ 	// getDefaultExport function for compatibility with non-harmony modules
/******/ 	__webpack_require__.n = function(module) {
/******/ 		var getter = module && module.__esModule ?
/******/ 			function getDefault() { return module['default']; } :
/******/ 			function getModuleExports() { return module; };
/******/ 		__webpack_require__.d(getter, 'a', getter);
/******/ 		return getter;
/******/ 	};
/******/
/******/ 	// Object.prototype.hasOwnProperty.call
/******/ 	__webpack_require__.o = function(object, property) { return Object.prototype.hasOwnProperty.call(object, property); };
/******/
/******/ 	// __webpack_public_path__
/******/ 	__webpack_require__.p = "";
/******/
/******/
/******/ 	// Load entry module and return exports
/******/ 	return __webpack_require__(__webpack_require__.s = "./src/main/resources/public/js/game/tictactoe.js");
/******/ })
/************************************************************************/
/******/ ({

/***/ "./src/main/resources/public/js/game/tictactoe.js":
/*!********************************************************!*\
  !*** ./src/main/resources/public/js/game/tictactoe.js ***!
  \********************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

/*
Game rules:
TODO
*/
var scores, activePlayer, gamePlaying;
scores = [0, 0];
init();

function haveWinner(playerMark) {
  if (document.getElementById('btn-cell-1-1').textContent === playerMark && document.getElementById('btn-cell-2-1').textContent === playerMark && document.getElementById('btn-cell-3-1').textContent === playerMark) {
    document.getElementById('btn-cell-1-1').style.color = '#555';
    document.getElementById('btn-cell-2-1').style.color = '#555';
    document.getElementById('btn-cell-3-1').style.color = '#555';
    return true;
  }

  if (document.getElementById('btn-cell-1-2').textContent === playerMark && document.getElementById('btn-cell-2-2').textContent === playerMark && document.getElementById('btn-cell-3-2').textContent === playerMark) {
    return true;
  }

  if (document.getElementById('btn-cell-1-3').textContent === playerMark && document.getElementById('btn-cell-2-3').textContent === playerMark && document.getElementById('btn-cell-3-3').textContent === playerMark) {
    return true;
  }

  if (document.getElementById('btn-cell-1-1').textContent === playerMark && document.getElementById('btn-cell-1-2').textContent === playerMark && document.getElementById('btn-cell-1-3').textContent === playerMark) {
    return true;
  }

  if (document.getElementById('btn-cell-2-1').textContent === playerMark && document.getElementById('btn-cell-2-2').textContent === playerMark && document.getElementById('btn-cell-2-3').textContent === playerMark) {
    return true;
  }

  if (document.getElementById('btn-cell-3-1').textContent === playerMark && document.getElementById('btn-cell-3-2').textContent === playerMark && document.getElementById('btn-cell-3-3').textContent === playerMark) {
    return true;
  }

  if (document.getElementById('btn-cell-1-1').textContent === playerMark && document.getElementById('btn-cell-2-2').textContent === playerMark && document.getElementById('btn-cell-3-3').textContent === playerMark) {
    return true;
  }

  if (document.getElementById('btn-cell-1-3').textContent === playerMark && document.getElementById('btn-cell-2-2').textContent === playerMark && document.getElementById('btn-cell-3-1').textContent === playerMark) {
    return true;
  }

  return false;
}

function cellSelected(row, column) {
  var playerMark = activePlayer === 0 ? 'X' : 'O';
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

document.querySelector('#btn-cell-1-1').addEventListener('click', function () {
  cellSelected(1, 1);
});
document.querySelector('#btn-cell-1-2').addEventListener('click', function () {
  cellSelected(1, 2);
});
document.querySelector('#btn-cell-1-3').addEventListener('click', function () {
  cellSelected(1, 3);
});
document.querySelector('#btn-cell-2-1').addEventListener('click', function () {
  cellSelected(2, 1);
});
document.querySelector('#btn-cell-2-2').addEventListener('click', function () {
  cellSelected(2, 2);
});
document.querySelector('#btn-cell-2-3').addEventListener('click', function () {
  cellSelected(2, 3);
});
document.querySelector('#btn-cell-3-1').addEventListener('click', function () {
  cellSelected(3, 1);
});
document.querySelector('#btn-cell-3-2').addEventListener('click', function () {
  cellSelected(3, 2);
});
document.querySelector('#btn-cell-3-3').addEventListener('click', function () {
  cellSelected(3, 3);
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

/***/ })

/******/ });
//# sourceMappingURL=tictactoe.js.map
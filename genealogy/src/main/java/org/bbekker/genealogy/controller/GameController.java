package org.bbekker.genealogy.controller;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/app/game")
public class GameController {

	@Autowired
	MessageSource messageSource;

	@RequestMapping(path = "/dice", method = RequestMethod.GET)
	public String runDiceGame(
			Locale locale,
			Model model) {

		return "game/dice";
	}

	@RequestMapping(path = "/tictactoe", method = RequestMethod.GET)
	public String runTicTacToeGame(
			Locale locale,
			Model model) {

		return "game/tictactoe";
	}

}

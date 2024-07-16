package com.example.kartiksapra.watchlist.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.example.kartiksapra.watchlist.entity.Movie;
import com.example.kartiksapra.watchlist.service.DatabaseService;

@RestController
public class MovieController {

	@Autowired
	DatabaseService databaseService;
	@GetMapping("/watchlistItemForm")
	public ModelAndView showWatchListForm(@RequestParam(required = false) Integer id) {
		
		System.out.println(id);
		String viewName = "watchlistItemForm";
		
		Map<String, Object> model = new HashMap<>();
		
		if(id == null) {
			model.put("watchlistItem", new Movie());
		} else {
			model.put("watchlistItem", databaseService.getMovieById(id));
		}

		return new ModelAndView(viewName, model);
	}
	
	@PostMapping("/watchlistItemForm")
	public ModelAndView submitWatchListForm(@Valid @ModelAttribute("watchlistItem") Movie movie, BindingResult bindingResult) {
		
		if(bindingResult.hasErrors()) {
			System.out.println(bindingResult.hasErrors());
			// if errors are there, redisplay the form and let user enter again"
			return new ModelAndView("watchlistItemForm");
		}
		/*
		 if(id == null) {
		 create new movie
		 } else {
		 update 
		 }
		 */
		
		Integer id = movie.getId();
		if(id == null) {
			databaseService.create(movie);
		} else {
			databaseService.update(movie, id);
		}
		
		RedirectView rd = new RedirectView();
		rd.setUrl("/watchlist");
		
		return new ModelAndView(rd);
	}
	
	@GetMapping("/watchlist")
	public ModelAndView getWatchlist() {

		String viewName = "watchlist";
		Map<String, Object> model = new HashMap<>();
		List<Movie> movieList = databaseService.getAllMovies();
		model.put("watchlistrows", movieList);
		model.put("noofmovies", movieList.size());
		return new ModelAndView(viewName, model);
	}
}

package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Race;
import com.game.service.PlayerService;

import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest")
public class RestApiController {

    private final PlayerService playerService;


    public RestApiController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/players")
    public List<Player> getPlayersList(@RequestParam(defaultValue = "%",required = false) String name,
                                       @RequestParam(defaultValue = "%",required = false) String title,
                                       @RequestParam(defaultValue = "0",required = false) Integer minLevel,
                                       @RequestParam(defaultValue = "10000",required = false) Integer maxLevel,
                                       @RequestParam(defaultValue = "0") Integer pageNumber,
                                       @RequestParam(defaultValue = "3") Integer pageSize,
                                       @RequestParam(required = false) Race race,
                                       @RequestParam(defaultValue = "0",required = false) Integer minExperience,
                                       @RequestParam(defaultValue = "2000000000",required = false) Integer maxExperience,
                                       @RequestParam(defaultValue = "false",required = false) Boolean banned
                                       ) {

        return playerService.getPlayersList(name,title,minLevel,maxLevel,
                pageNumber,pageSize,race,minExperience,maxExperience,banned);
    }
    @GetMapping("/players/count")
    public Integer getPlayersCount(@RequestParam(defaultValue = "%",required = false) String name,
                                   @RequestParam(defaultValue = "%",required = false) String title,
                                   @RequestParam(defaultValue = "0",required = false) Integer minLevel,
                                   @RequestParam(defaultValue = "10000",required = false) Integer maxLevel,
                                   @RequestParam(defaultValue = "0") Integer pageNumber,
                                   @RequestParam(defaultValue = "3") Integer pageSize,
                                   @RequestParam(required = false) Race race,
                                   @RequestParam(defaultValue = "0",required = false) Integer minExperience,
                                   @RequestParam(defaultValue = "2000000000",required = false) Integer maxExperience,
                                   @RequestParam(required = false) Boolean banned) {

        return playerService.getPlayersCount(name,title,minLevel,maxLevel,
                pageNumber,pageSize,race,minExperience,maxExperience,banned);
    }
    @GetMapping("/players/{id}")
    public Player getPlayerById(@PathVariable Long id) {

       return playerService.getPlayerById(id);
    }
}

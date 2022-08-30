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
                                       @RequestParam(defaultValue = "40") Integer pageSize) {

        return playerService.getPlayersList(name,title,minLevel,maxLevel,pageNumber,pageSize);
    }
    @GetMapping("/players/count")
    public Integer getPlayersCount() {
        return playerService.getPlayersCount();
    }
}

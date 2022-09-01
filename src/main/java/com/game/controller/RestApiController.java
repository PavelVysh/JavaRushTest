package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
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
                                       @RequestParam(required = false) Boolean banned,
                                       @RequestParam(required = false) Profession profession,
                                       @RequestParam(required = false) Long after,
                                       @RequestParam(required = false) Long before,
                                       @RequestParam(required = false) PlayerOrder order
                                       ) {

        return playerService.getPlayersList(name,title,minLevel,maxLevel,
                pageNumber,pageSize,race,minExperience,maxExperience,banned,profession,after,before,order);
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
                                   @RequestParam(defaultValue = "10000000",required = false) Integer maxExperience,
                                   @RequestParam(required = false) Boolean banned,
                                   @RequestParam(required = false) Profession profession,
                                   @RequestParam(required = false) Long after,
                                   @RequestParam(required = false) Long before,
                                   @RequestParam(required = false) PlayerOrder order) {

        return playerService.getPlayersCount(name,title,minLevel,maxLevel,
                pageNumber,pageSize,race,minExperience,maxExperience,banned,profession,after,before,order);
    }
    @GetMapping("/players/{id}")
    public Player getPlayerById(@PathVariable Long id) {

       return playerService.getPlayerById(id);
    }
    @PostMapping("/players")
    public Player createPlayer(@RequestBody Player player) {
        return playerService.createPlayer(player);
    }
    @DeleteMapping("/players/{id}")
    public void deletePlayer(@PathVariable Long id) {
        playerService.deletePlayer(id);
    }
    @PostMapping("/players/{id}")
    public Player updatePlayer(@PathVariable Long id,@RequestBody Player player) {


        return playerService.updatePlayer(id,player);

    }
}

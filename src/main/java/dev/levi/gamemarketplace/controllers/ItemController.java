package dev.levi.gamemarketplace.controllers;

import dev.levi.gamemarketplace.dtos.responses.ItemResponse;
import dev.levi.gamemarketplace.entities.Player;
import dev.levi.gamemarketplace.mappers.ItemMapper;
import dev.levi.gamemarketplace.services.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ItemMapper itemMapper;

    @GetMapping
    public ResponseEntity<List<ItemResponse>> getItems(
            @AuthenticationPrincipal Player authenticatedPlayer
    ) {
        var response = itemService.findByOwnerId(authenticatedPlayer)
                .stream().map(itemMapper::toResponse).toList();
        return ResponseEntity.ok(response);
    }
}

package dev.levi.gamemarketplace.mappers;

import dev.levi.gamemarketplace.dtos.responses.PlayerResponse;
import dev.levi.gamemarketplace.entities.Player;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlayerMapper {
    PlayerResponse toResponse(Player player);
}
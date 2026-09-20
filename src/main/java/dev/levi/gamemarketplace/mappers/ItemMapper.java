package dev.levi.gamemarketplace.mappers;

import dev.levi.gamemarketplace.dtos.responses.ItemResponse;
import dev.levi.gamemarketplace.entities.Item;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    ItemResponse toResponse(Item item);
}
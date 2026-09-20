package dev.levi.gamemarketplace.mappers;

import dev.levi.gamemarketplace.dtos.responses.RewardClaimResponse;
import dev.levi.gamemarketplace.entities.RewardClaim;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RewardClaimMapper {

    @Mapping(target = "itemId", source = "item.id")
    @Mapping(target = "itemType", source = "item.type")
    @Mapping(target = "itemRarity", source = "item.rarity")
    RewardClaimResponse toResponse(RewardClaim claim);
}

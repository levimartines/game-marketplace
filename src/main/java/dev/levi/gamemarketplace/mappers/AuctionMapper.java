package dev.levi.gamemarketplace.mappers;

import dev.levi.gamemarketplace.dtos.responses.AuctionResponse;
import dev.levi.gamemarketplace.dtos.responses.BidResponse;
import dev.levi.gamemarketplace.entities.Auction;
import dev.levi.gamemarketplace.entities.Bid;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuctionMapper {

    @Mapping(target = "itemId", source = "item.id")
    @Mapping(target = "itemType", source = "item.type")
    @Mapping(target = "sellerId", source = "seller.id")
    @Mapping(target = "sellerUsername", source = "seller.username")
    AuctionResponse toAuctionResponse(Auction auction);

    @Mapping(target = "auctionId", source = "auction.id")
    @Mapping(target = "bidderId", source = "bidder.id")
    @Mapping(target = "bidderUsername", source = "bidder.username")
    BidResponse toBidResponse(Bid bid);
}
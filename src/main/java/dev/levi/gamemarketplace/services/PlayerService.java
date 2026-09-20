package dev.levi.gamemarketplace.services;


import dev.levi.gamemarketplace.dtos.requests.RegisterPlayerRequest;
import dev.levi.gamemarketplace.entities.Player;
import dev.levi.gamemarketplace.error.exceptions.PlayerAlreadyExistsException;
import dev.levi.gamemarketplace.repositories.PlayerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Player register(RegisterPlayerRequest request) {
        if (playerRepository.findByUsername(request.username()).isPresent()) {
            throw new PlayerAlreadyExistsException("username", request.username());
        }
        if (playerRepository.findByEmail(request.email()).isPresent()) {
            throw new PlayerAlreadyExistsException("email", request.email());
        }

        Player player = new Player();
        player.setUsername(request.username());
        player.setEmail(request.email());
        player.setPasswordHash(passwordEncoder.encode(request.password()));
        player.setGoldBalance(0L);

        return playerRepository.save(player);
    }
}

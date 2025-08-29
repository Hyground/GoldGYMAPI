package com.goldgym.api.security;

import com.goldgym.api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class UsuarioDetailsService implements UserDetailsService {
  private final UsuarioRepository repo;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return repo.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("No existe: " + username));
  }
}
